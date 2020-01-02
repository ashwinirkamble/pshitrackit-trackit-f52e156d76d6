$(function() {
	$("table.alt-color tr:odd").not(".ignore").css("background-color", "#f9f9f9");
	$("table.alt-color tr:even").not(".ignore").css("background-color", "#fff;");


	$("table.alt-color tr:odd").not(".ignore,.nohover").hover(
		function() {
			$(this).css("background-color", "#ebebeb");
		},
		function() {
			$(this).css("background-color", "#f9f9f9");
		}
	);

	$("table.alt-color tr:even").not(".ignore,.nohover").hover(
		function() {
			$(this).css("background-color", "#ebebeb");
		},
		function() {
			$(this).css("background-color", "#fff;");
		}
	);
});
