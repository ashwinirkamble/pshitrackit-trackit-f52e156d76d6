<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="fn"    uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="pageTitle" value="Contract: ${contractForm.id == null ? '' : contractForm.contractNumber}"/>

<%@ include file="../../layout/old/layout-header.jsp" %>

<tags:adminBreadcrumb pageTitle="${pageTitle}" parentTitle="Manage Contracts" parentUrl="contract.do"/>

<div class="content">
  <div class="container-fluid">
    <div class="row">
      <div class="col-md-1"></div>
      <div class="col-md-10">
        <div class="card ">
          <%@ include file="../../include/report/contract/contractCardHeader.jsp" %>
          <div class="card-body pshi-form-body">
            <div class="row">
              <label for="contractNumber" class="col-sm-3 col-form-label">Contract Number:</label>
              <div class="col-sm-9 pshi-view-field">
                <logic:notEmpty name="contractForm" property="contractNumber">
                  ${contractForm.contractNumber}
                </logic:notEmpty>
                <logic:empty name="contractForm" property="contractNumber">-</logic:empty>
              </div>
            </div>
            <div class="row">
              <label for="name" class="col-sm-3 col-form-label">Name:</label>
              <div class="col-sm-9 pshi-view-field">
                <logic:notEmpty name="contractForm" property="name">
                  ${contractForm.name}
                </logic:notEmpty>
                <logic:empty name="contractForm" property="name">-</logic:empty>
              </div>
            </div>
            <div class="row">
              <label for="organizationFk" class="col-sm-3 col-form-label">Organization:</label>
              <div class="col-sm-9 pshi-view-field">
                <logic:notEmpty name="contractForm" property="organization">
                  ${contractForm.organization.name}
                </logic:notEmpty>
                <logic:empty name="contractForm" property="organization">-</logic:empty>
              </div>
            </div>
            <div class="row">
              <label for="startDate" class="col-sm-3 col-form-label">Start Date:</label>
              <div class="col-sm-3 pshi-view-field">
                <logic:notEmpty name="contractForm" property="startDate">
                  ${contractForm.startDateStr}
                </logic:notEmpty>
                <logic:empty name="contractForm" property="startDate">-</logic:empty>
              </div>
              <label for="endDate" class="col-sm-3 col-form-label">End Date:</label>
              <div class="col-sm-3 pshi-view-field">
                <logic:notEmpty name="contractForm" property="endDate">
                  ${contractForm.endDateStr}
                </logic:notEmpty>
                <logic:empty name="contractForm" property="endDate">-</logic:empty>
              </div>
            </div>
            <div class="row">
              <label for="notes" class="col-sm-3 col-form-label">Notes:</label>
              <div class="col-sm-9 pshi-view-field">
                <logic:notEmpty name="contractForm" property="notes">
                  ${contractForm.notes}
                </logic:notEmpty>
                <logic:empty name="contractForm" property="notes">-</logic:empty>
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
        </div>
      </div>
      <div class="col-md-1"></div>
    </div>
  </div>
</div>

<%@ include file="../../layout/old/layout-footer.jsp" %>

<script type="text/javascript" src="js/valdate.js"></script>
<script type="text/javascript" src="js/support/contract.js"></script>
</body>
</html>

