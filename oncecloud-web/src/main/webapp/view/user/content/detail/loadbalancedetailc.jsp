<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content detail" id="platformcontent" lbUuid="${lbUuid}">
	<div class="intro">
		<h1>负载均衡&nbsp;Load&nbsp;Balances</h1>
		<p class="lead">
			<em>负载均衡&nbsp;(Load&nbsp;Balances)&nbsp;</em>可以将来自多个公网地址的访问流量分发到多台主机上，并支持自动检测并隔离不可用的主机，从而提高业务的服务能力和可用性。同时，你还可以随时通过添加或删减主机来调整你的服务能力，而且这些操作不会影响业务的正常访问。
		</p>
	</div>
	<ol class="breadcrumb oc-crumb">
		<li><a href="${basePath}loadbalance"><span
				class="glyphicon glyphicon-random cool-cyan"></span><span
				class="ctext">LOADBALANCE</span></a></li>
		<li class="active"><a href="${basePath}loadbalance/detail">${showId}</a></li>
	</ol>
	<div class="col-md-4">
		<div class="detail-item">
			<div class="title">
				<h3>
					基本属性&nbsp;<a href="javascript:void(0)" class="btn-refresh"><span
						class="glyphicon glyphicon-refresh"></span></a>
					<div class="btn-group">
						<button class="btn btn-default dropdown-toggle"
							data-toggle="dropdown">
							<span class="glyphicon glyphicon-tasks"></span>
						</button>
						<ul class="dropdown-menu">
							<li><a id="modify"><span
									class="glyphicon glyphicon-pencil"></span>修改</a></li>
						</ul>
					</div>
				</h3>
			</div>
			<dl id="basic-list" class="my-horizontal"></dl>
		</div>
		<div class="detail-item">
			<div class="title">
				<h3>
					关联资源&nbsp;<a href="javascript:void(0)" class="btn-refresh"><span
						class="glyphicon glyphicon-refresh"></span></a>
					<div class="btn-group">
						<button class="btn btn-default dropdown-toggle"
							data-toggle="dropdown">
							<span class="glyphicon glyphicon-tasks"></span>
						</button>
						<ul class="dropdown-menu">
						</ul>
					</div>
				</h3>
			</div>
			<dl id="depend-list" class="my-horizontal"></dl>
		</div>
	</div>
	<div class="col-md-8">
		<div class="detail-item detail-right">
			<div class="once-pane" style="padding: 0">
				<div class="title">
					<h3 class="uppercase">功能</h3>
					<span class="oc-update" id="suggestion" style="display: none">修改尚未更新，请点击"应用修改"</span>
				</div>
				<div class="once-toolbar">
					<button id="fe_create" class="btn btn-default">+&nbsp;添加监听器</button>
					<button id="fe_apply" class="btn btn-default">&nbsp;应用修改</button>
					<button id="monitor" class="btn btn-default btn-disable" disabled>流量监控</button>
				</div>
				<div id="fore_list">
				</div>
			</div>
			<div id="LbModalContainer" class="modal fade" tabindex="-1"
				role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>
		</div>
	</div>
</div>