<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="fn"    uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="pageTitle" value="Manage Contracts"/>

<%@ include file="../../layout/old/layout-header.jsp" %>

<tags:adminBreadcrumb pageTitle="${pageTitle}"/>

<div class="card">
  <%@ include file="../../include/report/contract/contractCardHeader.jsp" %>
  <div class="card-body">
    <table class="contractTable">
      <thead>
        <tr>
          <th>Contract Number</th>
          <th>Name</th>
          <th>Organization</th>
          <th>Last Updated</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <logic:iterate id="contract" name="contractList">
          <tr>
            <td><a href="contract.do?action=view&id=${contract.id}">${contract.contractNumber}</a></td>
            <td>
              ${contract.name}
              <logic:empty name="contract" property="name">-</logic:empty>
            </td>
            <td>
              <logic:notEmpty name="contract" property="organization">
                ${contract.organization.name}
              </logic:notEmpty>
              <logic:empty name="contract" property="organization">-</logic:empty>
            </td>
            <td><span title="${contract.lastUpdatedBy}">${contract.lastUpdatedDateStr}</span></td>
            <td align="center">
              <a href="contract.do?action=form&id=${contract.id}">
                <i class="material-icons">edit</i>
              </a>
              <a href="contract.do?action=delete&id=${contract.id}"
                onclick="return confirmDelete('${contract.id}', '${contract.contractNumber}');">
                <i class="material-icons">delete</i>
              </a>
            </td>
          </tr>
        </logic:iterate>
      </tbody>
    </table>
  </div>
</div>

<%@ include file="../../layout/old/layout-footer.jsp" %>

<script type="text/javascript" src="js/jquery-altcolor.js"></script>
<script type="text/javascript" src="js/valdate.js"></script>
<script type="text/javascript" src="js/report/contract.js"></script>
</body>
</html>


