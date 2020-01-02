package com.premiersolutionshi.common.ui.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.premiersolutionshi.common.constant.CommonMessage;
import com.premiersolutionshi.common.constant.ManagedList;
import com.premiersolutionshi.common.constant.MessageType;
import com.premiersolutionshi.common.domain.Project;
import com.premiersolutionshi.common.domain.User;
import com.premiersolutionshi.common.service.ManagedListItemService;
import com.premiersolutionshi.common.service.ProjectService;
import com.premiersolutionshi.common.service.UserService;
import com.premiersolutionshi.common.ui.form.BaseForm;
import com.premiersolutionshi.common.util.ConfigUtils;
import com.premiersolutionshi.common.util.DateUtils;
import com.premiersolutionshi.common.util.SqlUtils;
import com.premiersolutionshi.common.util.StringUtils;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.bean.ProjectBean;
import com.premiersolutionshi.old.model.LoginModel;
import com.premiersolutionshi.old.model.ProjectModel;
import com.premiersolutionshi.old.model.UserModel;
import com.premiersolutionshi.old.util.CommonMethods;

public abstract class BaseAction extends Action {
    private static Logger logger = Logger.getLogger(BaseAction.class.getSimpleName());
    protected static final String FORWARD_INDEX = "index";
    protected static final String FORWARD_SEARCH = "search";
    protected static final String FORWARD_LIST = "list";
    protected static final String FORWARD_VIEW = "view";
    protected static final String FORWARD_ADD = "add";
    protected static final String FORWARD_EDIT = "edit";
    protected static final String FORWARD_FORM = "form";
    protected static final String FORWARD_SAVE = "save";
    protected static final String FORWARD_EXPORT_TO_EXCEL = "exportToExcel";
    protected static final String FORWARD_DELETE = "delete";
    protected static final String FORWARD_ERROR = "error";
    protected static final String FORWARD_COPY = "copy";

    private Integer projectPk;
    private HttpSession httpSession;
    private HttpServletRequest request;
    private String action;
    private Boolean devEnv;
    private LoginBean loginBean;
    private ProjectBean projectBean;
    private Project project;
   

    private UserService userService;
    private ProjectService projectService;
    private ManagedListItemService managedListItemService;

    public BaseAction(Class<?> clazz) {
        logger = Logger.getLogger(clazz.getSimpleName());
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        this.request = request;
        setAction(request.getParameter("action"));
        String returnAction = getAction();
        handleMessages(request);
        boolean commitChanges = true;
        project = null;
        projectPk = null;

        SqlSession sqlSession = SqlUtils.getMybatisSession();
        if (sqlSession == null) {
            commitChanges = false;
            returnAction = "errorUnexpected";
        }
        else {
            initializeServices(request, sqlSession);
            BaseForm baseForm = (BaseForm) form;
            if (baseForm != null) {
                setAction(baseForm.getAction());
            }
            User currentUser = getCurrentUser();
            setProjectPk(request.getParameter("projectPk"));

            Connection conn = null;
            try {
                conn = CommonMethods.getConn(servlet);
                setLoginBean(LoginModel.getUserInfo(conn, request));

                //this is for user's name up on the top-right
                request.setAttribute("loginBean", getLoginBean());
                request.setAttribute("currentUser", currentUser);
                request.setAttribute("isDevEnv", isDevEnv());
                request.setAttribute("action", getAction());
                request.setAttribute("buildNumber", ConfigUtils.getBuildNumber());

                //this is on every page.
                request.setAttribute("isSysadmin", request.isUserInRole("sysadmin"));
                request.setAttribute("leftbar_projectList", ProjectModel.getProjectList(conn, loginBean));
                request.setAttribute("path", path());
                
                /*Ashwini 27/11 */
                request.setAttribute("currentPto_travelList", UserModel.getCurrentPtoTravelList(conn, loginBean,request));

                returnAction = run(conn, request, response, form);

                Integer projectPk = getProjectPk();
                if (projectPk != null) {
                    setProject(projectService.getById(projectPk));
                    projectBean = ProjectModel.getProjectBean(conn, projectPk);
                    request.setAttribute("currDmsVersion", getCurrDmsVersion());
                    request.setAttribute("currFacetVersion", getCurrFacetVersion());
                    request.setAttribute("currOsVersion", getCurrOsVersion());
                }
                request.setAttribute("projectSelectList", projectService.getByUser(currentUser));
                request.setAttribute("projectPk", projectPk);
                request.setAttribute("project", getProject());
            }
            catch (SQLException e) {
                String errorMsg = String.format("%8s%-30s | %s", "", "ERROR", e);
                logError(errorMsg, e);
                returnAction = "errorDatabaseDown";
                commitChanges = false;
            }
            catch (Exception e) {
                String errorMsg = String.format("%8s%-30s | %s", "", "ERROR", e);
                logError(errorMsg, e);
                request.setAttribute("errorMsg", e.toString());
                returnAction = "errorUnexpected";
                commitChanges = false;
            }
            finally {
                closeSession(commitChanges, conn, sqlSession);
            }
        }
        return !CommonMethods.isEmpty(returnAction) ? mapping.findForward(returnAction) : null;
    }

    private void closeSession(boolean commitChanges, Connection conn, SqlSession sqlSession) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            }
            catch (SQLException e) {
            }
        }
        if (sqlSession != null) {
            if (!commitChanges) {
                sqlSession.rollback();
            }
            sqlSession.commit();
            sqlSession.close(); 
        }
    }

    protected void initializeServices(HttpServletRequest request, SqlSession sqlSession) {
        this.request = request;
        if (sqlSession != null) {
            userService = new UserService(sqlSession);
            userService.setHttpServletRequest(request);
            projectService = new ProjectService(sqlSession, userService);
            managedListItemService = new ManagedListItemService(sqlSession, userService);
        }
    }

    /**
     * This should return the "forwarder".
     * @param conn
     * @param request
     * @param response
     * @param form
     * @return Name of forwarder.
     * @throws IOException
     */
    protected abstract String run(Connection conn, HttpServletRequest request, HttpServletResponse response, ActionForm form)
        throws IOException;

    /**
     * The URL path to this set of pages. "i.e. atoUpdate.do"
     * 
     * @return The URL path.
     */
    protected abstract String path();

    protected void handleMessages(HttpServletRequest request) {
        //String noun = " item" + (count > 1 ? "s" : "");
        String msg = request.getParameter("msg");
        String additional = request.getParameter("additional");
        Integer id = StringUtils.parseInt(request.getParameter("id"));
        if (StringUtils.isEmpty(msg)) {
            return;
        }
        CommonMessage commonMessage = CommonMessage.getByKey(msg);
        if (id == null || id <= 0) {
            setMessage(commonMessage, additional);
        }
        else {
            setMessage(commonMessage.getMessage(additional, id), commonMessage.getType());
        }
    }

    protected User getCurrentUser() {
        return userService == null ? null : userService.getCurrentUser();
    }

    protected void setMessage(CommonMessage commonMessage, String additional) {
        setMessage(commonMessage.getMessage(additional, null), commonMessage.getType());
    }

    protected void setMessage(String message) {
        setMessage(message, MessageType.INFO);
    }

    protected void setMessage(String message, MessageType messageType) {
        if (request == null) {
            return;
        }
        request.setAttribute("message", StringUtils.sanitizeUrl(message));
        request.setAttribute("messageType", messageType.getName());
    }

    protected boolean redirectValidationError(boolean isNew, HttpServletResponse response, Integer id, String message) throws IOException {
        if (isNew) {
            redirectWithMessage(response, FORWARD_FORM, CommonMessage.SAVE_FAILED, null, message, null);
        }
        else {
            redirectWithMessage(response, FORWARD_FORM, CommonMessage.SAVE_FAILED, id, message, null);
        }
        return false;
    }

    protected void redirectWithMessage(HttpServletResponse response, CommonMessage commonMessage) throws IOException {
        redirectWithMessage(response, null, commonMessage, null, null, null);
    }

    protected void redirectWithMessage(HttpServletResponse response, CommonMessage commonMessage, Integer id) throws IOException {
        redirectWithMessage(response, null, commonMessage, id, null, null);
    }

    protected void redirectWithMessage(HttpServletResponse response, CommonMessage commonMessage, String additional) throws IOException {
        redirectWithMessage(response, null, commonMessage, null, additional, null);
    }

    protected void redirectWithMessage(HttpServletResponse response, CommonMessage commonMessage, Integer id, String additional)
            throws IOException {
        redirectWithMessage(response, null, commonMessage, id, additional, null);
    }

    protected void redirectWithMessage(HttpServletResponse response, String action, CommonMessage commonMessage) throws IOException {
        redirectWithMessage(response, action, commonMessage, null, null, null);
    }

    protected void redirectWithMessage(HttpServletResponse response, CommonMessage commonMessage, String additional, String additionalParam)
        throws IOException {
        redirectWithMessage(response, null, commonMessage, null, additional, additionalParam);
    }

    protected void redirectWithMessage(HttpServletResponse response, String action, CommonMessage commonMessage, Integer id,
            String additional, String additionalParam) throws IOException {
        redirectWithMessage(response, path(), action, commonMessage, id, additional, additionalParam);
    }

    protected void redirectWithMessage(HttpServletResponse response, String path, String action, CommonMessage commonMessage, Integer id,
            String additional, String additionalParam) throws IOException {
        if (response == null) {
            return;
        }
        int argCount = 0;
        StringBuilder str = new StringBuilder(path);
        if (!StringUtils.isEmpty(action)) {
            str.append(argCount == 0 ? "?" : "&");
            str.append("action=").append(action);
            argCount++;
        }
        if (id != null) {
            str.append(argCount == 0 ? "?" : "&");
            str.append("id=").append(id);
            argCount++;
        }
        Integer projectPk = getProjectPk();
        if (projectPk != null) {
            str.append(argCount == 0 ? "?" : "&");
            str.append("projectPk=").append(projectPk);
            argCount++;
        }
        if (!StringUtils.isEmpty(additionalParam)) {
            str.append(argCount == 0 ? "?" : "&");
            str.append(additionalParam);
        }
        if (commonMessage != null) {
            str.append(argCount == 0 ? "?" : "&");
            str.append("msg=").append(commonMessage.getKey());
            argCount++;
        }
        if (!StringUtils.isEmpty(additional)) {
            additional = StringUtils.escapeHtml(additional);
            str.append(argCount == 0 ? "?" : "&");
            str.append("additional=").append(additional);
        }
        response.sendRedirect(str.toString());
    }

    protected static Logger getLogger() {
        return logger;
    }

    protected HttpSession getHttpSession() {
        return httpSession;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    protected String getAction() {
        if (StringUtils.isEmpty(action)) {
            return "index";
        }
        return action;
    }

    protected void setAction(String action) {
        this.action = action;
    }

    protected LoginBean getLoginBean() {
        return loginBean;
    }

    protected void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    protected ProjectBean getProjectBean() {
        return projectBean;
    }

    protected UserService getUserService() {
        return userService;
    }

    protected ProjectService getProjectService() {
        return projectService;
    }

    protected ManagedListItemService getManagedListItemService() {
        return managedListItemService;
    }

    protected Integer getProjectPk() {
        return projectPk;
    }

    protected Project getProject() {
        return project;
    }

    protected void setProject(Project project) {
        this.project = project;
    }

    protected String getCurrFacetVersion() {
        return managedListItemService.getCurrentDefault(ManagedList.FACET_VERSIONS, projectPk);
    }

    protected String getCurrOsVersion() {
        return managedListItemService.getCurrentDefault(ManagedList.OS_VERSIONS, projectPk);
    }

    protected String getCurrDmsVersion() {
        return DateUtils.getCurrDmsVersion();
    }

    protected void setProjectPk(String projectPk) {
        setProjectPk(StringUtils.parseInt(projectPk));
    }

    protected void setProjectPk(Integer projectPk) {
        if (projectPk != null && projectPk > 0) {
            this.projectPk = projectPk;
        }
    }

    protected Boolean isDevEnv() {
        if (devEnv == null) {
            devEnv = ConfigUtils.isDevEnv();
        }
        return devEnv;
    }

    protected void logInfo(String message) {
        getLogger().info(message);
    }

    protected void logError(String message) {
        logError(message, null);
    }

    protected void logError(String message, Exception e) {
        getLogger().error(message);
        if (e != null) {
            e.printStackTrace();
        }
    }
}