<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>
<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="nested"  uri="http://struts.apache.org/tags-nested" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="defaultPageTitle" value="FACET Configured System List"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk"		scope="request" class="java.lang.String"/>
<jsp:useBean id="resultList" 	scope="request" class="java.util.ArrayList"/>

<jsp:useBean id="currFacetVersion" scope="request" class="java.lang.String"/>
<jsp:useBean id="currDmsVersion"	 scope="request" class="java.lang.String"/>

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

		<p align="center">
		<a href="system.do?action=configuredSystemAdd&projectPk=<bean:write name="projectPk"/>" class="btn btn-success"><span class="glyphicon glyphicon-plus"></span> Configure New System</a>
		</p>

    <tags:projectPkBreadcrumb projectPk="${projectPk}" pageTitle="${pageTitle}" hideOptions="true"/>

		<% if (resultList.size() == 0) { %>
			<p class="error" align="center">
				No Results Found
			</p>
		<% } else { %>
			<div id="data-loading" style="display:block;text-align:left;">
			Loading...
			</div>

			<div id="data-div" style="display:none;">
			<p>
			<a href="export.do?action=configuredSystemXlsx&projectPk=<bean:write name="projectPk"/>" class="btn btn-default"><img src="images/file_icons/sml_file_xls.gif"/> Export to Excel</a>
			</p>

			<p align="left">
			Show/Hide Columns:
			<a class="toggle-vis" data-column="3" href="javascript:void(0);"><span class="glyphicon glyphicon-eye-open" id="vis-3" style="color:black;"></span> Status</a>
			&nbsp;
			<a class="toggle-vis" data-column="5" href="javascript:void(0);"><span class="glyphicon glyphicon-eye-close" id="vis-5" style="color:black;"></span> Working Capital Fund</a>
			&nbsp;
			<a class="toggle-vis" data-column="6" href="javascript:void(0);"> <span class="glyphicon glyphicon-eye-open" id="vis-6" style="color:black;"></span> Current Location</a>
			&nbsp;
			<a class="toggle-vis" data-column="7" href="javascript:void(0);"> <span class="glyphicon glyphicon-eye-close" id="vis-7" style="color:black;"></span> Ghost Version</a>
			&nbsp;
			<a class="toggle-vis" data-column="9" href="javascript:void(0);"> <span class="glyphicon glyphicon-eye-close" id="vis-9" style="color:black;"></span> DMS Version</a>
			&nbsp;
			<a class="toggle-kofax" href="javascript:void(0);"> <span class="glyphicon glyphicon-eye-close" id="vis-k" style="color:black;"></span> Kofax License</a>
			&nbsp;
			<a class="toggle-vis" data-column="12" href="javascript:void(0);"> <span class="glyphicon glyphicon-eye-close" id="vis-12" style="color:black;"></span> Network Adapter Type</a>
			</p>

			<p align="center">
			<table id="configuredSystemTable" class="row-border stripe" style="width:100%;">
			<thead>
				<tr>
					<th>Ship Assigned</th>
					<th>Homeport</th>
					<th>Computer Name</th>
					<th>Status</th>
					<th>Multi-Ship</th>
					<th>Working Capital Fund</th>
					<th>Current Location</th>
					<th>Ghost Version</th>
					<th>FACET<br/>Version</th>
					<th>DMS<br/>Version</th>
					<th>Kofax Serial No</th>
					<th>Kofax Product Code</th>
					<th>Network Adapter Type</th>
					<th>Last Support Visit</th>
					<th>Last Support Reason</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody>
			<logic:iterate id="resultBean" name="resultList" type="com.premiersolutionshi.old.bean.SystemBean">
				<tr style="vertical-align:top;" align="left">
					<td>
						<a href="system.do?action=configuredSystemDetail&configuredSystemPk=<bean:write name="resultBean" property="configuredSystemPk"/>&projectPk=<bean:write name="projectPk"/>">
						<logic:notEmpty name="resultBean" property="shipName">
							<b><bean:write name="resultBean" property="shipName"/></b>
						</logic:notEmpty>
						<logic:empty name="resultBean" property="shipName">
							<i>Unassigned</i>
						</logic:empty>
						</a>
						<logic:notEmpty name="resultBean" property="decomDate">
							<br/><span style="color:#F00;font-weight:bold;">DECOM Date: <bean:write name="resultBean" property="decomDate"/></span>
						</logic:notEmpty>
					</td>
					<td><bean:write name="resultBean" property="homeport"/></td>
					<td><b><bean:write name="resultBean" property="computerName"/></b></td>
					<td>
						<logic:equal name="resultBean" property="isPreppedInd" value="Y">
							<i>Prepped &amp; Ready</i>
						</logic:equal>
						<logic:equal name="resultBean" property="isPreppedInd" value="A">
							<span style="color:#0A0;">Active</span>
						</logic:equal>
					</td>

					<td align="center">
						<logic:equal name="resultBean" property="multiShipInd" value="Y">Y</logic:equal>
						<logic:notEqual name="resultBean" property="multiShipInd" value="Y">N</logic:notEqual>
					</td>

					<td align="center">
						<logic:equal name="resultBean" property="nwcfInd" value="Y">Y</logic:equal>
						<logic:notEqual name="resultBean" property="nwcfInd" value="Y">N</logic:notEqual>
					</td>

					<td><bean:write name="resultBean" property="location"/></td>
					<td><bean:write name="resultBean" property="ghostVersion"/></td>
					<td>
						<logic:notEmpty name="resultBean" property="facetVersion">
							<bean:write name="resultBean" property="facetVersion"/>
							<% if (resultBean.getFacetVersion().equals(currFacetVersion)) { %>
								<img src="images/checkbox_checked.png"/>
							<% } else { %>
								<img src="images/icon_error.gif"/>
							<% } %>
						</logic:notEmpty>
					</td>
					<td>
						<logic:notEmpty name="resultBean" property="dmsVersion">
							<bean:write name="resultBean" property="dmsVersion"/>
							<% if (resultBean.getDmsVersion().equals(currDmsVersion)) { %>
								<img src="images/checkbox_checked.png"/>
							<% } else { %>
								<img src="images/icon_error.gif"/>
							<% } %>
						</logic:notEmpty>
					</td>
					<td><bean:write name="resultBean" property="kofaxLicenseKey"/></td>
					<td><bean:write name="resultBean" property="kofaxProductCode"/></td>
					<td>
						<logic:notEmpty name="resultBean" property="networkAdapter">
							<bean:write name="resultBean" property="networkAdapter"/>
							<% if (resultBean.getNetworkAdapter().equals("MS Loopback Adapter")) { %>
								<img src="images/checkbox_checked.png"/>
							<% } else { %>
								<img src="images/icon_error.gif"/>
							<% } %>
						</logic:notEmpty>
					</td>
					<td>
						<logic:present name="resultBean" property="lastVisitBean">
							<bean:define id="lastVisitBean" name="resultBean" property="lastVisitBean" type="com.premiersolutionshi.old.bean.SupportBean"/>
							<logic:notEmpty name="lastVisitBean" property="supportVisitDate">
								<bean:write name="lastVisitBean" property="supportVisitDate"/>
							</logic:notEmpty>
						</logic:present>
					</td>
					<td>
						<logic:present name="resultBean" property="lastVisitBean">
							<bean:define id="lastVisitBean" name="resultBean" property="lastVisitBean" type="com.premiersolutionshi.old.bean.SupportBean"/>
							<logic:notEmpty name="lastVisitBean" property="supportVisitDate">
								<bean:write name="lastVisitBean" property="category"/>
							</logic:notEmpty>
						</logic:present>
					</td>
					<td align="center" nowrap>
						<a href="system.do?action=configuredSystemEdit&configuredSystemPk=<bean:write name="resultBean" property="configuredSystemPk"/>&projectPk=<bean:write name="projectPk"/>"><img src="images/icon_edit.png" title="Edit" height="16px"/></a>
						&nbsp;<a href="system.do?action=configuredSystemDeleteDo&configuredSystemPk=<bean:write name="resultBean" property="configuredSystemPk"/>&projectPk=<bean:write name="projectPk"/>" onclick="return confirmDelete('<bean:write name="resultBean" property="computerNameJs"/>');"><img src="images/icon_delete.png" title="Delete" height="16px"/></a>
					</td>
				</tr>
			</logic:iterate>
			</tbody>
			</table>

			<p>
			<a href="export.do?action=configuredSystemXlsx&projectPk=<bean:write name="projectPk"/>" class="btn btn-default"><img src="images/file_icons/sml_file_xls.gif"/> Export to Excel</a>
			</p>
			</div>
		<% } %>
	</div></div>

	<%@ include file="../include/app-col2.jsp" %>
</div></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>

<script type="text/javascript">
	$(document).ready(function () {
		var table = $('#configuredSystemTable')
			.on('init.dt', function () {
				$('#data-loading').hide();
				$('#data-div').show();
			})
			.DataTable( {
				"paging": false,
				"columnDefs": [
					{ "visible": false, "targets": 4 },
					{ "visible": false, "targets": 5 },
					{ "visible": false, "targets": 7 },
					{ "visible": false, "targets": 9 },
					{ "visible": false, "targets": 10 },
					{ "visible": false, "targets": 11 },
					{ "visible": false, "targets": 12 },
					{ "orderable": false, "targets": 15 }
				],
			"aoColumns": [
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				{ "orderSequence": [ "desc", "asc", "asc" ] },
				null
			],
				stateSave: false
			});

		$('a.toggle-kofax').on('click', function (e) {
			e.preventDefault();

			// Toggle the visibility
			if (table.column(10).visible()) {
				table.column(10).visible(false);
				table.column(11).visible(false);
				$('#vis-k').removeClass('glyphicon-eye-open').addClass('glyphicon-eye-close');
			} else {
				table.column(10).visible(true);
				table.column(11).visible(true);
				$('#vis-k').removeClass('glyphicon-eye-close').addClass('glyphicon-eye-open');
			} //end of if
		});

		$('a.toggle-vis').on( 'click', function (e) {
			e.preventDefault();

			// Get the column API object
			var column = table.column($(this).attr('data-column'));

			// Toggle the visibility
			if (column.visible()) {
				column.visible(false);
				$('#vis-' + $(this).attr('data-column')).removeClass('glyphicon-eye-open').addClass('glyphicon-eye-close');
			} else {
				column.visible(true);
				$('#vis-' + $(this).attr('data-column')).removeClass('glyphicon-eye-close').addClass('glyphicon-eye-open');
			} //end of else
		});
		$('a.toggle-kofax').click();
		$('a.toggle-kofax').click();
	});

	function confirmDelete(computerName) {
		return confirm("Are you sure you want to delete " + computerName);
	} //end of confirmDelete
</script>

</body>
</html>
