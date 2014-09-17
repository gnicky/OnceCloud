<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content" id="platformcontent" novnc="${vncServer}" platformBasePath="${basePath}">
	<div class="intro">
		<h1>主机&nbsp;Instances</h1>
		<p class="lead">
			云平台为您提供一种随时获取的、弹性的计算能力，这种计算能力的体现就是<em>主机&nbsp;(Instance)</em>。主机就是一台配置好了的服务器，它有您期望的硬件配置、操作系统和网络配置。通常情况下，您的请求可以在60秒左右的时间内完成，所以您完全可以动态地、按需使用计算能力。
		</p>
	</div>
	<div class="once-pane">
		<div class="once-toolbar">
			<button class="btn btn-default btn-refresh"><span class="glyphicon glyphicon-refresh" style="margin-right:0"></span></button>
			<button id="create" class="btn btn-primary" url="${basePath}instance/create">+&nbsp;新建</button>
			<button class="btn btn-default btn-disable" id="startup" disabled><span class="glyphicon glyphicon-play"></span>启动</button>
			<button class="btn btn-default btn-disable" id="shutdown" disabled><span class="glyphicon glyphicon-stop"></span>关机</button>
			<div class="btn-group">
				<button class="btn btn-default dropdown-toggle" data-toggle="dropdown">更多操作...
					<span class="caret" style="margin-left:15px"></span>
				</button>
				<ul class="dropdown-menu">
					<li><a class="btn-forbidden" id="restart"><span class="glyphicon glyphicon-repeat"></span>重启</a></li>
					<li><a class="btn-forbidden" id="addtovlan" url="${basePath}instance/bindnetwork"><span class="glyphicon glyphicon-barcode"></span>加入网络</a></li>
					<li><a class="btn-forbidden backup" id="backup" url="${basePath}snapshot/create?rsid=null&rstype=null&rsname=null"><span class="glyphicon glyphicon-camera"></span>备份</a></li>
					<li><a class="btn-forbidden" id="image" url="${basePath}image/clone?rsid="><span class="glyphicon glyphicon-record"></span>制作映像</a></li>
					<li><a class="btn-forbidden" id="destroy"><span class="glyphicon glyphicon-trash"></span>销毁</a></li>
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
							<div><ul class="pagination" id="pageDivider" style="display:inline"></ul></div>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<table class="table table-bordered once-table">
			<thead>
				<tr>
					<th width="4%"><!-- checkbox --></th>
					<th width="12%">ID</th>
					<th width="10%">名称</th>
					<th width="10%">状态</th>
					<th width="6%">CPU</th>
					<th width="6%">内存</th>
					<th width="18%">网络</th>
					<th width="14%">公网IP</th>
					<th width="10%">上次备份时间</th>
					<th width="10%">运行时间</th>
				</tr>
			</thead>
			<tbody id="tablebody">
			</tbody>
		</table>
	</div>
	<div id="InstanceModalContainer" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>
</div>