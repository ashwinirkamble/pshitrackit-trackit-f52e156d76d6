<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<bean:define id="defaultPageTitle" value="Laptop List"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk"		scope="request" class="java.lang.String"/>
<jsp:useBean id="resultList"	scope="request" class="java.util.ArrayList"/>

<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">

  <title>TrackIT - <bean:write name="pageTitle"/></title>

	<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/stylesheet.css"/>
	<link rel="stylesheet" type="text/css" href="css/jquery.dataTables.min.css"/>
</head>

<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask leftmenu"><div class="colright">
	<div class="col1wrap"><div class="col1">
		<%@ include file="../include/content-header.jsp" %>

    <tags:projectPkBreadcrumb projectPk="${projectPk}" pageTitle="${pageTitle}" hideOptions="true" />

		<a href="hardware.do?action=laptopAdd&projectPk=<bean:write name="projectPk"/>" class="btn btn-success"><span class="glyphicon glyphicon-plus"></span> Add New Laptop</a>

		<% if (resultList.size() == 0) { %>
			<p class="error" align="center">
				No Laptops Found
			</p>
		<% } else { %>
			<a href="export.do?action=laptopXlsx&projectPk=<bean:write name="projectPk"/>" class="btn btn-default"><img src="images/file_icons/sml_file_xls.gif"/> Export to Excel</a>

			<p align="center">
			<center>
			<table id="laptopTable" class="display" cellspacing="0">
			<thead>
				<tr>
					<th>Ship Assigned To</th>
					<th>Status</th>
					<th>Computer Name</th>
					<th>Tag</th>
					<th>Customer</th>
					<th>Contract Number</th>
					<th>Product Name</th>
					<th>Serial Number</th>
					<th>Date Received</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody>
			<logic:iterate id="resultBean" name="resultList" type="com.premiersolutionshi.old.bean.HardwareBean">
				<tr align="left" valign="top">
					<td width="300">
						<bean:write name="resultBean" property="shipName"/><br/>
						<span style="color:#777;font-style:italic;"><bean:write name="resultBean" property="homeport"/></span>
					</td>
					<td width="200"><bean:write name="resultBean" property="status"/><br/><bean:write name="resultBean" property="statusNotes"/></td>
					<td><bean:write name="resultBean" property="computerName"/></td>
					<td><bean:write name="resultBean" property="tag"/></td>
					<td><bean:write name="resultBean" property="customer"/></td>
					<td><bean:write name="resultBean" property="contractNumber"/></td>
					<td><bean:write name="resultBean" property="productName"/></td>
					<td><bean:write name="resultBean" property="serialNumber"/></td>
					<td align="center"><bean:write name="resultBean" property="receivedDate"/></td>
					<td align="center">
						<a href="hardware.do?action=laptopEdit&laptopPk=<bean:write name="resultBean" property="laptopPk"/>&projectPk=<bean:write name="projectPk"/>"><img src="images/icon_edit.png" title="Edit"/></a>
						&nbsp;<a href="hardware.do?action=laptopDeleteDo&laptopPk=<bean:write name="resultBean" property="laptopPk"/>&projectPk=<bean:write name="projectPk"/>" onclick="return confirmDelete('<bean:write name="resultBean" property="productName"/>');"><img src="images/icon_delete.png" title="Delete"/></a>
					</td>
				</tr>
			</logic:iterate>
			</tbody>
			</table>
			</center>
			</p>

			<p align="center">
			<a href="export.do?action=laptopXlsx&projectPk=<bean:write name="projectPk"/>" class="btn btn-default"><img src="images/file_icons/sml_file_xls.gif"/> Export to Excel</a>
			</p>
		<% } %>
	</div></div>

	<%@ include file="../include/app-col2.jsp" %>
</div></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>

<script type="text/javascript">
	$(document).ready(function () {
		var table = $('#laptopTable').DataTable( {
			"order": [[ 1, "asc" ]],
			"lengthMenu": [[10, 50, 100, -1], [10, 50, 100, "All"]],
			"pageLength": -1,
			"columnDefs": [
				{ "orderable": false, "targets": 7 }
			],
			stateSave: true
		});
	});

	function confirmDelete(productName) {
		return confirm("Are you sure you want to delete " + productName);
	} //end of confirmDelete
</script>

</body>
</html>
