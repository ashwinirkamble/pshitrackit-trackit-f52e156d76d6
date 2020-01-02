package com.premiersolutionshi.support.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum IssuePhase {
    UNKNOWN                        (0, "Not specified", true)
    , ADDITIONAL_SYSTEM_REQUESTS   (1, "Additional System Requests", false)
    , BACKFILE                     (2, "Backfile", false)
    , CUSTOMER_TRAINING            (3, "Customer Training", false)
    , DECOM_PREP                   (4, "Decom Prep", false)
    , EXTERNAL                     (5, "External", false)
    , INTERNAL_TRAINING            (6, "Internal Training", false)
    , ONGOING_SCANNING             (8, "Ongoing Scanning", false)
    , PATCHES_UPDATES              (9, "Patches/Updates", false)
    , RETURN_TO_SHIP               (10, "Return to ship", false)
    , SHIPS_INSTALLATION           (11, "Ships Installation", false)
    , SHIPS_SUPPORT                (12, "Ships Support", false)
    , SHIPS_UPGRADE                (13, "Ships Upgrade", false)
    ;
    private int code;
    private String name;
    private boolean hidden;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public boolean isHidden() {
        return hidden;
    }

    private IssuePhase(int code, String name, boolean hidden) {
        this.code = code;
        this.name = name;
        this.hidden = hidden;
    }

    public static IssuePhase getByCode(int code) {
        IssuePhase[] values = values();
        if (code >= 0 && code < values.length) {
            return values[code];
        }
        return UNKNOWN;
    }

    public static IssuePhase getByName(String name) {
        IssuePhase[] values = values();
        Map<String, IssuePhase> map = new HashMap<>();
        for (IssuePhase unitDivision : values) {
            map.put(unitDivision.getName(), unitDivision);
        }
        return map.get(name);
    }

    public static ArrayList<IssuePhase> getList() {
        return new ArrayList<>(Arrays.asList(values()));
    }

    public static ArrayList<IssuePhase> getListNotHidden() {
        ArrayList<IssuePhase> list = new ArrayList<>(Arrays.asList(values()));
        int size = list.size();
        for (int i = 0; i < size; i++) {
            IssuePhase issuePhase = list.get(i);
            if (issuePhase.isHidden()) {
                list.remove(i--);
                size--;
            }
        }
        return list;
    }
}
