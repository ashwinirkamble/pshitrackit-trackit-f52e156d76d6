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
import com.premiersolutionshi.old.model.LookupModel;
import com.premiersolutionshi.old.util.CommonMethods;

import com.premiersolutionshi.old.bean.UserBean;


/**
 * Navigation logic for the application's MENU process
 */
public final class MenuAction extends Action {
    private static Logger logger = Logger.getLogger(MenuAction.class.getSimpleName());
    private NumberFormat nf1 = new DecimalFormat("0.0");

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
        // LookupBean inputBean = (LookupBean) form;
        String action = CommonMethods.nvl(request.getParameter("action"), "index").trim();
        String returnAction = null;
        LoginBean loginBean = new LoginBean();
        UserBean userBean=new UserBean();
        String username = null;
        request.setAttribute("contentHeader_projectName", "");
        double startTime = System.currentTimeMillis();
        try (Connection conn = CommonMethods.getConn(servlet, "jdbc/pshi")) {
            loginBean = LoginModel.getUserInfo(conn, request);
            username = loginBean == null ? null : loginBean.getUsername();
            logger.info(String.format("%9s%-32s | %-34s | %s", "", "MENU NAVIGATION [START]", "Action: " + action,
                    "Username: " + username));
            switch (action) {
                case "index":
                    returnAction = showIndex(request);
                    break;
                case "reports":
                    returnAction = "reports";
                    break;
                case "gitLog":
                    request.setAttribute("resultList", LookupModel.getGitLog());
                    returnAction = "gitLog";
                    break;
                case "gitShow":
                    returnAction = showGitShow(request);
                    break;
                    
              
                default:
                    returnAction = "error404";
                    break;
            }
            if (!CommonMethods.isEmpty(returnAction))
                CommonMethods.appFinally(conn, returnAction, loginBean, -1, request);
        }
        catch (SQLException e) {
            logger.error(String.format("%11s%-30s | %s", "", "ERROR", e));
            returnAction = "errorDatabaseDown";
        }
        catch (Exception e) {
            logger.error(String.format("%11s%-30s | %s", "", "ERROR", e));
            request.setAttribute("errorMsg", e.toString());
            returnAction = "errorUnexpected";
        }

        double endTime = System.currentTimeMillis();
        String elapsedTime = nf1.format((endTime - startTime) / (double) 1000);
        logger.info(String.format("%9s%-32s | %-34s | %-52s | %s\r\n", "", "MENU NAVIGATION [END]", "Return: " + returnAction,
                "Username: " + CommonMethods.nvl(username, "GUEST"), "Elapsed Time: " + elapsedTime + " sec"));

        return !CommonMethods.isEmpty(returnAction) ? mapping.findForward(returnAction) : null;
    }

    private String showIndex(HttpServletRequest request) {
        String returnAction;
        switch (CommonMethods.nes(request.getParameter("operation"))) {
        case "updateSuccess":
            request.setAttribute("successMsg", "Successfully changed password");
            break;
        case "updateFailed":
            request.setAttribute("errorMsg", "An error occurred while trying to change the password");
            break;
        }

        request.setAttribute("tomcatVersion", request.getSession().getServletContext().getServerInfo());
        String javaRuntimeVer = System.getProperty("java.runtime.version");
        request.setAttribute("javaVersion", javaRuntimeVer + " (" + System.getProperty("sun.arch.data.model") + "-bit)");

        returnAction = "index";
        return returnAction;
    }

    private String showGitShow(HttpServletRequest request) throws Exception {
        String returnAction;
        String id = CommonMethods.nes(request.getParameter("id"));
        request.setAttribute("id", id);
        request.setAttribute("pageName", "Commit Log <" + id + ">");
        request.setAttribute("resultList", LookupModel.getGitFile(id));
        returnAction = "gitShow";
        return returnAction;
    }
}
