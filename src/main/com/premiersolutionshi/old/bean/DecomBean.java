package com.premiersolutionshi.old.bean;

import java.util.List;

import org.apache.struts.action.ActionForm;

import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Data holder for a DECOM form
 *
 * @author Anthony Tsuhako
 * @version 1.0, 11/25/2013
 * @since JDK 7, Apache Struts 1.3.10
 */
public class DecomBean extends ActionForm {
    private static final long serialVersionUID = -5443953717264098560L;

    private String decomWorkflowPk;
    private String shipPk;
    private String shipName;
    private String type;
    private String hull;
    private String homeport;
    private String computerName;
    private String rsupply;
    private String laptopTag;
    private String scannerTag;
    private String systemReceivedDate;
    private String decomDate;
    private String systemReturnedDate;
    private String transmittalCheckDate;
    private String shipContactedDate;
    private String transmittalReconDate;
    private String hardwareStatus;
    private String hardwareStatusNotes;
    private String laptopResetDate;
    private String uic;
    private String backupDate;
    private String contractNumber;
    private String tycomDisplay;
    private String comments;

    public String getDecomWorkflowPk() {
        return this.decomWorkflowPk;
    }

    public void setDecomWorkflowPk(String decomWorkflowPk) {
        this.decomWorkflowPk = decomWorkflowPk;
    }

    public String getShipPk() {
        return this.shipPk;
    }

    public void setShipPk(String shipPk) {
        this.shipPk = shipPk;
    }

    public String getShipName() {
        return this.shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHull() {
        return this.hull;
    }

    public void setHull(String hull) {
        this.hull = hull;
    }

    public String getHomeport() {
        return this.homeport;
    }

    public void setHomeport(String homeport) {
        this.homeport = homeport;
    }

    public String getTycomDisplay() {
        return this.tycomDisplay;
    }

    public void setTycomDisplay(String tycomDisplay) {
        this.tycomDisplay = tycomDisplay;
    }

    public String getComputerName() {
        return this.computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public String getRsupply() {
        return this.rsupply;
    }

    public void setRsupply(String rsupply) {
        this.rsupply = rsupply;
    }

    public String getLaptopTag() {
        return this.laptopTag;
    }

    public void setLaptopTag(String laptopTag) {
        this.laptopTag = laptopTag;
    }

    public String getScannerTag() {
        return this.scannerTag;
    }

    public void setScannerTag(String scannerTag) {
        this.scannerTag = scannerTag;
    }

    public String getSystemReceivedDate() {
        return this.systemReceivedDate;
    }

    public void setSystemReceivedDate(String systemReceivedDate) {
        this.systemReceivedDate = systemReceivedDate;
    }

    public String getDecomDate() {
        return this.decomDate;
    }

    public void setDecomDate(String decomDate) {
        this.decomDate = decomDate;
    }

    public String getSystemReturnedDate() {
        return this.systemReturnedDate;
    }

    public void setSystemReturnedDate(String systemReturnedDate) {
        this.systemReturnedDate = systemReturnedDate;
    }


    public String getBackupDate() {
        return this.backupDate;
    }

    public void setBackupDate(String backupDate) {
        this.backupDate = backupDate;
    }

    public String getTransmittalCheckDate() {
        return this.transmittalCheckDate;
    }

    public void setTransmittalCheckDate(String transmittalCheckDate) {
        this.transmittalCheckDate = transmittalCheckDate;
    }


    public String getComments() {
        return this.comments;
    }

    public List<String> getCommentsBr() {
        return java.util.Arrays.asList(CommonMethods.nes(this.comments).split("\n", -1));
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getShipContactedDate() {
        return this.shipContactedDate;
    }

    public void setShipContactedDate(String shipContactedDate) {
        this.shipContactedDate = shipContactedDate;
    }

    public String getTransmittalReconDate() {
        return this.transmittalReconDate;
    }

    public void setTransmittalReconDate(String transmittalReconDate) {
        this.transmittalReconDate = transmittalReconDate;
    }

    public String getHardwareStatus() {
        return this.hardwareStatus;
    }

    public void setHardwareStatus(String hardwareStatus) {
        this.hardwareStatus = hardwareStatus;
    }

    public String getHardwareStatusNotes() {
        return this.hardwareStatusNotes;
    }

    public void setHardwareStatusNotes(String hardwareStatusNotes) {
        this.hardwareStatusNotes = hardwareStatusNotes;
    }

    public String getLaptopResetDate() {
        return this.laptopResetDate;
    }

    public void setLaptopResetDate(String laptopResetDate) {
        this.laptopResetDate = laptopResetDate;
    }

    public String getUic() {
        return this.uic;
    }

    public void setUic(String uic) {
        this.uic = uic;
    }

    public String getContractNumber() {
        return this.contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }
} // end of class
