<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<%@ attribute name="value" required="true" type="com.premiersolutionshi.common.domain.Organization" %>

<logic:notEmpty name="value">
  ${value.name}<br/>
  <logic:notEmpty name="value" property="address1">
    ${value.address1}<br/>
  </logic:notEmpty>
  <logic:notEmpty name="value" property="address2">
    ${value.address2}<br/>
  </logic:notEmpty>
  <logic:notEmpty name="value" property="city">
    ${value.city}<logic:empty name="value" property="stateProvince"><br/></logic:empty>
  </logic:notEmpty>
  <logic:notEmpty name="value" property="stateProvince"><logic:notEmpty name="value" property="city">,</logic:notEmpty>
    ${value.stateProvince}<br/>
  </logic:notEmpty>
  ${value.zip} ${value.country}
</logic:notEmpty>
<logic:empty name="value">-</logic:empty>
