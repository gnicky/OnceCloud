<%@ page import="com.oncecloud.listener.UserSessionListener"%>
<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content">
	<div class="intro">
		<div class="row" style="margin:0">
			<div class="col-md-3">
				<h2><img class="location" src="${basePath}img/user/location.png" /><strong>苏州</strong></h2>
			</div>
			<div class="col-md-5 charge">
				在线用户：<a class="charge-total" href="javascript:void(0)">0</a>
			</div>
			<div class="col-md-4">
			</div>
		</div>
	</div>
	<div class="resource">
		<div class="resource-item" data-type="datacenter">
			<strong title="数据中心" id="dcNum">0</strong>
			<h5><span class="glyphicon glyphicon-globe"></span>数据中心</h5>
		</div>
		<div class="resource-item" data-type="rack">
			<strong title="机架" id="rackNum">0</strong>
			<h5><span class="glyphicon glyphicon-road"></span>机架</h5>
		</div>
		<div class="resource-item" data-type="pool">
			<strong title="资源池" id="poolNum">0</strong>
			<h5><span class="glyphicon glyphicon-tint"></span>资源池</h5>
		</div>
		<div class="resource-item" data-type="host">
			<strong title="服务器" id="serviceNum">0</strong>
			<h5><span class="glyphicon glyphicon-tasks"></span>服务器</h5>
		</div>
		<div class="resource-item" data-type="storage">
			<strong title="存储" id="srNum">0</strong>
			<h5><span class="glyphicon glyphicon-hdd"></span>存储</h5>
		</div>
		<div class="resource-item" data-type="instance">
			<strong title="主机" id="vmNum">0</strong>
			<h5><span class="glyphicon glyphicon-cloud"></span>主机</h5>
		</div>
		<div class="resource-item" data-type="address">
			<strong title="公网IP" id="outipNum">0</strong>
			<h5><span class="glyphicon glyphicon-eye-open"></span>公网IP</h5>
		</div>
		<div class="resource-item" data-type="address">
			<strong title="DHCP" id="dhcpNum">0</strong>
			<h5><span class="glyphicon glyphicon-random"></span>DHCP</h5>
		</div>
		<div class="resource-item" data-type="firewall">
			<strong title="防火墙" id="fireNum">0</strong>
			<h5><span class="glyphicon glyphicon-flash"></span>防火墙</h5>
		</div>
		<div class="resource-item" data-type="image">
			<strong title="映像" id="imageNum">0</strong>
			<h5><span class="glyphicon glyphicon-record"></span>映像</h5>
		</div>
	</div>
	<div class="area">
		<h4><a href="${basePath}common/log.jsp">最近操作</a></h4>
		<div class="inner" id="act-area">
		</div>
	</div>
	<div class="area">
		<h4><a href="${basePath}common/service.jsp">待处理问题</a></h4>
		<div class="inner" id="service-area">
		</div>
	</div>
</div>