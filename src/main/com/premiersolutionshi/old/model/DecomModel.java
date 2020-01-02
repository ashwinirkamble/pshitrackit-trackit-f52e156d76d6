package com.premiersolutionshi.old.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.premiersolutionshi.old.bean.DecomBean;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.bean.ShipBean;
import com.premiersolutionshi.old.util.CommonMethods;
/**
 * Business logic for the application's DECOM module
 *
 * @author   Anthony Tsuhako
 * @version  1.0, 11/25/2013
 * @since    JDK 7, Apache Struts 1.3.10
 */
public class DecomModel {
	private static  Logger logger = Logger.getLogger(DecomModel.class.getName());

	/****************************************************************************
	 * Function: debugLog
	 ****************************************************************************/
	private static void debugLog(String type, String functionName, Exception e) {
		debugLog(type, functionName, e.toString());
	} //end of debugLog

	private static void debugLog(String type, String functionName, String statement) {
		if (type.equals("INFO") || type.equals("SQL")) {
			logger.info(String.format("%11s%-30s | %-34s | %s", "", type, functionName, statement));
		} else if (type.equals("ERROR")) {
			logger.error(String.format("%11s%-30s | %-34s | %s", "", type, functionName, statement));
		} else {
			logger.debug(String.format("%11s%-30s | %-34s | %s", "", type, functionName, statement));
		} //end of else
	} //end of debugLog

	/****************************************************************************
	 * Function: getSummaryList
	 ****************************************************************************/
	public static ArrayList<DecomBean> getSummaryList(Connection conn, DecomBean inputBean) {
		StringBuffer sqlStmt = new StringBuffer("SELECT decom_workflow_pk, uic, ship_name, type, hull, tycom_display, homeport, computer_name, rsupply, laptop_tag, scanner_tag, "
	        + "system_received_date_fmt, decom_date_fmt, ship_contacted_date_fmt, transmittal_recon_date_fmt, system_returned_date_fmt, backup_date_fmt, "
	        + "transmittal_check_date_fmt, hardware_status, hardware_status_notes, laptop_reset_date_fmt, comments "
	        + "FROM decom_workflow_vw");

		//WHERE
		if (!CommonMethods.isEmpty(inputBean.getContractNumber())) sqlStmt.append(" WHERE ship_fk IN (SELECT ship_pk FROM configured_system_vw WHERE contract_number = ?)");

		//ORDER BY
		sqlStmt.append(" ORDER BY decom_date");

		ArrayList<DecomBean> resultList = new ArrayList<DecomBean>();

		debugLog("SQL", "getSummaryList", sqlStmt.toString());

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
			if (!CommonMethods.isEmpty(inputBean.getContractNumber())) pStmt.setString(1, inputBean.getContractNumber());
			ResultSet rs = pStmt.executeQuery();
			String prevUic = null;
			while (rs.next()) {
				if (rs.getString("uic").equals(CommonMethods.nes(prevUic))) {
					DecomBean resultBean = resultList.remove(resultList.size()-1);

					if (!CommonMethods.isEmpty(rs.getString("computer_name"))) resultBean.setComputerName((!CommonMethods.isEmpty(resultBean.getComputerName()) ? resultBean.getComputerName() + ", " : "") + rs.getString("computer_name"));
					if (!CommonMethods.isEmpty(rs.getString("laptop_tag"))) resultBean.setLaptopTag((!CommonMethods.isEmpty(resultBean.getLaptopTag()) ? resultBean.getLaptopTag() + ", " : "") + rs.getString("laptop_tag"));
					if (!CommonMethods.isEmpty(rs.getString("scanner_tag"))) resultBean.setScannerTag((!CommonMethods.isEmpty(resultBean.getScannerTag()) ? resultBean.getScannerTag() + ", " : "") + rs.getString("scanner_tag"));

					resultList.add(resultBean);
				} else {
					DecomBean resultBean = new DecomBean();
					resultBean.setDecomWorkflowPk(rs.getString("decom_workflow_pk"));
					resultBean.setUic(rs.getString("uic"));
					resultBean.setShipName(rs.getString("ship_name"));
					resultBean.setType(rs.getString("type"));
					resultBean.setHull(rs.getString("hull"));
					resultBean.setTycomDisplay(rs.getString("tycom_display"));
					resultBean.setHomeport(rs.getString("homeport"));
					resultBean.setRsupply(rs.getString("rsupply"));
					resultBean.setComputerName(rs.getString("computer_name"));
					resultBean.setLaptopTag(rs.getString("laptop_tag"));
					resultBean.setScannerTag(rs.getString("scanner_tag"));
					resultBean.setSystemReceivedDate(rs.getString("system_received_date_fmt"));
					resultBean.setDecomDate(rs.getString("decom_date_fmt"));
					resultBean.setSystemReturnedDate(rs.getString("system_returned_date_fmt"));
					resultBean.setShipContactedDate(rs.getString("ship_contacted_date_fmt"));
					resultBean.setBackupDate(rs.getString("backup_date_fmt"));
					resultBean.setTransmittalCheckDate(rs.getString("transmittal_check_date_fmt"));
					resultBean.setTransmittalReconDate(rs.getString("transmittal_recon_date_fmt"));
					resultBean.setHardwareStatus(rs.getString("hardware_status"));
					resultBean.setHardwareStatusNotes(rs.getString("hardware_status_notes"));
					resultBean.setLaptopResetDate(rs.getString("laptop_reset_date_fmt"));
					resultBean.setComments(rs.getString("comments"));
					resultList.add(resultBean);
					prevUic = rs.getString("uic");
				} //end of else
			} //end of while
		} catch (Exception e) {
			debugLog("ERROR", "getSummaryList", e);
		} //end of catch

		return resultList;
	} //end of getSummaryList

	/****************************************************************************
	 * Function: getWorkflowMap
	 ****************************************************************************/
	public static HashMap<String, DecomBean> getWorkflowMap(Connection conn) {
		String sqlStmt = "SELECT decom_workflow_pk, ship_name, uic, type, hull, tycom_display, homeport, computer_name, rsupply, laptop_tag, scanner_tag, system_received_date_fmt, decom_date_fmt, ship_contacted_date_fmt, transmittal_recon_date_fmt, system_returned_date_fmt, backup_date_fmt, transmittal_check_date_fmt, hardware_status, hardware_status_notes, laptop_reset_date_fmt, comments FROM decom_workflow_vw ORDER BY decom_date";
		HashMap<String, DecomBean> resultMap = new HashMap<String, DecomBean>();

		debugLog("SQL", "getWorkflowMap", sqlStmt);

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				DecomBean resultBean = new DecomBean();
				resultBean.setDecomWorkflowPk(rs.getString("decom_workflow_pk"));
				resultBean.setShipName(rs.getString("ship_name"));
				resultBean.setType(rs.getString("type"));
				resultBean.setHull(rs.getString("hull"));
				resultBean.setTycomDisplay(rs.getString("tycom_display"));
				resultBean.setHomeport(rs.getString("homeport"));
				resultBean.setRsupply(rs.getString("rsupply"));
				resultBean.setComputerName(rs.getString("computer_name"));
				resultBean.setLaptopTag(rs.getString("laptop_tag"));
				resultBean.setScannerTag(rs.getString("scanner_tag"));
				resultBean.setSystemReceivedDate(rs.getString("system_received_date_fmt"));
				resultBean.setDecomDate(rs.getString("decom_date_fmt"));
				resultBean.setSystemReturnedDate(rs.getString("system_returned_date_fmt"));
				resultBean.setShipContactedDate(rs.getString("ship_contacted_date_fmt"));
				resultBean.setBackupDate(rs.getString("backup_date_fmt"));
				resultBean.setTransmittalCheckDate(rs.getString("transmittal_check_date_fmt"));
				resultBean.setTransmittalReconDate(rs.getString("transmittal_recon_date_fmt"));
				resultBean.setHardwareStatus(rs.getString("hardware_status"));
				resultBean.setHardwareStatusNotes(rs.getString("hardware_status_notes"));
				resultBean.setLaptopResetDate(rs.getString("laptop_reset_date_fmt"));
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
	public static DecomBean getWorkflowBean(Connection conn, int decomWorkflowPk) {
		return getWorkflowBean(conn, decomWorkflowPk, null);
	} //end of getWorkflowBean

	public static DecomBean getWorkflowBean(Connection conn, String uic) {
		return getWorkflowBean(conn, -1, uic);
	} //end of getWorkflowBean

	private static DecomBean getWorkflowBean(Connection conn, int decomWorkflowPk, String uic) {
		String sqlStmt = "SELECT decom_workflow_pk, ship_name, type, hull, tycom_display, homeport, computer_name, rsupply, laptop_tag, scanner_tag, system_received_date_fmt, decom_date_fmt, ship_contacted_date_fmt, transmittal_recon_date_fmt, system_returned_date_fmt, backup_date_fmt, transmittal_check_date_fmt, hardware_status, hardware_status_notes, laptop_reset_date_fmt, comments FROM decom_workflow_vw";
		String sqlWhere = decomWorkflowPk > -1 ? " WHERE decom_workflow_pk = ?" : " WHERE uic = ?";
		DecomBean resultBean = new DecomBean();

		if (decomWorkflowPk <= -1 && CommonMethods.isEmpty(uic)) return null;

		debugLog("SQL", "getWorkflowBean", sqlStmt + sqlWhere + (decomWorkflowPk > -1 ? " (decom_workflow_pk = " + decomWorkflowPk + ")" : " (uic = " + CommonMethods.nes(uic) + ")"));

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt + sqlWhere)) {
			if (decomWorkflowPk > -1)
				pStmt.setInt(1, decomWorkflowPk);
			else
				pStmt.setString(1, uic);

			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				resultBean.setDecomWorkflowPk(rs.getString("decom_workflow_pk"));
				resultBean.setShipName(rs.getString("ship_name"));
				resultBean.setType(rs.getString("type"));
				resultBean.setHull(rs.getString("hull"));
				resultBean.setTycomDisplay(rs.getString("tycom_display"));
				resultBean.setHomeport(rs.getString("homeport"));
				resultBean.setRsupply(rs.getString("rsupply"));
				if (!CommonMethods.isEmpty(rs.getString("computer_name"))) resultBean.setComputerName((!CommonMethods.isEmpty(resultBean.getComputerName()) ? resultBean.getComputerName() + ", " : "") + rs.getString("computer_name"));
				if (!CommonMethods.isEmpty(rs.getString("laptop_tag"))) resultBean.setLaptopTag((!CommonMethods.isEmpty(resultBean.getLaptopTag()) ? resultBean.getLaptopTag() + ", " : "") + rs.getString("laptop_tag"));
				if (!CommonMethods.isEmpty(rs.getString("scanner_tag"))) resultBean.setScannerTag((!CommonMethods.isEmpty(resultBean.getScannerTag()) ? resultBean.getScannerTag() + ", " : "") + rs.getString("scanner_tag"));
				resultBean.setSystemReceivedDate(rs.getString("system_received_date_fmt"));
				resultBean.setShipContactedDate(rs.getString("ship_contacted_date_fmt"));
				resultBean.setDecomDate(rs.getString("decom_date_fmt"));
				resultBean.setSystemReturnedDate(rs.getString("system_returned_date_fmt"));
				resultBean.setBackupDate(rs.getString("backup_date_fmt"));
				resultBean.setTransmittalCheckDate(rs.getString("transmittal_check_date_fmt"));
				resultBean.setTransmittalReconDate(rs.getString("transmittal_recon_date_fmt"));
				resultBean.setHardwareStatus(rs.getString("hardware_status"));
				resultBean.setHardwareStatusNotes(rs.getString("hardware_status_notes"));
				resultBean.setLaptopResetDate(rs.getString("laptop_reset_date_fmt"));
				resultBean.setComments(rs.getString("comments"));
			} //end of while
		} catch (Exception e) {
			debugLog("ERROR", "getWorkflowBean", e);
		} //end of catch

		return resultBean;
	} //end of getWorkflowBean

	/****************************************************************************
	 * Function: insertWorkflow
	 ****************************************************************************/
	public static boolean insertWorkflow(Connection conn, DecomBean inputBean, LoginBean loginBean) {
		String sqlStmt = "INSERT INTO decom_workflow (ship_fk, last_updated_by, last_updated_date) VALUES (?,?,?)";
		boolean ranOk = false;

		debugLog("SQL", "insertWorkflow", sqlStmt);

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
			conn.setAutoCommit(false);
			CommonMethods.setInt(pStmt, 1, inputBean.getShipPk());
			CommonMethods.setString(pStmt, 2, loginBean.getFullName());
			pStmt.setString(3, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
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
	public static boolean updateWorkflow(Connection conn, DecomBean inputBean, LoginBean loginBean) {
		String sqlStmt = "UPDATE decom_workflow SET system_received_date = ?, decom_date = ?, ship_contacted_date = ?, transmittal_recon_date = ?, system_returned_date = ?, backup_date = ?, transmittal_check_date = ?, hardware_status = ?, hardware_status_notes = ?, laptop_reset_date = ?, comments = ?, last_updated_by = ?, last_updated_date = ? WHERE decom_workflow_pk = ?";
		boolean ranOk = false;

		if (CommonMethods.cInt(inputBean.getDecomWorkflowPk()) == -1) return false;

		debugLog("SQL", "updateWorkflow", sqlStmt + " (decom_workflow_pk = " + inputBean.getDecomWorkflowPk() + ")");

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
			conn.setAutoCommit(false);
			CommonMethods.setDate(pStmt, 1, inputBean.getSystemReceivedDate());
			CommonMethods.setDate(pStmt, 2, inputBean.getDecomDate());
			CommonMethods.setDate(pStmt, 3, inputBean.getShipContactedDate());
			CommonMethods.setDate(pStmt, 4, inputBean.getTransmittalReconDate());
			CommonMethods.setDate(pStmt, 5, inputBean.getSystemReturnedDate());
			CommonMethods.setDate(pStmt, 6, inputBean.getBackupDate());
			CommonMethods.setDate(pStmt, 7, inputBean.getTransmittalCheckDate());

			CommonMethods.setString(pStmt, 8, inputBean.getHardwareStatus());
			CommonMethods.setString(pStmt, 9, inputBean.getHardwareStatusNotes());
			CommonMethods.setDate(pStmt, 10, inputBean.getLaptopResetDate());

			CommonMethods.setString(pStmt, 11, inputBean.getComments());
			CommonMethods.setString(pStmt, 12, loginBean.getFullName());
			pStmt.setString(13, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
			pStmt.setInt(14, CommonMethods.cInt(inputBean.getDecomWorkflowPk()));
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
	public static boolean deleteWorkflow(Connection conn, DecomBean inputBean) {
		String sqlStmt = "DELETE FROM decom_workflow WHERE decom_workflow_pk = ?";
		boolean ranOk = false;

		if (CommonMethods.cInt(inputBean.getDecomWorkflowPk()) <= -1) return false;

		debugLog("SQL", "deleteWorkflow", sqlStmt + " (decom_workflow_pk = " + inputBean.getDecomWorkflowPk() + ")");

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
			conn.setAutoCommit(false);
			pStmt.setInt(1, CommonMethods.cInt(inputBean.getDecomWorkflowPk()));
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
	public static ArrayList<ShipBean> getAvailShipList(Connection conn, String contractNumber) {
		StringBuffer sqlStmt = new StringBuffer("SELECT "
		        + "ship_pk, uic, ship_name, type, hull, service_code, homeport, tycom, tycom_display, rsupply "
		        + "FROM ship_vw "
		        + "WHERE ship_pk NOT IN (SELECT ship_fk FROM decom_workflow)");

		//WHERE
		if (!CommonMethods.isEmpty(contractNumber)) sqlStmt.append(" AND ship_pk IN (SELECT ship_pk FROM configured_system_vw WHERE contract_number = ?)");

		//ORDER BY
		sqlStmt.append(" ORDER BY ship_name");

		ArrayList<ShipBean> resultList = new ArrayList<ShipBean>();

		debugLog("SQL", "getAvailShipList", sqlStmt.toString());

		try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
			if (!CommonMethods.isEmpty(contractNumber)) pStmt.setString(1, contractNumber);
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
} //end of class
