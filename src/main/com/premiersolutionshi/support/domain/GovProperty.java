package com.premiersolutionshi.support.domain;

import java.time.LocalDate;

import com.premiersolutionshi.common.domain.ModifiedDomain;
import com.premiersolutionshi.common.util.DateUtils;
import com.premiersolutionshi.common.util.StringUtils;

public class GovProperty extends ModifiedDomain {
    private static final long serialVersionUID = 134164191138253469L;
    private LocalDate dateListed;
    private String nationalStockNumber;
    private String description;
    private String projectContract;
    private Integer received;
    private Integer issued;
    private Integer transferred;
    private Integer onHand;
    private String location;
    private String disposition;

    public String getDateListedSql() {
        return DateUtils.formatToSqliteDate(dateListed);
    }

    public String getDateListedStr() {
        return dateListed == null ? DateUtils.getNowInBasicFormat() : DateUtils.COMMON_BASIC_FORMAT.format(dateListed);
    }

    public void setDateListedStr(String dateListedStr) {
        if (StringUtils.isEmpty(dateListedStr)) {
            return;
        }
        setDateListed(DateUtils.parseBasicDate(dateListedStr));
    }

    public LocalDate getDateListed() {
        return dateListed;
    }

    public void setDateListed(LocalDate dateListed) {
        this.dateListed = dateListed;
    }

    public String getNationalStockNumber() {
        return nationalStockNumber;
    }

    public void setNationalStockNumber(String nationalStockNumber) {
        this.nationalStockNumber = nationalStockNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProjectContract() {
        return projectContract;
    }

    public void setProjectContract(String projectContract) {
        this.projectContract = projectContract;
    }

    public Integer getReceived() {
        return received;
    }

    public void setReceived(Integer received) {
        this.received = received;
    }

    public Integer getIssued() {
        return issued;
    }

    public void setIssued(Integer issued) {
        this.issued = issued;
    }

    public Integer getTransferred() {
        return transferred;
    }

    public void setTransferred(Integer transferred) {
        this.transferred = transferred;
    }

    public Integer getOnHand() {
        return onHand;
    }

    public void setOnHand(Integer onHand) {
        this.onHand = onHand;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }
}
