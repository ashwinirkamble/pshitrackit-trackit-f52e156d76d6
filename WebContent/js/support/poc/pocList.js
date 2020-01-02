$(document).ready(function () {
  if (shipPk !== '') {
    $( "#tabs" ).tabs({ active: -1 });
    var i = 0;
    $("#accordion").children("div").each( function() {
      if ($(this).attr("id") == "accordion-tab-" + shipPk) {
        $("#accordion").accordion({ active: i });
      }
      i++;
    });
    location.hash = "#accordion-tab-" + shipPk;
    applyDataTable('ship-poc-table-' + shipPk);
  }
  else {
    applyDataTable('employee-poc-table-0');
  }
});

//this will track which tables have already had dataTables applied to it.
var dataTableSet = {};

function applyDataTable(tableClass) {
  if (!dataTableSet[tableClass]) {
    //the action column is located differently for each column. We identify it so we can make it unsortable.
    var isEmployeeTable = tableClass.startsWith('employee-');
    var isShipPocTable = tableClass.startsWith('ship-');
    var actionCol = [10];//poc-(default)
    var lastUpdatedDateCol = 9;
    if (isEmployeeTable) {
      actionCol = null;
    }
    else if (isShipPocTable){
      lastUpdatedDateCol = 10;
      actionCol = [11];
    }
    var table = $('.' + tableClass).DataTable({
      paging : false,
      searching : true,
      stateSave : false,
      columnDefs : [
        { orderable: false, targets: actionCol }
      ],
      order: [[lastUpdatedDateCol, 'desc']],
    }).draw();
    dataTableSet[tableClass] = true;
  }
  return false;
}

function toggleChevron(e) {
  $(e.target)
    .prev('.panel-heading')
    .find('i.indicator')
    .toggleClass('glyphicon-chevron-down glyphicon-chevron-left');
}

$('#searchForm').on('hidden.bs.collapse', toggleChevron);
$('#searchForm').on('shown.bs.collapse', toggleChevron);

$(function() {
 $("#accordion").accordion({
    heightStyle: "content",
    active: false,
    collapsible: true
  });

  $("#tabs").tabs();
});

function confirmDeletePoc(lastName, firstName) {
  return confirm("Are you sure you want to delete the POC information for " + firstName + " " + lastName);
}
