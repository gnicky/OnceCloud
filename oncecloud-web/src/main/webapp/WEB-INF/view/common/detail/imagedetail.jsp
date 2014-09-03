<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
  	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String title = "映像";
	int sideActive = 2;
	User user = null;
	String imageUuid = null;
	String imageType = null;
	if (session.getAttribute("user") != null) {
		user = (User)session.getAttribute("user");
		imageUuid = (String)session.getAttribute("imageUuid");
		imageType = (String)session.getAttribute("imageType");
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
	<script src="<%=basePath %>common/js/detail/imagedetail.js"></script>
	<%@ include file="../content/detail/imagedetailc.jsp" %>
</body>
</html>