<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="defaultPageTitle" value="Bulk Email Tool Preview"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="bulkIssueForm" scope="request" class="com.premiersolutionshi.support.ui.form.BulkIssueForm"/>

<bean:define id="defaultPageTitle" value="${pageTitle} Preview"/>
<%@ include file="../../layout/old/layout-header.jsp" %>

<html:form action="bulkIssue.do?projectPk=${projectPk}" method="POST">
  <input type="hidden" name="action" value="submit"/>
  <html:hidden name="bulkIssueForm" property="projectFk"/>
  <html:hidden name="bulkIssueForm" property="title" styleId="title"/>
  <html:hidden name="bulkIssueForm" property="personAssigned"/>
  <html:hidden name="bulkIssueForm" property="categoryName"/>
  <html:hidden name="bulkIssueForm" property="status"/>
  <html:hidden name="bulkIssueForm" property="openedBy"/>
  <html:hidden name="bulkIssueForm" property="openedDateStr"/>
  <html:hidden name="bulkIssueForm" property="autoCloseToStatus"/>
  <html:hidden name="bulkIssueForm" property="autoCloseDateStr"/>
  <html:hidden name="bulkIssueForm" property="comments"/>

  <p align="center">
    <button onclick="document.location = './bulkIssue.do?projectPk=${projectPk}'; return false;">Back</button>
    <logic:notEmpty name="generatedIssueList">
      <html:submit value="Submit"/>
    </logic:notEmpty>
  </p>

  <div class="center">
    <table id="tanTable_style2" class="border-zero cellspacing-zero">
    <tbody>
      <tr><th>Issues to be Generated</th></tr>
      <tr><td class="nobordered" align="left">
        <table id="configuredSystemTable" class="pshi-table-small" style="width:1020px">
        <thead>
          <tr>
            <th>Issue Title</th>
            <th>Unit Name</th>
            <th>Category</th>
            <th>Status</th>
            <th>Person Assigned</th>
            <th>Opened By</th>
            <th>Open Date</th>
            <th>Auto Close</th>
            <%--
            <th style="width:60px;">Comment</th>
             --%>
          </tr>
        </thead>
        <tbody>
          <logic:empty name="generatedIssueList">
            <tr><td colspan="9" align="center">No issues were generated.</td></tr>
          </logic:empty>
          <logic:notEmpty name="generatedIssueList">
            <logic:iterate id="issue" name="generatedIssueList" type="com.premiersolutionshi.support.domain.Issue">
              <bean:define id="ship" name="issue" property="ship" />
              <bean:define id="configuredSystem" name="issue" property="configuredSystem" />
              <bean:define id="laptop" name="configuredSystem" property="laptop" />
              <tr style="vertical-align: top;" align="left">
                <td>${issue.title}</td>
                <td>${issue.ship.fullName}</td>
                <td>${issue.category}</td>
                <td>${issue.status}</td>
                <td>${issue.personAssigned}</td>
                <td>${issue.openedBy}</td>
                <td><tags:formatDate value="${issue.openedDate}" /></td>
                <td align="center">
                  <logic:empty name="bulkIssueForm" property="autoCloseDate">
                    N/A
                  </logic:empty>
                  <logic:notEmpty name="bulkIssueForm" property="autoCloseDate">
                    ${issue.autoCloseStatus} on 
                    <tags:formatDate value="${issue.autoCloseDate}" />
                  </logic:notEmpty>
                  <input type="hidden" name="includeConfiguredSystemPkArr" value="${configuredSystem.id}" />
                </td>
                <%--
                <td align="center">
                  <input type="hidden" name="includeConfiguredSystemPkArr" value="${configuredSystem.id}" />
                  <button type="button" class="btn btn-default btn-xs comment-dialog-btn">Comments</button>
                  <html:hidden name="issue" property="allComments" />
                  <button type="button" class="btn btn-default btn-xs email-monthly-dialog-btn">
                    <span class="glyphicon glyphicon-envelope"></span>
                  </button>

                  <html:hidden name="issue" property="title" />
                  <html:hidden name="ship" property="shipName" />
                  <html:hidden name="ship" property="pocEmails" />
                  <html:hidden name="ship" property="primaryPocEmails" />
                  <html:hidden name="configuredSystem" property="computerName" />
                </td>
                --%>
              </tr>
            </logic:iterate>
          </logic:notEmpty>
        </tbody>
        </table>
      </td></tr>
    </tbody>
    </table>
  </div>

  <p align="center">
    <button onclick="document.location = './bulkIssue.do?projectPk=${projectPk}'; return false;">Back</button>
    <logic:notEmpty name="generatedIssueList">
      <html:submit value="Submit"/>
    </logic:notEmpty>
  </p>
</html:form>

<%@ include file="../../layout/old/layout-footer.jsp" %>

<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>

<!-- 
<tags:emailDialog />
 -->
<tags:infoDialog />

<script type="text/javascript">
  var openedByList = [];
  <logic:present name="openedByList">
    <logic:iterate id="openedBy" name="openedByList" type="java.lang.String">
      openedByList.push("${openedBy}");
    </logic:iterate>
  </logic:present>
  var currOsVersion = "${currOsVersion}";
  var currFacetVersion = "${currFacetVersion}";
  var fullName = '<bean:write name="loginBean" property="fullName"/>';
</script>
<script type="text/javascript" src="js/util/mailto.js"></script>
<script type="text/javascript" src="js/support/bulkIssue.js"></script>

</body>
</html>
