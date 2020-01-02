package com.premiersolutionshi.common.service;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import com.premiersolutionshi.common.dao.ProjectDao;
import com.premiersolutionshi.common.domain.Customer;
import com.premiersolutionshi.common.domain.Project;
import com.premiersolutionshi.common.domain.User;

@Component("projectService")
public class ProjectService extends ModifiedService<Project> {
    public ProjectService(SqlSession sqlSession, UserService userService) {
        super(sqlSession, ProjectDao.class, userService);
    }

    @Override
    protected ProjectDao getDao() {
        return (ProjectDao) super.getDao();
    }

    public ArrayList<Customer> getCustomers() {
        try {
            return getDao().getCustomers();
        }
        catch (Exception e) {
            getLogger().error("Could not getCustomers.");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public ArrayList<Project> getByUser(User user) {
        Integer userFk = user == null ? null : user.getId();
        if (userFk != null && userFk > 0) {
            try {
                return getDao().getByUserFk(userFk);
            }
            catch(Exception e) {
                logError("Could not getByUserFk. userFk=" + userFk, e);
            }
        }
        return new ArrayList<>();
    }
}