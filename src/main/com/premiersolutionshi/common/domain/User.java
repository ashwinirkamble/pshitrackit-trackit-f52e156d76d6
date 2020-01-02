package com.premiersolutionshi.common.domain;

public class User extends BasePoc {
    private static final long serialVersionUID = 7670440826444045733L;
    private String username;
    private String password;
    private String organization;
    private String title;
    private String quickDial;
    private String cellNumber;
    private String enabled;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuickDial() {
        return quickDial;
    }

    public void setQuickDial(String quickDial) {
        this.quickDial = quickDial;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }
}