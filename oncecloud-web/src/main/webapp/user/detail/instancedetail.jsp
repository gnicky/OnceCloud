<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%@ page language="java" import="com.oncecloud.main.Constant"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String title = "主机";
	int sideActive = 1;
	User user = null;
	String vncServer = Constant.noVNCServerPublic;
	String instanceUuid = null;
	if (session.getAttribute("user") != null) {
		user = (User)session.getAttribute("user");
		instanceUuid = (String)session.getAttribute("instanceUuid");
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
	<link rel="stylesheet" href="<%=basePath %>css/instancecomponent.css" />
</head>
<body class="cloud-body">
	<%@ include file="../../share/sidebar.jsp" %>
	<%@ include file="../../share/js.jsp" %>
	<script src="<%=basePath %>js/highcharts.js"></script>
	<script src="<%=basePath %>user/js/detail/instancedetail.js"></script>
	<script src="<%=basePath %>js/bootstrap-switch.min.js"></script>
	<%@ include file="../content/detail/instancedetailc.jsp" %>
</body>
</html>