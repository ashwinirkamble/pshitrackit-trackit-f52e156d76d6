<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<jsp:useBean id="resultList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="type" scope="request" class="java.lang.String"/>
<jsp:useBean id="msoFooterData" scope="request" class="java.lang.String"/>
<jsp:useBean id="inputBean" scope="request" class="com.premiersolutionshi.old.bean.BackfileBean"/>

<bean:define id="defaultPageTitle" value="FACET Backfile Workflow Export"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>

<%
	response.setHeader("Pragma", "public");
	response.setHeader("Cache-Control", "max-age=0");
	response.setContentType("application/vnd.ms-excel");

	StringBuffer filename = new StringBuffer("facet_backfile_workflow_export_" + type);
	if (!CommonMethods.isEmpty(inputBean.getContractNumber())) filename.append("_" + inputBean.getContractNumber());
	filename.append(".xls");

	response.setHeader("Content-Disposition", "attachment;filename=" + filename.toString());
%>

<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40">

<head>
<style>
<!--
table
@page {
	mso-header-data: "&C<bean:write name="pageTitle"/>";
	mso-footer-data: "&L<bean:write name="msoFooterData"/>&R&P of &N";
	margin: 1.0in .75in 1.0in .75in;
	mso-header-margin: .5in;
	mso-footer-margin: .5in;
}

BODY {
	background-color: #FFFFFF;
}

tr {
	mso-height-source: auto;
}

col {
	mso-width-source: auto;
}

br {
	mso-data-placement: same-cell;
}

.style0 {
	mso-number-format: General;
	text-align: general;
	vertical-align: top;
	white-space: nowrap;
	mso-rotate: 0;
	mso-background-source: auto;
	mso-pattern: auto;
	color: windowtext;
	font-size: 10pt;
	font-style: normal;
	text-decoration: none;
	font-family: Calibri, Arial;
	mso-generic-font-family: auto;
	mso-font-charset: 0;
	mso-protection: locked visible;
	mso-style-name: Normal;
	mso-style-id: 0;
	padding: 3px;
	mso-ignore: padding;
}

td {
	mso-style-parent: style0;
}

#xlsTable th {
	background-color: #e4dfec;
	color: black;
	font-weight: bold;
	border: .5pt solid #000;
	vertical-align: bottom;
	}

#xlsTable td {
	mso-style-parent: style0;
	border: .5pt solid #ccc;
	}

#xlsTable .rightBorder {
	border-right: .5pt solid black;
	}

#xlsTable td.numRow {
	mso-number-format:"\#\,\#\#0";
	}

#xlsTable td.txtRow {
	mso-number-format: "\@";
	}
-->
</style>
</head>

<body>
<table id="xlsTable" border="0" cellspacing="3" cellpadding="3">
<colgroup>
	<col span="22"/>
	<col span="23" style="width:115px;"/>
</colgroup>
<thead>
	<tr>
		<th colspan="4">Unit Information</th>
		<th colspan="9">PSHI Boxes Received</th>
		<th colspan="9">Boxes Sent to Scanning</th>
		<th colspan="4">General Information</th>
		<th colspan="3">Initial Workflow</th>
		<th colspan="2">FY16</th>
		<th colspan="2">FY15</th>
		<th colspan="2">FY14/13 Scanning</th>
		<th colspan="1">FY12/11</th>
		<th colspan="6">Final Steps</th>
		<th colspan="3">Return to Unit</th>

		<th rowspan="2">Comments</th>
	</tr>
	<tr>
		<th>Unit Name</th>
		<th>Type/Hull</th>
		<th>TYCOM</th>
		<th>Homeport</th>

		<th>FY16</th>
		<th>FY15</th>
		<th>FY14</th>
		<th>FY13</th>
		<th>FY12</th>
		<th>FY11</th>
		<th>FY10</th>
		<th>Other / Reports</th>
		<th>Total</th>

		<th>FY16</th>
		<th>FY15</th>
		<th>FY14</th>
		<th>FY13</th>
		<th>FY12</th>
		<th>FY11</th>
		<th>FY10</th>
		<th>Other / Reports</th>
		<th>Total</th>

		<th>Overall<br/>Due Date</th>
		<th>FY14/13<br/>Due Date</th>
		<th>Scheduled<br/>Training<br/>Date</th>
		<th>Completed<br/>Date</th>

		<th>Backfile<br/>Date<br/>Requested</th>
		<th>Date<br/>Received<br/>By PSHI</th>
		<th>Date<br/>Delivered<br/>to Scanning</th>

		<th>FY16<br/>Completed<br/>Date</th>
		<th>FY16<br/>CD Sent for<br/>Customer<br/>Date</th>

		<th>FY15<br/>Completed<br/>Date</th>
		<th>FY15<br/>CD Sent for<br/>Customer<br/>Date</th>

		<th>FY14/13<br/>Completed<br/>Date</th>
		<th>FY14/13<br/>CD Sent for<br/>Customer<br/>Date</th>

		<th>FY12/11<br/>Completed<br/>Date</th>

		<th>Extract File<br/>Created</th>
		<th>All backfile<br/>CD<br/>mailed to<br/>Cust/SD</th>
		<th>Verify All<br/>Backfiles<br/>Installed<br/>in FACET</th>
		<th>All Backfile<br/>CD<br/>Delivered to<br/>LogCop</th>
		<th>Verified<br/>LOGCOP<br/>Backfile<br/>Uploaded</th>
		<th>Final Backfile<br/>Report<br/>Generated<br/>and Given to<br/>CompacFlt</th>

		<th>Return<br/>to</br>Unit</th>
		<th>Backfile<br/>Returned<br/>Date</th>
		<th>Client<br/>Received<br/>Confirmation<br/>Date</th>
	</tr>
</thead>
<tbody>
<logic:iterate id="resultBean" name="resultList" type="com.premiersolutionshi.old.bean.BackfileBean">
	<tr>
		<td><bean:write name="resultBean" property="shipName"/></td>
		<td><bean:write name="resultBean" property="type"/> <bean:write name="resultBean" property="hull"/></td>
		<td><bean:write name="resultBean" property="tycomDisplay"/></td>
		<td class="rightBorder"><bean:write name="resultBean" property="homeport"/></td>

		<td align="center"><bean:write name="resultBean" property="fy16PshiBoxCnt"/></td>
		<td align="center"><bean:write name="resultBean" property="fy15PshiBoxCnt"/></td>
		<td align="center"><bean:write name="resultBean" property="fy14PshiBoxCnt"/></td>
		<td align="center"><bean:write name="resultBean" property="fy13PshiBoxCnt"/></td>
		<td align="center"><bean:write name="resultBean" property="fy12PshiBoxCnt"/></td>
		<td align="center"><bean:write name="resultBean" property="fy11PshiBoxCnt"/></td>
		<td align="center"><bean:write name="resultBean" property="fy10PshiBoxCnt"/></td>
		<td align="center"><bean:write name="resultBean" property="otherPshiBoxCnt"/></td>
		<td align="center" class="rightBorder"><b><bean:write name="resultBean" property="totalPshiBoxCnt"/></b></td>

		<td align="center"><bean:write name="resultBean" property="fy16BoxCnt"/></td>
		<td align="center"><bean:write name="resultBean" property="fy15BoxCnt"/></td>
		<td align="center"><bean:write name="resultBean" property="fy14BoxCnt"/></td>
		<td align="center"><bean:write name="resultBean" property="fy13BoxCnt"/></td>
		<td align="center"><bean:write name="resultBean" property="fy12BoxCnt"/></td>
		<td align="center"><bean:write name="resultBean" property="fy11BoxCnt"/></td>
		<td align="center"><bean:write name="resultBean" property="fy10BoxCnt"/></td>
		<td align="center"><bean:write name="resultBean" property="otherBoxCnt"/></td>
		<td align="center" class="rightBorder"><b><bean:write name="resultBean" property="totalBoxCnt"/></b></td>

		<td align="right" class="rightBorder"><bean:write name="resultBean" property="dueDate"/></td>
		<td align="right"><bean:write name="resultBean" property="fy1314DueDate"/></td>
		<td align="right"><bean:write name="resultBean" property="schedTrainingDate"/></td>
		<td align="right"><bean:write name="resultBean" property="completedDate"/></td>

		<td align="right" style="<bean:write name="resultBean" property="requestedDateCss"/>"><bean:write name="resultBean" property="requestedDate"/></td>
		<td align="right" style="<bean:write name="resultBean" property="receivedDateCss"/>"><bean:write name="resultBean" property="receivedDate"/></td>
		<td align="right" style="<bean:write name="resultBean" property="scanningDeliveredDateCss"/>"><bean:write name="resultBean" property="scanningDeliveredDate"/></td>

		<td align="right" style="<bean:write name="resultBean" property="fy16CompletedDateCss"/>"><bean:write name="resultBean" property="fy16CompletedDate"/></td>
		<td align="right" style="<bean:write name="resultBean" property="fy16MailedDateCss"/>"><bean:write name="resultBean" property="fy16MailedDate"/></td>

		<td align="right" style="<bean:write name="resultBean" property="fy15CompletedDateCss"/>"><bean:write name="resultBean" property="fy15CompletedDate"/></td>
		<td align="right" style="<bean:write name="resultBean" property="fy15MailedDateCss"/>"><bean:write name="resultBean" property="fy15MailedDate"/></td>

		<td align="right" style="<bean:write name="resultBean" property="fy1314CompletedDateCss"/>"><bean:write name="resultBean" property="fy1314CompletedDate"/></td>
		<td align="right" style="<bean:write name="resultBean" property="fy1314BurnedDateCss"/>"><bean:write name="resultBean" property="fy1314BurnedDate"/></td>

		<td align="right" style="<bean:write name="resultBean" property="fy1112CompletedDateCss"/>"><bean:write name="resultBean" property="fy1112CompletedDate"/></td>

		<td align="right" style="<bean:write name="resultBean" property="extractDateCss"/>"><bean:write name="resultBean" property="extractDate"/></td>
		<td align="right" style="<bean:write name="resultBean" property="fy1314MailedDateCss"/>"><bean:write name="resultBean" property="fy1314MailedDate"/></td>
		<td align="right" style="<bean:write name="resultBean" property="laptopInstalledDateCss"/>"><bean:write name="resultBean" property="laptopInstalledDate"/></td>
		<td align="right" style="<bean:write name="resultBean" property="logcopDeliveredDateCss"/>"><bean:write name="resultBean" property="logcopDeliveredDate"/></td>
		<td align="right" style="<bean:write name="resultBean" property="logcopUploadedDateCss"/>"><bean:write name="resultBean" property="logcopUploadedDate"/></td>
		<td align="right" style="<bean:write name="resultBean" property="finalReportDateCss"/>"><bean:write name="resultBean" property="finalReportDate"/></td>

		<logic:equal name="resultBean" property="returnInd" value="Y">
			<td align="center">Y</td>
			<td align="right" style="<bean:write name="resultBean" property="returnedDateCss"/>"><bean:write name="resultBean" property="returnedDate"/></td>
			<td align="right" style="<bean:write name="resultBean" property="returnConfirmedDateCss"/>"><bean:write name="resultBean" property="returnConfirmedDate"/></td>
		</logic:equal>

		<logic:notEqual name="resultBean" property="returnInd" value="Y">
			<td align="center">N</td>
			<td style="background:#999;"></td>
			<td style="background:#999;"></td>
		</logic:notEqual>

		<td class="txtRow">
			<logic:iterate id="comments" name="resultBean" property="commentsBr" indexId="i">
				<bean:write name="comments"/><% if (i+1 < resultBean.getCommentsBr().size()) { %><br/><% } %>
			</logic:iterate>
		</td>
	</tr>
</logic:iterate>
</tbody>
</table>
</body>
</html>
