<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="defaultPageTitle" value="Manage UIC List"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<%@ include file="../layout/old/layout-header.jsp" %>

<tags:adminBreadcrumb pageTitle="${pageTitle}" />

<div class="card">
  <div class="card-header card-header-info card-header-icon">
    <div class="card-icon">
      <i class="material-icons">directions_boat</i>
    </div>
    <div style="display: inline-flex; float:left;">
      <h4 class="card-title">Units</h4>
    </div>
    <div class="dropdown show" style="display: inline-flex;margin-top: 10px; float: right;">
      <a class="btn btn-secondary dropdown-toggle" href="#" role="button" style="margin: 0; color: black;"
        id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
        <i class="material-icons">settings</i>
      </a>
      <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
        <a href="ship.do?action=shipAdd" class="dropdown-item" style="display:${isSysadmin ? 'block' : 'none'};">
          <i class="material-icons">add</i> Add New Unit
        </a>
      </div>
    </div>
  </div>
  <div class="card-body">
    <div class="row">
      <div class="col">
        <table id="dataTable" class="stripe row-border">
          <thead>
            <tr>
              <th>UIC</th>
              <th>Unit Name</th>
              <th>Type/Hull</th>
              <th>Tycom</th>
              <th>RSupply</th>
              <th>Homeport</th>
              <th style="display:${isSysadmin ? 'block' : 'none'};">Action</th>
            </tr>
          </thead>
          <tbody>
            <logic:iterate id="shipBean" name="resultList" type="com.premiersolutionshi.old.bean.ShipBean">
              <tr align="left">
                <td align="center">${shipBean.uic}</td>
                <td>
                  <a href="javascript:void(0);" onclick="return showUnitPopup(${shipBean.shipPk},1);">
                    ${shipBean.shipName}
                  </a>
                </td>
                <td>${shipBean.type} ${shipBean.hull}</td>
                <td>${shipBean.tycomDisplay}</td>
                <td>${shipBean.rsupply}</td>
                <td>${shipBean.homeport}</td>
                <td align="center" nowrap style="display:${isSysadmin ? 'block' : 'none'}">
                  <a href="ship.do?action=shipEdit&shipPk=${shipBean.shipPk}">
                    <i class="material-icons">edit</i>
                  </a>
                  <a href="ship.do?action=shipDeleteDo&shipPk=${shipBean.shipPk}"
                     onclick="return confirmDelete('${shipBean.shipNameJs}');">
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

<%@ include file="../layout/old/layout-footer.jsp" %>

<script src="js/jquery.dataTables.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/support/unit/showUnitPopup.js"></script>

<script type="text/javascript">
  $(document).ready(function () {
    $('#dataTable').DataTable({
      "order": [[ 1, "asc" ]],
      "paging": true,
      "columnDefs": [{ "orderable": false, "targets": 6 }],
      stateSave: true
    });
  });

  function confirmDelete(shipName) {
    return confirm("Are you sure you want to delete " + shipName);
  } //end of confirmDelete
</script>

</body>
</html>
