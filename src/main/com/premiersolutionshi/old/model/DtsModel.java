package com.premiersolutionshi.old.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.upload.FormFile;

import com.premiersolutionshi.old.bean.DtsBean;
import com.premiersolutionshi.old.bean.LoginBean;
import com.premiersolutionshi.old.util.CommonMethods;
import com.premiersolutionshi.old.util.ModelUtils;

/**
 * Business logic for the application's DTS module
 *
 * @author Anthony Tsuhako
 * @version 1.0, 11/25/2013
 * @since JDK 7, Apache Struts 1.3.10
 */
public final class DtsModel extends BaseModel {
    private static Logger logger = Logger.getLogger(DtsModel.class.getSimpleName());

    /****************************************************************************
     * Function: processLogcopZip
     ****************************************************************************/
    public static boolean processLogcopZip(Connection conn, DtsBean inputBean, LoginBean loginBean) {
        FormFile f = inputBean.getFile();
        boolean ranOk = false;

        try (ZipInputStream zin = new ZipInputStream(f.getInputStream())) {
            conn.setAutoCommit(false);
            if (f != null) {
                ZipEntry zipEntry = null;
                while ((zipEntry = zin.getNextEntry()) != null) {
                    if (zipEntry.getName().indexOf("facet_report_by_doctype.xls") > -1) {
                        ranOk = deleteLoadTable(conn) && parseDoctypeXlsx(conn, zin) && processDoctypeReport(conn);
                    } // end of if
                } // end of while
            } // end of if
        }
        catch (Exception e) {
            debugLog("ERROR", "processLogcopZip", e, logger);
        }
        finally {
            try {
                conn.setAutoCommit(true);
            }
            catch (Exception e) {
            }
        } // end of catch

        return ranOk;
    } // end of processLogcopZip

    /*
     * private static boolean parseDoctypeReport(Connection conn, InputStream
     * in) { String sqlStmt =
     * "INSERT INTO issue (issue_pk, project_fk) VALUES (?,?)"; boolean ranOk =
     * false;
     * 
     * try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
     * BufferedReader br = new BufferedReader(new InputStreamReader(in)); String
     * inputLine = null;
     * 
     * while ((inputLine = br.readLine()) != null) { if
     * (inputLine.indexOf("shipCell1") > -1) debugLog("INFO",
     * "parseDoctypeReport", inputLine); } //end of while br.close(); } catch
     * (Exception e) { debugLog("ERROR", "parseDoctypeReport", e); ranOk =
     * false; } //end of catch
     * 
     * return true; } //end of parseDoctypeReport
     */

    /****************************************************************************
     * Function: deleteLoadTable
     ****************************************************************************/
    private static boolean deleteLoadTable(Connection conn) throws Exception {
        boolean ranOk = false;

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM load_logcop_report");
            ranOk = true;
        }
        catch (Exception e) {
            ranOk = false;
            debugLog("ERROR", "deleteLoadTable", e, logger);
            throw e;
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
        } // end of finally

        return ranOk;
    } // end of deleteLoadTable

    /****************************************************************************
     * Function: processDoctypeReport
     ****************************************************************************/
    private static boolean processDoctypeReport(Connection conn) throws Exception {
        boolean ranOk = false;

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM logcop_report_prev");
            stmt.executeUpdate("INSERT INTO logcop_report_prev SELECT * FROM logcop_report");

            stmt.executeUpdate("DELETE FROM logcop_report");
            stmt.executeUpdate("INSERT INTO logcop_report SELECT * FROM load_logcop_report");

            stmt.executeUpdate("DELETE FROM load_logcop_report");
            ranOk = true;
        }
        catch (Exception e) {
            ranOk = false;
            debugLog("ERROR", "processDoctypeReport", e, logger);
            throw e;
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
        } // end of finally

        return ranOk;
    } // end of processDoctypeReport

    private static boolean parseDoctypeXlsx(Connection conn, ZipInputStream zin) throws Exception {
        String[] columns = { "report_date", "ship_name", "transmittal_num", "doc_type", "facet_version", "upload_date", "upload_user",
                "doc_cnt", "transmittal_file", "computer_name" };
        String sqlStmt = ModelUtils.generateInsertStmt("load_logcop_report", columns);
        boolean success = false;

        Workbook workbook = new XSSFWorkbook(zin);
        try (PreparedStatement pStmt = conn.prepareStatement(sqlStmt)) {
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet.getPhysicalNumberOfRows() > 0) {
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    if (sheet.getRow(i) != null) {
                        // Get the Report Date
                        if (getCellValue(sheet, i, 0, evaluator, formatter).startsWith("Report Date")) {
                            String reportDate = getCellValue(sheet, i, 0, evaluator, formatter);
                            reportDate = reportDate.substring(reportDate.indexOf(": ") + 2, reportDate.lastIndexOf(" "));
                            if (!CommonMethods.isEmpty(reportDate))
                                CommonMethods.setDate(pStmt, 1, reportDate);
                        }

                        // Get the Unit Name
                        if (sheet.getLastRowNum() >= 1 && getCellValue(sheet, i, 1, evaluator, formatter).indexOf("FACET") > -1) {
                            String unitName = getCellValue(sheet, i, 0, evaluator, formatter);
                            if (unitName.indexOf(" (") > -1)
                                unitName = unitName.substring(0, unitName.indexOf(" ("));
                            pStmt.setString(2, unitName);

                            // Cycle to get everything
                            String docType = null;
                            while (sheet.getRow(i++) != null && !CommonMethods.isEmpty(getCellValue(sheet, i, 1, evaluator, formatter))) {
                                docType = !CommonMethods.isEmpty(getCellValue(sheet, i, 0, evaluator, formatter))
                                        ? getCellValue(sheet, i, 0, evaluator, formatter)
                                        : docType;

                                String uploadDate = getCellValue(sheet, i, 3, evaluator, formatter);
                                if (uploadDate.indexOf(" ") > -1)
                                    uploadDate = uploadDate.substring(0, uploadDate.indexOf(" "));

                                pStmt.setInt(3, CommonMethods.cInt(getCellValue(sheet, i, 1, evaluator, formatter))); // transmittal_num
                                pStmt.setString(4, docType); // doc_type
                                pStmt.setString(5, getCellValue(sheet, i, 2, evaluator, formatter)); // facet_version
                                CommonMethods.setDate(pStmt, 6, uploadDate); // upload_date
                                pStmt.setString(7, getCellValue(sheet, i, 4, evaluator, formatter)); // upload_user
                                pStmt.setInt(8, CommonMethods.cInt(getCellValue(sheet, i, 5, evaluator, formatter).replaceAll(",", ""))); // doc_cnt

                                String transmittalFile = getCellValue(sheet, i, 7, evaluator, formatter);

                                String computerName = null;
                                if (transmittalFile.indexOf("_") > -1) {
                                    computerName = transmittalFile.substring(0, transmittalFile.indexOf("_"));
                                }
                                else {
                                    computerName = null;
                                } // end of else

                                pStmt.setString(9, transmittalFile); // transmittal_file
                                CommonMethods.setString(pStmt, 10, computerName);
                                pStmt.executeUpdate();
                            } // end of while
                        } // end of if

                    } // end of if
                } // end of for
                success = true;
            } // end of if
        }
        catch (Exception e) {
            success = false;
            debugLog("ERROR", "parseDoctypeXlsx", e, logger);
            throw e;
        }
        finally {
            try {
                if (success)
                    conn.commit();
                else
                    conn.rollback();
            }
            catch (Exception e) {
            }
            workbook.close();
        } // end of finally

        return success;
    } // end of parseDoctypeXlsx

    /****************************************************************************
     * Function: getCellValue
     ****************************************************************************/
    private static String getCellValue(Sheet sheet, int rowNum, int cellNum, FormulaEvaluator evaluator, DataFormatter formatter) {
        String resultStr = null;
        Row row = null;
        Cell cell = null;

        if ((row = sheet.getRow(rowNum)) != null && row.getLastCellNum() >= cellNum) {
            if ((cell = row.getCell(cellNum)) != null) {
                if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                    resultStr = formatter.formatCellValue(cell, evaluator);
                }
                else if (cell.getCellType() != Cell.CELL_TYPE_ERROR) {
                    resultStr = formatter.formatCellValue(cell);
                } // end of else
            } // end of if
        } // end of if

        return CommonMethods.nvl(resultStr, "NULL");
    } // end of getCellValue
} // end of class
