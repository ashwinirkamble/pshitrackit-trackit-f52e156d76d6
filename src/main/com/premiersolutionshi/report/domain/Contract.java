package com.premiersolutionshi.report.domain;

import java.time.LocalDate;

import com.premiersolutionshi.common.domain.ModifiedDomain;
import com.premiersolutionshi.common.domain.Organization;
import com.premiersolutionshi.common.util.DateUtils;
import com.premiersolutionshi.common.util.StringUtils;

public class Contract extends ModifiedDomain {
    private static final long serialVersionUID = -6989615430443395092L;

    private String contractNumber;
    private String name;
    private String notes;
    private Integer organizationFk;
    private LocalDate startDate;
    private LocalDate endDate;

    private Organization organization;

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getStartDateSql() {
        return DateUtils.formatToSqliteDate(startDate);
    }

    public String getStartDateStr() {
        return startDate == null ? null : DateUtils.COMMON_BASIC_FORMAT.format(startDate);
    }

    public void setStartDateStr(String startDateStr) {
        if (StringUtils.isEmpty(startDateStr)) {
            return;
        }
        setStartDate(DateUtils.parseBasicDate(startDateStr));
    }
    public String getEndDateSql() {
        return DateUtils.formatToSqliteDate(endDate);
    }

    public String getEndDateStr() {
        return endDate == null ? null : DateUtils.COMMON_BASIC_FORMAT.format(endDate);
    }

    public void setEndDateStr(String endDateStr) {
        if (StringUtils.isEmpty(endDateStr)) {
            return;
        }
        setEndDate(DateUtils.parseBasicDate(endDateStr));
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getOrganizationFk() {
        return organizationFk;
    }

    public void setOrganizationFk(Integer organizationFk) {
        this.organizationFk = organizationFk;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
