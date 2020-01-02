package com.premiersolutionshi.support.ui.action;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.struts.action.ActionForm;

import com.premiersolutionshi.common.constant.CommonMessage;
import com.premiersolutionshi.common.constant.ManagedList;
import com.premiersolutionshi.common.domain.ManagedListItem;
import com.premiersolutionshi.common.domain.User;
import com.premiersolutionshi.common.service.FileInfoService;
import com.premiersolutionshi.common.service.UserService;
import com.premiersolutionshi.common.ui.action.BaseAction;
import com.premiersolutionshi.support.constant.BulkIssueCategory;
import com.premiersolutionshi.support.domain.ConfiguredSystemWithIssues;
import com.premiersolutionshi.support.domain.Issue;
import com.premiersolutionshi.support.service.ConfiguredSystemService;
import com.premiersolutionshi.support.service.IssueCategoryService;
import com.premiersolutionshi.support.service.IssueCommentsService;
import com.premiersolutionshi.support.service.IssueService;
import com.premiersolutionshi.support.service.PocService;
import com.premiersolutionshi.support.service.TransmittalService;
import com.premiersolutionshi.support.ui.form.BulkIssueForm;

public class BulkIssueAction extends BaseAction {
    private FileInfoService fileInfoService;
    private IssueService issueService;
    private IssueCategoryService issueCategoryService;
    private IssueCommentsService issueCommentsService;
    private ConfiguredSystemService configuredSystemService;
    private TransmittalService transmittalService;
    private PocService pocService;

    private String currFacetVersion;
    private String currOsVersion;

    public BulkIssueAction() {
        super(BulkIssueAction.class);
    }

    @Override
    protected String run(Connection conn, HttpServletRequest request, HttpServletResponse response, ActionForm form) throws IOException {
        BulkIssueForm bulkIssueForm = (BulkIssueForm) form;
        if (bulkIssueForm != null) {
            setAction(bulkIssueForm.getAction());
        }
        String action = getAction();
        switch (action) {
        case "preview":
            action = handlePreview(request, response, form);
            break;
        case "submit":
            action = handleSubmit(request, response, form);
            break;
        default:
            handleView(request, conn, transmittalService);
        }
        return action;
    }

    private String handlePreview(HttpServletRequest request, HttpServletResponse response, ActionForm form) {
        BulkIssueForm bulkIssueForm = (BulkIssueForm) form;
        ArrayList<Issue> generatedIssueList = null;
        ArrayList<Issue> openMonthlyIssues = null;
        int categoryCode = bulkIssueForm.getCategory().getCode();
        if (categoryCode != BulkIssueCategory.UNKNOWN.getCode()) {
            ArrayList<ConfiguredSystemWithIssues> fullList = configuredSystemService.getAllNotRetiredWithIssues(transmittalService,
                    issueService, currFacetVersion, currOsVersion, pocService);
            ArrayList<ConfiguredSystemWithIssues> configuredSystemList = new ArrayList<>();
            String[] includeConfiguredSystemPkArr = bulkIssueForm.getIncludeConfiguredSystemPkArr();
            if (includeConfiguredSystemPkArr != null && includeConfiguredSystemPkArr.length > 0) {
                List<String> includeCsPkList = Arrays.asList(includeConfiguredSystemPkArr);
                Set<String> includedCsPk = new HashSet<>(includeCsPkList);
                for (ConfiguredSystemWithIssues csWithIssues : fullList) {
                    String id = csWithIssues.getId() + "";
                    if (includedCsPk.contains(id)) {
                        configuredSystemList.add(csWithIssues);
                    }
                }
                boolean isMonthlyEmail = categoryCode == BulkIssueCategory.MONTHLY_EMAIL.getCode();
                generatedIssueList = issueService.generateIssuesByConfiguredSystemList(issueCategoryService, bulkIssueForm,
                        configuredSystemList, isMonthlyEmail);
                if (isMonthlyEmail) {
                    openMonthlyIssues = issueService.getOpenMonthlyIssues();
                }
            }
        }
        else {
            getLogger().error("BulkIssueAction | handlePreview | Bulk Issue Category is not recognized.");
        }
        request.setAttribute("projectPk", bulkIssueForm.getProjectFk() + "");
        request.setAttribute("generatedIssueList", generatedIssueList);
        request.setAttribute("openMonthlyIssues", openMonthlyIssues);
        request.setAttribute("bulkIssueForm", bulkIssueForm);
        return "preview";
    }

    private String handleSubmit(HttpServletRequest request, HttpServletResponse response, ActionForm form) throws IOException {
        BulkIssueForm bulkIssueForm = (BulkIssueForm) form;
        String[] includeConfiguredSystemPkArr = bulkIssueForm.getIncludeConfiguredSystemPkArr();
        Integer projectFk = bulkIssueForm.getProjectFk();
        if (includeConfiguredSystemPkArr != null && includeConfiguredSystemPkArr.length > 0) {
            int issuesCreatedCount = issueService.submitBulkIssueForm(bulkIssueForm, issueCategoryService, issueCommentsService,
                    transmittalService, currFacetVersion, currOsVersion);
            response.sendRedirect(path() + "?projectPk=" + projectFk + "&msg=" + CommonMessage.SAVE_SUCCESS.getKey() + "&additional="
                    + issuesCreatedCount + " issues");
        }
        else {
            response.sendRedirect(path() + "?projectPk=" + projectFk + "&msg=" + CommonMessage.NONE_SELECTED.getKey());
        }
        return FORWARD_VIEW;
    }

    private void handleView(HttpServletRequest request, Connection conn, TransmittalService transmittalService) {
        Integer projectFk = getProjectPk();
        String currentUserFullName = getCurrentUserFullName();

        ArrayList<ConfiguredSystemWithIssues> configuredSystemList = configuredSystemService.getAllNotRetiredWithIssues(transmittalService,
                issueService, currFacetVersion, currOsVersion, pocService);
        LocalDate today = LocalDate.now();
        ArrayList<ManagedListItem> supportTeamList = getManagedListItemService().getByListAndProjectFk(ManagedList.SUPPORT_TEAM, projectFk);

        BulkIssueForm bulkIssueForm = new BulkIssueForm();
        bulkIssueForm.setOpenedBy(currentUserFullName);
        bulkIssueForm.setPersonAssigned(ManagedList.getCurrentDefault(supportTeamList));
        bulkIssueForm.setOpenedDate(today);

        request.setAttribute("projectPk", projectFk + "");
        request.setAttribute("configuredSystemList", configuredSystemList);
        request.setAttribute("bulkIssueForm", bulkIssueForm);
        request.setAttribute("supportTeamList", supportTeamList);
    }

    private String getCurrentUserFullName() {
        UserService userService = getUserService();
        if (userService != null) {
            User currentUser = userService.getCurrentUser();
            if (currentUser != null) {
                return currentUser.getFullName();
            }
        }
        return "Not set";
    }

    protected void initializeServices(HttpServletRequest request, SqlSession sqlSession) {
        super.initializeServices(request, sqlSession);
        UserService userService = getUserService();
        fileInfoService = new FileInfoService(sqlSession, userService);
        pocService = new PocService(sqlSession, userService);
        configuredSystemService = new ConfiguredSystemService(sqlSession);
        issueCommentsService = new IssueCommentsService(sqlSession, userService);
        issueCategoryService = new IssueCategoryService(sqlSession, userService);
        issueService = new IssueService(sqlSession, userService, issueCommentsService, issueCategoryService, configuredSystemService, fileInfoService,
            getManagedListItemService(), pocService);
        transmittalService = new TransmittalService(sqlSession);
    }

    @Override
    public String path() {
        return "bulkIssue.do";
    }

}
