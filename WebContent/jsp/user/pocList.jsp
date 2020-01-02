<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="defaultPageTitle" value="POC List"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/>
<logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>

<jsp:useBean id="inputBean" scope="request" class="com.premiersolutionshi.old.bean.UserBean"/>

<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>TrackIT - ${pageTitle}</title>

  <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/stylesheet.css"/>
  <link rel="stylesheet" type="text/css" href="css/jquery-ui.min.css"/>
  <link rel="stylesheet" type="text/css" href="js/jquery/datatables/jquery.dataTables.min.css"/>
  <style type="text/css">td.highlight { background-color: whitesmoke !important; }</style>
</head>

<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask leftmenu"><div class="colright">
  <div class="col1wrap"><div class="col1">
    <%@ include file="../include/content-header.jsp" %>

    <tags:projectPkBreadcrumb projectPk="${projectPk}" pageTitle="${pageTitle}" hideOptions="true" />

    <div id="searchForm" class="panel panel-primary">
      <div class="panel-heading" data-toggle="collapse" data-target=".poc-search-panel">
        <h3 class="panel-title">
        <a class="accordion-toggle">Search</a>
        <i class="indicator glyphicon glyphicon-chevron-down pull-right"></i>
        </h3>
      </div>
      <div class="poc-search-panel collapse" style="padding:10px;">
        <form action="user.do" method="GET">
          <input type="hidden" name="action" value="pocList"/>
          <input type="hidden" name="projectPk" value="${projectPk}"/>
          <input type="hidden" name="searchPerformed" value="Y"/>
          <div class="center">
            <table id="bootstrapFormTable" class="border-zero">
              <tbody>
                <tr>
                  <td class="fieldName"><label for="title">Last Name</label></td>
                  <td><html:text name="inputBean" property="lastName" styleId="lastName" styleClass="form-control input-sm"/></td>
                  <td class="fieldName"><label for="title">First Name</label></td>
                  <td><html:text name="inputBean" property="firstName" styleId="firstName" styleClass="form-control input-sm"/></td>
                </tr>
                <tr>
                  <td class="fieldName"><label for="title">Unit/Organization</label></td>
                  <td><html:text name="inputBean" property="organization" styleId="organization" styleClass="form-control input-sm"/></td>
                </tr>
              </tbody>
            </table>
          </div>
          <div class="center CENTERED">
            <button type="submit" class="btn btn-primary">
              <span class="glyphicon glyphicon-search"></span> Search
            </button>
            <a href="user.do?action=pocList&projectPk=${projectPk}&searchPerformed=Y" class="btn btn-default">
              <span class="glyphicon glyphicon-list-alt"></span> View All
            </a>
          </div>
        </form>
      </div>
    </div>

    <p>
      <a href="user.do?action=pocAdd&projectPk=${projectPk}" class="btn btn-success">
        <span class="glyphicon glyphicon-plus"></span> Add New ${projectBean.projectName} POC
      </a>
    <p>

    <div id="tabs">
      <ul>
        <logic:notEmpty name="employeeList">
        <logic:iterate id="organizationBean" name="employeeList" type="com.premiersolutionshi.old.bean.OrganizationBean" indexId="i">
          <li><a href="#tab-employee-${i}" onclick="applyDataTable('employee-poc-table-${i}')">${organizationBean.organization} Employees</a></li>
        </logic:iterate>
        </logic:notEmpty>

        <logic:notEmpty name="resultList">
        <logic:iterate id="organizationBean" name="resultList" type="com.premiersolutionshi.old.bean.OrganizationBean" indexId="i">
          <li><a href="#tab-poc-${i}" onclick="applyDataTable('poc-table-${i}')">${organizationBean.organization}</a></li>
        </logic:iterate>
        </logic:notEmpty>

        <li><a href="#tab-ships">Unit POCs</a></li>
      </ul>

      <logic:notEmpty name="employeeList">
      <logic:iterate id="organizationBean" name="employeeList" type="com.premiersolutionshi.old.bean.OrganizationBean" indexId="i">
        <div id="tab-employee-${i}">
          <div class="center">
          ${organizationBean.organization} Employees
          <table class="employee-poc-table-${i} hover">
          <thead>
            <tr>
              <th>Last Name</th>
              <th>First Name</th>
              <th>Job Title</th>
              <th>Email</th>
              <th>Work Number</th>
              <th>Quick<br/>Dial</th>
              <th>Fax Number</th>
              <th>Cell Number</th>
              <th>Last Updated By</th>
              <th>Last Updated Date</th>
            </tr>
          </thead>
          <tbody>
            <logic:iterate id="resultBean" name="organizationBean" property="pocList" type="com.premiersolutionshi.old.bean.UserBean">
              <tr>
                <td height="20">${resultBean.lastName}</td>
                <td>${resultBean.firstName}</td>
                <td>${resultBean.title}</td>
                <td><a href="mailto:${resultBean.email}">${resultBean.email}</a></td>
                <td align="center" class="nowrap">${resultBean.workNumber}</td>
                <td align="center">${resultBean.quickDial}</td>
                <td align="center" class="nowrap">${resultBean.faxNumber}</td>
                <td align="center" class="nowrap">${resultBean.cellNumber}</td>
                <td align="center">${resultBean.lastUpdatedBy}</td>
                <td align="center"><tags:formatDateTime value="${resultBean.lastUpdatedDate}" pattern="YYYY-MM-dd"/></td>
              </tr>
            </logic:iterate>
            <%--<tr class="ignore"><td class="NOBORDER">&nbsp;</td></tr> --%>
          </tbody>
          </table>
          </div>
        </div>
      </logic:iterate>
      </logic:notEmpty>

      <logic:notEmpty name="resultList">
      <logic:iterate id="organizationBean" name="resultList" type="com.premiersolutionshi.old.bean.OrganizationBean" indexId="i">
        <div id="tab-poc-<%= i %>">
          <p>
            ${organizationBean.organization}
            <a href="mailto:${organizationBean.allPocEmails}">
              <img src="images/icon_email_send.png" height="15" style="width:15px"/> E-Mail All
            </a>
          </p>

          <div class="center">
          <table class="poc-table-${i} hover">
            <thead>
              <tr>
                <th>Rank</th>
                <th>Last Name</th>
                <th>First Name</th>
                <th>Job Title</th>
                <th>Email</th>
                <th>Work Number</th>
                <th>Fax Number</th>
                <th>Cell Number</th>
                <th>Last Updated By</th>
                <th>Last Updated Date</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
            <logic:iterate id="resultBean" name="organizationBean" property="pocList" type="com.premiersolutionshi.old.bean.UserBean">
              <tr>
                <td>${resultBean.rank}</td>
                <td>${resultBean.lastName}</td>
                <td>${resultBean.firstName}</td>
                <td>${resultBean.title}</td>
                <td><a href="mailto:${resultBean.email}">${resultBean.email}</a></td>
                <td align="center" class="nowrap">${resultBean.workNumber}</td>
                <td align="center" class="nowrap">${resultBean.faxNumber}</td>
                <td align="center" class="nowrap">${resultBean.cellNumber}</td>
                <td align="center">${resultBean.lastUpdatedBy}</td>
                <td align="center"><tags:formatDateTime value="${resultBean.lastUpdatedDate}" pattern="YYYY-MM-dd"/></td>
                <td align="center" nowrap>
                  <a href="user.do?action=pocEdit&pocPk=${resultBean.pocPk}&projectPk=${projectPk}"><img src="images/icon_edit.png" title="Edit"/></a>
                  &nbsp;<a href="user.do?action=pocDeleteDo&pocPk=${resultBean.pocPk}&projectPk=${projectPk}" onclick="return confirmDeletePoc('${resultBean.lastNameJs}', '${resultBean.firstNameJs}');"><img src="images/icon_delete.png" title="Delete"/></a>
                </td>
              </tr>
            </logic:iterate>
            <%--<tr class="ignore"><td class="NOBORDER">&nbsp;</td></tr> --%>
            </tbody>
          </table>
          </div>
        </div>
      </logic:iterate>
      </logic:notEmpty>

      <div id="tab-ships">
        <p align="center">
          <a href="user.do?action=shipPocAdd&projectPk=${projectPk}"><span class="glyphicon glyphicon-plus"></span> Add New Unit POC</a>
        </p>

        <logic:notEmpty name="shipList">
        <p align="center">
          <a href="export.do?action=unitContactXlsx&projectPk=${projectPk}" class="btn btn-default"><img src="images/file_icons/sml_file_xls.gif"/> Export to Excel</a>
        </p>

        <div id="accordion">
          <logic:iterate id="shipBean" name="shipList" type="com.premiersolutionshi.old.bean.ShipBean" indexId="j">
          <h3 style="text-align:left;" onclick="applyDataTable('ship-poc-table-${shipBean.shipPk}')">
            <a title="${shipBean.uic}">
              ${shipBean.shipNameTypeHull}
            </a>
          </h3>
          <div id="accordion-tab-${shipBean.shipPk}">
            <div class="center">
            <b>${shipBean.shipName} POCs</b>
            <a href="mailto:${shipBean.allPocEmails}">
              <img src="images/icon_email_send.png" height="15" style="width:15px"/> E-Mail All
            </a>
            <a href="mailto:${shipBean.primaryPocEmails}">
              <img src="images/icon_email_send.png" height="15" style="width:15px"/> E-Mail Primary POCs
            </a>
            <a href="user.do?action=shipPocAdd&shipPk=${shipBean.shipPk}&projectPk=${projectPk}">
              <span class="glyphicon glyphicon-plus"></span> Add New POC
            </a>
            <table class="ship-poc-table-${shipBean.shipPk} hover">
            <thead>
              <tr>
                <th>Primary?</th>
                <th>Rank</th>
                <th>Last Name</th>
                <th>First Name</th>
                <th>Job Title</th>
                <th>Division</th>
                <th>Email</th>
                <th>Work Number</th>
                <th>Cell/Alt Number</th>
                <th>Last Updated By</th>
                <th>Last Updated Date</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
            <logic:iterate id="resultBean" name="shipBean" property="pocList" type="com.premiersolutionshi.old.bean.UserBean">
              <tr>
                <td align="center">
                  <logic:equal name="resultBean" property="isPrimaryPoc" value="Y">
                    <img src="images/icon_success.png" height="12"/>
                  </logic:equal>
                </td>
                <td>${resultBean.rank}</td>
                <td>${resultBean.lastName}</td>
                <td>${resultBean.firstName}</td>
                <td>${resultBean.title}</td>
                <td>${resultBean.dept}</td>
                <td>
                  <logic:notEmpty name="resultBean" property="email"><a href="mailto:${resultBean.email}">${resultBean.email}</a></logic:notEmpty>
                  <logic:notEmpty name="resultBean" property="altEmail"><br/><a href="mailto:${resultBean.altEmail}">${resultBean.altEmail}</a></logic:notEmpty>
                </td>
                <td align="center" class="nowrap">${resultBean.workNumber}</td>
                <td align="center" class="nowrap">${resultBean.cellNumber}</td>
                <td align="center">${resultBean.lastUpdatedBy}</td>
                <td align="center"><tags:formatDateTime value="${resultBean.lastUpdatedDate}" pattern="YYYY-MM-dd"/></td>
                <td align="center" nowrap>
                  <a href="user.do?action=shipPocEdit&shipPocPk=${resultBean.shipPocPk}&projectPk=${projectPk}"><img src="images/icon_edit.png" title="Edit"/></a>
                  &nbsp;<a href="user.do?action=shipPocDeleteDo&shipPocPk=${resultBean.shipPocPk}&projectPk=${projectPk}" onclick="return confirmDeletePoc('${resultBean.lastNameJs}', '${resultBean.firstNameJs}');"><img src="images/icon_delete.png" title="Delete"/></a>
                </td>
              </tr>
            </logic:iterate>
            </tbody>
            </table>
            </div>
          </div>
          </logic:iterate>
          </div> <!-- accordion -->
        </logic:notEmpty>
      </div> <!-- tab-ship -->

    </div> <!-- tabs -->
  </div></div>

  <%@ include file="../include/app-col2.jsp" %>
</div></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-altcolor.js"></script>

<% String shipPk = CommonMethods.nes(request.getParameter("shipPk")); %>
<script type="text/javascript">
var shipPk = "<%=shipPk%>";
</script>
<script type="text/javascript" src="js/support/poc/pocList.js"></script>

</body>
</html>
