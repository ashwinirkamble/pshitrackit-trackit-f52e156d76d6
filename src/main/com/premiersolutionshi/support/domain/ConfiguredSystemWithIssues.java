package com.premiersolutionshi.support.domain;

import java.util.ArrayList;

import com.premiersolutionshi.common.util.DateUtils;
import com.premiersolutionshi.common.util.StringUtils;

public class ConfiguredSystemWithIssues extends ConfiguredSystem {
    private static final long serialVersionUID = 6923359438436280889L;

    private String currFacetVersion;
    private String currOsVersion;
    private int rsupplyUpgradeIssueCount;
    private TransmittalSummary transmittalSummary;
    private ArrayList<String> outstandingIssueList;
    private ArrayList<String> missingAtoStrList;
    private ArrayList<String> inactivityList;
    private TransmittalComputerWithNumList transmittalComputerWithNumList;

    public String getCurrFacetVersion() {
        return currFacetVersion;
    }

    public void setCurrFacetVersion(String currFacetVersion) {
        this.currFacetVersion = currFacetVersion;
    }

    public String getCurrOsVersion() {
        return currOsVersion;
    }

    public void setCurrOsVersion(String currOsVersion) {
        this.currOsVersion = currOsVersion;
    }

    public String getCurrDmsVersion() {
        return DateUtils.getCurrDmsVersion();
    }

    public String getComputerName() {
        Laptop laptop = getLaptop();
        return laptop == null ? null : laptop.getComputerName();
    }

    public int getRsupplyUpgradeIssueCount() {
        return rsupplyUpgradeIssueCount;
    }

    public void setRsupplyUpgradeIssueCount(int rsupplyUpgradeIssueCount) {
        this.rsupplyUpgradeIssueCount = rsupplyUpgradeIssueCount;
    }

    public TransmittalSummary getTransmittalSummary() {
        return transmittalSummary;
    }

    public void setTransmittalSummary(TransmittalSummary transmittalSummary) {
        this.transmittalSummary = transmittalSummary;
    }

    /**
     * List of ATO dates and their corresponding date in this format YYYYMMdd.
     * @return List of ATO date strings.
     */
    public ArrayList<String> getMissingAtoStrList() {
        return missingAtoStrList;
    }

    public void setMissingAtoStrList(ArrayList<String> missingAtoStrList) {
        this.missingAtoStrList = missingAtoStrList;
    }

    public ArrayList<String> getInactivityList() {
        if (inactivityList == null) {
            if (transmittalSummary == null) {
                return null;
            }
            Ship ship = getShip();
            String shipType = ship == null ? null : ship.getType();
            inactivityList = transmittalSummary.getInactivityList(getFuelClosureDate(), getS2ClosureDate(), shipType);
        }
        return inactivityList;
    }

    public TransmittalComputerWithNumList getTransmittalComputerWithNumList() {
        return transmittalComputerWithNumList;
    }

    public void setTransmittalComputerWithNumList(TransmittalComputerWithNumList transmittalComputerWithNumList) {
        this.transmittalComputerWithNumList = transmittalComputerWithNumList;
    }

    public ArrayList<String> getOutstandingIssueList() {
        if (outstandingIssueList == null) {
            outstandingIssueList = new ArrayList<>();
            if (isDmsVersionOutdated()) {
                outstandingIssueList.add("DMS as of " + getDmsVersion());
            }
            if (isFacetVersionOutdated()) {
                outstandingIssueList.add("FACET v" + getFacetVersion());
            }
            if (isOsVersionOutdated()) {
                outstandingIssueList.add("OS Version " + getOsVersion());
            }
            if (isMissingAto()) {
                outstandingIssueList.add(missingAtoStrList.size() + " Pending ATO Updates");
            }
            if (rsupplyUpgradeIssueCount > 0) {
                outstandingIssueList.add("RSupply Upgrade");
            }
            if (isTransmittalInactivity()) {
                outstandingIssueList.add("DACS Inactivity");//"LOGCOP Inactivity"
            }
            if (transmittalComputerWithNumList != null && transmittalComputerWithNumList.getMissingCount() > 0) {
                outstandingIssueList.add(transmittalComputerWithNumList.getMissingCount() + " missing transmittals");
            }
        }
        return outstandingIssueList;
    }

    public boolean isDmsVersionOutdated() {
        return !StringUtils.isEmpty(getDmsVersion()) && !getDmsVersion().equals(getCurrDmsVersion());
    }

    public boolean isFacetVersionOutdated() {
        return !StringUtils.isEmpty(getFacetVersion()) && !getFacetVersion().equals(currFacetVersion);
    }

    public boolean isOsVersionOutdated() {
        return !StringUtils.isEmpty(getOsVersion()) && !getOsVersion().equals(currOsVersion);
    }

    public boolean isTransmittalInactivity() {
        ArrayList<String> inactivityList = getInactivityList();
        return inactivityList != null && !inactivityList.isEmpty();
    }

    public String getMonthlyEmailMessage() {
        return getMonthlyEmailMessage(currFacetVersion, currOsVersion);
    }

    public boolean isMissingAto() {
        return missingAtoStrList != null && !missingAtoStrList.isEmpty();
    }

    public String getMonthlyEmailMessage(String currFacetVersion, String currOsVersion) {
        StringBuilder message = new StringBuilder();
        message
        .append("ALCON,\n\n")
        .append("This is a monthly reminder from the FACET Support Team regarding your FACET system. The following items are currently pending for your unit:\n\n")
        .append("DEFECTIVE MATERIAL REFERENCE (DMS) FILE UPDATE:\n")
        .append("The latest DMS file has been posted to LOGCOP-FACET. To update the file on your system, download the 'DefectiveMaterialReference.xls' ")
        .append("file from the 'FACET System Updates' section of the File Repository link in LOGCOP-FACET and copy it into the 'FIARModule' folder on ")
        .append("the C drive of the FACET laptop, overwriting the existing file.\n\nPlease confirm when this has been done.\n\n")
        .append(getAtoMissingMessage())
        .append(getFacetVersionMessage())
        .append(getOsVersionMessage())
        .append(getRsupplyUpgradeIndMessage())
        .append(getInactivityMessage())
        .append(getMissingTransmittalMessage())
        .append("\nIf you have any questions, please contact FACET Support at support@premiersolutionshi.com or 808-396-4444\n\n")
        .append("V/R,\n")
        .append("FACET Support Team\n")
        .append("T: (808) 396-4444\n")
        .append("E: support@premiersolutionshi.com\n");
        return message.toString();
      }

    private String getOsVersionMessage() {
        StringBuilder message = new StringBuilder();
        if (isOsVersionOutdated()) {
            message.append("OPERATING SYSTEM UPDATE:\n")
                .append("Your operating system needs to be updated to the latest version (")
                .append(currOsVersion)
                .append("). You should have been notified about this previously and given instructions.")
                .append("Please confirm when this has been done.\n\n");
        }
        return message.toString();
    }

    public String getMissingTransmittalMessage() {
        StringBuilder message = new StringBuilder();
          TransmittalComputerWithNumList transmittalNumList = getTransmittalComputerWithNumList();
          if (transmittalNumList != null && transmittalNumList.getMissingCount() > 0) {
              message.append("MISSING TRANSMITTALS:\n")
              .append("The following export transmittals (ZIP files) have been generated on your system but have not been uploaded to ")
              .append("the LOGCOP-FACET website. Please upload the associated ZIP files ASAP to ensure your system is backed up and ")
              .append("files are accessible ashore:\n");
            for (String missingTransmittal : transmittalNumList.getMissingTransmittalStrNums()) {
              message.append("-- ").append(missingTransmittal).append("\n");
            }
            message.append("\n");
          }
          return message.toString();
      }

    /**
     * See "ReportModel.getInactivityList(resultBean)"
     * @return
     */
    private String getInactivityMessage() {
        ArrayList<String> inactivityList = getInactivityList();
        StringBuilder message = new StringBuilder();
        if (isTransmittalInactivity()) {
            message.append("UPLOAD ACTIVITY:\n")
            .append("Your command has no record of activity within the required timeframe in LOGCOP-FACET for the following types ")
            .append("of documents. Please upload the appropriate FACET files ASAP:\n");
          for (String inactivity : inactivityList) {
              message.append("-- ").append(inactivity).append("\n");
          }
          message.append("\n");
        }
        return message.toString();
      }

    private String getRsupplyUpgradeIndMessage() {
        StringBuilder message = new StringBuilder();
        if (getRsupplyUpgradeIssueCount() > 0) {
            message.append("RSUPPLY UPGRADE:\n")
            .append("Your system needs to be updated to match your current RSUPPLY version. You should have been notified about ")
            .append("this previously and given instructions. Please confirm when this has been done.\n\n");
        }
        return message.toString();
    }


    private String getFacetVersionMessage() {
        StringBuilder message = new StringBuilder();
        if (isFacetVersionOutdated()) {
            message.append("FACET SYSTEM UPDATE:\n")
            .append("Your system needs to be updated to the latest version (")
            .append(currFacetVersion)
            .append("). You should have been notified about this previously and given instructions. ")
            .append("Please confirm when this has been done.\n\n");
        }
        return message.toString();
    }

    private String getAtoMissingMessage() {
        StringBuilder message = new StringBuilder();
        if (isMissingAto()) {
            message.append("ATO UPDATES:\nThe following ATO Updates need to be applied:\n");
            for (String atoMissing : missingAtoStrList) {
                message.append("-- ATOUpdates_").append(atoMissing).append("\n");
            }
            message.append("\nFollow the procedures in each associated PDF file to update your system. Please confirm when this has been done.\n");
        }
        return message.toString();
    }

    public static ConfiguredSystemWithIssues copy(ConfiguredSystem cs) {
        ConfiguredSystemWithIssues withIssues = new ConfiguredSystemWithIssues();
        withIssues.setId(cs.getId());

        withIssues.setShip(cs.getShip());
        withIssues.setLaptop(cs.getLaptop());
        withIssues.setCurrentLocation(cs.getCurrentLocation());

        withIssues.setShipFk(cs.getShipFk());
        withIssues.setLaptopFk(cs.getLaptopFk());
        withIssues.setScannerFk(cs.getScannerFk());
        withIssues.setKofaxLicenseFk(cs.getKofaxLicenseFk());
        withIssues.setMsOfficeLicenseFk(cs.getMsOfficeLicenseFk());
        withIssues.setUic(cs.getUic());
        withIssues.setFacetVersion(cs.getFacetVersion());
        withIssues.setFacetVersionHistory(cs.getFacetVersionHistory());
        withIssues.setKofaxProductName(cs.getKofaxProductName());
        withIssues.setKofaxVersion(cs.getKofaxVersion());
        withIssues.setKofaxVersionHistory(cs.getKofaxVersionHistory());
        withIssues.setAccessVersion(cs.getAccessVersion());
        withIssues.setAccessVersionHistory(cs.getAccessVersionHistory());
        withIssues.setDocumentationVersion(cs.getDocumentationVersion());
        withIssues.setDocumentationVersionHistory(cs.getDocumentationVersionHistory());
        withIssues.setNotes(cs.getNotes());
        withIssues.setLastUpdatedBy(cs.getLastUpdatedBy());
        withIssues.setLastUpdatedDate(cs.getLastUpdatedDate());
        withIssues.setGhostVersion(cs.getGhostVersion());
        withIssues.setIsPreppedInd(cs.getIsPreppedInd());
        withIssues.setDummyDatabaseVersion(cs.getDummyDatabaseVersion());
        withIssues.setNetworkAdapter(cs.getNetworkAdapter());
        withIssues.setAdminPassword(cs.getAdminPassword());
        withIssues.setDmsVersion(cs.getDmsVersion());
        withIssues.setS2ClosureDate(cs.getS2ClosureDate());
        withIssues.setFuelClosureDate(cs.getFuelClosureDate());
        withIssues.setMultiShipInd(cs.getMultiShipInd());
        withIssues.setMultiShipUicList(cs.getMultiShipUicList());
        withIssues.setNwcfInd(cs.getNwcfInd());
        withIssues.setContractNumber(cs.getContractNumber());
        withIssues.setHardwareFileFk(cs.getHardwareFileFk());
        withIssues.setTrainingFileFk(cs.getTrainingFileFk());
        withIssues.setLaptop1FileFk(cs.getLaptop1FileFk());
        withIssues.setLaptop2FileFk(cs.getLaptop2FileFk());
        withIssues.setPostInstallFileFk(cs.getPostInstallFileFk());
        withIssues.setVrsLicenseFk(cs.getVrsLicenseFk());
        withIssues.setForm1348NoLocationInd(cs.getForm1348NoLocationInd());
        withIssues.setForm1348NoClassInd(cs.getForm1348NoClassInd());
        withIssues.setOsVersion(cs.getOsVersion());
        return withIssues;
    }
}
