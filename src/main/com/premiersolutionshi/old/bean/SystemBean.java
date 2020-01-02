package com.premiersolutionshi.old.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.upload.FormFile;

/**
 * Data holder for a SYSTEM form
 */
public class SystemBean extends BaseBean {
    private static final long serialVersionUID = 7709757033586979115L;

    private String configuredSystemPk = null;
    private String laptopPk = null;
    private String scannerPk = null;
    private String kofaxLicensePk = null;
    private String vrsLicensePk = null;
    private String msOfficeLicensePk = null;
    private String computerName = null;
    private String osVersion = null;
    private String facetVersion = null;
    private String facetVersionOrder = null;
    private String facetVersionHistory = null;
    private String dummyDatabaseVersion = null;
    private String kofaxProductName = null;
    private String currKofaxProductName = null;
    private String kofaxVersion = null;
    private String kofaxVersionHistory = null;
    private String accessVersion = null;
    private String currAccessVersion = null;
    private String accessVersionHistory = null;
    private String documentationVersion = null;
    private String documentationVersionHistory = null;
    private String notes = null;
    private String laptopTag = null;
    private String laptopProductName = null;
    private String laptopSerialNumber = null;
    private String laptopStatus = null;
    private String scannerTag = null;
    private String scannerProductName = null;
    private String scannerSerialNumber = null;
    private String scannerStatus = null;
    private String kofaxLicenseKey = null;
    private String kofaxProductCode = null;
    private String vrsLicenseKey = null;
    private String vrsProductCode = null;
    private String msOfficeProductName = null;
    private String msOfficeLicenseKey = null;
    private String uic = null;
    private String shipName = null;
    private String type = null;
    private String hull = null;
    private String homeport = null;
    private String rsupply = null;
    private String ghostVersion = null;
    private String currGhostVersion = null;
    private ArrayList<FormFile> fileList = null;
    private ArrayList<FileBean> attachedFileList = null;
    private String[] deleteFilePkArr = null;
    private String isPreppedInd = null;
    private ArrayList<SystemLocationBean> locHistList = null;
    private String location = null;
    private String locationDate = null;
    private String reason = null;
    private String networkAdapter = null;
    private String adminPassword = null;
    private String decomDate = null;
    private String dmsVersion = null;
    private String s2ClosureDate = null;
    private String fuelClosureDate = null;
    private BackfileBean backfileBean = null;
    private TrainingBean trainingBean = null;
    private DecomBean decomBean = null;
    private SupportBean lastVisitBean = null;
    private String primaryPocEmails = null;
    private String pocEmails = null;
    private String shipPk = null;
    private String missingTransmittalStr = null;
    private ArrayList<String> missingTransmittalList = null;
    private ArrayList<String> atoMissingList = null;
    private ArrayList<String> atoInstalledList = null;
    private ArrayList<String> atoOutstandingList = null;
    private String rsupplyUpgradeInd = null;
    private ArrayList<String> inactivityList = null;

    private String lastTransmittalNum = null;
    private String lastUploadDate = null;
    private String form1348UploadDate = null;
    private String form1149UploadDate = null;
    private String foodApprovalUploadDate = null;
    private String foodReceiptUploadDate = null;
    private String pcardAdminUploadDate = null;
    private String pcardInvoiceUploadDate = null;
    private String priceChangeUploadDate = null;
    private String sfoedlUploadDate = null;
    private String uolUploadDate = null;
    private String multiShipInd = null;
    private String multiShipUicList = null;
    private ArrayList<ShipBean> multiShipList = null;
    private String transmittalFacetVersion = null;
    private String transmittalFacetVersionOrder = null;
    private String nwcfInd = null;
    private String currContractNumber = null;
    private String contractNumber = null;

    private FormFile hardwareFile = null;
    private FileBean hardwareFileBean = null;
    private String hardwareFilePk = null;
    private String hardwareFileUploadedDate = null;
    private String hardwareFileDeletedInd = null;
    private FormFile trainingFile = null;
    private FileBean trainingFileBean = null;
    private String trainingFilePk = null;
    private String trainingFileUploadedDate = null;
    private String trainingFileDeletedInd = null;
    private FormFile laptop1File = null;
    private FileBean laptop1FileBean = null;
    private String laptop1FilePk = null;
    private String laptop1FileUploadedDate = null;
    private String laptop1FileDeletedInd = null;
    private FormFile laptop2File = null;
    private FileBean laptop2FileBean = null;
    private String laptop2FilePk = null;
    private String laptop2FileUploadedDate = null;
    private String laptop2FileDeletedInd = null;
    private FormFile postInstallFile = null;
    private FileBean postInstallFileBean = null;
    private String postInstallFilePk = null;
    private String postInstallFileUploadedDate = null;
    private String postInstallFileDeletedInd = null;
    private String form1348NoLocationInd = null;
    private String form1348NoClassInd = null;
    private String shipFk;

    public String getAccessVersion() {
        return nes(this.accessVersion);
    }

    public String getAccessVersionHistory() {
        return nes(this.accessVersionHistory);
    }

    public List<String> getAccessVersionHistoryBr() {
        return java.util.Arrays.asList(nes(this.accessVersionHistory).split("\n", -1));
    }

    public String getAdminPassword() {
        return this.adminPassword;
    }

    public ArrayList<String> getAtoInstalledList() {
        return this.atoInstalledList;
    }

    public ArrayList<String> getAtoMissingList() {
        return this.atoMissingList;
    }

    public String getAtoMissingListSize() {
        return this.atoMissingList == null ? "0" : String.valueOf(this.atoMissingList.size());
    }

    public ArrayList<FileBean> getAttachedFileList() {
        return this.attachedFileList == null ? new ArrayList<FileBean>() : this.attachedFileList;
    }

    public BackfileBean getBackfileBean() {
        return this.backfileBean;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getComputerName() {
        return nes(this.computerName);
    }

    public String getComputerNameJs() {
        return js(this.computerName);
    }

    public String getConfiguredSystemPk() {
        return nes(this.configuredSystemPk);
    }

    public String getContractNumber() {
        return this.contractNumber;
    }

    public String getCurrAccessVersion() {
        return nes(this.currAccessVersion);
    }

    public String getCurrContractNumber() {
        return this.currContractNumber;
    }

    public String getCurrGhostVersion() {
        return this.currGhostVersion;
    }

    public String getCurrKofaxProductName() {
        return nes(this.currKofaxProductName);
    }

    public DecomBean getDecomBean() {
        return this.decomBean;
    }

    public String getDecomDate() {
        return this.decomDate;
    }

    public String[] getDeleteFilePkArr() {
        return this.deleteFilePkArr;
    }

    public String getDmsVersion() {
        return this.dmsVersion;
    }

    public String getDocumentationVersion() {
        return nes(this.documentationVersion);
    }

    public List<String> getDocumentationVersionBr() {
        return java.util.Arrays.asList(nes(this.documentationVersion).split("\n", -1));
    }

    public String getDocumentationVersionHistory() {
        return nes(this.documentationVersionHistory);
    }

    public List<String> getDocumentationVersionHistoryBr() {
        return java.util.Arrays.asList(nes(this.documentationVersionHistory).split("\n", -1));
    }

    public String getDummyDatabaseVersion() {
        return this.dummyDatabaseVersion;
    }

    public String getFacetVersion() {
        return nes(this.facetVersion);
    }

    public String getFacetVersionHistory() {
        return nes(this.facetVersionHistory);
    }

    public List<String> getFacetVersionHistoryBr() {
        return java.util.Arrays.asList(nes(this.facetVersionHistory).split("\n", -1));
    }

    public String getFacetVersionOrder() {
        return nes(this.facetVersionOrder);
    }

    public ArrayList<FormFile> getFileList() {
        return this.fileList;
    }

    public String getFoodApprovalUploadDate() {
        return this.foodApprovalUploadDate;
    }

    public String getFoodReceiptUploadDate() {
        return this.foodReceiptUploadDate;
    }

    public String getForm1149UploadDate() {
        return this.form1149UploadDate;
    }

    public String getForm1348NoClassInd() {
        return this.form1348NoClassInd;
    }

    public String getForm1348NoLocationInd() {
        return this.form1348NoLocationInd;
    }

    public String getForm1348UploadDate() {
        return this.form1348UploadDate;
    }

    public String getFuelClosureDate() {
        return this.fuelClosureDate;
    }

    public String getGhostVersion() {
        return this.ghostVersion;
    }

    public FormFile getHardwareFile() {
        return this.hardwareFile;
    }

    public FileBean getHardwareFileBean() {
        return this.hardwareFileBean;
    }

    public String getHardwareFileDeletedInd() {
        return this.hardwareFileDeletedInd;
    }

    public String getHardwareFilePk() {
        return this.hardwareFilePk;
    }

    public String getHardwareFileUploadedDate() {
        return this.hardwareFileUploadedDate;
    }

    public String getHomeport() {
        return nes(this.homeport);
    }

    public String getHull() {
        return nes(this.hull);
    }

    public ArrayList<String> getInactivityList() {
        return this.inactivityList;
    }

    public String getIsPreppedInd() {
        return this.isPreppedInd;
    }

    public String getKofaxLicenseKey() {
        return nes(this.kofaxLicenseKey);
    }

    public String getKofaxLicensePk() {
        return nes(this.kofaxLicensePk);
    }

    public String getKofaxProductCode() {
        return nes(this.kofaxProductCode);
    }

    public String getKofaxProductName() {
        return nes(this.kofaxProductName);
    }

    public String getKofaxVersion() {
        return nes(this.kofaxVersion);
    }

    public String getKofaxVersionHistory() {
        return nes(this.kofaxVersionHistory);
    }

    public List<String> getKofaxVersionHistoryBr() {
        return java.util.Arrays.asList(nes(this.kofaxVersionHistory).split("\n", -1));
    }

    public FormFile getLaptop1File() {
        return this.laptop1File;
    }

    public FileBean getLaptop1FileBean() {
        return this.laptop1FileBean;
    }

    public String getLaptop1FileDeletedInd() {
        return this.laptop1FileDeletedInd;
    }

    public String getLaptop1FilePk() {
        return this.laptop1FilePk;
    }

    public String getLaptop1FileUploadedDate() {
        return this.laptop1FileUploadedDate;
    }

    public FormFile getLaptop2File() {
        return this.laptop2File;
    }

    public FileBean getLaptop2FileBean() {
        return this.laptop2FileBean;
    }

    public String getLaptop2FileDeletedInd() {
        return this.laptop2FileDeletedInd;
    }

    public String getLaptop2FilePk() {
        return this.laptop2FilePk;
    }

    public String getLaptop2FileUploadedDate() {
        return this.laptop2FileUploadedDate;
    }

    public String getLaptopPk() {
        return nes(this.laptopPk);
    }

    public String getLaptopProductName() {
        return nes(this.laptopProductName);
    }

    public String getLaptopSerialNumber() {
        return nes(this.laptopSerialNumber);
    }

    public String getLaptopStatus() {
        return this.laptopStatus;
    }

    public String getLaptopTag() {
        return nes(this.laptopTag);
    }

    public String getLastTransmittalNum() {
        return this.lastTransmittalNum;
    }

    public String getLastUploadDate() {
        return this.lastUploadDate;
    }

    public SupportBean getLastVisitBean() {
        return this.lastVisitBean;
    }

    public String getLocation() {
        return this.location;
    }

    public String getLocationDate() {
        return this.locationDate;
    }

    public ArrayList<SystemLocationBean> getLocHistList() {
        return this.locHistList;
    }

    public ArrayList<String> getMissingTransmittalList() {
        return this.missingTransmittalList;
    }

    public String getMissingTransmittalListSize() {
        return this.missingTransmittalList == null ? "0" : String.valueOf(this.missingTransmittalList.size());
    }

    public String getMissingTransmittalStr() {
        return this.missingTransmittalStr;
    }

    public String getMsOfficeLicenseKey() {
        return nes(this.msOfficeLicenseKey);
    }

    public String getMsOfficeLicensePk() {
        return nes(this.msOfficeLicensePk);
    }

    public String getMsOfficeProductName() {
        return nes(this.msOfficeProductName);
    }

    public String getMultiShipInd() {
        return this.multiShipInd;
    }

    public ArrayList<ShipBean> getMultiShipList() {
        return this.multiShipList;
    }

    public String getMultiShipUicList() {
        return this.multiShipUicList;
    }

    public String getNetworkAdapter() {
        return this.networkAdapter;
    }

    public String getNotes() {
        return nes(this.notes);
    }

    public List<String> getNotesBr() {
        return java.util.Arrays.asList(nes(this.notes).split("\n", -1));
    }

    public String getNwcfInd() {
        return this.nwcfInd;
    }

    public String getPcardAdminUploadDate() {
        return this.pcardAdminUploadDate;
    }

    public String getPcardInvoiceUploadDate() {
        return this.pcardInvoiceUploadDate;
    }

    public String getPocEmails() {
        return this.pocEmails;
    }

    public FormFile getPostInstallFile() {
        return this.postInstallFile;
    }

    public FileBean getPostInstallFileBean() {
        return this.postInstallFileBean;
    }

    public String getPostInstallFileDeletedInd() {
        return this.postInstallFileDeletedInd;
    }

    public String getPostInstallFilePk() {
        return this.postInstallFilePk;
    }

    public String getPostInstallFileUploadedDate() {
        return this.postInstallFileUploadedDate;
    }

    public String getPriceChangeUploadDate() {
        return this.priceChangeUploadDate;
    }

    public String getPrimaryPocEmails() {
        return this.primaryPocEmails;
    }

    public String getReason() {
        return this.reason;
    }

    public String getRsupply() {
        return nes(this.rsupply);
    }

    public String getRsupplyUpgradeInd() {
        return this.rsupplyUpgradeInd;
    }

    public String getS2ClosureDate() {
        return this.s2ClosureDate;
    }

    public String getScannerPk() {
        return nes(this.scannerPk);
    }

    public String getScannerProductName() {
        return nes(this.scannerProductName);
    }

    public String getScannerSerialNumber() {
        return nes(this.scannerSerialNumber);
    }

    public String getScannerStatus() {
        return this.scannerStatus;
    }

    public String getScannerTag() {
        return nes(this.scannerTag);
    }

    public String getSfoedlUploadDate() {
        return this.sfoedlUploadDate;
    }

    public String getShipFk() {
        return shipFk;
    }

    public String getShipName() {
        return nes(this.shipName);
    }

    public String getShipNameFacetName() {
        return nes(this.shipName) + " - " + nes(this.computerName);
    }

    public String getShipNameJs() {
        return js(this.shipName);
    }

    public String getShipPk() {
        return nes(this.shipPk);
    }

    public TrainingBean getTrainingBean() {
        return this.trainingBean;
    }

    public FormFile getTrainingFile() {
        return this.trainingFile;
    }

    public FileBean getTrainingFileBean() {
        return this.trainingFileBean;
    }

    public String getTrainingFileDeletedInd() {
        return this.trainingFileDeletedInd;
    }

    public String getTrainingFilePk() {
        return this.trainingFilePk;
    }

    public String getTrainingFileUploadedDate() {
        return this.trainingFileUploadedDate;
    }

    public String getTransmittalFacetVersion() {
        return this.transmittalFacetVersion;
    }

    public String getTransmittalFacetVersionOrder() {
        return this.transmittalFacetVersionOrder;
    }

    public String getType() {
        return nes(this.type);
    }

    public String getUic() {
        return nes(this.uic);
    }

    public String getUolUploadDate() {
        return this.uolUploadDate;
    }

    public String getVrsLicenseKey() {
        return this.vrsLicenseKey;
    }

    public String getVrsLicensePk() {
        return this.vrsLicensePk;
    }

    public String getVrsProductCode() {
        return this.vrsProductCode;
    }

    public void setAccessVersion(String newAccessVersion) {
        this.accessVersion = newAccessVersion;
    }

    public void setAccessVersionHistory(String newAccessVersionHistory) {
        this.accessVersionHistory = newAccessVersionHistory;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public void setAtoInstalledList(ArrayList<String> atoInstalledList) {
        this.atoInstalledList = atoInstalledList;
    }

    public ArrayList<String> getAtoOutstandingList() {
        return atoOutstandingList;
    }

    public void setAtoOutstandingList(ArrayList<String> atoOutstandingList) {
        this.atoOutstandingList = atoOutstandingList;
    }

    public void setAtoMissingList(ArrayList<String> atoMissingList) {
        this.atoMissingList = atoMissingList;
    }

    public void setAttachedFileList(ArrayList<FileBean> attachedFileList) {
        this.attachedFileList = attachedFileList;
    }

    public void setBackfileBean(BackfileBean backfileBean) {
        this.backfileBean = backfileBean;
    }

    public void setComputerName(String newComputerName) {
        this.computerName = newComputerName;
    }

    public void setConfiguredSystemPk(String newConfiguredSystemPk) {
        this.configuredSystemPk = newConfiguredSystemPk;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public void setCurrAccessVersion(String newCurrAccessVersion) {
        this.currAccessVersion = newCurrAccessVersion;
    }

    public void setCurrContractNumber(String currContractNumber) {
        this.currContractNumber = currContractNumber;
    }

    public void setCurrGhostVersion(String currGhostVersion) {
        this.currGhostVersion = currGhostVersion;
    }

    public void setCurrKofaxProductName(String newCurrKofaxProductName) {
        this.currKofaxProductName = newCurrKofaxProductName;
    }

    public void setDecomBean(DecomBean decomBean) {
        this.decomBean = decomBean;
    }

    public void setDecomDate(String decomDate) {
        this.decomDate = decomDate;
    }

    public void setDeleteFilePkArr(String[] deleteFilePkArr) {
        this.deleteFilePkArr = deleteFilePkArr;
    }

    public void setDmsVersion(String dmsVersion) {
        this.dmsVersion = dmsVersion;
    }

    public void setDocumentationVersion(String newDocumentationVersion) {
        this.documentationVersion = newDocumentationVersion;
    }

    public void setDocumentationVersionHistory(String newDocumentationVersionHistory) {
        this.documentationVersionHistory = newDocumentationVersionHistory;
    }

    public void setDummyDatabaseVersion(String dummyDatabaseVersion) {
        this.dummyDatabaseVersion = dummyDatabaseVersion;
    }

    public void setFacetVersion(String newFacetVersion) {
        this.facetVersion = newFacetVersion;
    }

    public void setFacetVersionHistory(String newFacetVersionHistory) {
        this.facetVersionHistory = newFacetVersionHistory;
    }

    public void setFacetVersionOrder(String facetVersionOrder) {
        this.facetVersionOrder = facetVersionOrder;
    }

    public void setFileList(ArrayList<FormFile> fileList) {
        this.fileList = fileList;
    }

    public void setFoodApprovalUploadDate(String foodApprovalUploadDate) {
        this.foodApprovalUploadDate = foodApprovalUploadDate;
    }

    public void setFoodReceiptUploadDate(String foodReceiptUploadDate) {
        this.foodReceiptUploadDate = foodReceiptUploadDate;
    }

    public void setForm1149UploadDate(String form1149UploadDate) {
        this.form1149UploadDate = form1149UploadDate;
    }

    public void setForm1348NoClassInd(String form1348NoClassInd) {
        this.form1348NoClassInd = form1348NoClassInd;
    }

    public void setForm1348NoLocationInd(String form1348NoLocationInd) {
        this.form1348NoLocationInd = form1348NoLocationInd;
    }

    public void setForm1348UploadDate(String form1348UploadDate) {
        this.form1348UploadDate = form1348UploadDate;
    }

    public void setFuelClosureDate(String fuelClosureDate) {
        this.fuelClosureDate = fuelClosureDate;
    }

    public void setGhostVersion(String ghostVersion) {
        this.ghostVersion = ghostVersion;
    }

    public void setHardwareFile(FormFile hardwareFile) {
        this.hardwareFile = hardwareFile;
    }

    public void setHardwareFileBean(FileBean hardwareFileBean) {
        this.hardwareFileBean = hardwareFileBean;
    }

    public void setHardwareFileDeletedInd(String hardwareFileDeletedInd) {
        this.hardwareFileDeletedInd = hardwareFileDeletedInd;
    }

    public void setHardwareFilePk(String hardwareFilePk) {
        this.hardwareFilePk = hardwareFilePk;
    }

    public void setHardwareFileUploadedDate(String hardwareFileUploadedDate) {
        this.hardwareFileUploadedDate = hardwareFileUploadedDate;
    }

    public void setHomeport(String newHomeport) {
        this.homeport = newHomeport;
    }

    public void setHull(String newHull) {
        this.hull = newHull;
    }

    public void setInactivityList(ArrayList<String> inactivityList) {
        this.inactivityList = inactivityList;
    }

    public void setIsPreppedInd(String isPreppedInd) {
        this.isPreppedInd = isPreppedInd;
    }

    public void setKofaxLicenseKey(String newKofaxLicenseKey) {
        this.kofaxLicenseKey = newKofaxLicenseKey;
    }

    public void setKofaxLicensePk(String newKofaxLicensePk) {
        this.kofaxLicensePk = newKofaxLicensePk;
    }

    public void setKofaxProductCode(String newKofaxProductCode) {
        this.kofaxProductCode = newKofaxProductCode;
    }

    public void setKofaxProductName(String newKofaxProductName) {
        this.kofaxProductName = newKofaxProductName;
    }

    public void setKofaxVersion(String newKofaxVersion) {
        this.kofaxVersion = newKofaxVersion;
    }

    public void setKofaxVersionHistory(String newKofaxVersionHistory) {
        this.kofaxVersionHistory = newKofaxVersionHistory;
    }

    public void setLaptop1File(FormFile laptop1File) {
        this.laptop1File = laptop1File;
    }

    public void setLaptop1FileBean(FileBean laptop1FileBean) {
        this.laptop1FileBean = laptop1FileBean;
    }

    public void setLaptop1FileDeletedInd(String laptop1FileDeletedInd) {
        this.laptop1FileDeletedInd = laptop1FileDeletedInd;
    }

    public void setLaptop1FilePk(String laptop1FilePk) {
        this.laptop1FilePk = laptop1FilePk;
    }

    public void setLaptop1FileUploadedDate(String laptop1FileUploadedDate) {
        this.laptop1FileUploadedDate = laptop1FileUploadedDate;
    }

    public void setLaptop2File(FormFile laptop2File) {
        this.laptop2File = laptop2File;
    }

    public void setLaptop2FileBean(FileBean laptop2FileBean) {
        this.laptop2FileBean = laptop2FileBean;
    }

    public void setLaptop2FileDeletedInd(String laptop2FileDeletedInd) {
        this.laptop2FileDeletedInd = laptop2FileDeletedInd;
    }

    public void setLaptop2FilePk(String laptop2FilePk) {
        this.laptop2FilePk = laptop2FilePk;
    }

    public void setLaptop2FileUploadedDate(String laptop2FileUploadedDate) {
        this.laptop2FileUploadedDate = laptop2FileUploadedDate;
    }

    public void setLaptopPk(String newLaptopPk) {
        this.laptopPk = newLaptopPk;
    }

    public void setLaptopProductName(String newLaptopProductName) {
        this.laptopProductName = newLaptopProductName;
    }

    public void setLaptopSerialNumber(String newLaptopSerialNumber) {
        this.laptopSerialNumber = newLaptopSerialNumber;
    }

    public void setLaptopStatus(String laptopStatus) {
        this.laptopStatus = laptopStatus;
    }

    public void setLaptopTag(String newLaptopTag) {
        this.laptopTag = newLaptopTag;
    }

    public void setLastTransmittalNum(String lastTransmittalNum) {
        this.lastTransmittalNum = lastTransmittalNum;
    }

    public void setLastUploadDate(String lastUploadDate) {
        this.lastUploadDate = lastUploadDate;
    }

    public void setLastVisitBean(SupportBean lastVisitBean) {
        this.lastVisitBean = lastVisitBean;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLocationDate(String locationDate) {
        this.locationDate = locationDate;
    }

    public void setLocHistList(ArrayList<SystemLocationBean> locHistList) {
        this.locHistList = locHistList;
    }

    public void setMissingTransmittalList(ArrayList<String> missingTransmittalList) {
        this.missingTransmittalList = missingTransmittalList;
    }

    public void setMissingTransmittalStr(String missingTransmittalStr) {
        this.missingTransmittalStr = missingTransmittalStr;
    }

    public void setMsOfficeLicenseKey(String newMsOfficeLicenseKey) {
        this.msOfficeLicenseKey = newMsOfficeLicenseKey;
    }

    public void setMsOfficeLicensePk(String newMsOfficeLicensePk) {
        this.msOfficeLicensePk = newMsOfficeLicensePk;
    }

    public void setMsOfficeProductName(String newMsOfficeProductName) {
        this.msOfficeProductName = newMsOfficeProductName;
    }

    public void setMultiShipInd(String multiShipInd) {
        this.multiShipInd = multiShipInd;
    }

    public void setMultiShipList(ArrayList<ShipBean> multiShipList) {
        this.multiShipList = multiShipList;
    }

    public void setMultiShipUicList(String multiShipUicList) {
        this.multiShipUicList = multiShipUicList;
    }

    public void setNetworkAdapter(String networkAdapter) {
        this.networkAdapter = networkAdapter;
    }

    public void setNotes(String newNotes) {
        this.notes = newNotes;
    }

    public void setNwcfInd(String nwcfInd) {
        this.nwcfInd = nwcfInd;
    }

    public void setPcardAdminUploadDate(String pcardAdminUploadDate) {
        this.pcardAdminUploadDate = pcardAdminUploadDate;
    }

    public void setPcardInvoiceUploadDate(String pcardInvoiceUploadDate) {
        this.pcardInvoiceUploadDate = pcardInvoiceUploadDate;
    }

    public void setPocEmails(String pocEmails) {
        this.pocEmails = pocEmails;
    }

    public void setPostInstallFile(FormFile postInstallFile) {
        this.postInstallFile = postInstallFile;
    }

    public void setPostInstallFileBean(FileBean postInstallFileBean) {
        this.postInstallFileBean = postInstallFileBean;
    }

    public void setPostInstallFileDeletedInd(String postInstallFileDeletedInd) {
        this.postInstallFileDeletedInd = postInstallFileDeletedInd;
    }

    public void setPostInstallFilePk(String postInstallFilePk) {
        this.postInstallFilePk = postInstallFilePk;
    }

    public void setPostInstallFileUploadedDate(String postInstallFileUploadedDate) {
        this.postInstallFileUploadedDate = postInstallFileUploadedDate;
    }

    public void setPriceChangeUploadDate(String priceChangeUploadDate) {
        this.priceChangeUploadDate = priceChangeUploadDate;
    }

    public void setPrimaryPocEmails(String primaryPocEmails) {
        this.primaryPocEmails = primaryPocEmails;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setRsupply(String newRsupply) {
        this.rsupply = newRsupply;
    }

    public void setRsupplyUpgradeInd(String rsupplyUpgradeInd) {
        this.rsupplyUpgradeInd = rsupplyUpgradeInd;
    }

    public void setS2ClosureDate(String s2ClosureDate) {
        this.s2ClosureDate = s2ClosureDate;
    }

    public void setScannerPk(String newScannerPk) {
        this.scannerPk = newScannerPk;
    }

    public void setScannerProductName(String newScannerProductName) {
        this.scannerProductName = newScannerProductName;
    }

    public void setScannerSerialNumber(String newScannerSerialNumber) {
        this.scannerSerialNumber = newScannerSerialNumber;
    }

    public void setScannerStatus(String scannerStatus) {
        this.scannerStatus = scannerStatus;
    }

    public void setScannerTag(String newScannerTag) {
        this.scannerTag = newScannerTag;
    }

    public void setSfoedlUploadDate(String sfoedlUploadDate) {
        this.sfoedlUploadDate = sfoedlUploadDate;
    }

    public void setShipFk(String shipFk) {
        this.shipFk = shipFk;
    }

    public void setShipName(String newShipName) {
        this.shipName = newShipName;
    }

    public void setShipPk(String newShipPk) {
        this.shipPk = newShipPk;
    }

    public void setTrainingBean(TrainingBean trainingBean) {
        this.trainingBean = trainingBean;
    }

    public void setTrainingFile(FormFile trainingFile) {
        this.trainingFile = trainingFile;
    }

    public void setTrainingFileBean(FileBean trainingFileBean) {
        this.trainingFileBean = trainingFileBean;
    }

    public void setTrainingFileDeletedInd(String trainingFileDeletedInd) {
        this.trainingFileDeletedInd = trainingFileDeletedInd;
    }

    public void setTrainingFilePk(String trainingFilePk) {
        this.trainingFilePk = trainingFilePk;
    }

    public void setTrainingFileUploadedDate(String trainingFileUploadedDate) {
        this.trainingFileUploadedDate = trainingFileUploadedDate;
    }

    public void setTransmittalFacetVersion(String transmittalFacetVersion) {
        this.transmittalFacetVersion = transmittalFacetVersion;
    }

    public void setTransmittalFacetVersionOrder(String transmittalFacetVersionOrder) {
        this.transmittalFacetVersionOrder = transmittalFacetVersionOrder;
    }

    public void setType(String newType) {
        this.type = newType;
    }

    public void setUic(String newUic) {
        this.uic = newUic;
    }

    public void setUolUploadDate(String uolUploadDate) {
        this.uolUploadDate = uolUploadDate;
    }

    public void setVrsLicenseKey(String vrsLicenseKey) {
        this.vrsLicenseKey = vrsLicenseKey;
    }

    public void setVrsLicensePk(String vrsLicensePk) {
        this.vrsLicensePk = vrsLicensePk;
    }

    public void setVrsProductCode(String vrsProductCode) {
        this.vrsProductCode = vrsProductCode;
    }
} // end of class
