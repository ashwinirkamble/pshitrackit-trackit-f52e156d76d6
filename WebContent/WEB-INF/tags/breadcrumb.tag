<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic"%>

<%@ attribute name="baseTitle" required="true" type="java.lang.String" %>
<%@ attribute name="baseUrl" required="true" type="java.lang.String" %>
<%@ attribute name="pageTitle" required="false" type="java.lang.String" %>
<%@ attribute name="parentUrl" required="false" type="java.lang.String" %>
<%@ attribute name="parentTitle" required="false" type="java.lang.String" %>
<%@ attribute name="parent2Url" required="false" type="java.lang.String" %>
<%@ attribute name="parent2Title" required="false" type="java.lang.String" %>

<div class="center">
  <ol class="breadcrumb">
    <li><a href="${baseUrl}">${baseTitle}</a></li>
    <logic:notEmpty name="parentUrl">
      <logic:notEmpty name="parentTitle">
        <li><a href="${parentUrl}">${parentTitle}</a></li>
      </logic:notEmpty>
    </logic:notEmpty>
    <logic:notEmpty name="parent2Url">
      <logic:notEmpty name="parent2Title">
        <li><a href="${parent2Url}">${parent2Title}</a></li>
      </logic:notEmpty>
    </logic:notEmpty>
    <logic:notEmpty name="pageTitle">
      <li>${pageTitle}</li>
    </logic:notEmpty>
  </ol>
</div>
