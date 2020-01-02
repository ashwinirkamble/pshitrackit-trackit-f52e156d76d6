$(document).ready(function() {
  //load summary into the editor
  var form = document.statusReportForm;
  if (form) {
    //TODO? verify if all inputs exist

    var summaryInput = form.summary;
    var editorInput = document.getElementById("summaryEditor");
    var editorContent = editorInput ? editorInput.firstChild : null;
    if (editorContent) {
      editorContent.innerHTML = summaryInput.value;
    }
  }
});

$(function() {
  var actionCol = 4;
  var table = $(".statusReportTable").DataTable({
    paging : false,
    searching : false,
    //stateSave : false
    columnDefs : [
      //{ type: "date", targets: lastUpdatedDateCol },
      { orderable: false, targets: actionCol },
    ],
  });
});
function validateStatusReport() {
  var form = document.statusReportForm;
  if (!form) {
    alert("Warning. Form validation is not possible.");
    return false;
  }
  var projectNameInput = form.projectName;
  var statusReportTypeCodeInput = form.statusReportTypeCode;
  var contractFkInput = form.contractFk;
  var reportStartDateInput = form.reportStartDateStr;
  var reportEndDateInput = form.reportEndDateStr;
  var organizationFkInput = form.organizationFk;
  var contractorOrgFkInput = form.contractorOrgFk;

  var projectName = projectNameInput ? projectNameInput.value.trim() : "";
  var statusReportTypeCode = statusReportTypeCodeInput ? statusReportTypeCodeInput.value.trim() : "";
  var contractFk = contractFkInput ? contractFkInput.value.trim() : "";
  var reportStartDate = reportStartDateInput ? reportStartDateInput.value.trim() : "";
  var reportEndDate = reportEndDateInput ? reportEndDateInput.value.trim() : "";
  var organizationFk = organizationFkInput ? organizationFkInput.value.trim() : "";
  var contractorOrgFk = contractorOrgFkInput ? contractorOrgFkInput.value.trim() : "";

  if (projectName === "") {
    alert("Project Name is required.");
    if (projectNameInput) {
      projectNameInput.focus();
    }
    return false;
  }
  if (statusReportTypeCode === "") {
    alert("Report Type is required.");
    if (statusReportTypeCodeInput) {
      statusReportTypeCodeInput.focus();
    }
    return false;
  }
  if (contractFk === "") {
    alert("Contract is required.");
    if (contractFkInput) {
      contractFkInput.focus();
    }
    return false;
  }
  if (reportStartDate === "") {
    alert("Report Start Date is required.");
    if (reportStartDateInput) {
      reportStartDateInput.focus();
    }
    return false;
  }
  if (reportEndDate === "") {
    alert("Report End Date is required.");
    if (reportEndDateInput) {
      reportEndDateInput.focus();
    }
    return false;
  }
  if (organizationFk === "") {
    alert("Organization is required.");
    if (organizationFkInput) {
      organizationFkInput.focus();
    }
    return false;
  }
  if (contractorOrgFk === "") {
    alert("Contractor is required.");
    if (contractorOrgFkInput) {
      contractorOrgFkInput.focus();
    }
    return false;
  }
  return true;
}
function onSaveStatusReport() {
  var form = document.statusReportForm;
  var summaryInput = form.summary;
  var editorInput = document.getElementById("summaryEditor");
  var editorContent = editorInput ? editorInput.firstChild : null;
  var summary = editorContent ? editorContent.innerHTML.trim() : "";
  if (summaryInput) {
    summaryInput.value = summary;
  }
  else {
    return false;
  }
  return true;
}
