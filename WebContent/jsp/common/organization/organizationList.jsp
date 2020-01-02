<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="fn"    uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="pageTitle" value="POC List"/>

<%@ include file="../../layout/old/layout-header.jsp" %>

<tags:projectBreadcrumb project="${project}" pageTitle="${pageTitle}"/>

<logic:notEmpty name="project">
  <%@ include file="../../include/support/poc/pocSearchBar.jsp" %>

  <div class="card">
    <div class="card-header card-header-info card-header-icon">
      <div class="card-icon">
        <i class="material-icons">directions_boat</i>
      </div>
      <div style="display: inline-flex; float:left;">
        <h4 class="card-title">Organizations</h4>
      </div>
      <div class="dropdown show" style="display: inline-flex;margin-top: 10px; float: right;">
        <a class="btn btn-secondary dropdown-toggle" href="#" role="button" style="margin: 0; color: black;"
          id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          <i class="material-icons">settings</i>
        </a>
        <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
          <a class="dropdown-item" href="organization.do?action=form&projectPk=${project.id}">
            <i class="material-icons">add</i>
            Add New Organization
          </a>
        </div>
      </div>
    </div>
    <div class="card-body">
      <table class="organizationTable stripe row-border">
        <thead>
          <tr>
            <th>Name</th>
            <%--<th>Primary Poc</th> --%>
            <th>Address</th>
            <th>Phone</th>
            <th>Email</th>
            <th>URL</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <logic:iterate id="organization" name="organizationList">
            <tr>
              <td>
                <a href="${path}?action=view&projectPk=${projectPk}&id=${organization.id}">${organization.name}</a>
              </td>
              <%--<td>${organization.primaryPocFk}</td> --%>
              <td>
                <logic:notEmpty name="organization" property="address1">
                  ${organization.address1}<br/>
                </logic:notEmpty>
                <logic:notEmpty name="organization" property="address2">
                  ${organization.address2}<br/>
                </logic:notEmpty>
                <logic:notEmpty name="organization" property="city">
                  ${organization.city},
                </logic:notEmpty>
                <logic:notEmpty name="organization" property="stateProvince">
                  ${organization.stateProvince}<br/>
                </logic:notEmpty>
                ${organization.zip} ${organization.country}
              </td>
              <td>${organization.phone}<logic:empty name="organization" property="phone">-</logic:empty></td>
              <td>${organization.email}<logic:empty name="organization" property="email">-</logic:empty></td>
              <td><a href="${organization.url}">${organization.url}</a><logic:empty name="organization" property="url">-</logic:empty></td>
              <td align="center">
                <a href="organization.do?action=form&projectPk=${project.id}&id=${organization.id}">
                  <i class="material-icons">edit</i>
                </a>
                <a href="organization.do?action=delete&projectPk=${project.id}&id=${organization.id}"
                  onclick="return confirmDelete('${organization.id}');">
                  <i class="material-icons">delete</i>
                </a>
              </td>
            </tr>
          </logic:iterate>
        </tbody>
      </table>
    </div>
  </div>
</logic:notEmpty>

<%@ include file="../../layout/old/layout-footer.jsp" %>

<script type="text/javascript" src="js/jquery-altcolor.js"></script>
<script type="text/javascript" src="js/valdate.js"></script>

<script type="text/javascript">
$(function() {
  var actionCol = 5;
  var table = $(".organizationTable").DataTable({
    // paging : false,
    // searching : false,
    // stateSave : false,
    columnDefs : [
      { orderable: false, targets: actionCol }
    ],
  });
});
</script>
</body>
</html>


