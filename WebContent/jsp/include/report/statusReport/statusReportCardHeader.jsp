<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<div class="card-header card-header-info card-header-icon">
	<div class="card-icon">
		<i class="material-icons">assignment</i>
	</div>
	<div style="display: inline-flex; float: left;">
		<h4 class="card-title">${pageTitle}</h4>
	</div>
	<div class="dropdown show"
		style="display: inline-flex; margin-top: 10px; float: right;">
		<a class="btn btn-secondary dropdown-toggle" href="#" role="button"
			style="margin: 0; color: black;" id="dropdownMenuLink"
			data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			<i class="material-icons">settings</i>
		</a>
		<div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
			<logic:notEmpty name="statusReportForm">
				<logic:notEmpty name="statusReportForm" property="id">
					<logic:equal name="action" value="form">
						<a class="dropdown-item"
							href="${path}?action=view&projectPk=${projectPk}&id=${statusReportForm.id}">
							<i class="material-icons">search</i> View Status Report
						</a>
					</logic:equal>
					<logic:equal name="action" value="view">
						<a class="dropdown-item"
							href="${path}?action=form&projectPk=${projectPk}&id=${statusReportForm.id}">
							<i class="material-icons">edit</i> Edit Status Report
						</a>
					</logic:equal>
					<logic:equal name="action" value="view">
						<a class="dropdown-item"
							href="${path}?action=${statusReportForm.statusReportType.ref}&projectPk=${projectPk}&id=${statusReportForm.id}">
							<i class="material-icons">assignment</i> Generate Report
						</a>
					</logic:equal>
					<a class="dropdown-item"
						onclick="return confirmDelete(${statusReportForm.id}, '${statusReportForm.projectName}');"
						href="${path}?action=delete&projectPk=${projectPk}&id=${statusReportForm.id}">
						<i class="material-icons">delete</i> Delete Status Report
					</a>
				</logic:notEmpty>
			</logic:notEmpty>
			<a class="dropdown-item"
				href="${path}?action=form&projectPk=${projectPk}"> <i
				class="material-icons">add</i> Add New Status Report
			</a>
		</div>
	</div>
</div>

