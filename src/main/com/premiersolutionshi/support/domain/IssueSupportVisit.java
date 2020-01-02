package com.premiersolutionshi.support.domain;

import java.time.LocalDate;

import com.premiersolutionshi.common.util.DateUtils;
import com.premiersolutionshi.common.util.StringUtils;

/**
 * This is used to display the "last support visit."
 */
public class IssueSupportVisit extends IssueStatusDomain {
    private static final long serialVersionUID = -1950977081086215735L;

    private String personAssigned;
    private LocalDate supportVisitDate;
    private String supportVisitLoc;
    private Integer supportVisitTime;
    private Integer supportVisitEndTime;
    private String supportVisitReason;
    private String supportVisitLocNotes;
    private Integer issueCategoryFk;

    private IssueCategory issueCategory;

    /**
     * "category" column deprecated -- use issue_category_fk instead
     * @return 
     */
    public String getCategory() {
        return issueCategory == null ? "N/A" : issueCategory.getCategory();
    }

    public Integer getIssueCategoryFk() {
        return issueCategoryFk;
    }

    public void setIssueCategoryFk(Integer issueCategoryFk) {
        this.issueCategoryFk = issueCategoryFk;
    }

    public IssueCategory getIssueCategory() {
        return issueCategory;
    }

    public void setIssueCategory(IssueCategory issueCategory) {
        this.issueCategory = issueCategory;
    }

    public String getPersonAssigned() {
        return personAssigned;
    }

    public void setPersonAssigned(String personAssigned) {
        this.personAssigned = personAssigned;
    }

    public String getSupportVisitDateSql() {
        return DateUtils.formatToSqliteDate(supportVisitDate);
    }

    public String getSupportVisitDateStr() {
        return DateUtils.formatToBasicFormat(supportVisitDate);
    }

    public void setSupportVisitDateStr(String supportVisitDate) {
        this.supportVisitDate = DateUtils.parseBasicDate(supportVisitDate);
    }

    public LocalDate getSupportVisitDate() {
        return supportVisitDate;
    }

    public void setSupportVisitDate(LocalDate supportVisitDate) {
        this.supportVisitDate = supportVisitDate;
    }

    public String getSupportVisitLoc() {
        return supportVisitLoc;
    }

    public void setSupportVisitLoc(String supportVisitLoc) {
        this.supportVisitLoc = supportVisitLoc;
    }

    public Integer getSupportVisitTime() {
        return supportVisitTime;
    }

    public String getSupportVisitTimeStr() {
        return StringUtils.convertToTime(getSupportVisitTime());
    }

    public void setSupportVisitTime(Integer supportVisitTime) {
        this.supportVisitTime = supportVisitTime;
    }

    public Integer getSupportVisitEndTime() {
        return supportVisitEndTime;
    }

    public String getSupportVisitEndTimeStr() {
        return StringUtils.convertToTime(getSupportVisitEndTime());
    }

    public void setSupportVisitEndTime(Integer supportVisitEndTime) {
        this.supportVisitEndTime = supportVisitEndTime;
    }

    public String getSupportVisitReason() {
        return supportVisitReason;
    }

    public void setSupportVisitReason(String supportVisitReason) {
        this.supportVisitReason = supportVisitReason;
    }

    public String getSupportVisitLocNotes() {
        return supportVisitLocNotes;
    }

    public void setSupportVisitLocNotes(String supportVisitLocNotes) {
        this.supportVisitLocNotes = supportVisitLocNotes;
    }
}
