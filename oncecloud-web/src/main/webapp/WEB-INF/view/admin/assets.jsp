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
	.pool_floor{ float:left; width:25%; border-bottom:1px solid #ddd; border-right:1px solid #ddd; height:114px;}
	.pool_pic{ float:left; width:26%;margin:5px 0; position:relative; }
	.pool_pic img { margin:0 20px; width:60px; height:60px;margin-top:20px;}
	.pool_inf { float:right; width:70%; font-family:"Microsoft Yahei";margin:20px 0; line-height:26px; font-size:16px; color:#666; }
	.pool_inf .no em{ color:#1F90C8; text-decoration:none; font-style:normal;}
	.pool_inf .price em{ color:#f60; text-decoration:none;font-style:normal;}
	
	@media screen and (min-width: 1px) and (max-width: 1400px) {
		.pool_pic{ float:left; width:21%;margin:5px 0;}
	          .pool_pic img { margin:10px ; width:100%; height:auto;}
		.pool_inf { float:right; width:70%; font-family:"Microsoft Yahei";margin:20px 0; line-height:24px; font-size:14px; color:#666; }
	     
	}
	
	.pool_floor span{ padding: 0px 10px; color: rgb(153, 153, 153); line-height: 110px; font-size: 16px;}
	li{ list-style:none;}
	.hard_list{width:100%;position:relative;left:-20px;}
	.hard_list ul{ width:100%;}
	.hard_list li{ font-size: 16px;border-radius:8px; background:#ddd; padding:20px 10px; width:30%; float:left; margin:10px 20px 10px 0;}
	.hard_list li.red{ background:#ff9500; color:#fff;}
	.hard_list li.green{ background:#090; color:#fff;}
	.hard_list li.blue{ background:#34aadc; color:#fff;}
	.hard_list li.gray{ background:#ccc; color:#fff;}
	.hard_list li p{ font-family:Arial;}
	.pool_title { float:left;width:100%;height:42px; line-height:42px; background:#eee; border-bottom:1px solid #ddd; color:#333; padding:0 20px; font-size:14px;}
	
	.hrr-btn {float: right;margin-right: 20px;margin-top: -30px;}
	.hrr-btn a { text-decoration:none; padding:10px 30px; background:#f60; color:#fff; font-size:14px; font-weight:bold; }
	.hrr-btn a:hover { background:#f60;}
	.hard_count{ clear:both; text-align:right;font-size:16px;padding-right:9%;margin-top:30px;}
	.hard_count span{ color:#333; margin-bottom:5px;display:inline-block;margin-left:30px;}
	.hard_count span strong{ color:#f60; font-size:14px;}
	.hard_count span.tips{ font-weight:bold; display:block;margin-top:20px}
	.hard_count span.tips strong{ font-size:30px; color:#c00;}
	
	</style>
</head>
<body class="cloud-body">
	<%@ include file="../share/sidebar.jsp" %>
	<%@ include file="content/assetsc.jsp" %>
	<%@ include file="../share/js.jsp" %>
	<script src="${basePath}js/admin/assets.js"></script>
</body>
</html>