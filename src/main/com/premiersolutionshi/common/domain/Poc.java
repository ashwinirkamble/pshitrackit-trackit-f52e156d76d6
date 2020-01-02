package com.premiersolutionshi.common.domain;

import com.premiersolutionshi.common.util.StringUtils;
import com.premiersolutionshi.support.domain.Ship;

public class Poc extends ModifiedDomain {
    private static final long serialVersionUID = 8853840653207497901L;
    private Integer organizationFk;
    private Integer shipFk;
    private String lastName;
    private String firstName;
    private String email;
    private String altEmail;
    private String workNumber;
    private Integer workNumberExt;
    private String cellNumber;
    private String faxNumber;
    private String title;
    private String rank;
    private String dept;
    private String notes;
    private boolean primaryPoc;

    private Organization organization;
    private Ship ship;

    public String getFullName() {
        if (StringUtils.isEmpty(firstName)) {
            return StringUtils.isEmpty(lastName) ? null : lastName;
        }
        return firstName + " " + lastName;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public Integer getOrganizationFk() {
        return organizationFk;
    }

    public void setOrganizationFk(Integer organizationFk) {
        this.organizationFk = organizationFk;
    }

    public Integer getShipFk() {
        return shipFk;
    }

    public void setShipFk(Integer shipFk) {
        this.shipFk = shipFk;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }

    public Integer getWorkNumberExt() {
        return workNumberExt;
    }

    public void setWorkNumberExt(Integer workNumberExt) {
        this.workNumberExt = workNumberExt;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getAltEmail() {
        return altEmail;
    }

    public void setAltEmail(String altEmail) {
        this.altEmail = altEmail;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isPrimaryPoc() {
        return primaryPoc;
    }

    public void setPrimaryPoc(boolean primaryPoc) {
        this.primaryPoc = primaryPoc;
    }
}
