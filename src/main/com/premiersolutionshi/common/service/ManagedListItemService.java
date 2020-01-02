package com.premiersolutionshi.common.service;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;

import com.premiersolutionshi.common.constant.ManagedList;
import com.premiersolutionshi.common.dao.ManagedListItemDao;
import com.premiersolutionshi.common.domain.ManagedListItem;
import com.premiersolutionshi.common.util.DomainUtil;
import com.premiersolutionshi.common.util.StringUtils;

public class ManagedListItemService extends ModifiedService<ManagedListItem> {
    public ManagedListItemService(SqlSession sqlSession, UserService userService) {
        super(sqlSession, ManagedListItemDao.class, userService);
    }

    @Override
    protected ManagedListItemDao getDao() {
        return (ManagedListItemDao) super.getDao();
    }

    @Override
    protected void beforeSave(ManagedListItem domain) {
        domain.setItemValue(StringUtils.removeXss(domain.getItemValue()));
        ManagedList managedList = domain.getManagedList();
        if (managedList.isGlobal()) {
            domain.setProjectFk(null);
        }
        if (!managedList.isUseSortOrder()) {
            domain.setSortOrder(0);
        }
        super.beforeSave(domain);
    }

    /**
     * This can only be used by Global Managed lists.
     * @param managedList
     * @return List of Items.
     */
    public ArrayList<ManagedListItem> getByList(ManagedList managedList) {
        if (managedList.isGlobal()) {
            return getByListAndProjectFk(managedList, null);
        }
        return new ArrayList<>();
    }

    public ArrayList<String> getStringListByListAndProjectFk(ManagedList managedList, Integer projectFk) {
        ArrayList<ManagedListItem> list = getByListAndProjectFk(managedList, projectFk);
        ArrayList<String> stringList = new ArrayList<>();
        for (ManagedListItem item : list) {
            stringList.add(item.getItemValue());
        }
        return stringList;
    }
    public ArrayList<ManagedListItem> getByListAndProjectFk(ManagedList managedList, Integer projectFk) {
        if (managedList != null) {
            boolean global = managedList.isGlobal();
            if (global || projectFk != null) {
                return getByListCodeAndProjectFk(managedList.getCode(), global ? null : projectFk);
            }
        }
        logError("getByListAndProjectFk | Mananged List and/or ProjectFk were not provided or was not Global. "
                + "managedList=" + managedList + ", projectFk=" + projectFk);
        return new ArrayList<>();
    }

    public ArrayList<ManagedListItem> getByListCodeAndProjectFk(int managedListCode, Integer projectFk) {
        try {
            return getDao().getByListCodeAndProjectFk(managedListCode, projectFk);
        }
        catch (Exception e) {
            logError("Could not getByListCodeAndProjectFk. managedListCode=" + managedListCode
                + ", managedList=" + ManagedList.getByCode(managedListCode)
                + ", projectFk=" + projectFk, e);
        }
        return new ArrayList<>();
    }

    public boolean handeSorting(String[] pkArr) {
        if (pkArr == null || pkArr.length <= 0) {
            logError("handeSorting | No pk array input provided. ");
            return false;
        }
        Integer itemId = StringUtils.parseInt(pkArr[0]);
        if (itemId != null) {
            ManagedListItem item = getById(itemId);
            ManagedList managedList = item.getManagedList();
            ArrayList<ManagedListItem> list = getByListAndProjectFk(managedList, item.getProjectFk());
            int sortOrder = 0;
            for (String pkStr : pkArr) {
                Integer pk = StringUtils.parseInt(pkStr);
                ManagedListItem pkItem = DomainUtil.findOnListById(list, pk);
                if (pkItem != null) {
                    if (pkItem.getSortOrder() != sortOrder) {
                        pkItem.setSortOrder(sortOrder);
                        save(pkItem);
                    }
                    sortOrder++;
                }
            }
        }
        return true;
    }

    public String getCurrentDefault(ManagedList managedList, Integer projectFk) {
        if (managedList == null) {
            logError("getCurrentDefault | Managed List is null.");
            return null;
        }
        if (projectFk == null || projectFk <= 0) {
            logError("getCurrentDefault | Project PK is null or not valid.");
            return null;
        }
        if (!managedList.isUseCurrentDefault()) {
            logError("getCurrentDefault | The Managed List does not use Current/Default.");
            return null;
        }
        int managedListCode = managedList.getCode();
        try {
            String currentDefault = getDao().getCurrentDefault(managedListCode, projectFk);
            if (!StringUtils.isEmpty(currentDefault)) {
                return currentDefault;
            }
            logError("getCurrentDefault | A Current Default was not found.");
        }
        catch (Exception e) {
            logError("Could not getCurrentDefault. managedListCode=" + managedListCode
                + ", managedList=" + ManagedList.getByCode(managedListCode)
                + ", projectFk=" + projectFk, e);
        }
        return null;
    }
}
