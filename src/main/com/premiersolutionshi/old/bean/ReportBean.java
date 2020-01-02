package com.premiersolutionshi.old.bean;

import java.util.ArrayList;

import org.apache.struts.action.ActionForm;

/**
 * Data holder for a REPORT form
 */
public class ReportBean extends ActionForm {
    private static final long serialVersionUID = 7176869602013089629L;
    private String shipPk = null;
	public String getShipPk() { return this.shipPk; }
	public void setShipPk(String shipPk) { this.shipPk = shipPk; }

	private String reportDate = null;
	public String getReportDate() { return this.reportDate; }
	public void setReportDate(String reportDate) { this.reportDate = reportDate; }

	private String shipName = null;
	public String getShipName() { return this.shipName; }
	public void setShipName(String shipName) { this.shipName = shipName; }

	private String facetName = null;
	public String getFacetName() { return this.facetName; }
	public void setFacetName(String facetName) { this.facetName = facetName; }

	private String homeport = null;
	public String getHomeport() { return this.homeport; }
	public void setHomeport(String homeport) { this.homeport = homeport; }

	private String transmittalNum = null;
	public String getTransmittalNum() { return this.transmittalNum; }
	public void setTransmittalNum(String transmittalNum) { this.transmittalNum = transmittalNum; }

	private String docType = null;
	public String getDocType() { return this.docType; }
	public void setDocType(String docType) { this.docType = docType; }

	private String facetVersion = null;
	public String getFacetVersion() { return this.facetVersion; }
	public void setFacetVersion(String facetVersion) { this.facetVersion = facetVersion; }

	private String uploadDate = null;
	public String getUploadDate() { return this.uploadDate; }
	public void setUploadDate(String uploadDate) { this.uploadDate = uploadDate; }

	private String uploadUser = null;
	public String getUploadUser() { return this.uploadUser; }
	public void setUploadUser(String uploadUser) { this.uploadUser = uploadUser; }

	private String docCnt = null;
	public String getDocCnt() { return this.docCnt; }
	public void setDocCnt(String docCnt) { this.docCnt = docCnt; }

	private String transmittalCnt = null;
	public String getTransmittalCnt() { return this.transmittalCnt; }
	public void setTransmittalCnt(String transmittalCnt) { this.transmittalCnt = transmittalCnt; }

	private String shipCnt = null;
	public String getShipCnt() { return this.shipCnt; }
	public void setShipCnt(String shipCnt) { this.shipCnt = shipCnt; }

	private ArrayList<ReportBean> transmittalList = null;
	public ArrayList<ReportBean> getTransmittalList() { return this.transmittalList; }
	public void setTransmittalList(ArrayList<ReportBean> transmittalList) { this.transmittalList = transmittalList; }

	private String exceptionReason = null;
	public String getExceptionReason() { return this.exceptionReason; }
	public void setExceptionReason(String exceptionReason) { this.exceptionReason = exceptionReason; }

	private String[] shipPkArr = null;
	public String[] getShipPkArr() { return this.shipPkArr; }
	public void setShipPkArr(String[] shipPkArr) { this.shipPkArr = shipPkArr; }

	private String[] transmittalNumArr = null;
	public String[] getTransmittalNumArr() { return this.transmittalNumArr; }
	public void setTransmittalNumArr(String[] transmittalNumArr) { this.transmittalNumArr = transmittalNumArr; }

	private String[] exceptionReasonArr = null;
	public String[] getExceptionReasonArr() { return this.exceptionReasonArr; }
	public void setExceptionReasonArr(String[] exceptionReasonArr) { this.exceptionReasonArr = exceptionReasonArr; }

	private String lastTransmittalNum = null;
	public String getLastTransmittalNum() { return this.lastTransmittalNum; }
	public void setLastTransmittalNum(String lastTransmittalNum) { this.lastTransmittalNum = lastTransmittalNum; }

	private String lastUploadDate = null;
	public String getLastUploadDate() { return this.lastUploadDate; }
	public void setLastUploadDate(String lastUploadDate) { this.lastUploadDate = lastUploadDate; }

	private String form1348UploadDate = null;
	public String getForm1348UploadDate() { return this.form1348UploadDate; }
	public void setForm1348UploadDate(String form1348UploadDate) { this.form1348UploadDate = form1348UploadDate; }

	private String form1149UploadDate = null;
	public String getForm1149UploadDate() { return this.form1149UploadDate; }
	public void setForm1149UploadDate(String form1149UploadDate) { this.form1149UploadDate = form1149UploadDate; }

	private String foodApprovalUploadDate = null;
	public String getFoodApprovalUploadDate() { return this.foodApprovalUploadDate; }
	public void setFoodApprovalUploadDate(String foodApprovalUploadDate) { this.foodApprovalUploadDate = foodApprovalUploadDate; }

	private String foodReceiptUploadDate = null;
	public String getFoodReceiptUploadDate() { return this.foodReceiptUploadDate; }
	public void setFoodReceiptUploadDate(String foodReceiptUploadDate) { this.foodReceiptUploadDate = foodReceiptUploadDate; }

	private String pcardAdminUploadDate = null;
	public String getPcardAdminUploadDate() { return this.pcardAdminUploadDate; }
	public void setPcardAdminUploadDate(String pcardAdminUploadDate) { this.pcardAdminUploadDate = pcardAdminUploadDate; }

	private String pcardInvoiceUploadDate = null;
	public String getPcardInvoiceUploadDate() { return this.pcardInvoiceUploadDate; }
	public void setPcardInvoiceUploadDate(String pcardInvoiceUploadDate) { this.pcardInvoiceUploadDate = pcardInvoiceUploadDate; }

	private String priceChangeUploadDate = null;
	public String getPriceChangeUploadDate() { return this.priceChangeUploadDate; }
	public void setPriceChangeUploadDate(String priceChangeUploadDate) { this.priceChangeUploadDate = priceChangeUploadDate; }

	private String sfoedlUploadDate = null;
	public String getSfoedlUploadDate() { return this.sfoedlUploadDate; }
	public void setSfoedlUploadDate(String sfoedlUploadDate) { this.sfoedlUploadDate = sfoedlUploadDate; }

	private String uolUploadDate = null;
	public String getUolUploadDate() { return this.uolUploadDate; }
	public void setUolUploadDate(String uolUploadDate) { this.uolUploadDate = uolUploadDate; }

	private ArrayList<String> missingTransmittalList = null;
	public ArrayList<String> getMissingTransmittalList() { return this.missingTransmittalList; }
	public String getMissingTransmittalListSize() { return this.missingTransmittalList == null ? "0" : String.valueOf(this.missingTransmittalList.size()); }
	public void setMissingTransmittalList(ArrayList<String> missingTransmittalList) { this.missingTransmittalList = missingTransmittalList; }

	private String lastUploadDateCss = null;
	public String getLastUploadDateCss() { return this.lastUploadDateCss; }
	public void setLastUploadDateCss(String lastUploadDateCss) { this.lastUploadDateCss = lastUploadDateCss; }

	private String form1348UploadDateCss = null;
	public String getForm1348UploadDateCss() { return this.form1348UploadDateCss; }
	public void setForm1348UploadDateCss(String form1348UploadDateCss) { this.form1348UploadDateCss = form1348UploadDateCss; }

	private String form1149UploadDateCss = null;
	public String getForm1149UploadDateCss() { return this.form1149UploadDateCss; }
	public void setForm1149UploadDateCss(String form1149UploadDateCss) { this.form1149UploadDateCss = form1149UploadDateCss; }

	private String foodApprovalUploadDateCss = null;
	public String getFoodApprovalUploadDateCss() { return this.foodApprovalUploadDateCss; }
	public void setFoodApprovalUploadDateCss(String foodApprovalUploadDateCss) { this.foodApprovalUploadDateCss = foodApprovalUploadDateCss; }

	private String foodReceiptUploadDateCss = null;
	public String getFoodReceiptUploadDateCss() { return this.foodReceiptUploadDateCss; }
	public void setFoodReceiptUploadDateCss(String foodReceiptUploadDateCss) { this.foodReceiptUploadDateCss = foodReceiptUploadDateCss; }

	private String pcardAdminUploadDateCss = null;
	public String getPcardAdminUploadDateCss() { return this.pcardAdminUploadDateCss; }
	public void setPcardAdminUploadDateCss(String pcardAdminUploadDateCss) { this.pcardAdminUploadDateCss = pcardAdminUploadDateCss; }

	private String pcardInvoiceUploadDateCss = null;
	public String getPcardInvoiceUploadDateCss() { return this.pcardInvoiceUploadDateCss; }
	public void setPcardInvoiceUploadDateCss(String pcardInvoiceUploadDateCss) { this.pcardInvoiceUploadDateCss = pcardInvoiceUploadDateCss; }

	private String priceChangeUploadDateCss = null;
	public String getPriceChangeUploadDateCss() { return this.priceChangeUploadDateCss; }
	public void setPriceChangeUploadDateCss(String priceChangeUploadDateCss) { this.priceChangeUploadDateCss = priceChangeUploadDateCss; }

	private String sfoedlUploadDateCss = null;
	public String getSfoedlUploadDateCss() { return this.sfoedlUploadDateCss; }
	public void setSfoedlUploadDateCss(String sfoedlUploadDateCss) { this.sfoedlUploadDateCss = sfoedlUploadDateCss; }

	private String uolUploadDateCss = null;
	public String getUolUploadDateCss() { return this.uolUploadDateCss; }
	public void setUolUploadDateCss(String uolUploadDateCss) { this.uolUploadDateCss = uolUploadDateCss; }

	private String missingTransmittalCss = null;
	public String getMissingTransmittalCss() { return this.missingTransmittalCss; }
	public void setMissingTransmittalCss(String missingTransmittalCss) { this.missingTransmittalCss = missingTransmittalCss; }

	private String s2ClosureNotes = null;
	public String getS2ClosureNotes() { return this.s2ClosureNotes; }
	public void setS2ClosureNotes(String s2ClosureNotes) { this.s2ClosureNotes = s2ClosureNotes; }

	private String fuelClosureNotes = null;
	public String getFuelClosureNotes() { return this.fuelClosureNotes; }
	public void setFuelClosureNotes(String fuelClosureNotes) { this.fuelClosureNotes = fuelClosureNotes; }
} // end of class
