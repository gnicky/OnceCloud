<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String title = "备份";
	int sideActive = 8;
	User user = null;
	String firewallId = null;
	if (session.getAttribute("user") != null) {
		user = (User)session.getAttribute("user");
		firewallId = (String)session.getAttribute("firewallId");
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
	<%@ include file="../content/detail/firewalldetailc.jsp" %>
	<%@ include file="../../share/js.jsp" %>
	<script src="<%=basePath %>user/js/detail/firewalldetail.js"></script>
</body>
</html>