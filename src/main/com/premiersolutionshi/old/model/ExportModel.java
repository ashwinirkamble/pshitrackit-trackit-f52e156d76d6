package com.premiersolutionshi.old.model;

import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderFormatting;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ComparisonOperator;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FontFormatting;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.premiersolutionshi.old.bean.BackfileBean;
import com.premiersolutionshi.old.bean.DecomBean;
import com.premiersolutionshi.old.bean.HardwareBean;
import com.premiersolutionshi.old.bean.ProjectBean;
import com.premiersolutionshi.old.bean.ReportBean;
import com.premiersolutionshi.old.bean.ShipBean;
import com.premiersolutionshi.old.bean.SoftwareBean;
import com.premiersolutionshi.old.bean.SupportBean;
import com.premiersolutionshi.old.bean.SystemBean;
import com.premiersolutionshi.old.bean.TrainingBean;
import com.premiersolutionshi.old.bean.UserBean;
import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Business logic for the application's EXPORT module
 */
public class ExportModel {
    private static Logger logger = Logger.getLogger(ExportModel.class.getSimpleName());

    //    private static void debugLog(String type, String functionName, Exception e) {
    //        debugLog(type, functionName, e.toString());
    //    }

    //    private static void debugLog(String type, String functionName, String statement) {
    //        if (type.equals("INFO") || type.equals("SQL")) {
    //            logger.info(String.format("%12s%-30s | %-34s | %s", "", type, functionName, statement));
    //        } else if (type.equals("ERROR")) {
    //            logger.error(String.format("%12s%-30s | %-34s | %s", "", type, functionName, statement));
    //        } else {
    //            logger.debug(String.format("%12s%-30s | %-34s | %s", "", type, functionName, statement));
    //        }
    //    }

    private static Cell writeCell(Row row, int c, CellStyle style) throws Exception {
        return writeCell(row, c, style, null, null, null);
    }

    public static Cell writeCell(Row row, int c, CellStyle style, String value) throws Exception {
        return writeCell(row, c, style, value, null, null);
    }
    
    public static Cell writeCell(Row row, int c, CellStyle style, Object value) throws Exception {
        return writeCell(row, c, style, value.toString(), null, null);
    }

    public static Cell writeIntCell(Row row, int c, CellStyle style, Integer value) throws Exception {
        Cell cell = row.createCell(c);
        cell.setCellStyle(style);
        cell.setCellValue(value);
        return cell;
    }

    private static Cell writeCell(Row row, int c, CellStyle style, String value, String type) throws Exception {
        return writeCell(row, c, style, value, type, null);
    }

    private static Cell writeCell(Row row, int c, CellStyle style, String value, String type, String format) throws Exception {
        Cell cell = row.createCell(c);
        cell.setCellStyle(style);

        if (!CommonMethods.isEmpty(value)) {
            switch(CommonMethods.nes(type)) {
                case "double":
                    cell.setCellValue(Double.parseDouble(value));
                    break;
                case "date":
                    cell.setCellValue(CommonMethods.getDateCal(value, format));
                    break;
                case "formula":
                    cell.setCellType(Cell.CELL_TYPE_FORMULA);
                    cell.setCellFormula(value);
                    break;
                default:
                    cell.setCellValue(value);
                    break;
            }
        }
        else {
            cell.setCellValue("");
        }
        return cell;
    }

    private static void writeSectionBorder(Sheet sheet, int i, int j) throws Exception {
        RegionUtil.setBorderLeft(BorderFormatting.BORDER_THICK, new CellRangeAddress(i,i,j,j), sheet, sheet.getWorkbook());
        RegionUtil.setLeftBorderColor(IndexedColors.BLACK.index, new CellRangeAddress(i,i,j,j), sheet, sheet.getWorkbook());
    }

    private static Cell writeHyperlink(Workbook wb, Row row, int k, HashMap<String, CellStyle> styles, String value) throws Exception {
        Cell cell = row.createCell(k);

        if (!CommonMethods.isEmpty(value)) {
            cell.setCellValue(value);

            try {
                cell.setCellStyle(styles.get("general"));
                Hyperlink link = wb.getCreationHelper().createHyperlink(Hyperlink.LINK_EMAIL);
                link.setAddress("mailto:" + value);
                cell.setHyperlink(link);
                cell.setCellStyle(styles.get("hyperlink"));
            } catch (java.lang.IllegalArgumentException e) {
                //disregard hyperlink
                cell.setCellStyle(styles.get("general"));
            }
        }

        return cell;
    }

    private static CellStyle createBorderedStyle(Workbook wb) {
        return createBorderedStyle(wb, IndexedColors.GREY_25_PERCENT);
    }

    private static CellStyle createBorderedStyle(Workbook wb, IndexedColors borderColor) {
        CellStyle style = wb.createCellStyle();
        style.setBorderTop(BorderFormatting.BORDER_THIN);
        style.setBorderRight(BorderFormatting.BORDER_THIN);
        style.setBorderBottom(BorderFormatting.BORDER_THIN);
        style.setBorderLeft(BorderFormatting.BORDER_THIN);

        style.setTopBorderColor(borderColor.index);
        style.setRightBorderColor(borderColor.index);
        style.setBottomBorderColor(borderColor.index);
        style.setLeftBorderColor(borderColor.index);

        return style;
    }

    /**
     * create a library of cell styles
     */
    public static HashMap<String, CellStyle> createStyles(Workbook wb){
        HashMap<String, CellStyle> styles = new HashMap<String, CellStyle>();
        DataFormat df = wb.createDataFormat();
        CellStyle style = null;
        Font font = null;

        //Header style
        style = createBorderedStyle(wb, IndexedColors.BLACK);
        font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(IndexedColors.WHITE.index);
        style.setFont(font);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.index);
        style.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
        styles.put("header", style);

        //General style
        style = createBorderedStyle(wb);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        styles.put("general", style);

        //Red text style
        style = createBorderedStyle(wb);
        font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(IndexedColors.RED.index);
        style.setFont(font);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        styles.put("red-text", style);

        //Gold bg text style
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.GOLD.index);
        style.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        styles.put("gold-bg-text", style);

        //Teal bg text style
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.TEAL.index);
        style.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        styles.put("teal-bg-text", style);

        //Center style
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        styles.put("center", style);

        //Wrapped style
        style = createBorderedStyle(wb);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setWrapText(true);
        styles.put("wrapped", style);

        //Date style
        style = createBorderedStyle(wb);
        style.setDataFormat(df.getFormat("m/d/yyyy"));
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        styles.put("date", style);

        //Red Date style
        style = createBorderedStyle(wb);
        font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(IndexedColors.RED.index);
        style.setFont(font);
        style.setDataFormat(df.getFormat("m/d/yyyy"));
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        styles.put("red-date", style);

        //Red Date style
        style = createBorderedStyle(wb);
        style.setFillForegroundColor(IndexedColors.RED.index);
        style.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
        style.setDataFormat(df.getFormat("m/d/yyyy"));
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        styles.put("red-bg-date", style);

        //Yellow Date style
        style = createBorderedStyle(wb);
        style.setFillForegroundColor(IndexedColors.YELLOW.index);
        style.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
        style.setDataFormat(df.getFormat("m/d/yyyy"));
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        styles.put("yellow-bg-date", style);

        //Green Date style
        style = createBorderedStyle(wb);
        style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.index);
        style.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
        style.setDataFormat(df.getFormat("m/d/yyyy"));
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        styles.put("green-bg-date", style);

        //Blue Date style
        style = createBorderedStyle(wb);
        style.setFillForegroundColor(IndexedColors.PALE_BLUE.index);
        style.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
        style.setDataFormat(df.getFormat("m/d/yyyy"));
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        styles.put("blue-bg-date", style);

        //Grey Date style
        style = createBorderedStyle(wb);
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index);
        style.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
        style.setDataFormat(df.getFormat("m/d/yyyy"));
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        styles.put("grey-bg-date", style);

        //Greyed style
        style = createBorderedStyle(wb);
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index);
        style.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        styles.put("greyed", style);

        //Date time style
        style = createBorderedStyle(wb);
        style.setDataFormat(df.getFormat("m/d/yyyy hh:mm:ss"));
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        styles.put("datetime", style);

        //Hyperlink
        style = createBorderedStyle(wb);
        Font hlinkFont = wb.createFont();
        hlinkFont.setColor(IndexedColors.BLUE.index);
        hlinkFont.setUnderline(Font.U_SINGLE);
        style.setFont(hlinkFont);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        styles.put("hyperlink", style);

        return styles;
    }

    public static void writeIssueListXlsx(OutputStream out, ArrayList<SupportBean> resultList) {
        SXSSFWorkbook wb = null;
        try {
            wb = new SXSSFWorkbook();
            HashMap<String, CellStyle> styles = createStyles(wb);

            Sheet sheet = wb.createSheet("Issue List");
            Row row = null;

            //Conditional Formatting
            SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();

            CellRangeAddress[] statusCol = {new CellRangeAddress(0,65535,3,3)};
            CellRangeAddress[] priorityCol = {new CellRangeAddress(0,65535,4,4)};

            ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"1 - New\"");
            FontFormatting format1 = rule1.createFontFormatting();
            format1.setFontColorIndex(IndexedColors.GREEN.index);
            sheetCF.addConditionalFormatting(statusCol, rule1);

            ConditionalFormattingRule rule2 = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"2 - Active\"");
            FontFormatting format2 = rule2.createFontFormatting();
            format2.setFontColorIndex(IndexedColors.TEAL.index);
            sheetCF.addConditionalFormatting(statusCol, rule2);

            ConditionalFormattingRule rule3 = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"3 - Resolved\"");
            FontFormatting format3 = rule3.createFontFormatting();
            format3.setFontColorIndex(IndexedColors.GREY_50_PERCENT.index);
            sheetCF.addConditionalFormatting(statusCol, rule3);

            ConditionalFormattingRule rule4 = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"4 - Pending Possible Resolution\"");
            FontFormatting format4 = rule4.createFontFormatting();
            format4.setFontColorIndex(IndexedColors.PLUM.index);
            sheetCF.addConditionalFormatting(statusCol, rule4);

            ConditionalFormattingRule rule5 = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"5 - Closed\"");
            FontFormatting format5 = rule5.createFontFormatting();
            format5.setFontColorIndex(IndexedColors.GREY_50_PERCENT.index);
            sheetCF.addConditionalFormatting(statusCol, rule5);

            ConditionalFormattingRule rule6 = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"6 - Closed (Successful)\"");
            FontFormatting format6 = rule6.createFontFormatting();
            format6.setFontColorIndex(IndexedColors.GREY_50_PERCENT.index);
            sheetCF.addConditionalFormatting(statusCol, rule6);

            ConditionalFormattingRule rule7 = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"7 - Closed (No Response)\"");
            FontFormatting format7 = rule7.createFontFormatting();
            format7.setFontColorIndex(IndexedColors.GREY_50_PERCENT.index);
            sheetCF.addConditionalFormatting(statusCol, rule7);

            ConditionalFormattingRule rule8 = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"8 - Closed (Unavailable)\"");
            FontFormatting format8 = rule8.createFontFormatting();
            format8.setFontColorIndex(IndexedColors.GREY_50_PERCENT.index);
            sheetCF.addConditionalFormatting(statusCol, rule8);

            ConditionalFormattingRule rule9 = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"High\"");
            FontFormatting format9 = rule9.createFontFormatting();
            format9.setFontColorIndex(IndexedColors.RED.index);
            sheetCF.addConditionalFormatting(priorityCol, rule9);

            //set column widths, the width is measured in units of 1/256th of a character width
            int[] widthArr = {7,54,65,29,9,27,11,9,10,11,8,12,26,77,20,13,13,20,49,11,19,17,17,40,20,18,24,20,20,20,30};

            int c = 0;
            for (int w : widthArr) {
                sheet.setColumnWidth(c++, 256*w);
            }

            //Create header row
            row = sheet.createRow(0);
            String[] headerArr = {
                "Issue #", "Title", "Summary", "Status", "Priority", "Category", "Initiated By", "Division", "Is Training Provided?", "Is Training Onsite?",
                "PSHI Email Sent?", "Unit Email Responded?", "Phase", "Unit Name", "Opened By", "Opened Date", "Closed Date", "Person Assigned", "Resolution",
                "Total Time (min)", "Homeport", "Support Visit Date", "Support Visit Time", "Support Visit Location", "Trainer", "Last Updated", "Contract System Installed",
                "Laptop Down Reason", "Scanner Down Reason", "Software Issue", "Support Visit Location Notes"
            };

            c = 0;
            for (String value : headerArr) {
                writeCell(row, c++, styles.get("header"), value);
            }

            //freeze the pane
            sheet.createFreezePane(0,1);

            //Write data
            int i = 1;
            for (SupportBean resultBean : resultList) {
                row = sheet.createRow(i++);
                c = 0;
                writeCell(row, c++, styles.get("general"), resultBean.getIssuePk(), "double");
                writeCell(row, c++, styles.get("general"), resultBean.getTitle());
                writeCell(row, c++, styles.get("wrapped"), resultBean.getDescription());
                writeCell(row, c++, styles.get("general"), resultBean.getStatus());
                writeCell(row, c++, styles.get("general"), resultBean.getPriority());
                writeCell(row, c++, styles.get("general"), resultBean.getCategory());
                writeCell(row, c++, styles.get("general"), resultBean.getInitiatedBy());
                writeCell(row, c++, styles.get("center"),  resultBean.getDept());
                writeCell(row, c++, styles.get("center"),  resultBean.getIsTrainingProvided());
                writeCell(row, c++, styles.get("center"),  resultBean.getIsTrainingOnsite());
                writeCell(row, c++, styles.get("center"),  resultBean.getIsEmailSent());
                writeCell(row, c++, styles.get("center"),  resultBean.getIsEmailResponded());
                writeCell(row, c++, styles.get("general"), resultBean.getPhase());
                writeCell(row, c++, styles.get("general"), resultBean.getShipName());
                writeCell(row, c++, styles.get("general"), resultBean.getOpenedBy());
                writeCell(row, c++, styles.get("date"),    resultBean.getOpenedDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("date"),    resultBean.getClosedDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("general"), resultBean.getPersonAssigned());
                writeCell(row, c++, styles.get("general"), resultBean.getResolution());
                writeCell(row, c++, styles.get("center"),  resultBean.getTotalTime(), "double");
                writeCell(row, c++, styles.get("general"), resultBean.getHomeport());
                writeCell(row, c++, styles.get("date"),    resultBean.getSupportVisitDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("general"), resultBean.getSupportVisitTime());
                writeCell(row, c++, styles.get("general"), resultBean.getSupportVisitLoc());
                writeCell(row, c++, styles.get("general"), resultBean.getTrainer());
                writeCell(row, c++, styles.get("datetime"), resultBean.getLastUpdatedDate(), "date", "MM/dd/yyyy HH:mm:ss");
                writeCell(row, c++, styles.get("general"), resultBean.getContractNumber());
                writeCell(row, c++, styles.get("general"), resultBean.getLaptopIssue());
                writeCell(row, c++, styles.get("general"), resultBean.getScannerIssue());
                writeCell(row, c++, styles.get("general"), resultBean.getSoftwareIssue());
                writeCell(row, c++, styles.get("general"), resultBean.getSupportVisitLocNotes());
            }

            wb.write(out);
        } catch (Exception e) {
            logger.error(e);
        } finally { // dispose of temporary files backing this workbook on disk
            //try { wb.close(); } catch (Exception e) {}
            try { wb.dispose(); } catch (Exception e) {}
        }
    }

    public static void writeIssueSummaryByShipXlsx(OutputStream out, Connection conn, int projectPk, String contractNumber) {
        SXSSFWorkbook wb = null;
        ArrayList<SupportBean> resultList = SupportModel.getSummaryByShipList(conn, projectPk, contractNumber);

        try {
            wb = new SXSSFWorkbook();
            HashMap<String, CellStyle> styles = createStyles(wb);

            Sheet sheet = wb.createSheet("Issue Summary By Ship");
            Row row = null;
            Cell cell = null;

            //set column widths, the width is measured in units of 1/256th of a character width
            int[] widthArr = {77,19,12,12,17,37,9,9,9,9,9,9,9,9,9    };

            int c = 0;
            for (int w : widthArr) {
                sheet.setColumnWidth(c++, 256*w);
            }

            //Create header row
            row = sheet.createRow(0);
            String[] headerArr = {
                "Unit Name", "Homeport", "Total Issues", "Open Issues", "# of Support Visit", "Last Support Visit", "Breakdown By Category",
                null, null, null, null, null, null, null, null
                };

            c = 0;
            for (String value : headerArr) {
                writeCell(row, c++, styles.get("header"), value);
            }

            row = sheet.createRow(1);
            String[] header2Arr = {
                null, null, null, null, null, null,
                "LOGCOP", "FACET", "Kofax", "Administrative Receipt Tool", "FACET Update", "Laptop", "Follow-Up Training", "Backfile", "Misc / Other"
                };

            c = 0;
            for (String value : header2Arr) {
                writeCell(row, c++, styles.get("header"), value);
            }

            //Merge regions
            sheet.addMergedRegion(new CellRangeAddress(0,1,0,0));
            sheet.addMergedRegion(new CellRangeAddress(0,1,1,1));
            sheet.addMergedRegion(new CellRangeAddress(0,1,2,2));
            sheet.addMergedRegion(new CellRangeAddress(0,1,3,3));
            sheet.addMergedRegion(new CellRangeAddress(0,1,4,4));
            sheet.addMergedRegion(new CellRangeAddress(0,1,5,5));
            sheet.addMergedRegion(new CellRangeAddress(0,0,6,14));

            //freeze the pane
            sheet.createFreezePane(0,2);

            //Write data
            int r = 2;
            for (SupportBean resultBean : resultList) {
                c = 0;
                row = sheet.createRow(r++);
                writeCell(row, c++, styles.get("general"), CommonMethods.nvl(resultBean.getShipName(), "Non-ship specific issue"));
                writeCell(row, c++, styles.get("general"), resultBean.getHomeport());
                cell = row.createCell(c++); cell.setCellStyle(styles.get("center"));  if (!resultBean.getIssueCnt().equals("-")) cell.setCellValue(Double.parseDouble(resultBean.getIssueCnt()));
                cell = row.createCell(c++); cell.setCellStyle(styles.get("center"));  if (!resultBean.getOpenIssueCnt().equals("-")) cell.setCellValue(Double.parseDouble(resultBean.getOpenIssueCnt()));
                cell = row.createCell(c++); cell.setCellStyle(styles.get("center"));  if (!resultBean.getSupportVisitCnt().equals("-")) cell.setCellValue(Double.parseDouble(resultBean.getSupportVisitCnt()));

                writeCell(row, c++, styles.get("general"), CommonMethods.nes(resultBean.getSupportVisitDate()) + " " + CommonMethods.nes(resultBean.getCategory()));

                cell = row.createCell(c++); cell.setCellStyle(styles.get("center"));  if (!resultBean.getLogcopCnt().equals("-")) cell.setCellValue(Double.parseDouble(resultBean.getLogcopCnt()));
                cell = row.createCell(c++); cell.setCellStyle(styles.get("center"));  if (!resultBean.getFacetCnt().equals("-")) cell.setCellValue(Double.parseDouble(resultBean.getFacetCnt()));
                cell = row.createCell(c++); cell.setCellStyle(styles.get("center"));  if (!resultBean.getKofaxCnt().equals("-")) cell.setCellValue(Double.parseDouble(resultBean.getKofaxCnt()));
                cell = row.createCell(c++); cell.setCellStyle(styles.get("center"));  if (!resultBean.getDummyCnt().equals("-")) cell.setCellValue(Double.parseDouble(resultBean.getDummyCnt()));
                cell = row.createCell(c++); cell.setCellStyle(styles.get("center"));  if (!resultBean.getUpdateCnt().equals("-")) cell.setCellValue(Double.parseDouble(resultBean.getUpdateCnt()));
                cell = row.createCell(c++); cell.setCellStyle(styles.get("center"));  if (!resultBean.getLaptopCnt().equals("-")) cell.setCellValue(Double.parseDouble(resultBean.getLaptopCnt()));
                cell = row.createCell(c++); cell.setCellStyle(styles.get("center"));  if (!resultBean.getTrainingCnt().equals("-")) cell.setCellValue(Double.parseDouble(resultBean.getTrainingCnt()));
                cell = row.createCell(c++); cell.setCellStyle(styles.get("center"));  if (!resultBean.getBackfileCnt().equals("-")) cell.setCellValue(Double.parseDouble(resultBean.getBackfileCnt()));
                cell = row.createCell(c++); cell.setCellStyle(styles.get("center"));  if (!resultBean.getOtherCnt().equals("-")) cell.setCellValue(Double.parseDouble(resultBean.getOtherCnt()));
            }

            //Write totalRow
            row = sheet.createRow(r++);
            c = 0;
            writeCell(row, c++, styles.get("header"));
            writeCell(row, c++, styles.get("header"), "Totals:");
            writeCell(row, c++, styles.get("header"), "SUM(C3:C" + (r-1) + ")", "formula");
            writeCell(row, c++, styles.get("header"), "SUM(D3:D" + (r-1) + ")", "formula");
            writeCell(row, c++, styles.get("header"), "SUM(E3:E" + (r-1) + ")", "formula");
            writeCell(row, c++, styles.get("header"));
            writeCell(row, c++, styles.get("header"), "SUM(G3:G" + (r-1) + ")", "formula");
            writeCell(row, c++, styles.get("header"), "SUM(H3:H" + (r-1) + ")", "formula");
            writeCell(row, c++, styles.get("header"), "SUM(I3:I" + (r-1) + ")", "formula");
            writeCell(row, c++, styles.get("header"), "SUM(J3:J" + (r-1) + ")", "formula");
            writeCell(row, c++, styles.get("header"), "SUM(K3:K" + (r-1) + ")", "formula");
            writeCell(row, c++, styles.get("header"), "SUM(L3:L" + (r-1) + ")", "formula");
            writeCell(row, c++, styles.get("header"), "SUM(M3:M" + (r-1) + ")", "formula");
            writeCell(row, c++, styles.get("header"), "SUM(N3:N" + (r-1) + ")", "formula");
            writeCell(row, c++, styles.get("header"), "SUM(O3:O" + (r-1) + ")", "formula");

            //Merge regions
            sheet.addMergedRegion(new CellRangeAddress(r-1,r-1,0,1));

            wb.write(out);
        } catch (Exception e) {
            logger.error(e);
        } finally { // dispose of temporary files backing this workbook on disk
            //try { wb.close(); } catch (Exception e) {}
            try { wb.dispose(); } catch (Exception e) {}
        }
    }

    public static void writeLaptopXlsx(OutputStream out, Connection conn, int projectPk) {
        SXSSFWorkbook wb = null;
        ArrayList<HardwareBean> resultList = HardwareModel.getLaptopList(conn);

        try {
            wb = new SXSSFWorkbook();
            HashMap<String, CellStyle> styles = createStyles(wb);

            Sheet sheet = wb.createSheet("Laptops");
            Row row = null;

            //set column widths, the width is measured in units of 1/256th of a character width
            int c = 0;
            int[] widthArr = {55,19,22,35,24,17,12,17,18,14,14,18,12,15,15,50};

            for (int w : widthArr) {
                sheet.setColumnWidth(c++, 256*w);
            }

            //Create header row
            row = sheet.createRow(0);
            String[] headerArr = { "Assigned To", null, "Status", "Status Notes", "Computer Name", "Tag", "Customer", "Contract Number",
                "Product Name", "Model Number", "Serial Number", "MAC Address", "Origin", "Date Received", "Date Prepped", "Notes"
            };

            c = 0;
            for (String value : headerArr) {
                writeCell(row, c++, styles.get("header"), value);
            }

            row = sheet.createRow(1);
            String[] header2Arr = {
                "Unit Name", "Homeport",
                null, null, null, null, null, null, null, null, null, null, null, null, null, null
                };

            c = 0;
            for (String value : header2Arr) {
                writeCell(row, c++, styles.get("header"), value);
            }

            //Merge regions
            sheet.addMergedRegion(new CellRangeAddress(0,0,0,1));
            sheet.addMergedRegion(new CellRangeAddress(0,1,2,2));
            sheet.addMergedRegion(new CellRangeAddress(0,1,3,3));
            sheet.addMergedRegion(new CellRangeAddress(0,1,4,4));
            sheet.addMergedRegion(new CellRangeAddress(0,1,5,5));
            sheet.addMergedRegion(new CellRangeAddress(0,1,6,6));
            sheet.addMergedRegion(new CellRangeAddress(0,1,7,7));
            sheet.addMergedRegion(new CellRangeAddress(0,1,8,8));
            sheet.addMergedRegion(new CellRangeAddress(0,1,9,9));
            sheet.addMergedRegion(new CellRangeAddress(0,1,10,10));
            sheet.addMergedRegion(new CellRangeAddress(0,1,11,11));
            sheet.addMergedRegion(new CellRangeAddress(0,1,12,12));
            sheet.addMergedRegion(new CellRangeAddress(0,1,13,13));
            sheet.addMergedRegion(new CellRangeAddress(0,1,14,14));
            sheet.addMergedRegion(new CellRangeAddress(0,1,15,15));

            //freeze the pane
            sheet.createFreezePane(0,2);

            //Write data
            int r = 2;
            for (HardwareBean resultBean : resultList) {
                row = sheet.createRow(r++);
                c = 0;
                writeCell(row, c++, styles.get("general"), resultBean.getShipName());
                writeCell(row, c++, styles.get("general"), resultBean.getHomeport());
                writeCell(row, c++, styles.get("general"), resultBean.getStatus());
                writeCell(row, c++, styles.get("general"), resultBean.getStatusNotes());
                writeCell(row, c++, styles.get("general"), resultBean.getComputerName());
                writeCell(row, c++, styles.get("general"), resultBean.getTag());
                writeCell(row, c++, styles.get("general"), resultBean.getCustomer());
                writeCell(row, c++, styles.get("general"), resultBean.getContractNumber());
                writeCell(row, c++, styles.get("general"), resultBean.getProductName());
                writeCell(row, c++, styles.get("general"), resultBean.getModelNumber());
                writeCell(row, c++, styles.get("general"), resultBean.getSerialNumber());
                writeCell(row, c++, styles.get("general"), resultBean.getMacAddress());
                writeCell(row, c++, styles.get("general"), resultBean.getOrigin());

                writeCell(row, c++, styles.get("date"), resultBean.getReceivedDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("date"), resultBean.getPreppedDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("wrapped"), resultBean.getNotes());
            }

            wb.write(out);
        } catch (Exception e) {
            logger.error(e);
        } finally { // dispose of temporary files backing this workbook on disk
            //try { wb.close(); } catch (Exception e) {}
            try { wb.dispose(); } catch (Exception e) {}
        }
    }

    public static void writeScannerXlsx(OutputStream out, Connection conn, int projectPk) {
        SXSSFWorkbook wb = null;
        ArrayList<HardwareBean> resultList = HardwareModel.getScannerList(conn);

        try {
            wb = new SXSSFWorkbook();
            HashMap<String, CellStyle> styles = createStyles(wb);

            Sheet sheet = wb.createSheet("Scanners");
            Row row = null;

            //set column widths, the width is measured in units of 1/256th of a character width
            int c = 0;
            int[] widthArr = {55, 19, 22, 35, 24, 17, 12, 17, 18, 18, 14, 12, 15, 15, 50};

            for (int w : widthArr) {
                sheet.setColumnWidth(c++, 256*w);
            }

            //Create header row
            row = sheet.createRow(0);
            String[] headerArr = {
                "Assigned To", null, "Status", "Status Notes", "Computer Name", "Tag", "Customer", "Contract Number", "Product Name", "Model Number",
                "Serial Number", "Origin", "Date Received", "Date Prepped", "Notes"
                };

            c = 0;
            for (String value : headerArr) {
                writeCell(row, c++, styles.get("header"), value);
            }

            row = sheet.createRow(1);
            String[] header2Arr = {
                "Unit Name", "Homeport",
                null, null, null, null, null, null, null, null, null, null, null, null, null
                };

            c = 0;
            for (String value : header2Arr) {
                writeCell(row, c++, styles.get("header"), value);
            }

            //Merge regions
            sheet.addMergedRegion(new CellRangeAddress(0,0,0,1));
            sheet.addMergedRegion(new CellRangeAddress(0,1,2,2));
            sheet.addMergedRegion(new CellRangeAddress(0,1,3,3));
            sheet.addMergedRegion(new CellRangeAddress(0,1,4,4));
            sheet.addMergedRegion(new CellRangeAddress(0,1,5,5));
            sheet.addMergedRegion(new CellRangeAddress(0,1,6,6));
            sheet.addMergedRegion(new CellRangeAddress(0,1,7,7));
            sheet.addMergedRegion(new CellRangeAddress(0,1,8,8));
            sheet.addMergedRegion(new CellRangeAddress(0,1,9,9));
            sheet.addMergedRegion(new CellRangeAddress(0,1,10,10));
            sheet.addMergedRegion(new CellRangeAddress(0,1,11,11));
            sheet.addMergedRegion(new CellRangeAddress(0,1,12,12));
            sheet.addMergedRegion(new CellRangeAddress(0,1,13,13));
            sheet.addMergedRegion(new CellRangeAddress(0,1,14,14));

            //freeze the pane
            sheet.createFreezePane(0,2);

            //Write data
            int r = 2;
            for (HardwareBean resultBean : resultList) {
                row = sheet.createRow(r++);
                c = 0;
                writeCell(row, c++, styles.get("general"), resultBean.getShipName());
                writeCell(row, c++, styles.get("general"), resultBean.getHomeport());
                writeCell(row, c++, styles.get("general"), resultBean.getStatus());
                writeCell(row, c++, styles.get("general"), resultBean.getStatusNotes());
                writeCell(row, c++, styles.get("general"), resultBean.getComputerName());
                writeCell(row, c++, styles.get("general"), resultBean.getTag());
                writeCell(row, c++, styles.get("general"), resultBean.getCustomer());
                writeCell(row, c++, styles.get("general"), resultBean.getContractNumber());
                writeCell(row, c++, styles.get("general"), resultBean.getProductName());
                writeCell(row, c++, styles.get("general"), resultBean.getModelNumber());
                writeCell(row, c++, styles.get("general"), resultBean.getSerialNumber());
                writeCell(row, c++, styles.get("general"), resultBean.getOrigin());
                writeCell(row, c++, styles.get("date"), resultBean.getReceivedDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("date"), resultBean.getPreppedDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("wrapped"), resultBean.getNotes());
            }

            wb.write(out);
        } catch (Exception e) {
            logger.error(e);
        } finally { // dispose of temporary files backing this workbook on disk
            //try { wb.close(); } catch (Exception e) {}
            try { wb.dispose(); } catch (Exception e) {}
        }
    }

    public static void writeMiscHardwareXlsx(OutputStream out, Connection conn, int projectPk) {
        SXSSFWorkbook wb = null;
        ArrayList<HardwareBean> resultList = HardwareModel.getMiscList(conn);

        try {
            wb = new SXSSFWorkbook();
            HashMap<String, CellStyle> styles = createStyles(wb);

            Sheet sheet = wb.createSheet("Misc Hardware");
            Row row = null;

            //set column widths, the width is measured in units of 1/256th of a character width
            int c = 0;
            int[] widthArr = {30,15,9,17,17,10,15,17,12,17,15,18,50};

            for (int w : widthArr) {
                sheet.setColumnWidth(c++, 256*w);
            }

            //Create header row
            row = sheet.createRow(0);
            String[] headerArr = {
                "Product Name", "Product Type", "Tag", "Model Number", "Serial Number", "Origin", "Date Received",
                "Warranty Expiration Date", "Customer", "Contract Number", "Status", "Status Notes", "Notes"
            };

            c = 0;
            for (String value : headerArr) {
                writeCell(row, c++, styles.get("header"), value);
            }

            //freeze the pane
            sheet.createFreezePane(0,1);

            //Write data
            int r = 1;
            for (HardwareBean resultBean : resultList) {
                row = sheet.createRow(r++);
                c = 0;
                writeCell(row, c++, styles.get("general"), resultBean.getProductType());
                writeCell(row, c++, styles.get("general"), resultBean.getProductName());
                writeCell(row, c++, styles.get("general"), resultBean.getTag());
                writeCell(row, c++, styles.get("general"), resultBean.getModelNumber());
                writeCell(row, c++, styles.get("general"), resultBean.getSerialNumber());
                writeCell(row, c++, styles.get("general"), resultBean.getOrigin());
                writeCell(row, c++, styles.get("date"), resultBean.getReceivedDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("date"), resultBean.getReceivedDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("general"), resultBean.getCustomer());
                writeCell(row, c++, styles.get("general"), resultBean.getContractNumber());
                writeCell(row, c++, styles.get("general"), resultBean.getStatus());
                writeCell(row, c++, styles.get("wrapped"), resultBean.getStatusNotes());
                writeCell(row, c++, styles.get("wrapped"), resultBean.getNotes());
            }

            wb.write(out);
        } catch (Exception e) {
            logger.error(e);
        } finally { // dispose of temporary files backing this workbook on disk
            //try { wb.close(); } catch (Exception e) {}
            try { wb.dispose(); } catch (Exception e) {}
        }
    }

    public static void writeKofaxLicenseXlsx(OutputStream out, Connection conn, int projectPk) {
        SXSSFWorkbook wb = null;
        ArrayList<SoftwareBean> resultList = SoftwareModel.getKofaxLicenseList(conn);
        try {
            wb = new SXSSFWorkbook();
            HashMap<String, CellStyle> styles = createStyles(wb);

            Sheet sheet = wb.createSheet("Kofax Licenses");
            Row row = null;

            //set column widths, the width is measured in units of 1/256th of a character width
            int c = 0;
            int[] widthArr = {35,13,14,15,17,15,15,13,50};

            for (int w : widthArr) {
                sheet.setColumnWidth(c++, 256*w);
            }

            //Create header row
            row = sheet.createRow(0);
            String[] headerArr = {"Assigned To", "License Key", "Product Code", "Customer", "Contract Number", "Date Received", "License Expiration Date", "Internal Use Only", "Notes"};

            c = 0;
            for (String value : headerArr) {
                writeCell(row, c++, styles.get("header"), value);
            }

            //freeze the pane
            sheet.createFreezePane(0,1);

            //Write data
            int r = 1;
            for (SoftwareBean resultBean : resultList) {
                row = sheet.createRow(r++);
                c = 0;
                writeCell(row, c++, styles.get("general"), resultBean.getComputerName());
                writeCell(row, c++, styles.get("general"), resultBean.getLicenseKey());
                writeCell(row, c++, styles.get("general"), resultBean.getProductCode());
                writeCell(row, c++, styles.get("general"), resultBean.getCustomer());
                writeCell(row, c++, styles.get("general"), resultBean.getContractNumber());
                writeCell(row, c++, styles.get("date"), resultBean.getReceivedDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("date"), resultBean.getLicenseExpiryDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("center"), resultBean.getInternalUseInd());
                writeCell(row, c++, styles.get("wrapped"), resultBean.getNotes());
            }

            wb.write(out);
        } catch (Exception e) {
            logger.error(e);
        } finally { // dispose of temporary files backing this workbook on disk
            //try { wb.close(); } catch (Exception e) {}
            try { wb.dispose(); } catch (Exception e) {}
        }
    }

    public static void writeMsOfficeLicenseXlsx(OutputStream out, Connection conn, int projectPk) {
        SXSSFWorkbook wb = null;
        ArrayList<SoftwareBean> resultList = SoftwareModel.getMsOfficeLicenseList(conn);

        try {
            wb = new SXSSFWorkbook();
            HashMap<String, CellStyle> styles = createStyles(wb);

            Sheet sheet = wb.createSheet("MS Office Licenses");
            Row row = null;

            //set column widths, the width is measured in units of 1/256th of a character width
            int c = 0;
            int[] widthArr = {35,36,15,15,17,15,50};

            for (int w : widthArr) {
                sheet.setColumnWidth(c++, 256*w);
            }

            //Create header row
            row = sheet.createRow(0);
            String[] headerArr = {"Product Name", "License Key", "Seats Activated", "Customer", "Contract Number", "Date Received", "Notes"};

            c = 0;
            for (String value : headerArr) {
                writeCell(row, c++, styles.get("header"), value);
            }

            //freeze the pane
            sheet.createFreezePane(0,1);

            //Write data
            int r = 1;
            for (SoftwareBean resultBean : resultList) {
                row = sheet.createRow(r++);
                c = 0;
                writeCell(row, c++, styles.get("general"), resultBean.getProductName());
                writeCell(row, c++, styles.get("general"), resultBean.getLicenseKey());
                writeCell(row, c++, styles.get("center"), resultBean.getInstalledCnt(), "double");
                writeCell(row, c++, styles.get("general"), resultBean.getCustomer());
                writeCell(row, c++, styles.get("general"), resultBean.getContractNumber());
                writeCell(row, c++, styles.get("date"), resultBean.getReceivedDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("wrapped"), resultBean.getNotes());
            }

            wb.write(out);
        } catch (Exception e) {
            logger.error(e);
        } finally { // dispose of temporary files backing this workbook on disk
            //try { wb.close(); } catch (Exception e) {}
            try { wb.dispose(); } catch (Exception e) {}
        }
    }

    public static void writeVrsLicenseXlsx(OutputStream out, Connection conn, int projectPk) {
        SXSSFWorkbook wb = null;
        ArrayList<SoftwareBean> resultList = SoftwareModel.getVrsLicenseList(conn);

        try {
            wb = new SXSSFWorkbook();
            HashMap<String, CellStyle> styles = createStyles(wb);

            Sheet sheet = wb.createSheet("VRS Licenses");
            Row row = null;

            //set column widths, the width is measured in units of 1/256th of a character width
            int c = 0;
            int[] widthArr = {35,13,14,15,17,15,50};

            for (int w : widthArr) {
                sheet.setColumnWidth(c++, 256*w);
            }

            //Create header row
            row = sheet.createRow(0);
            String[] headerArr = {"Assigned To", "License Key", "Product Code", "Customer", "Contract Number", "Date Received", "Notes"};

            c = 0;
            for (String value : headerArr) {
                writeCell(row, c++, styles.get("header"), value);
            }

            //freeze the pane
            sheet.createFreezePane(0,1);

            //Write data
            int r = 1;
            for (SoftwareBean resultBean : resultList) {
                row = sheet.createRow(r++);
                c = 0;
                writeCell(row, c++, styles.get("general"), resultBean.getComputerName());
                writeCell(row, c++, styles.get("general"), resultBean.getLicenseKey());
                writeCell(row, c++, styles.get("general"), resultBean.getProductCode());
                writeCell(row, c++, styles.get("general"), resultBean.getCustomer());
                writeCell(row, c++, styles.get("general"), resultBean.getContractNumber());
                writeCell(row, c++, styles.get("date"), resultBean.getReceivedDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("wrapped"), resultBean.getNotes());
            }

            wb.write(out);
        } catch (Exception e) {
            logger.error(e);
        } finally { // dispose of temporary files backing this workbook on disk
            //try { wb.close(); } catch (Exception e) {}
            try { wb.dispose(); } catch (Exception e) {}
        }
    }

    public static void writeMiscLicenseXlsx(OutputStream out, Connection conn, int projectPk) {
        SXSSFWorkbook wb = null;
        ArrayList<SoftwareBean> resultList = SoftwareModel.getMiscLicenseList(conn);
        try {
            wb = new SXSSFWorkbook();
            HashMap<String, CellStyle> styles = createStyles(wb);

            Sheet sheet = wb.createSheet("Misc Licenses");
            Row row = null;

            //set column widths, the width is measured in units of 1/256th of a character width
            int c = 0;
            int[] widthArr = {30,20,15,15,15,17,17,15,30,50};

            for (int w : widthArr) {
                sheet.setColumnWidth(c++, 256*w);
            }

            //Create header row
            row = sheet.createRow(0);
            String[] headerArr = {
                "Product Name", "Product Key", "Seats Activated", "Date Received", "License Expiration Date", "Customer",
                "Contract Number", "Status", "Status Notes", "Notes"
            };

            c = 0;
            for (String value : headerArr) {
                writeCell(row, c++, styles.get("header"), value);
            }

            //freeze the pane
            sheet.createFreezePane(0,1);

            //Write data
            int r = 1;
            for (SoftwareBean resultBean : resultList) {
                row = sheet.createRow(r++);
                c = 0;
                writeCell(row, c++, styles.get("general"), resultBean.getProductName());
                writeCell(row, c++, styles.get("general"), resultBean.getProductKey());
                writeCell(row, c++, styles.get("center"), resultBean.getInstalledCnt(), "double");
                writeCell(row, c++, styles.get("date"), resultBean.getReceivedDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("date"), resultBean.getLicenseExpiryDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("general"), resultBean.getCustomer());
                writeCell(row, c++, styles.get("general"), resultBean.getContractNumber());
                writeCell(row, c++, styles.get("general"), resultBean.getStatus());
                writeCell(row, c++, styles.get("wrapped"), resultBean.getStatusNotes());
                writeCell(row, c++, styles.get("wrapped"), resultBean.getNotes());
            }

            wb.write(out);
        } catch (Exception e) {
            logger.error(e);
        } finally { // dispose of temporary files backing this workbook on disk
            //try { wb.close(); } catch (Exception e) {}
            try { wb.dispose(); } catch (Exception e) {}
        }
    }

    public static void writeBackfileWorkflowXlsx(OutputStream out, Connection conn, BackfileBean inputBean) {
        SXSSFWorkbook wb = null;

        try {
            wb = new SXSSFWorkbook();
            HashMap<String, CellStyle> styles = createStyles(wb);

            writeBackfileWorkflowSheet(wb, styles, conn, inputBean, "inProd");
            writeBackfileWorkflowSheet(wb, styles, conn, inputBean, "pending");
            writeBackfileWorkflowSheet(wb, styles, conn, inputBean, "overdue");
            writeBackfileWorkflowSheet(wb, styles, conn, inputBean, "completed");
            writeBackfileWorkflowSheet(wb, styles, conn, inputBean, "notRequired");

            wb.write(out);
        } catch (Exception e) {
            logger.error(e);
        } finally { // dispose of temporary files backing this workbook on disk
            //try { wb.close(); } catch (Exception e) {}
            try { wb.dispose(); } catch (Exception e) {}
        }
    }

    private static void writeBackfileWorkflowSheet(SXSSFWorkbook wb, HashMap<String, CellStyle> styles, Connection conn, BackfileBean inputBean, String type) throws Exception {
        Sheet sheet = null;
        Row row = null;
        ArrayList<BackfileBean> resultList = BackfileModel.getSummaryList(conn, inputBean, "ship_name", "ASC", type);

        switch (type) {
            case "pending":
                sheet = wb.createSheet("Pending");
                break;
            case "inProd":
                sheet = wb.createSheet("In Production");
                break;
            case "overdue":
                sheet = wb.createSheet("Overdue");
                break;
            case "completed":
                sheet = wb.createSheet("Completed");
                break;
            case "notRequired":
                sheet = wb.createSheet("Not Required");
                break;
        }

        //set column widths, the width is measured in units of 1/256th of a character width
        int[] widthArr = {
            55, 11, 8, 20, 5, 5, 5, 5, 5, 5, 5, 8, 6, 5, 5, 5, 5, 5, 5, 5, 8, 6, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14,
            14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 100
        };

        int c = 0;
        for (int w : widthArr) {
            sheet.setColumnWidth(c++, 256*w);
        }

        //Create header row
        row = sheet.createRow(0);
        String[] header1NameArr = {
            "Unit Information", null, null, null,
            "PSHI Boxes Received", null, null, null, null, null, null, null, null,
            "Boxes Sent to Scanning", null, null, null, null, null, null, null, null,
            "General Information", null, null, null,
            "Initial Workflow", null, null,
            "FY16", null,
            "FY15", null,
            "FY14/13 Scanning", null,
            "FY12/11",
            "Final Steps", null, null, null, null, null,
            "Return to Unit", null, null,
            "Comments"
            };

        c = 0;
        for (String value : header1NameArr) {
            writeCell(row, c++, styles.get("header"), value);
        }

        //Write section borders
        writeSectionBorder(sheet,0,4);
        writeSectionBorder(sheet,0,13);
        writeSectionBorder(sheet,0,22);
        writeSectionBorder(sheet,0,26);
        writeSectionBorder(sheet,0,29);
        writeSectionBorder(sheet,0,31);
        writeSectionBorder(sheet,0,33);
        writeSectionBorder(sheet,0,36);
        writeSectionBorder(sheet,0,42);
        writeSectionBorder(sheet,0,45);

        row = sheet.createRow(1);
        String[] header2NameArr = {
            "Unit Name", "Type/Hull", "TYCOM", "Homeport", "FY16",
            "FY15", "FY14", "FY13", "FY12", "FY11",
            "FY10", "Other / Reports", "Total", "FY16", "FY15",
            "FY14", "FY13", "FY12", "FY11", "FY10",
            "Other / Reports", "Total", "Overall Due Date", "FY14/13 Due Date", "Scheduled Training Date",
            "Completed Date", "Backfile Date Requested", "Date Received By PSHI", "Date Delivered to Scanning", "FY16 Completed Date",
            "FY16 CD Sent for Customer Date", "FY15 Completed Date", "FY15 CD Sent for Customer Date", "FY14/13 Completed Date", "FY14/13 CD Sent for Customer Date",
            "FY12/11 Completed Date", "Extract File Created", "All backfile CD mailed to Cust/SD", "Verify All Backfiles Installed in FACET", "All Backfile CD Delivered to LogCop",
            "Verified LOGCOP Backfile Uploaded", "Final Backfile Report Generated and Given to CompacFlt", "Return to Unit", "Backfile Returned Date", "Client Received Confirmation Date",
            null
        };

        c = 0;
        for (String value : header2NameArr) {
            writeCell(row, c++, styles.get("header"), value);
        }

        //Merge regions
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,3));
        sheet.addMergedRegion(new CellRangeAddress(0,0,4,12));
        sheet.addMergedRegion(new CellRangeAddress(0,0,13,21));
        sheet.addMergedRegion(new CellRangeAddress(0,0,22,25));
        sheet.addMergedRegion(new CellRangeAddress(0,0,26,28));
        sheet.addMergedRegion(new CellRangeAddress(0,0,29,30));
        sheet.addMergedRegion(new CellRangeAddress(0,0,31,32));
        sheet.addMergedRegion(new CellRangeAddress(0,0,33,34));
        sheet.addMergedRegion(new CellRangeAddress(0,0,36,41));
        sheet.addMergedRegion(new CellRangeAddress(0,0,42,44));
        sheet.addMergedRegion(new CellRangeAddress(0,1,45,45));

        //Write section borders
        writeSectionBorder(sheet,1,4);
        writeSectionBorder(sheet,1,13);
        writeSectionBorder(sheet,1,22);
        writeSectionBorder(sheet,1,26);
        writeSectionBorder(sheet,1,29);
        writeSectionBorder(sheet,1,31);
        writeSectionBorder(sheet,1,33);
        writeSectionBorder(sheet,1,36);
        writeSectionBorder(sheet,1,42);
        writeSectionBorder(sheet,1,45);

        //freeze the pane
        sheet.createFreezePane(0,2);

        //Write data
        int r = 2;
        for (BackfileBean resultBean : resultList) {
            row = sheet.createRow(r++);
            c = 0;
            writeCell(row, c++, styles.get("general"), resultBean.getShipName());
            writeCell(row, c++, styles.get("general"), CommonMethods.nes(resultBean.getType()) + " " + CommonMethods.nes(resultBean.getHull()));
            writeCell(row, c++, styles.get("general"), resultBean.getTycomDisplay());
            writeCell(row, c++, styles.get("general"), resultBean.getHomeport());

            //Write section borders
            writeSectionBorder(sheet,r-1,4);

            if (type.equals("notRequired")) {
                writeCell(row, c++, styles.get("greyed"), "Not Required");
                do {
                    writeCell(row, c++, styles.get("greyed"));
                } while (c < 45);

                //Merge regions
                sheet.addMergedRegion(new CellRangeAddress(r-1,r-1,4,44));
            } else {
                writeCell(row, c++, styles.get("center"), resultBean.getFy16PshiBoxCnt(), "double");
                writeCell(row, c++, styles.get("center"), resultBean.getFy15PshiBoxCnt(), "double");
                writeCell(row, c++, styles.get("center"), resultBean.getFy14PshiBoxCnt(), "double");
                writeCell(row, c++, styles.get("center"), resultBean.getFy13PshiBoxCnt(), "double");
                writeCell(row, c++, styles.get("center"), resultBean.getFy12PshiBoxCnt(), "double");
                writeCell(row, c++, styles.get("center"), resultBean.getFy11PshiBoxCnt(), "double");
                writeCell(row, c++, styles.get("center"), resultBean.getFy10PshiBoxCnt(), "double");
                writeCell(row, c++, styles.get("center"), resultBean.getOtherPshiBoxCnt(), "double");
                writeCell(row, c++, styles.get("center"), "SUM(E" + String.valueOf(c) + ":L" + String.valueOf(c) + ")", "formula");

                writeCell(row, c++, styles.get("center"), resultBean.getFy16BoxCnt(), "double");
                writeCell(row, c++, styles.get("center"), resultBean.getFy15BoxCnt(), "double");
                writeCell(row, c++, styles.get("center"), resultBean.getFy14BoxCnt(), "double");
                writeCell(row, c++, styles.get("center"), resultBean.getFy13BoxCnt(), "double");
                writeCell(row, c++, styles.get("center"), resultBean.getFy12BoxCnt(), "double");
                writeCell(row, c++, styles.get("center"), resultBean.getFy11BoxCnt(), "double");
                writeCell(row, c++, styles.get("center"), resultBean.getFy10BoxCnt(), "double");
                writeCell(row, c++, styles.get("center"), resultBean.getOtherBoxCnt(), "double");
                writeCell(row, c++, styles.get("center"), "SUM(N" + String.valueOf(c) + ":U" + String.valueOf(c) + ")", "formula");

                writeCell(row, c++, styles.get("date"), resultBean.getDueDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("date"), resultBean.getFy1314DueDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("date"), resultBean.getSchedTrainingDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("date"), resultBean.getCompletedDate(), "date", "MM/dd/yyyy");

                writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getRequestedDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), resultBean.getRequestedDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getReceivedDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), resultBean.getReceivedDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getScanningDeliveredDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), resultBean.getScanningDeliveredDate(), "date", "MM/dd/yyyy");

                writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getFy16CompletedDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), resultBean.getFy16CompletedDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getFy16MailedDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), resultBean.getFy16MailedDate(), "date", "MM/dd/yyyy");

                writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getFy15CompletedDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), resultBean.getFy15CompletedDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getFy15MailedDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), resultBean.getFy15MailedDate(), "date", "MM/dd/yyyy");

                writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getFy1314CompletedDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), resultBean.getFy1314CompletedDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getFy1314BurnedDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), resultBean.getFy1314BurnedDate(), "date", "MM/dd/yyyy");

                writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getFy1112CompletedDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), resultBean.getFy1112CompletedDate(), "date", "MM/dd/yyyy");

                writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getExtractDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), resultBean.getExtractDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getFy1314MailedDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), resultBean.getFy1314MailedDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getLaptopInstalledDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), resultBean.getLaptopInstalledDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getLogcopDeliveredDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), resultBean.getLogcopDeliveredDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getLogcopUploadedDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), resultBean.getLogcopUploadedDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getFinalReportDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), resultBean.getFinalReportDate(), "date", "MM/dd/yyyy");

                if (CommonMethods.nes(resultBean.getReturnInd()).equals("Y")) {
                    writeCell(row, c++, styles.get("center"), "Y");
                    writeCell(row, c++, styles.get("date"), resultBean.getReturnedDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get("date"), resultBean.getReturnConfirmedDate(), "date", "MM/dd/yyyy");
                } else {
                    writeCell(row, c++, styles.get("center"), "N");
                    writeCell(row, c++, styles.get("greyed"));
                    writeCell(row, c++, styles.get("greyed"));
                }

                //Write section borders
                writeSectionBorder(sheet,r-1,4);
                writeSectionBorder(sheet,r-1,13);
                writeSectionBorder(sheet,r-1,22);
                writeSectionBorder(sheet,r-1,26);
                writeSectionBorder(sheet,r-1,29);
                writeSectionBorder(sheet,r-1,31);
                writeSectionBorder(sheet,r-1,33);
                writeSectionBorder(sheet,r-1,36);
                writeSectionBorder(sheet,r-1,42);
            }

            writeCell(row, c++, styles.get("wrapped"), resultBean.getComments());
            writeSectionBorder(sheet,r-1,45);

        }
    }

    public static void writeUnitContactXlsx(OutputStream out, Connection conn, int projectPk) {
        SXSSFWorkbook wb = null;
        ArrayList<ShipBean> resultList = ShipModel.getAllShipPocList(conn);

        try {
            wb = new SXSSFWorkbook();
            HashMap<String, CellStyle> styles = createStyles(wb);

            Sheet sheet = wb.createSheet("Unit POCs");
            Row row = null;

            //set column widths, the width is measured in units of 1/256th of a character width
            int[] widthArr = {52,9,11,20,20,42,12,35,35,21,21};

            int c = 0;
            for (int w : widthArr) {
                sheet.setColumnWidth(c++, 256*w);
            }

            //Create header row
            row = sheet.createRow(0);
            String[] headerArr = {"Unit", "Primary POC?", "Rank", "Last Name", "First Name", "Job Title", "Division", "Email", "Alt Email", "Work Number", "Cell/Alt Number"};

            c = 0;
            for (String value : headerArr) {
                writeCell(row, c++, styles.get("header"), value);
            }

            //freeze the pane
            sheet.createFreezePane(0,1);

            //Write data
            int r = 1;
            for (ShipBean shipBean : resultList) {
                for (UserBean resultBean : shipBean.getPocList()) {
                    row = sheet.createRow(r++);
                    c = 0;
                    writeCell(row, c++, styles.get("general"), shipBean.getShipNameTypeHull());
                    writeCell(row, c++, styles.get("center"), resultBean.getIsPrimaryPoc());
                    writeCell(row, c++, styles.get("general"), resultBean.getRank());
                    writeCell(row, c++, styles.get("general"), resultBean.getLastName());
                    writeCell(row, c++, styles.get("general"), resultBean.getFirstName());
                    writeCell(row, c++, styles.get("general"), resultBean.getTitle());
                    writeCell(row, c++, styles.get("general"), resultBean.getDept());
                    writeHyperlink(wb, row, c++, styles, resultBean.getEmail());
                    writeHyperlink(wb, row, c++, styles, resultBean.getAltEmail());
                    writeCell(row, c++, styles.get("general"), resultBean.getWorkNumber());
                    writeCell(row, c++, styles.get("general"), resultBean.getCellNumber());
                }
            }

            wb.write(out);
        } catch (Exception e) {
            logger.error(e);
        } finally { // dispose of temporary files backing this workbook on disk
            //try { wb.close(); } catch (Exception e) {}
            try { wb.dispose(); } catch (Exception e) {}
        }
    }

    public static void writeTaskXlsx(OutputStream out, Connection conn, ProjectBean inputBean, String sortBy, String sortDir, int projectPk) {
        SXSSFWorkbook wb = null;
        ArrayList<ProjectBean> resultList = ProjectModel.getTaskList(conn, projectPk, inputBean, sortBy, sortDir);
        try {
            wb = new SXSSFWorkbook();
            HashMap<String, CellStyle> styles = createStyles(wb);

            Sheet sheet = wb.createSheet("Tasks");
            Row row = null;

            //Conditional Formatting
            SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
            ConditionalFormattingRule rule;
            FontFormatting fontFormat;

            CellRangeAddress[] statusCol = {new CellRangeAddress(0,65535,4,4)};
            CellRangeAddress[] priorityCol = {new CellRangeAddress(0,65535,5,5)};

            rule = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"Completed\"");
            fontFormat = rule.createFontFormatting();
            fontFormat.setFontColorIndex(IndexedColors.GREY_80_PERCENT.index);
            sheetCF.addConditionalFormatting(statusCol, rule);

            rule = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"Resolved\"");
            fontFormat = rule.createFontFormatting();
            fontFormat.setFontColorIndex(IndexedColors.RED.index);
            sheetCF.addConditionalFormatting(statusCol, rule);

            rule = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"In Progress\"");
            fontFormat = rule.createFontFormatting();
            fontFormat.setFontColorIndex(IndexedColors.ORANGE.index);
            sheetCF.addConditionalFormatting(statusCol, rule);

            rule = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"Not Started\"");
            fontFormat = rule.createFontFormatting();
            fontFormat.setFontColorIndex(IndexedColors.GREEN.index);
            sheetCF.addConditionalFormatting(statusCol, rule);

            rule = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"Completed\"");
            fontFormat = rule.createFontFormatting();
            fontFormat.setFontColorIndex(IndexedColors.GREY_80_PERCENT.index);
            sheetCF.addConditionalFormatting(priorityCol, rule);

            rule = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"1-Critical\"");
            fontFormat = rule.createFontFormatting();
            fontFormat.setFontColorIndex(IndexedColors.PLUM.index);
            sheetCF.addConditionalFormatting(priorityCol, rule);

            rule = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"2-High\"");
            fontFormat = rule.createFontFormatting();
            fontFormat.setFontColorIndex(IndexedColors.RED.index);
            sheetCF.addConditionalFormatting(priorityCol, rule);

            rule = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"3-Medium\"");
            fontFormat = rule.createFontFormatting();
            fontFormat.setFontColorIndex(IndexedColors.ORANGE.index);
            sheetCF.addConditionalFormatting(priorityCol, rule);

            rule = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"4-Low\"");
            fontFormat = rule.createFontFormatting();
            fontFormat.setFontColorIndex(IndexedColors.GREEN.index);
            sheetCF.addConditionalFormatting(priorityCol, rule);

            //set column widths, the width is measured in units of 1/256th of a character width
            int[] widthArr = {6, 50, 60, 17, 11, 10, 12, 34, 18, 8, 11, 8, 10, 18, 15, 20, 15, 14, 35, 19, 14, 14, 14, 20, 43, 43, 19, 14};

            int c = 0;
            for (int w : widthArr) {
                sheet.setColumnWidth(c++, 256*w);
            }

            //Create header row
            row = sheet.createRow(0);
            String[] headerArr = {
                "Task #", "Title", "Description", "Category", "Status", "Priority", "Target Quarter", "Area of Effort", "Type of Effort",
                "Level of Effort", "Client Approved", "Client Priority", "PSHI Approved", "Recommendation", "Documentation Updated?",
                "Documentation Notes", "Fixed/Added in Version", "Deployed Date", "Ship", "Person Assigned", "Created Date", "Due Date",
                "Completed Date", "Contract Number", "Sub Tasks", "Notes", "Last Updated By", "Last Updated Date"
                };

            c = 0;
            for (String value : headerArr) {
                writeCell(row, c++, styles.get("header"), value);
            }

            //freeze the pane
            sheet.createFreezePane(0,1);

            //Write data
            int r = 1;
            for (ProjectBean resultBean : resultList) {
                row = sheet.createRow(r++);
                c = 0;
                writeCell(row, c++, styles.get("general"), resultBean.getTaskPk(), "double");
                writeCell(row, c++, styles.get("general"), resultBean.getTitle());
                writeCell(row, c++, styles.get("wrapped"), resultBean.getDescription());
                writeCell(row, c++, styles.get("general"), resultBean.getCategory());
                writeCell(row, c++, styles.get("general"), resultBean.getStatus());
                writeCell(row, c++, styles.get("general"), resultBean.getPriority());

                if (resultBean.getCategory().equals("Future Requests")) {
                    writeCell(row, c++, styles.get("center"), resultBean.getQuarterYear());
                    writeCell(row, c++, styles.get("general"), resultBean.getEffortArea());
                    writeCell(row, c++, styles.get("general"), resultBean.getEffortType());
                    writeCell(row, c++, styles.get("center"), resultBean.getLoe());
                    writeCell(row, c++, styles.get("center"), resultBean.getIsClientApproved().equals("Y") ? "Y" : null);
                    writeCell(row, c++, styles.get("general"), resultBean.getClientPriority());
                    writeCell(row, c++, styles.get("center"), resultBean.getIsPshiApproved().equals("Y") ? "Y" : null);
                    writeCell(row, c++, styles.get("general"), resultBean.getRecommendation());
                    writeCell(row, c++, styles.get("center"), resultBean.getDocUpdatedInd());
                    writeCell(row, c++, styles.get("general"), resultBean.getDocNotes());
                    writeCell(row, c++, styles.get("wrapped"), resultBean.getVersionIncluded());
                    writeCell(row, c++, styles.get("date"), resultBean.getDeployedDate(), "date", "MM/dd/yyyy");
                } else {
                    for (int i = 0; i < 12; i++) {
                        writeCell(row, c++, styles.get("greyed"));
                    }
                }

                writeCell(row, c++, styles.get("general"), resultBean.getShipName());
                writeCell(row, c++, styles.get("general"), resultBean.getPersonAssigned());
                writeCell(row, c++, styles.get("date"), resultBean.getCreatedDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("date"), resultBean.getDueDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("date"), resultBean.getCompletedDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("general"), resultBean.getContractNumber());
                writeCell(row, c++, styles.get("wrapped"), resultBean.getSubTasks());
                writeCell(row, c++, styles.get("wrapped"), resultBean.getNotes());
                writeCell(row, c++, styles.get("general"), resultBean.getLastUpdatedBy());
                writeCell(row, c++, styles.get("date"), resultBean.getLastUpdatedDate(), "date", "MM/dd/yyyy");
            }

            wb.write(out);
        } catch (Exception e) {
            logger.error(e);
        } finally { // dispose of temporary files backing this workbook on disk
            //try { wb.close(); } catch (Exception e) {}
            try { wb.dispose(); } catch (Exception e) {}
        }
    }

    public static void writeDecomWorkflowXlsx(OutputStream out, Connection conn, DecomBean inputBean, int projectPk) {
        SXSSFWorkbook wb = null;
        ArrayList<DecomBean> resultList = DecomModel.getSummaryList(conn, inputBean);

        try {
            wb = new SXSSFWorkbook();
            HashMap<String, CellStyle> styles = createStyles(wb);

            Sheet sheet = wb.createSheet("Decom Workflow");
            Row row = null;

            //set column widths, the width is measured in units of 1/256th of a character width
            int[] widthArr = {50, 10, 8, 15, 30, 16, 16, 8, 14, 14, 14, 10, 30, 14, 14, 14, 14, 12, 52};

            int c = 0;
            for (int w : widthArr) {
                sheet.setColumnWidth(c++, 256*w);
            }

            //Create header row
            row = sheet.createRow(0);
            String[] headerArr = {
                    "Vessel Information", null, null, null, null, null, null, null, null,
                    "Decom Workflow", null, null, null, null, null, null, null, null, null
            };

            c = 0;
            for (String value : headerArr) {
                writeCell(row, c++, styles.get("header"), value);
            }

            //Merge regions
            sheet.addMergedRegion(new CellRangeAddress(0,0,0,8));
            sheet.addMergedRegion(new CellRangeAddress(0,0,9,18));

            //Write section borders
            writeSectionBorder(sheet,0,9);

            row = sheet.createRow(1);
            String[] header2Arr = {
                    "Vessel Name", "Type/Hull", "TYCOM", "Homeport", "Computer Name", "Laptop Tag", "Scanner Tag", "RSupply Version",
                    "Decom Date", "Client Contacted With Decom Instructions", "Hardware Received By PSHI and Inventory Lists 'Status' Updated",
                    "Status of Hardware Received", "Status of Hardware Received Notes", "FIARModule Folder(s) Copied to P Drive",
                    "Backup Provided to TYCOM", "Records Checked Against LOGCOP", "Transmittal Reconciled With LOGCOP",
                    "Laptop Reset and All Hardware Availability Updated", "Comments"
            };

            c = 0;
            for (String value : header2Arr) {
                writeCell(row, c++, styles.get("header"), value);
            }

            //Write section borders
            writeSectionBorder(sheet,1,9);

            //freeze the pane
            sheet.createFreezePane(0,2);

            //Write data
            int r = 2;
            for (DecomBean resultBean : resultList) {
                row = sheet.createRow(r++);
                c = 0;
                writeCell(row, c++, styles.get("general"), resultBean.getShipName());
                writeCell(row, c++, styles.get("general"), CommonMethods.nes(resultBean.getType()) + " " + CommonMethods.nes(resultBean.getHull()));
                writeCell(row, c++, styles.get("general"), resultBean.getTycomDisplay());
                writeCell(row, c++, styles.get("general"), resultBean.getHomeport());
                writeCell(row, c++, styles.get("general"), resultBean.getComputerName());
                writeCell(row, c++, styles.get("center"),  resultBean.getLaptopTag());
                writeCell(row, c++, styles.get("center"),  resultBean.getScannerTag());
                writeCell(row, c++, styles.get("general"), resultBean.getRsupply());
                writeCell(row, c++, styles.get("red-date"), resultBean.getDecomDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("date"),    resultBean.getShipContactedDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("date"),    resultBean.getSystemReceivedDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("general"), resultBean.getHardwareStatus());
                writeCell(row, c++, styles.get("wrapped"), resultBean.getHardwareStatusNotes());
                writeCell(row, c++, styles.get("date"),    resultBean.getSystemReturnedDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("date"),    resultBean.getBackupDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("date"),    resultBean.getTransmittalCheckDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("date"),    resultBean.getTransmittalReconDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("date"),    resultBean.getLaptopResetDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get("wrapped"), resultBean.getComments());

                //Write section borders
                writeSectionBorder(sheet,r-1,9);
            }

            wb.write(out);
        } catch (Exception e) {
            logger.error(e);
        } finally { // dispose of temporary files backing this workbook on disk
            //try { wb.close(); } catch (Exception e) {}
            try { wb.dispose(); } catch (Exception e) {}
        }
    }

    public static void writeTransmittalSummaryXlsx(OutputStream out, Connection conn, int projectPk) {
        SXSSFWorkbook wb = null;
        ArrayList<ReportBean> resultList = ReportModel.getTransmittalSummaryList(conn);

        try {
            wb = new SXSSFWorkbook();
            HashMap<String, CellStyle> styles = createStyles(wb);

            Sheet sheet = wb.createSheet("LOGCOP Transmittal Summary");
            Row row = null;

            //set column widths, the width is measured in units of 1/256th of a character width
            int[] widthArr = {50,29,20,14,14,14,14,14,14,14,14,14,14,18};

            int c = 0;
            for (int w : widthArr) {
                sheet.setColumnWidth(c++, 256*w);
            }

            //Create header row
            row = sheet.createRow(0);
            String[] headerArr = {
                "Unit Name", "FACET Name", "Homeport", "Last Activity", "1348", "1149", "Food Approval", "Food Receipt", "Pcard Admin", "Pcard Invoice",
                "Price Change Report", "SFOEDL", "UOL", "# of Missing Transmittals"
            };

            c = 0;
            for (String value : headerArr) {
                writeCell(row, c++, styles.get("header"), value);
            }

            //freeze the pane
            sheet.createFreezePane(0,1);

            //Write data
            int r = 1;
            for (ReportBean resultBean : resultList) {
                row = sheet.createRow(r++);
                c = 0;
                writeCell(row, c++, styles.get("general"), resultBean.getShipName());
                writeCell(row, c++, styles.get("general"), resultBean.getFacetName());
                writeCell(row, c++, styles.get("general"), resultBean.getHomeport());
                writeCell(row, c++, styles.get("date"), resultBean.getLastUploadDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getForm1348UploadDateCss(), "background:#ff0;", "yellow-bg-date", "background:#d00;", "red-bg-date", "background:#0c0;", "green-bg-date", "background:#999;", "grey-bg-date", "date")), resultBean.getForm1348UploadDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getForm1149UploadDateCss(), "background:#ff0;", "yellow-bg-date", "background:#d00;", "red-bg-date", "background:#0c0;", "green-bg-date", "background:#999;", "grey-bg-date", "date")), resultBean.getForm1149UploadDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getFoodApprovalUploadDateCss(), "background:#ff0;", "yellow-bg-date", "background:#d00;", "red-bg-date", "background:#0c0;", "green-bg-date", "background:#999;", "grey-bg-date", "date")), resultBean.getFoodApprovalUploadDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getFoodReceiptUploadDateCss(), "background:#ff0;", "yellow-bg-date", "background:#d00;", "red-bg-date", "background:#0c0;", "green-bg-date", "background:#999;", "grey-bg-date", "date")), resultBean.getFoodReceiptUploadDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getPcardAdminUploadDateCss(), "background:#ff0;", "yellow-bg-date", "background:#d00;", "red-bg-date", "background:#0c0;", "green-bg-date", "background:#999;", "grey-bg-date", "date")), resultBean.getPcardAdminUploadDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getPcardInvoiceUploadDateCss(), "background:#ff0;", "yellow-bg-date", "background:#d00;", "red-bg-date", "background:#0c0;", "green-bg-date", "background:#999;", "grey-bg-date", "date")), resultBean.getPcardInvoiceUploadDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getPriceChangeUploadDateCss(), "background:#ff0;", "yellow-bg-date", "background:#d00;", "red-bg-date", "background:#0c0;", "green-bg-date", "background:#999;", "grey-bg-date", "date")), resultBean.getPriceChangeUploadDate(), "date", "MM/dd/yyyy");
                writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getSfoedlUploadDateCss(), "background:#ff0;", "yellow-bg-date", "background:#d00;", "red-bg-date", "background:#0c0;", "green-bg-date", "background:#999;", "grey-bg-date", "date")), resultBean.getSfoedlUploadDate(), "date", "MM/dd/yyyy");

                if (CommonMethods.nes(resultBean.getUolUploadDate()).equals("N/A")) {
                    writeCell(row, c++, styles.get("center"), "N/A");
                } else {
                    writeCell(row, c++, styles.get(CommonMethods.decode(resultBean.getUolUploadDateCss(), "background:#ff0;", "yellow-bg-date", "background:#d00;", "red-bg-date", "background:#0c0;", "green-bg-date", "background:#999;", "grey-bg-date", "date")), resultBean.getUolUploadDate(), "date", "MM/dd/yyyy");
                }

                writeCell(row, c++, styles.get("center"), resultBean.getMissingTransmittalListSize(), "double");
            }

            //Conditional Formatting
            SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();

            CellRangeAddress[] allSection = {new CellRangeAddress(1,r-1,3,12)};
            CellRangeAddress[] lastActivityCol = {new CellRangeAddress(1,r-1,3,3)};
            CellRangeAddress[] uolCol = {new CellRangeAddress(1,r-1,12,12)};
            CellRangeAddress[] missingCntCol = {new CellRangeAddress(1,r-1,13,13)};

            ConditionalFormattingRule rule;
            //FontFormatting fontFormat;
            PatternFormatting patternFormat;

            //Empty Dates
            rule = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"\"");
            patternFormat = rule.createPatternFormatting();
            patternFormat.setFillBackgroundColor(IndexedColors.RED.index);
            sheetCF.addConditionalFormatting(allSection, rule);

            //Last Activity rule
            rule = sheetCF.createConditionalFormattingRule(ComparisonOperator.NOT_EQUAL, "\"\"");
            patternFormat = rule.createPatternFormatting();
            patternFormat.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN.index);
            sheetCF.addConditionalFormatting(lastActivityCol, rule);

            //UOL rule
            rule = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "\"N/A\"");
            patternFormat = rule.createPatternFormatting();
            patternFormat.setFillBackgroundColor(IndexedColors.GREY_40_PERCENT.index);
            sheetCF.addConditionalFormatting(uolCol, rule);

            //Missing Transmittals rules
            rule = sheetCF.createConditionalFormattingRule(ComparisonOperator.EQUAL, "0");
            patternFormat = rule.createPatternFormatting();
            patternFormat.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN.index);
            sheetCF.addConditionalFormatting(missingCntCol, rule);

            rule = sheetCF.createConditionalFormattingRule(ComparisonOperator.GT, "0");
            patternFormat = rule.createPatternFormatting();
            patternFormat.setFillBackgroundColor(IndexedColors.RED.index);
            sheetCF.addConditionalFormatting(missingCntCol, rule);

            wb.write(out);
        } catch (Exception e) {
            logger.error(e);
        } finally { // dispose of temporary files backing this workbook on disk
            //try { wb.close(); } catch (Exception e) {}
            try { wb.dispose(); } catch (Exception e) {}
        }
    }

    public static void writeConfiguredSystemXlsx(OutputStream out, Connection conn, int projectPk) {
        SXSSFWorkbook wb = null;
        ArrayList<SystemBean> resultList = SystemModel.getFullConfiguredSystemList(conn);
        String currFacetVersion = LookupModel.getCurrFacetVersion(conn, projectPk);
        String currDmsVersion = SystemModel.getCurrDmsVersion();

        try {
            wb = new SXSSFWorkbook();
            HashMap<String, CellStyle> styles = createStyles(wb);

            Sheet sheet = wb.createSheet("FACET Systems");
            Row row = null;

            //Conditional Formatting
            //SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();

            //CellRangeAddress[] statusCol = {new CellRangeAddress(0,65535,4,4)};
            //CellRangeAddress[] priorityCol = {new CellRangeAddress(0,65535,5,5)};

            //set column widths, the width is measured in units of 1/256th of a character width
            int[] widthArr = {
                50,19,8,12,15,15,15,30,14,16,18,15,60,27,50,50,35,10,40,16,18,14,25,25,16,16,16,50,16,11,13,36,36,
                14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,
                9,17,14,14,14,14,14,14,14,14,14
                };

            int c = 0;
            for (int w : widthArr) {
                sheet.setColumnWidth(c++, 256*w);
            }

            //Create header row
            row = sheet.createRow(0);
            String[] headerArr = {
                "Unit Assigned", null, null, null,
                "FACET Support", null, null, null,
                "PSHI-Maintained Software", null, null, null, null, null,
                "POCs", null,
                "Laptop", null, null, null, null, null, null, null,
                "Scanner", null, null, null,
                "Kofax", null, null,
                "MS Office", null,
                "Backfile Workflow", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                "Training Workflow", null, null, null, null, null, null, null, null, null,
                "Decom Workflow", null, null, null, null, null, null, null, null,
                "Misc", null,
                "Uploaded Files", null, null, null, null,
                "VRS", null,
                "Misc", null
                };

            c = 0;
            for (String value : headerArr) {
                writeCell(row, c++, styles.get("header"), value);
            }

            //Write section borders
            writeSectionBorder(sheet,0,4);
            writeSectionBorder(sheet,0,8);
            writeSectionBorder(sheet,0,14);
            writeSectionBorder(sheet,0,16);
            writeSectionBorder(sheet,0,24);
            writeSectionBorder(sheet,0,28);
            writeSectionBorder(sheet,0,31);
            writeSectionBorder(sheet,0,33);
            writeSectionBorder(sheet,0,53);
            writeSectionBorder(sheet,0,63);
            writeSectionBorder(sheet,0,72);
            writeSectionBorder(sheet,0,74);
            writeSectionBorder(sheet,0,79);
            writeSectionBorder(sheet,0,81);

            row = sheet.createRow(1);
            String[] header2Arr = {
                "Unit Name", "Homeport", "RSupply", "DECOM Date",
                "Total # of Support Issues", "Total # of Open Support Issues", "Total # of Completed Support Visits", "Latest Support Visit (inc. upcoming scheduled)",
                "FACET Version", "Kofax BC Version", "Dummy DB Version", "DMS Version", "ATO Installed", "Current Location",
                "Primary POCs", "All POCs",
                "Computer Name", "Multi-Ship", "Multi-Ship UICs", "Tag", "Product Name", "Serial Number", "Network Adapter Type", "Status", "Tag",
                "Product Name", "Serial Number", "Status", "Product Name", "License Key", "Product Code",
                "Product Name", "License Key",
                "Overall Due Date", "FY16 Completed Date", "FY16 Mailed Date", "FY15 Completed Date", "FY15 Mailed Date", "FY14/13 Due Date", "Scheduled Training Date", "Completed Date", "Backfile Date Requested", "Date Received By PSHI", "Date Delivered to Scanning", "FY14/13 Completed Date", "FY14/13 CD Sent for Customer Date", "FY12/11 Completed Date", "Extract File Created", "All backfile CD mailed to Cust/SD", "Verify All Backfiles Installed in FACET", "All Backfile CD Delivered to LogCop", "Verified LOGCOP Backfile Uploaded", "Final Backfile Report Generated and Given to CompacFlt",
                "Location File Received", "Location File Reviewed", "PacFlt Sent Notification to Send Food Report", "System Shipped", "Computer Name in Database", "Computer Name Provided to Logcop", "Training Kit Ready", "Estimated Training Month", "Scheduled Training Date", "Actual Training Date",
                "Client Contacted With Decom Instructions", "Hardware Received By PSHI and Inventory Lists 'Status' Updated", "Status of Hardware Received", "Status of Hardware Received Notes", "FIARModule Folder(s) Copied to P Drive", "Backup Provided to TYCOM", "Records Checked Against LOGCOP", "Transmittal Reconciled With LOGCOP", "Laptop Reset and All Hardware Availability Updated",
                "NWCF", "Contract System Installed",
                "Vessel Date Sheet", "Training Sign In Sheet", "FACET Laptop Configuration Info", "Trainer Laptop Checklist 002", "Post Install Checklist",
                "License Key", "Product Code",
                "1348 No Location", "1348 No Class SL HAZMAT"
                };
            //"Final Laptop Prep Checklist 001" was replaced by "FACET Laptop Configuration Info"

            c = 0;
            for (String value : header2Arr) {
                writeCell(row, c++, styles.get("header"), value);
            }

            //Merge regions
            sheet.addMergedRegion(new CellRangeAddress(0,0,0,3));
            sheet.addMergedRegion(new CellRangeAddress(0,0,4,7));
            sheet.addMergedRegion(new CellRangeAddress(0,0,8,13));
            sheet.addMergedRegion(new CellRangeAddress(0,0,14,15));
            sheet.addMergedRegion(new CellRangeAddress(0,0,16,23));
            sheet.addMergedRegion(new CellRangeAddress(0,0,24,27));
            sheet.addMergedRegion(new CellRangeAddress(0,0,28,30));
            sheet.addMergedRegion(new CellRangeAddress(0,0,31,32));
            sheet.addMergedRegion(new CellRangeAddress(0,0,33,52));
            sheet.addMergedRegion(new CellRangeAddress(0,0,53,62));
            sheet.addMergedRegion(new CellRangeAddress(0,0,63,71));
            sheet.addMergedRegion(new CellRangeAddress(0,0,72,73));
            sheet.addMergedRegion(new CellRangeAddress(0,0,74,78));
            sheet.addMergedRegion(new CellRangeAddress(0,0,79,80));
            sheet.addMergedRegion(new CellRangeAddress(0,0,81,82));

            //Write section borders
            writeSectionBorder(sheet,1,4);
            writeSectionBorder(sheet,1,8);
            writeSectionBorder(sheet,1,14);
            writeSectionBorder(sheet,1,16);
            writeSectionBorder(sheet,1,24);
            writeSectionBorder(sheet,1,28);
            writeSectionBorder(sheet,1,31);
            writeSectionBorder(sheet,1,33);
            writeSectionBorder(sheet,1,53);
            writeSectionBorder(sheet,1,63);
            writeSectionBorder(sheet,1,72);
            writeSectionBorder(sheet,1,74);
            writeSectionBorder(sheet,1,79);
            writeSectionBorder(sheet,1,81);

            //freeze the pane
            sheet.createFreezePane(0,2);

            //Write data
            int r = 2;
            for (SystemBean resultBean : resultList) {
                row = sheet.createRow(r++);
                c = 0;
                writeCell(row, c++, styles.get("general"), resultBean.getShipName());
                writeCell(row, c++, styles.get("general"), resultBean.getHomeport());
                writeCell(row, c++, styles.get("general"), resultBean.getRsupply());
                writeCell(row, c++, styles.get("red-date"), resultBean.getDecomDate(), "date", "MM/dd/yyyy"); //red-text

                if (resultBean.getLastVisitBean() != null) {
                    SupportBean lastVisitBean = resultBean.getLastVisitBean();
                    writeCell(row, c++, styles.get("center"), lastVisitBean.getIssueCnt(), "double");
                    writeCell(row, c++, styles.get("center"), lastVisitBean.getOpenIssueCnt(), "double");
                    writeCell(row, c++, styles.get("center"), lastVisitBean.getSupportVisitCnt(), "double");
                    if (!CommonMethods.isEmpty(lastVisitBean.getSupportVisitDate())) {
                        writeCell(row, c++, styles.get("general"), lastVisitBean.getSupportVisitDate() + " (" + lastVisitBean.getCategory() + ")");
                    } else {
                        writeCell(row, c++, styles.get("general"));
                    }
                } else {
                    writeCell(row, c++, styles.get("general"));
                    writeCell(row, c++, styles.get("general"));
                    writeCell(row, c++, styles.get("general"));
                    writeCell(row, c++, styles.get("general"));
                } //else

                if (!CommonMethods.isEmpty(resultBean.getFacetVersion()) && !resultBean.getFacetVersion().equals(currFacetVersion)) {
                    writeCell(row, c++, styles.get("red-text"), resultBean.getFacetVersion()); //red-text
                } else {
                    writeCell(row, c++, styles.get("general"), resultBean.getFacetVersion());
                }

                writeCell(row, c++, styles.get("general"), resultBean.getKofaxVersion());
                writeCell(row, c++, styles.get("general"), resultBean.getKofaxVersion());

                if (!CommonMethods.isEmpty(resultBean.getDmsVersion()) && !resultBean.getDmsVersion().equals(currDmsVersion)) {
                    writeCell(row, c++, styles.get("red-text"), resultBean.getDmsVersion()); //red-text
                } else {
                    writeCell(row, c++, styles.get("general"), resultBean.getDmsVersion());
                }

                writeCell(row, c++, styles.get("wrapped"), CommonMethods.printArray(resultBean.getAtoInstalledList()));
                writeCell(row, c++, styles.get("general"), resultBean.getLocation());

                writeCell(row, c++, styles.get("wrapped"), resultBean.getPrimaryPocEmails());
                writeCell(row, c++, styles.get("wrapped"), resultBean.getPocEmails());

                writeCell(row, c++, styles.get("general"), resultBean.getComputerName());
                writeCell(row, c++, styles.get("center"), CommonMethods.nvl(resultBean.getMultiShipInd(), "N"));
                writeCell(row, c++, styles.get("wrapped"), resultBean.getMultiShipUicList());
                writeCell(row, c++, styles.get("general"), resultBean.getLaptopTag());
                writeCell(row, c++, styles.get("general"), resultBean.getLaptopProductName());
                writeCell(row, c++, styles.get("general"), resultBean.getLaptopSerialNumber());
                writeCell(row, c++, styles.get("general"), resultBean.getNetworkAdapter());
                writeCell(row, c++, styles.get("general"), resultBean.getLaptopStatus());

                writeCell(row, c++, styles.get("general"), resultBean.getScannerTag());
                writeCell(row, c++, styles.get("general"), resultBean.getScannerProductName());
                writeCell(row, c++, styles.get("general"), resultBean.getScannerSerialNumber());
                writeCell(row, c++, styles.get("general"), resultBean.getScannerStatus());

                writeCell(row, c++, styles.get("general"), resultBean.getKofaxProductName());
                writeCell(row, c++, styles.get("general"), resultBean.getKofaxLicenseKey());
                writeCell(row, c++, styles.get("general"), resultBean.getKofaxProductCode());

                writeCell(row, c++, styles.get("general"), resultBean.getMsOfficeProductName());
                writeCell(row, c++, styles.get("general"), resultBean.getMsOfficeLicenseKey());

                BackfileBean backfileBean = resultBean.getBackfileBean();
                if (backfileBean == null) {
                    writeCell(row, c++, styles.get("greyed"), "No Backfile Workflow Found");
                    for (int f = 0; f < 19; f++) {
                        writeCell(row, c++, styles.get("greyed"));
                    }
                } else if (backfileBean.getIsRequired().equals("N")) {
                    writeCell(row, c++, styles.get("greyed"), "Backfile Not Required");
                    for (int f = 0; f < 19; f++) {
                        writeCell(row, c++, styles.get("greyed"));
                    }
                } else {
                    writeCell(row, c++, styles.get("date"), backfileBean.getDueDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get("date"), backfileBean.getFy1314DueDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get("date"), backfileBean.getSchedTrainingDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get("date"), backfileBean.getCompletedDate(), "date", "MM/dd/yyyy");

                    writeCell(row, c++, styles.get(CommonMethods.decode(backfileBean.getRequestedDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), backfileBean.getRequestedDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get(CommonMethods.decode(backfileBean.getReceivedDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), backfileBean.getReceivedDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get(CommonMethods.decode(backfileBean.getScanningDeliveredDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), backfileBean.getScanningDeliveredDate(), "date", "MM/dd/yyyy");

                    writeCell(row, c++, styles.get(CommonMethods.decode(backfileBean.getFy16CompletedDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), backfileBean.getFy16CompletedDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get(CommonMethods.decode(backfileBean.getFy16MailedDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), backfileBean.getFy16MailedDate(), "date", "MM/dd/yyyy");

                    writeCell(row, c++, styles.get(CommonMethods.decode(backfileBean.getFy15CompletedDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), backfileBean.getFy15CompletedDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get(CommonMethods.decode(backfileBean.getFy15MailedDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), backfileBean.getFy15MailedDate(), "date", "MM/dd/yyyy");

                    writeCell(row, c++, styles.get(CommonMethods.decode(backfileBean.getFy1314CompletedDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), backfileBean.getFy1314CompletedDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get(CommonMethods.decode(backfileBean.getFy1314BurnedDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), backfileBean.getFy1314BurnedDate(), "date", "MM/dd/yyyy");

                    writeCell(row, c++, styles.get(CommonMethods.decode(backfileBean.getFy1112CompletedDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), backfileBean.getFy1112CompletedDate(), "date", "MM/dd/yyyy");

                    writeCell(row, c++, styles.get(CommonMethods.decode(backfileBean.getExtractDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), backfileBean.getExtractDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get(CommonMethods.decode(backfileBean.getFy1314MailedDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), backfileBean.getFy1314MailedDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get(CommonMethods.decode(backfileBean.getLaptopInstalledDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), backfileBean.getLaptopInstalledDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get(CommonMethods.decode(backfileBean.getLogcopDeliveredDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), backfileBean.getLogcopDeliveredDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get(CommonMethods.decode(backfileBean.getLogcopUploadedDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), backfileBean.getLogcopUploadedDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get(CommonMethods.decode(backfileBean.getFinalReportDateCss(), "background:#ff0;", "yellow-bg-date", "background:#f00;", "red-bg-date", "background:#0ae;", "blue-bg-date", "date")), backfileBean.getFinalReportDate(), "date", "MM/dd/yyyy");
                }

                TrainingBean trainingBean = resultBean.getTrainingBean();
                if (trainingBean == null) {
                    writeCell(row, c++, styles.get("greyed"), "No Training Workflow Found");
                    for (int f = 0; f < 9; f++) {
                        writeCell(row, c++, styles.get("greyed"));
                    }
                } else {
                    writeCell(row, c++, styles.get("date"), trainingBean.getLocFileRecvDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get("date"), trainingBean.getLocFileRevDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get("date"), trainingBean.getPacfltFoodReportDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get("date"), trainingBean.getSystemReadyDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get("date"), trainingBean.getComputerNameDbDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get("date"), trainingBean.getComputerNameLogcopDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get("date"), trainingBean.getTrainingKitReadyDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get("general"), trainingBean.getEstTrainingMonth());
                    writeCell(row, c++, styles.get("date"), trainingBean.getSchedTrainingDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get("date"), trainingBean.getActualTrainingDate(), "date", "MM/dd/yyyy");
                }

                DecomBean decomBean = resultBean.getDecomBean();
                if (decomBean == null) {
                    writeCell(row, c++, styles.get("greyed"), "No Decom Workflow Found");
                    for (int f = 0; f < 8; f++) {
                        writeCell(row, c++, styles.get("greyed"));
                    }
                } else {
                    writeCell(row, c++, styles.get("date"), decomBean.getShipContactedDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get("date"), decomBean.getSystemReceivedDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get("general"), decomBean.getHardwareStatus());
                    writeCell(row, c++, styles.get("wrapped"), decomBean.getHardwareStatusNotes());
                    writeCell(row, c++, styles.get("date"), decomBean.getSystemReturnedDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get("date"), decomBean.getBackupDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get("date"), decomBean.getTransmittalCheckDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get("date"), decomBean.getTransmittalReconDate(), "date", "MM/dd/yyyy");
                    writeCell(row, c++, styles.get("date"), decomBean.getLaptopResetDate(), "date", "MM/dd/yyyy");
                }

                writeCell(row, c++, styles.get("center"), CommonMethods.nvl(resultBean.getNwcfInd(), "N"));
                writeCell(row, c++, styles.get("general"), resultBean.getContractNumber());

                writeCell(row, c++, styles.get("date"), resultBean.getHardwareFileUploadedDate(), "date", "yyyy-MM-DD hh:mm:ss");
                writeCell(row, c++, styles.get("date"), resultBean.getTrainingFileUploadedDate(), "date", "yyyy-MM-DD hh:mm:ss");
                writeCell(row, c++, styles.get("date"), resultBean.getLaptop1FileUploadedDate(), "date", "yyyy-MM-DD hh:mm:ss");
                writeCell(row, c++, styles.get("date"), resultBean.getLaptop2FileUploadedDate(), "date", "yyyy-MM-DD hh:mm:ss");
                writeCell(row, c++, styles.get("date"), resultBean.getPostInstallFileUploadedDate(), "date", "yyyy-MM-DD hh:mm:ss");

                if (!CommonMethods.isEmpty(resultBean.getVrsLicensePk()) && resultBean.getVrsLicensePk().equals("0")) {
                    writeCell(row, c++, styles.get("general"), "N/A");
                } else {
                    writeCell(row, c++, styles.get("general"), resultBean.getVrsLicenseKey());
                }

                writeCell(row, c++, styles.get("general"), resultBean.getVrsProductCode());

                writeCell(row, c++, styles.get("center"), CommonMethods.nvl(resultBean.getForm1348NoLocationInd(), "N"));
                writeCell(row, c++, styles.get("center"), CommonMethods.nvl(resultBean.getForm1348NoClassInd(), "N"));

                //Write section borders
                writeSectionBorder(sheet,r-1,4);
                writeSectionBorder(sheet,r-1,8);
                writeSectionBorder(sheet,r-1,14);
                writeSectionBorder(sheet,r-1,16);
                writeSectionBorder(sheet,r-1,24);
                writeSectionBorder(sheet,r-1,28);
                writeSectionBorder(sheet,r-1,31);
                writeSectionBorder(sheet,r-1,33);
                writeSectionBorder(sheet,r-1,53);
                writeSectionBorder(sheet,r-1,63);
                writeSectionBorder(sheet,r-1,72);
                writeSectionBorder(sheet,r-1,74);
                writeSectionBorder(sheet,r-1,79);
                writeSectionBorder(sheet,r-1,81);
            }

            wb.write(out);
        } catch (Exception e) {
            logger.error(e);
        } finally { // dispose of temporary files backing this workbook on disk
            //try { wb.close(); } catch (Exception e) {}
            try { wb.dispose(); } catch (Exception e) {}
        }
    }

    public static void writeTrainingWorkflowXlsx(OutputStream out, Connection conn, TrainingBean inputBean, int projectPk) {
        SXSSFWorkbook wb = null;

        try {
            wb = new SXSSFWorkbook();
            HashMap<String, CellStyle> styles = createStyles(wb);

            writeTrainingScheduleSheet(wb, styles, conn, inputBean);
            writeTrainingWorkflowSheet(wb, styles, conn, inputBean, "inProd");
            writeTrainingWorkflowSheet(wb, styles, conn, inputBean, "unsched");
            writeTrainingWorkflowSheet(wb, styles, conn, inputBean, "overdue");
            writeTrainingWorkflowSheet(wb, styles, conn, inputBean, "completed");

            wb.write(out);
        } catch (Exception e) {
            logger.error(e);
        } finally { // dispose of temporary files backing this workbook on disk
            //try { wb.close(); } catch (Exception e) {}
            try { wb.dispose(); } catch (Exception e) {}
        }
    }

    private static void writeTrainingScheduleSheet(SXSSFWorkbook wb, HashMap<String, CellStyle> styles, Connection conn, TrainingBean inputBean) throws Exception {
        Sheet sheet = null;
        Row row = null;
        ArrayList<TrainingBean> resultList = TrainingModel.getSummaryList(conn, "report", inputBean, "noSortBy", "noSortDir");
        int currYear = CommonMethods.cInt(CommonMethods.getDate("yyyy"));

        sheet = wb.createSheet("Calendar");

        //set column widths, the width is measured in units of 1/256th of a character width
        int[] widthArr = {50,11,8,19,10,14,14,14,14,14};

        int c = 0;
        for (int w : widthArr) {
            sheet.setColumnWidth(c++, 256*w);
        }

        for (int year = 2013; year <= currYear; year++) {
            for (int month = (year == 2013 ? 9 : 1); month <= (year == currYear ? 9 : 12); month++) {
                int startDow = CommonMethods.cInt(CommonMethods.getDate(month + "/1/" + year, "DOW#"));
                int startDay = 1;
                int endDay = 8 - startDow;
                int daysInMonth = CommonMethods.cInt(CommonMethods.getDate(month + "/1/" + year, "MAX"));
                while (startDay <= daysInMonth) {
                    sheet.setColumnWidth(c++, 256*4);
                    startDay = (startDay == 1 ? endDay + 1 : startDay + 7);
                    endDay+=7;
                }
            }
        }

        //Create header row
        row = sheet.createRow(0);
        String[] header1NameArr = {
            "Activity Information", null, null, null, null,
            "Backfile", null,
            "Training Workflow", null, null
            };

        c = 0;
        for (String value : header1NameArr) {
            writeCell(row, c++, styles.get("header"), value);
        }

        //Write section borders
        writeSectionBorder(sheet,0,5);
        writeSectionBorder(sheet,0,7);
        writeSectionBorder(sheet,0,10);

        for (int year = 2013; year <= currYear; year++) {
            for (int month = (year == 2013 ? 9 : 1); month <= (year == currYear ? 9 : 12); month++) {
                writeCell(row, c++, styles.get("header"), CommonMethods.getMonthNameShort(month-1) + "-" + year);
                writeSectionBorder(sheet,0,c-1);
                for (int w = 1; w < CommonMethods.cInt(CommonMethods.getDate(month + "/1/" + year, "WOM")); w++) {
                    writeCell(row, c++, styles.get("header")); //blank placeholders
                }
            }
        }

        row = sheet.createRow(1);
        String[] header2NameArr = {"Unit Name", "Type/Hull", "TYCOM", "Homeport", "RSupply Version", "Received Date", "Completed Date", "Scheduled Date", "Actual Date", "Confirmed By Client"};

        c = 0;
        for (String value : header2NameArr) {
            writeCell(row, c++, styles.get("header"), value);
        }

        for (int year = 2013; year <= currYear; year++) {
            for (int month = (year == 2013 ? 9 : 1); month <= (year == currYear ? 9 : 12); month++) {
                int startDow = CommonMethods.cInt(CommonMethods.getDate(month + "/1/" + year, "DOW#"));
                int startDay = 1;
                int endDay = 8 - startDow;
                int daysInMonth = CommonMethods.cInt(CommonMethods.getDate(month + "/1/" + year, "MAX"));
                int w = 1;
                while (startDay <= daysInMonth) {
                    writeCell(row, c++, styles.get("header"), "Wk " + w);
                    if (w == 1) writeSectionBorder(sheet,1,c-1);
                    startDay = (startDay == 1 ? endDay + 1 : startDay + 7);
                    endDay+=7;
                    w++;
                }
            }
        }

        //Merge regions
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,4));
        sheet.addMergedRegion(new CellRangeAddress(0,0,5,6));
        sheet.addMergedRegion(new CellRangeAddress(0,0,7,9));
        c = 10;
        for (int year = 2013; year <= currYear; year++) {
            for (int month = (year == 2013 ? 9 : 1); month <= (year == currYear ? 9 : 12); month++) {
                int weekCnt = CommonMethods.cInt(CommonMethods.getDate(month + "/1/" + year, "WOM"));
                sheet.addMergedRegion(new CellRangeAddress(0,0,c,c+weekCnt-1));
                c += weekCnt;
            }
        }

        //Write section borders
        writeSectionBorder(sheet,1,5);
        writeSectionBorder(sheet,1,7);
        writeSectionBorder(sheet,1,10);

        //freeze the pane
        sheet.createFreezePane(0,2);

        //Write data
        int r = 2;
        for (TrainingBean resultBean : resultList) {
            row = sheet.createRow(r++);
            c = 0;

            writeCell(row, c++, styles.get("general"), resultBean.getShipName());
            writeCell(row, c++, styles.get("general"), CommonMethods.nes(resultBean.getType()) + " " + CommonMethods.nes(resultBean.getHull()));
            writeCell(row, c++, styles.get("general"), resultBean.getTycomDisplay());
            writeCell(row, c++, styles.get("general"), resultBean.getHomeport());
            writeCell(row, c++, styles.get("general"), resultBean.getRsupply());

            writeCell(row, c++, styles.get("date"), resultBean.getBackfileRecvDate(), "date", "MM/dd/yyyy");
            writeCell(row, c++, styles.get("date"), resultBean.getBackfileCompletedDate(), "date", "MM/dd/yyyy");

            if (!CommonMethods.isEmpty(resultBean.getSchedTrainingDate())) {
                writeCell(row, c++, styles.get("date"), resultBean.getSchedTrainingDate(), "date", "MM/dd/yyyy");
            } else if (!CommonMethods.isEmpty(resultBean.getActualTrainingDate())) {
                writeCell(row, c++, styles.get("general"), "-");
            } else {
                writeCell(row, c++, styles.get("general"), "Unscheduled");
            }

            if (!CommonMethods.isEmpty(resultBean.getActualTrainingDate())) {
                writeCell(row, c++, styles.get("date"), resultBean.getActualTrainingDate(), "date", "MM/dd/yyyy");
            } else {
                writeCell(row, c++, styles.get("general"), "-");
            }

            writeCell(row, c++, styles.get("center"), resultBean.getClientConfirmedInd());

            //Write section borders
            writeSectionBorder(sheet,r-1,5);
            writeSectionBorder(sheet,r-1,7);
            writeSectionBorder(sheet,r-1,10);

            for (int year = 2013; year <= currYear; year++) {
                for (int month = (year == 2013 ? 9 : 1); month <= (year == currYear ? 9 : 12); month++) {
                    int startDow = CommonMethods.cInt(CommonMethods.getDate(month + "/1/" + year, "DOW#"));
                    int startDay = 1;
                    int endDay = 8 - startDow;
                    int daysInMonth = CommonMethods.cInt(CommonMethods.getDate(month + "/1/" + year, "MAX"));
                    String currDate = CommonMethods.nvl(resultBean.getActualTrainingDate(), resultBean.getSchedTrainingDate());
                    int w = 1;
                    while (startDay <= daysInMonth) {
                        if (CommonMethods.dateDiff(currDate, month + "/" + startDay + "/" + year) <= 0 && CommonMethods.dateDiff(currDate, month + "/" + (endDay <= daysInMonth ? endDay : daysInMonth) + "/" + year) >= 0) {
                            if (!CommonMethods.isEmpty(resultBean.getActualTrainingDate())) {
                                writeCell(row, c++, styles.get("gold-bg-text"), CommonMethods.getDate(currDate, "DD"), "double");
                            } else {
                                writeCell(row, c++, styles.get("teal-bg-text"), CommonMethods.getDate(currDate, "DD"), "double");
                            }
                        } else {
                            writeCell(row, c++, styles.get("general"));
                        }
                        if (w == 1) writeSectionBorder(sheet,r-1,c-1);
                        startDay = (startDay == 1 ? endDay + 1 : startDay + 7);
                        endDay+=7;
                        w++;
                    }
                }
            }

        }
    }

    private static void writeTrainingWorkflowSheet(SXSSFWorkbook wb, HashMap<String, CellStyle> styles, Connection conn, TrainingBean inputBean, String type) throws Exception {
        Sheet sheet = null;
        Row row = null;
        ArrayList<TrainingBean> resultList = TrainingModel.getSummaryList(conn, type, inputBean, "ship_name", "ASC");

        switch (type) {
            case "inProd":
                sheet = wb.createSheet("In Production");
                break;
            case "unsched":
                sheet = wb.createSheet("Unscheduled");
                break;
            case "overdue":
                sheet = wb.createSheet("Overdue");
                break;
            case "completed":
                sheet = wb.createSheet("Completed");
                break;
        }

        //set column widths, the width is measured in units of 1/256th of a character width
        int[] widthArr = {50,10,8,16,10,14,14,14,14,14,14,14,14,14,14,14,14,14};

        int c = 0;
        for (int w : widthArr) {
            sheet.setColumnWidth(c++, 256*w);
        }

        //Create header row
        row = sheet.createRow(0);
        c = 0;
        String[] header1NameArr = {
            "Unit Information", null, null, null, null,
            "Backfile", null,
            "Training Workflow", null, null, null, null, null, null, null, null, null, null
            };

        for (String value : header1NameArr) {
            writeCell(row, c++, styles.get("header"), value);
        }

        //Write section borders
        writeSectionBorder(sheet,0,5);
        writeSectionBorder(sheet,0,7);

        row = sheet.createRow(1);
        String[] header2NameArr = { "Unit Name", "Type/Hull", "TYCOM", "Homeport", "RSupply Version", "Received Date", "Completed Date",
            "Location File Received", "Location File Reviewed", "PacFlt Sent Notification to Send Food Report", "System Shipped",
            "Computer Name in Database", "Computer Name Provided to Logcop", "Training Kit Ready", "Estimated Training Month",
            "Scheduled Training Date", "Actual Training Date", "Confirmed By Client"
        };

        c = 0;
        for (String value : header2NameArr) {
            writeCell(row, c++, styles.get("header"), value);
        }

        //Merge regions
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,4));
        sheet.addMergedRegion(new CellRangeAddress(0,0,5,6));
        sheet.addMergedRegion(new CellRangeAddress(0,0,7,17));

        //Write section borders
        writeSectionBorder(sheet,1,5);
        writeSectionBorder(sheet,1,7);

        //freeze the pane
        sheet.createFreezePane(0,2);

        //Write data
        int r = 2;
        for (TrainingBean resultBean : resultList) {
            row = sheet.createRow(r++);
            c = 0;

            writeCell(row, c++, styles.get("general"), resultBean.getShipName());
            writeCell(row, c++, styles.get("general"), CommonMethods.nes(resultBean.getType()) + " " + CommonMethods.nes(resultBean.getHull()));
            writeCell(row, c++, styles.get("general"), resultBean.getTycomDisplay());
            writeCell(row, c++, styles.get("general"), resultBean.getHomeport());
            writeCell(row, c++, styles.get("general"), resultBean.getRsupply());

            writeCell(row, c++, styles.get("date"), resultBean.getBackfileRecvDate(), "date", "MM/dd/yyyy");
            writeCell(row, c++, styles.get("date"), resultBean.getBackfileCompletedDate(), "date", "MM/dd/yyyy");

            writeCell(row, c++, styles.get("date"), resultBean.getLocFileRecvDate(), "date", "MM/dd/yyyy");
            writeCell(row, c++, styles.get("date"), resultBean.getLocFileRevDate(), "date", "MM/dd/yyyy");
            writeCell(row, c++, styles.get("date"), resultBean.getPacfltFoodReportDate(), "date", "MM/dd/yyyy");
            writeCell(row, c++, styles.get("date"), resultBean.getSystemReadyDate(), "date", "MM/dd/yyyy");
            writeCell(row, c++, styles.get("date"), resultBean.getComputerNameDbDate(), "date", "MM/dd/yyyy");
            writeCell(row, c++, styles.get("date"), resultBean.getComputerNameLogcopDate(), "date", "MM/dd/yyyy");
            writeCell(row, c++, styles.get("date"), resultBean.getTrainingKitReadyDate(), "date", "MM/dd/yyyy");
            writeCell(row, c++, styles.get("general"), resultBean.getEstTrainingMonth());
            writeCell(row, c++, styles.get("date"), resultBean.getSchedTrainingDate(), "date", "MM/dd/yyyy");
            writeCell(row, c++, styles.get("date"), resultBean.getActualTrainingDate(), "date", "MM/dd/yyyy");
            writeCell(row, c++, styles.get("center"), resultBean.getClientConfirmedInd());

            //Write section borders
            writeSectionBorder(sheet,r-1,5);
            writeSectionBorder(sheet,r-1,7);
        }
    }
}
