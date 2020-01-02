package com.premiersolutionshi.support.domain;

import java.time.LocalDate;

import com.premiersolutionshi.common.domain.ModifiedDomain;
import com.premiersolutionshi.common.util.DateUtils;

public class Laptop extends ModifiedDomain {
    private static final long serialVersionUID = -1505431527054540551L;
    private String productName;
    private String computerName;
    private String tag;
    private String modelNumber;
    private String serialNumber;
    private String origin;
    private LocalDate receivedDate;
    private String notes;
    private LocalDate preppedDate;
    private String macAddress;
    private String status;
    private String statusNotes;
    private String customer;
    private String contractNumber;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getReceivedDateSql() {
        return DateUtils.formatToSqliteDate(receivedDate);
    }

    public void setReceivedDateSql(String receivedDateSql) {
        this.receivedDate = DateUtils.parseSqliteDate(receivedDateSql);
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getPreppedDate() {
        return preppedDate;
    }

    public void setPreppedDate(LocalDate preppedDate) {
        this.preppedDate = preppedDate;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusNotes() {
        return statusNotes;
    }

    public void setStatusNotes(String statusNotes) {
        this.statusNotes = statusNotes;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }
}
