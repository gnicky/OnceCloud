<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%@ page language="java" import="com.oncecloud.manager.QuotaManager"%>
<%@ page language="java" import="com.oncecloud.hbm.Quota"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String title = "控制台";
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
	<link rel="stylesheet" href="<%=basePath %>css/dashboard.css" />
</head>
<body class="cloud-body">
	<%@ include file="../share/sidebar.jsp" %>
	<%@ include file="content/dashboardc.jsp" %>
	<%@ include file="../share/js.jsp" %>
	<script src="js/dashboard.js"></script>
</body>
</html>