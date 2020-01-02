<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="pageTitle" value="Issue Category Admin"/>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk" scope="request" class="java.lang.String"/>

<jsp:useBean id="issueCategoryList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="scannerIssueList"  scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="laptopIssueList"   scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="softwareIssueList" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="facetVersionList"  scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="locationList"      scope="request" class="java.util.ArrayList"/>

<jsp:useBean id="operation" scope="request" class="java.lang.String"/>
<jsp:useBean id="currFacetVersion" scope="request" class="java.lang.String"/>

<bean:define id="pageTitle" value="System Variables - Issue Categories"/>

<%@ include file="../layout/old/layout-header.jsp" %>

<tags:projectBreadcrumb project="${project}" pageTitle="System Variables" parentTitle="Issue Categories" />

<div class="row">
  <div class="col-sm-3"></div>
  <div class="col-sm-6">
    <div class="card">
      <div class="card-header card-header-info card-header-icon">
        <div class="card-icon">
          <i class="material-icons">settings</i>
        </div>
        <div style="display: inline-flex; float:left;">
          <h4 class="card-title">Issue Categories</h4>
        </div>
      </div>
      <div class="card-body">
        <div class="row">
          <div class="col-sm-4">
            <!-- Support Issue Categories -->
            <table id="tanTable_style2" style="width:100%;">
              <thead>
                <tr>
                  <th>Managed Lists</th>
                </tr>
              </thead>
              <tbody>
                <logic:notEmpty name="managedLists">
                  <logic:iterate id="list" name="managedLists">
                  <tr>
                    <td>
                      <a href="managedList.do?projectPk=${projectPk}&managedListCode=${list.code}"
                        ${list.code == selectedManagedList.code ? 'class="disabled selected"' : ''}>${list.name}
                      </a>
                    </td>
                  </tr>
                  </logic:iterate>
                </logic:notEmpty>
                <tr>
                  <td>
                    <a href="system.do?action=systemVariables&projectPk=${projectPk}" class="disabled selected">
                      Issue Categories
                    </a>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <div class="col-sm-8">
            <table id="tanTable_style2" class="alt-color border-zero cellspacing-zero" style="width:100%;">
            <thead>
              <tr class="ignore">
                <td colspan="2" align="center" class="noborder header" style="background:#fff;font-size:13px;font-weight:bold;">Support Issue Categories</td>
              </tr>
              <tr>
                <th>Category</th>
                <th>Action</th>
              </tr>
            </thead>
              <tbody>
                <logic:iterate id="resultBean" name="issueCategoryList" type="com.premiersolutionshi.old.bean.LookupBean">
                  <tr>
                    <td>${resultBean.value}</td>
                    <td align="center">
                      <a href="lookup.do?action=issueCategoryDeleteDo&key=${resultBean.key}&projectPk=${projectPk}"
                        onclick="return confirmDelete('${resultBean.value}');">
                        <i class="material-icons">delete</i>
                      </a>
                    </td>
                  </tr>
                </logic:iterate>
              </tbody>
            <tfoot>
              <html:form action="lookup.do" method="POST" styleClass="form-horizontal">
              <input type="hidden" name="action" value="issueCategoryAddDo"/>
              <input type="hidden" name="projectPk" value="${projectPk}"/>
              <tr>
                <td colspan="2" class="navbar-form">
                  <div class="input-group">
                    <span class="input-group-addon" id="basic-addon1"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span></span>
                    <input type="text" class="form-control" name="value" id="new-category" placeholder="New Category" aria-describedby="basic-addon1">
                  </div>
                  <button type="submit" class="btn btn-success">Add</button>
                </td>
              </tr>
              </html:form>
            </tfoot>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="col-sm-3"></div>
</div>
<%@ include file="../layout/old/layout-footer.jsp" %>

<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/jquery-altcolor.js"></script>
<script type="text/javascript" src="js/common/managedList.js"></script>
</body>
</html>
