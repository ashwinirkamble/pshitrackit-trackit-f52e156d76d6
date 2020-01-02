<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="fn"    uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="pageTitle" value="POC${pocForm.id == null ? ' - Add New' : ': '.concat(pocForm.fullName)}"/>

<%@ include file="../../layout/old/layout-header.jsp" %>

<logic:notEmpty name="pocForm" property="organization">
  <tags:projectBreadcrumb project="${project}" pageTitle="${pageTitle}"
    parentTitle="POC List" parentUrl="organization.do?projectPk=${project.id}"
    parent2Title="${pocForm.organization.name}"
    parent2Url="organization.do?action=view&projectPk=${project.id}&id=${pocForm.organization.id}"
  />
</logic:notEmpty>
<logic:notEmpty name="pocForm" property="ship">
  <tags:projectBreadcrumb project="${project}" pageTitle="${pageTitle}"
    parentTitle="POC List" parentUrl="organization.do?projectPk=${project.id}"
    parent2Title="${pocForm.ship.shipName}"
    parent2Url="organization.do?action=unitPocList&projectPk=${project.id}&shipFk=${pocForm.ship.id}"
  />
</logic:notEmpty>

<%@ include file="../../include/support/poc/pocSearchBar.jsp" %>

<html:form method="POST" enctype="multipart/form-data" action="poc.do" styleId="pocForm">
<div class="content">
  <div class="container-fluid">
    <div class="row">
      <div class="col-md-1"></div>
      <div class="col-md-10">
        <div class="card">
          <%@ include file="../../include/support/poc/pocCardHeader.jsp" %>
          <div class="card-body pshi-form-body">
            <div class="row">
              <logic:notEmpty name="pocForm" property="organization">
                <label class="col-sm-3 col-form-label">Organization:</label>
                <div class="col-sm-9 pshi-view-field">
                  <a href="organization.do?action=view&projectPk=${projectPk}&id=${pocForm.organization.id}"
                    title="View Organization POCs">
                    ${pocForm.organization.name}
                  </a>
                </div>
              </logic:notEmpty>
              <logic:notEmpty name="pocForm" property="ship">
                <label class="col-sm-3 col-form-label">Unit:</label>
                <div class="col-sm-9 pshi-view-field">
                  <tags:shipPocLink value="${pocForm.ship}"/>
                </div>
              </logic:notEmpty>
            </div>
            <div class="row">
              <label for="lastName" class="col-sm-3 col-form-label">Name:</label>
              <div class="col-sm-9 pshi-view-field">
                ${pocForm.firstName} ${pocForm.lastName}
                ${pocForm.primaryPoc ? '<i class="material-icons" title="Primary POC" style="color:green;font-size:16px;">check_circle</i>' : ''}
              </div>
            </div>
            <div class="row">
              <label for="title" class="col-sm-3 col-form-label">Title:</label>
              <div class="col-sm-9 pshi-view-field">
                ${pocForm.title}
                <logic:empty name="pocForm" property="title">-</logic:empty>
              </div>
            </div>
            <div class="row">
              <label for="dept" class="col-sm-3 col-form-label">DIV:</label>
              <div class="col-sm-3 pshi-view-field">
                ${pocForm.dept}
                <logic:empty name="pocForm" property="dept">-</logic:empty>
              </div>
              <label for="rank" class="col-sm-3 col-form-label">Rank:</label>
              <div class="col-sm-3 pshi-view-field">
                ${pocForm.rank}
                <logic:empty name="pocForm" property="rank">-</logic:empty>
              </div>
            </div>
            <div class="row">
              <label for="email" class="col-sm-3 col-form-label">Email:</label>
              <div class="col-sm-9 pshi-view-field">
                <tags:emailLink value="${pocForm.email}"/>
                <logic:empty name="pocForm" property="email">-</logic:empty>
              </div>
            </div>
            <div class="row">
              <label for="altEmail" class="col-sm-3 col-form-label">Alt. Email:</label>
              <div class="col-sm-9 pshi-view-field">
                <tags:emailLink value="${pocForm.altEmail}"/>
                <logic:empty name="pocForm" property="altEmail">-</logic:empty>
              </div>
            </div>
            <div class="row">
              <label for="workNumber" class="col-sm-3 col-form-label">Work Number:</label>
              <div class="col-sm-3 pshi-view-field">
                ${pocForm.workNumber}
                <logic:empty name="pocForm" property="workNumber">-</logic:empty>
                <logic:notEmpty name="pocForm" property="workNumberExt">
                  x${pocForm.workNumberExt}
                </logic:notEmpty>
              </div>
              <label for="faxNumber" class="col-sm-3 col-form-label">Fax Number:</label>
              <div class="col-sm-3 pshi-view-field">
                ${pocForm.faxNumber}
                <logic:empty name="pocForm" property="faxNumber">-</logic:empty>
              </div>
            </div>
            <div class="row">
              <label for="cellNumber" class="col-sm-3 col-form-label">Cell Number:</label>
              <div class="col-sm-9 pshi-view-field">
                ${pocForm.cellNumber}
                <logic:empty name="pocForm" property="cellNumber">-</logic:empty>
              </div>
            </div>
            <div class="row">
              <label for="notes" class="col-sm-3 col-form-label">Notes:</label>
              <div class="col-sm-9 pshi-view-field">
                ${pocForm.notes.replaceAll('\\n', '<br/>')}
                <logic:empty name="pocForm" property="notes">-</logic:empty>
              </div>
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
      </div>
      <div class="col-md-1"></div>
    </div>
  </div>
</div>
</html:form>
<%@ include file="../../layout/old/layout-footer.jsp" %>

<script type="text/javascript" src="js/valdate.js"></script>
<script type="text/javascript" src="js/support/poc/pocForm.js"></script>
<script type="text/javascript" src="js/support/unit/showUnitPopup.js"></script>
</body>
</html>

