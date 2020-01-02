package com.premiersolutionshi.old.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.premiersolutionshi.common.constant.ManagedList;
import com.premiersolutionshi.old.bean.GitLogBean;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.bean.LookupBean;
import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Business logic for the application's LOOKUP module
 */
public final class LookupModel extends BaseModel {
    private static Logger logger = Logger.getLogger(LookupModel.class.getSimpleName());

    private static void debugLog(String type, String functionName, Exception e) {
        debugLog(type, functionName, e.toString());
    }

    private static void debugLog(String type, String functionName, String statement) {
        debugLog(type, functionName, statement, logger);
    }

    public static ArrayList<LookupBean> getCategoryList(Connection conn, int projectPk) {
        String sqlStmt = "SELECT issue_category_pk, category FROM issue_category WHERE project_fk = ? ORDER BY category";
        ArrayList<LookupBean> resultList = new ArrayList<LookupBean>();

        debugLog("SQL", "getCategoryList", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, projectPk);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                LookupBean resultBean = new LookupBean();
                resultBean.setKey(rs.getString("issue_category_pk"));
                resultBean.setValue(rs.getString("category"));
                resultList.add(resultBean);
            }
        } catch (Exception e) {
            debugLog("ERROR", "getCategoryList", e);
        }

        return resultList;
    }

    private static int getCategoryCnt(Connection conn, int issueCategoryPk) {
        String sqlStmt = "SELECT COUNT(1) FROM issue WHERE issue_category_fk = ?";
        int resultCnt = -1;

        debugLog("SQL", "getCategoryCnt", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(issueCategoryPk));
            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) resultCnt = rs.getInt(1);
        } catch (Exception e) {
            debugLog("ERROR", "getCategoryCnt", e);
        }

        return resultCnt;
    }

    public static boolean insertIssueCategory(Connection conn, String value, int projectPk, LoginBean loginBean) {
        String sqlStmt = "INSERT INTO issue_category(category, project_fk, last_updated_by, last_updated_date) VALUES (?, ?, ?, ?)";
        boolean ranOk = false;

        if (CommonMethods.isEmpty(value)) return false;

        debugLog("SQL", "insertIssueCategory", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setString(1, value);
            pStmt.setInt(2, projectPk);
            pStmt.setString(3, loginBean.getFullName());
            pStmt.setString(4, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            ranOk = pStmt.executeUpdate() == 1;
        } catch (Exception e) {
            debugLog("ERROR", "insertIssueCategory", e);
            ranOk = false;
        }

        return ranOk;
    }

    public static boolean deleteIssueCategory(Connection conn, int pk) {
        String sqlStmt = "DELETE FROM issue_category WHERE issue_category_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(pk) <= -1) return false;

        //Verify there are no current issues with specified category
        if (getCategoryCnt(conn, pk) != 0) return false;

        debugLog("SQL", "deleteIssueCategory", sqlStmt + " (pk = " + pk + ")");

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(pk));
            ranOk = (pStmt.executeUpdate() == 1);
        } catch (Exception e) {
            debugLog("ERROR", "deleteIssueCategory", e);
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }

        return ranOk;
    }

    public static boolean insertItem(Connection conn, LoginBean loginBean, int projectPk, ManagedList managedList, String value) {
        String sqlStmt = "INSERT INTO managed_list_item "
            + "(managed_list_code, item_value, project_fk, sort_order, current_default, last_updated_by_fk, last_updated_date) "
            + "VALUES (?, ?, ?, ?, ?, ?, datetime('now', 'localtime'))";
        boolean ranOk = false;
        if (CommonMethods.isEmpty(value)) return false;

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, managedList.getCode());
            pStmt.setString(2, value);
            pStmt.setInt(3, projectPk);
            pStmt.setInt(4, 0); //sort_order
            pStmt.setInt(5, 0); //current_default
            pStmt.setString(6, loginBean.getUserPk());
            if (managedList.equals(ManagedList.FACET_VERSIONS)) {
                ranOk = pushFacetVersion(conn, projectPk) && pStmt.executeUpdate() == 1;
            }
        } catch (Exception e) {
            debugLog("ERROR", "insertItem", e);
            ranOk = false;
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }
        return ranOk;
    }

    public static ArrayList<LookupBean> getList(Connection conn, ManagedList managedList) {
        return getList(conn, null, managedList);
    }

    public static ArrayList<LookupBean> getList(Connection conn, Integer projectPk, ManagedList managedList) {
        if (managedList == null) {
            return new ArrayList<>();
        }
        StringBuilder sqlStmt = new StringBuilder();
        sqlStmt.append("SELECT ")
        .append("managed_list_item_pk, ")
        .append("mli.managed_list_code AS mli_managed_list_code, ")
        .append("mli.item_value AS mli_item_value, ")
        .append("mli.project_fk AS mli_project_fk, ")
        .append("mli.sort_order AS mli_sort_order, ")
        .append("mli.current_default AS mli_current_default, ")
        .append("mli.last_updated_by_fk AS mli_last_updated_by_fk, ")
        .append("u.first_name || ' ' || u.last_name AS mli_last_updated_by, ")
        .append("strftime('%Y-%m-%d %H:%M:%f', mli.last_updated_date) AS mli_last_updated_date ")
        .append("FROM managed_list_item mli ")
        .append("LEFT OUTER JOIN users u ON user_pk = mli.last_updated_by_fk ")
        .append("WHERE mli.managed_list_code = ? ")
        .append(projectPk == null || managedList.isGlobal() ? "" : "AND mli.project_fk = ? ")
        .append("ORDER BY mli.managed_list_code, mli.sort_order, mli.item_value")
        ;
        int managedListCode = managedList.getCode();
        ArrayList<LookupBean> resultList = new ArrayList<LookupBean>();
        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            pStmt.setInt(1, managedListCode);
            if (projectPk != null || !managedList.isGlobal()) {
                pStmt.setInt(2, projectPk);
            }
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                LookupBean resultBean = new LookupBean();
                resultBean.setKey(rs.getString("managed_list_item_pk"));
                resultBean.setValue(rs.getString("mli_item_value"));
                resultList.add(resultBean);
            }
            System.out.println("resultList.size()=" + resultList.size() + ", managedListCode=" + managedListCode + ", managedList.isGlobal()=" + managedList.isGlobal());
        } catch (Exception e) {
            debugLog("ERROR", "getList", e);
        }
        return resultList;
    }

    public static ArrayList<LookupBean> getLocationList(Connection conn, int projectPk) {
        return getList(conn, projectPk, ManagedList.SHIP_VISIT_LOCATIONS);
    }

    public static boolean insertLocation(Connection conn, String value, int projectPk, LoginBean loginBean) {
        return insertItem(conn, loginBean, projectPk, ManagedList.SHIP_VISIT_LOCATIONS, value);
    }

    public static boolean deleteLocation(Connection conn, int pk) {
        String sqlStmt = "DELETE FROM location WHERE location_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(pk) <= -1) return false;

        debugLog("SQL", "deleteLocation", sqlStmt + " (pk = " + pk + ")");

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(pk));
            ranOk = (pStmt.executeUpdate() == 1);
        } catch (Exception e) {
            debugLog("ERROR", "deleteLocation", e);
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }

        return ranOk;
    }

    public static ArrayList<LookupBean> getShipTypeList(Connection conn) {
        return getList(conn, null, ManagedList.SHIP_TYPES);
    }

    public static ArrayList<LookupBean> getLaptopIssueList(Connection conn, int projectPk) {
        return getList(conn, projectPk, ManagedList.LAPTOP_ISSUES);
    }

    public static boolean insertLaptopIssue(Connection conn, String value, int projectPk, LoginBean loginBean) {
        return insertItem(conn, loginBean, projectPk, ManagedList.LAPTOP_ISSUES, value);
    }

    public static boolean deleteLaptopIssue(Connection conn, int pk) {
        String sqlStmt = "DELETE FROM laptop_issue WHERE laptop_issue_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(pk) <= -1) return false;

        debugLog("SQL", "deleteLaptopIssue", sqlStmt + " (pk = " + pk + ")");

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(pk));
            ranOk = (pStmt.executeUpdate() == 1);
        } catch (Exception e) {
            debugLog("ERROR", "deleteLaptopIssue", e);
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }

        return ranOk;
    }

    public static ArrayList<LookupBean> getScannerIssueList(Connection conn, int projectPk) {
        return getList(conn, projectPk, ManagedList.SCANNER_ISSUES);
    }

    public static boolean insertScannerIssue(Connection conn, String value, int projectPk, LoginBean loginBean) {
        return insertItem(conn, loginBean, projectPk, ManagedList.SCANNER_ISSUES, value);
    }

    public static boolean deleteScannerIssue(Connection conn, int pk) {
        String sqlStmt = "DELETE FROM scanner_issue WHERE scanner_issue_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(pk) <= -1) return false;

        debugLog("SQL", "deleteScannerIssue", sqlStmt + " (pk = " + pk + ")");

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(pk));
            ranOk = (pStmt.executeUpdate() == 1);
        } catch (Exception e) {
            debugLog("ERROR", "deleteScannerIssue", e);
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }

        return ranOk;
    }

    public static ArrayList<LookupBean> getSoftwareIssueList(Connection conn, int projectPk) {
        return getList(conn, projectPk, ManagedList.SOFTWARE_ISSUES);
    }

    public static boolean insertSoftwareIssue(Connection conn, String value, int projectPk, LoginBean loginBean) {
        return insertItem(conn, loginBean, projectPk, ManagedList.SOFTWARE_ISSUES, value);
    }

    public static boolean deleteSoftwareIssue(Connection conn, int pk) {
        String sqlStmt = "DELETE FROM software_issue WHERE software_issue_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(pk) <= -1) return false;

        debugLog("SQL", "deleteSoftwareIssue", sqlStmt + " (pk = " + pk + ")");

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(pk));
            ranOk = (pStmt.executeUpdate() == 1);
        } catch (Exception e) {
            debugLog("ERROR", "deleteSoftwareIssue", e);
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }

        return ranOk;
    }

    public static ArrayList<LookupBean> getFacetVersionList(Connection conn, int projectPk) {
        return getList(conn, projectPk, ManagedList.FACET_VERSIONS);
    }

    public static ArrayList<LookupBean> getOsVersionList(Connection conn, int projectPk) {
        return getList(conn, projectPk, ManagedList.OS_VERSIONS);
    }

    public static boolean insertFacetVersion(Connection conn, String value, int projectPk, LoginBean loginBean) {
        return insertItem(conn, loginBean, projectPk, ManagedList.FACET_VERSIONS, value);
    }

    private static boolean pushFacetVersion(Connection conn, int projectPk) {
        String sqlStmt = "UPDATE facet_version SET sort_order = sort_order+1 WHERE project_fk = ?";
        boolean ranOk = false;

        debugLog("SQL", "pushFacetVersion", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, projectPk);
            ranOk = (pStmt.executeUpdate() >= 0);
        } catch (Exception e) {
            debugLog("ERROR", "pushFacetVersion", e);
        }
        return ranOk;
    }

    public static String getCurrentDefault(Connection conn, int projectPk, ManagedList managedList) {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT ")
        .append("mli.item_value AS mli_item_value ")
        .append("FROM managed_list_item mli ")
        .append("LEFT OUTER JOIN users u ON user_pk = mli.last_updated_by_fk ")
        .append("WHERE mli.managed_list_code = ? ")
        .append("AND mli.project_fk = ? AND current_default = 1 ")
        .append("LIMIT 1 ")
        ;
        String sqlStmt = sqlStr.toString();
        String returnStr = null;
        if (CommonMethods.cInt(projectPk) <= -1) return null;
        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, managedList.getCode());
            pStmt.setInt(2, projectPk);
            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                returnStr = rs.getString(1);
            }
        } catch (Exception e) {
            debugLog("ERROR", "getCurrentDefault", e);
        }
        return returnStr;
    }

    public static String getCurrOsVersion(Connection conn, int projectPk) {
        return getCurrentDefault(conn, projectPk, ManagedList.OS_VERSIONS);
    }

    public static String getCurrFacetVersion(Connection conn, int projectPk) {
        return getCurrentDefault(conn, projectPk, ManagedList.FACET_VERSIONS);
    }

    public static boolean setCurrFacetVersion(Connection conn, String pk, int projectPk, LoginBean loginBean) {
        String sqlStmt = "UPDATE facet_version SET is_curr = 'Y' WHERE project_fk = ? AND facet_version_pk = ?";
        boolean ranOk = false;
        if (CommonMethods.cInt(pk) <= -1) return false;
        if (projectPk <= -1) return false;
        debugLog("SQL", "setCurrFacetVersion", "project_fk: " + projectPk + ", facet_version_pk: " + pk);
        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, projectPk);
            pStmt.setInt(2, CommonMethods.cInt(pk));
            ranOk = unsetCurrFacetVersion(conn, projectPk) && pStmt.executeUpdate() == 1;
        } catch (Exception e) {
            debugLog("ERROR", "setCurrFacetVersion", e);
            ranOk = false;
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }
        return ranOk;
    }

    private static boolean unsetCurrFacetVersion(Connection conn, int projectPk) {
        String sqlStmt = "UPDATE facet_version SET is_curr = NULL WHERE project_fk = ? AND is_curr IS NOT NULL";
        boolean ranOk = false;
        debugLog("SQL", "unsetCurrFacetVersion", sqlStmt);
        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, projectPk);
            ranOk = (pStmt.executeUpdate() >= 0);
        } catch (Exception e) {
            debugLog("ERROR", "unsetCurrFacetVersion", e);
        }
        return ranOk;
    }

    public static boolean updateFacetVersionOrder(Connection conn, String[] keyArr, LoginBean loginBean) {
        String sqlStmt = "UPDATE facet_version SET sort_order = ?, last_updated_by = ?, last_updated_date = ? WHERE facet_version_pk = ?";
        boolean ranOk = true;
        if (keyArr == null || keyArr.length <= 0) return false; //Nothing to perform?  This should be an error, return false
        debugLog("SQL", "updateFacetVersionOrder", "keyArr: " + CommonMethods.printArray(keyArr, ", ", ""));
        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setString(2, loginBean.getFullName()); //Updated By
            pStmt.setString(3, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS")); //Updated Date
            for (int i = 0; i < keyArr.length; i++) {
                pStmt.setInt(1, i+1);
                pStmt.setInt(4, CommonMethods.cInt(keyArr[i]));
                pStmt.addBatch();
            }
            for (int rsCnt : pStmt.executeBatch()) {
                ranOk &= rsCnt == 1;
            }
        } catch (Exception e) {
            debugLog("ERROR", "updateFacetVersionOrder", e);
            ranOk = false;
        }
        return ranOk;
    }

    public static boolean deleteFacetVersion(Connection conn, int pk) {
        String sqlStmt = "DELETE FROM facet_version WHERE facet_version_pk = ?";
        boolean ranOk = false;
        if (CommonMethods.cInt(pk) <= -1) return false;
        debugLog("SQL", "deleteFacetVersion", sqlStmt + " (pk = " + pk + ")");
        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(pk));
            ranOk = (pStmt.executeUpdate() == 1);
        } catch (Exception e) {
            debugLog("ERROR", "deleteFacetVersion", e);
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }
        return ranOk;
    }

    public static ArrayList<GitLogBean> getGitLog() throws Exception {
        ArrayList<GitLogBean> resultList = new ArrayList<GitLogBean>();
        try {
            File inputDir = new File("C:\\gitlog");

            if (inputDir.exists() && inputDir.isDirectory()) {

                File[] fileArr = inputDir.listFiles();
                for (File file : fileArr) {
                    if (file.exists()) {
                        int idx = 0;
                        boolean isEarlier = true;
                        GitLogBean currCommit = getGitMetaData(file);

                        while (idx < resultList.size() && isEarlier) {
                            isEarlier = CommonMethods.dateDiff(resultList.get(idx).getDate(), currCommit.getDate()) < 0;
                            if (isEarlier) idx++;
                        }

                        resultList.add(idx, currCommit);
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return resultList;
    }

    private static GitLogBean getGitMetaData(File file) throws Exception {
        GitLogBean resultBean = new GitLogBean();
        resultBean.setId(file.getName());
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String inputLine = null;

            int currLine = 1;
            while ((inputLine = br.readLine()) != null && currLine <= 5) {
                if (currLine == 3) {
                    String[] date = inputLine.substring(12, 32).split(" ");
                    resultBean.setDate((CommonMethods.getMonthNumber(date[0])+1) + "/" + date[1] + "/" + date[3]);
                }
                if (currLine == 5) resultBean.setMsg(inputLine.trim());
                currLine++;
            }
            br.close();
        } catch (Exception e) {
            throw e;
        }
        return resultBean;
    }

    public static ArrayList<String> getGitFile(String id) throws Exception {
        ArrayList<String> resultList = new ArrayList<String>();
        if (id == null || !id.matches("[A-Za-z0-9]+")) throw new Exception("Git Commit ID is not a proper hash");
        try {
            BufferedReader br = new BufferedReader(new FileReader("C:\\gitlog\\" + id));
            String inputLine = null;

            while ((inputLine = br.readLine()) != null) {
                resultList.add(inputLine);
            }
            br.close();
        } catch (Exception e) {
            throw e;
        }
        return resultList;
    }

    public static boolean insertSupportTeam(Connection conn, String fullName, int projectPk, LoginBean loginBean) {
        return insertItem(conn, loginBean, projectPk, ManagedList.SUPPORT_TEAM, fullName);
    }

    public static boolean deleteSupportTeam(Connection conn, int pk) {
        String sqlStmt = "DELETE FROM support_team WHERE support_team_pk = ?";
        boolean ranOk = false;
        if (CommonMethods.cInt(pk) <= -1) return false;
        debugLog("SQL", "deleteSupportTeam", sqlStmt + " (pk = " + pk + ")");
        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(pk));
            ranOk = (pStmt.executeUpdate() == 1);
        } catch (Exception e) {
            debugLog("ERROR", "deleteSupportTeam", e);
        } finally {
            try { if (ranOk) conn.commit(); else conn.rollback(); } catch (Exception e) {}
            try { conn.setAutoCommit(true); } catch (Exception e) {}
        }
        return ranOk;
    }
}
