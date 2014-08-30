<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String title = "服务器";
	int sideActive = 15;
	User user = null;
	String hostUuid = null;
	if (session.getAttribute("user") != null) {
		user = (User)session.getAttribute("user");
		hostUuid = (String)session.getAttribute("hostUuid");
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
	<%@ include file="../../share/head.jsp" %>
	<link rel="stylesheet" href="<%=basePath %>css/bootstrap-switch.min.css" />
</head>
<body class="cloud-body">
	<%@ include file="../../share/sidebar.jsp" %>
	<%@ include file="../../share/js.jsp" %>
	<script src="<%=basePath %>js/highcharts.js"></script>
	<script src="<%=basePath %>js/bootstrap-switch.min.js"></script>
	<script src="<%=basePath %>admin/js/detail/hostdetail.js"></script>
	<%@ include file="../content/detail/hostdetailc.jsp" %>
</body>
</html>