<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String title = "私有网络";
	int sideActive = 5;
	User user = null;
	String vnUuid = null;
	String routerUuid = null;
	if (session.getAttribute("user") != null) {
		user = (User)session.getAttribute("user");
		vnUuid = (String)session.getAttribute("vnetUuid");
		routerUuid = (String)session.getAttribute("routerUuid");
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
</head>
<body class="cloud-body">
	<%@ include file="../../share/sidebar.jsp" %>
	<%@ include file="../../share/js.jsp" %>
	<script src="<%=basePath %>user/js/detail/vnetdetail.js"></script>
	<%@ include file="../content/detail/vnetdetailc.jsp" %>
</body>
</html>