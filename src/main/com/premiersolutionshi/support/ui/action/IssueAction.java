package com.premiersolutionshi.support.ui.action;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.mail.EmailException;
import org.apache.ibatis.session.SqlSession;
import org.apache.struts.action.ActionForm;

import com.premiersolutionshi.common.constant.CommonMessage;
import com.premiersolutionshi.common.constant.ManagedList;
import com.premiersolutionshi.common.domain.BaseDomain;
import com.premiersolutionshi.common.domain.ManagedListItem;
import com.premiersolutionshi.common.domain.User;
import com.premiersolutionshi.common.service.FileInfoService;
import com.premiersolutionshi.common.service.ManagedListItemService;
import com.premiersolutionshi.common.service.UserService;
import com.premiersolutionshi.common.ui.action.BaseAction;
import com.premiersolutionshi.common.ui.form.ValueAndLabel;
import com.premiersolutionshi.common.util.StringUtils;
import com.premiersolutionshi.old.bean.SupportBean;
import com.premiersolutionshi.old.model.EmailModel;
import com.premiersolutionshi.support.constant.IssuePhase;
import com.premiersolutionshi.support.constant.IssueStatus;
import com.premiersolutionshi.support.domain.ConfiguredSystem;
import com.premiersolutionshi.support.domain.Issue;
import com.premiersolutionshi.support.domain.IssueCategory;
import com.premiersolutionshi.support.domain.IssueComments;
import com.premiersolutionshi.support.service.ConfiguredSystemService;
import com.premiersolutionshi.support.service.IssueCategoryService;
import com.premiersolutionshi.support.service.IssueCommentsService;
import com.premiersolutionshi.support.service.IssueService;
import com.premiersolutionshi.support.service.PocService;
import com.premiersolutionshi.support.service.ShipService;
import com.premiersolutionshi.support.ui.form.IssueForm;

public class IssueAction extends BaseAction {
    private FileInfoService fileInfoService;
    private IssueService issueService;
    private IssueCommentsService issueCommentsService;
    private IssueCategoryService issueCategoryService;
    private ConfiguredSystemService configuredSystemService;
    private ShipService shipService;
    private PocService pocService;

    public IssueAction() {
        super(IssueAction.class);
    }

    @Override
    protected void initializeServices(HttpServletRequest request, SqlSession sqlSession) {
        super.initializeServices(request, sqlSession);
        UserService userService = getUserService();
        configuredSystemService = new ConfiguredSystemService(sqlSession);
        fileInfoService = new FileInfoService(sqlSession, userService);
        pocService = new PocService(sqlSession, userService);
        issueCommentsService = new IssueCommentsService(sqlSession, userService);
        issueCategoryService = new IssueCategoryService(sqlSession, userService);
        issueService = new IssueService(sqlSession, userService, issueCommentsService, issueCategoryService, configuredSystemService,
                fileInfoService, getManagedListItemService(), pocService);
        shipService = new ShipService(sqlSession);
    }

    @Override
    protected String run(Connection conn, HttpServletRequest request, HttpServletResponse response, ActionForm form) throws IOException {
        String action = getAction();
        Integer id = StringUtils.parseInt(request.getParameter("id"));
        if (id != null) {
            ((BaseDomain) form).setId(id);
        }
        if (getService() != null) {
            getService().autoCloseIssues();
        }
        if (action != null) {
            if (action.equals(FORWARD_VIEW) || action.equals(FORWARD_EDIT)) {
                return handleView(request, response, action, id);
            }
            else if (action.equals(FORWARD_SAVE)) {
                return handleSave(conn, request, response, form, action);
            }
        }
        return handleView(request, response, action, id);
    }

    private void sendIssueEmail(Connection conn, IssueForm issueForm, SupportBean originalBean, boolean isNew) {
        SupportBean inputBean = issueForm.getBean();
        try {
            EmailModel emailModel = new EmailModel();
            if (isNew) {
                emailModel.sendNewIssueEmail(conn, inputBean, issueForm.getId(), getLoginBean());
            }
            else {
                emailModel.sendEditIssueEmail(conn, inputBean, originalBean, getLoginBean());
            }
        }
        catch (EmailException e) {
            logError("Could not handle sending an Issue email.", e);
        }
    }

    protected String handleSave(Connection conn, HttpServletRequest request, HttpServletResponse response, ActionForm form, String action)
        throws IOException {
        IssueForm issueForm = (IssueForm) form;
        Integer id = issueForm.getId();
        boolean isNew = id == null || id <= 0;
        SupportBean originalBean = null;
        if (!isNew) {
            Issue origIssue = getService().getById(id);
            if (origIssue != null) {
                originalBean = origIssue.getBean();
            }
        }
        if (isValidIssueForm(issueForm, isNew, response)) {
            ConfiguredSystem configuredSystem = configuredSystemService.getById(issueForm.getConfiguredSystemFk());
            if (issueForm.getIssueCategory() == null) {
                IssueCategory issueCategory = issueCategoryService.getById(issueForm.getIssueCategoryFk());
                issueForm.setIssueCategory(issueCategory);
            }
            issueForm.setConfiguredSystem(configuredSystem);
            setProjectPk(issueForm.getProjectFk());
            boolean success = getService().saveForm(issueForm);
            Integer newId = issueForm.getId();
            if (isNew && !success) {
                redirectWithMessage(response, FORWARD_ADD, CommonMessage.SAVE_FAILED, newId, null, null);
            }
            else {
                redirectWithMessage(response, FORWARD_VIEW, CommonMessage.SAVE_SUCCESS, newId, null, null);
            }
            if (success) {
                sendIssueEmail(conn, issueForm, originalBean, isNew);
            }
        }
        return null;
    }


    private boolean isValidIssueForm(IssueForm issueForm, boolean isNew, HttpServletResponse response) throws IOException {
        if (issueForm != null) {
            Integer id = issueForm.getId();
            Integer issueCategoryFk = issueForm.getIssueCategoryFk();
            if (issueCategoryFk == null || issueCategoryFk <= 0) {
                return redirectIssueValidationError(isNew, response, id, "Issue Category field is Required.");
            }
            if (StringUtils.isEmpty(issueForm.getDept())) {
                return redirectIssueValidationError(isNew, response, id, "Division field is Required.");
            }
            if (StringUtils.isEmpty(issueForm.getInitiatedBy())) {
                return redirectIssueValidationError(isNew, response, id, "Initiated By field is Required.");
            }
            if (StringUtils.isEmpty(issueForm.getStatus())) {
                return redirectIssueValidationError(isNew, response, id, "Status field is Required.");
            }
            IssueCategory issueCategory = issueCategoryService.getById(issueCategoryFk);
            if (issueCategory != null) {
                issueForm.setIssueCategory(issueCategory);
                if (issueCategory.getCategory().equals("Follow-Up Training")
                    && StringUtils.isEmpty(issueForm.getIsTrainingProvided())
                    && issueForm.getIssueStatus().equals(IssueStatus.CLOSED_SUCCESSFUL)) {
                    return redirectIssueValidationError(isNew, response, id, "Follow-Up Training Provided field is Required.");
                }
            }
        }
        return true;
    }

    protected boolean redirectIssueValidationError(boolean isNew, HttpServletResponse response, Integer id, String message) throws IOException {
        if (isNew) {
            redirectWithMessage(response, FORWARD_ADD, CommonMessage.SAVE_FAILED, null, message, null);
        }
        else {
            redirectWithMessage(response, FORWARD_EDIT, CommonMessage.SAVE_FAILED, id, message, null);
        }
        return false;
    }

    protected String handleView(HttpServletRequest request, HttpServletResponse response, String action, Integer id) throws IOException {
        boolean isNew = id == null || id <= 0;
        Issue issue = isNew ? createNewDomain() : issueService.getById(id);
        IssueForm issueForm = new IssueForm();
        issueForm.copy(issue);
        Integer projectFk = issueForm.getProjectFk();
        if (projectFk == null) {
            logError("Project FK not provided.");
        }
        else {
            setProjectPk(projectFk);
        }
        if (issue == null) {
            //response. CommonMessage.NOT_FOUND, id);
            logError("Issue was not found id=" + id);
            String redirectUrl = "support.do?action=issueList&projectPk=" + getProjectPk()
                + (id != null ? "&issuePk=" + id : "")
                + "&operation=notFound"
            ;
            System.out.println("Redirecting to: '" + redirectUrl + "'");
            response.sendRedirect(redirectUrl);
            return null;
        }
        if (StringUtils.safeEquals(action, FORWARD_COPY)) {
            String description = issueForm.getDescription();
            issueForm.setDescription((StringUtils.isEmpty(description) ? "" : description) + "\n\nCopied from #" + id);
            issueForm.setOpenedBy(getCurrentUser().getFullName());
            issueForm.setOpenedDate(LocalDate.now());

            //copy comments
            ArrayList<IssueComments> issueCommentList = issue.getIssueCommentList();
            if (issueCommentList != null && !issueCommentList.isEmpty()) {
                int size = issueCommentList.size();
                String[] commentsArr = new String[size];
                for (int i = 0; i < size; i ++) {
                    IssueComments issueComments = issueCommentList.get(i);
                    commentsArr[i] = issueComments.getComments();
                }
                issueForm.setCommentsArr(commentsArr);
            }
        }
        ManagedListItemService managedListItemService = getManagedListItemService();
        ArrayList<ManagedListItem> supportTeamList = managedListItemService.getByListAndProjectFk(ManagedList.SUPPORT_TEAM, projectFk);
        if (isNew) {
            Integer shipPk = StringUtils.parseInt(request.getParameter("shipPk"));
            if (shipPk != null) {
                issueForm.setShipFk(shipPk);
            }
        }
        else {
            String personAssigned = issueForm.getPersonAssigned();
            String trainer = issueForm.getTrainer();
            addToListIfNotExist(supportTeamList, personAssigned);
            addToListIfNotExist(supportTeamList, trainer);
        }
        if (StringUtils.isEmpty(action)) {
            setAction(action = isNew ? FORWARD_ADD : FORWARD_EDIT);
        }
        else if (action.equals(FORWARD_COPY)) {
            issueForm.setId(null);
            issueForm.setIssueFileList(null);
            issueForm.setIssueCommentList(null);
            issueForm.setAtoFk(null);
            issueForm.setLastUpdatedBy(null);
            issueForm.setLastUpdatedDate(null);
            issueForm.setCreatedBy(null);
            issueForm.setIsEmailResponded(null);
            issueForm.setIsEmailSent(null);
            String title = issueForm.getTitle();
            issueForm.setTitle("Copy of " + (StringUtils.isEmpty(title) ? "Issue #" + id : title));
        }
        ArrayList<IssueCategory> issueCategoryList = issueCategoryService.getByProjectFk(projectFk);
        ArrayList<ConfiguredSystem> configuredSystemList = configuredSystemService.getAll();
        ArrayList<ManagedListItem> locationList = managedListItemService.getByListAndProjectFk(ManagedList.SHIP_VISIT_LOCATIONS, projectFk);
        ArrayList<ManagedListItem> deptList = managedListItemService.getByListAndProjectFk(ManagedList.UNIT_DIVISIONS, projectFk);
        ArrayList<ManagedListItem> laptopIssueList = managedListItemService.getByListAndProjectFk(ManagedList.LAPTOP_ISSUES, projectFk);
        ArrayList<ManagedListItem> scannerIssueList = managedListItemService.getByListAndProjectFk(ManagedList.SCANNER_ISSUES, projectFk);
        ArrayList<ManagedListItem> softwareIssueList = managedListItemService.getByListAndProjectFk(ManagedList.SOFTWARE_ISSUES, projectFk);
        String pageFrom = request.getParameter("pageFrom");
        pageFrom = StringUtils.isEmpty(pageFrom) ? "issueList" : pageFrom;

        request.setAttribute("projectPk", projectFk + "");
        request.setAttribute("pageFrom", pageFrom);

        request.setAttribute("action", action);
        request.setAttribute("issueForm", issueForm);
        request.setAttribute("issueCategoryList", issueCategoryList);
        request.setAttribute("configuredSystemList", configuredSystemList);
        request.setAttribute("totalTimeList", getTotalTimeList());
        request.setAttribute("statusList", IssueStatus.getList());
        request.setAttribute("shipList", shipService.getAll());
        request.setAttribute("homeportList", shipService.getHomeportList());
        request.setAttribute("phaseList", IssuePhase.getList());
        request.setAttribute("supportTeamList", supportTeamList);
        request.setAttribute("laptopIssueList", laptopIssueList);
        request.setAttribute("scannerIssueList", scannerIssueList);
        request.setAttribute("softwareIssueList", softwareIssueList);
        request.setAttribute("deptList", deptList);
        request.setAttribute("locationList", locationList);
        return action;
    }

    private void addToListIfNotExist(ArrayList<ManagedListItem> managedListItems, String itemValue) {
        boolean isOnList = false;
        if (managedListItems!= null && !managedListItems.isEmpty()) {
            for (ManagedListItem item : managedListItems) {
                String value = item.getItemValue();
                if (value != null && value.equals(itemValue)) {
                    isOnList = true;
                }
            }
        }
        if (!isOnList) {
            managedListItems.add(new ManagedListItem(itemValue));
        }
    }

    protected IssueService getService() {
        return issueService;
    }

    protected Issue createNewDomain() {
        User currentUser = getUserService().getCurrentUser();
        String currentFullName = currentUser.getFullName();
        boolean isCurrentUserOnSupportTeam = false;
        String currentDefault = null;

        ArrayList<ManagedListItem> supportTeamList = getManagedListItemService().getByListAndProjectFk(ManagedList.SUPPORT_TEAM, getProjectPk());
        for (ManagedListItem item : supportTeamList) {
            if (item.getItemValue().equals(currentFullName)) {
                isCurrentUserOnSupportTeam = true;
            }
            if (item.isCurrentDefault()) {
                currentDefault = item.getItemValue();
            }
        }

        Issue issue = new Issue();
        //issue.setInitiatedBy(DEFAULT_INITIATED_BY);//requested by Miracle
        //issue.setDept("N/A");
        issue.setProjectFk(getProjectPk());
        issue.setOpenedDate(LocalDate.now());
        issue.setOpenedBy(currentFullName);
        if (!isCurrentUserOnSupportTeam && !StringUtils.isEmpty(currentDefault)) {
            issue.setPersonAssigned(currentDefault);
            issue.setTrainer(currentDefault);
        }
        else {
            issue.setPersonAssigned(currentFullName);
            issue.setTrainer(currentFullName);
        }
        return issue;
    }

    public static ArrayList<ValueAndLabel> getTotalTimeList() {
        ArrayList<ValueAndLabel> list = new ArrayList<>();
        addValueAndLabelMinutes(list, 15);
        addValueAndLabelMinutes(list, 30);
        addValueAndLabelMinutes(list, 45);
        int minutes = 30;
        int iterations = 17;
        for (int i = 1; i < iterations; i++) {
            int totalMinutes = minutes * i;
            addValueAndLabelMinutes(list, totalMinutes);
        }
        return list;
    }

    private static String convertToHoursAndMinutes(int inputMinutes) {
        if (inputMinutes == 0) {
            return "0 minutes";
        }
        int hours = inputMinutes / 60;
        int minutes = inputMinutes % 60;
        StringBuilder str = new StringBuilder();
        if (hours > 0) {
            str.append(hours).append(" hour");
            if (hours > 1) {
                str.append("s");
            }
        }
        if (minutes > 0) {
            if (str.length() > 0) {
                str.append(" ");
            }
            str.append(minutes).append(" minute");
            if (minutes > 1) {
                str.append("s");
            }
        }
        return str.toString();
    }

    private static void addValueAndLabelMinutes(ArrayList<ValueAndLabel> list, int totalMinutes) {
        list.add(getValueAndLabel(totalMinutes));
    }

    public static ValueAndLabel getValueAndLabel(int totalMinutes) {
        return new ValueAndLabel(totalMinutes + "", convertToHoursAndMinutes(totalMinutes));
    }

    @Override
    protected String path() {
        return "issue.do";
    }
}
