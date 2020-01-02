package com.premiersolutionshi.support.constant;

import java.util.HashMap;
import java.util.Map;

public enum BulkIssueCategory {
    UNKNOWN             (0, "N/A")
    , MONTHLY_EMAIL     (1, "Monthly E-Mail Notification")
    , FOLLOWUP_TRAINING (2, "Follow-Up Training")
    , OS_UPDATE         (3, "OS Update")
    , FACET_UPDATE      (4, "FACET Update")
    ;
    private int code;
    private String name;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private BulkIssueCategory(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static BulkIssueCategory getByCode(int code) {
        BulkIssueCategory[] values = values();
        if (code >= 0 && code < values.length) {
            return values[code];
        }
        return UNKNOWN;
    }

    public static BulkIssueCategory getByName(String name) {
        BulkIssueCategory[] values = values();
        Map<String, BulkIssueCategory> map = new HashMap<>();
        for (BulkIssueCategory bulkIssueCategory : values) {
            map.put(bulkIssueCategory.getName(), bulkIssueCategory);
        }
        return map.get(name);
    }
}
