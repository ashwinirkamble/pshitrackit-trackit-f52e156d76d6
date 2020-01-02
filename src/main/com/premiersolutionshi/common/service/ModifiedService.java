package com.premiersolutionshi.common.service;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;

import com.premiersolutionshi.common.domain.ModifiedDomain;
import com.premiersolutionshi.common.domain.User;

public class ModifiedService<T extends ModifiedDomain> extends BaseService<T> {
    private HttpServletRequest httpServletRequest;
    private UserService userService;
    private User currentUser;

    public ModifiedService(Class<?> daoClazz) {
        super(daoClazz);
    }

    public ModifiedService(SqlSession sqlSession, Class<?> daoClazz, UserService userService) {
        super(sqlSession, daoClazz);
        this.userService = userService;
    }

    @Override
    protected void beforeInsert(T domain) {
        super.beforeInsert(domain);
        if (userService != null) {
            User currentUser = getCurrentUser();
            if (currentUser != null) {
                domain.setCreatedByFk(currentUser.getId());
                domain.setCreatedBy(currentUser.getFullName());
            }
        }
        domain.setCreatedDate(LocalDateTime.now());
    }

    @Override
    protected void beforeSave(T domain) {
        super.beforeSave(domain);
        if (userService != null) {
            User currentUser = getCurrentUser();
            if (currentUser != null) {
                domain.setLastUpdatedByFk(currentUser.getId());
                domain.setLastUpdatedBy(currentUser.getFullName());
            }
        }
        domain.setLastUpdatedDate(LocalDateTime.now());
    }

    protected User getCurrentUser() {
        if (currentUser == null && userService != null) {
            currentUser = userService.getCurrentUser();
        }
        return currentUser;
    }

    protected UserService getUserService() {
        return userService;
    }

    protected void setUserService(UserService userService) {
        this.userService = userService;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }
}
