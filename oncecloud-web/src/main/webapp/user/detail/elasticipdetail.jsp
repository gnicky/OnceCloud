<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String title = "公网IP";
	int sideActive = 6;
	User user = null;
	String eip = null;
	String eipId = null;
	if (session.getAttribute("user") != null) {
		user = (User)session.getAttribute("user");
		eip = (String)session.getAttribute("eip");
		eipId = (String)session.getAttribute("eipId");
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
	<script src="<%=basePath %>user/js/detail/elasticipdetail.js"></script>
	<%@ include file="../content/detail/elasticipdetailc.jsp" %>
</body>
</html>