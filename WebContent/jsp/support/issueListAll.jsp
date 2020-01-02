<!DOCTYPE html>
<%@ page language="java" %>
<%@ page import="com.premiersolutionshi.old.util.CommonMethods" %>

<%@ taglib prefix="bean"  uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html"  uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<bean:define id="defaultPageTitle" value="Issue List (All)"/>
<jsp:useBean id="customPageTitle" scope="request" class="java.lang.String"/><bean:define id="pageTitle" name="customPageTitle"/><logic:equal name="pageTitle" value=""><bean:define id="pageTitle" name="defaultPageTitle"/></logic:equal>
<jsp:useBean id="loginBean" scope="request" class="com.premiersolutionshi.old.bean.LoginBean"/>

<jsp:useBean id="projectPk" scope="request" class="java.lang.String"/>
<jsp:useBean id="inputBean" scope="request" class="com.premiersolutionshi.old.bean.SupportBean"/>

<jsp:useBean id="resultList" scope="request" class="java.util.ArrayList"/>

<jsp:useBean id="sortBy"  scope="request" class="java.lang.String"/>
<jsp:useBean id="sortDir" scope="request" class="java.lang.String"/>

<jsp:useBean id="pageFrom" scope="request" class="java.lang.String"/>

<%
	int resultCnt = CommonMethods.cInt(request.getAttribute("resultCnt"));
	int currPage = CommonMethods.cInt(request.getAttribute("page"));
	int lastPage = CommonMethods.cInt(request.getAttribute("lastPage"));

	int navFirstPage = -1;
	int navLastPage = -1;

	java.text.NumberFormat nf = new java.text.DecimalFormat("###,###,###,##0");

	if (currPage <= 10) {
		navFirstPage = 1;
		navLastPage = Math.min(20, lastPage);
	} else if (currPage >= lastPage-10) {
		navFirstPage = Math.max(1, lastPage-20);
		navLastPage = lastPage;
	} else {
		navFirstPage = currPage - 10;
		navLastPage = currPage + 10;
	} //end of else
%>

<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">

  <title>TrackIT - <bean:write name="pageTitle"/></title>

	<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="css/stylesheet.css"/>

	<style>
		input[type="radio"], input[type="checkbox"] {
			margin-top: -1px;
			vertical-align: middle;
			}
		.panel-heading:hover {
			cursor: pointer;
			}
	</style>
</head>

<body>
<%@ include file="../include/app-header.jsp" %>

<div class="colmask leftmenu"><div class="colright">
	<div class="col1wrap"><div class="col1 col1bootstrap">
		<%@ include file="../include/content-header.jsp" %>

    <tags:projectPkBreadcrumb projectPk="${projectPk}" pageTitle="${pageTitle}" hideOptions="true" />

		<% if (pageFrom.equals("issueListAll")) { %>
		<div id="searchForm" class="panel panel-primary">
			<div class="panel-heading" data-toggle="collapse" data-target="#search-box">
				<h3 class="panel-title">
				<a class="accordion-toggle">Search Criterias</a>
				<i class="indicator glyphicon glyphicon-chevron-down pull-right"></i>
				</h3>
			</div>

			<div class="panel-collapse in" id="search-box">
			<div class="panel-body">
			<form action="support.do" method="GET">
				<input type="hidden" name="action" value="issueListAll"/>
				<input type="hidden" name="projectPk" value="<bean:write name="projectPk"/>"/>
				<input type="hidden" name="sortBy" value="issue_pk"/>
				<input type="hidden" name="sortDir" value="DESC"/>
				<input type="hidden" name="page" value="1"/>
				<input type="hidden" name="searchPerformed" value="Y"/>

				<center>
				<table border="0" id="bootstrapFormTable">
				<colgroup>
					<col width="120"/>
					<col width="220"/>
					<col width="70"/>
					<col width="50"/>
					<col width="170"/>
				</colgroup>
				<tbody>
					<tr>
						<td class="fieldName"><label for="title">Title/Summary</label></td>
						<td colspan="4"><html:text name="inputBean" property="title" styleId="title" styleClass="form-control input-sm"/></td>
					</tr>
					<tr>
						<td class="fieldName"><label for="status">Status</label></td>
						<td>
							<html:select name="inputBean" property="status" styleId="status" styleClass="form-control input-sm">
								<html:option value="">View All</html:option>
								<html:option value="1 - New"/>
								<html:option value="2 - Active"/>
								<html:option value="3 - Resolved"/>
								<html:option value="4 - Pending Possible Resolution"/>
								<html:option value="5 - Closed"/>
								<html:option value="6 - Closed (Successful)"/>
								<html:option value="7 - Closed (No Response)"/>
								<html:option value="8 - Closed (Unavailable)"/>
							</html:select>
						</td>
						<td class="fieldName"><label for="category">Category</label></td>
						<td colspan="2">
							<html:select name="inputBean" property="issueCategoryFk" styleId="category" styleClass="form-control input-sm">
								<html:option value="">View All</html:option>
								<logic:present name="categoryList"><html:options collection="categoryList" property="key" labelProperty="value"/></logic:present>
							</html:select>
						</td>
					</tr>
					<tr>
						<td class="fieldName"><label for="uic">Unit</label></td>
						<td colspan="4">
							<html:select name="inputBean" property="uic" styleId="uic" styleClass="form-control input-sm">
								<html:option value="">View All</html:option>
								<html:options collection="configuredSystemList" property="uic" labelProperty="shipName"/>
							</html:select>
						</td>
					</tr>
					<tr>
						<td class="fieldName"><label for="dept">Division</label></td>
						<td align="left" colspan="2">
              <html:select name="inputBean" property="dept" styleClass="form-control">
                <html:option value="">View All</html:option>
                <html:options collection="divisionList" property="value"/>
              </html:select>
						</td>
						<td class="fieldName"><label for="priority">Priority</label></td>
						<td align="left">
							<label class="radio-inline">
								<html:radio name="inputBean" property="priority" value=""/>
								Any
							</label>
							<label class="radio-inline" style="color:#d00;font-weight:bold;">
								<html:radio name="inputBean" property="priority" value="High"/>
								High Priority
							</label>
						</td>
					</tr>
					<tr>
						<td class="fieldName"><label for="comments">Internal Comments</label></td>
						<td colspan="4"><html:text name="inputBean" property="comments" styleId="comments" styleClass="form-control input-sm"/></td>
					</tr>
				</tbody>
				</table>
				</center>

				<div class="form-group">
					<div class="col-sm-12 center CENTERED">
            <span>
  						<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span> Search</button>
  						<a href="support.do?action=issueListAll&projectPk=<%= projectPk %>&searchPerformed=Y&page=1" class="btn btn-default"><span class="glyphicon glyphicon-list-alt"></span> View All</a>
            </span>
					</div>
				</div>
			</form>
			</div>
			</div>
		</div>
		<% } %>

		<% if (resultList.size() == 0) { %>
			<p class="error" align="center">
			No Issues Found
			</p>
		<% } %>

		<% if (resultList.size() > 0) { %>
			<logic:notEqual name="inputBean" property="pagination" value="true">
			<p align="center">
				<%= nf.format(resultList.size()) %> issues found
			</p>
			</logic:notEqual>

			<p>
			<table width="100%">
			<tbody>
				<tr>
					<td align="left" width="33%">
            <%@ include file="../include/support/issue/jumpToIssuePk.jsp" %>
					</td>

					<td align="center" width="34%">
						<a href="export.do?action=<bean:write name="pageFrom"/>Xlsx&projectPk=<%= projectPk %>" class="btn btn-default"><img src="images/file_icons/sml_file_xls.gif"/> Export to Excel</a>
						<a href="issue.do?action=add&projectPk=<bean:write name="projectPk"/>&pageFrom=<bean:write name="pageFrom"/>" class="btn btn-success"><span class="glyphicon glyphicon-plus"></span> Add New Support Issue</a>
					</td>

					<td width="33%">
						&nbsp;
					</td>
				</tr>
			</tbody>
			</table>
			</p>


			<logic:equal name="inputBean" property="pagination" value="true">
				<nav>
				<ul class="pagination">
					<% if (navFirstPage > 1) { %>
					<li>
						<a href="support.do?action=issueListAll&projectPk=<%= projectPk %>&page=<%= navFirstPage - 1 %>" aria-label="Previous">
							<span aria-hidden="true">&laquo;</span>
						</a>
					</li>
					<% } %>

					<% for (int i = navFirstPage; i <= navLastPage; i++) { %>
						<% if (i == currPage) { %>
							<li class="active"><a href="#"><%= i %> <span class="sr-only">(current)</span></a></li>
						<% } else { %>
							<li><a href="support.do?action=issueListAll&projectPk=<%= projectPk %>&page=<%= i %>"><%= i %></a></li>
						<% } %>
					<% } %>

					<% if (lastPage > navLastPage) { %>
						<li>
							<a href="support.do?action=issueListAll&projectPk=<%= projectPk %>&page=<%= navLastPage + 1 %>" aria-label="Next">
								<span aria-hidden="true">&raquo;</span>
							</a>
						</li>
					<% } %>
				</ul>
				</nav>

				<nav>
				<ul class="pager">
					<% if (currPage == 1) { %>
						<li class="previous disabled"><a href="#"><span aria-hidden="true">&larr;</span> Previous</a></li>
					<% } else { %>
						<li class="previous"><a href="support.do?action=issueListAll&projectPk=<%= projectPk %>&page=<%= currPage - 1 %>"><span aria-hidden="true">&larr;</span> Previous</a></li>
					<% } %>

					<li><%= nf.format(resultCnt) %> issues found</li>

					<% if (currPage >= lastPage) { %>
						<li class="next disabled"><a href="#">Next <span aria-hidden="true">&rarr;</span></a></li>
					<% } else { %>
						<li class="next"><a href="support.do?action=issueListAll&projectPk=<%= projectPk %>&page=<%= currPage + 1 %>">Next <span aria-hidden="true">&rarr;</span></a></li>
					<% } %>
				</ul>
				</nav>
			</logic:equal>

			<p>
			<table id="dataTable_style2" border="0" cellspacing="0" class="alt-color" width="100%">
			<thead>
				<tr>
					<th nowrap style="text-align:center;">
						<a href="javascript: changeSort('issue_pk', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">#</a>
						<% if (sortBy.equals("issue_pk") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("issue_pk") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th nowrap>Title/Summary</th>
					<th nowrap>
						<a href="javascript: changeSort('status', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Status</a>
						<% if (sortBy.equals("status") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("status") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th nowrap>
						<a href="javascript: changeSort('ship_name', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Unit Name</a>
						<% if (sortBy.equals("ship_name") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("ship_name") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th nowrap>
						<a href="javascript: changeSort('category', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Category</a>
						<% if (sortBy.equals("category") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("category") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th nowrap style="text-align:center;">
						<a href="javascript: changeSort('opened_date', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Opened Date</a>
						<% if (sortBy.equals("opened_date") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("opened_date") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th nowrap>
						<a href="javascript: changeSort('person_assigned', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Person Assigned</a>
						<% if (sortBy.equals("person_assigned") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("person_assigned") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th nowrap>
						<a href="javascript: changeSort('last_updated_date', '<bean:write name="sortBy"/>', '<bean:write name="sortDir"/>');">Last Updated</a>
						<% if (sortBy.equals("last_updated_date") && sortDir.equals("ASC")) { %><img src="images/arrow_blue_down.gif"/><% } %>
						<% if (sortBy.equals("last_updated_date") && sortDir.equals("DESC")) { %><img src="images/arrow_blue_up.gif"/><% } %>
					</th>
					<th style="text-align:center;">Action</th>
				</tr>
			</thead>
			<tbody>
			<logic:iterate id="resultBean" name="resultList" type="com.premiersolutionshi.old.bean.SupportBean">
				<% if (CommonMethods.nes(resultBean.getPriority()).equals("High")) { %>
					<tr style="vertical-align:top;" align="left" class="highpriority">
				<% } else { %>
					<tr style="vertical-align:top;" align="left">
				<% } %>
					<td align="center"><a href="issue.do?id=<bean:write name="resultBean" property="issuePk"/>&projectPk=<bean:write name="projectPk"/>&pageFrom=<bean:write name="pageFrom"/>"><bean:write name="resultBean" property="issuePk"/></a></td>
					<td width="250">
						<a href="issue.do?id=<bean:write name="resultBean" property="issuePk"/>&projectPk=<bean:write name="projectPk"/>&pageFrom=<bean:write name="pageFrom"/>"><b>Issue #<bean:write name="resultBean" property="issuePk"/></b></a>
						<% if (CommonMethods.nes(resultBean.getPriority()).equals("High")) { %>
							&nbsp;<span style="color:#f00;font-weight:bold;">HIGH PRIORITY</span>
						<% } %>
						<br/>
						<bean:write name="resultBean" property="title"/>
					</td>
					<td style="<bean:write name="resultBean" property="statusCss"/>" width="175">
						<bean:write name="resultBean" property="status"/><br/>
						<i><bean:write name="resultBean" property="resolution"/></i>
					</td>
					<td title="Unit Name">
						<bean:write name="resultBean" property="shipName"/><br/>
						<span style="color:#777;font-style:italic;"><bean:write name="resultBean" property="homeport"/></span>
					</td>
					<td><bean:write name="resultBean" property="category"/></td>
					<td align="center"><bean:write name="resultBean" property="openedDate"/></td>
					<td><bean:write name="resultBean" property="personAssigned"/></td>
					<td style="font-style:italic;">
						<bean:write name="resultBean" property="lastUpdatedDate"/>
						<logic:notEmpty name="resultBean" property="lastUpdatedBy">
						<br/>by <bean:write name="resultBean" property="lastUpdatedBy"/>
						</logic:notEmpty>
					</td>
					<td nowrap align="center">
						<a href="issue.do?action=edit&id=<bean:write name="resultBean" property="issuePk"/>&projectPk=<bean:write name="projectPk"/>&pageFrom=<bean:write name="pageFrom"/>"><img src="images/icon_edit.png" title="Edit Issue #<bean:write name="resultBean" property="issuePk"/>"/></a>
						&nbsp;<a href="issue.do?action=copy&id=<bean:write name="resultBean" property="issuePk"/>&projectPk=<bean:write name="projectPk"/>&pageFrom=<bean:write name="pageFrom"/>"><img src="images/icon_copy.png" title="Copy Issue #<bean:write name="resultBean" property="issuePk"/>"/></a>
						&nbsp;<a href="support.do?action=issueDeleteDo&issuePk=<bean:write name="resultBean" property="issuePk"/>&projectPk=<bean:write name="projectPk"/>&pageFrom=<bean:write name="pageFrom"/>" onclick="return confirmDelete('<bean:write name="resultBean" property="issuePk"/>');"><img src="images/icon_delete.png" title="Delete Issue #<bean:write name="resultBean" property="issuePk"/>"/></a>
					</td>
				</tr>
			</logic:iterate>
			</tbody>
			</table>
			</p>

			<logic:equal name="inputBean" property="pagination" value="true">
				<nav>
				<ul class="pager">
					<% if (currPage == 1) { %>
						<li class="previous disabled"><a href="#"><span aria-hidden="true">&larr;</span> Previous</a></li>
					<% } else { %>
						<li class="previous"><a href="support.do?action=<bean:write name="pageFrom"/>&projectPk=<%= projectPk %>&page=<%= currPage - 1 %>"><span aria-hidden="true">&larr;</span> Previous</a></li>
					<% } %>

					<li><%= nf.format(resultCnt) %> results found</li>

					<% if (currPage >= lastPage) { %>
						<li class="next disabled"><a href="#">Next <span aria-hidden="true">&rarr;</span></a></li>
					<% } else { %>
						<li class="next"><a href="support.do?action=<bean:write name="pageFrom"/>&projectPk=<%= projectPk %>&page=<%= currPage + 1 %>">Next <span aria-hidden="true">&rarr;</span></a></li>
					<% } %>
				</ul>
				</nav>

				<nav>
				<ul class="pagination">
					<% if (navFirstPage > 1) { %>
					<li>
						<a href="support.do?action=<bean:write name="pageFrom"/>&projectPk=<%= projectPk %>&page=<%= navFirstPage - 1 %>" aria-label="Previous">
							<span aria-hidden="true">&laquo;</span>
						</a>
					</li>
					<% } %>

					<% for (int i = navFirstPage; i <= navLastPage; i++) { %>
						<% if (i == currPage) { %>
							<li class="active"><a href="#"><%= i %> <span class="sr-only">(current)</span></a></li>
						<% } else { %>
							<li><a href="support.do?action=<bean:write name="pageFrom"/>&projectPk=<%= projectPk %>&page=<%= i %>"><%= i %></a></li>
						<% } %>
					<% } %>

					<% if (lastPage > navLastPage) { %>
						<li>
							<a href="support.do?action=<bean:write name="pageFrom"/>&projectPk=<%= projectPk %>&page=<%= navLastPage + 1 %>" aria-label="Next">
								<span aria-hidden="true">&raquo;</span>
							</a>
						</li>
					<% } %>
				</ul>
				</nav>
			</logic:equal>

			<div>
			<a href="export.do?action=<bean:write name="pageFrom"/>Xlsx&projectPk=<%= projectPk %>" class="btn btn-default"><img src="images/file_icons/sml_file_xls.gif"/> Export to Excel</a>
			</div>
		<% } %>


		<form name="sortForm" action="support.do" method="GET">
			<input type="hidden" name="action" value="<bean:write name="pageFrom"/>"/>
			<input type="hidden" name="projectPk" value="<bean:write name="projectPk"/>"/>
			<html:hidden name="inputBean" property="title"/>
			<html:hidden name="inputBean" property="status"/>
			<html:hidden name="inputBean" property="issueCategoryFk"/>
			<html:hidden name="inputBean" property="uic"/>
			<html:hidden name="inputBean" property="dept"/>
			<html:hidden name="inputBean" property="priority"/>
			<html:hidden name="inputBean" property="comments"/>
			<input type="hidden" name="sortBy" value="<bean:write name="sortBy"/>"/>
			<input type="hidden" name="sortDir" value="<bean:write name="sortDir"/>"/>
			<input type="hidden" name="page" value="1"/>
			<input type="hidden" name="searchPerformed" value="Y"/>
		</form>
	</div></div>

	<%@ include file="../include/app-col2.jsp" %>
</div></div>

<jsp:include page="../include/app-footer.jsp"/>

<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/jquery-altcolor.js"></script>

<script type="text/javascript">
	function toggleChevron(e) {
		$(e.target)
			.prev('.panel-heading')
			.find('i.indicator')
			.toggleClass('glyphicon-chevron-down glyphicon-chevron-left');
	}

	$('#searchForm').on('hidden.bs.collapse', toggleChevron);
	$('#searchForm').on('shown.bs.collapse', toggleChevron);

	function confirmDelete(issuePk) {
		return confirm("Are you sure you want to delete issue #" + issuePk);
	} //end of confirmDelete
</script>

</body>
</html>
