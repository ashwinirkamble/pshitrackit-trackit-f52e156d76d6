package com.premiersolutionshi.old.bean;

import java.time.LocalDateTime;

import com.premiersolutionshi.common.util.DateUtils;
import com.premiersolutionshi.common.util.StringUtils;

public class LastUpdatedBean extends BaseBean {
    private static final long serialVersionUID = -8100125343709882885L;
    private String createdBy;
    private LocalDateTime createdDate;
    private Integer lastUpdatedByFk;
    private String lastUpdatedBy;
    private LocalDateTime lastUpdatedDate;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public String getCreatedDateStr() {
        return DateUtils.formatToLongFormat(createdDate);
    }

    public void setCreatedDateStr(String createdDateStr) {
        if (!StringUtils.isEmpty(createdDateStr)) {
            this.createdDate = DateUtils.parseLongDate(createdDateStr);
        }
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedDateSql() {
        return DateUtils.formatToSqliteDatetime(createdDate);
    }

    public Integer getLastUpdatedByFk() {
        return lastUpdatedByFk;
    }

    public void setLastUpdatedByFk(Integer lastUpdatedByFk) {
        this.lastUpdatedByFk = lastUpdatedByFk;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getLastUpdatedDateStr() {
        return lastUpdatedDate == null ? "" : DateUtils.COMMON_LONG_FORMAT.format(lastUpdatedDate);
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        if (lastUpdatedDate != null) {
            this.lastUpdatedDate = lastUpdatedDate;
        }
    }

    public String getLastUpdatedDateSql() {
        return DateUtils.formatToSqliteDatetime(lastUpdatedDate);
    }
}
