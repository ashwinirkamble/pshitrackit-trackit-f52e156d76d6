package com.premiersolutionshi.support.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import com.premiersolutionshi.common.domain.User;
import com.premiersolutionshi.common.service.BulkDomainService;
import com.premiersolutionshi.common.service.UserService;
import com.premiersolutionshi.common.util.StringUtils;
import com.premiersolutionshi.support.dao.IssueCommentsDao;
import com.premiersolutionshi.support.domain.Issue;
import com.premiersolutionshi.support.domain.IssueComments;

@Component("issueCommentsService")
public class IssueCommentsService extends BulkDomainService<IssueComments> {
    public IssueCommentsService() {
        super(IssueCommentsDao.class);
    }

    public IssueCommentsService(SqlSession sqlSession, UserService userService) {
        super(sqlSession, IssueCommentsDao.class, userService);
    }

    @Override
    protected int getInsertLimit() {
        return 500;
    }

    @Override
    protected void beforeSave(IssueComments domain) {
        super.beforeSave(domain);
        System.out.println("|||||||||||||||||||||| before domain.getComments()=" + domain.getComments());
        domain.setComments(StringUtils.escapeHtml(domain.getComments()));
        System.out.println("|||||||||||||||||||||| after domain.getComments()=" + domain.getComments());
    }

    @Override
    protected IssueCommentsDao getDao() {
        return (IssueCommentsDao) super.getDao();
    }

    public boolean deleteByIssueFk(Integer issueFk) {
        if (issueFk == null) {
            return false;
        }
        try {
            int rowsDeleted = getDao().deleteByIssueFk(issueFk);
            getLogger().info("Successfully deleteByIssueFk rowsDeleted=" + rowsDeleted);
            return true;
        }
        catch(Exception e) {
            getLogger().error("Could not deleteByIssueFk");
            e.printStackTrace();
        }
        return false;
    }

    public void bulkAddComment(ArrayList<Issue> issueList, String comments) {
        if (issueList == null || issueList.isEmpty() || StringUtils.isEmpty(comments)) {
            return;
        }
        List<Integer> issuePkList = new ArrayList<>();
        for (Issue issue : issueList) {
            Integer id = issue.getId();
            if (id != null) {
                issuePkList.add(id);
            }
        }
        bulkAddComment(issuePkList, comments);
    }

    public void bulkAddComment(String[] issuePkStrArr, String comments) {
        if (issuePkStrArr == null || issuePkStrArr.length <= 0 || StringUtils.isEmpty(comments)) {
            return;
        }
        List<Integer> issuePkList = new ArrayList<>();
        for (String issuePkStr : issuePkStrArr) {
            Integer issuePkInteger = StringUtils.parseInt(issuePkStr);
            if (issuePkInteger != null) {
                issuePkList.add(issuePkInteger);
            }
        }
        bulkAddComment(issuePkList, comments);
    }

    public void bulkAddComment(List<Integer> issuePkArr, String comments) {
        if (issuePkArr == null || issuePkArr.isEmpty() || StringUtils.isEmpty(comments)) {
            return;
        }
        comments = comments.trim();
        ArrayList<IssueComments> issueCommentList = new ArrayList<>();
        for (Integer issuePk : issuePkArr) {
            User currentUser = getUserService().getCurrentUser();
            String fullName = currentUser == null ? null : currentUser.getFullName();
            IssueComments issueComment = new IssueComments();
            issueComment.setIssueFk(issuePk);
            issueComment.setComments(comments);
            issueComment.setCreatedBy(fullName);
            issueCommentList.add(issueComment);
        }
        int rowsInserted = batchInsert(issueCommentList);
        if (rowsInserted > 0) {
            int length = comments.length();
            int endIndex = length < 50 ? length : 50;
            logInfo("Bulk added " + rowsInserted + " issue commments: '" + comments.substring(0, endIndex) + "'");
        }
    }
}
