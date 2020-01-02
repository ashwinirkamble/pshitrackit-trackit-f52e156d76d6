$(document).ready(function() {
});
function validateOrganization() {
  var form = document.organizationForm;
  if (!form) {
    alert("Warning. Form validation is not possible.");
    return false;
  }
  var nameInput = form.name;
  //var address1Input = form.address1;
  //var address2Input = form.address2;
  //var zipInput = form.zip;
  //var stateProvinceInput = form.stateProvince;
  //var countryInput = form.country;
  //var emailInput = form.email;
  //var urlInput = form.url;
  //var phoneInput = form.phone;
  //var primaryPocFkInput = form.primaryPocFk;

  var name = nameInput ? nameInput.value.trim() : "";
  //var address1 = address1Input ? address1Input.value.trim() : "";
  //var address2 = address2Input ? address2Input.value.trim() : "";
  //var zip = zipInput ? zipInput.value.trim() : "";
  //var stateProvince = stateProvinceInput ? stateProvinceInput.value.trim() : "";
  //var country = countryInput ? countryInput.value.trim() : "";
  //var email = emailInput ? emailInput.value.trim() : "";
  //var url = urlInput ? urlInput.value.trim() : "";
  //var phone = phoneInput ? phoneInput.value.trim() : "";
  //var primaryPocFk = primaryPocFkInput ? primaryPocFkInput.value.trim() : "";

  if (name === "") {
    alert("Name is required.");
    nameInput.focus();
    return false;
  }
  return true;
}

