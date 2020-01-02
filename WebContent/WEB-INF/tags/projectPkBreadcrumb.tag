<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<%@ attribute name="projectPk" required="true" %>
<%@ attribute name="projectName" required="false" type="java.lang.String" %>
<%@ attribute name="pageTitle" required="false" type="java.lang.String" %>
<%@ attribute name="parentUrl" required="false" type="java.lang.String" %>
<%@ attribute name="parentTitle" required="false" type="java.lang.String" %>
<%@ attribute name="parent2Url" required="false" type="java.lang.String" %>
<%@ attribute name="parent2Title" required="false" type="java.lang.String" %>
<%@ attribute name="hideOptions" required="false" type="java.lang.String" %>

<table style="width:100%;"><tbody><tr><td>
  <tags:breadcrumb baseTitle="${projectName} Dashboard" baseUrl="page.do?projectPk=${projectPk}"
    pageTitle="${pageTitle}"
    parentTitle="${parentTitle}" parentUrl="${parentUrl}"
    parent2Title="${parent2Title}" parent2Url="${parent2Url}"
  />
</td>
<logic:notEqual name="hideOptions" value="true">
  <td style="width:150px;vertical-align:top;">
    <div class="dropdown show">
      <a class="btn btn-secondary dropdown-toggle" href="#" role="button" style="margin: 0; color: black;"
        id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
        Options
      </a>
      <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">  <!-- &projectPk=${projectPk}&pageFrom=issueList" -->
        <a class="dropdown-item" href="./issue.do?action=add&projectPk=${projectPk}&pageFrom=issueList">
          <i class="material-icons">add</i>
          Add Support Issue
        </a>
        <a class="dropdown-item" href="./project.do?action=taskAdd&projectPk=${projectPk}">
          <i class="material-icons">add</i>
          Add Task
        </a>
        <logic:equal name="projectPk" value="1">
          <a class="dropdown-item" href="./atoUpdate.do?action=add&projectPk=${projectPk}">
            <i class="material-icons">add</i>
            Add New ATO
          </a>
        </logic:equal>
      </div>
    </div>
  </td>
</logic:notEqual>
</tr></tbody></table>
