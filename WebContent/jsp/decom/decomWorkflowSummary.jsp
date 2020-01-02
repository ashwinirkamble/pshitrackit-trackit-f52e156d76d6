<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="defaultPageTitle" value="Decom Workflow List"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk" scope="request" class="java.lang.String"/>
<jsp:useBean id="inputBean" scope="request" class="com.premiersolutionshi.old.bean.DecomBean"/>
<jsp:useBean id="resultList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="contractNumberList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="shipList" scope="request" class="java.util.ArrayList"/>

<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">

  <title>TrackIT - <bean:write name="pageTitle"/></title>

	<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/stylesheet.css"/>
	<link rel="stylesheet" type="text/css" href="css/jquery.dataTables.min.css"/>

  <style>
  	.datefield {
			text-align: center;
			}
		#tanTable_style2 th {
			font-size: 11px;
			vertical-align: bottom;
			}
  </style>
</head>

<body>
<%@ include file="../include/app-header.jsp" %>

<%@ include file="../include/content-header.jsp" %>

<tags:projectPkBreadcrumb projectPk="${projectPk}" pageTitle="${pageTitle}" hideOptions="true" />

<% if (contractNumberList.size() > 0) { %>
	<div>
		<form class="form-inline" action="decom.do" method="GET">
			<input type="hidden" name="action" value="workflowSummary"/>
			<input type="hidden" name="searchPerformed" value="Y"/>
			<input type="hidden" name="projectPk" value="<bean:write name="projectPk"/>"/>
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
<% } %>

<% if (shipList.size() > 0) { %>
<div>
	<html:form action="decom.do" method="POST" styleClass="form-inline">
	<input type="hidden" name="action" value="workflowAddDo"/>
	<input type="hidden" name="projectPk" value="<bean:write name="projectPk"/>"/>
	<div class="form-group">
	<label for="shipPk">Start Tracking Ship:</label>
	<html:select name="inputBean" property="shipPk" styleId="shipPk" styleClass="form-control input-sm">
		<html:options collection="shipList" property="shipPk" labelProperty="shipName"/>
	</html:select>
	<input type="hidden" id="shipName" name="shipName" value=""/>
	</div>
	<html:submit value="Add" styleClass="btn btn-success"/>
	</html:form>
</div></br>
<% } %>

<% if (resultList.size() > 0) { %>
	<p align="center">
	<a href="export.do?action=decomWorkflowXlsx&projectPk=<bean:write name="projectPk"/>" class="btn btn-default"><img src="images/file_icons/sml_file_xls.gif"/> Export to Excel</a>
	</p>
<% } %>

<p>
<table id="decomTable" class="display" cellspacing="0">
<thead>
	<tr>
		<th colspan="5">Vessel Information</th>
		<th rowspan="2">Action</th>
		<th colspan="9">Decom Workflow</th>
	</tr>
	<tr>
		<th>Vessel Name</th>
		<th>Type/Hull</th>
		<th>Homeport</th>
		<th>RSupply</th>
		<th>Decom Date</th>
		<th>Client Contacted With Decom Instructions</th>
		<th>Hardware Received By PSHI and Inventory Lists 'Status' Updated</th>
		<th>Status of Hardware Received</th>
		<th>FIARModule Folder(s) Copied to P Drive</th>
		<th>Backup Provided<br/>to TYCOM</th>
		<th>Records Checked<br/>Against LOGCOP</th>
		<th>Transmittal Reconciled<br/>With LOGCOP</th>
		<th>Laptop Reset and All Hardware Availability Updated</th>
		<th>Comments</th>
	</tr>
</thead>
<tbody>
<logic:iterate id="resultBean" name="resultList" type="com.premiersolutionshi.old.bean.DecomBean">
	<tr valign="top">
		<td align="left"><bean:write name="resultBean" property="shipName"/></td>
		<td>
			<bean:write name="resultBean" property="type"/>
			<bean:write name="resultBean" property="hull"/>
		</td>
		<td><bean:write name="resultBean" property="homeport"/></td>
		<td><bean:write name="resultBean" property="rsupply"/></td>
		<td align="center" style="color:red;font-weight:bold;"><bean:write name="resultBean" property="decomDate"/></td>
		<td align="center" nowrap>
			<a href="decom.do?action=workflowEdit&uic=<bean:write name="resultBean" property="uic"/>&projectPk=<bean:write name="projectPk"/>"><img src="images/icon_edit.png" title="Edit <bean:write name="resultBean" property="shipName"/>"/></a>
			&nbsp;<a href="decom.do?action=workflowDeleteDo&decomWorkflowPk=<bean:write name="resultBean" property="decomWorkflowPk"/>&projectPk=<bean:write name="projectPk"/>" onclick="return confirmDelete('<bean:write name="resultBean" property="shipName"/>');"><img src="images/icon_delete.png" title="Delete <bean:write name="resultBean" property="shipName"/>"/></a>
		</td>
		<td align="center"><bean:write name="resultBean" property="shipContactedDate"/></td>
		<td align="center"><bean:write name="resultBean" property="systemReceivedDate"/></td>
		<td align="center">
			<bean:write name="resultBean" property="hardwareStatus"/>
			<br/><bean:write name="resultBean" property="hardwareStatusNotes"/>
		</td>
		<td align="center"><bean:write name="resultBean" property="systemReturnedDate"/></td>
		<td align="center"><bean:write name="resultBean" property="backupDate"/></td>
		<td align="center"><bean:write name="resultBean" property="transmittalCheckDate"/></td>
		<td align="center"><bean:write name="resultBean" property="transmittalReconDate"/></td>
		<td align="center"><bean:write name="resultBean" property="laptopResetDate"/></td>
		<td align="center"><bean:write name="resultBean" property="comments"/></td>
	</tr>
</logic:iterate>
</tbody>
</table>
</p>

<p>
<logic:equal name="projectPk" value="1"> <!-- Default to Support Issue (for project 1 only) -->
	<a href="support.do?action=issueList&projectPk=<bean:write name="projectPk"/>"><img src="images/home.png"/></a>
</logic:equal>
<logic:notEqual name="projectPk" value="1"> <!-- Default to Task List -->
	<a href="project.do?action=taskList&searchPerformed=Y&projectPk=<bean:write name="projectPk"/>"><img src="images/home.png"/></a>
</logic:notEqual>
</p>


<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>

<script type="text/javascript">
	$(document).ready(function () {
		var table = $('#decomTable').DataTable( {
			"paging": false,
			"columnDefs": [{ "orderable": false, "targets": 5 }],
			stateSave: true
		});
	});

	$(function() {
		$("#shipPk").on("change", function() {
			$("#shipName").val($(this).children("option").filter(":selected").text());
		}).change();
	});

	function confirmDelete(shipName) {
		return confirm("Are you sure you want to delete the decom workflow for " + shipName);
	} //end of confirmDelete
</script>

</body>
</html>
