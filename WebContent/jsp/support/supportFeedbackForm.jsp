<!DOCTYPE html>
<%@ page language="java" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<jsp:useBean id="inputBean" scope="request" class="com.premiersolutionshi.old.bean.SupportBean"/>
<jsp:useBean id="shipBean" scope="request" class="com.premiersolutionshi.old.bean.ShipBean"/>

<jsp:useBean id="configuredSystemList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="shipPocList" scope="request" class="java.util.ArrayList"/>

<jsp:useBean id="transmittalSummaryBean" scope="request" class="com.premiersolutionshi.old.bean.ReportBean"/>
<jsp:useBean id="missingTransmittalStr" scope="request" class="java.lang.String"/>

<jsp:useBean id="currFacetVersion" scope="request" class="java.lang.String"/>
<jsp:useBean id="currDmsVersion"   scope="request" class="java.lang.String"/>

<jsp:useBean id="generatedDate" scope="request" class="java.lang.String"/>
<html>
<head>
  <title></title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
  <style>
    @media screen {
      body {
        margin:0;
        padding:0;
        font: 12px Arial, Helvetica,"Lucida Grande", serif;
        width: 650px;
        text-align:left;
        }
    }

    @media print {
      body {
        margin:0;
        padding:0;
        line-height: 1.4em;
        word-spacing:1px;
        letter-spacing:0.2px;
        font: 8pt Arial, Helvetica,"Lucida Grande", serif;
        color: #000;
        text-align: left;
        }
    }

    #vesselInfo { padding: 5px 0 10px 0; }
    #purpose { padding: 5px 0 10px 0; }
    #hardware { padding: 5px 0 10px 0; }
    #software { padding: 5px 0 10px 0; }
    #tasks { padding: 5px 0; }
    #transmittalSummary { padding: 5px 0; }

    #title { vertical-align:bottom; font-weight:bold; font-size:16pt; text-align:right; }
    #generatedDate { vertical-align:bottom; font-size:7pt; text-align:right; }

    #vesselField { font-weight: bold; padding: 15px 5px 0 0; vertical-align: bottom; }
    #vesselValue { padding-left: 7px; border-bottom: 1px solid black; width: 230px; vertical-align: bottom; }

    #dateField { font-weight: bold; padding: 15px 5px 0 20px; vertical-align: bottom; }
    #dateValue { padding-left: 7px; border-bottom: 1px solid black; width: 100px; vertical-align: bottom; }

    #pocField { font-weight: bold; padding: 15px 5px 0 0; vertical-align: bottom; }
    #pocValue { padding-left: 7px; border-bottom: 1px solid black; width: 185px; vertical-align: bottom; }

    #locationField { font-weight: bold; padding: 15px 5px 0 150px; vertical-align: bottom; }
    #locationValue { padding-left: 7px; border-bottom: 1px solid black; width: 130px; vertical-align: bottom; }

    #rsupplyField { font-weight: bold; padding: 15px 5px 0 20px; vertical-align: bottom; }
    #rsupplyValue { padding-left: 7px; border-bottom: 1px solid black; width: 70px; vertical-align: bottom; }

    #tasklist { line-height: 15pt; padding-left: 15px; }

    table { margin: 0; padding: 0; border: 0; }

    .borderlessTable td { padding: 2px 10px; }
    .purposeTable td { padding: 0 5px; }
    #pocTable td { font-size:8pt; padding: 0 3px; }

    .fieldName { text-align: right; color: #999; }

    td.borderRight { border-right: 5px solid #777; }

    hr { display: block; height: 1px; background: transparent; width: 100%; border: none; border-top: 5px solid #777; padding: 0; margin: 0; }
  </style>
</head>
<body>
  <div id="header">
    <table width="100%" border="0" cellspacing="0">
    <tbody>
      <tr>
        <td width="185" rowspan="2"><img src="images/logo_pshi.png" width="180"/></td>
        <td id="title">TRAINING/SUPPORT FEEDBACK FORM</td>
      </tr>
      <tr>
        <td id="generatedDate">Generated on <bean:write name="generatedDate"/></td>
      </tr>
    </tbody>
    </table>
  </div>

  <hr/>

  <div id="vesselInfo">
    <table class="border-zero cellspacing-zero">
    <tbody>
      <tr>
        <td id="vesselField" nowrap>VESSEL NAME:</td>
        <td id="vesselValue" nowrap><bean:write name="shipBean" property="shipNameTypeHull"/></td>
        <td id="rsupplyField" nowrap>RSupply:</td>
        <td id="rsupplyValue" nowrap><bean:write name="shipBean" property="rsupply"/></td>
        <td id="dateField" nowrap>Date:</td>
        <td id="dateValue" nowrap>
          <bean:write name="inputBean" property="supportVisitDate"/>
          <bean:write name="inputBean" property="supportVisitTime"/>
        </td>
      </tr>
    </tbody>
    </table>

    <table>
    <tbody>
      <tr>
        <td id="pocField" nowrap>PSHI CONTACT:</td>
        <td id="pocValue" nowrap>
          <logic:present name="inputBean" property="pocArr">
          <logic:iterate id="poc" name="inputBean" property="pocArr" indexId="i">
            <bean:write name="poc"/><% if (i < inputBean.getPocArr().length-1) { %>,<% } %>
          </logic:iterate>
          </logic:present>
        </td>
        <td id="locationField" nowrap>Location:</td>
        <td id="locationValue" nowrap>
          <logic:notEmpty name="inputBean" property="supportVisitLoc">
          <bean:write name="inputBean" property="supportVisitLoc"/>
          </logic:notEmpty>
          <logic:empty name="inputBean" property="supportVisitLoc">
          <bean:write name="shipBean" property="homeport"/>
          </logic:empty>
        </td>
      </tr>
    </tbody>
    </table>
  </div>

  <hr/>

  <div id="purpose">
  <b>PURPOSE:</b>

  <table class="border-zero cellspacing-zero" cellpadding="5">
  <tbody>
    <tr valign="bottom">
      <td class="checkbox" style="font-size: 1.4em;">&#9633;</td>
      <td class="borderRight"><b>Training/Follow-Up Training</b></td>
      <td class="checkbox" style="font-size: 1.4em;">&#9633;</td>
      <td class="borderRight"><b>Support / Sustainment</b></td>
      <td><b>Vessel POCs</b></td>
    </tr>
    <tr valign="top">
      <td></td>
      <td class="borderRight">
        <table class="border-zero cellspacing-zero cellpadding-zero">
        <tbody>
          <tr>
            <td nowrap><b>Start: ________</b>&nbsp;&nbsp;</td>
            <td nowrap><b>End: ________</b></td>
          </tr>
          <tr>
            <td>Post to R-Supply</td>
            <td>
              Yes <span style="font-size:1.4em;;">&#9633;</span>
              &nbsp;
              No <span style="font-size:1.4em;;">&#9633;</span>
            </td>
          </tr>
          <tr>
            <td>Upload to LOGCOP&nbsp;</td>
            <td>
              Yes <span style="font-size:1.4em;;">&#9633;</span>
              &nbsp;
              No <span style="font-size:1.4em;;">&#9633;</span>
            </td>
          </tr>
          <tr>
            <td>Receipt to FSM</td>
            <td>
              Yes <span style="font-size: 1.4em;">&#9633;</span>
              &nbsp;
              No <span style="font-size:1.4em;;">&#9633;</span>
            </td>
          </tr>
        </tbody>
        </table>
      </td>
      <td></td>
      <td class="borderRight" nowrap>
        <table class="border-zero cellspacing-zero cellpadding-zero">
        <tbody>
          <tr>
            <td nowrap><b>Start: ________</b>&nbsp;&nbsp;</td>
            <td nowrap><b>End: ________</b></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td colspan="2">Number of attendees: _______</td>
          </tr>
        </tbody>
        </table>
      </td>
      <td>
        <logic:empty name="shipPocList">
          No POCs on record
        </logic:empty>
        <logic:notEmpty name="shipPocList">
          <table id="pocTable" border="0" cellspacing="0">
          <tbody>
            <logic:iterate id="shipPocBean" name="shipPocList" type="com.premiersolutionshi.old.bean.UserBean">
              <tr>
              <td>
                <bean:write name="shipPocBean" property="rank"/>
                <bean:write name="shipPocBean" property="lastName"/>
              </td>
              <td>
                <logic:notEmpty name="shipPocBean" property="workNumber">
                <bean:write name="shipPocBean" property="workNumber"/>
                </logic:notEmpty>
              </td>
            </logic:iterate>
          </tbody>
          </table>
        </logic:notEmpty>
      </td>
  </tbody>
  </table>
  </div>

  <hr/>

  <div id="hardware">
    <b>HARDWARE:</b>

    <p>
    <table class="purposeTable" border="0" cellspacing="0">
    <tbody>
      <tr>
        <td>Scanner cleaned and working properly</td>
        <td>
          Yes <span style="font-size: 1.4em;">&#9633;</span>
          &nbsp;
          No <span style="font-size:1.4em;;">&#9633;</span>
        </td>
      </tr>
      <tr>
        <td>Laptop checked and working properly</td>
        <td>
          Yes <span style="font-size: 1.4em;">&#9633;</span>
          &nbsp;
          No <span style="font-size:1.4em;;">&#9633;</span>
        </td>
      </tr>
    </tbody>
    </table>
    </p>

    <b>Comments/Recommendations:</b>
  </div>

  <hr/>

  <div id="software">
    <b>SOFTWARE:</b>

    <p>
    <table class="borderlessTable" border="0" cellspacing="0">
    <tbody>
      <tr>
        <td class="fieldName">Computer Name:</td>
        <logic:iterate id="configuredSystemBean" name="configuredSystemList" type="com.premiersolutionshi.old.bean.SystemBean">
          <td>
            <bean:write name="configuredSystemBean" property="computerName"/>
            <logic:empty name="configuredSystemBean" property="computerName">--</logic:empty>
          </td>
        </logic:iterate>
      </tr>

      <tr>
        <td class="fieldName" nowrap>FACET Version:</td>
      <logic:iterate id="configuredSystemBean" name="configuredSystemList" type="com.premiersolutionshi.old.bean.SystemBean">
        <td>
          <logic:notEmpty name="configuredSystemBean" property="facetVersion">
            <bean:write name="configuredSystemBean" property="facetVersion"/>
            <% if (configuredSystemBean.getFacetVersion().equals(currFacetVersion)) { %>
              <img src="images/checkbox_checked.png" height="10px"/>
            <% } else { %>
              <img src="images/icon_error.gif" height="10px"/>
            <% } %>
          </logic:notEmpty>
          <logic:empty name="configuredSystemBean" property="facetVersion">--</logic:empty>
        </td>
      </logic:iterate>
      </tr>

      <tr>
        <td class="fieldName" nowrap>DMS List as of:</td>
        <logic:iterate id="configuredSystemBean" name="configuredSystemList" type="com.premiersolutionshi.old.bean.SystemBean">
          <td>
            <logic:notEmpty name="configuredSystemBean" property="dmsVersion">
              <bean:write name="configuredSystemBean" property="dmsVersion"/>
              <% if (configuredSystemBean.getDmsVersion().equals(currDmsVersion)) { %>
                <img src="images/checkbox_checked.png" height="10px"/>
              <% } else { %>
                <img src="images/icon_error.gif" height="10px"/>
              <% } %>
            </logic:notEmpty>
            <logic:empty name="configuredSystemBean" property="dmsVersion">--</logic:empty>
          </td>
        </logic:iterate>
      </tr>

      <tr>
        <td>&nbsp;</td>
      </tr>

      <tr>
        <td class="fieldName" nowrap>Network Adapter Type:</td>
          <logic:iterate id="configuredSystemBean" name="configuredSystemList" type="com.premiersolutionshi.old.bean.SystemBean">
            <td>
              <logic:notEmpty name="configuredSystemBean" property="networkAdapter">
                <bean:write name="configuredSystemBean" property="networkAdapter"/>
                <% if (configuredSystemBean.getNetworkAdapter().equals("MS Loopback Adapter")) { %>
                  <img src="images/checkbox_checked.png" height="10px"/>
                <% } else { %>
                  <img src="images/icon_error.gif" height="10px"/>
                <% } %>
              </logic:notEmpty>
              <logic:empty name="configuredSystemBean" property="networkAdapter">--</logic:empty>
            </td>
          </logic:iterate>
      </tr>

      <tr>
        <td class="fieldName" nowrap>Admin password:</td>
        <logic:iterate id="configuredSystemBean" name="configuredSystemList" type="com.premiersolutionshi.old.bean.SystemBean">
          <td>
            <bean:write name="configuredSystemBean" property="adminPassword"/>
            <logic:empty name="configuredSystemBean" property="adminPassword">--</logic:empty>
          </td>
        </logic:iterate>
      </tr>
    </tbody>
    </table>
    </p>

    <b>Comments/Recommendations:</b>
  </div>

  <hr/>

  <div id="tasks" style="font-weight:bold;">
    GENERAL FEEDBACK & OTHER COMMENTS:<br/>
    <p>
    Follow-Up Issues:
    <div id="tasklist">
      <logic:present name="inputBean" property="taskArr">
      <logic:iterate id="task" name="inputBean" property="taskArr">
        <span style="font-size:1.4em;;">&#9633;</span>
        <bean:write name="task"/><br/>
      </logic:iterate>
      </logic:present>
      <logic:notEmpty name="transmittalSummaryBean" property="lastTransmittalNum">
        <logic:notEmpty name="missingTransmittalStr">
          <span style="font-size:1.4em;;">&#9633;</span>
          Missing Transmittals: <bean:write name="missingTransmittalStr"/><br/>
        </logic:notEmpty>

        <span style="font-size:1.4em;;">&#9633;</span>
        Latest Transmittal: #<bean:write name="transmittalSummaryBean" property="lastTransmittalNum"/><br/>
      </logic:notEmpty>
      <logic:empty name="transmittalSummaryBean" property="lastTransmittalNum">
        <span style="font-size:1.4em;;">&#9633;</span>
        No activity<br/>
      </logic:empty>
    </div>
    </p>

    <p>Enhance Feedback:</p>

    Questions/Comments:
  </div>

  <hr/>

  <div id="transmittalSummary">
    <b>LOGCOP ACTIVITY:</b>

    <p>
    Material 1348s (required every 10 days -
    <logic:notEmpty name="transmittalSummaryBean" property="form1348UploadDate">
      last upload <bean:write name="transmittalSummaryBean" property="form1348UploadDate"/>)
    </logic:notEmpty>
    <logic:empty name="transmittalSummaryBean" property="form1348UploadDate">no upload history)</logic:empty><br/>

    Fuel 1149s (required by the 7th of each month -
    <logic:notEmpty name="transmittalSummaryBean" property="form1149UploadDate">
      last upload <bean:write name="transmittalSummaryBean" property="form1149UploadDate"/>)
    </logic:notEmpty>
    <logic:empty name="transmittalSummaryBean" property="form1149UploadDate">no upload history)</logic:empty><br/>

    Food Receipts (required every 10 days -
    <logic:notEmpty name="transmittalSummaryBean" property="foodReceiptUploadDate">
      last upload <bean:write name="transmittalSummaryBean" property="foodReceiptUploadDate"/>)
    </logic:notEmpty>
    <logic:empty name="transmittalSummaryBean" property="foodReceiptUploadDate">no upload history)</logic:empty><br/>

    Food Requisitions (required every 10 days -
    <logic:notEmpty name="transmittalSummaryBean" property="foodApprovalUploadDate">
      last upload <bean:write name="transmittalSummaryBean" property="foodApprovalUploadDate"/>)
    </logic:notEmpty>
    <logic:empty name="transmittalSummaryBean" property="foodApprovalUploadDate">no upload history)</logic:empty><br/>

    Purchase Card - Admin Files (required within the past year -
    <logic:notEmpty name="transmittalSummaryBean" property="pcardAdminUploadDate">
      last upload <bean:write name="transmittalSummaryBean" property="pcardAdminUploadDate"/>)
    </logic:notEmpty>
    <logic:empty name="transmittalSummaryBean" property="pcardAdminUploadDate">no upload history)</logic:empty><br/>

    Purchase Card - Invoice Files (required by the 7th of each month -
    <logic:notEmpty name="transmittalSummaryBean" property="pcardInvoiceUploadDate">
      last upload <bean:write name="transmittalSummaryBean" property="pcardInvoiceUploadDate"/>)
    </logic:notEmpty>
    <logic:empty name="transmittalSummaryBean" property="pcardInvoiceUploadDate">no upload history)</logic:empty><br/>

    Price Change Reports (required by the 7th of each month -
    <logic:notEmpty name="transmittalSummaryBean" property="priceChangeUploadDate">
      last upload <bean:write name="transmittalSummaryBean" property="priceChangeUploadDate"/>)
    </logic:notEmpty>
    <logic:empty name="transmittalSummaryBean" property="priceChangeUploadDate">no upload history)</logic:empty><br/>

    SFOEDL Reports (required by the 7th of each month -
    <logic:notEmpty name="transmittalSummaryBean" property="sfoedlUploadDate">
      last upload <bean:write name="transmittalSummaryBean" property="sfoedlUploadDate"/>)
    </logic:notEmpty>
    <logic:empty name="transmittalSummaryBean" property="sfoedlUploadDate">no upload history)</logic:empty><br/>

    <% if (!shipBean.getType().equals("SSN") && !shipBean.getType().equals("SSBN") && !shipBean.getType().equals("SSGN")) { %>
      UOL Reports (required by the 7th of each month -
      <logic:notEmpty name="transmittalSummaryBean" property="uolUploadDate">
        last upload <bean:write name="transmittalSummaryBean" property="uolUploadDate"/>)
      </logic:notEmpty>
      <logic:empty name="transmittalSummaryBean" property="uolUploadDate">no upload history)</logic:empty><br/>
    <% } %>
    </p>
  </div>
</body>
</html>
