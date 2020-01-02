<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<%@ attribute name="pageTitle" required="false" type="java.lang.String" %>
<%@ attribute name="parentUrl" required="false" type="java.lang.String" %>
<%@ attribute name="parentTitle" required="false" type="java.lang.String" %>

<tags:breadcrumb baseTitle="Administration" baseUrl="page.do"
  pageTitle="${pageTitle}"
  parentTitle="${parentTitle}" parentUrl="${parentUrl}" />
