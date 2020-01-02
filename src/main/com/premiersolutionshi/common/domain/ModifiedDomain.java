package com.premiersolutionshi.common.domain;

import java.time.LocalDateTime;

import com.premiersolutionshi.common.util.DateUtils;
import com.premiersolutionshi.common.util.StringUtils;

/**
 * @author LewisNakao
 *
 */
public class ModifiedDomain extends BaseDomain {
    private static final long serialVersionUID = -6390287760879045494L;

    private Integer createdByFk;
    private String createdBy;
    private LocalDateTime createdDate;
    private Integer lastUpdatedByFk;
    private String lastUpdatedBy;
    private LocalDateTime lastUpdatedDate;

    public Integer getCreatedByFk() {
        return createdByFk;
    }

    public void setCreatedByFk(Integer createdByFk) {
        this.createdByFk = createdByFk;
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

    public void copy(ModifiedDomain domain) {
        if (domain == null) {
            return;
        }
        super.copy(domain);
        setCreatedByFk(domain.getCreatedByFk());
        setCreatedBy(domain.getCreatedBy());
        setCreatedDate(domain.getCreatedDate());
        setLastUpdatedByFk(domain.getLastUpdatedByFk());
        setLastUpdatedBy(domain.getLastUpdatedBy());
        setLastUpdatedDate(domain.getLastUpdatedDate());
    }
}
