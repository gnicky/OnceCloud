<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String title = "地址池";
	int sideActive = 4;
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
</head>
<body class="cloud-body">
	<%@ include file="../share/sidebar.jsp" %>
	<%@ include file="content/addressc.jsp" %>
	<%@ include file="../share/js.jsp" %>
	<script src="js/address.js"></script>
</body>
</html>