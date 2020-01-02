package com.premiersolutionshi.common.domain;

import java.util.ArrayList;

public class Customer extends BaseDomain {
    private static final long serialVersionUID = -9090922799453774231L;

    private String customerName;
    private ArrayList<Project> projects;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }
}
