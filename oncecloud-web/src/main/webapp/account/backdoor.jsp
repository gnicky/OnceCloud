<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page language="java" import="com.oncecloud.manager.UserManager"%>
<%@ page language="java" import="com.oncecloud.hbm.User"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	if (session.getAttribute("user") != null) {
		User user = (User) session.getAttribute("user");
		int level = user.getUserLevel();
		if (level == 0) {
%>
<script>
	window.location.href = "<%=basePath%>admin/dashboard.jsp";
</script>
<%
		}
  	}
%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>网驰管理平台</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" href="<%=basePath%>bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" href="<%=basePath%>css/login.css" />
<link rel="shortcut icon" href="fav.ico" />
</head>
<body class="login-body">
	<script src="<%=basePath %>js/jquery-1.11.0.min.js"></script>
	<div class="home">
		<div class="logo"></div>
		<div class="login-box">
			<form id="login-form" action="<%=basePath %>UserAction?action=adminlogin" autocomplete="off"
				method="post" class="login-form">
				<div style="margin: 12px 0">
					<input type="text" name="user" class="login-input"
						placeholder="用户名">
				</div>
				<div style="margin: 12px 0">
					<input type="password" name="pw" class="login-input"
						placeholder="密码">
				</div>
				<div style="margin: 12px 0">
					<input type="text" id="vercode" name="vercode" class="login-input" placeholder="验证码">
					<div style="margin-top: 12px">
						<img alt="" id="authImg" align="absmiddle">
						<a id="ver-change" class="ver-change" onclick="refresh()">换一个</a>
					</div>
				</div>
				<div>
					<input type="submit" class='btn btn-primary' value="登录">
				</div>
			</form>
		</div>
		<div class="footer">
			<!--			<a>版权所有：中国科学院软件研究所</a>-->
			<a>&nbsp;&nbsp;</a>
		</div>
	</div>
	<script>
		refresh();

		function refresh() {
			document.getElementById("authImg").src="<%=basePath%>AuthImg?now="+new Date();
			$.ajax({
				type: 'get',
				url: '/UserAction',
				data: 'action=queryvercode&now='+new Date(),
				dataType: 'json',
				success: function(array) {
					var ver = array[0].vercode;
					$("#login-form").data('vercode', ver);
				},
				error: function() {}
			});
		}
	</script>
</body>
</html>