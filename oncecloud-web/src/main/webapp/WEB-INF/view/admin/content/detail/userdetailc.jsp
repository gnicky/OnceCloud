<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<style type="text/css">
	.col-md-6 {
		padding: 0 10px 0 0;
	}
</style>
<div class="content detail" id="platformcontent" userid="${userid}" platformBasePath="${basePath}">
	<div class="intro">
		<h1>用户&nbsp;Users</h1>
		<p class="lead" style="margin-top:10px">
			<em>用户&nbsp;(User)</em>是云平台的具体使用者，可以通过管理后台对用户进行增删改查，并对用户的资源限额进行调整。
		</p>
	</div>
	<ol class="breadcrumb oc-crumb">
		<li><a href="/user"><span class="glyphicon glyphicon-user cool-green"></span><span class="ctext">USER</span></a></li>
		<li class="active"><a href="/user/detail">${username}</a></li>
	</ol>
	<div class="col-md-4">
		<div class="detail-item">
			<div class="title">
				<h3>用户信息&nbsp;<a id="basic-fresh" class="btn-refresh"><span class="glyphicon glyphicon-refresh"></span></a>
					<div class="btn-group">
						<button class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							<span class="glyphicon glyphicon-tasks"></span>
						</button>
						<ul class="dropdown-menu">
							<li><a id="modify" url="${basePath}admin/create/createuser.jsp"><span class="glyphicon glyphicon-pencil"></span>修改</a></li>
						</ul>
					</div>
				</h3>
			</div>
			<dl id="basic-list" class="my-horizontal"></dl>
		</div>
	</div>
	<div class="col-md-8">
		<div class="detail-item detail-right" style="padding:30px 50px">
			<div class="title">
				<h3>资源配额&nbsp;<a id="quota-fresh" class="btn-refresh"><span class="glyphicon glyphicon-refresh"></span></a>
			</div>
			<div class="once-toolbar">
					<button id="quota_update" state="edit" class="btn btn-default">修改配额</button>
			</div>
			<div class="row" style="margin:10px 0">
				<form class="form form-horizontal" id="quota-form">
					<input type="hidden" id="quotaid">
					<div class="col-md-6">
						<table class="table table-bordered once-table">
							<thead>
								<tr>
									<th>资源类型</th>
									<th>已用配额</th>
									<th>总配额</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>公网IP</td>
									<td id="eipU"></td>
									<td id="eipT" old=""></td>
								</tr>
								<tr>
									<td>主机</td>
									<td id="vmU"></td>
									<td id="vmT" old=""></td>
								</tr>
								<tr>
									<td>备份</td>
									<td id="bkU"></td>
									<td id="bkT" old=""></td>
								</tr>
								<tr>
									<td>映像</td>
									<td id="imgU"></td>
									<td id="imgT" old=""></td>
								</tr>
								<tr>
									<td>硬盘</td>
									<td id="volU"></td>
									<td id="volT" old=""></td>
								</tr>
								<tr>
									<td>SSH密钥</td>
									<td id="sshU"></td>
									<td id="sshT" old=""></td>
								</tr>
								<tr>
									<td>防火墙</td>
									<td id="fwU"></td>
									<td id="fwT" old=""></td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="col-md-6">
						<table class="table table-bordered once-table">
							<thead>
								<tr>
									<th>资源类型</th>
									<th>已用配额</th>
									<th>总配额</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>路由器</td>
									<td id="rtU"></td>
									<td id="rtT" old=""></td>
								</tr>
								<tr>
									<td>私有网络</td>
									<td id="vlanU"></td>
									<td id="vlanT" old=""></td>
								</tr>
								<tr>
									<td>负载均衡器</td>
									<td id="lbU"></td>
									<td id="lbT" old=""></td>
								</tr>
								<tr>
									<td>硬盘容量(GB)</td>
									<td id="diskU"></td>
									<td id="diskT" old=""></td>
								</tr>
								<tr>
									<td>带宽(Mbps)</td>
									<td id="bwU"></td>
									<td id="bwT" old=""></td>
								</tr>
								<tr>
									<td>内存(GB)</td>
									<td id="memU"></td>
									<td id="memT" old=""></td>
								</tr>
								<tr>
									<td>CPU(个)</td>
									<td id="cpuU"></td>
									<td id="cpuT" old=""></td>
								</tr>
							</tbody>
						</table>
					</div>
				</form>
			</div>
		</div>
	</div>
	<div id="UserModalContainer" type="edit" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>
</div>