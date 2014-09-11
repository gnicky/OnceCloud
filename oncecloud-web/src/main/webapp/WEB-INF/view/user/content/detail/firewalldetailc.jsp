<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content detail" id="platformcontent" firewallId="${firewallId}">
	<div class="intro">
		<h1>防火墙&nbsp;Security&nbsp;Groups</h1>
		<p class="lead">
			为了加强位于基础网络中的主机的安全性，您可以在主机之前放置<em>防火墙&nbsp;(Security&nbsp;Group)</em>。您可以自建更多的防火墙。自建防火墙在初始状态下，所有端口都是封闭的，您需要建立规则以打开相应的端口。
		</p>
	</div>
	<ol class="breadcrumb oc-crumb">
		<li><a href="${basePath}firewall"><span class="glyphicon glyphicon-flash cool-green"></span><span class="ctext">FIREWALL</span></a></li>
		<li class="active"><a href="${basePath}firewall/detail">${showId}</a></li>
	</ol>
	<div class="col-md-4">
		<div class="detail-item">
			<div class="title">
				<h3>基本属性&nbsp;<a href="javascript:void(0)" class="btn-refresh"><span class="glyphicon glyphicon-refresh"></span></a>
					<!-- <div class="btn-group">
						<button class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							<span class="glyphicon glyphicon-tasks"></span>
						</button>
						<ul class="dropdown-menu">
						</ul>
					</div> -->
				</h3>
			</div>
			<dl id="basic-list" class="my-horizontal"></dl>
		</div>
	</div>
	<div class="col-md-8">
		<div class="detail-item detail-right">
			<div class="once-pane" style="padding:0">
				<div class="title"><h3 class="uppercase">规则</h3><span class="oc-update" id="suggestion" style="display: none;">修改尚未更新，请点击"应用修改"</span></div>
				<div class="once-toolbar">
					<button class="btn btn-default rule-refresh"><span class="glyphicon glyphicon-refresh" style="margin-right:0"></span></button>
					<button id="createrule" class="btn btn-default" url="${basePath}firewallrule/create">+&nbsp;新建</button>
					<button id="confirm" class="btn btn-default">应用修改</button>
					<button id="deleterule" class="btn btn-default btn-disable" disabled><span class="glyphicon glyphicon-trash"></span>删除</button>
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
							<th><!--checkbox--></th>
							<th>名称</th>
							<th>优先级</th>
							<th>协议</th>
							<th>起始端口</th>
							<th>结束端口</th>
							<th>源IP</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody id="tablebody">
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div id="RuleModalContainer" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>
</div>