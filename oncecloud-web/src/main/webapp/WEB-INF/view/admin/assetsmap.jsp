<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<%@ include file="../share/head.jsp" %>
	<style type="text/css">
		#companyMap{
			bottom: 5px;
		    left: 5px;
		    position: absolute;
		    right: 5px;
		    top: 150px;
		}
		#companyMap #map-iframe{
			border: 0px;
			width: 100%;
			height: 100%;
		}
		#companyMap.full #map-iframe{
			position: fixed;
			top:0px;
			left:0px;
			bottom: 0xp;
			z-index: 999;
		}
		.once-pane button{
			float: right;
			margin-left:20px;
		}
	</style>
</head>
<body class="cloud-body">
	<%@ include file="../share/sidebar.jsp" %>
	<%@ include file="content/assetsmapc.jsp" %>
	<%@ include file="../share/js.jsp" %>
	<script src="${basePath}js/admin/assetsmap.js"></script>
</body>
</html>