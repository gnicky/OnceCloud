<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="navigation">
	<div class="profile">
		<div class="avartar">
			<div id="avartar-top"></div>
			<div id="avartar-bottom"></div>
		</div>
		<c:choose>
			<c:when test="${user.userLevel==0}">
				<p class="user-name">
					<a href="javascript:void(0)">管理员</a>
				</p>
				<div class="user-link">
					<a title="首页"><span class="glyphicon glyphicon-home"></span></a> <a
						href="${basePath}dashboard" title="控制台"><span
						class="glyphicon glyphicon-dashboard"></span></a> <a
						href="javascript:void(0)"><span
						class="glyphicon glyphicon-briefcase"></span></a> <a target="_blank"
						href="${basePath}/map/CompanyMap.jsp" title="用户地图展示"><span
						class="glyphicon glyphicon-book"></span></a> <a
						href="${basePath}logout" title="退出登录"><span
						class="glyphicon glyphicon-off"></span></a>
				</div>
			</c:when>
			<c:otherwise>
				<p class="user-name">
					<a href="javascript:void(0)">${user.userName}</a>
				</p>
				<div class="user-link">
					<a title="首页"><span class="glyphicon glyphicon-home"></span></a> <a
						href="${basePath}dashboard" title="控制台"><span
						class="glyphicon glyphicon-dashboard"></span></a> <a title="帐户充值"
						href="${basePath}account/recharge.jsp"><span
						class="glyphicon glyphicon-briefcase"></span></a> <a title="用户指南"><span
						class="glyphicon glyphicon-book"></span></a> <a
						href="${basePath}logout" title="退出登录"><span
						class="glyphicon glyphicon-off"></span></a>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="sidebar">
		<c:choose>
			<c:when test="${user.userLevel==0}">
				<ul class="nav nav-list">
					<li <c:if test="${sideActive==1}">class="active"</c:if>><a
						href="${basePath}instance"><span
							class="glyphicon glyphicon-cloud cool-orange"></span><span
							class="name">虚拟机</span><span class="title">Instances</span></a>
						<div class="cool-border"></div></li>
					<li <c:if test="${sideActive==3}">class="active"</c:if>><a
						href="${basePath}user"><span
							class="glyphicon glyphicon-user cool-green"></span><span
							class="name">用户</span><span class="title">Users</span></a>
						<div class="cool-border"></div></li>
					<li <c:if test="${sideActive==2}">class="active"</c:if>><a
						href="${basePath}common/image.jsp"><span
							class="glyphicon glyphicon-record cool-blue"></span><span
							class="name">映像</span><span class="title">Images</span></a>
						<div class="cool-border" style="margin-left: 0"></div></li>
					<li <c:if test="${sideActive==4}">class="active"</c:if>><a
						href="${basePath}admin/address.jsp"><span
							class="glyphicon glyphicon-indent-left cool-purple"></span><span
							class="name">地址池</span><span class="title">Address Pool</span></a>
						<div class="cool-border" style="margin-left: 0"></div></li>
				</ul>
				<ul class="nav nav-list" style="margin-top: 25px">
					<li <c:if test="${sideActive==12}">class="active"</c:if>><div
							class="cool-border" style="margin-left: 0"></div> <a
						href="${basePath}admin/datacenter.jsp"><span
							class="glyphicon glyphicon-globe cool-green"></span><span
							class="name">数据中心</span><span class="title">Data Centers</span></a>
						<div class="cool-border"></div></li>
					<li <c:if test="${sideActive==13}">class="active"</c:if>><a
						href="${basePath}admin/rack.jsp"><span
							class="glyphicon glyphicon-road cool-purple"></span><span
							class="name">机架</span><span class="title">Racks</span></a>
						<div class="cool-border"></div></li>
					<li <c:if test="${sideActive==14}">class="active"</c:if>><a
						href="${basePath}admin/pool.jsp"><span
							class="glyphicon glyphicon-tint cool-blue"></span><span
							class="name">资源池</span><span class="title">Pools</span></a>
						<div class="cool-border"></div></li>
					<li <c:if test="${sideActive==15}">class="active"</c:if>><a
						href="${basePath}admin/host.jsp"><span
							class="glyphicon glyphicon-tasks cool-red"></span><span
							class="name">服务器</span><span class="title">Servers</span></a>
						<div class="cool-border"></div></li>
					<li <c:if test="${sideActive==16}">class="active"</c:if>><a
						href="${basePath}storage"><span
							class="glyphicon glyphicon-hdd cool-cyan"></span><span
							class="name">存储</span><span class="title">Storages</span></a>
						<div class="cool-border"></div></li>
				</ul>
				<ul class="nav nav-list" style="margin-top: 50px">
					<li <c:if test="${sideActive==9}">class="active"</c:if>><div
							class="cool-border" style="margin-left: 0"></div> <a
						href="${basePath}common/log.jsp"><span
							class="glyphicon glyphicon-list-alt cool-purple"></span><span
							class="name">操作日志</span><span class="title">Activities</span></a>
						<div class="cool-border"></div></li>
					<li <c:if test="${sideActive==11}">class="active"</c:if>><a
						href="${basePath}common/service.jsp"><span
							class="glyphicon glyphicon-question-sign cool-cyan"></span><span
							class="name">表单</span><span class="title">Services</span></a>
						<div class="cool-border"></div></li>
				</ul>
			</c:when>
			<c:otherwise>
				<ul class="nav nav-list">
					<li <c:if test="${sideActive==1}">class="active"</c:if>><a
						href="${basePath}instance"><span
							class="glyphicon glyphicon-cloud cool-orange"></span><span
							class="name">主机</span><span class="title">Instances</span></a>
						<div class="cool-border"></div></li>
					<li <c:if test="${sideActive==3}">class="active"</c:if>><a
						href="${basePath}volume"><span
							class="glyphicon glyphicon-hdd cool-blue"></span><span
							class="name">硬盘</span><span class="title">Volumes</span></a>
						<div class="cool-border"></div></li>
					<li <c:if test="${sideActive==4}">class="active"</c:if>><a
						href="${basePath}snapshot"><span
							class="glyphicon glyphicon-camera cool-blue"></span><span
							class="name">备份</span><span class="title">Snapshots</span></a>
						<div class="cool-border"></div></li>
					<li <c:if test="${sideActive==5}">class="active"</c:if>><a
						href="${basePath}router"><span
							class="glyphicon glyphicon-indent-left cool-green"></span><span
							class="name">网络</span><span class="title">Networks</span></a>
						<div class="cool-border"></div></li>
					<li <c:if test="${sideActive==6}">class="active"</c:if>><a
						href="${basePath}elasticip"><span
							class="glyphicon glyphicon-globe cool-green"></span><span
							class="name">公网IP</span><span class="title">Elastic IPs</span></a>
						<div class="cool-border"></div></li>
					<li <c:if test="${sideActive==7}">class="active"</c:if>><a
						href="${basePath}loadbalance"><span
							class="glyphicon glyphicon-random cool-cyan"></span><span
							class="name">负载均衡</span><span class="title">Load Balances</span></a>
						<div class="cool-border"></div></li>
					<!-- 			<li <c:if test="${sideActive==12}">class="active"</c:if>><a href="${basePath}user/database.jsp"><span class="glyphicon glyphicon-inbox cool-purple"></span><span class="name">数据库</span><span class="title">Databases</span></a><div class="cool-border"></div></li> -->
					<li <c:if test="${sideActive==8}">class="active"</c:if>><a
						href="${basePath}firewall"><span
							class="glyphicon glyphicon-flash cool-green"></span><span
							class="name">防火墙</span><span class="title">Security Groups</span></a>
						<div class="cool-border"></div></li>
					<li <c:if test="${sideActive==2}">class="active"</c:if>><a
						href="${basePath}image"><span
							class="glyphicon glyphicon-record cool-blue"></span><span
							class="name">映像</span><span class="title">Images</span></a>
						<div class="cool-border" style="margin-left: 0"></div></li>
					<li <c:if test="${sideActive==13}">class="active"</c:if>><a
						href="${basePath}alarm"><span
							class="glyphicon glyphicon-bell cool-red"></span><span
							class="name">监控警告</span><span class="title">Alarms</span></a>
						<div class="cool-border" style="margin-left: 0"></div></li>
				</ul>
				<ul class="nav nav-list" style="margin-top: 50px">
					<li <c:if test="${sideActive==9}">class="active"</c:if>><div
							class="cool-border" style="margin-left: 0"></div> <a
						href="${basePath}log"><span
							class="glyphicon glyphicon-list-alt cool-purple"></span><span
							class="name">操作日志</span><span class="title">Activities</span></a>
						<div class="cool-border"></div></li>
					<li <c:if test="${sideActive==10}">class="active"</c:if>><a
						href="${basePath}expensesummary"><span
							class="glyphicon glyphicon-shopping-cart cool-red"></span><span
							class="name">消费记录</span><span class="title">Consumptions</span></a>
						<div class="cool-border"></div></li>
					<li <c:if test="${sideActive==11}">class="active"</c:if>><a
						href="${basePath}service"><span
							class="glyphicon glyphicon-question-sign cool-cyan"></span><span
							class="name">表单</span><span class="title">Services</span></a>
						<div class="cool-border"></div></li>
				</ul>
			</c:otherwise>
		</c:choose>
	</div>
</div>