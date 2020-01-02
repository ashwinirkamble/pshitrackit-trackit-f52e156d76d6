<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<bean:define id="defaultPageTitle" value="Bulk Email Tool"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="configuredSystemList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="projectPk" scope="request" class="java.lang.String"/>

<jsp:useBean id="inputBean" scope="request" class="com.premiersolutionshi.old.bean.SupportBean"/>

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
	<link rel="stylesheet" type="text/css" href="css/jquery-ui.min.css"/>
	<link rel="stylesheet" type="text/css" href="css/jquery.dataTables.min.css"/>

	<style>
		#configuredSystemTable td {
			font-size: 85%;
			}
		#configuredSystemTable button {
			font-size: inherit;
			}
	</style>
</head>

<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask leftmenu"><div class="colright">
	<div class="col1wrap"><div class="col1">
		<%@ include file="../include/content-header.jsp" %>

		<html:form action="support.do" method="POST">
		<input type="hidden" name="action" value="bulkEmailToolDo"/>
		<input type="hidden" name="projectPk" value="<bean:write name="projectPk"/>"/>

		<p>
		<center>
		<table id="tanTable_style2" border="0" cellspacing="0" width="1020">
		<tbody>
			<tr><th>General Information</th></tr>
			<tr><td class="nobordered" align="left"> <!-- tan_table -->
				<table class="border-zero cellspacing-zero cellpadding-3">
				<colgroup>
					<col width="120"/>
				</colgroup>
				<tbody>
					<tr>
						<td class="fieldName"><span class="regAsterisk">*</span> Category:</td>
						<td>
							<html:select name="inputBean" property="category" styleId="category">
								<html:option value="Monthly E-Mail Notification"/>
								<html:option value="Follow-Up Training"/>
								<html:option value="FACET Update"/>
							</html:select>
						</td>
					</tr>
					<tr>
						<td class="fieldName"><span class="regAsterisk">*</span> Title/Summary:</td>
						<td><html:text name="inputBean" property="title" styleId="title" size="80" maxlength="100"/></td>
					</tr>
				</tbody>
				</table>
			</td></tr> <!-- end tan_table -->


			<tr><th>Detailed Information</th></tr>
			<tr><td class="nobordered" align="left"> <!-- tan_table -->
				<table class="border-zero cellspacing-zero cellpadding-3">
				<colgroup>
					<col width="120"/>
					<col width="230"/>
					<col width="100"/>
				</colgroup>
				<tbody>
					<tr>
						<td class="fieldName">Person Assigned:</td>
						<td>
							<html:select name="inputBean" property="personAssigned">
								<logic:present name="supportTeamList"><html:options name="supportTeamList"/></logic:present>
							</html:select>
						</td>
					</tr>
					<tr>
						<td class="fieldName"><span class="regAsterisk">*</span> Opened By:</td>
						<td><html:text name="inputBean" property="openedBy" styleId="openedBy" size="25" maxlength="75"/></td>
						<td class="fieldName"><span class="regAsterisk">*</span> Opened Date:</td>
						<td><html:text name="inputBean" property="openedDate" styleId="openedDate" styleClass="datepicker" size="9"/></td>
					</tr>
				</tbody>
				</table>
			</td></tr> <!-- end tan_table -->


			<tr><th>Current Status</th></tr>
			<tr><td class="nobordered" align="left"> <!-- tan_table -->
				<table class="border-zero cellspacing-zero cellpadding-3">
				<colgroup><col width="120"/></colgroup>
				<tbody>
					<tr>
						<td class="fieldName"><span class="regAsterisk">*</span> Status:</td>
						<td>
							<html:select name="inputBean" property="status" styleId="status">
								<html:option value="1 - New"/>
								<html:option value="2 - Active"/>
								<html:option value="3 - Resolved"/>
								<html:option value="4 - Pending Possible Resolution"/>
								<html:option value="6 - Closed (Successful)"/>
								<html:option value="7 - Closed (No Response)"/>
								<html:option value="8 - Closed (Unavailable)"/>
							</html:select>
						</td>
						<td class="closedDateTd fieldName">Closed Date:</td>
						<td class="closedDateTd"><html:text name="inputBean" property="closedDate" styleId="closedDate" styleClass="datepicker" size="9"/></td>
					</tr>
					<tr id="resolutionRow">
						<td class="fieldName"><span class="regAsterisk">*</span> Resolution:</td>
						<td><html:text name="inputBean" property="resolution" styleId="resolution" size="40" maxlength="50"/></td>
						<td class="fieldName">Total Time:</td>
						<td>
							<html:select name="inputBean" property="totalTime" styleId="totalTime">
								<html:option value="15">15 min</html:option>
								<html:option value="30">30 min</html:option>
								<html:option value="45">45 min</html:option>
								<html:option value="60">1 hour</html:option>
								<html:option value="90">1 hour 30 min</html:option>
								<html:option value="120">2 hours</html:option>
								<html:option value="150">2 hours 30 min</html:option>
								<html:option value="180">3 hours</html:option>
								<html:option value="210">3 hours 30 min</html:option>
								<html:option value="240">4 hours</html:option>
								<html:option value="270">4 hours 30 min</html:option>
								<html:option value="300">5 hours</html:option>
								<html:option value="330">5 hours 30 min</html:option>
								<html:option value="360">6 hours</html:option>
								<html:option value="390">6 hours 30 min</html:option>
								<html:option value="420">7 hours</html:option>
								<html:option value="450">7 hours 30 min</html:option>
								<html:option value="480">8 hours</html:option>
							</html:select>
						</td>
					</tr>
					<tr>
						<td class="autoCloseTd fieldName"><span class="glyphicon glyphicon-time"></span> Auto Close On:</td>
						<td class="autoCloseTd"><html:text name="inputBean" property="autoCloseDate" size="9" styleClass="datepicker"/></td>
						<td class="autoCloseTd fieldName"><span class="glyphicon glyphicon-time"></span> Auto Close To:</td>
						<td class="autoCloseTd">
							<html:select name="inputBean" property="autoCloseStatus">
								<html:option value=""/>
								<html:option value="6 - Closed (Successful)"/>
								<html:option value="7 - Closed (No Response)"/>
								<html:option value="8 - Closed (Unavailable)"/>
							</html:select>
						</td>
					</tr>
				</tbody>
				</table>
			</td></tr> <!-- end tan_table -->


			<tr class="emailBody"><th>E-Mail Body</th></tr>
			<tr class="emailBody"><td class="nobordered" align="center"> <!-- tan_table -->
				<textarea name="commentsArr" id="commentsArr" rows="10" class="form-control input-sm"></textarea><br/>
				Note: Do not use any &quot; character in the e-mail body as the e-mail may not be properly generated
			</td></tr> <!-- end tan_table -->


			<tr><th>Select FACET Units</th></tr>
			<tr><td class="nobordered" align="left"> <!-- tan_table -->

				<table id="configuredSystemTable" class="display" cellspacing="0">
				<thead>
					<tr>
						<th>Unit Assigned</th>
						<th>Homeport</th>
						<th>Computer Name</th>
						<th>Location</th>
						<th>Outstanding Issues</th>
						<th>Monthly<br/>E-Mail</th>
						<th>E-Mail</th>
						<th>
							Include<br/>
							<a id="selectAllLink" href="javascript:void(0);">Select All</a><br/>
							<a id="emailAllLink" class="emailBody" href="javascript:void(0);">E-Mail Selected</a>
						</th>
					</tr>
				</thead>
				<tbody>
				<% String currShipPk = "-1"; %>
				<logic:iterate id="resultBean" name="configuredSystemList" type="com.premiersolutionshi.old.bean.SystemBean">
				<logic:notEmpty name="resultBean" property="shipName">
					<tr style="vertical-align:top;" align="left">
						<td><b><bean:write name="resultBean" property="shipName"/></b></td>
						<td><bean:write name="resultBean" property="homeport"/></td>
						<td><b><bean:write name="resultBean" property="computerName"/></b></td>
						<td><bean:write name="resultBean" property="location"/></td>
						<td>
							<% if (!CommonMethods.isEmpty(resultBean.getDmsVersion()) && !resultBean.getDmsVersion().equals(currDmsVersion)) { %>
								<img src="images/icon_error.gif"/>
								DMS as of <bean:write name="resultBean" property="dmsVersion"/>
								<br/>
							<% } %>

							<% if (resultBean.getAtoMissingList() != null && resultBean.getAtoMissingList().size() > 0) { %>
								<img src="images/icon_error.gif"/>
								<bean:write name="resultBean" property="atoMissingListSize"/> Pending ATO Updates
								<br/>
							<% } %>

							<% if (!CommonMethods.isEmpty(resultBean.getFacetVersion()) && !resultBean.getFacetVersion().equals(currFacetVersion)) { %>
								<img src="images/icon_error.gif"/>
								FACET v<bean:write name="resultBean" property="facetVersion"/>
								<br/>
							<% } %>

							<logic:equal name="resultBean" property="rsupplyUpgradeInd" value="Y">
								<img src="images/icon_error.gif"/>
								RSupply Upgrade
								<br/>
							</logic:equal>

							<% if (resultBean.getInactivityList() != null && resultBean.getInactivityList().size() > 0) { %>
								<img src="images/icon_error.gif"/>
								DACS Inactivity
								<br/>
							<% } %>

							<% if (resultBean.getMissingTransmittalList() != null && resultBean.getMissingTransmittalList().size() > 0) { %>
								<img src="images/icon_error.gif"/>
								<bean:write name="resultBean" property="missingTransmittalListSize"/> missing transmittals
								<br/>
							<% } %>
						</td>

<% if (!currShipPk.equals(resultBean.getShipPk())) { %>
<input type="hidden" name="shipPkArr" value="<bean:write name="resultBean" property="shipPk"/>"/>
<html:hidden name="resultBean" property="shipName"/>
<html:hidden name="resultBean" property="primaryPocEmails"/>
<html:hidden name="resultBean" property="pocEmails"/>

<input type="hidden" name="monthlyEmailArr" value="This is a monthly reminder from the FACET Support Team regarding your FACET system. The following items are currently pending for your unit:

DEFECTIVE MATERIAL REFERENCE (DMS) FILE UPDATE:
The latest DMS file has been posted to LOGCOP-FACET. To update the file on your system, download the 'DefectiveMaterialReference.xls' file from the 'FACET System Updates' section of the File Repository link in LOGCOP-FACET and copy it into the 'FIARModule' folder on the C drive of the FACET laptop, overwriting the existing file. Please confirm when this has been done.

<% if (resultBean.getAtoMissingList() != null && resultBean.getAtoMissingList().size() > 0) { %>ATO UPDATES:
The following ATO Updates need to be applied:
<logic:iterate id="atoFilename" name="resultBean" property="atoMissingList" type="java.lang.String">ATOUpdates_<bean:write name="atoFilename"/>
</logic:iterate>Follow the procedures in each associated PDF file to update your system. Please confirm when this has been done.<% } %>

<% if (!CommonMethods.isEmpty(resultBean.getFacetVersion()) && !resultBean.getFacetVersion().equals(currFacetVersion)) { %>FACET SYSTEM UPDATE:
Your system needs to be updated to the latest version. You should have been notified about this previously and given instructions. Please confirm when this has been done.<% } %>

<logic:equal name="resultBean" property="rsupplyUpgradeInd" value="Y">RSUPPLY UPGRADE:
Your system needs to be updated to match your current RSUPPLY version. You should have been notified about this previously and given instructions. Please confirm when this has been done.</logic:equal>

<% if (resultBean.getInactivityList() != null && resultBean.getInactivityList().size() > 0) { %>UPLOAD ACTIVITY:
Your command has no record of activity within the required timeframe in LOGCOP-FACET for the following types of documents. Please upload the appropriate FACET files ASAP:
<logic:iterate id="inactivityStr" name="resultBean" property="inactivityList" type="java.lang.String">--<bean:write name="inactivityStr"/>
</logic:iterate><% } %>

<% if (resultBean.getMissingTransmittalList() != null && resultBean.getMissingTransmittalList().size() > 0) { %>MISSING TRANSMITTALS:
The following export transmittals (ZIP files) have been generated on your system but have not been uploaded to the LOGCOP-FACET website. Please upload the associated ZIP files ASAP to ensure your system is backed up and files are accessible ashore:
<logic:iterate id="transmittalNum" name="resultBean" property="missingTransmittalList" type="java.lang.String">--<bean:write name="transmittalNum"/>
</logic:iterate><% } %>

If you have any questions, please contact FACET Support at support@premiersolutionshi.com or 808-396-4444

V/R,

FACET Support Team
T: (808) 396-4444
E: support@ premiersolutionshi.com"/>
						<td align="center"><button class="monthlyEmailLink">E-Mail</button></td>
						<td align="center"><button class="standardEmailLink">E-Mail</button></td>
						<td align="center"><input type="checkbox" name="includeShipPkArr" value="<bean:write name="resultBean" property="shipPk"/>"/></td>
<% } else { %>
						<td></td>
						<td></td>
						<td></td>
<% } %>

					</tr>
					<% currShipPk = resultBean.getShipPk(); %>
				</logic:notEmpty>
				</logic:iterate>
				</tbody>
				</table>
			</td></tr> <!-- end tan_table -->

		</tbody>
		</table>
		</center>
		</p>

		<p align="center">
		<center>

		</center>
		</p>

		<p align="center">
		<html:submit value="Submit"/>
		</p>
		</html:form>
	</div></div>

	<%@ include file="../include/app-col2.jsp" %>
</div></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>

<script type="text/javascript">
	var monthNames = [ "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" ];

	$(function() {
		var table = $('#configuredSystemTable').DataTable( {
			paging: false,
			searching: false,
			columnDefs: [
				{ "orderable": false, "targets": [5,6,7] }
			],
			stateSave: false
		});

		var openedByList = [
			<logic:iterate id="openedBy" name="openedByList" type="java.lang.String" indexId="i">
				<% if (i > 0) { %>, <% } %>"<bean:write name="openedBy"/>"
			</logic:iterate>
		];

		$("#openedBy").autocomplete({
			source: openedByList
		});

		$(".datepicker").attr('autocomplete', 'off');
		$(".datepicker").datepicker();

		$('.monthlyEmailLink').on('click', function() {
			var emailBody = $(this).closest('tr').find('[name="monthlyEmailArr"]').attr('value').replace(/\r\r\r/g, '\r\r').replace(/\r\r\r/g, '\r\r').replace(/\r\r\r/g, '\r\r');
			var mailToLink = 'mailto:' + $(this).closest('tr').find('[name="primaryPocEmails"]').attr('value') + '?subject=FACET Monthly Notification for ' + $(this).closest('tr').find('[name="shipName"]').attr('value') + '&body=' + emailBody.replace('&', '%26').replace(' ', '%20').replace('+', '%2B').replace(/\n/g, '%0A').replace(/\r/g, '%0D');
			if (mailToLink.length < 2000) {
				window.location.href = mailToLink;
				$(this).closest('tr').find('[name="includeShipPkArr"]').attr('checked', true);
			} else {
				if (prompt('Warning: The e-mail exceeds the maximum length of a URL mailto link.  Please copy this email body to your clipboard and paste it in the opening e-mail draft.', emailBody)) {
					window.location.href = 'mailto:' + $(this).closest('tr').find('[name="primaryPocEmails"]').attr('value') + '?subject=FACET Monthly Notification for ' + $(this).closest('tr').find('[name="shipName"]').attr('value') + '&body=<insert body text>';
					$(this).closest('tr').find('[name="includeShipPkArr"]').attr('checked', true);
				} //end of if
			} //end of else
			return false;
		});

		$('.standardEmailLink').on('click', function() {
			window.location.href = 'mailto:' + $(this).closest('tr').find('[name="primaryPocEmails"]').attr('value') + '?subject=' + $('#title').val() + ' for ' + $(this).closest('tr').find('[name="shipName"]').attr('value') + '&body=' + $('#commentsArr').val().replace(/\n/g, '%0A').replace('&', '%26').replace(' ', '%20').replace('+', '%2B');
			$(this).closest('tr').find('[name="includeShipPkArr"]').attr('checked', true);
			return false;
		});

		$('#selectAllLink').on('click', function() {
			$('[name="includeShipPkArr"]').each(function(key, value) {
				$(this).attr('checked', true);
			});
		});

		$('#emailAllLink').on('click', function() {
			var allemail = '';
			$('[name="includeShipPkArr"]').each(function(key, value) {
				var email = $(this).closest('tr').find('[name="primaryPocEmails"]').attr('value');
				if (value.checked && email != '') {
					allemail += email + ';';
				} //end of if
			});

			if (allemail != '') {
				if (prompt('Copy all e-mail addresses to your clipboard and click OK to continue', allemail)) {
					window.location.href = 'mailto:?subject=' + $('#title').val() + '&body=' + $('#commentsArr').val().replace(/\n/g, '%0A').replace('&', '%26').replace(' ', '%20').replace('+', '%2B');
				} //end of if
			} else {
				alert('No e-mail addresses found');
			} //end of else
		});

		$('#category').on('change', function() {
			if ($(this).val() == 'Monthly E-Mail Notification') {
				table.column(5).visible(true);
				table.column(6).visible(false);
				$('#title').val('FACET Monthly Activity');
				$('.emailBody').hide();
			} else if ($(this).val() == 'Follow-Up Training') {
				table.column(5).visible(false);
				table.column(6).visible(true);
				$('#title').val('FACET Follow-Up Training');
				$('#commentsArr').val('ALCON,\n\nI will be in <location> from <date>-<date>. If you would like to schedule any follow-up training or support pertaining to the FACET system, including LS and CS training, please respond to reserve a timeslot (0900/1100/1300) along with the type of training you require.  Precedence will be given to those units that did not receive a support visit on a previous travel, after which it will be first-come, first-serve to those who respond to this e-mail.\n\nIf you\'re in-port and available during this timeframe, at a minimum, I will be stopping by to update your system and to upload any missing/outstanding Export Transmittal Files to DACS if time permits.  Thank you.\n\nV/r,\n\n<bean:write name="loginBean" property="fullName"/>\nFACET Support Team\nPremier Solutions HI\n808-396-4444');
				$('.emailBody').show();
			} else if ($(this).val() == 'FACET Update') {
				table.column(5).visible(false);
				table.column(6).visible(true);
				$('#title').val('FACET System Update Release (<bean:write name="currFacetVersion"/>)');
				$('#commentsArr').val('ALCON,\n\nThe latest FACET version has been released.  Please update your FACET system to <bean:write name="currFacetVersion"/> to ensure you have the latest features for the FACET System.\n\nYou will soon receive a separate e-mail notification from AMRDEC to download the update files.  Alternatively, the files are also available on the LOGCOP-FACET website on the black ribbon under the File Repository tab.  Please select the appropriate version based on your NCTSS RSupply Version (i.e. Viking/Patriot or CY04/Charger).\n\nPlease notify us when this is completed.  Feel free to contact us for any questions regarding this notice.\n\nV/r,\n\nFACET Support Team\nPremier Solutions HI\n808-396-4444');
				$('.emailBody').show();
			} //end of else-if
		}).change();

		$('#status').on('change', function() {
			if ($(this).val() == '3 - Resolved' || $(this).val() == '4 - Pending Possible Resolution') {
				$('.closedDateTd').hide();
				$('.autoCloseTd').show();
				$('#resolutionRow').show();
				if ($('#totalTime').val() == null || $('#totalTime').val() == '') $('#totalTime').val('15');
			} else if ($(this).val() == '5 - Closed' || $(this).val() == '6 - Closed (Successful)' || $(this).val() == '7 - Closed (No Response)' || $(this).val() == '8 - Closed (Unavailable)') {
				$('.closedDateTd').show();
				$('.autoCloseTd').hide();
				$('#resolutionRow').show();
				if ($('#totalTime').val() == null || $('#totalTime').val() == '') $('#totalTime').val('15');
				var d = new Date();
				if ($('#closedDate').val() == null || $('#closedDate').val() == '') $('#closedDate').val((d.getMonth()+1) + '/' + d.getDate() + '/' + d.getFullYear());
			} else { //Regular menu item
				$('.closedDateTd').hide();
				$('.autoCloseTd').show();
				$('#resolution').val(null);
				$('#totalTime').val(null);
				$('#resolutionRow').hide();
			} //end of else
		}).change();
	});

	$(document).ready(function () {
		$('#openedBy').val('<bean:write name="loginBean" property="fullName"/>');
		var d = new Date();
		$('#openedDate').val((d.getMonth()+1) + '/' + d.getDate() + '/' + d.getFullYear());
	});

</script>

</body>
</html>
