
$(document).ready(function() {
});
function validateGovProperty() {
  var form = document.govPropertyForm;
  if (!form) {
    alert("Warning. Form validation is not possible.");
    return false;
  }
  var dateListedInput = form.dateListedStr;
  var nationalStockNumberInput = form.nationalStockNumber;
  var idNumberInput = form.idNumber;
  var descriptionInput = form.description;
  var projectContractInput = form.projectContract;
  var receivedInput = form.received;
  var issuedInput = form.issued;
  var transferredInput = form.transferred;
  var onHandInput = form.onHand;
  var locationInput = form.location;
  var dispositionInput = form.disposition;

  var dateListed = dateListedInput ? dateListedInput.value.trim() : "";
  var nationalStockNumber = nationalStockNumberInput ? nationalStockNumberInput.value.trim() : "";
  var idNumber = idNumberInput ? idNumberInput.value.trim() : "";
  var description = descriptionInput ? descriptionInput.value.trim() : "";
  var projectContract = projectContractInput ? projectContractInput.value.trim() : "";
  var received = receivedInput ? receivedInput.value.trim() : "";
  var issued = issuedInput ? issuedInput.value.trim() : "";
  var transferred = transferredInput ? transferredInput.value.trim() : "";
  var onHand = onHandInput ? onHandInput.value.trim() : "";
  var location = locationInput ? locationInput.value.trim() : "";
  var disposition = dispositionInput ? dispositionInput.value.trim() : "";

  if (dateListed === "") {
    alert("Date Listed is required. dateListed=" + dateListed);
    dateListedInput.focus();
    return false;
  }
  if (nationalStockNumber === "") {
    alert("National Stock Number is required.");
    nationalStockNumberInput.focus();
    return false;
  }
  return true;
}

