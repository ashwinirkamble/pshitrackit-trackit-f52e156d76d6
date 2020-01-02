package com.premiersolutionshi.old.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.premiersolutionshi.common.constant.ManagedList;
import com.premiersolutionshi.common.util.StringUtils;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.bean.SystemBean;
import com.premiersolutionshi.old.model.BackfileModel;
import com.premiersolutionshi.old.model.HardwareModel;
import com.premiersolutionshi.old.model.LoginModel;
import com.premiersolutionshi.old.model.LookupModel;
import com.premiersolutionshi.old.model.ProjectModel;
import com.premiersolutionshi.old.model.ShipModel;
import com.premiersolutionshi.old.model.SoftwareModel;
import com.premiersolutionshi.old.model.SupportModel;
import com.premiersolutionshi.old.model.SystemModel;
import com.premiersolutionshi.old.model.TrainingModel;
import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Navigation logic for the application's SYSTEM process
 */
public final class SystemAction extends BaseOldAction {
    private static Logger logger = Logger.getLogger(SystemAction.class.getSimpleName());

    private Logger getLogger() {
        return logger;
    }

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     *
     * @param mapping
     *            The ActionMapping used to select this instance
     * @param actionForm
     *            The optional ActionForm bean for this request (if any)
     * @param request
     *            The HTTP request we are processing
     * @param response
     *            The HTTP response we are creating
     * @return The forward name of the jsp page to load into the browser
     * @throws IOException
     *             If an input/output error occurs
     * @throws ServletException
     *             If a servlet exception occurs
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        // HttpSession session = request.getSession();
        String action = CommonMethods.nes(request.getParameter("action")).trim();
        String returnAction = null;
        LoginBean loginBean = new LoginBean();
        SystemBean inputBean = (SystemBean) form;
        String operation = null;// , sortBy = null, sortDir = null;

        int projectPk = CommonMethods.cInt(request.getParameter("projectPk"));
        if (projectPk == -1)
            action = "errorExpiredSession";

        double startTime = System.currentTimeMillis();

        try (Connection conn = CommonMethods.getConn(servlet, "jdbc/pshi")) {

            loginBean = LoginModel.getUserInfo(conn, request);
            if (loginBean == null) {
                action = "errorExpiredSession";
            }
            String username = loginBean == null ? "GUEST" : loginBean.getUsername();

            getLogger().info(String.format("%7s%-32s | %-34s | %s", "", "SYSTEM NAVIGATION [START]", "Action: " + action,
                    "Username: " + username));

            switch (action) {
            case "hardwareSoftwareSummary":
                returnAction = "hardwareSoftwareSummary";
                break;

            case "configuredSystemList":
                switch (CommonMethods.nes(request.getParameter("operation"))) {
                case "addFailed":
                    request.setAttribute("errorMsg", "An error occurred while trying to insert the system");
                    break;
                case "deleteSuccess":
                    request.setAttribute("successMsg", "Successfully deleted " + CommonMethods.nes(request.getParameter("computerName")));
                    break;
                case "deleteFailed":
                    request.setAttribute("errorMsg", "An error occurred while trying to delete the system");
                    break;
                } // end of switch

                request.setAttribute("currFacetVersion", LookupModel.getCurrFacetVersion(conn, projectPk));
                request.setAttribute("currDmsVersion", SystemModel.getCurrDmsVersion());

                request.setAttribute("resultList", SystemModel.getConfiguredSystemList(conn));
                returnAction = "configuredSystemList";
                break;

            case "configuredSystemDetail":
                inputBean = SystemModel.getConfiguredSystemBean(conn, inputBean);
                switch (CommonMethods.nes(request.getParameter("operation"))) {
                case "addSuccess":
                    request.setAttribute("successMsg",
                            "Successfully inserted " + CommonMethods.nvl(inputBean.getShipName(), inputBean.getComputerName()));
                    break;
                case "editSuccess":
                    request.setAttribute("successMsg",
                            "Successfully updated " + CommonMethods.nvl(inputBean.getShipName(), inputBean.getComputerName()));
                    break;
                case "editFailed":
                    request.setAttribute("errorMsg", "An error occurred while trying to update the system");
                    break;
                } // end of switch

                ArrayList<SystemBean> currList = SystemModel.getConfiguredSystemPkList(conn);

                request.setAttribute("currStr",
                        SystemModel.getCurrRecordStr(currList, CommonMethods.cInt(inputBean.getConfiguredSystemPk())));
                request.setAttribute("prevBean", SystemModel.getPrevBean(currList, CommonMethods.cInt(inputBean.getConfiguredSystemPk())));
                request.setAttribute("nextBean", SystemModel.getNextBean(currList, CommonMethods.cInt(inputBean.getConfiguredSystemPk())));

                // Get Curr Versions
                request.setAttribute("currFacetVersion", LookupModel.getCurrFacetVersion(conn, projectPk));
                request.setAttribute("currDmsVersion", SystemModel.getCurrDmsVersion());

                if (!CommonMethods.isEmpty(inputBean.getUic())) {
                    request.setAttribute("backfileBean", BackfileModel.getWorkflowBean(conn, inputBean.getUic()));
                    request.setAttribute("trainingBean", TrainingModel.getWorkflowBean(conn, inputBean.getUic()));

                    // Ship pop-up data
                    request.setAttribute("shipBean", ShipModel.getShipBean(conn, inputBean.getUic()));
                    request.setAttribute("configuredSystemList", SystemModel.getConfiguredSystemListByUic(conn, inputBean.getUic()));
                    // request.setAttribute("shipTaskList",
                    // ProjectModel.getShipTaskList(conn,
                    // CommonMethods.cInt(projectPk), inputBean.getUic()));
                    request.setAttribute("shipPocList", ShipModel.getPocList(conn, inputBean.getUic()));
                    request.setAttribute("shipIssueList",
                            SupportModel.getIssueList(conn, CommonMethods.cInt(projectPk), inputBean.getUic(), "open"));
                    request.setAttribute("shipLastVisitBean", SupportModel.getLastVisitBean(conn, inputBean.getUic()));
                    request.setAttribute("shipUpcomingVisitList", SupportModel.getUpcomingVisitList(conn, inputBean.getUic()));
                } // end of if

                request.setAttribute("resultBean", inputBean);
                request.setAttribute("lastVisitBean", SupportModel.getLastVisitBean(conn, inputBean.getUic()));
                returnAction = "configuredSystemDetail";
                break;

            case "configuredSystemAdd":
            	String operationParam = request.getParameter("operation");
            	if (!StringUtils.isEmpty(operationParam) && operationParam.equals("addFailed")) {
            		request.setAttribute("errorMsg", "Edit failed. Please be sure to check if all required fields are filled.");
            	}
                request.setAttribute("laptopList", HardwareModel.getAvailableLaptopList(conn));
                request.setAttribute("kofaxLicenseList", SoftwareModel.getAvailableKofaxLicenseList(conn));
                request.setAttribute("scannerList", HardwareModel.getAvailableScannerList(conn));
                request.setAttribute("vrsLicenseList", SoftwareModel.getAvailableVrsLicenseList(conn));
                request.setAttribute("msOfficeLicenseList", SoftwareModel.getMsOfficeLicenseList(conn));
                request.setAttribute("facetVersionList", LookupModel.getFacetVersionList(conn, projectPk));
                request.setAttribute("kofaxProductNameList", SystemModel.getKofaxProductNameList(conn));
                request.setAttribute("ghostVersionList", SystemModel.getGhostVersionList(conn));
                request.setAttribute("accessVersionList", SystemModel.getAccessVersionList(conn));
                request.setAttribute("dmsVersionList", SystemModel.getDmsVersionList());
                request.setAttribute("shipList", ShipModel.getShipList(conn));
                request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));

                request.setAttribute("editType", "add");
                request.setAttribute("customPageTitle", "Add New FACET System");
                returnAction = "configuredSystemAdd";
                break;

            case "configuredSystemAddDo":

                if (inputBean != null && StringUtils.isEmpty(inputBean.getContractNumber())) {
                    getLogger().info(String.format("%9s%-30s | %s", "", "INFO", "Redirecting to configuredSystemList"));
                    response.sendRedirect("system.do?action=configuredSystemList&projectPk=" + projectPk + "&operation=addFailed");
                    returnAction = null;
                }
                else {
	                int newPk = SystemModel.insertConfiguredSystem(conn, inputBean, loginBean, CommonMethods.getUploadDir(request));
	                if (newPk > -1) {
	                    getLogger().info(String.format("%9s%-30s | %s", "", "INFO", "Redirecting to configuredSystemDetail"));
	                    response.sendRedirect("system.do?action=configuredSystemDetail&projectPk=" + projectPk
	                            + "&operation=addSuccess&configuredSystemPk=" + newPk);
	                    returnAction = null;
	                }
	                else {
	                    getLogger().info(String.format("%9s%-30s | %s", "", "INFO", "Redirecting to configuredSystemList"));
	                    response.sendRedirect("system.do?action=configuredSystemList&projectPk=" + projectPk + "&operation=addFailed");
	                    returnAction = null;
	                } // end of else
                }
                break;

            case "configuredSystemEdit":
            	String operationEditParam = request.getParameter("operation");
            	if (!StringUtils.isEmpty(operationEditParam) && operationEditParam.equals("editFailed")) {
            		request.setAttribute("errorMsg", "Edit failed. Please be sure to check if all required fields are filled.");
            	}
                request.setAttribute("systemBean", SystemModel.getConfiguredSystemBean(conn, inputBean));

                request.setAttribute("osVersionList", LookupModel.getOsVersionList(conn, projectPk));
                request.setAttribute("laptopList", HardwareModel.getAvailableLaptopList(conn, inputBean.getConfiguredSystemPk()));
                request.setAttribute("scannerList", HardwareModel.getAvailableScannerList(conn, inputBean.getConfiguredSystemPk()));
                request.setAttribute("kofaxLicenseList",
                        SoftwareModel.getAvailableKofaxLicenseList(conn, inputBean.getConfiguredSystemPk()));
                request.setAttribute("vrsLicenseList", SoftwareModel.getAvailableVrsLicenseList(conn, inputBean.getConfiguredSystemPk()));
                request.setAttribute("msOfficeLicenseList", SoftwareModel.getMsOfficeLicenseList(conn));
                request.setAttribute("facetVersionList", LookupModel.getFacetVersionList(conn, projectPk));
                request.setAttribute("kofaxProductNameList", SystemModel.getKofaxProductNameList(conn));
                request.setAttribute("ghostVersionList", SystemModel.getGhostVersionList(conn));
                request.setAttribute("accessVersionList", SystemModel.getAccessVersionList(conn));
                request.setAttribute("dmsVersionList", SystemModel.getDmsVersionList());
                request.setAttribute("shipList", ShipModel.getShipList(conn));
                request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));

                request.setAttribute("editType", "edit");
                request.setAttribute("customPageTitle", "Edit FACET System");
                returnAction = "configuredSystemEdit";
                break;

            case "configuredSystemEditDo":
                if (inputBean != null && StringUtils.isEmpty(inputBean.getContractNumber())) {
                    getLogger().info(String.format("%9s%-30s | %s", "", "INFO", "Redirecting to configuredSystemList"));
                    response.sendRedirect("system.do?action=configuredSystemEdit&configuredSystemPk=" + inputBean.getConfiguredSystemPk() + "&projectPk=" + projectPk + "&operation=editFailed");
                    operation = "editFailed";
                    returnAction = null;
                }
                else {
	                operation = SystemModel.updateConfiguredSystem(conn, inputBean, loginBean, CommonMethods.getUploadDir(request))
	                        ? "editSuccess"
	                        : "editFailed";
	                getLogger().info(String.format("%9s%-30s | %s", "", "INFO", "Redirecting to configuredSystemDetail"));
	                response.sendRedirect("system.do?action=configuredSystemDetail&projectPk=" + projectPk + "&operation=" + operation
	                        + "&configuredSystemPk=" + inputBean.getConfiguredSystemPk());
	                returnAction = null;
                }
                break;

            case "configuredSystemDeleteDo":
                inputBean = SystemModel.getConfiguredSystemBean(conn, inputBean);
                operation = SystemModel.deleteConfiguredSystem(conn, inputBean, CommonMethods.getUploadDir(request)) ? "deleteSuccess"
                        : "deleteFailed";
                getLogger().info(String.format("%9s%-30s | %s", "", "INFO", "Redirecting to configuredSystemList"));
                response.sendRedirect("system.do?action=configuredSystemList&projectPk=" + projectPk + "&operation=" + operation
                        + "&computerName=" + CommonMethods.urlClean(inputBean.getComputerName()));
                returnAction = null;
                break;

            case "systemVariables":
                switch (CommonMethods.nes(request.getParameter("operation"))) {
                case "issueCategoryAddSuccess":
                    request.setAttribute("alertSuccess", "Support Issue Category has been successfully added.");
                    break;
                case "issueCategoryAddFailed":
                    request.setAttribute("alertDanger", "An error occurred while trying to insert the Support Issue Category.");
                    break;
                case "issueCategoryDeleteSuccess":
                    request.setAttribute("alertSuccess", "Support Issue Category has been successfully deleted.");
                    break;
                case "issueCategoryDeleteFailed":
                    request.setAttribute("alertDanger", "Cannot delete category with existing support issues.");
                    break;

                case "locationAddSuccess":
                    request.setAttribute("alertSuccess", "Location has been successfully added.");
                    break;
                case "locationAddFailed":
                    request.setAttribute("alertDanger", "An error occurred while trying to insert the location.");
                    break;
                case "locationDeleteSuccess":
                    request.setAttribute("alertSuccess", "Location has been successfully deleted.");
                    break;
                case "locationDeleteFailed":
                    request.setAttribute("alertDanger", "An error occurred while trying to delete the location.");
                    break;

                case "laptopIssueAddSuccess":
                    request.setAttribute("alertSuccess", "Laptop Issue has been successfully added.");
                    break;
                case "laptopIssueAddFailed":
                    request.setAttribute("alertDanger", "An error occurred while trying to insert the Laptop Issue.");
                    break;
                case "laptopIssueDeleteSuccess":
                    request.setAttribute("alertSuccess", "Laptop Issue has been successfully deleted.");
                    break;
                case "laptopIssueDeleteFailed":
                    request.setAttribute("alertDanger", "An error occurred while trying to delete the Laptop Issue.");
                    break;

                case "scannerIssueAddSuccess":
                    request.setAttribute("alertSuccess", "Scanner Issue has been successfully added.");
                    break;
                case "scannerIssueAddFailed":
                    request.setAttribute("alertDanger", "An error occurred while trying to insert the Scanner Issue.");
                    break;
                case "scannerIssueDeleteSuccess":
                    request.setAttribute("alertSuccess", "Scanner Issue has been successfully deleted.");
                    break;
                case "scannerIssueDeleteFailed":
                    request.setAttribute("alertDanger", "An error occurred while trying to delete the Scanner Issue.");
                    break;

                case "softwareIssueAddSuccess":
                    request.setAttribute("alertSuccess", "Software Issue has been successfully added.");
                    break;
                case "softwareIssueAddFailed":
                    request.setAttribute("alertDanger", "An error occurred while trying to insert the Software Issue.");
                    break;
                case "softwareIssueDeleteSuccess":
                    request.setAttribute("alertSuccess", "Software Issue has been successfully deleted.");
                    break;
                case "softwareIssueDeleteFailed":
                    request.setAttribute("alertDanger", "An error occurred while trying to delete the Software Issue.");
                    break;

                case "facetVersionAddSuccess":
                    request.setAttribute("alertSuccess", "Facet Version has been successfully added.");
                    break;
                case "facetVersionAddFailed":
                    request.setAttribute("alertDanger", "An error occurred while trying to insert the Facet Version.");
                    break;
                case "facetVersionDeleteSuccess":
                    request.setAttribute("alertSuccess", "Facet Version has been successfully deleted.");
                    break;
                case "facetVersionDeleteFailed":
                    request.setAttribute("alertDanger", "An error occurred while trying to delete the Facet Version.");
                    break;

                case "facetVersionSetSuccess":
                    request.setAttribute("alertSuccess", "Current Facet Version has been successfully set.");
                    break;
                case "facetVersionSetFailed":
                    request.setAttribute("alertDanger", "An error occurred while trying to set the Current Facet Version.");
                    break;
                } // end of switch

                request.setAttribute("managedLists", ManagedList.getNotHidden());
                request.setAttribute("issueCategoryList", LookupModel.getCategoryList(conn, projectPk));
                request.setAttribute("locationList", LookupModel.getLocationList(conn, projectPk));
                request.setAttribute("laptopIssueList", LookupModel.getLaptopIssueList(conn, projectPk));
                request.setAttribute("scannerIssueList", LookupModel.getScannerIssueList(conn, projectPk));
                request.setAttribute("softwareIssueList", LookupModel.getSoftwareIssueList(conn, projectPk));
                request.setAttribute("facetVersionList", LookupModel.getFacetVersionList(conn, projectPk));
                request.setAttribute("currFacetVersion", LookupModel.getCurrFacetVersion(conn, projectPk));

                request.setAttribute("operation", request.getParameter("operation"));

                returnAction = "systemVariables";
                break;

            case "checkListStep1Doc":
                request.setAttribute("resultBean", SystemModel.getConfiguredSystemBean(conn, inputBean));
                returnAction = "checkListStep1Doc";
                break;

            case "checkListStep2Doc":
                request.setAttribute("resultBean", SystemModel.getConfiguredSystemBean(conn, inputBean));
                returnAction = "checkListStep2Doc";
                break;

            case "zbookCheckListStep1Doc":
                request.setAttribute("resultBean", SystemModel.getConfiguredSystemBean(conn, inputBean));
                returnAction = "zbookCheckListStep1Doc";
                break;

            case "zbookCheckListStep2Doc":
                request.setAttribute("resultBean", SystemModel.getConfiguredSystemBean(conn, inputBean));
                returnAction = "zbookCheckListStep2Doc";
                break;

            case "vesselDataSheetDoc":
                inputBean = SystemModel.getConfiguredSystemBean(conn, inputBean);
                request.setAttribute("systemBean", inputBean);
                request.setAttribute("backfileBean", BackfileModel.getWorkflowBean(conn, inputBean.getUic()));
                request.setAttribute("trainingBean", TrainingModel.getWorkflowBean(conn, inputBean.getUic()));
                returnAction = "vesselDataSheetDoc";
                break;

            case "errorExpiredSession":
                returnAction = "errorExpiredSession";
                break;

            default:
                returnAction = "error404";
                break;
            } // end of switch

            if (!CommonMethods.isEmpty(returnAction))
                CommonMethods.appFinally(conn, returnAction, loginBean, projectPk, request);
        }
        catch (SQLException e) {
            getLogger().error(String.format("%9s%-30s | %s", "", "ERROR", e));
            returnAction = "errorDatabaseDown";
        }
        catch (Exception e) {
            getLogger().error(String.format("%9s%-30s | %s", "", "ERROR", e));
            request.setAttribute("errorMsg", e.toString());
            returnAction = "errorUnexpected";
        } // end of catch

        double endTime = System.currentTimeMillis();
        String elapsedTime = nf1.format((endTime - startTime) / (double) 1000);
        String username = loginBean == null ? "GUEST" : loginBean.getUsername();
        getLogger().info(String.format("%7s%-32s | %-34s | %-52s | %s\r\n", "", "SYSTEM NAVIGATION [END]", "Return: " + returnAction,
                "Username: " + CommonMethods.nvl(username, "GUEST"), "Elapsed Time: " + elapsedTime + " sec"));

        return !CommonMethods.isEmpty(returnAction) ? mapping.findForward(returnAction) : null;
    }
}
