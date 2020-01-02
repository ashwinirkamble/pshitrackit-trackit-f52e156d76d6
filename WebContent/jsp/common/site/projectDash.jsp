<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import ="java.time.LocalDate" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean"%>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html"%>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<bean:define id="pageTitle" value="${project.projectName} Dashboard" />

<%@ include file="../../layout/old/layout-header.jsp"%>

<tags:projectBreadcrumb project="${project}"/>

<div class="card " style="margin-top:0">
<div class="card-body ">
  <div class="row">
    <div class="col" style="padding-top:12px;">
      <%@ include file="../../include/support/issue/jumpToIssuePk.jsp" %>
    </div>
    <div class="col" style="padding-top:12px;">
      <%@ include file="../../include/support/task/jumpToTaskPk.jsp" %>
    </div>
    <div class="col">
      <%@ include file="../../include/support/poc/pocSearchBox.jsp" %>
    </div>
  </div>
  <div class="row">
    <div class="col">
      <h3>Support</h3>
      <ul class="pshi-menu-list">
        <logic:equal name="project" property="id" value="1">
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="atoUpdate.do?action=list&projectPk=${projectPk}">
              <i class="material-icons">contact_support</i>
              <span>ATO Updates</span>
            </a>
          </li>
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="bulkIssue.do?projectPk=${projectPk}">
              <i class="material-icons">contact_mail</i>
              <span>Bulk E-mail Tool</span>
            </a>
          </li>
          <li class="pshi-menu-list-item">
            <a class="calendar-link" href="support.do?action=shipVisitCalendar&projectPk=${projectPk}">
              <i class="material-icons">calendar_today</i>
              <span class="day"><%=LocalDate.now().getDayOfMonth()%></span>
              <span class="link-text">Support Calendar</span>
            </a>
          </li>
        </logic:equal>
        <li class="pshi-menu-list-item">
          <a class="nav-link" href="organization.do?projectPk=${projectPk}">
            <i class="material-icons">import_contacts</i>
            <span>POC List</span>
          </a>
        </li>
        <li class="pshi-menu-list-item">
          <a class="nav-link" href="support.do?action=issueList&projectPk=${projectPk}">
            <i class="material-icons">contact_support</i>
            <span>Current Issues <span id="issueCnt" class="badge badge-pill badge-primary issueCnt">0</span></span>
          </a>
        </li>
        <logic:equal name="project" property="id" value="1">
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="support.do?action=myIssueList&projectPk=${projectPk}">
              <i class="material-icons">contact_support</i>
              <span>My Issues <span id="myIssueCnt" class="badge badge-pill badge-primary myIssueCnt">0</span></span>
            </a>
          </li>
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="support.do?action=issueListAll&projectPk=${projectPk}">
              <i class="material-icons">contact_support</i>
              <span>All Issues</span>
            </a>
          </li>
        </logic:equal>
        <li class="pshi-menu-list-item">
          <a class="nav-link" href="managedList.do?projectPk=${projectPk}">
            <i class="material-icons">settings</i>
            <span>System Variables</span>
          </a>
        </li>
        <li class="pshi-menu-list-item">
          <a class="nav-link" href="project.do?action=taskList&projectPk=${projectPk}">
            <i class="material-icons">assignment_turned_in</i>
            <span>Task List</span>
          </a>
        </li>
      </ul>
    </div>

    <logic:equal name="project" property="id" value="1">
      <div class="col">
        <h3>DACS Reports</h3>
        <ul class="pshi-menu-list">
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="dts.do?action=dtsUpload&projectPk=${projectPk}">
              <i class="material-icons">assignment</i>
              <span>Upload DACS Reports</span>
            </a>
          </li>
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="report.do?action=missingTransmittal&projectPk=${projectPk}">
              <i class="material-icons">assignment</i>
              <span>Missing Transmittals</span>
            </a>
          </li>
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="report.do?action=transmittalSummary&projectPk=${projectPk}">
              <i class="material-icons">assignment</i>
              <span>Transmittal Summary</span>
            </a>
          </li>
        </ul>
        <h3>Workflows</h3>
        <ul class="pshi-menu-list">
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="backfile.do?action=workflowSummary&projectPk=${projectPk}">
              <i class="material-icons">near_me</i>
              <span>Backfile Workflow</span>
            </a>
          </li>
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="training.do?action=workflowSummary&projectPk=${projectPk}">
              <i class="material-icons">near_me</i>
              <span>Training Workflow</span>
            </a>
          </li>
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="decom.do?action=workflowSummary&projectPk=${projectPk}">
              <i class="material-icons">near_me</i>
              <span>Decom Workflow</span>
            </a>
          </li>
        </ul>
      </div>

      <div class="col">
        <h3>FACET Hardware</h3>
        <ul class="pshi-menu-list">
          <li class="pshi-menu-list-item">
            <a href="system.do?action=configuredSystemList&projectPk=${projectPk}">
              <i class="material-icons">laptop_windows</i>
              <span>Configured Systems</span>
            </a>
          </li>
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="hardware.do?action=laptopList&projectPk=${projectPk}">
              <i class="material-icons">laptop_windows</i>
              <span>Laptops</span>
            </a>
          </li>
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="hardware.do?action=scannerList&projectPk=${projectPk}">
              <i class="material-icons">scanner</i>
              <span>Scanners</span>
            </a>
          </li>
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="hardware.do?action=miscList&projectPk=${projectPk}">
              <i class="material-icons">developer_board</i>
              <span>Misc Hardware</span>
            </a>
          </li>
        </ul>
        <h3>FACET Software</h3>
        <ul class="pshi-menu-list">
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="software.do?action=kofaxLicenseList&projectPk=${projectPk}">
              <i class="material-icons">dns</i>
              <span>Kofax Licenses</span>
            </a>
          </li>
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="software.do?action=bulkKofaxLicenseEdit&projectPk=${projectPk}">
              <i class="material-icons">dns</i>
              <span>Bulk Kofax Expiration</span>
            </a>
          </li>
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="software.do?action=vrsLicenseList&projectPk=${projectPk}">
              <i class="material-icons">dns</i>
              <span>VRS Licenses</span>
            </a>
          </li>
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="software.do?action=msOfficeLicenseList&projectPk=${projectPk}">
              <i class="material-icons">dns</i>
              <span>MS Office Licenses</span>
            </a>
          </li>
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="software.do?action=miscLicenseList&projectPk=${projectPk}">
              <i class="material-icons">dns</i>
              <span>Misc Software</span>
            </a>
          </li>
        </ul>
      </div>
    </logic:equal>
  </div>
</div>
</div>

<%@ include file="../../layout/old/layout-footer.jsp"%>

<script type="text/javascript" src="js/jquery-altcolor.js"></script>
<script type="text/javascript" src="js/valdate.js"></script>
<script type="text/javascript"></script>
</body>
</html>
