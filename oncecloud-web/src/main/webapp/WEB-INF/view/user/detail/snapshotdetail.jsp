<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<%@ include file="../../share/head.jsp" %>
	<link rel="stylesheet" href="${basePath}css/jquery.contextMenu.css" />
	<link rel="stylesheet" href="${basePath}css/snapshot.css" />
</head>
<body class="cloud-body">
	<%@ include file="../../share/sidebar.jsp" %>
    <%@ include file="../content/detail/snapshotdetailc.jsp" %>
	<%@ include file="../../share/js.jsp" %>
	<script src="${basePath}js/jquery.ui.position.js"></script>
	<script src="${basePath}js/jquery.contextMenu.js"></script>
	<script src="${basePath}js/user/detail/snapshotdetail.js"></script>
</body>
</html>