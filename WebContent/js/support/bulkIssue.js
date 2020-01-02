
$(function() {
  var table = $('#configuredSystemTable').DataTable( {
    paging: false,
    searching: true,
    columnDefs: [
      { "orderable": false, "targets": [5,6,7] }
    ],
    stateSave: false
  });

  $("#openedBy").autocomplete({ source: openedByList });

  $(".datepicker").prop('autocomplete', 'off');
  $(".datepicker").datepicker();

  $('.comment-dialog-btn').on('click', function() {
    var closestTr = $(this).closest('tr')
    , issueTitle = closestTr.find('[name="title"]').prop('value')
    , content = closestTr.find('[name="allComments"]').prop('value')
    ;
    showInfoDialog(title, content);
    return false;
  });

  $('.email-monthly-dialog-btn').on('click', function() {
    var closestTr = $(this).closest('tr')
    , configuredSystemPk = closestTr.find('[name="configuredSystemPk"]').prop('value')
    , title = $('#title').val()
    , shipName = closestTr.find('[name="shipName"]').prop('value')
    , computerName = closestTr.find('[name="computerName"]').prop('value')
    , primaryPocEmails = closestTr.find('[name="primaryPocEmails"]').prop('value')
    , subject = title + ' for ' + computerName + " on the " + shipName
    , body = closestTr.find('[name="monthlyEmailMessage"]').prop('value')
    ;
    showEmailDialog(primaryPocEmails, subject, body);
    closestTr.find('[name="includeConfiguredSystemPkArr"]').prop('checked', 1);
    return false;
  });

  $('#category').on('change', function() {
    if ($(this).val() == 'Monthly E-Mail Notification') {
      table.column(5).visible(true);
      table.column(6).visible(false);
      $('#title').val('FACET Monthly Activity');
      $('#commentsInput').val('');
      $('.emailBody').hide();
    } else if ($(this).val() == 'Follow-Up Training') {
      table.column(5).visible(false);
      table.column(6).visible(true);
      $('#title').val('FACET Follow-Up Training');
      $('#commentsInput').val('ALCON,\n\nI will be in <location> from <date>-<date>. If you would like to schedule any follow-up training or support pertaining to the FACET system, including LS and CS training, please respond to reserve a timeslot (0900/1100/1300) along with the type of training you require.  Precedence will be given to those units that did not receive a support visit on a previous travel, after which it will be first-come, first-serve to those who respond to this e-mail.\n\nIf you\'re in-port and available during this timeframe, at a minimum, I will be stopping by to update your system and to upload any missing/outstanding Export Transmittal Files to DACS if time permits.  Thank you.\n\nV/r,\n\n'
        + fullName
        +'\nFACET Support Team\nPremier Solutions HI\n808-396-4444');
      $('.emailBody').show();
    } else if ($(this).val() == 'OS Update') {
      table.column(5).visible(false);
      table.column(6).visible(true);
      $('#title').val('OS System Update Release (' + currOsVersion + ')');
      $('#commentsInput').val('');
      $('.emailBody').hide();
    } else if ($(this).val() == 'FACET Update') {
      table.column(5).visible(false);
      table.column(6).visible(true);
      $('#title').val('FACET System Update Release (' + currFacetVersion + ')');
      $('#commentsInput').val('ALCON,\n\nThe latest FACET version has been released.  Please update your FACET system to ' + currFacetVersion 
          + ' to ensure you have the latest features for the FACET System.\n\nYou will soon receive a separate e-mail notification from '
          + 'AMRDEC to download the update files.  Alternatively, the files are also available on the DACS-FACET website on the black '
          + 'ribbon under the File Repository tab.  Please select the appropriate version based on your NCTSS RSupply Version (i.e. '
          + 'Viking/Patriot or CY04/Charger).\n\nPlease notify us when this is completed.  Feel free to contact us for any questions '
          + 'regarding this notice.\n\nV/r,\n\nFACET Support Team\nPremier Solutions HI\n808-396-4444');
      $('.emailBody').show();
    }
  }).change();

  $('#status').on('change', function() {
    if ($(this).val() == '3 - Resolved' || $(this).val() == '4 - Pending Possible Resolution') {
      $('.closedDateTd').hide();
      $('.autoCloseTd').show();
      $('#resolutionRow').show();
      if ($('#totalTime').val() == null || $('#totalTime').val() == '') $('#totalTime').val('15');
    } else if ($(this).val() == '5 - Closed' || $(this).val() == '6 - Closed (Successful)' || $(this).val() == '7 - Closed (No Response)' || $(this).val() == '8 - Closed (Unavailable)') {
      $('.closedDateTd').show();
      $('.autoCloseTd').hide();
      $('#resolutionRow').show();
      if ($('#totalTime').val() == null || $('#totalTime').val() == '') $('#totalTime').val('15');
      var d = new Date();
      if ($('#closedDate').val() == null || $('#closedDate').val() == '') $('#closedDate').val((d.getMonth()+1) + '/' + d.getDate() + '/' + d.getFullYear());
    } else {
      //Regular menu item
      $('.closedDateTd').hide();
      $('.autoCloseTd').show();
      $('#resolution').val(null);
      $('#totalTime').val(null);
      $('#resolutionRow').hide();
    }
  }).change();
});


//old way
//function sendEmailWithPrompt(addresses, subject, body) {
//  var mailToLink = 'mailto:' + addresses + '?subject=' + subject + '&body=' + formatToUrlFriendly(body);
//  //console.log("sendEmail mailToLink:" + mailToLink);
//  if (mailToLink.length < MAILTO_URL_MAX_SIZE) {
//    window.location.href = mailToLink;
//  }
//  else {
//    if (prompt('Warning: The e-mail exceeds the maximum length of a URL mailto link. ' +
//        + 'Please copy this email body to your clipboard and paste it in the opening e-mail draft.', body)) {
//      var bodyInput = document.getElementById('dialog-body');
//      if (bodyInput) {
//        bodyInput.select();
//        document.execCommand('copy');
//      }
//      sendEmail(addresses, subject, '<insert body text>');
//    }
//  }
//  return false;
//}


