<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content" id="platformcontent">
	<div class="intro">
		<h1>负载均衡&nbsp;Load&nbsp;Balances</h1>
		<p class="lead">
			<em>负载均衡&nbsp;(Load&nbsp;Balances)&nbsp;</em>可以将来自多个公网地址的访问流量分发到多台主机上，并支持自动检测并隔离不可用的主机，从而提高业务的服务能力和可用性。同时，你还可以随时通过添加或删减主机来调整你的服务能力，而且这些操作不会影响业务的正常访问。
		</p>
	</div>
	<ul class="nav nav-tabs once-tab">
		<li class="tab-filter active">
			<a href="javascript:void(0)"><span class="glyphicon glyphicon-random"></span>负载均衡</a>
		</li>
		<li class="tab-filter">
			<a href="javascript:void(0)"><span class="glyphicon glyphicon-barcode"></span>转发策略</a>
		</li>
	</ul>
	<div class="once-pane">
		<div class="once-toolbar">
			<button class="btn btn-default btn-refresh">
				<span class="glyphicon glyphicon-refresh" style="margin-right: 0"></span>
			</button>
			<button id="create" class="btn btn-primary"
				url="${basePath}loadbalance/create">
				+&nbsp;新建</button>	
			<div class="btn-group">
				<button class="btn btn-default dropdown-toggle"
					data-toggle="dropdown">
					更多操作... <span class="caret" style="margin-left: 15px"></span>
				</button>
				<ul class="dropdown-menu">
					<li><a class="btn-forbidden" id="startup"><span class="glyphicon glyphicon-play"></span>启动</a></li>
					<li><a class="btn-forbidden" id="shutdown"><span class="glyphicon glyphicon-stop"></span>关机</a></li>
		           <li><a class="btn-forbidden" id="destroy"><span class="glyphicon glyphicon-trash"></span>销毁</a></li>
				</ul>
			</div>
			<input class="search" id="search" value="">
			<div class="toolbar-right">
				<table>
					<tr>
						<td>每页&nbsp;</td>
						<td><input id="limit" name="limit" class="page" value="10"></td>
						<td>&nbsp;个&nbsp;页数&nbsp;<a id="currentP"></a>&nbsp;/&nbsp;<a
							id="totalP"></a></td>
						<td style="padding-left: 10px">
							<div>
								<ul class="pagination" id="pageDivider" style="display:inline"></ul>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<table class="table table-bordered once-table">
			<thead>
				<tr>
				    <th width="4%"></th>
					<th width="16%">ID</th>
					<th width="16%">名称</th>
					<th width="16%">状态</th>
					<th width="16%">公网IP</th>
					<th width="16%">最大连接数</th>
				    <th width="16%">创建时间</th>
				</tr>
			</thead>
			<tbody id="tablebody">
			</tbody>
		</table>
	</div>
    <div id="LBModalContainer" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>
</div>