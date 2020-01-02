package com.premiersolutionshi.old.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.premiersolutionshi.common.util.DateUtils;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.bean.ShipBean;
import com.premiersolutionshi.old.bean.TrainingBean;
import com.premiersolutionshi.old.util.CommonMethods;
/**
 * Business logic for the application's TRAINING module
 *
 * @author   Anthony Tsuhako
 * @version  1.0, 11/25/2013
 * @since    JDK 7, Apache Struts 1.3.10
 */
public class TrainingModel {
	private static  Logger logger = Logger.getLogger(TrainingModel.class.getName());

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
	public static ArrayList<TrainingBean> getSummaryList(Connection conn, String type, String sortBy, String sortDir) {
		return getSummaryList(conn, type, new TrainingBean(), sortBy, sortDir);
	} //end of getSummaryList

	public static ArrayList<TrainingBean> getSummaryList(Connection conn, String type, TrainingBean inputBean, String sortBy, String sortDir) {
		StringBuffer sqlStmt = new StringBuffer("SELECT training_workflow_pk, ship_name, type, hull, tycom_display, homeport, rsupply, backfile_recv_date_fmt, backfile_completed_date_fmt, loc_file_recv_date_fmt, loc_file_rev_date_fmt, pacflt_food_report_date_fmt, system_ready_date_fmt, computer_name_db_date_fmt, computer_name_logcop_date_fmt, training_kit_ready_date_fmt, sched_training_date_fmt, actual_training_date_fmt, est_training_month, comments, trainer_user_fk, trainer_full_name, trainer, client_confirmed_ind FROM training_workflow_vw WHERE 1=1");

		//WHERE + ORDER BY
		if (!CommonMethods.isEmpty(inputBean.getContractNumber())) sqlStmt.append(" AND ship_fk IN (SELECT ship_pk FROM configured_system_vw WHERE contract_number = ?)");

		switch (CommonMethods.nes(type)) {
			case "inProd"		 : sqlStmt.append(" AND actual_training_date IS NULL");break;
			case "unsched"	 : sqlStmt.append(" AND sched_training_date IS NULL AND actual_training_date IS NULL");break;
			case "overdue"	 : sqlStmt.append(" AND actual_training_date IS NULL AND sched_training_date < date('now', '-10 hours')");break;
			case "completed" : sqlStmt.append(" AND actual_training_date IS NOT NULL");break;
			case "report"		 : sqlStmt.append(" ORDER BY IFNULL(actual_training_date, sched_training_date) IS NULL, IFNULL(actual_training_date, sched_training_date), ship_name");break;
		} //end of switch

		//ORDER BY
		switch (sortBy + "_" + sortDir) {
			case "ship_name_ASC"	: sqlStmt.append(" ORDER BY ship_name ASC");break;
			case "ship_name_DESC"	: sqlStmt.append(" ORDER BY ship_name DESC");break;
			case "homeport_ASC"		: sqlStmt.append(" ORDER BY homeport ASC");break;
			case "homeport_DESC"	: sqlStmt.append(" ORDER BY homeport DESC");break;
			case "type_hull_ASC":				sqlStmt.append(" ORDER BY type ASC, hull ASC");break;
			case "type_hull_DESC":			sqlStmt.append(" ORDER BY type DESC, hull DESC");break;
			case "tycom_display_ASC":		sqlStmt.append(" ORDER BY tycom_display IS NULL OR tycom_display='', tycom_display ASC, ship_name");break;
			case "tycom_display_DESC":	sqlStmt.append(" ORDER BY tycom_display IS NULL OR tycom_display='', tycom_display DESC, ship_name");break;
		} //end of switch

		ArrayList<TrainingBean> resultList = new ArrayList<TrainingBean>();

		debugLog("SQL", "getSummaryList", sqlStmt.toString());

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
			if (!CommonMethods.isEmpty(inputBean.getContractNumber())) pStmt.setString(1, inputBean.getContractNumber());
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				TrainingBean resultBean = new TrainingBean();
				resultBean.setTrainingWorkflowPk(rs.getString("training_workflow_pk"));
				//resultBean.setShipName(CommonMethods.nes(rs.getString("ship_name")) + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG") ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")" : ""));
				resultBean.setShipName(rs.getString("ship_name"));
				resultBean.setType(rs.getString("type"));
				resultBean.setHull(rs.getString("hull"));
				resultBean.setTycomDisplay(rs.getString("tycom_display"));
				resultBean.setHomeport(rs.getString("homeport"));
//				resultBean.setComputerName(rs.getString("computer_name"));
				resultBean.setRsupply(rs.getString("rsupply"));
//				resultBean.setLaptopTag(rs.getString("laptop_tag"));
//				resultBean.setScannerTag(rs.getString("scanner_tag"));
				resultBean.setBackfileRecvDate(rs.getString("backfile_recv_date_fmt"));
				resultBean.setBackfileCompletedDate(rs.getString("backfile_completed_date_fmt"));
				resultBean.setLocFileRecvDate(rs.getString("loc_file_recv_date_fmt"));
				resultBean.setLocFileRevDate(rs.getString("loc_file_rev_date_fmt"));
				resultBean.setPacfltFoodReportDate(rs.getString("pacflt_food_report_date_fmt"));
				resultBean.setSystemReadyDate(rs.getString("system_ready_date_fmt"));
				resultBean.setComputerNameDbDate(rs.getString("computer_name_db_date_fmt"));
				resultBean.setComputerNameLogcopDate(rs.getString("computer_name_logcop_date_fmt"));
				resultBean.setTrainingKitReadyDate(rs.getString("training_kit_ready_date_fmt"));
				resultBean.setSchedTrainingDate(rs.getString("sched_training_date_fmt"));
				resultBean.setActualTrainingDate(rs.getString("actual_training_date_fmt"));

				if (CommonMethods.isValidDateStr(resultBean.getSchedTrainingDate()) && CommonMethods.isEmpty(resultBean.getActualTrainingDate()) && CommonMethods.dateDiff(CommonMethods.getDate("MM/DD/YYYY"), resultBean.getSchedTrainingDate()) <= 0) {
					resultBean.setSchedTrainingDateCss("color:#f00;");
				} //end of if

				resultBean.setEstTrainingMonth(rs.getString("est_training_month"));
				resultBean.setComments(rs.getString("comments"));
				resultBean.setTrainerUserFk(rs.getString("trainer_user_fk"));
				resultBean.setTrainerFullName(rs.getString("trainer_full_name"));
				resultBean.setTrainer(rs.getString("trainer"));
				resultBean.setClientConfirmedInd(rs.getString("client_confirmed_ind"));
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
	public static HashMap<String, TrainingBean> getWorkflowMap(Connection conn) {
		String sqlStmt = "SELECT training_workflow_pk, ship_name, uic, type, hull, tycom_display, homeport, rsupply, backfile_recv_date_fmt, backfile_completed_date_fmt, loc_file_recv_date_fmt, loc_file_rev_date_fmt, pacflt_food_report_date_fmt, system_ready_date_fmt, computer_name_db_date_fmt, computer_name_logcop_date_fmt, training_kit_ready_date_fmt, sched_training_date_fmt, actual_training_date_fmt, est_training_month, comments, trainer, trainer_full_name, client_confirmed_ind FROM training_workflow_vw";
		HashMap<String, TrainingBean> resultMap = new HashMap<String, TrainingBean>();

		debugLog("SQL", "getWorkflowMap", sqlStmt);

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				TrainingBean resultBean = new TrainingBean();
				resultBean.setTrainingWorkflowPk(rs.getString("training_workflow_pk"));
				//resultBean.setShipName(CommonMethods.nes(rs.getString("ship_name")) + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG") ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")" : ""));
				resultBean.setShipName(rs.getString("ship_name"));
				resultBean.setType(rs.getString("type"));
				resultBean.setHull(rs.getString("hull"));
				resultBean.setTycomDisplay(rs.getString("tycom_display"));
				resultBean.setHomeport(rs.getString("homeport"));
//				resultBean.setComputerName(rs.getString("computer_name"));
				resultBean.setRsupply(rs.getString("rsupply"));
//				resultBean.setLaptopTag(rs.getString("laptop_tag"));
//				resultBean.setScannerTag(rs.getString("scanner_tag"));
				resultBean.setBackfileRecvDate(rs.getString("backfile_recv_date_fmt"));
				resultBean.setBackfileCompletedDate(rs.getString("backfile_completed_date_fmt"));
				resultBean.setLocFileRecvDate(rs.getString("loc_file_recv_date_fmt"));
				resultBean.setLocFileRevDate(rs.getString("loc_file_rev_date_fmt"));
				resultBean.setPacfltFoodReportDate(rs.getString("pacflt_food_report_date_fmt"));
				resultBean.setSystemReadyDate(rs.getString("system_ready_date_fmt"));
				resultBean.setComputerNameDbDate(rs.getString("computer_name_db_date_fmt"));
				resultBean.setComputerNameLogcopDate(rs.getString("computer_name_logcop_date_fmt"));
				resultBean.setTrainingKitReadyDate(rs.getString("training_kit_ready_date_fmt"));
				resultBean.setSchedTrainingDate(rs.getString("sched_training_date_fmt"));
				resultBean.setActualTrainingDate(rs.getString("actual_training_date_fmt"));

				if (CommonMethods.isValidDateStr(resultBean.getSchedTrainingDate()) && CommonMethods.isEmpty(resultBean.getActualTrainingDate()) && CommonMethods.dateDiff(CommonMethods.getDate("MM/DD/YYYY"), resultBean.getSchedTrainingDate()) <= 0) {
					resultBean.setSchedTrainingDateCss("color:#f00;");
				} //end of if

				resultBean.setEstTrainingMonth(rs.getString("est_training_month"));
				resultBean.setComments(rs.getString("comments"));
				resultBean.setTrainerFullName(rs.getString("trainer_full_name"));
				resultBean.setTrainer(rs.getString("trainer"));
				resultBean.setClientConfirmedInd(rs.getString("client_confirmed_ind"));
				resultMap.put(rs.getString("uic"), resultBean);
			} //end of while
		} catch (Exception e) {
			debugLog("ERROR", "getWorkflowMap", e);
		} //end of catch

		return resultMap;
	} //end of getWorkflowMap

	/****************************************************************************
	 * Function: getBarGraphLabelList
	 * Notes: Sync this fucntion with getBarGraphLabelList
	 ****************************************************************************/
	public static ArrayList<String> getBarGraphLabelList(Connection conn) {
		ArrayList<String> resultList = new ArrayList<String>();

		String calDate = CommonMethods.getDate("MM/01/YYYY", -120);
		for (int i = 0; i < 12; i++) {
			int daysInMonth = CommonMethods.cInt(CommonMethods.getDate(calDate, "MAX"));
			calDate = CommonMethods.getDate(calDate, "MM/DD/YYYY", daysInMonth);
			resultList.add(CommonMethods.getDate(calDate, "MON YYYY"));
		} //end of for

		return resultList;
	} //end of getBarGraphLabelList

	/****************************************************************************
	 * Function: getSchedValueList
	 * Notes: Sync this fucntion with getBarGraphLabelList
	 ****************************************************************************/
	public static ArrayList<String> getSchedValueList(Connection conn, TrainingBean inputBean) {
		StringBuffer sqlStmt = new StringBuffer("SELECT strftime('%Y%m', sched_training_date) AS yyyymm, COUNT(1) AS cnt FROM training_workflow_vw WHERE sched_training_date IS NOT NULL AND actual_training_date IS NULL");
		if (!CommonMethods.isEmpty(inputBean.getContractNumber())) sqlStmt.append(" AND ship_fk IN (SELECT ship_pk FROM configured_system_vw WHERE contract_number = ?)");
		sqlStmt.append(" GROUP BY strftime('%Y%m', sched_training_date)");

		ArrayList<String> resultList = new ArrayList<String>();
		HashMap<String, String> resultMap = new HashMap<String, String>();

		debugLog("SQL", "getSchedValueList", sqlStmt.toString());

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
			if (!CommonMethods.isEmpty(inputBean.getContractNumber())) pStmt.setString(1, inputBean.getContractNumber());
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) resultMap.put(rs.getString("yyyymm"), rs.getString("cnt"));

			String calDate = CommonMethods.getDate("MM/01/YYYY", -120);
			for (int i = 0; i < 12; i++) {
				int daysInMonth = CommonMethods.cInt(CommonMethods.getDate(calDate, "MAX"));
				calDate = CommonMethods.getDate(calDate, "MM/DD/YYYY", daysInMonth);
				resultList.add(CommonMethods.nvl(resultMap.get(CommonMethods.getDate(calDate, "YYYYMM")), "0"));
			} //end of for
		} catch (Exception e) {
			debugLog("ERROR", "getSchedValueList", e);
		} //end of catch

		return resultList;
	} //end of getSchedValueList

	/****************************************************************************
	 * Function: getActualValueList
	 * Notes: Sync this fucntion with getBarGraphLabelList
	 ****************************************************************************/
	public static ArrayList<String> getActualValueList(Connection conn, TrainingBean inputBean) {
		StringBuffer sqlStmt = new StringBuffer("SELECT strftime('%Y%m', actual_training_date) AS yyyymm, COUNT(1) AS cnt FROM training_workflow_vw WHERE actual_training_date IS NOT NULL");
		if (!CommonMethods.isEmpty(inputBean.getContractNumber())) sqlStmt.append(" AND ship_fk IN (SELECT ship_pk FROM configured_system_vw WHERE contract_number = ?)");
		sqlStmt.append(" GROUP BY strftime('%Y%m', actual_training_date)");

		ArrayList<String> resultList = new ArrayList<String>();
		HashMap<String, String> resultMap = new HashMap<String, String>();

		debugLog("SQL", "getActualValueList", sqlStmt.toString());

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
			if (!CommonMethods.isEmpty(inputBean.getContractNumber())) pStmt.setString(1, inputBean.getContractNumber());
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) resultMap.put(rs.getString("yyyymm"), rs.getString("cnt"));

			String calDate = CommonMethods.getDate("MM/01/YYYY", -120);
			for (int i = 0; i < 12; i++) {
				int daysInMonth = CommonMethods.cInt(CommonMethods.getDate(calDate, "MAX"));
				calDate = CommonMethods.getDate(calDate, "MM/DD/YYYY", daysInMonth);
				resultList.add(CommonMethods.nvl(resultMap.get(CommonMethods.getDate(calDate, "YYYYMM")), "0"));
			} //end of for
		} catch (Exception e) {
			debugLog("ERROR", "getActualValueList", e);
		} //end of catch

		return resultList;
	} //end of getActualValueList

	/****************************************************************************
	 * Function: getWorkflowBean
	 ****************************************************************************/
	public static TrainingBean getWorkflowBean(Connection conn, int trainingWorkflowPk) {
		return getWorkflowBean(conn, trainingWorkflowPk, null);
	} //end of getWorkflowBean

	public static TrainingBean getWorkflowBean(Connection conn, String uic) {
		return getWorkflowBean(conn, -1, uic);
	} //end of getWorkflowBean

	private static TrainingBean getWorkflowBean(Connection conn, int trainingWorkflowPk, String uic) {
		String sqlStmt = "SELECT training_workflow_pk, ship_name, homeport, rsupply, backfile_recv_date_fmt, backfile_completed_date_fmt, loc_file_recv_date_fmt, loc_file_rev_date_fmt, pacflt_food_report_date_fmt, system_ready_date_fmt, computer_name_db_date_fmt, computer_name_logcop_date_fmt, training_kit_ready_date_fmt, sched_training_date_fmt, sched_training_time, actual_training_date_fmt, est_training_month, comments, trainer_user_fk, trainer_full_name, trainer, sched_training_loc, client_confirmed_ind FROM training_workflow_vw";
		String sqlWhere = trainingWorkflowPk > -1 ? " WHERE training_workflow_pk = ?" : " WHERE uic = ?";
		TrainingBean resultBean = null;

		if (trainingWorkflowPk <= -1 && CommonMethods.isEmpty(uic)) return null;

		debugLog("SQL", "getWorkflowBean", sqlStmt + sqlWhere + (trainingWorkflowPk > -1 ? " (training_workflow_pk = " + trainingWorkflowPk + ")" : " (uic = " + CommonMethods.nes(uic) + ")"));

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt + sqlWhere)) {
			if (trainingWorkflowPk > -1)
				pStmt.setInt(1, trainingWorkflowPk);
			else
				pStmt.setString(1, uic);

			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				resultBean = new TrainingBean();
				resultBean.setTrainingWorkflowPk(rs.getString("training_workflow_pk"));
				resultBean.setShipName(rs.getString("ship_name"));
				resultBean.setHomeport(rs.getString("homeport"));
//				resultBean.setComputerName(rs.getString("computer_name"));
				resultBean.setRsupply(rs.getString("rsupply"));
//				resultBean.setLaptopTag(rs.getString("laptop_tag"));
//				resultBean.setScannerTag(rs.getString("scanner_tag"));
				resultBean.setBackfileRecvDate(rs.getString("backfile_recv_date_fmt"));
				resultBean.setBackfileCompletedDate(rs.getString("backfile_completed_date_fmt"));
				resultBean.setLocFileRecvDate(rs.getString("loc_file_recv_date_fmt"));
				resultBean.setLocFileRevDate(rs.getString("loc_file_rev_date_fmt"));
				resultBean.setPacfltFoodReportDate(rs.getString("pacflt_food_report_date_fmt"));
				resultBean.setSystemReadyDate(rs.getString("system_ready_date_fmt"));
				resultBean.setComputerNameDbDate(rs.getString("computer_name_db_date_fmt"));
				resultBean.setComputerNameLogcopDate(rs.getString("computer_name_logcop_date_fmt"));
				resultBean.setTrainingKitReadyDate(rs.getString("training_kit_ready_date_fmt"));
				resultBean.setSchedTrainingDate(rs.getString("sched_training_date_fmt"));
				if (!CommonMethods.isEmpty(rs.getString("sched_training_time"))) resultBean.setSchedTrainingTime(CommonMethods.padString(rs.getString("sched_training_time"), "0", 4));
				resultBean.setActualTrainingDate(rs.getString("actual_training_date_fmt"));

				if (CommonMethods.isValidDateStr(resultBean.getSchedTrainingDate()) && CommonMethods.isEmpty(resultBean.getActualTrainingDate()) && CommonMethods.dateDiff(CommonMethods.getDate("MM/DD/YYYY"), resultBean.getSchedTrainingDate()) <= 0) {
					resultBean.setSchedTrainingDateCss("color:#f00;");
				} //end of if

				resultBean.setEstTrainingMonth(rs.getString("est_training_month"));
				resultBean.setComments(rs.getString("comments"));
				resultBean.setTrainerUserFk(rs.getString("trainer_user_fk"));
				resultBean.setTrainerFullName(rs.getString("trainer_full_name"));
				resultBean.setTrainer(rs.getString("trainer"));
				resultBean.setSchedTrainingLoc(rs.getString("sched_training_loc"));
				resultBean.setClientConfirmedInd(rs.getString("client_confirmed_ind"));
			} //end of if
		} catch (Exception e) {
			debugLog("ERROR", "getWorkflowBean", e);
		} //end of catch

		return resultBean;
	} //end of getWorkflowBean

	/****************************************************************************
	 * Function: insertWorkflow
	 ****************************************************************************/
	public static boolean insertWorkflow(Connection conn, TrainingBean inputBean, LoginBean loginBean) {
		String sqlStmt = "INSERT INTO training_workflow (ship_fk, sched_training_loc, last_updated_by, last_updated_date) VALUES (?,?,?,?)";
		boolean ranOk = false;

		ShipBean shipBean = ShipModel.getShipBean(conn, CommonMethods.cInt(inputBean.getShipPk()), null);

		debugLog("SQL", "insertWorkflow", sqlStmt);

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
			conn.setAutoCommit(false);
			CommonMethods.setInt(pStmt, 1, shipBean.getShipPk());
			CommonMethods.setString(pStmt, 2, ShipModel.getHomeport(conn, CommonMethods.cInt(inputBean.getShipPk()), null));
			CommonMethods.setString(pStmt, 3, loginBean.getFullName());
			pStmt.setString(4, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
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
	public static boolean updateWorkflow(Connection conn, TrainingBean inputBean, LoginBean loginBean) {
		String sqlStmt = "UPDATE training_workflow SET loc_file_recv_date = ?, loc_file_rev_date = ?, pacflt_food_report_date = ?, system_ready_date = ?, "
	        + "computer_name_db_date = ?, computer_name_logcop_date = ?, training_kit_ready_date = ?, sched_training_date = ?, sched_training_time = ?, "
	        + "actual_training_date = ?, est_training_month = ?, comments = ?, trainer_user_fk = ?, trainer = ?, sched_training_loc = ?, "
	        + "client_confirmed_ind = ?, last_updated_by = ?, last_updated_date = ? "
	        + "WHERE training_workflow_pk = ?";
		boolean ranOk = false;

		if (CommonMethods.cInt(inputBean.getTrainingWorkflowPk()) == -1) return false;

		debugLog("SQL", "updateWorkflow", sqlStmt + " (training_workflow_pk = " + inputBean.getTrainingWorkflowPk() + ")");

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
			conn.setAutoCommit(false);
			CommonMethods.setDate(pStmt, 1, inputBean.getLocFileRecvDate());
			CommonMethods.setDate(pStmt, 2, inputBean.getLocFileRevDate());
			CommonMethods.setDate(pStmt, 3, inputBean.getPacfltFoodReportDate());
			CommonMethods.setDate(pStmt, 4, inputBean.getSystemReadyDate());
			CommonMethods.setDate(pStmt, 5, inputBean.getComputerNameDbDate());
			CommonMethods.setDate(pStmt, 6, inputBean.getComputerNameLogcopDate());
			CommonMethods.setDate(pStmt, 7, inputBean.getTrainingKitReadyDate());
			CommonMethods.setDate(pStmt, 8, inputBean.getSchedTrainingDate());
			CommonMethods.setInt(pStmt, 9, inputBean.getSchedTrainingTime());
			CommonMethods.setDate(pStmt, 10, inputBean.getActualTrainingDate());
			CommonMethods.setString(pStmt, 11, inputBean.getEstTrainingMonth());
			CommonMethods.setString(pStmt, 12, inputBean.getComments());
			CommonMethods.setInt(pStmt, 13, inputBean.getTrainerUserFk());
			CommonMethods.setString(pStmt, 14, inputBean.getTrainer());
			CommonMethods.setString(pStmt, 15, inputBean.getSchedTrainingLoc());
			CommonMethods.setString(pStmt, 16, CommonMethods.nvl(inputBean.getClientConfirmedInd(), "", "Yes"));
			CommonMethods.setString(pStmt, 17, loginBean.getFullName());
			pStmt.setString(18, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
			pStmt.setInt(19, CommonMethods.cInt(inputBean.getTrainingWorkflowPk()));
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
	public static boolean deleteWorkflow(Connection conn, TrainingBean inputBean) {
		String sqlStmt = "DELETE FROM training_workflow WHERE training_workflow_pk = ?";
		boolean ranOk = false;

		if (CommonMethods.cInt(inputBean.getTrainingWorkflowPk()) <= -1) return false;

		debugLog("SQL", "deleteWorkflow", sqlStmt + " (training_workflow_pk = " + inputBean.getTrainingWorkflowPk() + ")");

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
			conn.setAutoCommit(false);
			pStmt.setInt(1, CommonMethods.cInt(inputBean.getTrainingWorkflowPk()));
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
	public static ArrayList<ShipBean> getAvailShipList(Connection conn, TrainingBean inputBean) {
		StringBuffer sqlStmt = new StringBuffer("SELECT ship_pk, uic, ship_name, type, hull, service_code, homeport, tycom, tycom_display, rsupply FROM ship_vw WHERE ship_pk NOT IN (SELECT ship_fk FROM training_workflow)");

		//WHERE
		if (!CommonMethods.isEmpty(inputBean.getContractNumber())) sqlStmt.append(" AND ship_pk IN (SELECT ship_pk FROM configured_system_vw WHERE contract_number = ?)");

		//ORDER BY
		sqlStmt.append(" ORDER BY ship_name");

		ArrayList<ShipBean> resultList = new ArrayList<ShipBean>();

		debugLog("SQL", "getAvailShipList", sqlStmt.toString());

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
			if (!CommonMethods.isEmpty(inputBean.getContractNumber())) pStmt.setString(1, inputBean.getContractNumber());
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

	public static ArrayList<String> getTrainingMonthList() {
		ArrayList<String> resultList = new ArrayList<String>();
		int twoYearsAhead = LocalDate.now().getYear() + 2;
		for (int year = twoYearsAhead; year >= 2014; year--) {
			for (int month = 11; month >= 0; month--) {
				resultList.add(CommonMethods.getMonthNameShort(month) + " " + year);
			} //end of for
		} //end of for
		return resultList;
	} //end of getTrainingMonthList

    public static boolean isValidWorkflow(TrainingBean inputBean) {
        if (inputBean == null) {
            return false;
        }
        String actualTrainingDateStr = inputBean.getActualTrainingDate();
        if (!StringUtils.isEmpty(actualTrainingDateStr)) {
            LocalDate actualTrainingDate = LocalDate.parse(actualTrainingDateStr, DateUtils.COMMON_BASIC_FORMAT);
            if (actualTrainingDate.isAfter(LocalDate.now())) {
                return false;
            }
        }
        return true;
    }

} //end of class
