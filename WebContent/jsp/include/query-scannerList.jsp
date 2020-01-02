<h2>Query Scanners</h2>

<html:form action="hardware.do" method="GET">
<input type="hidden" name="action" value="scannerList"/>
<input type="hidden" name="sortBy" value="<bean:write name="sortBy"/>"/>
<input type="hidden" name="sortDir" value="<bean:write name="sortDir"/>"/>
<input type="hidden" name="searchPerformed" value="Y"/>

<p align="left">
Laptop Assigned To<br/>
<html:text name="inputBean" property="computerName" size="12" maxlength="50"/>
</p>

<p align="left">
Tag<br/>
<html:text name="inputBean" property="tag" size="6" maxlength="50"/>
</p>

<p align="left">
Model Number<br/>
<html:text name="inputBean" property="modelNumber" size="12" maxlength="50"/>
</p>

<p align="left">
Serial Number<br/>
<html:text name="inputBean" property="serialNumber" size="12" maxlength="50"/>
</p>

<!--p align="left">
Product Name<br/>
<html:text name="inputBean" property="productName" size="15" maxlength="50"/>
</p-->

<p align="left">
Date Received<br/>
<html:text name="inputBean" property="receivedDate" styleClass="datepicker" size="9"/>
</p>

<p align="left">
Date Prepped<br/>
<html:text name="inputBean" property="preppedDate" styleClass="datepicker" size="9"/>
</p>

<p align="center">
<center>
<table id="borderlessTable" border="0" cellspacing="0">
<tbody>
<tr>
<td align="center"><html:submit value="Search"/></td>
<td align="center"><input type="button" onclick="window.location='hardware.do?action=scannerList&searchPerformed=Y';" value="View All"/></td>
</tr>
</tbody>
</table>
</center>
</p>
</html:form>
