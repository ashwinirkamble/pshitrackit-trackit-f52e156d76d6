package com.premiersolutionshi.support.domain;

import com.premiersolutionshi.common.domain.ModifiedDomain;
import com.premiersolutionshi.support.constant.IssueStatus;

public class IssueStatusDomain extends ModifiedDomain {
    private static final long serialVersionUID = -4713944812124969755L;

    private IssueStatus issueStatus;

    public int getStatusCode() {
        return getIssueStatus().getCode();
    }

    public void setStatusCode(int statusCode) {
        this.issueStatus = IssueStatus.getByCode(statusCode);
    }

    public IssueStatus getIssueStatus() {
        return issueStatus == null ? IssueStatus.NEW : issueStatus;
    }

    public void setIssueStatus(IssueStatus issueStatus) {
        this.issueStatus = issueStatus;
    }

    public String getStatusCss() {
        return getIssueStatus().getColor();
    }

    public String getStatus() {
        return issueStatus == null ? IssueStatus.NEW.getName() : issueStatus.getName();
    }

    public void setStatus(String statusStr) {
        this.issueStatus = IssueStatus.getByName(statusStr);
    }

    public boolean isClosed() {
        if (issueStatus != null && issueStatus.isClosed()) {
            return true;
        }
        return false;
    }
}
