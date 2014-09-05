<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content" id="platformcontent">
	<div class="intro">
		<h1>网络&nbsp;Networks</h1>
		<p class="lead">
			通过云平台的SDN技术，您可以快速地搭建您专属的私有云环境。相比于基础网络而言，这个网络可以提供100%的安全隔离，而且有丰富的工具帮助您进行自动化管理。要使用私有网络，请创建一个路由器，然后再创建一个或多个私有网络并连接到这个路由器，最后创建主机并加入到这些私有网络即可。
		</p>
	</div>
	<ul class="nav nav-tabs once-tab">
		<li class="tab-filter" type="rt">
			<a href="${basePath}router"><span class="glyphicon glyphicon-fullscreen"></span>路由器</a>
		</li>
		<li class="tab-filter active" type="vlan">
			<a href="${basePath}vnet"><span class="glyphicon glyphicon-barcode"></span>私有网络</a>
		</li>
	</ul>
	<div class="once-pane">
		<div class="alert alert-info" id="once-tab-title">
			私有网络&nbsp;(vLans)&nbsp;用于主机之间互联，它类似物理世界中使用交换机&nbsp;(L2 Switch)&nbsp;组成的局域网。私有网络之间是100%隔离的。
		</div>
		<div class="once-toolbar">
			<button class="btn btn-default btn-refresh">
				<span class="glyphicon glyphicon-refresh" style="margin-right: 0"></span>
			</button>
			<button id="create" class="btn btn-primary"
				url="${basePath}vnet/create">
				+&nbsp;新建</button>	
			<div class="btn-group">
				<button class="btn btn-default dropdown-toggle"
					data-toggle="dropdown">
					更多操作... <span class="caret" style="margin-left: 15px"></span>
				</button>
				<ul class="dropdown-menu">
					<li><a class="btn-forbidden" id="addvm" url="${basePath}vnet/bindvm"><span class="glyphicon glyphicon-cloud"></span>添加主机</a></li>
					<li><a class="btn-forbidden" id="link" url="${basePath}vnet/linkrouter"><span class="glyphicon glyphicon-cloud-upload"></span>连接路由器</a></li>
		           <li><a class="btn-forbidden" id="unlink"><span class="glyphicon glyphicon-cloud-download"></span>离开路由器</a></li>
		           <li><a class="btn-forbidden" id="delete"><span class="glyphicon glyphicon-trash"></span>删除</a></li>
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
				<tr id="tablehead">
				    <th width="4%"></th>
					<th width="24%">ID</th>
					<th width="24%">名称</th>
					<th width="24%">路由器</th>
					<th width="24%">运行时间</th>
				</tr>
			</thead>
			<tbody id="tablebody">
			</tbody>
		</table>
	</div>
    <div id="VnetModalContainer" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>
</div>