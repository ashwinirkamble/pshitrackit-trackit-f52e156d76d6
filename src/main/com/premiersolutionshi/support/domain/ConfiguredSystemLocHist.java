package com.premiersolutionshi.support.domain;

import java.time.LocalDate;

import com.premiersolutionshi.common.domain.ModifiedDomain;
import com.premiersolutionshi.common.util.DateUtils;

public class ConfiguredSystemLocHist extends ModifiedDomain {
    private static final long serialVersionUID = 6757927795755229109L;

    private int configuredSystemFk;
    private String location;
    private LocalDate locationDate;
    private String reason;

    public String getLocationDateSql() {
        return DateUtils.formatToSqliteDate(locationDate);
    }

    public int getConfiguredSystemFk() {
        return configuredSystemFk;
    }

    public void setConfiguredSystemFk(int configuredSystemFk) {
        this.configuredSystemFk = configuredSystemFk;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getLocationDate() {
        return locationDate;
    }

    public void setLocationDate(LocalDate locationDate) {
        this.locationDate = locationDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
