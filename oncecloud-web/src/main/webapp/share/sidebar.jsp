<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%
	int level = user.getUserLevel();
%>
<div class="navigation">
	<div class="profile">
		<div class="avartar">
			<div id="avartar-top"></div>
			<div id="avartar-bottom"></div>
		</div>
		<%
			if (level == 0) {
		%>
		<p class="user-name">
			<a href="javascript:void(0)">管理员</a>
		</p>
		<div class="user-link">
			<a title="首页"><span class="glyphicon glyphicon-home"></span></a>
			<a href="<%=basePath %>admin/dashboard.jsp" title="控制台"><span class="glyphicon glyphicon-dashboard"></span></a>
			<a href="javascript:void(0)"><span class="glyphicon glyphicon-briefcase"></span></a>
			<a target="_blank" href="<%=basePath %>/map/CompanyMap.jsp" title="用户地图展示"><span class="glyphicon glyphicon-book"></span></a>
			<a href="<%=basePath %>UserAction?action=logout" title="退出登录"><span class="glyphicon glyphicon-off"></span></a>
		</div>
		<%
			} else {
		%>
		<p class="user-name">
			<a href="javascript:void(0)"><%=user.getUserName() %></a>
		</p>
		<div class="user-link">
			<a title="首页"><span class="glyphicon glyphicon-home"></span></a>
			<a href="<%=basePath %>user/dashboard.jsp" title="控制台"><span class="glyphicon glyphicon-dashboard"></span></a>
			<a title="帐户充值" href="<%=basePath %>account/recharge.jsp"><span class="glyphicon glyphicon-briefcase"></span></a>
			<a title="用户指南"><span class="glyphicon glyphicon-book"></span></a>
			<a href="<%=basePath %>UserAction?action=logout" title="退出登录"><span class="glyphicon glyphicon-off"></span></a>
		</div>
		<%
			}
		%>
	</div>
	<div class="sidebar">
		<%
			if (level == 0) {
		%>
		<ul class="nav nav-list">
			<li <% if (sideActive == 1) { %> class="active" <% } %>><a href="<%=basePath %>admin/instance.jsp"><span class="glyphicon glyphicon-cloud cool-orange"></span><span class="name">虚拟机</span><span class="title">Instances</span></a><div class="cool-border"></div></li>
			<li <% if (sideActive == 3) { %> class="active" <% } %>><a href="<%=basePath %>admin/user.jsp"><span class="glyphicon glyphicon-user cool-green"></span><span class="name">用户</span><span class="title">Users</span></a><div class="cool-border"></div></li>
			<li <% if (sideActive == 2) { %> class="active" <% } %>><a href="<%=basePath %>common/image.jsp"><span class="glyphicon glyphicon-record cool-blue"></span><span class="name">映像</span><span class="title">Images</span></a><div class="cool-border" style="margin-left:0"></div></li>
			<li <% if (sideActive == 4) { %> class="active" <% } %>><a href="<%=basePath %>admin/address.jsp"><span class="glyphicon glyphicon-indent-left cool-purple"></span><span class="name">地址池</span><span class="title">Address Pool</span></a><div class="cool-border" style="margin-left:0"></div></li>
		</ul>
		<ul class="nav nav-list" style="margin-top:25px">
			<li <% if (sideActive == 12) { %> class="active" <% } %>><div class="cool-border" style="margin-left:0"></div><a href="<%=basePath %>admin/datacenter.jsp"><span class="glyphicon glyphicon-globe cool-green"></span><span class="name">数据中心</span><span class="title">Data Centers</span></a><div class="cool-border"></div></li>
			<li <% if (sideActive == 13) { %> class="active" <% } %>><a href="<%=basePath %>admin/rack.jsp"><span class="glyphicon glyphicon-road cool-purple"></span><span class="name">机架</span><span class="title">Racks</span></a><div class="cool-border"></div></li>
			<li <% if (sideActive == 14) { %> class="active" <% } %>><a href="<%=basePath %>admin/pool.jsp"><span class="glyphicon glyphicon-tint cool-blue"></span><span class="name">资源池</span><span class="title">Pools</span></a><div class="cool-border"></div></li>
			<li <% if (sideActive == 15) { %> class="active" <% } %>><a href="<%=basePath %>admin/host.jsp"><span class="glyphicon glyphicon-tasks cool-red"></span><span class="name">服务器</span><span class="title">Servers</span></a><div class="cool-border"></div></li>
			<li <% if (sideActive == 16) { %> class="active" <% } %>><a href="<%=basePath %>admin/storage.jsp"><span class="glyphicon glyphicon-hdd cool-cyan"></span><span class="name">存储</span><span class="title">Storages</span></a><div class="cool-border"></div></li>
		</ul>
		<ul class="nav nav-list" style="margin-top:50px">
			<li <% if (sideActive == 9) { %> class="active" <% } %>><div class="cool-border" style="margin-left:0"></div><a href="<%=basePath %>common/log.jsp"><span class="glyphicon glyphicon-list-alt cool-purple"></span><span class="name">操作日志</span><span class="title">Activities</span></a><div class="cool-border"></div></li>
			<li <% if (sideActive == 11) { %> class="active" <% } %>><a href="<%=basePath %>common/service.jsp"><span class="glyphicon glyphicon-question-sign cool-cyan"></span><span class="name">表单</span><span class="title">Services</span></a><div class="cool-border"></div></li>
		</ul>
		<%
			} else {
		%>
		<ul class="nav nav-list">
			<li <% if (sideActive == 1) { %> class="active" <% } %>><a href="<%=basePath %>user/instance.jsp"><span class="glyphicon glyphicon-cloud cool-orange"></span><span class="name">主机</span><span class="title">Instances</span></a><div class="cool-border"></div></li>
			<li <% if (sideActive == 3) { %> class="active" <% } %>><a href="<%=basePath %>user/volume.jsp"><span class="glyphicon glyphicon-hdd cool-blue"></span><span class="name">硬盘</span><span class="title">Volumes</span></a><div class="cool-border"></div></li>
			<li <% if (sideActive == 4) { %> class="active" <% } %>><a href="<%=basePath %>user/snapshot.jsp"><span class="glyphicon glyphicon-camera cool-blue"></span><span class="name">备份</span><span class="title">Snapshots</span></a><div class="cool-border"></div></li>
			<li <% if (sideActive == 5) { %> class="active" <% } %>><a href="<%=basePath %>user/router.jsp"><span class="glyphicon glyphicon-indent-left cool-green"></span><span class="name">网络</span><span class="title">Networks</span></a><div class="cool-border"></div></li>
			<li <% if (sideActive == 6) { %> class="active" <% } %>><a href="<%=basePath %>user/elasticip.jsp"><span class="glyphicon glyphicon-globe cool-green"></span><span class="name">公网IP</span><span class="title">Elastic IPs</span></a><div class="cool-border"></div></li>
			<li <% if (sideActive == 7) { %> class="active" <% } %>><a href="<%=basePath %>user/loadbalance.jsp"><span class="glyphicon glyphicon-random cool-cyan"></span><span class="name">负载均衡</span><span class="title">Load Balances</span></a><div class="cool-border"></div></li>
<!-- 			<li <% if (sideActive == 12) { %> class="active" <% } %>><a href="<%=basePath %>user/database.jsp"><span class="glyphicon glyphicon-inbox cool-purple"></span><span class="name">数据库</span><span class="title">Databases</span></a><div class="cool-border"></div></li> -->
			<li <% if (sideActive == 8) { %> class="active" <% } %>><a href="<%=basePath %>user/firewall.jsp"><span class="glyphicon glyphicon-flash cool-green"></span><span class="name">防火墙</span><span class="title">Security Groups</span></a><div class="cool-border"></div></li>
			<li <% if (sideActive == 2) { %> class="active" <% } %>><a href="<%=basePath %>common/image.jsp"><span class="glyphicon glyphicon-record cool-blue"></span><span class="name">映像</span><span class="title">Images</span></a><div class="cool-border" style="margin-left:0"></div></li>
			<li <% if (sideActive == 13) { %> class="active" <% } %>><a href="<%=basePath %>user/alarm.jsp"><span class="glyphicon glyphicon-bell cool-red"></span><span class="name">监控警告</span><span class="title">Alarms</span></a><div class="cool-border" style="margin-left:0"></div></li>
		</ul>
		<ul class="nav nav-list" style="margin-top:50px">
			<li <% if (sideActive == 9) { %> class="active" <% } %>><div class="cool-border" style="margin-left:0"></div><a href="<%=basePath %>common/log.jsp"><span class="glyphicon glyphicon-list-alt cool-purple"></span><span class="name">操作日志</span><span class="title">Activities</span></a><div class="cool-border"></div></li>
			<li <% if (sideActive == 10) { %> class="active" <% } %>><a href="<%=basePath %>user/expensesummary.jsp"><span class="glyphicon glyphicon-shopping-cart cool-red"></span><span class="name">消费记录</span><span class="title">Consumptions</span></a><div class="cool-border"></div></li>
			<li <% if (sideActive == 11) { %> class="active" <% } %>><a href="<%=basePath %>common/service.jsp"><span class="glyphicon glyphicon-question-sign cool-cyan"></span><span class="name">表单</span><span class="title">Services</span></a><div class="cool-border"></div></li>
		</ul>
		<%
			}
		%>
	</div>
</div>