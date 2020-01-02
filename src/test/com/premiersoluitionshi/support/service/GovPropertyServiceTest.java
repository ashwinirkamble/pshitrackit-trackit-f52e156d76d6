package com.premiersoluitionshi.support.service;

import com.premiersoluitionshi.common.service.BaseServiceTest;
import com.premiersoluitionshi.common.util.TestData;
import com.premiersolutionshi.support.domain.GovProperty;
import com.premiersolutionshi.support.service.GovPropertyService;

public class GovPropertyServiceTest extends BaseServiceTest<GovProperty> {
    public GovPropertyServiceTest() {
        super(GovPropertyServiceTest.class);
    }

    private GovPropertyService govPropertyService;

    public static void main(String[] args) {
        GovPropertyServiceTest test = new GovPropertyServiceTest();
        test.runBaseTests();
    }

    @Override
    public GovProperty createInstance() {
        GovProperty govProperty = new GovProperty();
        applyRandomData(govProperty);
        return govProperty;
    }

    @Override
    public GovPropertyService getService() {
        return govPropertyService;
    }

    @Override
    protected void init() {
        govPropertyService = new GovPropertyService(getSqlSession(), getUserService());
    }

    @Override
    public boolean compare(GovProperty domain1, GovProperty domain2) {
        if (domain1 == null || domain2 == null) {
            System.err.println("Cannot compare with null values.");
            System.exit(0);
        }
        testEquals(domain1.getId(), domain2.getId(), "PK");
        testEquals(domain1.getDateListed(), domain2.getDateListed(), "DateListed");
        testEquals(domain1.getNationalStockNumber(), domain2.getNationalStockNumber(), "NationalStockNumber");
        testEquals(domain1.getDescription(), domain2.getDescription(), "Description");
        testEquals(domain1.getProjectContract(), domain2.getProjectContract(), "ProjectContract");
        testEquals(domain1.getReceived(), domain2.getReceived(), "Received");
        testEquals(domain1.getIssued(), domain2.getIssued(), "Issued");
        testEquals(domain1.getTransferred(), domain2.getTransferred(), "Transferred");
        testEquals(domain1.getOnHand(), domain2.getOnHand(), "OnHand");
        testEquals(domain1.getLocation(), domain2.getLocation(), "Location");
        testEquals(domain1.getDisposition(), domain2.getDisposition(), "Disposition");
        //testEquals(domain1.getProjectFk(), domain2.getProjectFk(), "ProjectFk");
        //testEquals(domain1.getCreatedByFk(), domain2.getCreatedByFk(), "CreatedByFk");
        //testEquals(domain1.getCreatedDate(), domain2.getCreatedDate(), "CreatedDate");
        testEquals(domain1.getLastUpdatedByFk(), domain2.getLastUpdatedByFk(), "LastUpdatedByFk");
        //testEquals(domain1.getLastUpdatedDate(), domain2.getLastUpdatedDate(), "LastUpdatedDate");
        return true;
    }

    @Override
    public void applyRandomData(GovProperty domain) {
        domain.setDateListed(TestData.genRandDate());
        domain.setNationalStockNumber(TestData.genRandString());
        domain.setDescription(TestData.genRandString());
        domain.setProjectContract(TestData.genRandString());
        domain.setReceived(TestData.genRandInt());
        domain.setIssued(TestData.genRandInt());
        domain.setTransferred(TestData.genRandInt());
        domain.setOnHand(TestData.genRandInt());
        domain.setLocation(TestData.genRandString());
        domain.setDisposition(TestData.genRandString());
        domain.setCreatedByFk(TestData.genRandInt());
        domain.setCreatedDate(TestData.genRandDateTime());
        domain.setLastUpdatedByFk(TestData.genRandInt());
        domain.setLastUpdatedDate(TestData.genRandDateTime());
    }
}
