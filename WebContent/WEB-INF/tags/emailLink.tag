<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<%@ attribute name="value" required="false" type="java.lang.String" %>

<logic:notEmpty name="value">
  <a href="mailto:${value}">${value}</a>
</logic:notEmpty>