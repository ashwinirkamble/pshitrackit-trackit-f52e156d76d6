package com.premiersolutionshi.old.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.struts.upload.FormFile;

import com.premiersolutionshi.old.util.CommonMethods;

/**
 * Data holder for a SUPPORT form
 */

public class SupportBean extends BaseStatusBean {
    private static final long serialVersionUID = 3158162331015867686L;
    private String issuePk = null;
    private String projectPk = null;
    private String shipPk = null;
    private String uic = null;
    private String shipName = null;
    private String type = null;
    private String hull = null;
    private String homeport = null;
    private String title = null;
    private String description = null;
    private String priority = null;
    private String issueCategoryFk = null;
    private String category = null;
    private String phase = null;
    private String currPhase = null;
    private String openedBy = null;
    private String openedDate = null;
    private String closedDate = null;
    private String personAssigned = null;
    private String currPersonAssigned = null;
    private String resolution = null;
    private String totalTime = null;
    private String createdBy = null;
    private String createdDate = null;
    private String comments = null;
    private String[] commentsArr = null;
    private ArrayList<FileBean> issueFileList = null;
    private ArrayList<FormFile> fileList = null;
    private ArrayList<SupportBean> issueCommentsList = null;
    private String[] deleteFilePkArr = null;
    private String[] relatedIssuePkArr = null;
    private String[] deleteRelatedIssuePkArr = null;
    private String issueCnt = null;
    private String openIssueCnt = null;
    private String[] taskArr = null;
    private String[] pocArr = null;
    private String supportVisitDate = null;
    private String supportVisitTime = null;
    private String supportVisitEndTime = null;
    private String logcopCnt = null;
    private String facetCnt = null;
    private String kofaxCnt = null;
    private String dummyCnt = null;
    private String updateCnt = null;
    private String laptopCnt = null;
    private String trainingCnt = null;
    private String backfileCnt = null;
    private String otherCnt = null;
    private String trainer = null;
    private String currTrainer = null;
    private String supportVisitLoc = null;
    private String supportVisitLocNotes = null;
    private String initiatedBy = null;
    private String dept = null;
    private String isEmailSent = null;
    private String isEmailResponded = null;
    private String isTrainingProvided = null;
    private String isTrainingOnsite = null;
    private String lastUpdatedBy = null;
    private String lastUpdatedDate = null;
    private String supportVisitCnt = null;
    private String[] shipPkArr = null;
    private String[] monthlyEmailArr = null;
    private String[] includeShipPkArr = null;
    private String[] reminderIssuePkArr = null;
    private String[] appliedIssuePkArr = null;
    private String[] removeIssuePkArr = null;
    private String atoPk = null;
    private String atoDate = null;
    private String atoFilename = null;
    private String totalCnt = null;
    private String appliedCnt = null;
    private String primaryPocEmails = null;
    private String pocEmails = null;
    private String autoCloseDate = null;
    private String autoCloseStatus = null;
    private String contractNumber = null;
    private String searchMode = null;
    private boolean pagination = false;
    private String sortBy = null;
    private String sortDir = null;
    private int pageNum = 1;
    private String supportVisitReason = null;
    private String[] priorityReasonArr = null;
    private String locationNotes = null;
    private String laptopIssue = null;
    private String scannerIssue = null;
    private String softwareIssue = null;
    private String[] closeIssuePkArr = null;
    private String updateFacetVersion = null;
    private String updateOsVersion = null;
    private String updateConfiguredSystemInd = null;
    private String computerName;
    private String osVersion;
    private String[] configuredSystemPkArr = null;
    private String[] includeConfiguredSystemPkArr = null;

    private Integer configuredSystemFk;

    public String getAppliedCnt() {
        return this.appliedCnt;
    }

    public String[] getAppliedIssuePkArr() {
        return this.appliedIssuePkArr;
    }

    public String getAtoDate() {
        return this.atoDate;
    }

    public String getAtoFilename() {
        return this.atoFilename;
    }

    public String getAtoPk() {
        return this.atoPk;
    }

    public String getAutoCloseDate() {
        return this.autoCloseDate;
    }

    public String getAutoCloseStatus() {
        return this.autoCloseStatus;
    }

    public String getBackfileCnt() {
        return this.backfileCnt;
    }

    public String getCategory() {
        return this.category;
    }

    public String getClosedDate() {
        return this.closedDate;
    }

    public String[] getCloseIssuePkArr() {
        return this.closeIssuePkArr;
    }

    public String getComments() {
        return this.comments;
    }

    public String[] getCommentsArr() {
        return this.commentsArr;
    }

    public List<String> getCommentsBr() {
        return Arrays.asList(CommonMethods.nes(this.comments).split("\n", -1));
    }

    public String getContractNumber() {
        return this.contractNumber;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public String getCreatedDate() {
        return this.createdDate;
    }

    public String getCurrPersonAssigned() {
        return this.currPersonAssigned;
    }

    public String getCurrPhase() {
        return this.currPhase;
    }

    public String getCurrTrainer() {
        return this.currTrainer;
    }

    public String[] getDeleteFilePkArr() {
        return this.deleteFilePkArr;
    }

    public String[] getDeleteRelatedIssuePkArr() {
        return this.deleteRelatedIssuePkArr;
    }

    public void getDeleteRelatedIssuePkArr(String[] deleteRelatedIssuePkArr) {
        this.deleteRelatedIssuePkArr = deleteRelatedIssuePkArr;
    }

    public String getDept() {
        return this.dept;
    }

    public String getDescription() {
        return this.description;
    }

    public List<String> getDescriptionBr() {
        return Arrays.asList(CommonMethods.nes(this.description).split("\n", -1));
    }

    public String getDummyCnt() {
        return this.dummyCnt;
    }

    public String getFacetCnt() {
        return this.facetCnt;
    }

    public ArrayList<FormFile> getFileList() {
        return this.fileList;
    }

    public String getHomeport() {
        return this.homeport;
    }

    public String getHull() {
        return this.hull;
    }

    public String[] getIncludeShipPkArr() {
        return this.includeShipPkArr;
    }

    public String getInitiatedBy() {
        return this.initiatedBy;
    }

    public String getIsEmailResponded() {
        return this.isEmailResponded;
    }

    public String getIsEmailSent() {
        return this.isEmailSent;
    }

    public String getIssueCategoryFk() {
        return this.issueCategoryFk;
    }

    public String getIssueCnt() {
        return this.issueCnt;
    }

    public ArrayList<SupportBean> getIssueCommentsList() {
        return this.issueCommentsList;
    }

    public ArrayList<FileBean> getIssueFileList() {
        return this.issueFileList;
    }

    public String getIssuePk() {
        return this.issuePk;
    }

    public String getIsTrainingOnsite() {
        return this.isTrainingOnsite;
    }

    public String getIsTrainingProvided() {
        return this.isTrainingProvided;
    }

    public String getKofaxCnt() {
        return this.kofaxCnt;
    }

    public String getLaptopCnt() {
        return this.laptopCnt;
    }

    public String getLaptopIssue() {
        return this.laptopIssue;
    }

    public String getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    public String getLastUpdatedDate() {
        return this.lastUpdatedDate;
    }

    public String getLocationNotes() {
        return this.locationNotes;
    }

    public String getLogcopCnt() {
        return this.logcopCnt;
    }

    public String[] getMonthlyEmailArr() {
        return this.monthlyEmailArr;
    }

    public String getOpenedBy() {
        return this.openedBy;
    }

    public String getOpenedDate() {
        return this.openedDate;
    }

    public String getOpenIssueCnt() {
        return this.openIssueCnt;
    }

    public String getOtherCnt() {
        return this.otherCnt;
    }

    public int getPageNum() {
        return this.pageNum;
    }

    public String getPersonAssigned() {
        return this.personAssigned;
    }

    public String getPhase() {
        return this.phase;
    }

    public String[] getPocArr() {
        return this.pocArr;
    }

    public String getPocEmails() {
        return this.pocEmails;
    }

    public String getPrimaryPocEmails() {
        return this.primaryPocEmails;
    }

    public String getPriority() {
        return this.priority;
    }

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

    public String[] getPriorityReasonArr() {
        return this.priorityReasonArr;
    }

    public String getProjectPk() {
        return this.projectPk;
    }

    public String[] getRelatedIssuePkArr() {
        return this.relatedIssuePkArr;
    }

    public String[] getReminderIssuePkArr() {
        return this.reminderIssuePkArr;
    }

    public String[] getRemoveIssuePkArr() {
        return this.removeIssuePkArr;
    }

    public String getResolution() {
        return this.resolution;
    }

    public String getScannerIssue() {
        return this.scannerIssue;
    }

    public String getSearchMode() {
        return this.searchMode;
    }

    public String getShipName() {
        return this.shipName;
    }

    public String getShipPk() {
        return this.shipPk;
    }

    public String[] getShipPkArr() {
        return this.shipPkArr;
    }

    public String getSoftwareIssue() {
        return this.softwareIssue;
    }

    public String getSortBy() {
        return this.sortBy;
    }

    public String getSortDir() {
        return this.sortDir;
    }

    public String getSupportVisitCnt() {
        return this.supportVisitCnt;
    }

    public String getSupportVisitDate() {
        return this.supportVisitDate;
    }

    public String getSupportVisitEndTime() {
        return this.supportVisitEndTime;
    }

    public String getSupportVisitLoc() {
        return this.supportVisitLoc;
    }

    public String getSupportVisitLocNotes() {
        return this.supportVisitLocNotes;
    }

    public String getSupportVisitReason() {
        return this.supportVisitReason;
    }

    public String getSupportVisitTime() {
        return this.supportVisitTime;
    }

    public String[] getTaskArr() {
        return this.taskArr;
    }

    public String getTitle() {
        return this.title;
    }

    public String getTotalCnt() {
        return this.totalCnt;
    }

    public String getTotalTime() {
        return this.totalTime;
    }

    public String getTrainer() {
        return this.trainer;
    }

    public String getTrainingCnt() {
        return this.trainingCnt;
    }

    public String getType() {
        return this.type;
    }

    public String getUic() {
        return this.uic;
    }

    public String getUpdateCnt() {
        return this.updateCnt;
    }

    public String getUpdateConfiguredSystemInd() {
        return this.updateConfiguredSystemInd;
    }

    public String getUpdateFacetVersion() {
        return this.updateFacetVersion;
    }

    public String getUpdateOsVersion() {
        return this.updateOsVersion;
    }

    public boolean isPagination() {
        return this.pagination;
    }

    public void setAppliedCnt(String appliedCnt) {
        this.appliedCnt = appliedCnt;
    }

    public void setAppliedIssuePkArr(String[] appliedIssuePkArr) {
        this.appliedIssuePkArr = appliedIssuePkArr;
    }

    public void setAtoDate(String atoDate) {
        this.atoDate = atoDate;
    }

    public void setAtoFilename(String atoFilename) {
        this.atoFilename = atoFilename;
    }

    public void setAtoPk(String atoPk) {
        this.atoPk = atoPk;
    }

    public void setAutoCloseDate(String autoCloseDate) {
        this.autoCloseDate = autoCloseDate;
    }

    public void setAutoCloseStatus(String autoCloseStatus) {
        this.autoCloseStatus = autoCloseStatus;
    }

    public void setBackfileCnt(String backfileCnt) {
        this.backfileCnt = backfileCnt;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setClosedDate(String closedDate) {
        this.closedDate = closedDate;
    }

    public void setCloseIssuePkArr(String[] closeIssuePkArr) {
        this.closeIssuePkArr = closeIssuePkArr;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setCommentsArr(String[] commentsArr) {
        this.commentsArr = commentsArr;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setCurrPersonAssigned(String currPersonAssigned) {
        this.currPersonAssigned = currPersonAssigned;
    }

    public void setCurrPhase(String currPhase) {
        this.currPhase = currPhase;
    }

    public void setCurrTrainer(String currTrainer) {
        this.currTrainer = currTrainer;
    }

    public void setDeleteFilePkArr(String[] deleteFilePkArr) {
        this.deleteFilePkArr = deleteFilePkArr;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDummyCnt(String dummyCnt) {
        this.dummyCnt = dummyCnt;
    }

    public void setFacetCnt(String facetCnt) {
        this.facetCnt = facetCnt;
    }

    public void setFileList(ArrayList<FormFile> newFileList) {
        this.fileList = newFileList;
    }

    public void setHomeport(String homeport) {
        this.homeport = homeport;
    }

    public void setHull(String hull) {
        this.hull = hull;
    }

    public void setIncludeShipPkArr(String[] includeShipPkArr) {
        this.includeShipPkArr = includeShipPkArr;
    }

    public void setInitiatedBy(String initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    public void setIsEmailResponded(String isEmailResponded) {
        this.isEmailResponded = isEmailResponded;
    }

    public void setIsEmailSent(String isEmailSent) {
        this.isEmailSent = isEmailSent;
    }

    public void setIssueCategoryFk(String issueCategoryFk) {
        this.issueCategoryFk = issueCategoryFk;
    }

    public void setIssueCnt(String issueCnt) {
        this.issueCnt = issueCnt;
    }

    public void setIssueCommentsList(ArrayList<SupportBean> issueCommentsList) {
        this.issueCommentsList = issueCommentsList;
    }

    public void setIssueFileList(ArrayList<FileBean> issueFileList) {
        this.issueFileList = issueFileList;
    }

    public void setIssuePk(String issuePk) {
        this.issuePk = issuePk;
    }

    public void setIsTrainingOnsite(String isTrainingOnsite) {
        this.isTrainingOnsite = isTrainingOnsite;
    }

    public void setIsTrainingProvided(String isTrainingProvided) {
        this.isTrainingProvided = isTrainingProvided;
    }

    public void setKofaxCnt(String kofaxCnt) {
        this.kofaxCnt = kofaxCnt;
    }

    public void setLaptopCnt(String laptopCnt) {
        this.laptopCnt = laptopCnt;
    }

    public void setLaptopIssue(String laptopIssue) {
        this.laptopIssue = laptopIssue;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public void setLocationNotes(String locationNotes) {
        this.locationNotes = locationNotes;
    }

    public void setLogcopCnt(String logcopCnt) {
        this.logcopCnt = logcopCnt;
    }

    public void setMonthlyEmailArr(String[] monthlyEmailArr) {
        this.monthlyEmailArr = monthlyEmailArr;
    }

    public void setOpenedBy(String openedBy) {
        this.openedBy = openedBy;
    }

    public void setOpenedDate(String openedDate) {
        this.openedDate = openedDate;
    }

    public void setOpenIssueCnt(String openIssueCnt) {
        this.openIssueCnt = openIssueCnt;
    }

    public void setOtherCnt(String otherCnt) {
        this.otherCnt = otherCnt;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setPagination(boolean pagination) {
        this.pagination = pagination;
    }

    public void setPersonAssigned(String personAssigned) {
        this.personAssigned = personAssigned;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public void setPocArr(String[] pocArr) {
        this.pocArr = pocArr;
    }

    public void setPocEmails(String pocEmails) {
        this.pocEmails = pocEmails;
    }

    public void setPrimaryPocEmails(String primaryPocEmails) {
        this.primaryPocEmails = primaryPocEmails;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setPriorityReason(String priorityReason) {
        if (priorityReason != null) {
            this.priorityReasonArr = priorityReason.split(", ");
        }
    }

    public void setPriorityReasonArr(String[] priorityReasonArr) {
        this.priorityReasonArr = priorityReasonArr;
    }

    public void setProjectPk(String projectPk) {
        this.projectPk = projectPk;
    }

    public void setRelatedIssuePkArr(String[] relatedIssuePkArr) {
        this.relatedIssuePkArr = relatedIssuePkArr;
    }

    public void setReminderIssuePkArr(String[] reminderIssuePkArr) {
        this.reminderIssuePkArr = reminderIssuePkArr;
    }

    public void setRemoveIssuePkArr(String[] removeIssuePkArr) {
        this.removeIssuePkArr = removeIssuePkArr;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public void setScannerIssue(String scannerIssue) {
        this.scannerIssue = scannerIssue;
    }

    public void setSearchMode(String searchMode) {
        this.searchMode = searchMode;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public void setShipPk(String shipPk) {
        this.shipPk = shipPk;
    }

    public void setShipPkArr(String[] shipPkArr) {
        this.shipPkArr = shipPkArr;
    }

    public void setSoftwareIssue(String softwareIssue) {
        this.softwareIssue = softwareIssue;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }

    public void setSupportVisitCnt(String supportVisitCnt) {
        this.supportVisitCnt = supportVisitCnt;
    }

    public void setSupportVisitDate(String supportVisitDate) {
        this.supportVisitDate = supportVisitDate;
    }

    public void setSupportVisitEndTime(String supportVisitEndTime) {
        this.supportVisitEndTime = supportVisitEndTime;
    }

    public void setSupportVisitLoc(String supportVisitLoc) {
        this.supportVisitLoc = supportVisitLoc;
    }

    public void setSupportVisitLocNotes(String supportVisitLocNotes) {
        this.supportVisitLocNotes = supportVisitLocNotes;
    }

    public void setSupportVisitReason(String supportVisitReason) {
        this.supportVisitReason = supportVisitReason;
    }

    public void setSupportVisitTime(String supportVisitTime) {
        this.supportVisitTime = supportVisitTime;
    }

    public void setTaskArr(String[] taskArr) {
        this.taskArr = taskArr;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTotalCnt(String totalCnt) {
        this.totalCnt = totalCnt;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public void setTrainer(String trainer) {
        this.trainer = trainer;
    }

    public void setTrainingCnt(String trainingCnt) {
        this.trainingCnt = trainingCnt;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUic(String uic) {
        this.uic = uic;
    }

    public void setUpdateCnt(String updateCnt) {
        this.updateCnt = updateCnt;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public void setUpdateConfiguredSystemInd(String updateConfiguredSystemInd) {
        this.updateConfiguredSystemInd = updateConfiguredSystemInd;
    }

    public void setUpdateFacetVersion(String updateFacetVersion) {
        this.updateFacetVersion = updateFacetVersion;
    }

    public void setUpdateOsVersion(String updateOsVersion) {
        this.updateOsVersion = updateOsVersion;
    }

    public String[] getConfiguredSystemPkArr() {
        return configuredSystemPkArr;
    }

    public void setConfiguredSystemPkArr(String[] configuredSystemPkArr) {
        this.configuredSystemPkArr = configuredSystemPkArr;
    }

    public String[] getIncludeConfiguredSystemPkArr() {
        return includeConfiguredSystemPkArr;
    }

    public void setIncludeConfiguredSystemPkArr(String[] includeConfiguredSystemPkArr) {
        this.includeConfiguredSystemPkArr = includeConfiguredSystemPkArr;
    }

    public void setDeleteRelatedIssuePkArr(String[] deleteRelatedIssuePkArr) {
        this.deleteRelatedIssuePkArr = deleteRelatedIssuePkArr;
    }

    public Integer getConfiguredSystemFk() {
        return configuredSystemFk;
    }

    public void setConfiguredSystemFk(Integer configuredSystemFk) {
        this.configuredSystemFk = configuredSystemFk;
    }
}
