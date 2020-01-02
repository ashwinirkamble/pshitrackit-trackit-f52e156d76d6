<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="fn"    uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="pageTitle" value="POC List"/>

<%@ include file="../../layout/old/layout-header.jsp" %>

<tags:projectBreadcrumb project="${project}" pageTitle="${pageTitle}"/>

<%@ include file="../../include/support/poc/pocSearchBar.jsp" %>

<%@ include file="../../layout/old/layout-footer.jsp" %>

<script type="text/javascript" src="js/jquery-altcolor.js"></script>
<script type="text/javascript" src="js/valdate.js"></script>
</body>
</html>


