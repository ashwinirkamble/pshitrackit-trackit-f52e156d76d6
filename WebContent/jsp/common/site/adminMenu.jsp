<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import ="java.time.LocalDate" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean"%>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html"%>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<bean:define id="pageTitle" value="Administration" />

<%@ include file="../../layout/old/layout-header.jsp"%>

<tags:adminBreadcrumb/>

<div class="card " style="margin-top:0">
  <div class="card-body ">
    <div class="row">
      <div class="col">
        <h3>System Administration</h3>
        <ul class="pshi-menu-list">
          <% if (request.isUserInRole("sysadmin")) { %>
            <li class="pshi-menu-list-item">
              <a class="nav-link" href="project.do?action=projectList">
                <i class="material-icons">settings</i>
                <span>Manage Projects</span>
              </a>
            </li>
            <li class="pshi-menu-list-item">
              <a class="nav-link" href="user.do?action=userList">
                <i class="material-icons">group</i>
                <span>Manage Users</span>
              </a>
            </li>
          <% } %>
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="ship.do?action=shipList">
              <i class="material-icons">directions_boat</i>
              <span>Manage UIC List</span>
            </a>
          </li>
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="govProperty.do">
              <i class="material-icons">list</i>
              <span>Government Property</span>
            </a>
          </li>
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="managedList.do">
              <i class="material-icons">settings</i>
              <span>Global System Variables</span>
            </a>
          </li>
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="contract.do">
              <i class="material-icons">assignment</i>
              <span>Manage Contracts</span>
            </a>
          </li>
	  
	   <%--Comment out Ashwini comment start%-->
          <%-- <li class="pshi-menu-list-item" style="${isDevEnv ? '' : 'display:none;'}">
            <a class="nav-link" href="statusReport.do">
              <i class="material-icons">assignment</i>
              <span>Status Reports</span>
            </a>
          </li> --%>
	  <%--Ashwini comment end--%>
        </ul>
      </div>
  
      <div class="col">
        <h3>User Administration</h3>
        <ul class="pshi-menu-list">
          <li class="pshi-menu-list-item">
            <a class="nav-link calendar-link" href="user.do?action=ptoTravelList">
              <i class="material-icons">calendar_today</i>
              <span class="day"><%=LocalDate.now().getDayOfMonth()%></span>
              <span class="link-text">Manage PTO/Travel</span>
            </a>
          </li>
          <li class="pshi-menu-list-item">
            <a class="nav-link" href="login.do?action=changePassword">
              <i class="material-icons">security</i>
              <span>Change Password</span>
            </a>
          </li>
          <% if (request.isUserInRole("sysadmin")) { %>
            <li class="pshi-menu-list-item">
              <a class="nav-link" href="user.do?action=userEdit&userPk=${loginBean.userPk}">
                <i class="material-icons">person</i>
                <span>Edit User Profile</span>
              </a>
            </li>
          <% } %>
        </ul>
      </div>
    </div>
  </div>
</div>

<%@ include file="../../layout/old/layout-footer.jsp"%>

<script type="text/javascript" src="js/jquery-altcolor.js"></script>
<script type="text/javascript" src="js/valdate.js"></script>
<script type="text/javascript"></script>
</body>
</html>

