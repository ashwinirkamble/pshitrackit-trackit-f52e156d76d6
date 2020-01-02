package com.premiersolutionshi.common.service;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import com.premiersolutionshi.common.dao.UserDao;
import com.premiersolutionshi.common.domain.User;
import com.premiersolutionshi.common.util.StringUtils;

@Component("userService")
public class UserService extends BaseService<User> {
    private User currentUser;

    public UserService() {
        super(UserDao.class);
    }

    public UserService(SqlSession sqlSession) {
        super(sqlSession, UserDao.class);
    }

    public UserService(SqlSession sqlSession, HttpServletRequest request) {
        super(sqlSession, UserDao.class);
        setHttpServletRequest(request);
    }

    public User getByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            getLogger().error("There was no Username provided.");
            return null;
        }
        try {
            return getDao().getByUsername(username);
        }
        catch (Exception e) {
            getLogger().error("Unable to getByUsername.");
            e.printStackTrace();
        }
        return null;
    }

    public User getCurrentUser() {
        if (currentUser == null) {
            //System.err.println("Could not getCurrentUser. HttpServletRequest is not set.");
        }
        return currentUser;
    }

    public void setHttpServletRequest(HttpServletRequest request) {
        if (request == null) {
            return;
        }
        currentUser = getByUsername(request.getRemoteUser());
    }

    @Override
    protected UserDao getDao() {
        return (UserDao) super.getDao();
    }
}
