var BASE_TITLE = 'FACET Security Update - ATO_';

$(function() {
  $(".datepicker").attr('autocomplete', 'off');
  $(".datepicker").datepicker();

  var table = $('#completedTable').DataTable({
    paging : false,
    searching : false,
    stateSave : false
  });

  var table = $('#inProgressTable').DataTable({
    paging : false,
    searching : false,
    columnDefs : [{
      "orderable" : false,
      "targets" : [3, 4]
    }],
    stateSave : false
  });

  var table = $('#availableTable').DataTable({
    paging : false,
    searching : false,
    columnDefs : [{
      "orderable" : false,
      "targets" : [3, 4]
    }],
    stateSave : false
  });

  $("#atoDateStr").on('change', function() {
    $('#title').val(BASE_TITLE + getElemDateFormatted('atoDateStr'));
  });
  $('#title').val(BASE_TITLE + getElemDateFormatted('atoDateStr'));

  $('.reminderEmailLink').on('click', function() {
    emailConfiguredSystemPocs(this);
    $(this).closest('tr').find('[name="reminderIssuePkArr"]').attr('checked', true);
    return false;
  });
});

function getElemDateFormatted(elemId) {
  var elem = document.getElementById(elemId);
  var dateStr = 'DATE NOT SET';
  if (elem) {
    var d = new Date(elem.value);
    dateStr = d ? d.getFullYear() + padLeft((d.getMonth() + 1), 2) + padLeft(d.getDate(), 2) : dateStr;
  }
  return dateStr;
}


function valFields() {
  var atoDate = stripSpaces(document.atoUpdateForm.atoDateStr.value);
  var openedDate = stripSpaces(document.atoUpdateForm.openedDateStr.value);
  document.atoUpdateForm.atoDateStr.value = atoDate;
  document.atoUpdateForm.openedDateStr.value = openedDate;
  if (atoDate.length < 1) {
    alert('You must enter in an ATO date.');
  document.atoUpdateForm.atoDateStr.focus();
  return false;
}
if (openedDate.length < 1) {
  alert('You must enter in an opened date.');
    document.atoUpdateForm.openedDateStr.focus();
    return false;
  }
  return true;
}
