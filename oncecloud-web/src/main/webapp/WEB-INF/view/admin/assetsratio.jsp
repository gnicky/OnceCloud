<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
     <title>计算资源台账</title>
	<%@ include file="../share/head.jsp" %>
	
	<style type="text/css">
	.re_title{ background:#eee; padding:5px 0; font-family:"微软雅黑"; font-size:16px; font-weight:normal; padding-left:10px;margin:0px;}
	li{ list-style:none;}
	.box_resouce{ width:100%; font-family:"微软雅黑";}
	.box_resouce li{ width:45%; border:1px solid #ddd; border-bottom:2px solid #34aadc; float:left; margin:10px;}
	.box_resouce li h4{ background:#eee; padding:5px 10px; margin:0;}
	.box_resouce li .scontent{ padding:5% 10%; text-align:center;}
	.box_resouce li .scontent strong{ font-size:40px; font-weight:normal; color:#34aadc; margin-bottom:30px; display:block;}
	.box_resouce li p{ text-align:center; color:#666;}
	.box_resouce li p em{ font-style:normal; font-size:26px; color:#f60;}
	.box_resouce li .progress-bar {background:#ddd; border-radius:100px; height:16px; width:100%;float:none;}
	.box_resouce li .progress-bar span {background:#34aadc; border-radius:100px; height:16px; display:block;}
	
	.box_resouce li.green{border-bottom:2px solid #090;}
	.box_resouce li.green .content strong{ color:#090;}
	.box_resouce li.green p em{ color:#090;}
	.box_resouce li.green .progress-bar span {background:#090; border-radius:100px; height:16px; display:block;}
	
	.box_resouce li.orange{border-bottom:2px solid #ff9500;}
	.box_resouce li.orange .scontent strong{ color:#ff9500;}
	.box_resouce li.orange p em{ color:#ff9500;}
	.box_resouce li.orange .progress-bar span {background:#ff9500; border-radius:100px; height:16px; display:block;}
	
	.box_resouce li.gray{border-bottom:2px solid #999;}
	.box_resouce li.gray .scontent strong{ color:#999;}
	.box_resouce li.gray p em{ color:#f60;}
	.box_resouce li.gray .progress-bar span {background:#999; border-radius:100px; height:16px; display:block;}
	
	.count{ margin-top:30px; font-family:"微软雅黑"; text-align:right; margin-right:120px;}
	.count span{ display:inline-block; margin-left:10px;}
	.count span em{ font-size:30px;  color:#f60; font-style:normal;}
	.clear_both{ clear:both; font-size:0; height:0; overflow:hidden;}
	.l-count {
    text-align: left;
    margin-left: 40px;
    margin-bottom:10px;
}
</style>
</head>
<body class="cloud-body">
	<%@ include file="../share/sidebar.jsp" %>
	<%@ include file="content/assetsratioc.jsp" %>
	<%@ include file="../share/js.jsp" %>
	
</body>
</html>