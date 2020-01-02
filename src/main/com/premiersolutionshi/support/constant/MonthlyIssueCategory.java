package com.premiersolutionshi.support.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum MonthlyIssueCategory {
    FACET("FACET Update"),
    OS("OS Update"),
    RSUPPLY("RSupply Upgrade"),
    ATO("ATO Maintenance Release"),
    DMS("DMS Release"),
    INACTIVITY("DACS Inactivity"),
    MISSING_TRANSMITTALS("DACS Missing Transmittals");

    private String name;

    private MonthlyIssueCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<MonthlyIssueCategory> getAll() {
        return Arrays.asList(values());
    }

    public static List<String> getAllNames() {
        List<MonthlyIssueCategory> all = getAll();
        List<String> namesList = new ArrayList<>();
        for (MonthlyIssueCategory category : all) {
            namesList.add(category.getName());
        }
        return namesList;
    }
}
