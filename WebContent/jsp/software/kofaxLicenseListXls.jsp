<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<jsp:useBean id="resultList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="msoFooterData" scope="request" class="java.lang.String"/>

<bean:define id="defaultPageTitle" value="Kofax Licenses"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>

<%
	response.setHeader("Pragma", "public");
	response.setHeader("Cache-Control", "max-age=0");
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-Disposition", "attachment;filename=kofax_license.xls");
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
	background-color: #97cbfd;
	color: black;
	font-weight: bold;
	border: .5pt solid #000;
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
-->
</style>
</head>

<body>
<table id="xlsTable" border="0" cellspacing="3" cellpadding="3">
	<thead>
		<tr>
			<th>Assigned To</th>
			<th>License Key</th>
			<th>Product Code</th>
			<th>Customer</th>
			<th>Contract Number</th>
			<th>Date Received</th>
			<th>Notes</th>
		</tr>
	</thead>
	<tbody>
	<logic:iterate id="resultBean" name="resultList" type="com.premiersolutionshi.old.bean.SoftwareBean">
		<tr>
			<td class="txtRow"><bean:write name="resultBean" property="computerName"/></td>
			<td class="txtRow"><bean:write name="resultBean" property="licenseKey"/></td>
			<td class="txtRow"><bean:write name="resultBean" property="productCode"/></td>
			<td class="txtRow"><bean:write name="resultBean" property="customer"/></td>
			<td class="txtRow"><bean:write name="resultBean" property="contractNumber"/></td>
			<td><bean:write name="resultBean" property="receivedDate"/></td>
			<td>
				<logic:iterate id="notes" name="resultBean" property="notesBr" indexId="i">
					<bean:write name="notes"/><% if (i+1 < resultBean.getNotesBr().size()) { %><br/><% } %>
				</logic:iterate>
			</td>
		</tr>
	</logic:iterate>
	</tbody>
</table>
</body>
</html>
