package com.premiersolutionshi.support.domain;

import java.time.LocalDate;

import com.premiersolutionshi.common.domain.ModifiedDomain;

public class ConfiguredSystem extends ModifiedDomain {
    private static final long serialVersionUID = -5080849686217149864L;
    private String laptopFk;
    private String scannerFk;
    private String kofaxLicenseFk;
    private String msOfficeLicenseFk;
    private Integer shipFk;
    private String uic;
    private String dmsVersion;
    private String osVersion;
    private String facetVersion;
    private String facetVersionHistory;
    private String kofaxProductName;
    private String kofaxVersion;
    private String kofaxVersionHistory;
    private String accessVersion;
    private String accessVersionHistory;
    private String documentationVersion;
    private String documentationVersionHistory;
    private String notes;
    private String ghostVersion;
    private String isPreppedInd;
    private String dummyDatabaseVersion;
    private String networkAdapter;
    private String adminPassword;
    private LocalDate s2ClosureDate;
    private LocalDate fuelClosureDate;
    private String multiShipInd;
    private String multiShipUicList;
    private String nwcfInd;
    private String contractNumber;
    private String hardwareFileFk;
    private String trainingFileFk;
    private String laptop1FileFk;
    private String laptop2FileFk;
    private String postInstallFileFk;
    private String vrsLicenseFk;
    private String form1348NoLocationInd;
    private String form1348NoClassInd;

    private Ship ship;
    private Laptop laptop;
    private ConfiguredSystemLocHist currentLocation;

    public String getComputerName() {
        return laptop == null ? "Not assigned a laptop #" + getId() : laptop.getComputerName();
    }

    public String getUic() {
        return uic;
    }

    public void setUic(String uic) {
        this.uic = uic;
    }

    public String getFacetVersion() {
        return facetVersion;
    }

    public void setFacetVersion(String facetVersion) {
        this.facetVersion = facetVersion;
    }

    public String getFacetVersionHistory() {
        return facetVersionHistory;
    }

    public void setFacetVersionHistory(String facetVersionHistory) {
        this.facetVersionHistory = facetVersionHistory;
    }

    public String getKofaxProductName() {
        return kofaxProductName;
    }

    public void setKofaxProductName(String kofaxProductName) {
        this.kofaxProductName = kofaxProductName;
    }

    public String getKofaxVersion() {
        return kofaxVersion;
    }

    public void setKofaxVersion(String kofaxVersion) {
        this.kofaxVersion = kofaxVersion;
    }

    public String getKofaxVersionHistory() {
        return kofaxVersionHistory;
    }

    public void setKofaxVersionHistory(String kofaxVersionHistory) {
        this.kofaxVersionHistory = kofaxVersionHistory;
    }

    public String getAccessVersion() {
        return accessVersion;
    }

    public void setAccessVersion(String accessVersion) {
        this.accessVersion = accessVersion;
    }

    public String getAccessVersionHistory() {
        return accessVersionHistory;
    }

    public void setAccessVersionHistory(String accessVersionHistory) {
        this.accessVersionHistory = accessVersionHistory;
    }

    public String getDocumentationVersion() {
        return documentationVersion;
    }

    public void setDocumentationVersion(String documentationVersion) {
        this.documentationVersion = documentationVersion;
    }

    public String getDocumentationVersionHistory() {
        return documentationVersionHistory;
    }

    public void setDocumentationVersionHistory(String documentationVersionHistory) {
        this.documentationVersionHistory = documentationVersionHistory;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getGhostVersion() {
        return ghostVersion;
    }

    public void setGhostVersion(String ghostVersion) {
        this.ghostVersion = ghostVersion;
    }

    public String getIsPreppedInd() {
        return isPreppedInd;
    }

    public void setIsPreppedInd(String isPreppedInd) {
        this.isPreppedInd = isPreppedInd;
    }

    public String getDummyDatabaseVersion() {
        return dummyDatabaseVersion;
    }

    public void setDummyDatabaseVersion(String dummyDatabaseVersion) {
        this.dummyDatabaseVersion = dummyDatabaseVersion;
    }

    public String getNetworkAdapter() {
        return networkAdapter;
    }

    public void setNetworkAdapter(String networkAdapter) {
        this.networkAdapter = networkAdapter;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getDmsVersion() {
        return dmsVersion;
    }

    public void setDmsVersion(String dmsVersion) {
        this.dmsVersion = dmsVersion;
    }

    public LocalDate getS2ClosureDate() {
        return s2ClosureDate;
    }

    public void setS2ClosureDate(LocalDate s2ClosureDate) {
        this.s2ClosureDate = s2ClosureDate;
    }

    public LocalDate getFuelClosureDate() {
        return fuelClosureDate;
    }

    public void setFuelClosureDate(LocalDate fuelClosureDate) {
        this.fuelClosureDate = fuelClosureDate;
    }

    public String getMultiShipInd() {
        return multiShipInd;
    }

    public void setMultiShipInd(String multiShipInd) {
        this.multiShipInd = multiShipInd;
    }

    public String getMultiShipUicList() {
        return multiShipUicList;
    }

    public void setMultiShipUicList(String multiShipUicList) {
        this.multiShipUicList = multiShipUicList;
    }

    public String getNwcfInd() {
        return nwcfInd;
    }

    public void setNwcfInd(String nwcfInd) {
        this.nwcfInd = nwcfInd;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getForm1348NoLocationInd() {
        return form1348NoLocationInd;
    }

    public void setForm1348NoLocationInd(String form1348NoLocationInd) {
        this.form1348NoLocationInd = form1348NoLocationInd;
    }

    public String getForm1348NoClassInd() {
        return form1348NoClassInd;
    }

    public void setForm1348NoClassInd(String form1348NoClassInd) {
        this.form1348NoClassInd = form1348NoClassInd;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getLaptopFk() {
        return laptopFk;
    }

    public void setLaptopFk(String laptopFk) {
        this.laptopFk = laptopFk;
    }

    public String getScannerFk() {
        return scannerFk;
    }

    public void setScannerFk(String scannerFk) {
        this.scannerFk = scannerFk;
    }

    public String getKofaxLicenseFk() {
        return kofaxLicenseFk;
    }

    public void setKofaxLicenseFk(String kofaxLicenseFk) {
        this.kofaxLicenseFk = kofaxLicenseFk;
    }

    public String getMsOfficeLicenseFk() {
        return msOfficeLicenseFk;
    }

    public void setMsOfficeLicenseFk(String msOfficeLicenseFk) {
        this.msOfficeLicenseFk = msOfficeLicenseFk;
    }

    public String getHardwareFileFk() {
        return hardwareFileFk;
    }

    public void setHardwareFileFk(String hardwareFileFk) {
        this.hardwareFileFk = hardwareFileFk;
    }

    public String getTrainingFileFk() {
        return trainingFileFk;
    }

    public void setTrainingFileFk(String trainingFileFk) {
        this.trainingFileFk = trainingFileFk;
    }

    public String getLaptop1FileFk() {
        return laptop1FileFk;
    }

    public void setLaptop1FileFk(String laptop1FileFk) {
        this.laptop1FileFk = laptop1FileFk;
    }

    public String getLaptop2FileFk() {
        return laptop2FileFk;
    }

    public void setLaptop2FileFk(String laptop2FileFk) {
        this.laptop2FileFk = laptop2FileFk;
    }

    public String getPostInstallFileFk() {
        return postInstallFileFk;
    }

    public void setPostInstallFileFk(String postInstallFileFk) {
        this.postInstallFileFk = postInstallFileFk;
    }

    public String getVrsLicenseFk() {
        return vrsLicenseFk;
    }

    public void setVrsLicenseFk(String vrsLicenseFk) {
        this.vrsLicenseFk = vrsLicenseFk;
    }

    public Integer getShipFk() {
        return shipFk;
    }

    public void setShipFk(Integer shipFk) {
        this.shipFk = shipFk;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public Laptop getLaptop() {
        return laptop;
    }

    public void setLaptop(Laptop laptop) {
        this.laptop = laptop;
    }

    public String getLocation() {
        return currentLocation == null ? "" : currentLocation.getLocation();
    }

    public ConfiguredSystemLocHist getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(ConfiguredSystemLocHist currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getPrimaryPocEmails() {
        return ship == null ? null : ship.getPrimaryPocEmails();
    }
}
