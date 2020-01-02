<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods"%>

<jsp:useBean id="alertSuccess" scope="request" class="java.lang.String" />
<jsp:useBean id="alertWarning" scope="request" class="java.lang.String" />
<jsp:useBean id="alertDanger" scope="request" class="java.lang.String" />
<jsp:useBean id="contentNotification_css" scope="request" class="java.lang.String" />
<jsp:useBean id="contentNotification_icon" scope="request" class="java.lang.String" />
<jsp:useBean id="contentNotification_text" scope="request" class="java.lang.String" />

<%-- CONTENT-HEADER --%>
<logic:notEmpty name="pageTitle">
  <div id="content-header" class="center">
    <logic:present name="pageTitle">
      <logic:notEmpty name="pageTitle">
        <p class="page-title center">
          <bean:write name="pageTitle" />
          <% if (CommonMethods.isIn((String[]) request.getAttribute("contentHeader_projectPages"), request.getServletPath())) { %>
          <logic:present name="contentHeader_projectName">
            <logic:notEmpty name="contentHeader_projectName">
              <br />
              <span style="font-size: 12px; color: #777;"><bean:write name="contentHeader_projectName" /></span>
            </logic:notEmpty>
          </logic:present>
          <% } %>
          <%-- 
          <logic:notPresent name="contentHeader_projectName">
            <%@ include file="./project/projectSelect.jsp" %>
          </logic:notPresent>
         --%>
        </p>
      </logic:notEmpty>
    </logic:present>
    <logic:notEmpty name="alertSuccess">
      <div class="alert alert-success alert-dismissible" role="alert">
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        <strong>Success!</strong>
        <bean:write name="alertSuccess" />
      </div>
    </logic:notEmpty>
    <logic:notEmpty name="alertWarning">
      <div class="alert alert-warning alert-dismissible" role="alert">
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        <strong>Warning!</strong>
        <bean:write name="alertWarning" />
      </div>
    </logic:notEmpty>
    <logic:notEmpty name="alertDanger">
      <div class="alert alert-danger alert-dismissible" role="alert">
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        <strong>Error!</strong>
        <bean:write name="alertDanger" />
      </div>
    </logic:notEmpty>
    <logic:notEmpty name="errorMsg">
      <div class="alert alert-danger alert-dismissible" role="alert">
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        <strong>Error!</strong>
        <bean:write name="errorMsg" />
      </div>
    </logic:notEmpty>
    <logic:notEmpty name="contentNotification_css">
      <logic:notEmpty name="contentNotification_icon">
        <logic:notEmpty name="contentNotification_text">
          <p class="<bean:write name="contentNotification_css"/>">
            <img src="<bean:write name="contentNotification_icon"/>" height="16" width="16" />&nbsp;
            <bean:write name="contentNotification_text" />
          </p>
        </logic:notEmpty>
      </logic:notEmpty>
    </logic:notEmpty>
    <br />
  </div>
</logic:notEmpty>