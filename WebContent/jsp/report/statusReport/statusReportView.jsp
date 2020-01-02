<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="fn"    uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="pageTitle" value="Status Report${statusReportForm.id == null ? '' : ': '.concat(statusReportForm.projectName)}"/>

<%@ include file="../../layout/old/layout-header.jsp" %>

<tags:adminBreadcrumb pageTitle="${pageTitle}" parentTitle="Manage Status Reports" parentUrl="statusReport.do"/>

<div class="content">
  <div class="container-fluid">
    <div class="row">
      <div class="col-md-12">
        <div class="card ">
          <%@ include file="../../include/report/statusReport/statusReportCardHeader.jsp" %>
          <div class="card-body pshi-form-body">
            <div class="row">
              <label for="projectName" class="col-sm-2 col-form-label">Project Name:</label>
              <div class="col-sm-10 pshi-view-field">
                <logic:notEmpty name="statusReportForm" property="projectName">
                  ${statusReportForm.projectName}
                </logic:notEmpty>
                <logic:empty name="statusReportForm" property="projectName">-</logic:empty>
              </div>
            </div>
            <div class="row">
              <label for="statusReportTypeCode" class="col-sm-2 col-form-label">Report Type:</label>
              <div class="col-sm-10 pshi-view-field">
                <logic:notEmpty name="statusReportForm" property="statusReportType">
                  ${statusReportForm.statusReportType.name}
                </logic:notEmpty>
                <logic:empty name="statusReportForm" property="statusReportTypeCode">-</logic:empty>
              </div>
            </div>
            <div class="row">
              <label for="contractFk" class="col-sm-2 col-form-label">Contract:</label>
              <div class="col-sm-4 pshi-view-field">
                <logic:notEmpty name="statusReportForm" property="contract">
                  ${statusReportForm.contract.contractNumber}
                </logic:notEmpty>
                <logic:empty name="statusReportForm" property="contract">-</logic:empty>
              </div>
              <label for="reportStartDate" class="col-sm-2 col-form-label">Date:</label>
              <div class="col-sm-4 pshi-view-field">
                ${statusReportForm.reportStartDateStr} - ${statusReportForm.reportEndDateStr}
              </div>
            </div>
            <div class="row">
              <label class="col-sm-2 col-form-label">Organization:</label>
              <div class="col-sm-4 pshi-view-field">
                <tags:organizationDisplay value="${statusReportForm.organization}"/>
              </div>
              <label class="col-sm-2 col-form-label">Contractor:</label>
              <div class="col-sm-4 pshi-view-field">
                <tags:organizationDisplay value="${statusReportForm.contractorOrg}"/>
              </div>
            </div>
            <div class="row">
              <label for="name" class="col-sm-2 col-form-label">Name:</label>
              <div class="col-sm-4 pshi-view-field">
                <logic:notEmpty name="statusReportForm" property="name">
                  ${statusReportForm.name}
                </logic:notEmpty>
                <logic:empty name="statusReportForm" property="name">-</logic:empty>
              </div>
              <label for="projectManager" class="col-sm-2 col-form-label">Project Manager:</label>
              <div class="col-sm-4 pshi-view-field">
                <logic:notEmpty name="statusReportForm" property="projectManager">
                  ${statusReportForm.projectManager}
                </logic:notEmpty>
                <logic:empty name="statusReportForm" property="projectManager">-</logic:empty>
              </div>
            </div>
            <div class="row">
              <label for="contractingOfficerCotr" class="col-sm-2 col-form-label">COTR:</label>
              <div class="col-sm-4 pshi-view-field">
                <logic:notEmpty name="statusReportForm" property="contractingOfficerCotr">
                  ${statusReportForm.contractingOfficerCotr}
                </logic:notEmpty>
                <logic:empty name="statusReportForm" property="contractingOfficerCotr">-</logic:empty>
              </div>
              <label for="contractingOfficerCor" class="col-sm-2 col-form-label">COR:</label>
              <div class="col-sm-4 pshi-view-field">
                <logic:notEmpty name="statusReportForm" property="contractingOfficerCor">
                  ${statusReportForm.contractingOfficerCor}
                </logic:notEmpty>
                <logic:empty name="statusReportForm" property="contractingOfficerCor">-</logic:empty>
              </div>
            </div>
            <div class="row">
              <label for="objective" class="col-sm-2 col-form-label">Objective:</label>
              <div class="col-sm-10 pshi-view-field">
                <logic:notEmpty name="statusReportForm" property="objective">
                  ${statusReportForm.objective.replaceAll("\\n", "<br/>")}
                </logic:notEmpty>
                <logic:empty name="statusReportForm" property="objective">-</logic:empty>
              </div>
            </div>
            <div class="row">
              <label for="summary" class="col-sm-2 col-form-label">Summary:</label>
              <div class="col-sm-10 pshi-view-field">
                <logic:notEmpty name="statusReportForm" property="summary">
                  ${statusReportForm.summary}
                </logic:notEmpty>
                <logic:empty name="statusReportForm" property="summary">-</logic:empty>
              </div>
            </div>
            <div class="row">
              <label class="col-sm-2 col-form-label">Contracts:</label>
              <div class="col-sm-10 pshi-view-field">
                <bean:define id="statusReportContractList" name="statusReportForm" property="contractList"/>
                statusReportContractList.size()=${statusReportContractList.size()}
                <logic:iterate id="contract" name="statusReportContractList" indexId="index"
                  type="com.premiersolutionshi.report.domain.Contract">
                  <logic:greaterEqual value="0" parameter="index">,</logic:greaterEqual>
                  #{contract.contractNumber}
                </logic:iterate>
                <logic:empty name="statusReportForm" property="contractList">-</logic:empty>
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
        </div>
      </div>
    </div>
  </div>
</div>

<%@ include file="../../layout/old/layout-footer.jsp" %>

<script type="text/javascript" src="js/valdate.js"></script>
<script type="text/javascript" src="js/support/statusReportForm.js"></script>
</body>
</html>

