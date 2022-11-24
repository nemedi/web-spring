<%@page import="demo.TalkServlet"%>
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Talk</title>
		<link rel="stylesheet" href="bootstrap.css">
		<script src="jquery.js"></script>
		<script>
			$(document).ready(function() {
				setTimeout('location.reload()', 2000);
			});
		</script>
	</head>
	<body>
		<div class="container">
			<h2>Messages</h2>
			<ul class="list-group">
			<% for (String message : TalkServlet.getMessages()) { %>
				<li class="list-group-item">
					<%= message %>
				</li>
			<% } %>
			</ul>
		</div>
	</body>
</html>