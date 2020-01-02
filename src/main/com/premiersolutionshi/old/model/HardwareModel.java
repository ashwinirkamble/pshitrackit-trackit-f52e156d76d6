package com.premiersolutionshi.old.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.premiersolutionshi.common.util.StringUtils;
import com.premiersolutionshi.old.bean.HardwareBean;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.util.CommonMethods;
/**
 * Business logic for the application's HARDWARE module
 */
public class HardwareModel {
    private static Logger logger = Logger.getLogger(HardwareModel.class.getSimpleName());

    private static void debugLog(String type, String functionName, Exception e) {
        debugLog(type, functionName, e.toString());
    }

    private static void debugLog(String type, String functionName, String statement) {
        if (type.equals("INFO") || type.equals("SQL")) {
            logger.info(String.format("%8s%-30s | %-34s | %s", "", type, functionName, statement));
        } else if (type.equals("ERROR")) {
            logger.error(String.format("%8s%-30s | %-34s | %s", "", type, functionName, statement));
        } else {
            logger.debug(String.format("%8s%-30s | %-34s | %s", "", type, functionName, statement));
        }
    }

    private static ArrayList<String> getStrList(Connection conn, String sqlStmt) {
        ArrayList<String> resultList = new ArrayList<String>();

        debugLog("SQL", "getStrList", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) resultList.add(rs.getString(1));
        } catch (Exception e) {
            debugLog("ERROR", "getStrList", e);
        }

        return resultList;
    }

    public static ArrayList<String> getLaptopProductNameList(Connection conn) {
        return getStrList(conn, "SELECT DISTINCT product_name FROM laptop WHERE product_name IS NOT NULL ORDER BY product_name");
    }

    public static ArrayList<String> getLaptopModelNumberList(Connection conn) {
        return getStrList(conn, "SELECT DISTINCT model_number FROM laptop WHERE model_number IS NOT NULL ORDER BY model_number DESC");
    }

    public static ArrayList<String> getLaptopOriginList(Connection conn) {
        return getStrList(conn, "SELECT DISTINCT origin FROM laptop WHERE origin IS NOT NULL ORDER BY origin");
    }

    public static ArrayList<String> getScannerProductNameList(Connection conn) {
        return getStrList(conn, "SELECT DISTINCT product_name FROM scanner WHERE product_name IS NOT NULL ORDER BY product_name");
    }

    public static ArrayList<String> getScannerModelNumberList(Connection conn) {
        return getStrList(conn, "SELECT DISTINCT model_number FROM scanner WHERE model_number IS NOT NULL ORDER BY model_number");
    }

    public static ArrayList<String> getScannerOriginList(Connection conn) {
        return getStrList(conn, "SELECT DISTINCT origin FROM scanner WHERE origin IS NOT NULL ORDER BY origin");
    }

    public static ArrayList<HardwareBean> getAvailableLaptopList(Connection conn) {
        return getLaptopList(conn, true, -1);
    }

    public static ArrayList<HardwareBean> getAvailableLaptopList(Connection conn, String configuredSystemPk) {
        return getLaptopList(conn, true, CommonMethods.cInt(configuredSystemPk));
    }

    public static ArrayList<String> getProductTypeList(Connection conn) {
        return getStrList(conn, "SELECT DISTINCT product_type FROM misc_hardware WHERE product_type IS NOT NULL ORDER BY product_type");
    }

    public static ArrayList<HardwareBean> getLaptopList(Connection conn) {
        return getLaptopList(conn, false, -1);
    }

    public static ArrayList<HardwareBean> getLaptopList(Connection conn, boolean availOnlyInd, int configuredSystemPk) {
        StringBuffer sqlStmt = new StringBuffer("SELECT laptop_pk, computer_name, tag, product_name, model_number, serial_number, mac_address, "
                + "origin, strftime('%m/%d/%Y', received_date) AS received_date_fmt, strftime('%m/%d/%Y', prepped_date) AS prepped_date_fmt, "
                + "notes, ship_name, type, hull, homeport, status, status_notes, customer, contract_number "
                + "FROM laptop_vw "
                + "WHERE laptop_pk IS NOT NULL");

        //Optional WHERE Variables
        if (availOnlyInd && configuredSystemPk > -1) {
            sqlStmt.append(" AND computer_name IS NOT NULL "
                    + "AND laptop_pk NOT IN (SELECT laptop_fk FROM configured_system WHERE laptop_fk IS NOT NULL AND configured_system_pk <> ?)");
        } else if (availOnlyInd) {
            sqlStmt.append(" AND computer_name IS NOT NULL "
                    + "AND laptop_pk NOT IN (SELECT laptop_fk FROM configured_system WHERE laptop_fk IS NOT NULL)");
        }

        //ORDER BY
        sqlStmt.append(" ORDER BY computer_name IS NULL OR computer_name='', computer_name ASC");

        ArrayList<HardwareBean> resultList = new ArrayList<HardwareBean>();

        debugLog("SQL", "getLaptopList", sqlStmt.toString());

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            //Optional WHERE Variables
            if (availOnlyInd && configuredSystemPk > -1) pStmt.setInt(1, configuredSystemPk);

            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                HardwareBean resultBean = new HardwareBean();
                resultBean.setLaptopPk(rs.getString("laptop_pk"));
                resultBean.setProductName(rs.getString("product_name"));
                resultBean.setComputerName(rs.getString("computer_name"));
                resultBean.setTag(rs.getString("tag"));
                resultBean.setModelNumber(rs.getString("model_number"));
                resultBean.setSerialNumber(rs.getString("serial_number"));
                resultBean.setMacAddress(rs.getString("mac_address"));
                resultBean.setOrigin(rs.getString("origin"));
                resultBean.setReceivedDate(rs.getString("received_date_fmt"));
                resultBean.setPreppedDate(rs.getString("prepped_date_fmt"));
                resultBean.setNotes(rs.getString("notes"));
                resultBean.setShipName(CommonMethods.nes(rs.getString("ship_name")) + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG") ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")" : ""));
                resultBean.setHomeport(rs.getString("homeport"));
                resultBean.setStatus(rs.getString("status"));
                resultBean.setStatusNotes(rs.getString("status_notes"));
                resultBean.setCustomer(rs.getString("customer"));
                resultBean.setContractNumber(rs.getString("contract_number"));
                resultList.add(resultBean);
            }
        } catch (Exception e) {
            debugLog("ERROR", "getLaptopList", e);
        }

        return resultList;
    }

    public static HardwareBean getLaptopBean(Connection conn, HardwareBean inputBean) {
        String sqlStmt = "SELECT laptop_pk, product_name, computer_name, tag, model_number, serial_number, mac_address, origin, strftime('%m/%d/%Y', received_date) AS received_date_fmt, strftime('%m/%d/%Y', prepped_date) AS prepped_date_fmt, notes, status, status_notes, customer, contract_number FROM laptop WHERE laptop_pk = ?";
        HardwareBean resultBean = new HardwareBean();

        if (CommonMethods.cInt(inputBean.getLaptopPk()) <= -1) return null;

        debugLog("SQL", "getLaptopBean", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getLaptopPk()));

            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                resultBean.setLaptopPk(rs.getString("laptop_pk"));
                resultBean.setProductName(rs.getString("product_name"));
                resultBean.setComputerName(rs.getString("computer_name"));
                resultBean.setTag(rs.getString("tag"));
                resultBean.setCurrProductName(rs.getString("product_name")); //for edit pages
                resultBean.setModelNumber(rs.getString("model_number"));
                resultBean.setCurrModelNumber(rs.getString("model_number")); //for edit pages
                resultBean.setSerialNumber(rs.getString("serial_number"));
                resultBean.setMacAddress(rs.getString("mac_address"));
                resultBean.setOrigin(rs.getString("origin"));
                resultBean.setCurrOrigin(rs.getString("origin")); //for edit pages
                resultBean.setReceivedDate(rs.getString("received_date_fmt"));
                resultBean.setPreppedDate(rs.getString("prepped_date_fmt"));
                resultBean.setNotes(rs.getString("notes"));
                resultBean.setStatus(rs.getString("status"));
                resultBean.setStatusNotes(rs.getString("status_notes"));
                resultBean.setCustomer(rs.getString("customer"));
                resultBean.setCurrCustomer(rs.getString("customer")); //for edit pages
                resultBean.setContractNumber(rs.getString("contract_number"));
                resultBean.setCurrContractNumber(rs.getString("contract_number")); //for edit pages
            }
        } catch (Exception e) {
            debugLog("ERROR", "getLaptopBean", e);
        }

        return resultBean;
    }

    public static String getNewLaptopTagNum(Connection conn) {
        String sqlStmt = "SELECT SUBSTR(MAX(tag), 2) FROM laptop WHERE tag LIKE 'L%' AND LENGTH(tag) = 4";
        String returnStr = null;

        debugLog("SQL", "getNewLaptopTagNum", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            returnStr = String.valueOf(rs.getInt(1) + 1);
        } catch (Exception e) {
            debugLog("ERROR", "getNewLaptopTagNum", e);
        }

        return CommonMethods.padString(CommonMethods.nvl(returnStr, "1"), "0", 3);
    }

    private static boolean scannerHasDupSerial(Connection conn, HardwareBean scannerBean) {
        String serialNumber = scannerBean.getSerialNumber();
        String scannerPk = scannerBean.getScannerPk();
        //if there isn't a computer name or S/N, it it will be ignored.
        boolean hasSerialNumber = !StringUtils.isEmpty(serialNumber);
        ArrayList<HardwareBean> scannerList = getScannerList(conn);
        for (HardwareBean hardwareBean : scannerList) {
            if (!StringUtils.safeEquals(scannerPk, hardwareBean.getScannerPk())) {
                if (hasSerialNumber && StringUtils.safeEquals(hardwareBean.getSerialNumber(), serialNumber)) {
                    debugLog("ERROR", "scannerHasDupSerial", "Cannot insert duplicate Serial Numbers.");
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean laptopHasDupSerialOrComputerName(Connection conn, HardwareBean laptopBean) {
        String computerName = laptopBean.getComputerName();
        String serialNumber = laptopBean.getSerialNumber();
        String laptopPk = laptopBean.getLaptopPk();
        //if there isn't a computer name or S/N, it it will be ignored.
        boolean hasComputerName = !StringUtils.isEmpty(computerName);
        boolean hasSerialNumber = !StringUtils.isEmpty(serialNumber);
        ArrayList<HardwareBean> laptopList = getLaptopList(conn);
        for (HardwareBean hardwareBean : laptopList) {
            if (!StringUtils.safeEquals(laptopPk, hardwareBean.getLaptopPk())) {
                if (hasComputerName && StringUtils.safeEquals(hardwareBean.getComputerName(), computerName)) {
                    debugLog("ERROR", "laptopHasDupSerialOrComputerName", "Cannot insert duplicate Computer Names.");
                    return true;
                }
                if (hasSerialNumber && StringUtils.safeEquals(hardwareBean.getSerialNumber(), serialNumber)) {
                    debugLog("ERROR", "laptopHasDupSerialOrComputerName", "Cannot insert duplicate Serial Numbers.");
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean insertLaptop(Connection conn, HardwareBean inputBean, LoginBean loginBean) {
        String sqlStmt = "INSERT INTO laptop (product_name, computer_name, tag, model_number, serial_number, mac_address, origin, received_date, prepped_date, notes, status, status_notes, customer, contract_number, last_updated_by, last_updated_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        boolean ranOk = false;
        if (laptopHasDupSerialOrComputerName(conn, inputBean)) {
            return false;
        }

        debugLog("SQL", "insertLaptop", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            CommonMethods.setString(pStmt, 1, inputBean.getProductName());
            CommonMethods.setString(pStmt, 2, inputBean.getComputerName());
            CommonMethods.setString(pStmt, 3, inputBean.getTag());
            CommonMethods.setString(pStmt, 4, inputBean.getModelNumber());
            CommonMethods.setString(pStmt, 5, inputBean.getSerialNumber());
            CommonMethods.setString(pStmt, 6, inputBean.getMacAddress());
            CommonMethods.setString(pStmt, 7, inputBean.getOrigin());
            CommonMethods.setDate(pStmt, 8, inputBean.getReceivedDate());
            CommonMethods.setDate(pStmt, 9, inputBean.getPreppedDate());
            CommonMethods.setString(pStmt, 10, inputBean.getNotes());
            CommonMethods.setString(pStmt, 11, inputBean.getStatus());
            CommonMethods.setString(pStmt, 12, inputBean.getStatusNotes());
            CommonMethods.setString(pStmt, 13, inputBean.getCustomer());
            CommonMethods.setString(pStmt, 14, inputBean.getContractNumber());
            CommonMethods.setString(pStmt, 15, loginBean.getFullName());
            pStmt.setString(16, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            ranOk = (pStmt.executeUpdate() == 1);
        } catch (Exception e) {
            debugLog("ERROR", "insertLaptop", e);
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }
        return ranOk;
    }

    public static boolean updateLaptop(Connection conn, HardwareBean inputBean, LoginBean loginBean) {
        String sqlStmt = "UPDATE laptop SET product_name = ?, computer_name = ?, tag = ?, model_number = ?, serial_number = ?, mac_address = ?, origin = ?, received_date = ?, prepped_date = ?, notes = ?, status = ?, status_notes = ?, customer = ?, contract_number = ?, last_updated_by = ?, last_updated_date = ? WHERE laptop_pk = ?";
        boolean ranOk = false;
        if (laptopHasDupSerialOrComputerName(conn, inputBean)) {
            return false;
        }
        debugLog("SQL", "updateLaptop", sqlStmt + " (laptop_pk = " + inputBean.getLaptopPk() + ")");

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            CommonMethods.setString(pStmt, 1, inputBean.getProductName());
            CommonMethods.setString(pStmt, 2, inputBean.getComputerName());
            CommonMethods.setString(pStmt, 3, inputBean.getTag());
            CommonMethods.setString(pStmt, 4, inputBean.getModelNumber());
            CommonMethods.setString(pStmt, 5, inputBean.getSerialNumber());
            CommonMethods.setString(pStmt, 6, inputBean.getMacAddress());
            CommonMethods.setString(pStmt, 7, inputBean.getOrigin());
            CommonMethods.setDate(pStmt, 8, inputBean.getReceivedDate());
            CommonMethods.setDate(pStmt, 9, inputBean.getPreppedDate());
            CommonMethods.setString(pStmt, 10, inputBean.getNotes());
            CommonMethods.setString(pStmt, 11, inputBean.getStatus());
            CommonMethods.setString(pStmt, 12, inputBean.getStatusNotes());
            CommonMethods.setString(pStmt, 13, inputBean.getCustomer());
            CommonMethods.setString(pStmt, 14, inputBean.getContractNumber());
            CommonMethods.setString(pStmt, 15, loginBean.getFullName());
            pStmt.setString(16, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            pStmt.setInt(17, CommonMethods.cInt(inputBean.getLaptopPk()));
            ranOk = (pStmt.executeUpdate() == 1);
        } catch (Exception e) {
            debugLog("ERROR", "updateLaptop", e);
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }

        return ranOk;
    }

    public static boolean deleteLaptop(Connection conn, HardwareBean inputBean) {
        String sqlStmt = "DELETE FROM laptop WHERE laptop_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getLaptopPk()) <= -1) return false;

        debugLog("SQL", "deleteLaptop", sqlStmt + " (laptop_pk = " + inputBean.getLaptopPk() + ")");

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getLaptopPk()));
            ranOk = (pStmt.executeUpdate() == 1);
        } catch (Exception e) {
            debugLog("ERROR", "deleteLaptop", e);
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }

        return ranOk;
    }

    public static ArrayList<HardwareBean> getAvailableScannerList(Connection conn) {
        return getScannerList(conn, true, -1);
    }

    public static ArrayList<HardwareBean> getAvailableScannerList(Connection conn, String configuredSystemPk) {
        return getScannerList(conn, true, CommonMethods.cInt(configuredSystemPk));
    }

    public static ArrayList<HardwareBean> getScannerList(Connection conn) {
        return getScannerList(conn, false, -1);
    }

    public static ArrayList<HardwareBean> getScannerList(Connection conn, boolean availOnlyInd, int configuredSystemPk) {
        StringBuffer sqlStmt = new StringBuffer("SELECT scanner_pk, product_name, tag, model_number, serial_number, origin, strftime('%m/%d/%Y', received_date) AS received_date_fmt, strftime('%m/%d/%Y', prepped_date) AS prepped_date_fmt, notes, status, status_notes, customer, contract_number, computer_name, ship_name, type, hull, homeport FROM scanner_vw WHERE scanner_pk IS NOT NULL");

        //Optional WHERE Variables
        if (availOnlyInd && configuredSystemPk > -1) {
            sqlStmt.append(" AND tag IS NOT NULL AND scanner_pk NOT IN (SELECT scanner_fk FROM configured_system WHERE scanner_fk IS NOT NULL AND configured_system_pk <> ?)");
        } else if (availOnlyInd) {
            sqlStmt.append(" AND tag IS NOT NULL AND scanner_pk NOT IN (SELECT scanner_fk FROM configured_system WHERE scanner_fk IS NOT NULL)");
        }

        //ORDER BY
        sqlStmt.append(" ORDER BY tag IS NULL OR tag='', tag ASC");

        ArrayList<HardwareBean> resultList = new ArrayList<HardwareBean>();

        debugLog("SQL", "getScannerList", sqlStmt.toString());

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            //Optional WHERE Variables
            if (availOnlyInd && configuredSystemPk > -1) pStmt.setInt(1, configuredSystemPk);

            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                HardwareBean resultBean = new HardwareBean();
                resultBean.setScannerPk(rs.getString("scanner_pk"));
                resultBean.setProductName(rs.getString("product_name"));
                resultBean.setTag(rs.getString("tag"));
                resultBean.setModelNumber(rs.getString("model_number"));
                resultBean.setSerialNumber(rs.getString("serial_number"));
                resultBean.setOrigin(rs.getString("origin"));
                resultBean.setReceivedDate(rs.getString("received_date_fmt"));
                resultBean.setPreppedDate(rs.getString("prepped_date_fmt"));
                resultBean.setNotes(rs.getString("notes"));
                resultBean.setStatus(rs.getString("status"));
                resultBean.setStatusNotes(rs.getString("status_notes"));
                resultBean.setShipName(CommonMethods.nes(rs.getString("ship_name")) + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG") ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")" : ""));
                resultBean.setHomeport(rs.getString("homeport"));
                resultBean.setComputerName(rs.getString("computer_name"));
                resultBean.setCustomer(rs.getString("customer"));
                resultBean.setContractNumber(rs.getString("contract_number"));
                resultList.add(resultBean);
            }
        } catch (Exception e) {
            debugLog("ERROR", "getScannerList", e);
        }

        return resultList;
    }

    public static HardwareBean getScannerBean(Connection conn, HardwareBean inputBean) {
        String sqlStmt = "SELECT scanner_pk, product_name, tag, model_number, serial_number, origin, strftime('%m/%d/%Y', received_date) AS received_date_fmt, strftime('%m/%d/%Y', prepped_date) AS prepped_date_fmt, notes, status, status_notes, customer, contract_number FROM scanner WHERE scanner_pk = ?";
        HardwareBean resultBean = new HardwareBean();

        if (CommonMethods.cInt(inputBean.getScannerPk()) <= -1) return null;

        debugLog("SQL", "getScannerBean", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getScannerPk()));

            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                resultBean.setScannerPk(rs.getString("scanner_pk"));
                resultBean.setProductName(rs.getString("product_name"));
                resultBean.setCurrProductName(rs.getString("product_name")); //for edit pages
                resultBean.setModelNumber(rs.getString("model_number"));
                resultBean.setCurrModelNumber(rs.getString("model_number")); //for edit pages
                resultBean.setTag(rs.getString("tag"));
                resultBean.setSerialNumber(rs.getString("serial_number"));
                resultBean.setOrigin(rs.getString("origin"));
                resultBean.setCurrOrigin(rs.getString("origin")); //for edit pages
                resultBean.setReceivedDate(rs.getString("received_date_fmt"));
                resultBean.setPreppedDate(rs.getString("prepped_date_fmt"));
                resultBean.setNotes(rs.getString("notes"));
                resultBean.setStatus(rs.getString("status"));
                resultBean.setStatusNotes(rs.getString("status_notes"));
                resultBean.setCustomer(rs.getString("customer"));
                resultBean.setCurrCustomer(rs.getString("customer")); //for edit pages
                resultBean.setContractNumber(rs.getString("contract_number"));
                resultBean.setCurrContractNumber(rs.getString("contract_number")); //for edit pages
            }
        } catch (Exception e) {
            debugLog("ERROR", "getScannerBean", e);
        }

        return resultBean;
    }

    public static String getNewScannerTagNum(Connection conn) {
        String sqlStmt = "SELECT SUBSTR(MAX(tag), 2) FROM scanner WHERE tag LIKE 'S%' AND LENGTH(tag) = 4";
        String returnStr = null;

        debugLog("SQL", "getNewScannerTagNum", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            returnStr = String.valueOf(rs.getInt(1) + 1);
        } catch (Exception e) {
            debugLog("ERROR", "getNewScannerTagNum", e);
        }

        return CommonMethods.padString(CommonMethods.nvl(returnStr, "1"), "0", 3);
    }

    public static boolean insertScanner(Connection conn, HardwareBean inputBean, LoginBean loginBean) {
        String sqlStmt = "INSERT INTO scanner (product_name, tag, model_number, serial_number, origin, received_date, prepped_date, notes, status, status_notes, customer, contract_number, last_updated_by, last_updated_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        boolean ranOk = false;
        if (scannerHasDupSerial(conn, inputBean)) {
            return false;
        }
        debugLog("SQL", "insertScanner", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setString(1, inputBean.getProductName());
            CommonMethods.setString(pStmt, 2, inputBean.getTag());
            CommonMethods.setString(pStmt, 3, inputBean.getModelNumber());
            CommonMethods.setString(pStmt, 4, inputBean.getSerialNumber());
            CommonMethods.setString(pStmt, 5, inputBean.getOrigin());
            CommonMethods.setDate(pStmt, 6, inputBean.getReceivedDate());
            CommonMethods.setDate(pStmt, 7, inputBean.getPreppedDate());
            CommonMethods.setString(pStmt, 8, inputBean.getNotes());
            CommonMethods.setString(pStmt, 9, inputBean.getStatus());
            CommonMethods.setString(pStmt, 10, inputBean.getStatusNotes());
            CommonMethods.setString(pStmt, 11, inputBean.getCustomer());
            CommonMethods.setString(pStmt, 12, inputBean.getContractNumber());
            pStmt.setString(13, loginBean.getFullName());
            pStmt.setString(14, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            ranOk = (pStmt.executeUpdate() == 1);
        } catch (Exception e) {
            debugLog("ERROR", "insertScanner", e);
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }

        return ranOk;
    }

    public static boolean updateScanner(Connection conn, HardwareBean inputBean, LoginBean loginBean) {
        String sqlStmt = "UPDATE scanner SET product_name = ?, tag = ?, model_number = ?, serial_number = ?, origin = ?, received_date = ?, prepped_date = ?, notes = ?, status = ?, status_notes = ?, customer = ?, contract_number = ?, last_updated_by = ?, last_updated_date = ? WHERE scanner_pk = ?";
        boolean ranOk = false;
      

        debugLog("SQL", "updateScanner", sqlStmt + " (scanner_pk = " + inputBean.getScannerPk() + ")");

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setString(1, inputBean.getProductName());
            CommonMethods.setString(pStmt, 2, inputBean.getTag());
            CommonMethods.setString(pStmt, 3, inputBean.getModelNumber());
            CommonMethods.setString(pStmt, 4, inputBean.getSerialNumber());
            CommonMethods.setString(pStmt, 5, inputBean.getOrigin());
            CommonMethods.setDate(pStmt, 6, inputBean.getReceivedDate());
            CommonMethods.setDate(pStmt, 7, inputBean.getPreppedDate());
            CommonMethods.setString(pStmt, 8, inputBean.getNotes());
            CommonMethods.setString(pStmt, 9, inputBean.getStatus());
            CommonMethods.setString(pStmt, 10, inputBean.getStatusNotes());
            CommonMethods.setString(pStmt, 11, inputBean.getCustomer());
            CommonMethods.setString(pStmt, 12, inputBean.getContractNumber());
            pStmt.setString(13, loginBean.getFullName());
            pStmt.setString(14, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            pStmt.setInt(15, CommonMethods.cInt(inputBean.getScannerPk()));
            ranOk = (pStmt.executeUpdate() == 1);
        } catch (Exception e) {
            debugLog("ERROR", "updateScanner", e);
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }

        return ranOk;
    }

    public static boolean deleteScanner(Connection conn, HardwareBean inputBean) {
        String sqlStmt = "DELETE FROM scanner WHERE scanner_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getScannerPk()) <= -1) return false;

        debugLog("SQL", "deleteScanner", sqlStmt + " (scanner_pk = " + inputBean.getScannerPk() + ")");

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getScannerPk()));
            ranOk = (pStmt.executeUpdate() == 1);
        } catch (Exception e) {
            debugLog("ERROR", "deleteScanner", e);
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }

        return ranOk;
    }

    public static ArrayList<HardwareBean> getMiscList(Connection conn) {
        String sqlStmt = "SELECT misc_hardware_pk, product_type, product_name, model_number, tag, serial_number, origin, notes, status, status_notes, customer, contract_number, received_date_fmt, warranty_expiry_date_fmt FROM misc_hardware_vw ORDER BY product_type, product_name";

        ArrayList<HardwareBean> resultList = new ArrayList<HardwareBean>();

        debugLog("SQL", "getMiscList", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                HardwareBean resultBean = new HardwareBean();
                resultBean.setMiscHardwarePk(rs.getString("misc_hardware_pk"));
                resultBean.setProductType(rs.getString("product_type"));
                resultBean.setProductName(rs.getString("product_name"));
                resultBean.setTag(rs.getString("tag"));
                resultBean.setModelNumber(rs.getString("model_number"));
                resultBean.setSerialNumber(rs.getString("serial_number"));
                resultBean.setOrigin(rs.getString("origin"));
                resultBean.setNotes(rs.getString("notes"));
                resultBean.setStatus(rs.getString("status"));
                resultBean.setStatusNotes(rs.getString("status_notes"));
                resultBean.setCustomer(rs.getString("customer"));
                resultBean.setContractNumber(rs.getString("contract_number"));
                resultBean.setReceivedDate(rs.getString("received_date_fmt"));
                resultBean.setWarrantyExpiryDate(rs.getString("warranty_expiry_date_fmt"));
                resultList.add(resultBean);
            }
        } catch (Exception e) {
            debugLog("ERROR", "getMiscList", e);
        }

        return resultList;
    }

    public static HardwareBean getMiscBean(Connection conn, HardwareBean inputBean) {
        String sqlStmt = "SELECT misc_hardware_pk, product_type, product_name, tag, model_number, serial_number, origin, notes, status, status_notes, customer, contract_number, received_date_fmt, warranty_expiry_date_fmt FROM misc_hardware_vw WHERE misc_hardware_pk = ?";
        HardwareBean resultBean = new HardwareBean();

        if (CommonMethods.cInt(inputBean.getMiscHardwarePk()) <= -1) return null;

        debugLog("SQL", "getMiscBean", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getMiscHardwarePk()));

            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                resultBean.setMiscHardwarePk(rs.getString("misc_hardware_pk"));
                resultBean.setProductType(rs.getString("product_type"));
                resultBean.setCurrProductType(rs.getString("product_type")); //for edit pages
                resultBean.setProductName(rs.getString("product_name"));
                resultBean.setTag(rs.getString("tag"));
                resultBean.setModelNumber(rs.getString("model_number"));
                resultBean.setSerialNumber(rs.getString("serial_number"));
                resultBean.setOrigin(rs.getString("origin"));
                resultBean.setNotes(rs.getString("notes"));
                resultBean.setStatus(rs.getString("status"));
                resultBean.setStatusNotes(rs.getString("status_notes"));
                resultBean.setCustomer(rs.getString("customer"));
                resultBean.setCurrCustomer(rs.getString("customer")); //for edit pages
                resultBean.setContractNumber(rs.getString("contract_number"));
                resultBean.setCurrContractNumber(rs.getString("contract_number")); //for edit pages
                resultBean.setReceivedDate(rs.getString("received_date_fmt"));
                resultBean.setWarrantyExpiryDate(rs.getString("warranty_expiry_date_fmt"));
            }
        } catch (Exception e) {
            debugLog("ERROR", "getMiscBean", e);
        }

        return resultBean;
    }

    public static boolean insertMisc(Connection conn, HardwareBean inputBean, LoginBean loginBean) {
        String sqlStmt = "INSERT INTO misc_hardware (product_type, product_name, tag, model_number, serial_number, origin, notes, status, status_notes, customer, contract_number, received_date, warranty_expiry_date, last_updated_by, last_updated_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        boolean ranOk = false;

        debugLog("SQL", "insertMiscHardware", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            CommonMethods.setString(pStmt, 1, inputBean.getProductType());
            CommonMethods.setString(pStmt, 2, inputBean.getProductName());
            CommonMethods.setString(pStmt, 3, inputBean.getTag());
            CommonMethods.setString(pStmt, 4, inputBean.getModelNumber());
            CommonMethods.setString(pStmt, 5, inputBean.getSerialNumber());
            CommonMethods.setString(pStmt, 6, inputBean.getOrigin());
            CommonMethods.setString(pStmt, 7, inputBean.getNotes());
            CommonMethods.setString(pStmt, 8, inputBean.getStatus());
            CommonMethods.setString(pStmt, 9, inputBean.getStatusNotes());
            CommonMethods.setString(pStmt, 10, inputBean.getCustomer());
            CommonMethods.setString(pStmt, 11, inputBean.getContractNumber());
            CommonMethods.setDate(pStmt, 12, inputBean.getReceivedDate());
            CommonMethods.setDate(pStmt, 13, inputBean.getWarrantyExpiryDate());
            CommonMethods.setString(pStmt, 14, loginBean.getFullName());
            pStmt.setString(15, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            ranOk = (pStmt.executeUpdate() == 1);
        } catch (Exception e) {
            debugLog("ERROR", "insertMisc", e);
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }

        return ranOk;
    }

    public static boolean updateMisc(Connection conn, HardwareBean inputBean, LoginBean loginBean) {
        String sqlStmt = "UPDATE misc_hardware SET product_type = ?, product_name = ?, tag = ?, model_number = ?, serial_number = ?, origin = ?, notes = ?, status = ?, status_notes = ?, customer = ?, contract_number = ?, received_date = ?, warranty_expiry_date = ?, last_updated_by = ?, last_updated_date = ? WHERE misc_hardware_pk = ?";
        boolean ranOk = false;

        debugLog("SQL", "updateMiscHardware", sqlStmt + " (misc_hardware_pk = " + inputBean.getMiscHardwarePk() + ")");

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            CommonMethods.setString(pStmt, 1, inputBean.getProductType());
            CommonMethods.setString(pStmt, 2, inputBean.getProductName());
            CommonMethods.setString(pStmt, 3, inputBean.getTag());
            CommonMethods.setString(pStmt, 4, inputBean.getModelNumber());
            CommonMethods.setString(pStmt, 5, inputBean.getSerialNumber());
            CommonMethods.setString(pStmt, 6, inputBean.getOrigin());
            CommonMethods.setString(pStmt, 7, inputBean.getNotes());
            CommonMethods.setString(pStmt, 8, inputBean.getStatus());
            CommonMethods.setString(pStmt, 9, inputBean.getStatusNotes());
            CommonMethods.setString(pStmt, 10, inputBean.getCustomer());
            CommonMethods.setString(pStmt, 11, inputBean.getContractNumber());
            CommonMethods.setDate(pStmt, 12, inputBean.getReceivedDate());
            CommonMethods.setDate(pStmt, 13, inputBean.getWarrantyExpiryDate());
            CommonMethods.setString(pStmt, 14, loginBean.getFullName());
            pStmt.setString(15, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            pStmt.setInt(16, CommonMethods.cInt(inputBean.getMiscHardwarePk()));
            ranOk = (pStmt.executeUpdate() == 1);
        } catch (Exception e) {
            debugLog("ERROR", "updateMisc", e);
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }

        return ranOk;
    }

    public static boolean deleteMisc(Connection conn, HardwareBean inputBean) {
        String sqlStmt = "DELETE FROM misc_hardware WHERE misc_hardware_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getMiscHardwarePk()) <= -1) return false;

        debugLog("SQL", "deleteMisc", sqlStmt + " (misc_hardware_pk = " + inputBean.getMiscHardwarePk() + ")");

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getMiscHardwarePk()));
            ranOk = (pStmt.executeUpdate() == 1);
        } catch (Exception e) {
            debugLog("ERROR", "deleteMiscHardware", e);
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }

        return ranOk;
    }
}
