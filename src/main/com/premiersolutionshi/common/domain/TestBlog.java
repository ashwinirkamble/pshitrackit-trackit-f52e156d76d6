package com.premiersolutionshi.common.domain;

public class TestBlog extends ModifiedDomain {
    private static final long serialVersionUID = -1700514413980689083L;

    private String title;
    private String body;
    private User lastUpdatedUser;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getLastUpdatedUser() {
        return lastUpdatedUser;
    }
}
