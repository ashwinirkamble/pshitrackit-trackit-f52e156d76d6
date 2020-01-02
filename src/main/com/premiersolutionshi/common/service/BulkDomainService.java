package com.premiersolutionshi.common.service;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;

import com.premiersolutionshi.common.dao.BulkDao;
import com.premiersolutionshi.common.domain.ModifiedDomain;

public abstract class BulkDomainService<T extends ModifiedDomain> extends ModifiedService<T> {
    private static final Integer DEFAULT_INSERT_LIMIT = 200;

    public BulkDomainService(Class<?> daoClazz) {
        super(daoClazz);
    }

    public BulkDomainService(SqlSession sqlSession, Class<?> daoClazz, UserService userService) {
        super(sqlSession, daoClazz, userService);
    }

    @Override
    protected BulkDao<T> getDao() {
        return (BulkDao<T>) super.getDao();
    }

    protected int getInsertLimit() {
        return DEFAULT_INSERT_LIMIT;
    }

    public int batchInsert(ArrayList<T> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        int listSize = list.size();
        int insertLimit = getInsertLimit();
        int rowsInserted = 0;
        logInfo("Starting batchInsert process list.size()=" + listSize + ", insertLimit=" + insertLimit);
        beforeBatchInsert(list);
        if (listSize >= insertLimit) {
            int batchCount = (int) Math.ceil((double) listSize / (double) insertLimit);
            int fromIndex;
            int toIndex;
            for (int i = 0; i < batchCount; i++) {
                fromIndex = i * insertLimit;
                toIndex = ((i + 1) * insertLimit);
                if (i == batchCount - 1) {
                    toIndex = listSize;
                }
                //System.out.println("insertLimit=" + insertLimit + ", batchCount=" + batchCount + ", fromIndex=" + fromIndex + ", toIndex=" + toIndex);
                ArrayList<T> sublist = new ArrayList<>(list.subList(fromIndex, toIndex));
                int rows = insertBatch(sublist);
                if (rows != sublist.size()) {
                    logError("Did not insert sublist batch of " + sublist.size() + " domain objects, rows inserted: " + rows);
                }
                rowsInserted += rows;
            }
        }
        else {
            rowsInserted = insertBatch(list);
        }
        if (rowsInserted == listSize) {
            logInfo("Successfully batch inserted  " + listSize + " rows.");
        }
        else {
            logError("Did not batch insert expected rows: " + listSize + ", rows inserted: " + rowsInserted + " rows.");
        }
        return rowsInserted;
    }

    private void beforeBatchInsert(ArrayList<T> list) {
        for (T domain : list) {
            beforeInsert(domain);
        }
    }

    private int insertBatch(ArrayList<T> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        int listSize = list.size();
        String action = "insertBatch list.size()=" + listSize;
        try {
            int rowsInserted = getDao().insertAll(list);
            if (rowsInserted > 0) {
                logInfo("Successfully " + action + ", rowsInserted=" + rowsInserted);
            }
            return rowsInserted;
        }
        catch(Exception e) {
            logError("Could not" + action, e);
        }
        return 0;
    }
}
