<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="defaultPageTitle" value="Decom Add/Edit"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk" scope="request" class="java.lang.String"/>
<jsp:useBean id="inputBean" scope="request" class="com.premiersolutionshi.old.bean.DecomBean"/>
<jsp:useBean id="projectList" scope="request" class="java.util.ArrayList"/>

<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">

  <title>TrackIT - <bean:write name="pageTitle"/></title>

	<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/stylesheet.css"/>
	<link rel="stylesheet" type="text/css" href="css/jquery-ui.min.css"/>

	<style>
		.workflowDate {
			text-align: center;
			}
	 </style>
</head>
<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask leftmenu"><div class="colright">
	<div class="col1wrap"><div class="col1">
		<%@ include file="../include/content-header.jsp" %>

    <tags:projectPkBreadcrumb projectPk="${projectPk}" pageTitle="${pageTitle}" hideOptions="true"
      parentUrl="decom.do?action=workflowSummary&projectPk=${projectPk}" parentTitle="Decom Workflow List"/>

		<p align="center">
		Fields marked with an asterisk <span class="regAsterisk">*</span> are required.
		</p>

		<p align="center">
		<html:form action="decom.do" onsubmit="return valFields();" method="POST">
		<input type="hidden" name="projectPk" value="<bean:write name="projectPk"/>"/>
		<table id="tanTable_style2" border="0" cellspacing="0" width="740">
		<html:hidden name="inputBean" property="shipName" styleId="shipName"/>
		<input type="hidden" name="action" value="workflowEditDo"/>
		<html:hidden name="inputBean" property="decomWorkflowPk"/>
		<tbody>
			<tr><th>Ship</th></tr>
			<tr><td class="nobordered" align="center"> <!-- tan_table -->
				<center>
				<table class="border-zero cellspacing-zero cellpadding-3">
				<tbody>
					<tr>
						<td class="fieldName">Vessel Name:</td>
						<td class="fieldValue"><bean:write name="inputBean" property="shipName"/></td>
					</tr>
					<tr>
						<td class="fieldName">Type/Hull:</td>
						<td class="fieldValue">
							<bean:write name="inputBean" property="type"/>
							<bean:write name="inputBean" property="hull"/>
						</td>
					</tr>
					<tr>
						<td class="fieldName">TYCOM:</td>
						<td class="fieldValue"><bean:write name="inputBean" property="tycomDisplay"/></td>
					</tr>
					<tr>
						<td class="fieldName">Homeport:</td>
						<td class="fieldValue"><bean:write name="inputBean" property="homeport"/></td>
					</tr>
					<tr>
						<td class="fieldName">RSupply:</td>
						<td class="fieldValue"><bean:write name="inputBean" property="rsupply"/></td>
					</tr>
					<tr>
						<td class="fieldName">Computer Name(s):</td>
						<td class="fieldValue"><bean:write name="inputBean" property="computerName"/></td>
					</tr>
					<tr>
						<td class="fieldName">Laptop Tag(s):</td>
						<td class="fieldValue"><bean:write name="inputBean" property="laptopTag"/></td>
					</tr>
					<tr>
						<td class="fieldName">Scanner Tag(s):</td>
						<td class="fieldValue"><bean:write name="inputBean" property="scannerTag"/></td>
					</tr>
				</tbody>
				</table>
				</center>
			</td></tr> <!-- end tan_table -->

			<tr><td class="nobordered" align="left"> <!-- tan_table -->
				<table class="border-zero cellspacing-zero cellpadding-3">
				<tbody>
					<tr>
						<td class="fieldName"><span style="color:red;font-weight:bold;">DECOM DATE</span></td>
						<td><html:text name="inputBean" property="decomDate" styleClass="form-control input-sm workflowDate"/></td>
					</tr>
					<tr>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td class="fieldName">Client Contacted With Decom Instructions</td>
						<td><html:text name="inputBean" property="shipContactedDate" styleClass="form-control input-sm workflowDate"/></td>
					</tr>
					<tr>
						<td class="fieldName">Hardware Received By PSHI and Inventory Lists 'Status' Updated</td>
						<td><html:text name="inputBean" property="systemReceivedDate" styleClass="form-control input-sm workflowDate"/></td>
					</tr>
					<tr>
						<td class="fieldName TOP">Status of Hardware Received</td>
						<td class="TOP">
							<html:select name="inputBean" property="hardwareStatus" styleClass="form-control input-sm">
								<html:option value=""/>
								<html:option value="Complete"/>
								<html:option value="Partial"/>
							</html:select>
						</td>
						<td class="fieldName TOP">Notes</td>
						<td class="TOP"><html:textarea name="inputBean" property="hardwareStatusNotes" rows="3" styleClass="form-control input-sm"/></td>
					</tr>
					<tr>
						<td class="fieldName">FIARModule Folder(s) Copied to P Drive</td>
						<td><html:text name="inputBean" property="systemReturnedDate" styleClass="form-control input-sm workflowDate"/></td>
					</tr>
					<tr>
						<td class="fieldName">Backfile Provided to TYCOM</td>
						<td><html:text name="inputBean" property="backupDate" styleClass="form-control input-sm workflowDate"/></td>
					</tr>
					<tr>
						<td class="fieldName">Records Checked Against LOGCOP</td>
						<td><html:text name="inputBean" property="transmittalCheckDate" styleClass="form-control input-sm workflowDate"/></td>
					</tr>
					<tr>
						<td class="fieldName">Transmittals Reconciled With LOGCOP</td>
						<td><html:text name="inputBean" property="transmittalReconDate" styleClass="form-control input-sm workflowDate"/></td>
					</tr>
					<tr>
						<td class="fieldName">Laptop Reset and All Hardware Availability Updated</td>
						<td><html:text name="inputBean" property="laptopResetDate" styleClass="form-control input-sm workflowDate"/></td>
					</tr>
				</tbody>
				</table>
			</td></tr> <!-- end tan_table -->

			<tr><th>Comments</th></tr>
			<tr><td class="nobordered" align="center"> <!-- tan_table -->
				<html:textarea name="inputBean" property="comments" rows="5"  styleClass="form-control input-sm"/>
			</td></tr> <!-- end tan_table -->
		</tbody>
		</table>

		<table id="borderlessTable" border="0" cellspacing="0">
		<tbody>
			<tr>
				<td align="center">
					<button type="submit" value="Submit" class="btn btn-primary"><span class="glyphicon glyphicon-ok"></span> Save</button>
				</td>
				<td align="center">
					<a class="btn btn-default" href="decom.do?action=workflowSummary&projectPk=<bean:write name="projectPk"/>"><span class="glyphicon glyphicon-remove"></span> Cancel</a>
				</td>
			</tr>
		</tbody>
		</table>
		</html:form>
		</p>
	</div></div>

	<%@ include file="../include/app-col2.jsp" %>
</div></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/valdate.js"></script>

<script type="text/javascript">
	$(function() {
		var homeportList = [
			<logic:iterate id="homeport" name="homeportList" type="java.lang.String" indexId="i">
				<% if (i > 0) { %>, <% } %>"<bean:write name="homeport"/>"
			</logic:iterate>
		];

		$("#homeport").autocomplete({
			source: homeportList
		});

		$(".workflowDate").attr('autocomplete', 'off');
		$(".workflowDate").datepicker();
		$(".datepicker").attr('autocomplete', 'off');
		$(".datepicker").datepicker();
	});

	function valFields() {
		return true;
	} //end of valFields
</script>

</body>
</html>
