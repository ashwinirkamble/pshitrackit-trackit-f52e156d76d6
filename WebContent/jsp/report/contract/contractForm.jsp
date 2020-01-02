<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="fn"    uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="pageTitle"
  value="${contractForm.id == null ? 'Add' : 'Edit'} Contract${contractForm.id == null ? '' : ': '.concat(contractForm.contractNumber)}"/>

<%@ include file="../../layout/old/layout-header.jsp" %>

<tags:adminBreadcrumb pageTitle="${pageTitle}" parentTitle="Manage Contracts" parentUrl="contract.do"/>

<html:form method="POST" enctype="multipart/form-data" action="contract.do" styleId="contractForm"
  onsubmit="return validateContract();">
  <input type="hidden" name="action" value="save"/>
  <html:hidden name="contractForm" property="createdByFk"/>
  <html:hidden name="contractForm" property="createdDateStr"/>
  <html:hidden name="contractForm" property="id"/>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-1"></div>
        <div class="col-md-10">
          <div class="card ">
            <%@ include file="../../include/report/contract/contractCardHeader.jsp" %>
            <div class="card-body pshi-form-body">
            <div class="row">
              <label for="contractNumber" class="col-sm-3 col-form-label"><span class="required">*</span> Contract Number:</label>
              <div class="col-sm-9">
                <html:text styleClass="form-control" maxlength="255" styleId="contractNumber" property="contractNumber"/>
              </div>
            </div>
            <div class="row">
              <label for="name" class="col-sm-3 col-form-label">Name:</label>
              <div class="col-sm-9">
                <html:text styleClass="form-control" maxlength="255" styleId="name" property="name"/>
              </div>
            </div>
            <div class="row">
              <label for="organizationFk" class="col-sm-3 col-form-label">Organization:</label>
              <div class="col-sm-9">
                <html:select name="contractForm" property="organizationFk" styleClass="form-control input-sm">
                  <html:option value="">N/A</html:option>
                  <html:options collection="organizationList" property="id" labelProperty="name"/>
                </html:select>
              </div>
            </div>
            <div class="row">
              <label for="startDate" class="col-sm-3 col-form-label">Start Date:</label>
              <div class="col-sm-3">
                <html:text name="contractForm" property="startDateStr" styleId="startDate" maxlength="11" styleClass="form-control datepicker"/>
              </div>
              <label for="endDate" class="col-sm-3 col-form-label">End Date:</label>
              <div class="col-sm-3">
                <html:text name="contractForm" property="endDateStr" styleId="endDate" maxlength="11" styleClass="form-control datepicker"/>
              </div>
            </div>
            <div class="row">
              <label for="notes" class="col-sm-3 col-form-label">Notes:</label>
              <div class="col-sm-9">
                <html:textarea name="contractForm" property="notes" styleId="notes" rows="4" styleClass="form-control"/>
              </div>
            </div>
          </div>
          <div class="row">
            <div class="col">
              <logic:notEmpty name="contractForm" property="id">
                <tags:lastUpdatedBy by="${contractForm.lastUpdatedBy}" date="${contractForm.lastUpdatedDateStr}"/>
              </logic:notEmpty>
            </div>
          </div>
          <div class="card-footer ml-auto mr-auto">
            <html:submit value="Save" styleClass="btn btn-fill btn-primary"/>
            <a class="btn btn-default" href="contract.do">Cancel</a>
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
<script type="text/javascript" src="js/report/contract.js"></script>
</body>
</html>

