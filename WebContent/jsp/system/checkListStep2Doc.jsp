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
	response.setHeader("Content-Disposition", "attachment;filename=trainer_laptop_checklist_step002_" + resultBean.getComputerName() + "_HP_eBook.doc");
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
<table id="docTable" cellspacing="0" width="725">
<colgroup><col width="225"/><col width="200"/><col width="180"/><col width="120"/></colgroup>
<tbody>
	<tr>
		<td width="220" style="font-weight:bold;">Vessel Name</td>
		<td width="500" colspan="3"><bean:write name="resultBean" property="shipName"/></td>
	</tr>
	<tr>
		<td width="220" style="font-weight:bold;">Station Type</td>
		<td width="500" colspan="3">
			<table id="subTable">
			<tbody>
			<tr>
				<td width="250"><span style="font-size:17pt;">&#9633;</span> Day Forward Only</td>
				<td><span style="font-size:17pt;">&#9633;</span> Backfile Only</td>
			</tr>
			<tr>
				<td width="250"><span style="font-size:17pt;">&#9633;</span> Day Forward & Backfile</td>
				<td><span style="font-size:17pt;">&#9633;</span> Multi Ship</td>
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
		<td width="380" style="border-bottom:3px solid black;" colspan="2"><bean:write name="resultBean" property="ghostVersion"/></td>
		<td width="120" style="border-bottom:3px solid black;">&nbsp;</td>
	</tr>
	<tr>
		<td width="220" style="font-weight:bold;">Computer Serial #</td>
		<td width="380" colspan="2"><bean:write name="resultBean" property="laptopSerialNumber"/></td>
		<td width="120"><span style="font-size:17pt;">&#9633;</span> Entered in DB</td>
	</tr>
	<tr>
		<td width="220" style="font-weight:bold;">Computer Name</td>
		<td width="200"><bean:write name="resultBean" property="computerName"/></td>
		<td width="180"><b>Tag #</b> <bean:write name="resultBean" property="laptopTag"/></td>
		<td width="120"><span style="font-size:17pt;">&#9633;</span> Entered in DB</td>
	</tr>
	<tr>
		<td width="220" style="font-weight:bold;">Scanner Serial #</td>
		<td width="200"><bean:write name="resultBean" property="scannerSerialNumber"/></td>
		<td width="180"><b>Tag #</b> <bean:write name="resultBean" property="scannerTag"/></td>
		<td width="120"><span style="font-size:17pt;">&#9633;</span> Entered in DB</td>
	</tr>
	<tr>
		<td width="220" style="font-weight:bold;">FACET Version</td>
		<td width="380" colspan="2"><bean:write name="resultBean" property="facetVersion"/></td>
		<td width="120"><span style="font-size:17pt;">&#9633;</span> Entered in DB</td>
	</tr>
	<tr>
		<td width="220" style="font-weight:bold;">Kofax Batch Classes Version</td>
		<td width="380" colspan="2"><bean:write name="resultBean" property="kofaxVersion"/></td>
		<td width="120"><span style="font-size:17pt;">&#9633;</span> Entered in DB</td>
	</tr>
	<tr>
		<td width="220" style="font-weight:bold;">Locations Updated</td>
		<td width="500" colspan="3"><span style="font-size:17pt;">&#9633;</span></td>
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
		<td width="30" class="numRow">1</td>
		<td width="625">
			<b>At least one week before installation date, distribute pre-install notifications:</b><br/>
			<ul>
			<li>Notify FLC to start sending the StoresWeb files to the appropriate ship contacts</li>
			<li>Notify LogCop Team of computer name and pending registration</li>
			<li>Notify ship of:
				<ul>
					<li>Install/Training date/time</li>
					<li>Tell to apply for LogCop access immediately (SUPPO, FSO, LS, CS, S1 LPO, S2 LPO, Primary and Backup Records Keepers, and any others that will be using the system) and let us know who applied and then we have to tell LT Baugh/Anthony to approve requests</li>
					<li>hold (don’t post/complete) all open receipts received one day before install date so they can be processed during install/training</li>
				</ul>
			</li>
			</ul>
		</td>
		<td width="70" class="numRow"><i>N/A</i></td>
	</tr>

	<tr>
		<td width="30" class="numRow">2</td>
		<td width="625">
			<b>Login with Premier Solutions HI login</b> (password is “<b>Hawaiitech2013!</b>” without quotes)
		</td>
		<td width="70">&nbsp;</td>
	</tr>

	<tr>
		<td width="30" class="numRow">3</td>
		<td width="625">
			<b>Check version of FACET database and Kofax Batch Classes and acquire appropriate files and update if needed:</b><br/>
			<ul>
				<li>If FACET database needs to be updated: Delete existing “Navy.mdb” file and “Navy &lt;version #&gt;.mdb” file at “C:\FIARModule” and copy new “Navy.mdb” file and “Navy &lt;version #&gt;.mdb” file to “C:\FIARModule”</li>
				<li>IF Kofax Batch Classes need to be updated: Open Kofax Capture 10.0 “Administration” program (Start > Kofax Capture 10 > Administration) > Click “Import” on the Home tab > Browse to appropriate version folder on external hard drive > Select the CAB file > Click “OK” to unpacking screen > Click “Add All” button > Click “Import” button > Click “OK” to confirmation > Click “Publish” in Home tab > Click “Select All” > Click “Publish” > After publishing completes, look for checkmarks next to each batch class in the left panel and verify that Results log says that publishing was successful and 0 warnings; 0 errors <span style="color:red;font-weight:bold;">for every Batch Class</span> > click Close > close Administration program</li>
				<li>Verify that the User Guide version is correct and most current version.</li>
			</ul>
		</td>
		<td width="70" class="numRow"><i>N/A</i></td>
	</tr>

	<tr>
		<td width="30" class="numRow">4</td>
		<td width="625">
			<b>Create Users</b><br/>
			<ul>
				<li>ONLY If you have a list of Ship users:</li>
				<ul>
					<li>
						Create logins: Control Panel > “Add or Remove User Accounts” > “Create a new account” >
						<ul>
							<li>User name = convention of “&lt;Rank&gt; &lt;Lastname&gt;”, eg. “LS3 Rodriguez”</li>
							<li>IF user should have administrator rights, change radio button to “Administrator”. Otherwise, leave as “Standard user”</li>
							<li>Click “Create Account” button </li>
						</ul>
					</li>
					<li><b>BE SURE ANOTHER ADMIN ACCOUNT HAS BEEN CREATED</b> and then delete “Admin” user login</li>
				</ul>
				<li>IF no list of users, confirm existing “Admin” login is set as an administrator</li>
			</ul>
		</td>
		<td width="70" class="numRow"><i>N/A</i></td>
	</tr>

	<tr>
		<td width="30" class="numRow">5</td>
		<td width="625">
			<b>Update locations & defective material list if needed</b><br/>
			<ul>
				<li>LOCATIONS: If locations list needs to be updated and the locations list has been received: Copy ship’s RSupply <b>unique</b> locations to “C:\FIARModule\Locations.txt” (if there are existing items in this file, they should be deleted first). If multiple ship system, include UIC value in second column</li>
				<li>DEFECTIVE MATERIAL LIST: If “C:\FIARModule\DefectiveMaterialReference.xls” file’s last modified date is not the current month: Get latest Word file from (<a href="https://www.navsup.navy.mil/navsupwss-extranet/dms">https://www.navsup.navy.mil/navsupwss-extranet/dms</a>) > delete contents of “DefectiveMaterialReference.xls” file > copy all contents in Word file and paste into column A (select only the first cell A1) of  “DefectiveMaterialReference.xls” file > save and close</li>
			</ul>
		</td>
		<td width="70" class="numRow"><i>N/A</i></td>
	</tr>

	<tr>
		<td width="30" class="numRow">6</td>
		<td width="625">
			<b>Update Settings that correspond to RSupply system and site for Kofax 1348 Batch Classes</b><br/>
			<ul>
				<li><b>IF CY04/Charger RSupply system</b>, set the “MultipleLocations” <b>Batch field</b> “Default” value to “No”: Kofax Administration program > right click on “Material Rcpts - DD1348-1A” in “Batch” tab in “Definitions” section > Properties > Click on “Yes” for “MultipleLocations” field’s “Default” value and change to “No” > click “OK”. Repeat for “Material Rcpts - DD1348-1A Dummy” Batch Class.  Add X31 settings once in production</li>
				<li>
					<b>IF multiple ship system:</b>
					<ul>
						<li>Set the “UIC” <b>Batch field</b> “Required” value to “True”: Kofax Administration program > right click on “Material Rcpts - DD1348-1A” in “Batch” tab in “Definitions” section > Properties > Click on “False” for “UIC” field’s “Required” value and change to “True” > click “OK”. <i>(Not available for “Material Rcpts - DD1348-1A Dummy” Batch Class)</i></li>
						<li>Create UIC dropdown list: Kofax Administration program > “Field Types” tab in “Definitions” section > right click on “Navy-UICList” field > Properties > “Values” tab > edit values appropriately and verify “Force match” checkbox (uncheck if you don’t want to limit the values to those listed. If keeping checked, ensure a blank record exists so user won’t be given a default value, unless a default value is desired) > click “OK” when done</li>
					</ul>
				</li>
				<li><b>IF Backfile scan mode is needed</b>, update “ScanMode” <b>Batch field</b>: Kofax Administration program > right click on “Material Rcpts - DD1348-1A” in “Batch” tab in “Definitions” section > Properties > Click on “True” for “ScanMode” field’s “Hidden” value and change to “False” > Click on “False” for “ScanMode” field’s “Required” value and change to “True” > set “ScanMode” field’s “Default” value appropriately (blank is okay – this will make the user have to pick a value) > QUESTION: IF day forward is not needed, should we hide Circle Confirmation, Received By, and Date Received fields? > click “OK”. <i>(Not available for “Material Rcpts - DD1348-1A Dummy” Batch Class)</i></li>
				<li><b>If any changes above have been made</b>, publish the Batch Class: Click “Publish” in Home tab > select “Material Rcpts - DD1348-1A” Batch Class > click “Publish” > verify that Results log says that publishing was successful and 0 warnings; 0 errors. Repeat for “Material Rcpts - DD1348-1A Dummy” Batch Class and X31 once in production. > click Close > close Administration program</li>
			</ul>
		</td>
		<td width="70" class="numRow"><i>N/A</i></td>
	</tr>

	<tr>
		<td width="30" class="numRow">7</td>
		<td width="625">
			<b>Set FACET database System Type</b><br/>
			Double click on “FACET” icon on Desktop to open database > double click on logo in Navigation screen to open “DisableBypassKey” screen > enter password crab.napkin<br/>
			<ul>
				<li>Set for appropriate RSupply system by clicking on correct System Switch button (Viking/Patriot OR CY04/Charger) > click OK in Done message > confirm change by opening Material screen and validating only one location field for CY04/Charger or multiple location fields for Viking/Patriot</li>
				<li>Set for single or multiple ship system by clicking on correct System Switch button > click OK in Done message > validate by opening New Locations screen and confirming UIC shown for multi ship system or not shown for single ship system</li>
				<li>Close database</li>
			</ul>
		</td>
		<td width="70" class="numRow"><i>N/A</i></td>
	</tr>

	<tr>
		<td width="30" class="numRow">8</td>
		<td width="625">
			<b>Validate database is locked:</b>
			Go to desktop and click ONCE on “FACET” icon on Desktop to select it > hold down Shift key on keyboard and press Enter on keyboard (<b>keep holding down Shift key until database opens</b>). Database should open and Navigation screen should open automatically and Navigation Pane on left should not be visible. If Navigation screen doesn’t open automatically and Navigation Pane on left is visible, close the database and then open it by double clicking on desktop icon (don’t hold Shift key), close database, and then repeat previous step for opening with Shift key held and verify it opens normally (not in design mode)
		</td>
		<td width="70">&nbsp;</td>
	</tr>

	<tr>
		<td width="30" class="numRow">9</td>
		<td width="625">
			<b>Prep for Training:</b>
			Create a copy of FIARMODULE items: In “C:\FIARModule”, make a copy in place of “Navy.mdb”, “Locations.txt”, “Output” and “KofaxDocs” folder. " - Copy" should be automatically added to the names of the copies (eg. “Navy.mdb - Copy”, “Locations.txt - Copy”, “Output - Copy” and “KofaxDocs - Copy” folder)
		</td>
		<td width="70">&nbsp;</td>
	</tr>

	<tr>
		<td width="30" class="numRow">10</td>
		<td width="625">
			<b>Food StoresWeb Excel Files:</b>
			Copy over StoresWeb Excel files to laptop if any received<br/>
		</td>
		<td width="70">&nbsp;</td>
	</tr>

	<tr>
		<td width="30" class="numRow">11</td>
		<td width="625">
			<b>Verify shortcut to FACET database points to “C:\FIARModule\Navy.mdb” in Public Desktop:</b>
			Right click on FACET shortcut on desktop and verify Target in Shortcut tab is “C:\FIARModule\Navy.mdb” and Location in General tab is “C:\Users\Public\Desktop”. If not, recreate shortcut: Go to “C:\FIARModule” > right click on “Navy.mdb” and select “Create Shortcut” > rename new shortcut to “FACET” > cut the shortcut > go to “C:\Users\Public\Desktop” and paste the shortcut > go to desktop and verify the FACET shortcut settings again
		</td>
		<td width="70">&nbsp;</td>
	</tr>

	<tr>
		<td width="30" class="numRow">12</td>
		<td width="625">
			<b>Set time/time zone appropriately:</b>
			click on time/date in lower right of taskbar (system tray) > click “Change date and time settings…” > change as needed
		</td>
		<td width="70">&nbsp;</td>
	</tr>
</tbody>
</table>
</center>
</p>
</div>
</p>
</div>

<div style='mso-element:footer' id=f1>
<p class=MsoFooter style="text-align:center;">
	<table border="0">
	<tr>
	<td style="width:100px;font-size:8pt;vertical-align:bottom;">
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
	<td style="width:550px;font-size:8pt;text-align:center;vertical-align:bottom;">
		<bean:write name="pageTitle"/>
	</td>
	<td style="width:100px;font-size:8pt;text-align:right;vertical-align:bottom;">v1.0</td>
	</tr>
	</table>
</div>
</body>
</html>
