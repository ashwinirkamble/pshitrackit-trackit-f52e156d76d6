package com.premiersolutionshi.old.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Business logic for the application's LOGIN process
 */
public final class LoginModel {
    private static Logger logger = Logger.getLogger(LoginModel.class.getSimpleName());

    private static void debugLog(String type, String functionName, Exception e) {
        debugLog(type, functionName, e.toString());
    }

    private static void debugLog(String type, String functionName, String statement) {
        if (type.equals("INFO") || type.equals("SQL")) {
            logger.info(String.format("%25s%-30s | %-34s | %s", "", type, functionName, statement));
        }
        else if (type.equals("ERROR")) {
            logger.error(String.format("%25s%-30s | %-34s | %s", "", type, functionName, statement));
        }
        else {
            logger.debug(String.format("%25s%-30s | %-34s | %s", "", type, functionName, statement));
        }
    }

    public static LoginBean getUserInfo(Connection conn, HttpServletRequest request) {
        String sqlStmt = "SELECT user_pk, username, last_name, first_name "
            + "FROM users "
            + "WHERE username = ?";

        if (CommonMethods.isEmpty(request.getRemoteUser()))
            return null;
        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setString(1, request.getRemoteUser());

            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                return getLoginBeanResult(conn, rs);
            }
        }
        catch (Exception e) {
            debugLog("SQL", "getUserInfo", sqlStmt + " (username = " + request.getRemoteUser() + ")");
            debugLog("ERROR", "getUserInfo", e);
        }

        return null;
    }

    private static LoginBean getLoginBeanResult(Connection conn, ResultSet rs) throws SQLException {
        LoginBean resultBean = new LoginBean();
        resultBean.setUserPk(rs.getString("user_pk"));
        resultBean.setUsername(rs.getString("username"));
        resultBean.setLastName(rs.getString("last_name"));
        resultBean.setFirstName(rs.getString("first_name"));
        resultBean.setFullName(CommonMethods.trim(resultBean.getFirstName() + " " + resultBean.getLastName()));
        resultBean.setProjectPkArr(getUserProjectPkArr(conn, rs.getInt("user_pk")));
        return resultBean;
    }

    private static String[] getUserProjectPkArr(Connection conn, int userPk) {
        String sqlStmt = "SELECT project_fk FROM user_project WHERE user_fk = ?";
        ArrayList<String> resultList = new ArrayList<String>();

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            pStmt.setInt(1, userPk);
            ResultSet rs = pStmt.executeQuery();
            while (rs.next())
                resultList.add(rs.getString(1));
        }
        catch (Exception e) {
            debugLog("SQL", "getUserProjectPkArr", sqlStmt + "(" + userPk + ")");
            debugLog("ERROR", "getUserProjectPkArr", e);
        }

        return (String[]) resultList.toArray(new String[0]);
    }

    public static boolean changePassword(Connection conn, LoginBean inputBean, LoginBean loginBean) {
        String sqlStmt = "UPDATE users SET password = ? WHERE user_pk = ? AND password = ?";
        boolean ranOk = false;

        if (CommonMethods.cInt(loginBean.getUserPk()) == -1)
            return false;
        if (inputBean.getPassword().length() < 4 || !inputBean.getPassword().equals(inputBean.getPasswordConfirm()))
            return false;

        debugLog("SQL", "changePassword", sqlStmt);

        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            conn.setAutoCommit(false);
            pStmt.setString(1, DigestUtils.sha256Hex(inputBean.getPassword()));
            pStmt.setInt(2, CommonMethods.cInt(loginBean.getUserPk()));
            pStmt.setString(3, DigestUtils.sha256Hex(inputBean.getOldPassword()));
            ranOk = (pStmt.executeUpdate() == 1);
        }
        catch (Exception e) {
            debugLog("ERROR", "changePassword", e);
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
}
