<h2>Query Laptops</h2>

<html:form action="hardware.do" method="GET">
<input type="hidden" name="action" value="laptopList"/>
<input type="hidden" name="sortBy" value="<bean:write name="sortBy"/>"/>
<input type="hidden" name="sortDir" value="<bean:write name="sortDir"/>"/>
<input type="hidden" name="searchPerformed" value="Y"/>

<p align="left">
Ship Assigned to<br/>
<html:text name="inputBean" property="shipName" styleId="shipName" size="20" maxlength="50"/>
</p>

<p align="left">
Computer Name<br/>
<html:text name="inputBean" property="computerName" size="12" maxlength="50"/>
</p>

<p align="left">
Tag<br/>
<html:text name="inputBean" property="tag" size="6" maxlength="50"/>
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
MAC Address<br/>
<html:text name="inputBean" property="macAddress" size="18" maxlength="17"/>
</p>

<p align="center">
<center>
<table id="borderlessTable" border="0" cellspacing="0">
<tbody>
<tr>
<td align="center"><html:submit value="Search"/></td>
<td align="center"><input type="button" onclick="window.location='hardware.do?action=laptopList&searchPerformed=Y';" value="View All"/></td>
</tr>
</tbody>
</table>
</center>
</p>
</html:form>
