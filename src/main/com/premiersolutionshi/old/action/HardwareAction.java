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

import com.premiersolutionshi.old.bean.HardwareBean;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.model.HardwareModel;
import com.premiersolutionshi.old.model.LoginModel;
import com.premiersolutionshi.old.model.ProjectModel;
import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Navigation logic for the application's HARDWARE process
 */
public final class HardwareAction extends Action {
	private static Logger logger = Logger.getLogger(HardwareAction.class.getSimpleName());
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
		HardwareBean inputBean = (HardwareBean) form;
		String operation = null, tagNum = null;//, sortBy = null, sortDir = null;

		int projectPk = CommonMethods.cInt(request.getParameter("projectPk"));
		if (projectPk == -1) action = "errorExpiredSession";

		double startTime = System.currentTimeMillis();

		try (Connection conn = CommonMethods.getConn(servlet, "jdbc/pshi")) {
			loginBean = LoginModel.getUserInfo(conn, request);

			logger.info(String.format("%5s%-32s | %-34s | %s", "", "HARDWARE NAVIGATION [START]", "Action: " + action, "Username: " + loginBean.getUsername()));

			/********************************** PREPARE ACTION **********************************/
			switch (action) {
				case "laptopList":
					switch (CommonMethods.nes(request.getParameter("operation"))) {
						case "editSuccess": 	request.setAttribute("successMsg", "Successfully updated " + CommonMethods.nes(request.getParameter("productName")) + (!CommonMethods.isEmpty(request.getParameter("tag")) ? " (" + request.getParameter("tag") + ")" : ""));break;
						case "editFailed":  	request.setAttribute("errorMsg", "An error occurred while trying to update the laptop");break;
						case "deleteSuccess": request.setAttribute("successMsg", "Successfully deleted " + CommonMethods.nes(request.getParameter("productName")) + (!CommonMethods.isEmpty(request.getParameter("tag")) ? " (" + request.getParameter("tag") + ")" : ""));break;
						case "deleteFailed":  request.setAttribute("errorMsg", "An error occurred while trying to delete the laptop");break;
					} //end of switch

					request.setAttribute("resultList", HardwareModel.getLaptopList(conn));
					returnAction = "laptopList";
					break;

				case "laptopAdd":
					switch (CommonMethods.nes(request.getParameter("operation"))) {
						case "addSuccess": 		request.setAttribute("successMsg", "Successfully inserted " + CommonMethods.nes(request.getParameter("productName")) + (!CommonMethods.isEmpty(request.getParameter("tag")) ? " (" + request.getParameter("tag") + ")" : ""));break;
						case "addFailed":   	request.setAttribute("errorMsg", "An error occurred while trying to insert the laptop");break;
					} //end of switch

					//Auto increment COMPUTER NAME and TAG
					inputBean = new HardwareBean();
					tagNum = HardwareModel.getNewLaptopTagNum(conn);
					inputBean.setComputerName("FACET" + tagNum);
					inputBean.setTag("L" + tagNum);

					request.setAttribute("inputBean", inputBean);
					request.setAttribute("productNameList", HardwareModel.getLaptopProductNameList(conn));
					request.setAttribute("modelNumberList", HardwareModel.getLaptopModelNumberList(conn));
					request.setAttribute("originList", HardwareModel.getLaptopOriginList(conn));
					request.setAttribute("customerList", ProjectModel.getCustomerList(conn));
					request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));
					request.setAttribute("editType", "add");
					request.setAttribute("customPageTitle", "Add New Laptop");
					returnAction = "laptopAdd";
					break;

				case "laptopAddDo":
					operation = HardwareModel.insertLaptop(conn, inputBean, loginBean) ? "addSuccess" : "addFailed";
					logger.info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to laptopAdd"));
					response.sendRedirect("hardware.do?action=laptopAdd&projectPk=" + projectPk + "&operation=" + operation + "&productName=" + CommonMethods.urlClean(inputBean.getProductName()) + "&tag=" + CommonMethods.urlClean(inputBean.getTag()));
					returnAction = null;
					break;

				case "laptopEdit":
					inputBean = HardwareModel.getLaptopBean(conn, inputBean);
					request.setAttribute("inputBean", inputBean);
					request.setAttribute("productNameList", HardwareModel.getLaptopProductNameList(conn));
					request.setAttribute("modelNumberList", HardwareModel.getLaptopModelNumberList(conn));
					request.setAttribute("originList", HardwareModel.getLaptopOriginList(conn));
					request.setAttribute("customerList", ProjectModel.getCustomerList(conn));
					request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));
					request.setAttribute("editType", "edit");
					request.setAttribute("customPageTitle", "Edit " + inputBean.getProductName() + (!CommonMethods.isEmpty(inputBean.getTag()) ? " (" + inputBean.getTag() + ")" : ""));
					returnAction = "laptopEdit";
					break;

				case "laptopEditDo":
					operation = HardwareModel.updateLaptop(conn, inputBean, loginBean) ? "editSuccess" : "editFailed";
					logger.info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to laptopList"));
					response.sendRedirect("hardware.do?action=laptopList&projectPk=" + projectPk + "&operation=" + operation + "&productName=" + CommonMethods.urlClean(inputBean.getProductName()) + "&tag=" + CommonMethods.urlClean(inputBean.getTag()));
					returnAction = null;
					break;

				case "laptopDeleteDo":
					inputBean = HardwareModel.getLaptopBean(conn, inputBean);
					operation = HardwareModel.deleteLaptop(conn, inputBean) ? "deleteSuccess" : "deleteFailed";
					logger.info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to laptopList"));
					response.sendRedirect("hardware.do?action=laptopList&projectPk=" + projectPk + "&operation=" + operation + "&productName=" + CommonMethods.urlClean(inputBean.getProductName()) + "&tag=" + CommonMethods.urlClean(inputBean.getTag()));
					returnAction = null;
					break;

				case "scannerList":
					switch (CommonMethods.nes(request.getParameter("operation"))) {
						case "editSuccess": 	request.setAttribute("successMsg", "Successfully updated " + CommonMethods.nes(request.getParameter("productName")) + (!CommonMethods.isEmpty(request.getParameter("tag")) ? " (" + request.getParameter("tag") + ")" : ""));break;
						case "editFailed":  	request.setAttribute("errorMsg", "An error occurred while trying to update the scanner");break;
						case "deleteSuccess": request.setAttribute("successMsg", "Successfully deleted " + CommonMethods.nes(request.getParameter("productName")) + (!CommonMethods.isEmpty(request.getParameter("tag")) ? " (" + request.getParameter("tag") + ")" : ""));break;
						case "deleteFailed":  request.setAttribute("errorMsg", "An error occurred while trying to delete the scanner");break;
					} //end of switch

					request.setAttribute("resultList", HardwareModel.getScannerList(conn));
					request.setAttribute("productNameList", HardwareModel.getScannerProductNameList(conn));
					request.setAttribute("originList", HardwareModel.getScannerOriginList(conn));
					returnAction = "scannerList";
					break;

				case "scannerAdd":
					switch (CommonMethods.nes(request.getParameter("operation"))) {
						case "addSuccess": 		request.setAttribute("successMsg", "Successfully inserted " + CommonMethods.nes(request.getParameter("productName")) + (!CommonMethods.isEmpty(request.getParameter("tag")) ? " (" + request.getParameter("tag") + ")" : ""));break;
						case "addFailed":   	request.setAttribute("errorMsg", "An error occurred while trying to insert the scanner");break;
					} //end of switch

					//Auto increment TAG
					inputBean = new HardwareBean();
					inputBean.setTag("S" + HardwareModel.getNewScannerTagNum(conn));

					request.setAttribute("inputBean", inputBean);
					request.setAttribute("productNameList", HardwareModel.getScannerProductNameList(conn));
					request.setAttribute("modelNumberList", HardwareModel.getScannerModelNumberList(conn));
					request.setAttribute("originList", HardwareModel.getScannerOriginList(conn));
					request.setAttribute("customerList", ProjectModel.getCustomerList(conn));
					request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));
					request.setAttribute("editType", "add");
					request.setAttribute("customPageTitle", "Add New Scanner");
					returnAction = "scannerAdd";
					break;

				case "scannerAddDo":
					operation = HardwareModel.insertScanner(conn, inputBean, loginBean) ? "addSuccess" : "addFailed";
					logger.info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to scannerAdd"));
					response.sendRedirect("hardware.do?action=scannerAdd&projectPk=" + projectPk + "&operation=" + operation + "&productName=" + CommonMethods.urlClean(inputBean.getProductName()) + "&tag=" + CommonMethods.urlClean(inputBean.getTag()));
					returnAction = null;
					break;

				case "scannerEdit":
					inputBean = HardwareModel.getScannerBean(conn, inputBean);
					request.setAttribute("inputBean", inputBean);
					request.setAttribute("productNameList", HardwareModel.getScannerProductNameList(conn));
					request.setAttribute("modelNumberList", HardwareModel.getScannerModelNumberList(conn));
					request.setAttribute("originList", HardwareModel.getScannerOriginList(conn));
					request.setAttribute("customerList", ProjectModel.getCustomerList(conn));
					request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));
					request.setAttribute("editType", "edit");
					request.setAttribute("customPageTitle", "Edit " + inputBean.getProductName() + (!CommonMethods.isEmpty(inputBean.getTag()) ? " (" + inputBean.getTag() + ")" : ""));
					returnAction = "scannerEdit";
					break;

				case "scannerEditDo":
					operation = HardwareModel.updateScanner(conn, inputBean, loginBean) ? "editSuccess" : "editFailed";
					logger.info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to scannerList"));
					response.sendRedirect("hardware.do?action=scannerList&projectPk=" + projectPk + "&operation=" + operation + "&productName=" + CommonMethods.urlClean(inputBean.getProductName()) + "&tag=" + CommonMethods.urlClean(inputBean.getTag()));
					returnAction = null;
					break;

				case "scannerDeleteDo":
					inputBean = HardwareModel.getScannerBean(conn, inputBean);
					operation = HardwareModel.deleteScanner(conn, inputBean) ? "deleteSuccess" : "deleteFailed";
					logger.info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to scannerList"));
					response.sendRedirect("hardware.do?action=scannerList&projectPk=" + projectPk + "&operation=" + operation + "&productName=" + CommonMethods.urlClean(inputBean.getProductName()) + "&tag=" + CommonMethods.urlClean(inputBean.getTag()));
					returnAction = null;
					break;

				case "miscList":
					switch (CommonMethods.nes(request.getParameter("operation"))) {
						case "addSuccess": 		request.setAttribute("successMsg", "Successfully inserted " + CommonMethods.nes(request.getParameter("productName")));break;
						case "addFailed":   	request.setAttribute("errorMsg", "An error occurred while trying to insert the misc hardware");break;
						case "editSuccess": 	request.setAttribute("successMsg", "Successfully updated " + CommonMethods.nes(request.getParameter("productName")));break;
						case "editFailed":  	request.setAttribute("errorMsg", "An error occurred while trying to update the misc hardware");break;
						case "deleteSuccess": request.setAttribute("successMsg", "Successfully deleted " + CommonMethods.nes(request.getParameter("productName")));break;
						case "deleteFailed":  request.setAttribute("errorMsg", "An error occurred while trying to delete the misc hardware");break;
					} //end of switch

					request.setAttribute("resultList", HardwareModel.getMiscList(conn));
					returnAction = "miscList";
					break;

				case "miscAdd":
					request.setAttribute("productTypeList", HardwareModel.getProductTypeList(conn));
					request.setAttribute("customerList", ProjectModel.getCustomerList(conn));
					request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));
					request.setAttribute("editType", "add");
					request.setAttribute("customPageTitle", "Add New Misc Hardware");
					returnAction = "miscAdd";
					break;

				case "miscAddDo":
					operation = HardwareModel.insertMisc(conn, inputBean, loginBean) ? "addSuccess" : "addFailed";
					logger.info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to miscList"));
					response.sendRedirect("hardware.do?action=miscList&projectPk=" + projectPk + "&operation=" + operation + "&productName=" + CommonMethods.urlClean(inputBean.getProductName()));
					returnAction = null;
					break;

				case "miscEdit":
					inputBean = HardwareModel.getMiscBean(conn, inputBean);
					request.setAttribute("inputBean", inputBean);
					request.setAttribute("productTypeList", HardwareModel.getProductTypeList(conn));
					request.setAttribute("customerList", ProjectModel.getCustomerList(conn));
					request.setAttribute("contractNumberList", ProjectModel.getContractNumberList(conn, projectPk));
					request.setAttribute("editType", "edit");
					request.setAttribute("customPageTitle", "Edit " + inputBean.getProductName());
					returnAction = "miscEdit";
					break;

				case "miscEditDo":
					operation = HardwareModel.updateMisc(conn, inputBean, loginBean) ? "editSuccess" : "editFailed";
					logger.info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to miscList"));
					response.sendRedirect("hardware.do?action=miscList&projectPk=" + projectPk + "&operation=" + operation + "&productName=" + CommonMethods.urlClean(inputBean.getProductName()));
					returnAction = null;
					break;

				case "miscDeleteDo":
					inputBean = HardwareModel.getMiscBean(conn, inputBean);
					operation = HardwareModel.deleteMisc(conn, inputBean) ? "deleteSuccess" : "deleteFailed";
					logger.info(String.format("%7s%-30s | %s", "", "INFO", "Redirecting to miscList"));
					response.sendRedirect("hardware.do?action=miscList&projectPk=" + projectPk + "&operation=" + operation + "&productName=" + CommonMethods.urlClean(inputBean.getProductName()));
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
		logger.info(String.format("%5s%-32s | %-34s | %-52s | %s\r\n", "", "HARDWARE NAVIGATION [END]", "Return: " + returnAction, "Username: " + CommonMethods.nvl(loginBean.getUsername(), "GUEST"), "Elapsed Time: " + elapsedTime + " sec"));

		return !CommonMethods.isEmpty(returnAction) ? mapping.findForward(returnAction) : null;
	} // end of execute
} // end of class
