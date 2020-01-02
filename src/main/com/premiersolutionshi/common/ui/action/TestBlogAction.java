package com.premiersolutionshi.common.ui.action;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.springframework.beans.factory.annotation.Autowired;

import com.premiersolutionshi.common.domain.TestBlog;
import com.premiersolutionshi.common.service.TestBlogService;
import com.premiersolutionshi.common.service.TestService;

public class TestBlogAction extends BaseAdminAction<TestBlog> {
    private TestBlogService testBlogService;

    @Autowired
    private TestService testService;

    public TestBlogAction() {
        super(TestBlogAction.class);
    }

    @Override
    protected String run(Connection conn, HttpServletRequest request, HttpServletResponse response, ActionForm form) throws IOException {
        if (getAction().equals("viewByTitle")) {
            TestBlog testBlog = getService().getByTitle("test title");
            request.setAttribute("TestBlog_domain", testBlog);
        }
        System.out.println("||||||||||||||||||" + testService.getString());
        return super.run(conn, request, response, form);
    }

    @Override
    protected TestBlogService getService() {
        return testBlogService;
    }

    @Override
    protected TestBlog createNewDomain() {
        return new TestBlog();
    }

    @Override
    protected String path() {
        return "testBlog.do";
    }

    @Override
    protected String handleView(HttpServletRequest request, String action, Integer id) throws IOException {
       
        return null;
    }

}
