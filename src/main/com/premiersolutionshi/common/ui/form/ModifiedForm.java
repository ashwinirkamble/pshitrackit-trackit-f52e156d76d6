package com.premiersolutionshi.common.ui.form;

import java.time.LocalDateTime;

import com.premiersolutionshi.common.util.DateUtils;

public class ModifiedForm extends BaseFormImpl {
    private static final long serialVersionUID = -3495791264158867067L;
    private Integer createdById;
    private String createdBy;
    private LocalDateTime createdDate;
    private Integer lastUpdatedById;
    private String lastUpdatedBy;
    private LocalDateTime lastUpdatedDate;

    public Integer getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Integer createdById) {
        this.createdById = createdById;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedDateSql() {
        return DateUtils.formatToSqliteDatetime(createdDate);
    }

    public Integer getLastUpdatedById() {
        return lastUpdatedById;
    }

    public void setLastUpdatedById(Integer lastUpdatedById) {
        this.lastUpdatedById = lastUpdatedById;
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
