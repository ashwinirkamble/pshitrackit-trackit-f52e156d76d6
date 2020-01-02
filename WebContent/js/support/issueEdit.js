$(function() {
  $("#openedBy").autocomplete({
    source: openedByList
  });

  $('[name="supportVisitLoc"]').autocomplete({
    source: homeportList
  });

  $(".datepicker").attr('autocomplete', 'off');
  $(".datepicker").datepicker();

  $('#shipPk').on('change', function() {
    shipPk = $(this).val();
    if (shipPk != '') {
      $('.support-visit-section').show();
      //$.ajax({
      //  type: 'POST',
      //  url: 'ajax.do?action=shipComputerName&projectPk=' + projectPk + '&shipPk=' + $(this).val(),
      //  async: false
      //}).responseText;
      //handle changing configured systems select dropdown.
      var csList = shipFkToCsListMap[shipPk];
      if (csList) {
        var $dropdown = $("#configuredSystemFk");
        //clear all options and add blank option
        $dropdown.find('option').remove().end().append('<option value=""></option>');
        //populate with configured systems
        $.each(csList, function() {
          $dropdown.append($("<option />").val(this.id).text(this.computerName));
        });
        $('#configuredSystemFk > option').eq(configuredSystemFk).prop('selected', true);
        $('#configuredSystemFk').val(configuredSystemFk);
      }
    } else {
      $('#facetInfoTr').hide();
      $('.support-visit-section').hide();
      $('#configuredSystemFk').val('');
      $('#configuredSystemFk').disable();
      $('#configuredSystemFk').prop('disabled', 'disabled');
    }
  });

  $('#isEmailSent').on('change', function() {
    if ($(this).prop("checked")) {
      $('.emailResponseTd').show();
    } else {
      $('.emailResponseTd').hide();
    }
  });

  $('#doUpdateConfiguredSystemVersion').on('change', function() {
    if ($(this).prop("checked")) {
      $('.update-configured-system-version').css('color', '#0a0');
    } else {
      $('.update-configured-system-version').css('color', '#ccc');
    }
  });

  $('input[name="isTrainingProvided"]').on('change', function() {
    if ($(this).val() == 'Y') {
      $('.followup-onsite').fadeIn(200);
    } else {
      $('.followup-onsite').fadeOut(200);
    }
  });

  $('#category').on('change', function() {
    if ($(this).find('option:selected').text() == 'Backfile') {
      $('.followup-training').hide();
      $('#supportVisitDateReq').hide();
    } else if ($(this).find('option:selected').text() == 'Updates') {
      $('.followup-training').hide();
      $('#supportVisitDateReq').hide();
    } else if ($(this).find('option:selected').text() == 'Follow-Up Training') {
      $('.followup-training').show();
      if ($('input[name="isTrainingProvided"]:checked').val() == 'Y') {
        $('.followup-onsite').show();
      } else {
        $('.followup-onsite').hide();
      }
      if ($('#status').val() == '6 - Closed (Successful)') {
        $('#supportVisitDateReq').show();
      } else {
        $('#supportVisitDateReq').hide();
      }
    } else {
      $('.followup-training').hide();
      $('#supportVisitDateReq').hide();
    }
  });

  $('#supportVisitDate').on('change', function() {
    if ($(this).val() != '') {
      $('.support-visit-info').show();
      $('input[name="autoCloseDate"]').val('').prop('disabled', true);
      $('select[name="autoCloseStatus"]').prop('selectedIndex', 0).prop('disabled', true);
    } else {
      $('.support-visit-info').hide();
      $('input[name="autoCloseDate"]').prop('disabled', false);
      $('select[name="autoCloseStatus"]').prop('disabled', false);
    }
    $('input[name=supportVisitTime]').focus();
  });

  $('#newUploadFileTbl').on('click', '.ibtnAdd', function (event) {
    var newTrLine ='';
    newTrLine += '<tr class="form-group">';
    newTrLine += '<td><input type="file" name="fileList" size="75"/></td>';
    newTrLine += '<td><input type="button" class="ibtnDel btn btn-danger" value="Delete"/></td>';
    newTrLine += '</tr>';
    $('#newUploadFileTbl > tbody:last').append(newTrLine);
  });

  $('#newUploadFileTbl').on('click', '.ibtnDel', function (event) {
    $(this).closest('tr').remove();
  });

  $('#commentsTable').on('click', '.ibtnAdd', function (event) {
    var newTrLine ='';
    newTrLine += '<tr>';
    newTrLine += '<td class="TOP"><textarea name="commentsArr" rows="5" class="form-control input-sm"></textarea></td>';
    newTrLine += '<td class="TOP"><input type="button" class="ibtnDel btn btn-danger" value="Delete"/></td>';
    newTrLine += '</tr>';
    $('#commentsTable > tbody:first').prepend(newTrLine);
    document.getElementsByName("commentsArr")[0].focus();
  });

  $('#commentsTable').on('click', '.ibtnDel', function (event) {
    $(this).closest('tr').remove();
  });

  $('#status').on('change', function (event) {
    if ($(this).val() == '5 - Closed' || $(this).val() == '6 - Closed (Successful)' || $(this).val() == '7 - Closed (No Response)' || $(this).val() == '8 - Closed (Unavailable)') {
      $('.closedDateTd').show();
      $('.auto-close-info').hide();
      $('#resolution-row').show();
      var d = new Date();
      if ($('[name="closedDate"]').val() == null || $('[name="closedDate"]').val() == '')
        $('[name="closedDate"]').val((d.getMonth()+1) + '/' + d.getDate() + '/' + d.getFullYear());

      if ($('#category :selected').text() == 'Follow-Up Training' && $(this).val() == '6 - Closed (Successful)') {
        $('#supportVisitDateReq').show();
      } else {
        $('#supportVisitDateReq').hide();
      }

      //Retrieve other ship issues when closing a ship issue with a scheduled support visit
      if ($(this).val() == '6 - Closed (Successful)' && $('#shipPk').val() != "" && $('#supportVisitDate').val() != "") {
        $.ajax({
          type: 'POST',
          url: 'ajax.do?action=shipCloseOtherIssues&projectPk=' + projectPk + '&shipPk=' + $('#shipPk').val()
            + (issuePk == '' ? '' : '&currIssuePk=' + issuePk),
          async: false
          }).responseText;
      } else {
        $('.close-related-issues').hide();
      }
    } else { //Regular menu item
      $('.closedDateTd').hide();
      $('.auto-close-info').show();
      $('[name="resolution"]').val('');
      $('[name="totalTime"]').val('');
      $('#resolution-row').hide();
      $('#supportVisitDateReq').hide();
      $('.close-related-issues').hide();
    }
  }).change();

  $('#priority').on('click', function (event) {
    if ($(this).prop('checked')) {
      $('.priority-reason').show();
      $('#priority-high-banner').show();
      $('#priority-normal-banner').hide();
    } else {
      //Clear checkboxes/texts and hide
      $('input[name=priorityReasonArr]').each(function() {
        $(this).removeAttr('checked');
      });
      $('select[name=laptopIssue]').val('');
      $('select[name=scannerIssue]').val('');
      $('select[name=softwareIssue]').val('');
  
      $('select[name=laptopIssue]').hide();
      $('select[name=scannerIssue]').hide();
      $('select[name=softwareIssue]').hide();
  
      $('.priority-reason').hide();
      $('#priority-high-banner').hide();
      $('#priority-normal-banner').show();
    }
  });

  $('#laptop-issue').on('click', function (event) {
    if ($(this).prop('checked')) {
      $('select[name=laptopIssue]').fadeIn(200);
    } else {
      $('select[name=laptopIssue]').val('');
      $('select[name=laptopIssue]').fadeOut(200);
    }
  });

  $('#scanner-issue').on('click', function (event) {
    if ($(this).prop('checked')) {
      $('select[name=scannerIssue]').fadeIn(200);
    } else {
      $('select[name=scannerIssue]').val('');
      $('select[name=scannerIssue]').fadeOut(200);
    }
  });

  $('#software-issue').on('click', function (event) {
    if ($(this).prop('checked')) {
      $('select[name=softwareIssue]').fadeIn(200);
    } else {
      $('select[name=softwareIssue]').val('');
      $('select[name=softwareIssue]').fadeOut(200);
    }
  });

  $('#shipPk').val(shipPk).change();
  if ($('#shipPk').val() != '') {
    $('.support-visit-section').show();
  } else {
    $('.support-visit-section').hide();
    $('#facetInfoTr').hide();
  }
  handleAddNewSelect(document.supportForm.currPersonAssigned, 'personAssigned');
  handleAddNewSelect(document.supportForm.currTrainer, 'trainer');
  if ($('#priority').prop('checked')) {
    $('.priority-reason').show();
    $('#priority-high-banner').show();
    $('#priority-normal-banner').hide();

    if ($('#laptop-issue').prop('checked')) {
      $('select[name=laptopIssue]').show();
    }
    if ($('#scanner-issue').prop('checked')) {
      $('select[name=scannerIssue]').show();
    }
    if ($('#software-issue').prop('checked')) {
      $('select[name=softwareIssue]').show();
    }
  }

  if ($('#category :selected').text() == 'Follow-Up Training') {
    $('.followup-training').show();

    if ($('input[name="isTrainingProvided"]:checked').val() == 'Y') {
      $('.followup-onsite').show();
    } else {
      $('.followup-onsite').hide();
    }

    if ($('#status').val() == '6 - Closed (Successful)') {
      $('#supportVisitDateReq').show();
    }
  } else {
    $('.followup-training').hide();
  }

  if ($('#isEmailSent').prop("checked")) {
    $('.emailResponseTd').show();
  } else {
    $('.emailResponseTd').hide();
  }

  if ($('#supportVisitDate').val() != '') {
    $('.support-visit-info').show();
    $('input[name="autoCloseDate"]').val('').prop('disabled', true);
    $('select[name="autoCloseStatus"]').prop('selectedIndex', 0).prop('disabled', true);
  } else {
    $('.support-visit-info').hide();
    $('input[name="autoCloseDate"]').prop('disabled', false);
    $('select[name="autoCloseStatus"]').prop('disabled', false);
  }

  if (editType === "add" || editType === "copy") {
    var d = new Date();
    document.supportForm.openedBy.value = userFullName;
    document.supportForm.openedDate.value = (d.getMonth()+1) + '/' + d.getDate() + '/' + d.getFullYear();
  }

  if (editType === "copy") {
    document.supportForm.closedDate.value = '';
  }

  $.ajax({
    type: 'POST',
    url: 'ajax.do?action=shipComputerName&projectPk=' + projectPk + '&shipPk=' + $('#shipPk').val(),
    async: false
  }).responseText;
});

function addShipFkToCsListMap(shipFk, configuredSystem) {
  if (!shipFkToCsListMap[shipFk]) {
    shipFkToCsListMap[shipFk] = [];
  }
  shipFkToCsListMap[shipFk].push(configuredSystem);
}

function handleAddNewSelect(select, elementId) {
  var addNewInput = document.getElementById(elementId);
  if (!select || !elementId || !addNewInput) {
    return;
  }
  if (select.value === 'null') {
    addNewInput.value = '';
    addNewInput.style.display = 'inline';
    addNewInput.focus();
  } else {
    addNewInput.value = select.value;
    addNewInput.style.display = 'none';
  }
}

//old version
function checkNew(currObj, elementName) {
  if (currObj.value == 'null') {
    document.getElementsByName(elementName)[0].value = '';
    document.getElementsByName(elementName)[0].style.display = 'inline';
    document.getElementsByName(elementName)[0].focus();
  } else {
    document.getElementsByName(elementName)[0].value = currObj.value;
    document.getElementsByName(elementName)[0].style.display = 'none';
  }
}

function valFields() {
  var title          = stripSpaces(document.supportForm.title.value);
  var personAssigned = stripSpaces(document.supportForm.personAssigned.value);
  var trainer        = stripSpaces(document.supportForm.trainer.value);
  var openedBy        = stripSpaces(document.supportForm.openedBy.value);
  var openedDate     = stripSpaces(document.supportForm.openedDate.value);
  var closedDate     = stripSpaces(document.supportForm.closedDate.value);
  var resolution     = stripSpaces(document.supportForm.resolution.value);
  var status    = document.supportForm.status.value;
  var totalTime = document.supportForm.totalTime.value;

  var e = document.getElementById("category");
  var category = e.options[e.selectedIndex].text;

  var supportVisitDate = stripSpaces(document.supportForm.supportVisitDate.value);
  var supportVisitTime = stripSpaces(document.supportForm.supportVisitTime.value);
  var supportVisitEndTime = stripSpaces(document.supportForm.supportVisitEndTime.value);
  var supportVisitLoc = stripSpaces(document.supportForm.supportVisitLoc.value);

  document.supportForm.title.value = title;
  document.supportForm.personAssigned.value = personAssigned;
  document.supportForm.trainer.value = trainer;
  document.supportForm.openedBy.value = openedBy;
  document.supportForm.openedDate.value = openedDate;
  document.supportForm.closedDate.value = closedDate;
  document.supportForm.resolution.value = resolution;
  document.supportForm.supportVisitDate.value = supportVisitDate;
  document.supportForm.supportVisitLoc.value = supportVisitLoc;

  if (title.length < 1) {
    alert('You must enter in a title.');
    document.supportForm.title.focus();
    return false;
  }

  if (category.length < 1) {
    alert('You must select a category.');
    document.supportForm.category.focus();
    return false;
  }

  if (personAssigned.length < 1) {
    alert('You must enter in a person assigned to this issue.');
    document.supportForm.personAssigned.focus();
    return false;
  }

  if (openedBy.length < 1) {
    alert('You must enter who the issue was opened by.');
    document.supportForm.openedBy.focus();
    return false;
  }

  if (openedDate.length < 1) {
    alert('You must enter in a opened date.');
    document.supportForm.openedDate.focus();
    return false;
  } else if (!validateDate(openedDate, 'Begin Date')) {
    document.supportForm.openedDate.focus();
    return false;
  }

  if (status == '5 - Closed' || status == '6 - Closed (Successful)' || status == '7 - Closed (No Response)' || status == '8 - Closed (Unavailable)') {
    //Closed date is required
    if (closedDate.length < 1) {
      alert('You must enter in a closed date.');
      document.supportForm.closedDate.focus();
      return false;
    } else if (!validateDate(closedDate, 'Closed Date')) {
      document.supportForm.closedDate.focus();
      return false;
    }
  }

  if (!document.supportForm.initiatedBy[0].checked && !document.supportForm.initiatedBy[1].checked) {
    alert('You must enter who the issue was initiated by.');
    return false;
  }

  if (document.supportForm.isTrainingProvided.checked && document.supportForm.dept[4].checked) {
    alert('You must specify which department the training was provided to.');
    return false;
  }

  if ($('[name="resolution"]').is(':visible') && resolution.length < 1) {
    alert('You must enter in a resolution.');
    document.supportForm.resolution.focus();
    return false;
  }

  if ($('[name="totalTime"]').is(':visible') && totalTime.length < 1) {
    alert('You must enter in the total time.');
    document.supportForm.totalTime.focus();
    return false;
  }

  if ($('select[name="laptopIssue"]').is(':visible') && $('select[name="laptopIssue"]').val() == '') {
    alert('You must select a laptop reason.');
    $('select[name="laptopIssue"]').focus();
    return false;
  }

  if ($('select[name="scannerIssue"]').is(':visible') && $('select[name="scannerIssue"]').val() == '') {
    alert('You must select a scanner reason.');
    $('select[name="scannerIssue"]').focus();
    return false;
  }

  if ($('select[name="softwareIssue"]').is(':visible') && $('select[name="softwareIssue"]').val() == '') {
    alert('You must select a software reason.');
    $('select[name="softwareIssue"]').focus();
    return false;
  }


  if (supportVisitTime.length > 0) {
    if (!checkAllNumbers(supportVisitTime)) {
      alert('Please enter in a valid support visit start time');
      document.supportForm.supportVisitTime.focus();
      return false;
    }

    //Fix malformed times
    if (supportVisitTime.length == 1) { //assume this is the hour
      supportVisitTime = '0' + supportVisitTime + '00';
    } else if (supportVisitTime.length == 2) { //assume this is the hour
      supportVisitTime = supportVisitTime + '00';
    } else if (supportVisitTime.length == 3) {
      supportVisitTime = '0' + supportVisitTime;
    }

    document.supportForm.supportVisitTime.value = supportVisitTime;

    if (parseInt(supportVisitTime) > 2359) {
      alert('Please enter in a valid support visit start time (0000-2359)');
      document.supportForm.supportVisitTime.focus();
      return false;
    }
  }

  if (supportVisitEndTime.length > 0) {
    if (!checkAllNumbers(supportVisitEndTime)) {
      alert('Please enter in a valid support visit end time');
      document.supportForm.supportVisitEndTime.focus();
      return false;
    }

    //Fix malformed times
    if (supportVisitEndTime.length == 1) { //assume this is the hour
      supportVisitEndTime = '0' + supportVisitEndTime + '00';
    } else if (supportVisitEndTime.length == 2) { //assume this is the hour
      supportVisitEndTime = supportVisitEndTime + '00';
    } else if (supportVisitEndTime.length == 3) {
      supportVisitEndTime = '0' + supportVisitEndTime;
    }

    document.supportForm.supportVisitEndTime.value = supportVisitEndTime;

    if (parseInt(supportVisitEndTime) > 2359) {
      alert('Please enter in a valid support visit end time (0000-2359)');
      document.supportForm.supportVisitEndTime.focus();
      return false;
    }
  }

  //Case: End time is entered but start time is missing
  if (supportVisitEndTime.length > 0 && supportVisitTime.length == 0) {
    alert('Please enter in a support visit start time');
    document.supportForm.supportVisitTime.focus();
    return false;
  }

  //Case: End time is later than start time
  if (supportVisitTime.length > 0 && supportVisitEndTime.length > 0 && parseInt(supportVisitTime) >= parseInt(supportVisitEndTime)) {
    alert('Support visit end time must be later than the start time');
    document.supportForm.supportVisitEndTime.focus();
    return false;
  }

  if (category == 'Follow-Up Training' && status == '6 - Closed (Successful)') {
    if (supportVisitDate.length < 1) {
      alert('You must enter in a support visit date.');
      document.supportForm.supportVisitDate.focus();
      return false;
    } else if (!validateDate(supportVisitDate, 'Begin Date')) {
      document.supportForm.supportVisitDate.focus();
      return false;
    }

    if ($('[name=isTrainingProvided]:checked').val() == null) {
      alert('You must specify if the follow-up training was provided.');
      document.getElementsByName('isTrainingProvided')[0].focus();
      return false;
    }
  }

  if ($('[name=supportVisitLoc]').is(':visible') && $('[name=supportVisitLoc]').val().length < 1) {
    alert('You must enter in a support visit location.');
    document.supportForm.supportVisitLoc.focus();
    return false;
  }

  if ($('[name=isTrainingOnsite]').is(':visible') && $('[name=isTrainingOnsite]:checked').val() == null) {
    alert('You must specify if the follow-up training was performed onsite.');
    document.getElementsByName('isTrainingOnsite')[0].focus();
    return false;
  }

  if (status == '6 - Closed (Successful)' && $('[name=dept]:checked').val() == null) {
    alert('You must select a division');
    document.getElementsByName('dept')[0].focus();
    return false;
  }

  return true;
}
