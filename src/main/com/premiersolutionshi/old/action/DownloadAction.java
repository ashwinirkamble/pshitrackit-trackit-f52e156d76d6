package com.premiersolutionshi.old.action;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.premiersolutionshi.old.bean.FileBean;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.model.FileModel;
import com.premiersolutionshi.old.model.LoginModel;
import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Navigation logic for the application's FILE DOWNLOAD process
 *
 * @author   Anthony Tsuhako
 * @version  1.0, 11/25/2013
 * @since    JDK 7, Apache Struts 1.3.10
 */
public final class DownloadAction extends Action {
	private static Logger logger = Logger.getLogger(DownloadAction.class.getSimpleName());

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
		int filePk = CommonMethods.cInt(request.getParameter("filePk"));
		LoginBean loginBean = new LoginBean();
		String returnAction = null;

		long startTime = System.currentTimeMillis();

		try (Connection conn = CommonMethods.getConn(servlet, "jdbc/pshi")) {
			loginBean = LoginModel.getUserInfo(conn, request);

			logger.info(String.format("%4s%-32s | %-34s | %s", "", "DOWNLOAD FILE [START]", "filePk: " + filePk, "Username: " + loginBean.getUsername()));

			response.reset();
			FileBean fileBean = FileModel.getFileBean(conn, filePk);
			InputStream sFile = new FileInputStream(CommonMethods.getUploadDir(request) + "\\upload\\" + filePk);
			response.setContentType(fileBean.getContentType());
			response.addHeader("Content-Disposition", "attachment;filename=" + fileBean.getFilename());
			returnAction = null;

			if (sFile != null) {
				byte[] byteArr = new byte[512 * 1024]; // 512 KB buffer
				int byteRead = 0;
				while ((byteRead = sFile.read(byteArr)) != -1) {
					response.getOutputStream().write(byteArr, 0, byteRead);
				}
				response.flushBuffer();
			    sFile.close();
			}
		} catch (FileNotFoundException e) {
			logger.error(String.format("%6s%-30s | %s", "", "ERROR", e));
			returnAction = "error404";
		} catch (Exception e) {
			logger.error(String.format("%6s%-30s | %s", "", "ERROR", e));
			request.setAttribute("errorMsg", e.toString());
			returnAction = "errorUnepected";
		}

		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;
		logger.info(String.format("%4s%-32s | %-34s | %-52s | %s\r\n", "", "DOWNLOAD FILE [END]", "Return: " + CommonMethods.nvl(returnAction, "N/A"), "Username: " + loginBean.getUsername(), "Elapsed Time: " + (elapsedTime / 1000) + " sec"));

		return !CommonMethods.isEmpty(returnAction) ? mapping.findForward(returnAction) : null;
	} // end of execute
} // end of class