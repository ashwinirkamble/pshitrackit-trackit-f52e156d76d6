$(document).ready(function() {
});

$(function() {
  var lastUpdatedDateCol = 3;
  var actionCol = 4;
  var table = $(".contractTable").DataTable({
    paging : false,
    searching : false,
    //stateSave : false
    columnDefs : [
      { type: "date", targets: lastUpdatedDateCol },
      { orderable: false, targets: actionCol },
    ],
  });
});
function validateContract() {
  var form = document.contractForm;
  if (!form) {
    alert("Warning. Form validation is not possible.");
    return false;
  }
  var contractNumberInput = form.contractNumber;

  var contractNumber = contractNumberInput ? contractNumberInput.value.trim() : "";

  if (contractNumber === "") {
    alert("Contract Number is required.");
    contractNumberInput.focus();
    return false;
  }
  return true;
}

