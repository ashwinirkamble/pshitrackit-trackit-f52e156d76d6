package com.premiersolutionshi.old.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.premiersolutionshi.common.util.DateUtils;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.bean.ShipBean;
import com.premiersolutionshi.old.bean.UserBean;
import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Business logic for the application's SHIP module
 */
public class ShipModel extends BaseModel {
    private static final String BASE_SHIP_POC_COLUMNS = "poc_pk, last_name, first_name, title, rank, dept, email, alt_email, "
        + "work_number, fax_number, cell_number, "
        + "CASE WHEN is_primary_poc THEN 'Y' ELSE 'N' END AS is_primary_poc ";
    private static final String BASE_SHIP_POC_SELECT = "SELECT "
        + BASE_SHIP_POC_COLUMNS
        + "FROM poc p INNER JOIN ship s ON ship_pk = ship_fk ";
    private static final String TITLE_RANK_ORDER = "CASE WHEN title='SUPPO' THEN 0 "
        + "WHEN title='ASUPPO' THEN 1 WHEN rank='LSCM' THEN 2 WHEN rank='LSCS' THEN 3 "
        + "WHEN rank='LSC' THEN 4 WHEN rank='LS1' THEN 5 WHEN rank='LS2' THEN 6 WHEN rank='LS3' THEN 7 "
        + "WHEN rank='LSSN' THEN 8 WHEN rank='LSSA' THEN 9 WHEN rank='CSCM' THEN 10 WHEN title='FSO' THEN 11 "
        + "WHEN rank='CSCS' THEN 12 WHEN rank='CSC' THEN 13 WHEN rank='CS1' THEN 14 WHEN rank='CS2' THEN 15 "
        + "WHEN rank='CS3' THEN 16 WHEN rank='CSSN' THEN 17 WHEN rank='CSSA' THEN 18 ELSE 19 END";
    private static Logger logger = Logger.getLogger(ShipModel.class.getSimpleName());

    public static ArrayList<String> getTypeList(Connection conn) {
        ArrayList<String> strList = getStrList(conn, "SELECT DISTINCT type FROM ship WHERE type IS NOT NULL ORDER BY type", logger);
        Set<String> shipTypeSet = new HashSet<String>();
        shipTypeSet.add("AS");
        shipTypeSet.add("ATG");
        shipTypeSet.add("CG");
        shipTypeSet.add("CVN");
        shipTypeSet.add("DDG");
        shipTypeSet.add("FFG");
        shipTypeSet.add("LCC");
        shipTypeSet.add("LCS");
        shipTypeSet.add("LHA");
        shipTypeSet.add("LHD");
        shipTypeSet.add("LPD");
        shipTypeSet.add("LSD");
        shipTypeSet.add("MCM");
        shipTypeSet.add("SSBN");
        shipTypeSet.add("SSGN");
        shipTypeSet.add("SSN");
        shipTypeSet.add("CBMU");
        shipTypeSet.add("CRG");
        shipTypeSet.add("EODG");
        shipTypeSet.add("EODMU");
        shipTypeSet.add("MDSU");
        shipTypeSet.add("NCG");
        shipTypeSet.add("NCR");
        shipTypeSet.add("NMCB");
        shipTypeSet.add("UCT");
        shipTypeSet.addAll(strList);
        strList = new ArrayList<>(shipTypeSet);
        Collections.sort(strList);
        return strList;
    }

    public static ArrayList<String> getHomeportList(Connection conn) {
        /*
         * Bangor, WA Bremerton, WA Diego Garcia Everett, WA Groton, CT Guam
         * Kings Bay, GA Little Creek, VA Manama, Bahrain Mayport, FL Norfolk,
         * VA Pearl Harbor, HI Portsmouth, NH San Diego, CA Sasebo, Japan
         * Yokosuka, Japan
         */
        return getStrList(conn, "SELECT DISTINCT homeport FROM ship WHERE homeport IS NOT NULL ORDER BY homeport", logger);
    }

    public static ArrayList<String> getTycomList(Connection conn) {
        return getStrList(conn, "SELECT DISTINCT tycom FROM ship WHERE tycom IS NOT NULL ORDER BY tycom", logger);
    }

    public static ArrayList<String> getRsupplyList(Connection conn) {
        return getStrList(conn, "SELECT DISTINCT rsupply FROM ship WHERE rsupply IS NOT NULL ORDER BY rsupply", logger);
    }

    public static ArrayList<ShipBean> getShipList(Connection conn) {
        return getShipList(conn, "ALL");
    }

    public static ArrayList<ShipBean> getShipList(Connection conn, String type) {
        String sqlStmt = null;

        if (CommonMethods.nes(type).equals("FACET")) {
            sqlStmt = "SELECT DISTINCT ship_pk, uic, ship_name, type, hull, service_code, homeport, tycom, tycom_display, rsupply "
                    + "FROM configured_system_vw " + "WHERE ship_pk IS NOT NULL " + "ORDER BY ship_name";
        }
        else { // ALL
            sqlStmt = "SELECT ship_pk, uic, ship_name, type, hull, service_code, homeport, tycom, tycom_display, rsupply FROM ship_vw ORDER BY ship_name";
        }

        ArrayList<ShipBean> resultList = new ArrayList<ShipBean>();

        debugLog("SQL", "getShipList", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                ShipBean resultBean = new ShipBean();
                resultBean.setShipPk(rs.getString("ship_pk"));
                resultBean.setUic(rs.getString("uic"));
                resultBean.setShipName(rs.getString("ship_name"));
                resultBean.setType(rs.getString("type"));
                resultBean.setHull(rs.getString("hull"));
                resultBean.setShipNameTypeHull(
                        rs.getString("ship_name") + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG")
                                ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")"
                                : ""));
                resultBean.setServiceCode(rs.getString("service_code"));
                resultBean.setHomeport(rs.getString("homeport"));
                resultBean.setTycom(rs.getString("tycom"));
                resultBean.setTycomDisplay(rs.getString("tycom_display"));
                resultBean.setRsupply(rs.getString("rsupply"));
                resultList.add(resultBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getShipList", e, logger);
        }

        return resultList;
    }

    public static ShipBean getShipBean(Connection conn, int shipPk) {
        return getShipBean(conn, shipPk, null);
    }

    public static ShipBean getShipBean(Connection conn, String uic) {
        return getShipBean(conn, -1, uic);
    }

    public static ShipBean getShipBean(Connection conn, int shipPk, String uic) {
        String sqlStmt = null;
        ShipBean resultBean = new ShipBean();

        if (shipPk <= -1 && CommonMethods.isEmpty(uic))
            return null;

        if (shipPk >= 0)
            sqlStmt = "SELECT ship_pk, uic, ship_name, type, hull, service_code, homeport, tycom, tycom_display, rsupply FROM ship_vw WHERE ship_pk = ?";
        else if (!CommonMethods.isEmpty(uic))
            sqlStmt = "SELECT ship_pk, uic, ship_name, type, hull, service_code, homeport, tycom, tycom_display, rsupply FROM ship_vw WHERE uic = ?";

        debugLog("SQL", "getShipBean", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            if (shipPk >= 0)
                pStmt.setInt(1, shipPk);
            else if (!CommonMethods.isEmpty(uic))
                pStmt.setString(1, uic);

            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                resultBean.setShipPk(rs.getString("ship_pk"));
                resultBean.setUic(rs.getString("uic"));
                resultBean.setShipName(rs.getString("ship_name"));
                resultBean.setShipNameTypeHull(
                        rs.getString("ship_name") + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG")
                                ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")"
                                : ""));
                resultBean.setType(rs.getString("type"));
                resultBean.setHull(rs.getString("hull"));
                resultBean.setServiceCode(rs.getString("service_code"));
                resultBean.setHomeport(rs.getString("homeport"));
                resultBean.setTycom(rs.getString("tycom"));
                resultBean.setTycomDisplay(rs.getString("tycom_display"));
                resultBean.setRsupply(rs.getString("rsupply"));
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getShipBean", e, logger);
        }

        return resultBean;
    }

    public static String getShipName(Connection conn, String uic) {
        String sqlStmt = "SELECT ship_name, type, hull FROM ship_vw WHERE uic = ?";
        String resultStr = null;

        if (CommonMethods.isEmpty(uic))
            return null;

        debugLog("SQL", "getShipName", sqlStmt + " (" + uic + ")", logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setString(1, uic);
            ResultSet rs = pStmt.executeQuery();
            if (rs.next())
                resultStr = rs.getString("ship_name") + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG")
                        ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")"
                        : "");
        }
        catch (Exception e) {
            debugLog("ERROR", "getShipName", e, logger);
        }

        return CommonMethods.nes(resultStr);
    }

    public static String getShipName(Connection conn, int shipPk) {
        String sqlStmt = "SELECT ship_name, type, hull FROM ship_vw WHERE ship_pk = ?";
        String resultStr = null;

        if (shipPk <= -1)
            return null;

        debugLog("SQL", "getShipName", sqlStmt + " (" + shipPk + ")", logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, shipPk);
            ResultSet rs = pStmt.executeQuery();
            if (rs.next())
                resultStr = rs.getString("ship_name") + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG")
                        ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")"
                        : "");
        }
        catch (Exception e) {
            debugLog("ERROR", "getShipName", e, logger);
        }

        return CommonMethods.nes(resultStr);
    }

    public static boolean insertShip(Connection conn, ShipBean inputBean, LoginBean loginBean) {
        String sqlStmt = "INSERT INTO ship (uic, ship_name, type, hull, service_code, homeport, tycom, rsupply) VALUES (?,?,?,?,?,?,?,?)";
        boolean ranOk = false;

        debugLog("SQL", "insertShip", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setString(1, inputBean.getUic());
            pStmt.setString(2, inputBean.getShipName());
            CommonMethods.setString(pStmt, 3, inputBean.getType());
            CommonMethods.setString(pStmt, 4, inputBean.getHull());
            pStmt.setString(5, inputBean.getServiceCode());
            CommonMethods.setString(pStmt, 6, inputBean.getHomeport());
            CommonMethods.setString(pStmt, 7, inputBean.getTycom());
            CommonMethods.setString(pStmt, 8, inputBean.getRsupply());
            ranOk = (pStmt.executeUpdate() == 1);
        }
        catch (Exception e) {
            debugLog("ERROR", "insertShip", e, logger);
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

    protected static String getShipUic(Connection conn, int shipPk) {
        String sqlStmt = "SELECT uic FROM ship_vw WHERE ship_pk = ?";
        String resultStr = null;
        if (shipPk <= -1) {
            return null;
        }
        debugLog("SQL", "getShipUic", sqlStmt + " (" + shipPk + ")", logger);
        try {
            PreparedStatement pStmt = conn.prepareStatement(sqlStmt);
            Throwable localThrowable3 = null;
            try {
                pStmt.setInt(1, shipPk);
                ResultSet rs = pStmt.executeQuery();
                if (rs.next()) {
                    resultStr = rs.getString("uic");
                }
            }
            catch (Throwable localThrowable1) {
                localThrowable3 = localThrowable1;
                throw localThrowable1;
            }
            finally {
                if (pStmt != null) {
                    if (localThrowable3 != null) {
                        try {
                            pStmt.close();
                        }
                        catch (Throwable localThrowable2) {
                            localThrowable3.addSuppressed(localThrowable2);
                        }
                    }
                    else {
                        pStmt.close();
                    }
                }
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getShipUic", e, logger);
        }
        return CommonMethods.nes(resultStr);
    }

    public static boolean updateShip(Connection conn, ShipBean inputBean, LoginBean loginBean) {
        String sqlStmt = "UPDATE ship SET uic = ?, ship_name = ?, type = ?, hull = ?, service_code = ?, homeport = ?, tycom = ?, rsupply = ? WHERE ship_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getShipPk()) <= -1)
            return false;

        debugLog("SQL", "updateShip", sqlStmt + " (shipPk = " + inputBean.getShipPk() + ")", logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setString(1, inputBean.getUic());
            pStmt.setString(2, inputBean.getShipName());
            CommonMethods.setString(pStmt, 3, inputBean.getType());
            CommonMethods.setString(pStmt, 4, inputBean.getHull());
            pStmt.setString(5, inputBean.getServiceCode());
            CommonMethods.setString(pStmt, 6, inputBean.getHomeport());
            CommonMethods.setString(pStmt, 7, inputBean.getTycom());
            CommonMethods.setString(pStmt, 8, inputBean.getRsupply());
            pStmt.setInt(9, CommonMethods.cInt(inputBean.getShipPk()));
            ranOk = (pStmt.executeUpdate() == 1);
        }
        catch (Exception e) {
            debugLog("ERROR", "updateShip", e, logger);
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

    public static boolean deleteShip(Connection conn, ShipBean inputBean) {
        String sqlStmt = "DELETE FROM ship WHERE ship_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getShipPk()) <= -1)
            return false;

        debugLog("SQL", "deleteShip", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getShipPk()));
            ranOk = (pStmt.executeUpdate() == 1);
        }
        catch (Exception e) {
            debugLog("ERROR", "deleteShip", e, logger);
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

    public static ArrayList<ShipBean> getAllShipPocList(Connection conn) {
        return getAllShipPocList(conn, new UserBean());
    }

    public static ArrayList<ShipBean> getAllShipPocList(Connection conn, UserBean inputBean) {
        StringBuffer sqlStmt = new StringBuffer(
            "SELECT ship_fk, uic, ship_name, type, hull, "
            + "poc_pk, last_name, first_name, rank, title, dept, "
            + "email, alt_email, work_number, fax_number, cell_number, "
            + "CASE WHEN is_primary_poc THEN 'Y' ELSE 'N' END as is_primary_poc, "
            + "last_updated_by, last_updated_date "
            + "FROM poc p LEFT OUTER JOIN ship s ON ship_pk = p.ship_fk "
            + "WHERE ship_fk IS NOT NULL ");
        ArrayList<ShipBean> resultList = new ArrayList<ShipBean>();

        // BUILD WHERE
        if (!CommonMethods.isEmpty(inputBean.getLastName()))
            sqlStmt.append(" AND INSTR(LOWER(last_name), LOWER(?)) > 0");
        if (!CommonMethods.isEmpty(inputBean.getFirstName()))
            sqlStmt.append(" AND INSTR(LOWER(first_name), LOWER(?)) > 0");
        if (!CommonMethods.isEmpty(inputBean.getOrganization()))
            sqlStmt.append(" AND INSTR(LOWER(ship_name), LOWER(?)) > 0");

        // ORDER BY
        sqlStmt.append("  ORDER BY ship_name, last_updated_by DESC, "
                + TITLE_RANK_ORDER
                + ", last_name, first_name");

        debugLog("SQL", "getAllShipPocList", sqlStmt.toString(), logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            int i = 1;

            // SET WHERE VALUES
            if (!CommonMethods.isEmpty(inputBean.getLastName()))
                pStmt.setString(i++, inputBean.getLastName());
            if (!CommonMethods.isEmpty(inputBean.getFirstName()))
                pStmt.setString(i++, inputBean.getFirstName());
            if (!CommonMethods.isEmpty(inputBean.getOrganization()))
                pStmt.setString(i++, inputBean.getOrganization());

            ResultSet rs = pStmt.executeQuery();
            ShipBean parentBean = null;
            ArrayList<UserBean> childList = null;
            String currUic = null;

            while (rs.next()) {
                if (!rs.getString("uic").equals(CommonMethods.nes(currUic))) {
                    if (parentBean != null) {
                        parentBean.setPocList(childList);
                        resultList.add(parentBean);
                    }
                    currUic = rs.getString("uic");
                    parentBean = new ShipBean();
                    parentBean.setUic(rs.getString("uic"));
                    parentBean.setShipPk(rs.getString("ship_fk"));
                    parentBean.setShipName(rs.getString("ship_name"));
                    parentBean.setType(rs.getString("type"));
                    parentBean.setHull(rs.getString("hull"));
                    parentBean.setShipNameTypeHull(
                            rs.getString("ship_name") + (!CommonMethods.isEmpty(rs.getString("type")) && !rs.getString("type").equals("ATG")
                                    ? " (" + rs.getString("type") + " " + rs.getString("hull") + ")"
                                    : ""));
                    childList = new ArrayList<UserBean>();
                }

                UserBean resultBean = new UserBean();
                resultBean.setShipPocPk(rs.getString("poc_pk"));
                resultBean.setLastName(rs.getString("last_name"));
                resultBean.setFirstName(rs.getString("first_name"));
                resultBean.setRank(rs.getString("rank"));
                resultBean.setTitle(rs.getString("title"));
                resultBean.setDept(rs.getString("dept"));
                resultBean.setEmail(rs.getString("email"));
                resultBean.setAltEmail(rs.getString("alt_email"));
                resultBean.setWorkNumber(rs.getString("work_number"));
                resultBean.setFaxNumber(rs.getString("fax_number"));
                resultBean.setCellNumber(rs.getString("cell_number"));
                resultBean.setIsPrimaryPoc(rs.getString("is_primary_poc"));
                resultBean.setLastUpdatedBy(rs.getString("last_updated_by"));
                resultBean.setLastUpdatedDate(DateUtils.parseSqliteDatetime(rs.getString("last_updated_date")));
                childList.add(resultBean);
            }

            if (parentBean != null) {
                parentBean.setPocList(childList);
                resultList.add(parentBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getAllShipPocList", e, logger);
        }

        return resultList;
    }

    public static ArrayList<UserBean> getPocList(Connection conn, String uic) {
        String sqlStmt = BASE_SHIP_POC_SELECT
                + "AND s.uic = ? "
                + "ORDER BY last_updated_date DESC, "
                + TITLE_RANK_ORDER + ", last_name";
        ArrayList<UserBean> resultList = new ArrayList<UserBean>();

        if (CommonMethods.isEmpty(uic))
            return null;

        debugLog("SQL", "getPocList", sqlStmt + " (uic = " + uic + ")", logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setString(1, uic);

            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                UserBean resultBean = new UserBean();
                resultBean.setShipPocPk(rs.getString("poc_pk"));
                resultBean.setLastName(rs.getString("last_name"));
                resultBean.setFirstName(rs.getString("first_name"));
                resultBean.setTitle(rs.getString("title"));
                resultBean.setRank(rs.getString("rank"));
                resultBean.setDept(rs.getString("dept"));
                resultBean.setEmail(rs.getString("email"));
                resultBean.setAltEmail(rs.getString("alt_email"));
                resultBean.setWorkNumber(rs.getString("work_number"));
                resultBean.setFaxNumber(rs.getString("fax_number"));
                resultBean.setCellNumber(rs.getString("cell_number"));
                resultBean.setIsPrimaryPoc(rs.getString("is_primary_poc"));
                resultList.add(resultBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getPocList", e, logger);
        }

        return resultList;
    }

    public static HashMap<String, ArrayList<UserBean>> getPocMap(Connection conn) {
        String sqlStmt = "SELECT ship_fk, uic, ship_name, type, hull, "
                + "poc_pk, last_name, first_name, rank, title, dept, "
                + "email, alt_email, work_number, fax_number, cell_number, "
                + "CASE WHEN is_primary_poc THEN 'Y' ELSE 'N' END as is_primary_poc, "
                + "last_updated_by, last_updated_date "
                + "FROM poc p LEFT OUTER JOIN ship s ON ship_pk = p.ship_fk "
                + "WHERE ship_fk IS NOT NULL "
                + "ORDER BY " + TITLE_RANK_ORDER + ", last_name";
        HashMap<String, ArrayList<UserBean>> resultMap = new HashMap<String, ArrayList<UserBean>>();

        debugLog("SQL", "getPocMap", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                UserBean resultBean = new UserBean();
                resultBean.setShipPocPk(rs.getString("poc_pk"));
                resultBean.setLastName(rs.getString("last_name"));
                resultBean.setFirstName(rs.getString("first_name"));
                resultBean.setTitle(rs.getString("title"));
                resultBean.setRank(rs.getString("rank"));
                resultBean.setDept(rs.getString("dept"));
                resultBean.setEmail(rs.getString("email"));
                resultBean.setAltEmail(rs.getString("alt_email"));
                resultBean.setWorkNumber(rs.getString("work_number"));
                resultBean.setFaxNumber(rs.getString("fax_number"));
                resultBean.setCellNumber(rs.getString("cell_number"));
                resultBean.setIsPrimaryPoc(rs.getString("is_primary_poc"));
                if (resultMap.get(rs.getString("uic")) == null)
                    resultMap.put(rs.getString("uic"), new ArrayList<UserBean>());
                resultMap.get(rs.getString("uic")).add(resultBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getPocMap", e, logger);
        }

        return resultMap;
    }

    public static UserBean getPocBean(Connection conn, int pocPk) {
        String sqlStmt = "SELECT ship_fk, uic, ship_name, type, hull, "
                + "poc_pk, last_name, first_name, rank, title, dept, "
                + "email, alt_email, work_number, fax_number, cell_number, "
                + "CASE WHEN is_primary_poc THEN 'Y' ELSE 'N' END as is_primary_poc, "
                + "last_updated_by, last_updated_date "
                + "FROM poc p LEFT OUTER JOIN ship s ON ship_pk = p.ship_fk "
                + "WHERE ship_fk IS NOT NULL AND poc_pk = ?";
        UserBean resultBean = new UserBean();

        if (pocPk == -1)
            return null;

        debugLog("SQL", "getPocBean", sqlStmt + " (poc_pk = " + pocPk + ")", logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, pocPk);

            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                resultBean.setShipPocPk(rs.getString("poc_pk"));
                resultBean.setShipPk(rs.getString("ship_fk"));
                resultBean.setLastName(rs.getString("last_name"));
                resultBean.setFirstName(rs.getString("first_name"));
                resultBean.setTitle(rs.getString("title"));
                resultBean.setRank(rs.getString("rank"));
                resultBean.setDept(rs.getString("dept"));
                resultBean.setEmail(rs.getString("email"));
                resultBean.setAltEmail(rs.getString("alt_email"));
                resultBean.setWorkNumber(rs.getString("work_number"));
                resultBean.setFaxNumber(rs.getString("fax_number"));
                resultBean.setCellNumber(rs.getString("cell_number"));
                resultBean.setIsPrimaryPoc(rs.getString("is_primary_poc"));
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getPocBean", e, logger);
        }

        return resultBean;
    }
//
//    public static boolean insertPoc(Connection conn, UserBean inputBean, LoginBean loginBean) {
//        String sqlStmt = "INSERT INTO ship_poc (ship_fk, last_name, first_name, title, rank, dept, email, alt_email, work_number, fax_number, cell_number, is_primary_poc, last_updated_by, last_updated_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//        boolean ranOk = false;
//
//        if (CommonMethods.cInt(inputBean.getShipPk()) == -1)
//            return false;
//
//        debugLog("SQL", "insertPoc", sqlStmt, logger);
//
//        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
//            conn.setAutoCommit(false);
//            CommonMethods.setInt(pStmt, 1, inputBean.getShipPk());
//            CommonMethods.setString(pStmt, 2, inputBean.getLastName());
//            CommonMethods.setString(pStmt, 3, inputBean.getFirstName());
//            CommonMethods.setString(pStmt, 4, inputBean.getTitle());
//            CommonMethods.setString(pStmt, 5, inputBean.getRank());
//            CommonMethods.setString(pStmt, 6, inputBean.getDept());
//            CommonMethods.setString(pStmt, 7, inputBean.getEmail());
//            CommonMethods.setString(pStmt, 8, inputBean.getAltEmail());
//            CommonMethods.setString(pStmt, 9, inputBean.getWorkNumber());
//            CommonMethods.setString(pStmt, 10, inputBean.getFaxNumber());
//            CommonMethods.setString(pStmt, 11, inputBean.getCellNumber());
//            pStmt.setString(12, CommonMethods.nvl(inputBean.getIsPrimaryPoc(), "N"));
//            pStmt.setString(13, loginBean.getFullName());
//            pStmt.setString(14, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
//            ranOk = (pStmt.executeUpdate() == 1);
//        }
//        catch (Exception e) {
//            debugLog("ERROR", "insertPoc", e, logger);
//            ranOk = false;
//        }
//        finally {
//            try {
//                if (ranOk)
//                    conn.commit();
//                else
//                    conn.rollback();
//            }
//            catch (Exception e) {
//            }
//            try {
//                conn.setAutoCommit(true);
//            }
//            catch (Exception e) {
//            }
//        }
//
//        return ranOk;
//    }
//
//    public static boolean updatePoc(Connection conn, UserBean inputBean, LoginBean loginBean) {
//        String sqlStmt = "UPDATE ship_poc SET ship_fk = ?, last_name = ?, first_name = ?, title = ?, "
//                + "rank = ?, dept = ?, email = ?, alt_email = ?, work_number = ?, fax_number = ?, cell_number = ?, is_primary_poc = ?, last_updated_by = ?, last_updated_date = ? WHERE ship_poc_pk = ?";
//        boolean ranOk = false;
//
//        if (CommonMethods.cInt(inputBean.getShipPocPk()) == -1)
//            return false;
//
//        debugLog("SQL", "updatePoc", sqlStmt + " (ship_poc_pk = " + inputBean.getShipPocPk() + ")", logger);
//
//        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
//            conn.setAutoCommit(false);
//            CommonMethods.setInt(pStmt, 1, inputBean.getShipPk());
//            CommonMethods.setString(pStmt, 2, inputBean.getLastName());
//            CommonMethods.setString(pStmt, 3, inputBean.getFirstName());
//            CommonMethods.setString(pStmt, 4, inputBean.getTitle());
//            CommonMethods.setString(pStmt, 5, inputBean.getRank());
//            CommonMethods.setString(pStmt, 6, inputBean.getDept());
//            CommonMethods.setString(pStmt, 7, inputBean.getEmail());
//            CommonMethods.setString(pStmt, 8, inputBean.getAltEmail());
//            CommonMethods.setString(pStmt, 9, inputBean.getWorkNumber());
//            CommonMethods.setString(pStmt, 10, inputBean.getFaxNumber());
//            CommonMethods.setString(pStmt, 11, inputBean.getCellNumber());
//            pStmt.setString(12, CommonMethods.nvl(inputBean.getIsPrimaryPoc(), "N"));
//            pStmt.setString(13, loginBean.getFullName());
//            pStmt.setString(14, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
//            pStmt.setInt(15, CommonMethods.cInt(inputBean.getShipPocPk()));
//            ranOk = (pStmt.executeUpdate() == 1);
//        }
//        catch (Exception e) {
//            debugLog("ERROR", "updatePoc", e, logger);
//            ranOk = false;
//        }
//        finally {
//            try {
//                if (ranOk)
//                    conn.commit();
//                else
//                    conn.rollback();
//            }
//            catch (Exception e) {
//            }
//            try {
//                conn.setAutoCommit(true);
//            }
//            catch (Exception e) {
//            }
//        }
//
//        return ranOk;
//    }
//
//    public static boolean deletePoc(Connection conn, UserBean inputBean) {
//        String sqlStmt = "DELETE FROM ship_poc WHERE ship_poc_pk = ?";
//        boolean ranOk = false;
//
//        if (CommonMethods.cInt(inputBean.getShipPocPk()) <= -1)
//            return false;
//
//        debugLog("SQL", "deletePoc", sqlStmt + " (ship_poc_pk = " + inputBean.getShipPocPk() + ")", logger);
//
//        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
//            conn.setAutoCommit(false);
//            pStmt.setInt(1, CommonMethods.cInt(inputBean.getShipPocPk()));
//            ranOk = (pStmt.executeUpdate() == 1);
//        }
//        catch (Exception e) {
//            debugLog("ERROR", "deletePoc", e, logger);
//        }
//        finally {
//            try {
//                if (ranOk)
//                    conn.commit();
//                else
//                    conn.rollback();
//            }
//            catch (Exception e) {
//            }
//            try {
//                conn.setAutoCommit(true);
//            }
//            catch (Exception e) {
//            }
//        }
//        return ranOk;
//    }

    public static String getHomeport(Connection conn, int shipPk, String uic) {
        String sqlStmt = null;
        String returnStr = null;

        if (shipPk <= -1 && CommonMethods.isEmpty(uic))
            return null;

        if (shipPk >= 0)
            sqlStmt = "SELECT homeport FROM ship_vw WHERE ship_pk = ?";
        else if (!CommonMethods.isEmpty(uic))
            sqlStmt = "SELECT homeport FROM ship_vw WHERE uic = ?";

        debugLog("SQL", "getHomeport", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            if (shipPk >= 0)
                pStmt.setInt(1, shipPk);
            else if (!CommonMethods.isEmpty(uic))
                pStmt.setString(1, uic);

            ResultSet rs = pStmt.executeQuery();
            if (rs.next())
                returnStr = rs.getString("homeport");
        }
        catch (Exception e) {
            debugLog("ERROR", "getHomeport", e, logger);
        }

        return returnStr;
    }

    public static String getPrimaryPocEmails(ArrayList<UserBean> pocList) {
        StringBuffer resultStr = new StringBuffer();
        if (pocList == null)
            return null;
        for (UserBean pocBean : pocList) {
            if (CommonMethods.nes(pocBean.getIsPrimaryPoc()).equals("Y")) {
                if (resultStr.length() > 0)
                    resultStr.append(";");
                resultStr.append(pocBean.getEmail());
            }
        }
        return resultStr.toString();
    }

    public static String getPocEmails(ArrayList<UserBean> pocList) {
        StringBuffer resultStr = new StringBuffer();
        if (pocList == null)
            return null;
        for (UserBean pocBean : pocList) {
            if (resultStr.length() > 0)
                resultStr.append(";");
            resultStr.append(pocBean.getEmail());
        }
        return resultStr.toString();
    }
}
