$(document).ready(function () {
  document.shipForm.shipName.focus();
});

$(function() {
  $('#uicInput').on('change keyup paste', checkUicIsUnique);
});

function checkUicIsUnique() {
  var uic = $('#uicInput').val();
  var size = shipList.length;
  var content = "";
  for (var i = 0; i < size; i++) {
    var ship = shipList[i];
    if (uic.toLowerCase() === ship.uic.toLowerCase() && originalShipPk !== ship.id) {
      content += "<tr><td>" + ship.uic + "</td>"
      + "<td><a href='ship.do?action=shipEdit&shipPk=" + ship.id + "' target='_blank'>" + ship.shipName + "</a></td></tr>";
    }
  }
  var modalBodyElem = $("#duplicateUics > .modal-dialog > .modal-content > .modal-body");
  if (content != "") {
    modalBodyElem.html(
      "<div>Duplicate UICs are allowed, but not with the same Unit Name.</div>"
      + "<table class='pshi-table-small' style='width:100%'>"
      + "<thead><tr><th>UIC</th><th>Ship Name</th></tr></thead>"
      + "<tbody>" + content + "</tbody></table>");
    $('#duplicateUicButton').show();
  }
  else {
    modalBodyElem.html("");
    $('#duplicateUicButton').hide();
  }
}

function valFields() {
  var shipName = stripSpaces(document.shipForm.shipName.value.trim());
  var uic = stripSpaces(document.shipForm.uic.value.trim());
  var type = document.shipForm.type.value;
  var hull = stripSpaces(document.shipForm.hull.value.trim());
  var homeport = document.shipForm.homeport.value;
  var tycom = document.shipForm.tycom.value;
  var rsupply = document.shipForm.rsupply.value;
  document.shipForm.shipName.value = shipName;
  document.shipForm.uic.value = uic;
  document.shipForm.hull.value = hull;

  if (shipName.length < 1) {
    alert("Unit Name input is required.");
    document.shipForm.shipName.focus();
    return false;
  }
  var duplicateUnitName = false;
  console.log("checking here");
  for (var i = 0; i < shipList.length; i++) {
    var ship = shipList[i];
    console.log("ship.shipName=" + ship.shipName);
    if (ship.id !== originalShipPk && ship.shipName === shipName) {
      duplicateUnitName = true;
      break;
    }
  }
  console.log("duplicateUnitName=" + duplicateUnitName + ", shipName=" + shipName + ", uic=" + uic);
  if (duplicateUnitName) {
    alert("Duplicate Unit Names are not allowed. Another Unit with the same name already exists.");
    document.shipForm.shipName.focus();
    return false;
  }
  if (uic.length <= 0) {
    alert("UIC input is required.");
    document.shipForm.uic.focus();
    return false;
  }
  if (hull.length <= 1) {
    alert("Hull input is required.");
    document.shipForm.hull.focus();
    return false;
  }
  return true;
}

function checkNew(currObj, elementName) {
  if (currObj.value == '') {
    document.getElementsByName(elementName)[0].value = '';
    document.getElementsByName(elementName)[0].style.display = 'inline';
    document.getElementsByName(elementName)[0].focus();
  } else {
    document.getElementsByName(elementName)[0].value = currObj.value;
    document.getElementsByName(elementName)[0].style.display = 'none';
  }
}