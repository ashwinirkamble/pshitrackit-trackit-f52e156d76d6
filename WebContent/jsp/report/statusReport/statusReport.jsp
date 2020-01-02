<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean"%>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html"%>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<bean:define id="pageTitle" value="Status Reports" />

<%@ include file="../../layout/old/layout-header.jsp"%>

<tags:adminBreadcrumb pageTitle="${pageTitle}"/>

<div class="card" style="margin-top:0;">
  <div class="container">
    <div class="row">
      <div class="col">
        <h3>Monthly Reports</h3>
        <ul class="pshi-menu-list">
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="statusReport.do?action=monthlyBusinessVolume">
              <i class="material-icons">content_paste</i>
              <span>Business Volume Report</span>
            </a>
          </li>
        </ul>
      </div>
      <div class="col">
        <h3>Weekly Reports</h3>
        <ul class="pshi-menu-list">
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="/pshi-tracker-design/examples/dashboard.php">
              <i class="material-icons">dashboard</i>
              <span>Weekly Status Report</span>
            </a>
          </li>
        </ul>
      </div>
    </div>
  </div>
</div>

<%@ include file="../../layout/old/layout-footer.jsp"%>
</body>
</html>
