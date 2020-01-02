package com.premiersolutionshi.support.ui.form;

import java.util.ArrayList;

import org.apache.struts.upload.FormFile;

import com.premiersolutionshi.old.bean.SupportBean;
import com.premiersolutionshi.old.util.CommonMethods;
import com.premiersolutionshi.support.domain.ConfiguredSystem;
import com.premiersolutionshi.support.domain.Issue;
import com.premiersolutionshi.support.domain.Ship;

public class IssueForm extends Issue {
    private static final long serialVersionUID = 3068821158747897493L;

    private String laptopIssue;
    private String scannerIssue;
    private String softwareIssue;
    private String currPersonAssigned;
    private String currTrainer;
    private String[] priorityReasonArr;
    private String[] commentsArr;
    private String[] closeIssuePkArr;
    private String[] deleteFilePkArr;
    private ArrayList<FormFile> fileList;

    @Override
    public String getPriorityReason() {
        if (this.priorityReasonArr == null) {
            return null;
        }
        StringBuilder str = new StringBuilder();
        for (String priorityReason : this.priorityReasonArr) {
            if (str.length() > 0) {
                str.append(", ");
            }
            str.append(priorityReason);
        }
        return str.toString();
    }

    public String getPriorityReasonBanner() {
        if (this.priorityReasonArr == null) {
            return null;
        }
        StringBuilder str = new StringBuilder();
        for (String priorityReason : this.priorityReasonArr) {
            if (str.length() > 0) {
                str.append(", ");
            }
            str.append(priorityReason);
            if ((priorityReason.equals("Laptop Down")) && (!CommonMethods.isEmpty(this.laptopIssue))) {
                str.append(" (" + this.laptopIssue + ")");
            }
            else if ((priorityReason.equals("Scanner Down")) && (!CommonMethods.isEmpty(this.scannerIssue))) {
                str.append(" (" + this.scannerIssue + ")");
            }
            else if ((priorityReason.equals("Software Issue")) && (!CommonMethods.isEmpty(this.softwareIssue))) {
                str.append(" (" + this.softwareIssue + ")");
            }
        }
        return str.toString();
    }

    @Override
    public void setPriorityReason(String priorityReason) {
        super.setPriorityReason(priorityReason);
        if (priorityReason != null) {
            setPriorityReasonArr(priorityReason.split(", "));
        }
    }

    public String[] getPriorityReasonArr() {
        return this.priorityReasonArr;
    }

    public void setPriorityReasonArr(String[] priorityReasonArr) {
        this.priorityReasonArr = priorityReasonArr;
    }

    public String[] getCommentsArr() {
        return commentsArr;
    }

    public void setCommentsArr(String[] commentsArr) {
        this.commentsArr = commentsArr;
    }

    public String[] getCloseIssuePkArr() {
        return closeIssuePkArr;
    }

    public void setCloseIssuePkArr(String[] closeIssuePkArr) {
        this.closeIssuePkArr = closeIssuePkArr;
    }

    public String[] getDeleteFilePkArr() {
        return deleteFilePkArr;
    }

    public void setDeleteFilePkArr(String[] deleteFilePkArr) {
        this.deleteFilePkArr = deleteFilePkArr;
    }

    @Override
    public Ship getShip() {
        Ship ship = super.getShip();
        return ship == null ? new Ship() : ship;
    }

    @Override
    public ConfiguredSystem getConfiguredSystem() {
        ConfiguredSystem configuredSystem = super.getConfiguredSystem();
        return configuredSystem == null ? new ConfiguredSystem() : configuredSystem;
    }

    public String getLaptopIssue() {
        return laptopIssue;
    }

    public void setLaptopIssue(String laptopIssue) {
        this.laptopIssue = laptopIssue;
    }

    public String getScannerIssue() {
        return scannerIssue;
    }

    public void setScannerIssue(String scannerIssue) {
        this.scannerIssue = scannerIssue;
    }

    public String getSoftwareIssue() {
        return softwareIssue;
    }

    public void setSoftwareIssue(String softwareIssue) {
        this.softwareIssue = softwareIssue;
    }

    public String getCurrPersonAssigned() {
        return currPersonAssigned;
    }

    public void setCurrPersonAssigned(String currPersonAssigned) {
        this.currPersonAssigned = currPersonAssigned;
    }

    public String getCurrTrainer() {
        return currTrainer;
    }

    public void setCurrTrainer(String currTrainer) {
        this.currTrainer = currTrainer;
    }

    public ArrayList<FormFile> getFileList() {
        return fileList;
    }

    public void setFileList(ArrayList<FormFile> fileList) {
        this.fileList = fileList;
    }

    public void copy(Issue domain) {
        if (domain == null) {
            return;
        }
        super.copy(domain);
        setShip(domain.getShip());
        setConfiguredSystem(domain.getConfiguredSystem());
        setIssueCategory(domain.getIssueCategory());
        setIssueFileList(domain.getIssueFileList());

        setProjectFk(domain.getProjectFk());
        setShipFk(domain.getShipFk());
        setTitle(domain.getTitle());
        String description = domain.getDescription();
        //description += "\n\nCopied from Issue #" + domain.getId();
        setDescription(description);
        setStatus(domain.getStatus());
        setPriority(domain.getPriority());

        setIssueCommentList(domain.getIssueCommentList());
        setLaptopIssue(domain.getLaptopIssue());
        setScannerIssue(domain.getScannerIssue());
        setSoftwareIssue(domain.getSoftwareIssue());
        setPriorityReason(domain.getPriorityReason());

        setPhase(domain.getPhase());
        setOpenedBy(domain.getOpenedBy());
        setOpenedDate(domain.getOpenedDate());
        setClosedDate(domain.getClosedDate());
        setPersonAssigned(domain.getPersonAssigned());
        setCurrPersonAssigned(domain.getPersonAssigned());
        setSupportVisitDate(domain.getSupportVisitDate());
        setSupportVisitLoc(domain.getSupportVisitLoc());
        setSupportVisitTime(domain.getSupportVisitTime());
        setTrainer(domain.getTrainer());
        setCurrTrainer(domain.getTrainer());
        setResolution(domain.getResolution());
        setTotalTime(domain.getTotalTime());
        setInitiatedBy(domain.getInitiatedBy());
        setDept(domain.getDept());
        setIsEmailSent(domain.getIsEmailSent());
        setIsEmailResponded(domain.getIsEmailResponded());
        setIsTrainingProvided(domain.getIsTrainingProvided());
        setIsTrainingOnsite(domain.getIsTrainingOnsite());
        setAtoFk(domain.getAtoFk());
        setAutoCloseDate(domain.getAutoCloseDate());
        setIssueCategoryFk(domain.getIssueCategoryFk());
        setSupportVisitEndTime(domain.getSupportVisitEndTime());
        setSupportVisitReason(domain.getSupportVisitReason());
        setSupportVisitLocNotes(domain.getSupportVisitLocNotes());
        setAutoCloseStatus(domain.getAutoCloseStatus());
        setConfiguredSystemFk(domain.getConfiguredSystemFk());
    }

    public SupportBean getBean() {
        SupportBean bean = super.getBean();
        bean.setCommentsArr(getCommentsArr());
        bean.setFileList(getFileList());
        return bean;
    }
}
