<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content detail" id="platformcontent"
	routerUuid="${routerUuid}" showid="${showId}" basepath="${basePath}" rtIp="">
	<div class="intro">
		<h1>网络&nbsp;Networks</h1>
		<p class="lead">
			通过云平台的SDN技术，您可以快速地搭建您专属的私有云环境。相比于基础网络而言，这个网络可以提供100%的安全隔离，而且有丰富的工具帮助您进行自动化管理。要使用私有网络，请创建一个路由器，然后再创建一个或多个私有网络并连接到这个路由器，最后创建主机并加入到这些私有网络即可。
		</p>
	</div>

	<div class="row" style="margin: 0; border-bottom: 1px solid #f3f3f3">
		<div class="col-md-4">
			<ol class="breadcrumb oc-crumb">
				<li><a href="${basePath}router"><span
						class="glyphicon glyphicon-fullscreen cool-green"></span><span
						class="ctext">ROUTER</span></a></li>
				<li class="active"><a href="${basePath}router/detail">${showId}</a></li>
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
						<button class="btn btn-default dropdown-toggle" data-toggle="dropdown">
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
				<div class="title">
					<h3 class="uppercase">功能</h3>
					<span class="oc-update" id="suggestion" style="display: none">修改尚未更新，请点击"应用修改"</span>
				</div>
				<!-- <div class="once-toolbar">
					<button id="fe_create" class="btn btn-default">+&nbsp;添加</button>
					<button id="apply" class="btn btn-default">&nbsp;应用修改</button>
					<button id="monitor" style="display: none;"
						class="btn btn-default btn-disable" disabled>流量监控</button>
				</div> -->
				<div class="tabs">
					<ul class="nav nav-tabs filter-once-tab">
						<li class="tab-filter selected" type="dhcp"><a href="#">DHCP服务器</a>
						</li>
						<li class="tab-filter" type="port-forwarding"><a href="#">端口转发</a>
						</li>
						<li class="tab-filter" type="tunnel"><a href="#">隧道服务</a></li>
						<li class="tab-filter" type="vpn"><a href="#">VPN服务</a></li>
						<li class="tab-filter" type="filtering"><a href="#">过滤控制</a>
						</li>
					</ul>
					<div class="filter-once-pane">
						<div id="dhcp" class="pane-filter" style="display: block;">
							<div>
								<p class="alert alert-info">
									DHCP&nbsp;服务为您的私有网络提供&nbsp;IP&nbsp;地址分配。分配的地址都是固定地址，即一个主机在其生命周期内获得的地址是保持不变的。
								</p>
							</div>
							<div class="vxnets" id="vxnets-t"></div>
						</div>
						<div id="port-forwarding" class="pane-filter"
							style="display: none;">
							<div>
								<p class="alert alert-info">
									添加端口转发规则，允许来自&nbsp;Internet&nbsp;或者&nbsp;基础网络&nbsp;(vxnet-0)&nbsp;对您的私有网络内部服务的访问。
								</p>
								<div class="hty-pane">
									<div class="once-toolbar">
										<button class="btn btn-default" id="pf-refresh">
											<span class="glyphicon glyphicon-refresh"
												style="margin-right:0"></span>
										</button>
										<button id="pf_create" class="btn btn-default">+&nbsp;添加</button>
										<button id="deletepf" class="btn btn-default btn-disable"
											disabled style="color: #b8b8b8">
											<span class="glyphicon glyphicon-trash"></span>删除
										</button>
									</div>
									<table class="table table-bordered once-table">
										<thead>
											<tr>
												<th>
													<!--checkbox-->
												</th>
												<th>名称</th>
												<th>协议</th>
												<th>源端口</th>
												<th>内网 IP</th>
												<th>内网端口</th>
												<!-- <th>操作</th> -->
											</tr>
										</thead>
										<tbody id="tablebody">
										</tbody>
									</table>
								</div>
							</div>
						</div>
						<!-- <div id="tunnel" class="pane-filter" style="display: none;">
							<div>
								<p class="alert alert-info">
									隧道服务可以将多个不同地域的局域网连接在一起，形成兼顾公有私有的混合云计算环境，助您有效管理多地域的数据中心。更多详情请查看
								</p>
							</div>
						</div>
						<div id="vpn" class="pane-filter" style="display: none;">
							<div>
								<p class="alert alert-info">
									VPN&nbsp;服务使得您可以远程安全地拨入网驰&nbsp;OnceAsCloud&nbsp;中的私有网络。更多详情请查看
								</p>
							</div>
							<div>
								VPN服务&nbsp;<a href="#" id="openvpn">[打开]</a>
							</div>
						</div>
						<div id="filtering" class="pane-filter" style="display: none;">
							<div>
								<p class="alert alert-info">
									缺省情况下，路由器所管理的私有网络之间是可以相互连通的，你可以通过设置过滤规则来控制路由器内部私有网络之间的隔离。</p>
							</div>
						</div> -->
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 分割线 -->
	<div id="imageview" style="display: none">
		<div class="graph graph-router">
			<div class="graph-wrapper">
				<h2 class="graph-title">
					Router: ${showId} 结构示意图<!-- <a class="btn btn-monitor"><span
						class="icon icon-chart"></span>&nbsp;流量监控</a><a
						class="btn btn-applied" href="#"><span
						class="icon icon-router"></span><span class="text">应用修改</span></a> -->
				</h2>
				<div class="col-md-2 actions">
					<div class="graph-actions">
						    <a id="startup" class="btn btn-forbidden" href="#"><span
							class="glyphicon glyphicon-play"></span>
							<span class="text">启动</span></a>
							<a id="shutdown" class="btn" href="#"><span class="glyphicon glyphicon-stop"></span>
							<span class="text">关闭</span></a>
							<a id="destroy" class="btn btn-danger" href="#">
							 <span class="glyphicon glyphicon-trash"></span>
							<span class="text">销毁</span></a>
					</div>
				</div>
				<div class="col-md-10 components">
					<div class="graph-component component-router-network" id="componentsId"
						style="height: 250px;">
						<div class="graph-component component-router-cloud"></div>
						<div class="graph-component component-router-eip" id="routerip" style="display:none">
						   <a class="btn-delete" id="deleteIp" href="#">
						      <span class="glyphicon glyphicon-remove"></span>
						   </a>
						   <span class="glyphicon glyphicon-globe"></span>
						   <a class="component-id" href="#" data-permalink="">117.121.26.40</a>
						</div>
						
						<a id="routernoip"  class="btn" href="#"><span class="glyphicon glyphicon-globe"></span><span
							class="text">绑定公网IP</span></a>
						<div class="graph-component component-router-vxnet0"></div>
					</div>
					<div class="graph-component component-router-sg" id="firewalldiv">
						<a class="sg-name" href="#"
							data-permalink="">缺省防火墙</a><a id="changefirewall" class="btn" href="#"><span
							class="glyphicon glyphicon-pencil"></span><span class="text">修改防火墙</span></a>
					</div>
					<div class="graph-component component-router" id="routeDiv">
						<span class="private-ip">10.50.23.137</span><a class="router-id"
							data-permalink="">rtr-9v87p09x</a>
					</div>
					<div id="vnetslist" class="graph-component component-router-vxnets"
						style="height: 250px;">
						<div class="tree">
							<div class="component-router-vxnet" style="height: 125px;">
								<a class="btn-delete btn-delete-vxnet" href="#"
									style="top: 50.5px;"><span class="glyphicon glyphicon-remove"></span></a><a
									class="ip-network" href="#"
									data-permalink="" style="top: 72.5px;">192.168.1.0/24 /
									192.168.1.1</a><a class="vxnet-name"
									href="#" data-permalink="">vnet</a>
								<div class="graph-component component-vxnet-instances"
									style="height: 125px; width: 104px;">
									<div class="component-vxnet-instance"
										style="margin-top: 32.5px; margin-bottom: 32.5px;">
										<a class="btn-delete" href="#"><span
											class="glyphicon glyphicon-remove"></span></a><span class="private-ip">.2</span><a
											class="instance-name"
											href="#"
											data-permalink="">vmtest</a>
									</div>
									<div class="component-vxnet-instance none"
										style="height: 125px;">
										<a class="btn" href="#"
											style="margin-top: 29.5px; margin-bottom: 29.5px;"><span
											class="glyphicon glyphicon-plus"></span><span class="text">添加主机</span></a>
									</div>
								</div>
							</div>
							<div class="component-router-vxnet none" style="height: 125px;">
								<a id="addvnet" class="btn" href="#" style="top: 38.5px;"><span
									class="icon icon-vxnet" id="iconid"></span><span class="text">连接路由器</span></a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="RouterModalContainer" type="edit" class="modal fade"
		tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
		aria-hidden="true"></div>
</div>
