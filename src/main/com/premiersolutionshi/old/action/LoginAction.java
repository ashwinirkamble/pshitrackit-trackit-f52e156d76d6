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
import com.premiersolutionshi.old.model.LoginModel;
import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Navigation logic for the application's LOGIN process
 */
public final class LoginAction extends Action {
    private static Logger logger = Logger.getLogger(LoginAction.class.getSimpleName());
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
        String action = CommonMethods.nes(request.getParameter("action")).trim();
        String returnAction = null;
        LoginBean loginBean = new LoginBean();
        LoginBean inputBean = (LoginBean)form;
        String operation = null;

        double startTime = System.currentTimeMillis();

        try (Connection conn = CommonMethods.getConn(servlet, "jdbc/pshi")) {
            loginBean = LoginModel.getUserInfo(conn, request);
            //HttpSession session = request.getSession();

            logger.info(String.format("%22s%-32s | %-34s | %s", "", "LOGIN NAVIGATION [START]", "Action: " + action, "Username: " + loginBean.getUsername()));

            /********************************** PREPARE ACTION **********************************/
            switch (action) {
                case "changePassword":
                    returnAction = "changePassword";
                    break;

                case "changePasswordDo":
                    operation = LoginModel.changePassword(conn, inputBean, loginBean) ? "updateSuccess" : "updateFailed";
                    logger.info(String.format("%24s%-30s | %s", "", "INFO", "Redirecting to index"));
                    response.sendRedirect("menu.do?action=index&operation=" + operation);
                    returnAction = null;
                    break;

                    //case "login":
                    //    String passwordHash = DigestUtils.sha256Hex(inputBean.getPassword());
                    //    break;

                default:
                    returnAction = "error404";
                    break;
            }

            if (!CommonMethods.isEmpty(returnAction)) {
                CommonMethods.appFinally(conn, returnAction, loginBean, -1, request);
            }
        } catch (SQLException e) {
            logger.error(String.format("%24s%-30s | %s", "", "ERROR", e));
            returnAction = "errorDatabaseDown";
        } catch (Exception e) {
            logger.error(String.format("%24s%-30s | %s", "", "ERROR", e));
            request.setAttribute("errorMsg", e.toString());
            returnAction = "errorUnexpected";
        } 

        double endTime = System.currentTimeMillis();
        String elapsedTime = nf1.format((endTime - startTime) / (double) 1000);
        logger.info(String.format("%22s%-32s | %-34s | %-52s | %s\r\n", "", "LOGIN NAVIGATION [END]", "Return: " + returnAction, "Username: " + CommonMethods.nvl(loginBean.getUsername(), "GUEST"), "Elapsed Time: " + elapsedTime + " sec"));

        return !CommonMethods.isEmpty(returnAction) ? mapping.findForward(returnAction) : null;
    }
}
