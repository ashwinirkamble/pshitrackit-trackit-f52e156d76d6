package com.premiersolutionshi.support.domain;

import java.time.LocalDate;
import java.util.ArrayList;

import com.premiersolutionshi.common.domain.FileInfo;
import com.premiersolutionshi.common.ui.form.ValueAndLabel;
import com.premiersolutionshi.common.util.DateUtils;
import com.premiersolutionshi.common.util.StringUtils;
import com.premiersolutionshi.old.bean.SupportBean;
import com.premiersolutionshi.support.ui.action.IssueAction;

public class Issue extends IssueSupportVisit {
    private static final long serialVersionUID = 9069152451000505725L;
    private Integer projectFk;
    private Integer shipFk;
    private Integer atoFk;
    private Integer configuredSystemFk;
    private String title;
    private String description;
    private String priority;
    private String phase;
    private String openedBy;
    private LocalDate openedDate;
    private LocalDate closedDate;
    private String trainer;
    private String resolution;
    private Integer totalTime;
    private String initiatedBy;
    private String dept;
    private String isEmailSent;
    private String isEmailResponded;
    private String isTrainingProvided;
    private String isTrainingOnsite;
    private LocalDate autoCloseDate;
    private String priorityReason;
    private String laptopIssue;
    private String scannerIssue;
    private String softwareIssue;
    private String autoCloseStatus;

    private Ship ship;
    private ConfiguredSystem configuredSystem;
    private ArrayList<IssueComments> issueCommentList;
    private ArrayList<FileInfo> issueFileList;

    public String getComputerName() {
        if (configuredSystem != null && configuredSystem.getLaptop() != null) {
            return configuredSystem.getLaptop().getComputerName();
        }
        return "N/A";
    }

    public String getOpenedDateStr() {
        return openedDate == null ? DateUtils.getNowInBasicFormat() : DateUtils.COMMON_BASIC_FORMAT.format(openedDate);
    }

    public void setOpenedDateStr(String openedDateStr) {
        if (StringUtils.isEmpty(openedDateStr)) {
            return;
        }
        setOpenedDate(DateUtils.parseBasicDate(openedDateStr));
    }

    public String getClosedDateStr() {
        return closedDate == null ? null : DateUtils.COMMON_BASIC_FORMAT.format(closedDate);
    }

    public void setClosedDateStr(String closedDateStr) {
        if (StringUtils.isEmpty(closedDateStr)) {
            return;
        }
        setClosedDate(DateUtils.parseBasicDate(closedDateStr));
    }

    public Integer getProjectFk() {
        return projectFk;
    }

    public void setProjectFk(Integer projectFk) {
        this.projectFk = projectFk;
    }

    public Integer getShipFk() {
        return shipFk;
    }

    public void setShipFk(Integer shipFk) {
        this.shipFk = shipFk;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getOpenedBy() {
        return openedBy;
    }

    public void setOpenedBy(String openedBy) {
        this.openedBy = openedBy;
    }

    public LocalDate getOpenedDate() {
        return openedDate;
    }

    public void setOpenedDate(LocalDate openedDate) {
        this.openedDate = openedDate;
    }

    public LocalDate getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(LocalDate closedDate) {
        this.closedDate = closedDate;
    }

    public String getOpenedDateSql() {
        return DateUtils.formatToSqliteDate(openedDate);
    }

    public void setOpenedDateSql(String openedDateSql) {
        this.openedDate = DateUtils.parseSqliteDate(openedDateSql);
    }

    public String getClosedDateSql() {
        return DateUtils.formatToSqliteDate(closedDate);
    }

    public void setClosedDateSql(String closedDateSql) {
        this.closedDate = DateUtils.parseSqliteDate(closedDateSql);
    }

    public String getTrainer() {
        return trainer;
    }

    public void setTrainer(String trainer) {
        this.trainer = trainer;
    }

    public String getResolution() {
        return resolution == null ? "" : resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public String getTotalTimeStr() {
        if (totalTime != null) {
            ValueAndLabel valueAndLabel = IssueAction.getValueAndLabel(totalTime);
            return valueAndLabel.getLabel();
        }
        return "";
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public String getInitiatedBy() {
        return initiatedBy;
    }

    public void setInitiatedBy(String initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public boolean isEmailSent() {
        return StringUtils.safeEquals("Y", isEmailSent);
    }

    public boolean isEmailResponded() {
        return StringUtils.safeEquals("Y", isEmailResponded);
    }

    public boolean isTrainingProvided() {
        return StringUtils.safeEquals("Y", isTrainingProvided);
    }

    public String getIsEmailSent() {
        return isEmailSent;
    }

    public void setIsEmailSent(String isEmailSent) {
        this.isEmailSent = isEmailSent;
    }

    public String getIsEmailResponded() {
        return isEmailResponded;
    }

    public void setIsEmailResponded(String isEmailResponded) {
        this.isEmailResponded = isEmailResponded;
    }

    public String getIsTrainingProvided() {
        return isTrainingProvided;
    }

    

    public void setIsTrainingProvided(String isTrainingProvided) {
        this.isTrainingProvided = isTrainingProvided;
    }

    public String getIsTrainingOnsite() {
        return isTrainingOnsite;
    }

    public void setIsTrainingOnsite(String isTrainingOnsite) {
        this.isTrainingOnsite = isTrainingOnsite;
    }

    public Integer getAtoFk() {
        return atoFk;
    }

    public void setAtoFk(Integer atoFk) {
        this.atoFk = atoFk;
    }

    public String getAutoCloseDateSql() {
        return DateUtils.formatToSqliteDate(autoCloseDate);
    }

    public String getAutoCloseDateStr() {
        return DateUtils.formatToBasicFormat(autoCloseDate);
    }

    public void setAutoCloseDateStr(String autoCloseDate) {
        this.autoCloseDate = DateUtils.parseBasicDate(autoCloseDate);
    }

    public LocalDate getAutoCloseDate() {
        return autoCloseDate;
    }
    
    public void setAutoCloseDate(LocalDate autoCloseDate) {
        this.autoCloseDate = autoCloseDate;
    }

    public Integer getConfiguredSystemFk() {
        return configuredSystemFk;
    }

    public void setConfiguredSystemFk(Integer configuredSystemFk) {
        if (configuredSystemFk != null && configuredSystemFk > 0) {
            this.configuredSystemFk = configuredSystemFk;
        }
    }

    public String getPriorityReason() {
        return priorityReason;
    }

    public void setPriorityReason(String priorityReason) {
        this.priorityReason = priorityReason;
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

    public String getAutoCloseStatus() {
        return autoCloseStatus;
    }

    public void setAutoCloseStatus(String autoCloseStatus) {
        this.autoCloseStatus = autoCloseStatus;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public ConfiguredSystem getConfiguredSystem() {
        return configuredSystem;
    }

    public void setConfiguredSystem(ConfiguredSystem configuredSystem) {
        this.configuredSystem = configuredSystem;
    }

    public ArrayList<IssueComments> getIssueCommentList() {
        if (issueCommentList == null) {
            issueCommentList = new ArrayList<>();
        }
        return issueCommentList;
    }

    public void setIssueCommentList(ArrayList<IssueComments> issueCommentList) {
        this.issueCommentList = issueCommentList;
    }

    public ArrayList<FileInfo> getIssueFileList() {
        if (issueFileList == null) {
            issueFileList = new ArrayList<>();
        }
        return issueFileList;
    }

    public void setIssueFileList(ArrayList<FileInfo> issueFileList) {
        this.issueFileList = issueFileList;
    }

    public String getAllComments() {
        StringBuilder str = new StringBuilder();
        ArrayList<IssueComments> comments = getIssueCommentList();
        for (IssueComments comment : comments) {
            str.append(comment.getCreatedBy()).append(":\n\n")
                .append(comment.getComments()).append(":\n\n");
        }
        return str.toString();
    }

    public SupportBean getBean() {
        SupportBean bean = new SupportBean();
        bean.setIssuePk(getId() + "");
        bean.setProjectPk(getProjectFk() + "");
        bean.setShipPk(getShipFk() + "");
        bean.setTitle(getTitle());
        bean.setDescription(getDescription());
        bean.setStatus(getStatus());
        bean.setPriority(getPriority());
        bean.setLaptopIssue(getLaptopIssue());
        bean.setScannerIssue(getScannerIssue());
        bean.setSoftwareIssue(getSoftwareIssue());
        bean.setPriorityReason(getPriorityReason());
        bean.setPhase(getPhase());
        bean.setOpenedBy(getOpenedBy());
        bean.setOpenedDate(getOpenedDateStr());
        bean.setClosedDate(getClosedDateStr());
        bean.setPersonAssigned(getPersonAssigned());
        bean.setCurrPersonAssigned(getPersonAssigned());
        bean.setSupportVisitDate(getSupportVisitDateStr());
        bean.setSupportVisitLoc(getSupportVisitLoc());
        bean.setSupportVisitTime(getSupportVisitTime() + "");
        bean.setTrainer(getTrainer());
        bean.setCurrTrainer(getTrainer());
        bean.setResolution(getResolution());
        bean.setTotalTime(getTotalTime() + "");
        bean.setInitiatedBy(getInitiatedBy());
        bean.setDept(getDept());
        bean.setIsEmailSent(getIsEmailSent());
        bean.setIsEmailResponded(getIsEmailResponded());
        bean.setIsTrainingProvided(getIsTrainingProvided());
        bean.setIsTrainingOnsite(getIsTrainingOnsite());
        bean.setAtoPk(getAtoFk() + ""); 
        bean.setAutoCloseDate(getAutoCloseDateStr());
        bean.setIssueCategoryFk(getIssueCategoryFk() + "");
        bean.setSupportVisitEndTime(getSupportVisitEndTime() + "");
        bean.setSupportVisitReason(getSupportVisitReason());
        bean.setSupportVisitLocNotes(getSupportVisitLocNotes());
        bean.setAutoCloseStatus(getAutoCloseStatus());
        bean.setConfiguredSystemFk(getConfiguredSystemFk());
        return bean;
    }
}
