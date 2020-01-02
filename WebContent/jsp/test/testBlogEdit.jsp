<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods"%>
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
<jsp:useBean id="domain" scope="request" class="com.premiersolutionshi.common.domain.TestBlog" />

<%@ include file="../layout/material-header.jsp"%>

<div class="container-fluid">
  <div class="row">
    <div class="col-lg-12">
      <nav aria-label="breadcrumb">
        <ol class="breadcrumb" style="margin-bottom: 0;">
          <li class="breadcrumb-item"><a href="/menu.do">Home</a></li>
          <li class="breadcrumb-item"><a href="/testBlog.do?action=list">Test Blog</a></li>
          <li class="breadcrumb-item active" aria-current="page">
          ${empty domain.id ? 'Add' : 'Edit'} Test Blog
          </li>
        </ol>
      </nav>
    </div>

    <div class="col-md-12">
      <div class="card" style="margin-top:10px;">
        <div class="card-body">
          ${empty domain.id ? 'Add' : 'Edit'} Test Blog
          <div class="row">
            <div class="col-lg-6">
              <html:form action="testBlog.do?action=save" onsubmit="return true;" method="POST">
                <html:hidden name="domain" property="id"  />
                <div class="form-group">
                  <label>Title</label>
                  <html:text name="domain" property="title" styleClass="form-control" size="65" maxlength="255"/>
                  <p class="help-block">Title of the blog.</p>
                </div>
                <div class="form-group">
                  <label>Body</label>
                  <html:text name="domain" property="body" styleClass="form-control" size="65" maxlength="255"/>
                  <p class="help-block">Body of the blog.</p>
                </div>
                <html:submit value="Save"/>
              </html:form>
            </div>
            <!-- /.col-lg-6 (nested) -->
          </div>
          <!-- /.row -->
        </div>
        <!-- /.panel-body -->
      </div>
      <!-- /.panel-default -->
    </div>
    <!-- /.panel-default -->
  </div>
  <!-- /.row -->
</div>
<%@ include file="../layout/material-footer.jsp"%>

