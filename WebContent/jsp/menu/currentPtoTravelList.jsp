
<%@ page language="java"%>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>


<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="defaultPageTitle" value="Current PTO/Travel"/>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>
<jsp:useBean id="userBean" scope="request" class="com.premiersolutionshi.old.bean.UserBean"/>


<%@ include file="../layout/old/layout-header.jsp" %>

<html>
<head>
<meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
 <link rel="stylesheet" type="text/css" href="css/stylesheet.css"/>
</head>
<body>
<logic:iterate id="resultBean" name="CurrentPtoList" type="com.premiersolutionshi.old.bean.UserBean">
<table id="dataTable_style2" border="0" cellspacing="0" class="alt-color">
        <thead>
        <tr align="center">
          <th align="left">User</th>
          <th>Begin</th>
          <th>End</th>
          <th>Type</th>
        </tr>
      </thead>
      <tbody>
       <tr>
     <td> <bean:write name="resultBean" property="firstName"/>
           <bean:write name="resultBean" property="lastName"/>
          </td>
          <td align="center"><bean:write name="resultBean" property="startDate"/></td>
          <td align="center"><bean:write name="resultBean" property="endDate"/></td>
          <td align="center"><bean:write name="resultBean" property="leaveType"/></td>
                
   
</tbody>
</table>
</logic:iterate>

</body>
</html>