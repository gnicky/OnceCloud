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
	<script src="${basePath}js/admin/detail/companymapdetail.js"></script>
	<div class="content" id="platformcontent" platformUserId="${cid}" platformBasePath="${basePath}">
	<!-- <div class="intro">
		<h1>用户资源统计&nbsp;</h1>
		
	</div> -->
	
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
							<th>ID</th>
							<th>名称</th>
							<th>规则数&nbsp;(个)</th>
							<th>创建时间</th>
						</tr>
					</thead>
					<tbody id="tablebody">
					</tbody>
				</table>
			</div>
		</div>
		</div>
	</div>
</div>
</body>
</html>