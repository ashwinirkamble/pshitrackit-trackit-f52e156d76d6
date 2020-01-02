<script type="text/javascript">
var tip = d3.tip()
	.attr('class', 'd3-tip')
	.offset([-10, 0])
	.html(function(d) {
		if (d.name == 'Scheduled')
			return "<strong>Ships Scheduled:</strong> <span style='color:yellow'>" + d.sched_ships + "</span>";
		else
			return "<strong>Ships Trained:</strong> <span style='color:yellow'>" + d.actual_ships + "</span>";
	});

var margin = {top: 20, right: 20, bottom: 30, left: 40},
    width = 760 - margin.left - margin.right,
    height = 450 - margin.top - margin.bottom;

var x = d3.scale.ordinal()
    .rangeRoundBands([0, width], .1);

var y = d3.scale.linear()
    .rangeRound([height, 0]);

var color = d3.scale.ordinal()
    .range(["#98abc5", "#8a89a6"]);

var xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom");

var yAxis = d3.svg.axis()
    .scale(y)
    .orient("left");

	var data = [];

function paintBarChart() {
var svg = d3.select("#trainingBarChart").append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
    .call(tip)
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

  color.domain(d3.keys(data[0]).filter(function(key) { return key !== "label" && key !== "sched_ships" && key !== "actual_ships"; }));

  data.forEach(function(d) {
    var y0 = 0;
    d.data = color.domain().map(function(name) { return { name: name, y0: y0, y1: y0 += +d[name], sched_ships: d.sched_ships, actual_ships: d.actual_ships }; });
    d.total = d.data[d.data.length - 1].y1;
  });


  x.domain(data.map(function(d) { return d.label; }));
  y.domain([0, d3.max(data, function(d) { return d.total; })]);

  svg.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis);

  svg.append("g")
      .attr("class", "y axis")
      .call(yAxis)
    .append("text")
      .attr("transform", "rotate(-90)")
      .attr("y", 6)
      .attr("dy", ".71em")
      .style("text-anchor", "end")
      .text("Training");

  var label = svg.selectAll(".label")
      .data(data)
    .enter().append("g")
      .attr("class", "g")
      .attr("transform", function(d) { return "translate(" + x(d.label) + ",0)"; });

  label.selectAll(".bar")
      .data(function(d) { return d.data; })
    .enter().append("rect")
      .attr("width", x.rangeBand())
      .attr("y", function(d) { return y(d.y1); })
      .attr("height", function(d) { return y(d.y0) - y(d.y1); })
      .style("fill", function(d) { return color(d.name); })
			.on('mouseover', tip.show)
			.on('mouseout', tip.hide);

  var legend = svg.selectAll(".legend")
      .data(color.domain().slice().reverse())
    .enter().append("g")
      .attr("class", "legend")
      .attr("transform", function(d, i) { return "translate(0," + i * 20 + ")"; });

  legend.append("rect")
      .attr("x", width - 18)
      .attr("width", 18)
      .attr("height", 18)
      .style("fill", color);

  legend.append("text")
      .attr("x", width - 24)
      .attr("y", 9)
      .attr("dy", ".35em")
      .style("text-anchor", "end")
      .text(function(d) { return d; });

} //end of paintBarChart

$( "#refreshBtn" ).on( "click", function() {
	$.ajax({url: 'ajax.do?action=trainingBarChart&projectPk=1'});
});
</script>