<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="fn"    uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="pageTitle" value="Organization: ${organizationForm.name}"/>

<%@ include file="../../layout/old/layout-header.jsp" %>

<tags:projectBreadcrumb project="${project}" pageTitle="${pageTitle}"
  parentTitle="POC List" parentUrl="organization.do?projectPk=${project.id}"/>

<%@ include file="../../include/support/poc/pocSearchBar.jsp" %>

<html:form method="POST" enctype="multipart/form-data" action="organization.do" styleId="organizationForm">
<div class="content">
  <div class="container-fluid">
    <div class="row">
      <div class="col-md-1"></div>
      <div class="col-md-10">
        <div class="card ">
          <div class="card-header card-header-info card-header-icon">
            <div class="card-icon">
              <i class="material-icons">business</i>
            </div>
            <div style="display: inline-flex; float:left;">
              <h4 class="card-title">${pageTitle}</h4>
            </div>
            <div class="dropdown show" style="display: inline-flex;margin-top: 10px; float: right;">
              <a class="btn btn-secondary dropdown-toggle" href="#" role="button" style="margin: 0; color: black;"
                id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <i class="material-icons">settings</i>
              </a>
              <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                <logic:notEmpty name="organizationForm" property="id">
                  <a class="dropdown-item" href="${path}?action=form&projectPk=${projectPk}&id=${organizationForm.id}">
                    <i class="material-icons">edit</i>
                    Edit Organization
                  </a>
                </logic:notEmpty>
                <a class="dropdown-item" href="poc.do?action=form&projectPk=${projectPk}&organizationFk=${organizationForm.id}">
                  <i class="material-icons">add</i>
                  Add New POC
                </a>
                <a class="dropdown-item" href="${path}?action=form&projectPk=${projectPk}">
                  <i class="material-icons">add</i>
                  Add New Organization
                </a>
              </div>
            </div>
            </div>
          <div class="card-body pshi-form-body">
            <div class="row">
              <label for="name" class="col-sm-3 col-form-label">Name:</label>
              <div class="col-sm-9 pshi-view-field">${organizationForm.name}</div>
            </div>
            <div class="row">
              <label for="address1" class="col-sm-3 col-form-label">Address:</label>
              <div class="col-sm-9 pshi-view-field">
                <logic:notEmpty name="organizationForm" property="address1">
                  ${organizationForm.address1}<br/>
                </logic:notEmpty>
                <logic:notEmpty name="organizationForm" property="address2">
                  ${organizationForm.address2}<br/>
                </logic:notEmpty>
                <logic:notEmpty name="organizationForm" property="city">
                  ${organizationForm.city},
                </logic:notEmpty>
                <logic:notEmpty name="organizationForm" property="city">
                  ${organizationForm.stateProvince}<br/>
                </logic:notEmpty>
                ${organizationForm.zip} ${organizationForm.country}
              </div>
            </div>
            <div class="row">
              <label for="email" class="col-sm-3 col-form-label">Email:</label>
              <div class="col-sm-9 pshi-view-field">
                ${organizationForm.email}<logic:empty name="organizationForm" property="email">-</logic:empty>
              </div>
            </div>
            <div class="row">
              <label for="url" class="col-sm-3 col-form-label">URL:</label>
              <div class="col-sm-9 pshi-view-field">
                <logic:notEmpty name="organizationForm" property="url">
                  <a href="${organizationForm.url}">${organizationForm.url}</a>
                </logic:notEmpty>
                <logic:empty name="organizationForm" property="url">-</logic:empty>
              </div>
            </div>
            <div class="row">
              <label for="phone" class="col-sm-3 col-form-label">Phone:</label>
              <div class="col-sm-9 pshi-view-field">
                ${organizationForm.phone}<logic:empty name="organizationForm" property="phone">-</logic:empty>
              </div>
            </div>
            <div class="row">
              <label for="faxNumber" class="col-sm-3 col-form-label">Fax Number:</label>
              <div class="col-sm-9 pshi-view-field">
                ${organizationForm.faxNumber}<logic:empty name="organizationForm" property="faxNumber">-</logic:empty>
              </div>
            </div>
            <div class="row">
              <label for="notes" class="col-sm-3 col-form-label">Notes:</label>
              <div class="col-sm-9 pshi-view-field">
                ${organizationForm.notes.replaceAll('\\n','<br/>')}
                <logic:empty name="organizationForm" property="notes">-</logic:empty>
              </div>
            </div>
            <%--
            <div class="row">
              <label for="primaryPocFk" class="col-sm-3 col-form-label">Primary POC:</label>
              <div class="col-sm-9">${organizationForm.primaryPocFk}</div>
            </div>
            --%>
            <logic:notEmpty name="organizationForm" property="pocEmails">
              <div class="row">
                <div class="col-sm-12 ml-auto mr-auto" style="text-align:center;">
                  <logic:notEmpty name="organizationForm" property="primaryPocEmails">
                    <a href="mailto:${organizationForm.primaryPocEmails}" class="btn btn-primary">
                      <i class="material-icons">email</i> Email Primary POCs
                    </a>
                  </logic:notEmpty>
                  <a href="mailto:${organizationForm.pocEmails}" class="btn btn-primary">
                    <i class="material-icons">email</i> Email All POCs
                  </a>
                </div>
              </div>
            </logic:notEmpty>
          </div>
          <div class="row">
            <div class="col">
              <logic:notEmpty name="organizationForm" property="id">
                <tags:lastUpdatedBy by="${organizationForm.lastUpdatedBy}" date="${organizationForm.lastUpdatedDateStr}"/>
              </logic:notEmpty>
            </div>
          </div>
        </div>
      </div>
      <div class="col-md-1"></div>
    </div>

    <%@ include file="../../include/support/poc/pocTableDisplay.jsp" %>
  </div>
</div>
</html:form>

<%@ include file="../../layout/old/layout-footer.jsp" %>

<script type="text/javascript" src="js/valdate.js"></script>
<script type="text/javascript" src="js/support/poc/organizationForm.js"></script>
<script type="text/javascript" src="js/support/poc/pocForm.js"></script>

<script type="text/javascript">
$(function() {
  var actionCol = 3;
  var lastUpdatedDateCol = 4;
  var table = $(".pocTable").DataTable({
    stateSave : false,
    columnDefs : [
      { orderable: false, targets: actionCol },
      { type: "date", targets: lastUpdatedDateCol }, //Last Updated
    ],
    order: [[lastUpdatedDateCol, 'desc']],
  });
});
</script>
</body>
</html>

