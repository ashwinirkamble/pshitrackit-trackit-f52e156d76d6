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
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.bean.ProjectBean;
import com.premiersolutionshi.old.model.EmailModel;
import com.premiersolutionshi.old.model.LoginModel;
import com.premiersolutionshi.old.model.LookupModel;
import com.premiersolutionshi.old.model.ProjectModel;
import com.premiersolutionshi.old.model.ShipModel;
import com.premiersolutionshi.old.model.SupportModel;
import com.premiersolutionshi.old.model.SystemModel;
import com.premiersolutionshi.old.model.UserModel;
import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Navigation logic for the application's PROJECT process
 *
 * @author   Anthony Tsuhako
 * @version  1.0, 11/25/2013
 * @since    JDK 7, Apache Struts 1.3.10
 */
public final class ProjectAction extends Action {
	private static Logger logger = Logger.getLogger(ProjectAction.class.getSimpleName());
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
		ProjectBean inputBean = (ProjectBean) form;
		String operation = null, sortBy = null, sortDir = null;

		int projectPk = CommonMethods.cInt(request.getParameter("projectPk"));
		ProjectBean projectBean = null;

		double startTime = System.currentTimeMillis();

		try (Connection conn = CommonMethods.getConn(servlet, "jdbc/pshi")) {
			loginBean = LoginModel.getUserInfo(conn, request);

			logger.info(String.format("%6s%-32s | %-34s | %s", "", "PROJECT NAVIGATION [START]", "Action: " + action, "Username: " + loginBean.getUsername()));

			/********************************** PREPARE ACTION **********************************/
			switch (action) {
				case "projectList":
					if (!request.isUserInRole("sysadmin")) {
						returnAction = "errorNoRole";
					} else {
						switch (CommonMethods.nes(request.getParameter("operation"))) {
							case "addSuccess": 		request.setAttribute("successMsg", "Successfully inserted " + inputBean.getProjectName());break;
							case "addFailed":			request.setAttribute("errorMsg", "An error occurred while trying to insert " + inputBean.getProjectName());break;
							case "editSuccess": 	request.setAttribute("successMsg", "Successfully updated record");break;
							case "editFailed":  	request.setAttribute("errorMsg", "An error occurred while trying to update the record");break;
							case "deleteSuccess": request.setAttribute("successMsg", "Successfully deleted " + inputBean.getProjectName());break;
							case "deleteFailed":  request.setAttribute("errorMsg", "An error occurred while trying to delete " + inputBean.getProjectName());break;
						} //end of switch
						request.setAttribute("resultList", ProjectModel.getProjectList(conn));
						returnAction = "projectList";
					}//end of else
					break;

				case "projectAddDo":
					if (!request.isUserInRole("sysadmin")) {
						returnAction = "errorNoRole";
					} else {
						operation = ProjectModel.insertProject(conn, inputBean, loginBean) ? "addSuccess" : "addFailed";
						logger.info(String.format("%8s%-30s | %s", "", "INFO", "Redirecting to projectList"));
						response.sendRedirect("project.do?action=projectList&projectPk=" + projectPk + "&operation=" + operation + "&projectName=" + CommonMethods.urlClean(inputBean.getProjectName()));
						returnAction = null;
					} //end of else
					break;

				case "projectDeleteDo":
					if (!request.isUserInRole("sysadmin")) {
						returnAction = "errorNoRole";
					} else {
						inputBean = ProjectModel.getProjectBean(conn, projectPk);
						operation = ProjectModel.deleteProject(conn, inputBean, CommonMethods.getUploadDir(request)) ? "deleteSuccess" : "deleteFailed";
						logger.info(String.format("%8s%-30s | %s", "", "INFO", "Redirecting to projectList"));
						response.sendRedirect("project.do?action=projectList&projectPk=" + projectPk + "&operation=" + operation + "&projectName=" + CommonMethods.urlClean(inputBean.getProjectName()));
						returnAction = null;
					} //end of else
					break;

				case "taskList":
					if (projectPk == -1) {
						returnAction = "errorExpiredSession";
					} else if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
						returnAction = "errorNoRole";
					} else {
						sortBy = CommonMethods.nvl(CommonMethods.nes(request.getParameter("searchPerformed")).equals("Y") ? request.getParameter("sortBy") : CommonMethods.nes(session.getAttribute("taskList_sortBy")), "task_pk");
						sortDir = CommonMethods.nvl(CommonMethods.nes(request.getParameter("searchPerformed")).equals("Y") ? request.getParameter("sortDir") : CommonMethods.nes(session.getAttribute("taskList_sortDir")), "ASC");
						if (!CommonMethods.nes(request.getParameter("searchPerformed")).equals("Y")) inputBean = (ProjectBean)session.getAttribute("taskList_inputBean");
						if (inputBean == null) inputBean = new ProjectBean();

						//Save parameters to session
						session.setAttribute("taskList_inputBean", inputBean);
						session.setAttribute("taskList_sortBy", sortBy);
						session.setAttribute("taskList_sortDir", sortDir);

						switch (CommonMethods.nes(request.getParameter("operation"))) {
							case "addFailed":   	request.setAttribute("errorMsg", "An error occurred while trying to insert the record");break;
							case "editFailed":  	request.setAttribute("errorMsg", "An error occurred while trying to update the record");break;
							case "deleteSuccess": request.setAttribute("successMsg", "Successfully deleted task #" + request.getParameter("taskPk"));break;
							case "deleteFailed":  request.setAttribute("errorMsg", "An error occurred while trying to delete the record");break;
							case "notFound": 			request.setAttribute("errorMsg", "Task #" + request.getParameter("taskPk") + " does not exist");break;
						} //end of switch

						projectBean = ProjectModel.getProjectBean(conn, projectPk);
						request.setAttribute("inputBean", inputBean);
						request.setAttribute("projectBean", projectBean);

						request.setAttribute("resultList", ProjectModel.getTaskList(conn, projectPk, inputBean, sortBy, sortDir));

						request.setAttribute("personAssignedList", ProjectModel.getPersonAssignedList(conn, projectPk));
						request.setAttribute("categoryList", ProjectModel.getCategoryList(conn, projectPk));
						request.setAttribute("statusList", ProjectModel.getStatusList(conn));
						request.setAttribute("sourceList", ProjectModel.getSourceList(conn, projectPk));
						request.setAttribute("priorityList", ProjectModel.getPriorityList(conn));
						request.setAttribute("shipList", ShipModel.getShipList(conn));
						request.setAttribute("homeportList", ShipModel.getHomeportList(conn));
						request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));

						request.setAttribute("sortBy", sortBy);
						request.setAttribute("sortDir", sortDir);
						returnAction = "taskList";
					} //end of else
					break;

				case "taskDetail":
					inputBean = ProjectModel.getTaskBean(conn, inputBean);

					if (inputBean == null || CommonMethods.isEmpty(inputBean.getTaskPk())) {
						logger.info(String.format("%8s%-30s | %s", "", "INFO", "Redirecting to taskList"));
						response.sendRedirect("project.do?action=taskList&projectPk=" + projectPk + "&operation=notFound&taskPk=" + request.getParameter("taskPk"));
						returnAction = null;
					} else if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
						returnAction = "errorNoRole";
					} else {
						switch (CommonMethods.nes(request.getParameter("operation"))) {
							case "addSuccess"	: request.setAttribute("successMsg", "Successfully inserted task #" + request.getParameter("taskPk"));break;
							case "editSuccess": request.setAttribute("successMsg", "Successfully updated task #" + request.getParameter("taskPk"));break;
						} //end of switch

						//Check if from right-bar task summary link and save new parameters in session
						if(CommonMethods.nes(request.getParameter("pagefrom")).equals("rightbar")) {
							ProjectBean newSessionBean = new ProjectBean();
							newSessionBean.setPersonAssigned(loginBean.getFullName());
							session.setAttribute("taskList_inputBean", newSessionBean);
							session.setAttribute("taskList_sortBy", "due_date");
							session.setAttribute("taskList_sortDir", "ASC");
						} //end of if

						ArrayList<ProjectBean> currTaskList = ProjectModel.getTaskList(conn, projectPk, (ProjectBean)session.getAttribute("taskList_inputBean"), CommonMethods.nes(session.getAttribute("taskList_sortBy")), CommonMethods.nes(session.getAttribute("taskList_sortDir")));

						request.setAttribute("currStr", ProjectModel.getCurrRecordStr(currTaskList, CommonMethods.cInt(inputBean.getTaskPk())));
						request.setAttribute("prevBean", ProjectModel.getPrevTaskBean(currTaskList, CommonMethods.cInt(inputBean.getTaskPk())));
						request.setAttribute("nextBean", ProjectModel.getNextTaskBean(currTaskList, CommonMethods.cInt(inputBean.getTaskPk())));

						request.setAttribute("resultBean", inputBean);
						request.setAttribute("projectBean", ProjectModel.getProjectBean(conn, projectPk));

						request.setAttribute("pocBean", UserModel.getEmployeePocBean(conn, inputBean.getPersonAssigned()));

						if (!CommonMethods.isEmpty(inputBean.getUic())) {
							//Get Curr Versions
							request.setAttribute("currFacetVersion", LookupModel.getCurrFacetVersion(conn, projectPk));
                            request.setAttribute("currOsVersion", LookupModel.getCurrOsVersion(conn, projectPk));
							request.setAttribute("currDmsVersion", SystemModel.getCurrDmsVersion());

							//Ship pop-up data
							request.setAttribute("shipBean", ShipModel.getShipBean(conn, inputBean.getUic()));
							request.setAttribute("configuredSystemList", SystemModel.getConfiguredSystemListByUic(conn, inputBean.getUic()));
							//request.setAttribute("shipTaskList", ProjectModel.getShipTaskList(conn, projectPk, inputBean.getUic()));
							request.setAttribute("shipPocList", ShipModel.getPocList(conn, inputBean.getUic()));
							request.setAttribute("shipIssueList", SupportModel.getIssueList(conn, projectPk, inputBean.getUic(), "open"));
							request.setAttribute("shipLastVisitBean", SupportModel.getLastVisitBean(conn, inputBean.getUic()));
							request.setAttribute("shipUpcomingVisitList", SupportModel.getUpcomingVisitList(conn, inputBean.getUic()));
						} //end of if

						request.setAttribute("customPageTitle", "View Task #" + inputBean.getTaskPk());
						returnAction = "taskDetail";
					} //end of else
					break;

				case "taskAdd":
					if (projectPk == -1) {
						returnAction = "errorExpiredSession";
					} else if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
						returnAction = "errorNoRole";
					} else {
						projectBean = ProjectModel.getProjectBean(conn, projectPk);
						inputBean = new ProjectBean();
						inputBean.setCreatedDate(CommonMethods.getDate("MM/DD/YYYY"));
						inputBean.setDocUpdatedInd("N");
						request.setAttribute("inputBean", inputBean);
						request.setAttribute("projectBean", projectBean);
						request.setAttribute("personAssignedList", ProjectModel.getPersonAssignedList(conn, projectPk));
						request.setAttribute("categoryList", ProjectModel.getCategoryList(conn, projectPk));
						request.setAttribute("statusList", ProjectModel.getStatusList(conn));
						request.setAttribute("sourceList", ProjectModel.getSourceList(conn, projectPk));
						request.setAttribute("priorityList", ProjectModel.getPriorityList(conn));
						request.setAttribute("shipList", ShipModel.getShipList(conn));
						request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));
						request.setAttribute("editType", "add");
						returnAction = "taskAdd";
					} //end of else
					break;

				case "taskCopy":
					inputBean = ProjectModel.getTaskBean(conn, inputBean);

					if (projectPk == -1) {
						returnAction = "errorExpiredSession";
					} else if (!CommonMethods.isIn(loginBean.getProjectPkArr(), inputBean.getProjectPk())) {
						returnAction = "errorNoRole";
					} else {
						request.setAttribute("inputBean", inputBean);
						request.setAttribute("projectBean", ProjectModel.getProjectBean(conn, projectPk));
						request.setAttribute("personAssignedList", ProjectModel.getPersonAssignedList(conn, projectPk));
						request.setAttribute("categoryList", ProjectModel.getCategoryList(conn, projectPk));
						request.setAttribute("statusList", ProjectModel.getStatusList(conn));
						request.setAttribute("sourceList", ProjectModel.getSourceList(conn, projectPk));
						request.setAttribute("priorityList", ProjectModel.getPriorityList(conn));
						request.setAttribute("shipList", ShipModel.getShipList(conn));
						request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));
						request.setAttribute("customPageTitle", "Copy Task #" + inputBean.getTaskPk());
						request.setAttribute("editType", "add");
						returnAction = "taskAdd";
					} //end of else
					break;

				case "taskAddDo":
					if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
						returnAction = "errorNoRole";
					} else {
						int newTaskPk = ProjectModel.insertTask(conn, inputBean, loginBean, CommonMethods.getUploadDir(request));
						operation = newTaskPk >= 1 ? "addSuccess" : "addFailed";

						//Send email notification on changes
						EmailModel emailModel = new EmailModel();
						if (operation.equals("addSuccess") && projectPk == 1) emailModel.sendTaskAddHtmlEmail(conn, inputBean, newTaskPk, loginBean, request);

						if (operation.equals("addSuccess")) { //Success
							logger.info(String.format("%8s%-30s | %s", "", "INFO", "Redirecting to taskDetail"));
							response.sendRedirect("project.do?action=taskDetail&projectPk=" + projectPk + "&operation=" + operation + "&taskPk=" + newTaskPk);
							returnAction = null;
						} else { //Failed
							logger.info(String.format("%8s%-30s | %s", "", "INFO", "Redirecting to taskList"));
							response.sendRedirect("project.do?action=taskList&projectPk=" + projectPk + "&operation=" + operation + "&taskPk=" + newTaskPk);
							returnAction = null;
						} //end of else
					} //end of else
					break;

				case "taskEdit":
					inputBean = ProjectModel.getTaskBean(conn, inputBean);

					if (CommonMethods.isEmpty(inputBean.getTaskPk())) {
						logger.info(String.format("%8s%-30s | %s", "", "INFO", "Redirecting to taskList"));
						response.sendRedirect("project.do?action=taskList&projectPk=" + projectPk + "&operation=notFound&taskPk=" + request.getParameter("taskPk"));
						returnAction = null;
					} else if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
						returnAction = "errorNoRole";
					} else {
						request.setAttribute("inputBean", inputBean);
						request.setAttribute("projectBean", ProjectModel.getProjectBean(conn, projectPk));
						request.setAttribute("personAssignedList", ProjectModel.getPersonAssignedList(conn, projectPk));
						request.setAttribute("categoryList", ProjectModel.getCategoryList(conn, projectPk));
						request.setAttribute("statusList", ProjectModel.getStatusList(conn));
						request.setAttribute("sourceList", ProjectModel.getSourceList(conn, projectPk));
						request.setAttribute("priorityList", ProjectModel.getPriorityList(conn));
						request.setAttribute("shipList", ShipModel.getShipList(conn));
						request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));
						request.setAttribute("customPageTitle", "Edit Task #" + inputBean.getTaskPk());
						request.setAttribute("editType", "edit");
						returnAction = "taskEdit";
					} //end of else
					break;

				case "taskEditDo":
					ProjectBean origBean = ProjectModel.getTaskBean(conn, inputBean);
					if (!CommonMethods.isIn(loginBean.getProjectPkArr(), origBean.getProjectPk())) {
						returnAction = "errorNoRole";
					} else {
						operation = ProjectModel.updateTask(conn, inputBean, loginBean, CommonMethods.getUploadDir(request)) ? "editSuccess" : "editFailed";

						//Send email notification on changes
						EmailModel emailModel = new EmailModel();
						if (operation.equals("editSuccess") && origBean.getProjectPk().equals("1")) emailModel.sendTaskEditHtmlEmail(conn, origBean, inputBean, loginBean, request);

						if (operation.equals("editSuccess")) { //Success
							logger.info(String.format("%8s%-30s | %s", "", "INFO", "Redirecting to taskDetail"));
							response.sendRedirect("project.do?action=taskDetail&projectPk=" + projectPk + "&operation=" + operation + "&taskPk=" + inputBean.getTaskPk());
							returnAction = null;
						} else { //Failed
							logger.info(String.format("%8s%-30s | %s", "", "INFO", "Redirecting to taskList"));
							response.sendRedirect("project.do?action=taskList&projectPk=" + projectPk + "&operation=" + operation + "&taskPk=" + inputBean.getTaskPk());
							returnAction = null;
						} //end of else
					} //end of else
					break;

				case "taskDeleteDo":
					inputBean = ProjectModel.getTaskBean(conn, inputBean);
					if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
						returnAction = "errorNoRole";
					} else {
						operation = ProjectModel.deleteTask(conn, inputBean, CommonMethods.getUploadDir(request)) ? "deleteSuccess" : "deleteFailed";

						//Send email notification on changes
						EmailModel emailModel = new EmailModel();
						if (operation.equals("deleteSuccess") && projectPk == 1) emailModel.sendTaskDeleteHtmlEmail(conn, inputBean, loginBean, request);

						logger.info(String.format("%8s%-30s | %s", "", "INFO", "Redirecting to taskList"));
						response.sendRedirect("project.do?action=taskList&projectPk=" + projectPk + "&operation=" + operation + "&taskPk=" + inputBean.getTaskPk());
						returnAction = null;
					} //end of else
					break;

/*
				case "taskAgendaList":
					if (!CommonMethods.nes(request.getParameter("agenda")).equalsIgnoreCase("staff") && !CommonMethods.nes(request.getParameter("agenda")).equalsIgnoreCase("client")) {
						returnAction = "error404";
					} else if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
						returnAction = "errorNoRole";
					} else {
						inputBean = new ProjectBean();
						String[] newSearchMeetingArr = new String[1];
						newSearchMeetingArr[0] = request.getParameter("agenda");
						inputBean.setSearchMeetingArr(newSearchMeetingArr);

						request.setAttribute("resultList", ProjectModel.getTaskList(conn, projectPk, inputBean, "task_pk", "ASC"));
						request.setAttribute("agenda", request.getParameter("agenda"));
						request.setAttribute("customPageTitle", "Weekly " + request.getParameter("agenda") + " Meeting Agenda");
						returnAction = "taskAgendaList";
					} //end of if
					break;

				case "taskAgendaListXls":
					inputBean = new ProjectBean();
					if (!CommonMethods.nes(request.getParameter("agenda")).equalsIgnoreCase("staff") && !CommonMethods.nes(request.getParameter("agenda")).equalsIgnoreCase("client")) {
						returnAction = "error404";
					} else if (!CommonMethods.isIn(loginBean.getProjectPkArr(), projectPk)) {
						returnAction = "errorNoRole";
					} else {
						inputBean = new ProjectBean();
						String[] newSearchMeetingArr = new String[1];
						newSearchMeetingArr[0] = request.getParameter("agenda");
						inputBean.setSearchMeetingArr(newSearchMeetingArr);
						request.setAttribute("resultList", ProjectModel.getTaskList(conn, projectPk, inputBean, "task_pk", "ASC"));
						request.setAttribute("msoFooterData", CommonMethods.getDate());
						request.setAttribute("customPageTitle", "Weekly " + request.getParameter("agenda") + " Meeting Agenda");
						returnAction = "taskListXls";
					} //end of if
					break;

				case "outlookCsv":
					response.reset();
					response.setContentType("text/txt");
					response.addHeader("Content-Disposition", "attachment;filename=outlook_tasks.txt");
					ProjectModel.writeOutlookCsv(conn, loginBean, response.getOutputStream());
					response.flushBuffer();
					returnAction = null;
					break;
*/

				default:
					returnAction = "error404";
					break;
			} // end of switch

			if (!CommonMethods.isEmpty(returnAction)) CommonMethods.appFinally(conn, returnAction, loginBean, projectPk, request);
		} catch (SQLException e) {
			logger.error(String.format("%8s%-30s | %s", "", "ERROR", e));
			e.printStackTrace();
			returnAction = "errorDatabaseDown";
		} catch (Exception e) {
			logger.error(String.format("%8s%-30s | %s", "", "ERROR", e));
			request.setAttribute("errorMsg", e.toString());
			e.printStackTrace();
			returnAction = "errorUnexpected";
		} // end of catch

		double endTime = System.currentTimeMillis();
		String elapsedTime = nf1.format((endTime - startTime) / (double) 1000);
		logger.info(String.format("%6s%-32s | %-34s | %-52s | %s\r\n", "", "PROJECT NAVIGATION [END]", "Return: " + returnAction, "Username: " + CommonMethods.nvl(loginBean.getUsername(), "GUEST"), "Elapsed Time: " + elapsedTime + " sec"));

		return !CommonMethods.isEmpty(returnAction) ? mapping.findForward(returnAction) : null;
	} // end of execute
} // end of class
