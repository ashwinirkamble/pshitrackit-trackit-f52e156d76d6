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

<%-- <jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean" /> --%>

<jsp:useBean id="all" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="message" scope="request" class="java.lang.String" />
<jsp:useBean id="messageType" scope="request" class="java.lang.String" />

<%@ include file="../layout/material-header.jsp"%>
<div class="container-fluid">
  <div class="row">
    <div class="col-lg-12 center">
      <a href="testBlog.do?action=add">
        <button type="button" class="btn btn-primary">
          Add New
        </button>
      </a>
      <div class="pull-right">
        <div class="btn-group">
          <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
            Actions <span class="caret"></span>
          </button>
          <ul class="dropdown-menu pull-right" role="menu">
            <li><a href="testBlog.do?action=add"><i class="fa fa-plus fa-fw"></i>Add New</a></li>
            <!-- <li><a href="#">Action</a></li>
            <li><a href="#">Something else here</a></li>
            <li class="divider"></li>
            <li><a href="#">Separated link</a></li> -->
          </ul>
        </div>
      </div>
    </div>
  </div>

  <div class="row">
    <div class="col-md-12">
      <div class="card" style="margin-top:10px;">
        <div class="card-body">
          <div class="table-responsive">
            <table class="table table-bordered table-hover table-striped">
              <thead class=" text-primary">
                <tr>
                  <th>ID</th>
                  <th>Title</th>
                  <th>Body</th>
                  <th>Last Updated</th>
                  <th>Edit</th>
                </tr>
              </thead>
              <tbody>
                <logic:iterate id="testBlog" name="all" type="com.premiersolutionshi.common.domain.TestBlog" indexId="testBlogIndex">
                  <tr>
                    <td><bean:write name="testBlog" property="id" /> </td>
                    <td><bean:write name="testBlog" property="title" /> </td>
                    <td><bean:write name="testBlog" property="body" /> </td>
                    <td>
                      <bean:write name="testBlog" property="lastUpdatedDate" />
                    </td>
                    <td>
                      <a href="/testBlog.do?action=edit&id=${testBlog.id}">Edit</a>
                    </td>
                  </tr>
                </logic:iterate>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<%@ include file="../layout/material-footer.jsp"%>
