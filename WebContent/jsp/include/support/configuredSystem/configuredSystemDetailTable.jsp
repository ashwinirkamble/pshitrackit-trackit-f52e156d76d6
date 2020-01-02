<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<thead>
  <tr>
    <th colspan="2" class="center">
      <div style="width:100px;white-space: nowrap;">
      <%--Ashwini /system.do to ./system.do--%>
        ${configuredSystem.computerName}
        <a title="View ${configuredSystem.computerName}" target="_blank"
          href="./system.do?action=configuredSystemDetail&configuredSystemPk=${configuredSystem.id}&projectPk=${projectPk}">
          <i class="material-icons" style="font-size:20px;">pageview</i>
        </a>
        <a title="Edit ${configuredSystem.computerName}" target="_blank"
          href="./system.do?action=configuredSystemEdit&configuredSystemPk=${configuredSystem.id}&projectPk=${projectPk}">
          <i class="material-icons" style="font-size:20px;">edit</i>
        </a>
      </div>
    </th>
  </tr>
</thead>
<tbody>
  <logic:notEmpty name="configuredSystem" property="currentLocation">
    <tr>
      <td class="fieldName">Current Location:</td>
      <td>${configuredSystem.currentLocation.location}</td>
    </tr>
  </logic:notEmpty>
  <tr>
    <td class="fieldName">FACET Version:</td>
    <td>
      ${configuredSystem.facetVersion}
      <logic:empty name="configuredSystem" property="facetVersion">--</logic:empty>
      <logic:notEmpty name="configuredSystem" property="facetVersion">
        <logic:equal name="configuredSystem" property="facetVersion" value="${currFacetVersion}">
          <img src="images/checkbox_checked.png" title="FACET Version is current"/>
        </logic:equal>
        <logic:notEqual name="configuredSystem" property="facetVersion" value="${currFacetVersion}">
          (Current: ${currFacetVersion})
          <img src="images/icon_error.gif" title="FACET Version is not current"/> 
        </logic:notEqual>
      </logic:notEmpty>
    </td>
  </tr>
  <tr>
    <td class="fieldName">OS Version:</td>
    <td>
      ${configuredSystem.osVersion}
      <logic:empty name="configuredSystem" property="osVersion">--</logic:empty>
      <logic:notEmpty name="configuredSystem" property="osVersion">
        <logic:equal name="configuredSystem" property="osVersion" value="${currosVersion}">
          <img src="images/checkbox_checked.png" title="OS Version is current"/>
        </logic:equal>
        <logic:notEqual name="configuredSystem" property="osVersion" value="${currOsVersion}">
          (Current: ${currOsVersion})
          <img src="images/icon_error.gif" title="OS Version is not current"/> 
        </logic:notEqual>
      </logic:notEmpty>
    </td>
  </tr>
  <tr>
    <td class="fieldName">DMS List as of:</td>
    <td>
      ${configuredSystem.dmsVersion}
      <logic:empty name="configuredSystem" property="dmsVersion">--</logic:empty>
      <logic:notEmpty name="configuredSystem" property="dmsVersion">
        <logic:equal name="configuredSystem" property="dmsVersion" value="${currDmsVersion}">
          <img src="images/checkbox_checked.png" title="DMS Version is current."/>
        </logic:equal>
        <logic:notEqual name="configuredSystem" property="dmsVersion" value="${currDmsVersion}">
          (Current: ${currDmsVersion})
          <img src="images/icon_error.gif" title="DMS Version is NOT current."/>
        </logic:notEqual>
      </logic:notEmpty>
    </td>
  </tr>
  <tr>
    <td class="fieldName">Network Adapter Type:</td>
    <td>
      ${configuredSystem.networkAdapter}
      <logic:equal name="configuredSystem" property="networkAdapter" value="MS Loopback Adapter">
        <img src="images/checkbox_checked.png" title="Using 'MS Loopback Adapter'"/>
      </logic:equal>
      <logic:notEqual name="configuredSystem" property="networkAdapter" value="MS Loopback Adapter">
        <img src="images/icon_error.gif" title="NOT using 'MS Loopback Adapter'"/> 
      </logic:notEqual>
    </td>
  </tr>
  <tr>
  <td class="fieldName">Admin password:</td>
  <td>${configuredSystem.adminPassword}</td>
  </tr>
</tbody>

