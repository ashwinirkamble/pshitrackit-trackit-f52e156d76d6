<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="fn"    uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="pageTitle" value="Government Property Tracking Summary"></bean:define>

<%@ include file="../../layout/old/layout-header.jsp" %>

<tags:adminBreadcrumb pageTitle="${pageTitle}" />

<div class="card">
  <div class="card-header card-header-info card-header-icon">
    <div class="card-icon">
      <i class="material-icons">list</i>
    </div>
    <div style="display: inline-flex; float:left;">
      <h4 class="card-title">${pageTitle}</h4>
    </div>
    <div class="dropdown" style="display: inline-flex;margin-top: 10px; float: right;">
      <a class="btn btn-secondary dropdown-toggle" href="#" role="button" style="margin: 0; color: black;"
        id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
        <i class="material-icons">settings</i>
      </a>
      <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
        <a href="${path}?action=form" class="dropdown-item">
          <i class="material-icons">add</i>
          Add New Gov Property
        </a>
        <a href="${path}?action=exportToExcel" class="dropdown-item">
          <i class="material-icons" style="color:green;">view_list</i>
          Export to Excel
        </a>
      </div>
    </div>
  </div>
  <div class="card-body">
    <div class="row">
      <div class="col">
        <table class="govPropertyTable">
          <thead>
            <tr>
              <th colspan="5"></th>
              <th colspan="4" class="gov-property-table-quantities">Quantities</th>
              <th colspan="6"></th>
            </tr>
            <tr>
              <th>ID</th>
              <th>Date Listed</th>
              <th>National Stock Number</th>
              <th>Description</th>
              <th>Project/Contract</th>
              <th>Received</th>
              <th>Issued</th>
              <th>Transferred</th>
              <th>On Hand</th>
              <th>Location</th>
              <th>Disposition</th>
              <%--
              <th>Created By</th>
              <th>Created Date</th>
              <th>Last Updated By</th>
              <th>Last Updated Date</th>
              --%>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <logic:iterate id="govProperty" name="govPropertyList">
              <tr>
                <td align="center">${govProperty.id}</td>
                <td>${govProperty.dateListedStr}</td>
                <td>${govProperty.nationalStockNumber}</td>
                <td>${govProperty.description.replaceAll('\\n','<br/>')}</td>
                <td>${govProperty.projectContract}</td>
                <td align="center">${govProperty.received}</td>
                <td align="center">${govProperty.issued}</td>
                <td align="center">${govProperty.transferred}</td>
                <td align="center">${govProperty.onHand}</td>
                <td>${govProperty.location}</td>
                <td>${govProperty.disposition}</td>
                <%-- 
                <td>${govProperty.createdBy}</td>
                <td>${govProperty.createdDateStr}</td>
                <td>${govProperty.lastUpdatedBy}</td>
                <td>${govProperty.lastUpdatedDateStr}</td>
                --%>
                <td align="center">
                  <a href="govProperty.do?action=form&id=${govProperty.id}">
                    <i class="material-icons">edit</i>
                  </a>
                   
                  <a href="govProperty.do?action=delete&id=${govProperty.id}" onclick="return confirmDeleteGovProperty('${govProperty.id}');">
                    <i class="material-icons">delete</i>
                  </a>
                </td>
              </tr>
            </logic:iterate>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>

<%@ include file="../../layout/old/layout-footer.jsp" %>

<script type="text/javascript" src="js/jquery-altcolor.js"></script>
<script type="text/javascript" src="js/valdate.js"></script>

<script type="text/javascript">
$(function() {
  var table = $(".govPropertyTable").DataTable({
    "columnDefs": [
      { type: "date", targets: 1 }, //Date
      { orderable: false, targets: 11 }, //Actions
    ],
  });
});
function confirmDeleteGovProperty(id) {
  return confirm("Are you sure you want to delete record #" + id + "?");
}
</script>
</body>
</html>


