<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<%@ include file="../share/head.jsp" %>
	<style type="text/css">
	article, aside, details, figcaption, figure, footer, header, hgroup, menu, nav, section {
	    display: block;
	}
	         
	         
	#address_pool { margin: 0 auto; width:100%; }
	.pool_floor{ float:left; width:33.333333%; border-bottom:1px solid #ddd; border-right:1px solid #ddd; }
	.pool_pic{ float:left; width:26%;margin:5px 0; position:relative; }
	.pool_pic img { margin:0 40px; width:60px; height:60px;position:absolute; top:100%;}
	.pool_inf { float:right; width:70%; font-family:"Microsoft Yahei";margin:20px 0; line-height:26px; font-size:22px; color:#353644; }
	.pool_inf .no em{ color:#1F90C8; text-decoration:none; font-style:normal;}
	.pool_inf .price em{ color:#f60; text-decoration:none;font-style:normal;}
	
	@media screen and (min-width: 1px) and (max-width: 1400px) {
		.pool_pic{ float:left; width:21%;margin:5px 0;}
	          .pool_pic img { margin:10px ; width:100%; height:auto;}
		.pool_inf { float:right; width:70%; font-family:"Microsoft Yahei";margin:20px 0; line-height:24px; font-size:16px; color:#353644; }
	     
	}
	
	li{ list-style:none;}
	.hard_list{}
	.hard_list ul{ width:100%;}
	.hard_list li{ font-size: 16px;border-radius:8px; background:#ddd; padding:10px 10px; width:30%; float:left; margin:10px 20px 10px 0;}
	.hard_list li.red{ background:#ff9500; color:#fff;}
	.hard_list li.green{ background:#090; color:#fff;}
	.hard_list li.blue{ background:#34aadc; color:#fff;}
	.hard_list li.gray{ background:#ccc; color:#fff;}
	.hard_list li p{ font-family:Arial;}
	.pool_title { float:left;width:100%;height:42px; line-height:42px; background:#eee; border-bottom:1px solid #ddd; color:#333; padding:0 20px; font-size:14px;}
	
	.hrr-btn {float: right;margin-right: 20px;margin-top: -30px;}
	.hrr-btn a { text-decoration:none; padding:10px 30px; background:#cea100; color:#fff; font-size:14px; font-weight:bold; }
	.hrr-btn a:hover { background:#D9A800;}
	
	</style>
</head>
<body class="cloud-body">
	<%@ include file="../share/sidebar.jsp" %>
	<%@ include file="content/assetsc.jsp" %>
	<%@ include file="../share/js.jsp" %>
	<script src="${basePath}js/admin/assets.js"></script>
</body>
</html>