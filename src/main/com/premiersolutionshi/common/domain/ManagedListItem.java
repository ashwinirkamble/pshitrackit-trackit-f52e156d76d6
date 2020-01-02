package com.premiersolutionshi.common.domain;

import com.premiersolutionshi.common.constant.ManagedList;

public class ManagedListItem extends SortableDomain {
    private static final long serialVersionUID = -7253871605294390137L;
    private ManagedList managedList;
    private String itemValue;
    private boolean currentDefault;
    private boolean hidden;

    public ManagedListItem() {
    }

    public ManagedListItem(String itemValue) {
        this.itemValue = itemValue;
    }

    public int getManagedListCode() {
        return managedList == null ? ManagedList.UNKNOWN.getCode() : managedList.getCode();
    }

    public void setManagedListCode(int code) {
        this.managedList = ManagedList.getByCode(code);
    }

    public ManagedList getManagedList() {
        return managedList;
    }

    public void setManagedList(ManagedList managedList) {
        this.managedList = managedList;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public boolean isCurrentDefault() {
        return currentDefault;
    }

    public void setCurrentDefault(boolean currentDefault) {
        this.currentDefault = currentDefault;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
