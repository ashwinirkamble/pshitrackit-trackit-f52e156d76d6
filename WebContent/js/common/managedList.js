$(function() {
  $('input').attr('autocomplete', 'off');

  saveSortOrder = function(e, ui) {
    var arr = [];

    //sortPk
    $('input', ui.item.parent()).each(function (i) {
      arr.push($(this).attr('value'));
    });

    var pkListStr = arr.join(',');

    $('#sort-status').attr('src', 'images/loading.gif').show();

    $.ajax({
      url: 'managedList.do',
      type: 'POST',
      data: {action: 'sortOrder', pkList: pkListStr},
      dataType: 'json',
      success: function(data) {
        if (data.result) {
          $('#sort-status').attr('src', 'images/icon_success.png');
        } else {
          $('#sort-status').attr('src', 'images/icon_error.png');
        } //end of else
      },
      error: function(e){
        $('#sort-status').attr('src', 'images/icon_warning.png');
      }
    });
  };

  $('.sortable tfoot').sortable({
    placeholder: 'ui-state-highlight',
    cancel: '.ui-state-disabled',
    stop: saveSortOrder
  }).disableSelection();
});

function confirmDelete(inputName) {
  return confirm("Are you sure you want to delete \"" + inputName + "\"?");
}
