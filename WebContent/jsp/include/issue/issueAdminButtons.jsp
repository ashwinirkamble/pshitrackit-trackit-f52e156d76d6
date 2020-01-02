<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tr>
  <td align="center" class="nofill">
    <logic:notEmpty name="issueForm" property="id">
      <tags:lastUpdatedBy by="${issueForm.lastUpdatedBy}" date="${issueForm.lastUpdatedDateStr}"/>
    </logic:notEmpty>
        
    <button type="submit" value="Submit" class="btn btn-primary">
      <span class="glyphicon glyphicon-ok"></span>
      Save
    </button>
    <a class="btn btn-default" href="support.do?action=${pageFrom}&projectPk=${projectPk}">
      <span class="glyphicon glyphicon-remove"></span>
      Cancel
    </a>
    <logic:notEmpty name="issueForm" property="id">
      <a class="btn btn-info" href="issue.do?action=copy&id=${issueForm.id}&projectPk=${projectPk}&pageFrom=${pageFrom}">
        <i class="material-icons">flip_to_front</i>
        Copy
      </a>
      <a class="btn btn-danger" href="javascript:confirmDeleteIssue(${issueForm.id});">
        <i class="material-icons">delete</i>
        Delete
      </a>
    </logic:notEmpty>
  </td>
</tr>