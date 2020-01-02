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

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.util.StringUtils;

import com.premiersolutionshi.common.constant.ManagedList;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.bean.ShipBean;
import com.premiersolutionshi.old.model.LoginModel;
import com.premiersolutionshi.old.model.LookupModel;
import com.premiersolutionshi.old.model.ShipModel;
import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Navigation logic for the application's SHIP process
 */
public final class ShipAction extends Action {
    private static Logger logger = Logger.getLogger(ShipAction.class.getSimpleName());
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
        ShipBean inputBean = (ShipBean) form;
        String operation = null;//, sortBy = null, sortDir = null;

        double startTime = System.currentTimeMillis();
        request.setAttribute("contentHeader_projectName", "");
        request.setAttribute("isSysadmin", request.isUserInRole("sysadmin"));

        try (Connection conn = CommonMethods.getConn(servlet, "jdbc/pshi")) {
            loginBean = LoginModel.getUserInfo(conn, request);
            //HttpSession session = request.getSession();

            logger.info(String.format("%9s%-32s | %-34s | %s", "", "SHIP NAVIGATION [START]", "Action: " + action, "Username: " + loginBean.getUsername()));

            /********************************** PREPARE ACTION **********************************/
            ArrayList<ShipBean> shipList = ShipModel.getShipList(conn);
            switch (action) {
                case "shipList":
                    switch (CommonMethods.nes(request.getParameter("operation"))) {
                        case "addSuccess":        request.setAttribute("successMsg", "Successfully inserted " + CommonMethods.nes(request.getParameter("shipName")));break;
                        case "addFailed":         request.setAttribute("errorMsg", "An error occurred while trying to insert the ship");break;
                        case "duplicateDetected": request.setAttribute("errorMsg", "Ship Name and UIC are not Unique");break;
                        case "editSuccess":       request.setAttribute("successMsg", "Successfully updated " + CommonMethods.nes(request.getParameter("shipName")));break;
                        case "editFailed":        request.setAttribute("errorMsg", "An error occurred while trying to update the ship");break;
                        case "deleteSuccess":     request.setAttribute("successMsg", "Successfully deleted " + CommonMethods.nes(request.getParameter("shipName")));break;
                        case "deleteFailed":      request.setAttribute("errorMsg", "An error occurred while trying to delete the ship");break;
                    }

                    request.setAttribute("resultList", shipList);
                    returnAction = "shipList";
                    break;

                case "shipEdit":
                    inputBean = ShipModel.getShipBean(conn, CommonMethods.cInt(inputBean.getShipPk()));
                    request.setAttribute("inputBean", inputBean);
                    request.setAttribute("customPageTitle", "Edit " + inputBean.getShipName());
                case "shipAdd":
                    if (!request.isUserInRole("sysadmin")) {
                        returnAction = "errorNoRole";
                    } else {
                        request.setAttribute("typeList", LookupModel.getList(conn, ManagedList.SHIP_TYPES));
                        request.setAttribute("homeportList", LookupModel.getList(conn, ManagedList.SHIP_VISIT_LOCATIONS));
                        request.setAttribute("tycomList", ShipModel.getTycomList(conn));
                        request.setAttribute("rsupplyList", ShipModel.getRsupplyList(conn));
                        request.setAttribute("editType", action.equals("shipAdd") ? "add" : "edit");
                        if (action.equals("shipAdd")) {
                            request.setAttribute("customPageTitle", "Add New Unit");
                        }
                        returnAction = action;
                    }
                    request.setAttribute("shipList", shipList);
                    break;

                case "shipEditDo":
                case "shipAddDo":
                    if (!request.isUserInRole("sysadmin")) {
                        returnAction = "errorNoRole";
                    } else {
                        boolean hasDuplicate = false;
                        String shipPk = inputBean.getShipPk();
                        String inputShipName = inputBean.getShipName();
                        boolean isNew = StringUtils.isEmpty(shipPk);
                        for (ShipBean shipBean : shipList) {
                            if (isNew || !shipPk.equals(shipBean.getShipPk())) {
                                if (inputShipName.equals(shipBean.getShipName())) {
                                    hasDuplicate = true;
                                    break;
                                }
                            }
                        }
                        if (!hasDuplicate) {
                            if (isNew) {
                                operation = ShipModel.insertShip(conn, inputBean, loginBean) ? "addSuccess" : "addFailed";
                            }
                            else {
                                operation = ShipModel.updateShip(conn, inputBean, loginBean) ? "editSuccess" : "editFailed";
                            }
                            logger.info(String.format("%11s%-30s | %s", "", "INFO", "Redirecting to shipList"));
                            response.sendRedirect("ship.do?action=shipList&operation=" + operation + "&shipName=" + CommonMethods.urlClean(inputBean.getShipName()));
                            returnAction = null;
                        }
                        else {
                            logger.error("Ship Name and UIC are not unique.");
                            response.sendRedirect("ship.do?action=shipList&operation=duplicateDetected&shipName=" + CommonMethods.urlClean(inputBean.getShipName()));
                        }
                    }
                    break;

                case "shipDeleteDo":
                    if (!request.isUserInRole("sysadmin")) {
                        returnAction = "errorNoRole";
                    } else {
                        inputBean = ShipModel.getShipBean(conn, CommonMethods.cInt(inputBean.getShipPk()));
                        operation = ShipModel.deleteShip(conn, inputBean) ? "deleteSuccess" : "deleteFailed";
                        logger.info(String.format("%11s%-30s | %s", "", "INFO", "Redirecting to shipList"));
                        response.sendRedirect("ship.do?action=shipList&operation=" + operation + "&shipName=" + CommonMethods.urlClean(inputBean.getShipName()));
                        returnAction = null;
                    }
                    break;

                default:
                    returnAction = "error404";
                    break;
            }

            if (!CommonMethods.isEmpty(returnAction)) CommonMethods.appFinally(conn, returnAction, loginBean, -1, request);
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
        logger.info(String.format("%9s%-32s | %-34s | %-52s | %s\r\n", "", "SHIP NAVIGATION [END]", "Return: " + returnAction, "Username: " + CommonMethods.nvl(loginBean.getUsername(), "GUEST"), "Elapsed Time: " + elapsedTime + " sec"));

        return !CommonMethods.isEmpty(returnAction) ? mapping.findForward(returnAction) : null;
    }
}
