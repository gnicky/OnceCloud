<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>用户地图展示</title>
	<%-- <%@ include file="../share/head.jsp" %> --%>
	<style type="text/css">
      body, html,#allmap {width: 100%;height: 100%;overflow: hidden;margin:0;}
      .companydetail{margin:10px;cursor:pointer}
      .companydetail span {padding-bottom: 2px; display:inline-block}
      
      <style type="text/css">
	.map_info{margin:10px 0;}
	.map_info span{ color:#666; padding:7px 5px;display:inline-block;width:110px}
	.map_info span:hover{ background:#eee;}
	.map_info a{ color:#dd0000; cursor:pointer;font-weight:bold;text-decoration:none;}
	.map_info a:hover{ text-decoration:underline;}
	
	.BMapLib_SearchInfoWindow .BMapLib_bubble_title{
	    line-hieght:31px !important; 
	    background-color:#333 !important;
	    color:#fff !important;
	    font-weight:bold !important;
	    overflow:hidden !important;
	    height:31px !important;
	}
	
	.BMapLib_SearchInfoWindow .BMapLib_bubble_tools div{
	   background-color:#333 !important;
	}
</style>
      
    </style>
</head>
<body class="cloud-body">
	<%-- <%@ include file="../share/sidebar.jsp" %>
	<%@ include file="../share/js.jsp" %> --%>
	<script src="${basePath}js/jquery-1.11.1.min.js"></script>
	 <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=3E4ABfa2b7d282748a5697fc730a41f0"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/library/SearchInfoWindow/1.5/src/SearchInfoWindow_min.js"></script>
<link rel="stylesheet" href="http://api.map.baidu.com/library/SearchInfoWindow/1.5/src/SearchInfoWindow_min.css" />
	<script src="${basePath}js/admin/companymap.js"></script>
	
	  <div id="allmap"></div>
</body>
</html>