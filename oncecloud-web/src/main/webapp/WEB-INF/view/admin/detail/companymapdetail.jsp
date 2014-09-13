<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>用户资源详情</title>
	 <%@ include file="../../share/head.jsp" %> 
</head>
<body class="cloud-body">
    <%@ include file="../../share/sidebar.jsp" %>
	<%@ include file="../../share/js.jsp" %> 
	<script src="${basePath}js/highcharts.js"></script>
	<script src="${basePath}js/admin/detail/companymapdetail.js"></script>
	<div class="content" id="platformcontent" platformUserId="${cid}" platformBasePath="${basePath}">
	<div class="row" style="margin:0; border-bottom: 1px solid #f3f3f3">
	    <div class="col-md-12">	
		<div class="detail-item">
			<div class="title">
				<h3>虚拟主机列表&nbsp;
				</h3>
			</div>
			<div id="depend-list" class="my-horizontal">
				<table class="table table-bordered once-table">
					<thead>
						<tr>
							<!-- <th>ID</th> -->
							<th>名称</th>
							<th>状态</th>
							<th>CPU</th>
							<th>内存</th>
							<th>创建时间</th>
						</tr>
					</thead>
					<tbody id="tablebodyvm">
					</tbody>
				</table>
			</div>
		</div>
		
		
		<div class="detail-item">
			<div class="title">
				<h3>硬盘列表&nbsp;
				</h3>
			</div>
			<div id="depend-list" class="my-horizontal">
				<table class="table table-bordered once-table">
					<thead>
						<tr>
							<!-- <th>ID</th> -->
							<th>名称</th>
							<th>状态</th>
							<th>容量 (GB)</th>
							<th>创建时间</th>
						</tr>
					</thead>
					<tbody id="tablebodyvol">
					</tbody>
				</table>
			</div>
		</div>
		
		
		<div class="detail-item">
			<div class="title">
				<h3>公网IP列表&nbsp;
				</h3>
			</div>
			<div id="depend-list" class="my-horizontal">
				<table class="table table-bordered once-table">
					<thead>
						<tr>
							<!-- <th>ID</th> -->
							<th>名称</th>
							<th>地址</th>
							<th>状态</th>
							<th>带宽 (Mbps)</th>
							<th>IP分组</th>
							<th>创建时间</th>
						</tr>
					</thead>
					<tbody id="tablebodyeip">
					</tbody>
				</table>
			</div>
		</div>
		
		<div  class="detail-item">
			 <div class="title">
				<h3>虚拟机资源图表&nbsp;
				</h3>	
			 </div>
				<div id="chart-area-1">
					<div id="vmpic"></div>
				</div>
			</div>
		</div>
  </div>
	</div>
</body>
</html>