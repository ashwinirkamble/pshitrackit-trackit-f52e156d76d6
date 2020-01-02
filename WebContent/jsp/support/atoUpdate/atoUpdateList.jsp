<%@ page language="java" %>
<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags"  tagdir="/WEB-INF/tags" %>

<bean:define id="defaultPageTitle" value="ATO Update List"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/>
<bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value="">
<bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<%@ include file="../../layout/old/layout-header.jsp" %>

<tags:projectBreadcrumb project="${project}" pageTitle="${pageTitle}"/>

<p class="center">
  <a class="btn btn-primary" href="atoUpdate.do?action=add&projectPk=${projectPk}">Add New ATO</a>
</p>

<div class="card " style="margin-top:0">
  <div class="card-body ">
    <div class="row">
      <div class="col">
        <table id="datatable" class="alt-color" style="border:0; margin:0 auto;">
        <thead>
          <tr>
            <th>ATO Name</th>
            <th>Opened</th>
            <th>Total Affected</th>
            <th># Applied</th>
            <th>Last Updated By</th>
            <th>Last Updated</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
        <logic:present name="atoUpdateList">
          <logic:iterate id="atoUpdate" name="atoUpdateList" type="com.premiersolutionshi.support.domain.AtoUpdate">
            <tr>
              <td><a href="atoUpdate.do?action=edit&id=${atoUpdate.id}&projectPk=${projectPk}">${atoUpdate.label}</a></td>
              <td align="center"><tags:formatDate value="${atoUpdate.openedDate}" /></td>
              <td align="center">${atoUpdate.totalIssues}</td>
              <td align="center">${atoUpdate.totalIssuesClosed}</td>
              <td align="center">${atoUpdate.lastUpdatedBy}</td>
              <td align="center">${atoUpdate.lastUpdatedDateStr}</td>
              <td align="center">
                <a href="atoUpdate.do?action=edit&id=${atoUpdate.id}">
                  <img src="images/icon_edit.png" title="Edit"/>
                </a>
                &nbsp;
                <a href="atoUpdate.do?action=delete&id=${atoUpdate.id}" onclick="return confirmDelete('${atoUpdate.label}');">
                  <img src="images/icon_delete.png" title="Delete"/>
                </a>
              </td>
            </tr>
          </logic:iterate>
        </logic:present>
        </tbody>
        </table>
      </div>
    </div>
  </div>
</div>

<%@ include file="../../layout/old/layout-footer.jsp" %>

<script type="text/javascript">
function confirmDelete(atoFilename) {
  return confirm("Are you sure you want to delete '" + atoFilename + "'");
}
$(function() {
  var table = $('#datatable').DataTable({
    paging : true,
    pageLength: 10,
    searching : true,
    stateSave : false,
  }).order([0, 'desc']).draw();
});
</script>
</body>
</html>
