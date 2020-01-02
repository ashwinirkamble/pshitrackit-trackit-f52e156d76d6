package com.premiersolutionshi.common.constant;

public enum PocType {
    UNKNOWN(0, "Unknown")
    , ORGANIZATION(1, "Organization")
    , SHIP(2, "Ship")
    ;

    private int code;
    private String name;

    private PocType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static PocType getByCode(int code) {
        PocType[] values = values();
        if (code >= 0 && code < values.length) {
            return values[code];
        }
        return ORGANIZATION;
    }
}
