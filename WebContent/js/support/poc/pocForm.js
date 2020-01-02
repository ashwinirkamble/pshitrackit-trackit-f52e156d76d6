$(document).ready(function() {
});
function validatePoc() {
  var form = document.pocForm[1];
  if (!form) {
    alert("Warning. Form validation is not possible.");
    return false;
  }
  var pocTypeCodeInput = form.pocTypeCode;
  var organizationFkInput = form.organizationFk;
  var shipFkInput = form.shipFk;
  var lastNameInput = form.lastName;
  var firstNameInput = form.firstName;

  var pocTypeCode = pocTypeCodeInput ? pocTypeCodeInput.value.trim() : "";
  var shipFk = shipFkInput ? shipFkInput.value.trim() : "";
  var organizationFk = organizationFkInput ? organizationFkInput.value.trim() : "";
  var lastName = lastNameInput ? lastNameInput.value.trim() : "";
  var firstName = firstNameInput ? firstNameInput.value.trim() : "";

  if (pocTypeCode === '1' && organizationFk === "") {
    alert("Organization is required.");
    organizationFkInput.focus();
    return false;
  }
  if (pocTypeCode === '2' && shipFk === "") {
    alert("Unit is required.");
    if (pocTypeCodeInput) {
      pocTypeCodeInput.focus();
    }
    return false;
  }
  if (lastName === "") {
    alert("Last Name is required.");
    if (lastNameInput) {
      lastNameInput.focus();
    }
    return false;
  }
  if (firstName === "") {
    alert("First Name is required.");
    firstNameInput.focus();
    return false;
  }
  return true;
}

function confirmDeletePoc(id, name) {
  name = name || '';
  return confirm("Are you sure you want to delete POC #" + id + (name === '' ? '' : " (" + name + ")") + "?");
}

