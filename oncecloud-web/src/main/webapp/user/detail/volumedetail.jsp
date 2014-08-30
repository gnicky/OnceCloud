<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String title = "硬盘";
	int sideActive = 3;
	User user = null;
	String volumeUuid = null;
	if (session.getAttribute("user") != null) {
		user = (User)session.getAttribute("user");
		volumeUuid = (String)session.getAttribute("volumeUuid");
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
	<script src="<%=basePath %>user/js/detail/volumedetail.js"></script>
	<%@ include file="../content/detail/volumedetailc.jsp" %>
</body>
</html>