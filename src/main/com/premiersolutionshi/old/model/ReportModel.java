package com.premiersolutionshi.old.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.premiersolutionshi.old.bean.ChartBean;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.bean.ReportBean;
import com.premiersolutionshi.old.bean.SystemBean;
import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Business logic for the application's REPORT module
 */
public final class ReportModel {
    private static Logger logger = Logger.getLogger(ReportModel.class.getSimpleName());

    private static void debugLog(String type, String functionName, Exception e) {
        debugLog(type, functionName, e.toString());
    }

    private static void debugLog(String type, String functionName, String statement) {
        if (type.equals("INFO") || type.equals("SQL")) {
            logger.info(String.format("%10s%-30s | %-34s | %s", "", type, functionName, statement));
        } else if (type.equals("ERROR")) {
            logger.error(String.format("%10s%-30s | %-34s | %s", "", type, functionName, statement));
        } else {
            logger.debug(String.format("%10s%-30s | %-34s | %s", "", type, functionName, statement));
        }
    }

    public static ReportBean getLatestSummaryBean(Connection conn) {
        String sqlStmt = "SELECT strftime('%m/%d/%Y', MAX(report_date)) AS report_date_fmt, COUNT(1) AS transmittal_cnt, COUNT(DISTINCT ship_name) AS ship_cnt, SUM(doc_cnt) AS sum_doc_cnt FROM logcop_report";
        ReportBean resultBean = new ReportBean();
        NumberFormat nf = new DecimalFormat("###,###,###,##0");

        debugLog("SQL", "getLatestSummaryBean", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                resultBean.setReportDate(rs.getString("report_date_fmt"));
                resultBean.setTransmittalCnt(nf.format(rs.getDouble("transmittal_cnt")));
                resultBean.setShipCnt(nf.format(rs.getDouble("ship_cnt")));
                resultBean.setDocCnt(nf.format(rs.getDouble("sum_doc_cnt")));
            }
        } catch (Exception e) {
            debugLog("ERROR", "getLatestSummaryBean", e);
        }

        return resultBean;
    }

    public static ReportBean getDifferenceBean(Connection conn) {
        String sqlStmt = "SELECT (SELECT strftime('%m/%d/%Y', MIN(report_date)) FROM logcop_report_prev) as report_date, COUNT(DISTINCT ship_name) AS ship_cnt, COUNT(DISTINCT transmittal_num) AS transmittal_cnt, SUM(doc_cnt) AS sum_doc_cnt FROM (SELECT ship_name, transmittal_num, doc_type, facet_version, upload_date, upload_user, doc_cnt FROM logcop_report EXCEPT SELECT ship_name, transmittal_num, doc_type, facet_version, upload_date, upload_user, doc_cnt FROM logcop_report_prev ORDER BY ship_name, transmittal_num)";
        ReportBean resultBean = new ReportBean();
        NumberFormat nf = new DecimalFormat("###,###,###,##0");

        debugLog("SQL", "getDifferenceBean", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                resultBean.setReportDate(rs.getString("report_date"));
                resultBean.setTransmittalCnt(nf.format(rs.getDouble("transmittal_cnt")));
                resultBean.setShipCnt(nf.format(rs.getDouble("ship_cnt")));
                resultBean.setDocCnt(nf.format(rs.getDouble("sum_doc_cnt")));
            }
        } catch (Exception e) {
            debugLog("ERROR", "getDifferenceBean", e);
        }

        return resultBean;
    }

    public static ArrayList<ReportBean> getDifferenceList(Connection conn) {
        String sqlStmt = "SELECT DISTINCT ship_pk, ship_name, computer_name, transmittal_num, strftime('%m/%d/%Y', upload_date) AS upload_date_fmt FROM (SELECT ship_pk, ship_name, computer_name, transmittal_num, doc_type, facet_version, upload_date, upload_user, doc_cnt FROM logcop_report_vw EXCEPT SELECT ship_pk, ship_name, computer_name, transmittal_num, doc_type, facet_version, upload_date, upload_user, doc_cnt FROM logcop_report_prev_vw ORDER BY ship_name, computer_name, transmittal_num)";
        ArrayList<ReportBean> resultList = new ArrayList<ReportBean>();
        //String shipPk = null, shipName = null, computerName = null, uploadDate = null;
        String shipName = null, computerName = null, uploadDate = null;
        ArrayList<String> transmittalList = new ArrayList<>();

        debugLog("SQL", "getDifferenceList", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                if (!CommonMethods.nes(computerName).equals(rs.getString("computer_name"))) {
                    if (!CommonMethods.isEmpty(computerName)) {
                        ReportBean resultBean = new ReportBean();
                        resultBean.setShipName(shipName);
                        resultBean.setFacetName(computerName);
                        resultBean.setUploadDate(uploadDate);
                        resultBean.setTransmittalNum(printTransmittalList(transmittalList));
                        resultList.add(resultBean);
                    }
                    //String shipPk = rs.getString("ship_pk");
                    shipName = rs.getString("ship_name");
                    computerName = rs.getString("computer_name");
                    uploadDate = rs.getString("upload_date_fmt");
                    transmittalList.clear();
                }

                uploadDate = (CommonMethods.isValidDateStr(uploadDate) && CommonMethods.isValidDateStr(rs.getString("upload_date_fmt")) && (CommonMethods.dateDiff(uploadDate, rs.getString("upload_date_fmt")) < 0)) ? uploadDate : rs.getString("upload_date_fmt");
                transmittalList.add(rs.getString("transmittal_num"));
            }

            if (!CommonMethods.isEmpty(computerName)) {
                ReportBean resultBean = new ReportBean();
                resultBean.setShipName(shipName);
                resultBean.setFacetName(computerName);
                resultBean.setUploadDate(uploadDate);
                resultBean.setTransmittalNum(printTransmittalList(transmittalList));
                resultList.add(resultBean);
            }
        } catch (Exception e) {
            debugLog("ERROR", "getDifferenceList", e);
        }

        return resultList;
    }

    public static String printTransmittalList(ArrayList<String> resultList) {
        StringBuffer returnStr = new StringBuffer();

        if (resultList == null || resultList.size() <= 0) return new String();

        for (int i = 0; i < resultList.size(); i++) {
            int transmittalNum = CommonMethods.cInt(resultList.get(i));
            if (returnStr.length() > 0) returnStr.append(", ");
            returnStr.append(String.valueOf(transmittalNum));

            while (i + 1 < resultList.size() && (CommonMethods.cInt(resultList.get(i))+1) == CommonMethods.cInt(resultList.get(i+1))) {
                i++;
            }
            if (transmittalNum != CommonMethods.cInt(resultList.get(i))) returnStr.append("-" +  CommonMethods.cInt(resultList.get(i)));
        }

        return returnStr.toString();
    }

    public static ArrayList<ReportBean> getMissingTransmittalList(Connection conn) {
        String sqlStmt = "SELECT ship_pk, ship_name, computer_name, transmittal_num FROM logcop_report_vw WHERE ship_pk IS NOT NULL AND transmittal_num < 999999 ORDER BY ship_name, computer_name, transmittal_order, doc_type_order";
        ArrayList<ReportBean> resultList = new ArrayList<ReportBean>();

        HashMap<String, String> exceptionMap = getExceptionMap(conn);

        String shipPk = null, shipName = null, computerName = null;
        ArrayList<ReportBean> transmittalList = null;
        int prevTransmittalNum = 0;

        debugLog("SQL", "getMissingTransmittalList", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                if (!CommonMethods.nes(computerName).equals(rs.getString("computer_name"))) {
                    if (!CommonMethods.isEmpty(computerName) && transmittalList != null && transmittalList.size() > 0) {
                        ReportBean resultBean = new ReportBean();
                        resultBean.setShipPk(shipPk);
                        resultBean.setShipName(shipName);
                        resultBean.setFacetName(computerName);
                        resultBean.setTransmittalList(transmittalList);
                        resultList.add(resultBean);
                    }
                    shipPk = rs.getString("ship_pk");
                    shipName = rs.getString("ship_name");
                    computerName = rs.getString("computer_name");
                    prevTransmittalNum = 0;
                    transmittalList = new ArrayList<ReportBean>();
                }

                for (int i = prevTransmittalNum + 1; i < rs.getInt("transmittal_num"); i++) {
                    ReportBean missingBean = new ReportBean();
                    missingBean.setTransmittalNum(CommonMethods.padString(i, "0", 6));
                    missingBean.setExceptionReason(exceptionMap.get(shipPk + "_" + i));
                    transmittalList.add(missingBean);
                }
                prevTransmittalNum = rs.getInt("transmittal_num");
            }

            if (!CommonMethods.isEmpty(computerName) && transmittalList != null && transmittalList.size() > 0) {
                ReportBean resultBean = new ReportBean();
                resultBean.setShipPk(shipPk);
                resultBean.setShipName(shipName);
                resultBean.setFacetName(computerName);
                resultBean.setTransmittalList(transmittalList);
                resultList.add(resultBean);
            }
        } catch (Exception e) {
            debugLog("ERROR", "getMissingTransmittalList", e);
        }

        return resultList;
    }

    public static ArrayList<String> getMissingTransmittalList(Connection conn, int shipPk) {
        String sqlStmt = "SELECT transmittal_num FROM logcop_report_vw WHERE ship_pk = ? AND transmittal_num < 999999 ORDER BY transmittal_order, doc_type_order";
        ArrayList<String> resultList = new ArrayList<String>();
        HashMap<String, String> exceptionMap = getExceptionMap(conn);
        int prevTransmittalNum = 0;

        debugLog("SQL", "getMissingTransmittalList", sqlStmt + " (" + shipPk + ")");

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, shipPk);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                for (int i = prevTransmittalNum + 1; i < rs.getInt("transmittal_num"); i++) {
                    if (CommonMethods.isEmpty(exceptionMap.get(shipPk + "_" + i))) {
                        resultList.add(CommonMethods.padString(i, "0", 6));
                    }
                }
                prevTransmittalNum = rs.getInt("transmittal_num");
            }
        } catch (Exception e) {
            debugLog("ERROR", "getMissingTransmittalList", e);
        }

        return resultList;
    }

    public static HashMap<String, ArrayList<String>> getMissingTransmittalMap(Connection conn) {
        String sqlStmt = "SELECT ship_pk, ship_name, transmittal_num "
            + "FROM logcop_report_vw "
            + "WHERE ship_pk IS NOT NULL "
            + "AND transmittal_num < 999999 "
            + "ORDER BY ship_name, transmittal_order, doc_type_order";
        HashMap<String, ArrayList<String>> resultMap = new HashMap<String, ArrayList<String>>();

        HashMap<String, String> exceptionMap = getExceptionMap(conn);

        String shipPk = null;
        int prevTransmittalNum = 0;

        debugLog("SQL", "getMissingTransmittalMap", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                if (!CommonMethods.nes(shipPk).equals(rs.getString("ship_pk"))) {
                    shipPk = rs.getString("ship_pk");
                    prevTransmittalNum = 0;
                }

                for (int i = prevTransmittalNum + 1; i < rs.getInt("transmittal_num"); i++) {
                    if (CommonMethods.isEmpty(exceptionMap.get(shipPk + "_" + i))) {
                        if (resultMap.get(shipPk) == null) resultMap.put(shipPk, new ArrayList<String>());
                        resultMap.get(shipPk).add(CommonMethods.padString(i, "0", 6));
                    }
                }
                prevTransmittalNum = rs.getInt("transmittal_num");
            }
        } catch (Exception e) {
            debugLog("ERROR", "getMissingTransmittalMap", e);
        }

        return resultMap;
    }

    public static ArrayList<ReportBean> getTransmittalSummaryList(Connection conn) {
        String sqlStmt = "SELECT DISTINCT ship_pk, ship_name, computer_name, type, hull, homeport, last_transmittal_num, last_upload_date_fmt, "
            + "form_1348_upload_date_fmt, form_1149_upload_date_fmt, food_approval_upload_date_fmt, food_receipt_upload_date_fmt, "
            + "pcard_admin_upload_date_fmt, pcard_invoice_upload_date_fmt, price_change_upload_date_fmt, sfoedl_upload_date_fmt, uol_upload_date_fmt "
            + "FROM configured_system_transmittal_vw "
            + "WHERE is_prepped_ind = 'A' "
            + "AND ship_pk IS NOT NULL "
            + "ORDER BY ship_name, computer_name";
        ArrayList<ReportBean> resultList = new ArrayList<ReportBean>();
        HashMap<String, ArrayList<String>> missingTransmittalMap = getMissingTransmittalMap(conn);
        HashMap<String, String> s2ClosureMap = SystemModel.getS2ClosureShipPkMap(conn);
        HashMap<String, String> fuelClosureMap = SystemModel.getFuelClosureShipPkMap(conn);

        String dueDate = CommonMethods.getDate("MM/01/YYYY", CommonMethods.cInt(CommonMethods.getDate("DD")) < 7 ? -7 : 0); //If current day is < 7, set to 1st of previous month; else set to first of current month

        debugLog("SQL", "getTransmittalSummaryList", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                ReportBean resultBean = new ReportBean();
                resultBean.setShipPk(rs.getString("ship_pk"));
                resultBean.setShipName(rs.getString("ship_name") + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG") ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")" : ""));
                resultBean.setFacetName(rs.getString("computer_name"));
                resultBean.setHomeport(rs.getString("homeport"));
                resultBean.setLastTransmittalNum(rs.getString("last_transmittal_num"));
                resultBean.setLastUploadDate(rs.getString("last_upload_date_fmt"));
                resultBean.setForm1348UploadDate(rs.getString("form_1348_upload_date_fmt"));
                resultBean.setForm1149UploadDate(rs.getString("form_1149_upload_date_fmt"));
                resultBean.setFoodApprovalUploadDate(rs.getString("food_approval_upload_date_fmt"));
                resultBean.setFoodReceiptUploadDate(rs.getString("food_receipt_upload_date_fmt"));
                resultBean.setPcardAdminUploadDate(rs.getString("pcard_admin_upload_date_fmt"));
                resultBean.setPcardInvoiceUploadDate(rs.getString("pcard_invoice_upload_date_fmt"));
                resultBean.setPriceChangeUploadDate(rs.getString("price_change_upload_date_fmt"));
                resultBean.setSfoedlUploadDate(rs.getString("sfoedl_upload_date_fmt"));

                if (!CommonMethods.isEmpty(rs.getString("type")) && (rs.getString("type").equals("SSN") || rs.getString("type").equals("SSBN") || rs.getString("type").equals("SSGN"))) {
                    resultBean.setUolUploadDate("N/A");
                } else {
                    resultBean.setUolUploadDate(rs.getString("uol_upload_date_fmt"));
                }

                resultBean.setMissingTransmittalList(missingTransmittalMap.get(rs.getString("ship_pk")));

                //Determine red/yellow/green status colors
                if (CommonMethods.isEmpty(rs.getString("last_upload_date_fmt"))) {
                    resultBean.setLastUploadDateCss("background:#d00;"); //red
                } else {
                    resultBean.setLastUploadDateCss("background:#0c0;font-weight:bold;"); //green
                }

                resultBean.setForm1348UploadDateCss(getStatusCss(rs.getString("form_1348_upload_date_fmt"), 10));

                if (!CommonMethods.isEmpty(fuelClosureMap.get(rs.getString("ship_pk")))) {
                    resultBean.setFuelClosureNotes("No refueling until " + fuelClosureMap.get(rs.getString("ship_pk")));
                    if (CommonMethods.isEmpty(rs.getString("form_1149_upload_date_fmt"))) {
                        resultBean.setForm1149UploadDateCss("background:#d00;"); //red
                    } else {
                        resultBean.setForm1149UploadDateCss("background:#999;"); //gray
                    }
                } else {
                    resultBean.setForm1149UploadDateCss(getStatusCss(rs.getString("form_1149_upload_date_fmt"), dueDate));
                }

                if (!CommonMethods.isEmpty(s2ClosureMap.get(rs.getString("ship_pk")))) {
                    resultBean.setS2ClosureNotes("S2 Galley closed until " + s2ClosureMap.get(rs.getString("ship_pk")));
                    if (CommonMethods.isEmpty(rs.getString("food_approval_upload_date_fmt"))) {
                        resultBean.setFoodApprovalUploadDateCss("background:#d00;"); //red
                    } else {
                        resultBean.setFoodApprovalUploadDateCss("background:#999;"); //gray
                    }

                    if (CommonMethods.isEmpty(rs.getString("food_receipt_upload_date_fmt"))) {
                        resultBean.setFoodReceiptUploadDateCss("background:#d00;"); //red
                    } else {
                        resultBean.setFoodReceiptUploadDateCss("background:#999;"); //gray
                    }
                } else {
                    resultBean.setFoodApprovalUploadDateCss(getStatusCss(rs.getString("food_approval_upload_date_fmt"), 10));
                    resultBean.setFoodReceiptUploadDateCss(getStatusCss(rs.getString("food_receipt_upload_date_fmt"), 10));
                }

                resultBean.setPcardAdminUploadDateCss(getStatusCss(rs.getString("pcard_admin_upload_date_fmt"), 365));
                resultBean.setPcardInvoiceUploadDateCss(getStatusCss(rs.getString("pcard_invoice_upload_date_fmt"), dueDate));
                resultBean.setPriceChangeUploadDateCss(getStatusCss(rs.getString("price_change_upload_date_fmt"), dueDate));
                resultBean.setSfoedlUploadDateCss(getStatusCss(rs.getString("sfoedl_upload_date_fmt"), dueDate));

                if (!CommonMethods.isEmpty(rs.getString("type")) && (rs.getString("type").equals("SSN") || rs.getString("type").equals("SSBN") || rs.getString("type").equals("SSGN"))) {
                    resultBean.setUolUploadDateCss("background:#999;"); //gray
                } else {
                    resultBean.setUolUploadDateCss(getStatusCss(rs.getString("uol_upload_date_fmt"), dueDate));
                }

                if (resultBean.getMissingTransmittalList() != null && resultBean.getMissingTransmittalList().size() > 0) {
                    resultBean.setMissingTransmittalCss("background:#d00;"); //red
                } else {
                    resultBean.setMissingTransmittalCss("background:#0c0;"); //green
                }

//                if (CommonMethods.isEmpty(configuredSystemBean.getSfoedlUploadDate()) || CommonMethods.isValidDateStr(configuredSystemBean.getSfoedlUploadDate()) && CommonMethods.dateDiff(configuredSystemBean.getSfoedlUploadDate(), dueDate) >= 0) {

                resultList.add(resultBean);
            }
        } catch (Exception e) {
            debugLog("ERROR", "getTransmittalSummaryList", e);
        }

        return resultList;
    }

    private static String getStatusCss(String date, int periodicity) {
        if (CommonMethods.isEmpty(date)) {
            return "background:#d00;"; //red
        } else if (CommonMethods.isValidDateStr(date) && CommonMethods.dateDiff(date, CommonMethods.getDate("MM/DD/YYYY")) > periodicity) {
            return "background:#ff0;"; //yellow
        } else {
            return "background:#0c0;"; //green
        }
    }

    private static String getStatusCss(String date, String dueDate) {
        if (CommonMethods.isEmpty(date)) {
            return "background:#d00;"; //red
        } else if (CommonMethods.isValidDateStr(date) && CommonMethods.dateDiff(date, dueDate) >= 0) {
            return "background:#ff0;"; //yellow
        } else {
            return "background:#0c0;"; //green
        }
    }

    public static ReportBean getTransmittalSummaryBean(Connection conn, int shipPk) {
        String sqlStmt = "SELECT last_transmittal_num, last_upload_date_fmt, form_1348_upload_date_fmt, "
            + "form_1149_upload_date_fmt, food_approval_upload_date_fmt, food_receipt_upload_date_fmt, "
            + "pcard_admin_upload_date_fmt, pcard_invoice_upload_date_fmt, price_change_upload_date_fmt, "
            + "sfoedl_upload_date_fmt, uol_upload_date_fmt "
            + "FROM configured_system_transmittal_vw WHERE ship_pk = ?";
        ReportBean resultBean = new ReportBean();

        debugLog("SQL", "getTransmittalSummaryBean", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, shipPk);
            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                resultBean.setLastTransmittalNum(rs.getString("last_transmittal_num"));
                resultBean.setLastUploadDate(rs.getString("last_upload_date_fmt"));
                resultBean.setForm1348UploadDate(rs.getString("form_1348_upload_date_fmt"));
                resultBean.setForm1149UploadDate(rs.getString("form_1149_upload_date_fmt"));
                resultBean.setFoodApprovalUploadDate(rs.getString("food_approval_upload_date_fmt"));
                resultBean.setFoodReceiptUploadDate(rs.getString("food_receipt_upload_date_fmt"));
                resultBean.setPcardAdminUploadDate(rs.getString("pcard_admin_upload_date_fmt"));
                resultBean.setPcardInvoiceUploadDate(rs.getString("pcard_invoice_upload_date_fmt"));
                resultBean.setPriceChangeUploadDate(rs.getString("price_change_upload_date_fmt"));
                resultBean.setSfoedlUploadDate(rs.getString("sfoedl_upload_date_fmt"));
                resultBean.setUolUploadDate(rs.getString("uol_upload_date_fmt"));
            }
        } catch (Exception e) {
            debugLog("ERROR", "getTransmittalSummaryBean", e);
        }

        return resultBean;
    }

    public static HashMap<String, String> getExceptionMap(Connection conn) {
        String sqlStmt = "SELECT ship_fk, transmittal_num, exception_reason FROM transmittal_exception";
        HashMap<String, String> resultMap = new HashMap<String, String>();

        debugLog("SQL", "getExceptionMap", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                resultMap.put(rs.getString("ship_fk") + "_" + rs.getString("transmittal_num"), rs.getString("exception_reason"));
            }
        } catch (Exception e) {
            debugLog("ERROR", "getExceptionMap", e);
        }

        return resultMap;
    }

    public static boolean saveExceptionList(Connection conn, ReportBean inputBean, LoginBean loginBean) {
        boolean ranOk = false;

        try {
            conn.setAutoCommit(false);
            ranOk = deleteExceptionList(conn) && insertExceptionList(conn, inputBean);
        } catch (Exception e) {
            debugLog("ERROR", "saveExceptionList", e);
            ranOk = false;
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }

        return ranOk;
    }

    public static boolean deleteExceptionList(Connection conn) {
        String sqlStmt = "DELETE FROM transmittal_exception";
        boolean ranOk = false;

        debugLog("SQL", "deleteExceptionList", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ranOk = (pStmt.executeUpdate() >= 0);
        } catch (Exception e) {
            debugLog("ERROR", "deleteExceptionList", e);
        }

        return ranOk;
    }

    public static boolean insertExceptionList(Connection conn, ReportBean inputBean) {
        String sqlStmt = "INSERT INTO transmittal_exception (ship_fk, transmittal_num, exception_reason) VALUES (?,?,?)";
        boolean ranOk = true;

        if (inputBean.getExceptionReasonArr().length <= 0) return true; //Nothing to insert

        debugLog("SQL", "insertExceptionList", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            for (int i = 0; i < inputBean.getExceptionReasonArr().length; i++) {
                if (!CommonMethods.isEmpty(inputBean.getExceptionReasonArr()[i])) {
                    pStmt.setInt(1, CommonMethods.cInt(inputBean.getShipPkArr()[i]));
                    pStmt.setInt(2, CommonMethods.cInt(inputBean.getTransmittalNumArr()[i]));
                    pStmt.setString(3, inputBean.getExceptionReasonArr()[i]);
                    ranOk &= (pStmt.executeUpdate() == 1);
                }
            }
        } catch (Exception e) {
            debugLog("ERROR", "insertExceptionList", e);
            ranOk = false;
        }

        return ranOk;
    }

    public static ArrayList<String> getInactivityList(SystemBean configuredSystemBean) {
        ArrayList<String> resultList = new ArrayList<String>();
        //If current day is < 7, set to 1st of previous month; else set to first of current month
        String dueDate = CommonMethods.getDate("MM/01/YYYY", CommonMethods.cInt(CommonMethods.getDate("DD")) < 7 ? -7 : 0); 

        if (CommonMethods.isEmpty(configuredSystemBean.getForm1348UploadDate()) 
            || CommonMethods.isValidDateStr(configuredSystemBean.getForm1348UploadDate()) 
            && CommonMethods.dateDiff(configuredSystemBean.getForm1348UploadDate(), CommonMethods.getDate("MM/DD/YYYY")) > 10
        ) {
            resultList.add("Material 1348s (required every 10 days - "
                + (CommonMethods.isEmpty(configuredSystemBean.getForm1348UploadDate()) ? "no upload history" : "last upload " + configuredSystemBean.getForm1348UploadDate()) + ")");
        }

        if ((CommonMethods.isEmpty(configuredSystemBean.getFuelClosureDate())
                || CommonMethods.dateDiff(CommonMethods.getDate("MM/DD/YYYY"), configuredSystemBean.getFuelClosureDate()) <= 0)
            && (CommonMethods.isEmpty(configuredSystemBean.getForm1149UploadDate())
                || CommonMethods.isValidDateStr(configuredSystemBean.getForm1149UploadDate())
            && CommonMethods.dateDiff(configuredSystemBean.getForm1149UploadDate(), dueDate) >= 0)) {
            resultList.add("Fuel 1149s (required by the 7th of each month - " + (CommonMethods.isEmpty(configuredSystemBean.getForm1149UploadDate()) ? "no upload history" : "last upload " + configuredSystemBean.getForm1149UploadDate()) + ")");
        }

        if (CommonMethods.isEmpty(configuredSystemBean.getS2ClosureDate()) || CommonMethods.dateDiff(CommonMethods.getDate("MM/DD/YYYY"), configuredSystemBean.getS2ClosureDate()) <= 0) {
            if (CommonMethods.isEmpty(configuredSystemBean.getFoodReceiptUploadDate()) || CommonMethods.isValidDateStr(configuredSystemBean.getFoodReceiptUploadDate()) && CommonMethods.dateDiff(configuredSystemBean.getFoodReceiptUploadDate(), CommonMethods.getDate("MM/DD/YYYY")) > 10) {
                resultList.add("Food Receipts (required every 10 days - " + (CommonMethods.isEmpty(configuredSystemBean.getFoodReceiptUploadDate()) ? "no upload history" : "last upload " + configuredSystemBean.getFoodReceiptUploadDate()) + ")");
            }
            if (CommonMethods.isEmpty(configuredSystemBean.getFoodApprovalUploadDate()) || CommonMethods.isValidDateStr(configuredSystemBean.getFoodApprovalUploadDate()) && CommonMethods.dateDiff(configuredSystemBean.getFoodApprovalUploadDate(), CommonMethods.getDate("MM/DD/YYYY")) > 10) {
                resultList.add("Food Requisitions (required every 10 days - " + (CommonMethods.isEmpty(configuredSystemBean.getFoodApprovalUploadDate()) ? "no upload history" : "last upload " + configuredSystemBean.getFoodApprovalUploadDate()) + ")");
            }
        }

        if (CommonMethods.isEmpty(configuredSystemBean.getPcardAdminUploadDate()) || CommonMethods.isValidDateStr(configuredSystemBean.getPcardAdminUploadDate()) && CommonMethods.dateDiff(configuredSystemBean.getPcardAdminUploadDate(), CommonMethods.getDate("MM/DD/YYYY")) > 365) {
            resultList.add("Purchase Card - Admin Files (required within the past year - " + (CommonMethods.isEmpty(configuredSystemBean.getPcardAdminUploadDate()) ? "no upload history" : "last upload " + configuredSystemBean.getPcardAdminUploadDate()) + ")");
        }

        if (CommonMethods.isEmpty(configuredSystemBean.getPcardInvoiceUploadDate()) || CommonMethods.isValidDateStr(configuredSystemBean.getPcardInvoiceUploadDate()) && CommonMethods.dateDiff(configuredSystemBean.getPcardInvoiceUploadDate(), dueDate) >= 0) {
            resultList.add("Purchase Card - Invoice Files (required by the 7th of each month - " + (CommonMethods.isEmpty(configuredSystemBean.getPcardInvoiceUploadDate()) ? "no upload history" : "last upload " + configuredSystemBean.getPcardInvoiceUploadDate()) + ")");
        }

        if (CommonMethods.isEmpty(configuredSystemBean.getPriceChangeUploadDate()) || CommonMethods.isValidDateStr(configuredSystemBean.getPriceChangeUploadDate()) && CommonMethods.dateDiff(configuredSystemBean.getPriceChangeUploadDate(), dueDate) >= 0) {
            resultList.add("Price Change Reports (required by the 7th of each month - " + (CommonMethods.isEmpty(configuredSystemBean.getPriceChangeUploadDate()) ? "no upload history" : "last upload " + configuredSystemBean.getPriceChangeUploadDate()) + ")");
        }

        if (CommonMethods.isEmpty(configuredSystemBean.getSfoedlUploadDate()) || CommonMethods.isValidDateStr(configuredSystemBean.getSfoedlUploadDate()) && CommonMethods.dateDiff(configuredSystemBean.getSfoedlUploadDate(), dueDate) >= 0) {
            resultList.add("SFOEDL Reports (required by the 7th of each month - " + (CommonMethods.isEmpty(configuredSystemBean.getSfoedlUploadDate()) ? "no upload history" : "last upload " + configuredSystemBean.getSfoedlUploadDate()) + ")");
        }

        if (!configuredSystemBean.getType().equals("SSN") && !configuredSystemBean.getType().equals("SSBN") && !configuredSystemBean.getType().equals("SSGN") && (CommonMethods.isEmpty(configuredSystemBean.getUolUploadDate()) || CommonMethods.isValidDateStr(configuredSystemBean.getUolUploadDate()) 
            && CommonMethods.dateDiff(configuredSystemBean.getUolUploadDate(), dueDate) >= 0)) {
            resultList.add("UOL Reports (required by the 7th of each month - " + (CommonMethods.isEmpty(configuredSystemBean.getUolUploadDate()) ? "no upload history" : "last upload " + configuredSystemBean.getUolUploadDate()) + ")");
        }

        return resultList;
    }

    public static ArrayList<SystemBean> getFacetDiscrepancyList(Connection conn, int projectPk) {
        String sqlStmt = "SELECT "
            + "configured_system_pk, ship_name, type, hull, computer_name, facet_version, facet_version_order, transmittal_facet_version, "
            + "transmittal_facet_version_order "
            + "FROM configured_system_transmittal_vw "
            + "WHERE transmittal_facet_version_order IS NOT NULL "
            + "AND facet_version_order IS NOT NULL "
            + "AND transmittal_facet_version_order <> facet_version_order "
            + "ORDER BY ship_name, computer_name";
        ArrayList<SystemBean> resultList = new ArrayList<SystemBean>();

        debugLog("SQL", "getFacetDiscrepancyList", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                SystemBean resultBean = new SystemBean();
                resultBean.setConfiguredSystemPk(rs.getString("configured_system_pk"));
                resultBean.setShipName(rs.getString("ship_name") + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG") ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")" : ""));
                resultBean.setComputerName(rs.getString("computer_name"));
                resultBean.setFacetVersion(rs.getString("facet_version"));
                resultBean.setFacetVersionOrder(rs.getString("facet_version_order"));
                resultBean.setTransmittalFacetVersion(rs.getString("transmittal_facet_version"));
                resultBean.setTransmittalFacetVersionOrder(rs.getString("transmittal_facet_version_order"));
                resultList.add(resultBean);
            }
        } catch (Exception e) {
            debugLog("ERROR", "getFacetDiscrepancyList", e);
        }

        return resultList;
    }

    public static ArrayList<SystemBean> getFacetUnknownList(Connection conn) {
        String sqlStmt = "SELECT DISTINCT ship_name, transmittal_computer_name FROM logcop_report_vw "
                + "WHERE transmittal_computer_name NOT IN (SELECT computer_name FROM configured_system_vw WHERE multi_ship_ind = 'Y') "
                + "AND ship_pk IS NULL "
                + "AND ship_name IS NOT NULL "
                + "ORDER BY ship_name, transmittal_computer_name";
        ArrayList<SystemBean> resultList = new ArrayList<SystemBean>();

        debugLog("SQL", "getFacetUnknownList", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                SystemBean resultBean = new SystemBean();
                resultBean.setShipName(rs.getString("ship_name"));
                resultBean.setComputerName(rs.getString("transmittal_computer_name"));
                resultList.add(resultBean);
            }
        } catch (Exception e) {
            debugLog("ERROR", "getFacetUnknownList", e);
        }

        return resultList;
    }

    public static ArrayList<ChartBean> getDocCntByMonthList(Connection conn, String facetName) {
        String sqlStmt = "SELECT strftime('%Y%m', upload_date) AS yyyymm, SUM(doc_cnt) AS cnt FROM logcop_report_vw WHERE computer_name = ? AND transmittal_num > 0 AND transmittal_num < 999999 GROUP BY strftime('%Y%m', upload_date)";
        ArrayList<ChartBean> resultList = new ArrayList<ChartBean>();
        HashMap<String, String> cntMap = new HashMap<String, String>();

        debugLog("SQL", "getDocCntByMonthList", sqlStmt + " (" + facetName + ")");

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setString(1, facetName);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                cntMap.put(rs.getString("yyyymm"), String.valueOf(rs.getInt("cnt")));
            }

            int startYear = CommonMethods.cInt(CommonMethods.getDate("YYYY", -(30*12))); //12 months back
            int startMonth = CommonMethods.cInt(CommonMethods.getDate("MM", -(30*12))); //12 months back

            for (int year = startYear; year <= CommonMethods.cInt(CommonMethods.getDate("YYYY")); year++) {
                for (int month = (year == startYear ? startMonth : 1); month <= (year == CommonMethods.cInt(CommonMethods.getDate("YYYY")) ? CommonMethods.cInt(CommonMethods.getDate("MM")) : 12); month++) {
                    ChartBean resultBean = new ChartBean();
                    resultBean.setLabel(CommonMethods.getMonthNameShort(month-1) + " " + year);
                    resultBean.setValue(CommonMethods.nvl(cntMap.get(year + CommonMethods.padString(month, "0", 2)), "0"));
                    resultList.add(resultBean);
                }
            }
        } catch (Exception e) {
            debugLog("ERROR", "getDocCntByMonthList", e);
        }

        return resultList;
    }

    public static ArrayList<ReportBean> getTranmittalDetailList(Connection conn, String facetName) {
        String sqlStmt = "SELECT transmittal_order, transmittal_num, doc_type, doc_cnt, upload_date_fmt, upload_user "
            + "FROM logcop_report_vw WHERE computer_name = ? "
            + "ORDER BY transmittal_order, upload_date, doc_type_order";
        ArrayList<ReportBean> resultList = new ArrayList<ReportBean>();
        HashMap<String, String> exceptionMap = getExceptionMap(conn);
        int prevTransmittalNum = 0;
        String prevUpload = new String();
        NumberFormat nf = new DecimalFormat("###,###,###,##0");

        debugLog("SQL", "getMissingTransmittalList", sqlStmt + " (" + facetName + ")");

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setString(1, facetName);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                if (prevTransmittalNum == rs.getInt("transmittal_order") && prevUpload.equals(rs.getString("upload_user") + rs.getString("upload_date_fmt"))) {
                    //if its the same, append doc type and cnt
                    ReportBean resultBean = (ReportBean)resultList.remove(0);
                    if (!CommonMethods.isEmpty(rs.getString("doc_type"))) resultBean.setDocType(resultBean.getDocType() + ", " + rs.getString("doc_type"));
                    resultBean.setDocCnt(nf.format(CommonMethods.cInt(resultBean.getDocCnt().replaceAll(",","")) + rs.getInt("doc_cnt")));
                    resultList.add(0, resultBean);
                } else {
                    if (rs.getInt("transmittal_order") >= 2) {
                        for (int i = (prevTransmittalNum < 0 ? 0 : prevTransmittalNum) + 1; i < rs.getInt("transmittal_order"); i++) {
                            if (!CommonMethods.isEmpty(exceptionMap.get(facetName + "_" + i))) { //Exception List
                                ReportBean resultBean = new ReportBean();
                                resultBean.setTransmittalNum(CommonMethods.padString(i, "0", 6));
                                resultBean.setExceptionReason(exceptionMap.get(facetName + "_" + i));
                                resultList.add(0, resultBean);
                            } else { //Missing Transmittal
                                ReportBean resultBean = new ReportBean();
                                resultBean.setTransmittalNum(CommonMethods.padString(i, "0", 6));
                                resultList.add(0, resultBean);
                            }
                        }
                    }

                    ReportBean resultBean = new ReportBean();
                    resultBean.setTransmittalNum(CommonMethods.padString(rs.getInt("transmittal_num"), "0", 6));
                    resultBean.setDocType(rs.getString("doc_type"));
                    resultBean.setDocCnt(nf.format(rs.getInt("doc_cnt")));
                    resultBean.setUploadDate(rs.getString("upload_date_fmt"));
                    resultBean.setUploadUser(rs.getString("upload_user"));
                    resultList.add(0, resultBean);

                    prevTransmittalNum = rs.getInt("transmittal_order");
                    prevUpload = rs.getString("upload_user") + rs.getString("upload_date_fmt"); //to try and weed out day forward 00001 from backfiles
                }
            }
        } catch (Exception e) {
            debugLog("ERROR", "getMissingTransmittalList", e);
        }

        return resultList;
    }
}
