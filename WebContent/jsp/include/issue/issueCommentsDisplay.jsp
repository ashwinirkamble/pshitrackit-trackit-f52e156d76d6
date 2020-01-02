<%@ page language="java" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ page import="com.premiersolutionshi.common.util.StringUtils" %>

<tr><th>Internal Comments</th></tr>
<tr><td align="left">
  <table id="commentsTable" class="border-zero cellspacing-zero alt-color" style="width:100%">
  <colgroup>
    <col width="620">
  </colgroup>
  <% if (request.getAttribute("action").equals("add") || request.getAttribute("action").equals("edit")) { %>
    <thead>
      <tr class="ignore"><td colspan="2" class="newRow">
        <a href="javascript:void(0);" class="addCommentBtn btn btn-info btn-sml">
          <i class="material-icons">add</i>
          Add New Comment
        </a>
      </td></tr>
    </thead>
  <% } %>
  <tbody>
    <logic:notEmpty name="issueForm" property="commentsArr">
      <logic:iterate id="comment" name="issueForm" property="commentsArr">
        <tr>
          <td class="TOP">
            <textarea name="commentsArr" rows="5" class="form-control input-sm">${comment}</textarea>
          </td>
          <td class="TOP">
            <input type="button" class="ibtnDel btn btn-danger" title="Delete this comment." value="&#215;">
          </td>
        </tr>
      </logic:iterate>
    </logic:notEmpty>
    <logic:notEmpty name="issueForm" property="issueCommentList">
      <logic:iterate id="issueComment" name="issueForm" property="issueCommentList" type="com.premiersolutionshi.support.domain.IssueComments">
        <tr class="nohover"><td colspan="2">
          <div style="float:left;"><b>${issueComment.createdBy}</b></div>
          <div style="float:right;"><i>${issueComment.createdDateStr}</i></div>
          <br clear="all"/>
          <div style="padding-left:15px;">
            <div id="issue-comment-${issueComment.id}"
              class="issue-comment${issueComment.tooLong ? ' issue-comment-shortened' : ''}">
              ${issueComment.comments.replaceAll('\\n','<br/>')}
            </div>
          </div>
          <div class="${issueComment.tooLong ? '' : 'hidden'}">
            <a href="#" class="issue-comment-more-btn" title="Show the rest of the comment."
              onclick="return toggleCommentExpansion(${issueComment.id}, this);">&#9660;</a>
          </div>
        </td></tr>
      </logic:iterate>
    </logic:notEmpty>
  </tbody>
  </table>
</td></tr>

