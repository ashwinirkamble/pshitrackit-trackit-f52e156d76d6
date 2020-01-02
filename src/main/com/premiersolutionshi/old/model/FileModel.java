package com.premiersolutionshi.old.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;

import com.premiersolutionshi.old.bean.FileBean;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Business logic for the application's LOOKUP module
 *
 * @author   Anthony Tsuhako
 * @version  1.0, 11/25/2013
 * @since    JDK 7, Apache Struts 1.3.10
 */
public final class FileModel {
	private static Logger logger = Logger.getLogger(FileModel.class.getSimpleName());

	/****************************************************************************
	 * Function: debugLog
	 ****************************************************************************/
	private static void debugLog(String type, String functionName, Exception e) {
		debugLog(type, functionName, e.toString());
	} //end of debugLog

	private static void debugLog(String type, String functionName, String statement) {
		if (type.equals("INFO") || type.equals("SQL")) {
			logger.info(String.format("%12s%-30s | %-34s | %s", "", type, functionName, statement));
		} else if (type.equals("ERROR")) {
			logger.error(String.format("%12s%-30s | %-34s | %s", "", type, functionName, statement));
		} else {
			logger.debug(String.format("%12s%-30s | %-34s | %s", "", type, functionName, statement));
		} //end of else
	} //end of debugLog

	/****************************************************************************
	 * Function: getFileBean
	 ****************************************************************************/
	public static FileBean getFileBean(Connection conn, int filePk) {
		String sqlStmt = "SELECT file_pk, filename, extension, filesize, content_type, uploaded_by, strftime('%m/%d/%Y %H:%M:%S', uploaded_date) as uploaded_date_fmt FROM file_info WHERE file_pk = ?";
		FileBean resultBean = new FileBean();

		debugLog("SQL", "getFileBean", sqlStmt);

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
			pStmt.setInt(1, filePk);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				resultBean.setFilePk(rs.getString("file_pk"));
				resultBean.setFilename(rs.getString("filename"));
				resultBean.setExtension(rs.getString("extension"));
				resultBean.setImage(CommonMethods.getFileIcon(resultBean.getExtension(), "lrg"));
				resultBean.setSmlImage(CommonMethods.getFileIcon(resultBean.getExtension(), "sml"));
				resultBean.setFilesize(rs.getString("filesize"));
				resultBean.setContentType(rs.getString("content_type"));
				resultBean.setUploadedBy(rs.getString("uploaded_by"));
				resultBean.setUploadedDate(rs.getString("uploaded_date_fmt"));
			} //end of if
		} catch (Exception e) {
			debugLog("ERROR", "getFileBean", e);
		} //end of catch

		return resultBean;
	} //end of getFileBean

	/****************************************************************************
	 * Function: getNewFilePk
	 ****************************************************************************/
	private static int getNewFilePk(Connection conn) throws Exception {
		String sqlStmt = "SELECT MAX(file_pk) FROM file_info";
		int returnVal = 1; //default to 1 if not found

		debugLog("SQL", "getNewFilePk", sqlStmt);

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
			ResultSet rs = pStmt.executeQuery();
			returnVal = rs.getInt(1) + 1;
		} catch (Exception e) {
			debugLog("ERROR", "getNewFilePk", e);
			throw e;
		} //end of catch

		return returnVal;
	} //end of getNewFilePk

	/****************************************************************************
	 * Function: saveFile
	 ****************************************************************************/
	public static int saveFile(Connection conn, FormFile f, LoginBean loginBean, String uploadDir) throws Exception {
		String sqlStmt = "INSERT INTO file_info (file_pk, filename, extension, content_type, filesize, uploaded_by, uploaded_date) VALUES (?,?,?,?,?,?,?)";
		int newFilePk = -1;
		boolean ranOk = false;

		String filename = f.getFileName();
		String extension = filename.lastIndexOf(".") > -1 ? filename.substring(filename.lastIndexOf(".") + 1) : "";
		if (extension.length() > 4) extension = extension.substring(0, 4);

		debugLog("SQL", "saveFile", sqlStmt);

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
			newFilePk = getNewFilePk(conn);
			if (newFilePk > -1 && f.getFileSize() > 0
		        && CommonMethods.writeBinaryFile(f.getInputStream(), uploadDir + "\\upload", String.valueOf(newFilePk))) {
				pStmt.setInt(1, newFilePk);
				pStmt.setString(2, filename);
				CommonMethods.setString(pStmt, 3, extension, 4);
				pStmt.setString(4, f.getContentType());
				pStmt.setInt(5, f.getFileSize());
				pStmt.setString(6, loginBean.getFullName());
				pStmt.setString(7, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
				ranOk = (pStmt.executeUpdate() == 1);
			} //end of if
		} catch (Exception e) {
			debugLog("ERROR", "saveFile", e);
			throw e;
		} //end of catch

		if (!ranOk || newFilePk <= -1) throw new Exception("Error occurred while saving file");

		return newFilePk;
	} //end of saveFile

	/****************************************************************************
	 * Function: getFilesToDeleteList
	 ****************************************************************************/
	private static ArrayList<Integer> getFilesToDeleteList(Connection conn) {
		String sqlStmt = "SELECT file_pk FROM file_info WHERE deleted = 1";
		ArrayList<Integer> resultList = new ArrayList<Integer>();

		try (PreparedStatement stmt = conn.prepareStatement(sqlStmt)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) resultList.add(rs.getInt(1));
		} catch (Exception e) {
			debugLog("SQL", "getFilesToDeleteList", sqlStmt);
			debugLog("ERROR", "getFilesToDeleteList", e);
		} //end of catch

		return resultList;
	} //end of getFilesToDeleteList

	/****************************************************************************
	 * Function: deleteFiles
	 ****************************************************************************/
	public static boolean deleteFiles(Connection conn, String uploadDir) {
		String sqlStmt = "DELETE FROM file_info WHERE file_pk = ?";
		boolean ranOk = true;

		ArrayList<Integer> filePkList = getFilesToDeleteList(conn);
		if (filePkList.size() <= 0) return true; //Nothing to delete

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
			for (Integer filePk : filePkList) {
				pStmt.setInt(1, filePk);
				debugLog("INFO", "deleteFiles", "Delete file #" + filePk);
				ranOk &= CommonMethods.deleteBinaryFile(String.valueOf(filePk), uploadDir + "\\upload") && pStmt.executeUpdate() == 1;
			} //end of for
		} catch (Exception e) {
			debugLog("SQL", "deleteFiles", sqlStmt);
			debugLog("ERROR", "deleteFiles", e);
			ranOk = false;
		} //end of catch

		return ranOk;
	} //end of deleteFiles
} //end of class
