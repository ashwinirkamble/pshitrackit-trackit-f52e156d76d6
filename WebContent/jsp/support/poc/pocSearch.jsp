<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="fn"    uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="pageTitle" value="POC Search: '${searchText}'"/>

<%@ include file="../../layout/old/layout-header.jsp" %>

<tags:projectBreadcrumb project="${project}" pageTitle="${pageTitle}"
  parentTitle="POC List" parentUrl="organization.do?projectPk=${project.id}"/>

<%@ include file="../../include/support/poc/pocSearchBar.jsp" %>

<div class="card">
  <div class="card-header card-header-info card-header-icon">
    <div class="card-icon">
      <i class="material-icons">import_contacts</i>
    </div>
    <div style="display: inline-flex; float:left;">
      <h4 class="card-title">${pageTitle}</h4>
    </div>
  </div>
  <div class="card-body">
    <table class="pocTable">
      <thead>
        <tr>
          <th>Name</th>
          <th>E-mail</th>
          <th>Work Number</th>
          <th>Organization/Unit</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <logic:iterate id="poc" name="pocSearchResults">
          <tr>
            <td>
              <a href="${path}?action=view&projectPk=${poc.projectFk}&id=${poc.id}">${poc.firstName} ${poc.lastName}</a>
            </td>
            <td>
              <tags:emailLink value="${poc.email}" />
              <logic:empty name="poc" property="email">-</logic:empty>
            </td>
            <td>${poc.workNumber} ${poc.workNumberExt == null || poc.workNumberExt <= 0 ? '' : ' x'.concat(poc.workNumberExt)}</td>
            <td>
              <logic:notEmpty name="poc" property="organization">
                <a href="organization.do?projectPk=${projectPk}&id=${poc.organization.id}"
                  title="View Organization POCs">
                  ${poc.organization.name}
                </a>
              </logic:notEmpty>
              <logic:notEmpty name="poc" property="ship">
                <tags:shipPocLink value="${poc.ship}"/>
              </logic:notEmpty>
            </td>
            <td align="center">
              <a href="poc.do?action=form&id=${poc.id}">
                <i class="material-icons">edit</i>
              </a>
              <a href="poc.do?action=delete&id=${poc.id}" onclick="return confirmDeletePoc('${poc.id}');">
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
<script type="text/javascript" src="js/support/unit/showUnitPopup.js"></script>

<script type="text/javascript">
$(function() {
  var actionCol = 4;
  var table = $(".pocTable").DataTable({
    stateSave : false,
    columnDefs : [
      { orderable: false, targets: actionCol }
    ],
  });
});
function confirmDeletePoc(id) {
  return confirm("Are you sure you want to delete record #" + id + "?");
}
</script>
</body>
</html>


