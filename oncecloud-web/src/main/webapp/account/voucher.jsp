<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String title = "代金券";
	int sideActive = 100;
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
	<link rel="stylesheet" href="<%=basePath %>css/business.css" />
</head>
<body class="cloud-body">
	<script src="<%=basePath %>js/jquery-1.11.0.min.js"></script>
	<script src="<%=basePath %>bootstrap/js/bootstrap.min.js"></script>
	<script src='<%=basePath %>js/bootbox.min.js'></script>
	<%@ include file="content/voucherc.jsp" %>
	<script src="js/voucher.js"></script>
</body>
</html>