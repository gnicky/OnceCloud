<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content">
	<div class="intro">
		<div class="row" style="margin: 0">
			<div class="col-md-3">
				<h2>
					<img class="location" src="${basePath}img/user/location.png" /><strong>苏州</strong>
				</h2>
			</div>
			<div class="col-md-5 charge">
				账户余额：<a class="charge-total" id="charge-total"></a>&nbsp;<span
					class="unit" style="color: #daf07d">元</span> <a class="link"
					href="${basePath}expense/summary">查看消费记录</a>
			</div>
			<div class="col-md-4" style="padding-top: 29px">
				<button id="viewquota" class="btn btn-primary"
					style="font-size: 12px; padding: 7px 15px"
					url="${basePath}user/modal/viewquota">
					<span class="glyphicon glyphicon-th-list"
						style="padding-right: 5px"></span>查看资源限额
				</button>
			</div>
		</div>
	</div>
	<div class="resource">
		<div class="resource-item" data-type="instance">
			<strong title="主机">${quotaUsed.quotaVM}</strong>
			<h5>
				<span class="glyphicon glyphicon-cloud"></span>主机
			</h5>
		</div>
		<div class="resource-item" data-type="volume">
			<strong title="硬盘">${quotaUsed.quotaDiskN}</strong>
			<h5>
				<span class="glyphicon glyphicon-inbox"></span>硬盘
			</h5>
		</div>
		<div class="resource-item" data-type="snapshot">
			<strong title="备份">${quotaUsed.quotaSnapshot}</strong>
			<h5>
				<span class="glyphicon glyphicon-camera"></span>备份
			</h5>
		</div>
		<div class="resource-item" data-type="router">
			<strong title="路由器">${quotaUsed.quotaRoute}</strong>
			<h5>
				<span class="glyphicon glyphicon-fullscreen"></span>路由器
			</h5>
		</div>
		<div class="resource-item" data-type="router">
			<strong title="私有网络">${quotaUsed.quotaVlan}</strong>
			<h5>
				<span class="glyphicon glyphicon-indent-left"></span>私有网络
			</h5>
		</div>
		<div class="resource-item" data-type="elasticip">
			<strong title="公网IP">${quotaUsed.quotaIP}</strong>
			<h5>
				<span class="glyphicon glyphicon-globe"></span>公网IP
			</h5>
		</div>
		<div class="resource-item" data-type="loadbalance">
			<strong title="负载均衡器">${quotaUsed.quotaLoadBalance}</strong>
			<h5>
				<span class="glyphicon glyphicon-random"></span>负载均衡器
			</h5>
		</div>
		<div class="resource-item" data-type="firewall">
			<strong title="防火墙">${quotaUsed.quotaFirewall}</strong>
			<h5>
				<span class="glyphicon glyphicon-flash"></span>防火墙
			</h5>
		</div>
		<div class="resource-item" data-type="service">
			<strong title="表单">0</strong>
			<h5>
				<span class="glyphicon glyphicon-wrench"></span>表单
			</h5>
		</div>
		<div class="resource-item" data-type="image">
			<strong title="映像">${quotaUsed.quotaImage}</strong>
			<h5>
				<span class="glyphicon glyphicon-record"></span>映像
			</h5>
		</div>
	</div>
	<div class="area">
		<h4>
			<a href="${basePath}common/log.jsp">最近操作</a>
		</h4>
		<div class="inner" id="act-area"></div>
	</div>
	<div class="area">
		<h4>
			<a href="${basePath}common/service.jsp">最新问题</a>
		</h4>
		<div class="inner" id="service-area"></div>
	</div>
	<div id="DashboardModalContainer" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>
</div>