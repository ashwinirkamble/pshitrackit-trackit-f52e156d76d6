<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="fn"    uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="pageTitle" value="${statusReportForm.id == null ? 'Add' : 'Edit'} Status Report${statusReportForm.id == null ? '' : ': '.concat(statusReportForm.projectName)}"></bean:define>

<%@ include file="../../layout/old/layout-header.jsp" %>

<tags:adminBreadcrumb pageTitle="${pageTitle}" parentTitle="Manage Status Reports" parentUrl="statusReport.do"/>

<html:form method="POST" enctype="multipart/form-data" action="statusReport.do?action=save" styleId="statusReportForm"
  onsubmit="return validateStatusReport();">
  <html:hidden name="statusReportForm" property="createdByFk"/>
  <html:hidden name="statusReportForm" property="createdDateStr"/>
  <html:hidden name="statusReportForm" property="id"/>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-12">
          <div class="card ">
            <%@ include file="../../include/report/statusReport/statusReportCardHeader.jsp" %>
            <div class="card-body pshi-form-body">
            <div class="row">
              <label for="projectName" class="col-sm-3 col-form-label"><span class="required">*</span> Project Name:</label>
              <div class="col-sm-9">
                <html:text name="statusReportForm" property="projectName" styleClass="form-control" maxlength="127" styleId="projectName"/>
              </div>
            </div>
            <div class="row">
              <label class="col-sm-3 col-form-label"><span class="required">*</span> Report Type:</label>
              <div class="col-sm-9">
                <html:select name="statusReportForm" property="statusReportTypeCode" styleClass="form-control input-sm">
                  <html:options collection="statusReportTypeList" property="code" labelProperty="name"/>
                </html:select>
              </div>
            </div>
            <div class="row">
              <label for="contractFk" class="col-sm-3 col-form-label"><span class="required">*</span> Contract:</label>
              <div class="col-sm-9">
                <html:select name="statusReportForm" property="contractFk" styleClass="form-control input-sm">
                  <html:option value="">N/A</html:option>
                  <html:options collection="contractList" property="id" labelProperty="contractNumber"/>
                </html:select>
              </div>
            </div>
            <div class="row">
              <label for="reportStartDate" class="col-sm-3 col-form-label"><span class="required">*</span> Report Start Date:</label>
              <div class="col-sm-9">
                <html:text name="statusReportForm" property="reportStartDateStr" styleId="reportStartDate" maxlength="11" styleClass="form-control datepicker"/>
              </div>
            </div>
            <div class="row">
              <label for="reportEndDate" class="col-sm-3 col-form-label"><span class="required">*</span> Report End Date:</label>
              <div class="col-sm-9">
                <html:text name="statusReportForm" property="reportEndDateStr" styleId="reportEndDate" maxlength="11" styleClass="form-control datepicker"/>
              </div>
            </div>
            <div class="row">
              <label for="organizationFk" class="col-sm-3 col-form-label"><span class="required">*</span> Organization:</label>
              <div class="col-sm-9">
                <html:select name="statusReportForm" property="organizationFk" styleClass="form-control input-sm">
                  <html:option value="">N/A</html:option>
                  <html:options collection="organizationList" property="id" labelProperty="name"/>
                </html:select>
              </div>
            </div>
            <div class="row">
              <label for="contractorOrgFk" class="col-sm-3 col-form-label"><span class="required">*</span> Contractor:</label>
              <div class="col-sm-9">
                <html:select name="statusReportForm" property="contractorOrgFk" styleClass="form-control input-sm">
                  <html:option value="">N/A</html:option>
                  <html:options collection="organizationList" property="id" labelProperty="name"/>
                </html:select>
              </div>
            </div>
            <div class="row">
              <label for="name" class="col-sm-3 col-form-label">Name:</label>
              <div class="col-sm-9">
                <html:text styleClass="form-control" maxlength="127" styleId="name" property="name"/>
              </div>
            </div>
            <div class="row">
              <label for="projectManager" class="col-sm-3 col-form-label">Project Manager:</label>
              <div class="col-sm-9">
                <html:text styleClass="form-control" maxlength="127" styleId="projectManager" property="projectManager"/>
              </div>
            </div>
            <div class="row">
              <label for="contractingOfficerCotr" class="col-sm-3 col-form-label">COTR:</label>
              <div class="col-sm-9">
                <html:text styleClass="form-control" maxlength="127" styleId="contractingOfficerCotr" property="contractingOfficerCotr"/>
              </div>
            </div>
            <div class="row">
              <label for="contractingOfficerCor" class="col-sm-3 col-form-label">COR:</label>
              <div class="col-sm-9">
                <html:text styleClass="form-control" maxlength="127" styleId="contractingOfficerCor" property="contractingOfficerCor"/>
              </div>
            </div>
            <div class="row">
              <label for="objective" class="col-sm-3 col-form-label">Objective:</label>
              <div class="col-sm-9">
                <html:textarea name="statusReportForm" property="objective" styleId="objective" rows="4" styleClass="form-control"/>
              </div>
            </div>
            <div class="row" style="margin-top: 10px;">
              <label for="summary" class="col-sm-3 col-form-label">Summary:</label>
              <div class="col-sm-9">
                <html:textarea name="statusReportForm" property="summary" styleId="summary" rows="4" styleClass="form-control hidden"/>
                <div id="summaryEditor" style="height: 300px;"></div>
              </div>
            </div>
            <div class="row" style="margin-top: 10px;">
              <label class="col-sm-3 col-form-label">Contracts:</label>
              <div class="col-sm-9">
                <logic:iterate id="contract" name="contractList" type="com.premiersolutionshi.report.domain.Contract">
                  <input type="checkbox" name="contractFkArr" value="${contract.id}" />
                  ${contract.contractNumber} ${contract.name}
                  <br/>
                </logic:iterate>
              </div>
            </div>
          </div>
          <div class="row">
            <div class="col">
              <logic:notEmpty name="statusReportForm" property="id">
                <tags:lastUpdatedBy by="${statusReportForm.lastUpdatedBy}" date="${statusReportForm.lastUpdatedDateStr}"/>
              </logic:notEmpty>
            </div>
          </div>
          <div class="card-footer ml-auto mr-auto">
            <html:submit value="Save" styleClass="btn btn-fill btn-primary" onclick="return onSaveStatusReport();"/>
            <a class="btn btn-default" href="statusReport.do">Cancel</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</html:form>

<%@ include file="../../layout/old/layout-footer.jsp" %>

<script type="text/javascript" src="js/valdate.js"></script>
<script type="text/javascript" src="js/report/statusReport.js"></script>

<link rel="stylesheet" href="js/quilljs/quill.snow.1.3.6.css">
<script type="text/javascript" src="js/quilljs/quill.1.3.6.min.js"></script>
<script>
  var quill = new Quill('#summaryEditor', { theme: 'snow' });
</script>
</body>
</html>

