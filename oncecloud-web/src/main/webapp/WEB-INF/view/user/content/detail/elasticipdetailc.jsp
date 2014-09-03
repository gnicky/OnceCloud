<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content detail" id="platformcontent" eip="${eip}" basePath="${basePath}">
	<div class="intro">
		<h1>公网IP&nbsp;Elastic&nbsp;IPs</h1>
		<p class="lead">
			<em>公网&nbsp;IP&nbsp;</em>地址是在互联网上合法的静态 IP 地址。在本系统中，公网 IP
			地址与您的账户而非特定的资源（主机）关联，您可以将申请到的公网 IP
			地址分配到任意主机，并随时可以解绑、再分配到其他主机，如此可以快速替换您的对外主机。
		</p>
	</div>
	<ol class="breadcrumb oc-crumb">
		<li><a href="${basePath}elasticip"><span
				class="glyphicon glyphicon-globe cool-green"></span><span
				class="ctext">ElASTICIP</span></a></li>
		<li class="active"><a
			href="${basePath}elasticip/detail">${showId}</a></li>
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
	<div class="col-md-8"></div>
	<div id="EipModalContainer" class="modal fade" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>
</div>