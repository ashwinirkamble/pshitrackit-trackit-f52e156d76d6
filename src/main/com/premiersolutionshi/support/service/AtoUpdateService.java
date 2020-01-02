package com.premiersolutionshi.support.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.premiersolutionshi.common.domain.User;
import com.premiersolutionshi.common.service.ModifiedService;
import com.premiersolutionshi.common.service.UserService;
import com.premiersolutionshi.common.util.DateUtils;
import com.premiersolutionshi.support.constant.IssueStatus;
import com.premiersolutionshi.support.dao.AtoUpdateDao;
import com.premiersolutionshi.support.domain.AtoUpdate;
import com.premiersolutionshi.support.domain.ConfiguredSystem;
import com.premiersolutionshi.support.domain.Issue;
import com.premiersolutionshi.support.domain.IssueCategory;
import com.premiersolutionshi.support.domain.Laptop;
import com.premiersolutionshi.support.domain.Ship;
import com.premiersolutionshi.support.ui.form.AtoUpdateForm;

@Component("atoUpdateService")
public class AtoUpdateService extends ModifiedService<AtoUpdate> {
    private static final String ATO_UPDATE_DESCRIPTION = "Auto-generated by the ATO Update tool";
    private static final String ATO_UPDATE_BASE_TITLE = "Monthly ATO Maintenance Release - ATOUpdates_";
    private static final String DEFAULT_ISSUE_CATEGORY_NAME = "ATO Maintenance Release";
    private static final String DEFAULT_ISSUE_DEPT = "N/A";
    private static final String DEFAULT_SHIPS_SUPPORT = "Ships Support";
    private static final String DEFAULT_INITIATED_BY = "PSHI";
    private static final String DEFAULT_PERSON_ASSIGNED = "Other";

    public AtoUpdateService(SqlSession sqlSession, UserService userService) {
        super(sqlSession, AtoUpdateDao.class, userService);
        setLogger(Logger.getLogger(this.getClass().getSimpleName()));
    }

    @Override
    protected AtoUpdateDao getDao() {
        return (AtoUpdateDao) super.getDao();
    }

    /**
     * See SupportModel.insertAto(Connection conn, SupportBean supportBean, LoginBean loginBean)
     * @param atoUpdateForm
     * @param configuredSystemService
     * @param issueService
     * @param issueCategoryService
     * @param issueCommentsService
     * @return The Saved ATO Update Form
     */
    public AtoUpdate saveForm(AtoUpdateForm atoUpdateForm, ConfiguredSystemService configuredSystemService, IssueService issueService,
            IssueCategoryService issueCategoryService, IssueCommentsService issueCommentsService) {
        Integer projectPk = atoUpdateForm.getProjectFk();
        if (projectPk == null) {
            logError("projectPk is NULL cannot save.");
            return null;
        }
        if (save(atoUpdateForm)) {
            String[] includeCsPkArr = atoUpdateForm.getIncludeConfiguredSystemPkArr();
            String[] reminderIssuePkArr = atoUpdateForm.getReminderIssuePkArr();
            String[] appliedIssuePkArr = atoUpdateForm.getAppliedIssuePkArr();
            String[] removeIssuePkArr = atoUpdateForm.getRemoveIssuePkArr();

            handleBulkInsertOfIssues(atoUpdateForm, includeCsPkArr, configuredSystemService, issueService, issueCategoryService,
                    issueCommentsService);
            issueCommentsService.bulkAddComment(reminderIssuePkArr, "Reminder e-mail sent via ATO Update module.");

            if (appliedIssuePkArr != null && appliedIssuePkArr.length > 0) {
                String personAssigned = getCurrentUser().getFullName();
                issueService.handleBulkCloseIssues(appliedIssuePkArr, "Closed by ATO Update module", personAssigned);
            }

            issueService.handleBulkDeleteIssues(removeIssuePkArr);

            return atoUpdateForm;
        }
        return null;
    }

    private void handleBulkInsertOfIssues(AtoUpdate atoUpdate, String[] includeConfiguredSystemPkArr,
        ConfiguredSystemService configuredSystemService, IssueService issueService, IssueCategoryService issueCategoryService,
        IssueCommentsService issueCommentsService) {
        if (includeConfiguredSystemPkArr == null || includeConfiguredSystemPkArr.length == 0) {
            return;
        }
        Integer atoId = atoUpdate.getId();
        LocalDate atoDate = atoUpdate.getAtoDate();
        LocalDate openedDate = atoUpdate.getOpenedDate();
        Integer projectPk = atoUpdate.getProjectFk();
        String atoComments = atoUpdate.getComments();

        String categoryName = DEFAULT_ISSUE_CATEGORY_NAME;
        IssueCategory issueCategory = issueCategoryService.getByName(categoryName);
        // we can't continue if the issue category doesn't exist.
        if (issueCategory == null) {
            logError("Could not find Issue Category '" + categoryName + "' in the database. Cannot generate issues.");
            return;
        }
        Integer issueCategoryFk = issueCategory.getId();
        String atoDateStr = DateUtils.formatToPattern(atoDate, "yyyyMMdd");
        String baseTitle = ATO_UPDATE_BASE_TITLE + atoDateStr;
        String description = ATO_UPDATE_DESCRIPTION;
        String personAssigned = DEFAULT_PERSON_ASSIGNED;
        String phase = DEFAULT_SHIPS_SUPPORT;
        User currentUser = getUserService().getCurrentUser();
        String currentUserFullName = currentUser == null ? null : currentUser.getFullName();
        List<String> configuredSystemPkList = Arrays.asList(includeConfiguredSystemPkArr);
        List<ConfiguredSystem> configuredSystems = configuredSystemService.getByPkList(configuredSystemPkList);

        ArrayList<Issue> issueList = new ArrayList<>();
        for (ConfiguredSystem configuredSystem : configuredSystems) {
            Ship ship = configuredSystem.getShip();
            Laptop laptop = configuredSystem.getLaptop();
            String computerName = laptop == null ? "" : " - " + laptop.getComputerName();
            String title = baseTitle + computerName;
            Integer shipId = ship == null ? null : ship.getId();
            Integer configuredSystemFk = configuredSystem.getId();

            Issue issue = new Issue();
            issue.setTitle(title);
            issue.setCreatedDate(LocalDateTime.now());
            issue.setOpenedDate(openedDate);
            issue.setIssueCategoryFk(issueCategoryFk);
            issue.setStatus(IssueStatus.ACTIVE.getName());
            issue.setPhase(phase);
            issue.setDept(DEFAULT_ISSUE_DEPT);
            issue.setDescription(description);
            issue.setOpenedBy(currentUserFullName);
            issue.setCreatedBy(currentUserFullName);
            issue.setPersonAssigned(personAssigned);
            issue.setAtoFk(atoId);
            issue.setProjectFk(projectPk);
            issue.setConfiguredSystemFk(configuredSystemFk);
            issue.setShipFk(shipId);
            issue.setIsEmailSent("Y");
            issue.setInitiatedBy(DEFAULT_INITIATED_BY);
            issue.setOpenedDate(openedDate);
            issueList.add(issue);
        }
        int rowsInserted = issueService.batchInsert(issueList);
        if (rowsInserted > 0) {
            // re-retrieve the issues so that we have their issuePk values.
            ArrayList<Issue> atoIssues = issueService.getByAtoFk(atoId);
            if (atoIssues != null && !atoIssues.isEmpty()) {
                //issueCommentsService.bulkAddComment(atoIssues, "Support issue created");
                issueCommentsService.bulkAddComment(atoIssues, atoComments);
            }
        }
    }
}
