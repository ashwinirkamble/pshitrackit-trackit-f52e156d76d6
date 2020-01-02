package com.premiersolutionshi.support.service;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import com.premiersolutionshi.common.service.ModifiedService;
import com.premiersolutionshi.common.service.UserService;
import com.premiersolutionshi.support.dao.IssueCategoryDao;
import com.premiersolutionshi.support.domain.IssueCategory;

@Component("issueCategoryService")
public class IssueCategoryService extends ModifiedService<IssueCategory> {
    public IssueCategoryService(SqlSession sqlSession, UserService userService) {
        super(sqlSession, IssueCategoryDao.class, userService);
    }

    public IssueCategory getByName(String categoryName) {
        try {
            return getDao().getByName(categoryName);
        }
        catch (Exception e) {
            logError("Could not getByName categoryName='" + categoryName + "'.", e);
        }
        return null;
    }

    public ArrayList<IssueCategory> getByProjectFk(Integer projectFk) {
        if (projectFk != null) {
            try {
                return getDao().getByProjectFk(projectFk);
            }
            catch (Exception e) {
                logError("Could not getByProjectFk projectFk=" + projectFk, e);
            }
        }
        else {
            logError("getByProjectFk | Parameter projectFk IS NULL");
        }
        return new ArrayList<>();
    }

    @Override
    protected IssueCategoryDao getDao() {
        return (IssueCategoryDao) super.getDao();
    }
}