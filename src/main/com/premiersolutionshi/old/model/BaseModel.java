package com.premiersolutionshi.old.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public abstract class BaseModel {
    private static final String LOGTYPE_SQL = "SQL";
    private static final String LOGTYPE_INFO = "INFO";
    private static final String LOGTYPE_ERROR = "ERROR";

    protected static void debugLog(String type, String functionName, Exception e, Logger logger) {
        String exceptionError = e.toString();
        debugLog(type, functionName, exceptionError, logger);
        e.printStackTrace();
    }

    protected static void logInfo(String functionName, String statement, Logger logger) {
        debugLog(LOGTYPE_INFO, functionName, statement, logger);
    }

    protected static void logError(String functionName, String statement, Logger logger) {
        debugLog(LOGTYPE_ERROR, functionName, statement, logger);
    }

    protected static void logError(String functionName, String statement, Logger logger, Exception e) {
        logError(functionName, statement, logger);
        e.printStackTrace();
    }

    protected static void debugLog(String type, String functionName, String statement, Logger logger) {
        //String logMessage = String.format(logger.getName() + " %13s%-30s %-34s %s", "", type, functionName, statement);
        StringBuilder str = new StringBuilder();
        String logMessage = str.append(functionName).append(" | ").append(statement).toString();
        if (type.equals(LOGTYPE_INFO) || type.equals(LOGTYPE_SQL)) {
            logger.info(logMessage);
        } else if (type.equals(LOGTYPE_ERROR)) {
            logger.error(logMessage);
        } else {
            logger.debug(logMessage);
        }
    }

    protected static ArrayList<String> getStrList(Connection conn, String sqlStmt, Logger logger) {
        ArrayList<String> resultList = new ArrayList<String>();
        debugLog(LOGTYPE_SQL, "getStrList", sqlStmt, logger);
        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) resultList.add(rs.getString(1));
        } catch (Exception e) {
            debugLog(LOGTYPE_ERROR, "getStrList", e, logger);
        }
        return resultList;
    }

}
