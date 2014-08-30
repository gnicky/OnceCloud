<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String title = "用户信息";
	int sideActive = 3;
	User user = null;
	Integer userid = null;
	String username = null;
	if (session.getAttribute("user") != null) {
		user = (User)session.getAttribute("user");
		userid = (Integer)session.getAttribute("userid");
		username = (String)session.getAttribute("username");
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
	<script src="<%=basePath %>js/jquery.validate.js"></script>
	<script src="<%=basePath %>admin/js/detail/userdetail.js"></script>
	<%@ include file="../content/detail/userdetailc.jsp" %>
</body>
</html>