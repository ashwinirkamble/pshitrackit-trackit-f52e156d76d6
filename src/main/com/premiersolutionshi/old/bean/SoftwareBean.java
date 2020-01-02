package com.premiersolutionshi.old.bean;

import java.util.List;

import org.apache.struts.action.ActionForm;

import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Data holder for a SOFTWARE form
 *
 * @author   Anthony Tsuhako
 * @version  1.0, 11/25/2013
 * @since    JDK 7, Apache Struts 1.3.10
 */
public class SoftwareBean extends ActionForm {
	private boolean isEmpty(String tData) { return (tData == null || tData.trim().length() == 0 || tData.equalsIgnoreCase("null")); }
	private String nvl(String value, String nullValue) { return (isEmpty(value) ? nullValue : value); }
	private String nes(String tStr) { return (isEmpty(tStr) ? "" : tStr); }
	private String js(String tStr) { return nes(tStr).replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "&quot;").replaceAll("'", "\\\\\'"); }

	private String kofaxLicensePk = null;
	public String getKofaxLicensePk() { return nes(this.kofaxLicensePk); }
	public void setKofaxLicensePk(String newKofaxLicensePk) { this.kofaxLicensePk = newKofaxLicensePk; }

	private String vrsLicensePk = null;
	public String getVrsLicensePk() { return this.vrsLicensePk; }
	public void setVrsLicensePk(String vrsLicensePk) { this.vrsLicensePk = vrsLicensePk; }

	private String msOfficeLicensePk = null;
	public String getMsOfficeLicensePk() { return nes(this.msOfficeLicensePk); }
	public void setMsOfficeLicensePk(String newMsOfficeLicensePk) { this.msOfficeLicensePk = newMsOfficeLicensePk; }

	private String productName = null;
	public String getProductName() { return nes(this.productName); }
	public String getProductNameJs() { return js(this.productName); }
	public void setProductName(String newProductName) { this.productName = newProductName; }

	private String currProductName = null;
	public String getCurrProductName() { return nes(this.currProductName); }
	public void setCurrProductName(String newCurrProductName) { this.currProductName = newCurrProductName; }

	private String licenseKey = null;
	public String getLicenseKey() { return nes(this.licenseKey); }
	public String getLicenseKeyJs() { return js(this.licenseKey); }
	public void setLicenseKey(String newLicenseKey) { this.licenseKey = newLicenseKey; }

	private String productCode = null;
	public String getProductCode() { return nes(this.productCode); }
	public void setProductCode(String newProductCode) { this.productCode = newProductCode; }

	public String getLicenseKeyProductCode() { return (nes(this.internalUseInd).equals("Y") ? "Internal Use Only | " : "") +	nes(this.licenseKey) + " | " + nes(this.productCode); }

	private String receivedDate = null;
	public String getReceivedDate() { return nes(this.receivedDate); }
	public void setReceivedDate(String newReceivedDate) { this.receivedDate = newReceivedDate; }

	private String notes = null;
	public String getNotes() { return nes(this.notes); }
	public List<String> getNotesBr() { return java.util.Arrays.asList(nes(this.notes).replaceAll("\r", "").split("\n", -1)); }
	public void setNotes(String newNotes) { this.notes = newNotes; }

	private String configuredSystemPk = null;
	public String getConfiguredSystemPk() { return nes(this.configuredSystemPk); }
	public void setConfiguredSystemPk(String newConfiguredSystemPk) { this.configuredSystemPk = newConfiguredSystemPk; }

	private String availableOnlyInd = null;
	public String getAvailableOnlyInd() { return nes(this.availableOnlyInd); }
	public void setAvailableOnlyInd(String newAvailableOnlyInd) { this.availableOnlyInd = newAvailableOnlyInd; }

	private String computerName = null;
	public String getComputerName() { return nes(this.computerName); }
	public void setComputerName(String newComputerName) { this.computerName = newComputerName; }

	private String installedCnt = null;
	public String getInstalledCnt() { return this.installedCnt; }
	public void setInstalledCnt(String installedCnt) { this.installedCnt = installedCnt; }

	private String customer = null;
	public String getCustomer() { return this.customer; }
	public void setCustomer(String customer) { this.customer = customer; }

	private String currCustomer = null;
	public String getCurrCustomer() { return this.currCustomer; }
	public void setCurrCustomer(String currCustomer) { this.currCustomer = currCustomer; }

	private String contractNumber = null;
	public String getContractNumber() { return this.contractNumber; }
	public void setContractNumber(String contractNumber) { this.contractNumber = contractNumber; }

	private String currContractNumber = null;
	public String getCurrContractNumber() { return this.currContractNumber; }
	public void setCurrContractNumber(String currContractNumber) { this.currContractNumber = currContractNumber; }

	private String miscLicensePk = null;
	public String getMiscLicensePk() { return this.miscLicensePk; }
	public void setMiscLicensePk(String miscLicensePk) { this.miscLicensePk = miscLicensePk; }

	private String productKey = null;
	public String getProductKey() { return this.productKey; }
	public void setProductKey(String productKey) { this.productKey = productKey; }

	private String licenseExpiryDate = null;
	public String getLicenseExpiryDate() { return this.licenseExpiryDate; }
	public void setLicenseExpiryDate(String licenseExpiryDate) { this.licenseExpiryDate = licenseExpiryDate; }

	public String getLicenseExpiryDateCss() {
		int dateDiff = CommonMethods.dateDiff(CommonMethods.getDate("MM/DD/YYYY"), this.licenseExpiryDate);
		if (this.licenseExpiryDate == null || !CommonMethods.isValidDateStr(this.licenseExpiryDate)) {
			return "";
		} else if (dateDiff <= 0) {
			return "color:#f00;";
		} else if (dateDiff <= 30) {
			return "color:#770;";
		} else {
			return "";
		} //end of else
	} //end of getWarrantyExpiryDateCss

	private String status = null;
	public String getStatus() { return this.status; }
	public void setStatus(String status) { this.status = status; }

	private String statusNotes = null;
	public String getStatusNotes() { return this.statusNotes; }
	public void setStatusNotes(String statusNotes) { this.statusNotes = statusNotes; }

	private String internalUseInd = null;
	public String getInternalUseInd() { return this.internalUseInd; }
	public void setInternalUseInd(String internalUseInd) { this.internalUseInd = internalUseInd; }

	private String[] kofaxLicensePkArr = null;
	public String[] getKofaxLicensePkArr() { return this.kofaxLicensePkArr; }
	public void setKofaxLicensePkArr(String[] kofaxLicensePkArr) { this.kofaxLicensePkArr = kofaxLicensePkArr; }
} // end of class
