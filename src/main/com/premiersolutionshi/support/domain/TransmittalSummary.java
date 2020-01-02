package com.premiersolutionshi.support.domain;

import java.time.LocalDate;
import java.util.ArrayList;

import com.premiersolutionshi.common.domain.BaseDomain;
import com.premiersolutionshi.common.util.DateUtils;
import com.premiersolutionshi.common.util.StringUtils;
import com.premiersolutionshi.old.util.CommonMethods;

public class TransmittalSummary extends BaseDomain {
    private static final long serialVersionUID = 6720558071819358646L;

    private String computerName;
    private String facetVersion;
    private int facetVersionOrder;
    private int lastTransmittalNum;
    private LocalDate lastUploadDate;
    private LocalDate form1348UploadDate;
    private LocalDate form1149UploadDate;
    private LocalDate foodApprovalUploadDate;
    private LocalDate foodReceiptUploadDate;
    private LocalDate pcardAdminUploadDate;
    private LocalDate pcardInvoiceUploadDate;
    private LocalDate priceChangeUploadDate;
    private LocalDate sfoedlUploadDate;
    private LocalDate uolUploadDate;
    private ArrayList<String> inactivityList;

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public String getFacetVersion() {
        return facetVersion;
    }

    public void setFacetVersion(String facetVersion) {
        this.facetVersion = facetVersion;
    }

    public int getFacetVersionOrder() {
        return facetVersionOrder;
    }

    public void setFacetVersionOrder(int facetVersionOrder) {
        this.facetVersionOrder = facetVersionOrder;
    }

    public int getLastTransmittalNum() {
        return lastTransmittalNum;
    }

    public void setLastTransmittalNum(int lastTransmittalNum) {
        this.lastTransmittalNum = lastTransmittalNum;
    }

    public LocalDate getLastUploadDate() {
        return lastUploadDate;
    }

    public void setLastUploadDate(LocalDate lastUploadDate) {
        this.lastUploadDate = lastUploadDate;
    }

    public LocalDate getForm1348UploadDate() {
        return form1348UploadDate;
    }

    public void setForm1348UploadDate(LocalDate form1348UploadDate) {
        this.form1348UploadDate = form1348UploadDate;
    }

    public LocalDate getForm1149UploadDate() {
        return form1149UploadDate;
    }

    public void setForm1149UploadDate(LocalDate form1149UploadDate) {
        this.form1149UploadDate = form1149UploadDate;
    }

    public LocalDate getFoodApprovalUploadDate() {
        return foodApprovalUploadDate;
    }

    public void setFoodApprovalUploadDate(LocalDate foodApprovalUploadDate) {
        this.foodApprovalUploadDate = foodApprovalUploadDate;
    }

    public LocalDate getFoodReceiptUploadDate() {
        return foodReceiptUploadDate;
    }

    public void setFoodReceiptUploadDate(LocalDate foodReceiptUploadDate) {
        this.foodReceiptUploadDate = foodReceiptUploadDate;
    }

    public LocalDate getPcardAdminUploadDate() {
        return pcardAdminUploadDate;
    }

    public void setPcardAdminUploadDate(LocalDate pcardAdminUploadDate) {
        this.pcardAdminUploadDate = pcardAdminUploadDate;
    }

    public LocalDate getPcardInvoiceUploadDate() {
        return pcardInvoiceUploadDate;
    }

    public void setPcardInvoiceUploadDate(LocalDate pcardInvoiceUploadDate) {
        this.pcardInvoiceUploadDate = pcardInvoiceUploadDate;
    }

    public LocalDate getPriceChangeUploadDate() {
        return priceChangeUploadDate;
    }

    public void setPriceChangeUploadDate(LocalDate priceChangeUploadDate) {
        this.priceChangeUploadDate = priceChangeUploadDate;
    }

    public LocalDate getSfoedlUploadDate() {
        return sfoedlUploadDate;
    }

    public void setSfoedlUploadDate(LocalDate sfoedlUploadDate) {
        this.sfoedlUploadDate = sfoedlUploadDate;
    }

    public LocalDate getUolUploadDate() {
        return uolUploadDate;
    }

    public void setUolUploadDate(LocalDate uolUploadDate) {
        this.uolUploadDate = uolUploadDate;
    }

    public String getLastUploadDateSql() {
        return DateUtils.formatToSqliteDate(lastUploadDate);
    }

    public String getForm1348UploadDateSql() {
        return DateUtils.formatToSqliteDate(form1348UploadDate);
    }

    public String getForm1149UploadDateSql() {
        return DateUtils.formatToSqliteDate(form1149UploadDate);
    }

    public String getFoodApprovalUploadDateSql() {
        return DateUtils.formatToSqliteDate(foodApprovalUploadDate);
    }

    public String getFoodReceiptUploadDateSql() {
        return DateUtils.formatToSqliteDate(foodReceiptUploadDate);
    }

    public String getPcardAdminUploadDateSql() {
        return DateUtils.formatToSqliteDate(pcardAdminUploadDate);
    }

    public String getPcardInvoiceUploadDateSql() {
        return DateUtils.formatToSqliteDate(pcardInvoiceUploadDate);
    }

    public String getPriceChangeUploadDateSql() {
        return DateUtils.formatToSqliteDate(priceChangeUploadDate);
    }

    public String getSfoedlUploadDateSql() {
        return DateUtils.formatToSqliteDate(sfoedlUploadDate);
    }

    public String getUolUploadDateSql() {
        return DateUtils.formatToSqliteDate(uolUploadDate);
    }

    /**
     * Recreation of ReportModel.getInactivityList()
     * @param fuelClosureDate
     * @param s2ClosureDate
     * @param shipType
     * @return List of Inactivity items.
     */
    public ArrayList<String> getInactivityList(LocalDate fuelClosureDate, LocalDate s2ClosureDate, String shipType) {
        inactivityList = new ArrayList<String>();
        //If current day is < 7, set to 1st of previous month; else set to first of current month
        LocalDate dueDate = LocalDate.now();
        int dayOfMonth = dueDate.getDayOfMonth();
        if (dayOfMonth < 7) {
            dueDate = dueDate.minusMonths(1);
        }
        dueDate.withDayOfMonth(1);

        LocalDate today = LocalDate.now();
        addInactivityItem("Material 1348s (required every 10 days -", form1348UploadDate, today, 10);
        addInactivityItem("Fuel 1149s (required by the 7th of each month -", form1149UploadDate, dueDate, 0);

        if (DateUtils.daysDiff(s2ClosureDate, today) >= 0) {
            addInactivityItem("Food Receipts (required every 10 days -", foodReceiptUploadDate, today, 10);
            addInactivityItem("Food Requisitions (required every 10 days -", foodApprovalUploadDate, today, 10);
        }
        addInactivityItem("Purchase Card - Admin Files (required within the past year -", pcardAdminUploadDate, today, 365);
        addInactivityItem("Purchase Card - Invoice Files (required by the 7th of each month -", pcardInvoiceUploadDate, dueDate, 0);
        addInactivityItem("Price Change Reports (required by the 7th of each month -", priceChangeUploadDate, dueDate, 0);
        addInactivityItem("SFOEDL Reports (required by the 7th of each month -", sfoedlUploadDate, dueDate, 0);

        if (!StringUtils.isEmpty(shipType) && !shipType.equals("SSN") && !shipType.equals("SSBN") && !shipType.equals("SSGN")) {
            addInactivityItem("UOL Reports (required by the 7th of each month -", uolUploadDate, dueDate, 0);
        }
        return inactivityList;
    }

    private void addInactivityItem(String inactivityStr, LocalDate uploadDate, LocalDate dueDate, int daysDiffThreshold) {
        if (uploadDate != null) {
            if (DateUtils.daysDiff(uploadDate, dueDate) >= daysDiffThreshold) {
                String uploadDateStr = DateUtils.formatToBasicFormat(uploadDate);
                inactivityList.add(inactivityStr + " last upload: " + uploadDateStr + ")");
            }
        }
        else {
            inactivityList.add(inactivityStr + " no upload history)");
        }
    }

    public static void main(String[] args) {

        String dueDate = CommonMethods.getDate("MM/01/YYYY", CommonMethods.cInt(CommonMethods.getDate("DD")) < 7 ? -7 : 0); 
        System.out.println(dueDate);
        LocalDate dueDate2 = LocalDate.now();
        int dayOfMonth = dueDate2.getDayOfMonth();
        if (dayOfMonth < 7) {
            dueDate2.minusMonths(1);
        }
        System.out.println(dueDate2);
    }
}
