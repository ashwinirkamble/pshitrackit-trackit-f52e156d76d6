package com.premiersolutionshi.old.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.bean.LookupBean;
import com.premiersolutionshi.old.model.LoginModel;
import com.premiersolutionshi.old.model.LookupModel;
import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Navigation logic for the application's LOOKUP process
 */
public final class LookupAction extends Action {
    private static Logger logger = Logger.getLogger(LookupAction.class.getSimpleName());
    private NumberFormat nf1 = new DecimalFormat("0.0");

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
        //HttpSession session = request.getSession();
        String action = CommonMethods.nes(request.getParameter("action")).trim();
        String returnAction = null;
        LoginBean loginBean = new LoginBean();
        LookupBean inputBean = (LookupBean)form;
        String operation = null;

        int projectPk = CommonMethods.cInt(request.getParameter("projectPk"));
        if (projectPk == -1) action = "errorExpiredSession";

        double startTime = System.currentTimeMillis();

        try (Connection conn = CommonMethods.getConn(servlet, "jdbc/pshi")) {
            loginBean = LoginModel.getUserInfo(conn, request);

            logger.info(String.format("%7s%-32s | %-34s | %s", "", "LOOKUP NAVIGATION [START]", "Action: " + action, "Username: " + loginBean.getUsername()));

            /********************************** PREPARE ACTION **********************************/
            switch (action) {
                case "issueCategoryAddDo":
                    logger.info(String.format("%9s%-30s | %s", "", "INFO", "Redirecting to systemVariables"));
                    operation = LookupModel.insertIssueCategory(conn, inputBean.getValue(), projectPk, loginBean) ? "issueCategoryAddSuccess" : "issueCategoryAddFailed";
                    response.sendRedirect("system.do?action=systemVariables&projectPk=" + projectPk + "&operation=" + operation);
                    returnAction = null;
                    break;

                case "issueCategoryDeleteDo":
                    logger.info(String.format("%9s%-30s | %s", "", "INFO", "Redirecting to systemVariables"));
                    operation = LookupModel.deleteIssueCategory(conn, CommonMethods.cInt(inputBean.getKey())) ? "issueCategoryDeleteSuccess" : "issueCategoryDeleteFailed";
                    response.sendRedirect("system.do?action=systemVariables&projectPk=" + projectPk + "&operation=" + operation);
                    returnAction = null;
                    break;

//                case "locationAddDo":
//                    logger.info(String.format("%9s%-30s | %s", "", "INFO", "Redirecting to systemVariables"));
//                    operation = LookupModel.insertLocation(conn, inputBean.getValue(), projectPk, loginBean) ? "locationAddSuccess" : "locationAddFailed";
//                    response.sendRedirect("system.do?action=systemVariables&projectPk=" + projectPk + "&operation=" + operation);
//                    returnAction = null;
//                    break;
//
//                case "locationDeleteDo":
//                    logger.info(String.format("%9s%-30s | %s", "", "INFO", "Redirecting to systemVariables"));
//                    operation = LookupModel.deleteLocation(conn, CommonMethods.cInt(inputBean.getKey())) ? "locationDeleteSuccess" : "locationDeleteFailed";
//                    response.sendRedirect("system.do?action=systemVariables&projectPk=" + projectPk + "&operation=" + operation);
//                    returnAction = null;
//                    break;
//
//                case "laptopIssueAddDo":
//                    logger.info(String.format("%9s%-30s | %s", "", "INFO", "Redirecting to systemVariables"));
//                    operation = LookupModel.insertLaptopIssue(conn, inputBean.getValue(), projectPk, loginBean) ? "laptopIssueAddSuccess" : "laptopIssueAddFailed";
//                    response.sendRedirect("system.do?action=systemVariables&projectPk=" + projectPk + "&operation=" + operation);
//                    returnAction = null;
//                    break;
//
//                case "laptopIssueDeleteDo":
//                    logger.info(String.format("%9s%-30s | %s", "", "INFO", "Redirecting to systemVariables"));
//                    operation = LookupModel.deleteLaptopIssue(conn, CommonMethods.cInt(inputBean.getKey())) ? "laptopIssueDeleteSuccess" : "laptopIssueDeleteFailed";
//                    response.sendRedirect("system.do?action=systemVariables&projectPk=" + projectPk + "&operation=" + operation);
//                    returnAction = null;
//                    break;
//
//                case "scannerIssueAddDo":
//                    logger.info(String.format("%9s%-30s | %s", "", "INFO", "Redirecting to systemVariables"));
//                    operation = LookupModel.insertScannerIssue(conn, inputBean.getValue(), projectPk, loginBean) ? "scannerIssueAddSuccess" : "scannerIssueAddFailed";
//                    response.sendRedirect("system.do?action=systemVariables&projectPk=" + projectPk + "&operation=" + operation);
//                    returnAction = null;
//                    break;
//
//                case "scannerIssueDeleteDo":
//                    logger.info(String.format("%9s%-30s | %s", "", "INFO", "Redirecting to systemVariables"));
//                    operation = LookupModel.deleteScannerIssue(conn, CommonMethods.cInt(inputBean.getKey())) ? "scannerIssueDeleteSuccess" : "scannerIssueDeleteFailed";
//                    response.sendRedirect("system.do?action=systemVariables&projectPk=" + projectPk + "&operation=" + operation);
//                    returnAction = null;
//                    break;
//
//                case "softwareIssueAddDo":
//                    logger.info(String.format("%9s%-30s | %s", "", "INFO", "Redirecting to systemVariables"));
//                    operation = LookupModel.insertSoftwareIssue(conn, inputBean.getValue(), projectPk, loginBean) ? "softwareIssueAddSuccess" : "softwareIssueAddFailed";
//                    response.sendRedirect("system.do?action=systemVariables&projectPk=" + projectPk + "&operation=" + operation);
//                    returnAction = null;
//                    break;
//
//                case "softwareIssueDeleteDo":
//                    logger.info(String.format("%9s%-30s | %s", "", "INFO", "Redirecting to systemVariables"));
//                    operation = LookupModel.deleteSoftwareIssue(conn, CommonMethods.cInt(inputBean.getKey())) ? "softwareIssueDeleteSuccess" : "softwareIssueDeleteFailed";
//                    response.sendRedirect("system.do?action=systemVariables&projectPk=" + projectPk + "&operation=" + operation);
//                    returnAction = null;
//                    break;
//
//                case "supportTeamAddDo":
//                    logger.info(String.format("%9s%-30s | %s", "", "INFO", "Redirecting to systemVariables"));
//                    operation = LookupModel.insertSupportTeam(conn, inputBean.getValue(), projectPk, loginBean) ? "supportTeamAddSuccess" : "supportTeamAddFailed";
//                    response.sendRedirect("system.do?action=systemVariables&projectPk=" + projectPk + "&operation=" + operation);
//                    returnAction = null;
//                    break;
//                    
//                case "supportTeamDeleteDo":
//                    logger.info(String.format("%9s%-30s | %s", "", "INFO", "Redirecting to systemVariables"));
//                    operation = LookupModel.deleteSupportTeam(conn, CommonMethods.cInt(inputBean.getKey())) ? "supportTeamDeleteSuccess" : "supportTeamDeleteFailed";
//                    response.sendRedirect("system.do?action=systemVariables&projectPk=" + projectPk + "&operation=" + operation);
//                    returnAction = null;
//                    break;
//
//                case "facetVersionAddDo":
//                    logger.info(String.format("%9s%-30s | %s", "", "INFO", "Redirecting to systemVariables"));
//                    operation = LookupModel.insertFacetVersion(conn, inputBean.getValue(), projectPk, loginBean) ? "facetVersionAddSuccess" : "facetVersionAddFailed";
//                    response.sendRedirect("system.do?action=systemVariables&projectPk=" + projectPk + "&operation=" + operation);
//                    returnAction = null;
//                    break;
//
//                case "facetVersionSetDo":
//                    logger.info(String.format("%9s%-30s | %s", "", "INFO", "Redirecting to systemVariables"));
//                    operation = LookupModel.setCurrFacetVersion(conn, inputBean.getKey(), projectPk, loginBean) ? "facetVersionSetSuccess" : "facetVersionSetFailed";
//                    response.sendRedirect("system.do?action=systemVariables&projectPk=" + projectPk + "&operation=" + operation);
//                    returnAction = null;
//                    break;
//
//                case "facetVersionDeleteDo":
//                    logger.info(String.format("%9s%-30s | %s", "", "INFO", "Redirecting to systemVariables"));
//                    operation = LookupModel.deleteFacetVersion(conn, CommonMethods.cInt(inputBean.getKey())) ? "facetVersionDeleteSuccess" : "facetVersionDeleteFailed";
//                    response.sendRedirect("system.do?action=systemVariables&projectPk=" + projectPk + "&operation=" + operation);
//                    returnAction = null;
//                    break;

                case "errorExpiredSession":
                    returnAction = "errorExpiredSession";
                    break;

                default:
                    returnAction = "error404";
                    break;
            }

            if (!CommonMethods.isEmpty(returnAction)) CommonMethods.appFinally(conn, returnAction, loginBean, projectPk, request);
        } catch (SQLException e) {
            logger.error(String.format("%9s%-30s | %s", "", "ERROR", e));
            returnAction = "errorDatabaseDown";
        } catch (Exception e) {
            logger.error(String.format("%9s%-30s | %s", "", "ERROR", e));
            request.setAttribute("errorMsg", e.toString());
            returnAction = "errorUnexpected";
        }

        double endTime = System.currentTimeMillis();
        String elapsedTime = nf1.format((endTime - startTime) / (double) 1000);
        logger.info(String.format("%7s%-32s | %-34s | %-52s | %s\r\n", "", "LOOKUP NAVIGATION [END]", "Return: " + returnAction, "Username: " + CommonMethods.nvl(loginBean.getUsername(), "GUEST"), "Elapsed Time: " + elapsedTime + " sec"));

        return !CommonMethods.isEmpty(returnAction) ? mapping.findForward(returnAction) : null;
    }
}
