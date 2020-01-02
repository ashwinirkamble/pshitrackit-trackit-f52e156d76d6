package com.premiersolutionshi.common.ui.action;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;

import com.premiersolutionshi.common.util.StringUtils;

public abstract class BasePagedAction extends BaseAction {
    private static final int DEFAULT_PAGE_SIZE = 20;

    private int currentPage = 1;
    private int pageSize = DEFAULT_PAGE_SIZE;
    private int totalCount = 1;

    public BasePagedAction(Class<?> clazz) {
        super(clazz);
    }


    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) totalCount / pageSize);
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * Set the totalCount and pageSize if desired.
     */
    protected abstract void setupPaging();

    @Override
    protected String run(Connection conn, HttpServletRequest request, HttpServletResponse response, ActionForm form) throws IOException {
        Integer page = StringUtils.parseInt(request.getParameter("page"));
        if (page != null) {
            setCurrentPage(page);
        }
        setupPaging();
        request.setAttribute("currentPage", getCurrentPage());
        request.setAttribute("totalPages", getTotalPages());
        request.setAttribute("pageSize", getPageSize());
        return "index";
    }

}
