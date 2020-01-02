package com.premiersoluitionshi.support.service;

import com.premiersoluitionshi.common.service.BaseServiceTest;
import com.premiersoluitionshi.common.util.TestData;
import com.premiersolutionshi.support.domain.AtoUpdate;
import com.premiersolutionshi.support.service.AtoUpdateService;

public class AtoUpdateServiceTest extends BaseServiceTest<AtoUpdate> {
    private AtoUpdateService atoUpdateService;

    public AtoUpdateServiceTest() {
        super(AtoUpdateServiceTest.class);
    }

    public static void main(String[] args) {
        AtoUpdateServiceTest test = new AtoUpdateServiceTest();
        test.runBaseTests();
    }

    @Override
    public AtoUpdate createInstance() {
        AtoUpdate atoUpdate = new AtoUpdate();
        applyRandomData(atoUpdate);
        return atoUpdate;
    }

    @Override
    public AtoUpdateService getService() {
        return atoUpdateService;
    }

    @Override
    protected void init() {
        atoUpdateService = new AtoUpdateService(getSqlSession(), getUserService());
    }

    @Override
    public boolean compare(AtoUpdate domain1, AtoUpdate domain2) {
        if (domain1 == null || domain2 == null) {
            logError("Cannot compare with null values.");
            return false;
        }
        testEquals(domain1.getId(), domain2.getId(), "PK");
        testEquals(domain1.getProjectFk(), domain2.getProjectFk(), "ProjectFk");
        testEquals(domain1.getAtoDate(), domain2.getAtoDate(), "AtoDate");
        testEquals(domain1.getComments(), domain2.getComments(), "Comments");
        testEquals(domain1.getOpenedDate(), domain2.getOpenedDate(), "OpenedDate");
        testEquals(domain1.getLastUpdatedBy(), domain2.getLastUpdatedBy(), "LastUpdatedBy");
        testEquals(domain1.getLastUpdatedDate(), domain2.getLastUpdatedDate(), "LastUpdatedDate");
        return true;
    }

    @Override
    public void applyRandomData(AtoUpdate domain) {
        domain.setProjectFk(TestData.genRandInt());
        domain.setAtoDate(TestData.genRandDate());
        domain.setComments(TestData.genRandString());
        domain.setOpenedDate(TestData.genRandDate());
        domain.setLastUpdatedBy(TestData.genRandString());
        domain.setLastUpdatedDate(TestData.genRandDateTime());
    }
}
