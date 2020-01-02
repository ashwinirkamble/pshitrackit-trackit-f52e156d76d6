<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>
<%@ page import="com.premiersolutionshi.old.bean.HardwareBean" %>
<%@ page import="com.premiersolutionshi.old.bean.SoftwareBean" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="defaultPageTitle" value="FACET Configured System Add/Edit"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/>
<bean:define id="pageTitle" name="customPageTitle"/>
<logic:equal name="pageTitle" value="">
  <bean:define id="pageTitle" name="defaultPageTitle"/>
</logic:equal>

<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk"         scope="request" class="java.lang.String"/>
<jsp:useBean id="systemBean"        scope="request" class="com.premiersolutionshi.old.bean.SystemBean"/>

<jsp:useBean id="laptopList"        scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="scannerList"       scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="kofaxLicenseList"  scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="vrsLicenseList"    scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="osVersionList"     scope="request" class="java.util.ArrayList"/>

<jsp:useBean id="editType"          scope="request" class="java.lang.String"/>

<html>
<head>
  <meta charset="utf-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />

  <title>TrackIT - ${pageTitle}</title>

  <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/stylesheet.css"/>
  <link rel="stylesheet" type="text/css" href="css/jquery-ui.min.css"/>

  <style>
    #local-sub-table th {
      background: none;
      }

    #local-sub-table tr:first-child td:first-child {
      background: #92d050;
      }

    input[type="radio"], input[type="checkbox"] {
      margin-top: -1px;
      vertical-align: middle;
    }
  </style>
</head>

<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask leftmenu"><div class="colright">
  <div class="col1wrap"><div class="col1">
    <%@ include file="../include/content-header.jsp" %>
<%--ashwini system.do -- ./system.do --%>
    <tags:projectPkBreadcrumb projectPk="${projectPk}" pageTitle="${pageTitle}" hideOptions="true"
      parentUrl="./system.do?action=configuredSystemList&projectPk=${projectPk}" parentTitle="FACET Configured System List"/>

    <p align="center">
    Fields marked with an asterisk <span class="regAsterisk">*</span> are required.
    </p>

    <html:form action="system.do" onsubmit="return valFields();" method="POST" enctype="multipart/form-data">
    <input type="hidden" name="projectPk" value="${projectPk}"/>
    <% if (editType.equals("add")) { %>
      <input type="hidden" name="action" value="configuredSystemAddDo"/>
    <% } else { %>
      <input type="hidden" name="action" value="configuredSystemEditDo"/>
      <html:hidden name="systemBean" property="configuredSystemPk" />
    <% } %>
    <div class="center">
    <table id="tanTable_style2" class="pshi-borderless-table" style="width: 750px">
    <tbody>
      <tr><th>Ship Assigned</th></tr>
      <tr><td class="nobordered" align="left"> <!-- tan_table -->
        <table class="border-zero">
        <colgroup><col width="150"/></colgroup>
          <tr>
            <td class="fieldName">Ship Assigned:</td>
            <td colspan="3">
              <html:select name="systemBean" property="shipFk" styleClass="form-control input-sm">
                <html:option value="">None</html:option>
                <html:options collection="shipList" property="shipPk" labelProperty="shipNameTypeHull"/>
              </html:select>
              <%-- Removed on 2018-09-10 -Lewis --%>
              <%-- <html:select name="systemBean" property="uic" styleClass="form-control input-sm"> --%>
              <%--   <html:option value="">None</html:option> --%>
              <%--   <html:options collection="shipList" property="uic" labelProperty="shipNameTypeHull"/> --%>
              <%-- </html:select> --%>
            </td>
          </tr>
          <tr>
            <td class="fieldName">Status:</td>
            <td colspan="3">
              <label class="radio-inline">
                <html:radio name="systemBean" property="isPreppedInd" styleId="isPreppedIndA" value="A"/>
                Active FACET
              </label>

              <label class="radio-inline">
                <html:radio name="systemBean" property="isPreppedInd" styleId="isPreppedIndY" value="Y"/>
                Prepped &amp; Ready
              </label>

              <label class="radio-inline">
                <html:radio name="systemBean" property="isPreppedInd" styleId="isPreppedIndN" value="N"/>
                Inactive
              </label>
            </td>
          </tr>
          <tr>
            <td></td>
            <td>
              <label class="checkbox-inline">
                <html:checkbox name="systemBean" property="multiShipInd" styleId="multiShipInd" value="Y"/>
                Configured for multi-ship?
              </label>
            </td>
            <td class="fieldName multiShipTd">UICs (comma-separated):</td>
            <td class="multiShipTd"><html:text name="systemBean" property="multiShipUicList" styleClass="form-control input-sm"/></td>
          </tr>
          <tr>
            <td></td>
            <td>
              <label class="checkbox-inline">
                <html:checkbox name="systemBean" property="nwcfInd" styleId="nwcfInd" value="Y"/>
                Configured for Working Capital Fund
              </label>
            </td>
            <td>
              <label class="checkbox-inline">
                <html:checkbox name="systemBean" property="form1348NoLocationInd" styleId="form1348NoLocationInd" value="Y"/>
                1348 No Location
              </label>
            </td>
            <td>
              <label class="checkbox-inline">
                <html:checkbox name="systemBean" property="form1348NoClassInd" styleId="form1348NoClassInd" value="Y"/>
                1348 No Class SL HAZMAT
              </label>
            </td>
          </tr>
          <tr><td colspan="4">&nbsp;</td></tr>
          <tr>
            <td class="fieldName">S-2 Galley Closed Until:</td>
            <td><html:text name="systemBean" property="s2ClosureDate" styleClass="form-control input-sm datepicker"/></td>
            <td class="fieldName">Fuel Closed Until:</td>
            <td><html:text name="systemBean" property="fuelClosureDate" styleClass="form-control input-sm datepicker"/></td>
          </tr>
        </table>
      </td></tr> <!-- end tan_table -->


      <tr><th>PSHI-Maintained Software</th></tr>
      <tr><td class="nobordered" align="left"> <!-- tan_table -->
        <table class="border-zero cellspacing-zero">
        <colgroup><col width="175"/><col width="175"/><col width="175"/></colgroup>
          <tr>
            <td class="fieldName">OS Version:</td>
            <td>
              <html:select name="systemBean" property="osVersion" styleClass="form-control input-sm">
                <html:option value="">&nbsp;</html:option>
                <html:options collection="osVersionList" name="value" property="value" labelProperty="value"/>
              </html:select>
            </td>
            <td></td>
            <td></td>
          </tr>
        
          <tr>
            <td class="fieldName">FACET Version:</td>
            <td>
              <html:select name="systemBean" property="facetVersion" styleClass="form-control input-sm">
                <html:option value="">&nbsp;</html:option>
                <logic:present name="facetVersionList"><html:options collection="facetVersionList" property="value" labelProperty="value"/></logic:present>
              </html:select>
            </td>
            <td></td>
            <td></td>
          </tr>

          <html:hidden name="systemBean" property="kofaxVersion"/>
          <html:hidden name="systemBean" property="dummyDatabaseVersion"/>

          <tr>
            <td class="fieldName">DMS List as of:</td>
            <td>
              <html:select name="systemBean" property="dmsVersion" styleClass="form-control input-sm">
                <html:option value="">&nbsp;</html:option>
                <logic:present name="dmsVersionList"><html:options name="dmsVersionList"/></logic:present>
              </html:select>
            </td>
          </tr>
          <tr>
            <td class="fieldName">Network Adapter Type:</td>
            <td colspan="3">
              <label class="radio-inline">
                <html:radio name="systemBean" property="networkAdapter" styleId="msLoopbackAdapter" value="MS Loopback Adapter"/>
                MS Loopback Adapter
              </label>
              <label class="radio-inline">
                <html:radio name="systemBean" property="networkAdapter" styleId="physicalLoopbackAdapter" value="Physical Loopback Adapter"/>
                Physical Loopback Adapter
              </label>
              <label class="radio-inline">
                <html:radio name="systemBean" property="networkAdapter" styleId="unknownLoopbackAdapter" value=""/>
                Unknown
              </label>
            </td>
          </tr>
          <tr>
            <td class="fieldName">Admin Password:</td>
            <td><html:text name="systemBean" property="adminPassword" styleClass="form-control input-sm"/></td>
          </tr>
        </table>
      </td></tr> <!-- end tan_table -->


      <tr><th>Laptop (Hardware)</th></tr>
      <tr><td class="nobordered" align="left"> <!-- tan_table -->
        <table class="border-zero cellspacing-zero cellpadding-3" width="100%">
        <colgroup>
          <col width="150"/>
          <col width="150"/>
          <col width="350"/>
        </colgroup>
          <tr>
            <td class="fieldName">Computer Name:</td>
            <td colspan="2" width="500">
              <html:select name="systemBean" property="laptopPk" styleId="laptopSelect" styleClass="form-control input-sm">
                <html:option value=""/>
                <html:options collection="laptopList" property="laptopPk" labelProperty="computerName"/>
              </html:select>
            </td>
          </tr>
          <tr class="laptopInfo">
            <td class="fieldName">Tag:</td>
            <td id="laptopTag" colspan="2"></td>
          </tr>
          <tr class="laptopInfo">
            <td class="fieldName">Product Name:</td>
            <td id="laptopProductName" colspan="2"></td>
          </tr>
          <tr class="laptopInfo">
            <td class="fieldName">Serial Number:</td>
            <td id="laptopSerialNumber" colspan="2"></td>
          </tr>
          <tr class="laptopInfo">
            <td class="fieldName">Ghost Version:</td>
            <td>
              <html:select name="systemBean" property="currGhostVersion" onchange="checkNew(this, 'ghostVersion');" styleClass="form-control input-sm">
                <html:option value="">&nbsp;</html:option>
                <logic:present name="ghostVersionList"><html:options name="ghostVersionList"/></logic:present>
                <html:option value="null">Add new...</html:option>
              </html:select>
            </td>
            <td><html:text name="systemBean" property="ghostVersion" maxlength="50" style="display:none;" styleClass="form-control input-sm"/></td>
          </tr>
        </table>
      </td></tr>


      <tr class="laptopInfo"><th><img src="images/logo_kofax.png" width="110" height="30"/></th></tr>
      <tr class="laptopInfo"><td class="nobordered" align="left"> <!-- tan_table -->
        <table class="border-zero cellspacing-zero cellpadding-3" width="100%">
        <colgroup><col width="150"/><col width="150"/></colgroup>
          <tr>
            <td class="fieldName"><span class="regAsterisk">*</span> Kofax License Key /<br/>Product Code:</td>
            <td colspan="2">
              <html:select name="systemBean" property="kofaxLicensePk" styleId="kofaxLicenseSelect" styleClass="form-control input-sm">
                <html:option value=""></html:option>
                <html:options collection="kofaxLicenseList" property="kofaxLicensePk" labelProperty="licenseKeyProductCode"/>
              </html:select>
            </td>
          </tr>
          <tr>
            <td class="fieldName TOP">Kofax License Notes:</td>
            <td id="kofaxLicenseNotes" class="TOP" colspan="2"></td>
          </tr>
          <tr>
            <td class="fieldName"><span class="regAsterisk">*</span> Kofax Product Name:</td>
            <td>
              <html:select name="systemBean" property="currKofaxProductName" onchange="checkNew(this, 'kofaxProductName');" styleClass="form-control input-sm">
                <html:options name="kofaxProductNameList"/>
                <html:option value="null">Add new...</html:option>
              </html:select>
            </td>
            <td><html:text name="systemBean" property="kofaxProductName" maxlength="50" style="display:none;" styleClass="form-control input-sm"/></td>
          </tr>
        </table>
      </td></tr> <!-- end tan_table -->


      <tr class="laptopInfo"><th><img src="images/logo_vrs_2.png"/></th></tr>
      <tr class="laptopInfo"><td class="nobordered" align="left"> <!-- tan_table -->
        <table class="border-zero cellspacing-zero cellpadding-3" width="100%">
        <colgroup><col width="150"/><col width="540"/></colgroup>
          <tr>
            <td class="fieldName"><span class="regAsterisk">*</span> VRS License:</td>
            <td>
              <html:select name="systemBean" property="vrsLicensePk" styleId="vrsLicenseSelect" styleClass="form-control input-sm">
                <html:option value=""></html:option>
                <html:options collection="vrsLicenseList" property="vrsLicensePk" labelProperty="licenseKeyProductCode"/>
                <html:option value="0">N/A</html:option>
              </html:select>
            </td>
          </tr>
          <tr>
            <td class="fieldName TOP">VRS License Notes:</td>
            <td id="vrsLicenseNotes" class="TOP"></td>
          </tr>
        </table>
      </td></tr> <!-- end tan_table -->


      <tr><th>Scanner (Hardware)</th></tr>
      <tr><td class="nobordered" align="left"> <!-- tan_table -->
        <table class="border-zero cellspacing-zero cellpadding-3" width="100%">
        <colgroup><col width="150"/></colgroup>
          <tr>
            <td class="fieldName">Tag:</td>
            <td colspan="2">
              <html:select name="systemBean" property="scannerPk" styleId="scannerSelect" styleClass="form-control input-sm">
                <html:option value=""/>
                <html:options collection="scannerList" property="scannerPk" labelProperty="tag"/>
              </html:select>
            </td>
          </tr>
          <tr class="scannerInfo">
            <td class="fieldName">Product Name:</td>
            <td id="scannerProductName" colspan="2"></td>
          </tr>
          <tr class="scannerInfo">
            <td class="fieldName">Model Number:</td>
            <td id="scannerModelNumber" colspan="2"></td>
          </tr>
          <tr class="scannerInfo">
            <td class="fieldName">Serial Number:</td>
            <td id="scannerSerialNumber" colspan="2"></td>
          </tr>
        </table>
      </td></tr> <!-- end tan_table -->


      <tr><th><img src="images/logo_msoffice.png" width="77" height="32"/></th></tr>
      <tr><td class="nobordered" align="left"> <!-- tan_table -->
        <table class="border-zero cellspacing-zero cellpadding-3" width="100%">
        <colgroup><col width="150"/><col width="200"/></colgroup>
          <tr>
            <td class="fieldName">License Key:</td>
            <td colspan="2">
              <html:select name="systemBean" property="msOfficeLicensePk" styleClass="form-control input-sm">
                <html:option value=""/>
                <html:options collection="msOfficeLicenseList" property="msOfficeLicensePk" labelProperty="licenseKey"/>
              </html:select>
            </td>
          </tr>
          <tr>
            <td class="fieldName">Access Version:</td>
            <td>
              <html:select name="systemBean" property="currAccessVersion" onchange="checkNew(this, 'accessVersion');" styleClass="form-control input-sm">
                <html:option value="">&nbsp;</html:option>
                <html:options name="accessVersionList"/>
                <html:option value="null">Add new...</html:option>
              </html:select>
            </td>
            <td><html:text name="systemBean" property="accessVersion" maxlength="50" style="display:none;" styleClass="form-control input-sm"/></td>
          </tr>
        </table>
      </td></tr> <!-- end tan_table -->


      <tr><th>Location Tracking</th></tr>
      <tr><td class="nobordered" align="left"> <!-- tan_table -->
        <table id="locationTbl" border="0" cellspacing="0" width="100%">
        <thead><tr><td class="newRow" id="updateLocRow"><img src="images/icon_plus.gif"/> <a href="javascript:void(0);" class="ibtnAdd">Add Current Location</a></td></tr></thead>
        <tbody>
        <logic:present name="systemBean" property="locHistList">
        <logic:notEmpty name="systemBean" property="locHistList">
          <tr><td colspan="6">
            <table id="local-sub-table" border="0" class="alt-color">
            <thead>
              <tr class="ignore">
                <th style="text-align:left;">Location</th>
                <th>As of</th>
                <th style="text-align:left;">Reason</th>
              </tr>
            </thead>
            <tbody>
            <logic:iterate id="locationBean" name="systemBean" property="locHistList" type="com.premiersolutionshi.old.bean.SystemLocationBean" indexId="locationIndex">
              <tr class="TOP">
                <td nowrap>
                  ${locationBean.location}
                  <% if (locationIndex == 0) { %><i>(Current)</i><% } %>
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
        </tbody>
        </table>
      </td></tr> <!-- end tan_table -->


      <tr><th>Documentation</th></tr>
      <tr><td class="nobordered" align="left"> <!-- tan_table -->
        <table class="border-zero cellspacing-zero cellpadding-3" width="100%">
        <colgroup><col width="180"/><col width="180"/></colgroup>
          <tr>
            <td class="fieldName"><span class="regAsterisk">*</span> Contract Number for Install:</td>
            <td>
              <html:select name="systemBean" property="currContractNumber" onchange="checkNew(this, 'contractNumber');" styleClass="form-control input-sm">
                <html:option value="">&nbsp;</html:option>
                <logic:present name="contractNumberList"><html:options name="contractNumberList"/></logic:present>
                <html:option value="null">Add new...</html:option>
              </html:select>
            </td>
            <td><html:text name="systemBean" property="contractNumber" maxlength="50" style="display:none;" styleClass="form-control input-sm"/></td>
          </tr>
          <tr>
            <td class="fieldName TOP">Documentation Version:</td>
            <td colspan="2"><html:textarea name="systemBean" property="documentationVersion" rows="3" styleClass="form-control input-sm"/></td>
          </tr>
        </table>
      </td></tr> <!-- end tan_table -->


      <tr><th>Version History</th></tr>
      <tr><td class="nobordered" align="left"> <!-- tan_table -->
        <table class="border-zero cellspacing-zero cellpadding-3" width="100%">
        <colgroup><col width="180"/></colgroup>
          <tr>
            <td class="fieldName TOP">FACET Version History:</td>
            <td><html:textarea name="systemBean" property="facetVersionHistory" rows="3" styleClass="form-control input-sm"/></td>
          </tr>

          <html:hidden name="systemBean" property="kofaxVersionHistory"/>

          <tr>
            <td class="fieldName TOP">Access Version History:</td>
            <td><html:textarea name="systemBean" property="accessVersionHistory" rows="3" styleClass="form-control input-sm"/></td>
          </tr>
          <tr>
            <td class="fieldName TOP">Documentation Version History:</td>
            <td><html:textarea name="systemBean" property="documentationVersionHistory" rows="3" styleClass="form-control input-sm"/></td>
          </tr>
        </table>
      </td></tr> <!-- end tan_table -->


      <tr><th>Notes</th></tr>
      <tr><td class="nobordered" align="left"> <!-- tan_table -->
        <html:textarea name="systemBean" property="notes" rows="5" styleClass="form-control input-sm"/>
      </td></tr> <!-- end tan_table -->


      <tr><th>Attached Files</th></tr>
      <tr><td class="nobordered" align="left"> <!-- tan_table -->
          <table class="file-list-table">
          <colgroup>
            <col/>
            <col/>
            <col width="25"/>
            <col/>
          </colgoup>
          <thead>
            <tr>
              <th colspan="2">File</th>
              <th>Delete</th>
              <th>Upload</th>
            </tr>
          </thead>
          <tbody>
            <logic:notEmpty name="systemBean" property="hardwareFileBean">
              <tr>
                <input type="hidden" name="hardwareFilePk" value="<bean:write name="systemBean" property="hardwareFileBean.filePk"/>"/>
                <td><img src="<bean:write name="systemBean" property="hardwareFileBean.smlImage"/>"/></td>
                <td><a href="download.do?filePk=<bean:write name="systemBean" property="hardwareFileBean.filePk"/>">Vessel Data Sheet</a></td>
                <td align="center"><input type="checkbox" name="hardwareFileDeletedInd" value="Y"/></td>
              </tr>
            </logic:notEmpty>
            <logic:empty name="systemBean" property="hardwareFileBean">
              <tr>
                <td><img src="images/icon_error.gif"/></td>
                <td style="color:#f00;">Vessel Data Sheet (Missing)</td>
                <td colspan="2"><input type="file" name="hardwareFile" size="45"/></td>
              </tr>
            </logic:empty>

            <logic:notEmpty name="systemBean" property="trainingFileBean">
              <tr>
                <input type="hidden" name="trainingFilePk" value="<bean:write name="systemBean" property="trainingFileBean.filePk"/>"/>
                <td><img src="<bean:write name="systemBean" property="trainingFileBean.smlImage"/>"/></td>
                <td><a href="download.do?filePk=<bean:write name="systemBean" property="trainingFileBean.filePk"/>">Training Sign-In Sheet</a></td>
                <td align="center"><input type="checkbox" name="trainingFileDeletedInd" value="Y"/></td>
              </tr>
            </logic:notEmpty>
            <logic:empty name="systemBean" property="trainingFileBean">
              <tr>
                <td><img src="images/icon_error.gif"/></td>
                <td style="color:#f00;">Training Sign-In Sheet (Missing)</td>
                <td colspan="2"><input type="file" name="trainingFile" size="45"/></td>
              </tr>
            </logic:empty>

            <logic:notEmpty name="systemBean" property="laptop1FileBean">
              <tr>
                <input type="hidden" name="laptop1FilePk" value="<bean:write name="systemBean" property="laptop1FileBean.filePk"/>"/>
                <td><img src="<bean:write name="systemBean" property="laptop1FileBean.smlImage"/>"/></td>
                <td><a href="download.do?filePk=<bean:write name="systemBean" property="laptop1FileBean.filePk"/>">FACET Laptop Configuration Info</a></td>
                <td align="center"><input type="checkbox" name="laptop1FileDeletedInd" value="Y"/></td>
              </tr>
            </logic:notEmpty>
            <logic:empty name="systemBean" property="laptop1FileBean">
              <tr>
                <td><img src="images/icon_error.gif"/></td>
                <td style="color:#f00;">FACET Laptop Configuration Info (Missing)</td>
                <td colspan="2"><input type="file" name="laptop1File" size="45"/></td>
              </tr>
            </logic:empty>

            <logic:notEmpty name="systemBean" property="laptop2FileBean">
              <tr>
                <input type="hidden" name="laptop2FilePk" value="<bean:write name="systemBean" property="laptop2FileBean.filePk"/>"/>
                <td><img src="<bean:write name="systemBean" property="laptop2FileBean.smlImage"/>"/></td>
                <td><a href="download.do?filePk=<bean:write name="systemBean" property="laptop2FileBean.filePk"/>">Trainer Laptop Checklist - Step 002</a></td>
                <td align="center"><input type="checkbox" name="laptop2FileDeletedInd" value="Y"/></td>
              </tr>
            </logic:notEmpty>
            <logic:empty name="systemBean" property="laptop2FileBean">
              <tr>
                <td><img src="images/icon_error.gif"/></td>
                <td style="color:#f00;">Trainer Laptop Checklist - Step 002 (Missing)</td>
                <td colspan="2"><input type="file" name="laptop2File" size="45"/></td>
              </tr>
            </logic:empty>

            <logic:notEmpty name="systemBean" property="postInstallFileBean">
              <tr>
                <input type="hidden" name="postInstallFilePk" value="<bean:write name="systemBean" property="postInstallFileBean.filePk"/>"/>
                <td><img src="<bean:write name="systemBean" property="postInstallFileBean.smlImage"/>"/></td>
                <td><a href="download.do?filePk=<bean:write name="systemBean" property="postInstallFileBean.filePk"/>">Post-Install Checklist</a></td>
                <td align="center"><input type="checkbox" name="postInstallFileDeletedInd" value="Y"/></td>
              </tr>
            </logic:notEmpty>
            <logic:empty name="systemBean" property="postInstallFileBean">
              <tr>
                <td><img src="images/icon_error.gif"/></td>
                <td style="color:#f00;">Post-Install Checklist (Missing)</td>
                <td colspan="2"><input type="file" name="postInstallFile" size="45"/></td>
              </tr>
            </logic:empty>

            <tr>
              <td colspan="4"><hr width="100%"/></td>
            </tr>

            <logic:iterate id="fileBean" name="systemBean" property="attachedFileList" type="com.premiersolutionshi.old.bean.FileBean">
              <tr>
                <td><img src="${fileBean.smlImage}"/></td>
                <td><a href="download.do?filePk=${fileBean.filePk}">${fileBean.filename}</a></td>
                <td align="center"><input type="checkbox" name="deleteFilePkArr" value="${fileBean.filePk}"/></td>
              </tr>
            </logic:iterate>
          </tbody>
          </table>

        <table id="newUploadFileTbl">
        <tbody></tbody>
        <tfoot><tr><td colspan="2" class="newRow"><img src="images/icon_plus.gif"/> <a href="javascript:void(0);" class="ibtnAdd">Add New File</a></td></tr></tfoot>
        </table>
      </td></tr> <!-- end tan_table -->
    </tbody>
    </table>

    <table id="borderlessTable" class="cellspacing-zero border-zero">
      <tbody>
        <tr>
          <td align="center">
            <% if (editType.equals("add")) { %>
            <button type="submit" value="Submit" class="btn btn-success">
              <span class="glyphicon glyphicon-ok"></span> Insert
            </button> <% } else { %>
            <button type="submit" value="Submit" class="btn btn-primary">
              <span class="glyphicon glyphicon-ok"></span> Save
            </button> <% } %>
          </td>
          <td align="center">
            <a class="btn btn-default"
              href="system.do?action=configuredSystemDetail&configuredSystemPk=${systemBean.configuredSystemPk}&projectPk=${projectPk}"><span
              class="glyphicon glyphicon-view"></span> View</a>
          </td>
          <td align="center">
            <a class="btn btn-default"
              href="system.do?action=configuredSystemList&projectPk=${projectPk}"><span
              class="glyphicon glyphicon-remove"></span> Cancel</a>
          </td>
        </tr>
      </tbody>
    </table>
    </div>
    </html:form>
  </div></div>

  <%@ include file="../include/app-col2.jsp" %>
</div></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-altcolor.js"></script>

<script type="text/javascript">
var laptopArr = {
  <logic:iterate id="laptopBean" name="laptopList" type="com.premiersolutionshi.old.bean.HardwareBean" indexId="laptopIndex">
    ${laptopBean.laptopPk}: [
      "${laptopBean.tag}",
      "${laptopBean.productName}",
      "${laptopBean.serialNumber}"
    ]
    <% if (laptopIndex < laptopList.size() - 1) { %>,<% } %>
  </logic:iterate>
};

  var scannerArr = {
<logic:iterate id="scannerBean" name="scannerList" type="com.premiersolutionshi.old.bean.HardwareBean" indexId="scannerListIndex">
${scannerBean.scannerPk}:
  ["${scannerBean.productName}",
  "${scannerBean.modelNumber}",
  "${scannerBean.serialNumber}"]<% if (scannerListIndex < scannerList.size() - 1) { %>,<% } %>
</logic:iterate>
  };

  var kofaxLicenseArr = {
<logic:iterate id="kofaxLicenseBean" name="kofaxLicenseList" type="com.premiersolutionshi.old.bean.SoftwareBean" indexId="kofaxIndex">
  ${kofaxLicenseBean.kofaxLicensePk}:
    ["<logic:iterate id="notes" name="kofaxLicenseBean" property="notesBr" indexId="kofaxNotesIndex">${notes}</logic:iterate>"]<% if (kofaxIndex < kofaxLicenseList.size() - 1) { %>,<% } %>
</logic:iterate>
  };

  var vrsLicenseArr = {
<logic:iterate id="vrsLicenseBean" name="vrsLicenseList" type="com.premiersolutionshi.old.bean.SoftwareBean" indexId="vrsLicenseIndex">
  '${vrsLicenseBean.vrsLicensePk}': ["<logic:iterate id="notes" name="vrsLicenseBean" property="notesBr" indexId="vrsNoteIndex">${notes} </logic:iterate>"]<% if (vrsLicenseIndex < vrsLicenseList.size() - 1) { %>,<% } %>
</logic:iterate>
  };

  $(function() {
    $(".datepicker").attr('autocomplete', 'off');
    $(".datepicker").datepicker();

    $('#newUploadFileTbl').on('click', '.ibtnAdd', function (event) {
      var newTrLine ='';
      newTrLine += '<tr>';
      newTrLine += '  <td><input type="file" name="fileList" size="70"/></td>';
      newTrLine += '  <td><input type="button" class="ibtnDel" value="Delete"/></td>';
      newTrLine += '</tr>';
      $('#newUploadFileTbl > tbody:last').append(newTrLine);
    });

    $('#newUploadFileTbl').on('click', '.ibtnDel', function (event) {
      $(this).closest('tr').remove();
    });

    $('#locationTbl').on('click', '.ibtnAdd', function (event) {
      var newTrLine ='';
      newTrLine += '<tr>';
      newTrLine += '  <td class="fieldName">Location:</td>';
      newTrLine += '  <td><input type="text" name="location" size="15"/></td>';

      newTrLine += '  <td class="fieldName" nowrap>As of:</td>';
      newTrLine += '  <td><input type="text" name="locationDate" size="9" id="currLocDate"/></td>';

      newTrLine += '  <td class="fieldName">Reason:</td>';
      newTrLine += '  <td><input type="text" name="reason" size="20"/></td>';

//        newTrLine += '  <!--td><input type=".docTable" class="ibtnDel" value="Cancel"/></td-->';
      newTrLine += '</tr>';

      $('#updateLocRow').hide();
      $('#locationTbl > tbody:first').prepend(newTrLine);

      $('#currLocDate').attr('autocomplete', 'off');
      $('#currLocDate').datepicker();

      document.getElementsByName("location")[0].focus();
    });

    $('#locationTbl').on('click', '.ibtnDel', function (event) {
      $('#updateLocRow').show();
      $(this).closest('tr').remove();
    });

    $('#multiShipInd').on('change', function(event) {
      if($(this).prop('checked')) {
        $('.multiShipTd').show();
      } else {
        $('.multiShipTd').hide();
      } //end of else
    });

  });

  $(document).ready(function () {
    <% if (editType.equals("add")) { %>
      //Set default values for new record
      document.systemForm.currKofaxProductName[0].selected = true;
      document.systemForm.currAccessVersion[0].selected = true;
    <% } %>

    checkNew(document.systemForm.currKofaxProductName, 'kofaxProductName');
    checkNew(document.systemForm.currAccessVersion, 'accessVersion');

    $('#laptopSelect').on('change', function() {
        $("#laptopSelect option:selected").each(function() {
          if ($(this).val()) {
            $("#laptopTag").text(laptopArr[$(this).val()][0]);
            $("#laptopProductName").text(laptopArr[$(this).val()][1]);
            $("#laptopSerialNumber").text(laptopArr[$(this).val()][2]);
            $('.laptopInfo').show();
          } else {
            $("#laptopTag").text("");
            $("#laptopProductName").text("");
            $("#laptopSerialNumber").text("");
            $('.laptopInfo').hide();
          }
        });
    }).change();

    $('#scannerSelect').on('change', function() {
      $("#scannerSelect option:selected").each(function() {
        if ($(this).val()) {
          $("#scannerProductName").text(scannerArr[$(this).val()][0]);
          $("#scannerModelNumber").text(scannerArr[$(this).val()][1]);
          $("#scannerSerialNumber").text(scannerArr[$(this).val()][2]);
          $('.scannerInfo').show();
        } else {
          $("#scannerProductName").text("");
          $("#scannerModelNumber").text("");
          $("#scannerSerialNumber").text("");
          $('.scannerInfo').hide();
        }
      });
    }).change();

    $('#kofaxLicenseSelect').on('change', function() {
        $("#kofaxLicenseSelect option:selected").each(function() {
          if ($(this).val()) {
            $("#kofaxLicenseNotes").text(kofaxLicenseArr[$(this).val()][0]);
          } else {
            $("#kofaxLicenseNotes").text("");
          }
        });
    }).change();

    $('#vrsLicenseSelect').on('change', function() {
        $("#vrsLicenseSelect option:selected").each(function() {
          if ($(this).val()) {
            $("#vrsLicenseNotes").text(vrsLicenseArr[$(this).val()][0]);
          } else {
            $("#vrsLicenseNotes").text("");
          }
        });
    }).change();

    if($('#multiShipInd').prop('checked')) {
      $('.multiShipTd').show();
    } else {
      $('.multiShipTd').hide();
    } //end of else

    <logic:equal name="editType" value="add">
    document.systemForm.uic.focus();
    </logic:equal>
  });

  function valFields() {
    var laptopPk = document.systemForm.laptopPk.value;
    var scannerPk = document.systemForm.scannerPk.value;
    var kofaxLicensePk = document.systemForm.kofaxLicensePk.value;
    var vrsLicensePk = document.systemForm.vrsLicensePk.value;
    var contractNumber = document.systemForm.contractNumber.value;

    if (laptopPk.length < 1 && scannerPk.length < 1) {
      alert('You must select either a laptop or scanner to configure.');
      document.systemForm.laptopPk.focus();
      return false;
    } //end of if

    if (contractNumber.length < 1 && contractNumber.length < 1) {
      alert('You must enter a Contract Number.');
      document.systemForm.currContractNumber.focus();
      return false;
    }

    if (laptopPk.length > 0 && kofaxLicensePk.length < 1) {
      alert('You must select select a Kofax license.');
      document.systemForm.kofaxLicensePk.focus();
      return false;
    } //end of if

    if (laptopPk.length > 0 && vrsLicensePk.length < 1) {
      alert('You must select select a VRS license.');
      document.systemForm.vrsLicensePk.focus();
      return false;
    } //end of if

    return true;
  } //end of valFields

  function checkNew(currObj, elementName) {
    if (currObj.value == 'null') {
      document.getElementsByName(elementName)[0].value = '';
      document.getElementsByName(elementName)[0].style.display = 'inline';
      document.getElementsByName(elementName)[0].focus();
    } else {
      document.getElementsByName(elementName)[0].value = currObj.value;
      document.getElementsByName(elementName)[0].style.display = 'none';
    } //end of if
  } //end of checkNew

  function clearUpload(divName, objName) {
    document.getElementsByName(objName)[0].value = '';
    document.getElementById(divName + "_1").style.display = 'inline';
    document.getElementById(divName + "_2").style.display = 'none';
    return false;
  } //end of clearUpload
</script>

</body>
</html>
