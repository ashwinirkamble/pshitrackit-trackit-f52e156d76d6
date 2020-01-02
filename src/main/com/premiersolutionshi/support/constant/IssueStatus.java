package com.premiersolutionshi.support.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum IssueStatus {
    UNKNOWN              (0, "0 - Unknown", "8c8c8c")
    , NEW                (1, "1 - New", "007000")
    , ACTIVE             (2, "2 - Active", "248F8F")
    , RESOLVED           (3, "3 - Resolved", "8c8c8c")
    , PENDING            (4, "4 - Pending Possible Resolution", "800080")
    , CLOSED             (5, "5 - Closed", "8c8c8c")
    , CLOSED_SUCCESSFUL  (6, "6 - Closed (Successful)", "8c8c8c")
    , CLOSED_NORESPONSE  (7, "7 - Closed (No Response)", "8c8c8c")
    , CLOSED_UNAVAILABLE (8, "8 - Closed (Unavailable)", "8c8c8c")
    ;

    private int code;
    private String name;
    private String color;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    private IssueStatus(int code, String name, String color) {
        this.code = code;
        this.name = name;
        this.color = color;
    }

    public boolean isClosed() {
        return code > 4;
    }

    public static IssueStatus getByCode(int code) {
        IssueStatus[] values = values();
        if (code >= 0 && code < values.length) {
            return values[code];
        }
        return NEW;
    }

    public static ArrayList<IssueStatus> getList() {
        return new ArrayList<>(Arrays.asList(values()));
    }

    public static IssueStatus getByName(String statusStr) {
        IssueStatus[] values = values();
        for (IssueStatus issueStatus : values) {
            if (issueStatus.getName().equals(statusStr)) {
                return issueStatus;
            }
        }
        return IssueStatus.UNKNOWN;
    }

    public List<Integer> getClosedStatusCodes() {
        List<Integer> statusCodeList = new ArrayList<>();
        statusCodeList.add(CLOSED.getCode());
        statusCodeList.add(CLOSED_NORESPONSE.getCode());
        statusCodeList.add(CLOSED_SUCCESSFUL.getCode());
        statusCodeList.add(CLOSED_UNAVAILABLE.getCode());
        return statusCodeList;
    }
}
