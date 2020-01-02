package com.premiersolutionshi.old.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.premiersolutionshi.common.util.StringUtils;

public final class ModelUtils {

    public static String generateInsertStmt(String tableName, String[] columns) {
        if (StringUtils.isEmpty(tableName) || columns.length == 0) {
            return null;
        }
        int count = columns.length;
        StringBuilder insertStmt = new StringBuilder();
        insertStmt.append("INSERT INTO ").append(tableName).append(" (\n  ").append(columns[0]);
        for (int i = 1; i < count; i++) {
            insertStmt.append(",\n  ").append(columns[i]);
        }
        insertStmt.append("\n) VALUES (");
        insertStmt.append("?");
        for (int i = 1; i < count; i++) {
            insertStmt.append(",").append("?");
        }
        insertStmt.append(")");
        return insertStmt.toString();
    }

    public static int getColumnIndex(String[] columns, String columnName) {
        int length = columns.length;
        for (int i = 0; i < length; i++) {
            String column = columns[i];
            if (column.equals(columnName)) {
                return i + 1;
            }
        }
        return 0;
    }

    public static SqlColumn getSqlColumn(List<SqlColumn> sqlColumns, String columnName) throws SQLException {
        for (SqlColumn sqlColumn : sqlColumns) {
            if (sqlColumn.getName().equals(columnName)) {
                return sqlColumn;
            }
        }
        throw new SQLException("ModelUtils | getSqlColumn | Could not find column '" + columnName + "' in given list of columns!");
    }

    public static void setInt(PreparedStatement pStmt, List<SqlColumn> sqlColumns, String columnName, Integer value) throws SQLException {
        if (StringUtils.isEmpty(columnName)) {
            return;
        }
        SqlColumn sqlColumn = getSqlColumn(sqlColumns, columnName);
        if (sqlColumn != null) {
            setInt(pStmt, sqlColumn, value);
        }
    }

    public static void setDateStr(PreparedStatement pStmt, List<SqlColumn> sqlColumns, String columnName, String value) throws SQLException {
        if (StringUtils.isEmpty(columnName)) {
            return;
        }
        SqlColumn sqlColumn = getSqlColumn(sqlColumns, columnName);
        if (sqlColumn != null) {
            setDate(pStmt, sqlColumn, value);
        }
    }

    public static void setDatetimeNow(PreparedStatement pStmt, List<SqlColumn> sqlColumns, String columnName) throws SQLException {
        SqlColumn sqlColumn = getSqlColumn(sqlColumns, columnName);
        if (sqlColumn != null) {
            long now = (new Date()).getTime();
            pStmt.setDate(sqlColumn.getIndex(), new java.sql.Date(now));
        }

    }

    public static void setNull(PreparedStatement pStmt, List<SqlColumn> sqlColumns, String columnName) throws SQLException {
        if (StringUtils.isEmpty(columnName)) {
            return;
        }
        SqlColumn sqlColumn = getSqlColumn(sqlColumns, columnName);
        if (sqlColumn != null) {
            ColumnType columnType = sqlColumn.getType();
            if (columnType.equals(ColumnType.INTEGER)) {
                pStmt.setNull(sqlColumn.getIndex(), Types.INTEGER);
            }
            else if (columnType.equals(ColumnType.TEXT) || columnType.equals(ColumnType.VARCHAR)) {
                pStmt.setNull(sqlColumn.getIndex(), Types.VARCHAR);
            }
            else if (columnType.equals(ColumnType.DATE) || columnType.equals(ColumnType.DATETIME)) {
                pStmt.setNull(sqlColumn.getIndex(), Types.DATE);
            }
        }
    }

    public static void setValue(PreparedStatement pStmt, List<SqlColumn> sqlColumns, String columnName, String value) throws SQLException {
        if (StringUtils.isEmpty(columnName)) {
            return;
        }
        SqlColumn sqlColumn = getSqlColumn(sqlColumns, columnName);
        if (sqlColumn != null) {
            if (value == null) {
                setNull(pStmt, sqlColumns, columnName);
            }
            else {
                ColumnType columnType = sqlColumn.getType();
                if (columnType.equals(ColumnType.INTEGER)) {
                    setInt(pStmt, sqlColumn, value);
                }
                else if (columnType.equals(ColumnType.TEXT) || columnType.equals(ColumnType.VARCHAR)) {
                    setString(pStmt, sqlColumn, value);
                }
                else if (columnType.equals(ColumnType.DATE) || columnType.equals(ColumnType.DATETIME)) {
                    setDate(pStmt, sqlColumn, value);
                }
                else {
                    throw new SQLException("ModelUtils | setValue | SQL Column type is NOT supported: " + columnType);
                }
            }
        }
    }

    private static void setDate(PreparedStatement pStmt, SqlColumn sqlColumn, String value) throws SQLException {
        java.sql.Date date = OldDateUtils.parseBasicDateToSqlDate(value);
        setDate(pStmt, sqlColumn, date);
    }

    public static void setDate(PreparedStatement pStmt, SqlColumn sqlColumn, Date date) throws SQLException {
        if (date != null) {
            pStmt.setDate(sqlColumn.getIndex(), new java.sql.Date(date.getTime()));
        }
    }

    /**
     * Copied from "CommonMethods.setString()"
     * @param pStmt
     * @param sqlColumn
     * @param value
     * @throws SQLException
     */
    private static void setString(PreparedStatement pStmt, SqlColumn sqlColumn, String value) throws SQLException {
        if (!StringUtils.isEmpty(value)) {
            pStmt.setString(sqlColumn.getIndex(), value);
        } else {
            pStmt.setNull(sqlColumn.getIndex(), java.sql.Types.VARCHAR);
        }
    }

    /**
     * Copied from "CommonMethods.setInt()"
     * @param pStmt
     * @param sqlColumn
     * @param value
     * @throws SQLException
     */
    private static void setInt(PreparedStatement pStmt, SqlColumn sqlColumn, String value) throws SQLException {
        if (!StringUtils.isEmpty(value)) {
            pStmt.setInt(sqlColumn.getIndex(), StringUtils.parseInt(value));
        } else {
            pStmt.setNull(sqlColumn.getIndex(), java.sql.Types.NUMERIC);
        }
    }

    public static void setInt(PreparedStatement pStmt, SqlColumn sqlColumn, Integer value) throws SQLException {
        if (value != null) {
            pStmt.setInt(sqlColumn.getIndex(), value);
        } else {
            pStmt.setNull(sqlColumn.getIndex(), java.sql.Types.NUMERIC);
        }
    }

    public static String generateUpdateStmt(String tableName, List<SqlColumn> columns) {
        return generateUpdateStmt(tableName, columns, tableName + "_pk");
    }

    public static String generateUpdateStmt(String tableName, List<SqlColumn> columns, String pkColumn) {
        if (StringUtils.isEmpty(tableName) || columns.isEmpty()) {
            return null;
        }
        StringBuilder updateStmt = new StringBuilder();
        int count = columns.size();
        updateStmt.append("UPDATE ").append(tableName).append(" SET\n  ").append(columns.get(0).getName()).append(" = ?");
        for (int i = 1; i < count; i++) {
            updateStmt.append(",\n  ").append(columns.get(i).getName()).append(" = ?");
        }
        updateStmt.append("\nWHERE ").append(pkColumn).append(" = ?");
        return updateStmt.toString();
    }

    public static void main(String[] args) {
        //String[] columns = {"title", "author", "last_updated_by", "last_updated_date"};
        //System.out.println(generateInsertStmt());
        //        StringJoiner stmt = new StringJoiner(",");
//        int count = columns.length;
//        for (int i = 0; i < count; i++) {
//            stmt.add(columns[i]);
//        }
//        System.out.println(stmt.toString());

    }
}
