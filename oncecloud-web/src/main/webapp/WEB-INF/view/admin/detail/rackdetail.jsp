<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String title = "机架拓扑图";
	int sideActive = 0;
	User user = null;
	if (session.getAttribute("user") != null) {
		user = (User)session.getAttribute("user");
	} else {
%>
<script>
	window.location.href = "<%=basePath %>account/login.jsp";
</script>
<%
		return;
	}
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<%@ include file="../share/head.jsp" %>
	<link rel="stylesheet" href="<%=basePath %>css/tuoputu.css" />
</head>
<body class="cloud-body">
	<%@ include file="../share/sidebar.jsp" %>
	<%@ include file="content/topologyc.jsp" %>
	<%@ include file="../share/js.jsp" %>
	<script src="<%=basePath %>js/jquery.jsPlumb-1.6.2-min.js"></script>
	<script src="js/topology.js"></script>
</body>
</html>