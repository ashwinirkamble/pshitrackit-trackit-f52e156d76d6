<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<jsp:useBean id="resultList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="totalBean" scope="request" class="com.premiersolutionshi.old.bean.SupportBean"/>
<jsp:useBean id="msoFooterData" scope="request" class="java.lang.String"/>

<bean:define id="defaultPageTitle" value="Issue List"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>

<%
	response.setHeader("Pragma", "public");
	response.setHeader("Cache-Control", "max-age=0");
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-Disposition", "attachment;filename=issue_summary_by_ship.xls");
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
			<th rowspan="2">Unit Name</th>
			<th rowspan="2">Homeport</th>
			<th rowspan="2">Total<br/>Issues</th>
			<th rowspan="2">Open<br/>Issues</th>
			<th rowspan="2"># of<br/>Support<br/>Visits</td>
			<th rowspan="2">Last Support Visit</td>
			<th colspan="9">Breakdown By Category</th>
		</tr>
		<tr>
			<th>LOGCOP</th>
			<th>FACET</th>
			<th>Kofax</th>
			<th>Administrative Receipt Tool</th>
			<th>FACET Update</th>
			<th>Laptop</th>
			<th>Follow-Up Training</th>
			<th>Backfile</th>
			<th>Misc/Other</th>
		</tr>
	</thead>
	<tbody>
	<logic:iterate id="resultBean" name="resultList" type="com.premiersolutionshi.old.bean.SupportBean">
		<tr>
			<td>
				<logic:notEmpty name="resultBean" property="shipName">
					<bean:write name="resultBean" property="shipName"/>
				</logic:notEmpty>
				<logic:empty name="resultBean" property="shipName">
					<i>Non-ship specific issue</i>
				</logic:empty>
			</td>
			<td><bean:write name="resultBean" property="homeport"/></td>
			<td align="center"><b><bean:write name="resultBean" property="issueCnt"/></b></td>
			<td align="center"><bean:write name="resultBean" property="openIssueCnt"/></td>
			<td align="center"><bean:write name="resultBean" property="supportVisitCnt"/></td>
			<td align="left">
				<logic:notEmpty name="resultBean" property="supportVisitDate">
					<bean:write name="resultBean" property="supportVisitDate"/>
					(<bean:write name="resultBean" property="category"/>)
				</logic:notEmpty>
			</td>
			<td align="center"><bean:write name="resultBean" property="logcopCnt"/></td>
			<td align="center"><bean:write name="resultBean" property="facetCnt"/></td>
			<td align="center"><bean:write name="resultBean" property="kofaxCnt"/></td>
			<td align="center"><bean:write name="resultBean" property="dummyCnt"/></td>
			<td align="center"><bean:write name="resultBean" property="updateCnt"/></td>
			<td align="center"><bean:write name="resultBean" property="laptopCnt"/></td>
			<td align="center"><bean:write name="resultBean" property="trainingCnt"/></td>
			<td align="center"><bean:write name="resultBean" property="backfileCnt"/></td>
			<td align="center"><bean:write name="resultBean" property="otherCnt"/></td>
		</tr>
	</logic:iterate>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="2" style="text-align:right;font-weight:bold;">Total:</td>
			<td style="font-weight:bold;text-align:center;"><bean:write name="totalBean" property="issueCnt"/></td>
			<td style="font-weight:bold;text-align:center;"><bean:write name="totalBean" property="openIssueCnt"/></td>
			<td style="font-weight:bold;text-align:center;"><bean:write name="totalBean" property="supportVisitCnt"/></td>
			<td/>
			<td style="font-weight:bold;text-align:center;"><bean:write name="totalBean" property="logcopCnt"/></td>
			<td style="font-weight:bold;text-align:center;"><bean:write name="totalBean" property="facetCnt"/></td>
			<td style="font-weight:bold;text-align:center;"><bean:write name="totalBean" property="kofaxCnt"/></td>
			<td style="font-weight:bold;text-align:center;"><bean:write name="totalBean" property="dummyCnt"/></td>
			<td style="font-weight:bold;text-align:center;"><bean:write name="totalBean" property="updateCnt"/></td>
			<td style="font-weight:bold;text-align:center;"><bean:write name="totalBean" property="laptopCnt"/></td>
			<td style="font-weight:bold;text-align:center;"><bean:write name="totalBean" property="trainingCnt"/></td>
			<td style="font-weight:bold;text-align:center;"><bean:write name="totalBean" property="backfileCnt"/></td>
			<td style="font-weight:bold;text-align:center;"><bean:write name="totalBean" property="otherCnt"/></td>
		</tr>
	</tfoot>
</table>
</body>
</html>
