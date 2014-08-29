<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String title = "备份";
	int sideActive = 4;
	User user = null;
	String resourceUuid = null;
	String resourceType = null;
	String resourceName = null;
	if (session.getAttribute("user") != null) {
		user = (User)session.getAttribute("user");
		resourceUuid = (String)session.getAttribute("resourceUuid");
		resourceType = (String)session.getAttribute("resourceType");
		resourceName = (String)session.getAttribute("resourceName");
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
	<link rel="stylesheet" href="<%=basePath %>css/jquery.contextMenu.css" />
	<link rel="stylesheet" href="<%=basePath %>css/snapshot.css" />
</head>
<body class="cloud-body">
	<%@ include file="../../share/sidebar.jsp" %>
	<%@ include file="../../share/js.jsp" %>
	<script src="<%=basePath %>js/jquery.ui.position.js"></script>
	<script src="<%=basePath %>js/jquery.contextMenu.js"></script>
	<script src="<%=basePath %>user/js/detail/snapshotdetail.js"></script>
	<%@ include file="../content/detail/snapshotdetailc.jsp" %>
</body>
</html>