package com.premiersolutionshi.common.dao;

import com.premiersolutionshi.common.domain.TestBlog;

public interface TestBlogDao extends BaseDao<TestBlog> {

    TestBlog getByTitle(String title);
}
