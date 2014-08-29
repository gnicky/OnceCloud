<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String title = "管理控制台";
	int sideActive = 0;
	User user = null;
	if (session.getAttribute("user") != null) {
		user = (User)session.getAttribute("user");
		int level = user.getUserLevel();
		if (level != 0) {
%>
<script>
	window.location.href = "<%=basePath %>account/backdoor.jsp";
</script>
<%
			return;
		}
	} else {
%>
<script>
	window.location.href = "<%=basePath %>account/backdoor.jsp";
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