<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="fn"    uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="pageTitle"
  value="${organizationForm.id == null ? 'Add' : 'Edit'} Organization ${organizationForm.id == null ? '' : organizationForm.name}"/>

<%@ include file="../../layout/old/layout-header.jsp" %>

<tags:projectBreadcrumb project="${project}" pageTitle="${pageTitle}"
  parentTitle="POC List" parentUrl="organization.do?projectPk=${project.id}"/>

<%@ include file="../../include/support/poc/pocSearchBar.jsp" %>

<html:form method="POST" enctype="multipart/form-data" action="organization.do" styleId="Form"
  onsubmit="return validateOrganization();">
  <input type="hidden" name="action" value="save"/>
  <input type="hidden" name="projectFk" value="${projectPk}"/>
  <html:hidden name="organizationForm" property="id"/>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-1"></div>
        <div class="col-md-10">
          <div class="card">
            <div class="card-header card-header-info card-header-icon">
              <div class="card-icon">
                <i class="material-icons">business</i>
              </div>
              <div style="display: inline-flex; float:left;">
                <h4 class="card-title">Organization Form</h4>
              </div>
              <div class="dropdown show" style="display: inline-flex;margin-top: 10px; float: right;">
                <a class="btn btn-secondary dropdown-toggle" href="#" role="button" style="margin: 0; color: black;"
                  id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                  <i class="material-icons">settings</i>
                </a>
                <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                  <logic:notEmpty name="organizationForm" property="id">
                    <a class="dropdown-item" href="organization.do?action=view&projectPk=${projectPk}&id=${organizationForm.id}">
                      <i class="material-icons">edit</i>
                      View Organization
                    </a>
                  </logic:notEmpty>
                  <a class="dropdown-item" href="organization.do?action=form&projectPk=${projectPk}">
                    <i class="material-icons">add</i>
                    Add New Organization
                  </a>
                </div>
              </div>
            </div>
            <div class="card-body pshi-form-body">
              <div class="row">
                <label for="name" class="col-sm-3 col-form-label"><span class="required">*</span> Name:</label>
                <div class="col-sm-9">
                  <html:text styleClass="form-control" maxlength="127" styleId="name" property="name"/>
                </div>
              </div>
              <div class="row">
                <label for="address1" class="col-sm-3 col-form-label">Address 1:</label>
                <div class="col-sm-9">
                  <html:text styleClass="form-control" maxlength="127" styleId="address1" property="address1"/>
                </div>
              </div>
              <div class="row">
                <label for="address2" class="col-sm-3 col-form-label">Address 2:</label>
                <div class="col-sm-9">
                  <html:text styleClass="form-control" maxlength="127" styleId="address2" property="address2"/>
                </div>
              </div>
              <div class="row">
                <label for="city" class="col-sm-3 col-form-label">City:</label>
                <div class="col-sm-4">
                  <html:text styleClass="form-control" maxlength="127" styleId="city" property="city"/>
                </div>
                <label for="stateProvince" class="col-sm-2 col-form-label">State:</label>
                <div class="col-sm-3">
                  <html:select name="organizationForm" property="stateProvince" styleClass="form-control input-sm">
                    <html:option value="">N/A</html:option>
                    <html:options collection="stateList" property="itemValue"/>
                  </html:select>
                </div>
              </div>
              <div class="row">
                <label for="zip" class="col-sm-3 col-form-label">Zip:</label>
                <div class="col-sm-4">
                  <html:text styleClass="form-control" maxlength="10" styleId="zip" property="zip"/>
                </div>
                <label for="country" class="col-sm-2 col-form-label">Country:</label>
                <div class="col-sm-3">
                  <html:text styleClass="form-control" maxlength="127" styleId="country" property="country"/>
                </div>
              </div>
              <div class="row">
                <label for="email" class="col-sm-3 col-form-label">Email:</label>
                <div class="col-sm-9">
                  <html:text styleClass="form-control" maxlength="127" styleId="email" property="email"/>
                </div>
              </div>
              <div class="row">
                <label for="url" class="col-sm-3 col-form-label">Url:</label>
                <div class="col-sm-9">
                  <html:text styleClass="form-control" maxlength="127" styleId="url" property="url"/>
                </div>
              </div>
              <div class="row">
                <label for="phone" class="col-sm-3 col-form-label">Phone:</label>
                <div class="col-sm-3">
                  <html:text styleClass="form-control" maxlength="127" styleId="phone" property="phone"/>
                </div>
                <label for="faxNumber" class="col-sm-3 col-form-label">Fax Number:</label>
                <div class="col-sm-3">
                  <html:text styleClass="form-control" maxlength="127" styleId="faxNumber" property="faxNumber"/>
                </div>
              </div>
              <div class="row">
                <label for="notes" class="col-sm-3 col-form-label">Notes:</label>
                <div class="col-sm-9">
                  <html:textarea name="organizationForm" property="notes" styleId="notes" rows="4" styleClass="form-control"/>
                </div>
              </div>
            </div>
            <logic:notEmpty name="organizationForm" property="id">
              <div class="row">
                <div class="col">
                  <tags:lastUpdatedBy by="${organizationForm.lastUpdatedBy}" date="${organizationForm.lastUpdatedDateStr}"/>
                </div>
              </div>
            </logic:notEmpty>
            <div class="card-footer ml-auto mr-auto center">
              <span>
                <html:submit value="Save" styleClass="btn btn-fill btn-primary"/>
                <a class="btn btn-default" href="organization.do?projectPk=${project.id}">Cancel</a>
              </span>
            </div>
          </div>
        </div>
        <div class="col-md-1"></div>
      </div>
    </div>
  </div>
</html:form>

<%@ include file="../../layout/old/layout-footer.jsp" %>

<script type="text/javascript" src="js/valdate.js"></script>
<script type="text/javascript" src="js/support/poc/organizationForm.js"></script>
</body>
</html>

