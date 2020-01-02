package com.premiersoluitionshi.common.service;

import java.util.ArrayList;

import com.premiersolutionshi.common.domain.ModifiedDomain;
import com.premiersolutionshi.common.service.BulkDomainService;

public abstract class BulkDomainServiceTest<T extends ModifiedDomain> extends BaseServiceTest<T> {

    public BulkDomainServiceTest(Class<?> clazz) {
        super(clazz);
    }

    public abstract BulkDomainService<T> getService();

    public void runBulkDomainTests() {
        logInfo("==============================================================");
        logInfo("= Run Bulk Domain Tests                                      =");
        logInfo("==============================================================");
        ArrayList<T> list = new ArrayList<>();
        for (int i = 0; i < 700; i++) {
            list.add(createInstance());
        }
        int rowsInserted = getService().batchInsert(list);
        if (rowsInserted == list.size()) {
            logInfo("SUCCESS: inserted list.size()=" + list.size());
        }
        else {
            logError("FAILED : list.size()=" + list.size() + ", rows inserted=" + rowsInserted);
        }
    }
}
