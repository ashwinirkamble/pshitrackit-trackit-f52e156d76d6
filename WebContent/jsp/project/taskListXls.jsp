<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<jsp:useBean id="resultList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="msoFooterData" scope="request" class="java.lang.String"/>
<jsp:useBean id="projectBean" scope="request" class="com.premiersolutionshi.old.bean.ProjectBean"/>

<bean:define id="defaultPageTitle" value="Task List"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>

<%
	response.setHeader("Pragma", "public");
	response.setHeader("Cache-Control", "max-age=0");
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-Disposition", "attachment;filename=export_tasklist.xls");
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
	vertical-align: top;
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
		<th>Task #</th>
		<th>Title</th>
		<th>Description</th>
		<th>Category</th>
		<th>Status</th>
		<th>Priority</th>

		<th>Target Quarter</th>
		<th>Area of Effort</th>
		<th>Type of Effort</th>
		<th>Level of Effort</th>
		<th>Client Approved</th>
		<th>Client Priority</th>
		<th>PSHI Approved</th>
		<th>Recommendation</th>
		<th>Documentation Updated?</th>
		<th>Documentation Notes</th>
		<th>Fixed/Added in Version</th>
		<th>Deplyoed Date</th>

		<th>Ship</th>
		<th>Person Assigned</th>
		<th>Created Date</th>
		<th>Due Date</th>
		<th>Completed Date</th>
		<th>Contract Number</th>
		<th>Sub Tasks</th>
		<th>Notes</th>
		<th>Last Updated By</th>
		<th>Last Updated Date</th>
	</tr>
</thead>
<tbody>
<logic:iterate id="resultBean" name="resultList" type="com.premiersolutionshi.old.bean.ProjectBean">
	<tr>
		<td class="numRow"><bean:write name="resultBean" property="taskPk"/></td>
		<td class="txtRow"><bean:write name="resultBean" property="title"/></td>
		<td class="txtRow">
			<logic:iterate id="description" name="resultBean" property="descriptionBr" indexId="i">
				<bean:write name="description"/><% if (i+1 < resultBean.getDescriptionBr().size()) { %><br/><% } %>
			</logic:iterate>
		</td>
		<td align="center"><bean:write name="resultBean" property="category"/></td>
		<td align="center" style="<bean:write name="resultBean" property="statusCss"/>"><bean:write name="resultBean" property="status"/></td>
		<td align="center" style="<bean:write name="resultBean" property="priorityCss"/>"><bean:write name="resultBean" property="priority"/></td>

		<logic:equal name="resultBean" property="category" value="Future Requests">
			<td align="center"><bean:write name="resultBean" property="quarterYear"/></td>
			<td><bean:write name="resultBean" property="effortArea"/></td>
			<td><bean:write name="resultBean" property="effortType"/></td>
			<td><bean:write name="resultBean" property="loe"/></td>

			<td align="center"><logic:equal name="resultBean" property="isClientApproved" value="Y">Y</logic:equal></td>

			<td><bean:write name="resultBean" property="clientPriority"/></td>

			<td align="center"><logic:equal name="resultBean" property="isPshiApproved" value="Y">Y</logic:equal></td>

			<td><bean:write name="resultBean" property="recommendation"/></td>

			<td align="center"><bean:write name="resultBean" property="docUpdatedInd"/></td>

			<td><bean:write name="resultBean" property="docNotes"/></td>

			<td class="txtRow">
				<logic:iterate id="versionIncluded" name="resultBean" property="versionIncludedBr" indexId="i">
					<bean:write name="versionIncluded"/><% if (i+1 < resultBean.getVersionIncludedBr().size()) { %><br/><% } %>
				</logic:iterate>
			</td>

			<td><bean:write name="resultBean" property="deployedDate"/></td>
		</logic:equal>
		<logic:notEqual name="resultBean" property="category" value="Future Requests">
			<td style="background:#999;"/>
			<td style="background:#999;"/>
			<td style="background:#999;"/>
			<td style="background:#999;"/>
			<td style="background:#999;"/>
			<td style="background:#999;"/>
			<td style="background:#999;"/>
			<td style="background:#999;"/>
			<td style="background:#999;"/>
			<td style="background:#999;"/>
			<td style="background:#999;"/>
			<td style="background:#999;"/>
		</logic:notEqual>


		<td><bean:write name="resultBean" property="shipName"/></td>
		<td><bean:write name="resultBean" property="personAssigned"/></td>
		<td align="center"><bean:write name="resultBean" property="createdDate"/></td>
		<td align="center" style="<bean:write name="resultBean" property="dueDateCss"/>"><bean:write name="resultBean" property="dueDate"/></td>
		<td align="center"><bean:write name="resultBean" property="completedDate"/></td>
		<td align="center"><bean:write name="resultBean" property="contractNumber"/></td>
		<td class="txtRow">
			<logic:iterate id="subTasks" name="resultBean" property="subTasksBr" indexId="i">
				<bean:write name="subTasks"/><% if (i+1 < resultBean.getSubTasksBr().size()) { %><br/><% } %>
			</logic:iterate>
		</td>
		<td class="txtRow">
			<logic:iterate id="notes" name="resultBean" property="notesBr" indexId="i">
				<bean:write name="notes"/><% if (i+1 < resultBean.getNotesBr().size()) { %><br/><% } %>
			</logic:iterate>
		</td>
		<td><bean:write name="resultBean" property="lastUpdatedBy"/></td>
		<td><bean:write name="resultBean" property="lastUpdatedDate"/></td>
	</tr>
</logic:iterate>
</tbody>
</table>
</body>
</html>
