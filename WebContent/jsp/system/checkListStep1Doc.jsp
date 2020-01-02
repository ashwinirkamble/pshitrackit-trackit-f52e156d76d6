<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<bean:define id="defaultPageTitle" value="FINAL HP EliteBook and zBook LAPTOP PREP CHECKLIST – STEP 001"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="resultBean" scope="request" class="com.premiersolutionshi.old.bean.SystemBean"/>

<%
	response.setHeader("Pragma", "public");
	response.setHeader("Cache-Control", "max-age=0");
	response.setContentType("application/vnd.ms-word");
	response.setHeader("Content-Disposition", "attachment;filename=final_laptop_prep_checklist_step001_" + resultBean.getComputerName() + ".doc");
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
		width: 680px;
		border-collapse: collapse;
		word-wrap: break-word;
		}

	#docTable td {
		border: 1px solid black;
		padding: 3px;
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

	ul, ol {
		padding-top: 0;
		padding-bottom: 0;
		margin-top: 0;
		margin-bottom: 0;
	}

	ul li, ol li {
		mso-margin-top-alt: 0;
		mso-margin-bottom-alt: 0;
	}

	.numRow {
		font-weight: bold;
		text-align: center;
		vertical-align: top;
		}

	@page WordSection1
		{size:8.5in 11.0in;
		margin:.5in .5in .5in .5in;
		mso-header-margin:.5in;
		mso-footer-margin:.5in;
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
<p align="center">
	<b><u><bean:write name="pageTitle"/></u></b>
</p>

<p align="center">
USE THE FOLLOWING STEPS TO ENSURE LAPTOP IS SET UP COMPLETELY.<br/>
** PLEASE MAKE SURE THIS CHECKLIST TRAVELS WITH LAPTOP **
</p>

<p align="center" style="font-size:16pt;color:red;font-weight:bold;">
*** DO NOT GIVE THIS LIST TO THE SHIP! ***
</p>

<p align="center">
<center>
<table id="docTable" cellspacing="0">
<colgroup><col width="230"/><col width="170"/><col width="50"/><col width="200"/></colgroup>
<tbody>
	<tr>
		<td width="230" style="font-weight:bold;">Vessel Name</td>
		<td width="420" colspan="3"><bean:write name="resultBean" property="shipName"/></td>
	</tr>
	<tr>
		<td width="230" style="font-weight:bold;">Vessel Type</td>
		<td width="170">
			<logic:notEqual name="resultBean" property="nwcfInd" value="Y">
				&#x2713;
			</logic:notEqual>
			<logic:equal name="resultBean" property="nwcfInd" value="Y">
				<span style="font-size:17pt;">&#9633;</span>
			</logic:equal>
			Standard
		</td>
		<td colspan="2" width="250">
			<logic:equal name="resultBean" property="nwcfInd" value="Y">
				&#x2713;
			</logic:equal>
			<logic:notEqual name="resultBean" property="nwcfInd" value="Y">
				<span style="font-size:17pt;">&#9633;</span>
			</logic:notEqual>
			LCS / ATG
		</td>
	</tr>

	<tr>
		<td style="font-weight:bold;">Station Type</td>
		<td colspan="3">
			<table id="subTable">
			<tbody>
			<tr>
				<td width="200"><span style="font-size:17pt;">&#9633;</span> Day Forward Only</td>
				<td><span style="font-size:17pt;">&#9633;</span> Backfile Only</td>
			</tr>
			<tr>
				<td width="200"><span style="font-size:17pt;">&#9633;</span> Day Forward & Backfile</td>
				<td>
					<logic:equal name="resultBean" property="multiShipInd" value="Y">
						&#x2713;
					</logic:equal>
					<logic:notEqual name="resultBean" property="multiShipInd" value="Y">
						<span style="font-size:17pt;">&#9633;</span>
					</logic:notEqual>
					Multi Ship
				</td>
			</tr>
			</tbody>
			</table>
		</td>
	</tr>
	<tr>
		<td style="font-weight:bold;">RSupply Version</td>
		<td colspan="3">
			<table id="subTable">
			<tbody>
				<tr>
					<td width="220">
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
		<td style="font-weight:bold;border-bottom:3px solid black;">Login/Users Created</td>
		<td colspan="3" style="font-weight:bold;border-bottom:3px solid black;"/>
	</tr>
	<tr>
		<td style="font-weight:bold;">Technician</td>
		<td colspan="3"/>
	</tr>
	<tr>
		<td style="font-weight:bold;">Date</td>
		<td colspan="3"/>
	</tr>
	<tr>
		<td style="font-weight:bold;border-bottom:3px solid black;">Ghost Version</td>
		<td colspan="3" style="border-bottom:3px solid black;"><bean:write name="resultBean" property="ghostVersion"/></td>
	</tr>
	<tr>
		<td style="font-weight:bold;">Computer Name</td>
		<td><bean:write name="resultBean" property="computerName"/></td>
		<td colspan="2"><b>Tag #</b> <bean:write name="resultBean" property="laptopTag"/></td>
	</tr>
	<tr>
		<td style="font-weight:bold;">Computer Serial #</td>
		<td colspan="3"><bean:write name="resultBean" property="laptopSerialNumber"/></td>
	</tr>

	<!--tr>
		<td style="font-weight:bold;">Scanner Serial #</td>
		<td><bean:write name="resultBean" property="scannerSerialNumber"/></td>
		<td colspan="2"><b>Tag #</b> <bean:write name="resultBean" property="scannerTag"/></td>
	</tr-->

	<!--tr>
		<td style="font-weight:bold;">Kofax Volume Info</td>
		<td colspan="2">
			<table id="subTable">
			<tbody>
			<tr>
				<td width="150"><span style="font-size:17pt;">&#9633;</span> 300k</td>
				<td><span style="font-size:17pt;">&#9633;</span> 60k</td>
			</tr>
			</tbody>
			</table>
		</td>
		<td width="130">&#9633; Entered in DB</td>
	</tr-->
	<!--tr>
		<td style="font-weight:bold;">Kofax/KTM Version</td>
		<td colspan="3">KC10.0.2, KTM 5.5 SP2, FP7</td>
	</tr-->


	<tr>
		<td style="font-weight:bold;">FACET Version</td>
		<td colspan="2"><bean:write name="resultBean" property="facetVersion"/></td>
		<td>&#9633; Entered in DB</td>
	</tr>

	<tr>
		<td style="font-weight:bold;">Kofax License/Product Code</td>
		<td colspan="2">
			<% if (!CommonMethods.isEmpty(resultBean.getKofaxLicenseKey())) { %>
			<bean:write name="resultBean" property="kofaxLicenseKey"/>
			|
			<bean:write name="resultBean" property="kofaxProductCode"/>
			<% } %>
		</td>
		<td>&#9633; Entered in DB</td>
	</tr>

	<tr>
		<td style="font-weight:bold;">Kofax Batch Class Version</td>
		<td colspan="2"><bean:write name="resultBean" property="kofaxVersion"/></td>
		<td>&#9633; Entered in DB</td>
	</tr>

	<tr>
		<td style="font-weight:bold;">VRS License</td>
		<td colspan="2">
			<% if (!CommonMethods.isEmpty(resultBean.getVrsLicensePk()) && resultBean.getVrsLicensePk().equals("0")) { %>
				N/A
			<% } else if (!CommonMethods.isEmpty(resultBean.getVrsLicenseKey())) { %>
				<bean:write name="resultBean" property="vrsLicenseKey"/>
				|
				<bean:write name="resultBean" property="vrsProductCode"/>
			<% } else { %>
				--
			<% } %>
		</td>
		<td>&#9633; Entered in DB</td>
	</tr>

	<tr>
		<td style="font-weight:bold;">Microsoft License Key</td>
		<td colspan="3"><bean:write name="resultBean" property="msOfficeLicenseKey"/></td>
	</tr>
	<tr>
		<td style="font-weight:bold;">Access Version</td>
		<td colspan="2"><bean:write name="resultBean" property="accessVersion"/></td>
		<td>&#9633; Entered in DB</td>
	</tr>
	<tr>
		<td style="font-weight:bold;">PSHI Admin Password</td>
		<td colspan="3"><% if (!CommonMethods.isEmpty(resultBean.getAdminPassword())) { %><bean:write name="resultBean" property="adminPassword"/><% } else { %>Hawaiitech2013!<% } %></td>
	</tr>
</tbody>
</table>
</center>
</p>

<p align="center" style="color:red;font-weight:bold;">
** Login with "pshi808" user account for all steps! **
</p>

<p align="center">
<center>
<table id="docTable" cellspacing="0" width="650">
<tbody>
	<tr>
		<td width="650" colspan="4" class="header">GENERAL LAPTOP PREP</td>
	</tr>
	<tr>
		<td width="40" style="font-weight:bold;" align="center">&#x2713;</td>
		<td width="30" style="font-weight:bold;" align="center">#</td>
		<td width="580" colspan="2" style="font-weight:bold;">TASK DESCRIPTION</td>
	</tr>
	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">1</td>
		<td width="580" colspan="2">
			<b>Label laptop and scanner</b><br/>
			<ul>
				<li>Apply "NOT FOR PERSONAL USE" label</li>
				<li>Apply laptop ID label with FACET name</li>
				<li>Apply "Unclassified" label</li>
			</ul>
		</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">2</td>
		<td width="580" colspan="2">
			<b>Confirm that the following hardware/software are available to prepare for ghost prepping:</b></br>
			<ul>
				<li>Laptop</li>
				<li>Clonezilla CD</li>
				<li><b>External hard drive</b> (verify with PM that ghost image is most current version)</li>
				<li>Kofax License</li>
				<li>Microsoft Office License</li>
				<li>Scanner</li>
				<li>Network cable access</li>
			</ul>
		</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">3</td>
		<td width="580" colspan="2">
			<b>Configure BIOS: (zBook only)</b><br/>
			<ul>
				<li>Power on laptop</li>
				<li>Press ESC once for Startup Menu</li>
				<li>F10 – BIOS Setup</li>
				<li>Advanced (on top)</li>
				<li>Device Configurations</li>
				<li>Scroll down to "SATA Device Mode" – set to "RAID"</li>
				<li>Confirm the change</li>
				<li>Exit</li>
				<li>Click "Yes" at the "Save settings" prompt</li>
			</ul>
		</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">4</td>
		<td width="580" colspan="2">
			<b>Restore laptop from Clonezilla image</b><br/>
			<ul>
				<li>Connect external hard drive</li>
				<li>Insert Clonezilla Restore CD into drive (use paper clip to open drive)</li>
				<li>Restart computer</li>
				<li>Restore Image:
					<ul>
						<li>Press "Enter" at the Clonezilla title screen. Wait for it to change.</li>
						<li>Press "Enter" at each prompt and wait for the next one until you reach "Available disk(s) on this machine" (6 times)</li>
						<li>Make sure your drive is listed (BUP_BK). You should see at least two drives.  When it’s listed, press ctrl-c to continue.</li>
						<li>At the next window, locate your drive. The drive contains "Seagate_Back".  It’s usually (but not always) the last option.  Select it and press "Enter".</li>
						<li>Press "Enter" for the next 3 prompts</li>
						<li>Select "restoredisk"</li>
						<li>
							Select correct file for laptop (EliteBook or zBook) and press "Enter"</li>
							<ul>
								<li>If there is more than one file, choose the most recent date for your laptop model</li>
							</ul>
						</li>
						<li>Press "Enter" once more</li>
						<li>Select "No, skip checking the image before restoring" and press "Enter".</li>
						<li>Press "Enter" once more</li>
						<li>Enter "y" and press "Enter" at the "Are you sure" prompt - twice.</li>
						<li>The restore will begin (There are 4 restores. Altogether it will take 20 minutes or so)</li>
						<li>Press "Enter"</li>
						<li>Select "poweroff" and press "Enter".</li>
					</ul>
				</li>
				<li>After it's been powered off, remove CD (use paper clip to open drive).</li>
			</ul>
		</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">5</td>
		<td width="580" colspan="2">
			Power on the computer.  At the login screen, Enter "pshi808" for the user and enter the admin password listed on the first page of this document.
		</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">6</td>
		<td width="580" colspan="2">
			<b>Update laptop name</b> <i>(ghosted unit has the laptop name that it was ghosted from)</i><br/>
			<ul>
				<li>Run "<b>&lt;External Hard Drive Letter&gt;:\prep\A04) RenameComputer</b>" as administrator.</li>
			</ul>
		</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">7</td>
		<td width="580" colspan="2">
			<b>Update BIOS as follows:</b><br/><br/>

			EliteBook:<br/>
			<ul>
				<li>Browse to "<b>&lt;External Hard Drive Letter&gt;:\prep\ A05) UpdateBIOS</b>" right-click and select "<b>Run as administrator</b>".</li>
				<li>Click "Apply Update Now"</li>
				<li>Wait for computer to reboot and log in again.</li>
			</ul><br/>

			zBook:
			<ul>
				<li>Browse to "<b>&lt;External Hard Drive Letter&gt;:\PrepZG2\ A07) UpdateBIOS</b>", right-click and select "<b>Run as administrator</b>".</li>
				<li>Accept defaults</li>
				<li>Click "Apply Update Now"</li>
				<li>Wait for computer to reboot and log in again.</li>
			</ul><br/>

			<b>You must reboot as part of this step.</b>  If you didn't reboot as part of a bios update, then reboot manually.
		</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">8</td>
		<td width="580" colspan="2">
			<b>Verify/correct Time/Date in Windows (use local time)</b><br/>
			<ul>
				<li>Adjust time zone if not being prepped in Hawaii</li>
			</ul>
		</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">9</td>
		<td width="580" colspan="2">
			<b>Verify/set screen resolution</b> (zBook)<br/>
			<ul>
				<li>Right-click on the desktop</li>
				<li>Select "Screen Resolution"</li>
				<li>Verify that it's set for "1600 x 900".</li>
				<li>Click "OK"</li>
				<li>If prompted select "Keep changes"</li>
			</ul>
		</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">10</td>
		<td width="580" colspan="2">
			<b>Run KofaxUtil</b><br/>
			<ul>
				<li>Run "<b>&lt;External Hard Drive Letter&gt;</b>:\prep\ A09) KofaxUtil as administrator.</li>
				<li>Verify that the correct machine name is displayed.</li>
			</ul>
		</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">11</td>
		<td width="580" colspan="2">
			<b>Install scanner profiles</b><br/>
			<ul>
				<li>Run "<b>&lt;External Hard Drive Letter&gt;</b>:\prep\ B05)CopyScannerProfiles</li>
			</ul>
		</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">12</td>
		<td width="580" colspan="2">
			<b>Activate Kofax license</b><br/>

			<ul>
				<li>Verify the computer is <b>not</b> connected to the network.</li>
				<li>Start &gt; All Programs &gt; Kofax Capture 10.0 &gt; License Utility</li>
				<li>Ignore any errors</li>
				<li>File &gt; Activate</li>
				<li>Click on "Manual"</li>
				<li>On a different computer, go to http://activatelegacy.kofax.com/support/Activation/manual.aspx</li>
				<li>For the email, use support@premiersolutionshi.com</li>
				<li>Enter license information for <b>Kofax Capture</b> (see page 1 of this checklist for info)</li>
				<li>Enter the Machine ID from the License Utility. (Note: Machine ID should be 02:00:4C:4F:4F:50)</li>
				<li>Copy and save the resulting text file.</li>
				<li>Transport that text file to the laptop via CD or USB, and paste the contents into the License Utility and click "Activate".</li>
				<li>File&gt;Exit</li>
			</ul>
		</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">13</td>
		<td width="580" colspan="2">
			<b>Connect laptop to network cable</b>
		</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">14</td>
		<td width="580" colspan="2">
			<b>Configure Scanner/Activate VRS license</b><br/><br/>

			<b>For fi-6140z:</b><br/>
			<ul>
				<li>No action required</li>
			</ul><br/>

			<b>For fi-7160:</b><br/>
			<ul>
				<li>Browse to "<b>&lt;External Hard Drive Letter&gt;:\PrepZG2\ A12) fi-7160 setup</b>", right-click and select "<b>Run as administrator</b>".</li>
				<li>Wait for the dos box (black command line box) to disappear.</li>
				<li>Start &gt; All Programs &gt; Kofax VRS &gt; Kofax VRS Administration Console.</li>
				<li>On the left side, select License.</li>
				<li>Enter PSHI as the company and support@premiersolutionshi.com for email.</li>
				<li>Enter the <b>Kofax VRS</b> license information that was supplied with the scanner.  Typically the Part Number is VP-W005-0001.</li>
				<li>Document license information on first page for entry into the database.</li>
				<li>Click "Activate".</li>
			</ul><br/>

			<b>For fi-6670:</b><br/>
			<ul>
				<li>Connect scanner.</li>
				<li>Start &gt; All Programs &gt; Kofax Capture 10.0 &gt; Administration.</li>
				<li>Registration will start automatically. Check box to agree.</li>
				<li>Enter PSHI as the company and support@premiersolutionshi.com for email and check "Register Online".</li>
			</ul>
		</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">15</td>
		<td width="580" colspan="2">
			<b>Activate Microsoft Office license</b><br/><br/>

			<b>For re-imaged machines:</b><br/>
			<ul>
				<li>Start Word.</li>
				<li>When the Microsoft Office Activation Wizard appears, keep the default and click "Next".</li>
				<li>Office should be able to activate itself.</li>
				<li>Exit Word.</li>
			</ul><br/>

			<b>For new machines:</b><br/>
			<ul>
				<li>Start Word</li>
				<li>Goto File > Help. Verify that it shows "Product Activated" under the Office logo</li>
				<li>
					If not:
					<ul>
						<li>Close Word</li>
						<li>Browse to "&lt;External Hard Drive Letter&gt;:\prep\ A13) ActivateOffice", right-click and "Run as administrator".</li>
						<li>Verify that license matches the information on the first page</li>
						<li>
							If not,
							<ul>
								<li>Cancel the update</li>
								<li>Right-click "A13) ActivateOffice" and select "Edit"</li>
							</ul>
						</li>
						<li>Change the License key to match the first page and run it again.</li>
					</ul>
				</li>
			</ul>

		</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">16</td>
		<td width="580" colspan="2">
			<b>Remove network cable</b>
		</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">17</td>
		<td width="580" colspan="2">
			<b>Change Network Settings</b><br/>
			<ul>
				<li>Click on "Start" and then enter "view network connections".</li>
				<li>Right-click "Local Area Connection" (It should say "Network cable unplugged"), select "Properties".</li>
				<li>Highlight "Internet Protocol Version 4 (TCP/IPv4)" and click "Properties".</li>
				<li>Click on "Use the following IP address"</li>
				<li>
					Enter:
					</ul>
						<li>IP Address: 169.254.141.156</li>
						<li>Subnet mask: 255.255.0.0</li>
					</ul>
				</li>
				<li>Click "OK"</li>
			</ul>

		</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">18</td>
		<td width="580" colspan="2">
			<b>Install ATO updates</b><br/>
			<ul>
				<li>Install all ATOs dated later then the ghost image version.</li>
				<li>List the ATOs installed in this block.</li>
			</ul>
		</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">19</td>
		<td width="580" colspan="2">
			<b>Verify McAfee virus definitions and that it’s enabled</b><br/>

			<ul>
				<li>Right-click the McAfee logo (Red, white and blue shield with a "V") in the System Tray.</li>
				<li>Select "VirusScan Console".</li>
				<li>Verify "Access Protection" and "On-Access Scanner" are both "Enabled".</li>
				<li>From the toolbar, click "Help" and "About VirusScan Enterprise".</li>
				<li>Check the "DAT Created On" date.  It should be a few days prior to the last ATO date.</li>
			</ul>
		</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">20</td>
		<td width="580" colspan="2">
			<b>Change BIOS settings</b><br/>
			<ul>
				<li>Reboot laptop</li>
				<li>When the computer boots, press ESC repeatedly at HP screen until text at lower left lights up</li>
				<li>Press F10</li>
				<li>Go to Security &gt; Setup BIOS Administrator Password. Enter "Premiertech2013"</li>
				<li>
					Go to System Configuration &gt; Built-in Device Options
					<ul>
						<li>Uncheck "Wireless button state"</li>
						<li>Uncheck "Embedded WLAN Device" (* May not be present on all models)</li>
						<li>Uncheck "Embedded Bluetooth Device"</li>
						<li>Uncheck "Embedded LAN Controller"</li>
						<li>Uncheck "Integrated Camera"</li>
						<li>Uncheck "Modem Device"</li>
						<li>Uncheck "Microphone"</li>
					</ul>
				</li>
				<li>Set "Wake on LAN" to "Disable"</li>
				<li>Exit</li>
				<li>Click "Yes" at the "Save settings" prompt</li>
			</ul>
		</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">21</td>
		<td width="480">
			<b>Update defective materials list</b><br/>
			<ul>
				<li>Copy "P:\Projects\0 - FACET Support\Defective materials listing\DefectiveMaterialReference.xls" to "C:\FIARModule" on the laptop.</li>
			</ul>
		</td>
		<td width="100">&nbsp;</td>
	</tr>

</tbody>
</table>
</center>
</p>


<br/>


<p align="center">
<center>
<table id="docTable" cellspacing="0" width="650">
<tbody>
	<tr>
		<td width="650" colspan="4" class="header">VESSEL SPECIFIC PREP</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">1</td>
		<td width="480">
			Run the latest FACET system update EXE <b>for the appropriate configuration</b>.
			<span style="font-weight:bold;color:red;">Be sure to follow the update procedures exactly as documented.</span>
		</td>
		<td width="100">&nbsp;</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">2</td>
		<td width="480">
			<b>Verify Computer Name</b><br/>
			Double click on "FACET" icon on Desktop to open database &gt; validate "Computer Name" value shown on Navigation form (click on it to change if needed – password is "Syst3mCh@nge" without quotes)
		</td>
		<td width="100">&nbsp;</td>
	</tr>

	<tr>
		<td width="650" colspan="4" class="subHeader">TESTING</td>
	</tr>

	<tr>
		<td width="650" colspan="4" style="text-align:center;">
			<b>NOTE:</b> If you have not rebooted since activating Kofax, this may not work as expected as the services (such as KTM Server, PDF and Export) may not be running.

		</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">1</td>
		<td width="480">
			Create a copy of FIARModule items:
			<ul>
				<li>Run "<b>&lt;External Hard Drive Letter&gt;</b>:\prep\T02) FAIRModule_Backup</li>
			</ul>
		</td>
		<td width="100">&nbsp;</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">2</td>
		<td width="480">
			Open Kofax Batch Manager
		</td>
		<td width="100">&nbsp;</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">3</td>
		<td width="480">
			Ideally, test all Batch Classes, but at a minimum, test the "Material Rcpts – DD13481A" and "SFOEDL Report" Batch Classes <b>with paper scanning</b> all the way through Export (Batches should disappear from Batch Manager).</br>
			<ul>
				<li>Verify scan profiles for Scan and VRS (refer to user guide for details)</li>
				<li>Verify that separator sheets work properly in the SFOEDL Reports Batch Class and then in the Validation module (<b>not KTM Validation</b>), show Batch Contents (View tab &gt; check "Batch Contents") and set options for Enter button (Orb button > Options > check "Enter to move to next field" > click "OK")</li>
				<li>Verify that 1348s are being read okay (process all the way through) and arrange KTM Validation for proper layout.</li>
				<li>Verify automated Kofax modules (KTM Server/Recognition Server, PDF Generator, Export should all start/run automatically)</li>
			</ul>
		</td>
		<td width="100">&nbsp;</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">4</td>
		<td width="480">
			Open FACET db and validate functions (Export to X71 and Export to FSM, Worklists, Excel export, ZIP file creation, etc)
		</td>
		<td width="100">&nbsp;</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">5</td>
		<td width="480">
			Test the ability to write files to a CD/RW.<br/>
			<ul>
				<li>Insert a CD/RW in the drive.</li>
				<li>Open Windows Explorer and verify that the drive is "CD/DVD Drive".</li>
				<li>Copy files created in step #4 above to the CD/RW.</li>
				<li>Eject the CD/RW and confirm that the files can be opened from the CD on another machine.</li>
			</ul>
		</td>
		<td width="100">&nbsp;</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">6</td>
		<td width="480">
			Once all testing is complete and successful:
			<ul>
				<li>Run "<b>&lt;External Hard Drive Letter&gt;</b>:\prep\T05) FAIRModule_Restore</li>
				<li><span style="font-weight:bold;color:red;text-decoration:underline;">Open FACET db to verify that no data/documents exist</span></li>
			</ul>
		</td>
		<td width="100">&nbsp;</td>
	</tr>


	<tr>
		<td width="650" colspan="4" class="subHeader">FINALIZE</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">1</td>
		<td width="480">
			Verify that PSHI tracking database (intranet) has been updated with version info for FACET
		</td>
		<td width="100">&nbsp;</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">2</td>
		<td width="480">
			<b>If system has been sitting for a while after prepping</b>, double check that FACET System Update/ATO Updates/DMS file is current
		</td>
		<td width="100">&nbsp;</td>
	</tr>

	<tr>
		<td width="650" colspan="4" class="subHeader">BACKFILE</td>
	</tr>

	<tr>
		<td width="40">&nbsp;</td>
		<td width="30" class="numRow">1</td>
		<td width="480">
			If Backfile exists, update data and documents per the procedures
		</td>
		<td width="100">&nbsp;</td>
	</tr>

</tbody>
</table>
</center>
</p>


</div>
</body>
</html>
