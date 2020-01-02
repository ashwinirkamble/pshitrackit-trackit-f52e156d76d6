package com.premiersolutionshi.old.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.util.StringUtils;

import com.premiersolutionshi.common.constant.ManagedList;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.bean.ProjectBean;
import com.premiersolutionshi.old.bean.ShipBean;
import com.premiersolutionshi.old.bean.SupportBean;
import com.premiersolutionshi.old.bean.SystemBean;
import com.premiersolutionshi.old.model.EmailModel;
import com.premiersolutionshi.old.model.LoginModel;
import com.premiersolutionshi.old.model.LookupModel;
import com.premiersolutionshi.old.model.ProjectModel;
import com.premiersolutionshi.old.model.ReportModel;
import com.premiersolutionshi.old.model.ShipModel;
import com.premiersolutionshi.old.model.SupportModel;
import com.premiersolutionshi.old.model.SystemModel;
import com.premiersolutionshi.old.model.UserModel;
import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Navigation logic for the application's SUPPORT process
 */
public final class SupportAction extends Action {
    private NumberFormat nf1 = new DecimalFormat("0.0");
    private static Logger logger = Logger.getLogger(SupportAction.class.getSimpleName());

    private Logger getLogger() {
        return logger;
    }

    /**
     * Process the specified HTTP request, and create the corresponding HTTP response (or forward to another web component that
     * will create it). Return an <code>ActionForward</code> instance describing where and how control should be forwarded, or
     * <code>null</code> if the response has already been completed.
     *
     * @param mapping           The ActionMapping used to select this instance
     * @param actionForm        The optional ActionForm bean for this request (if any)
     * @param request           The HTTP request we are processing
     * @param response          The HTTP response we are creating
     * @return                  The forward name of the jsp page to load into the browser
     * @throws IOException      If an input/output error occurs
     * @throws ServletException If a servlet exception occurs
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        HttpSession session = request.getSession();
        String action = CommonMethods.nes(request.getParameter("action")).trim();
        String returnAction = null;
        LoginBean loginBean = new LoginBean();
        SupportBean inputBean = (SupportBean) form;
        String operation = null, sortBy = null, sortDir = null, issuePk = null;
        int page = -1;
        ProjectBean projectBean = null;

        int projectPk = CommonMethods.cInt(request.getParameter("projectPk"));
        if (projectPk == -1) action = "errorExpiredSession";

        double startTime = System.currentTimeMillis();

        try (Connection conn = CommonMethods.getConn(servlet, "jdbc/pshi")) {
            loginBean = LoginModel.getUserInfo(conn, request);

            getLogger().info(String.format("%6s%-32s | %-34s | %s", "", "SUPPORT NAVIGATION [START]", "Action: " + action, "Username: " + loginBean.getUsername()));

            /********************************** PREPARE ACTION **********************************/
            String parameter = request.getParameter("shipPk");
            String pageFrom = CommonMethods.nes(request.getParameter("pageFrom"));
            switch (action) {
                case "issueList":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        handleOperationParameter(request);

                        String searchPerformed = CommonMethods.nes(request.getParameter("searchPerformed"));

                        String searchMode = CommonMethods.nvl(CommonMethods.nvl(request.getParameter("searchMode"), session.getAttribute("issueList_searchMode")), "open_non_monthly");
                        String uic = CommonMethods.nvl(request.getParameter("uic"), session.getAttribute("issueList_uic"));
                        String contractNumber = CommonMethods.nes(searchPerformed.equals("Y") ? request.getParameter("contractNumber") : session.getAttribute("issueList_contractNumber"));

                        if (searchMode.equals("ship") && CommonMethods.isEmpty(uic)) searchMode = "open_non_monthly";
                        if (!searchMode.equals("ship")) uic = null;

                        session.setAttribute("issueList_searchMode", searchMode);
                        session.setAttribute("issueList_uic", uic);
                        session.setAttribute("issueList_contractNumber", contractNumber);

                        if (!CommonMethods.isEmpty(contractNumber)) {
                            inputBean = new SupportBean();
                            inputBean.setContractNumber(contractNumber);
                            request.setAttribute("inputBean", inputBean);
                        }

                        request.setAttribute("searchMode", searchMode);
                        request.setAttribute("uic", uic);
                        request.setAttribute("issueList", SupportModel.getIssueList(conn, projectPk, uic, searchMode, contractNumber));
                        request.setAttribute("configuredSystemList", SupportModel.getShipWithIssueList(conn));

                        request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));

                        returnAction = "issueList";
                    }
                    break;

                case "issueListAll":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        handleOperationParameter(request);

                        //Get parameters from form or session
                        String searchPerformed = CommonMethods.nes(request.getParameter("searchPerformed"));
                        sortBy = CommonMethods.nvl(searchPerformed.equals("Y") ? request.getParameter("sortBy") : CommonMethods.nes(session.getAttribute("issueListAll_sortBy")), "issue_pk");
                        sortDir = CommonMethods.nvl(searchPerformed.equals("Y") ? request.getParameter("sortDir") : CommonMethods.nes(session.getAttribute("issueListAll_sortDir")), "DESC");
                        page = CommonMethods.cInt(CommonMethods.nvl(request.getParameter("page"), session.getAttribute("issueListAll_page")), 1);
                        if (!searchPerformed.equals("Y")) inputBean = (SupportBean)session.getAttribute("issueListAll_inputBean");
                        if (inputBean == null) inputBean = new SupportBean();

                        inputBean.setSortBy(sortBy);
                        inputBean.setSortDir(sortDir);

                        //Save parameters to session
                        session.setAttribute("issueListAll_inputBean", inputBean);
                        session.setAttribute("issueListAll_sortBy", sortBy);
                        session.setAttribute("issueListAll_sortDir", sortDir);
                        session.setAttribute("issueListAll_page", String.valueOf(page));

                        //Set default search parameters
                        inputBean.setSearchMode("all");
                        inputBean.setPagination(true);
                        inputBean.setPageNum(page);

                        int issueCnt = SupportModel.getIssueCnt(conn, inputBean, projectPk);
                        int lastPage = (int)Math.ceil((double)issueCnt / (double)100);

                        if (page > lastPage && lastPage > 0) page = lastPage;

                        request.setAttribute("resultCnt", String.valueOf(issueCnt));
                        request.setAttribute("resultList", SupportModel.getIssueList(conn, inputBean, projectPk));

                        request.setAttribute("configuredSystemList", SupportModel.getShipWithIssueList(conn));
                        request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));

                        request.setAttribute("statusList", SupportModel.getStatusList(conn, projectPk));

                        request.setAttribute("divisionList", LookupModel.getList(conn, projectPk, ManagedList.UNIT_DIVISIONS));
                        request.setAttribute("categoryList", LookupModel.getCategoryList(conn, projectPk));
                        request.setAttribute("phaseList", SupportModel.getPhaseList(conn, projectPk));
                        request.setAttribute("shipList", ShipModel.getShipList(conn, "FACET"));

                        request.setAttribute("inputBean", inputBean);
                        request.setAttribute("sortBy", sortBy);
                        request.setAttribute("sortDir", sortDir);
                        request.setAttribute("page", String.valueOf(page));
                        request.setAttribute("lastPage", String.valueOf(lastPage));

                        request.setAttribute("pageFrom", "issueListAll");

                        returnAction = "issueListAll";
                    }
                    break;

                case "myIssueList":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        switch (CommonMethods.nes(request.getParameter("operation"))) {
                            case "addFailed"    : request.setAttribute("errorMsg", "An error occurred while trying to insert the record");break;
                            case "editSuccess"  : request.setAttribute("successMsg", "Successfully updated issue!");break;
                            case "editFailed"   : request.setAttribute("errorMsg", "An error occurred while trying to update issue #" + request.getParameter("issuePk"));break;
                            case "deleteSuccess": request.setAttribute("successMsg", "Successfully deleted issue");break;
                            case "deleteFailed" : request.setAttribute("errorMsg", "An error occurred while trying to delete issue #" + request.getParameter("issuePk"));break;
                            case "notFound"     : request.setAttribute("errorMsg", "Issue #" + request.getParameter("issuePk") + " does not exist");break;
                            case "bulkSuccess"  : request.setAttribute("successMsg", "Successfully created " + request.getParameter("insertCnt") + " issues");break;
                            case "bulkWarning"  : request.setAttribute("warningMsg", "Warning! " + request.getParameter("insertCnt") + " issues created");break;
                            case "bulkError"    : request.setAttribute("errorMsg", "An error occurred while trying to bulk insert issues");break;
                        }

                        sortBy = CommonMethods.nvl(CommonMethods.nvl(request.getParameter("sortBy"), session.getAttribute("myIssueList_sortBy")), "issue_pk");
                        sortDir = CommonMethods.nvl(CommonMethods.nvl(request.getParameter("sortDir"), session.getAttribute("myIssueList_sortDir")), "DESC");

                        //Save parameters to session
                        session.setAttribute("myIssueList_sortBy", sortBy);
                        session.setAttribute("myIssueList_sortDir", sortDir);

                        request.setAttribute("pageFrom", "myIssueList");
                        request.setAttribute("resultList", SupportModel.getIssueList(conn, projectPk, loginBean, sortBy, sortDir));
                        request.setAttribute("customPageTitle", "My Issue List");
                        request.setAttribute("sortBy", sortBy);
                        request.setAttribute("sortDir", sortDir);

                        returnAction = "myIssueList";
                    }
                    break;

                case "issueReports":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        //Reports
                        String searchPerformed = CommonMethods.nes(request.getParameter("searchPerformed"));
                        String contractNumber = CommonMethods.nes(searchPerformed.equals("Y") ? request.getParameter("contractNumber") : session.getAttribute("issueList_contractNumber"));
                        session.setAttribute("issueList_contractNumber", contractNumber);

                        if (!CommonMethods.isEmpty(contractNumber)) {
                            inputBean = new SupportBean();
                            inputBean.setContractNumber(contractNumber);
                            request.setAttribute("inputBean", inputBean);
                        }

                        request.setAttribute("summaryByMonthList", SupportModel.getSummaryByMonthList(conn, projectPk, contractNumber));
                        request.setAttribute("summaryByShipList", SupportModel.getSummaryByShipList(conn, projectPk, contractNumber));
                        request.setAttribute("totalByShipBean", SupportModel.getTotalByShipBean(conn, projectPk, contractNumber));

                        //7-Day Summary Report
                        request.setAttribute("recentIssueTotal", String.valueOf(SupportModel.getRecentIssueTotal(conn, projectPk, contractNumber)));
                        request.setAttribute("recentIssueSummaryList", SupportModel.getRecentIssueSummaryList(conn, projectPk, contractNumber));
                        request.setAttribute("recentIssueList", SupportModel.getRecentIssueList(conn, projectPk, contractNumber));

                        request.setAttribute("recentClosedTotal", String.valueOf(SupportModel.getRecentClosedTotal(conn, projectPk, contractNumber)));
                        request.setAttribute("recentClosedList", SupportModel.getRecentClosedList(conn, projectPk, contractNumber));

                        request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));

                        returnAction = "issueReports";
                    }
                    break;

                case "issueDetail":
                    inputBean = SupportModel.getIssueBean(conn, inputBean);
                    if (inputBean == null) {
                        response.sendRedirect("support.do?action=issueList&projectPk=" + projectPk
                            + "&operation=notFound&issuePk=" + request.getParameter("issuePk"));
                    }
                    else {
                        //override to new issue view page:
                        String redirectUrl = "issue.do?id=" + issuePk + "&projectPk=" + inputBean.getProjectPk()
                            + (StringUtils.isEmpty(pageFrom) ? "" : "&pageFrom=" + pageFrom);
                        logger.info("Redirecting to '" + redirectUrl + "'");
                        response.sendRedirect(redirectUrl);
                        returnAction = null;
                    }

//                    if (inputBean == null || CommonMethods.isEmpty(inputBean.getIssuePk())) {
//                        String pageFrom = CommonMethods.nes(request.getParameter("pageFrom"));
//
//                        if (pageFrom.equals("issueListAll") || pageFrom.equals("myIssueList")) {
//                            getLogger().info(String.format("%8s%-30s | %s", "", "INFO", "Redirecting to " + pageFrom));
//                            response.sendRedirect("support.do?action=" + pageFrom + "&projectPk=" + projectPk + "&operation=notFound&issuePk=" + request.getParameter("issuePk"));
//                            returnAction = null;
//                        } else {
//                            getLogger().info(String.format("%8s%-30s | %s", "", "INFO", "Redirecting to issueList"));
//                            response.sendRedirect("support.do?action=issueList&projectPk=" + projectPk + "&operation=notFound&issuePk=" + request.getParameter("issuePk"));
//                            returnAction = null;
//                        }
//                    } else {
//                        switch (CommonMethods.nes(request.getParameter("operation"))) {
//                            case "addSuccess" : request.setAttribute("successMsg", "Successfully inserted new issue!");break;
//                            case "editSuccess": request.setAttribute("successMsg", "Successfully updated issue!");break;
//                        }
//
//                        request.setAttribute("resultBean", inputBean);
//                        request.setAttribute("pocBean", UserModel.getEmployeePocBean(conn, inputBean.getPersonAssigned()));
//
//                        if (!CommonMethods.isEmpty(inputBean.getUic())) {
//                            //Get Curr Versions
//                            request.setAttribute("currFacetVersion", LookupModel.getCurrFacetVersion(conn, projectPk));
//                            request.setAttribute("currOsVersion", LookupModel.getCurrOsVersion(conn, projectPk));
//                            request.setAttribute("currDmsVersion", SystemModel.getCurrDmsVersion());
//
//                            //Ship pop-up data
//                            request.setAttribute("shipBean", ShipModel.getShipBean(conn, inputBean.getUic()));
//                            request.setAttribute("configuredSystemList", SystemModel.getConfiguredSystemListByUic(conn, inputBean.getUic()));
//                            //request.setAttribute("shipTaskList", ProjectModel.getShipTaskList(conn, CommonMethods.cInt(projectPk), inputBean.getUic()));
//                            request.setAttribute("shipPocList", ShipModel.getPocList(conn, inputBean.getUic()));
//                            request.setAttribute("shipIssueList", SupportModel.getIssueList(conn, CommonMethods.cInt(projectPk), inputBean.getUic(), "open"));
//                            request.setAttribute("shipLastVisitBean", SupportModel.getLastVisitBean(conn, inputBean.getUic()));
//                            request.setAttribute("shipUpcomingVisitList", SupportModel.getUpcomingVisitList(conn, inputBean.getUic()));
//                        }
//
//                        request.setAttribute("customPageTitle", "View Issue #" + inputBean.getIssuePk());
//
//                        String pageFrom = CommonMethods.nes(request.getParameter("pageFrom"));
//                        if (pageFrom.equals("issueListAll")) {
//                            //Get parameters from session
//                            sortBy = CommonMethods.nvl(session.getAttribute("issueListAll_sortBy"), "issue_pk");
//                            sortDir = CommonMethods.nvl(session.getAttribute("issueListAll_sortDir"), "DESC");
//                            inputBean = (SupportBean)session.getAttribute("issueListAll_inputBean");
//                            if (inputBean == null) inputBean = new SupportBean();
//                            inputBean.setSearchMode("all");
//                            inputBean.setPagination(false);
//                            int issueCnt = SupportModel.getIssueCnt(conn, inputBean, projectPk);
//
//                            if (issueCnt < 1000) {
//                                ArrayList<SupportBean> currList = SupportModel.getIssueList(conn, inputBean, projectPk);
//
//                                request.setAttribute("currStr", SupportModel.getCurrRecordStr(currList, CommonMethods.cInt(request.getParameter("issuePk"))));
//                                request.setAttribute("prevBean", SupportModel.getPrevIssueBean(currList, CommonMethods.cInt(request.getParameter("issuePk"))));
//                                request.setAttribute("nextBean", SupportModel.getNextIssueBean(currList, CommonMethods.cInt(request.getParameter("issuePk"))));
//                            }
//                        } else if (pageFrom.equals("myIssueList")) {
//                            sortBy = CommonMethods.nvl(session.getAttribute("myIssueList_sortBy"), "issue_pk");
//                            sortDir = CommonMethods.nvl(session.getAttribute("myIssueList_sortDir"), "DESC");
//
//                            ArrayList<SupportBean> currList = SupportModel.getIssueList(conn, projectPk, loginBean, sortBy, sortDir);
//
//                            request.setAttribute("currStr", SupportModel.getCurrRecordStr(currList, CommonMethods.cInt(inputBean.getIssuePk())));
//                            request.setAttribute("prevBean", SupportModel.getPrevIssueBean(currList, CommonMethods.cInt(inputBean.getIssuePk())));
//                            request.setAttribute("nextBean", SupportModel.getNextIssueBean(currList, CommonMethods.cInt(inputBean.getIssuePk())));
//                        } else {
//                            String searchMode = CommonMethods.nvl(session.getAttribute("issueList_searchMode"), "open_non_monthly");
//                            String uic = CommonMethods.nes(session.getAttribute("issueList_uic"));
//                            String contractNumber = CommonMethods.nes(session.getAttribute("issueList_contractNumber"));
//                            if (searchMode.equals("ship") && CommonMethods.isEmpty(uic)) searchMode = "open_non_monthly";
//                            if (!searchMode.equals("ship")) uic = null;
//
//                            ArrayList<SupportBean> currList = SupportModel.getIssueList(conn, projectPk, uic, searchMode, contractNumber);
//
//                            request.setAttribute("currStr", SupportModel.getCurrRecordStr(currList, CommonMethods.cInt(inputBean.getIssuePk())));
//                            request.setAttribute("prevBean", SupportModel.getPrevIssueBean(currList, CommonMethods.cInt(inputBean.getIssuePk())));
//                            request.setAttribute("nextBean", SupportModel.getNextIssueBean(currList, CommonMethods.cInt(inputBean.getIssuePk())));
//                        }
//
//                        request.setAttribute("pageFrom", pageFrom);
//
//                        returnAction = "issueDetail";
//                    }
                    break;

                case "issueAddDo":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else if ((issuePk = SupportModel.findDupeScheduledDate(conn, inputBean, projectPk)) != null) { //duplicate support visit date - return to add
                        projectBean = ProjectModel.getProjectBean(conn, projectPk);
                        request.setAttribute("statusList", SupportModel.getStatusList(conn, projectPk));

                        request.setAttribute("categoryList", LookupModel.getCategoryList(conn, projectPk));
                        request.setAttribute("locationList", LookupModel.getLocationList(conn, projectPk));
                        request.setAttribute("laptopIssueList", LookupModel.getLaptopIssueList(conn, projectPk));
                        request.setAttribute("scannerIssueList", LookupModel.getScannerIssueList(conn, projectPk));
                        request.setAttribute("softwareIssueList", LookupModel.getSoftwareIssueList(conn, projectPk));

                        request.setAttribute("phaseList", SupportModel.getPhaseList(conn, projectPk));
                        request.setAttribute("openedByList", SupportModel.getOpenedByList(conn, projectPk));
                        request.setAttribute("homeportList", ShipModel.getHomeportList(conn));
                        request.setAttribute("shipList", ShipModel.getShipList(conn, "FACET"));
                        request.setAttribute("supportTeamList", SupportModel.getSupportTeamList(conn));
                        request.setAttribute("editType", "add");
                        request.setAttribute("customPageTitle", "Add New Support Issue for " + projectBean.getProjectName());

                        request.setAttribute("shipPk", parameter);
                        request.setAttribute("pageFrom", request.getParameter("pageFrom"));
                        request.setAttribute("errorMsg", "Error - Scheduled visit date exists in Issue #" + issuePk);

                        if (CommonMethods.isEmpty(inputBean.getTrainer())) {
                            inputBean.setCurrTrainer(loginBean.getFullName());
                            inputBean.setTrainer(loginBean.getFullName());
                        }
                        request.setAttribute("inputBean", inputBean);

                        returnAction = "issueAdd";
                    } else {
                        int newIssuePk = SupportModel.insertIssue(conn, inputBean, loginBean, CommonMethods.getUploadDir(request));
                        operation = newIssuePk >= 1 ? "addSuccess" : "addFailed";
                        if (operation.equals("addSuccess")) { //Success
                            //Send e-mail if assigned to person different than user (and issue is not closed)
                            if (!inputBean.getPersonAssigned().equals(loginBean.getFullName()) && !CommonMethods.isIn(SupportModel.closedLabels, inputBean.getStatus())) {
                                EmailModel emailModel = new EmailModel();
                                emailModel.sendNewIssueEmail(conn, inputBean, newIssuePk, loginBean);
                            }

                            getLogger().info(String.format("%8s%-30s | %s", "", "INFO", "Redirecting to issueDetail"));
                            response.sendRedirect("support.do?action=issueDetail&projectPk=" + projectPk + "&operation=" + operation + "&issuePk=" + newIssuePk + "&pageFrom=" + pageFrom);
                            returnAction = null;
                        } else if (pageFrom.equals("issueListAll") || pageFrom.equals("myIssueList")) { //Failed
                            getLogger().info(String.format("%8s%-30s | %s", "", "INFO", "Redirecting to " + pageFrom));
                            response.sendRedirect("support.do?action=" + pageFrom + "&projectPk=" + projectPk + "&operation=" + operation + "&issuePk=" + newIssuePk);
                            returnAction = null;
                        } else { //Failed
                            getLogger().info(String.format("%8s%-30s | %s", "", "INFO", "Redirecting to issueList"));
                            response.sendRedirect("support.do?action=issueList&projectPk=" + projectPk + "&operation=" + operation + "&issuePk=" + newIssuePk);
                            returnAction = null;
                        }
                    }
                    break;

                case "issueCopy":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        projectBean = ProjectModel.getProjectBean(conn, projectPk);
                        request.setAttribute("statusList", SupportModel.getStatusList(conn, projectPk));

                        request.setAttribute("categoryList", LookupModel.getCategoryList(conn, projectPk));
                        request.setAttribute("locationList", LookupModel.getLocationList(conn, projectPk));
                        request.setAttribute("laptopIssueList", LookupModel.getLaptopIssueList(conn, projectPk));
                        request.setAttribute("scannerIssueList", LookupModel.getScannerIssueList(conn, projectPk));
                        request.setAttribute("softwareIssueList", LookupModel.getSoftwareIssueList(conn, projectPk));

                        request.setAttribute("phaseList", SupportModel.getPhaseList(conn, projectPk));
                        request.setAttribute("openedByList", SupportModel.getOpenedByList(conn, projectPk));
                        request.setAttribute("homeportList", ShipModel.getHomeportList(conn));
                        request.setAttribute("shipList", ShipModel.getShipList(conn, "FACET"));
                        request.setAttribute("supportTeamList", SupportModel.getSupportTeamList(conn));
                        request.setAttribute("editType", "copy");
                        request.setAttribute("configuredSystemAllList", SystemModel.getConfiguredSystemList(conn));

                        inputBean = SupportModel.getIssueBean(conn, inputBean);

                        if (CommonMethods.isEmpty(inputBean.getTrainer())) {
                            inputBean.setCurrTrainer(loginBean.getFullName());
                            inputBean.setTrainer(loginBean.getFullName());
                        }

                        request.setAttribute("inputBean", inputBean);
                        request.setAttribute("customPageTitle", "Copy Support Issue #" + inputBean.getIssuePk());
                        request.setAttribute("pageFrom", request.getParameter("pageFrom"));

                        returnAction = "issueAdd";
                    }
                    break;
                case "issueAdd":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        request.setAttribute("editType", "add");
                        projectBean = ProjectModel.getProjectBean(conn, projectPk);
                        request.setAttribute("statusList", SupportModel.getStatusList(conn, projectPk));

                        request.setAttribute("categoryList", LookupModel.getCategoryList(conn, projectPk));
                        request.setAttribute("locationList", LookupModel.getLocationList(conn, projectPk));
                        request.setAttribute("laptopIssueList", LookupModel.getLaptopIssueList(conn, projectPk));
                        request.setAttribute("scannerIssueList", LookupModel.getScannerIssueList(conn, projectPk));
                        request.setAttribute("softwareIssueList", LookupModel.getSoftwareIssueList(conn, projectPk));

                        request.setAttribute("phaseList", SupportModel.getPhaseList(conn, projectPk));
                        request.setAttribute("openedByList", SupportModel.getOpenedByList(conn, projectPk));
                        request.setAttribute("homeportList", ShipModel.getHomeportList(conn));
                        request.setAttribute("shipList", ShipModel.getShipList(conn, "FACET"));
                        request.setAttribute("supportTeamList", SupportModel.getSupportTeamList(conn));
                        request.setAttribute("customPageTitle", "Add New Support Issue for " + projectBean.getProjectName());

                        request.setAttribute("shipPk", parameter);
                        request.setAttribute("pageFrom", request.getParameter("pageFrom"));

                        inputBean = new SupportBean();
                        inputBean.setCurrPersonAssigned(loginBean.getFullName());
                        inputBean.setPersonAssigned(loginBean.getFullName());
                        inputBean.setCurrTrainer(loginBean.getFullName());
                        inputBean.setTrainer(loginBean.getFullName());
                        inputBean.setCurrPhase("Ships Support");
                        inputBean.setPhase("Ships Support");
                        request.setAttribute("inputBean", inputBean);

                        returnAction = "issueAdd";
                    }
                    break;

                case "issueEdit":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        inputBean = SupportModel.getIssueBean(conn, inputBean);

                        if (CommonMethods.isEmpty(inputBean.getTrainer())) {
                            inputBean.setCurrTrainer(loginBean.getFullName());
                            inputBean.setTrainer(loginBean.getFullName());
                        }

                        request.setAttribute("editType", "edit");
                        request.setAttribute("inputBean", inputBean);
                        request.setAttribute("statusList", SupportModel.getStatusList(conn, projectPk));

                        request.setAttribute("categoryList", LookupModel.getCategoryList(conn, projectPk));
                        request.setAttribute("locationList", LookupModel.getLocationList(conn, projectPk));
                        request.setAttribute("laptopIssueList", LookupModel.getLaptopIssueList(conn, projectPk));
                        request.setAttribute("scannerIssueList", LookupModel.getScannerIssueList(conn, projectPk));
                        request.setAttribute("softwareIssueList", LookupModel.getSoftwareIssueList(conn, projectPk));

                        request.setAttribute("phaseList", SupportModel.getPhaseList(conn, projectPk));
                        request.setAttribute("openedByList", SupportModel.getOpenedByList(conn, projectPk));
                        request.setAttribute("homeportList", ShipModel.getHomeportList(conn));
                        request.setAttribute("shipList", ShipModel.getShipList(conn, "FACET"));
                        request.setAttribute("supportTeamList", SupportModel.getSupportTeamList(conn));
                        request.setAttribute("customPageTitle", "Edit Support Issue #" + inputBean.getIssuePk());
                        request.setAttribute("configuredSystemList", SupportModel.getShipWithIssueList(conn));
                        request.setAttribute("configuredSystemAllList", SystemModel.getConfiguredSystemList(conn));

                        if (pageFrom.equals("issueListAll")) {
                            //Get parameters from session
                            sortBy = CommonMethods.nvl(session.getAttribute("issueListAll_sortBy"), "issue_pk");
                            sortDir = CommonMethods.nvl(session.getAttribute("issueListAll_sortDir"), "DESC");
                            inputBean = (SupportBean)session.getAttribute("issueListAll_inputBean");
                            if (inputBean == null) inputBean = new SupportBean();
                            inputBean.setSearchMode("all");
                            inputBean.setPagination(false);
                            int issueCnt = SupportModel.getIssueCnt(conn, inputBean, projectPk);

                            if (issueCnt < 1000) {
                                ArrayList<SupportBean> currList = SupportModel.getIssueList(conn, inputBean, projectPk);

                                request.setAttribute("currStr", SupportModel.getCurrRecordStr(currList, CommonMethods.cInt(request.getParameter("issuePk"))));
                                request.setAttribute("prevBean", SupportModel.getPrevIssueBean(currList, CommonMethods.cInt(request.getParameter("issuePk"))));
                                request.setAttribute("nextBean", SupportModel.getNextIssueBean(currList, CommonMethods.cInt(request.getParameter("issuePk"))));
                            }
                        } else if (pageFrom.equals("myIssueList")) {
                            sortBy = CommonMethods.nvl(session.getAttribute("myIssueList_sortBy"), "issue_pk");
                            sortDir = CommonMethods.nvl(session.getAttribute("myIssueList_sortDir"), "ASC");

                            ArrayList<SupportBean> currList = SupportModel.getIssueList(conn, projectPk, loginBean, sortBy, sortDir);

                            request.setAttribute("currStr", SupportModel.getCurrRecordStr(currList, CommonMethods.cInt(inputBean.getIssuePk())));
                            request.setAttribute("prevBean", SupportModel.getPrevIssueBean(currList, CommonMethods.cInt(inputBean.getIssuePk())));
                            request.setAttribute("nextBean", SupportModel.getNextIssueBean(currList, CommonMethods.cInt(inputBean.getIssuePk())));
                        } else if (pageFrom.equals("issueList")) {
                            String searchMode = CommonMethods.nvl(session.getAttribute("issueList_searchMode"), "open_non_monthly");
                            String uic = CommonMethods.nes(session.getAttribute("issueList_uic"));
                            String contractNumber = CommonMethods.nes(session.getAttribute("issueList_contractNumber"));
                            if (searchMode.equals("ship") && CommonMethods.isEmpty(uic)) searchMode = "open_non_monthly";
                            if (!searchMode.equals("ship")) uic = null;

                            ArrayList<SupportBean> currList = SupportModel.getIssueList(conn, projectPk, uic, searchMode, contractNumber);

                            request.setAttribute("currStr", SupportModel.getCurrRecordStr(currList, CommonMethods.cInt(inputBean.getIssuePk())));
                            request.setAttribute("prevBean", SupportModel.getPrevIssueBean(currList, CommonMethods.cInt(inputBean.getIssuePk())));
                            request.setAttribute("nextBean", SupportModel.getNextIssueBean(currList, CommonMethods.cInt(inputBean.getIssuePk())));
                        }

                        request.setAttribute("pageFrom", pageFrom);
                        returnAction = "issueEdit";
                    }
                    break;

                case "issueEditDo":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else if ((issuePk = SupportModel.findDupeScheduledDate(conn, inputBean, projectPk)) != null) { //duplicate support visit date - return to edit
                        if (CommonMethods.isEmpty(inputBean.getTrainer())) {
                            inputBean.setCurrTrainer(loginBean.getFullName());
                            inputBean.setTrainer(loginBean.getFullName());
                        }

                        request.setAttribute("inputBean", inputBean);
                        request.setAttribute("statusList", SupportModel.getStatusList(conn, projectPk));

                        request.setAttribute("categoryList", LookupModel.getCategoryList(conn, projectPk));
                        request.setAttribute("locationList", LookupModel.getLocationList(conn, projectPk));
                        request.setAttribute("laptopIssueList", LookupModel.getLaptopIssueList(conn, projectPk));
                        request.setAttribute("scannerIssueList", LookupModel.getScannerIssueList(conn, projectPk));
                        request.setAttribute("softwareIssueList", LookupModel.getSoftwareIssueList(conn, projectPk));

                        request.setAttribute("phaseList", SupportModel.getPhaseList(conn, projectPk));
                        request.setAttribute("openedByList", SupportModel.getOpenedByList(conn, projectPk));
                        request.setAttribute("homeportList", ShipModel.getHomeportList(conn));
                        request.setAttribute("shipList", ShipModel.getShipList(conn, "FACET"));
                        request.setAttribute("supportTeamList", SupportModel.getSupportTeamList(conn));
                        request.setAttribute("editType", "edit");
                        request.setAttribute("customPageTitle", "Edit Support Issue #" + inputBean.getIssuePk());

                        request.setAttribute("pageFrom", pageFrom);
                        request.setAttribute("errorMsg", "Error - Scheduled visit date exists in Issue #" + issuePk);

                        returnAction = "issueEdit";
                    } else {
                        //Store currList for post-operation
                        ArrayList<SupportBean> currList = null;
                        if (pageFrom.equals("myIssueList") && CommonMethods.isIn(SupportModel.closedLabels, inputBean.getStatus())) {
                            sortBy = CommonMethods.nvl(session.getAttribute("myIssueList_sortBy"), "issue_pk");
                            sortDir = CommonMethods.nvl(session.getAttribute("myIssueList_sortDir"), "ASC");
                            currList = SupportModel.getIssueList(conn, projectPk, loginBean, sortBy, sortDir);
                        }

                        //Get original Bean for e-mail notification
                        SupportBean originalBean = SupportModel.getIssueBean(conn, inputBean);

                        //Execute the operation
                        operation = SupportModel.updateIssue(conn, inputBean, loginBean, CommonMethods.getUploadDir(request)) ? "editSuccess" : "editFailed";

                        //Determine re-direct
                        if (operation.equals("editSuccess")) {
                            //Send e-mail notification if status change or comment added and user is not same as person assigned
                            EmailModel emailModel = new EmailModel();
                            emailModel.sendEditIssueEmail(conn, inputBean, originalBean, loginBean);

                            //Success on closing while in myIssueList - Move onto next issue (if exists) otherwise issueList
                            if (pageFrom.equals("myIssueList") && CommonMethods.isIn(SupportModel.closedLabels, inputBean.getStatus())) {
                                SupportBean nextBean = SupportModel.getNextIssueBean(currList, CommonMethods.cInt(inputBean.getIssuePk()));
                                if (nextBean != null) {
                                    getLogger().info(String.format("%8s%-30s | %s", "", "INFO", "Redirecting to issueDetail (next issue)"));
                                    response.sendRedirect("support.do?action=issueDetail&projectPk=" + projectPk + "&operation=" + operation + "&issuePk=" + nextBean.getIssuePk() + "&pageFrom=" + pageFrom);
                                    returnAction = null;
                                } else {
                                    getLogger().info(String.format("%8s%-30s | %s", "", "INFO", "Redirecting to " + pageFrom));
                                    response.sendRedirect("support.do?action=" + pageFrom + "&projectPk=" + projectPk + "&operation=" + operation + "&issuePk=" + inputBean.getIssuePk());
                                    returnAction = null;
                                }
                            } else { //Default success
                                getLogger().info(String.format("%8s%-30s | %s", "", "INFO", "Redirecting to issueDetail"));
                                response.sendRedirect("support.do?action=issueDetail&projectPk=" + projectPk + "&operation=" + operation + "&issuePk=" + inputBean.getIssuePk() + "&pageFrom=" + pageFrom);
                                returnAction = null;
                            }
                        //Failed - return to pageFrom
                        } else if (pageFrom.equals("issueListAll") || pageFrom.equals("myIssueList")) {
                            getLogger().info(String.format("%8s%-30s | %s", "", "INFO", "Redirecting to " + pageFrom));
                            response.sendRedirect("support.do?action=" + pageFrom + "&projectPk=" + projectPk + "&operation=" + operation + "&issuePk=" + inputBean.getIssuePk());
                            returnAction = null;
                        //Failed - Default return
                        } else {
                            getLogger().info(String.format("%8s%-30s | %s", "", "INFO", "Redirecting to issueList"));
                            response.sendRedirect("support.do?action=issueList&projectPk=" + projectPk + "&operation=" + operation + "&issuePk=" + inputBean.getIssuePk());
                            returnAction = null;
                        }
                    }
                    break;

                case "issueDeleteDo":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        operation = SupportModel.deleteIssue(conn, inputBean, CommonMethods.getUploadDir(request)) ? "deleteSuccess" : "deleteFailed";

                        if (pageFrom.equals("issueListAll") || pageFrom.equals("myIssueList")) {
                            getLogger().info(String.format("%8s%-30s | %s", "", "INFO", "Redirecting to " + pageFrom));
                            response.sendRedirect("support.do?action=" + pageFrom + "&projectPk=" + projectPk + "&operation=" + operation + "&issuePk=" + inputBean.getIssuePk());
                            returnAction = null;
                        } else {
                            getLogger().info(String.format("%8s%-30s | %s", "", "INFO", "Redirecting to issueList"));
                            response.sendRedirect("support.do?action=issueList&projectPk=" + projectPk + "&operation=" + operation + "&issuePk=" + inputBean.getIssuePk());
                            returnAction = null;
                        }
                    }
                    break;

                case "generateFeedbackForm":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        if (!CommonMethods.isEmpty(inputBean.getIssuePk())) {
                            inputBean = SupportModel.getIssueBean(conn, inputBean);
                        } else if (!CommonMethods.isEmpty(request.getParameter("uic"))) {
                            inputBean = new SupportBean();
                            inputBean.setUic(request.getParameter("uic"));
                        }
                        request.setAttribute("inputBean", inputBean);
                        request.setAttribute("shipList", ShipModel.getShipList(conn, "FACET"));
                        request.setAttribute("userList", UserModel.getUserList(conn));
                        returnAction = "generateFeedbackForm";
                    }
                    break;

                case "generateFeedbackFormDo":
                    request.setAttribute("inputBean", inputBean);
                    ShipBean shipBean = ShipModel.getShipBean(conn, inputBean.getUic());
                    request.setAttribute("shipBean", shipBean);
                    request.setAttribute("configuredSystemList", SystemModel.getConfiguredSystemListByUic(conn, shipBean.getUic()));
                    request.setAttribute("shipPocList", ShipModel.getPocList(conn, shipBean.getUic()));
                    request.setAttribute("transmittalSummaryBean", ReportModel.getTransmittalSummaryBean(conn, CommonMethods.cInt(shipBean.getShipPk())));
                    request.setAttribute("missingTransmittalStr", ReportModel.printTransmittalList(ReportModel.getMissingTransmittalList(conn, CommonMethods.cInt(shipBean.getShipPk()))));

                    request.setAttribute("currFacetVersion", LookupModel.getCurrFacetVersion(conn, projectPk));
                    request.setAttribute("currDmsVersion", SystemModel.getCurrDmsVersion());

                    request.setAttribute("generatedDate", CommonMethods.getDate("MM/DD/YYYY HH24MI"));

                    returnAction = "supportFeedbackForm";
                    break;

                case "issueMonthlyChart":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        //Reports
                        String contractNumber = CommonMethods.nes(session.getAttribute("issueList_contractNumber"));
                        request.setAttribute("summaryByMonthList", SupportModel.getSummaryByMonthList(conn, projectPk, contractNumber));
                        request.setAttribute("contractNumber", contractNumber);
                        returnAction = "issueMonthlyChart";
                    }
                    break;

                case "shipVisitCalendar":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        int month = CommonMethods.cInt(request.getParameter("month"));
                        int year = CommonMethods.cInt(request.getParameter("year"));
                        if (month < 1 || month > 12) month = CommonMethods.cInt(CommonMethods.getDate("MM"));
                        if (year < 1900) year = CommonMethods.cInt(CommonMethods.getDate("YYYY"));

                        int prevMonth = month - 1 < 1 ? 12 : month - 1;
                        int prevYear = month -1 < 1 ? year - 1 : year;
                        int nextMonth = month + 1 > 12 ? 1 : month + 1;
                        int nextYear = month + 1 > 12 ? year + 1 : year;

                        request.setAttribute("inputBean", inputBean);
                        request.setAttribute("resultList", SupportModel.getReportCalendarList(conn, inputBean, month, year));

                        request.setAttribute("estSchedShipList", SupportModel.getEstSchedShipList(conn, month, year));
                        request.setAttribute("currMonthYear", CommonMethods.getMonthName(month-1) + " " + year);
                        request.setAttribute("prevMonthUrl", "support.do?action=shipVisitCalendar&month=" + prevMonth + "&year=" + prevYear + "&projectPk=" + projectPk + "&contractNumber=" + inputBean.getContractNumber());
                        request.setAttribute("nextMonthUrl", "support.do?action=shipVisitCalendar&month=" + nextMonth + "&year=" + nextYear + "&projectPk=" + projectPk + "&contractNumber=" + inputBean.getContractNumber());
                        request.setAttribute("month", String.valueOf(month));
                        request.setAttribute("year", String.valueOf(year));

                        request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));
                        returnAction = "shipVisitCalendar";
                    }
                    break;

                case "atoList":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        request.setAttribute("resultList", SupportModel.getAtoSummaryList(conn, projectPk));
                        returnAction = "atoList";

                        String atoDateStr = CommonMethods.getDate(inputBean.getAtoDate(), "YYYYMMDD");
                        switch (CommonMethods.nes(request.getParameter("operation"))) {
                            case "addSuccess"    : setMessage(request, "successMsg", "Successfully inserted ATOUpdates_" + atoDateStr);break;
                            case "addFailed"     : setMessage(request, "errorMsg", "An error occurred while trying to insert the ato record");break;
                            case "editSuccess"   : setMessage(request, "successMsg", "Successfully updated ATOUpdates_" + atoDateStr);break;
                            case "editFailed"    : setMessage(request, "errorMsg", "An error occurred while trying to update the ato record");break;
                            case "deleteSuccess" : setMessage(request, "successMsg", "Successfully deleted ATOUpdates_" + atoDateStr);break;
                            case "deleteFailed"  : setMessage(request, "errorMsg", "An error occurred while trying to delete the ato record");break;
                        }
                    }
                    break;

                case "atoAdd":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        request.setAttribute("editType", "add");
                        request.setAttribute("customPageTitle", "Add New ATO");
                        request.setAttribute("configuredSystemList", SystemModel.getEmailConfiguredSystemList(conn));
                        request.setAttribute("currFacetVersion", LookupModel.getCurrFacetVersion(conn, projectPk));
                        returnAction = "atoAdd";
                    }
                    break;

                case "atoAddDo":
                    operation = SupportModel.insertAto(conn, inputBean, loginBean) ? "addSuccess" : "addFailed";
                    getLogger().info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to atoList"));
                    response.sendRedirect("support.do?action=atoList&projectPk=" + projectPk + "&operation=" + operation + "&atoDate=" + inputBean.getAtoDate());
                    returnAction = null;
                    break;

                case "atoEdit":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        inputBean = SupportModel.getAtoSummaryBean(conn, CommonMethods.cInt(inputBean.getAtoPk()));
                        ArrayList<SystemBean> configuredSystemList = SystemModel.getEmailConfiguredSystemList(conn);
                        ArrayList<SupportBean> atoDetailList = SupportModel.getAtoDetailList(conn, CommonMethods.cInt(inputBean.getAtoPk()));
                        SupportModel.cleanConfiguredSystemList(configuredSystemList, atoDetailList);
                        request.setAttribute("inputBean", inputBean);
                        request.setAttribute("atoDetailList", atoDetailList);
                        request.setAttribute("customPageTitle", "Edit " + inputBean.getAtoFilename());
                        request.setAttribute("configuredSystemList", configuredSystemList);
                        request.setAttribute("editType", "edit");
                        returnAction = "atoEdit";
                    }
                    break;

                case "atoEditDo":
                    operation = SupportModel.updateAto(conn, inputBean, loginBean) ? "editSuccess" : "editFailed";
                    getLogger().info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to atoList"));
                    response.sendRedirect("support.do?action=atoList&projectPk=" + projectPk + "&operation=" + operation + "&atoDate=" + inputBean.getAtoDate());
                    returnAction = null;
                    break;

                case "atoDeleteDo":
                    inputBean = SupportModel.getAtoSummaryBean(conn, CommonMethods.cInt(inputBean.getAtoPk()));
                    operation = SupportModel.deleteAto(conn, inputBean, loginBean) ? "deleteSuccess" : "deleteFailed";
                    getLogger().info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to atoList"));
                    response.sendRedirect("support.do?action=atoList&projectPk=" + projectPk + "&operation=" + operation + "&atoDate=" + inputBean.getAtoDate());
                    returnAction = null;
                    break;

                case "bulkEmailTool":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        request.setAttribute("configuredSystemList", SystemModel.getEmailConfiguredSystemList(conn));
                        request.setAttribute("supportTeamList", SupportModel.getSupportTeamList(conn));
                        request.setAttribute("openedByList", SupportModel.getOpenedByList(conn, projectPk));

                        request.setAttribute("currFacetVersion", LookupModel.getCurrFacetVersion(conn, projectPk));
                        request.setAttribute("currDmsVersion", SystemModel.getCurrDmsVersion());
                        request.setAttribute("currOsVersion", LookupModel.getCurrOsVersion(conn, projectPk));

                        inputBean = new SupportBean();
                        inputBean.setPersonAssigned(loginBean.getFullName());
                        //inputBean.setAutoCloseStatus("7 - Closed (No Response)");
                        request.setAttribute("inputBean", inputBean);

                        returnAction = "bulkEmailTool";
                    }
                    break;

                case "bulkEmailToolDo":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
                        returnAction = "errorNoRole";
                    } else {
                        int insertCnt = SupportModel.bulkInsertIssue(conn, inputBean, loginBean);
                        operation = insertCnt > 0 ? "bulkSuccess" : insertCnt == 0 ? "bulkWarning" : "bulkError";
                        getLogger().info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to issueList"));
                        response.sendRedirect("support.do?action=issueList&projectPk=" + projectPk + "&operation=" + operation + "&insertCnt=" + insertCnt);
                        returnAction = null;
                    }
                    break;

                case "errorExpiredSession":
                    returnAction = "errorExpiredSession";
                    break;

                default:
                    returnAction = "error404";
                    break;
            }
            if (!CommonMethods.isEmpty(returnAction)) CommonMethods.appFinally(conn, returnAction, loginBean, projectPk, request);
        } catch (SQLException e) {
            getLogger().error(String.format("%8s%-30s | %s", "", "ERROR", e));
            returnAction = "errorDatabaseDown";
        } catch (Exception e) {
            getLogger().error(String.format("%8s%-30s | %s", "", "ERROR", e));
            request.setAttribute("errorMsg", e.toString());
            returnAction = "errorUnexpected";
            e.printStackTrace();
        }

        double endTime = System.currentTimeMillis();
        String elapsedTime = nf1.format((endTime - startTime) / (double) 1000);
        String logUsername = loginBean == null ? null : loginBean.getUsername();
        getLogger().info(String.format("%6s%-32s | %-34s | %-52s | %s\r\n", "", "SUPPORT NAVIGATION [END]", "Return: " + returnAction, "Username: " + CommonMethods.nvl(logUsername, "GUEST"), "Elapsed Time: " + elapsedTime + " sec"));

        return !CommonMethods.isEmpty(returnAction) ? mapping.findForward(returnAction) : null;
    }

    private void handleOperationParameter(HttpServletRequest request) {
        String operationParam = request.getParameter("operation");
        String issuePk = request.getParameter("issuePk");
        String insertCnt = request.getParameter("insertCnt");

        String sucMsg = "successMsg";
        String warMsg = "warningMsg";
        String errMsg = "errorMsg";
        switch (CommonMethods.nes(operationParam)) {
            case "addFailed"    : setMessage(request, errMsg, "An error occurred while trying to insert the record"); break;
            case "editFailed"   : setMessage(request, errMsg, "An error occurred while trying to update issue #" + issuePk); break;
            case "deleteSuccess": setMessage(request, sucMsg, "Successfully deleted issue #" + issuePk); break;
            case "deleteFailed" : setMessage(request, errMsg, "An error occurred while trying to delete issue #" + issuePk); break;
            case "notFound"     : setMessage(request, errMsg, "Issue #" + issuePk + " does not exist"); break;
            case "bulkSuccess"  : setMessage(request, sucMsg, "Successfully created " + insertCnt + " issues"); break;
            case "bulkWarning"  : setMessage(request, warMsg, "Warning! " + insertCnt + " issues created"); break;
            case "bulkError"    : setMessage(request, errMsg, "An error occurred while trying to bulk insert issues"); break;
        }
    }

    private void setMessage(HttpServletRequest request, String attributeName, String message) {
        request.setAttribute(attributeName, message);
    }
}
