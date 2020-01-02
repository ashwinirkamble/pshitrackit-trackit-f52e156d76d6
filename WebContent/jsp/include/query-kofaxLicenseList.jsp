<h2>Query Kofax Licenses</h2>

<html:form action="software.do" method="GET">
<input type="hidden" name="action" value="kofaxLicenseList"/>
<input type="hidden" name="sortBy" value="<bean:write name="sortBy"/>"/>
<input type="hidden" name="sortDir" value="<bean:write name="sortDir"/>"/>
<input type="hidden" name="searchPerformed" value="Y"/>

<p align="left">
Laptop Assigned To<br/>
<html:text name="inputBean" property="computerName" size="12" maxlength="50"/>
</p>

<p align="left">
License Key<br/>
<html:text name="inputBean" property="licenseKey" size="7" maxlength="7"/>
</p>

<p align="left">
Product Code<br/>
<html:text name="inputBean" property="productCode" size="9" maxlength="9"/>
</p>


<p align="left">
Date Received<br/>
<html:text name="inputBean" property="receivedDate" styleClass="datepicker" size="9"/>
</p>

<p align="center">
<center>
<table id="borderlessTable" border="0" cellspacing="0">
<tbody>
<tr>
<td align="center"><html:submit value="Search"/></td>
<td align="center"><input type="button" onclick="window.location='software.do?action=kofaxLicenseList&searchPerformed=Y';" value="View All"/></td>
</tr>
</tbody>
</table>
</center>
</p>
</html:form>
