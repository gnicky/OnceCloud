<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String title = "注册";
	if (session.getAttribute("user") != null) {
		User user = (User) session.getAttribute("user");
		int level = user.getUserLevel();
		if (level == 0) {
%>
<script>
	window.location.href = "<%=basePath%>admin/dashboard.jsp";
</script>
<%
	} else {
%>
<script>
	window.location.href = "<%=basePath%>user/dashboard.jsp";
</script>
<%
	}
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
	<%@ include file="content/registerc.jsp" %>
	<script src="<%=basePath %>js/jquery.validate.js"></script>
	<script src="js/register.js"></script>
</body>
</html>