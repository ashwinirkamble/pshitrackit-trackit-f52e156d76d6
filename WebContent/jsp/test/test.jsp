<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods"%>
<%@ page import="com.premiersolutionshi.common.domain.User"%>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean"%>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html"%>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic"%>
<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested"%>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String" />
<bean:define id="defaultPageTitle" value="FACET Configured System List" />
<bean:define id="pageTitle" name="customPageTitle" />
<logic:equal name="pageTitle" value="">
  <bean:define id="pageTitle" name="defaultPageTitle" />
</logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean" />
<jsp:useBean id="userList" scope="request" class="java.util.ArrayList" />
<%@ include file="../layout/layout-header.jsp"%>
<div class="row">
  <div class="col-lg-12">
    <h1 class="page-header">Dashboard</h1>
  </div>
  <!-- /.col-lg-12 -->
</div>
<!-- /.row -->

<div class="row">
  <div class="col-lg-8">
    <div class="panel panel-default">
      <div class="panel-heading">
        <i class="fa fa-users fa-fw"></i> User List (<%=userList.size()%> users)
        <div class="pull-right">
          <div class="btn-group">
            <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
              Actions <span class="caret"></span>
            </button>
            <ul class="dropdown-menu pull-right" role="menu">
              <li><a href="#">Action</a></li>
              <li><a href="#">Another action</a></li>
              <li><a href="#">Something else here</a></li>
              <li class="divider"></li>
              <li><a href="#">Separated link</a></li>
            </ul>
          </div>
        </div>
      </div>
      <!-- /.panel-heading -->
      <div class="panel-body">
        <div class="row">
          <div class="col-lg-4">
            <div class="table-responsive">
              <table class="table table-bordered table-hover table-striped">
                <thead>
                  <tr>
                    <th>Index</th>
                    <th>ID</th>
                    <th>Username</th>
                  </tr>
                </thead>
                <tbody>
                  <logic:iterate id="user" name="userList" type="com.premiersolutionshi.common.domain.User" indexId="userIndex">
                    <tr>
                      <td><%=userIndex%></td>
                      <td><bean:write name="user" property="id" /> </td>
                      <td><bean:write name="user" property="username" /> </td>
                    </tr>
                  </logic:iterate>
                </tbody>
              </table>
            </div>
            <!-- /.table-responsive -->
          </div>
          <!-- /.col-lg-4 (nested) -->
        </div>
        <!-- /.row -->
      </div>
      <!-- /.panel-body -->
    </div>
    <!-- /.panel -->
  </div>
  <!-- /.col-lg-4 -->
</div>
<!-- /.row -->
<%@ include file="../layout/layout-footer.jsp"%>