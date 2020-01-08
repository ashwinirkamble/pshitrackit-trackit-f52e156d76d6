<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>
<%@ page import="java.util.ArrayList" %>


<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="pageTitle" value="Home"/>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>
<jsp:useBean id="userBean" scope="request" class="com.premiersolutionshi.old.bean.UserBean"/>



<%@ include file="../layout/old/layout-header.jsp" %>

<div class="card">
 <div class="card-body">
    <div class="row">
      <div class="col-6">
        <h3>Projects</h3>
        <ul class="pshi-menu-list">
        <logic:iterate id="leftbar_projectBean" name="leftbar_projectList" type="com.premiersolutionshi.old.bean.ProjectBean">
          <logic:iterate id="leftbar_resultBean" name="leftbar_projectBean" property="taskList" type="com.premiersolutionshi.old.bean.ProjectBean">
            <bean:define id="projectPk" value="${leftbar_resultBean.projectPk}"/>
 	              <li class="pshi-menu-list-item">
              <a class="nav-link" href="page.do?projectPk=${leftbar_resultBean.projectPk}">
                <i class="material-icons">aspect_ratio</i>
                <span>${leftbar_resultBean.projectName}</span>
              </a>
            </li>
             </logic:iterate>
        </logic:iterate>
        
         <h3>Other Links</h3>
        <li class="pshi-menu-list-item">
            <a class="nav-link" href="govProperty.do">
              <i class="material-icons">list</i>
              <span>Government Property</span>
            </a>
          </li>
            <li class="pshi-menu-list-item">
            <a class="nav-link" href="user.do?action=ptoTravelList">
              <i class="material-icons">calendar_today</i>
              <span>Manage PTO/Travel</span>
            </a>
          </li>
        </ul>
      </div>
      <div class="col-5" class="panel panel-default" id="box1">
       <%--added code by Ashwini 21 nov 2019 --%>
        
     <logic:iterate id="leftbar_projectBean" name="leftbar_projectList" type="com.premiersolutionshi.old.bean.ProjectBean">
     <logic:iterate id="leftbar_resultBean" name="leftbar_projectBean" property="taskList" type="com.premiersolutionshi.old.bean.ProjectBean">          <bean:define id="projectPk" value="${leftbar_resultBean.projectPk}"/>
      <logic:equal name="leftbar_resultBean" property="projectPk" value="1">
            <h3>${leftbar_resultBean.projectName} Project</h3>
		<table>
		<tbody>
		<tr>
		<td>
		  <%@ include file="../include/support/issue/jumpToIssuePk.jsp" %>
		</td>
		<td>
		 <a href="issue.do?action=add&projectPk=${leftbar_resultBean.projectPk}&pageFrom=issueList" class="btn btn-success">
          <i class="material-icons">add</i>Add Issue</a><br/>
         </td>
         </tr>
		
		<tr>
		<td>
		    <%@ include file="../include/support/task/jumpToTaskPk.jsp" %>
        </td>
        <td>
            <a href="project.do?action=taskAdd&projectPk=${leftbar_resultBean.projectPk}" class="btn btn-success">
            <i class="material-icons">add</i>Add Task</a>
        </td>
		</tr>
		<tr>
		<td>
		   <%@ include file="../include/support/poc/pocSearchBox.jsp" %>
         </td>
		</tr>
		</tbody>
		</table>
		 </logic:equal>
            </logic:iterate>
            </logic:iterate>
         </div>
          </div>
   
          <%--added code for Current PTO/Travel by Ashwini 21 nov 2019 --%>
          
       <div class=row>
       <div class=col-10>
        <ul class="pshi-menu-list">
      	<li class="pshi-menu-list-item">
        <a>
        <i class="material-icons">calendar_today</i>
        
        <span>Current PTO/Travel</span>
        </a>
      </li>
      </ul>
      <% 
       int value = (int)request.getAttribute("currentPto_travelListSize");
      //System.out.println("size="+value);
     if (value == 0 ) 
     { %>
   
       <p class="error" align="center">
        No Events Found
        </p>
         <% }
     else { %>
        
      <p align="center">
 
      <table id="dataTable_style1" >
        <thead>
        <tr align="center">
          <th>User</th>
          <th>Begin</th>
          <th>End</th>
          <th>Type</th>
        </tr>
      </thead>
    
     <logic:iterate id="currentpto" name="currentPto_travelList" type="com.premiersolutionshi.old.bean.UserBean">
         <logic:iterate id="pto" name="currentpto" property="list" type="com.premiersolutionshi.old.bean.UserBean">         
     
       <p align="center">
       
      <tbody>
       <td  align="center">
           <bean:write name="pto" property="firstName"/>
           <bean:write name="pto" property="lastName"/>
          </td>
          <td align="center"><bean:write name="pto" property="startDate"/></td>
          <td align="center"><bean:write name="pto" property="endDate"/></td>
          <td align="center"><bean:write name="pto" property="leaveType"/></td>
          
        </tr>
      </logic:iterate>
            </logic:iterate>
     
      </tbody>
      </table>
      </p>
     
        <% } %>       
     
   </div>
   </div>
  </div>
</div>

   
<%@ include file="../layout/old/layout-footer.jsp" %>
</body>
</html>
