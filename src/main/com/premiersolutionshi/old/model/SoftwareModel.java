package com.premiersolutionshi.old.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.bean.SoftwareBean;
import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Business logic for the application's SOFTWARE module
 */
public class SoftwareModel extends BaseModel {
    private static Logger logger = Logger.getLogger(SoftwareModel.class.getSimpleName());

    public static ArrayList<String> getMsOfficeProductNameList(Connection conn) {
        return getStrList(conn, "SELECT DISTINCT product_name FROM ms_office_license WHERE product_name IS NOT NULL ORDER BY product_name", logger);
    }

    public static ArrayList<String> getMiscProductNameList(Connection conn) {
        return getStrList(conn, "SELECT DISTINCT product_name FROM misc_license WHERE product_name IS NOT NULL ORDER BY product_name", logger);
    }

    public static ArrayList<SoftwareBean> getAvailableKofaxLicenseList(Connection conn) {
        return getKofaxLicenseList(conn, true, -1);
    }

    public static ArrayList<SoftwareBean> getAvailableKofaxLicenseList(Connection conn, String configuredSystemPk) {
        return getKofaxLicenseList(conn, true, CommonMethods.cInt(configuredSystemPk));
    }

    public static ArrayList<SoftwareBean> getKofaxLicenseList(Connection conn) {
        return getKofaxLicenseList(conn, false, -1);
    }

    public static ArrayList<SoftwareBean> getKofaxLicenseList(Connection conn, boolean availOnlyInd, int configuredSystemPk) {
        StringBuffer sqlStmt = new StringBuffer(
                "SELECT kofax_license_pk, license_key, product_code, computer_name, type, hull, received_date_fmt, license_expiry_date_fmt, internal_use_ind, notes, customer, contract_number FROM kofax_license_vw WHERE kofax_license_pk IS NOT NULL");

        // Optional WHERE Variables
        if (availOnlyInd && configuredSystemPk > -1) {
            sqlStmt.append(
                    " AND kofax_license_pk NOT IN (SELECT kofax_license_fk FROM configured_system WHERE kofax_license_fk IS NOT NULL AND configured_system_pk <> ?)");
            sqlStmt.append(" ORDER BY internal_use_ind IS NULL OR internal_use_ind='', internal_use_ind, license_key");
        }
        else if (availOnlyInd) {
            sqlStmt.append(
                    " AND kofax_license_pk NOT IN (SELECT kofax_license_fk FROM configured_system WHERE kofax_license_fk IS NOT NULL)");
            sqlStmt.append(" ORDER BY internal_use_ind IS NULL OR internal_use_ind='', internal_use_ind, license_key");
        }
        else {
            sqlStmt.append(" ORDER BY license_key");
        }

        ArrayList<SoftwareBean> resultList = new ArrayList<SoftwareBean>();

        debugLog("SQL", "getKofaxLicenseList", sqlStmt.toString(), logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            // Optional WHERE Variables
            if (availOnlyInd && configuredSystemPk > -1)
                pStmt.setInt(1, configuredSystemPk);

            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                SoftwareBean resultBean = new SoftwareBean();
                resultBean.setKofaxLicensePk(rs.getString("kofax_license_pk"));
                resultBean.setLicenseKey(rs.getString("license_key"));
                resultBean.setProductCode(rs.getString("product_code"));
                resultBean.setReceivedDate(rs.getString("received_date_fmt"));
                resultBean.setComputerName(rs.getString("computer_name"));
                resultBean.setLicenseExpiryDate(rs.getString("license_expiry_date_fmt"));
                resultBean.setInternalUseInd(rs.getString("internal_use_ind"));
                resultBean.setNotes(rs.getString("notes"));
                resultBean.setCustomer(rs.getString("customer"));
                resultBean.setContractNumber(rs.getString("contract_number"));
                resultList.add(resultBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getKofaxLicenseList", e, logger);
        }

        return resultList;
    }

    public static SoftwareBean getKofaxLicenseBean(Connection conn, SoftwareBean inputBean) {
        String sqlStmt = "SELECT kofax_license_pk, license_key, product_code, received_date_fmt, license_expiry_date_fmt, internal_use_ind, notes, customer, contract_number FROM kofax_license_vw WHERE kofax_license_pk = ?";
        SoftwareBean resultBean = new SoftwareBean();

        if (CommonMethods.cInt(inputBean.getKofaxLicensePk()) <= -1)
            return null;

        debugLog("SQL", "getKofaxLicenseBean", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getKofaxLicensePk()));

            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                resultBean.setKofaxLicensePk(rs.getString("kofax_license_pk"));
                resultBean.setLicenseKey(rs.getString("license_key"));
                resultBean.setProductCode(rs.getString("product_code"));
                resultBean.setReceivedDate(rs.getString("received_date_fmt"));
                resultBean.setLicenseExpiryDate(rs.getString("license_expiry_date_fmt"));
                resultBean.setInternalUseInd(rs.getString("internal_use_ind"));
                resultBean.setNotes(rs.getString("notes"));
                resultBean.setCustomer(rs.getString("customer"));
                resultBean.setCurrCustomer(rs.getString("customer")); // for
                                                                      // edit
                                                                      // pages
                resultBean.setContractNumber(rs.getString("contract_number"));
                resultBean.setCurrContractNumber(rs.getString("contract_number")); // for
                                                                                   // edit
                                                                                   // pages
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getKofaxLicenseBean", e, logger);
        }

        return resultBean;
    }

    public static boolean insertKofaxLicense(Connection conn, SoftwareBean inputBean, LoginBean loginBean) {
        String sqlStmt = "INSERT INTO kofax_license (license_key, product_code, received_date, license_expiry_date, internal_use_ind, notes, customer, contract_number, last_updated_by, last_updated_date) VALUES (?,?,?,?,?,?,?,?,?,?)";
        boolean ranOk = false;

        debugLog("SQL", "insertKofaxLicense", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setString(1, inputBean.getLicenseKey());
            pStmt.setString(2, inputBean.getProductCode());
            CommonMethods.setDate(pStmt, 3, inputBean.getReceivedDate());
            CommonMethods.setDate(pStmt, 4, inputBean.getLicenseExpiryDate());
            CommonMethods.setString(pStmt, 5, inputBean.getInternalUseInd());
            CommonMethods.setString(pStmt, 6, inputBean.getNotes());
            CommonMethods.setString(pStmt, 7, inputBean.getCustomer());
            CommonMethods.setString(pStmt, 8, inputBean.getContractNumber());
            pStmt.setString(9, loginBean.getFullName());
            pStmt.setString(10, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            ranOk = (pStmt.executeUpdate() == 1);
        }
        catch (Exception e) {
            debugLog("ERROR", "insertKofaxLicense", e, logger);
        }
        finally {
            try {
                if (ranOk)
                    conn.commit();
                else
                    conn.rollback();
            }
            catch (Exception e) {
            }
            try {
                conn.setAutoCommit(true);
            }
            catch (Exception e) {
            }
        }

        return ranOk;
    }

    public static boolean updateKofaxLicense(Connection conn, SoftwareBean inputBean, LoginBean loginBean) {
        String sqlStmt = "UPDATE kofax_license SET license_key = ?, product_code = ?, received_date = ?, license_expiry_date = ?, internal_use_ind = ?, notes = ?, customer = ?, contract_number = ?, last_updated_by = ?, last_updated_date = ? WHERE kofax_license_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getKofaxLicensePk()) <= -1)
            return false;

        debugLog("SQL", "updateKofaxLicense", sqlStmt + " (kofax_license_pk = " + inputBean.getKofaxLicensePk() + ")", logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setString(1, inputBean.getLicenseKey());
            pStmt.setString(2, inputBean.getProductCode());
            CommonMethods.setDate(pStmt, 3, inputBean.getReceivedDate());
            CommonMethods.setDate(pStmt, 4, inputBean.getLicenseExpiryDate());
            CommonMethods.setString(pStmt, 5, inputBean.getInternalUseInd());
            CommonMethods.setString(pStmt, 6, inputBean.getNotes());
            CommonMethods.setString(pStmt, 7, inputBean.getCustomer());
            CommonMethods.setString(pStmt, 8, inputBean.getContractNumber());
            pStmt.setString(9, loginBean.getFullName());
            pStmt.setString(10, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            pStmt.setInt(11, CommonMethods.cInt(inputBean.getKofaxLicensePk()));
            ranOk = (pStmt.executeUpdate() == 1);
        }
        catch (Exception e) {
            debugLog("ERROR", "updateKofaxLicense", e, logger);
        }
        finally {
            try {
                if (ranOk)
                    conn.commit();
                else
                    conn.rollback();
            }
            catch (Exception e) {
            }
            try {
                conn.setAutoCommit(true);
            }
            catch (Exception e) {
            }
        }

        return ranOk;
    }

    public static boolean deleteKofaxLicense(Connection conn, SoftwareBean inputBean) {
        String sqlStmt = "DELETE FROM kofax_license WHERE kofax_license_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getKofaxLicensePk()) <= -1)
            return false;

        debugLog("SQL", "deleteKofaxLicense", sqlStmt + " (kofax_license = " + inputBean.getKofaxLicensePk() + ")", logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getKofaxLicensePk()));
            ranOk = (pStmt.executeUpdate() == 1);
        }
        catch (Exception e) {
            debugLog("ERROR", "deleteKofaxLicense", e, logger);
        }
        finally {
            try {
                if (ranOk)
                    conn.commit();
                else
                    conn.rollback();
            }
            catch (Exception e) {
            }
            try {
                conn.setAutoCommit(true);
            }
            catch (Exception e) {
            }
        }

        return ranOk;
    }

    public static boolean bulkUpdateKofaxLicense(Connection conn, SoftwareBean inputBean, LoginBean loginBean) {
        String sqlStmt = "UPDATE kofax_license SET license_expiry_date = ?, last_updated_by = ?, last_updated_date = ? WHERE kofax_license_pk = ?";
        boolean ranOk = true;

        if (CommonMethods.isEmpty(inputBean.getLicenseExpiryDate()))
            return false;
        if (inputBean.getKofaxLicensePkArr() == null || inputBean.getKofaxLicensePkArr().length <= 0)
            return true; // Nothing to delete

        debugLog("SQL", "deleteIssueFiles", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            CommonMethods.setDate(pStmt, 1, inputBean.getLicenseExpiryDate());
            pStmt.setString(2, loginBean.getFullName());
            pStmt.setString(3, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));

            for (String kofaxLicensePk : inputBean.getKofaxLicensePkArr()) {
                pStmt.setInt(4, CommonMethods.cInt(kofaxLicensePk));
                pStmt.addBatch();
            }

            for (int rsCnt : pStmt.executeBatch()) {
                ranOk &= rsCnt == 1;
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "bulkUpdateKofaxLicense", e, logger);
            ranOk = false;
        }

        return ranOk;
    }

    public static ArrayList<SoftwareBean> getAvailableVrsLicenseList(Connection conn) {
        return getVrsLicenseList(conn, true, -1);
    }

    public static ArrayList<SoftwareBean> getAvailableVrsLicenseList(Connection conn, String configuredSystemPk) {
        return getVrsLicenseList(conn, true, CommonMethods.cInt(configuredSystemPk));
    }

    public static ArrayList<SoftwareBean> getVrsLicenseList(Connection conn) {
        return getVrsLicenseList(conn, false, -1);
    }

    public static ArrayList<SoftwareBean> getVrsLicenseList(Connection conn, boolean availOnlyInd, int configuredSystemPk) {
        StringBuffer sqlStmt = new StringBuffer(
                "SELECT vrs_license_pk, license_key, product_code, computer_name, type, hull, strftime('%m/%d/%Y', received_date) AS received_date_fmt, "
                + "notes, customer, contract_number "
                + "FROM vrs_license_vw WHERE vrs_license_pk IS NOT NULL");

        // Optional WHERE Variables
        if (availOnlyInd && configuredSystemPk > -1) {
            sqlStmt.append(
                    " AND vrs_license_pk NOT IN (SELECT vrs_license_fk FROM configured_system WHERE vrs_license_fk IS NOT NULL AND configured_system_pk <> ?)");
        }
        else if (availOnlyInd) {
            sqlStmt.append(" AND vrs_license_pk NOT IN (SELECT vrs_license_fk FROM configured_system WHERE vrs_license_fk IS NOT NULL)");
        }

        // ORDER BY
        sqlStmt.append(" ORDER BY license_key ASC");

        ArrayList<SoftwareBean> resultList = new ArrayList<SoftwareBean>();

        debugLog("SQL", "getVrsLicenseList", sqlStmt.toString(), logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            // Optional WHERE Variables
            if (availOnlyInd && configuredSystemPk > -1)
                pStmt.setInt(1, configuredSystemPk);

            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                SoftwareBean resultBean = new SoftwareBean();
                resultBean.setVrsLicensePk(rs.getString("vrs_license_pk"));
                resultBean.setLicenseKey(rs.getString("license_key"));
                resultBean.setProductCode(rs.getString("product_code"));
                resultBean.setReceivedDate(rs.getString("received_date_fmt"));
                resultBean.setComputerName(rs.getString("computer_name"));
                resultBean.setNotes(rs.getString("notes"));
                resultBean.setCustomer(rs.getString("customer"));
                resultBean.setContractNumber(rs.getString("contract_number"));
                resultList.add(resultBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getVrsLicenseList", e, logger);
        }

        return resultList;
    }

    public static SoftwareBean getVrsLicenseBean(Connection conn, SoftwareBean inputBean) {
        String sqlStmt = "SELECT vrs_license_pk, license_key, product_code, strftime('%m/%d/%Y', received_date) AS received_date_fmt, notes, customer, contract_number FROM vrs_license WHERE vrs_license_pk = ?";
        SoftwareBean resultBean = new SoftwareBean();

        if (CommonMethods.cInt(inputBean.getVrsLicensePk()) <= -1)
            return null;

        debugLog("SQL", "getVrsLicenseBean", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getVrsLicensePk()));

            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                resultBean.setVrsLicensePk(rs.getString("vrs_license_pk"));
                resultBean.setLicenseKey(rs.getString("license_key"));
                resultBean.setProductCode(rs.getString("product_code"));
                resultBean.setReceivedDate(rs.getString("received_date_fmt"));
                resultBean.setNotes(rs.getString("notes"));
                resultBean.setCustomer(rs.getString("customer"));
                resultBean.setCurrCustomer(rs.getString("customer")); // for
                                                                      // edit
                                                                      // pages
                resultBean.setContractNumber(rs.getString("contract_number"));
                resultBean.setCurrContractNumber(rs.getString("contract_number")); // for
                                                                                   // edit
                                                                                   // pages
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getVrsLicenseBean", e, logger);
        }

        return resultBean;
    }

    public static boolean insertVrsLicense(Connection conn, SoftwareBean inputBean, LoginBean loginBean) {
        String sqlStmt = "INSERT INTO vrs_license (license_key, product_code, received_date, notes, customer, contract_number, last_updated_by, last_updated_date) VALUES (?,?,?,?,?,?,?,?)";
        boolean ranOk = false;

        debugLog("SQL", "insertVrsLicense", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setString(1, inputBean.getLicenseKey());
            pStmt.setString(2, inputBean.getProductCode());
            CommonMethods.setDate(pStmt, 3, inputBean.getReceivedDate());
            CommonMethods.setString(pStmt, 4, inputBean.getNotes());
            CommonMethods.setString(pStmt, 5, inputBean.getCustomer());
            CommonMethods.setString(pStmt, 6, inputBean.getContractNumber());
            pStmt.setString(7, loginBean.getFullName());
            pStmt.setString(8, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            ranOk = (pStmt.executeUpdate() == 1);
        }
        catch (Exception e) {
            debugLog("ERROR", "insertVrsLicense", e, logger);
        }
        finally {
            try {
                if (ranOk)
                    conn.commit();
                else
                    conn.rollback();
            }
            catch (Exception e) {
            }
            try {
                conn.setAutoCommit(true);
            }
            catch (Exception e) {
            }
        }

        return ranOk;
    }

    public static boolean updateVrsLicense(Connection conn, SoftwareBean inputBean, LoginBean loginBean) {
        String sqlStmt = "UPDATE vrs_license SET license_key = ?, product_code = ?, received_date = ?, notes = ?, customer = ?, contract_number = ?, last_updated_by = ?, last_updated_date = ? WHERE vrs_license_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getVrsLicensePk()) <= -1)
            return false;

        debugLog("SQL", "updateVrsLicense", sqlStmt + " (vrs_license_pk = " + inputBean.getVrsLicensePk() + ")", logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setString(1, inputBean.getLicenseKey());
            pStmt.setString(2, inputBean.getProductCode());
            CommonMethods.setDate(pStmt, 3, inputBean.getReceivedDate());
            CommonMethods.setString(pStmt, 4, inputBean.getNotes());
            CommonMethods.setString(pStmt, 5, inputBean.getCustomer());
            CommonMethods.setString(pStmt, 6, inputBean.getContractNumber());
            pStmt.setString(7, loginBean.getFullName());
            pStmt.setString(8, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            pStmt.setInt(9, CommonMethods.cInt(inputBean.getVrsLicensePk()));
            ranOk = (pStmt.executeUpdate() == 1);
        }
        catch (Exception e) {
            debugLog("ERROR", "updateVrsLicense", e, logger);
        }
        finally {
            try {
                if (ranOk)
                    conn.commit();
                else
                    conn.rollback();
            }
            catch (Exception e) {
            }
            try {
                conn.setAutoCommit(true);
            }
            catch (Exception e) {
            }
        }

        return ranOk;
    }

    public static boolean deleteVrsLicense(Connection conn, SoftwareBean inputBean) {
        String sqlStmt = "DELETE FROM vrs_license WHERE vrs_license_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getVrsLicensePk()) <= -1)
            return false;

        debugLog("SQL", "deleteVrsLicense", sqlStmt + " (vrs_license = " + inputBean.getVrsLicensePk() + ")", logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getVrsLicensePk()));
            ranOk = (pStmt.executeUpdate() == 1);
        }
        catch (Exception e) {
            debugLog("ERROR", "deleteVrsLicense", e, logger);
        }
        finally {
            try {
                if (ranOk)
                    conn.commit();
                else
                    conn.rollback();
            }
            catch (Exception e) {
            }
            try {
                conn.setAutoCommit(true);
            }
            catch (Exception e) {
            }
        }

        return ranOk;
    }

    public static ArrayList<SoftwareBean> getMsOfficeLicenseList(Connection conn) {
        String sqlStmt = "SELECT ms_office_license_pk, product_name, license_key, installed_cnt, received_date_fmt, notes, customer, contract_number FROM ms_office_license_vw ORDER BY license_key ASC";
        ArrayList<SoftwareBean> resultList = new ArrayList<SoftwareBean>();

        debugLog("SQL", "getMsOfficeLicenseList", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                SoftwareBean resultBean = new SoftwareBean();
                resultBean.setMsOfficeLicensePk(rs.getString("ms_office_license_pk"));
                resultBean.setProductName(rs.getString("product_name"));
                resultBean.setLicenseKey(rs.getString("license_key"));
                resultBean.setInstalledCnt(rs.getString("installed_cnt"));
                resultBean.setReceivedDate(rs.getString("received_date_fmt"));
                resultBean.setNotes(rs.getString("notes"));
                resultBean.setCustomer(rs.getString("customer"));
                resultBean.setContractNumber(rs.getString("contract_number"));
                resultList.add(resultBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getMsOfficeLicenseList", e, logger);
        }

        return resultList;
    }

    public static SoftwareBean getMsOfficeLicenseBean(Connection conn, SoftwareBean inputBean) {
        String sqlStmt = "SELECT ms_office_license_pk, product_name, license_key, strftime('%m/%d/%Y', received_date) AS received_date_fmt, notes, customer, contract_number FROM ms_office_license WHERE ms_office_license_pk = ?";
        SoftwareBean resultBean = new SoftwareBean();

        if (CommonMethods.cInt(inputBean.getMsOfficeLicensePk()) <= -1)
            return null;

        debugLog("SQL", "getMsOfficeLicenseBean", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getMsOfficeLicensePk()));

            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                resultBean.setMsOfficeLicensePk(rs.getString("ms_office_license_pk"));
                resultBean.setProductName(rs.getString("product_name"));
                resultBean.setCurrProductName(rs.getString("product_name")); // For
                                                                             // updating
                                                                             // purposes
                resultBean.setLicenseKey(rs.getString("license_key"));
                resultBean.setReceivedDate(rs.getString("received_date_fmt"));
                resultBean.setNotes(rs.getString("notes"));
                resultBean.setCustomer(rs.getString("customer"));
                resultBean.setCurrCustomer(rs.getString("customer")); // for
                                                                      // edit
                                                                      // pages
                resultBean.setContractNumber(rs.getString("contract_number"));
                resultBean.setCurrContractNumber(rs.getString("contract_number")); // for
                                                                                   // edit
                                                                                   // pages
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getMsOfficeLicenseBean", e, logger);
        }

        return resultBean;
    }

    public static boolean insertMsOfficeLicense(Connection conn, SoftwareBean inputBean, LoginBean loginBean) {
        String sqlStmt = "INSERT INTO ms_office_license (product_name, license_key, received_date, notes, customer, contract_number, last_updated_by, last_updated_date) VALUES (?,?,?,?,?,?,?,?)";
        boolean ranOk = false;

        debugLog("SQL", "insertMsOfficeLicense", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setString(1, inputBean.getProductName());
            pStmt.setString(2, inputBean.getLicenseKey());
            CommonMethods.setDate(pStmt, 3, inputBean.getReceivedDate());
            CommonMethods.setString(pStmt, 4, inputBean.getNotes());
            CommonMethods.setString(pStmt, 5, inputBean.getCustomer());
            CommonMethods.setString(pStmt, 6, inputBean.getContractNumber());
            pStmt.setString(7, loginBean.getFullName());
            pStmt.setString(8, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            ranOk = (pStmt.executeUpdate() == 1);
        }
        catch (Exception e) {
            debugLog("ERROR", "insertMsOfficeLicense", e, logger);
        }
        finally {
            try {
                if (ranOk)
                    conn.commit();
                else
                    conn.rollback();
            }
            catch (Exception e) {
            }
            try {
                conn.setAutoCommit(true);
            }
            catch (Exception e) {
            }
        }

        return ranOk;
    }

    public static boolean updateMsOfficeLicense(Connection conn, SoftwareBean inputBean, LoginBean loginBean) {
        String sqlStmt = "UPDATE ms_office_license SET product_name = ?, license_key = ?, received_date = ?, notes = ?, customer = ?, contract_number = ?, last_updated_by = ?, last_updated_date = ? WHERE ms_office_license_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getMsOfficeLicensePk()) <= -1)
            return false;

        debugLog("SQL", "updateMsOfficeLicense", sqlStmt + " (ms_office_license_pk = " + inputBean.getMsOfficeLicensePk() + ")", logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setString(1, inputBean.getProductName());
            pStmt.setString(2, inputBean.getLicenseKey());
            CommonMethods.setDate(pStmt, 3, inputBean.getReceivedDate());
            CommonMethods.setString(pStmt, 4, inputBean.getNotes());
            CommonMethods.setString(pStmt, 5, inputBean.getCustomer());
            CommonMethods.setString(pStmt, 6, inputBean.getContractNumber());
            pStmt.setString(7, loginBean.getFullName());
            pStmt.setString(8, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            pStmt.setInt(9, CommonMethods.cInt(inputBean.getMsOfficeLicensePk()));
            ranOk = (pStmt.executeUpdate() == 1);
        }
        catch (Exception e) {
            debugLog("ERROR", "updateMsOfficeLicense", e, logger);
        }
        finally {
            try {
                if (ranOk)
                    conn.commit();
                else
                    conn.rollback();
            }
            catch (Exception e) {
            }
            try {
                conn.setAutoCommit(true);
            }
            catch (Exception e) {
            }
        }

        return ranOk;
    }

    public static boolean deleteMsOfficeLicense(Connection conn, SoftwareBean inputBean) {
        String sqlStmt = "DELETE FROM ms_office_license WHERE ms_office_license_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getMsOfficeLicensePk()) <= -1)
            return false;

        debugLog("SQL", "deleteMsOfficeLicense", sqlStmt + " (ms_office_license_pk = " + inputBean.getMsOfficeLicensePk() + ")", logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getMsOfficeLicensePk()));
            ranOk = (pStmt.executeUpdate() == 1);
        }
        catch (Exception e) {
            debugLog("ERROR", "deleteMsOfficeLicense", e, logger);
        }
        finally {
            try {
                if (ranOk)
                    conn.commit();
                else
                    conn.rollback();
            }
            catch (Exception e) {
            }
            try {
                conn.setAutoCommit(true);
            }
            catch (Exception e) {
            }
        }

        return ranOk;
    }

    public static ArrayList<SoftwareBean> getMiscLicenseList(Connection conn) {
        String sqlStmt = "SELECT misc_license_pk, product_name, product_key, installed_cnt, received_date_fmt, license_expiry_date_fmt, customer, contract_number, status, status_notes, notes FROM misc_license_vw ORDER BY product_name ASC, product_key ASC";
        ArrayList<SoftwareBean> resultList = new ArrayList<SoftwareBean>();

        debugLog("SQL", "getMiscLicenseList", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                SoftwareBean resultBean = new SoftwareBean();
                resultBean.setMiscLicensePk(rs.getString("misc_license_pk"));
                resultBean.setProductName(rs.getString("product_name"));
                resultBean.setProductKey(rs.getString("product_key"));
                resultBean.setInstalledCnt(rs.getString("installed_cnt"));
                resultBean.setReceivedDate(rs.getString("received_date_fmt"));
                resultBean.setLicenseExpiryDate(rs.getString("license_expiry_date_fmt"));
                resultBean.setStatus(rs.getString("status"));
                resultBean.setStatusNotes(rs.getString("status_notes"));
                resultBean.setCustomer(rs.getString("customer"));
                resultBean.setContractNumber(rs.getString("contract_number"));
                resultBean.setNotes(rs.getString("notes"));
                resultList.add(resultBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getMiscLicenseList", e, logger);
        }

        return resultList;
    }

    public static SoftwareBean getMiscLicenseBean(Connection conn, SoftwareBean inputBean) {
        String sqlStmt = "SELECT misc_license_pk, product_name, product_key, installed_cnt, received_date_fmt, license_expiry_date_fmt, customer, contract_number, status, status_notes, notes FROM misc_license_vw WHERE misc_license_pk = ?";
        SoftwareBean resultBean = new SoftwareBean();

        if (CommonMethods.cInt(inputBean.getMiscLicensePk()) <= -1)
            return null;

        debugLog("SQL", "getMiscLicenseBean", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getMiscLicensePk()));

            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                resultBean.setMiscLicensePk(rs.getString("misc_license_pk"));
                resultBean.setProductName(rs.getString("product_name"));
                resultBean.setCurrProductName(rs.getString("product_name")); // For
                                                                             // updating
                                                                             // purposes
                resultBean.setProductKey(rs.getString("product_key"));
                resultBean.setInstalledCnt(rs.getString("installed_cnt"));
                resultBean.setReceivedDate(rs.getString("received_date_fmt"));
                resultBean.setLicenseExpiryDate(rs.getString("license_expiry_date_fmt"));
                resultBean.setCustomer(rs.getString("customer"));
                resultBean.setCurrCustomer(rs.getString("customer")); // for
                                                                      // edit
                                                                      // pages
                resultBean.setContractNumber(rs.getString("contract_number"));
                resultBean.setCurrContractNumber(rs.getString("contract_number")); // for
                                                                                   // edit
                                                                                   // pages
                resultBean.setStatus(rs.getString("status"));
                resultBean.setStatusNotes(rs.getString("status_notes"));
                resultBean.setNotes(rs.getString("notes"));
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getMiscLicenseBean", e, logger);
        }

        return resultBean;
    }

    public static boolean insertMiscLicense(Connection conn, SoftwareBean inputBean, LoginBean loginBean) {
        String sqlStmt = "INSERT INTO misc_license (product_name, product_key, installed_cnt, received_date, license_expiry_date, status, status_notes, customer, contract_number, notes, last_updated_by, last_updated_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        boolean ranOk = false;

        debugLog("SQL", "insertMiscLicense", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setString(1, inputBean.getProductName());
            pStmt.setString(2, inputBean.getProductKey());
            pStmt.setInt(3, CommonMethods.cInt(inputBean.getInstalledCnt(), 1));
            CommonMethods.setDate(pStmt, 4, inputBean.getReceivedDate());
            CommonMethods.setDate(pStmt, 5, inputBean.getLicenseExpiryDate());
            CommonMethods.setString(pStmt, 6, inputBean.getStatus());
            CommonMethods.setString(pStmt, 7, inputBean.getStatusNotes());
            CommonMethods.setString(pStmt, 8, inputBean.getCustomer());
            CommonMethods.setString(pStmt, 9, inputBean.getContractNumber());
            CommonMethods.setString(pStmt, 10, inputBean.getNotes());
            pStmt.setString(11, loginBean.getFullName());
            pStmt.setString(12, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            ranOk = (pStmt.executeUpdate() == 1);
        }
        catch (Exception e) {
            debugLog("ERROR", "insertMiscLicense", e, logger);
        }
        finally {
            try {
                if (ranOk)
                    conn.commit();
                else
                    conn.rollback();
            }
            catch (Exception e) {
            }
            try {
                conn.setAutoCommit(true);
            }
            catch (Exception e) {
            }
        }

        return ranOk;
    }

    public static boolean updateMiscLicense(Connection conn, SoftwareBean inputBean, LoginBean loginBean) {
        String sqlStmt = "UPDATE misc_license SET product_name = ?, product_key = ?, installed_cnt = ?, received_date = ?, license_expiry_date = ?, status = ?, status_notes = ?, customer = ?, contract_number = ?, notes = ?, last_updated_by = ?, last_updated_date = ? WHERE misc_license_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getMiscLicensePk()) <= -1)
            return false;

        debugLog("SQL", "updateMiscLicense", sqlStmt + " (misc_license_pk = " + inputBean.getMiscLicensePk() + ")", logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setString(1, inputBean.getProductName());
            pStmt.setString(2, inputBean.getProductKey());
            pStmt.setInt(3, CommonMethods.cInt(inputBean.getInstalledCnt(), 1));
            CommonMethods.setDate(pStmt, 4, inputBean.getReceivedDate());
            CommonMethods.setDate(pStmt, 5, inputBean.getLicenseExpiryDate());
            CommonMethods.setString(pStmt, 6, inputBean.getStatus());
            CommonMethods.setString(pStmt, 7, inputBean.getStatusNotes());
            CommonMethods.setString(pStmt, 8, inputBean.getCustomer());
            CommonMethods.setString(pStmt, 9, inputBean.getContractNumber());
            CommonMethods.setString(pStmt, 10, inputBean.getNotes());
            pStmt.setString(11, loginBean.getFullName());
            pStmt.setString(12, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            pStmt.setInt(13, CommonMethods.cInt(inputBean.getMiscLicensePk()));
            ranOk = (pStmt.executeUpdate() == 1);
        }
        catch (Exception e) {
            debugLog("ERROR", "updateMiscLicense", e, logger);
        }
        finally {
            try {
                if (ranOk)
                    conn.commit();
                else
                    conn.rollback();
            }
            catch (Exception e) {
            }
            try {
                conn.setAutoCommit(true);
            }
            catch (Exception e) {
            }
        }
        return ranOk;
    }

    public static boolean deleteMiscLicense(Connection conn, SoftwareBean inputBean) {
        String sqlStmt = "DELETE FROM misc_license WHERE misc_license_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getMiscLicensePk()) <= -1)
            return false;

        debugLog("SQL", "deleteMiscLicense", sqlStmt + " (misc_license_pk = " + inputBean.getMiscLicensePk() + ")", logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getMiscLicensePk()));
            ranOk = (pStmt.executeUpdate() == 1);
        }
        catch (Exception e) {
            debugLog("ERROR", "deleteMiscLicense", e, logger);
        }
        finally {
            try {
                if (ranOk)
                    conn.commit();
                else
                    conn.rollback();
            }
            catch (Exception e) {
            }
            try {
                conn.setAutoCommit(true);
            }
            catch (Exception e) {
            }
        }

        return ranOk;
    }
}
