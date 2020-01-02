<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<jsp:useBean id="resultList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="msoFooterData" scope="request" class="java.lang.String"/>
<jsp:useBean id="inputBean" scope="request" class="com.premiersolutionshi.old.bean.DecomBean"/>

<bean:define id="defaultPageTitle" value="Decom Workflow"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>

<%
	response.setHeader("Pragma", "public");
	response.setHeader("Cache-Control", "max-age=0");
	response.setContentType("application/vnd.ms-excel");

	StringBuffer filename = new StringBuffer("decom_workflow");
	if (!CommonMethods.isEmpty(inputBean.getContractNumber())) filename.append("_" + inputBean.getContractNumber());
	filename.append(".xls");

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
	<col span="9"/>
	<col span="6" width="73"/>
	<col span="1" width="250"/>
</colgroup>
<thead>
	<tr>
		<th colspan="9" class="endSection">Vessel Information</th>
		<th colspan="10">Decom Workflow</th>
	</tr>
	<tr>
		<th>Vessel Name</th>
		<th>Type/Hull</th>
		<th>TYCOM</th>
		<th>Homeport</th>
		<th>Computer Name</th>
		<th>Laptop Tag</th>
		<th>Scanner Tag</th>
		<th>RSupply Version</th>
		<th class="endSection">Decom Date</th>
		<th>Client Contacted With Decom Instructions</th>
		<th>Hardware Received By PSHI and Inventory Lists 'Status' Updated</th>
		<th>Status of Hardware Received</th>
		<th>Status of Hardware Received Notes</th>
		<th>FIARModule Folder(s) Copied to P Drive</th>
		<th>Backup Provided to TYCOM</th>
		<th>Records Checked Against LOGCOP</th>
		<th>Transmittal Reconciled With LOGCOP</th>
		<th>Laptop Reset and All Hardware Availability Updated</th>
		<th>Comments</th>
	</tr>
</thead>
<tbody>
<logic:iterate id="resultBean" name="resultList" type="com.premiersolutionshi.old.bean.DecomBean">
	<tr>
		<td><bean:write name="resultBean" property="shipName"/></td>
		<td>
			<bean:write name="resultBean" property="type"/>
			<bean:write name="resultBean" property="hull"/>
		</td>
		<td><bean:write name="resultBean" property="tycomDisplay"/></td>
		<td><bean:write name="resultBean" property="homeport"/></td>
		<td><bean:write name="resultBean" property="computerName"/></td>
		<td><bean:write name="resultBean" property="laptopTag"/></td>
		<td><bean:write name="resultBean" property="scannerTag"/></td>
		<td><bean:write name="resultBean" property="rsupply"/></td>
		<td class="endSection"><bean:write name="resultBean" property="decomDate"/></td>
		<td><bean:write name="resultBean" property="shipContactedDate"/></td>
		<td><bean:write name="resultBean" property="systemReceivedDate"/></td>
		<td><bean:write name="resultBean" property="hardwareStatus"/></td>
		<td><bean:write name="resultBean" property="hardwareStatusNotes"/></td>
		<td><bean:write name="resultBean" property="systemReturnedDate"/></td>
		<td><bean:write name="resultBean" property="backupDate"/></td>
		<td><bean:write name="resultBean" property="transmittalCheckDate"/></td>
		<td><bean:write name="resultBean" property="transmittalReconDate"/></td>
		<td><bean:write name="resultBean" property="laptopResetDate"/></td>
		<td align="left" width="250"><bean:write name="resultBean" property="comments"/></td>
	</tr>
</logic:iterate>
</tbody>
</table>
</body>
</html>
