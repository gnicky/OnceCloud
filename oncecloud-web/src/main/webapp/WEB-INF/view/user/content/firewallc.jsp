<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content" id="platformcontent">
	<div class="intro">
		<h1>防火墙&nbsp;Security&nbsp;Groups</h1>
		<p class="lead" style="margin-top:10px">
			为了加强位于基础网络中的主机的安全性，您可以在主机之前放置<em>防火墙&nbsp;(Security&nbsp;Group)</em>。您可以自建更多的防火墙。自建防火墙在初始状态下，所有端口都是封闭的，您需要建立规则以打开相应的端口。
		</p>
	</div>
	<div class="once-pane">
		<div class="once-toolbar">
			<button class="btn btn-default btn-refresh"><span class="glyphicon glyphicon-refresh" style="margin-right:0"></span></button>
			<button id="createfw" class="btn btn-primary" url="${basePath}firewall/create">+&nbsp;新建</button>
			<div class="btn-group">
				<button class="btn btn-default dropdown-toggle" data-toggle="dropdown">更多操作...
					<span class="caret" style="margin-left:15px"></span>
				</button>
				<ul class="dropdown-menu">
					<li><a class="btn-forbidden" id="bind" url="${basePath}user/modal/bindfirewall.jsp"><span class="glyphicon glyphicon-cloud"></span>加载到主机</a></li>
					<li><a class="btn-forbidden" id="bindlb" url="${basePath}user/modal/bindfirewall.jsp"><span class="glyphicon glyphicon-random"></span>加载到负载均衡</a></li>
					<li><a class="btn-forbidden" id="bindrt" url="${basePath}user/modal/bindfirewall.jsp"><span class="glyphicon glyphicon-fullscreen"></span>加载到路由器</a></li>
					<li><a class="btn-forbidden" style="display:none;" id="binddb" url="${basePath}user/modal/bindfirewall.jsp.jsp"><span class="glyphicon glyphicon-inbox"></span>加载到数据库</a></li>
					<li><a class="btn-forbidden" id="delete"><span class="glyphicon glyphicon-trash"></span>删除</a></li>
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
					<th>ID</th>
					<th>名称</th>
					<th>规则数&nbsp;(个)</th>
					<th>创建时间</th>
				</tr>
			</thead>
			<tbody id="tablebody">
			</tbody>
		</table>
	</div>
	<div id="FirewallModalContainer" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>
</div>