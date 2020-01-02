package com.premiersoluitionshi.common.service;

import com.premiersoluitionshi.common.util.TestData;
import com.premiersolutionshi.common.domain.Organization;
import com.premiersolutionshi.common.service.OrganizationService;

public class OrganizationServiceTest extends BaseServiceTest<Organization> {
    public OrganizationServiceTest() {
        super(OrganizationServiceTest.class);
    }

    private OrganizationService organizationService;

    public static void main(String[] args) {
        OrganizationServiceTest test = new OrganizationServiceTest();
        test.runBaseTests();
    }

    @Override
    public Organization createInstance() {
        Organization organization = new Organization();
        applyRandomData(organization);
        return organization;
    }

    @Override
    public OrganizationService getService() {
        return organizationService;
    }

    @Override
    protected void init() {
        organizationService = new OrganizationService(getSqlSession(), getUserService());
    }

    @Override
    public boolean compare(Organization domain1, Organization domain2) {
        if (domain1 == null || domain2 == null) {
            System.err.println("Cannot compare with null values.");
            System.exit(0);
        }
        testEquals(domain1.getId(), domain2.getId(), "PK");
        testEquals(domain1.getName(), domain2.getName(), "Name");
        testEquals(domain1.getAddress1(), domain2.getAddress1(), "Address1");
        testEquals(domain1.getAddress2(), domain2.getAddress2(), "Address2");
        testEquals(domain1.getZip(), domain2.getZip(), "Zip");
        testEquals(domain1.getStateProvince(), domain2.getStateProvince(), "StateProvince");
        testEquals(domain1.getCountry(), domain2.getCountry(), "Country");
        testEquals(domain1.getEmail(), domain2.getEmail(), "Email");
        testEquals(domain1.getUrl(), domain2.getUrl(), "Url");
        testEquals(domain1.getPhone(), domain2.getPhone(), "Phone");
        testEquals(domain1.getPrimaryPocFk(), domain2.getPrimaryPocFk(), "PrimaryPocFk");
        //testEquals(domain1.getCreatedByFk(), domain2.getCreatedByFk(), "CreatedByFk");
        testEquals(domain1.getLastUpdatedByFk(), domain2.getLastUpdatedByFk(), "LastUpdatedByFk");
      return true;
    }

    @Override
    public void applyRandomData(Organization domain) {
        domain.setName(TestData.genRandString());
        domain.setAddress1(TestData.genRandString());
        domain.setAddress2(TestData.genRandString());
        domain.setZip(TestData.genRandString());
        domain.setStateProvince(TestData.genRandString());
        domain.setCountry(TestData.genRandString());
        domain.setEmail(TestData.genRandString());
        domain.setUrl(TestData.genRandString());
        domain.setPhone(TestData.genRandString());
        domain.setPrimaryPocFk(TestData.genRandInt());
        domain.setCreatedByFk(TestData.genRandInt());
        domain.setCreatedDate(TestData.genRandDateTime());
        domain.setLastUpdatedByFk(TestData.genRandInt());
        domain.setLastUpdatedDate(TestData.genRandDateTime());
    }
}

