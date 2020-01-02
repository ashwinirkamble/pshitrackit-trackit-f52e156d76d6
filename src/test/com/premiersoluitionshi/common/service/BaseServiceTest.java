package com.premiersoluitionshi.common.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.premiersolutionshi.common.domain.BaseDomain;
import com.premiersolutionshi.common.domain.Domain;
import com.premiersolutionshi.common.service.BaseService;
import com.premiersolutionshi.common.service.UserService;
import com.premiersolutionshi.common.util.DateUtils;
import com.premiersolutionshi.common.util.DomainUtil;
import com.premiersolutionshi.common.util.SqlUtils;

public abstract class BaseServiceTest<T extends BaseDomain> {
    private UserService userService;
    private Logger logger;

    public BaseServiceTest(Class<?> clazz) {
        initializeServices();
        //Logger.getRootLogger().setLevel(Level.DEBUG);
        Logger.getRootLogger().setLevel(Level.INFO);
        Layout layout = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %p %c | %m%n");
        Logger.getRootLogger().addAppender(new ConsoleAppender(layout));
        logger = Logger.getLogger(clazz.getSimpleName());
    }

    protected Logger getLogger() {
        return logger;
    }

    protected abstract void init();

    public abstract BaseService<T> getService();

    public abstract T createInstance();

    public abstract void applyRandomData(T domain);

    public abstract boolean compare(T domain1, T domain2);

    protected SqlSession getSqlSession() {
        return SqlUtils.getMybatisSession();
    }

    private void compareTest(T domain1, T domain2) {
        if (domain1 == null || domain2 == null) {
            logInfo("Cannot compare when one of the two domains are null.");
            return;
        }
        compare(domain1, domain2);
        logInfo("compareTest successful!");
    }

    protected void initializeServices() {
        SqlSession sqlSession = getSqlSession();
        userService = new UserService(sqlSession);
        init();
    }

    public void runBaseTests() {
        logInfo("==============================================================");
        logInfo("= Run Base Tests                                             =");
        logInfo("==============================================================");
        T domain = createInstance();
        test(getService().save(domain), "save domain", domain);

        T domainInserted = getService().getById(domain.getId());
        test(domainInserted != null, "find inserted domain by 'getById'", domain);
        compareTest(domain, domainInserted);
        logInfo("SUCCESS: domain inserted came back as it was.");

        applyRandomData(domain);
        getService().save(domain);
        T domainUpdatedDb = getService().getById(domain.getId());
        if (domainUpdatedDb != null) {
            compareTest(domain, domainUpdatedDb);
        }
        logInfo("SUCCESS: domain updated came back as it was.");

        ArrayList<T> all = getService().getAll();
        T domainDb = DomainUtil.findOnListById(all, domain.getId());
        test(domainDb != null, "find inserted domain in 'getAll'", domain);
        if (domainDb != null) {
            compareTest(domain, domainDb);
        }
        logInfo("SUCCESS: domain inserted came back as it was in 'getAll' result.");

        getSqlSession().rollback(true);
        logInfo("BASE TESTS COMPLETE");
    }

    private boolean checkIfNull(Object var1, Object var2, String fieldName) {
        if (var1 == null) {
            System.err.println("Argument 1 is null. Cannot compare " + fieldName + ".");
            System.exit(0);//on failure, exit.
            return true;
        }
        else if (var2 == null) {
            System.err.println("Argument 2 is null. Cannot compare " + fieldName + ".");
            System.exit(0);//on failure, exit.
            return true;
        }
        return false;
    }
    
    protected boolean testEquals(boolean var1, boolean var2, String fieldName) {
        return test(var1 == var2, "comparing " + fieldName + ": var1=" + var1 + ", var2=" + var2);
    }

    protected boolean testEquals(Integer var1, Integer var2, String fieldName) {
        if (checkIfNull(var1, var2, fieldName)) {
            return false;
        }
        return test(var1.equals(var2), "comparing " + fieldName + ": var1=" + var1 + ", var2=" + var2);
    }
    
    protected boolean testEquals(String var1, String var2, String fieldName) {
        if (checkIfNull(var1, var2, fieldName)) {
            return false;
        }
        return test(var1.equals(var2), "comparing " + fieldName + ": var1=" + var1 + ", var2=" + var2);
    }

    protected boolean testEquals(LocalDate var1, LocalDate var2, String fieldName) {
        if (checkIfNull(var1, var2, fieldName)) {
            return false;
        }
        return test(DateUtils.equals(var1, var2), "comparing " + fieldName + ": var1=" + var1 + ", var2=" + var2);
    }

    protected boolean testEquals(LocalDateTime var1, LocalDateTime var2, String fieldName) {
        if (checkIfNull(var1, var2, fieldName)) {
            return false;
        }
        return test(DateUtils.equalsToMinute(var1, var2), "comparing " + fieldName + ": var1=" + var1 + ", var2=" + var2);
    }

    protected boolean test(boolean success, String action) {
        return test(success, action, null);
    }

    protected boolean test(boolean success, String action, Domain domain) {
        String message = "[" + action + "]";
        if (!success) {
            if (domain != null) {
                message += " domain=" + domain;
            }
            logError("FAILED : " + message);
            System.exit(0);//on failure, exit.
            return false;
        }
        return true;
    }

    protected void logInfo(String message) {
        getLogger().info(message);
    }

    protected void logError(String message) {
        getLogger().error(message, null);
    }

    protected void logError(String message, Exception e) {
        if (e == null) {
            getLogger().error(message);
        }
        else {
            getLogger().error(message, e);
        }
    }

    public UserService getUserService() {
        return userService;
    }
}
