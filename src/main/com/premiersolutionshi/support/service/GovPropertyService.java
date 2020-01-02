package com.premiersolutionshi.support.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.apache.ibatis.session.SqlSession;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.premiersolutionshi.common.service.ModifiedService;
import com.premiersolutionshi.common.service.UserService;
import com.premiersolutionshi.common.util.StringUtils;
import com.premiersolutionshi.old.model.ExportModel;
import com.premiersolutionshi.support.dao.GovPropertyDao;
import com.premiersolutionshi.support.domain.GovProperty;

public class GovPropertyService extends ModifiedService<GovProperty> {
    public GovPropertyService(SqlSession sqlSession, UserService userService) {
        super(sqlSession, GovPropertyDao.class, userService);
    }

    @Override
    protected GovPropertyDao getDao() {
        return (GovPropertyDao) super.getDao();
    }


    @Override
    protected void beforeSave(GovProperty domain) {
        if (domain == null) {
            return;
        }
        super.beforeSave(domain);
        //sanitize data with "escapeHtml", "removeXss", or "sanitizeUrl"
        domain.setNationalStockNumber(StringUtils.removeXss(domain.getNationalStockNumber()));
        domain.setDescription(StringUtils.escapeHtml(domain.getDescription()));
        domain.setProjectContract(StringUtils.removeXss(domain.getProjectContract()));
        domain.setLocation(StringUtils.removeXss(domain.getLocation()));
        domain.setDisposition(StringUtils.removeXss(domain.getDisposition()));
    }

    public void writeXlsx(ServletOutputStream outputStream, ArrayList<GovProperty> resultList) {
        String[] headerArr = {
            "ID", "Date", "National Stock Number", "Description", "Project/Contract", "Received", "Issued",
            "Transferred", "On Hand", "Location", "Disposition",
            "Created By", "Created Date", "Last Updated By", "Last Updated Date"
        };
        Map<String, Integer> headerToWidth = new HashMap<>();
        //set column widths, the width is measured in units of 1/256th of a character width
        headerToWidth.put("ID", 7);
        headerToWidth.put("Date", 15);
        headerToWidth.put("National Stock Number", 20);
        headerToWidth.put("Description", 30);
        headerToWidth.put("Project/Contract", 20);

        headerToWidth.put("Received", 15);
        headerToWidth.put("Issued", 15);
        headerToWidth.put("Transferred", 15);
        headerToWidth.put("On Hand", 15);

        headerToWidth.put("Location", 20);
        headerToWidth.put("Disposition", 20);
        headerToWidth.put("Created By", 20);
        headerToWidth.put("Created Date", 20);
        headerToWidth.put("Last Updated By", 20);
        headerToWidth.put("Last Updated Date", 20);
        int colNum = 0;
        int rowNum = 0;

        SXSSFWorkbook wb = null;
        try {
            wb = new SXSSFWorkbook();
            HashMap<String, CellStyle> styles = ExportModel.createStyles(wb);
            Sheet sheet = wb.createSheet("Government Property List");

            //Add header information:
            //  Premier Solutions, LLC
            //  Contractor Acquired & Government Property Tracking Summary
            CellStyle centeredStyle = wb.createCellStyle();
            centeredStyle.setAlignment(CellStyle.ALIGN_CENTER);
            Row titleRow = sheet.createRow(rowNum++);
            Cell titleRowCell = titleRow.createCell(0);
            titleRowCell.setCellValue("Premier Solutions, LLC");
            titleRowCell.setCellStyle(centeredStyle);
            Row titleRow2 = sheet.createRow(rowNum++);
            Cell titleRow2Cell = titleRow2.createCell(0);
            titleRow2Cell.setCellStyle(centeredStyle);
            titleRow2Cell.setCellValue("Contractor Acquired and Government Property Tracking Summary");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headerArr.length - 1));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, headerArr.length - 1));

            //Create header row
            Row headerRow = sheet.createRow(rowNum++);
            colNum = 0;
            for (String headerName : headerArr) {
                ExportModel.writeCell(headerRow, colNum, styles.get("header"), headerName);
                sheet.setColumnWidth(colNum, 256 * headerToWidth.get(headerName));
                colNum++;
            }
            //freeze the pane
            sheet.createFreezePane(0, rowNum - 1);
            for (GovProperty govProperty : resultList) {
                Row row = sheet.createRow(rowNum++);
                colNum = 0;
                ExportModel.writeIntCell(row, colNum++, styles.get("center"), govProperty.getId());
                ExportModel.writeCell(row, colNum++, styles.get("date"), govProperty.getDateListedStr());
                ExportModel.writeCell(row, colNum++, styles.get("general"), govProperty.getNationalStockNumber());
                ExportModel.writeCell(row, colNum++, styles.get("wrapped"), govProperty.getDescription());
                ExportModel.writeCell(row, colNum++, styles.get("general"), govProperty.getProjectContract());
                ExportModel.writeIntCell(row, colNum++, styles.get("center"), govProperty.getReceived());
                ExportModel.writeIntCell(row, colNum++, styles.get("center"), govProperty.getIssued());
                ExportModel.writeIntCell(row, colNum++, styles.get("center"), govProperty.getTransferred());
                ExportModel.writeIntCell(row, colNum++, styles.get("center"), govProperty.getOnHand());
                ExportModel.writeCell(row, colNum++, styles.get("general"), govProperty.getLocation());
                ExportModel.writeCell(row, colNum++, styles.get("general"), govProperty.getDisposition());
                ExportModel.writeCell(row, colNum++, styles.get("general"), govProperty.getCreatedBy());
                ExportModel.writeCell(row, colNum++, styles.get("date"), govProperty.getCreatedDateStr());
                ExportModel.writeCell(row, colNum++, styles.get("general"), govProperty.getLastUpdatedBy());
                ExportModel.writeCell(row, colNum++, styles.get("date"), govProperty.getLastUpdatedDateStr());
            }
            wb.write(outputStream);
        } catch (Exception e) {
            logError("Failed to export Gov Property List.", e);
        } finally {
            try { wb.dispose(); } catch (Exception e) {}
        }
    }
}