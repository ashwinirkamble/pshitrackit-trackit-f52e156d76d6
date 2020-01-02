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
import com.premiersolutionshi.old.bean.SoftwareBean;
import com.premiersolutionshi.old.model.LoginModel;
import com.premiersolutionshi.old.model.ProjectModel;
import com.premiersolutionshi.old.model.SoftwareModel;
import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Navigation logic for the application's SOFTWARE process
 */
public final class SoftwareAction extends Action {
	private static Logger logger = Logger.getLogger(SoftwareAction.class.getSimpleName());
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
		SoftwareBean inputBean = (SoftwareBean)form;
		String operation = null;//, sortBy = null, sortDir = null;

		int projectPk = CommonMethods.cInt(request.getParameter("projectPk"));
		if (projectPk == -1) action = "errorExpiredSession";

		double startTime = System.currentTimeMillis();

		try (Connection conn = CommonMethods.getConn(servlet, "jdbc/pshi")) {
			loginBean = LoginModel.getUserInfo(conn, request);

			logger.info(String.format("%5s%-32s | %-34s | %s", "", "SOFTWARE NAVIGATION [START]", "Action: " + action, "Username: " + loginBean.getUsername()));

			/********************************** PREPARE ACTION **********************************/
			switch (action) {
				case "kofaxLicenseList":
					switch (CommonMethods.nes(request.getParameter("operation"))) {
						case "editSuccess"   : request.setAttribute("successMsg", "Successfully updated " + CommonMethods.nes(request.getParameter("licenseKey")));break;
						case "editFailed"    : request.setAttribute("errorMsg", "An error occurred while trying to update the license");break;
						case "deleteSuccess" : request.setAttribute("successMsg", "Successfully deleted " + CommonMethods.nes(request.getParameter("licenseKey")));break;
						case "deleteFailed"  : request.setAttribute("errorMsg", "An error occurred while trying to delete the license");break;
					} //end of switch

					request.setAttribute("resultList", SoftwareModel.getKofaxLicenseList(conn));
					returnAction = "kofaxLicenseList";
					break;

				case "kofaxLicenseAdd":
					switch (CommonMethods.nes(request.getParameter("operation"))) {
						case "addSuccess" : request.setAttribute("successMsg", "Successfully inserted " + CommonMethods.nes(request.getParameter("licenseKey")));break;
						case "addFailed"  : request.setAttribute("errorMsg", "An error occurred while trying to insert the license");break;
					} //end of switch

					request.setAttribute("editType", "add");
					request.setAttribute("customPageTitle", "Add New Kofax License");
					request.setAttribute("customerList", ProjectModel.getCustomerList(conn));
					request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));
					returnAction = "kofaxLicenseAdd";
					break;

				case "kofaxLicenseAddDo":
					operation = SoftwareModel.insertKofaxLicense(conn, inputBean, loginBean) ? "addSuccess" : "addFailed";
					logger.info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to kofaxLicenseAdd"));
					response.sendRedirect("software.do?action=kofaxLicenseAdd&projectPk=" + projectPk + "&operation=" + operation + "&licenseKey=" + CommonMethods.urlClean(inputBean.getLicenseKey()));
					returnAction = null;
					break;

				case "kofaxLicenseEdit":
					inputBean = SoftwareModel.getKofaxLicenseBean(conn, inputBean);
					request.setAttribute("inputBean", inputBean);
					request.setAttribute("customerList", ProjectModel.getCustomerList(conn));
					request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));
					request.setAttribute("editType", "edit");
					request.setAttribute("customPageTitle", "Edit Kofax License " + inputBean.getLicenseKey());
					returnAction = "kofaxLicenseEdit";
					break;

				case "kofaxLicenseEditDo":
					operation = SoftwareModel.updateKofaxLicense(conn, inputBean, loginBean) ? "editSuccess" : "editFailed";
					logger.info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to kofaxLicenseList"));
					response.sendRedirect("software.do?action=kofaxLicenseList&projectPk=" + projectPk + "&operation=" + operation + "&licenseKey=" + CommonMethods.urlClean(inputBean.getLicenseKey()));
					returnAction = null;
					break;

				case "kofaxLicenseDeleteDo":
					inputBean = SoftwareModel.getKofaxLicenseBean(conn, inputBean);
					operation = SoftwareModel.deleteKofaxLicense(conn, inputBean) ? "deleteSuccess" : "deleteFailed";
					logger.info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to kofaxLicenseList"));
					response.sendRedirect("software.do?action=kofaxLicenseList&projectPk=" + projectPk + "&operation=" + operation + "&licenseKey=" + CommonMethods.urlClean(inputBean.getLicenseKey()));
					returnAction = null;
					break;

				case "bulkKofaxLicenseEdit":
					switch (CommonMethods.nes(request.getParameter("operation"))) {
						case "editSuccess"   : request.setAttribute("successMsg", "Successfully updated license expiration date");break;
						case "editFailed"    : request.setAttribute("errorMsg", "An error occurred while trying to update the license expiration date");break;
					} //end of switch

					request.setAttribute("resultList", SoftwareModel.getKofaxLicenseList(conn));
					returnAction = "bulkKofaxLicenseEdit";
					break;

				case "bulkKofaxLicenseEditDo":
					operation = SoftwareModel.bulkUpdateKofaxLicense(conn, inputBean, loginBean) ? "editSuccess" : "editFailed";
					logger.info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to kofaxLicenseList"));
					response.sendRedirect("software.do?action=bulkKofaxLicenseEdit&projectPk=" + projectPk + "&operation=" + operation);
					returnAction = null;
					break;

				case "vrsLicenseList":
					switch (CommonMethods.nes(request.getParameter("operation"))) {
						case "editSuccess"   : request.setAttribute("successMsg", "Successfully updated " + CommonMethods.nes(request.getParameter("licenseKey")));break;
						case "editFailed"    : request.setAttribute("errorMsg", "An error occurred while trying to update the license");break;
						case "deleteSuccess" : request.setAttribute("successMsg", "Successfully deleted " + CommonMethods.nes(request.getParameter("licenseKey")));break;
						case "deleteFailed"  : request.setAttribute("errorMsg", "An error occurred while trying to delete the license");break;
					} //end of switch

					request.setAttribute("resultList", SoftwareModel.getVrsLicenseList(conn));
					returnAction = "vrsLicenseList";
					break;

				case "vrsLicenseAdd":
					switch (CommonMethods.nes(request.getParameter("operation"))) {
						case "addSuccess" : request.setAttribute("successMsg", "Successfully inserted " + CommonMethods.nes(request.getParameter("licenseKey")));break;
						case "addFailed"  : request.setAttribute("errorMsg", "An error occurred while trying to insert the license");break;
					} //end of switch

					request.setAttribute("editType", "add");
					request.setAttribute("customPageTitle", "Add New VRS License");
					request.setAttribute("customerList", ProjectModel.getCustomerList(conn));
					request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));
					returnAction = "vrsLicenseAdd";
					break;

				case "vrsLicenseAddDo":
					operation = SoftwareModel.insertVrsLicense(conn, inputBean, loginBean) ? "addSuccess" : "addFailed";
					logger.info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to vrsLicenseAdd"));
					response.sendRedirect("software.do?action=vrsLicenseAdd&projectPk=" + projectPk + "&operation=" + operation + "&licenseKey=" + CommonMethods.urlClean(inputBean.getLicenseKey()));
					returnAction = null;
					break;

				case "vrsLicenseEdit":
					inputBean = SoftwareModel.getVrsLicenseBean(conn, inputBean);
					request.setAttribute("inputBean", inputBean);
					request.setAttribute("customerList", ProjectModel.getCustomerList(conn));
					request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));
					request.setAttribute("editType", "edit");
					request.setAttribute("customPageTitle", "Edit VRS License " + inputBean.getLicenseKey());
					returnAction = "vrsLicenseEdit";
					break;

				case "vrsLicenseEditDo":
					operation = SoftwareModel.updateVrsLicense(conn, inputBean, loginBean) ? "editSuccess" : "editFailed";
					logger.info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to vrsLicenseList"));
					response.sendRedirect("software.do?action=vrsLicenseList&projectPk=" + projectPk + "&operation=" + operation + "&licenseKey=" + CommonMethods.urlClean(inputBean.getLicenseKey()));
					returnAction = null;
					break;

				case "vrsLicenseDeleteDo":
					inputBean = SoftwareModel.getVrsLicenseBean(conn, inputBean);
					operation = SoftwareModel.deleteVrsLicense(conn, inputBean) ? "deleteSuccess" : "deleteFailed";
					logger.info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to vrsLicenseList"));
					response.sendRedirect("software.do?action=vrsLicenseList&projectPk=" + projectPk + "&operation=" + operation + "&licenseKey=" + CommonMethods.urlClean(inputBean.getLicenseKey()));
					returnAction = null;
					break;

				case "msOfficeLicenseList":
					switch (CommonMethods.nes(request.getParameter("operation"))) {
						case "addSuccess": 		request.setAttribute("successMsg", "Successfully inserted " + CommonMethods.nes(request.getParameter("licenseKey")));break;
						case "addFailed":   	request.setAttribute("errorMsg", "An error occurred while trying to insert the license");break;
						case "editSuccess": 	request.setAttribute("successMsg", "Successfully updated " + CommonMethods.nes(request.getParameter("licenseKey")));break;
						case "editFailed":  	request.setAttribute("errorMsg", "An error occurred while trying to update the license");break;
						case "deleteSuccess": request.setAttribute("successMsg", "Successfully deleted " + CommonMethods.nes(request.getParameter("licenseKey")));break;
						case "deleteFailed":  request.setAttribute("errorMsg", "An error occurred while trying to delete the license");break;
					} //end of switch

					request.setAttribute("resultList", SoftwareModel.getMsOfficeLicenseList(conn));
					returnAction = "msOfficeLicenseList";
					break;

				case "msOfficeLicenseAdd":
					request.setAttribute("productNameList", SoftwareModel.getMsOfficeProductNameList(conn));
					request.setAttribute("customerList", ProjectModel.getCustomerList(conn));
					request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));
					request.setAttribute("editType", "add");
					request.setAttribute("customPageTitle", "Add New MS Office License");
					returnAction = "msOfficeLicenseAdd";
					break;

				case "msOfficeLicenseAddDo":
					operation = SoftwareModel.insertMsOfficeLicense(conn, inputBean, loginBean) ? "addSuccess" : "addFailed";
					logger.info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to msOfficeLicenseList"));
					response.sendRedirect("software.do?action=msOfficeLicenseList&projectPk=" + projectPk + "&operation=" + operation + "&licenseKey=" + CommonMethods.urlClean(inputBean.getLicenseKey()));
					returnAction = null;
					break;

				case "msOfficeLicenseEdit":
					inputBean = SoftwareModel.getMsOfficeLicenseBean(conn, inputBean);
					request.setAttribute("productNameList", SoftwareModel.getMsOfficeProductNameList(conn));
					request.setAttribute("customerList", ProjectModel.getCustomerList(conn));
					request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));
					request.setAttribute("inputBean", inputBean);
					request.setAttribute("editType", "edit");
					request.setAttribute("customPageTitle", "Edit MS Office License " + inputBean.getLicenseKey());
					returnAction = "msOfficeLicenseEdit";
					break;

				case "msOfficeLicenseEditDo":
					operation = SoftwareModel.updateMsOfficeLicense(conn, inputBean, loginBean) ? "editSuccess" : "editFailed";
					logger.info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to msOfficeLicenseList"));
					response.sendRedirect("software.do?action=msOfficeLicenseList&projectPk=" + projectPk + "&operation=" + operation + "&licenseKey=" + CommonMethods.urlClean(inputBean.getLicenseKey()));
					returnAction = null;
					break;

				case "msOfficeLicenseDeleteDo":
					inputBean = SoftwareModel.getMsOfficeLicenseBean(conn, inputBean);
					operation = SoftwareModel.deleteMsOfficeLicense(conn, inputBean) ? "deleteSuccess" : "deleteFailed";
					logger.info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to msOfficeLicenseList"));
					response.sendRedirect("software.do?action=msOfficeLicenseList&projectPk=" + projectPk + "&operation=" + operation + "&licenseKey=" + CommonMethods.urlClean(inputBean.getLicenseKey()));
					returnAction = null;
					break;

				case "miscLicenseList":
					switch (CommonMethods.nes(request.getParameter("operation"))) {
						case "addSuccess": 		request.setAttribute("successMsg", "Successfully inserted " + CommonMethods.nes(request.getParameter("productName")));break;
						case "addFailed":   	request.setAttribute("errorMsg", "An error occurred while trying to insert the software");break;
						case "editSuccess": 	request.setAttribute("successMsg", "Successfully updated " + CommonMethods.nes(request.getParameter("productName")));break;
						case "editFailed":  	request.setAttribute("errorMsg", "An error occurred while trying to update the software");break;
						case "deleteSuccess": request.setAttribute("successMsg", "Successfully deleted " + CommonMethods.nes(request.getParameter("productName")));break;
						case "deleteFailed":  request.setAttribute("errorMsg", "An error occurred while trying to delete the software");break;
					} //end of switch

					request.setAttribute("resultList", SoftwareModel.getMiscLicenseList(conn));
					returnAction = "miscLicenseList";
					break;

				case "miscLicenseAdd":
					//Set default form values
					inputBean = new SoftwareBean();
					inputBean.setInstalledCnt("0");
					request.setAttribute("inputBean", inputBean);

					request.setAttribute("productNameList", SoftwareModel.getMiscProductNameList(conn));
					request.setAttribute("customerList", ProjectModel.getCustomerList(conn));
					request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));
					request.setAttribute("editType", "add");
					request.setAttribute("customPageTitle", "Add New Misc Software");
					returnAction = "miscLicenseAdd";
					break;

				case "miscLicenseAddDo":
					operation = SoftwareModel.insertMiscLicense(conn, inputBean, loginBean) ? "addSuccess" : "addFailed";
					logger.info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to miscLicenseList"));
					response.sendRedirect("software.do?action=miscLicenseList&projectPk=" + projectPk + "&operation=" + operation + "&productName=" + CommonMethods.urlClean(inputBean.getProductName()));
					returnAction = null;
					break;

				case "miscLicenseEdit":
					inputBean = SoftwareModel.getMiscLicenseBean(conn, inputBean);
					request.setAttribute("productNameList", SoftwareModel.getMiscProductNameList(conn));
					request.setAttribute("customerList", ProjectModel.getCustomerList(conn));
					request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));
					request.setAttribute("inputBean", inputBean);
					request.setAttribute("editType", "edit");
					request.setAttribute("customPageTitle", "Edit " + inputBean.getProductName());
					returnAction = "miscLicenseEdit";
					break;

				case "miscLicenseEditDo":
					operation = SoftwareModel.updateMiscLicense(conn, inputBean, loginBean) ? "editSuccess" : "editFailed";
					logger.info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to miscLicenseList"));
					response.sendRedirect("software.do?action=miscLicenseList&projectPk=" + projectPk + "&operation=" + operation + "&productName=" + CommonMethods.urlClean(inputBean.getProductName()));
					returnAction = null;
					break;

				case "miscLicenseDeleteDo":
					inputBean = SoftwareModel.getMiscLicenseBean(conn, inputBean);
					operation = SoftwareModel.deleteMiscLicense(conn, inputBean) ? "deleteSuccess" : "deleteFailed";
					logger.info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to miscLicenseList"));
					response.sendRedirect("software.do?action=miscLicenseList&projectPk=" + projectPk + "&operation=" + operation + "&productName=" + CommonMethods.urlClean(inputBean.getProductName()));
					returnAction = null;
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
			logger.error(String.format("%7s%-30s | %s", "", "ERROR", e));
			returnAction = "errorDatabaseDown";
		} catch (Exception e) {
			logger.error(String.format("%7s%-30s | %s", "", "ERROR", e));
			request.setAttribute("errorMsg", e.toString());
			returnAction = "errorUnexpected";
		} // end of catch

		double endTime = System.currentTimeMillis();
		String elapsedTime = nf1.format((endTime - startTime) / (double) 1000);
		logger.info(String.format("%5s%-32s | %-34s | %-52s | %s\r\n", "", "SOFTWARE NAVIGATION [END]", "Return: " + returnAction, "Username: " + CommonMethods.nvl(loginBean.getUsername(), "GUEST"), "Elapsed Time: " + elapsedTime + " sec"));

		return !CommonMethods.isEmpty(returnAction) ? mapping.findForward(returnAction) : null;
	} // end of execute
} // end of class
