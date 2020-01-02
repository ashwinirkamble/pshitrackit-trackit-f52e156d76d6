package com.premiersolutionshi.common.service;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.premiersolutionshi.common.dao.BaseDao;
import com.premiersolutionshi.common.dao.UtilDao;
import com.premiersolutionshi.common.domain.BaseDomain;
import com.premiersolutionshi.common.util.SqlUtils;

public abstract class BaseService<T extends BaseDomain> {

    private BaseDao<T> dao;
    private SqlSession sqlSession;
    private UtilDao utilDao;
    private Logger logger;

    @SuppressWarnings("unchecked")
    public BaseService(SqlSession sqlSession, Class<?> daoClazz) {
        setLogger(Logger.getLogger(this.getClass().getSimpleName()));
        if (sqlSession != null) {
            this.sqlSession = sqlSession;
            this.dao = (BaseDao<T>) sqlSession.getMapper(daoClazz);
            this.utilDao = sqlSession.getMapper(UtilDao.class);
        }
        else {
            logError("SQL Sesion was not setup. Please check logs for errors.");
        }
    }

    @SuppressWarnings("unchecked")
    public BaseService(Class<?> daoClazz) {
        setLogger(Logger.getLogger(this.getClass().getSimpleName()));
        if (sqlSession != null) {
            this.sqlSession = SqlUtils.getMybatisSession();
            this.dao = (BaseDao<T>) sqlSession.getMapper(daoClazz);
            this.utilDao = sqlSession.getMapper(UtilDao.class);
        }
        else {
            logError("SQL Sesion was not setup. Please check logs for errors.");
        }
    }

    protected BaseDao<T> getDao() {
        return dao;
    }

    protected UtilDao getUtilDao() {
        return utilDao;
    }

    protected int getLastInsertPk() {
        return getUtilDao().getLastInsertPk();
    }

    protected void beforeInsert(T domain) {
        beforeSave(domain);
    }

    protected void beforeUpdate(T domain) {
        beforeSave(domain);
    }

    protected void beforeDelete(T domain) {
        //placeholder
    }

    protected void beforeSave(T domain) {
        //placeholder
    }

    protected void afterInsert(T domain) {
        domain.setId(getLastInsertPk());
    }

    public T getById(Integer id) {
        if (id == null) {
            logError("getById | There was no ID provided.");
        }
        try {
            return getDao().getById(id);
        }
        catch (Exception e) {
            logError("Could not getById. id=" + id, e);
        }
        return null;
    }

    public int getCount() {
        try {
            return getDao().getCount();
        }
        catch (Exception e) {
            logError("getCount | Could not getCount.", e);
        }
        return 0;
    }

    public ArrayList<T> getLimit(int startRow, int size) {
        try {
            return getDao().getLimit(startRow, size);
        }
        catch (Exception e) {
            logError("Could not getLimit startRow=" + startRow + ", size=" + size + ".", e);
        }
        return new ArrayList<T>();
    }

    public ArrayList<T> getAll() {
        try {
            return getDao().getAll();
        }
        catch (Exception e) {
            logError("Could not getAll.", e);
        }
        return new ArrayList<T>();
    }

    public boolean insert(T domain) {
        if (domain == null) {
            logError("A domain was not provided to insert.");
            return false;
        }
        beforeInsert(domain);
        try {
            boolean success = getDao().insert(domain) == 1;
            if (success) {
                logInfo("Successfully inserted domain=" + domain);
                afterInsert(domain);
            }
            else {
                logError("Failed to insert domain=" + domain);
            }
            afterWriteOperation(success);
            return success;
        }
        catch (Exception e) {
            logError("Could not insert. domain=" + domain, e);
        }
        return false;
    }

    public boolean update(T domain) {
        if (domain == null) {
            logError("A domain was not provided to update.");
            return false;
        }
        beforeUpdate(domain);
        try {
            boolean success = getDao().update(domain) == 1;
            if (success) {
                logInfo("Successfully updated domain=" + domain);
            }
            else {
                logError("Failed to updated domain=" + domain);
            }
            afterWriteOperation(success);
            return success;
        }
        catch (Exception e) {
            logError("Could not update. domain=" + domain, e);
        }
        return false;
    }

    protected void afterWriteOperation(boolean success) {
        if (success) {
            sqlSession.commit();
        }
    }

    public boolean save(T domain) {
        if (domain == null) {
            logError("A domain was not provided to save.");
            return false;
        }
        Integer id = domain.getId();
        if (id == null || id <= 0) {
            return insert(domain);
        }
        return update(domain);
    }
    
    public boolean delete(T domain) {
        if (domain == null) {
            logError("A domain was not provided to delete.");
            return false;
        }
        beforeDelete(domain);
        if (deleteById(domain.getId())) {
            logInfo("Successfully deleted: " + domain);
        }
        return true;
    }

    public boolean deleteById(Integer id) {
        if (id == null) {
            logError("There was no ID provided for deletion.");
        }
        T domain = getById(id);
        if (domain == null) {
            logError("A domain with the ID: " + id + " was not found on the database.");
            return false;
        }
        boolean success = getDao().deleteById(id) == 1;
        if (success) {
            logInfo("Successfully deleted ID: " + id);
        }
        else {
            logError("Failed to delete ID: " +id);
        }
        afterWriteOperation(success);
        return success;
    }

    protected void logInfo(String message) {
        if (logger != null) {
            logger.info(message);
        }
        else {
            System.out.println("LOGGER IS NOT SET: message=" + message);
        }
    }

    protected void logError(String message) {
        if (logger != null) {
            logError(message, null);
        }
    }

    protected void logError(String message, Exception e) {
        if (logger != null) {
            logger.error(message, e);
        }
        else {
            System.err.println("LOGGER IS NOT SET: message=" + message);
        }
        if (e != null) {
            e.printStackTrace();
        }
    }

    protected Logger getLogger() {
        return logger;
    }

    protected void setLogger(Logger logger) {
        this.logger = logger;
    }

    public SqlSession getSqlSession() {
        return sqlSession;
    }
}
