<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<%@ attribute name="value" required="false" type="com.premiersolutionshi.support.domain.Ship" %>

<logic:notEmpty name="value">
  <a href="organization.do?action=unitPocList&projectPk=${projectPk}&shipFk=${value.id}"
    title="View '${value.shipName}' POCs">
    ${value.fullName}
  </a>
  <a href="javascript:void(0);" title="See Unit Info Popup"
    onclick="return showUnitPopup(${value.id},${projectPk});">
    <i class="material-icons" style="font-size:16px;">launch</i>
  </a>
</logic:notEmpty>