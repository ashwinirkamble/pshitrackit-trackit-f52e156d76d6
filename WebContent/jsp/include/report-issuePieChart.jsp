<script type="text/javascript">
issuePieChartData = [];

var issuePieChart = new d3pie("issuePieChart", {
	size: {
		canvasHeight: 600,
		canvasWidth: 650
	},
	header: {
		title: {
			text:    	"FACET Support Issues Opened",
			fontSize: 20
		},
		subtitle: {
			text: 		"Sub-title description here",
			fontSize: 16
		}
	},
	data: {
		content: issuePieChartData
	},
	misc: {
		gradient: {
			enabled: true
		}
	}
});

$.ajax({url: 'ajax.do?action=issuePieChart&projectPk=1'});


$("input[name='daysBack']").on('change', function() {
	$.ajax({url: 'ajax.do?action=issuePieChart&projectPk=1&daysBack=' + $(this).val()});
});
</script>