<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<bean:define id="defaultPageTitle" value="Generate Support Feedback Form"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk" scope="request" class="java.lang.String"/>
<jsp:useBean id="inputBean" scope="request" class="com.premiersolutionshi.old.bean.SupportBean"/>

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
		.ibtnAdd, .ibtnAddBlank, .ibtnDel { font-size: 10px; }
	</style>
</head>

<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask leftmenu"><div class="colright">
	<div class="col1wrap"><div class="col1">
		<%@ include file="../include/content-header.jsp" %>

		<p align="center">
		<center>
		<html:form action="support.do" method="GET" target="_blank">
		<input type="hidden" name="action" value="generateFeedbackFormDo"/>
		<input type="hidden" name="projectPk" value="<bean:write name="projectPk"/>"/>

		<table id="tanTable_style2" border="0" cellspacing="0">
		<colgroup>
			<col width="140"/>
			<col width="90"/>
			<col width="45"/>
			<col width="145"/>
			<col width="400"/>
		</colgroup>
		<tbody>
			<tr>
				<td class="fieldName" style="vertical-align:top;padding-top:7px;">Vessel:</td>
				<td class="nobordered" colspan="3">
					<html:select name="inputBean" property="uic" styleId="uic" tabindex="1">
						<html:option value=""/>
						<logic:present name="shipList"><html:options collection="shipList" property="uic" labelProperty="shipNameTypeHull"/></logic:present>
					</html:select>
				</td>
				<td rowspan="4" class="nobordered"><table id="currShipTasks"></table></td>
			</tr>
			<tr>
				<td class="fieldName" style="vertical-align:top;padding-top:7px;">Date:</td>
				<td class="nobordered"><html:text name="inputBean" property="supportVisitDate" styleClass="datepicker" size="9" tabindex="2"/></td>
				<td class="fieldName" style="vertical-align:top;padding-top:7px;">Time:</td>
				<td class="nobordered"><html:text name="inputBean" property="supportVisitTime" size="3" maxlength="4" tabindex="3"/></td>
			</tr>
			<tr>
				<td class="fieldName" style="vertical-align:top;padding-top:7px;">Location:</td>
				<td class="nobordered" colspan="3"><html:text name="inputBean" property="supportVisitLoc" size="20" tabindex="3"/></td>
			</tr>
			<tr>
				<td class="fieldName" style="vertical-align:top;padding-top:7px;">PSHI Contact:</TD>
				<td class="nobordered" colspan="3">
					<logic:present name="userList">
					<logic:iterate id="userBean" name="userList" type="com.premiersolutionshi.old.bean.UserBean" indexId="i">
						<html:multibox name="inputBean" property="pocArr">
							<bean:write name="userBean" property="firstName"/>
							<bean:write name="userBean" property="lastName"/>
						</html:multibox>

						<a href="javascript:void(0);" onclick="document.supportForm.pocArr[<%= i %>].checked = !document.supportForm.pocArr[<%= i %>].checked">
							<bean:write name="userBean" property="firstName"/>
							<bean:write name="userBean" property="lastName"/>
						</a><br/>
					</logic:iterate>
					</logic:present>
				</td>
			</tr>
			<tr>
				<td class="fieldName" style="vertical-align:top;padding-top:10px;">Tasks To Be Completed:</td>
				<td colspan="5" class="nobordered">
					<table id="tasks"><tbody id="sortable"></tbody></table>
					<input type="button" class="ibtnAddBlank" value="Add Task"/>
				</td>
			</tr>
		</tbody>
		</table>

		<table id="borderlessTable" border="0" cellspacing="0"><tbody>
			<tr>
				<td align="center"><html:submit value="Generate Form"/></td>
				<td align="center"><input type="button" onclick="window.location='support.do?action=issueList&projectPk=<bean:write name="projectPk"/>';" value="Cancel"/></td>
			</tr>
		</tbody></table>
		</html:form>
		</center>
		</p>
	</div></div>

	<%@ include file="../include/app-col2.jsp" %>
</div></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/ajax.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-altcolor.js"></script>

<script type="text/javascript">
	var taskArr = new Array();
	var issueArr = new Array();

	$(function() {
		$(".datepicker").attr('autocomplete', 'off');
		$(".datepicker").datepicker();

		$( "#sortable" ).sortable();
		$( "#sortable" ).disableSelection();

		$('#uic').on('change', function() {
			$('#currShipTasks').html('<tr><td colspan="2" align="center" width="400"><img src="images/loading.gif"/></td></tr>');
			ajaxRequest('ajax.do?action=currShipTasks&uic=' + $(this).val() + "&projectPk=<bean:write name="projectPk"/>");
		});

		$('#input[name=supportVisitDate]').on('change', function() {
			$('input[name=supportVisitTime]').focus();
		})

		//Add blank task line
		$( ".ibtnAddBlank" ).click(function() {
			var newTrLine ='';
			newTrLine += '<tr>';
			newTrLine += '	<td><span class="ui-icon ui-icon-arrowthick-2-n-s"></span></td>';
			newTrLine += '	<td><input type="text" name="taskArr" size="70" value=""/></td>';
			newTrLine += '	<td><input type="button" class="ibtnDel" value="Delete"/></td>';
			newTrLine += '</tr>';
			$('#tasks > tbody:last').append(newTrLine);

			document.getElementsByName("taskArr")[document.getElementsByName("taskArr").length-1].focus();
		});

		//Copy task from clicked line to task list
		$('#currShipTasks').on('click', '.ibtnAdd', function (event) {
			var txt = $(this).parent().next().find('input').val();

			var newTrLine = '';
			newTrLine += '<tr>';
			newTrLine += '	<td><span class="ui-icon ui-icon-arrowthick-2-n-s"></span></td>';
			newTrLine += '	<td><input type="text" name="taskArr" size="70" value="' + txt.replace('"', '').trim() + '"/></td>';
			newTrLine += '	<td><input type="button" class="ibtnDel" value="Delete"/></td>';
			newTrLine += '</tr>';
			$('#tasks > tbody:last').append(newTrLine);
		});

		//Delete selected task
		$('#tasks').on('click', '.ibtnDel', function (event) {
			$(this).closest('tr').remove();
		});
	});

	//Rer
	function refreshShipTaskList() {
		var htmlStr = "";

		if ($("#uic").children("option").filter(":selected").text() != "") {
			htmlStr += '<tr><td colspan="2" style="font-weight:bold;">Outstanding Task List for ' + $("#uic").children("option").filter(":selected").text() + '</td></tr>';
			if (taskArr.length > 0) {
				$.each(taskArr, function(index, text) {
					htmlStr += '<tr>';
					htmlStr += '	<td width="40"><input type="button" class="ibtnAdd" value="Add"/></td>';
					htmlStr += '	<td><input type="hidden" value="' + text + '"/>' + text + '</td>';
					htmlStr += '</tr>';
				});
			} else {
				htmlStr += '<tr><td colspan="2"><i>None</i></td></tr>';
			} //end of else

			htmlStr += '<tr><td colspan="2">&nbsp;</td></tr>';

			htmlStr += '<tr><td colspan="2" style="font-weight:bold;">Open Issue List for ' + $("#uic").children("option").filter(":selected").text() + '</td></tr>';
			if (issueArr.length > 0) {
				$.each(issueArr, function(index, text) {
					htmlStr += '<tr>';
					htmlStr += '	<td width="40"><input type="button" class="ibtnAdd" value="Add"/></td>';
					htmlStr += '	<td><input type="hidden" value="' + text + '"/>' + text + '</td>';
					htmlStr += '</tr>';
				});
			} else {
				htmlStr += '<tr><td colspan="2"><i>None</i></td></tr>';
			} //end of else
		} //end of if

		$("#currShipTasks").html(htmlStr);
	} //end of refreshShipTaskList

	$(document).ready(function () {
		$("#uic").change();
		$("#uic").focus();
	});
</script>

</body>
</html>
