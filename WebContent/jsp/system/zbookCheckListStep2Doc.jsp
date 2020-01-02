<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<bean:define id="defaultPageTitle" value="TRAINER LAPTOP CHECKLIST – STEP 002"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="resultBean" scope="request" class="com.premiersolutionshi.old.bean.SystemBean"/>

<%
	response.setHeader("Pragma", "public");
	response.setHeader("Cache-Control", "max-age=0");
	response.setContentType("application/vnd.ms-word");
	response.setHeader("Content-Disposition", "attachment;filename=trainer_laptop_checklist_step002_" + resultBean.getComputerName() + ".doc");
%>

<html>
<head>
<meta http-equiv=Content-Type content="text/html; charset=windows-1252">
<meta name=ProgId content=Word.Document>
<meta name=Generator content="Microsoft Word 14">
<meta name=Originator content="Microsoft Word 14">
<style>
<!--
	body {
		font-family: Arial;
		font-size: 11pt;
	}


	#docTable {
		width: 725px;
		border-collapse: collapse;
		word-wrap: break-word;
		}

	#docTable td {
		border-style: solid solid solid solid;
		border-width: 1px;
		border-color: black;
		padding: 2px 2px 2px 3px;
		}

	#docTable td.numRow {
		border-style: solid solid solid solid;
		border-width: 1px;
		border-color: black;
		font-weight: bold;
		text-align: center;
		vertical-align: top;
		}

	#docTable .header {
		font-size: 14pt;
		font-weight: bold;
		background: black;
		color: white;
		text-align: center;
	}

	#docTable .subHeader {
		background-color: #d9d9d9;
		font-weight: bold;
		text-align: center;
		}

	#subTable td {
		border: 0;
		}

	#dashedTable td {
		border-style: none dashed dashed none;
		border-width: 1px;
		border-color: black;
		padding: 2px 3px;
		}

	ul {
		padding-top: 0;
		padding-bottom: 0;
		margin-top: 0;
		margin-bottom: 0;
		margin-left: 25px;
	}

	ul li {
		mso-margin-top-alt: 3px;
		mso-margin-bottom-alt: 3px;
		padding: 3px;
	}

	@page WordSection1
		{size:8.5in 11.0in;
		margin:.5in .5in .5in .5in;
		mso-header-margin:.5in;
		mso-footer-margin:.5in;
		mso-footer: f1;
		mso-paper-source:0;}

div.WordSection1
	{page:WordSection1;}
-->
</style>
<!--[if gte mso 10]>
<style>
 /* Style Definitions */
 table.MsoNormalTable
	{mso-style-name:"Table Normal";
	mso-tstyle-rowband-size:0;
	mso-tstyle-colband-size:0;
	mso-style-noshow:yes;
	mso-style-priority:99;
	mso-style-parent:"";
	mso-padding-alt:0in 5.4pt 0in 5.4pt;
	mso-para-margin-top:0in;
	mso-para-margin-right:0in;
	mso-para-margin-bottom:10.0pt;
	mso-para-margin-left:0in;
	line-height:115%;
	mso-pagination:widow-orphan;
	font-size:11.0pt;
	font-family:"Arial","sans-serif";
	mso-ascii-font-family:Arial;
	mso-ascii-theme-font:minor-latin;
	mso-hansi-font-family:Arial;
	mso-hansi-theme-font:minor-latin;}
</style>
<![endif]--><!--[if gte mso 9]><xml>
 <o:shapedefaults v:ext="edit" spidmax="1026"/>
</xml><![endif]--><!--[if gte mso 9]><xml>
 <o:shapelayout v:ext="edit">
  <o:idmap v:ext="edit" data="1"/>
 </o:shapelayout></xml><![endif]-->
</head>

<body lang=EN-US style='tab-interval:.5in'>
<div class=WordSection1>
<p align="center" style="font-weight:bold;font-size:16pt;">
<u><bean:write name="pageTitle"/></u>
</p>


<p align="center" style="font-size:16pt;color:red;font-weight:bold;">
*** DO NOT GIVE THIS LIST TO THE SHIP! ***
</p>


<p align="center">
<center>
<table id="docTable" cellspacing="0" width="720">
<colgroup><col width="220"/><col width="200"/><col width="180"/><col width="120"/></colgroup>
<tbody>
	<tr>
		<td width="220" style="font-weight:bold;">Vessel Name</td>
		<td width="500" colspan="3"><bean:write name="resultBean" property="shipName"/></td>
	</tr>

	<tr>
		<td style="font-weight:bold;">Station Type</td>
		<td colspan="3">
			<table id="subTable">
			<tbody>
			<tr>
				<td width="140">
					<logic:equal name="resultBean" property="multiShipInd" value="Y">
						&#x2713;
					</logic:equal>
					<logic:notEqual name="resultBean" property="multiShipInd" value="Y">
						<span style="font-size:17pt;">&#9633;</span>
					</logic:notEqual>
					Multi-Activity
				</td>
				<td>
					<logic:equal name="resultBean" property="nwcfInd" value="Y">
						&#x2713;
					</logic:equal>
					<logic:notEqual name="resultBean" property="nwcfInd" value="Y">
						<span style="font-size:17pt;">&#9633;</span>
					</logic:notEqual>
					Working Capital Fund
				</td>
			</tr>

			<tr>
				<td width="140">
					<logic:equal name="resultBean" property="form1348NoLocationInd" value="Y">
						&#x2713;
					</logic:equal>
					<logic:notEqual name="resultBean" property="form1348NoLocationInd" value="Y">
						<span style="font-size:17pt;">&#9633;</span>
					</logic:notEqual>
					1348 No Location
				</td>
				<td>
					<logic:equal name="resultBean" property="form1348NoClassInd" value="Y">
						&#x2713;
					</logic:equal>
					<logic:notEqual name="resultBean" property="form1348NoClassInd" value="Y">
						<span style="font-size:17pt;">&#9633;</span>
					</logic:notEqual>
					1348 No Class SL HAZMAT
				</td>
			</tr>
			</tbody>
			</table>
		</td>
	</tr>

	<tr>
		<td width="220" style="font-weight:bold;border-bottom:3px solid black;">RSupply Version</td>
		<td width="500" colspan="3" style="border-bottom:3px solid black;">
			<table id="subTable">
			<tbody>
				<tr>
					<td width="250">
						<% if (resultBean.getRsupply().equals("Viking") || resultBean.getRsupply().equals("Patriot")) { %>
							&#x2713;
						<% } else { %>
							<span style="font-size:17pt;">&#9633;</span>
						<% } %>
						Viking/Patriot
					</td>
					<td>
						<% if (resultBean.getRsupply().equals("CY04") || resultBean.getRsupply().equals("Charger")) { %>
							&#x2713;
						<% } else { %>
							<span style="font-size:17pt;">&#9633;</span>
						<% } %>
						CY04/Charger
					</td>
				</tr>
			</tbody>
			</table>
		</td>
	</tr>
	<tr>
		<td width="220" style="font-weight:bold;border-bottom:3px solid black;">Ghost Version</td>
		<td width="500" style="border-bottom:3px solid black;" colspan="3"><bean:write name="resultBean" property="ghostVersion"/></td>
	</tr>
	<tr>
		<td width="220" style="font-weight:bold;">Computer Serial #</td>
		<td width="380" colspan="2"><bean:write name="resultBean" property="laptopSerialNumber"/></td>
		<td width="120"><span style="font-size:17pt;">&#9633;</span> Entered in DB</td>
	</tr>
	<tr>
		<td width="220" style="font-weight:bold;">Computer Name and Tag</td>
		<td width="200"><bean:write name="resultBean" property="computerName"/></td>
		<td width="180"><b>Tag #</b> <bean:write name="resultBean" property="laptopTag"/></td>
		<td width="120"><span style="font-size:17pt;">&#9633;</span> Entered in DB</td>
	</tr>
	<tr>
		<td width="220" style="font-weight:bold;">Scanner Serial # and Tag</td>
		<td width="200"><bean:write name="resultBean" property="scannerSerialNumber"/></td>
		<td width="180"><b>Tag #</b> <bean:write name="resultBean" property="scannerTag"/></td>
		<td width="120"><span style="font-size:17pt;">&#9633;</span> Entered in DB</td>
	</tr>
	<tr>
		<td width="220" style="font-weight:bold;">FACET Database Version</td>
		<td width="380" colspan="2"><bean:write name="resultBean" property="facetVersion"/></td>
		<td width="120"><span style="font-size:17pt;">&#9633;</span> Entered in DB</td>
	</tr>
</tbody>
</table>
</center>
</p>

<br/>

<p align="center">
<center>
<table id="docTable" cellspacing="0" width="725">
<thead>
	<tr>
		<td width="30" style="font-weight:bold;" align="center">#</td>
		<td width="625" style="font-weight:bold;">TASK DESCRIPTION</td>
		<td width="70" style="font-weight:bold;" align="center">Initials</td>
	</tr>
</thead>
<tbody>
	<tr>
		<td width="30" class="numRow"></td>
		<td width="625">
			<b>At least one week before installation date, distribute pre-install notifications:</b>
			<ul>
				<li>Notify FLC to start sending the StoresWeb files to the appropriate ship contacts</li>
				<li>Notify LogCop Team of computer name and pending registration</li>
				<li>
					Notify ship of:
					<ul>
						<li>Install/Training date/time</li>
						<li>Tell to apply for LogCop access immediately (SUPPO, FSO, LS, CS, S1 LPO, S2 LPO, Primary and Backup Records Keepers, and any others that will be using the system) and let us know who applied and then we have to tell LT Baugh/Anthony to approve requests</li>
						<li>hold (don’t post/complete) all open receipts received one day before install date so they can be processed during install/training</li>
					</ul>
				</li>
			</ul>
		</td>
		<td width="70">&nbsp;</td>
	</tr>

	<tr>
		<td width="30" class="numRow">1</td>
		<td width="625">
			<b>Login with Premier Solutions HI login</b> (user name “<b>pshi808</b>”, password is “<b>Hawaiitech2013!</b>” without quotes)
		</td>
		<td width="70">&nbsp;</td>
	</tr>

	<tr>
		<td width="30" class="numRow">2</td>
		<td width="625">
			<b>Apply All Recent ATO Updates as needed</b>
		</td>
		<td width="70">&nbsp;</td>
	</tr>

	<tr>
		<td width="30" class="numRow">3</td>
		<td width="625">
<b>Check FACET version and update if needed:</b><br>
Run the latest FACET system update EXE <b>for the appropriate configuration</b>.
<span style="font-color:red;font-weight:bold;">Be sure to follow the update procedures exactly as documented.</span>
		</td>
		<td width="70">&nbsp;</td>
	</tr>

	<tr>
		<td width="30" class="numRow">4</td>
		<td width="625">
<b>Verify Computer Name:</b><br>
Double click on “FACET” icon on Desktop to open database &gt; validate “Computer Name” value shown on Navigation form (click on it to change if needed – password is “Syst3mCh@nge” without quotes)
		</td>
		<td width="70">&nbsp;</td>
	</tr>

	<tr>
		<td width="30" class="numRow">5</td>
		<td width="625">
<b>Create Users</b>
<ul>
<li>ONLY If you have a list of users that need accounts created:</li>
<li>Create logins: Control Panel &gt; “Add or Remove User Accounts” &gt; “Create a new account” &gt; </li>
<li>User name = convention of “&lt;Rank&gt; &lt;Lastname&gt;”, eg. “LS3 Rodriguez”</li>
<li>IF user should have administrator rights, change radio button to “Administrator”. Otherwise, leave as “Standard user”. <b>AT LEAST ONE USER SHOULD BE GIVEN ADMINISTRATOR RIGHTS</b></li>
<li>Click “Create Account” button </li>
<li>Confirm existing “Admin” login or “facetadmin” login is set as an administrator and that you can login with that account using the password specified in the intranet’s Configured System record</li>
<li>Confirm no accounts are locked out by doing the following: Start button &gt; right click on Computer and select <b>Manage</b> &gt; <b>Local Users and Groups</b> &gt; <b>Users</b> &gt; right click on each user and select Properties &gt; <b>General</b> tab &gt; ensure “Account is locked out” is not checked</li>
</ul>
		</td>
		<td width="70">&nbsp;</td>
	</tr>

	<tr>
		<td width="30" class="numRow">6</td>
		<td width="625">
<b>Update locations & defective material list if needed</b>
<ul>
<li>LOCATIONS: If locations list needs to be updated and the locations list has been received: Copy ship’s RSupply <b>unique</b> locations to “C:\FIARModule\Locations.txt” (if there are existing items in this file, they should be deleted first). If multi-activity system, include UIC value in second column (refer to FACET System Update procedures for how to do this).</li>
<li>DEFECTIVE MATERIAL LIST: If “C:\FIARModule\DefectiveMaterialReference.xls” file’s last modified date is not the current month, replace with latest from “P:\Projects\0 - FACET Support\Defective materials listing” </li>

		</td>
		<td width="70">&nbsp;</td>
	</tr>

	<tr>
		<td width="30" class="numRow">7</td>
		<td width="625">
<b>Validate database is locked:</b> Go to desktop and click ONCE on “FACET” icon on Desktop to select it &gt; hold down Shift key on keyboard and press Enter on keyboard (<b>keep holding down Shift key until database opens</b>). Database should open and Navigation screen should open automatically and Navigation Pane on left should not be visible. If Navigation screen doesn’t open automatically and Navigation Pane on left is visible, close the database and then open it by double clicking on desktop icon (don’t hold Shift key), close database, and then repeat previous step for opening with Shift key held and verify it opens normally (not in design mode)
		</td>
		<td width="70">&nbsp;</td>
	</tr>

	<tr>
		<td width="30" class="numRow">8</td>
		<td width="625">
<b>Prep for Training:</b> Create a copy of FIARMODULE items: In “C:\FIARModule”, make a copy in place of “Navy.mdb”, “Locations.txt”, “Output” and “KofaxDocs” folder. " - Copy" should be automatically added to the names of the copies (eg. “Navy.mdb - Copy”, “Locations.txt - Copy”, “Output - Copy” and “KofaxDocs - Copy” folder)
		</td>
		<td width="70">&nbsp;</td>
	</tr>

	<tr>
		<td width="30" class="numRow">9</td>
		<td width="625">
<b>Food StoresWeb Excel Files:</b> Copy over StoresWeb Excel files to laptop if any received
		</td>
		<td width="70">&nbsp;</td>
	</tr>

	<tr>
		<td width="30" class="numRow">10</td>
		<td width="625">
<b>Verify shortcut to FACET database points to “C:\FIARModule\Navy.mdb” in Public Desktop:</b> Right click on FACET shortcut on desktop and verify Target in Shortcut tab is “C:\FIARModule\Navy.mdb” and Location in General tab is “C:\Users\Public\Desktop”. If not, recreate shortcut: Go to “C:\FIARModule” &gt; right click on “Navy.mdb” and select “Create Shortcut” &gt; rename new shortcut to “FACET” &gt; cut the shortcut &gt; go to “C:\Users\Public\Desktop” and paste the shortcut &gt; go to desktop and verify the FACET shortcut settings again
		</td>
		<td width="70">&nbsp;</td>
	</tr>

	<tr>
		<td width="30" class="numRow">11</td>
		<td width="625">
<b>Set date/time zone appropriately:</b> click on time/date in lower right of taskbar (system tray) &gt; click “Change date and time settings…” &gt; change as needed
		</td>
		<td width="70">&nbsp;</td>
	</tr>

	<tr>
		<td width="30" class="numRow">12</td>
		<td width="625">
<b>If Backfile exists:</b> Apply the backfile per the procedures
		</td>
		<td width="70">&nbsp;</td>
	</tr>

</tbody>
</table>
</center>
</p>
</div>

<div style='mso-element:footer' id=f1>
<p class=MsoFooter style="text-align:center;">
	<table border="0">
	<tr>
	<td style="width:125px;font-size:8pt;vertical-align:bottom;">
		Page <!--[if supportFields]><b><span style='mso-element:
			field-begin'></span><span style='mso-spacerun:yes'> </span>PAGE <span
			style='mso-element:field-separator'></span></b><![endif]--><b><span
			style='mso-no-proof:yes'>3</span></b><!--[if supportFields]><b><span
			style='mso-element:field-end'></span></b><![endif]--> of <!--[if supportFields]><b><span
			style='mso-element:field-begin'></span><span
			style='mso-spacerun:yes'> </span>NUMPAGES<span style='mso-spacerun:yes'>
			</span><span style='mso-element:field-separator'></span></b><![endif]--><b><span
			style='mso-no-proof:yes'>3</span></b><!--[if supportFields]><b><span
			style='mso-element:field-end'></span></b><![endif]--><w:sdtPr></w:sdtPr>
	</td>
	<td style="width:500px;font-size:8pt;text-align:center;vertical-align:bottom;">
		<bean:write name="pageTitle"/>
	</td>
	<td style="width:125px;font-size:8pt;text-align:right;vertical-align:bottom;">Updated 2018-03-12</td>
	</tr>
	</table>
</div>
</body>
</html>
