
$(function() {
  $('#selectAll').on('click', function() {
    var isChecked = $('#selectAll').is(':checked');
    $('[name="includeConfiguredSystemPkArr"]').each(function(key, value) {
      $(this).prop('checked', isChecked);
    });
  });

  $('.email-standard-dialog-btn').on('click', function() {
    emailConfiguredSystemPocs(this);
    $(this).closest('tr').find('[name="includeConfiguredSystemPkArr"]').attr('checked', true);
    return false;
  });

  $('#emailAllLink').on('click', function() {
    var addresses = '';
    var addressesMap = {};
    $('[name="includeConfiguredSystemPkArr"]').each(function(key, value) {
      var primaryPocEmails = $(this).closest('tr').find('[name="primaryPocEmails"]').prop('value').trim();
      if (value.checked && primaryPocEmails && primaryPocEmails != '' && primaryPocEmails != 'null') {
        //addresses += email + ';';
        var emailArr = primaryPocEmails.split(';');
        for (var i = 0; i < emailArr.length; i++) {
          addressesMap[emailArr[i].trim().toLowerCase()] = 1;
        }
      }
    });
    var subject = $('#title').val()
      , body = $('#commentsInput').val()
      , addresses = Object.keys(addressesMap).join(';');
    ;
    showEmailDialog(addresses, subject, body);
    return 0;
  });
});

function emailConfiguredSystemPocs(thisRef) {
  var closestTr = $(thisRef).closest('tr');
  var shipName = closestTr.find('[name="shipName"]').attr('value');
  var computerName = closestTr.find('[name="computerName"]').attr('value');
  var primaryPocEmails = closestTr.find('[name="primaryPocEmails"]').attr('value');
  var subject = $('#title').val() + ' for ' + computerName + " on the " + shipName;
  var body = $('#commentsInput').val();
  showEmailDialog(primaryPocEmails, subject, body);
  return false;
}