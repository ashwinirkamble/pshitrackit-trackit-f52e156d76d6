package com.premiersolutionshi.old.bean;


import com.premiersolutionshi.common.util.StringUtils;
import com.premiersolutionshi.support.constant.IssueStatus;

public class BaseStatusBean extends BaseBean {
    private static final long serialVersionUID = 6678450875368235285L;
    private IssueStatus issueStatus = null;

    public IssueStatus getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(IssueStatus issueStatus) {
        this.issueStatus = issueStatus;
    }

    public String getStatus() {
        return issueStatus == null ? null : issueStatus.getName();
    }

    public void setStatus(String status) {
        if (!StringUtils.isEmpty(status)) {
            this.issueStatus = IssueStatus.getByName(status);
        }
    }

    public String getStatusCss() {
        String hexColor = issueStatus == null ? IssueStatus.NEW.getColor() : issueStatus.getColor();
        return "color: #" + hexColor;
    }
}
