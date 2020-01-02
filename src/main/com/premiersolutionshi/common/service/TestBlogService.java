package com.premiersolutionshi.common.service;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import com.premiersolutionshi.common.dao.TestBlogDao;
import com.premiersolutionshi.common.domain.TestBlog;
import com.premiersolutionshi.common.util.StringUtils;

@Component("testBlogService")
public class TestBlogService extends BaseService<TestBlog> {
    public TestBlogService(SqlSession sqlSession) {
        super(sqlSession, TestBlogDao.class);
    }

    public TestBlog getByTitle(String title) {
        if (StringUtils.isEmpty(title)) {
            getLogger().error("There was no Title provided.");
        }
        try {
            return ((TestBlogDao) getDao()).getByTitle(title);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
