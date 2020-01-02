package com.premiersolutionshi.old.util;

public enum ColumnType {

    INTEGER("INTEGER")
    , VARCHAR("VARCHAR")
    , TEXT("TEXT")
    , DATETIME("DATETIME")
    , DATE("DATE")
    ;

    private String name;

    private ColumnType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
