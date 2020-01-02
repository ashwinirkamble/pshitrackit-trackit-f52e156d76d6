package com.premiersolutionshi.old.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.premiersolutionshi.common.util.DateUtils;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.bean.OrganizationBean;
import com.premiersolutionshi.old.bean.UserBean;
import com.premiersolutionshi.old.util.CommonMethods;
/**
 * Business logic for the application's POC module
 */
public class PocModel {
    private static Logger logger = Logger.getLogger(PocModel.class.getSimpleName());

    private static void debugLog(String type, String functionName, Exception e) {
        debugLog(type, functionName, e.toString());
    }

    private static void debugLog(String type, String functionName, String statement) {
        if (type.equals("INFO") || type.equals("SQL")) {
            logger.info(String.format("%13s%-30s | %-34s | %s", "", type, functionName, statement));
        } else if (type.equals("ERROR")) {
            logger.error(String.format("%13s%-30s | %-34s | %s", "", type, functionName, statement));
        } else {
            logger.debug(String.format("%13s%-30s | %-34s | %s", "", type, functionName, statement));
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

    public static ArrayList<String> getOrganizationList(Connection conn) {
        return getStrList(conn, "SELECT DISTINCT organization FROM poc WHERE organization IS NOT NULL ORDER BY organization DESC");
    }

    public static ArrayList<OrganizationBean> getPocList(Connection conn, int projectPk, UserBean inputBean) {
        StringBuffer sqlStmt = new StringBuffer("SELECT poc_pk, last_name, first_name, organization, title, rank, email, work_number, fax_number, "
                + "cell_number, last_updated_by, last_updated_date "
                + "FROM poc WHERE project_fk = ?");
        ArrayList<OrganizationBean> resultList = new ArrayList<OrganizationBean>();

        if (projectPk == -1) return null;

        //BUILD WHERE
        if (!CommonMethods.isEmpty(inputBean.getLastName()))     sqlStmt.append(" AND INSTR(LOWER(last_name), LOWER(?)) > 0");
        if (!CommonMethods.isEmpty(inputBean.getFirstName()))    sqlStmt.append(" AND INSTR(LOWER(first_name), LOWER(?)) > 0");
        if (!CommonMethods.isEmpty(inputBean.getOrganization())) sqlStmt.append(" AND INSTR(LOWER(organization), LOWER(?)) > 0");

        //ORDER BY
      sqlStmt.append(" ORDER BY organization, last_updated_date DESC, last_name, first_name");

        debugLog("SQL", "getPocList", sqlStmt.toString());

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            int i = 1;
            pStmt.setInt(i++, projectPk);

            //SET WHERE VALUES
            if (!CommonMethods.isEmpty(inputBean.getLastName())) pStmt.setString(i++, inputBean.getLastName());
            if (!CommonMethods.isEmpty(inputBean.getFirstName())) pStmt.setString(i++, inputBean.getFirstName());
            if (!CommonMethods.isEmpty(inputBean.getOrganization())) pStmt.setString(i++, inputBean.getOrganization());

            ResultSet rs = pStmt.executeQuery();

            OrganizationBean parentBean = null;
            ArrayList<UserBean> childList = null;
            String currOrganization = null;

            while (rs.next()) {
                if (!rs.getString("organization").equals(CommonMethods.nes(currOrganization))) {
                    if (parentBean != null) {
                        parentBean.setPocList(childList);
                        resultList.add(parentBean);
                    }
                    currOrganization = rs.getString("organization");
                    parentBean = new OrganizationBean(currOrganization);
                    childList = new ArrayList<UserBean>();
                }

                UserBean resultBean = new UserBean();
                resultBean.setPocPk(rs.getString("poc_pk"));
                resultBean.setLastName(rs.getString("last_name"));
                resultBean.setFirstName(rs.getString("first_name"));
                resultBean.setOrganization(rs.getString("organization"));
                resultBean.setTitle(rs.getString("title"));
                resultBean.setRank(rs.getString("rank"));
                resultBean.setEmail(rs.getString("email"));
                resultBean.setWorkNumber(rs.getString("work_number"));
                resultBean.setFaxNumber(rs.getString("fax_number"));
                resultBean.setCellNumber(rs.getString("cell_number"));
                resultBean.setLastUpdatedBy(rs.getString("last_updated_by"));
                resultBean.setLastUpdatedDate(DateUtils.parseSqliteDatetime(rs.getString("last_updated_date")));
                childList.add(resultBean);
            }

            if (parentBean != null) {
                parentBean.setPocList(childList);
                resultList.add(parentBean);
            }
        } catch (Exception e) {
            debugLog("ERROR", "getPocList", e);
        }

        return resultList;
    }

    public static UserBean getPocBean(Connection conn, int pocPk) {
        String sqlStmt = "SELECT poc_pk, project_fk, last_name, first_name, organization, title, rank, email, work_number, fax_number, "
                + "cell_number, last_updated_by, last_updated_date "
                + "FROM poc WHERE poc_pk = ?";
        UserBean resultBean = new UserBean();

        if (pocPk <= -1) return null;

        debugLog("SQL", "getPocBean", sqlStmt + " (poc_pk = " + pocPk + ")");

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, pocPk);

            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                resultBean.setPocPk(rs.getString("poc_pk"));
                resultBean.setProjectPk(rs.getString("project_fk"));
                resultBean.setLastName(rs.getString("last_name"));
                resultBean.setFirstName(rs.getString("first_name"));
                resultBean.setOrganization(rs.getString("organization"));
                resultBean.setCurrOrganization(rs.getString("organization")); //For updating purposes
                resultBean.setTitle(rs.getString("title"));
                resultBean.setRank(rs.getString("rank"));
                resultBean.setEmail(rs.getString("email"));
                resultBean.setWorkNumber(rs.getString("work_number"));
                resultBean.setFaxNumber(rs.getString("fax_number"));
                resultBean.setCellNumber(rs.getString("cell_number"));
                resultBean.setLastUpdatedBy(rs.getString("last_updated_by"));
                resultBean.setLastUpdatedDate(DateUtils.parseSqliteDatetime(rs.getString("last_updated_date")));
            }
        } catch (Exception e) {
            debugLog("ERROR", "getPocBean", e);
        }

        return resultBean;
    }

    public static boolean insertPoc(Connection conn, UserBean inputBean, LoginBean loginBean) {
        String sqlStmt = "INSERT INTO poc (project_fk, last_name, first_name, organization, title, rank, email, work_number, fax_number, cell_number, last_updated_by, last_updated_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getProjectPk()) == -1) return false;

        debugLog("SQL", "insertPoc", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getProjectPk()));
            CommonMethods.setString(pStmt, 2, inputBean.getLastName());
            CommonMethods.setString(pStmt, 3, inputBean.getFirstName());
            CommonMethods.setString(pStmt, 4, inputBean.getOrganization());
            CommonMethods.setString(pStmt, 5, inputBean.getTitle());
            CommonMethods.setString(pStmt, 6, inputBean.getRank());
            CommonMethods.setString(pStmt, 7, inputBean.getEmail());
            CommonMethods.setString(pStmt, 8, inputBean.getWorkNumber());
            CommonMethods.setString(pStmt, 9, inputBean.getFaxNumber());
            CommonMethods.setString(pStmt, 10, inputBean.getCellNumber());
            pStmt.setString(11, loginBean.getFullName());
            pStmt.setString(12, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            ranOk = (pStmt.executeUpdate() == 1);
        } catch (Exception e) {
            debugLog("ERROR", "insertPoc", e);
            ranOk = false;
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }

        return ranOk;
    }

    public static boolean updatePoc(Connection conn, UserBean inputBean, LoginBean loginBean) {
        String sqlStmt = "UPDATE poc SET last_name = ?, first_name = ?, organization = ?, title = ?, rank = ?, email = ?, work_number = ?, fax_number = ?, cell_number = ?, last_updated_by = ?, last_updated_date = ? WHERE poc_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getPocPk()) == -1) return false;

        debugLog("SQL", "updatePoc", sqlStmt + " (poc_pk = " + inputBean.getPocPk() + ")");

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            CommonMethods.setString(pStmt, 1, inputBean.getLastName());
            CommonMethods.setString(pStmt, 2, inputBean.getFirstName());
            CommonMethods.setString(pStmt, 3, inputBean.getOrganization());
            CommonMethods.setString(pStmt, 4, inputBean.getTitle());
            CommonMethods.setString(pStmt, 5, inputBean.getRank());
            CommonMethods.setString(pStmt, 6, inputBean.getEmail());
            CommonMethods.setString(pStmt, 7, inputBean.getWorkNumber());
            CommonMethods.setString(pStmt, 8, inputBean.getFaxNumber());
            CommonMethods.setString(pStmt, 9, inputBean.getCellNumber());
            pStmt.setString(10, loginBean.getFullName());
            pStmt.setString(11, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            pStmt.setInt(12, CommonMethods.cInt(inputBean.getPocPk()));
            ranOk = (pStmt.executeUpdate() == 1);
        } catch (Exception e) {
            debugLog("ERROR", "updatePoc", e);
            ranOk = false;
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }

        return ranOk;
    }

    public static boolean deletePoc(Connection conn, UserBean inputBean) {
        String sqlStmt = "DELETE FROM poc WHERE poc_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getPocPk()) <= -1) return false;

        debugLog("SQL", "deletePoc", sqlStmt + " (poc_pk = " + inputBean.getPocPk() + ")");

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getPocPk()));
            ranOk = (pStmt.executeUpdate() == 1);
        } catch (Exception e) {
            debugLog("ERROR", "deletePoc", e);
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }

        return ranOk;
    }
}
