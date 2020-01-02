<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<jsp:useBean id="resultList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="type" scope="request" class="java.lang.String"/>
<jsp:useBean id="msoFooterData" scope="request" class="java.lang.String"/>
<jsp:useBean id="inputBean" scope="request" class="com.premiersolutionshi.old.bean.TrainingBean"/>

<bean:define id="defaultPageTitle" value="FACET Schedule"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>

<%
	String filename = CommonMethods.isEmpty(inputBean.getContractNumber()) ? "facet_schedule.xls" : "facet_schedule_" + inputBean.getContractNumber() + ".xls";

	response.setHeader("Pragma", "public");
	response.setHeader("Cache-Control", "max-age=0");
	response.setContentType("application/vnd.ms-excel");
 	response.setHeader("Content-Disposition", "attachment;filename=" + filename);

 	int currYear = CommonMethods.cInt(CommonMethods.getDate("yyyy"));
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
	border-right: 1pt solid black;
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
	<col span="1" width="58"/>
</colgroup>
<thead>
	<tr>
		<th colspan="5" class="endSection">Activity Information</th>
		<th colspan="2" class="endSection">Backfile</th>
		<th colspan="3" class="endSection">Training Workflow</th>
		<%
			for (int year = 2013; year <= currYear; year++) {
				for (int month = (year == 2013 ? 9 : 1); month <= (year == currYear ? 9 : 12); month++) {
		%>
			<th class="endSection" colspan="<%= CommonMethods.getDate(month + "/1/" + year, "WOM") %>"><%= CommonMethods.getMonthName(month-1) %> - <%= year %></th>
		<% }} %>
	</tr>
	<tr>
		<th>Unit Name</th>
		<th>Type/Hull</th>
		<th>TYCOM</th>
		<th>Homeport</th>
		<th class="endSection">RSupply<br/>Version</th>

		<th>Received<br/>Date</th>
		<th class="endSection">Completed<br/>Date</th>

		<th>Scheduled<br/>Date</th>
		<th>Actual<br/>Date</th>
		<th class="endSection">Confirmed<br/>By Client</th>

		<%
			for (int year = 2013; year <= currYear; year++) {
				for (int month = (year == 2013 ? 9 : 1); month <= (year == currYear ? 9 : 12); month++) {
					int startDow = CommonMethods.cInt(CommonMethods.getDate(month + "/1/" + year, "DOW#"));
					int startDay = 1;
					int endDay = 8 - startDow;
					int daysInMonth = CommonMethods.cInt(CommonMethods.getDate(month + "/1/" + year, "MAX"));
					int i = 1;
					while (startDay <= daysInMonth) {
						// :: startDay - endDay <= daysInMonth ? endDay : daysInMonth
		%>
			<th <% if(endDay >= daysInMonth) { %>class="endSection"<% } %>>Wk <%= i %></th>
		<%
			i++;
			startDay = (startDay == 1 ? endDay + 1 : startDay + 7);
			endDay+=7;
			}}}
		%>
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

		<td align="right">
			<% if (!CommonMethods.isEmpty(resultBean.getSchedTrainingDate())) { %>
				<bean:write name="resultBean" property="schedTrainingDate"/>
			<% } else if (!CommonMethods.isEmpty(resultBean.getActualTrainingDate())) { %>
				-
			<% } else { %>
				Unscheduled
			<% } %>
		</td>
		<td align="right">
			<logic:notEmpty name="resultBean" property="actualTrainingDate">
				<bean:write name="resultBean" property="actualTrainingDate"/>
			</logic:notEmpty>
			<logic:empty name="resultBean" property="actualTrainingDate">
				-
			</logic:empty>
		</td>
		<td class="endSection" align="center"><bean:write name="resultBean" property="clientConfirmedInd"/></td>

		<%
			for (int year = 2013; year <= currYear; year++) {
				for (int month = (year == 2013 ? 9 : 1); month <= (year == currYear ? 9 : 12); month++) {
					int startDow = CommonMethods.cInt(CommonMethods.getDate(month + "/1/" + year, "DOW#"));
					int startDay = 1;
					int endDay = 8 - startDow;
					int daysInMonth = CommonMethods.cInt(CommonMethods.getDate(month + "/1/" + year, "MAX"));
					int i = 1;
					String currDate = CommonMethods.nvl(resultBean.getActualTrainingDate(), resultBean.getSchedTrainingDate());
					while (startDay <= daysInMonth) {
		%>
				<% if (CommonMethods.dateDiff(currDate, month + "/" + startDay + "/" + year) <= 0 && CommonMethods.dateDiff(currDate, month + "/" + (endDay <= daysInMonth ? endDay : daysInMonth) + "/" + year) >= 0) { %>
					<logic:notEmpty name="resultBean" property="actualTrainingDate">
						<td <% if(endDay >= daysInMonth) { %>class="endSection"<% } %> style="background-color:#eaa228;text-align:center;"><%= CommonMethods.getDate(currDate, "DD") %></td>
					</logic:notEmpty>
					<logic:empty name="resultBean" property="actualTrainingDate">
						<td <% if(endDay >= daysInMonth) { %>class="endSection"<% } %> style="background-color:#4bb2c5;text-align:center;"><%= CommonMethods.getDate(currDate, "DD") %></td>
					</logic:empty>
				<% } else { %>
					<td <% if(endDay >= daysInMonth) { %>class="endSection"<% } %>></td>
				<% } %>
		<%
			i++;
			startDay = (startDay == 1 ? endDay + 1 : startDay + 7);
			endDay+=7;
			}}}
		%>
	</tr>
</logic:iterate>

<tr>
	<td class="noborder">&nbsp;</td>
</tr>
<tr>
	<td class="noborder">Legend</td>
</tr>
<tr>
	<td class="noborder" style="background-color:#eaa228;">Training Completed</td>
</tr>
<tr>
	<td class="noborder" style="background-color:#4bb2c5;">Scheduled Training</td>
</tr>
</tbody>
</table>
</body>
</html>
