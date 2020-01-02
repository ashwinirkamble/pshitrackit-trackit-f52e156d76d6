package com.premiersolutionshi.old.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.premiersolutionshi.old.bean.BackfileBean;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.bean.LookupBean;
import com.premiersolutionshi.old.bean.ShipBean;
import com.premiersolutionshi.old.util.CommonMethods;
/**
 * Business logic for the application's BACKFILE module
 *
 * @author   Anthony Tsuhako
 * @version  1.0, 11/25/2013
 * @since    JDK 7, Apache Struts 1.3.10
 */
public class BackfileModel {
	private static  Logger logger = Logger.getLogger(BackfileModel.class.getName());

	/****************************************************************************
	 * Function: debugLog
	 ****************************************************************************/
	private static void debugLog(String type, String functionName, Exception e) {
		debugLog(type, functionName, e.toString());
	} //end of debugLog

	private static void debugLog(String type, String functionName, String statement) {
		if (type.equals("INFO") || type.equals("SQL")) {
			logger.info(String.format("%8s%-30s | %-34s | %s", "", type, functionName, statement));
		} else if (type.equals("ERROR")) {
			logger.error(String.format("%8s%-30s | %-34s | %s", "", type, functionName, statement));
		} else {
			logger.debug(String.format("%8s%-30s | %-34s | %s", "", type, functionName, statement));
		} //end of else
	} //end of debugLog

	/****************************************************************************
	 * Function: getSummaryList
	 ****************************************************************************/
	public static ArrayList<BackfileBean> getSummaryList(Connection conn, String type) {
		return getSummaryList(conn, new BackfileBean(), "ship_name", "ASC", type);
	} //end of getSummaryList

	public static ArrayList<BackfileBean> getSummaryList(Connection conn, BackfileBean inputBean, String sortBy, String sortDir, String type) {
		StringBuffer sqlStmt = new StringBuffer("SELECT backfile_workflow_pk, ship_fk, ship_name, type, hull, homeport, tycom_display, sched_training_date_fmt, "
		        + "is_required, fy16_pshi_box_cnt, fy15_pshi_box_cnt, fy14_pshi_box_cnt, fy13_pshi_box_cnt, fy12_pshi_box_cnt, fy11_pshi_box_cnt, "
		        + "fy10_pshi_box_cnt, other_pshi_box_cnt, total_pshi_box_cnt, fy16_box_cnt, fy15_box_cnt, fy14_box_cnt, fy13_box_cnt, fy12_box_cnt, "
		        + "fy11_box_cnt, fy10_box_cnt, other_box_cnt, total_box_cnt, requested_date_fmt, received_date_fmt, scanning_delivered_date_fmt, "
		        + "fy1314_burned_date_fmt, fy1314_mailed_date_fmt, fy1314_completed_date_fmt, fy1112_completed_date_fmt, extract_date_fmt, "
		        + "logcop_delivered_date_fmt, logcop_uploaded_date_fmt, laptop_installed_date_fmt, final_report_date_fmt, est_completed_date_fmt, "
		        + "destruction_date_fmt, returned_date_fmt, return_confirmed_date_fmt, est_fy1314_completed_date_fmt, est_fy1112_completed_date_fmt, "
		        + "due_date_fmt, fy1314_due_date_fmt, completed_date_fmt, requested_date_css, received_date_css, scanning_delivered_date_css, "
		        + "fy1314_burned_date_css, fy1314_mailed_date_css, fy1314_completed_date_css, fy1112_completed_date_css, extract_date_css, "
		        + "logcop_delivered_date_css, logcop_uploaded_date_css, laptop_installed_date_css, final_report_date_css, destruction_date_css, returned_date_css, "
		        + "return_confirmed_date_css, return_ind, fy16_completed_date_fmt, fy16_completed_date_css, fy16_mailed_date_fmt, fy16_mailed_date_css, "
		        + "fy15_completed_date_fmt, fy15_completed_date_css, fy15_mailed_date_fmt, fy15_mailed_date_css, comments FROM backfile_workflow_vw WHERE 1=1");

		//WHERE
		if (!CommonMethods.isEmpty(inputBean.getContractNumber())) sqlStmt.append(" AND ship_fk IN (SELECT ship_pk FROM configured_system_vw WHERE contract_number = ?)");

		switch (CommonMethods.nes(type)) {
			case "pending"	  : sqlStmt.append(" AND is_required = 'Y' AND received_date IS NULL");break;
			case "inProd"		  : sqlStmt.append(" AND is_required = 'Y' AND received_date IS NOT NULL AND completed_date IS NULL");break;
			case "overdue"	  : sqlStmt.append(" AND is_required = 'Y' AND ((completed_date IS NULL AND due_date < date('now', '-10 hours')) OR (fy1314_completed_date IS NULL AND fy1314_due_date < date('now', '-10 hours')))");break;
			case "completed"  : sqlStmt.append(" AND is_required = 'Y' AND completed_date IS NOT NULL");break;
			case "notRequired": sqlStmt.append(" AND is_required = 'N'");break;
		} //end of switch

		//ORDER BY
		switch (sortBy + "_" + sortDir) {
			case "ship_name_ASC"	: sqlStmt.append(" ORDER BY ship_name ASC");break;
			case "ship_name_DESC"	: sqlStmt.append(" ORDER BY ship_name DESC");break;
			case "homeport_ASC"		: sqlStmt.append(" ORDER BY homeport ASC");break;
			case "homeport_DESC"	: sqlStmt.append(" ORDER BY homeport DESC");break;
			case "due_date_ASC"		: sqlStmt.append(" ORDER BY due_date IS NULL OR due_date='', due_date ASC");break;
			case "due_date_DESC"	: sqlStmt.append(" ORDER BY due_date IS NULL OR due_date='', due_date DESC");break;
			case "sched_training_date_ASC"		: sqlStmt.append(" ORDER BY sched_training_date IS NULL OR sched_training_date='', sched_training_date ASC");break;
			case "sched_training_date_DESC"	: sqlStmt.append(" ORDER BY sched_training_date IS NULL OR sched_training_date='', sched_training_date DESC");break;
			case "type_hull_ASC":				sqlStmt.append(" ORDER BY type ASC, hull ASC");break;
			case "type_hull_DESC":			sqlStmt.append(" ORDER BY type DESC, hull DESC");break;
			case "tycom_display_ASC":		sqlStmt.append(" ORDER BY tycom_display IS NULL OR tycom_display='', tycom_display ASC, ship_name");break;
			case "tycom_display_DESC":	sqlStmt.append(" ORDER BY tycom_display IS NULL OR tycom_display='', tycom_display DESC, ship_name");break;
		} //end of switch

		ArrayList<BackfileBean> resultList = new ArrayList<BackfileBean>();

		debugLog("SQL", "getSummaryList", sqlStmt.toString());

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
			if (!CommonMethods.isEmpty(inputBean.getContractNumber())) pStmt.setString(1, inputBean.getContractNumber());
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				BackfileBean resultBean = new BackfileBean();
				resultBean.setBackfileWorkflowPk(rs.getString("backfile_workflow_pk"));
				resultBean.setShipPk(rs.getString("ship_fk"));
				resultBean.setShipName(rs.getString("ship_name"));
				resultBean.setType(rs.getString("type"));
				resultBean.setHull(rs.getString("hull"));
				resultBean.setTycomDisplay(rs.getString("tycom_display"));
				resultBean.setHomeport(rs.getString("homeport"));
				//resultBean.setComputerName(rs.getString("computer_name"));

				resultBean.setIsRequired(rs.getString("is_required"));

				resultBean.setFy16PshiBoxCnt(rs.getString("fy16_pshi_box_cnt"));
				resultBean.setFy15PshiBoxCnt(rs.getString("fy15_pshi_box_cnt"));
				resultBean.setFy14PshiBoxCnt(rs.getString("fy14_pshi_box_cnt"));
				resultBean.setFy13PshiBoxCnt(rs.getString("fy13_pshi_box_cnt"));
				resultBean.setFy12PshiBoxCnt(rs.getString("fy12_pshi_box_cnt"));
				resultBean.setFy11PshiBoxCnt(rs.getString("fy11_pshi_box_cnt"));
				resultBean.setFy10PshiBoxCnt(rs.getString("fy10_pshi_box_cnt"));
				resultBean.setOtherPshiBoxCnt(rs.getString("other_pshi_box_cnt"));
				resultBean.setTotalPshiBoxCnt(rs.getString("total_pshi_box_cnt"));

				resultBean.setFy16BoxCnt(rs.getString("fy16_box_cnt"));
				resultBean.setFy15BoxCnt(rs.getString("fy15_box_cnt"));
				resultBean.setFy14BoxCnt(rs.getString("fy14_box_cnt"));
				resultBean.setFy13BoxCnt(rs.getString("fy13_box_cnt"));
				resultBean.setFy12BoxCnt(rs.getString("fy12_box_cnt"));
				resultBean.setFy11BoxCnt(rs.getString("fy11_box_cnt"));
				resultBean.setFy10BoxCnt(rs.getString("fy10_box_cnt"));
				resultBean.setOtherBoxCnt(rs.getString("other_box_cnt"));
				resultBean.setTotalBoxCnt(rs.getString("total_box_cnt"));

				resultBean.setSchedTrainingDate(rs.getString("sched_training_date_fmt"));

				resultBean.setRequestedDate(rs.getString("requested_date_fmt"));
				resultBean.setReceivedDate(rs.getString("received_date_fmt"));
				resultBean.setScanningDeliveredDate(rs.getString("scanning_delivered_date_fmt"));
				resultBean.setFy1314BurnedDate(rs.getString("fy1314_burned_date_fmt"));
				resultBean.setFy1314MailedDate(rs.getString("fy1314_mailed_date_fmt"));
				resultBean.setFy1314CompletedDate(rs.getString("fy1314_completed_date_fmt"));
				resultBean.setFy1112CompletedDate(rs.getString("fy1112_completed_date_fmt"));
				resultBean.setExtractDate(rs.getString("extract_date_fmt"));
				resultBean.setLogcopDeliveredDate(rs.getString("logcop_delivered_date_fmt"));
				resultBean.setLogcopUploadedDate(rs.getString("logcop_uploaded_date_fmt"));
				resultBean.setLaptopInstalledDate(rs.getString("laptop_installed_date_fmt"));
				resultBean.setFinalReportDate(rs.getString("final_report_date_fmt"));
				resultBean.setDestructionDate(rs.getString("destruction_date_fmt"));
				resultBean.setReturnedDate(rs.getString("returned_date_fmt"));
				resultBean.setReturnConfirmedDate(rs.getString("return_confirmed_date_fmt"));

				resultBean.setEstCompletedDate(rs.getString("est_completed_date_fmt"));
				resultBean.setEstFy1314CompletedDate(rs.getString("est_fy1314_completed_date_fmt"));
				resultBean.setEstFy1112CompletedDate(rs.getString("est_fy1112_completed_date_fmt"));
				resultBean.setDueDate(rs.getString("due_date_fmt"));
				resultBean.setCompletedDate(rs.getString("completed_date_fmt"));

				resultBean.setRequestedDateCss(rs.getString("requested_date_css"));
				resultBean.setReceivedDateCss(rs.getString("received_date_css"));
				resultBean.setScanningDeliveredDateCss(rs.getString("scanning_delivered_date_css"));
				resultBean.setFy1314BurnedDateCss(rs.getString("fy1314_burned_date_css"));
				resultBean.setFy1314MailedDateCss(rs.getString("fy1314_mailed_date_css"));
				resultBean.setFy1314CompletedDateCss(rs.getString("fy1314_completed_date_css"));
				resultBean.setFy1112CompletedDateCss(rs.getString("fy1112_completed_date_css"));
				resultBean.setExtractDateCss(rs.getString("extract_date_css"));
				resultBean.setLogcopDeliveredDateCss(rs.getString("logcop_delivered_date_css"));
				resultBean.setLogcopUploadedDateCss(rs.getString("logcop_uploaded_date_css"));
				resultBean.setLaptopInstalledDateCss(rs.getString("laptop_installed_date_css"));
				resultBean.setFinalReportDateCss(rs.getString("final_report_date_css"));
				resultBean.setDestructionDateCss(rs.getString("destruction_date_css"));
				resultBean.setReturnedDateCss(rs.getString("returned_date_css"));
				resultBean.setReturnConfirmedDateCss(rs.getString("return_confirmed_date_css"));

				if (CommonMethods.isValidDateStr(resultBean.getDueDate()) && CommonMethods.isEmpty(resultBean.getCompletedDate()) && CommonMethods.dateDiff(CommonMethods.getDate("MM/DD/YYYY"), resultBean.getDueDate()) <= 0) {
					resultBean.setDueDateCss("color:#f00;");
				} //end of if

				resultBean.setFy1314DueDate(rs.getString("fy1314_due_date_fmt"));

				if (CommonMethods.isValidDateStr(resultBean.getFy1314DueDate()) && CommonMethods.isEmpty(resultBean.getFy1314CompletedDate()) && CommonMethods.dateDiff(CommonMethods.getDate("MM/DD/YYYY"), resultBean.getFy1314DueDate()) <= 0) {
					resultBean.setFy1314DueDateCss("color:#f00;");
				} //end of if

				resultBean.setReturnInd(rs.getString("return_ind"));

				resultBean.setFy16CompletedDate(rs.getString("fy16_completed_date_fmt"));
				resultBean.setFy16CompletedDateCss(rs.getString("fy16_completed_date_css"));
				resultBean.setFy16MailedDate(rs.getString("fy16_mailed_date_fmt"));
				resultBean.setFy16MailedDateCss(rs.getString("fy16_mailed_date_css"));
				resultBean.setFy15CompletedDate(rs.getString("fy15_completed_date_fmt"));
				resultBean.setFy15CompletedDateCss(rs.getString("fy15_completed_date_css"));
				resultBean.setFy15MailedDate(rs.getString("fy15_mailed_date_fmt"));
				resultBean.setFy15MailedDateCss(rs.getString("fy15_mailed_date_css"));

				resultBean.setComments(rs.getString("comments"));
				resultList.add(resultBean);
			} //end of while
		} catch (Exception e) {
			debugLog("ERROR", "getSummaryList", e);
		} //end of catch

		return resultList;
	} //end of getSummaryList

	/****************************************************************************
	 * Function: getWorkflowMap
	 ****************************************************************************/
	public static HashMap<String, BackfileBean> getWorkflowMap(Connection conn) {
		String sqlStmt = "SELECT backfile_workflow_pk, ship_fk, ship_name, uic, type, hull, homeport, tycom_display, sched_training_date_fmt, is_required, fy16_pshi_box_cnt, fy15_pshi_box_cnt, fy14_pshi_box_cnt, fy13_pshi_box_cnt, fy12_pshi_box_cnt, fy11_pshi_box_cnt, fy10_pshi_box_cnt, other_pshi_box_cnt, total_pshi_box_cnt, fy16_box_cnt, fy15_box_cnt, fy14_box_cnt, fy13_box_cnt, fy12_box_cnt, fy11_box_cnt, fy10_box_cnt, other_box_cnt, total_box_cnt, requested_date_fmt, received_date_fmt, scanning_delivered_date_fmt, fy1314_burned_date_fmt, fy1314_mailed_date_fmt, fy1314_completed_date_fmt, fy1112_completed_date_fmt, extract_date_fmt, logcop_delivered_date_fmt, logcop_uploaded_date_fmt, laptop_installed_date_fmt, final_report_date_fmt, est_completed_date_fmt, destruction_date_fmt, returned_date_fmt, return_confirmed_date_fmt, est_fy1314_completed_date_fmt, est_fy1112_completed_date_fmt, due_date_fmt, fy1314_due_date_fmt, completed_date_fmt, requested_date_css, received_date_css, scanning_delivered_date_css, fy1314_burned_date_css, fy1314_mailed_date_css, fy1314_completed_date_css, fy1112_completed_date_css, extract_date_css, logcop_delivered_date_css, logcop_uploaded_date_css, laptop_installed_date_css, final_report_date_css, destruction_date_css, returned_date_css, return_confirmed_date_css, return_ind, fy16_completed_date_fmt, fy16_completed_date_css, fy16_mailed_date_fmt, fy16_mailed_date_css, fy15_completed_date_fmt, fy15_completed_date_css, fy15_mailed_date_fmt, fy15_mailed_date_css, comments FROM backfile_workflow_vw";
		HashMap<String, BackfileBean> resultMap = new HashMap<String, BackfileBean>();

		debugLog("SQL", "getWorkflowMap", sqlStmt);

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				BackfileBean resultBean = new BackfileBean();
				resultBean.setBackfileWorkflowPk(rs.getString("backfile_workflow_pk"));
				resultBean.setShipPk(rs.getString("ship_fk"));
				resultBean.setShipName(rs.getString("ship_name"));
				resultBean.setType(rs.getString("type"));
				resultBean.setHull(rs.getString("hull"));
				resultBean.setTycomDisplay(rs.getString("tycom_display"));
				resultBean.setHomeport(rs.getString("homeport"));
				//resultBean.setComputerName(rs.getString("computer_name"));

				resultBean.setIsRequired(rs.getString("is_required"));

				resultBean.setFy16PshiBoxCnt(rs.getString("fy16_pshi_box_cnt"));
				resultBean.setFy15PshiBoxCnt(rs.getString("fy15_pshi_box_cnt"));
				resultBean.setFy14PshiBoxCnt(rs.getString("fy14_pshi_box_cnt"));
				resultBean.setFy13PshiBoxCnt(rs.getString("fy13_pshi_box_cnt"));
				resultBean.setFy12PshiBoxCnt(rs.getString("fy12_pshi_box_cnt"));
				resultBean.setFy11PshiBoxCnt(rs.getString("fy11_pshi_box_cnt"));
				resultBean.setFy10PshiBoxCnt(rs.getString("fy10_pshi_box_cnt"));
				resultBean.setOtherPshiBoxCnt(rs.getString("other_pshi_box_cnt"));
				resultBean.setTotalPshiBoxCnt(rs.getString("total_pshi_box_cnt"));

				resultBean.setFy16BoxCnt(rs.getString("fy16_box_cnt"));
				resultBean.setFy15BoxCnt(rs.getString("fy15_box_cnt"));
				resultBean.setFy14BoxCnt(rs.getString("fy14_box_cnt"));
				resultBean.setFy13BoxCnt(rs.getString("fy13_box_cnt"));
				resultBean.setFy12BoxCnt(rs.getString("fy12_box_cnt"));
				resultBean.setFy11BoxCnt(rs.getString("fy11_box_cnt"));
				resultBean.setFy10BoxCnt(rs.getString("fy10_box_cnt"));
				resultBean.setOtherBoxCnt(rs.getString("other_box_cnt"));
				resultBean.setTotalBoxCnt(rs.getString("total_box_cnt"));

				resultBean.setSchedTrainingDate(rs.getString("sched_training_date_fmt"));

				resultBean.setRequestedDate(rs.getString("requested_date_fmt"));
				resultBean.setReceivedDate(rs.getString("received_date_fmt"));
				resultBean.setScanningDeliveredDate(rs.getString("scanning_delivered_date_fmt"));
				resultBean.setFy1314BurnedDate(rs.getString("fy1314_burned_date_fmt"));
				resultBean.setFy1314MailedDate(rs.getString("fy1314_mailed_date_fmt"));
				resultBean.setFy1314CompletedDate(rs.getString("fy1314_completed_date_fmt"));
				resultBean.setFy1112CompletedDate(rs.getString("fy1112_completed_date_fmt"));
				resultBean.setExtractDate(rs.getString("extract_date_fmt"));
				resultBean.setLogcopDeliveredDate(rs.getString("logcop_delivered_date_fmt"));
				resultBean.setLogcopUploadedDate(rs.getString("logcop_uploaded_date_fmt"));
				resultBean.setLaptopInstalledDate(rs.getString("laptop_installed_date_fmt"));
				resultBean.setFinalReportDate(rs.getString("final_report_date_fmt"));
				resultBean.setDestructionDate(rs.getString("destruction_date_fmt"));
				resultBean.setReturnedDate(rs.getString("returned_date_fmt"));
				resultBean.setReturnConfirmedDate(rs.getString("return_confirmed_date_fmt"));

				resultBean.setEstCompletedDate(rs.getString("est_completed_date_fmt"));
				resultBean.setEstFy1314CompletedDate(rs.getString("est_fy1314_completed_date_fmt"));
				resultBean.setEstFy1112CompletedDate(rs.getString("est_fy1112_completed_date_fmt"));
				resultBean.setDueDate(rs.getString("due_date_fmt"));
				resultBean.setCompletedDate(rs.getString("completed_date_fmt"));

				resultBean.setRequestedDateCss(rs.getString("requested_date_css"));
				resultBean.setReceivedDateCss(rs.getString("received_date_css"));
				resultBean.setScanningDeliveredDateCss(rs.getString("scanning_delivered_date_css"));
				resultBean.setFy1314BurnedDateCss(rs.getString("fy1314_burned_date_css"));
				resultBean.setFy1314MailedDateCss(rs.getString("fy1314_mailed_date_css"));
				resultBean.setFy1314CompletedDateCss(rs.getString("fy1314_completed_date_css"));
				resultBean.setFy1112CompletedDateCss(rs.getString("fy1112_completed_date_css"));
				resultBean.setExtractDateCss(rs.getString("extract_date_css"));
				resultBean.setLogcopDeliveredDateCss(rs.getString("logcop_delivered_date_css"));
				resultBean.setLogcopUploadedDateCss(rs.getString("logcop_uploaded_date_css"));
				resultBean.setLaptopInstalledDateCss(rs.getString("laptop_installed_date_css"));
				resultBean.setFinalReportDateCss(rs.getString("final_report_date_css"));
				resultBean.setDestructionDateCss(rs.getString("destruction_date_css"));
				resultBean.setReturnedDateCss(rs.getString("returned_date_css"));
				resultBean.setReturnConfirmedDateCss(rs.getString("return_confirmed_date_css"));

				if (CommonMethods.isValidDateStr(resultBean.getDueDate()) && CommonMethods.isEmpty(resultBean.getCompletedDate()) && CommonMethods.dateDiff(CommonMethods.getDate("MM/DD/YYYY"), resultBean.getDueDate()) <= 0) {
					resultBean.setDueDateCss("color:#f00;");
				} //end of if

				resultBean.setFy1314DueDate(rs.getString("fy1314_due_date_fmt"));

				if (CommonMethods.isValidDateStr(resultBean.getFy1314DueDate()) && CommonMethods.isEmpty(resultBean.getFy1314CompletedDate()) && CommonMethods.dateDiff(CommonMethods.getDate("MM/DD/YYYY"), resultBean.getFy1314DueDate()) <= 0) {
					resultBean.setFy1314DueDateCss("color:#f00;");
				} //end of if

				resultBean.setReturnInd(rs.getString("return_ind"));

				resultBean.setFy16CompletedDate(rs.getString("fy16_completed_date_fmt"));
				resultBean.setFy16CompletedDateCss(rs.getString("fy16_completed_date_css"));
				resultBean.setFy16MailedDate(rs.getString("fy16_mailed_date_fmt"));
				resultBean.setFy16MailedDateCss(rs.getString("fy16_mailed_date_css"));
				resultBean.setFy15CompletedDate(rs.getString("fy15_completed_date_fmt"));
				resultBean.setFy15CompletedDateCss(rs.getString("fy15_completed_date_css"));
				resultBean.setFy15MailedDate(rs.getString("fy15_mailed_date_fmt"));
				resultBean.setFy15MailedDateCss(rs.getString("fy15_mailed_date_css"));

				resultBean.setComments(rs.getString("comments"));
				resultMap.put(rs.getString("uic"), resultBean);
			} //end of while
		} catch (Exception e) {
			debugLog("ERROR", "getWorkflowMap", e);
		} //end of catch

		return resultMap;
	} //end of getWorkflowMap

	/****************************************************************************
	 * Function: getWorkflowBean
	 ****************************************************************************/
	public static BackfileBean getWorkflowBean(Connection conn, int backfileWorkflowPk) {
		return getWorkflowBean(conn, backfileWorkflowPk, null);
	} //end of getWorkflowBean

	public static BackfileBean getWorkflowBean(Connection conn, String uic) {
		return getWorkflowBean(conn, -1, uic);
	} //end of getWorkflowBean

	private static BackfileBean getWorkflowBean(Connection conn, int backfileWorkflowPk, String uic) {
		String sqlStmt = "SELECT backfile_workflow_pk, ship_fk, ship_name, type, hull, sched_training_date_fmt, is_required, fy16_pshi_box_cnt, fy15_pshi_box_cnt, fy14_pshi_box_cnt, fy13_pshi_box_cnt, fy12_pshi_box_cnt, fy11_pshi_box_cnt, fy10_pshi_box_cnt, other_pshi_box_cnt, fy16_box_cnt, fy15_box_cnt, fy14_box_cnt, fy13_box_cnt, fy12_box_cnt, fy11_box_cnt, fy10_box_cnt, other_box_cnt, requested_date_fmt, received_date_fmt, scanning_delivered_date_fmt, fy1314_burned_date_fmt, fy1314_mailed_date_fmt, fy1314_completed_date_fmt, fy1112_completed_date_fmt, extract_date_fmt, logcop_delivered_date_fmt, logcop_uploaded_date_fmt, laptop_installed_date_fmt, final_report_date_fmt, est_completed_date_fmt, destruction_date_fmt, returned_date_fmt, return_confirmed_date_fmt, est_fy1314_completed_date_fmt, est_fy1112_completed_date_fmt, due_date_fmt, fy1314_due_date_fmt, requested_date_css, received_date_css, scanning_delivered_date_css, fy1314_burned_date_css, fy1314_mailed_date_css, fy1314_completed_date_css, fy1112_completed_date_css, extract_date_css, logcop_delivered_date_css, logcop_uploaded_date_css, laptop_installed_date_css, final_report_date_css, destruction_date_css, returned_date_css, return_confirmed_date_css, return_ind, fy16_completed_date_fmt, fy16_completed_date_css, fy16_mailed_date_fmt, fy16_mailed_date_css, fy15_completed_date_fmt, fy15_completed_date_css, fy15_mailed_date_fmt, fy15_mailed_date_css, comments FROM backfile_workflow_vw";
		String sqlWhere = backfileWorkflowPk > -1 ? " WHERE backfile_workflow_pk = ?" : " WHERE uic = ?";
		BackfileBean resultBean = null;

		if (backfileWorkflowPk <= -1 && CommonMethods.isEmpty(uic)) return null;

		debugLog("SQL", "getWorkflowBean", sqlStmt + sqlWhere + (backfileWorkflowPk > -1 ? " (backfile_workflow_pk = " + backfileWorkflowPk + ")" : " (uic = " + CommonMethods.nes(uic) + ")"));

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt + sqlWhere)) {
			if (backfileWorkflowPk > -1)
				pStmt.setInt(1, backfileWorkflowPk);
			else
				pStmt.setString(1, uic);

			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				resultBean = new BackfileBean();
				resultBean.setBackfileWorkflowPk(rs.getString("backfile_workflow_pk"));
				resultBean.setShipPk(rs.getString("ship_fk"));
				resultBean.setShipName(rs.getString("ship_name"));

				resultBean.setIsRequired(rs.getString("is_required"));

				resultBean.setFy16PshiBoxCnt(rs.getString("fy16_pshi_box_cnt"));
				resultBean.setFy15PshiBoxCnt(rs.getString("fy15_pshi_box_cnt"));
				resultBean.setFy14PshiBoxCnt(rs.getString("fy14_pshi_box_cnt"));
				resultBean.setFy13PshiBoxCnt(rs.getString("fy13_pshi_box_cnt"));
				resultBean.setFy12PshiBoxCnt(rs.getString("fy12_pshi_box_cnt"));
				resultBean.setFy11PshiBoxCnt(rs.getString("fy11_pshi_box_cnt"));
				resultBean.setFy10PshiBoxCnt(rs.getString("fy10_pshi_box_cnt"));
				resultBean.setOtherPshiBoxCnt(rs.getString("other_pshi_box_cnt"));

				resultBean.setFy16BoxCnt(rs.getString("fy16_box_cnt"));
				resultBean.setFy15BoxCnt(rs.getString("fy15_box_cnt"));
				resultBean.setFy14BoxCnt(rs.getString("fy14_box_cnt"));
				resultBean.setFy13BoxCnt(rs.getString("fy13_box_cnt"));
				resultBean.setFy12BoxCnt(rs.getString("fy12_box_cnt"));
				resultBean.setFy11BoxCnt(rs.getString("fy11_box_cnt"));
				resultBean.setFy10BoxCnt(rs.getString("fy10_box_cnt"));
				resultBean.setOtherBoxCnt(rs.getString("other_box_cnt"));

				resultBean.setSchedTrainingDate(rs.getString("sched_training_date_fmt"));

				resultBean.setRequestedDate(rs.getString("requested_date_fmt"));
				resultBean.setReceivedDate(rs.getString("received_date_fmt"));
				resultBean.setScanningDeliveredDate(rs.getString("scanning_delivered_date_fmt"));
				resultBean.setFy1314BurnedDate(rs.getString("fy1314_burned_date_fmt"));
				resultBean.setFy1314MailedDate(rs.getString("fy1314_mailed_date_fmt"));
				resultBean.setFy1314CompletedDate(rs.getString("fy1314_completed_date_fmt"));
				resultBean.setFy1112CompletedDate(rs.getString("fy1112_completed_date_fmt"));
				resultBean.setExtractDate(rs.getString("extract_date_fmt"));
				resultBean.setLogcopDeliveredDate(rs.getString("logcop_delivered_date_fmt"));
				resultBean.setLogcopUploadedDate(rs.getString("logcop_uploaded_date_fmt"));
				resultBean.setLaptopInstalledDate(rs.getString("laptop_installed_date_fmt"));
				resultBean.setFinalReportDate(rs.getString("final_report_date_fmt"));
				resultBean.setDestructionDate(rs.getString("destruction_date_fmt"));
				resultBean.setReturnedDate(rs.getString("returned_date_fmt"));
				resultBean.setReturnConfirmedDate(rs.getString("return_confirmed_date_fmt"));

				resultBean.setEstCompletedDate(rs.getString("est_completed_date_fmt"));
				resultBean.setEstFy1314CompletedDate(rs.getString("est_fy1314_completed_date_fmt"));
				resultBean.setEstFy1112CompletedDate(rs.getString("est_fy1112_completed_date_fmt"));

				resultBean.setRequestedDateCss(rs.getString("requested_date_css"));
				resultBean.setReceivedDateCss(rs.getString("received_date_css"));
				resultBean.setScanningDeliveredDateCss(rs.getString("scanning_delivered_date_css"));
				resultBean.setFy1314BurnedDateCss(rs.getString("fy1314_burned_date_css"));
				resultBean.setFy1314MailedDateCss(rs.getString("fy1314_mailed_date_css"));
				resultBean.setFy1314CompletedDateCss(rs.getString("fy1314_completed_date_css"));
				resultBean.setFy1112CompletedDateCss(rs.getString("fy1112_completed_date_css"));
				resultBean.setExtractDateCss(rs.getString("extract_date_css"));
				resultBean.setLogcopDeliveredDateCss(rs.getString("logcop_delivered_date_css"));
				resultBean.setLogcopUploadedDateCss(rs.getString("logcop_uploaded_date_css"));
				resultBean.setLaptopInstalledDateCss(rs.getString("laptop_installed_date_css"));
				resultBean.setFinalReportDateCss(rs.getString("final_report_date_css"));
				resultBean.setDestructionDateCss(rs.getString("destruction_date_css"));
				resultBean.setReturnedDateCss(rs.getString("returned_date_css"));
				resultBean.setReturnConfirmedDateCss(rs.getString("return_confirmed_date_css"));

				resultBean.setReturnInd(rs.getString("return_ind"));

				resultBean.setFy16CompletedDate(rs.getString("fy16_completed_date_fmt"));
				resultBean.setFy16CompletedDateCss(rs.getString("fy16_completed_date_css"));
				resultBean.setFy16MailedDate(rs.getString("fy16_mailed_date_fmt"));
				resultBean.setFy16MailedDateCss(rs.getString("fy16_mailed_date_css"));
				resultBean.setFy15CompletedDate(rs.getString("fy15_completed_date_fmt"));
				resultBean.setFy15CompletedDateCss(rs.getString("fy15_completed_date_css"));
				resultBean.setFy15MailedDate(rs.getString("fy15_mailed_date_fmt"));
				resultBean.setFy15MailedDateCss(rs.getString("fy15_mailed_date_css"));

				resultBean.setComments(rs.getString("comments"));
			} //end of if
		} catch (Exception e) {
			debugLog("ERROR", "getWorkflowBean", e);
		} //end of catch

		return resultBean;
	} //end of getWorkflowBean

	/****************************************************************************
	 * Function: insertWorkflow
	 ****************************************************************************/
	public static boolean insertWorkflow(Connection conn, BackfileBean inputBean, LoginBean loginBean) {
		String sqlStmt = "INSERT INTO backfile_workflow (ship_fk, fy16_pshi_box_cnt, fy15_pshi_box_cnt, fy14_pshi_box_cnt, fy13_pshi_box_cnt, fy12_pshi_box_cnt, fy11_pshi_box_cnt, fy10_pshi_box_cnt, other_pshi_box_cnt, fy16_box_cnt, fy15_box_cnt, fy14_box_cnt, fy13_box_cnt, fy12_box_cnt, fy11_box_cnt, fy10_box_cnt, other_box_cnt, requested_date, received_date, scanning_delivered_date, fy1314_burned_date, fy1314_mailed_date, fy1314_completed_date, fy1112_completed_date, extract_date, logcop_delivered_date, logcop_uploaded_date, laptop_installed_date, final_report_date, destruction_date, est_completed_date, est_fy1314_completed_date, est_fy1112_completed_date, requested_date_css, received_date_css, scanning_delivered_date_css, fy1314_burned_date_css, fy1314_mailed_date_css, fy1314_completed_date_css, fy1112_completed_date_css, extract_date_css, logcop_delivered_date_css, logcop_uploaded_date_css, laptop_installed_date_css, final_report_date_css, destruction_date_css, return_ind, returned_date, returned_date_css, return_confirmed_date, return_confirmed_date_css, fy16_completed_date, fy16_completed_date_css, fy16_mailed_date, fy16_mailed_date_css, fy15_completed_date, fy15_completed_date_css, fy15_mailed_date, fy15_mailed_date_css, is_required, comments, last_updated_by, last_updated_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		boolean ranOk = false;

		debugLog("SQL", "insertWorkflow", sqlStmt);

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
			conn.setAutoCommit(false);
			CommonMethods.setInt(pStmt, 1, inputBean.getShipPk());

			if (CommonMethods.nvl(inputBean.getIsRequired(), "N").equals("N")) { //Backfile NOT required - null all fields
				for (int i = 2; i <= 59; i++) {
					pStmt.setNull(i, java.sql.Types.VARCHAR);
				} //end of for

				pStmt.setString(60, "N");
			} else { //Backfile required - set all fields
				CommonMethods.setInt(pStmt, 2, inputBean.getFy16PshiBoxCnt());
				CommonMethods.setInt(pStmt, 3, inputBean.getFy15PshiBoxCnt());
				CommonMethods.setInt(pStmt, 4, inputBean.getFy14PshiBoxCnt());
				CommonMethods.setInt(pStmt, 5, inputBean.getFy13PshiBoxCnt());
				CommonMethods.setInt(pStmt, 6, inputBean.getFy12PshiBoxCnt());
				CommonMethods.setInt(pStmt, 7, inputBean.getFy11PshiBoxCnt());
				CommonMethods.setInt(pStmt, 8, inputBean.getFy10PshiBoxCnt());
				CommonMethods.setInt(pStmt, 9, inputBean.getOtherPshiBoxCnt());

				CommonMethods.setInt(pStmt, 10, inputBean.getFy16BoxCnt());
				CommonMethods.setInt(pStmt, 11, inputBean.getFy15BoxCnt());
				CommonMethods.setInt(pStmt, 12, inputBean.getFy14BoxCnt());
				CommonMethods.setInt(pStmt, 13, inputBean.getFy13BoxCnt());
				CommonMethods.setInt(pStmt, 14, inputBean.getFy12BoxCnt());
				CommonMethods.setInt(pStmt, 15, inputBean.getFy11BoxCnt());
				CommonMethods.setInt(pStmt, 16, inputBean.getFy10BoxCnt());
				CommonMethods.setInt(pStmt, 17, inputBean.getOtherBoxCnt());

				CommonMethods.setDate(pStmt, 18, inputBean.getRequestedDate());
				CommonMethods.setDate(pStmt, 19, inputBean.getReceivedDate());
				CommonMethods.setDate(pStmt, 20, inputBean.getScanningDeliveredDate());
				CommonMethods.setDate(pStmt, 21, inputBean.getFy1314BurnedDate());
				CommonMethods.setDate(pStmt, 22, inputBean.getFy1314MailedDate());
				CommonMethods.setDate(pStmt, 23, inputBean.getFy1314CompletedDate());
				CommonMethods.setDate(pStmt, 24, inputBean.getFy1112CompletedDate());
				CommonMethods.setDate(pStmt, 25, inputBean.getExtractDate());
				CommonMethods.setDate(pStmt, 26, inputBean.getLogcopDeliveredDate());
				CommonMethods.setDate(pStmt, 27, inputBean.getLogcopUploadedDate());
				CommonMethods.setDate(pStmt, 28, inputBean.getLaptopInstalledDate());
				CommonMethods.setDate(pStmt, 29, inputBean.getFinalReportDate());
				CommonMethods.setDate(pStmt, 30, inputBean.getDestructionDate());

				CommonMethods.setDate(pStmt, 31, inputBean.getEstCompletedDate());
				CommonMethods.setDate(pStmt, 32, inputBean.getEstFy1314CompletedDate());
				CommonMethods.setDate(pStmt, 33, inputBean.getEstFy1112CompletedDate());

				CommonMethods.setString(pStmt, 34, inputBean.getRequestedDateCss());
				CommonMethods.setString(pStmt, 35, inputBean.getReceivedDateCss());
				CommonMethods.setString(pStmt, 36, inputBean.getScanningDeliveredDateCss());
				CommonMethods.setString(pStmt, 37, inputBean.getFy1314BurnedDateCss());
				CommonMethods.setString(pStmt, 38, inputBean.getFy1314MailedDateCss());
				CommonMethods.setString(pStmt, 39, inputBean.getFy1314CompletedDateCss());
				CommonMethods.setString(pStmt, 40, inputBean.getFy1112CompletedDateCss());
				CommonMethods.setString(pStmt, 41, inputBean.getExtractDateCss());
				CommonMethods.setString(pStmt, 42, inputBean.getLogcopDeliveredDateCss());
				CommonMethods.setString(pStmt, 43, inputBean.getLogcopUploadedDateCss());
				CommonMethods.setString(pStmt, 44, inputBean.getLaptopInstalledDateCss());
				CommonMethods.setString(pStmt, 45, inputBean.getFinalReportDateCss());
				CommonMethods.setString(pStmt, 46, inputBean.getDestructionDateCss());

				if (CommonMethods.nes(inputBean.getReturnInd()).equalsIgnoreCase("Y")) {
					pStmt.setString(47, "Y");
					CommonMethods.setDate(pStmt, 48, inputBean.getReturnedDate());
					CommonMethods.setString(pStmt, 49, inputBean.getReturnedDateCss());
					CommonMethods.setDate(pStmt, 50, inputBean.getReturnConfirmedDate());
					CommonMethods.setString(pStmt, 51, inputBean.getReturnConfirmedDateCss());
				} else {
					pStmt.setNull(47, java.sql.Types.VARCHAR);
					pStmt.setNull(48, java.sql.Types.DATE);
					pStmt.setNull(49, java.sql.Types.VARCHAR);
					pStmt.setNull(50, java.sql.Types.DATE);
					pStmt.setNull(51, java.sql.Types.VARCHAR);
				} //end of else

				CommonMethods.setDate(pStmt, 52, inputBean.getFy16CompletedDate());
				CommonMethods.setString(pStmt, 53, inputBean.getFy16CompletedDateCss());
				CommonMethods.setDate(pStmt, 54, inputBean.getFy16MailedDate());
				CommonMethods.setString(pStmt, 55, inputBean.getFy16MailedDateCss());
				CommonMethods.setDate(pStmt, 56, inputBean.getFy15CompletedDate());
				CommonMethods.setString(pStmt, 57, inputBean.getFy15CompletedDateCss());
				CommonMethods.setDate(pStmt, 58, inputBean.getFy15MailedDate());
				CommonMethods.setString(pStmt, 59, inputBean.getFy15MailedDateCss());

				pStmt.setString(60, "Y");
			} //end of else

			CommonMethods.setString(pStmt, 61, inputBean.getComments());

			CommonMethods.setString(pStmt, 62, loginBean.getFullName());
			pStmt.setString(63, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
			ranOk = (pStmt.executeUpdate() == 1);
		} catch (Exception e) {
			debugLog("ERROR", "insertWorkflow", e);
			ranOk = false;
		} finally {
			try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
			try { conn.setAutoCommit(true); } catch (Exception e) {}
		} //end of finally

		return ranOk;
	} //end of insertWorkflow

	/****************************************************************************
	 * Function: updateWorkflow
	 ****************************************************************************/
	public static boolean updateWorkflow(Connection conn, BackfileBean inputBean, LoginBean loginBean) {
		String sqlStmt = "UPDATE backfile_workflow SET fy16_pshi_box_cnt=?, fy15_pshi_box_cnt=?, fy14_pshi_box_cnt=?, fy13_pshi_box_cnt=?, fy12_pshi_box_cnt=?, fy11_pshi_box_cnt=?, fy10_pshi_box_cnt=?, other_pshi_box_cnt=?, fy16_box_cnt=?, fy15_box_cnt=?, fy14_box_cnt=?, fy13_box_cnt=?, fy12_box_cnt=?, fy11_box_cnt=?, fy10_box_cnt=?, other_box_cnt=?, requested_date=?, received_date=?, scanning_delivered_date=?, fy1314_burned_date=?, fy1314_mailed_date=?, fy1314_completed_date=?, fy1112_completed_date=?, extract_date=?, logcop_delivered_date=?, logcop_uploaded_date=?, laptop_installed_date=?, final_report_date=?, destruction_date=?, est_completed_date=?, est_fy1314_completed_date=?, est_fy1112_completed_date=?, requested_date_css=?, received_date_css=?, scanning_delivered_date_css=?, fy1314_burned_date_css=?, fy1314_mailed_date_css=?, fy1314_completed_date_css=?, fy1112_completed_date_css=?, extract_date_css=?, logcop_delivered_date_css=?, logcop_uploaded_date_css=?, laptop_installed_date_css=?, final_report_date_css=?, destruction_date_css=?, return_ind=?, returned_date=?, returned_date_css=?, return_confirmed_date=?, return_confirmed_date_css=?, fy16_completed_date=?, fy16_completed_date_css=?, fy16_mailed_date=?, fy16_mailed_date_css=?, fy15_completed_date=?, fy15_completed_date_css=?, fy15_mailed_date=?, fy15_mailed_date_css=?, is_required=?, comments=?, last_updated_by=?, last_updated_date=? WHERE backfile_workflow_pk=?";
		boolean ranOk = false;

		if (CommonMethods.cInt(inputBean.getBackfileWorkflowPk()) == -1) return false;

		debugLog("SQL", "updateWorkflow", sqlStmt + " (backfile_workflow_pk = " + inputBean.getBackfileWorkflowPk() + ")");

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
			conn.setAutoCommit(false);

			if (CommonMethods.nvl(inputBean.getIsRequired(), "N").equals("N")) { //Backfile NOT required - null all fields
				for (int i = 1; i <= 58; i++) {
					pStmt.setNull(i, java.sql.Types.VARCHAR);
				} //end of for

				pStmt.setString(59, "N");
			} else { //Backfile required - set all fields
				CommonMethods.setInt(pStmt, 1, inputBean.getFy16PshiBoxCnt());
				CommonMethods.setInt(pStmt, 2, inputBean.getFy15PshiBoxCnt());
				CommonMethods.setInt(pStmt, 3, inputBean.getFy14PshiBoxCnt());
				CommonMethods.setInt(pStmt, 4, inputBean.getFy13PshiBoxCnt());
				CommonMethods.setInt(pStmt, 5, inputBean.getFy12PshiBoxCnt());
				CommonMethods.setInt(pStmt, 6, inputBean.getFy11PshiBoxCnt());
				CommonMethods.setInt(pStmt, 7, inputBean.getFy10PshiBoxCnt());
				CommonMethods.setInt(pStmt, 8, inputBean.getOtherPshiBoxCnt());

				CommonMethods.setInt(pStmt, 9, inputBean.getFy16BoxCnt());
				CommonMethods.setInt(pStmt, 10, inputBean.getFy15BoxCnt());
				CommonMethods.setInt(pStmt, 11, inputBean.getFy14BoxCnt());
				CommonMethods.setInt(pStmt, 12, inputBean.getFy13BoxCnt());
				CommonMethods.setInt(pStmt, 13, inputBean.getFy12BoxCnt());
				CommonMethods.setInt(pStmt, 14, inputBean.getFy11BoxCnt());
				CommonMethods.setInt(pStmt, 15, inputBean.getFy10BoxCnt());
				CommonMethods.setInt(pStmt, 16, inputBean.getOtherBoxCnt());

				CommonMethods.setDate(pStmt, 17, inputBean.getRequestedDate());
				CommonMethods.setDate(pStmt, 18, inputBean.getReceivedDate());
				CommonMethods.setDate(pStmt, 19, inputBean.getScanningDeliveredDate());
				CommonMethods.setDate(pStmt, 20, inputBean.getFy1314BurnedDate());
				CommonMethods.setDate(pStmt, 21, inputBean.getFy1314MailedDate());
				CommonMethods.setDate(pStmt, 22, inputBean.getFy1314CompletedDate());
				CommonMethods.setDate(pStmt, 23, inputBean.getFy1112CompletedDate());
				CommonMethods.setDate(pStmt, 24, inputBean.getExtractDate());
				CommonMethods.setDate(pStmt, 25, inputBean.getLogcopDeliveredDate());
				CommonMethods.setDate(pStmt, 26, inputBean.getLogcopUploadedDate());
				CommonMethods.setDate(pStmt, 27, inputBean.getLaptopInstalledDate());
				CommonMethods.setDate(pStmt, 28, inputBean.getFinalReportDate());
				CommonMethods.setDate(pStmt, 29, inputBean.getDestructionDate());

				CommonMethods.setDate(pStmt, 30, inputBean.getEstCompletedDate());
				CommonMethods.setDate(pStmt, 31, inputBean.getEstFy1314CompletedDate());
				CommonMethods.setDate(pStmt, 32, inputBean.getEstFy1112CompletedDate());

				CommonMethods.setString(pStmt, 33, inputBean.getRequestedDateCss());
				CommonMethods.setString(pStmt, 34, inputBean.getReceivedDateCss());
				CommonMethods.setString(pStmt, 35, inputBean.getScanningDeliveredDateCss());
				CommonMethods.setString(pStmt, 36, inputBean.getFy1314BurnedDateCss());
				CommonMethods.setString(pStmt, 37, inputBean.getFy1314MailedDateCss());
				CommonMethods.setString(pStmt, 38, inputBean.getFy1314CompletedDateCss());
				CommonMethods.setString(pStmt, 39, inputBean.getFy1112CompletedDateCss());
				CommonMethods.setString(pStmt, 40, inputBean.getExtractDateCss());
				CommonMethods.setString(pStmt, 41, inputBean.getLogcopDeliveredDateCss());
				CommonMethods.setString(pStmt, 42, inputBean.getLogcopUploadedDateCss());
				CommonMethods.setString(pStmt, 43, inputBean.getLaptopInstalledDateCss());
				CommonMethods.setString(pStmt, 44, inputBean.getFinalReportDateCss());
				CommonMethods.setString(pStmt, 45, inputBean.getDestructionDateCss());

				if (CommonMethods.nes(inputBean.getReturnInd()).equalsIgnoreCase("Y")) {
					pStmt.setString(46, "Y");
					CommonMethods.setDate(pStmt, 47, inputBean.getReturnedDate());
					CommonMethods.setString(pStmt, 48, inputBean.getReturnedDateCss());
					CommonMethods.setDate(pStmt, 49, inputBean.getReturnConfirmedDate());
					CommonMethods.setString(pStmt, 50, inputBean.getReturnConfirmedDateCss());
				} else {
					pStmt.setNull(46, java.sql.Types.VARCHAR);
					pStmt.setNull(47, java.sql.Types.DATE);
					pStmt.setNull(48, java.sql.Types.VARCHAR);
					pStmt.setNull(49, java.sql.Types.DATE);
					pStmt.setNull(50, java.sql.Types.VARCHAR);
				} //end of else

				CommonMethods.setDate(pStmt, 51, inputBean.getFy16CompletedDate());
				CommonMethods.setString(pStmt, 52, inputBean.getFy16CompletedDateCss());
				CommonMethods.setDate(pStmt, 53, inputBean.getFy16MailedDate());
				CommonMethods.setString(pStmt, 54, inputBean.getFy16MailedDateCss());
				CommonMethods.setDate(pStmt, 55, inputBean.getFy15CompletedDate());
				CommonMethods.setString(pStmt, 56, inputBean.getFy15CompletedDateCss());
				CommonMethods.setDate(pStmt, 57, inputBean.getFy15MailedDate());
				CommonMethods.setString(pStmt, 58, inputBean.getFy15MailedDateCss());

				pStmt.setString(59, "Y");
			} //end of else

			CommonMethods.setString(pStmt, 60, inputBean.getComments());
			CommonMethods.setString(pStmt, 61, loginBean.getFullName());
			pStmt.setString(62, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
			pStmt.setInt(63, CommonMethods.cInt(inputBean.getBackfileWorkflowPk()));
			ranOk = pStmt.executeUpdate() == 1;
		} catch (Exception e) {
			debugLog("ERROR", "updateWorkflow", e);
			ranOk = false;
		} finally {
			try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
			try { conn.setAutoCommit(true); } catch (Exception e) {}
		} //end of finally

		return ranOk;
	} //end of updateWorkflow

	/****************************************************************************
	 * Function: deleteWorkflow
	 ****************************************************************************/
	public static boolean deleteWorkflow(Connection conn, BackfileBean inputBean) {
		String sqlStmt = "DELETE FROM backfile_workflow WHERE backfile_workflow_pk = ?";
		boolean ranOk = false;

		if (CommonMethods.cInt(inputBean.getBackfileWorkflowPk()) <= -1) return false;

		debugLog("SQL", "deleteWorkflow", sqlStmt + " (backfile_workflow_pk = " + inputBean.getBackfileWorkflowPk() + ")");

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
			conn.setAutoCommit(false);
			pStmt.setInt(1, CommonMethods.cInt(inputBean.getBackfileWorkflowPk()));
			ranOk = (pStmt.executeUpdate() == 1);
		} catch (Exception e) {
			debugLog("ERROR", "deleteWorkflow", e);
		} finally {
			try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
			try { conn.setAutoCommit(true); } catch (Exception e) {}
		} //end of finally

		return ranOk;
	} //end of deleteWorkflow

	/****************************************************************************
	 * Function: getAvailShipList
	 ****************************************************************************/
	public static ArrayList<ShipBean> getAvailShipList(Connection conn) {
		String sqlStmt = "SELECT ship_pk, uic, ship_name, type, hull, service_code, homeport, tycom, tycom_display, rsupply FROM ship_vw WHERE ship_pk NOT IN (SELECT ship_fk FROM backfile_workflow) ORDER BY ship_name";

		ArrayList<ShipBean> resultList = new ArrayList<ShipBean>();

		debugLog("SQL", "getAvailShipList", sqlStmt);

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				ShipBean resultBean = new ShipBean();
				resultBean.setShipPk(rs.getString("ship_pk"));
				resultBean.setUic(rs.getString("uic"));
				resultBean.setShipName(rs.getString("ship_name") + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG") ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")" : ""));
				resultBean.setServiceCode(rs.getString("service_code"));
				resultBean.setHomeport(rs.getString("homeport"));
				resultBean.setTycom(rs.getString("tycom"));
				resultBean.setTycomDisplay(rs.getString("tycom_display"));
				resultBean.setRsupply(rs.getString("rsupply"));
				resultList.add(resultBean);
			} //end of while
		} catch (Exception e) {
			debugLog("ERROR", "getAvailShipList", e);
		} //end of catch

		return resultList;
	} //end of getAvailShipList

	/****************************************************************************
	 * Function: getReportList
	 ****************************************************************************/
	public static ArrayList<LookupBean> getReportList(Connection conn, BackfileBean inputBean, String type) {
		StringBuffer sqlStmt = new StringBuffer("SELECT "
	        + "ship_name, type, hull, fy16_pshi_box_cnt, fy15_pshi_box_cnt, fy14_pshi_box_cnt, fy13_pshi_box_cnt, "
	        + "fy12_pshi_box_cnt, fy11_pshi_box_cnt, fy10_pshi_box_cnt, other_pshi_box_cnt, total_pshi_box_cnt, fy16_box_cnt, fy15_box_cnt, "
	        + "fy14_box_cnt, fy13_box_cnt, fy12_box_cnt, fy11_box_cnt, fy10_box_cnt, other_box_cnt, total_box_cnt, requested_date_fmt, "
	        + "received_date_fmt, scanning_delivered_date_fmt, fy16_completed_date_fmt, fy16_mailed_date_fmt, fy15_completed_date_fmt, "
	        + "fy15_mailed_date_fmt, fy1314_burned_date_fmt, fy1314_mailed_date_fmt, fy1314_completed_date_fmt, fy1112_completed_date_fmt, "
	        + "extract_date_fmt, logcop_delivered_date_fmt, logcop_uploaded_date_fmt, laptop_installed_date_fmt, final_report_date_fmt, "
	        + "est_completed_date_fmt, destruction_date_fmt, return_ind, returned_date_fmt, return_confirmed_date_fmt, est_fy1314_completed_date_fmt, "
	        + "est_fy1112_completed_date_fmt, due_date_fmt, fy1314_due_date_fmt, completed_date_fmt, comments "
	        + "FROM backfile_workflow_vw "
	        + "WHERE 1=1");

		//WHERE
		if (!CommonMethods.isEmpty(inputBean.getContractNumber())) sqlStmt.append(" AND ship_fk IN (SELECT ship_pk FROM configured_system_vw WHERE contract_number = ?)");

		switch (CommonMethods.nes(type)) {
			case "inProd"		: sqlStmt.append(" AND completed_date IS NULL");break;
			case "overdue"	: sqlStmt.append(" AND ((completed_date IS NULL AND due_date < date('now', '-10 hours')) OR (fy1314_completed_date IS NULL AND fy1314_due_date < date('now', '-10 hours')))");break;
			case "completed": sqlStmt.append(" AND completed_date IS NOT NULL");break;
		} //end of switch

		//ORDER BY
		sqlStmt.append(" ORDER BY ship_name");

		ArrayList<LookupBean> resultList = new ArrayList<LookupBean>();

		int totalFy16Boxes = 0;
		int totalFy15Boxes = 0;
		int totalFy1314Boxes = 0;
		int totalFy1112Boxes = 0;
		int totalFy10Boxes = 0;
		int totalOtherBoxes = 0;

		ArrayList<String> shipNameArr = new ArrayList<String>();
		ArrayList<String> fy16PshiBoxCntArr = new ArrayList<String>();
		ArrayList<String> fy15PshiBoxCntArr = new ArrayList<String>();
		ArrayList<String> fy14PshiBoxCntArr = new ArrayList<String>();
		ArrayList<String> fy13PshiBoxCntArr = new ArrayList<String>();
		ArrayList<String> fy12PshiBoxCntArr = new ArrayList<String>();
		ArrayList<String> fy11PshiBoxCntArr = new ArrayList<String>();
		ArrayList<String> fy10PshiBoxCntArr = new ArrayList<String>();
		ArrayList<String> otherPshiBoxCntArr = new ArrayList<String>();
		ArrayList<String> totalPshiBoxCntArr = new ArrayList<String>();
		ArrayList<String> fy16BoxCntArr = new ArrayList<String>();
		ArrayList<String> fy15BoxCntArr = new ArrayList<String>();
		ArrayList<String> fy14BoxCntArr = new ArrayList<String>();
		ArrayList<String> fy13BoxCntArr = new ArrayList<String>();
		ArrayList<String> fy12BoxCntArr = new ArrayList<String>();
		ArrayList<String> fy11BoxCntArr = new ArrayList<String>();
		ArrayList<String> fy10BoxCntArr = new ArrayList<String>();
		ArrayList<String> otherBoxCntArr = new ArrayList<String>();
		ArrayList<String> totalBoxCntArr = new ArrayList<String>();
		ArrayList<String> requestedDateArr = new ArrayList<String>();
		ArrayList<String> receivedDateArr = new ArrayList<String>();
		ArrayList<String> scanningDeliveredDateArr = new ArrayList<String>();
		ArrayList<String> fy16MailedDateArr = new ArrayList<String>();
		ArrayList<String> fy16CompletedDateArr = new ArrayList<String>();
		ArrayList<String> fy15MailedDateArr = new ArrayList<String>();
		ArrayList<String> fy15CompletedDateArr = new ArrayList<String>();
		ArrayList<String> fy1314BurnedDateArr = new ArrayList<String>();
		ArrayList<String> fy1314MailedDateArr = new ArrayList<String>();
		ArrayList<String> fy1314CompletedDateArr = new ArrayList<String>();
		ArrayList<String> fy1112CompletedDateArr = new ArrayList<String>();
		ArrayList<String> extractDateArr = new ArrayList<String>();
		ArrayList<String> logcopDeliveredDateArr = new ArrayList<String>();
		ArrayList<String> logcopUploadedDateArr = new ArrayList<String>();
		ArrayList<String> laptopInstalledDateArr = new ArrayList<String>();
		ArrayList<String> finalReportDateArr = new ArrayList<String>();
		ArrayList<String> destructionDateArr = new ArrayList<String>();
		ArrayList<String> estCompletedDateArr = new ArrayList<String>();
		ArrayList<String> returnIndArr = new ArrayList<String>();
		ArrayList<String> returnedDateArr = new ArrayList<String>();
		ArrayList<String> returnConfirmedDateArr = new ArrayList<String>();
		ArrayList<String> estFy1314CompletedDateArr = new ArrayList<String>();
		ArrayList<String> estFy1112CompletedDateArr = new ArrayList<String>();
		ArrayList<String> dueDateArr = new ArrayList<String>();
		ArrayList<String> fy1314DueDateArr = new ArrayList<String>();
		ArrayList<String> completedDateArr = new ArrayList<String>();
		ArrayList<String> commentsArr = new ArrayList<String>();
		ArrayList<String> blankList = new ArrayList<String>();

		debugLog("SQL", "getReportList", sqlStmt.toString());

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
			if (!CommonMethods.isEmpty(inputBean.getContractNumber())) pStmt.setString(1, inputBean.getContractNumber());
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				totalFy16Boxes += rs.getInt("fy16_box_cnt");
				totalFy15Boxes += rs.getInt("fy15_box_cnt");
				totalFy1314Boxes += rs.getInt("fy14_box_cnt");
				totalFy1314Boxes += rs.getInt("fy13_box_cnt");
				totalFy1112Boxes += rs.getInt("fy12_box_cnt");
				totalFy1112Boxes += rs.getInt("fy11_box_cnt");
				totalFy10Boxes += rs.getInt("fy10_box_cnt");
				totalOtherBoxes += rs.getInt("other_box_cnt");

				shipNameArr.add(CommonMethods.nes(rs.getString("ship_name")));
				fy16PshiBoxCntArr.add(CommonMethods.nvl(rs.getString("fy16_pshi_box_cnt"), "-"));
				fy15PshiBoxCntArr.add(CommonMethods.nvl(rs.getString("fy15_pshi_box_cnt"), "-"));
				fy14PshiBoxCntArr.add(CommonMethods.nvl(rs.getString("fy14_pshi_box_cnt"), "-"));
				fy13PshiBoxCntArr.add(CommonMethods.nvl(rs.getString("fy13_pshi_box_cnt"), "-"));
				fy12PshiBoxCntArr.add(CommonMethods.nvl(rs.getString("fy12_pshi_box_cnt"), "-"));
				fy11PshiBoxCntArr.add(CommonMethods.nvl(rs.getString("fy11_pshi_box_cnt"), "-"));
				fy10PshiBoxCntArr.add(CommonMethods.nvl(rs.getString("fy10_pshi_box_cnt"), "-"));
				otherPshiBoxCntArr.add(CommonMethods.nvl(rs.getString("other_pshi_box_cnt"), "-"));
				totalPshiBoxCntArr.add(CommonMethods.nvl(rs.getString("total_pshi_box_cnt"), "-"));
				fy16BoxCntArr.add(CommonMethods.nvl(rs.getString("fy16_box_cnt"), "-"));
				fy15BoxCntArr.add(CommonMethods.nvl(rs.getString("fy15_box_cnt"), "-"));
				fy14BoxCntArr.add(CommonMethods.nvl(rs.getString("fy14_box_cnt"), "-"));
				fy13BoxCntArr.add(CommonMethods.nvl(rs.getString("fy13_box_cnt"), "-"));
				fy12BoxCntArr.add(CommonMethods.nvl(rs.getString("fy12_box_cnt"), "-"));
				fy11BoxCntArr.add(CommonMethods.nvl(rs.getString("fy11_box_cnt"), "-"));
				fy10BoxCntArr.add(CommonMethods.nvl(rs.getString("fy10_box_cnt"), "-"));
				otherBoxCntArr.add(CommonMethods.nvl(rs.getString("other_box_cnt"), "-"));
				totalBoxCntArr.add(CommonMethods.nvl(rs.getString("total_box_cnt"), "-"));
				requestedDateArr.add(CommonMethods.nes(rs.getString("requested_date_fmt")));
				receivedDateArr.add(CommonMethods.nes(rs.getString("received_date_fmt")));
				scanningDeliveredDateArr.add(CommonMethods.nes(rs.getString("scanning_delivered_date_fmt")));
				fy16MailedDateArr.add(CommonMethods.nes(rs.getString("fy16_mailed_date_fmt")));
				fy16CompletedDateArr.add(CommonMethods.nes(rs.getString("fy16_completed_date_fmt")));
				fy15MailedDateArr.add(CommonMethods.nes(rs.getString("fy15_mailed_date_fmt")));
				fy15CompletedDateArr.add(CommonMethods.nes(rs.getString("fy15_completed_date_fmt")));
				fy1314BurnedDateArr.add(CommonMethods.nes(rs.getString("fy1314_burned_date_fmt")));
				fy1314MailedDateArr.add(CommonMethods.nes(rs.getString("fy1314_mailed_date_fmt")));
				fy1314CompletedDateArr.add(CommonMethods.nes(rs.getString("fy1314_completed_date_fmt")));
				fy1112CompletedDateArr.add(CommonMethods.nes(rs.getString("fy1112_completed_date_fmt")));
				extractDateArr.add(CommonMethods.nes(rs.getString("extract_date_fmt")));
				logcopDeliveredDateArr.add(CommonMethods.nes(rs.getString("logcop_delivered_date_fmt")));
				logcopUploadedDateArr.add(CommonMethods.nes(rs.getString("logcop_uploaded_date_fmt")));
				laptopInstalledDateArr.add(CommonMethods.nes(rs.getString("laptop_installed_date_fmt")));
				finalReportDateArr.add(CommonMethods.nes(rs.getString("final_report_date_fmt")));
				destructionDateArr.add(CommonMethods.nes(rs.getString("destruction_date_fmt")));
				estCompletedDateArr.add(CommonMethods.nes(rs.getString("est_completed_date_fmt")));

				returnIndArr.add(CommonMethods.nvl(rs.getString("return_ind"), "N"));
				returnedDateArr.add(CommonMethods.nes(rs.getString("returned_date_fmt")));
				returnConfirmedDateArr.add(CommonMethods.nes(rs.getString("return_confirmed_date_fmt")));


				estFy1314CompletedDateArr.add(CommonMethods.nes(rs.getString("est_fy1314_completed_date_fmt")));
				estFy1112CompletedDateArr.add(CommonMethods.nes(rs.getString("est_fy1112_completed_date_fmt")));
				dueDateArr.add(CommonMethods.nes(rs.getString("due_date_fmt")));
				fy1314DueDateArr.add(CommonMethods.nes(rs.getString("fy1314_due_date_fmt")));
				completedDateArr.add(CommonMethods.nes(rs.getString("completed_date_fmt")));
				commentsArr.add(CommonMethods.nes(rs.getString("comments")));
				blankList.add("");
			} //end of while
		} catch (Exception e) {
			debugLog("ERROR", "getReportList", e);
		} //end of catch

		LookupBean lBean = null;

		lBean = new LookupBean();

		switch (CommonMethods.nes(type)) {
			case "inProd":
				lBean.setKey("Total Boxes In Production:");
				break;
			case "overdue":
				lBean.setKey("Total Boxes:");
				break;
			case "completed":
				lBean.setKey("Total Boxes Completed:");
				break;
		} //end of switch

		lBean.setValue(String.valueOf(totalFy16Boxes + totalFy15Boxes + totalFy1314Boxes + totalFy1112Boxes + totalFy10Boxes + totalOtherBoxes));
		lBean.setCssStyle("font-weight:bold;background:#daeef3;");
		resultList.add(lBean);

		lBean = new LookupBean();lBean.setKey("Total FY16 Boxes:");lBean.setValue(String.valueOf(totalFy16Boxes));lBean.setCssStyle("background:#daeef3;");resultList.add(lBean);
		lBean = new LookupBean();lBean.setKey("Total FY15 Boxes:");lBean.setValue(String.valueOf(totalFy15Boxes));lBean.setCssStyle("background:#daeef3;");resultList.add(lBean);
		lBean = new LookupBean();lBean.setKey("Total FY14 / FY13 Boxes:");lBean.setValue(String.valueOf(totalFy1314Boxes));lBean.setCssStyle("background:#daeef3;");resultList.add(lBean);
		lBean = new LookupBean();lBean.setKey("Total FY12 / FY11 Boxes:");lBean.setValue(String.valueOf(totalFy1112Boxes));lBean.setCssStyle("background:#daeef3;");resultList.add(lBean);
		lBean = new LookupBean();lBean.setKey("Total FY10 Boxes:");lBean.setValue(String.valueOf(totalFy10Boxes));lBean.setCssStyle("background:#daeef3;");resultList.add(lBean);
		lBean = new LookupBean();lBean.setKey("Total Reports/Other Boxes:");lBean.setValue(String.valueOf(totalOtherBoxes));lBean.setCssStyle("background:#daeef3;");resultList.add(lBean);

		lBean = new LookupBean();lBean.setKey("Vessel");lBean.setValueList(shipNameArr);lBean.setCssStyle("font-weight:bold;background:#e4dfec;");resultList.add(lBean);

	 	lBean = new LookupBean();lBean.setKey("All Backfiles - PSHI Received");lBean.setValueList(blankList);lBean.setCssStyle("font-weight:bold;");resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("FY16 Boxes");lBean.setValueList(fy16PshiBoxCntArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("FY15 Boxes");lBean.setValueList(fy15PshiBoxCntArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("FY14 Boxes");lBean.setValueList(fy14PshiBoxCntArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("FY13 Boxes");lBean.setValueList(fy13PshiBoxCntArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("FY12 Boxes");lBean.setValueList(fy12PshiBoxCntArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("FY11 Boxes");lBean.setValueList(fy11PshiBoxCntArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("FY10 Boxes");lBean.setValueList(fy10PshiBoxCntArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("Report / Other Boxes");lBean.setValueList(otherPshiBoxCntArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("Total Backfile Boxes PSHI Received");lBean.setValueList(totalPshiBoxCntArr);lBean.setCssStyle("font-weight:bold;");resultList.add(lBean);

	 	lBean = new LookupBean();lBean.setKey("");lBean.setValueList(blankList);lBean.setCssStyle("background:#000;border:.5px solid #000;");resultList.add(lBean);

	 	lBean = new LookupBean();lBean.setKey("All Backfiles - Sent to Scanning");lBean.setValueList(blankList);lBean.setCssStyle("font-weight:bold;background:#e4dfec;border:.5px solid #e4dfec;");resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("FY16 Boxes");lBean.setValueList(fy16BoxCntArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("FY15 Boxes");lBean.setValueList(fy15BoxCntArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("FY14 Boxes");lBean.setValueList(fy14BoxCntArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("FY13 Boxes");lBean.setValueList(fy13BoxCntArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("FY12 Boxes");lBean.setValueList(fy12BoxCntArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("FY11 Boxes");lBean.setValueList(fy11BoxCntArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("FY10 Boxes");lBean.setValueList(fy10BoxCntArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("Report / Other Boxes");lBean.setValueList(otherBoxCntArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("Total Backfile Boxes Sent to Scanning");lBean.setValueList(totalBoxCntArr);lBean.setCssStyle("font-weight:bold;");resultList.add(lBean);

	 	lBean = new LookupBean();lBean.setKey("");lBean.setValueList(blankList);lBean.setCssStyle("background:#000;border:.5px solid #000;");resultList.add(lBean);

	 	lBean = new LookupBean();lBean.setKey("Backfile Date Requested");lBean.setValueList(requestedDateArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("Date Received By PSHI");lBean.setValueList(receivedDateArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("Date Delivered to Scanning");lBean.setValueList(scanningDeliveredDateArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("Overall Due Date");lBean.setValueList(dueDateArr);lBean.setCssStyle("font-weight:bold;");resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("Estimated Completed Date");lBean.setValueList(estCompletedDateArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("All Backfiles - Actual Completion Date");lBean.setValueList(completedDateArr);resultList.add(lBean);

	 	lBean = new LookupBean();lBean.setKey("");lBean.setValueList(blankList);lBean.setCssStyle("background:#000;border:.5px solid #000;");resultList.add(lBean);

	 	lBean = new LookupBean();lBean.setKey("FY16");lBean.setValueList(blankList);lBean.setCssStyle("font-weight:bold;background:#e4dfec;border:.5px solid #e4dfec;");resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("FY16 Completed");lBean.setValueList(fy16CompletedDateArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("FY16 CD Sent for Customer");lBean.setValueList(fy16MailedDateArr);resultList.add(lBean);

	 	lBean = new LookupBean();lBean.setKey("FY15");lBean.setValueList(blankList);lBean.setCssStyle("font-weight:bold;background:#e4dfec;border:.5px solid #e4dfec;");resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("FY15 Completed");lBean.setValueList(fy15CompletedDateArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("FY15 CD Sent for Customer");lBean.setValueList(fy15MailedDateArr);resultList.add(lBean);

	 	lBean = new LookupBean();lBean.setKey("FY14/13");lBean.setValueList(blankList);lBean.setCssStyle("font-weight:bold;background:#e4dfec;border:.5px solid #e4dfec;");resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("Due Date");lBean.setValueList(fy1314DueDateArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("Scanning Center ETA");lBean.setValueList(estFy1314CompletedDateArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("FY14/13 Completed");lBean.setValueList(fy1314CompletedDateArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("FY14/13 CD Sent for Customer");lBean.setValueList(fy1314BurnedDateArr);resultList.add(lBean);

	 	lBean = new LookupBean();lBean.setKey("");lBean.setValueList(blankList);lBean.setCssStyle("background:#000;border:.5px solid #000;");resultList.add(lBean);

	 	lBean = new LookupBean();lBean.setKey("FY12/11");lBean.setValueList(blankList);lBean.setCssStyle("font-weight:bold;background:#e4dfec;border:.5px solid #e4dfec;");resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("Scanning Center ETA");lBean.setValueList(estFy1112CompletedDateArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("FY12/11 Completed");lBean.setValueList(fy1112CompletedDateArr);resultList.add(lBean);

	 	lBean = new LookupBean();lBean.setKey("");lBean.setValueList(blankList);lBean.setCssStyle("background:#000;border:.5px solid #000;");resultList.add(lBean);

	 	lBean = new LookupBean();lBean.setKey("All Backfiles");lBean.setValueList(blankList);lBean.setCssStyle("font-weight:bold;background:#e4dfec;border:.5px solid #e4dfec;");resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("Extract File Created");lBean.setValueList(extractDateArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("All backfile CD mailed to Cust/SD");lBean.setValueList(fy1314MailedDateArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("Verify All Backfiles Installed in FACET");lBean.setValueList(laptopInstalledDateArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("Verified LOGCOP Backfile Uploaded");lBean.setValueList(logcopUploadedDateArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("All Backfile CD Delivered to LogCop");lBean.setValueList(logcopDeliveredDateArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("Final Backfile Report Generated and Given to CompacFlt");lBean.setValueList(finalReportDateArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("Destruction Date");lBean.setValueList(destructionDateArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("All Backfiles Completed");lBean.setValueList(completedDateArr);resultList.add(lBean);

	 	lBean = new LookupBean();lBean.setKey("");lBean.setValueList(blankList);lBean.setCssStyle("background:#000;border:.5px solid #000;");resultList.add(lBean);

	 	lBean = new LookupBean();lBean.setKey("Return to Unit");lBean.setValueList(blankList);lBean.setCssStyle("font-weight:bold;background:#e4dfec;border:.5px solid #e4dfec;");resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("Return to Unit?");lBean.setValueList(returnIndArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("Backfile Returned Date");lBean.setValueList(returnedDateArr);resultList.add(lBean);
	 	lBean = new LookupBean();lBean.setKey("Client Received Confirmation Date");lBean.setValueList(returnConfirmedDateArr);resultList.add(lBean);

	 	lBean = new LookupBean();lBean.setKey("");lBean.setValueList(blankList);lBean.setCssStyle("background:#000;border:.5px solid #000;");resultList.add(lBean);

	 	lBean = new LookupBean();lBean.setKey("Comments");lBean.setValueList(commentsArr);resultList.add(lBean);

		return resultList;
	} //end of getReportList
} //end of class
