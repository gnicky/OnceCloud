<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<%@ include file="../share/head.jsp" %>
	<style type="text/css">
		#companyMap{
			position: absolute;
			top:100px;
			left:5px;
			right:5px;
			bottom:5px;
		}
		#map-iframe{
			border: 0px;
			width: 100%;
			height: 100%;
		}
		
		.big{
			display: block;
		    font-size: 14px;
		    padding: 3px;
		    position: absolute;
		    right: 20px;
		    top: 20px;
		}
		
		.small,.full .big,.full .leader{
			display: none;
		}
		.full h1{
			background: none repeat scroll 0 0 #000;
		    font-size: 14px;
		    height: 30px;
		    left: 0;
		    line-height: 30px;
		    margin: 0;
		    position: fixed;
		    width: 100%;
		}
		.full .small{
			display: block;
		    font-size: 14px;
		    padding: 4px;
		    position: absolute;
		    right: 20px;
		    top: 3px;
		}
		.full #map-iframe{
			position: fixed;
			top:30px;
			left:0px;
			bottom: 0xp;
			z-index: 999;
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