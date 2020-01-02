package com.premiersolutionshi.old.bean;

import java.util.List;

import org.apache.struts.action.ActionForm;

import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Data holder for a HARDWARE form
 *
 * @author   Anthony Tsuhako
 * @version  1.0, 11/25/2013
 * @since    JDK 7, Apache Struts 1.3.10
 */
public class HardwareBean extends ActionForm {
    private static final long serialVersionUID = -7037754192866370746L;

    private boolean isEmpty(String tData) { return (tData == null || tData.trim().length() == 0 || tData.equalsIgnoreCase("null")); }
	//private String nvl(String value, String nullValue) { return (isEmpty(value) ? nullValue : value); }
	private String nes(String tStr) { return (isEmpty(tStr) ? "" : tStr); }
	private String js(String tStr) { return nes(tStr).replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "&quot;").replaceAll("'", "\\\\\'"); }

	private String laptopPk = null;
	public String getLaptopPk() { return nes(this.laptopPk); }
	public void setLaptopPk(String newLaptopPk) { this.laptopPk = newLaptopPk; }

	private String scannerPk = null;
	public String getScannerPk() { return nes(this.scannerPk); }
	public void setScannerPk(String newScannerPk) { this.scannerPk = newScannerPk; }

	private String productName = null;
	public String getProductName() { return nes(this.productName); }
	public String getProductNameJs() { return js(this.productName); }
	public void setProductName(String newProductName) { this.productName = newProductName; }

	private String currProductName = null;
	public String getCurrProductName() { return nes(this.currProductName); }
	public void setCurrProductName(String newCurrProductName) { this.currProductName = newCurrProductName; }

	private String modelNumber = null;
	public String getModelNumber() { return nes(this.modelNumber); }
	public void setModelNumber(String newModelNumber) { this.modelNumber = newModelNumber; }

	private String currModelNumber = null;
	public String getCurrModelNumber() { return nes(this.currModelNumber); }
	public void setCurrModelNumber(String newCurrModelNumber) { this.currModelNumber = newCurrModelNumber; }

	private String serialNumber = null;
	public String getSerialNumber() { return nes(this.serialNumber); }
	public void setSerialNumber(String newSerialNumber) { this.serialNumber = newSerialNumber; }

	private String origin = null;
	public String getOrigin() { return nes(this.origin); }
	public void setOrigin(String newOrigin) { this.origin = newOrigin; }

	private String currOrigin = null;
	public String getCurrOrigin() { return nes(this.currOrigin); }
	public void setCurrOrigin(String newCurrOrigin) { this.currOrigin = newCurrOrigin; }

	private String receivedDate = null;
	public String getReceivedDate() { return nes(this.receivedDate); }
	public void setReceivedDate(String newReceivedDate) { this.receivedDate = newReceivedDate; }

	private String preppedDate = null;
	public String getPreppedDate() { return nes(this.preppedDate); }
	public void setPreppedDate(String newPreppedDate) { this.preppedDate = newPreppedDate; }

	private String tag = null;
	public String getTag() { return nes(this.tag); }
	public void setTag(String newTag) { this.tag = newTag; }

	private String computerName = null;
	public String getComputerName() { return nes(this.computerName); }
	public void setComputerName(String newComputerName) { this.computerName = newComputerName; }

	private String notes = null;
	public String getNotes() { return nes(this.notes); }
	public List<String> getNotesBr() { return java.util.Arrays.asList(nes(this.notes).split("\n", -1)); }
	public void setNotes(String newNotes) { this.notes = newNotes; }

	private String configuredSystemPk = null;
	public String getConfiguredSystemPk() { return nes(this.configuredSystemPk); }
	public void setConfiguredSystemPk(String newConfiguredSystemPk) { this.configuredSystemPk = newConfiguredSystemPk; }

	private String availableOnlyInd = null;
	public String getAvailableOnlyInd() { return nes(this.availableOnlyInd); }
	public void setAvailableOnlyInd(String newAvailableOnlyInd) { this.availableOnlyInd = newAvailableOnlyInd; }

	private String shipName = null;
	public String getShipName() { return this.shipName; }
	public void setShipName(String shipName) { this.shipName = shipName; }

	private String homeport = null;
	public String getHomeport() { return this.homeport; }
	public void setHomeport(String homeport) { this.homeport = homeport; }

	private String macAddress = null;
	public String getMacAddress() { return this.macAddress; }
	public void setMacAddress(String macAddress) { this.macAddress = macAddress; }

	private String status = null;
	public String getStatus() { return this.status; }
	public void setStatus(String status) { this.status = status; }

	private String statusNotes = null;
	public String getStatusNotes() { return this.statusNotes; }
	public void setStatusNotes(String statusNotes) { this.statusNotes = statusNotes; }

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

	private String miscHardwarePk = null;
	public String getMiscHardwarePk() { return this.miscHardwarePk; }
	public void setMiscHardwarePk(String miscHardwarePk) { this.miscHardwarePk = miscHardwarePk; }

	private String productType = null;
	public String getProductType() { return this.productType; }
	public void setProductType(String productType) { this.productType = productType; }

	private String currProductType = null;
	public String getCurrProductType() { return this.currProductType; }
	public void setCurrProductType(String currProductType) { this.currProductType = currProductType; }

	private String warrantyExpiryDate = null;
	public String getWarrantyExpiryDate() { return this.warrantyExpiryDate; }
	public void setWarrantyExpiryDate(String warrantyExpiryDate) { this.warrantyExpiryDate = warrantyExpiryDate; }

	public String getWarrantyExpiryDateCss() {
		int dateDiff = CommonMethods.dateDiff(CommonMethods.getDate("MM/DD/YYYY"), this.warrantyExpiryDate);
		if (this.warrantyExpiryDate == null || !CommonMethods.isValidDateStr(this.warrantyExpiryDate)) {
			return "";
		} else if (dateDiff <= 0) {
			return "color:#f00;";
		} else if (dateDiff <= 30) {
			return "color:#770;";
		} else {
			return "";
		} //end of else
	} //end of getWarrantyExpiryDateCss
} // end of class
