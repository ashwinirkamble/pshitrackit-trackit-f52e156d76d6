package com.premiersolutionshi.old.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

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

import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.bean.UserBean;
import com.premiersolutionshi.old.model.LoginModel;
import com.premiersolutionshi.old.model.ProjectModel;
import com.premiersolutionshi.old.model.UserModel;
import com.premiersolutionshi.old.util.CommonMethods;
import java.util.*;
/**
 * Navigation logic for the application's USER process
 */
public final class UserAction extends Action {
    private static Logger logger = Logger.getLogger(UserAction.class.getSimpleName());
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
        UserBean inputBean = (UserBean) form;
        String operation = null, sortBy = null, sortDir = null;

        int projectPk = CommonMethods.cInt(request.getParameter("projectPk"));

        
        double startTime = System.currentTimeMillis();

        try (Connection conn = CommonMethods.getConn(servlet, "jdbc/pshi")) {
            loginBean = LoginModel.getUserInfo(conn, request);
            HttpSession session = request.getSession();

            logger.info(String.format("%9s%-32s | %-34s | %s", "", "USER NAVIGATION [START]", "Action: " + action, "Username: " + loginBean.getUsername()));

            /********************************** PREPARE ACTION **********************************/
            String searchPerformedParam = CommonMethods.nes(request.getParameter("searchPerformed"));
            boolean isSearchPerformed = searchPerformedParam.equals("Y");
            String sortByParam = request.getParameter("sortBy");
            String sortDirParam = request.getParameter("sortDir");
            String operationParam = CommonMethods.nes(request.getParameter("operation"));
            switch (action) {
                case "userList":
                    if (!request.isUserInRole("sysadmin")) {
                        returnAction = "errorNoRole";
                    } else {
                        //Get parameters from form or session
                        String userListSortBy = CommonMethods.nes(session.getAttribute("userList_sortBy"));
                        String userListSortDir = CommonMethods.nes(session.getAttribute("userList_sortDir"));
                        sortBy = CommonMethods.nvl(isSearchPerformed ? sortByParam : userListSortBy, "user_name");
                        sortDir = CommonMethods.nvl(isSearchPerformed ? sortDirParam : userListSortDir, "ASC");
                        if (!isSearchPerformed) inputBean = (UserBean)session.getAttribute("userList_inputBean");
                        if (inputBean == null) inputBean = new UserBean();

                        //Save parameters to session
                        session.setAttribute("userList_inputBean", inputBean);
                        session.setAttribute("userList_sortBy", sortBy);
                        session.setAttribute("userList_sortDir", sortDir);

                        String usernameParam = CommonMethods.nes(request.getParameter("username"));
                        String errorMsg = "An error occurred while trying to ";
                        switch (operationParam) {
                            case "addSuccess":    request.setAttribute("successMsg", "Successfully inserted " + usernameParam);break;
                            case "addFailed":     request.setAttribute("errorMsg", errorMsg + "insert the user");break;
                            case "editSuccess":   request.setAttribute("successMsg", "Successfully updated " + usernameParam);break;
                            case "editFailed":    request.setAttribute("errorMsg", errorMsg + "update the user");break;
                            case "deleteSuccess": request.setAttribute("successMsg", "Successfully deleted " + usernameParam);break;
                            case "deleteFailed":  request.setAttribute("errorMsg", errorMsg + "delete the user");break;
                        }

                        request.setAttribute("inputBean", inputBean);
                        request.setAttribute("resultList", UserModel.getUserList(conn, inputBean, sortBy, sortDir));
                        request.setAttribute("sortBy", sortBy);
                        request.setAttribute("sortDir", sortDir);
                        returnAction = "userList";
                    }
                    break;

                case "userAdd":
                    if (!request.isUserInRole("sysadmin")) {
                        returnAction = "errorNoRole";
                    } else {
                        request.setAttribute("editType", "add");
                        request.setAttribute("organizationList", UserModel.getOrganizationList(conn));
                        request.setAttribute("projectList", ProjectModel.getProjectList(conn));
                        request.setAttribute("customPageTitle", "Add New User");
                        returnAction = "userAdd";
                    }
                    break;

                case "userAddDo":
                    if (!request.isUserInRole("sysadmin")) {
                        returnAction = "errorNoRole";
                    } else {
                        operation = UserModel.insertUser(conn, inputBean, loginBean) ? "addSuccess" : "addFailed";
                        logger.info(String.format("%11s%-30s | %s", "", "INFO", "Redirecting to userList"));
                        response.sendRedirect("user.do?action=userList&operation=" + operation + "&username=" + CommonMethods.urlClean(inputBean.getUsername()));
                        returnAction = null;
                    }
                    break;

                case "userEdit":
                    if (!request.isUserInRole("sysadmin")) {
                        returnAction = "errorNoRole";
                    } else {
                        inputBean = UserModel.getUserBean(conn, CommonMethods.cInt(inputBean.getUserPk()));
                        request.setAttribute("inputBean", inputBean);
                        request.setAttribute("organizationList", UserModel.getOrganizationList(conn));
                        request.setAttribute("projectList", ProjectModel.getProjectList(conn));
                        request.setAttribute("editType", "edit");
                        request.setAttribute("customPageTitle", "Edit " + inputBean.getUsername());
                        returnAction = "userEdit";
                    }
                    break;

                case "userEditDo":
                    if (!request.isUserInRole("sysadmin")) {
                        returnAction = "errorNoRole";
                    } else {
                        operation = UserModel.updateUser(conn, inputBean, loginBean) ? "editSuccess" : "editFailed";
                        logger.info(String.format("%11s%-30s | %s", "", "INFO", "Redirecting to UserList"));
                        response.sendRedirect("user.do?action=userList&operation=" + operation + "&username=" + CommonMethods.urlClean(inputBean.getUsername()));
                        returnAction = null;
                    }
                    break;

                case "userDeleteDo":
                    if (!request.isUserInRole("sysadmin")) {
                        returnAction = "errorNoRole";
                    } else {
                        inputBean = UserModel.getUserBean(conn, CommonMethods.cInt(inputBean.getUserPk()));
                        operation = UserModel.deleteUser(conn, inputBean) ? "deleteSuccess" : "deleteFailed";
                        logger.info(String.format("%11s%-30s | %s", "", "INFO", "Redirecting to userList"));
                        response.sendRedirect("user.do?action=userList&operation=" + operation + "&username=" + CommonMethods.urlClean(inputBean.getUsername()));
                        returnAction = null;
                    }
                    break;

//                case "pocList":
//                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
//                        returnAction = "errorNoRole";
//                    } else {
//                        switch (operationParam) {
//                            case "addSuccess":    request.setAttribute("successMsg", "Successfully inserted " + CommonMethods.nes(request.getParameter("fullName")));break;
//                            case "addFailed":     request.setAttribute("errorMsg", "An error occurred while trying to insert the POC");break;
//                            case "editSuccess":   request.setAttribute("successMsg", "Successfully updated " + CommonMethods.nes(request.getParameter("fullName")));break;
//                            case "editFailed":    request.setAttribute("errorMsg", "An error occurred while trying to update the POC");break;
//                            case "deleteSuccess": request.setAttribute("successMsg", "Successfully deleted " + CommonMethods.nes(request.getParameter("fullName")));break;
//                            case "deleteFailed":  request.setAttribute("errorMsg", "An error occurred while trying to delete the POC");break;
//                        }
//
//                        //Get parameters from form or session
//                        if (!isSearchPerformed) inputBean = (UserBean)session.getAttribute("pocList_inputBean");
//                        if (inputBean == null) inputBean = new UserBean();
//
//                        //Save parameters to session
//                        session.setAttribute("pocList_inputBean", inputBean);
//
//                        request.setAttribute("employeeList", UserModel.getEmployeePocList(conn, projectPk, inputBean));
//                        request.setAttribute("shipList", ShipModel.getAllShipPocList(conn, inputBean));//.subList(0, 4)
//                        request.setAttribute("resultList", PocModel.getPocList(conn, projectPk, inputBean));
//                        request.setAttribute("projectBean", ProjectModel.getProjectBean(conn, CommonMethods.cInt(request.getParameter("projectPk"))));
//                        request.setAttribute("inputBean", inputBean);
//                        returnAction = "pocList";
//                    }
//                    break;

//                case "pocAdd":
//                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
//                        returnAction = "errorNoRole";
//                    } else {
//                        ProjectBean projectBean = ProjectModel.getProjectBean(conn, projectPk);
//                        request.setAttribute("editType", "add");
//                        request.setAttribute("organizationList", PocModel.getOrganizationList(conn));
//                        request.setAttribute("inputBean", inputBean);
//                        request.setAttribute("customPageTitle", "Add New " + projectBean.getProjectName() + " POC");
//                        returnAction = "pocAdd";
//                    }
//                    break;

//                case "pocAddDo":
//                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
//                        returnAction = "errorNoRole";
//                    } else {
//                        operation = PocModel.insertPoc(conn, inputBean, loginBean) ? "addSuccess" : "addFailed";
//                        logger.info(String.format("%11s%-30s | %s", "", "INFO", "Redirecting to pocList"));
//                        response.sendRedirect("user.do?action=pocList&projectPk=" + projectPk + "&operation=" + operation + "&fullName=" + CommonMethods.urlClean(inputBean.getFirstName() + " " + inputBean.getLastName()));
//                        returnAction = null;
//                    }
//                    break;
//
//                case "pocEdit":
//                    inputBean = PocModel.getPocBean(conn, CommonMethods.cInt(inputBean.getPocPk()));
//                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
//                        returnAction = "errorNoRole";
//                    } else {
//                        request.setAttribute("inputBean", inputBean);
//                        request.setAttribute("organizationList", PocModel.getOrganizationList(conn));
//                        request.setAttribute("editType", "edit");
//                        request.setAttribute("customPageTitle", "Edit " + inputBean.getFirstName() + " " + inputBean.getLastName());
//                        returnAction = "pocEdit";
//                    }
//                    break;

//                case "pocEditDo":
//                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
//                        returnAction = "errorNoRole";
//                    } else {
//                        operation = PocModel.updatePoc(conn, inputBean, loginBean) ? "editSuccess" : "editFailed";
//                        logger.info(String.format("%11s%-30s | %s", "", "INFO", "Redirecting to pocList"));
//                        response.sendRedirect("user.do?action=pocList&projectPk=" + projectPk + "&operation=" + operation + "&fullName=" + CommonMethods.urlClean(inputBean.getFirstName() + " " + inputBean.getLastName()));
//                        returnAction = null;
//                    }
//                    break;
//
//                case "pocDeleteDo":
//                    inputBean = PocModel.getPocBean(conn, CommonMethods.cInt(inputBean.getPocPk()));
//                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
//                        returnAction = "errorNoRole";
//                    } else {
//                        operation = PocModel.deletePoc(conn, inputBean) ? "deleteSuccess" : "deleteFailed";
//                        logger.info(String.format("%11s%-30s | %s", "", "INFO", "Redirecting to pocList"));
//                        response.sendRedirect("user.do?action=pocList&projectPk=" + projectPk + "&operation=" + operation + "&fullName=" + CommonMethods.urlClean(inputBean.getFirstName() + " " + inputBean.getLastName()));
//                        returnAction = null;
//                    }
//                    break;

//                case "shipPocAdd":
//                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
//                        returnAction = "errorNoRole";
//                    } else {
//                        request.setAttribute("editType", "add");
//                        request.setAttribute("shipList", ShipModel.getShipList(conn));
//                        request.setAttribute("inputBean", inputBean);
//                        request.setAttribute("customPageTitle", "Add New Ship POC");
//                        returnAction = "shipPocAdd";
//                    }
//                    break;
//
//                case "shipPocAddDo":
//                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
//                        returnAction = "errorNoRole";
//                    } else {
//                        operation = ShipModel.insertPoc(conn, inputBean, loginBean) ? "addSuccess" : "addFailed";
//                        logger.info(String.format("%11s%-30s | %s", "", "INFO", "Redirecting to pocList"));
//                        response.sendRedirect("user.do?action=pocList&projectPk=" + projectPk + "&operation=" + operation + "&shipPk=" + inputBean.getShipPk() + "&fullName=" + CommonMethods.urlClean(inputBean.getFirstName() + " " + inputBean.getLastName()));
//                        returnAction = null;
//                    }
//                    break;

//                case "shipPocEdit":
//                    inputBean = ShipModel.getPocBean(conn, CommonMethods.cInt(inputBean.getShipPocPk()));
//                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
//                        returnAction = "errorNoRole";
//                    } else {
//                        request.setAttribute("inputBean", inputBean);
//                        request.setAttribute("shipList", ShipModel.getShipList(conn));
//                        request.setAttribute("editType", "edit");
//                        request.setAttribute("customPageTitle", "Edit " + inputBean.getFirstName() + " " + inputBean.getLastName());
//                        returnAction = "shipPocEdit";
//                    }
//                    break;

//                case "shipPocEditDo":
//                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
//                        returnAction = "errorNoRole";
//                    } else {
//                        operation = ShipModel.updatePoc(conn, inputBean, loginBean) ? "editSuccess" : "editFailed";
//                        logger.info(String.format("%11s%-30s | %s", "", "INFO", "Redirecting to pocList"));
//                        response.sendRedirect("user.do?action=pocList&projectPk=" + projectPk + "&operation=" + operation + "&shipPk=" + inputBean.getShipPk() + "&fullName=" + CommonMethods.urlClean(inputBean.getFirstName() + " " + inputBean.getLastName()));
//                        returnAction = null;
//                    }
//                    break;

//                case "shipPocDeleteDo":
//                    inputBean = ShipModel.getPocBean(conn, CommonMethods.cInt(inputBean.getShipPocPk()));
//                    if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
//                        returnAction = "errorNoRole";
//                    } else {
//                        operation = ShipModel.deletePoc(conn, inputBean) ? "deleteSuccess" : "deleteFailed";
//                        logger.info(String.format("%11s%-30s | %s", "", "INFO", "Redirecting to pocList"));
//                        response.sendRedirect("user.do?action=pocList&projectPk=" + projectPk + "&operation=" + operation + "&fullName=" + CommonMethods.urlClean(inputBean.getFirstName() + " " + inputBean.getLastName()));
//                        returnAction = null;
//                    }
//                    break;

                case "ptoTravelList":
                    switch (operationParam) {
                        case "addSuccess":         request.setAttribute("successMsg", "Successfully inserted PTO/Travel event");break;
                        case "addFailed":       request.setAttribute("errorMsg", "An error occurred while trying to insert the PTO/Travel event");break;
                        case "editSuccess":     request.setAttribute("successMsg", "Successfully updated PTO/Travel event");break;
                        case "editFailed":      request.setAttribute("errorMsg", "An error occurred while trying to update the PTO/Travel event");break;
                        case "deleteSuccess": request.setAttribute("successMsg", "Successfully deleted PTO/Travel event");break;
                        case "deleteFailed":  request.setAttribute("errorMsg", "An error occurred while trying to delete the PTO/Travel event");break;
                    }
                    
                    request.setAttribute("resultList", UserModel.getPtoTravelList(conn, loginBean, request));
                    returnAction = "ptoTravelList";

                    break;

                    // Added code for Current pto Travel List on home screen by Ashwini on 13-Nov-2019

                case "CurrentptoTravelList":
                    
                    request.setAttribute("CurrentptoTravelList", UserModel.getCurrentPtoTravelList(conn, loginBean,request));

                   // returnAction = "CurrentptoTravelList";

                    break;

                    
                case "ptoTravelAdd":
                    UserBean newBean = new UserBean();
                    newBean.setUserPk(loginBean.getUserPk());
                    String startDateStr = request.getParameter("startDate");
                    String endDateStr = request.getParameter("endDate");
                    if (!StringUtils.isEmpty(startDateStr)) {
                        newBean.setStartDate(startDateStr);
                    }
                    if (!StringUtils.isEmpty(endDateStr)) {
                        newBean.setEndDate(endDateStr);
                    }
                    if (StringUtils.isEmpty(endDateStr) && !StringUtils.isEmpty(startDateStr)) {
                        newBean.setEndDate(startDateStr);
                    }
                    request.setAttribute("inputBean", newBean);
                    request.setAttribute("editType", "add");
                    request.setAttribute("customPageTitle", "Add New PTO/Travel Event");
                    if (request.isUserInRole("sysadmin") || request.isUserInRole("pshi-user-admin")) request.setAttribute("userList", UserModel.getUserList(conn));
                    returnAction = "ptoTravelAdd";
                    break;

                case "ptoTravelAddDo":
                    if (!request.isUserInRole("sysadmin") && !request.isUserInRole("pshi-user-admin") && !inputBean.getUserPk().equals(loginBean.getUserPk())) {
                        returnAction = "errorNoRole";
                    } else {
                        operation = UserModel.insertPtoTravel(conn, inputBean, loginBean) ? "addSuccess" : "addFailed";
                        logger.info(String.format("%11s%-30s | %s", "", "INFO", "Redirecting to ptoTravelList"));
                        response.sendRedirect("user.do?action=ptoTravelList&operation=" + operation);
                        returnAction = null;
                    }
                    break;

                case "ptoTravelEdit":
                    inputBean = UserModel.getPtoTravelBean(conn, CommonMethods.cInt(inputBean.getPtoTravelPk()));
                    if (!request.isUserInRole("sysadmin") && !request.isUserInRole("pshi-user-admin") && !inputBean.getUserPk().equals(loginBean.getUserPk())) {
                        returnAction = "errorNoRole";
                    } else {
                        request.setAttribute("inputBean", inputBean);
                        if (request.isUserInRole("sysadmin") || request.isUserInRole("pshi-user-admin")) request.setAttribute("userList", UserModel.getUserList(conn));
                        request.setAttribute("editType", "edit");
                        request.setAttribute("customPageTitle", "Edit PTO/Travel Event for " + inputBean.getFirstName() + " " + inputBean.getLastName());
                        returnAction = "ptoTravelEdit";
                    }
                    break;

                case "ptoTravelEditDo":
                    if (!request.isUserInRole("sysadmin") && !request.isUserInRole("pshi-user-admin") && !inputBean.getUserPk().equals(loginBean.getUserPk())) {
                        returnAction = "errorNoRole";
                    } else {
                        operation = UserModel.updatePtoTravel(conn, inputBean, loginBean) ? "editSuccess" : "editFailed";
                        logger.info(String.format("%11s%-30s | %s", "", "INFO", "Redirecting to ptoTravelList"));
                        response.sendRedirect("user.do?action=ptoTravelList&operation=" + operation);
                        returnAction = null;
                    }
                    break;

                case "ptoTravelDeleteDo":
                    inputBean = UserModel.getPtoTravelBean(conn, CommonMethods.cInt(inputBean.getPtoTravelPk()));
                    if (!request.isUserInRole("sysadmin") && !request.isUserInRole("pshi-user-admin") && !inputBean.getUserPk().equals(loginBean.getUserPk())) {
                        returnAction = "errorNoRole";
                    } else {
                        operation = UserModel.deletePtoTravel(conn, inputBean) ? "deleteSuccess" : "deleteFailed";
                        logger.info(String.format("%11s%-30s | %s", "", "INFO", "Redirecting to ptoTravelList"));
                        response.sendRedirect("user.do?action=ptoTravelList&operation=" + operation);
                        returnAction = null;
                    }
                    break;
                    
                
                default:
                    returnAction = "error404";
                    break;
            }

            if (!CommonMethods.isEmpty(returnAction)) CommonMethods.appFinally(conn, returnAction, loginBean, projectPk, request);
        } catch (SQLException e) {
            logger.error(String.format("%11s%-30s | %s", "", "ERROR", e));
            returnAction = "errorDatabaseDown";
        } catch (Exception e) {
            logger.error(String.format("%11s%-30s | %s", "", "ERROR", e));
            request.setAttribute("errorMsg", e.toString());
            returnAction = "errorUnexpected";
        }

        double endTime = System.currentTimeMillis();
        String elapsedTime = nf1.format((endTime - startTime) / (double) 1000);
        logger.info(String.format("%9s%-32s | %-34s | %-52s | %s\r\n", "", "USER NAVIGATION [END]", "Return: " + returnAction, "Username: " + CommonMethods.nvl(loginBean.getUsername(), "GUEST"), "Elapsed Time: " + elapsedTime + " sec"));

        return !CommonMethods.isEmpty(returnAction) ? mapping.findForward(returnAction) : null;
    }
}
