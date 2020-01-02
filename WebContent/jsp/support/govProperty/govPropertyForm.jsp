<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="fn"    uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="pageTitle"
  value="${govPropertyForm.id == null ? 'Add' : 'Edit'} Government Property ${govPropertyForm.id == null ? '' : ' #'.concat(govPropertyForm.id)}"/>

<%@ include file="../../layout/old/layout-header.jsp" %>

<tags:adminBreadcrumb pageTitle="${pageTitle}" parentUrl="govProperty.do" parentTitle="Government Property Tracking"/>

<html:form action="govProperty.do" styleId="govPropertyForm" onsubmit="return validateGovProperty();" style="width:740px"
  method="POST" enctype="multipart/form-data">
  <input type="hidden" name="action" value="save"/>
  <html:hidden name="govPropertyForm" property="id"/>
  <div class="card">
    <div class="card-header card-header-info card-header-icon">
      <div class="card-icon">
        <i class="material-icons">list</i>
      </div>
      <div style="display: inline-flex; float:left;">
        <h4 class="card-title">Government Property Form</h4>
      </div>
      <div class="dropdown show" style="display: inline-flex;margin-top: 10px; float: right;">
        <a class="btn btn-secondary dropdown-toggle" href="#" role="button" style="margin: 0; color: black;"
          id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          <i class="material-icons">settings</i>
        </a>
        <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
          <a class="dropdown-item" href="${path}?action=form">
            <i class="material-icons">add</i>
            Add New Government Property
          </a>
        </div>
      </div>
    </div>
    <div class="card-body">
      <table id="bootstrapFormTable" class="border-zero" style="width:100%;">
        <colgroup>
          <col width="200">
        </colgroup>
        <tbody>
        <tr>
          <td class="fieldName"><span class="regAsterisk">*</span> Date:</td>
          <td colspan="7">
            <html:text name="govPropertyForm" property="dateListedStr" maxlength="11" size="10" styleClass="form-control input-sm datepicker"/>
          </td>
        </tr>
        <tr>
          <td class="fieldName"><span class="regAsterisk">*</span> National Stock Number:</td>
          <td colspan="7">
            <html:text name="govPropertyForm" property="nationalStockNumber" maxlength="127" styleClass="form-control input-sm"/>
          </td>
        </tr>
        <tr>
          <td class="fieldName TOP">Description:</td>
          <td colspan="7">
            <html:textarea name="govPropertyForm" property="description" rows="4" styleClass="form-control input-sm"/>
          </td>
        </tr>
        <tr>
          <td class="fieldName">Project/Contract:</td>
          <td colspan="7">
            <html:text name="govPropertyForm" property="projectContract" maxlength="127" styleClass="form-control input-sm"/>
          </td>
        </tr>
        <tr>
          <td class="fieldName">Received:</td>
          <td>
            <html:text name="govPropertyForm" property="received" size="10" styleClass="form-control input-sm integer"/>
          </td>
          <td class="fieldName">Issued:</td>
          <td>
            <html:text name="govPropertyForm" property="issued" size="10" styleClass="form-control input-sm integer"/>
          </td>
          <td class="fieldName">Transferred:</td>
          <td>
            <html:text name="govPropertyForm" property="transferred" size="10" styleClass="form-control input-sm integer"/>
          </td>
          <td class="fieldName" style="white-space: nowrap;">On Hand:</td>
          <td>
            <html:text name="govPropertyForm" property="onHand" size="10" styleClass="form-control input-sm integer"/>
          </td>
        </tr>
        <tr>
          <td class="fieldName">Location:</td>
          <td colspan="7">
            <html:text name="govPropertyForm" property="location" maxlength="127" styleClass="form-control input-sm"/>
          </td>
        </tr>
        <tr>
          <td class="fieldName">Disposition:</td>
          <td colspan="7">
            <html:text name="govPropertyForm" property="disposition" maxlength="127" styleClass="form-control input-sm"/>
          </td>
        </tr>
        <tr>
          <td colspan="8" align="center">
            <logic:notEmpty name="govPropertyForm" property="id">
              <tags:lastUpdatedBy by="${govPropertyForm.lastUpdatedBy}" date="${govPropertyForm.lastUpdatedDateStr}"/>
            </logic:notEmpty>
  
            <html:submit value="Save" styleClass="btn btn-primary"/>
            <a class="btn btn-default" href="govProperty.do">Cancel</a>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</html:form>

<%@ include file="../../layout/old/layout-footer.jsp" %>

<script type="text/javascript" src="js/valdate.js"></script>
<script type="text/javascript" src="js/support/govPropertyForm.js"></script>
</body>
</html>

