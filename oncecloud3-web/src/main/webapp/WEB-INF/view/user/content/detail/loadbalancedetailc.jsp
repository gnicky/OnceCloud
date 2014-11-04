<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content detail" id="platformcontent" lbUuid="${lbUuid}" showid="${showId}">
	<div class="intro">
		<h1>负载均衡&nbsp;Load&nbsp;Balances</h1>
		<p class="lead">
			<em>负载均衡&nbsp;(Load&nbsp;Balances)&nbsp;</em>可以将来自多个公网地址的访问流量分发到多台主机上，并支持自动检测并隔离不可用的主机，从而提高业务的服务能力和可用性。同时，你还可以随时通过添加或删减主机来调整你的服务能力，而且这些操作不会影响业务的正常访问。
		</p>
	</div>
	<div class="row" style="margin: 0; border-bottom: 1px solid #f3f3f3">
		<div class="col-md-4">
			<ol class="breadcrumb oc-crumb">
				<li><a href="${basePath}loadbalance"><span
						class="glyphicon glyphicon-random cool-cyan"></span><span
						class="ctext">LOADBALANCE</span></a></li>
				<li class="active"><a href="${basePath}loadbalance/detail">${showId}</a></li>
			</ol>
		</div>
		<div class="col-md-8">
			<div class="view-types">
				<span class="title"> 查看方式： </span> <a
					class="view-type type-text current" href="#" oc-type="text"> <span
					class="glyphicon glyphicon-text-width"> </span> 文字
				</a> <a class="view-type type-graph" href="#" oc-type="graph"> <span
					class="glyphicon glyphicon-picture"> </span> 图形
				</a>
			</div>
		</div>
	</div>
	<div id='textview'>
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
						<!-- <div class="btn-group">
						<button class="btn btn-default dropdown-toggle"
							data-toggle="dropdown">
							<span class="glyphicon glyphicon-tasks"></span>
						</button>
						<ul class="dropdown-menu">
						</ul>
					</div> -->
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
						<%--<button id="monitor" class="btn btn-default btn-disable" disabled>流量监控</button>--%>
					</div>
					<div id="fore_list"></div>
				</div>

			</div>
		</div>
	</div>

	<!-- 分割线 -->
	<div id="imageview" style="display: none">
		<div class="graph graph-LB">
			<div class="graph-wrapper">
				<div class="components">
					<div id="ipcontent" class="graph-component component-LB-eips"
						style="padding-top: 100px; padding-bottom: 100px;">
						<div id="unbandingIp"  style="display: none" class="component-LB-eip">
							<div class="component-eip">
								<a class="btn-delete" href="#"> <span
									class="glyphicon glyphicon-remove"></span>
								</a> <span class="glyphicon glyphicon-globe"></span> <a
									class="component-id" data-permalink=""></a>
							</div>
						</div>

						<a id="bandingIp"  class="btn" href="#"><span
							class="glyphicon glyphicon-globe"></span><span class="text">&nbsp;绑定公网IP</span></a>

					</div>
					<div class="graph-component component-LB"
						style="padding-top: 94px; padding-bottom: 94px;">
						<span class="glyphicon glyphicon-random"></span>
					</div>
					<div id="addlistener"
						class="graph-component component-LB-listeners"
						style="padding-top: 50px; padding-bottom: 50px;">

						<div class="graph-component component-LB-listener">
							<div class="listener-title">
								<a class="btn-delete btn-delete-listener" href="#"
									data-id="lbl-qv7k6dfw"><span class="glyphicon glyphicon-remove"></span></a><span
									class="component-id">lbl-qv7k6dfw</span><br>HTTP : 80
							</div>
							<div class="component-LB-backends">
								<div class="component-LB-backend down instance">
									<a class="btn-delete btn-delete-backend" href="#"
										data-id="lbb-zlyzqx2r"><span class="glyphicon glyphicon-remove"></span></a><a
										class="component-name" href="/pek2/instances/i-5oabh9fl/"
										data-permalink="">test</a><span class="port">:80</span>
								</div>
								
									<div class="component-LB-backend down instance">
									<a class="btn-delete btn-delete-backend" href="#"
										data-id="lbb-zlyzqx2r"><span class="glyphicon glyphicon-remove"></span></a><a
										class="component-name" href="/pek2/instances/i-5oabh9fl/"
										data-permalink="">test</a><span class="port">:80</span>
								</div>
									<div class="component-LB-backend down instance">
									<a class="btn-delete btn-delete-backend" href="#"
										data-id="lbb-zlyzqx2r"><span class="glyphicon glyphicon-remove"></span></a><a
										class="component-name" href="/pek2/instances/i-5oabh9fl/"
										data-permalink="">test</a><span class="port">:80</span>
								</div>
								<div class="component-LB-backend none">
									<a class="btn btn-add-backends" href="#"><span
										class="glyphicon glyphicon-plus"></span><span class="text">添加后端</span></a>
								</div>
							</div>
						</div>
						
						<div class="graph-component component-LB-listener">
							<div class="listener-title">
								<a class="btn-delete btn-delete-listener" href="#"
									data-id="lbl-qv7k6dfw"><span class="glyphicon glyphicon-remove"></span></a><span
									class="component-id">lbl-qv7k6dfw</span><br>HTTP : 80
							</div>
							<div class="component-LB-backends">
								<div class="component-LB-backend down instance">
									<a class="btn-delete btn-delete-backend" href="#"
										data-id="lbb-zlyzqx2r"><span class="glyphicon glyphicon-remove"></span></a><a
										class="component-name" href="/pek2/instances/i-5oabh9fl/"
										data-permalink="">test</a><span class="port">:80</span>
								</div>
								
									<div class="component-LB-backend down instance">
									<a class="btn-delete btn-delete-backend" href="#"
										data-id="lbb-zlyzqx2r"><span class="glyphicon glyphicon-remove"></span></a><a
										class="component-name" href="/pek2/instances/i-5oabh9fl/"
										data-permalink="">test</a><span class="port">:80</span>
								</div>
									
								<div class="component-LB-backend none">
									<a class="btn btn-add-backends" href="#"><span
										class="glyphicon glyphicon-plus"></span><span class="text">添加后端</span></a>
								</div>
							</div>
						</div>

						<div class="graph-component component-LB-listener none">
							<a class="btn btn-add-listener" href="#" id="btn_add_listener"><span
								class="glyphicon glyphicon-plus"></span>&nbsp;添加监听器</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div id="LbModalContainer" class="modal fade" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>
</div>