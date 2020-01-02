<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<jsp:useBean id="resultList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="msoFooterData" scope="request" class="java.lang.String"/>

<bean:define id="defaultPageTitle" value="Ship POCs"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>

<%
	response.setHeader("Pragma", "public");
	response.setHeader("Cache-Control", "max-age=0");
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-Disposition", "attachment;filename=ship_pocs.xls");
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
			<th>Vessel</th>
			<th>Primary?</th>
			<th>Rank</th>
			<th>Last Name</th>
			<th>First Name</th>
			<th>Job Title</th>
			<th>Dept</th>
			<th>Email</th>
			<th>Alt Email</th>
			<th>Work Number</th>
			<th>Cell/Alt Number</th>
		</tr>
	</thead>
	<tbody>
	<logic:iterate id="shipBean" name="resultList" type="com.premiersolutionshi.old.bean.ShipBean">
	<logic:iterate id="resultBean" name="shipBean" property="pocList" type="com.premiersolutionshi.old.bean.UserBean">
		<tr>
			<td><bean:write name="shipBean" property="shipName"/> (<bean:write name="shipBean" property="type"/> <bean:write name="shipBean" property="hull"/>)</td>
			<td align="center"><bean:write name="resultBean" property="isPrimaryPoc"/></td>
			<td><bean:write name="resultBean" property="rank"/></td>
			<td><bean:write name="resultBean" property="lastName"/></td>
			<td><bean:write name="resultBean" property="firstName"/></td>
			<td><bean:write name="resultBean" property="title"/></td>
			<td><bean:write name="resultBean" property="dept"/></td>
			<td><logic:notEmpty name="resultBean" property="email"><a href="mailto:<bean:write name="resultBean" property="email"/>"><bean:write name="resultBean" property="email"/></a></logic:notEmpty></td>
			<td><logic:notEmpty name="resultBean" property="altEmail"><br/><a href="mailto:<bean:write name="resultBean" property="altEmail"/>"><bean:write name="resultBean" property="altEmail"/></a></logic:notEmpty></td>
			<td><bean:write name="resultBean" property="workNumber"/></td>
			<td><bean:write name="resultBean" property="cellNumber"/></td>
		</tr>
	</logic:iterate>
	</logic:iterate>
	</tbody>
</table>
</body>
</html>
