package com.premiersoluitionshi.support.service;

import com.premiersoluitionshi.common.service.BaseServiceTest;
import com.premiersoluitionshi.common.util.TestData;
import com.premiersolutionshi.common.constant.ManagedList;
import com.premiersolutionshi.common.domain.ManagedListItem;
import com.premiersolutionshi.common.service.ManagedListItemService;

public class ManagedListItemServiceTest extends BaseServiceTest<ManagedListItem> {
    public ManagedListItemServiceTest() {
        super(ManagedListItemServiceTest.class);
    }

    private ManagedListItemService managedListItemService;

    public static void main(String[] args) {
        ManagedListItemServiceTest test = new ManagedListItemServiceTest();
        test.runBaseTests();
        System.out.println("" + test.getService().getByList(ManagedList.SHIP_TYPES));
    }

    @Override
    public ManagedListItem createInstance() {
        ManagedListItem managedListItem = new ManagedListItem();
        applyRandomData(managedListItem);
        return managedListItem;
    }

    @Override
    public ManagedListItemService getService() {
        return managedListItemService;
    }

    @Override
    protected void init() {
        managedListItemService = new ManagedListItemService(getSqlSession(), getUserService());
    }

    @Override
    public boolean compare(ManagedListItem domain1, ManagedListItem domain2) {
        if (domain1 == null || domain2 == null) {
            System.err.println("Cannot compare with null values.");
            System.exit(0);
        }
        testEquals(domain1.getId(), domain2.getId(), "PK");
        testEquals(domain1.getManagedListCode(), domain2.getManagedListCode(), "ManagedListCode");
        testEquals(domain1.getItemValue(), domain2.getItemValue(), "ItemValue");
        testEquals(domain1.isCurrentDefault(), domain2.isCurrentDefault(), "CurrentDefault");
        testEquals(domain1.isHidden(), domain2.isHidden(), "Hidden");
        testEquals(domain1.getSortOrder(), domain2.getSortOrder(), "SortOrder");
        testEquals(domain1.getProjectFk(), domain2.getProjectFk(), "ProjectFk");
        testEquals(domain1.getLastUpdatedByFk(), domain2.getLastUpdatedByFk(), "LastUpdatedByFk");
        testEquals(domain1.getLastUpdatedDate(), domain2.getLastUpdatedDate(), "LastUpdatedDate");
        return true;
    }

    @Override
    public void applyRandomData(ManagedListItem domain) {
        domain.setManagedListCode(TestData.genRandInt());
        domain.setItemValue(TestData.genRandString());
        domain.setCurrentDefault(TestData.genRandBoolean());
        domain.setHidden(TestData.genRandBoolean());
        domain.setSortOrder(TestData.genRandInt());
        domain.setProjectFk(TestData.genRandInt());
        domain.setLastUpdatedByFk(TestData.genRandInt());
        domain.setLastUpdatedDate(TestData.genRandDateTime());
    }
}

