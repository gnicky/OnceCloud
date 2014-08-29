<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String title = "表单";
	int sideActive = 11;
	User user = null;
	String qaId = null;
	if (session.getAttribute("user") != null) {
		user = (User)session.getAttribute("user");
		qaId = (String)session.getAttribute("qaId");
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
	<meta http-equiv="cache-control" content="no-cache">
</head>
<body class="cloud-body">
	<%@ include file="../../share/sidebar.jsp" %>
	<%@ include file="../../share/js.jsp" %>
	<script src="<%=basePath %>common/js/detail/servicedetail.js"></script>
	<%@ include file="../content/detail/servicedetailc.jsp" %>
</body>
</html>