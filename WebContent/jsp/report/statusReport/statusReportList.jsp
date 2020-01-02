<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="fn"    uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="pageTitle" value="Status Report List"></bean:define>

<%@ include file="../../layout/old/layout-header.jsp" %>

<tags:adminBreadcrumb pageTitle="${pageTitle}" parentTitle="Manage Status Reports" parentUrl="statusReport.do"></tags:adminBreadcrumb>

<div class="card">
  <%@ include file="../../include/report/statusReport/statusReportCardHeader.jsp" %>
  <div class="card-body">
    <table class="statusReportTable">
      <thead>
        <tr>
          <th>Project Name</th>
          <th>Report Type</th>
          <th>Contract</th>
          <th>Organization</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <logic:iterate id="statusReport" name="statusReportList">
          <tr>
            <td><a href="statusReport.do?action=view&id=${statusReport.id}">${statusReport.projectName}</a></td>
            <td>${statusReport.statusReportType.name}</td>
            <td>
              <logic:notEmpty name="statusReport" property="contract">
                ${statusReport.contract.name}
              </logic:notEmpty>
              <logic:empty name="statusReport" property="contract">-</logic:empty>
            </td>
            <td>
              <logic:notEmpty name="statusReport" property="organization">
                ${statusReport.organization.name}
              </logic:notEmpty>
              <logic:empty name="statusReport" property="organization">-</logic:empty>
            </td>
            <td align="center">
              <a href="statusReport.do?action=form&id=${statusReport.id}">
                <i class="material-icons">edit</i>
              </a>
              <a href="statusReport.do?action=delete&id=${statusReport.id}" onclick="return confirmDelete('${statusReport.id}', "${statusReport.name}");">
                <i class="material-icons">delete</i>
              </a>
            </td>
          </tr>
        </logic:iterate>
      </tbody>
    </table>
  </div>
</div>

<%@ include file="../../layout/old/layout-footer.jsp" %>

<script type="text/javascript" src="js/jquery-altcolor.js"></script>
<script type="text/javascript" src="js/valdate.js"></script>
<script type="text/javascript" src="js/report/statusReport.js"></script>
</body>
</html>


