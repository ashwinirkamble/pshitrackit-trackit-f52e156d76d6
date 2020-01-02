package com.premiersolutionshi.old.util;

public class SqlColumn {

    private int index;
    private String name;
    private ColumnType type;

    public SqlColumn(int index, String name, ColumnType type) {
        super();
        this.index = index;
        this.name = name;
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ColumnType getType() {
        return type;
    }

    public void setType(ColumnType type) {
        this.type = type;
    }
}
