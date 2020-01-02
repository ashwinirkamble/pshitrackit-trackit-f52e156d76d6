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

import com.premiersolutionshi.old.bean.DecomBean;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.model.DecomModel;
import com.premiersolutionshi.old.model.LoginModel;
import com.premiersolutionshi.old.model.ProjectModel;
import com.premiersolutionshi.old.model.ShipModel;
import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Navigation logic for the application's DECOM process
 *
 * @author   Anthony Tsuhako
 * @version  1.0, 11/25/2013
 * @since    JDK 7, Apache Struts 1.3.10
 */
public final class DecomAction extends Action {
	private static Logger logger = Logger.getLogger(DecomAction.class.getSimpleName());
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
		HttpSession session = request.getSession();
		String action = CommonMethods.nes(request.getParameter("action")).trim();
		String returnAction = null;
		LoginBean loginBean = new LoginBean();
		DecomBean inputBean = (DecomBean) form;
		String operation = null;//, sortBy = null, sortDir = null;

		int projectPk = CommonMethods.cInt(request.getParameter("projectPk"));
		if (projectPk == -1) action = "errorExpiredSession";

		double startTime = System.currentTimeMillis();

		try (Connection conn = CommonMethods.getConn(servlet, "jdbc/pshi")) {
			loginBean = LoginModel.getUserInfo(conn, request);

			logger.info(String.format("%8s%-32s | %-34s | %s", "", "DECOM NAVIGATION [START]", "Action: " + action, "Username: " + loginBean.getUsername()));

			/********************************** PREPARE ACTION **********************************/
			switch (action) {
				case "workflowSummary":
					if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) { //FACET project
						returnAction = "errorNoRole";
					} else {
						//Get parameters from form or session
						if (CommonMethods.nes(request.getParameter("searchPerformed")).equals("Y")) {
							session.setAttribute("decomWorkflowSummary_inputBean", inputBean);
						} else { //Get input bean from session
							inputBean = (DecomBean)session.getAttribute("decomWorkflowSummary_inputBean");
						} //end of else

						if (inputBean == null) inputBean = new DecomBean();

						switch (CommonMethods.nes(request.getParameter("operation"))) {
							case "addSuccess"		 : request.setAttribute("successMsg", "Successfully created decom workflow for " + CommonMethods.nes(request.getParameter("shipName")));break;
							case "addFailed"		 : request.setAttribute("errorMsg", "An error occurred while trying to insert the record");break;
							case "editSuccess"	 : request.setAttribute("successMsg", "Successfully updated decom workflow for " + CommonMethods.nes(request.getParameter("shipName")));break;
							case "editFailed"		 : request.setAttribute("errorMsg", "An error occurred while trying to update the record");break;
							case "deleteSuccess" : request.setAttribute("successMsg", "Successfully deleted decom workflow for " + CommonMethods.nes(request.getParameter("shipName")));break;
							case "deleteFailed"	 : request.setAttribute("errorMsg", "An error occurred while trying to delete the record");break;
						} //end of switch

						request.setAttribute("inputBean", inputBean);
						request.setAttribute("resultList", DecomModel.getSummaryList(conn, inputBean));
						request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));
						request.setAttribute("shipList", DecomModel.getAvailShipList(conn, inputBean.getContractNumber()));
						returnAction = "workflowSummary";
					} //end of else
					break;

				case "workflowAddDo":
					if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) { //FACET project
						returnAction = "errorNoRole";
					} else {
						operation = DecomModel.insertWorkflow(conn, inputBean, loginBean) ? "addSuccess" : "addFailed";
						logger.info(String.format("%10s%-30s | %s", "", "INFO", "Redirecting to workflowSummary"));
						response.sendRedirect("decom.do?action=workflowSummary&projectPk=" + projectPk + "&operation=" + operation + "&shipName=" + CommonMethods.urlClean(inputBean.getShipName()));
						returnAction = null;
					} //end of else
					break;

				case "workflowEdit":
					if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) { //FACET project
						returnAction = "errorNoRole";
					} else {
						inputBean = DecomModel.getWorkflowBean(conn, inputBean.getUic());
						request.setAttribute("editType", "edit");
						request.setAttribute("inputBean", inputBean);
						request.setAttribute("homeportList", ShipModel.getHomeportList(conn));
						request.setAttribute("customPageTitle", "Edit Decom Workflow for " + inputBean.getShipName());
						returnAction = "workflowEdit";
					} //end of else
					break;

				case "workflowEditDo":
					if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) { //FACET project
						returnAction = "errorNoRole";
					} else {
						operation = DecomModel.updateWorkflow(conn, inputBean, loginBean) ? "editSuccess" : "editFailed";
						logger.info(String.format("%10s%-30s | %s", "", "INFO", "Redirecting to workflowSummary"));
						response.sendRedirect("decom.do?action=workflowSummary&projectPk=" + projectPk + "&operation=" + operation + "&shipName=" + CommonMethods.urlClean(inputBean.getShipName()));
						returnAction = null;
					} //end of else
					break;

				case "workflowDeleteDo":
					if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) { //FACET project
						returnAction = "errorNoRole";
					} else {
						inputBean = DecomModel.getWorkflowBean(conn, CommonMethods.cInt(inputBean.getDecomWorkflowPk()));
						operation = DecomModel.deleteWorkflow(conn, inputBean) ? "deleteSuccess" : "deleteFailed";
						logger.info(String.format("%10s%-30s | %s", "", "INFO", "Redirecting to workflowSummary"));
						response.sendRedirect("decom.do?action=workflowSummary&projectPk=" + projectPk + "&operation=" + operation + "&shipName=" + CommonMethods.urlClean(inputBean.getShipName()));
						returnAction = null;
					} //end of else
					break;

				case "errorExpiredSession":
					returnAction = "errorExpiredSession";
					break;

				default:
					returnAction = "error404";
					break;
			} // end of switch

			if (!CommonMethods.isEmpty(returnAction)) CommonMethods.appFinally(conn, returnAction, loginBean, projectPk, request);
		} catch (SQLException e) {
			logger.error(String.format("%10s%-30s | %s", "", "ERROR", e));
			returnAction = "errorDatabaseDown";
		} catch (Exception e) {
			logger.error(String.format("%10s%-30s | %s", "", "ERROR", e));
			request.setAttribute("errorMsg", e.toString());
			returnAction = "errorUnexpected";
		} // end of catch

		double endTime = System.currentTimeMillis();
		String elapsedTime = nf1.format((endTime - startTime) / (double) 1000);
		logger.info(String.format("%8s%-32s | %-34s | %-52s | %s\r\n", "", "DECOM NAVIGATION [END]", "Return: " + returnAction, "Username: " + CommonMethods.nvl(loginBean.getUsername(), "GUEST"), "Elapsed Time: " + elapsedTime + " sec"));

		return !CommonMethods.isEmpty(returnAction) ? mapping.findForward(returnAction) : null;
	} // end of execute
} // end of class
