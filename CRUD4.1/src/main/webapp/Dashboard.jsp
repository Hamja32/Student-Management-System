<%@page import="java.util.List"%>
<%@ page import="com.crud.TopStudent"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Admin Dashboard</title>
<script type="text/javascript"
	src="https://www.gstatic.com/charts/loader.js"></script>

<style>
body {
	font-family: 'Segoe UI', sans-serif;
	
	background: #e3fff8;
	margin: 0;
	padding-bottom: 50px;
}

/* Main Container to center everything */
.main-container {
	max-width: 1200px;
	margin: 0 auto;
	padding: 20px;
}

.header {
	text-align: center;
	margin-bottom: 30px;
	color: #444;
}

/* --- SECTION 1: STATS CARDS (Top Row) --- */
.stats-container {
	display: flex;
	justify-content: space-between;
	gap: 20px;
	margin-bottom: 30px;
}

.card {
	background: white;
	padding: 25px;
	border-radius: 12px;
	flex: 1; /* Equal width */
	box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
	text-align: center;
	transition: transform 0.3s ease;
}

.card:hover {
	transform: translateY(-5px);
}

.card h3 {
	margin: 0;
	color: #888;
	font-size: 14px;
	text-transform: uppercase;
	letter-spacing: 1px;
}

.card p {
	margin: 15px 0 0;
	font-size: 40px;
	font-weight: 700;
	color: #2c3e50;
}

/* --- SECTION 2: GRID LAYOUT (Chart Left, Leaderboard Right) --- */
.dashboard-grid {
	display: flex;
	gap: 20px;
}

/* Left Side: Chart */
.chart-section {
	flex: 2; /* Takes 66% width */
	background: white;
	padding: 20px;
	border-radius: 12px;
	box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
}

/* Right Side: Leaderboard */
.leaderboard-section {
	flex: 1; /* Takes 33% width */
	background: #04614a; /* Optional: Make it card-like or transparent */
	padding: 20px; 
	border-radius: 12px;
}

/* Topper Card Styling Updates */
.topper-card {
	background: #d6ebda;
	padding: 15px;
	border-radius: 10px;
	text-align: center;
	box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
	margin-bottom: 15px; /* Spacing between toppers */
	display: flex; /* Horizontal Layout for cleaner look in sidebar */
	align-items: center;
	justify-content: space-between;
}

/* Percentage Badge */
.percent-badge {
	color: white;
	width: 50px;
	height: 50px;
	border-radius: 50%;
	line-height: 50px;
	text-align: center;
	font-weight: bold;
	font-size: 14px;
	box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}

/* Responsive Design (Mobile) */
@media ( max-width : 768px) {
	.stats-container, .dashboard-grid {
		flex-direction: column;
	}
	.topper-card {
		display: block;
	} /* Back to block on mobile */
	.percent-badge {
		margin: 10px auto;
	}
}
</style>

<script type="text/javascript">
	google.charts.load('current', {
		'packages' : [ 'corechart' ]
	});
	google.charts.setOnLoadCallback(drawChart);

	function drawChart() {
		var pass =
<%=request.getAttribute("passCount")%>
	;
		var fail =
<%=request.getAttribute("failCount")%>
	;

		var data = google.visualization.arrayToDataTable([
				[ 'Status', 'Number of Students' ],
				[ 'Pass Students ', pass ], [ 'Fail Students ', fail ] ]);

		var options = {
			title : 'Pass vs Fail Ratio',
			is3D : true,
			colors : [ '#2ecc71', '#e74c3c' ],
			pieSliceText : 'percentage',
			chartArea : {
				width : '90%',
				height : '80%'
			}, // Chart ko bada dikhane ke liye
			legend : {
				position : 'bottom'
			}
		};

		var chart = new google.visualization.PieChart(document
				.getElementById('piechart'));
		chart.draw(data, options);
	}
</script>
</head>
<body>

	<jsp:include page="Nav.jsp" />

	<div class="main-container">

		<div class="header">
			<h2>🏫 Dashboard Overview</h2>
		</div>

		<div class="stats-container">
			<div class="card" style="border-bottom: 4px solid #3498db;">
				<h3>Total Students</h3>
				<p><%=request.getAttribute("totalStudents")%></p>
			</div>
			<div class="card" style="border-bottom: 4px solid #2ecc71;">
				<h3>Passed</h3>
				<p><%=request.getAttribute("passCount")%></p>
			</div>
			<div class="card" style="border-bottom: 4px solid #e74c3c;">
				<h3>Failed</h3>
				<p><%=request.getAttribute("failCount")%></p>
			</div>
		</div>

		<div class="dashboard-grid">

			<div class="chart-section">
				<h3 style="text-align: center; color: #555;">Performance
					Analysis</h3>
				<div id="piechart" style="width: 100%; height: 400px;"></div>
			</div>

			<div class="leaderboard-section">
				<h3 style="color: #fff; margin-bottom: 15px;">🏆 Top 3
					Performers</h3>

				<%
				List<TopStudent> toppers = (List<TopStudent>) request.getAttribute("top3List");
				if (toppers != null && !toppers.isEmpty()) {
					String[] colors = { "#FFD700", "#C0C0C0", "#CD7F32" }; // Gold, Silver, Bronze
					String[] medals = { "🥇", "🥈", "🥉" };

					for (int i = 0; i < toppers.size(); i++) {
						TopStudent s = toppers.get(i);
				%>
				<div class="topper-card"
					style="border-left: 5px solid <%=colors[i]%>;">
					<div style="font-size: 24px; width: 40px;"><%=medals[i]%></div>

					<div style="text-align: left; flex-grow: 1; padding-left: 10px;">
						<div style="font-weight: bold; color: #2c3e50; font-size: 16px;"><%=s.getName()%></div>
						<div style="color: #888; font-size: 13px;">
							Roll No:
							<%=s.getRollNo()%></div>
					</div>

					<div class="percent-badge" style="background: <%=colors[i]%>;">
						<%=String.format("%.0f", s.getPercentage())%>%
					</div>
				</div>
				<%
				}
				} else {
				%>
				<p style="text-align: center; color: #999;">No results found.</p>
				<%
				}
				%>

				<div style="text-align: center; margin-top: 20px;">
					<a href="list"
						style="color: #3498db; text-decoration: none; font-weight: bold; font-size: 14px;">View
						All Students →</a>
				</div>
			</div>
		</div>

	</div>

</body>
</html>