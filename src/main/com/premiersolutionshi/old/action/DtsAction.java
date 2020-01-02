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

import com.premiersolutionshi.old.bean.DtsBean;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.model.DtsModel;
import com.premiersolutionshi.old.model.LoginModel;
import com.premiersolutionshi.old.model.ReportModel;
import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Navigation logic for the application's DTS process
 */
public final class DtsAction extends Action {
    private static Logger logger = Logger.getLogger(DtsAction.class.getSimpleName());
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
        DtsBean inputBean = (DtsBean) form;
        String operation = null;//, sortBy = null, sortDir = null;

        int projectPk = CommonMethods.cInt(request.getParameter("projectPk"));
        if (projectPk == -1) action = "errorExpiredSession";

        double startTime = System.currentTimeMillis();

        try (Connection conn = CommonMethods.getConn(servlet, "jdbc/pshi")) {
            loginBean = LoginModel.getUserInfo(conn, request);

            logger.info(String.format("%10s%-32s | %-34s | %s", "", "DTS NAVIGATION [START]", "Action: " + action, "Username: " + loginBean.getUsername()));

            /********************************** PREPARE ACTION **********************************/
            switch (action) {
                case "dtsUpload":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) { //FACET project
                        returnAction = "errorNoRole";
                    } else {
                        switch (CommonMethods.nes(request.getParameter("operation"))) {
                            case "uploadSuccess" : request.setAttribute("successMsg", "Successfully uploaded " + CommonMethods.nes(request.getParameter("filename")));break;
                            case "uploadFailed"     : request.setAttribute("errorMsg", "An error occurred while trying to upload " + CommonMethods.nes(request.getParameter("filename")));break;
                        } //end of switch

                        request.setAttribute("summaryBean", ReportModel.getLatestSummaryBean(conn));
                        request.setAttribute("differenceBean", ReportModel.getDifferenceBean(conn));
                        request.setAttribute("differenceList", ReportModel.getDifferenceList(conn));
                        request.setAttribute("facetDiscrepancyList", ReportModel.getFacetDiscrepancyList(conn, projectPk));
                        request.setAttribute("facetUnknownList", ReportModel.getFacetUnknownList(conn));

                        returnAction = "dtsUpload";
                    } //end of else
                    break;

                case "dtsUploadDo":
                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) { //FACET project
                        returnAction = "errorNoRole";
                    } else {
                        operation = DtsModel.processLogcopZip(conn, inputBean, loginBean) ? "uploadSuccess" : "uploadFailed";
                        String filename = inputBean.getFile() != null ? inputBean.getFile().getFileName() : "";
                        logger.info(String.format("%10s%-30s | %s", "", "INFO", "Redirecting to dtsUpload"));
                        response.sendRedirect("dts.do?action=dtsUpload&projectPk=" + projectPk + "&operation=" + operation + "&filename=" + CommonMethods.urlClean(filename));
                    } //end of else
                    break;

                default:
                    returnAction = "error404";
                    break;
            } // end of switch

            if (!CommonMethods.isEmpty(returnAction)) CommonMethods.appFinally(conn, returnAction, loginBean, projectPk, request);
        } catch (SQLException e) {
            logger.error(String.format("%12s%-30s | %s", "", "ERROR", e));
            returnAction = "errorDatabaseDown";
        } catch (Exception e) {
            logger.error(String.format("%12s%-30s | %s", "", "ERROR", e));
            request.setAttribute("errorMsg", e.toString());
            returnAction = "errorUnexpected";
        } // end of catch

        double endTime = System.currentTimeMillis();
        String elapsedTime = nf1.format((endTime - startTime) / (double) 1000);
        logger.info(String.format("%10s%-32s | %-34s | %-52s | %s\r\n", "", "DTS NAVIGATION [END]", "Return: " + returnAction, "Username: " + CommonMethods.nvl(loginBean.getUsername(), "GUEST"), "Elapsed Time: " + elapsedTime + " sec"));

        return !CommonMethods.isEmpty(returnAction) ? mapping.findForward(returnAction) : null;
    } // end of execute
} // end of class
