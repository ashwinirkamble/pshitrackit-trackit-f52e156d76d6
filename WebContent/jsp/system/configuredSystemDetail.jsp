<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="defaultPageTitle" value="FACET Configured System Details"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk"      scope="request" class="java.lang.String"/>
<jsp:useBean id="resultBean"     scope="request" class="com.premiersolutionshi.old.bean.SystemBean"/>
<jsp:useBean id="backfileBean"   scope="request" class="com.premiersolutionshi.old.bean.BackfileBean"/>
<jsp:useBean id="trainingBean"   scope="request" class="com.premiersolutionshi.old.bean.TrainingBean"/>
<jsp:useBean id="lastVisitBean" scope="request" class="com.premiersolutionshi.old.bean.SupportBean"/>

<jsp:useBean id="configuredSystemList" scope="request" class="java.util.ArrayList"/>

<jsp:useBean id="currFacetVersion" scope="request" class="java.lang.String"/>
<jsp:useBean id="currOsVersion" scope="request" class="java.lang.String"/>
<jsp:useBean id="currDmsVersion"   scope="request" class="java.lang.String"/>

<jsp:useBean id="currStr"    scope="request" class="java.lang.String"/>
<jsp:useBean id="prevBean"  scope="request" class="com.premiersolutionshi.old.bean.SystemBean"/>
<jsp:useBean id="nextBean"  scope="request" class="com.premiersolutionshi.old.bean.SystemBean"/>

<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>TrackIT - ${pageTitle}</title>

  <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/stylesheet.css"/>
  <link rel="stylesheet" type="text/css" href="css/jquery-ui.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/jquery-popup.css"/>

  <style>
    #backfile-table th { vertical-align: bottom; }
    #backfile-table td { border: 1px solid #E1EAF0; font-size: 11px; text-align:center; }

    #training-table th { vertical-align: bottom; }
    #training-table td { border: 1px solid #E1EAF0; font-size: 11px; text-align:center; }

    #local-sub-table tr:first-child td:first-child { background: #64E986; }
  </style>
</head>

<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask leftmenu"><div class="colright">
  <div class="col1wrap"><div class="col1">
    <%@ include file="../include/content-header.jsp" %>
<%--ashwini system.do to ./system.do --%>
    <tags:projectPkBreadcrumb projectPk="${projectPk}" pageTitle="${pageTitle}" hideOptions="true"
      parentUrl="./system.do?action=configuredSystemList&projectPk=${projectPk}" parentTitle="FACET Configured System List"/>

    <div class="center">
    <table id="borderlessTable" class="border-zero cellspacing-zero">
    <tbody>
      <tr>
        <td align="left" style="width:16px" nowrap>
          <% if (prevBean != null && !CommonMethods.isEmpty(prevBean.getConfiguredSystemPk())) { %>
            <a href="system.do?action=configuredSystemDetail&configuredSystemPk=${prevBean.configuredSystemPk}&projectPk=${projectPk}">
            <img src="images/arrow_left.png" style="width:16px"/></a>
          <% } %>
        </td>
        <td align="center" nowrap>${currStr}</td>
        <td align="right" style="width:16px" nowrap>
          <% if (nextBean != null && !CommonMethods.isEmpty(nextBean.getConfiguredSystemPk())) { %>
            <a href="system.do?action=configuredSystemDetail&configuredSystemPk=${nextBean.configuredSystemPk}&projectPk=${projectPk}">
              <img src="images/arrow_right.png" height="16" style="width:16px"/>
            </a>
          <% } %>
        </td>
      </tr>
    </tbody>
    </table>
    </div>

    <div class="center">
    <table id="tanTable_style2" class="border-zero cellspacing-zero" style="width: 800px">
    <tbody>
      <tr><th>Activity Information</th></tr>
      <tr><td class="nobordered" align="left">
        <table class="border-zero cellspacing-zero cellpadding-3">
        <colgroup>
          <col style="width:230px"/>
          <col style="width:70px"/>
          <col/>
          <col style="width:70px"/>
        </colgroup>
          <tr>
            <td class="fieldName">Unit Name:</td>
            <td colspan="5">
              <logic:notEmpty name="resultBean" property="shipName">
                <%--<a href="#" class="ship-popup">${resultBean.shipName}</a>--%>
                <a href="javascript:void(0);" onclick="return showUnitPopup(${resultBean.shipPk},${projectPk});">
                  ${resultBean.shipName}
                </a>
              </logic:notEmpty>
              <logic:empty name="resultBean" property="shipName">
                <i>Unassigned</i>
              </logic:empty>
            </td>
          </tr>

          <logic:equal name="resultBean" property="isPreppedInd" value="Y">
            <tr>
              <td class="fieldName">Status:</td>
              <td colspan="5"><i>Prepped &amp; Ready</i></td>
            </tr>
          </logic:equal>

          <logic:equal name="resultBean" property="isPreppedInd" value="A">
            <tr>
              <td class="fieldName">Status:</td>
              <td colspan="5" style="color:#0A0;">Active</td>
            </tr>
          </logic:equal>

          <tr>
            <td class="fieldName" style="vertical-align:top;">Configured for Multi-Ship: </td>
            <td colspan="5" style="vertical-align:top;">
              <logic:equal name="resultBean" property="multiShipInd" value="Y">
                Y
                <logic:present name="resultBean" property="multiShipList">
                -
                <logic:iterate id="multiShipBean" name="resultBean" property="multiShipList"
                  type="com.premiersolutionshi.old.bean.ShipBean" indexId="i">
                  <logic:notEmpty name="multiShipBean" property="shipName">
                    <a href="javascript:void(0);" title="${multiShipBean.shipName}">${multiShipBean.uic}</a><% if (i < resultBean.getMultiShipList().size()-1) { %>, <% } %>
                  </logic:notEmpty>
                  <logic:empty name="multiShipBean" property="shipName">
                    ${multiShipBean.uic}<% if (i < resultBean.getMultiShipList().size()-1) { %>, <% } %>
                  </logic:empty>
                </logic:iterate>
                </logic:present>
              </logic:equal>
              <logic:notEqual name="resultBean" property="multiShipInd" value="Y">
                N
              </logic:notEqual>
            </td>
          </tr>

          <tr>
            <td class="fieldName">Configured for Working Capital Fund: </td>
            <td>
              ${resultBean.nwcfInd.equals('Y') ? 'Y' : 'N'}
            </td>
            <td class="fieldName">1348 No Location: </td>
            <td>
              ${resultBean.form1348NoLocationInd.equals('Y') ? 'Y' : 'N'}
            </td>
<%--
            <td class="fieldName">1348:</td>
            <td>
              ${resultBean.form1348NoLocationInd.equals('Y') ? 'No Location' : 'Has Location'}
            </td>
--%>
            <td class="fieldName">1348 No Class SL HAZMAT: </td>
            <td>
              ${resultBean.form1348NoClassInd.equals('Y') ? 'Y' : 'N'}
            </td>
          </tr>

          <logic:notEmpty name="resultBean" property="homeport">
            <tr>
              <td class="fieldName">Homeport:</td>
              <td colspan="5">${resultBean.homeport}</td>
            </tr>
          </logic:notEmpty>

          <logic:notEmpty name="resultBean" property="rsupply">
            <tr>
              <td class="fieldName">RSupply Version:</td>
              <td colspan="5">${resultBean.rsupply}</td>
            </tr>
          </logic:notEmpty>

          <logic:notEmpty name="resultBean" property="decomDate">
            <tr>
              <td class="fieldName">DECOM Date:</td>
              <td colspan="5" style="color:red;font-weight:bold;">${resultBean.decomDate}</td>
            </tr>
          </logic:notEmpty>

          <% if (!CommonMethods.isEmpty(resultBean.getS2ClosureDate()) || !CommonMethods.isEmpty(resultBean.getFuelClosureDate())) { %>
            <tr>
              <td colspan="6">&nbsp;</td>
            </tr>
            <tr>
            <logic:notEmpty name="resultBean" property="s2ClosureDate">
              <td class="fieldName">S-2 Galley Closed Until</td>
              <td colspan="5" style="color:red;"><bean:write name="resultBean" property="s2ClosureDate"/></td>
            </logic:notEmpty>
            <logic:notEmpty name="resultBean" property="fuelClosureDate">
              <td class="fieldName">Fuel Closed Until</td>
              <td colspan="5" style="color:red;">${resultBean.fuelClosureDate}</td>
            </logic:notEmpty>
            </tr>
          <% } %>

          <logic:notEmpty name="lastVisitBean" property="supportVisitDate">
            <tr>
              <td colspan="6">&nbsp;</td>
            </tr>
            <tr>
              <td class="fieldName">Last Support Visit:</td>
              <td colspan="5"><a href="issue.do?id=${lastVisitBean.issuePk}&projectPk=${projectPk}">${lastVisitBean.supportVisitDate} (${lastVisitBean.category})</a></td>
            </tr>
          </logic:notEmpty>

          <logic:notEmpty name="resultBean" property="shipPk">
            <tr>
              <td colspan="6">&nbsp;</td>
            </tr>
            <tr>
              <td class="fieldName">LOGCOP:</td>
              <td colspan="5"><a href="report.do?action=transmittalDetail&facetName=${resultBean.computerName}&projectPk=${projectPk}">View Transmittal Details</a></td>
            </tr>
          </logic:notEmpty>
        </table>
      </td></tr>


      <tr><th>Equipment Info</th></tr>
      <tr><td class="nobordered" align="left">
        <table class="border-zero cellspacing-zero cellpadding-3">
        <colgroup><col style="width:230px"/><col style="width:200px"/></colgroup>
          <tr>
            <td class="fieldName" nowrap>Current Location:</td>
            <td><% if (!CommonMethods.isEmpty(resultBean.getLocation())) { %><b>${resultBean.location}</b><% } else { %>--<% } %></td>
          </tr>

          <tr><td colspan="4">&nbsp;</td></tr>

          <tr>
            <td class="fieldName">Computer Name:</td>
            <td><% if (!CommonMethods.isEmpty(resultBean.getComputerName())) { %>${resultBean.computerName}<% } else { %>--<% } %></td>
            <td class="fieldName">Laptop Tag:</td>
            <td><% if (!CommonMethods.isEmpty(resultBean.getLaptopTag())) { %>${resultBean.laptopTag}<% } else { %>--<% } %></td>
          </tr>

          <tr>
            <td class="fieldName" nowrap>Computer Model:</td>
            <td><% if (!CommonMethods.isEmpty(resultBean.getLaptopProductName())) { %>${resultBean.laptopProductName}<% } else { %>--<% } %></td>
          </tr>

          <tr>
            <td class="fieldName" nowrap>OS Version:</td>
            <td><% if (!CommonMethods.isEmpty(resultBean.getOsVersion())) { %>${resultBean.osVersion}<% } else { %>--<% } %></td>
          </tr>
          <tr>
            <td class="fieldName" nowrap>Serial No.:</td>
            <td><% if (!CommonMethods.isEmpty(resultBean.getLaptopSerialNumber())) { %>${resultBean.laptopSerialNumber}<% } else { %>--<% } %></td>
          </tr>
          <tr>
            <td class="fieldName" nowrap>Status:</td>
            <td colspan="3"><% if (!CommonMethods.isEmpty(resultBean.getLaptopStatus())) { %>${resultBean.laptopStatus}<% } else { %>--<% } %></td>
          </tr>

          <tr><td colspan="4">&nbsp;</td></tr>

          <tr>
            <td class="fieldName" nowrap>Scanner Model:</td>
            <td><% if (!CommonMethods.isEmpty(resultBean.getScannerProductName())) { %>${resultBean.scannerProductName}<% } else { %>--<% } %></td>
            <td class="fieldName" nowrap>Scanner Tag:
            <td><% if (!CommonMethods.isEmpty(resultBean.getScannerTag())) { %>${resultBean.scannerTag}<% } else { %>--<% } %></td>
          </tr>
          <tr>
            <td class="fieldName" nowrap>Serial No.:</td>
            <td><% if (!CommonMethods.isEmpty(resultBean.getScannerSerialNumber())) { %>${resultBean.scannerSerialNumber}<% } else { %>--<% } %></td>
          </tr>
          <tr>
            <td class="fieldName" nowrap>Status:</td>
            <td colspan="3"><% if (!CommonMethods.isEmpty(resultBean.getScannerStatus())) { %>${resultBean.scannerStatus}<% } else { %>--<% } %></td>
          </tr>
        </table>

        <hr style="color:#777;background-color:#777;height:1px;"/>

        <table class="border-zero cellspacing-zero cellpadding-3">
        <colgroup><col style="width:230px"/></colgroup>
          <tr>
            <td class="fieldName" nowrap>FACET Version:</td>
            <td>
              <% if (!CommonMethods.isEmpty(resultBean.getFacetVersion())) { %>
                ${resultBean.facetVersion}
                <% if (resultBean.getFacetVersion().equals(currFacetVersion)) { %>
                  <img src="images/checkbox_checked.png"/>
                <% } else { %>
                  <img src="images/icon_error.gif"/>
                <% } %>
              <% } else { %>--<% } %>
            </td>
          </tr>

          <tr>
            <td class="fieldName" nowrap style="vertical-align:top;">ATO Installed:</td>
            <td style="vertical-align:top;">
              <% if (resultBean.getAtoInstalledList() != null && resultBean.getAtoInstalledList().size() > 0) { %>
                <logic:iterate id="atoFilename" name="resultBean" property="atoInstalledList" indexId="i"><% if (i > 0) { %>, <% } %>${atoFilename}</logic:iterate>
              <% } else { %>--<% } %>
            </td>
          </tr>

          <tr>
            <td class="fieldName" nowrap>DMS List as of:</td>
            <td>
              <% if (!CommonMethods.isEmpty(resultBean.getDmsVersion())) { %>
                ${resultBean.dmsVersion}
                <% if (resultBean.getDmsVersion().equals(currDmsVersion)) { %>
                  <img src="images/checkbox_checked.png"/>
                <% } else { %>
                  <img src="images/icon_error.gif"/>
                <% } %>
              <% } else { %>--<% } %>
            </td>
          </tr>

          <tr><td colspan="2">&nbsp;</td></tr>

          <tr>
            <td class="fieldName" nowrap>Network Adapter Type:</td>
            <td>
              <% if (!CommonMethods.isEmpty(resultBean.getNetworkAdapter())) { %>
                ${resultBean.networkAdapter}
                <% if (resultBean.getNetworkAdapter().equals("MS Loopback Adapter")) { %>
                  <img src="images/checkbox_checked.png"/>
                <% } else { %>
                  <img src="images/icon_error.gif"/>
                <% } %>
              <% } else { %>--<% } %>
            </td>
          </tr>

          <tr><td colspan="2">&nbsp;</td></tr>

          <tr>
            <td class="fieldName" nowrap>Admin Password:</td>
            <td><% if (!CommonMethods.isEmpty(resultBean.getAdminPassword())) { %>${resultBean.adminPassword}<% } else { %>--<% } %></td>
          </tr>

          <tr><td colspan="2">&nbsp;</td></tr>

          <tr>
            <td class="fieldName" nowrap>Kofax Version:</td>
            <td><% if (!CommonMethods.isEmpty(resultBean.getKofaxProductName())) { %>${resultBean.kofaxProductName}<% } else { %>--<% } %></td>
          </tr>
          <tr>
            <td class="fieldName" nowrap>Kofax License:</td>
            <td><% if (!CommonMethods.isEmpty(resultBean.getKofaxLicenseKey())) { %>${resultBean.kofaxLicenseKey} | ${resultBean.kofaxProductCode}<% } else { %>--<% } %></td>
          </tr>

          <tr><td colspan="2">&nbsp;</td></tr>

          <tr>
            <td class="fieldName" nowrap>VRS License:</td>
            <td>
              <% if (!CommonMethods.isEmpty(resultBean.getVrsLicensePk()) && resultBean.getVrsLicensePk().equals("0")) { %>
                N/A
              <% } else if (!CommonMethods.isEmpty(resultBean.getVrsLicenseKey())) { %>
                ${resultBean.vrsLicenseKey}
                |
                ${resultBean.vrsProductCode}
              <% } else { %>
                --
              <% } %>
            </td>
          </tr>

          <tr><td colspan="2">&nbsp;</td></tr>

          <tr>
            <td class="fieldName" nowrap>Microsoft:</td>
            <td><% if (!CommonMethods.isEmpty(resultBean.getMsOfficeProductName())) { %>${resultBean.msOfficeProductName}<% } else { %>--<% } %></td>
          </tr>
          <tr>
            <td class="fieldName" nowrap>Access Version:</td>
            <td><% if (!CommonMethods.isEmpty(resultBean.getAccessVersion())) { %>${resultBean.accessVersion}<% } else { %>--<% } %></td>
          </tr>
          <tr>
            <td class="fieldName" nowrap>License Key:</td>
            <td><% if (!CommonMethods.isEmpty(resultBean.getMsOfficeLicenseKey())) { %>${resultBean.msOfficeLicenseKey}<% } else { %>--<% } %></td>
          </tr>
          <tr>
            <td class="fieldName TOP" nowrap>Access Version History:</td>
            <td>
              <% if (!CommonMethods.isEmpty(resultBean.getAccessVersionHistory())) { %>
                <logic:iterate id="str" name="resultBean" property="accessVersionHistoryBr" indexId="i">
                  ${str}<% if (i+1 < resultBean.getAccessVersionHistoryBr().size()) { %><br/><% } %>
                </logic:iterate>
              <% } else { %>--<% } %>
            </td>
          </tr>

          <tr><td colspan="2">&nbsp;</td></tr>

          <tr>
            <td class="fieldName" nowrap>Ghost Version:</td>
            <td><% if (!CommonMethods.isEmpty(resultBean.getGhostVersion())) { %>${resultBean.ghostVersion}<% } else { %>--<% } %></td>
          </tr>

          <tr><td colspan="2">&nbsp;</td></tr>

          <tr>
            <td class="fieldName" nowrap>Contract Installed Under:</td>
            <td><% if (!CommonMethods.isEmpty(resultBean.getContractNumber())) { %>${resultBean.contractNumber}<% } else { %>--<% } %></td>
          </tr>

          <tr><td colspan="2">&nbsp;</td></tr>

          <tr>
            <td class="fieldName TOP" nowrap>Documentation Version:</td>
            <td>
              <% if (!CommonMethods.isEmpty(resultBean.getDocumentationVersion())) { %>
                <logic:iterate id="str" name="resultBean" property="documentationVersionBr" indexId="i">
                  ${str}<% if (i+1 < resultBean.getDocumentationVersionBr().size()) { %><br/><% } %>
                </logic:iterate>
              <% } else { %>--<% } %>
            </td>
          </tr>

          <tr><td colspan="2">&nbsp;</td></tr>

          <tr>
            <td class="fieldName TOP" nowrap>Documentation Version History:</td>
            <td>
              <% if (!CommonMethods.isEmpty(resultBean.getDocumentationVersionHistory())) { %>
                <logic:iterate id="str" name="resultBean" property="documentationVersionHistoryBr" indexId="i">
                  ${str}<% if (i+1 < resultBean.getDocumentationVersionHistoryBr().size()) { %><br/><% } %>
                </logic:iterate>
              <% } else { %>--<% } %>
            </td>
          </tr>

          <tr><td colspan="2">&nbsp;</td></tr>

          <tr>
            <td class="fieldName TOP" nowrap>Notes:</td>
            <td>
              <% if (!CommonMethods.isEmpty(resultBean.getNotes())) { %>
                <logic:iterate id="str" name="resultBean" property="notesBr" indexId="i">
                  ${str}<% if (i+1 < resultBean.getNotesBr().size()) { %><br/><% } %>
                </logic:iterate>
              <% } else { %>--<% } %>
            </td>
          </tr>
        </table>
      </td></tr>


      <logic:present name="resultBean" property="locHistList">
      <logic:notEmpty name="resultBean" property="locHistList">
        <tr><th>Location Tracking History</th></tr>
        <tr><td class="nobordered" align="left">
          <table id="local-sub-table" class="border-zero alt-color">
          <colgroup>
            <col style="width:230px"/>
            <col style="width:80px"/>
            <col style="width:350px"/>
          </colgroup>
          <thead>
            <tr class="ignore">
              <th style="text-align:left;">Location</th>
              <th>As of</th>
              <th style="text-align:left;">Reason</th>
            </tr>
          </thead>
          <tbody>
          <logic:iterate id="locationBean" name="resultBean" property="locHistList" type="com.premiersolutionshi.old.bean.SystemLocationBean" indexId="i">
            <tr class="TOP nohover">
              <td nowrap>
                ${locationBean.location}
                <% if (i == 0) { %><i>(Current)</i><% } %>
              </td>
              <td nowrap align="center">${locationBean.locationDate}</td>
              <td>${locationBean.reason}</td>
            </tr>
          </logic:iterate>
          </tbody>
          </table>
        </td></tr>
      </logic:notEmpty>
      </logic:present>


      <logic:present name="backfileBean">
      <logic:notEmpty name="backfileBean" property="shipName">
        <tr>
          <th>
            Backfile Workflow
            <a href="backfile.do?action=workflowEdit&backfileWorkflowPk=${backfileBean.backfileWorkflowPk}&projectPk=${projectPk}"><img src="images/icon_edit.png" height="12"/></a>
          </th>
        </tr>
        <tr><td class="" align="center">
          <logic:equal name="backfileBean" property="isRequired" value="N">
            <div style="text-align:center;background:#aaa;color:#555;font-weight:bold;padding:5px;">Not Required</div>
          </logic:equal>
          <logic:equal name="backfileBean" property="isRequired" value="Y">
            <table id="backfile-table" class="border-zero">
            <colgroup>
              <col span="12" style="width:65px"/>
            </colgroup>
            <thead>
              <tr>
                <th style="font-size:10px;">Backfile<br/>Date<br/>Requested</th>
                <th style="font-size:10px;">Date<br/>Received<br/>By PSHI</th>
                <th style="font-size:10px;">Date<br/>Delivered<br/>to Scanning</th>
                <th style="font-size:10px;">FY14/13<br/>Completed<br/>Date</th>
                <th style="font-size:10px;">FY14/13<br/>CD Sent<br/>for Customer<br/>Date</th>
                <th style="font-size:10px;">FY12/11<br/>Completed<br/>Date</th>
                <th style="font-size:10px;">Extract File<br/>Created</th>
                <th style="font-size:10px;">All backfile<br/>CD mailed<br/>to Cust/SD</th>
                <th style="font-size:10px;">Verify<br/>All Backfiles<br/>Installed<br/>in FACET</th>
                <th style="font-size:10px;">All Backfile<br/>CD Delivered<br/>to LogCop</th>
                <th style="font-size:10px;">Verified<br/>LOGCOP<br/>Backfile<br/>Uploaded</th>
                <th style="font-size:10px;">Final Backfile<br/>Report<br/>Generated<br/>and Given to<br/>CompacFlt</th>
                <!--th>Destruction<br/>Date</th-->
              </tr>
            </thead>
            <tbody>
              <tr>
                <td style="${backfileBean.requestedDateCss}">${backfileBean.requestedDate}</td>
                <td style="${backfileBean.receivedDateCss}">${backfileBean.receivedDate}</td>
                <td style="${backfileBean.scanningDeliveredDateCss}">${backfileBean.scanningDeliveredDate}</td>
                <td style="<bean:write name="backfileBean" property="fy1314CompletedDateCss"/>"><bean:write name="backfileBean" property="fy1314CompletedDate"/></td>
                <td style="<bean:write name="backfileBean" property="fy1314BurnedDateCss"/>"><bean:write name="backfileBean" property="fy1314BurnedDate"/></td>
                <td style="<bean:write name="backfileBean" property="fy1112CompletedDateCss"/>"><bean:write name="backfileBean" property="fy1112CompletedDate"/></td>
                <td style="${backfileBean.extractDateCss}">${backfileBean.extractDate}</td>
                <td style="<bean:write name="backfileBean" property="fy1314MailedDateCss"/>"><bean:write name="backfileBean" property="fy1314MailedDate"/></td>
                <td style="${backfileBean.laptopInstalledDateCss}">${backfileBean.laptopInstalledDate}</td>
                <td style="${backfileBean.logcopDeliveredDateCss}">${backfileBean.logcopDeliveredDate}</td>
                <td style="${backfileBean.logcopUploadedDateCss}">${backfileBean.logcopUploadedDate}</td>
                <td style="${backfileBean.finalReportDateCss}">${backfileBean.finalReportDate}</td>
                <!--td style="${backfileBean.destructionDateCss}">${backfileBean.destructionDate}</td-->
              </tr>
            </tbody>
            </table>
          </logic:equal>
        </td></tr>
      </logic:notEmpty>
      </logic:present>

      <logic:present name="trainingBean">
      <logic:notEmpty name="trainingBean" property="shipName">
        <tr>
          <th>
            Training Workflow
            <a href="training.do?action=workflowEdit&trainingWorkflowPk=${trainingBean.trainingWorkflowPk}&projectPk=${projectPk}"><img src="images/icon_edit.png" height="12"/></a>
          </th>
        </tr>
        <tr><td align="center">
          <table id="training-table" class="border-zero">
          <colgroup>
            <col span="9" style="width:65px"/>
          </colgroup>
          <thead>
            <tr>
              <th>Location<br/>File<br/>Received</th>
              <th>Location<br/>File<br/>Reviewed</th>
              <th>PacFlt<br/>Sent<br/>Notification<br/>to Send<br/>Food Report</th>
              <th>System<br/>Shipped</th>
              <th>Computer<br/>Name<br/>in Database</th>
              <th>Computer<br/>Name<br/>Provided to<br/>Logcop</th>
              <th>Training Kit<br/>Ready</th>
              <th>Scheduled<br/>Training<br/>Date</th>
              <th>Actual<br/>Training<br/>Date</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>${trainingBean.locFileRecvDate}</td>
              <td>${trainingBean.locFileRevDate}</td>
              <td>${trainingBean.pacfltFoodReportDate}</td>
              <td>${trainingBean.systemReadyDate}</td>
              <td>${trainingBean.computerNameDbDate}</td>
              <td>${trainingBean.computerNameLogcopDate}</td>
              <td>${trainingBean.trainingKitReadyDate}</td>
              <td style="${trainingBean.schedTrainingDateCss}">${trainingBean.schedTrainingDate}</td>
              <td>${trainingBean.actualTrainingDate}</td>
            </tr>
          </tbody>
          </table>
        </td></tr>
      </logic:notEmpty>
      </logic:present>


      <tr><th>Template Files</th></tr>
      <tr><td class="nobordered" align="left">
        <% if (!CommonMethods.isEmpty(resultBean.getShipName())) { %>
          <a href="system.do?action=vesselDataSheetDoc&configuredSystemPk=${resultBean.configuredSystemPk}&projectPk=${projectPk}">
            <img src="images/file_icons/sml_file_doc.gif"/> Vessel Data Sheet [Template]
          </a><br/>

          <a href="files/2015 Training Sign In sheet.docx">
            <img src="images/file_icons/sml_file_doc.gif"/> Training Sign In Sheet [Template]
          </a><br/>
        <% } %>

        <% if (resultBean.getLaptopProductName().toLowerCase().indexOf("elitebook") > 0) { %>
          <%--
          <a href="system.do?action=checkListStep1Doc&configuredSystemPk=${resultBean.configuredSystemPk}&projectPk=${projectPk}">
            <img src="images/file_icons/sml_file_doc.gif"/> Final Laptop Prep Checklist - Step 001 (HP eBook) [Template]
          </a><br/>
           --%>
          <a href="system.do?action=zbookCheckListStep1Doc&configuredSystemPk=${resultBean.configuredSystemPk}&projectPk=${projectPk}">
            <img src="images/file_icons/sml_file_doc.gif"/> FACET Laptop Configuration Info [Template]
          </a><br/>

          <a href="system.do?action=checkListStep2Doc&configuredSystemPk=${resultBean.configuredSystemPk}&projectPk=${projectPk}">
            <img src="images/file_icons/sml_file_doc.gif"/> Trainer Laptop Checklist - Step 002 (HP eBook) [Template]
          </a><br/>
        <% } else { %>
          <a href="system.do?action=zbookCheckListStep1Doc&configuredSystemPk=${resultBean.configuredSystemPk}&projectPk=${projectPk}">
            <img src="images/file_icons/sml_file_doc.gif"/> FACET Laptop Configuration Info [Template]
          </a><br/>

          <a href="system.do?action=zbookCheckListStep2Doc&configuredSystemPk=${resultBean.configuredSystemPk}&projectPk=${projectPk}">
            <img src="images/file_icons/sml_file_doc.gif"/> Trainer Laptop Checklist - Step 002 [Template]
          </a><br/>
        <% } %>

        <% if (!CommonMethods.isEmpty(resultBean.getShipName())) { %>
          <a href="files/Post Installation Checklist.docx">
            <img src="images/file_icons/sml_file_doc.gif"/> Post-Install Checklist [Template]
          </a><br/>
        <% } %>
      </td></tr>


      <tr><th>Attached Files</th></tr>
      <tr><td class="nobordered" align="center">
        <div style="text-align:left;">
            <logic:empty name="resultBean" property="hardwareFileBean">
              <span style="color:#f00;"><img src="images/icon_error.gif"/> Vessel Data Sheet (Missing)</span><br/>
            </logic:empty>
            <logic:notEmpty name="resultBean" property="hardwareFileBean">
              <a href="download.do?filePk=<bean:write name="resultBean" property="hardwareFileBean.filePk"/>">
                <img src="<bean:write name="resultBean" property="hardwareFileBean.smlImage"/>"/>
                Vessel Data Sheet
              </a><br/>
            </logic:notEmpty>

            <logic:empty name="resultBean" property="trainingFileBean">
              <span style="color:#f00;"><img src="images/icon_error.gif"/> Training Sign In Sheet (Missing)</span><br/>
            </logic:empty>
            <logic:notEmpty name="resultBean" property="trainingFileBean">
              <a href="download.do?filePk=<bean:write name="resultBean" property="trainingFileBean.filePk"/>">
                <img src="<bean:write name="resultBean" property="trainingFileBean.smlImage"/>"/>
                Training Sign-In Sheet
              </a><br/>
            </logic:notEmpty>

            <logic:empty name="resultBean" property="laptop1FileBean">
              <span style="color:#f00;"><img src="images/icon_error.gif"/> FACET Laptop Configuration Info (Missing)</span><br/>
            </logic:empty>
            <logic:notEmpty name="resultBean" property="laptop1FileBean">
              <a href="download.do?filePk=<bean:write name="resultBean" property="laptop1FileBean.filePk"/>">
                <img src="<bean:write name="resultBean" property="laptop1FileBean.smlImage"/>"/>
                FACET Laptop Configuration Info
              </a><br/>
            </logic:notEmpty>

            <logic:empty name="resultBean" property="laptop2FileBean">
              <span style="color:#f00;"><img src="images/icon_error.gif"/> Trainer Laptop Checklist - Step 002 (Missing)</span><br/>
            </logic:empty>
            <logic:notEmpty name="resultBean" property="laptop2FileBean">
              <a href="download.do?filePk=<bean:write name="resultBean" property="laptop2FileBean.filePk"/>">
                <img src="<bean:write name="resultBean" property="laptop2FileBean.smlImage"/>"/>
                Trainer Laptop Checklist - Step 002
              </a><br/>
            </logic:notEmpty>

            <logic:empty name="resultBean" property="postInstallFileBean">
              <span style="color:#f00;"><img src="images/icon_error.gif"/> Post Install Checklist (Missing)</span><br/>
            </logic:empty>
            <logic:notEmpty name="resultBean" property="postInstallFileBean">
              <a href="download.do?filePk=<bean:write name="resultBean" property="postInstallFileBean.filePk"/>">
                <img src="<bean:write name="resultBean" property="postInstallFileBean.smlImage"/>"/>
                Post Install Checklist
              </a><br/>
            </logic:notEmpty>

          <hr width="100%"/>

          <logic:iterate id="fileBean" name="resultBean" property="attachedFileList" type="com.premiersolutionshi.old.bean.FileBean">
            <a href="download.do?filePk=${fileBean.filePk}">
              <img src="${fileBean.smlImage}"/>
              ${fileBean.filename}
            </a><br/>
          </logic:iterate>
        </div>
      </td></tr>
    </tbody>
    </table>
    </div>

    <p align="center">
    <a class="btn btn-primary" href="system.do?action=configuredSystemEdit&configuredSystemPk=${resultBean.configuredSystemPk}&projectPk=${projectPk}">
      <span class="glyphicon glyphicon-pencil"></span>
      Edit
    </a>
    <a class="btn btn-success" href="issue.do?action=add&shipPk=${resultBean.shipPk}&projectPk=${projectPk}">
      <span class="glyphicon glyphicon-edit"></span>
      Create New Support Issue
      </a>
    <a class="btn btn-warning" href="support.do?action=generateFeedbackForm&uic=${resultBean.uic}&projectPk=${projectPk}">
      <span class="glyphicon glyphicon-file"></span>
      Generate Support Feedback Form
    </a>
    <a class="btn btn-default" href="system.do?action=configuredSystemList&projectPk=${projectPk}">
      <span class="glyphicon glyphicon-list-alt"></span>
      Return to Configured System List
    </a>
    </p>
  </div></div>

  <%@ include file="../include/app-col2.jsp" %>
</div>
</div>

<div id="toPopup">
  <div class="close"></div>
  <span class="ecs_tooltip">Press Esc to close <span class="arrow"></span></span>
  <div id="popup_content"> <!--your content start-->
    <div id="ship-popup"> <!-- ship content -->
      <%@ include file="../include/popup-ship.jsp" %>
    </div> <!-- ship-content -->
  </div> <!--your content end-->
</div> <!--toPopup end-->
<div class="loader"></div>
<div id="backgroundPopup"></div>


<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-popup.js"></script>
<script type="text/javascript" src="js/jquery-altcolor.js"></script>
<script type="text/javascript" src="js/support/unit/showUnitPopup.js"></script>

<script type="text/javascript">
$(function() {
  $(document).tooltip();
});
</script>

</body>
</html>
