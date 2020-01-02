<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<jsp:useBean id="resultList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="type" scope="request" class="java.lang.String"/>
<jsp:useBean id="msoFooterData" scope="request" class="java.lang.String"/>
<jsp:useBean id="inputBean" scope="request" class="com.premiersolutionshi.old.bean.TrainingBean"/>

<bean:define id="defaultPageTitle" value="Training Workflow"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>

<%
	String filename = CommonMethods.isEmpty(inputBean.getContractNumber()) ? "training_workflow_" + type + ".xls" : "training_workflow_" + type + "_" + inputBean.getContractNumber() + ".xls";

	response.setHeader("Pragma", "public");
	response.setHeader("Cache-Control", "max-age=0");
	response.setContentType("application/vnd.ms-excel");
 	response.setHeader("Content-Disposition", "attachment;filename=" + filename);
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
	vertical-align: middle;
	white-space: nowrap;
	mso-rotate: 0;
	mso-background-source: auto;
	mso-pattern: auto;
	color: windowtext;
	font-size: 9pt;
	font-style: normal;
	text-decoration: none;
	font-family: Calibri;
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
	background-color: #97cbfd;
	color: black;
	font-weight: bold;
	border: .5pt solid black;
	vertical-align: bottom;
	}

#xlsTable td {
	mso-style-parent: style0;
	border: .5pt solid #ccc;
	}

#xlsTable td.numRow {
	mso-number-format:"\#\,\#\#0";
	}

#xlsTable td.txtRow {
	mso-number-format: "\@";
	}

#xlsTable .endSection {
	border-right: 1pt solid #000;
	}

#xlsTable .noborder {
	border: none;
	}
-->
</style>
</head>

<body>
<table id="xlsTable" border="0" cellspacing="3" cellpadding="3">
<colgroup>
	<col span="4"/>
	<col span="14" width="73"/>
</colgroup>
<thead>
	<tr>
	<th colspan="5" class="endSection">Ship Information</th>
	<th colspan="2" class="endSection">Backfile</th>
	<th colspan="11">Training Workflow</th>
	</tr>
	<tr>
		<th>Unit Name</th>
		<th>Type/Hull</th>
		<th>TYCOM</th>
		<th>Homeport</th>
		<th class="endSection">RSupply<br/>Version</th>

		<th>Received Date</th>
		<th class="endSection">Completed Date</th>

		<th>Location<br/>File<br/>Received</th>
		<th>Location<br/>File<br/>Reviewed</th>
		<th>PacFlt<br/>Sent<br/>Notification<br/>to Send<br/>Food Report</th>
		<th>System<br/>Shipped</th>
		<th>Computer<br/>Name<br/>in Database</th>
		<th>Computer<br/>Name<br/>Provided to<br/>Logcop</th>
		<th>Training Kit<br/>Ready</th>
		<th>Estimated<br/>Training<br/>Month</th>
		<th>Scheduled<br/>Training<br/>Date</th>
		<th>Actual<br/>Training<br/>Date</th>
		<th>Confirmed<br/>By Client</th>
	</tr>
</thead>
<tbody>
<logic:iterate id="resultBean" name="resultList" type="com.premiersolutionshi.old.bean.TrainingBean">
	<tr>
		<td><bean:write name="resultBean" property="shipName"/></td>
		<td>
			<bean:write name="resultBean" property="type"/>
			<bean:write name="resultBean" property="hull"/>
		</td>
		<td><bean:write name="resultBean" property="tycomDisplay"/></td>
		<td><bean:write name="resultBean" property="homeport"/></td>
		<td class="endSection"><bean:write name="resultBean" property="rsupply"/></td>

		<td><bean:write name="resultBean" property="backfileRecvDate"/></td>
		<td class="endSection"><bean:write name="resultBean" property="backfileCompletedDate"/></td>

		<td><bean:write name="resultBean" property="locFileRecvDate"/></td>
		<td><bean:write name="resultBean" property="locFileRevDate"/></td>
		<td><bean:write name="resultBean" property="pacfltFoodReportDate"/></td>
		<td><bean:write name="resultBean" property="systemReadyDate"/></td>
		<td><bean:write name="resultBean" property="computerNameDbDate"/></td>
		<td><bean:write name="resultBean" property="computerNameLogcopDate"/></td>
		<td><bean:write name="resultBean" property="trainingKitReadyDate"/></td>
		<td><bean:write name="resultBean" property="estTrainingMonth"/></td>
		<td><bean:write name="resultBean" property="schedTrainingDate"/></td>
		<td><bean:write name="resultBean" property="actualTrainingDate"/></td>
		<td align="center"><bean:write name="resultBean" property="clientConfirmedInd"/></td>
	</tr>
</logic:iterate>
</tbody>
</table>
</body>
</html>
