package com.premiersoluitionshi.support.dao;

import com.premiersolutionshi.common.dao.BaseDao;
import com.premiersolutionshi.common.domain.TestBlog;

public interface TestBlogDao extends BaseDao<TestBlog> {

    TestBlog getByTitle(String title);
}
