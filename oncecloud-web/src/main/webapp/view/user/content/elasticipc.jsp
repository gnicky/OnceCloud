<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content" id="platformcontent" platformUserId="<%=user.getUserId()%>" platformBasePath="<%=basePath %>">
	<div class="intro">
		<h1>公网IP&nbsp;Elastic&nbsp;IPs</h1>
		<p class="lead"> <em>公网&nbsp;IP&nbsp;(Elastic IP)</em>是在互联网上合法的静态 IP 地址。在本系统中，公网 IP 地址与您的账户而非特定的资源（主机）关联，您可以将申请到的公网 IP 地址分配到任意主机，并随时可以解绑、再分配到其他主机，如此可以快速替换您的对外主机。 </p>
	</div>
	<div class="once-pane">
		<div class="once-toolbar">
			<button class="btn btn-default btn-refresh"><span class="glyphicon glyphicon-refresh" style="margin-right:0"></span></button>
			<button id="apply" class="btn btn-primary" url="<%=basePath %>user/create/createelasticip.jsp">+&nbsp;申请</button>
			<div class="btn-group">
				<button class="btn btn-default dropdown-toggle" data-toggle="dropdown">更多操作... <span class="caret" style="margin-left:15px"></span> </button>
				<ul class="dropdown-menu">
					<li><a class="btn-forbidden" id="bind" url="<%=basePath %>user/modal/bindelasticip.jsp" disabled><span class="glyphicon glyphicon-cloud"></span>分配到主机</a></li>
					<li><a class="btn-forbidden" id="bindlb" url="<%=basePath %>user/modal/bindelasticip.jsp" disabled><span class="glyphicon glyphicon-random"></span>分配到负载均衡</a></li>
					<li><a class="btn-forbidden" id="bindrt" url="<%=basePath %>user/modal/bindelasticip.jsp" disabled><span class="glyphicon glyphicon-fullscreen"></span>分配到路由器</a></li>
					<li><a class="btn-forbidden" id="binddb" url="<%=basePath %>user/modal/bindelasticip.jsp" disabled><span class="glyphicon glyphicon-inbox"></span>分配到数据库</a></li>
					<li><a class="btn-forbidden" id="unbind" disabled><span class="glyphicon glyphicon-cloud-download"></span>解绑</a></li>
					<li><a class="btn-forbidden" id="bandwidth" disabled><span class="glyphicon glyphicon-stats"></span>修改带宽</a></li>
					<li><a class="btn-forbidden" id="delete" disabled><span class="glyphicon glyphicon-trash"></span>删除</a></li>
				</ul>
			</div>
			<input class="search" id="search" value="">
			<div class="toolbar-right">
				<table>
					<tr>
						<td>每页&nbsp;</td>
						<td><input id="limit" name="limit" class="page" value="10"></td>
						<td>&nbsp;个&nbsp;页数&nbsp;<a id="currentP"></a>&nbsp;/&nbsp;<a id="totalP"></a></td>
						<td style="padding-left:10px">
							<div><ul id="pageDivider" style="display:inline"></ul></div>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<table class="table table-bordered once-table">
			<thead>
				<tr>
					<th width="4%"></th>
					<th width="12%">ID</th>
					<th width="12%">名称</th>
					<th width="12%">地址</th>
					<th>状态</th>
					<th width="12%">应用资源</th>
					<th>带宽&nbsp;(Mbps)</th>
					<th>IP分组</th>
					<th width="12%">创建于</th>
				</tr>
			</thead>
			<tbody id="tablebody">
			</tbody>
		</table>
	</div>
	<div id="EipModalContainer" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	</div>
</div>
<div class="modal fade" id="changeBandwidth" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="margin-top:200px">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">申请公网IP<a class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove"></span></a></h4>
			</div>
			<div class="modal-body">
				<form class="form form-horizontal" id="changebw-form">
					<fieldset>
						<div class="item">
							<div class="control-label">带宽</div>
							<div class="controls">
								<input type="text" id="bandwidthSize" name="bandwidthSize">&nbsp;Mbps
								<span class="help">1Mbps - 10Mbps</span>
							</div>
						</div>
					</fieldset>
				</form>
			</div>
			<div class="modal-footer">
				<button id="bandwidthAction" type="button" class="btn btn-primary" style="margin-left:-100px">修改</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
			</div>
		</div>
	</div>
</div>
