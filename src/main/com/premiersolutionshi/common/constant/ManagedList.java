package com.premiersolutionshi.common.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import com.premiersolutionshi.common.domain.ManagedListItem;

public enum ManagedList {
    //                           name                          itemLabel        useSortOrder  useCurrentDefault  global hidden
    UNKNOWN                 ( 0, "", "", false, false, true, true)
    , SHIP_VISIT_LOCATIONS  ( 2, "Ship Visit Locations",       "Location",      false,         false,            true,  false)
    , LAPTOP_ISSUES         ( 3, "Laptop Issues",              "Issue",         false,         false,            false, false)
    , SCANNER_ISSUES        ( 4, "Scanner Issues",             "Issue",         false,         false,            false, false)
    , SOFTWARE_ISSUES       ( 5, "Software Issues",            "Category",      false,         false,            false, false)
    , SUPPORT_TEAM          ( 6, "Support Team",               "Team Member",   false,         true,             false, false)
    , UNIT_DIVISIONS        ( 7, "Unit Divisions",             "Division",      true,          true,             false, false)
    , FACET_VERSIONS        ( 8, "FACET Versions",             "FACET Version", true,          true,             false, false)
    , OS_VERSIONS           ( 9, "OS Versions",                "OS Version",    true,          true,             false, false)
    , SHIP_TYPES            (10, "Ship Types",                 "Ship Type",     false,         false,            true,  false)
    , STATE_PROVINCES       (11, "States/Provinces",           "State/Province",false,         true,             true,  false)
    ;
    private int code;
    private String name;
    private String itemLabel;
    private boolean useSortOrder;
    private boolean useCurrentDefault;
    private boolean global;
    private boolean hidden;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getItemLabel() {
        return itemLabel;
    }

    public boolean isUseSortOrder() {
        return useSortOrder;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isUseCurrentDefault() {
        return useCurrentDefault;
    }

    public boolean isGlobal() {
        return global;
    }

    private ManagedList(int code, String name, String itemLabel, boolean useSortOrder, boolean useCurrentDefault, boolean global, boolean hidden) {
        this.code = code;
        this.name = name;
        this.itemLabel = itemLabel;
        this.useSortOrder = useSortOrder;
        this.useCurrentDefault = useCurrentDefault;
        this.global = global;
        this.hidden = hidden;
    }

    public static ManagedList getByCode(int code) {
        ManagedList[] values = values();
        for (ManagedList managedList : values) {
            if (managedList.getCode() == code) {
                return managedList;
            }
        }
        return UNKNOWN;
    }

    public static ManagedList getByName(String name) {
        ManagedList[] values = values();
        for (ManagedList value : values) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return UNKNOWN;
    }

    public static ArrayList<ManagedList> getList() {
        ArrayList<ManagedList> list = new ArrayList<>(Arrays.asList(values()));
        return list;
    }

    public static ArrayList<ManagedList> getNotHidden() {
        ArrayList<ManagedList> list = getList();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ManagedList managedList = list.get(i);
            if (managedList.isHidden()) {
                list.remove(i--);
                size--;
            }
        }
        list.sort(Comparator.comparing(ManagedList::getName));
        return list;
    }

    public static ArrayList<ManagedList> getGlobalOnly() {
        ArrayList<ManagedList> list = getList();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ManagedList managedList = list.get(i);
            if (managedList.isHidden() || !managedList.isGlobal()) {
                list.remove(i--);
                size--;
            }
        }
        list.sort(Comparator.comparing(ManagedList::getName));
        return list;
    }

    public static ArrayList<ManagedList> getGlobal() {
        ArrayList<ManagedList> list = getList();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ManagedList managedList = list.get(i);
            if (managedList.isHidden() || !managedList.isGlobal()) {
                list.remove(i--);
                size--;
            }
        }
        list.sort(Comparator.comparing(ManagedList::getName));
        return list;
    }

    public static String getCurrentDefault(ArrayList<ManagedListItem> list) {
        for (ManagedListItem managedListItem : list) {
            if (managedListItem.isCurrentDefault()) {
                return managedListItem.getItemValue();
            }
        }
        return null;
    }
}
