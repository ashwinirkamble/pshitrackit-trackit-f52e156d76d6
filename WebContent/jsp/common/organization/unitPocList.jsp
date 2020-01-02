<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="fn"    uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="pageTitle" value="Unit: ${ship.shipName}"/>

<%@ include file="../../layout/old/layout-header.jsp" %>

<tags:projectBreadcrumb project="${project}" pageTitle="${pageTitle}"
  parentTitle="POC List" parentUrl="organization.do?projectPk=${project.id}"/>

<%@ include file="../../include/support/poc/pocSearchBar.jsp" %>

<div class="content">
  <div class="container-fluid">
    <div class="row">
      <div class="col-md-1"></div>
      <div class="col-md-10">
        <div class="card ">
          <div class="card-header card-header-info card-header-icon">
            <div class="card-icon">
              <i class="material-icons">directions_boat</i>
            </div>
            <div style="display: inline-flex; float:left;">
              <h4 class="card-title">Unit View</h4>
            </div>
            <div class="dropdown show" style="display: inline-flex;margin-top: 10px; float: right;">
              <a class="btn btn-secondary dropdown-toggle" href="#" role="button" style="margin: 0; color: black;"
                id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <i class="material-icons">settings</i>
              </a>
              <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                <a class="dropdown-item" href="ship.do?action=shipEdit&shipPk=${ship.id}&projectPk=${projectPk}">
                  <i class="material-icons">edit</i>
                  Edit Unit
                </a>
                <a class="dropdown-item" href="${path}?action=form&pocTypeCode=1&projectPk=${projectPk}">
                  <i class="material-icons">add</i>
                  Add New Organization
                </a>
                <a class="dropdown-item" href="poc.do?action=form&pocTypeCode=2&projectPk=${projectPk}&shipFk=${ship.id}">
                  <i class="material-icons">add</i>
                  Add New Unit POC
                </a>
              </div>
            </div>
          </div>
          <div class="card-body pshi-form-body">
            <div class="row">
              <label for="name" class="col-sm-3 col-form-label"><span class="required">*</span> Name:</label>
              <div class="col-sm-9 pshi-view-field">
                <tags:shipPocLink value="${ship}"/>
              </div>
            </div>
            <div class="row">
              <label for="serviceCode" class="col-sm-3 col-form-label">Service Code:</label>
              <div class="col-sm-3 pshi-view-field">
                ${ship.serviceCode}
                <logic:empty name="ship" property="serviceCode">-</logic:empty>
              </div>
              <label for="tycom" class="col-sm-3 col-form-label">TYCOM:</label>
              <div class="col-sm-3 pshi-view-field">
                ${ship.tycom}
                <logic:empty name="ship" property="tycom">-</logic:empty>
              </div>
            </div>
            <div class="row">
              <label for="homeport" class="col-sm-3 col-form-label">Homeport:</label>
              <div class="col-sm-3 pshi-view-field">
                ${ship.homeport}
                <logic:empty name="ship" property="homeport">-</logic:empty>
              </div>
              <label for="rsupply" class="col-sm-3 col-form-label">Rsupply:</label>
              <div class="col-sm-3 pshi-view-field">
                ${ship.rsupply}
                <logic:empty name="ship" property="rsupply">-</logic:empty>
              </div>
            </div>
            <logic:notEmpty name="ship" property="pocEmails">
              <div class="row">
                <div class="col-sm-12 ml-auto mr-auto" style="text-align:center;">
                  <logic:notEmpty name="ship" property="primaryPocEmails">
                    <a href="mailto:${ship.primaryPocEmails}" class="btn btn-primary">
                      <i class="material-icons">email</i> Email Primary POCs
                    </a>
                  </logic:notEmpty>
                  <a href="mailto:${ship.pocEmails}" class="btn btn-primary">
                    <i class="material-icons">email</i> Email All
                  </a>
                </div>
              </div>
            </logic:notEmpty>
          </div>
        </div>
      </div>
      <div class="col-md-1"></div>
    </div>

    <%@ include file="../../include/support/poc/pocTableDisplay.jsp" %>
  </div>
</div>

<%@ include file="../../layout/old/layout-footer.jsp" %>

<script type="text/javascript" src="js/valdate.js"></script>
<script type="text/javascript" src="js/support/poc/organizationForm.js"></script>
<script type="text/javascript" src="js/support/unit/showUnitPopup.js"></script>

<script type="text/javascript">
$(function() {
  var lastUpdatedDateCol = 4;
  var actionCol = 5;
  var table = $(".pocTable").DataTable({
    stateSave : false,
    columnDefs : [
      { orderable: false, targets: actionCol },
      { type: "date", targets: lastUpdatedDateCol }, //Last Updated
    ],
    order: [[lastUpdatedDateCol, 'desc']],
  });
});
function confirmDeletePoc(id) {
  return confirm("Are you sure you want to delete record #" + id + "?");
}
</script>
</body>
</html>

