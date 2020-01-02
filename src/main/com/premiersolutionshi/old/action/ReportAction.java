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
import com.premiersolutionshi.old.bean.ReportBean;
import com.premiersolutionshi.old.model.LoginModel;
import com.premiersolutionshi.old.model.ReportModel;
import com.premiersolutionshi.old.model.SystemModel;
import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Navigation logic for the application's REPORT process
 */
public final class ReportAction extends Action {
    private static Logger logger = Logger.getLogger(ReportAction.class.getSimpleName());
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
        ReportBean inputBean = (ReportBean) form;
        String operation = null;//, sortBy = null, sortDir = null;

        int projectPk = CommonMethods.cInt(request.getParameter("projectPk"));

        double startTime = System.currentTimeMillis();

        try (Connection conn = CommonMethods.getConn(servlet, "jdbc/pshi")) {
            loginBean = LoginModel.getUserInfo(conn, request);

            logger.info(String.format("%7s%-32s | %-34s | %s", "", "REPORT NAVIGATION [START]", "Action: " + action, "Username: " + loginBean.getUsername()));

            /********************************** PREPARE ACTION **********************************/
            switch (action) {
                case "missingTransmittal":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) { //FACET project
                        returnAction = "errorNoRole";
                    } else {
                        switch (CommonMethods.nes(request.getParameter("operation"))) {
                            case "saveSuccess" : request.setAttribute("successMsg", "Successfully saved exception list");break;
                            case "saveFailed"     : request.setAttribute("errorMsg", "An error occurred while trying to save the exception list");break;
                        }
                        request.setAttribute("resultList", ReportModel.getMissingTransmittalList(conn));
                        returnAction = "missingTransmittal";
                    }
                    break;

                case "exceptionListDo":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) { //FACET project
                        returnAction = "errorNoRole";
                    } else {
                        operation = ReportModel.saveExceptionList(conn, inputBean, loginBean) ? "saveSuccess" : "saveFailed";
                        logger.info(String.format("%10s%-30s | %s", "", "INFO", "Redirecting to missingTransmittal"));
                        response.sendRedirect("report.do?action=missingTransmittal&projectPk=" + projectPk + "&operation=" + operation);
                    }
                    break;

                case "transmittalSummary":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) { //FACET project
                        returnAction = "errorNoRole";
                    } else {
                        request.setAttribute("resultList", ReportModel.getTransmittalSummaryList(conn));
                        returnAction = "transmittalSummary";
                    }
                    break;

                case "transmittalDetail":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) { //FACET project
                        returnAction = "errorNoRole";
                    } else if (CommonMethods.isEmpty(inputBean.getFacetName())) {
                        returnAction = "error404";
                    } else {
                        request.setAttribute("chartList", ReportModel.getDocCntByMonthList(conn, inputBean.getFacetName()));
                        request.setAttribute("resultList", ReportModel.getTranmittalDetailList(conn, inputBean.getFacetName()));
                        request.setAttribute("configuredSystemList", SystemModel.getConfiguredSystemList(conn));
                        request.setAttribute("inputBean", inputBean);
                        request.setAttribute("customPageTitle", "Transmittal Details for " + inputBean.getFacetName());
                        returnAction = "transmittalDetail";
                    }
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
        logger.info(String.format("%7s%-32s | %-34s | %-52s | %s\r\n", "", "REPORT NAVIGATION [END]", "Return: " + returnAction, "Username: " + CommonMethods.nvl(loginBean.getUsername(), "GUEST"), "Elapsed Time: " + elapsedTime + " sec"));

        return !CommonMethods.isEmpty(returnAction) ? mapping.findForward(returnAction) : null;
    }
}
