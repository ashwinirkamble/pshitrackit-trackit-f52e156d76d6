package com.premiersolutionshi.old.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;

import com.premiersolutionshi.common.util.PrintClassPropertiesUtil;
import com.premiersolutionshi.old.bean.BackfileBean;
import com.premiersolutionshi.old.bean.DecomBean;
import com.premiersolutionshi.old.bean.FileBean;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.bean.ShipBean;
import com.premiersolutionshi.old.bean.SupportBean;
import com.premiersolutionshi.old.bean.SystemBean;
import com.premiersolutionshi.old.bean.SystemLocationBean;
import com.premiersolutionshi.old.bean.TrainingBean;
import com.premiersolutionshi.old.bean.UserBean;
import com.premiersolutionshi.old.util.CommonMethods;
import com.premiersolutionshi.old.util.ModelUtils;
/**
 * Business logic for the application's SYSTEM module
 */
public class SystemModel extends BaseModel {
    private static Logger logger = Logger.getLogger(SystemModel.class.getSimpleName());

    public static ArrayList<String> getFacetVersionList(Connection conn) {
        return getStrList(conn, "SELECT DISTINCT facet_version FROM configured_system "
                + "WHERE facet_version IS NOT NULL ORDER BY CASE WHEN facet_version LIKE '%m%' THEN 1 ELSE 2 END, facet_version DESC", logger);
    }

    public static ArrayList<String> getKofaxProductNameList(Connection conn) {
        return getStrList(conn, "SELECT DISTINCT kofax_product_name FROM configured_system "
                + "WHERE kofax_product_name IS NOT NULL ORDER BY kofax_product_name", logger);
    }

    public static ArrayList<String> getGhostVersionList(Connection conn) {
        return getStrList(conn, "SELECT DISTINCT ghost_version FROM configured_system "
                + "WHERE ghost_version IS NOT NULL ORDER BY ghost_version DESC", logger);
    }

    public static ArrayList<String> getAccessVersionList(Connection conn) {
        return getStrList(conn, "SELECT DISTINCT access_version FROM configured_system WHERE access_version IS NOT NULL ORDER BY access_version DESC", logger);
    }

    public static ArrayList<SystemBean> getConfiguredSystemList(Connection conn) {
        String sqlStmt = "SELECT "
            + "configured_system_pk, computer_name, facet_version, dummy_database_version, dms_version, "
            + "kofax_version, kofax_license_key, kofax_product_code, ship_name, type, hull, homeport, rsupply, "
            + "is_prepped_ind, network_adapter, ship_pk, uic, multi_ship_ind, multi_ship_uic_list, decom_date_fmt, "
            + "nwcf_ind, contract_number, ghost_version, form_1348_no_location_ind, form_1348_no_class_ind, ship_fk,"
            + "os_version "
            + "FROM configured_system_vw "
            + "ORDER BY ship_name IS NULL OR ship_name='', ship_name, computer_name";
        ArrayList<SystemBean> resultList = new ArrayList<SystemBean>();
        HashMap<Integer, String> currLocMap = getCurrLocMap(conn);
        HashMap<String, SupportBean> supportSummaryMap = SupportModel.getSummaryByShipMap(conn, 1);

        debugLog("SQL", "getConfiguredSystemList", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                SystemBean resultBean = new SystemBean();
                resultBean.setConfiguredSystemPk(rs.getString("configured_system_pk"));
                resultBean.setComputerName(rs.getString("computer_name"));

                resultBean.setFacetVersion(rs.getString("facet_version"));
                resultBean.setDummyDatabaseVersion(rs.getString("dummy_database_version"));
                resultBean.setDmsVersion(rs.getString("dms_version"));
                resultBean.setKofaxVersion(rs.getString("kofax_version"));

                resultBean.setKofaxLicenseKey(rs.getString("kofax_license_key"));
                resultBean.setKofaxProductCode(rs.getString("kofax_product_code"));

                resultBean.setRsupply(rs.getString("rsupply"));
                resultBean.setHomeport(rs.getString("homeport"));
                resultBean.setIsPreppedInd(rs.getString("is_prepped_ind"));
                resultBean.setDecomDate(rs.getString("decom_date_fmt"));

                resultBean.setLocation(currLocMap.get(rs.getInt("configured_system_pk")));

                resultBean.setNetworkAdapter(rs.getString("network_adapter"));

                resultBean.setMultiShipInd(rs.getString("multi_ship_ind"));
                resultBean.setMultiShipUicList(rs.getString("multi_ship_uic_list"));

                resultBean.setNwcfInd(rs.getString("nwcf_ind"));
                resultBean.setContractNumber(rs.getString("contract_number"));

                resultBean.setOsVersion(rs.getString("os_version"));
                resultBean.setGhostVersion(rs.getString("ghost_version"));

                resultBean.setForm1348NoLocationInd(rs.getString("form_1348_no_location_ind"));
                resultBean.setForm1348NoClassInd(rs.getString("form_1348_no_class_ind"));

                resultBean.setShipFk(rs.getString("ship_fk"));

                if (!CommonMethods.isEmpty(rs.getString("uic"))) {
                    resultBean.setShipPk(rs.getString("ship_pk"));
                    resultBean.setShipName(rs.getString("ship_name") + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG") ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")" : ""));
                    resultBean.setUic(rs.getString("uic"));
                    resultBean.setType(rs.getString("type"));
                    resultBean.setHull(rs.getString("hull"));
                    resultBean.setLastVisitBean(supportSummaryMap.get(rs.getString("uic")));
                }

                resultList.add(resultBean);
            }
        } catch (Exception e) {
            debugLog("ERROR", "getConfiguredSystemList", e, logger);
        }

        return resultList;
    }

    public static ArrayList<SystemBean> getConfiguredSystemListByUic(Connection conn, String uic) {
        String sqlStmt = "SELECT "
            + "configured_system_pk, computer_name, facet_version, kofax_version, dummy_database_version, dms_version, "
            + "network_adapter, admin_password, decom_date_fmt, s2_closure_date_fmt, fuel_closure_date_fmt, ship_fk "
            + "FROM configured_system_vw "
            + "WHERE uic = ? "
            + "ORDER BY computer_name";
        ArrayList<SystemBean> resultList = new ArrayList<SystemBean>();
        HashMap<Integer, String> currLocMap = getCurrLocMap(conn);

        debugLog("SQL", "getConfiguredSystemList", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setString(1, uic);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                SystemBean resultBean = new SystemBean();
                resultBean.setConfiguredSystemPk(rs.getString("configured_system_pk"));
                resultBean.setComputerName(rs.getString("computer_name"));

                resultBean.setFacetVersion(rs.getString("facet_version"));
                resultBean.setKofaxVersion(rs.getString("kofax_version"));
                resultBean.setDummyDatabaseVersion(rs.getString("dummy_database_version"));
                resultBean.setDmsVersion(rs.getString("dms_version"));
                resultBean.setNetworkAdapter(rs.getString("network_adapter"));
                resultBean.setAdminPassword(rs.getString("admin_password"));

                resultBean.setDecomDate(rs.getString("decom_date_fmt"));

                resultBean.setS2ClosureDate(rs.getString("s2_closure_date_fmt"));
                resultBean.setFuelClosureDate(rs.getString("fuel_closure_date_fmt"));

                resultBean.setLocation(currLocMap.get(rs.getInt("configured_system_pk")));

                resultList.add(resultBean);
            }
        } catch (Exception e) {
            debugLog("ERROR", "getConfiguredSystemList", e, logger);
        }

        return resultList;
    }

    public static ArrayList<SystemBean> getFullConfiguredSystemList(Connection conn) {
            String sqlStmt = "SELECT "
                + "configured_system_pk, computer_name, laptop_tag, laptop_product_name, laptop_serial_number, laptop_status, "
                + "laptop_status_notes, facet_version, dummy_database_version, dms_version, scanner_tag, "
                + "scanner_product_name, scanner_serial_number, scanner_status, scanner_status_notes, kofax_product_name, "
                + "kofax_version, kofax_license_key, kofax_product_code, vrs_license_key, vrs_product_code, "
                + "ms_office_product_name, ms_office_license_key, ship_name, type, hull, homeport, rsupply, is_prepped_ind, "
                + "network_adapter, ship_pk, uic, multi_ship_ind, multi_ship_uic_list, s2_closure_date_fmt, "
                + "fuel_closure_date_fmt, decom_date_fmt, last_transmittal_num, last_upload_date_fmt, "
                + "form_1348_upload_date_fmt, form_1149_upload_date_fmt, food_approval_upload_date_fmt, "
                + "food_receipt_upload_date_fmt, pcard_admin_upload_date_fmt, pcard_invoice_upload_date_fmt, "
                + "price_change_upload_date_fmt, sfoedl_upload_date_fmt, uol_upload_date_fmt, nwcf_ind, contract_number, "
                + "hardware_file_uploaded_date, training_file_uploaded_date, laptop1_file_uploaded_date, "
                + "laptop2_file_uploaded_date, post_install_file_uploaded_date, form_1348_no_location_ind, form_1348_no_class_ind "
                + "FROM configured_system_transmittal_vw "
                + "ORDER BY ship_name IS NULL "
                + "OR ship_name='', ship_name";

        ArrayList<SystemBean> resultList = new ArrayList<SystemBean>();
        HashMap<Integer, ArrayList<FileBean>> attachedFileMap = getAttachedFileMap(conn);
        HashMap<Integer, String> currLocMap = getCurrLocMap(conn);

        HashMap<String, BackfileBean> backfileMap = BackfileModel.getWorkflowMap(conn);
        HashMap<String, TrainingBean> trainingMap = TrainingModel.getWorkflowMap(conn);
        HashMap<String, DecomBean> decomMap = DecomModel.getWorkflowMap(conn);
        HashMap<String, SupportBean> supportSummaryMap = SupportModel.getSummaryByShipMap(conn, 1);
        HashMap<String, ArrayList<UserBean>> pocMap = ShipModel.getPocMap(conn);
        HashMap<String, ArrayList<String>> missingTransmittalMap = ReportModel.getMissingTransmittalMap(conn);
        HashMap<String, ArrayList<String>> atoInstalledMap = SupportModel.getAtoInstalledMap(conn);
        HashMap<String, ArrayList<String>> atoMissingMap = SupportModel.getAtoMissingMap(conn);
        String[] rsupplyUpgradeShipPkArr = SupportModel.getRsupplyUpgradeShipPkArr(conn);

        debugLog("SQL", "getFullConfiguredSystemList", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                SystemBean resultBean = new SystemBean();
                resultBean.setConfiguredSystemPk(rs.getString("configured_system_pk"));
                resultBean.setComputerName(rs.getString("computer_name"));
                resultBean.setLaptopTag(rs.getString("laptop_tag"));
                resultBean.setLaptopProductName(rs.getString("laptop_product_name"));
                resultBean.setLaptopSerialNumber(rs.getString("laptop_serial_number"));
                resultBean.setLaptopStatus(CommonMethods.trim(CommonMethods.nes(rs.getString("laptop_status")) + " " + CommonMethods.nes(rs.getString("laptop_status_notes"))));

                resultBean.setFacetVersion(rs.getString("facet_version"));
                resultBean.setDummyDatabaseVersion(rs.getString("dummy_database_version"));
                resultBean.setDmsVersion(rs.getString("dms_version"));

                resultBean.setScannerTag(rs.getString("scanner_tag"));
                resultBean.setScannerProductName(rs.getString("scanner_product_name"));
                resultBean.setScannerSerialNumber(rs.getString("scanner_serial_number"));
                resultBean.setScannerStatus(CommonMethods.trim(CommonMethods.nes(rs.getString("scanner_status")) + " " + CommonMethods.nes(rs.getString("scanner_status_notes"))));

                resultBean.setKofaxProductName(rs.getString("kofax_product_name"));
                resultBean.setKofaxLicenseKey(rs.getString("kofax_license_key"));
                resultBean.setKofaxProductCode(rs.getString("kofax_product_code"));
                resultBean.setKofaxVersion(rs.getString("kofax_version"));

                resultBean.setVrsLicenseKey(rs.getString("vrs_license_key"));
                resultBean.setVrsProductCode(rs.getString("vrs_product_code"));

                resultBean.setMsOfficeProductName(rs.getString("ms_office_product_name"));
                resultBean.setMsOfficeLicenseKey(rs.getString("ms_office_license_key"));

                resultBean.setShipName(rs.getString("ship_name") + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG") ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")" : ""));
                resultBean.setRsupply(rs.getString("rsupply"));
                resultBean.setHomeport(rs.getString("homeport"));
                resultBean.setIsPreppedInd(rs.getString("is_prepped_ind"));
                resultBean.setDecomDate(rs.getString("decom_date_fmt"));

                resultBean.setS2ClosureDate(rs.getString("s2_closure_date_fmt"));
                resultBean.setFuelClosureDate(rs.getString("fuel_closure_date_fmt"));

                resultBean.setLocation(currLocMap.get(rs.getInt("configured_system_pk")));

                resultBean.setAttachedFileList(attachedFileMap.get(rs.getInt("configured_system_pk")));

                resultBean.setNetworkAdapter(rs.getString("network_adapter"));

                resultBean.setMultiShipInd(rs.getString("multi_ship_ind"));
                resultBean.setMultiShipUicList(rs.getString("multi_ship_uic_list"));

                resultBean.setNwcfInd(rs.getString("nwcf_ind"));
                resultBean.setContractNumber(rs.getString("contract_number"));

                resultBean.setHardwareFileUploadedDate(rs.getString("hardware_file_uploaded_date"));
                resultBean.setTrainingFileUploadedDate(rs.getString("training_file_uploaded_date"));
                resultBean.setLaptop1FileUploadedDate(rs.getString("laptop1_file_uploaded_date"));
                resultBean.setLaptop2FileUploadedDate(rs.getString("laptop2_file_uploaded_date"));
                resultBean.setPostInstallFileUploadedDate(rs.getString("post_install_file_uploaded_date"));

                resultBean.setForm1348NoLocationInd(rs.getString("form_1348_no_location_ind"));
                resultBean.setForm1348NoClassInd(rs.getString("form_1348_no_class_ind"));

                //Transmittal Summary
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

                if (!CommonMethods.isEmpty(rs.getString("uic"))) {
                    resultBean.setShipPk(rs.getString("ship_pk"));
                    resultBean.setUic(rs.getString("uic"));
                    resultBean.setType(rs.getString("type"));
                    resultBean.setHull(rs.getString("hull"));
                    resultBean.setBackfileBean(backfileMap.get(rs.getString("uic")));
                    resultBean.setTrainingBean(trainingMap.get(rs.getString("uic")));
                    resultBean.setDecomBean(decomMap.get(rs.getString("uic")));
                    resultBean.setLastVisitBean(supportSummaryMap.get(rs.getString("uic")));
                    resultBean.setPrimaryPocEmails(ShipModel.getPrimaryPocEmails(pocMap.get(rs.getString("uic"))));
                    resultBean.setPocEmails(ShipModel.getPocEmails(pocMap.get(rs.getString("uic"))));
                    resultBean.setMissingTransmittalList(missingTransmittalMap.get(rs.getString("ship_pk")));
                    resultBean.setMissingTransmittalStr(ReportModel.printTransmittalList(missingTransmittalMap.get(rs.getString("ship_pk"))));
                    resultBean.setAtoInstalledList(atoInstalledMap.get(rs.getString("ship_pk")));
                    resultBean.setAtoMissingList(atoMissingMap.get(rs.getString("ship_pk")));
                    if (CommonMethods.isIn(rsupplyUpgradeShipPkArr, rs.getString("ship_pk"))) resultBean.setRsupplyUpgradeInd("Y");
                    resultBean.setInactivityList(ReportModel.getInactivityList(resultBean));
                }

                resultList.add(resultBean);
            }
        } catch (Exception e) {
            debugLog("ERROR", "getFullConfiguredSystemList", e, logger);
        }

        return resultList;
    }

    public static ArrayList<SystemBean> getConfiguredSystemPkList(Connection conn) {
        String sqlStmt = "SELECT configured_system_pk FROM configured_system_vw ORDER BY ship_name IS NULL OR ship_name='', ship_name";
        ArrayList<SystemBean> resultList = new ArrayList<SystemBean>();

        debugLog("SQL", "getConfiguredSystemPkList", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                SystemBean resultBean = new SystemBean();
                resultBean.setConfiguredSystemPk(rs.getString("configured_system_pk"));
                resultList.add(resultBean);
            }
        } catch (Exception e) {
            debugLog("ERROR", "getConfiguredSystemPkList", e, logger);
        }

        return resultList;
    }

    @Deprecated public static ArrayList<ShipBean> getConfiguredSystemActiveShipList(Connection conn) {
        String sqlStmt = "SELECT DISTINCT ship_pk, uic, ship_name, type, hull FROM configured_system_vw WHERE is_prepped_ind = 'A' AND ship_pk IS NOT NULL ORDER BY ship_name IS NULL OR ship_name='', ship_name";
        ArrayList<ShipBean> resultList = new ArrayList<ShipBean>();

        debugLog("SQL", "getConfiguredSystemActiveShipList", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                ShipBean resultBean = new ShipBean();
                resultBean.setShipPk(rs.getString("ship_pk"));
                resultBean.setUic(rs.getString("uic"));
                resultBean.setShipName(rs.getString("ship_name") + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG") ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")" : ""));
                resultList.add(resultBean);
            }
        } catch (Exception e) {
            debugLog("ERROR", "getConfiguredSystemActiveShipList", e, logger);
        }

        return resultList;
    }

    public static ArrayList<SystemBean> getEmailConfiguredSystemList(Connection conn) {
        return getEmailConfiguredSystemList(conn, false);
    }

    public static ArrayList<SystemBean> getEmailNoRetiredConfiguredSystem(Connection conn) {
        return getEmailConfiguredSystemList(conn, true);
    }

    private static ArrayList<SystemBean> getEmailConfiguredSystemList(Connection conn, boolean includeRetired) {
        String sqlStmt = "SELECT "
            + "configured_system_pk, computer_name, facet_version, dummy_database_version, dms_version, kofax_version, "
            + "ship_name, type, hull, homeport, network_adapter, ship_pk, uic, s2_closure_date_fmt, fuel_closure_date_fmt, "
            + "decom_date_fmt, last_transmittal_num, last_upload_date_fmt, form_1348_upload_date_fmt, "
            + "form_1149_upload_date_fmt, food_approval_upload_date_fmt, food_receipt_upload_date_fmt, "
            + "pcard_admin_upload_date_fmt, pcard_invoice_upload_date_fmt, price_change_upload_date_fmt, "
            + "sfoedl_upload_date_fmt, uol_upload_date_fmt "
            + "FROM configured_system_transmittal_vw "
            + "WHERE is_prepped_ind = 'A' "
            + "AND ship_pk IS NOT NULL "
            + (includeRetired ? "" : "AND computer_name NOT LIKE '%- RETIRED%' ")
            + "ORDER BY ship_name IS NULL OR ship_name='', ship_name";
        ArrayList<SystemBean> resultList = new ArrayList<SystemBean>();
        HashMap<Integer, String> currLocMap = getCurrLocMap(conn);

        HashMap<String, ArrayList<UserBean>> pocMap = ShipModel.getPocMap(conn);
        HashMap<String, ArrayList<String>> missingTransmittalMap = ReportModel.getMissingTransmittalMap(conn);
        HashMap<String, ArrayList<String>> atoInstalledMap = SupportModel.getAtoInstalledMap(conn);
        HashMap<String, ArrayList<String>> atoMissingMap = SupportModel.getAtoMissingMap(conn);
        String[] rsupplyUpgradeShipPkArr = SupportModel.getRsupplyUpgradeShipPkArr(conn);

        debugLog("SQL", "getEmailConfiguredSystemList", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                SystemBean resultBean = new SystemBean();
                resultBean.setConfiguredSystemPk(rs.getString("configured_system_pk"));
                resultBean.setComputerName(rs.getString("computer_name"));

                resultBean.setFacetVersion(rs.getString("facet_version"));
                resultBean.setDummyDatabaseVersion(rs.getString("dummy_database_version"));
                resultBean.setDmsVersion(rs.getString("dms_version"));
                resultBean.setKofaxVersion(rs.getString("kofax_version"));

                resultBean.setShipName(rs.getString("ship_name") + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG") ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")" : ""));
                resultBean.setHomeport(rs.getString("homeport"));

                resultBean.setS2ClosureDate(rs.getString("s2_closure_date_fmt"));
                resultBean.setFuelClosureDate(rs.getString("fuel_closure_date_fmt"));

                resultBean.setLocation(currLocMap.get(rs.getInt("configured_system_pk")));

                resultBean.setNetworkAdapter(rs.getString("network_adapter"));

                //Transmittal Summary
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

                resultBean.setShipPk(rs.getString("ship_pk"));
                resultBean.setUic(rs.getString("uic"));
                resultBean.setType(rs.getString("type"));
                resultBean.setHull(rs.getString("hull"));
                resultBean.setPrimaryPocEmails(ShipModel.getPrimaryPocEmails(pocMap.get(rs.getString("uic"))));
                resultBean.setPocEmails(ShipModel.getPocEmails(pocMap.get(rs.getString("uic"))));
                resultBean.setMissingTransmittalList(missingTransmittalMap.get(rs.getString("ship_pk")));
                resultBean.setMissingTransmittalStr(ReportModel.printTransmittalList(missingTransmittalMap.get(rs.getString("ship_pk"))));
                resultBean.setAtoInstalledList(atoInstalledMap.get(rs.getString("ship_pk")));
                resultBean.setAtoMissingList(atoMissingMap.get(rs.getString("ship_pk")));
                if (CommonMethods.isIn(rsupplyUpgradeShipPkArr, rs.getString("ship_pk"))) resultBean.setRsupplyUpgradeInd("Y");
                resultBean.setInactivityList(ReportModel.getInactivityList(resultBean));

                resultList.add(resultBean);
            }
        } catch (Exception e) {
            debugLog("ERROR", "getEmailConfiguredSystemList", e, logger);
        }

        return resultList;
    }

    public static SystemBean getConfiguredSystemBean(Connection conn, SystemBean inputBean) {
        String sqlStmt = "SELECT "
            + "configured_system_pk, laptop_fk, computer_name, laptop_tag, laptop_product_name, laptop_serial_number, "
            + "laptop_status, laptop_status_notes, facet_version, facet_version_history, dummy_database_version, scanner_fk, "
            + "scanner_tag, scanner_product_name, scanner_serial_number, scanner_status, scanner_status_notes, kofax_license_fk, "
            + "kofax_product_name, kofax_license_key, kofax_product_code, kofax_version, kofax_version_history, vrs_license_fk, "
            + "vrs_license_key, vrs_product_code, ms_office_license_fk, ms_office_product_name, ms_office_license_key, "
            + "access_version, access_version_history, os_version, "
            + "ship_pk, uic, ship_name, type, hull, rsupply, homeport, documentation_version, "
            + "documentation_version_history, notes, ghost_version, is_prepped_ind, network_adapter, admin_password, decom_date_fmt, "
            + "dms_version, s2_closure_date_fmt, fuel_closure_date_fmt, multi_ship_ind, multi_ship_uic_list, nwcf_ind, "
            + "contract_number, hardware_file_fk, training_file_fk, laptop1_file_fk, laptop2_file_fk, post_install_file_fk, "
            + "form_1348_no_location_ind, form_1348_no_class_ind, ship_fk "
            + "FROM configured_system_vw "
            + "WHERE configured_system_pk = ?";
        SystemBean systemBean = new SystemBean();

        String configuredSystemPk = inputBean.getConfiguredSystemPk();
        if (CommonMethods.cInt(configuredSystemPk) <= -1) return null;

        debugLog("SQL", "getConfiguredSystemBean", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            pStmt.setInt(1, CommonMethods.cInt(configuredSystemPk));

            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                systemBean.setConfiguredSystemPk(rs.getString("configured_system_pk"));

                systemBean.setLaptopPk(rs.getString("laptop_fk"));
                systemBean.setComputerName(rs.getString("computer_name"));
                systemBean.setOsVersion(rs.getString("os_version"));
                systemBean.setLaptopTag(rs.getString("laptop_tag"));
                systemBean.setLaptopProductName(rs.getString("laptop_product_name"));
                systemBean.setLaptopSerialNumber(rs.getString("laptop_serial_number"));
                systemBean.setLaptopStatus(CommonMethods.trim(CommonMethods.nes(rs.getString("laptop_status")) + " " + CommonMethods.nes(rs.getString("laptop_status_notes"))));

                systemBean.setFacetVersion(rs.getString("facet_version"));
                systemBean.setFacetVersionHistory(rs.getString("facet_version_history"));
                systemBean.setDummyDatabaseVersion(rs.getString("dummy_database_version"));

                systemBean.setScannerPk(rs.getString("scanner_fk"));
                systemBean.setScannerTag(rs.getString("scanner_tag"));
                systemBean.setScannerProductName(rs.getString("scanner_product_name"));
                systemBean.setScannerSerialNumber(rs.getString("scanner_serial_number"));
                systemBean.setScannerStatus(CommonMethods.trim(CommonMethods.nes(rs.getString("scanner_status")) + " " + CommonMethods.nes(rs.getString("scanner_status_notes"))));

                systemBean.setKofaxLicensePk(rs.getString("kofax_license_fk"));
                systemBean.setKofaxProductName(rs.getString("kofax_product_name"));
                systemBean.setCurrKofaxProductName(rs.getString("kofax_product_name")); //For editing
                systemBean.setKofaxLicenseKey(rs.getString("kofax_license_key"));
                systemBean.setKofaxProductCode(rs.getString("kofax_product_code"));
                systemBean.setKofaxVersion(rs.getString("kofax_version"));
                systemBean.setKofaxVersionHistory(rs.getString("kofax_version_history"));

                systemBean.setVrsLicensePk(rs.getString("vrs_license_fk"));
                systemBean.setVrsLicenseKey(rs.getString("vrs_license_key"));
                systemBean.setVrsProductCode(rs.getString("vrs_product_code"));

                systemBean.setMsOfficeLicensePk(rs.getString("ms_office_license_fk"));
                systemBean.setMsOfficeProductName(rs.getString("ms_office_product_name"));
                systemBean.setMsOfficeLicenseKey(rs.getString("ms_office_license_key"));
                systemBean.setAccessVersion(rs.getString("access_version"));
                systemBean.setCurrAccessVersion(rs.getString("access_version")); //For editing
                systemBean.setAccessVersionHistory(rs.getString("access_version_history"));

                systemBean.setGhostVersion(rs.getString("ghost_version"));
                systemBean.setCurrGhostVersion(rs.getString("ghost_version")); //For editing

                systemBean.setShipFk(rs.getString("ship_fk"));

                systemBean.setShipPk(rs.getString("ship_pk"));
                systemBean.setUic(rs.getString("uic"));
                systemBean.setShipName(rs.getString("ship_name") + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG") ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")" : ""));
                systemBean.setType(rs.getString("type"));
                systemBean.setHull(rs.getString("hull"));
                systemBean.setIsPreppedInd(rs.getString("is_prepped_ind"));
                systemBean.setHomeport(rs.getString("homeport"));
                systemBean.setRsupply(rs.getString("rsupply"));
                systemBean.setDecomDate(rs.getString("decom_date_fmt"));

                systemBean.setDmsVersion(rs.getString("dms_version"));
                systemBean.setS2ClosureDate(rs.getString("s2_closure_date_fmt"));
                systemBean.setFuelClosureDate(rs.getString("fuel_closure_date_fmt"));

                systemBean.setDocumentationVersion(rs.getString("documentation_version"));
                systemBean.setDocumentationVersionHistory(rs.getString("documentation_version_history"));

                systemBean.setNetworkAdapter(rs.getString("network_adapter"));
                systemBean.setAdminPassword(rs.getString("admin_password"));

                systemBean.setNwcfInd(rs.getString("nwcf_ind"));
                systemBean.setCurrContractNumber(rs.getString("contract_number"));
                systemBean.setContractNumber(rs.getString("contract_number"));

                systemBean.setNotes(rs.getString("notes"));

                systemBean.setForm1348NoLocationInd(rs.getString("form_1348_no_location_ind"));
                systemBean.setForm1348NoClassInd(rs.getString("form_1348_no_class_ind"));

                systemBean.setMultiShipInd(rs.getString("multi_ship_ind"));
                systemBean.setMultiShipUicList(rs.getString("multi_ship_uic_list"));
                if (!CommonMethods.isEmpty(systemBean.getMultiShipUicList())) {
                    ArrayList<ShipBean> shipList = new ArrayList<ShipBean>();
                    for (String uic : systemBean.getMultiShipUicList().replaceAll(" ", "").split(",")) {
                        ShipBean shipBean = new ShipBean();
                        shipBean.setUic(uic);
                        shipBean.setShipName(ShipModel.getShipName(conn, uic));
                        shipList.add(shipBean);
                    }
                    systemBean.setMultiShipList(shipList);
                }

                if (!CommonMethods.isEmpty(rs.getString("hardware_file_fk"))) systemBean.setHardwareFileBean(FileModel.getFileBean(conn, rs.getInt("hardware_file_fk")));
                if (!CommonMethods.isEmpty(rs.getString("training_file_fk"))) systemBean.setTrainingFileBean(FileModel.getFileBean(conn, rs.getInt("training_file_fk")));
                if (!CommonMethods.isEmpty(rs.getString("laptop1_file_fk"))) systemBean.setLaptop1FileBean(FileModel.getFileBean(conn, rs.getInt("laptop1_file_fk")));
                if (!CommonMethods.isEmpty(rs.getString("laptop2_file_fk"))) systemBean.setLaptop2FileBean(FileModel.getFileBean(conn, rs.getInt("laptop2_file_fk")));
                if (!CommonMethods.isEmpty(rs.getString("post_install_file_fk"))) systemBean.setPostInstallFileBean(FileModel.getFileBean(conn, rs.getInt("post_install_file_fk")));

                systemBean.setAttachedFileList(getAttachedFileList(conn, rs.getInt("configured_system_pk")));
                systemBean.setLocHistList(getLocHistList(conn, rs.getInt("configured_system_pk")));

                if (systemBean.getLocHistList() != null && systemBean.getLocHistList().size() > 0) {
                    systemBean.setLocation(systemBean.getLocHistList().get(0).getLocation());
                }

                systemBean.setAtoInstalledList(SupportModel.getAtoInstalledList(conn, rs.getInt("ship_pk")));
            }
        } catch (Exception e) {
            debugLog("ERROR", "getConfiguredSystemBean", e, logger);
        }
        return systemBean;
    }

    private static int getInsertedConfiguredSystemPk(Connection conn) {
        String sqlStmt = "SELECT MAX(configured_system_pk) FROM configured_system";
        int returnVal = -1;

        debugLog("SQL", "getInsertedConfiguredSystemPk", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            returnVal = rs.getInt(1);
        } catch (Exception e) {
            debugLog("ERROR", "getInsertedConfiguredSystemPk", e, logger);
        }

        return returnVal;
    }

    public static int insertConfiguredSystem(Connection conn, SystemBean systemBean, LoginBean loginBean, String uploadDir) {
        String[] columns = {
            "laptop_fk", "scanner_fk", "kofax_license_fk", "vrs_license_fk", "ms_office_license_fk", "facet_version",
            "facet_version_history", "kofax_product_name", "kofax_version", "kofax_version_history", "access_version",
            "access_version_history", "uic", "documentation_version", "documentation_version_history", "ghost_version",
            "is_prepped_ind", "notes", "dummy_database_version", "network_adapter", "admin_password", "dms_version",
            "s2_closure_date", "fuel_closure_date", "multi_ship_ind", "multi_ship_uic_list", "nwcf_ind",
            "contract_number", "hardware_file_fk", "training_file_fk", "laptop1_file_fk", "laptop2_file_fk",
            "post_install_file_fk", "form_1348_no_location_ind", "form_1348_no_class_ind", "ship_fk",
            "last_updated_by", "last_updated_date"
        };
        //String sqlStmt = "INSERT INTO configured_system (laptop_fk, scanner_fk, kofax_license_fk, vrs_license_fk, ms_office_license_fk, "
        //        + "facet_version, facet_version_history, kofax_product_name, kofax_version, kofax_version_history, access_version, "
        //        + "access_version_history, uic, documentation_version, documentation_version_history, ghost_version, is_prepped_ind, "
        //        + "notes, dummy_database_version, network_adapter, admin_password, dms_version, s2_closure_date, fuel_closure_date, "
        //        + "multi_ship_ind, multi_ship_uic_list, nwcf_ind, contract_number, hardware_file_fk, training_file_fk, laptop1_file_fk, "
        //        + "laptop2_file_fk, post_install_file_fk, form_1348_no_location_ind, form_1348_no_class_ind, ship_fk, last_updated_by, "
        //        + "last_updated_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sqlStmt = ModelUtils.generateInsertStmt("configured_system", columns);
        boolean ranOk = false;
        int newPk = -1;

        debugLog("SQL", "insertConfiguredSystem", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            CommonMethods.setInt(pStmt, 2, systemBean.getScannerPk());
            CommonMethods.setInt(pStmt, 5, systemBean.getMsOfficeLicensePk());
            CommonMethods.setString(pStmt, 6, systemBean.getFacetVersion());
            CommonMethods.setString(pStmt, 7, systemBean.getFacetVersionHistory());

            if (!CommonMethods.isEmpty(systemBean.getLaptopPk())) {
                pStmt.setInt(1, CommonMethods.cInt(systemBean.getLaptopPk()));
                CommonMethods.setInt(pStmt, 3, systemBean.getKofaxLicensePk());
                CommonMethods.setInt(pStmt, 4, systemBean.getVrsLicensePk());
                CommonMethods.setString(pStmt, 8, systemBean.getKofaxProductName());
                CommonMethods.setString(pStmt, 16, systemBean.getGhostVersion());
            } else {
                pStmt.setNull(1, java.sql.Types.NUMERIC);
                pStmt.setNull(3, java.sql.Types.NUMERIC);
                pStmt.setNull(4, java.sql.Types.NUMERIC);
                pStmt.setNull(8, java.sql.Types.VARCHAR);
                pStmt.setNull(16, java.sql.Types.VARCHAR);
            }

            CommonMethods.setString(pStmt, 9, systemBean.getKofaxVersion());
            CommonMethods.setString(pStmt, 10, systemBean.getKofaxVersionHistory());
            CommonMethods.setString(pStmt, 11, systemBean.getAccessVersion());
            CommonMethods.setString(pStmt, 12, systemBean.getAccessVersionHistory());
            CommonMethods.setString(pStmt, 13, systemBean.getUic());
            CommonMethods.setString(pStmt, 14, systemBean.getDocumentationVersion());
            CommonMethods.setString(pStmt, 15, systemBean.getDocumentationVersionHistory());
            CommonMethods.setString(pStmt, 17, systemBean.getIsPreppedInd());
            CommonMethods.setString(pStmt, 18, systemBean.getNotes());
            CommonMethods.setString(pStmt, 19, systemBean.getDummyDatabaseVersion());
            CommonMethods.setString(pStmt, 20, systemBean.getNetworkAdapter());
            CommonMethods.setString(pStmt, 21, systemBean.getAdminPassword());
            CommonMethods.setString(pStmt, 22, systemBean.getDmsVersion());
            CommonMethods.setDate(pStmt, 23, systemBean.getS2ClosureDate());
            CommonMethods.setDate(pStmt, 24, systemBean.getFuelClosureDate());

            CommonMethods.setString(pStmt, 25, systemBean.getMultiShipInd());

            if (CommonMethods.nes(systemBean.getMultiShipInd()).equals("Y")) {
                CommonMethods.setString(pStmt, 26, systemBean.getMultiShipUicList());
            } else {
                pStmt.setNull(26, java.sql.Types.VARCHAR);
            }

            CommonMethods.setString(pStmt, 27, systemBean.getNwcfInd());
            CommonMethods.setString(pStmt, 28, systemBean.getContractNumber());

            if (systemBean.getHardwareFile() != null && systemBean.getHardwareFile().getFileSize() > 0) {
                pStmt.setInt(29, FileModel.saveFile(conn, systemBean.getHardwareFile(), loginBean, uploadDir));
            } else {
                pStmt.setNull(29, java.sql.Types.NUMERIC);
            }

            if (systemBean.getTrainingFile() != null && systemBean.getTrainingFile().getFileSize() > 0) {
                pStmt.setInt(30, FileModel.saveFile(conn, systemBean.getTrainingFile(), loginBean, uploadDir));
            } else {
                pStmt.setNull(30, java.sql.Types.NUMERIC);
            }

            if (systemBean.getLaptop1File() != null && systemBean.getLaptop1File().getFileSize() > 0) {
                pStmt.setInt(31, FileModel.saveFile(conn, systemBean.getLaptop1File(), loginBean, uploadDir));
            } else {
                pStmt.setNull(31, java.sql.Types.NUMERIC);
            }

            if (systemBean.getLaptop2File() != null && systemBean.getLaptop2File().getFileSize() > 0) {
                pStmt.setInt(32, FileModel.saveFile(conn, systemBean.getLaptop2File(), loginBean, uploadDir));
            } else {
                pStmt.setNull(32, java.sql.Types.NUMERIC);
            }

            if (systemBean.getPostInstallFile() != null && systemBean.getPostInstallFile().getFileSize() > 0) {
                pStmt.setInt(33, FileModel.saveFile(conn, systemBean.getPostInstallFile(), loginBean, uploadDir));
            } else {
                pStmt.setNull(33, java.sql.Types.NUMERIC);
            }

            CommonMethods.setString(pStmt, 34, systemBean.getForm1348NoLocationInd());
            CommonMethods.setString(pStmt, 35, systemBean.getForm1348NoClassInd());

            pStmt.setString(36, systemBean.getShipFk());//added 2018-09-10 -Lewis
            pStmt.setString(37, loginBean.getFullName());
            pStmt.setString(38, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));

            ranOk = (pStmt.executeUpdate() == 1);

            //Get inserted issue pk to use for creating child records
            newPk = getInsertedConfiguredSystemPk(conn);
            systemBean.setConfiguredSystemPk(String.valueOf(newPk));

            ranOk &= newPk > -1 && insertAttachedFiles(conn, systemBean, loginBean, uploadDir) && insertLocHist(conn, systemBean, loginBean);
        } catch (Exception e) {
            debugLog("ERROR", "insertConfiguredSystem", e, logger);
            ranOk = false;
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }

        return ranOk ? newPk : -1;
    }

    public static boolean updateConfiguredSystem(Connection conn, SystemBean systemBean, LoginBean loginBean, String uploadDir) {
        String sqlStmt = "UPDATE configured_system "
                + "SET laptop_fk = ?, scanner_fk = ?, kofax_license_fk = ?, vrs_license_fk = ?, ms_office_license_fk = ?, facet_version = ?, facet_version_history = ?, "
                + "kofax_product_name = ?, kofax_version = ?, kofax_version_history = ?, access_version = ?, access_version_history = ?, "
                + "uic = ?, documentation_version = ?, documentation_version_history = ?, ghost_version = ?, is_prepped_ind = ?, "
                + "notes = ?, dummy_database_version = ?, network_adapter = ?, admin_password = ?, dms_version = ?, s2_closure_date = ?, "
                + "fuel_closure_date = ?, multi_ship_ind = ?, multi_ship_uic_list = ?, nwcf_ind = ?, contract_number = ?, hardware_file_fk = ?, "
                + "training_file_fk = ?, laptop1_file_fk = ?, laptop2_file_fk = ?, post_install_file_fk = ?, form_1348_no_location_ind = ?, "
                + "form_1348_no_class_ind = ?, ship_fk = ?, last_updated_by = ?, last_updated_date = ?, os_version = ? "
                + "WHERE configured_system_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(systemBean.getConfiguredSystemPk()) <= -1) return false;

        debugLog("SQL", "updateConfiguredSystem", sqlStmt + " (configured_system_pk = " + systemBean.getConfiguredSystemPk() + ")", logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);

            CommonMethods.setInt(pStmt, 2, systemBean.getScannerPk());
            CommonMethods.setInt(pStmt, 5, systemBean.getMsOfficeLicensePk());
            CommonMethods.setString(pStmt, 6, systemBean.getFacetVersion());
            CommonMethods.setString(pStmt, 7, systemBean.getFacetVersionHistory());

            if (!CommonMethods.isEmpty(systemBean.getLaptopPk())) {
                pStmt.setInt(1, CommonMethods.cInt(systemBean.getLaptopPk()));
                CommonMethods.setInt(pStmt, 3, systemBean.getKofaxLicensePk());
                CommonMethods.setInt(pStmt, 4, systemBean.getVrsLicensePk());
                CommonMethods.setString(pStmt, 8, systemBean.getKofaxProductName());
                CommonMethods.setString(pStmt, 16, systemBean.getGhostVersion());
            } else {
                pStmt.setNull(1, java.sql.Types.NUMERIC);
                pStmt.setNull(3, java.sql.Types.NUMERIC);
                pStmt.setNull(4, java.sql.Types.NUMERIC);
                pStmt.setNull(8, java.sql.Types.VARCHAR);
                pStmt.setNull(16, java.sql.Types.VARCHAR);
            }

            CommonMethods.setString(pStmt, 9, systemBean.getKofaxVersion());
            CommonMethods.setString(pStmt, 10, systemBean.getKofaxVersionHistory());
            CommonMethods.setString(pStmt, 11, systemBean.getAccessVersion());
            CommonMethods.setString(pStmt, 12, systemBean.getAccessVersionHistory());
            CommonMethods.setString(pStmt, 13, systemBean.getUic());
            CommonMethods.setString(pStmt, 14, systemBean.getDocumentationVersion());
            CommonMethods.setString(pStmt, 15, systemBean.getDocumentationVersionHistory());
            CommonMethods.setString(pStmt, 17, systemBean.getIsPreppedInd());
            CommonMethods.setString(pStmt, 18, systemBean.getNotes());
            CommonMethods.setString(pStmt, 19, systemBean.getDummyDatabaseVersion());
            CommonMethods.setString(pStmt, 20, systemBean.getNetworkAdapter());
            CommonMethods.setString(pStmt, 21, systemBean.getAdminPassword());
            CommonMethods.setString(pStmt, 22, systemBean.getDmsVersion());
            CommonMethods.setDate(pStmt, 23, systemBean.getS2ClosureDate());
            CommonMethods.setDate(pStmt, 24, systemBean.getFuelClosureDate());

            CommonMethods.setString(pStmt, 25, systemBean.getMultiShipInd());

            if (CommonMethods.nes(systemBean.getMultiShipInd()).equals("Y")) {
                CommonMethods.setString(pStmt, 26, systemBean.getMultiShipUicList());
            } else {
                pStmt.setNull(26, java.sql.Types.VARCHAR);
            }

            CommonMethods.setString(pStmt, 27, systemBean.getNwcfInd());
            CommonMethods.setString(pStmt, 28, systemBean.getContractNumber());

            if (systemBean.getHardwareFile() != null && systemBean.getHardwareFile().getFileSize() > 0) {
                pStmt.setInt(29, FileModel.saveFile(conn, systemBean.getHardwareFile(), loginBean, uploadDir));
            } else if (!CommonMethods.isEmpty(systemBean.getHardwareFileDeletedInd()) || CommonMethods.cInt(systemBean.getHardwareFilePk()) <= -1) {
                pStmt.setNull(29, java.sql.Types.NUMERIC);
            } else {
                pStmt.setInt(29, CommonMethods.cInt(systemBean.getHardwareFilePk()));
            }

            if (systemBean.getTrainingFile() != null && systemBean.getTrainingFile().getFileSize() > 0) {
                pStmt.setInt(30, FileModel.saveFile(conn, systemBean.getTrainingFile(), loginBean, uploadDir));
            } else if (!CommonMethods.isEmpty(systemBean.getTrainingFileDeletedInd()) || CommonMethods.cInt(systemBean.getTrainingFilePk()) <= -1) {
                pStmt.setNull(30, java.sql.Types.NUMERIC);
            } else {
                pStmt.setInt(30, CommonMethods.cInt(systemBean.getTrainingFilePk()));
            }

            if (systemBean.getLaptop1File() != null && systemBean.getLaptop1File().getFileSize() > 0) {
                pStmt.setInt(31, FileModel.saveFile(conn, systemBean.getLaptop1File(), loginBean, uploadDir));
            } else if (!CommonMethods.isEmpty(systemBean.getLaptop1FileDeletedInd()) || CommonMethods.cInt(systemBean.getLaptop1FilePk()) <= -1) {
                pStmt.setNull(31, java.sql.Types.NUMERIC);
            } else {
                pStmt.setInt(31, CommonMethods.cInt(systemBean.getLaptop1FilePk()));
            }

            if (systemBean.getLaptop2File() != null && systemBean.getLaptop2File().getFileSize() > 0) {
                pStmt.setInt(32, FileModel.saveFile(conn, systemBean.getLaptop2File(), loginBean, uploadDir));
            } else if (!CommonMethods.isEmpty(systemBean.getLaptop2FileDeletedInd()) || CommonMethods.cInt(systemBean.getLaptop2FilePk()) <= -1) {
                pStmt.setNull(32, java.sql.Types.NUMERIC);
            } else {
                pStmt.setInt(32, CommonMethods.cInt(systemBean.getLaptop2FilePk()));
            }

            if (systemBean.getPostInstallFile() != null && systemBean.getPostInstallFile().getFileSize() > 0) {
                pStmt.setInt(33, FileModel.saveFile(conn, systemBean.getPostInstallFile(), loginBean, uploadDir));
            } else if (!CommonMethods.isEmpty(systemBean.getPostInstallFileDeletedInd()) || CommonMethods.cInt(systemBean.getPostInstallFilePk()) <= -1) {
                pStmt.setNull(33, java.sql.Types.NUMERIC);
            } else {
                pStmt.setInt(33, CommonMethods.cInt(systemBean.getPostInstallFilePk()));
            }

            CommonMethods.setString(pStmt, 34, systemBean.getForm1348NoLocationInd());
            CommonMethods.setString(pStmt, 35, systemBean.getForm1348NoClassInd());

            pStmt.setString(36, systemBean.getShipFk());
            pStmt.setString(37, loginBean.getFullName());
            pStmt.setString(38, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));

            pStmt.setString(39, systemBean.getOsVersion());

            pStmt.setInt(40, CommonMethods.cInt(systemBean.getConfiguredSystemPk()));

            ranOk = (pStmt.executeUpdate() == 1) &&
                    updateAttachedFiles(conn, systemBean, loginBean, uploadDir) &&
                            insertLocHist(conn, systemBean, loginBean);
            System.out.println(PrintClassPropertiesUtil.toString(systemBean));
        } catch (Exception e) {
            debugLog("ERROR", "updateConfiguredSystem", e, logger);
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
            if (ranOk) FileModel.deleteFiles(conn, uploadDir);
        }

        return ranOk;
    }

    public static boolean deleteConfiguredSystem(Connection conn, SystemBean inputBean, String uploadDir) {
        String sqlStmt = "DELETE FROM configured_system WHERE configured_system_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getConfiguredSystemPk()) <= -1) return false;

        debugLog("SQL", "deleteConfiguredSystem", sqlStmt + " (configured_system_pk = " + inputBean.getConfiguredSystemPk() + ")", logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getConfiguredSystemPk()));
            ranOk = (pStmt.executeUpdate() == 1);
        } catch (Exception e) {
            debugLog("ERROR", "deleteConfiguredSystem", e, logger);
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
            if (ranOk) FileModel.deleteFiles(conn, uploadDir);
        }

        return ranOk;
    }

    private static HashMap<Integer, ArrayList<FileBean>> getAttachedFileMap(Connection conn) {
        String sqlStmt = "SELECT configured_system_fk, file_fk, filename, extension, filesize, uploaded_by, "
            + "strftime('%m/%d/%Y %H:%M:%S', uploaded_date) as uploaded_date_fmt "
            + "FROM configured_system_file_vw "
            + "ORDER BY configured_system_fk, filename";
        HashMap<Integer, ArrayList<FileBean>> resultMap = new HashMap<Integer, ArrayList<FileBean>>();
        //ArrayList<FileBean> resultList = new ArrayList<FileBean>();

        debugLog("SQL", "getAttachedFileMap", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                FileBean resultBean = new FileBean();
                resultBean.setFilePk(rs.getString("file_fk"));
                resultBean.setFilename(rs.getString("filename"));
                resultBean.setExtension(rs.getString("filesize"));
                resultBean.setImage(CommonMethods.getFileIcon(rs.getString("extension"), "lrg"));
                resultBean.setSmlImage(CommonMethods.getFileIcon(rs.getString("extension"), "sml"));
                resultBean.setUploadedBy(rs.getString("uploaded_by"));
                resultBean.setUploadedDate(rs.getString("uploaded_date_fmt"));

                if (resultMap.get(rs.getInt("configured_system_fk")) == null) {
                    resultMap.put(rs.getInt("configured_system_fk"), new ArrayList<FileBean>());
                }

                resultMap.get(rs.getInt("configured_system_fk")).add(resultBean);
            }
        } catch (Exception e) {
            debugLog("ERROR", "getAttachedFileMap", e, logger);
        }

        return resultMap;
    }

    private static ArrayList<FileBean> getAttachedFileList(Connection conn, int configuredSystemPk) {
        String sqlStmt = "SELECT "
                + "file_fk, filename, extension, filesize, uploaded_by, strftime('%m/%d/%Y %H:%M:%S', uploaded_date) as uploaded_date_fmt "
                + "FROM configured_system_file_vw "
                + "WHERE configured_system_fk = ? "
                + "ORDER BY filename";
        ArrayList<FileBean> resultList = new ArrayList<FileBean>();

        debugLog("SQL", "getAttachedFileList", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, configuredSystemPk);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                FileBean resultBean = new FileBean();
                resultBean.setFilePk(rs.getString("file_fk"));
                resultBean.setFilename(rs.getString("filename"));
                resultBean.setExtension(rs.getString("filesize"));
                resultBean.setImage(CommonMethods.getFileIcon(rs.getString("extension"), "lrg"));
                resultBean.setSmlImage(CommonMethods.getFileIcon(rs.getString("extension"), "sml"));
                resultBean.setUploadedBy(rs.getString("uploaded_by"));
                resultBean.setUploadedDate(rs.getString("uploaded_date_fmt"));
                resultList.add(resultBean);
            }
        } catch (Exception e) {
            debugLog("ERROR", "getAttachedFileList", e, logger);
        }

        return resultList;
    }

    private static boolean updateAttachedFiles(Connection conn, SystemBean inputBean, LoginBean loginBean, String uploadDir) {
        return deleteAttachedFiles(conn, inputBean, uploadDir) && insertAttachedFiles(conn, inputBean, loginBean, uploadDir);
    }

    private static boolean insertAttachedFiles(Connection conn, SystemBean inputBean, LoginBean loginBean, String uploadDir) {
        String sqlStmt = "INSERT INTO configured_system_file (configured_system_fk, file_fk) VALUES (?,?)";
        boolean ranOk = true;

        if (CommonMethods.cInt(inputBean.getConfiguredSystemPk()) <= -1) return false;
        if (inputBean.getFileList() == null || inputBean.getFileList().size() <= 0) return true; //No files to upload

        debugLog("SQL", "insertAttachedFiles", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getConfiguredSystemPk()));

            for (FormFile file : inputBean.getFileList()) {
                if (file.getFileSize() > 0) {
                    logger.debug("Saving: " + file.getFileName());
                    int newFilePk = FileModel.saveFile(conn, file, loginBean, uploadDir);
                    pStmt.setInt(2, newFilePk);
                    ranOk &= (newFilePk != -1 && pStmt.executeUpdate() == 1);
                }
            }
        } catch (Exception e) {
            debugLog("ERROR", "insertAttachedFiles", e, logger);
            ranOk = false;
        }

        return ranOk;
    }

    private static boolean deleteAttachedFiles(Connection conn, SystemBean inputBean, String uploadDir) {
        String sqlStmt = "DELETE FROM configured_system_file WHERE configured_system_fk = ? AND file_fk = ?";
        boolean ranOk = true;

        if (CommonMethods.cInt(inputBean.getConfiguredSystemPk()) <= -1) return false;
        if (inputBean.getDeleteFilePkArr() == null || inputBean.getDeleteFilePkArr().length <= 0) return true; //Nothing to delete

        debugLog("SQL", "deleteAttachedFiles", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getConfiguredSystemPk()));
            for (String filePk : inputBean.getDeleteFilePkArr()) {
                pStmt.setInt(2, CommonMethods.cInt(filePk));
                pStmt.addBatch();
            }

            for (int rsCnt : pStmt.executeBatch()) {
                ranOk &= rsCnt == 1;
            }
        } catch (Exception e) {
            debugLog("ERROR", "deleteAttachedFiles", e, logger);
            ranOk = false;
        } finally {
            if (ranOk) FileModel.deleteFiles(conn, uploadDir);
        }

        return ranOk;
    }

    private static HashMap<Integer, String> getCurrLocMap(Connection conn) {
        String sqlStmt = "SELECT configured_system_fk, location "
            + "FROM configured_system_loc_hist "
            + "ORDER BY location_date ASC, configured_system_loc_hist_pk ASC";
         HashMap<Integer, String> resultMap = new  HashMap<Integer, String>();

        debugLog("SQL", "getCurrLocMap", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) resultMap.put(rs.getInt(1), rs.getString(2));
        } catch (Exception e) {
            debugLog("ERROR", "getCurrLocMap", e, logger);
        }

        return resultMap;
    }

    private static ArrayList<SystemLocationBean> getLocHistList(Connection conn, int configuredSystemPk) {
        String sqlStmt = "SELECT location, strftime('%m/%d/%Y', location_date) AS location_date_fmt, reason, created_by, "
            + "strftime('%m/%d/%Y %H:%M:%S', created_date) AS created_date_fmt "
            + "FROM configured_system_loc_hist "
            + "WHERE configured_system_fk = ? "
            + "ORDER BY location_date DESC, configured_system_loc_hist_pk DESC";
        ArrayList<SystemLocationBean> resultList = new ArrayList<SystemLocationBean>();

        debugLog("SQL", "getLocHistList", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, configuredSystemPk);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                SystemLocationBean resultBean = new SystemLocationBean();
                resultBean.setLocation(rs.getString("location"));
                resultBean.setLocationDate(rs.getString("location_date_fmt"));
                resultBean.setReason(rs.getString("reason"));
                resultBean.setCreatedBy(rs.getString("created_by"));
                resultBean.setCreatedDate(rs.getString("created_date_fmt"));
                resultList.add(resultBean);
            }
        } catch (Exception e) {
            debugLog("ERROR", "getLocHistList", e, logger);
        }

        return resultList;
    }

    private static boolean insertLocHist(Connection conn, SystemBean inputBean, LoginBean loginBean) {
        String sqlStmt = "INSERT INTO configured_system_loc_hist "
            + "(configured_system_fk, location, location_date, reason, created_by, created_date) "
            + "VALUES (?,?,?,?,?,?)";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getConfiguredSystemPk()) <= -1) return false;
        if (CommonMethods.isEmpty(inputBean.getLocation())) return true; //Nothing to insert

        debugLog("SQL", "insertLocHist", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getConfiguredSystemPk()));
            pStmt.setString(2, inputBean.getLocation());
            CommonMethods.setDate(pStmt, 3, inputBean.getLocationDate());
            CommonMethods.setString(pStmt, 4, inputBean.getReason());
            pStmt.setString(5, loginBean.getFullName()); //Created By
            pStmt.setString(6, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS")); //Created Date
            ranOk = pStmt.executeUpdate() == 1;
        } catch (Exception e) {
            debugLog("ERROR", "insertLocHist", e, logger);
            ranOk = false;
        }

        return ranOk;
    }

    public static String getFacetName(Connection conn, int shipPk) {
        String sqlStmt = "SELECT computer_name "
            + "FROM configured_system_vw "
            + "WHERE ship_pk = ? "
            + "AND computer_name IS NOT NULL "
            + "ORDER BY computer_name";
        StringBuffer resultStr = new StringBuffer();

        if (shipPk <= -1) return null;

        debugLog("SQL", "getFacetName", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, shipPk);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                resultStr.append((resultStr.length() > 0 ? ", " : "") + rs.getString("computer_name"));
            }
        } catch (Exception e) {
            debugLog("ERROR", "getFacetName", e, logger);
        }

        return resultStr.toString();
    }

    public static HashMap<String, String> getFacetNameMap(Connection conn) {
        String sqlStmt = "SELECT ship_pk, computer_name "
            + "FROM configured_system_vw "
            + "WHERE ship_pk IS NOT NULL "
            + "AND computer_name IS NOT NULL "
            + "ORDER BY ship_pk, computer_name";
        HashMap<String, String> resultMap = new HashMap<String, String>();
        String shipPk = null;
        StringBuffer resultStr = new StringBuffer();

        debugLog("SQL", "getFacetNameMap", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                if (!CommonMethods.nes(shipPk).equals(rs.getString("ship_pk"))) {
                    if (!CommonMethods.isEmpty(shipPk)) resultMap.put(shipPk, resultStr.toString());
                    shipPk = rs.getString("ship_pk");
                    resultStr.setLength(0);
                }
                resultStr.append((resultStr.length() > 0 ? ", " : "") + rs.getString("computer_name"));
            }

            if (!CommonMethods.isEmpty(shipPk)) resultMap.put(shipPk, resultStr.toString());
        } catch (Exception e) {
            debugLog("ERROR", "getFacetNameMap", e, logger);
        }
        return resultMap;
    }

    public static String getCurrRecordStr(ArrayList<SystemBean> currList, int configuredSystemPk) {
        String returnStr = null;
        int i = 0;
        while (i < currList.size() && returnStr == null) {
            if (CommonMethods.cInt(((SystemBean)currList.get(i)).getConfiguredSystemPk()) == configuredSystemPk) {
                returnStr = "Record " + (i + 1) + " of " + currList.size();
            }
            i++;
        }
        return returnStr;
    }

    public static SystemBean getPrevBean(ArrayList<SystemBean> currList, int configuredSystemPk) {
        SystemBean resultBean = null;
        int i = 0;
        while (i < currList.size() && resultBean == null) {
            if (CommonMethods.cInt(((SystemBean)currList.get(i)).getConfiguredSystemPk()) == configuredSystemPk && i-1 >= 0) resultBean = currList.get(i-1);
            i++;
        }
        return resultBean;
    }

    public static SystemBean getNextBean(ArrayList<SystemBean> currList, int configuredSystemPk) {
        SystemBean resultBean = null;
        int i = 0;
        while (i < currList.size() && resultBean == null) {
            if (CommonMethods.cInt(((SystemBean)currList.get(i)).getConfiguredSystemPk()) == configuredSystemPk && i+1 < currList.size()) resultBean = currList.get(i+1);
            i++;
        }
        return resultBean;
    }

    public static ArrayList<String> getDmsVersionList() {
        ArrayList<String> resultList = new ArrayList<String>();
        int currYear = CommonMethods.cInt(CommonMethods.getDate("YYYY"));
        int currMonth = CommonMethods.cInt(CommonMethods.getDate("MM"))-1;

        for (int year = currYear; year >= 2013; year--) {
            for (int month = (year == currYear ? currMonth-1 : 11); month >= 0; month--) {
                resultList.add(CommonMethods.getMonthName(month) + " " + year);
            }
        }

        return resultList;
    }

    public static String getCurrDmsVersion() {
        return CommonMethods.getDate("Month YYYY", -1*CommonMethods.cInt(CommonMethods.getDate("DD")));
    }

    public static HashMap<String, String> getS2ClosureShipPkMap(Connection conn) {
        String sqlStmt = "SELECT DISTINCT ship_pk, s2_closure_date_fmt FROM configured_system_vw WHERE s2_closure_date > DATE('now', '-10 hours') ORDER BY ship_pk";
        HashMap<String, String> resultMap = new HashMap<String, String>();

        debugLog("SQL", "getS2ClosureShipPkMap", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                resultMap.put(rs.getString("ship_pk"), rs.getString("s2_closure_date_fmt"));
            }
        } catch (Exception e) {
            debugLog("ERROR", "getS2ClosureShipPkMap", e, logger);
        }

        return resultMap;
    }

    public static HashMap<String, String> getFuelClosureShipPkMap(Connection conn) {
        String sqlStmt = "SELECT DISTINCT ship_pk, fuel_closure_date_fmt FROM configured_system_vw WHERE fuel_closure_date > DATE('now', '-10 hours') ORDER BY ship_pk";
        HashMap<String, String> resultMap = new HashMap<String, String>();

        debugLog("SQL", "getFuelClosureShipPkMap", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                resultMap.put(rs.getString("ship_pk"), rs.getString("fuel_closure_date_fmt"));
            }
        } catch (Exception e) {
            debugLog("ERROR", "getFuelClosureShipPkMap", e, logger);
        }

        return resultMap;
    }

    public static HashMap<String, String> getContractNumberMap(Connection conn) {
        String sqlStmt = "SELECT DISTINCT ship_pk, contract_number FROM configured_system_vw WHERE ship_pk IS NOT NULL AND contract_number IS NOT NULL";
        debugLog("SQL", "getContractNumberMap", sqlStmt, logger);

        HashMap<String, String> resultMap = new HashMap<String, String>();
        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                resultMap.put(rs.getString("ship_pk"), rs.getString("contract_number"));
            }
        } catch (Exception e) {
            debugLog("ERROR", "getContractNumberMap", e, logger);
        }

        return resultMap;
    }

    //private static boolean insertSystemVariables(Connection conn) {
    //    String sqlStmt = "INSERT INTO system_variables (system_variables_pk) VALUES (1)";
    //    boolean ranOk = false;
    //
    //    debugLog("SQL", "insertSystemVariables", sqlStmt, logger);
    //
    //    try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
    //        ranOk = pStmt.executeUpdate() == 1;
    //    } catch (Exception e) {
    //        debugLog("ERROR", "insertSystemVariables", e, logger);
    //        ranOk = false;
    //    }
    //
    //    return ranOk;
    //}

    public static boolean updateSystemVariables(Connection conn, SystemBean inputBean, LoginBean loginBean) {
        String sqlStmt = "UPDATE system_variables SET facet_version = ?, last_updated_by = ?, last_updated_date = ?";
        boolean ranOk = false;

        debugLog("SQL", "updateSystemVariables", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            CommonMethods.setString(pStmt, 1, inputBean.getFacetVersion());
            pStmt.setString(2, loginBean.getFullName());
            pStmt.setString(3, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            ranOk = pStmt.executeUpdate() == 1;
        } catch (Exception e) {
            debugLog("ERROR", "updateSystemVariables", e, logger);
            ranOk = false;
        }
        return ranOk;
    }
}
