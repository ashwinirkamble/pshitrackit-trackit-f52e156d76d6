<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<bean:define id="defaultPageTitle" value="User Add/Edit"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="inputBean" scope="request" class="com.premiersolutionshi.old.bean.UserBean"/>
<jsp:useBean id="projectList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="editType"  scope="request" class="java.lang.String"/>

<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>TrackIT - ${pageTitle}</title>

  <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/stylesheet.css"/>
</head>

<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask holygrail"><div class="colmid"><div class="colleft">
  <div class="col1wrap"><div class="col1">
    <%@ include file="../include/content-header.jsp" %>

    <tags:adminBreadcrumb pageTitle="${pageTitle}"
      parentUrl="./user.do?action=userList" parentTitle="Manage Users" />

    <p align="center">
    Fields marked with an asterisk <span class="regAsterisk">*</span> are required.
    </p>

    <p align="center">
    <div class="center">
    <html:form action="user.do" onsubmit="return valFields();" method="POST">
    <table id="tanTable_style2" class="border-zero cellspacing-zero">
    <% if (editType.equals("add")) { %>
      <input type="hidden" name="action" value="userAddDo"/>
    <% } else { %>
      <input type="hidden" name="action" value="userEditDo"/>
      <html:hidden name="inputBean" property="userPk"/>
    <% } %>
    <tbody>
      <tr><th>Login Information</th></tr>
      <tr><td class="nobordered" align="left">
        <table class="border-zero cellspacing-zero cellpadding-3">
        <colgroup>
          <col style="width:120px"/>
          <col/>
          <col style="width:170px"/>
        </colgroup>
        <tbody>
          <tr>
            <td class="fieldName"><span class="regAsterisk">*</span> Username:</td>
            <td><html:text name="inputBean" property="username" size="20" maxlength="50"/></td>
            <td class="fieldName">
              <% if (editType.equals("add")) { %>
                <span class="regAsterisk">*</span> Password:
              <% } else { %>
              Password
              <span style="font-weight:bold;color:red;font-size:80%;">(Enter only to reset)</span>:
              <% } %>
            </td>
            <td><html:password name="inputBean" property="password" size="20" maxlength="50"/></td>
          </tr>
        </tbody>
        </table>
      </td></tr>

      <tr><th>User Information</th></tr>
      <tr><td class="nobordered" align="left">
        <table class="border-zero cellspacing-zero cellpadding-3">
        <colgroup>
          <col style="width:120px"/>
          <col/>
          <col style="width:120px"/>
        </colgroup>
        <tbody>
          <tr>
            <td class="fieldName"><span class="regAsterisk">*</span> Last Name:</td>
            <td><html:text name="inputBean" property="lastName" size="20" maxlength="50"/></td>
            <td class="fieldName"><span class="regAsterisk">*</span> First Name:</td>
            <td><html:text name="inputBean" property="firstName" size="20" maxlength="50"/></td>
          </tr>
          <tr>
            <td class="fieldName"><span class="regAsterisk">*</span> Company/Org:</td>
            <td colspan="3">
              <html:select name="inputBean" property="currOrganization" onchange="checkNew(this, 'organization');">
                <html:option value=""/>
                <html:options name="organizationList"/>
                <html:option value="null">Add new...</html:option>
              </html:select>
              <html:text name="inputBean" property="organization" style="display:none;"/>
            </td>
          </tr>
          <tr>
            <td class="fieldName">Job Title:</td>
            <td colspan="3"><html:text name="inputBean" property="title" size="45" maxlength="50"/></td>
          </tr>
        </tbody>
        </table>
      </td></tr>

      <tr><th>Contact Information</th></tr>
      <tr><td class="nobordered" align="left">
        <table class="border-zero cellspacing-zero cellpadding-3">
        <colgroup>
          <col style="width:120px"/>
          <col/>
          <col style="width:120px"/>
        </colgroup>
        <tbody>
          <tr>
            <td class="fieldName">E-Mail:</td>
            <td colspan="3"><html:text name="inputBean" property="email" size="38" maxlength="255"/></td>
          </tr>
          <tr>
            <td class="fieldName">Work Phone:</td>
            <td><html:text name="inputBean" property="workNumber" size="15" maxlength="25"/></td>
            <td class="fieldName">Quick Dial:</td>
            <td><html:text name="inputBean" property="quickDial" size="5" maxlength="10"/></td>
          </tr>
          <tr>
            <td class="fieldName">Fax Number:</td>
            <td><html:text name="inputBean" property="faxNumber" size="15" maxlength="25"/></td>
            <td class="fieldName">Cell Number:</td>
            <td><html:text name="inputBean" property="cellNumber" size="15" maxlength="25"/></td>
          </tr>
        </tbody>
        </table>
      </td></tr>

      <tr><th>Roles</th></tr>
      <tr><td class="nobordered" align="left" style="padding-left:15px;">
        <label>
          <html:multibox name="inputBean" property="roleArr" value="sysadmin"/>
          sysadmin
        </label>
        -
        <span style="color:#999;">System administrator to manage sites/users/projects</span>
        <br/>

        <label>
          <html:multibox name="inputBean" property="roleArr" value="pshi"/>
          pshi user
        </label>
        -
        <span style="color:#999;">Premier Intranet Employee Tools Application access</span>
        <br/>

        <label>
          <html:multibox name="inputBean" property="roleArr" value="pshi-user-admin"/>
          pshi-user-admin
        </label>
        -
        <span style="color:#999;">PSHI User Administrator</span>
        <br/>

        <label>
          <html:multibox name="inputBean" property="roleArr" value="logcop-facet"/>
          logcop-facet
        </label>
        -
        <span style="color:#999;">LOGCOP-FACET Application access</span>
        <br/>

        <label>
          <html:multibox name="inputBean" property="roleArr" value="manager-gui"/>
          manager-gui
        </label>
        -
        <span style="color:#999;">Apache Tomcat Manager</span>
        <br/>

      </td></tr>

      <tr><th>Projects</th></tr>
      <tr><td class="nobordered" align="left" style="padding-left:15px;">
        <% int k = 0; %>
        <logic:iterate id="projectBean" name="projectList" type="com.premiersolutionshi.old.bean.ProjectBean">
          <span style="color:#aaa;border-bottom:1px dotted #aaa;">${projectBean.customer}</span><br/>
          <logic:iterate id="resultBean" name="projectBean" property="taskList" type="com.premiersolutionshi.old.bean.ProjectBean">
            <label>
              <html:multibox name="inputBean" property="projectPkArr">
                ${resultBean.projectPk}
              </html:multibox>

              ${resultBean.projectName}
            </label>
            <br/>
            <% k++; %>
          </logic:iterate>
          <br/>
        </logic:iterate>
      </td></tr>
    </tbody>
    </table>

    <table id="borderlessTable" class="border-zero cellspacing-zero">
    <tbody>
      <tr>
        <td align="center">
          <% if (editType.equals("add")) { %>
            <html:submit value="Insert"/>
          <% } else { %>
            <html:submit value="Save"/>
          <% } %>
        </td>
        <td align="center"><input type="button" onclick="window.location='user.do?action=userList';" value="Cancel"/></td>
      </tr>
    </tbody>
    </table>
    </html:form>
    </div>
    </p>
  </div></div>
  <%@ include file="../include/app-col2.jsp" %>
  <%@ include file="../include/app-col3.jsp" %>
</div></div></div>

<%@ include file="../include/app-footer.jsp" %>

<script type="text/javascript" src="js/jquery-altcolor.js"></script>

<script type="text/javascript">
  $(document).ready(function () {
    document.userForm.username.focus();
  });

  function valFields() {
    var username = stripSpaces(document.userForm.username.value);
    var password = stripSpaces(document.userForm.password.value);
    var lastName = stripSpaces(document.userForm.lastName.value);
    var firstName = stripSpaces(document.userForm.firstName.value);
    var organization = stripSpaces(document.userForm.organization.value);

    document.userForm.username.value = username;
    document.userForm.lastName.value = lastName;
    document.userForm.firstName.value = firstName;

    if (username.length < 1) {
      alert("You must enter in a username.");
      document.userForm.username.focus();
      return false;
    } //end of if

    <% if (editType.equals("add")) { %>
    if (password.length < 1) {
      alert("You must enter in a password.");
      document.userForm.password.focus();
      return false;
    } //end of if
    <% } %>

    if (password.length >= 1 && password.length < 4) {
      alert("Password must be at least 4 characters long.");
      document.userForm.password.focus();
      return false;
    } //end of if

    if (lastName.length < 1) {
      alert("You must enter in a last name.");
      document.userForm.lastName.focus();
      return false;
    } //end of if

    if (firstName.length < 1) {
      alert("You must enter in a first name.");
      document.userForm.firstName.focus();
      return false;
    } //end of if

    if (organization.length < 1) {
      alert("You must specify an organization.");
      document.userForm.currOrganization.focus();
      return false;
    } //end of if

    return true;
  } //end of valFields

  function checkNew(currObj, elementName) {
    if (currObj.value == 'null') {
      document.getElementsByName(elementName)[0].value = '';
      document.getElementsByName(elementName)[0].style.display = 'inline';
      document.getElementsByName(elementName)[0].focus();
    } else {
      document.getElementsByName(elementName)[0].value = currObj.value;
      document.getElementsByName(elementName)[0].style.display = 'none';
    } //end of if
  } //end of checkNew
</script>

</body>
</html>
