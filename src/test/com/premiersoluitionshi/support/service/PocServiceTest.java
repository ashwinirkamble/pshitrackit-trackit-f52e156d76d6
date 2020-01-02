package com.premiersoluitionshi.support.service;

import java.util.ArrayList;

import com.premiersoluitionshi.common.service.BaseServiceTest;
import com.premiersoluitionshi.common.util.TestData;
import com.premiersolutionshi.common.domain.Poc;
import com.premiersolutionshi.common.util.JsonUtils;
import com.premiersolutionshi.support.service.PocService;

public class PocServiceTest extends BaseServiceTest<Poc> {
    public PocServiceTest() {
        super(PocServiceTest.class);
    }

    private PocService pocService;

    public static void main(String[] args) {
        PocServiceTest test = new PocServiceTest();
        test.runBaseTests();
        test.testJson();
    }

    public void testJson() {
        ArrayList<Poc> all = getService().getAll();
        if (all != null && !all.isEmpty()) {
            System.out.println(JsonUtils.toJson(all.get(0)));
        }
    }

    @Override
    public Poc createInstance() {
        Poc poc = new Poc();
        applyRandomData(poc);
        return poc;
    }

    @Override
    public PocService getService() {
        return pocService;
    }

    @Override
    protected void init() {
        pocService = new PocService(getSqlSession(), getUserService());
    }

    @Override
    public boolean compare(Poc domain1, Poc domain2) {
        if (domain1 == null || domain2 == null) {
            System.err.println("Cannot compare with null values.");
            System.exit(0);
        }
        testEquals(domain1.getId(), domain2.getId(), "PK");
        testEquals(domain1.getProjectFk(), domain2.getProjectFk(), "ProjectFk");
        testEquals(domain1.getOrganizationFk(), domain2.getOrganizationFk(), "OrganizationFk");
        testEquals(domain1.getShipFk(), domain2.getShipFk(), "ShipFk");
        testEquals(domain1.isPrimaryPoc(), domain2.isPrimaryPoc(), "PrimaryPoc");
        testEquals(domain1.getLastName(), domain2.getLastName(), "LastName");
        testEquals(domain1.getFirstName(), domain2.getFirstName(), "FirstName");
        testEquals(domain1.getTitle(), domain2.getTitle(), "Title");
        testEquals(domain1.getRank(), domain2.getRank(), "Rank");
        testEquals(domain1.getEmail(), domain2.getEmail(), "Email");
        testEquals(domain1.getWorkNumber(), domain2.getWorkNumber(), "WorkNumber");
        testEquals(domain1.getWorkNumberExt(), domain2.getWorkNumberExt(), "WorkNumberExt");
        testEquals(domain1.getFaxNumber(), domain2.getFaxNumber(), "FaxNumber");
        testEquals(domain1.getCellNumber(), domain2.getCellNumber(), "CellNumber");
        testEquals(domain1.getAltEmail(), domain2.getAltEmail(), "AltEmail");
        testEquals(domain1.getDept(), domain2.getDept(), "Dept");
        testEquals(domain1.getNotes(), domain2.getNotes(), "Notes");
        testEquals(domain1.getLastUpdatedBy(), domain2.getLastUpdatedBy(), "LastUpdatedBy");
        testEquals(domain1.getLastUpdatedDate(), domain2.getLastUpdatedDate(), "LastUpdatedDate");
      return true;
    }

    @Override
    public void applyRandomData(Poc domain) {
        domain.setProjectFk(TestData.genRandInt());
        domain.setOrganizationFk(TestData.genRandInt());
        domain.setShipFk(TestData.genRandInt());
        domain.setPrimaryPoc(TestData.genRandBoolean());
        domain.setLastName(TestData.genRandString());
        domain.setFirstName(TestData.genRandString());
        domain.setTitle(TestData.genRandString());
        domain.setRank(TestData.genRandString());
        domain.setEmail(TestData.genRandString());
        domain.setWorkNumber(TestData.genRandString());
        domain.setWorkNumberExt(TestData.genRandInt());
        domain.setFaxNumber(TestData.genRandString());
        domain.setCellNumber(TestData.genRandString());
        domain.setAltEmail(TestData.genRandString());
        domain.setDept(TestData.genRandString());
        domain.setNotes(TestData.genRandText());
        domain.setLastUpdatedBy(TestData.genRandString());
        domain.setLastUpdatedDate(TestData.genRandDateTime());
    }
}

