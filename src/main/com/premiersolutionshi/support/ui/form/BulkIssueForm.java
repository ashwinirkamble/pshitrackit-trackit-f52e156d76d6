package com.premiersolutionshi.support.ui.form;

import java.time.LocalDate;

import com.premiersolutionshi.common.ui.form.BaseForm;
import com.premiersolutionshi.common.ui.form.BaseFormImpl;
import com.premiersolutionshi.common.util.DateUtils;
import com.premiersolutionshi.support.constant.BulkIssueCategory;
import com.premiersolutionshi.support.constant.IssueStatus;

public class BulkIssueForm extends BaseFormImpl implements BaseForm {
    private static final long serialVersionUID = -9021402280376091018L;

    private String title;
    private BulkIssueCategory category;
    private String personAssigned;
    private String openedBy;
    private LocalDate openedDate;
    private LocalDate closedDate;
    private IssueStatus status;
    private LocalDate autoCloseDate;
    private IssueStatus autoCloseToStatus;
    private String resolution;
    private String totalTime;
    private String comments = null;

    private String action;
    
    private String[] includeShipPkArr = null;
    private String[] includeConfiguredSystemPkArr = null;
    private String[] monthlyEmailMessage = null;
    private String[] pocEmails = null;
    private String[] primaryPocEmails = null;

    public String getOpenedDateStr() {
        return openedDate == null ? DateUtils.getNowInBasicFormat() : DateUtils.COMMON_BASIC_FORMAT.format(openedDate);
    }

    public void setOpenedDateStr(String atoDateStr) {
        setOpenedDate(DateUtils.parseBasicDate(atoDateStr));
    }

    public String getClosedDateStr() {
        return DateUtils.formatToBasicFormat(closedDate);
    }

    public void setClosedDateStr(String closedDate) {
        this.closedDate = DateUtils.parseBasicDate(closedDate);
    }

    public String getAutoCloseDateStr() {
        return DateUtils.formatToBasicFormat(autoCloseDate);
    }

    public void setAutoCloseDateStr(String autoCloseDate) {
        this.autoCloseDate = DateUtils.parseBasicDate(autoCloseDate);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BulkIssueCategory getCategory() {
        return category;
    }

    public void setCategory(BulkIssueCategory category) {
        this.category = category;
    }

    public String getCategoryName() {
        return category == null ? BulkIssueCategory.UNKNOWN.getName() : category.getName();
    }

    public void setCategoryName(String category) {
        this.category = BulkIssueCategory.getByName(category);
    }

    public String getPersonAssigned() {
        return personAssigned;
    }

    public void setPersonAssigned(String personAssigned) {
        this.personAssigned = personAssigned;
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

    public String getStatus() {
        return status == null ? null : status.getName();
    }

    public void setStatus(String statusStr) {
        this.status = IssueStatus.getByName(statusStr);
    }

    public LocalDate getAutoCloseDate() {
        return autoCloseDate;
    }

    public void setAutoCloseDate(LocalDate autoCloseDate) {
        this.autoCloseDate = autoCloseDate;
    }

    public String getAutoCloseToStatus() {
        return autoCloseToStatus == null ? null : autoCloseToStatus.getName();
    }

    public void setAutoCloseToStatus(String autoCloseToStatusStr) {
        this.autoCloseToStatus = IssueStatus.getByName(autoCloseToStatusStr);
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String[] getIncludeConfiguredSystemPkArr() {
        return includeConfiguredSystemPkArr;
    }

    public void setIncludeConfiguredSystemPkArr(String[] includeConfiguredSystemPkArr) {
        this.includeConfiguredSystemPkArr = includeConfiguredSystemPkArr;
    }

    public String[] getIncludeShipPkArr() {
        return includeShipPkArr;
    }

    public void setIncludeShipPkArr(String[] includeShipPkArr) {
        this.includeShipPkArr = includeShipPkArr;
    }

    public String[] getMonthlyEmailMessage() {
        return monthlyEmailMessage;
    }

    public void setMonthlyEmailMessage(String[] monthlyEmailMessage) {
        this.monthlyEmailMessage = monthlyEmailMessage;
    }

    public String[] getPocEmails() {
        return pocEmails;
    }

    public void setPocEmails(String[] pocEmails) {
        this.pocEmails = pocEmails;
    }

    public String[] getPrimaryPocEmails() {
        return primaryPocEmails;
    }

    public void setPrimaryPocEmails(String[] primaryPocEmails) {
        this.primaryPocEmails = primaryPocEmails;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
