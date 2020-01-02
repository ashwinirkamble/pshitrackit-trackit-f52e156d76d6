package com.premiersolutionshi.common.constant;

public enum MessageType {

    NEUTRAL    (0, "secondary")
    , INFO     (1, "info")
    , WARNING  (2, "warning")
    , ERROR    (3, "danger")
    ;

    private int code;
    private String name;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private MessageType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static MessageType getByCode(int code) {
        MessageType[] values = values();
        if (code >= 0 && code < values.length) {
            return values[code];
        }
        return NEUTRAL;
    }
}
