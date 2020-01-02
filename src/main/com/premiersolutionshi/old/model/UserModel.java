package com.premiersolutionshi.old.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.time.LocalDate;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import com.premiersolutionshi.common.util.DateUtils;
import com.premiersolutionshi.old.bean.CalendarItemBean;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.bean.OrganizationBean;
import com.premiersolutionshi.old.bean.UserBean;
import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Business logic for the application's USER module
 *
 * @author Anthony Tsuhako
 * @version 1.0, 11/25/2013
 * @since JDK 7, Apache Struts 1.3.10
 */
public class UserModel extends BaseModel {
    private static Logger logger = Logger.getLogger(UserModel.class.getSimpleName());

    public static List<String> getOrganizationList(Connection conn) {
        return getStrList(conn, "SELECT DISTINCT organization FROM users WHERE organization IS NOT NULL ORDER BY organization DESC", logger);
    }

    public static List<UserBean> getUserList(Connection conn) {
        return getUserList(conn, new UserBean(), "username", "ASC");
    }

    public static List<UserBean> getUserList(Connection conn, UserBean inputBean, String sortBy, String sortDir) {
        StringBuffer sqlStmt = new StringBuffer(
        "SELECT user_pk, username, last_name, first_name, organization, title, email, work_number, quick_dial, fax_number, cell_number, "
        + "last_updated_by, last_updated_date "
        + "FROM users");

        // ORDER BY
        switch (sortBy + "_" + sortDir) {
        case "username_ASC":
            sqlStmt.append(" ORDER BY username ASC");
            break;
        case "username_DESC":
            sqlStmt.append(" ORDER BY username DESC");
            break;
        case "last_name_ASC":
            sqlStmt.append(" ORDER BY last_name ASC");
            break;
        case "last_name_DESC":
            sqlStmt.append(" ORDER BY last_name DESC");
            break;
        case "first_name_ASC":
            sqlStmt.append(" ORDER BY first_name ASC");
            break;
        case "first_name_DESC":
            sqlStmt.append(" ORDER BY first_name DESC");
            break;
        case "organization_ASC":
            sqlStmt.append(" ORDER BY organization IS NULL OR organization='', organization ASC");
            break;
        case "organization_DESC":
            sqlStmt.append(" ORDER BY organization IS NULL OR organization='', organization DESC");
            break;
        case "title_ASC":
            sqlStmt.append(" ORDER BY title IS NULL OR title='', title ASC");
            break;
        case "title_DESC":
            sqlStmt.append(" ORDER BY title IS NULL OR title='', title DESC");
            break;
        case "email_ASC":
            sqlStmt.append(" ORDER BY email IS NULL OR email='', email ASC");
            break;
        case "email_DESC":
            sqlStmt.append(" ORDER BY email IS NULL OR email='', email DESC");
            break;
        case "work_number_ASC":
            sqlStmt.append(" ORDER BY work_number IS NULL OR work_number='', work_number ASC");
            break;
        case "work_number_DESC":
            sqlStmt.append(" ORDER BY work_number IS NULL OR work_number='', work_number DESC");
            break;
        case "quick_dial_ASC":
            sqlStmt.append(" ORDER BY quick_dial IS NULL OR quick_dial='', quick_dial ASC");
            break;
        case "quick_dial_DESC":
            sqlStmt.append(" ORDER BY quick_dial IS NULL OR quick_dial='', quick_dial DESC");
            break;
        case "fax_number_ASC":
            sqlStmt.append(" ORDER BY fax_number IS NULL OR fax_number='', fax_number ASC");
            break;
        case "fax_number_DESC":
            sqlStmt.append(" ORDER BY fax_number IS NULL OR fax_number='', fax_number DESC");
            break;
        case "cell_number_ASC":
            sqlStmt.append(" ORDER BY cell_number IS NULL OR cell_number='', cell_number ASC");
            break;
        case "cell_number_DESC":
            sqlStmt.append(" ORDER BY cell_number IS NULL OR cell_number='', cell_number DESC");
            break;
        }

        List<UserBean> resultList = new ArrayList<UserBean>();

        debugLog("SQL", "getUserList", sqlStmt.toString(), logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                UserBean resultBean = new UserBean();
                resultBean.setUserPk(rs.getString("user_pk"));
                resultBean.setUsername(rs.getString("username"));
                resultBean.setLastName(rs.getString("last_name"));
                resultBean.setFirstName(rs.getString("first_name"));
                resultBean.setFullName(resultBean.getFirstName() + " " + resultBean.getLastName());
                resultBean.setOrganization(rs.getString("organization"));
                resultBean.setTitle(rs.getString("title"));
                resultBean.setEmail(rs.getString("email"));
                resultBean.setWorkNumber(rs.getString("work_number"));
                resultBean.setQuickDial(rs.getString("quick_dial"));
                resultBean.setFaxNumber(rs.getString("fax_number"));
                resultBean.setCellNumber(rs.getString("cell_number"));
                resultBean.setLastUpdatedBy(rs.getString("last_updated_by"));
                resultBean.setLastUpdatedDate(DateUtils.parseSqliteDatetime(rs.getString("last_updated_date")));
                resultList.add(resultBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getUserList", e, logger);
        }

        return resultList;
    }

    public static UserBean getUserBean(Connection conn, int userPk) {
        String sqlStmt = "SELECT user_pk, username, last_name, first_name, organization, title, email, work_number, quick_dial, fax_number, "
            + "cell_number, last_updated_by, last_updated_date "
            + "FROM users WHERE user_pk = ?";
        UserBean resultBean = new UserBean();

        if (userPk <= -1)
            return null;

        debugLog("SQL", "getUserBean", sqlStmt + " (user_pk = " + userPk + ")", logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, userPk);

            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                resultBean.setUserPk(rs.getString("user_pk"));
                resultBean.setUsername(rs.getString("username"));
                resultBean.setLastName(rs.getString("last_name"));
                resultBean.setFirstName(rs.getString("first_name"));
                resultBean.setOrganization(rs.getString("organization"));
                resultBean.setCurrOrganization(rs.getString("organization")); // For
                                                                              // updating
                                                                              // purposes
                resultBean.setTitle(rs.getString("title"));
                resultBean.setEmail(rs.getString("email"));
                resultBean.setWorkNumber(rs.getString("work_number"));
                resultBean.setQuickDial(rs.getString("quick_dial"));
                resultBean.setFaxNumber(rs.getString("fax_number"));
                resultBean.setCellNumber(rs.getString("cell_number"));
                resultBean.setRoleArr(getUserRoleArr(conn, userPk));
                resultBean.setProjectPkArr(getUserProjectPkArr(conn, userPk));
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getUserBean", e, logger);
        }

        return resultBean;
    }

    private static int getNewUserPk(Connection conn) throws Exception {
        String sqlStmt = "SELECT MAX(user_pk) FROM users";
        int returnVal = 1; // default to 1 if not found

        debugLog("SQL", "getNewUserPk", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            returnVal = rs.getInt(1) + 1;
        }
        catch (Exception e) {
            debugLog("ERROR", "getNewUserPk", e, logger);
            throw e;
        }

        return returnVal;
    }

    public static boolean insertUser(Connection conn, UserBean inputBean, LoginBean loginBean) {
        String sqlStmt = "INSERT INTO users (user_pk, username, password, last_name, first_name, organization, title, email, work_number, "
            + "quick_dial, fax_number, cell_number, last_updated_by, last_updated_date) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        boolean ranOk = false;

        if (inputBean.getPassword().length() < 4)
            return false;

        debugLog("SQL", "insertUser", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            int newUserPk = getNewUserPk(conn);

            pStmt.setInt(1, newUserPk);
            pStmt.setString(2, inputBean.getUsername());
            pStmt.setString(3, DigestUtils.sha256Hex(inputBean.getPassword()));
            CommonMethods.setString(pStmt, 4, inputBean.getLastName());
            CommonMethods.setString(pStmt, 5, inputBean.getFirstName());
            CommonMethods.setString(pStmt, 6, inputBean.getOrganization());
            CommonMethods.setString(pStmt, 7, inputBean.getTitle());
            CommonMethods.setString(pStmt, 8, inputBean.getEmail());
            CommonMethods.setString(pStmt, 9, inputBean.getWorkNumber());
            CommonMethods.setString(pStmt, 10, inputBean.getQuickDial());
            CommonMethods.setString(pStmt, 11, inputBean.getFaxNumber());
            CommonMethods.setString(pStmt, 12, inputBean.getCellNumber());
            pStmt.setString(13, loginBean.getFullName());
            pStmt.setString(14, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));

            ranOk = (pStmt.executeUpdate() == 1) && insertUserRoleArr(conn, newUserPk, inputBean.getRoleArr(), loginBean)
                    && insertUserProjectPkArr(conn, newUserPk, inputBean.getProjectPkArr(), loginBean);
        }
        catch (Exception e) {
            debugLog("ERROR", "insertUser", e, logger);
            ranOk = false;
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

    public static boolean updateUser(Connection conn, UserBean inputBean, LoginBean loginBean) {
        String sqlStmt = null;
        boolean ranOk = false;

        // SQL when password is supplied
        if (!CommonMethods.isEmpty(inputBean.getPassword()))
            sqlStmt = "UPDATE users SET username = ?, password = ?, last_name = ?, first_name = ?, organization = ?, title = ?, email = ?, work_number = ?, quick_dial = ?, fax_number = ?, cell_number = ?, last_updated_by = ?, last_updated_date = ? WHERE user_pk = ?";

        // SQL when password not specified
        else
            sqlStmt = "UPDATE users SET username = ?, last_name = ?, first_name = ?, organization = ?, title = ?, email = ?, work_number = ?, quick_dial = ?, fax_number = ?, cell_number = ?, last_updated_by = ?, last_updated_date = ? WHERE user_pk = ?";

        if (CommonMethods.cInt(inputBean.getUserPk()) == -1)
            return false;
        if (!CommonMethods.isEmpty(inputBean.getPassword()) && inputBean.getPassword().length() < 4)
            return false;

        debugLog("SQL", "updateUser", sqlStmt + " (user_pk = " + inputBean.getUserPk() + ")", logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);

            int i = 1;
            pStmt.setString(i++, inputBean.getUsername());
            if (!CommonMethods.isEmpty(inputBean.getPassword()))
                pStmt.setString(i++, DigestUtils.sha256Hex(inputBean.getPassword()));
            CommonMethods.setString(pStmt, i++, inputBean.getLastName());
            CommonMethods.setString(pStmt, i++, inputBean.getFirstName());
            CommonMethods.setString(pStmt, i++, inputBean.getOrganization());
            CommonMethods.setString(pStmt, i++, inputBean.getTitle());
            CommonMethods.setString(pStmt, i++, inputBean.getEmail());
            CommonMethods.setString(pStmt, i++, inputBean.getWorkNumber());
            CommonMethods.setString(pStmt, i++, inputBean.getQuickDial());
            CommonMethods.setString(pStmt, i++, inputBean.getFaxNumber());
            CommonMethods.setString(pStmt, i++, inputBean.getCellNumber());
            pStmt.setString(i++, loginBean.getFullName());
            pStmt.setString(i++, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            pStmt.setInt(i++, CommonMethods.cInt(inputBean.getUserPk()));

            // Get existing record to compare prev/curr roles and projects
            UserBean userBean = getUserBean(conn, CommonMethods.cInt(inputBean.getUserPk()));

            // Roles
            String[] pendingRoleArr = CommonMethods.arraySort(inputBean.getRoleArr());
            String[] currRoleArr = CommonMethods.arraySort(userBean.getRoleArr());
            String[] newRoleArr = CommonMethods.arraySubtract(pendingRoleArr, currRoleArr);
            String[] delRoleArr = CommonMethods.arraySubtract(currRoleArr, pendingRoleArr);

            // Project PKs
            String[] pendingProjectPkArr = CommonMethods.arraySort(inputBean.getProjectPkArr());
            String[] currProjectPkArr = CommonMethods.arraySort(userBean.getProjectPkArr());
            String[] newProjectPkArr = CommonMethods.arraySubtract(pendingProjectPkArr, currProjectPkArr);
            String[] delProjectPkArr = CommonMethods.arraySubtract(currProjectPkArr, pendingProjectPkArr);

            ranOk = (pStmt.executeUpdate() == 1)
                    && deleteUserRoleArr(conn, CommonMethods.cInt(inputBean.getUserPk()), delRoleArr, loginBean)
                    && insertUserRoleArr(conn, CommonMethods.cInt(inputBean.getUserPk()), newRoleArr, loginBean)
                    && deleteUserProjectPkArr(conn, CommonMethods.cInt(inputBean.getUserPk()), delProjectPkArr, loginBean)
                    && insertUserProjectPkArr(conn, CommonMethods.cInt(inputBean.getUserPk()), newProjectPkArr, loginBean);
        }
        catch (Exception e) {
            debugLog("ERROR", "updateUser", e, logger);
            ranOk = false;
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

    public static boolean deleteUser(Connection conn, UserBean inputBean) {
        String sqlStmt = "DELETE FROM users WHERE user_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getUserPk()) <= -1)
            return false;

        debugLog("SQL", "deleteUser", sqlStmt + " (username = " + inputBean.getUsername() + ")", logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getUserPk()));
            ranOk = (pStmt.executeUpdate() == 1);
        }
        catch (Exception e) {
            debugLog("ERROR", "deleteUser", e, logger);
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

    private static String[] getUserRoleArr(Connection conn, int userPk) {
        String sqlStmt = "SELECT rolename FROM user_role WHERE user_fk = ?";
        List<String> resultList = new ArrayList<String>();

        debugLog("SQL", "getUserRoleArr", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, userPk);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next())
                resultList.add(rs.getString(1));
        }
        catch (Exception e) {
            debugLog("ERROR", "getUserRoleArr", e, logger);
        }

        return (String[]) resultList.toArray(new String[0]);
    }

    private static boolean insertUserRoleArr(Connection conn, int userPk, String[] roleArr, LoginBean loginBean) {
        String sqlStmt = "INSERT INTO user_role (user_fk, rolename, last_updated_by) VALUES (?, ?, ?)";
        boolean ranOk = false;

        if (userPk == -1)
            return false;
        if (roleArr.length == 0)
            return true;

        debugLog("SQL", "insertUserRoleArr", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(userPk));
            pStmt.setInt(3, CommonMethods.cInt(loginBean.getUserPk()));

            for (String role : roleArr) {
                pStmt.setString(2, role);
                pStmt.addBatch();
            }

            int rsCnt = 0;
            for (int i : pStmt.executeBatch())
                rsCnt += i;
            ranOk = (rsCnt == roleArr.length);
        }
        catch (Exception e) {
            debugLog("ERROR", "insertUserRoleArr", e, logger);
        }

        return ranOk;
    }

    private static boolean deleteUserRoleArr(Connection conn, int userPk, String[] roleArr, LoginBean loginBean) {
        String sqlStmt = "DELETE FROM user_role WHERE user_fk = ? AND rolename = ?";
        boolean ranOk = false;

        if (userPk == -1)
            return false;
        if (roleArr.length == 0)
            return true;

        debugLog("SQL", "deleteUserRoleArr", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, userPk);

            for (String role : roleArr) {
                pStmt.setString(2, role);
                pStmt.addBatch();
            }

            int rsCnt = 0;
            for (int i : pStmt.executeBatch())
                rsCnt += i;
            ranOk = (rsCnt == roleArr.length);
        }
        catch (Exception e) {
            debugLog("ERROR", "deleteUserRoleArr", e, logger);
        }

        return ranOk;
    }

    private static String[] getUserProjectPkArr(Connection conn, int userPk) {
        String sqlStmt = "SELECT project_fk FROM user_project WHERE user_fk = ?";
        List<String> resultList = new ArrayList<String>();

        debugLog("SQL", "getUserProjectPkArr", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, userPk);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next())
                resultList.add(rs.getString(1));
        }
        catch (Exception e) {
            debugLog("ERROR", "getUserProjectPkArr", e, logger);
        }

        return (String[]) resultList.toArray(new String[0]);
    }

    private static boolean insertUserProjectPkArr(Connection conn, int userPk, String[] projectPkArr, LoginBean loginBean) {
        String sqlStmt = "INSERT INTO user_project (user_fk, project_fk, last_updated_by) VALUES (?, ?, ?)";
        boolean ranOk = false;

        if (userPk == -1)
            return false;
        if (projectPkArr.length == 0)
            return true;

        debugLog("SQL", "insertUserProjectPkArr", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, CommonMethods.cInt(userPk));
            pStmt.setInt(3, CommonMethods.cInt(loginBean.getUserPk()));

            for (String projectPk : projectPkArr) {
                pStmt.setInt(2, CommonMethods.cInt(projectPk));
                pStmt.addBatch();
            }

            int rsCnt = 0;
            for (int i : pStmt.executeBatch())
                rsCnt += i;
            ranOk = (rsCnt == projectPkArr.length);
        }
        catch (Exception e) {
            debugLog("ERROR", "insertUserProjectPkArr", e, logger);
        }

        return ranOk;
    }

    private static boolean deleteUserProjectPkArr(Connection conn, int userPk, String[] projectPkArr, LoginBean loginBean) {
        String sqlStmt = "DELETE FROM user_project WHERE user_fk = ? AND project_fk = ?";
        boolean ranOk = false;

        if (userPk == -1)
            return false;
        if (projectPkArr.length == 0)
            return true;

        debugLog("SQL", "deleteUserProjectPkArr", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, userPk);

            for (String projectPk : projectPkArr) {
                pStmt.setInt(2, CommonMethods.cInt(projectPk));
                pStmt.addBatch();
            }

            int rsCnt = 0;
            for (int i : pStmt.executeBatch())
                rsCnt += i;
            ranOk = (rsCnt == projectPkArr.length);
        }
        catch (Exception e) {
            debugLog("ERROR", "deleteUserProjectPkArr", e, logger);
        }

        return ranOk;
    }

    public static List<OrganizationBean> getEmployeePocList(Connection conn, int projectPk, UserBean inputBean) {
        StringBuffer sqlStmt = new StringBuffer(
                "SELECT last_name, first_name, organization, title, email, work_number, quick_dial, fax_number, "
                + "cell_number, u.last_updated_by AS u_last_updated_by, u.last_updated_date AS u_last_updated_date "
                + "FROM users u "
                + "INNER JOIN user_project p ON u.user_pk = p.user_fk AND p.project_fk = ?");
        List<OrganizationBean> resultList = new ArrayList<OrganizationBean>();

        // BUILD WHERE
        if (!CommonMethods.isEmpty(inputBean.getLastName()))
            sqlStmt.append(" AND INSTR(LOWER(last_name), LOWER(?)) > 0");
        if (!CommonMethods.isEmpty(inputBean.getFirstName()))
            sqlStmt.append(" AND INSTR(LOWER(first_name), LOWER(?)) > 0");
        if (!CommonMethods.isEmpty(inputBean.getOrganization()))
            sqlStmt.append(" AND INSTR(LOWER(organization), LOWER(?)) > 0");

        // ORDER BY
        sqlStmt.append(" ORDER BY organization DESC, last_name, first_name");

        debugLog("SQL", "getEmployeePocList", sqlStmt.toString(), logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            int i = 1;
            pStmt.setInt(i++, projectPk);

            // SET WHERE VALUES
            if (!CommonMethods.isEmpty(inputBean.getLastName()))
                pStmt.setString(i++, inputBean.getLastName());
            if (!CommonMethods.isEmpty(inputBean.getFirstName()))
                pStmt.setString(i++, inputBean.getFirstName());
            if (!CommonMethods.isEmpty(inputBean.getOrganization()))
                pStmt.setString(i++, inputBean.getOrganization());

            ResultSet rs = pStmt.executeQuery();

            OrganizationBean parentBean = null;
            List<UserBean> childList = null;
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
                resultBean.setLastName(rs.getString("last_name"));
                resultBean.setFirstName(rs.getString("first_name"));
                resultBean.setTitle(rs.getString("title"));
                resultBean.setEmail(rs.getString("email"));
                resultBean.setWorkNumber(rs.getString("work_number"));
                resultBean.setQuickDial(rs.getString("quick_dial"));
                resultBean.setFaxNumber(rs.getString("fax_number"));
                resultBean.setCellNumber(rs.getString("cell_number"));
                resultBean.setLastUpdatedBy(rs.getString("u_last_updated_by"));
                resultBean.setLastUpdatedDate(DateUtils.parseSqliteDatetime(rs.getString("u_last_updated_date")));
                childList.add(resultBean);
            }

            if (parentBean != null) {
                parentBean.setPocList(childList);
                resultList.add(parentBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getEmployeePocList", e, logger);
        }

        return resultList;
    }

    public static UserBean getEmployeePocBean(Connection conn, String fullName) {
        String sqlStmt = "SELECT last_name, first_name, organization, title, email, work_number, quick_dial, fax_number, "
                + "cell_number, last_updated_by, last_updated_date "
                + "FROM users WHERE first_name || ' ' || last_name = ?";
        UserBean resultBean = new UserBean();

        debugLog("SQL", "getEmployeePocBean", sqlStmt + " (full_name = " + fullName + ")", logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setString(1, fullName);
            ResultSet rs = pStmt.executeQuery();

            if (rs.next()) {
                resultBean.setLastName(rs.getString("last_name"));
                resultBean.setFirstName(rs.getString("first_name"));
                resultBean.setOrganization(rs.getString("organization"));
                resultBean.setTitle(rs.getString("title"));
                resultBean.setEmail(rs.getString("email"));
                resultBean.setWorkNumber(rs.getString("work_number"));
                resultBean.setQuickDial(rs.getString("quick_dial"));
                resultBean.setFaxNumber(rs.getString("fax_number"));
                resultBean.setCellNumber(rs.getString("cell_number"));
                resultBean.setLastUpdatedBy(rs.getString("last_updated_by"));
                resultBean.setLastUpdatedDate(DateUtils.parseSqliteDatetime(rs.getString("last_updated_date")));
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getEmployeePocBean", e, logger);
        }

        return resultBean;
    }

    public static String getEmployeeEmail(Connection conn, String fullName) {
        String sqlStmt = "SELECT email FROM users WHERE first_name || ' ' || last_name = ?";
        String returnStr = null;
        if (CommonMethods.isEmpty(fullName))
            return null;
        debugLog("SQL", "getEmployeeEmail", sqlStmt + " (full_name = " + fullName + ")", logger);
        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setString(1, fullName.trim());
            ResultSet rs = pStmt.executeQuery();
            if (rs.next())
                returnStr = rs.getString(1);
        }
        catch (Exception e) {
            debugLog("ERROR", "getEmployeeEmail", e, logger);
        }
        return returnStr;
    }

    public static List<UserBean> getPtoTravelList(Connection conn, LoginBean loginBean, HttpServletRequest request) {
        // if (!request.isUserInRole("sysadmin") &&
        // !request.isUserInRole("pshi-user-admin") &&
        // !inputBean.getUserPk().equals(loginBean.getUserPk())) {
        String sqlStmt = null;
        if (request.isUserInRole("sysadmin") || request.isUserInRole("pshi-user-admin")) {
            sqlStmt = "SELECT pto_travel_pk, user_fk, first_name, last_name, start_date_fmt, end_date_fmt, leave_type, location FROM pto_travel_vw ORDER BY start_date DESC, end_date DESC, last_name, first_name";
        }
        else {
            sqlStmt = "SELECT pto_travel_pk, user_fk, first_name, last_name, start_date_fmt, end_date_fmt, leave_type, location FROM pto_travel_vw WHERE user_fk = ? ORDER BY start_date DESC, end_date DESC, last_name, first_name";
        }
        List<UserBean> resultList = new ArrayList<UserBean>();

        debugLog("SQL", "getPtoTravelList", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            if (!request.isUserInRole("sysadmin") && !request.isUserInRole("pshi-user-admin")) {
                pStmt.setInt(1, CommonMethods.cInt(loginBean.getUserPk()));
            }

            ResultSet rs = pStmt.executeQuery();

            while (rs.next()) {
                UserBean resultBean = new UserBean();
                resultBean.setPtoTravelPk(rs.getString("pto_travel_pk"));
                resultBean.setUserPk(rs.getString("user_fk"));
                resultBean.setLastName(rs.getString("last_name"));
                resultBean.setFirstName(rs.getString("first_name"));
                resultBean.setStartDate(rs.getString("start_date_fmt"));
                resultBean.setEndDate(rs.getString("end_date_fmt"));
                resultBean.setLeaveType(rs.getString("leave_type"));
                resultBean.setLocation(rs.getString("location"));
                resultList.add(resultBean);
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getPtoTravelList", e, logger);
        }
        return resultList;
    }

   /*ashwini */
    public static ArrayList<UserBean> getCurrentPtoTravelList1(Connection conn, LoginBean loginBean, HttpServletRequest request) {
        ArrayList<UserBean> resultList = new ArrayList<UserBean>();
        
        if (request.isUserInRole("sysadmin") || request.isUserInRole("pshi-user-admin")) 
        {
        StringBuffer sqlStmt = new StringBuffer("SELECT user_fk,first_name, last_name,email, start_date_fmt, end_date_fmt, leave_type, location FROM pto_travel_vw");
        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
        	 ResultSet rs = pStmt.executeQuery();
        	 
        	    while (rs.next()) {
        	    	 
                    if(CommonMethods.isValidDateStr(rs.getString("start_date_fmt")) && CommonMethods.dateDiff(CommonMethods.getDate("MM/DD/YYYY"), rs.getString("start_date_fmt")) <= 0
                        	  && CommonMethods.isValidDateStr(rs.getString("end_date_fmt")) && CommonMethods.dateDiff(CommonMethods.getDate("MM/DD/YYYY"),rs.getString("end_date_fmt")) >= 0)
                        	 {
                    	 UserBean resultBean = new UserBean();
                    	 resultBean.setUserPk(rs.getString("user_fk"));
                    	 resultBean.setFirstName(rs.getString("first_name"));
                         resultBean.setStartDate(rs.getString("start_date_fmt"));
                    	 resultBean.setEmail(rs.getString("email"));
                         resultBean.setEndDate(rs.getString("end_date_fmt"));
                         resultBean.setLeaveType(rs.getString("leave_type"));
                         resultBean.setLocation(rs.getString("location"));
                         resultList.add(resultBean);
                        	 }
        	    }
        	   
        }catch(Exception e)
        {
            debugLog("ERROR", "getCurrentPtoTravelList1", e, logger);
        }
        }
    	return resultList;
    }
 /*ashwini currentPto working */
 public static ArrayList<UserBean> getCurrentPtoTravelList(Connection conn, LoginBean loginBean,HttpServletRequest request) {
    	

        StringBuffer sqlStmt = new StringBuffer("SELECT first_name, last_name, start_date_fmt, end_date_fmt, leave_type, location FROM pto_travel_vw");
        UserBean parentBean = null;
        ArrayList<UserBean> resultList = new ArrayList<UserBean>();

      if( request.isUserInRole("sysadmin") || request.isUserInRole("pshi-user-admin"))
      {
        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            ResultSet rs = pStmt.executeQuery();
           while (rs.next()) {
            	
                parentBean = new UserBean();
                parentBean.setList(new ArrayList<UserBean>());
                UserBean resultBean = new UserBean();

                resultBean.setLastName(rs.getString("last_name"));
                resultBean.setFirstName(rs.getString("first_name"));
                resultBean.setStartDate(rs.getString("start_date_fmt"));
                resultBean.setEndDate(rs.getString("end_date_fmt"));
                resultBean.setLeaveType(rs.getString("leave_type"));
                
                if(CommonMethods.isValidDateStr(rs.getString("start_date_fmt")) && CommonMethods.dateDiff(CommonMethods.getDate("MM/DD/YYYY"), rs.getString("start_date_fmt")) <= 0
                    	  && CommonMethods.isValidDateStr(rs.getString("end_date_fmt")) && CommonMethods.dateDiff(CommonMethods.getDate("MM/DD/YYYY"),rs.getString("end_date_fmt")) >= 0)
                    	 {
                      parentBean.getList().add(resultBean);
                    }
              resultList.add(parentBean);
              

            }

        } catch (Exception e) {
            debugLog("SQL", "getCurrentPtoTravelList", e, logger);
        }

      }
        return resultList;
    }
    
    /* ashwini 12/2 wokring hardcoding*/
    /*
    public static ArrayList<UserBean> getCurrentPtoTravelList(Connection conn, LoginBean loginBean) {
    	
        //String sqlStmt ="SELECT first_name, last_name, start_date_fmt, end_date_fmt, leave_type, location FROM pto_travel_vw where start_date_fmt";

        StringBuffer sqlStmt = new StringBuffer("SELECT first_name, last_name, start_date_fmt, end_date_fmt, leave_type, location FROM pto_travel_vw where start_date_fmt='12/02/2019'");
        UserBean parentBean = null;
        ArrayList<UserBean> resultList = new ArrayList<UserBean>();

      
       int size=0;
      if(loginBean!=null)
      {
        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt.toString())) {
            ResultSet rs = pStmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();

            int columnsNumber = rsmd.getColumnCount();

            
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.println(columnValue + " | " + rsmd.getColumnName(i));
                }
                System.out.println("");

                
                if(CommonMethods.isValidDateStr(rs.getString("start_date_fmt")) && CommonMethods.dateDiff(CommonMethods.getDate("MM/DD/YYYY"), rs.getString("start_date_fmt")) <= 0
                    	  && CommonMethods.isValidDateStr(rs.getString("end_date_fmt")) && CommonMethods.dateDiff(CommonMethods.getDate("MM/DD/YYYY"),rs.getString("end_date_fmt")) >= 0)
                    	 {  
                     System.out.println("You are under if statement");

                      
                      parentBean = new UserBean();
                      parentBean.setTaskList(new ArrayList<UserBean>());

                      UserBean resultBean = new UserBean();

                       resultBean.setLastName(rs.getString("last_name"));
                       resultBean.setFirstName(rs.getString("first_name"));
                       resultBean.setStartDate(rs.getString("start_date_fmt"));
                       resultBean.setEndDate(rs.getString("end_date_fmt"));
                       resultBean.setLeaveType(rs.getString("leave_type"));
                       
                      parentBean.getTaskList().add(resultBean);


                    }
              resultList.add(parentBean);
            }
            
             
              
            //  System.out.println("size of resultset= "+size);
           
            System.out.println("Size of resultList ="+resultList.size());
       } catch (Exception e) {
            debugLog("SQL", "getCurrentPtoTravelList", e, logger);

        	System.out.println("error in current PTO "+e);
        }
      }
        return resultList;
    }
*/    
    
    public static HashMap<String, List<CalendarItemBean>> getPtoTravelMap(Connection conn, int month, int year) {
        String sqlStmt = "SELECT first_name, last_name, start_date_fmt, end_date_fmt, leave_type, location "
                + "FROM pto_travel_vw "
                + "WHERE date(?) BETWEEN start_date AND end_date";
        HashMap<String, List<CalendarItemBean>> resultMap = new HashMap<String, List<CalendarItemBean>>();

        debugLog("SQL", "getPtoTravelMap", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            for (int i = 1; i <= CommonMethods.cInt(CommonMethods.getDate(month + "/1/" + year, "MAX")); i++) {
                pStmt.setString(1, year + "-" + CommonMethods.padString(month, "0", 2) + "-" + CommonMethods.padString(i, "0", 2));
                ResultSet rs = pStmt.executeQuery();
                while (rs.next()) {
                    CalendarItemBean resultBean = new CalendarItemBean();
                    resultBean.setLineItem(rs.getString("first_name") + " " + " - " + rs.getString("leave_type")
                            + (!CommonMethods.isEmpty(rs.getString("location")) ? " (" + rs.getString("location") + ")" : ""));

                    // Add to HashMap
                    if (resultMap.get(CommonMethods.padString(i, "0", 2)) == null)
                        resultMap.put(CommonMethods.padString(i, "0", 2), new ArrayList<CalendarItemBean>());
                    resultMap.get(CommonMethods.padString(i, "0", 2)).add(resultBean);
                }
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getPtoTravelMap", e, logger);
        }

        return resultMap;
    }

    public static UserBean getPtoTravelBean(Connection conn, int ptoTravelPk) {
        String sqlStmt = "SELECT "
            + "pto_travel_pk, user_fk, first_name, last_name, start_date_fmt, end_date_fmt, leave_type, location "
            + "FROM pto_travel_vw "
            + "WHERE pto_travel_pk = ?";
        UserBean resultBean = new UserBean();

        if (ptoTravelPk <= -1)
            return null;

        debugLog("SQL", "getPtoTravelBean", sqlStmt + " (pto_travel_pk = " + ptoTravelPk + ")", logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, ptoTravelPk);

            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                resultBean.setPtoTravelPk(rs.getString("pto_travel_pk"));
                resultBean.setUserPk(rs.getString("user_fk"));
                resultBean.setLastName(rs.getString("last_name"));
                resultBean.setFirstName(rs.getString("first_name"));
                resultBean.setStartDate(rs.getString("start_date_fmt"));
                resultBean.setEndDate(rs.getString("end_date_fmt"));
                resultBean.setLeaveType(rs.getString("leave_type"));
                resultBean.setLocation(rs.getString("location"));
            }
        }
        catch (Exception e) {
            debugLog("ERROR", "getPtoTravelBean", e, logger);
        }

        return resultBean;
    }

    public static boolean insertPtoTravel(Connection conn, UserBean inputBean, LoginBean loginBean) {
        String sqlStmt = "INSERT INTO pto_travel (user_fk, start_date, end_date, leave_type, location, last_updated_by, last_updated_date) VALUES (?,?,?,?,?,?,?)";
        boolean ranOk = false;

        debugLog("SQL", "insertPtoTravel", sqlStmt, logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getUserPk()));
            CommonMethods.setDate(pStmt, 2, inputBean.getStartDate());
            CommonMethods.setDate(pStmt, 3, inputBean.getEndDate());
            CommonMethods.setString(pStmt, 4, inputBean.getLeaveType());
            CommonMethods.setString(pStmt, 5, inputBean.getLocation());
            pStmt.setString(6, loginBean.getFullName());
            pStmt.setString(7, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            ranOk = (pStmt.executeUpdate() == 1);
        }
        catch (Exception e) {
            debugLog("ERROR", "insertPtoTravel", e, logger);
            ranOk = false;
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

    public static boolean updatePtoTravel(Connection conn, UserBean inputBean, LoginBean loginBean) {
        String sqlStmt = "UPDATE pto_travel SET user_fk = ?, start_date = ?, end_date = ?, leave_type = ?, location = ?, last_updated_by = ?, last_updated_date = ? WHERE pto_travel_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getPtoTravelPk()) == -1)
            return false;

        debugLog("SQL", "updatePtoTravel", sqlStmt + " (pto_travel_pk = " + inputBean.getPtoTravelPk() + ")", logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getUserPk()));
            CommonMethods.setDate(pStmt, 2, inputBean.getStartDate());
            CommonMethods.setDate(pStmt, 3, inputBean.getEndDate());
            CommonMethods.setString(pStmt, 4, inputBean.getLeaveType());
            CommonMethods.setString(pStmt, 5, inputBean.getLocation());
            pStmt.setString(6, loginBean.getFullName());
            pStmt.setString(7, CommonMethods.getDate("YYYY-MM-DD HH24:MI:SS"));
            pStmt.setInt(8, CommonMethods.cInt(inputBean.getPtoTravelPk()));
            ranOk = (pStmt.executeUpdate() == 1);
        }
        catch (Exception e) {
            debugLog("ERROR", "updatePtoTravel", e, logger);
            ranOk = false;
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

    public static boolean deletePtoTravel(Connection conn, UserBean inputBean) {
        String sqlStmt = "DELETE FROM pto_travel WHERE pto_travel_pk = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(inputBean.getPtoTravelPk()) <= -1)
            return false;

        debugLog("SQL", "deletePtoTravel", sqlStmt + " (pto_travel_pk = " + inputBean.getPtoTravelPk() + ")", logger);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setInt(1, CommonMethods.cInt(inputBean.getPtoTravelPk()));
            ranOk = (pStmt.executeUpdate() == 1);
        }
        catch (Exception e) {
            debugLog("ERROR", "deletePtoTravel", e, logger);
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
