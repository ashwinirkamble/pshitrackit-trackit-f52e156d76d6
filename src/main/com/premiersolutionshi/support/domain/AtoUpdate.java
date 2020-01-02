package com.premiersolutionshi.support.domain;

import java.time.LocalDate;

import com.premiersolutionshi.common.domain.ModifiedDomain;
import com.premiersolutionshi.common.util.DateUtils;

public class AtoUpdate extends ModifiedDomain {
    private static final long serialVersionUID = -5030884975014440879L;

    private Integer projectFk;
    private LocalDate atoDate;
    private LocalDate openedDate;
    private String comments;
    private String label;
    private int totalIssues;
    private int totalIssuesClosed; //applied_cnt (in the old code)

    public String getLabel() {
        if (label == null) {
            label = "ATOUpdates_" + DateUtils.formatToPattern(getAtoDate(), "yyyyMMdd");
        }
        return label;
    }

    public Integer getProjectFk() {
        return projectFk;
    }

    public void setProjectFk(Integer projectFk) {
        this.projectFk = projectFk;
    }

    public LocalDate getAtoDate() {
    	
    	/* Changes start by Ashwini  */
    	atoDate=LocalDate.now();
    	
    	/*Change end*/
        return atoDate;
    }

    public void setAtoDate(LocalDate atoDate) {
        this.atoDate = atoDate;
    }

    public LocalDate getOpenedDate() {
        return openedDate;
    }

    public String getOpenedDateSql() {
        return DateUtils.formatToSqliteDate(openedDate);
    }

    public String getAtoDateSql() {
        return DateUtils.formatToSqliteDate(atoDate);
    }

    public void setOpenedDate(LocalDate openedDate) {
        this.openedDate = openedDate;
    }

    public String getAtoDateStr() {
        return atoDate == null ? DateUtils.getNowInBasicFormat() : DateUtils.COMMON_BASIC_FORMAT.format(atoDate);
    }

    public void setAtoDateStr(String atoDateStr) {
        setAtoDate(DateUtils.parseBasicDate(atoDateStr));
    }

    public String getOpenedDateStr() {
        return openedDate == null ? DateUtils.getNowInBasicFormat() : DateUtils.COMMON_BASIC_FORMAT.format(openedDate);
    }

    public void setOpenedDateStr(String openedDateStr) {
        setOpenedDate(DateUtils.parseBasicDate(openedDateStr));
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getTotalIssues() {
        return totalIssues;
    }

    public void setTotalIssues(int totalIssues) {
        this.totalIssues = totalIssues;
    }

    public int getTotalIssuesClosed() {
        return totalIssuesClosed;
    }

    public void setTotalIssuesClosed(int totalIssuesClosed) {
        this.totalIssuesClosed = totalIssuesClosed;
    }
}