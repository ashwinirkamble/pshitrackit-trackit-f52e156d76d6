<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="fn"    uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="pageTitle"
  value="${pocForm.id == null ? 'Add' : 'Edit'} POC${pocForm.id == null ? '' : ': '.concat(pocForm.fullName)}"/>

<%@ include file="../../layout/old/layout-header.jsp" %>

<tags:projectBreadcrumb project="${project}"
  parentTitle="POC List" parentUrl="organization.do?projectPk=${project.id}"
  parent2Title="${organization == null ? '' : organization.name}"
  parent2Url="organization.do?action=view&projectPk=${projectPk}${organization == null ? '' : '&id='.concat(organization.id)}"
  pageTitle="${pageTitle}"
/>

<%@ include file="../../include/support/poc/pocSearchBar.jsp" %>

<html:form method="POST" enctype="multipart/form-data" action="poc.do" styleId="pocForm"
  onsubmit="return validatePoc();">
  <input type="hidden" name="action" value="save"/>
  <input type="hidden" name="projectFk" value="${projectPk}"/>
  <html:hidden name="pocForm" property="id"/>
  <html:hidden name="pocForm" property="pocTypeCode"/>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-1"></div>
        <div class="col-md-10">
          <div class="card">
            <%@ include file="../../include/support/poc/pocCardHeader.jsp" %>
            <div class="card-body pshi-form-body">
              <div class="row">
                <logic:equal name="pocForm" property="pocTypeCode" value="1">
                  <label for="organizationFk" class="col-sm-3 col-form-label"><span class="required">*</span> Organization:</label>
                  <div class="col-sm-9">
                    <html:select name="pocForm" property="organizationFk" styleClass="form-control input-sm">
                      <html:option value="">N/A</html:option>
                      <html:options collection="organizationList" property="id" labelProperty="name"/>
                    </html:select>
                  </div>
                </logic:equal>
                <logic:equal name="pocForm" property="pocTypeCode" value="2">
                  <label for="organizationFk" class="col-sm-3 col-form-label"><span class="required">*</span> Unit:</label>
                  <div class="col-sm-9">
                    <html:select name="pocForm" property="shipFk" styleClass="form-control input-sm">
                      <html:option value="">N/A</html:option>
                      <html:options collection="unitList" property="id" labelProperty="shipName"/>
                    </html:select>
                  </div>
                </logic:equal>
              </div>
              <div class="row">
                <label for="lastName" class="col-sm-3 col-form-label"><span class="required">*</span> Last Name:</label>
                <div class="col-sm-3">
                  <html:text styleClass="form-control" maxlength="50" property="lastName"/>
                </div>
                <label for="firstName" class="col-sm-3 col-form-label"><span class="required">*</span> First Name:</label>
                <div class="col-sm-3">
                  <html:text styleClass="form-control" maxlength="50" property="firstName"/>
                </div>
              </div>
              <div class="row">
                <label for="title" class="col-sm-3 col-form-label">Title:</label>
                <div class="col-sm-9">
                  <html:text styleClass="form-control" maxlength="50" property="title"/>
                </div>
              </div>
              <div class="row">
                <label for="title" class="col-sm-3 col-form-label">DIV:</label>
                <div class="col-sm-9">
                  <html:text styleClass="form-control" maxlength="50" property="dept"/>
                </div>
              </div>
              <div class="row">
                <label for="primaryPoc" class="col-sm-3 col-form-label">Is Primary:</label>
                <div class="col-sm-1">
                  <html:checkbox styleClass="form-control" property="primaryPoc"/>
                </div>
                <div class="col-sm-8"></div>
              </div>
              <div class="row">
                <label for="rank" class="col-sm-3 col-form-label">Rank:</label>
                <div class="col-sm-9">
                  <html:text styleClass="form-control" maxlength="10" property="rank"/>
                </div>
              </div>
              <div class="row">
                <label for="email" class="col-sm-3 col-form-label">Email:</label>
                <div class="col-sm-9">
                  <html:text styleClass="form-control" maxlength="255" property="email"/>
                </div>
              </div>
              <div class="row">
                <label for="workNumber" class="col-sm-3 col-form-label">Work Number:</label>
                <div class="col-sm-4">
                  <html:text styleClass="form-control" maxlength="25" property="workNumber"/>
                </div>
                <label for="workNumberExt" class="col-sm-2 col-form-label">Ext:</label>
                <div class="col-sm-3">
                  <html:text name="pocForm" property="workNumberExt" styleClass="form-control integer"/>
                </div>
              </div>
              <div class="row">
                <label for="faxNumber" class="col-sm-3 col-form-label">Fax Number:</label>
                <div class="col-sm-9">
                  <html:text styleClass="form-control" maxlength="25" property="faxNumber"/>
                </div>
              </div>
              <div class="row">
                <label for="cellNumber" class="col-sm-3 col-form-label">Cell Number:</label>
                <div class="col-sm-9">
                  <html:text styleClass="form-control" maxlength="25" property="cellNumber"/>
                </div>
              </div>
              <div class="row">
                <label for="title" class="col-sm-3 col-form-label">Notes:</label>
                <div class="col-sm-9">
                  <html:textarea styleClass="form-control" property="notes" rows="5"/>
                </div>
              </div>
              <div class="row">
                <div class="col">
                  <logic:notEmpty name="pocForm" property="id">
                    <tags:lastUpdatedBy by="${pocForm.lastUpdatedBy}" date="${pocForm.lastUpdatedDateStr}"/>
                  </logic:notEmpty>
                </div>
              </div>
            </div>
            <div class="card-footer ml-auto mr-auto center">
              <span>
                <html:submit value="Save" styleClass="btn btn-fill btn-primary"/>
                <a class="btn btn-default" href="poc.do">Cancel</a>
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
<script type="text/javascript" src="js/support/poc/pocForm.js"></script>
</body>
</html>

