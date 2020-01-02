package com.premiersolutionshi.common.domain;

public class Project extends ModifiedDomain {
    private static final long serialVersionUID = -6281141628220497483L;
    private String projectName;
    private String description;
    private String customer;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
}
