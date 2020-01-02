<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="defaultPageTitle" value="Backfile Workflow List"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk"   	 			scope="request" class="java.lang.String"/>

<jsp:useBean id="inputBean" 					scope="request" class="com.premiersolutionshi.old.bean.BackfileBean"/>

<jsp:useBean id="pendingList"	 				scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="inProdList"	 				scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="completedList"	 			scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="overdueList"					scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="notRequiredList"			scope="request" class="java.util.ArrayList"/>

<jsp:useBean id="contractNumberList" 	scope="request" class="java.util.ArrayList"/>

<jsp:useBean id="sortBy"    	 				scope="request" class="java.lang.String"/>
<jsp:useBean id="sortDir"   	 				scope="request" class="java.lang.String"/>

<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">

	<title>TrackIT - <bean:write name="pageTitle"/></title>

	<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
	<link rel="stylesheet" type="text/css" href="css/stylesheet.css"/>
	<link rel="stylesheet" type="text/css" href="css/jquery-ui.min.css"/>

	<style>
		.datefield {
			text-align: center;
		}
		#tanTable_style2 th {
			font-size: 10px;
			vertical-align: bottom;
			}
		#tanTable_style2 td {
			font-size: 12px;
			}
	</style>
</head>

<body>
<%@ include file="../include/app-header.jsp" %>

<%@ include file="../include/content-header.jsp" %>

<tags:projectPkBreadcrumb projectPk="${projectPk}" pageTitle="${pageTitle}" hideOptions="true" />

<% if (contractNumberList.size() > 0) { %>
	<center>
	<div>
		<form class="form-inline" action="backfile.do" method="GET">
			<input type="hidden" name="action" value="workflowSummary"/>
			<input type="hidden" name="projectPk" value="<bean:write name="projectPk"/>"/>
			<input type="hidden" name="sortBy" value="<bean:write name="sortBy"/>"/>
			<input type="hidden" name="sortDir" value="<bean:write name="sortDir"/>"/>
			<input type="hidden" name="searchPerformed" value="Y"/>
			<div class="form-group">
				<label for="contractNumber">Contract Number:</label>
				<html:select name="inputBean" property="contractNumber" styleClass="form-control input-sm">
					<html:option value="">View All</html:option>
					<html:options name="contractNumberList"/>
				</html:select>
			</div>
			<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span> Search</button>
		</form>
	</div><br/>
	</center>
<% } %>

<p align="center">
 <%--ashwini /backfile.do to ./backfile.do--%>
<a href="./backfile.do?action=workflowAdd&projectPk=<bean:write name="projectPk"/>" class="btn btn-success"><span class="glyphicon glyphicon-plus"></span> Add New Workflow</a>
</p>

<p align="center">
<a href="export.do?action=backfileWorkflowXlsx&projectPk=<%= projectPk %>" class="btn btn-default"><img src="images/file_icons/sml_file_xls.gif"/> Export to Excel</a>
</p>

<center>
<div id="tabs" style="width:97%;" align="center">
	<ul>
		<li><a href="#tabs-0">In Production (<%= inProdList.size() %>)</a></li>
		<li><a href="#tabs-1">Pending (<%= pendingList.size() %>)</a></li>
		<li><a href="#tabs-2">Overdue (<%= overdueList.size() %>)</a></li>
		<li><a href="#tabs-3">Completed (<%= completedList.size() %>)</a></li>
		<li><a href="#tabs-4">Not Required (<%= notRequiredList.size() %>)</a></li>
	</ul>
	<div id="tabs-0">
		<% if (inProdList.size() == 0) { %>
			<p class="error" align="center">
				No Backfiles in Production
				<logic:notEmpty name="inputBean" property="contractNumber">
					(for contract #<bean:write name="inputBean" property="contractNumber"/>)
				</logic:notEmpty>
			</p>
		<% } else { %>
			<p align="center">
			<center>
			<table id="tanTable_style2" border="0" class="alt-color" width="100%">
			<thead>
				<tr class="ignore">
					<td colspan="18" class="recordCnt">
						<b><%= inProdList.size() %> workflows found</b>
						<a href="backfile.do?action=workflowSummaryXls2&type=inProd&projectPk=<bean:write name="projectPk"/>"><img src="images/file_icons/sml_file_xls.gif"> Detailed Report</a>
					</td>
				</tr>
				<tr>
					<th colspan="3">Ship Information</th>
					<th rowspan="2">Action</th>
					<th colspan="15">Backfile Workflow</th>
				</tr>
				<tr>
					<th nowrap>
						<a href="javascript: changeSort('ship_name', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Unit Name</a>
						<% if (sortBy.equals("ship_name") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("ship_name") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th nowrap>
						<a href="javascript: changeSort('type_hull', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Type/Hull</a>
						<% if (sortBy.equals("type_hull") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("type_hull") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th nowrap>
						<a href="javascript: changeSort('homeport', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Homeport</a>
						<% if (sortBy.equals("homeport") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("homeport") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>

					<th>Overall<br/>Due<br/>Date</th>
					<th>FY14/13<br/>Due<br/>Date</th>
					<!--th>Completed<br/>Date</th-->
					<th nowrap>
						<a href="javascript: changeSort('sched_training_date', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Training<br/>Date</a>
						<% if (sortBy.equals("sched_training_date") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("sched_training_date") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th>Backfile<br/>Date<br/>Requested</th>
					<th>Date<br/>Received<br/>By PSHI</th>
					<th>Date<br/>Delivered<br/>to Scanning</th>
					<th>FY14/13<br/>Completed<br/>Date</th>
					<th>FY14/13<br/>CD Sent<br/>for Customer<br/>Date</th>
					<th>FY12/11<br/>Completed<br/>Date</th>
					<th>Extract File<br/>Created</th>
					<th>All backfile<br/>CD mailed<br/>to Cust/SD</th>
					<th>Verify<br/>All Backfiles<br/>Installed<br/>in FACET</th>
					<th>All Backfile<br/>CD Delivered<br/>to DACS</th>
					<th>Verified<br/>DACS<br/>Backfile<br/>Uploaded</th>
					<th>Final Backfile<br/>Report<br/>Generated<br/>and Given to<br/>CompacFlt</th>
				</tr>
			</thead>
			<tbody>
			<logic:iterate id="resultBean" name="inProdList" type="com.premiersolutionshi.old.bean.BackfileBean">
				<tr>
					<td>
						<bean:write name="resultBean" property="shipName"/>
						<logic:notEmpty name="resultBean" property="comments">
							<img src="images/icon_comments.png" height="13" width="13" title="<bean:write name="resultBean" property="comments"/>"/>
						</logic:notEmpty>
					</td>
					<td>
						<bean:write name="resultBean" property="type"/>
						<bean:write name="resultBean" property="hull"/>
					</td>
					<td><bean:write name="resultBean" property="homeport"/></td>
					<td align="center" nowrap>
						<a href="backfile.do?action=workflowEdit&backfileWorkflowPk=<bean:write name="resultBean" property="backfileWorkflowPk"/>&projectPk=<bean:write name="projectPk"/>"><img src="images/icon_edit.png" title="Edit <bean:write name="resultBean" property="shipName"/>"/></a>
						&nbsp;<a href="backfile.do?action=workflowDeleteDo&backfileWorkflowPk=<bean:write name="resultBean" property="backfileWorkflowPk"/>&projectPk=<bean:write name="projectPk"/>" onclick="return confirmDelete('<bean:write name="resultBean" property="shipName"/>');"><img src="images/icon_delete.png" title="Delete <bean:write name="resultBean" property="shipName"/>"/></a>
					</td>
					<td class="datefield" title="Overall Due Date"                                       style="<bean:write name="resultBean" property="dueDateCss"/>"><bean:write name="resultBean" property="dueDate"/></td>
					<td class="datefield" title="FY14/13 Due Date"                                       style="<bean:write name="resultBean" property="fy1314DueDateCss"/>"><bean:write name="resultBean" property="fy1314DueDate"/></td>

					<td class="datefield" title="Scheduled Training Date" style="background:#ccc;"><bean:write name="resultBean" property="schedTrainingDate"/></td>

					<td class="datefield" title="Backfile Date Requested"                                style="<bean:write name="resultBean" property="requestedDateCss"/>"><bean:write name="resultBean" property="requestedDate"/></td>
					<td class="datefield" title="Date Received By PSHI"                                  style="<bean:write name="resultBean" property="receivedDateCss"/>"><bean:write name="resultBean" property="receivedDate"/></td>
					<td class="datefield" title="Date Delivered to Scanning"                             style="<bean:write name="resultBean" property="scanningDeliveredDateCss"/>"><bean:write name="resultBean" property="scanningDeliveredDate"/></td>
					<td class="datefield" title="FY14/13 Completed Date"                                 style="<bean:write name="resultBean" property="fy1314CompletedDateCss"/>"><bean:write name="resultBean" property="fy1314CompletedDate"/></td>
					<td class="datefield" title="FY14/13 CD Sent for Customer Date"                      style="<bean:write name="resultBean" property="fy1314BurnedDateCss"/>"><bean:write name="resultBean" property="fy1314BurnedDate"/></td>
					<td class="datefield" title="FY12/11 Completed Date"                                 style="<bean:write name="resultBean" property="fy1112CompletedDateCss"/>"><bean:write name="resultBean" property="fy1112CompletedDate"/></td>
					<td class="datefield" title="Extract File Created"                                   style="<bean:write name="resultBean" property="extractDateCss"/>"><bean:write name="resultBean" property="extractDate"/></td>
					<td class="datefield" title="All backfile CD mailed to Cust/SD"                      style="<bean:write name="resultBean" property="fy1314MailedDateCss"/>"><bean:write name="resultBean" property="fy1314MailedDate"/></td>
					<td class="datefield" title="Verify All Backfiles Installed in FACET"                style="<bean:write name="resultBean" property="laptopInstalledDateCss"/>"><bean:write name="resultBean" property="laptopInstalledDate"/></td>
					<td class="datefield" title="All Backfile CD Delivered to LogCop"                    style="<bean:write name="resultBean" property="logcopDeliveredDateCss"/>"><bean:write name="resultBean" property="logcopDeliveredDate"/></td>
					<td class="datefield" title="Verified LOGCOP Backfile Uploaded"                      style="<bean:write name="resultBean" property="logcopUploadedDateCss"/>"><bean:write name="resultBean" property="logcopUploadedDate"/></td>
					<td class="datefield" title="Final Backfile Report Generated and Given to CompacFlt" style="<bean:write name="resultBean" property="finalReportDateCss"/>"><bean:write name="resultBean" property="finalReportDate"/></td>
				</tr>
			</logic:iterate>
			</tbody>
			</table>
			</center>
			</p>
		<% } %>
	</div>

	<div id="tabs-1">
		<% if (pendingList.size() == 0) { %>
			<p class="error" align="center">
				No Pending Backfiles
				<logic:notEmpty name="inputBean" property="contractNumber">
					(for contract #<bean:write name="inputBean" property="contractNumber"/>)
				</logic:notEmpty>
			</p>
		<% } else { %>
			<p align="center">
			<center>
			<table id="tanTable_style2" border="0" cellspacing="0" class="alt-color" width="100%">
			<thead>
				<tr class="ignore">
					<td colspan="18" class="recordCnt">
						<b><%= pendingList.size() %> workflows found</b>
						<a href="backfile.do?action=workflowSummaryXls2&type=pending&projectPk=<bean:write name="projectPk"/>"><img src="images/file_icons/sml_file_xls.gif"> Detailed Report</a>
					</td>
				</tr>
				<tr>
					<th colspan="3">Ship Information</th>
					<th rowspan="2">Action</th>
					<th colspan="15">Backfile Workflow</th>
				</tr>
				<tr>
					<th nowrap>
						<a href="javascript: changeSort('ship_name', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Unit Name</a>
						<% if (sortBy.equals("ship_name") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("ship_name") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th nowrap>
						<a href="javascript: changeSort('type_hull', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Type/Hull</a>
						<% if (sortBy.equals("type_hull") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("type_hull") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th nowrap>
						<a href="javascript: changeSort('homeport', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Homeport</a>
						<% if (sortBy.equals("homeport") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("homeport") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>

					<th>Overall<br/>Due<br/>Date</th>
					<th>FY14/13<br/>Due<br/>Date</th>
					<!--th>Completed<br/>Date</th-->
					<th nowrap>
						<a href="javascript: changeSort('sched_training_date', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Training<br/>Date</a>
						<% if (sortBy.equals("sched_training_date") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("sched_training_date") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th>Backfile<br/>Date<br/>Requested</th>
					<th>Date<br/>Received<br/>By PSHI</th>
					<th>Date<br/>Delivered<br/>to Scanning</th>
					<th>FY14/13<br/>Completed<br/>Date</th>
					<th>FY14/13<br/>CD Sent<br/>for Customer<br/>Date</th>
					<th>FY12/11<br/>Completed<br/>Date</th>
					<th>Extract File<br/>Created</th>
					<th>All backfile<br/>CD mailed<br/>to Cust/SD</th>
					<th>Verify<br/>All Backfiles<br/>Installed<br/>in FACET</th>
					<th>All Backfile<br/>CD Delivered<br/>to LogCop</th>
					<th>Verified<br/>LOGCOP<br/>Backfile<br/>Uploaded</th>
					<th>Final Backfile<br/>Report<br/>Generated<br/>and Given to<br/>CompacFlt</th>
				</tr>
			</thead>
			<tbody>
			<logic:iterate id="resultBean" name="pendingList" type="com.premiersolutionshi.old.bean.BackfileBean">
				<tr>
					<td>
						<bean:write name="resultBean" property="shipName"/>
						<logic:notEmpty name="resultBean" property="comments">
							<img src="images/icon_comments.png" height="13" width="13" title="<bean:write name="resultBean" property="comments"/>"/>
						</logic:notEmpty>
					</td>
					<td>
						<bean:write name="resultBean" property="type"/>
						<bean:write name="resultBean" property="hull"/>
					</td>
					<td><bean:write name="resultBean" property="homeport"/></td>
					<td align="center" nowrap>
						<a href="backfile.do?action=workflowEdit&backfileWorkflowPk=<bean:write name="resultBean" property="backfileWorkflowPk"/>&projectPk=<bean:write name="projectPk"/>"><img src="images/icon_edit.png" title="Edit <bean:write name="resultBean" property="shipName"/>"/></a>
						&nbsp;<a href="backfile.do?action=workflowDeleteDo&backfileWorkflowPk=<bean:write name="resultBean" property="backfileWorkflowPk"/>&projectPk=<bean:write name="projectPk"/>" onclick="return confirmDelete('<bean:write name="resultBean" property="shipName"/>');"><img src="images/icon_delete.png" title="Delete <bean:write name="resultBean" property="shipName"/>"/></a>
					</td>
					<td class="datefield" title="Overall Due Date"                                       style="<bean:write name="resultBean" property="dueDateCss"/>"><bean:write name="resultBean" property="dueDate"/></td>
					<td class="datefield" title="FY14/13 Due Date"                                       style="<bean:write name="resultBean" property="fy1314DueDateCss"/>"><bean:write name="resultBean" property="fy1314DueDate"/></td>

					<td class="datefield" title="Scheduled Training Date" style="background:#ccc;"><bean:write name="resultBean" property="schedTrainingDate"/></td>

					<td class="datefield" title="Backfile Date Requested"                                style="<bean:write name="resultBean" property="requestedDateCss"/>"><bean:write name="resultBean" property="requestedDate"/></td>
					<td class="datefield" title="Date Received By PSHI"                                  style="<bean:write name="resultBean" property="receivedDateCss"/>"><bean:write name="resultBean" property="receivedDate"/></td>
					<td class="datefield" title="Date Delivered to Scanning"                             style="<bean:write name="resultBean" property="scanningDeliveredDateCss"/>"><bean:write name="resultBean" property="scanningDeliveredDate"/></td>
					<td class="datefield" title="FY14/13 Completed Date"                                 style="<bean:write name="resultBean" property="fy1314CompletedDateCss"/>"><bean:write name="resultBean" property="fy1314CompletedDate"/></td>
					<td class="datefield" title="FY14/13 CD Sent for Customer Date"                      style="<bean:write name="resultBean" property="fy1314BurnedDateCss"/>"><bean:write name="resultBean" property="fy1314BurnedDate"/></td>
					<td class="datefield" title="FY12/11 Completed Date"                                 style="<bean:write name="resultBean" property="fy1112CompletedDateCss"/>"><bean:write name="resultBean" property="fy1112CompletedDate"/></td>
					<td class="datefield" title="Extract File Created"                                   style="<bean:write name="resultBean" property="extractDateCss"/>"><bean:write name="resultBean" property="extractDate"/></td>
					<td class="datefield" title="All backfile CD mailed to Cust/SD"                      style="<bean:write name="resultBean" property="fy1314MailedDateCss"/>"><bean:write name="resultBean" property="fy1314MailedDate"/></td>
					<td class="datefield" title="Verify All Backfiles Installed in FACET"                style="<bean:write name="resultBean" property="laptopInstalledDateCss"/>"><bean:write name="resultBean" property="laptopInstalledDate"/></td>
					<td class="datefield" title="All Backfile CD Delivered to LogCop"                    style="<bean:write name="resultBean" property="logcopDeliveredDateCss"/>"><bean:write name="resultBean" property="logcopDeliveredDate"/></td>
					<td class="datefield" title="Verified LOGCOP Backfile Uploaded"                      style="<bean:write name="resultBean" property="logcopUploadedDateCss"/>"><bean:write name="resultBean" property="logcopUploadedDate"/></td>
					<td class="datefield" title="Final Backfile Report Generated and Given to CompacFlt" style="<bean:write name="resultBean" property="finalReportDateCss"/>"><bean:write name="resultBean" property="finalReportDate"/></td>
				</tr>
			</logic:iterate>
			</tbody>
			</table>
			</center>
			</p>
		<% } %>
	</div>

	<div id="tabs-2">
		<% if (overdueList.size() == 0) { %>
			<p class="error" align="center">
				No Overdue Workfiles Found
				<logic:notEmpty name="inputBean" property="contractNumber">
					(for contract #<bean:write name="inputBean" property="contractNumber"/>)
				</logic:notEmpty>
			</p>
		<% } else { %>
			<p align="center">
			<center>
			<table id="tanTable_style2" border="0" cellspacing="0" class="alt-color" width="100%">
			<thead>
				<tr class="ignore">
					<td colspan="18" class="recordCnt">
						<b><%= overdueList.size() %> workflows found</b>
						<a href="backfile.do?action=workflowSummaryXls2&type=overdue&projectPk=<bean:write name="projectPk"/>"><img src="images/file_icons/sml_file_xls.gif"> Detailed Report</a>
					</td>
				</tr>
				<tr>
					<th colspan="3">Ship Information</th>
					<th rowspan="2">Action</th>
					<th colspan="15">Backfile Workflow</th>
				</tr>
				<tr>
					<th nowrap>
						<a href="javascript: changeSort('ship_name', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Unit Name</a>
						<% if (sortBy.equals("ship_name") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("ship_name") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th nowrap>
						<a href="javascript: changeSort('type_hull', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Type/Hull</a>
						<% if (sortBy.equals("type_hull") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("type_hull") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th nowrap>
						<a href="javascript: changeSort('homeport', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Homeport</a>
						<% if (sortBy.equals("homeport") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("homeport") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>

					<th>Overall<br/>Due<br/>Date</th>
					<th>FY14/13<br/>Due<br/>Date</th>
					<th nowrap>
						<a href="javascript: changeSort('sched_training_date', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Training<br/>Date</a>
						<% if (sortBy.equals("sched_training_date") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("sched_training_date") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th>Backfile<br/>Date<br/>Requested</th>
					<th>Date<br/>Received<br/>By PSHI</th>
					<th>Date<br/>Delivered<br/>to Scanning</th>
					<th>FY14/13<br/>Completed<br/>Date</th>
					<th>FY14/13<br/>CD Sent<br/>for Customer<br/>Date</th>
					<th>FY12/11<br/>Completed<br/>Date</th>
					<th>Extract File<br/>Created</th>
					<th>All backfile<br/>CD mailed<br/>to Cust/SD</th>
					<th>Verify<br/>All Backfiles<br/>Installed<br/>in FACET</th>
					<th>All Backfile<br/>CD Delivered<br/>to LogCop</th>
					<th>Verified<br/>LOGCOP<br/>Backfile<br/>Uploaded</th>
					<th>Final Backfile<br/>Report<br/>Generated<br/>and Given to<br/>CompacFlt</th>
				</tr>
			</thead>
			<tbody>
			<logic:iterate id="resultBean" name="overdueList" type="com.premiersolutionshi.old.bean.BackfileBean">
				<tr>
					<td>
						<bean:write name="resultBean" property="shipName"/>
						<logic:notEmpty name="resultBean" property="comments">
							<img src="images/icon_comments.png" height="13" width="13" title="<bean:write name="resultBean" property="comments"/>"/>
						 </logic:notEmpty>
					</td>
					<td>
						<bean:write name="resultBean" property="type"/>
						<bean:write name="resultBean" property="hull"/>
					</td>
					<td><bean:write name="resultBean" property="homeport"/></td>
					<td align="center" nowrap>
						<a href="backfile.do?action=workflowEdit&backfileWorkflowPk=<bean:write name="resultBean" property="backfileWorkflowPk"/>&projectPk=<bean:write name="projectPk"/>"><img src="images/icon_edit.png" title="Edit <bean:write name="resultBean" property="shipName"/>"/></a>
						&nbsp;<a href="backfile.do?action=workflowDeleteDo&backfileWorkflowPk=<bean:write name="resultBean" property="backfileWorkflowPk"/>&projectPk=<bean:write name="projectPk"/>" onclick="return confirmDelete('<bean:write name="resultBean" property="shipName"/>');"><img src="images/icon_delete.png" title="Delete <bean:write name="resultBean" property="shipName"/>"/></a>
					</td>
					<td class="datefield" title="Overall Due Date"                                       style="<bean:write name="resultBean" property="dueDateCss"/>"><bean:write name="resultBean" property="dueDate"/></td>
					<td class="datefield" title="FY14/13 Due Date"                                       style="<bean:write name="resultBean" property="fy1314DueDateCss"/>"><bean:write name="resultBean" property="fy1314DueDate"/></td>

					<td class="datefield" title="Scheduled Training Date" style="background:#ccc;"><bean:write name="resultBean" property="schedTrainingDate"/></td>

					<td class="datefield" title="Backfile Date Requested"                                style="<bean:write name="resultBean" property="requestedDateCss"/>"><bean:write name="resultBean" property="requestedDate"/></td>
					<td class="datefield" title="Date Received By PSHI"                                  style="<bean:write name="resultBean" property="receivedDateCss"/>"><bean:write name="resultBean" property="receivedDate"/></td>
					<td class="datefield" title="Date Delivered to Scanning"                             style="<bean:write name="resultBean" property="scanningDeliveredDateCss"/>"><bean:write name="resultBean" property="scanningDeliveredDate"/></td>
					<td class="datefield" title="FY14/13 Completed Date"                                 style="<bean:write name="resultBean" property="fy1314CompletedDateCss"/>"><bean:write name="resultBean" property="fy1314CompletedDate"/></td>
					<td class="datefield" title="FY14/13 CD Sent for Customer Date"                      style="<bean:write name="resultBean" property="fy1314BurnedDateCss"/>"><bean:write name="resultBean" property="fy1314BurnedDate"/></td>
					<td class="datefield" title="FY12/11 Completed Date"                                 style="<bean:write name="resultBean" property="fy1112CompletedDateCss"/>"><bean:write name="resultBean" property="fy1112CompletedDate"/></td>
					<td class="datefield" title="Extract File Created"                                   style="<bean:write name="resultBean" property="extractDateCss"/>"><bean:write name="resultBean" property="extractDate"/></td>
					<td class="datefield" title="All backfile CD mailed to Cust/SD"                      style="<bean:write name="resultBean" property="fy1314MailedDateCss"/>"><bean:write name="resultBean" property="fy1314MailedDate"/></td>
					<td class="datefield" title="Verify All Backfiles Installed in FACET"                style="<bean:write name="resultBean" property="laptopInstalledDateCss"/>"><bean:write name="resultBean" property="laptopInstalledDate"/></td>
					<td class="datefield" title="All Backfile CD Delivered to LogCop"                    style="<bean:write name="resultBean" property="logcopDeliveredDateCss"/>"><bean:write name="resultBean" property="logcopDeliveredDate"/></td>
					<td class="datefield" title="Verified LOGCOP Backfile Uploaded"                      style="<bean:write name="resultBean" property="logcopUploadedDateCss"/>"><bean:write name="resultBean" property="logcopUploadedDate"/></td>
					<td class="datefield" title="Final Backfile Report Generated and Given to CompacFlt" style="<bean:write name="resultBean" property="finalReportDateCss"/>"><bean:write name="resultBean" property="finalReportDate"/></td>
				</tr>
			</logic:iterate>
			</tbody>
			</table>
			</center>
			</p>
		<% } %>
	</div>
	<div id="tabs-3">
		<% if (completedList.size() == 0) { %>
			<p class="error" align="center">
				No Completed Workfiles Found
				<logic:notEmpty name="inputBean" property="contractNumber">
					(for contract #<bean:write name="inputBean" property="contractNumber"/>)
				</logic:notEmpty>
			</p>
		<% } else { %>
			<p align="center">
			<center>
			<table id="tanTable_style2" border="0" cellspacing="0" class="alt-color" width="100%">
			<thead>
				<tr class="ignore">
					<td colspan="19" class="recordCnt">
						<b><%= completedList.size() %> workflows found</b>
						<a href="backfile.do?action=workflowSummaryXls2&type=completed&projectPk=<bean:write name="projectPk"/>"><img src="images/file_icons/sml_file_xls.gif"> Detailed Report</a>
					</td>
				</tr>
				<tr>
					<th colspan="3">Ship Information</th>
					<th rowspan="2">Action</th>
					<th colspan="16">Backfile Workflow</th>
				</tr>
				<tr>
					<th nowrap>
						<a href="javascript: changeSort('ship_name', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Unit Name</a>
						<% if (sortBy.equals("ship_name") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("ship_name") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th nowrap>
						<a href="javascript: changeSort('type_hull', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Type/Hull</a>
						<% if (sortBy.equals("type_hull") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("type_hull") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th nowrap>
						<a href="javascript: changeSort('homeport', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Homeport</a>
						<% if (sortBy.equals("homeport") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("homeport") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>

					<th>Overall<br/>Due<br/>Date</th>
					<th>FY14/13<br/>Due<br/>Date</th>
					<th nowrap>
						<a href="javascript: changeSort('sched_training_date', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Training<br/>Date</a>
						<% if (sortBy.equals("sched_training_date") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("sched_training_date") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th>Completed<br/>Date</th>
					<th>Backfile<br/>Date<br/>Requested</th>
					<th>Date<br/>Received<br/>By PSHI</th>
					<th>Date<br/>Delivered<br/>to Scanning</th>
					<th>FY14/13<br/>Completed<br/>Date</th>
					<th>FY14/13<br/>CD Sent<br/>for Customer<br/>Date</th>
					<th>FY12/11<br/>Completed<br/>Date</th>
					<th>Extract File<br/>Created</th>
					<th>All backfile<br/>CD mailed<br/>to Cust/SD</th>
					<th>Verify<br/>All Backfiles<br/>Installed<br/>in FACET</th>
					<th>All Backfile<br/>CD Delivered<br/>to LogCop</th>
					<th>Verified<br/>LOGCOP<br/>Backfile<br/>Uploaded</th>
					<th>Final Backfile<br/>Report<br/>Generated<br/>and Given to<br/>CompacFlt</th>
				</tr>
			</thead>
			<tbody>
			<logic:iterate id="resultBean" name="completedList" type="com.premiersolutionshi.old.bean.BackfileBean">
				<tr>
					<td>
						<bean:write name="resultBean" property="shipName"/>
						<logic:notEmpty name="resultBean" property="comments">
							<img src="images/icon_comments.png" height="13" width="13" title="<bean:write name="resultBean" property="comments"/>"/>
						</logic:notEmpty>
					</td>
					<td>
						<bean:write name="resultBean" property="type"/>
						<bean:write name="resultBean" property="hull"/>
					</td>
					<td><bean:write name="resultBean" property="homeport"/></td>
					<td align="center" nowrap>
						<a href="backfile.do?action=workflowEdit&backfileWorkflowPk=<bean:write name="resultBean" property="backfileWorkflowPk"/>&projectPk=<bean:write name="projectPk"/>"><img src="images/icon_edit.png" title="Edit <bean:write name="resultBean" property="shipName"/>"/></a>
						&nbsp;<a href="backfile.do?action=workflowDeleteDo&backfileWorkflowPk=<bean:write name="resultBean" property="backfileWorkflowPk"/>&projectPk=<bean:write name="projectPk"/>" onclick="return confirmDelete('<bean:write name="resultBean" property="shipName"/>');"><img src="images/icon_delete.png" title="Delete <bean:write name="resultBean" property="shipName"/>"/></a>
					</td>
					<td class="datefield" title="Overall Due Date"                                       style="<bean:write name="resultBean" property="dueDateCss"/>"><bean:write name="resultBean" property="dueDate"/></td>
					<td class="datefield" title="FY14/13 Due Date"                                       style="<bean:write name="resultBean" property="fy1314DueDateCss"/>"><bean:write name="resultBean" property="fy1314DueDate"/></td>

					<td class="datefield" title="Scheduled Training Date" style="background:#ccc;"><bean:write name="resultBean" property="schedTrainingDate"/></td>
					<td class="datefield" title="Completed Date"><bean:write name="resultBean" property="completedDate"/></td>

					<td class="datefield" title="Backfile Date Requested"                                style="<bean:write name="resultBean" property="requestedDateCss"/>"><bean:write name="resultBean" property="requestedDate"/></td>
					<td class="datefield" title="Date Received By PSHI"                                  style="<bean:write name="resultBean" property="receivedDateCss"/>"><bean:write name="resultBean" property="receivedDate"/></td>
					<td class="datefield" title="Date Delivered to Scanning"                             style="<bean:write name="resultBean" property="scanningDeliveredDateCss"/>"><bean:write name="resultBean" property="scanningDeliveredDate"/></td>
					<td class="datefield" title="FY14/13 Completed Date"                                 style="<bean:write name="resultBean" property="fy1314CompletedDateCss"/>"><bean:write name="resultBean" property="fy1314CompletedDate"/></td>
					<td class="datefield" title="FY14/13 CD Sent for Customer Date"                      style="<bean:write name="resultBean" property="fy1314BurnedDateCss"/>"><bean:write name="resultBean" property="fy1314BurnedDate"/></td>
					<td class="datefield" title="FY12/11 Completed Date"                                 style="<bean:write name="resultBean" property="fy1112CompletedDateCss"/>"><bean:write name="resultBean" property="fy1112CompletedDate"/></td>
					<td class="datefield" title="Extract File Created"                                   style="<bean:write name="resultBean" property="extractDateCss"/>"><bean:write name="resultBean" property="extractDate"/></td>
					<td class="datefield" title="All backfile CD mailed to Cust/SD"                      style="<bean:write name="resultBean" property="fy1314MailedDateCss"/>"><bean:write name="resultBean" property="fy1314MailedDate"/></td>
					<td class="datefield" title="Verify All Backfiles Installed in FACET"                style="<bean:write name="resultBean" property="laptopInstalledDateCss"/>"><bean:write name="resultBean" property="laptopInstalledDate"/></td>
					<td class="datefield" title="All Backfile CD Delivered to LogCop"                    style="<bean:write name="resultBean" property="logcopDeliveredDateCss"/>"><bean:write name="resultBean" property="logcopDeliveredDate"/></td>
					<td class="datefield" title="Verified LOGCOP Backfile Uploaded"                      style="<bean:write name="resultBean" property="logcopUploadedDateCss"/>"><bean:write name="resultBean" property="logcopUploadedDate"/></td>
					<td class="datefield" title="Final Backfile Report Generated and Given to CompacFlt" style="<bean:write name="resultBean" property="finalReportDateCss"/>"><bean:write name="resultBean" property="finalReportDate"/></td>
				</tr>
			</logic:iterate>
			</tbody>
			</table>
			</center>
			</p>
		<% } %>
	</div>

	<div id="tabs-4">
		<% if (notRequiredList.size() == 0) { %>
			<p class="error" align="center">
				No Workfiles (Not-Required) Found
				<logic:notEmpty name="inputBean" property="contractNumber">
					(for contract #<bean:write name="inputBean" property="contractNumber"/>)
				</logic:notEmpty>
			</p>
		<% } else { %>
			<p align="center">
			<center>
			<table id="tanTable_style2" border="0" cellspacing="0" class="alt-color" width="100%">
			<thead>
				<tr class="ignore">
					<td colspan="19" class="recordCnt">
						<b><%= notRequiredList.size() %> workflows found</b>
						<a href="backfile.do?action=workflowSummaryXls2&type=notRequired&projectPk=<bean:write name="projectPk"/>"><img src="images/file_icons/sml_file_xls.gif"> Detailed Report</a>
					</td>
				</tr>
				<tr>
					<th colspan="3">Ship Information</th>
					<th rowspan="2">Action</th>
					<th colspan="16">Backfile Workflow</th>
				</tr>
				<tr>
					<th nowrap>
						<a href="javascript: changeSort('ship_name', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Unit Name</a>
						<% if (sortBy.equals("ship_name") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("ship_name") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th nowrap>
						<a href="javascript: changeSort('type_hull', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Type/Hull</a>
						<% if (sortBy.equals("type_hull") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("type_hull") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th nowrap>
						<a href="javascript: changeSort('homeport', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Homeport</a>
						<% if (sortBy.equals("homeport") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("homeport") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>

					<th>Overall<br/>Due<br/>Date</th>
					<th>FY14/13<br/>Due<br/>Date</th>
					<th nowrap>
						<a href="javascript: changeSort('sched_training_date', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Training<br/>Date</a>
						<% if (sortBy.equals("sched_training_date") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("sched_training_date") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th>Completed<br/>Date</th>
					<th>Backfile<br/>Date<br/>Requested</th>
					<th>Date<br/>Received<br/>By PSHI</th>
					<th>Date<br/>Delivered<br/>to Scanning</th>
					<th>FY14/13<br/>Completed<br/>Date</th>
					<th>FY14/13<br/>CD Sent<br/>for Customer<br/>Date</th>
					<th>FY12/11<br/>Completed<br/>Date</th>
					<th>Extract File<br/>Created</th>
					<th>All backfile<br/>CD mailed<br/>to Cust/SD</th>
					<th>Verify<br/>All Backfiles<br/>Installed<br/>in FACET</th>
					<th>All Backfile<br/>CD Delivered<br/>to LogCop</th>
					<th>Verified<br/>LOGCOP<br/>Backfile<br/>Uploaded</th>
					<th>Final Backfile<br/>Report<br/>Generated<br/>and Given to<br/>CompacFlt</th>
				</tr>
			</thead>
			<tbody>
			<logic:iterate id="resultBean" name="notRequiredList" type="com.premiersolutionshi.old.bean.BackfileBean">
				<tr>
					<td>
						<bean:write name="resultBean" property="shipName"/>
						<logic:notEmpty name="resultBean" property="comments">
							<img src="images/icon_comments.png" height="13" width="13" title="<bean:write name="resultBean" property="comments"/>"/>
						</logic:notEmpty>
					</td>
					<td>
						<bean:write name="resultBean" property="type"/>
						<bean:write name="resultBean" property="hull"/>
					</td>
					<td><bean:write name="resultBean" property="homeport"/></td>
					<td align="center" nowrap>
						<a href="backfile.do?action=workflowEdit&backfileWorkflowPk=<bean:write name="resultBean" property="backfileWorkflowPk"/>&projectPk=<bean:write name="projectPk"/>"><img src="images/icon_edit.png" title="Edit <bean:write name="resultBean" property="shipName"/>"/></a>
						&nbsp;<a href="backfile.do?action=workflowDeleteDo&backfileWorkflowPk=<bean:write name="resultBean" property="backfileWorkflowPk"/>&projectPk=<bean:write name="projectPk"/>" onclick="return confirmDelete('<bean:write name="resultBean" property="shipName"/>');"><img src="images/icon_delete.png" title="Delete <bean:write name="resultBean" property="shipName"/>"/></a>
					</td>
					<td colspan="16" style="text-align:center;background:#aaa;color:#555;font-weight:bold;">Not Required</td>
				</tr>
			</logic:iterate>
			</tbody>
			</table>
			</center>
			</p>
		<% } %>
	</div>

</div><br/>

<p>

<%-- Ashwini /support.do to ./support.do --%>
<logic:equal name="projectPk" value="1"> <!-- Default to Support Issue (for project 1 only) -->
	<a href="./support.do?action=issueList&projectPk=<bean:write name="projectPk"/>"><img src="images/home.png"/></a>
</logic:equal>
<%-- Ashwini /project.do to ./project.do %-->
<logic:notEqual name="projectPk" value="1"> <!-- Default to Task List -->
	<a href="./project.do?action=taskList&searchPerformed=Y&projectPk=<bean:write name="projectPk"/>"><img src="images/home.png"/></a>
</logic:notEqual>
</p>

</center>

<form name="sortForm" action="backfile.do" method="GET">
	<input type="hidden" name="action" value="workflowSummary"/>
	<input type="hidden" name="projectPk" value="<bean:write name="projectPk"/>"/>
	<input type="hidden" name="sortBy" value="<bean:write name="sortBy"/>"/>
	<input type="hidden" name="sortDir" value="<bean:write name="sortDir"/>"/>
	<input type="hidden" name="searchPerformed" value="Y"/>
</form>

<%@ include file="../include/app-footer.jsp" --%>

<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-altcolor.js"></script>

<script type="text/javascript">
	$(function() {
		$("#tabs").tabs();
		$(document).tooltip();
	});

	$(document).ready(function () {
		$('.datefield').each(function() {
//2014.03.05 ATT :: Disabled graying out of blank elements
//				if ($(this).html() == "") $(this).css("background-color", "#aaa");
			$(this).css("font-size", "85%");
		});
	});

	function confirmDelete(shipName) {
		return confirm("Are you sure you want to delete the backfile workflow for " + shipName);
	} //end of confirmDelete
</script>
</body>
</html>
