package com.premiersolutionshi.report.constant;

import java.util.ArrayList;
import java.util.Arrays;

public enum StatusReportType {
    UNKNOWN(0, "Unknown", "", "")
    , BUSINESS_VOLUME(1, "Monthly Business Volume Report", "businessVolume", "Monthly")
    , PROJECT_STATUS(2, "Monthly Project Status Report", "projectStatus", "Monthly")
    ;

    private int code;
    private String name;
    private String forwardRef;
    private String recurrence;

    private StatusReportType(int code, String name, String forwardRef, String recurrence) {
        this.code = code;
        this.name = name; 
        this.forwardRef = forwardRef; 
        this.recurrence = recurrence; 
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getForwardRef() {
        return forwardRef;
    }

    public String getRef() {
        return forwardRef + recurrence;
    }

    public String getRecurrence() {
        return recurrence;
    }

    public static StatusReportType getByCode(int code) {
        StatusReportType[] values = values();
        if (code >= 0 && code < values.length) {
            return values[code];
        }
        return UNKNOWN;
    }

    public static ArrayList<StatusReportType> getList() {
        ArrayList<StatusReportType> list = new ArrayList<>(Arrays.asList(values()));
        int size = list.size();
        for (int i = 0; i < size; i++) {
            StatusReportType managedList = list.get(i);
            if (managedList.getCode() == 0) {
                list.remove(i--);
                size--;
            }
        }
        return list;
    }
}
