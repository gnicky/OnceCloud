<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content" id="platformcontent">
	<div class="intro">
		<h1>主机&nbsp;Instances</h1>
		<p class="lead">
			云平台为您提供一种随时获取的、弹性的计算能力，这种计算能力的体现就是<em>主机&nbsp;(Instance)</em>。主机就是一台配置好了的服务器，它有您期望的硬件配置、操作系统和网络配置。通常情况下，您的请求可以在60秒左右的时间内完成，所以您完全可以动态地、按需使用计算能力。
		</p>
	</div>
	<div class="once-pane">
		<div class="once-toolbar">
			<button class="btn btn-default btn-refresh"><span class="glyphicon glyphicon-refresh" style="margin-right:0"></span></button>
			<div class="btn-group">
				<button class="btn btn-default dropdown-toggle" data-toggle="dropdown">更多操作...
					<span class="caret" style="margin-left:15px"></span>
				</button>
				<ul class="dropdown-menu">
					<li><a class="btn-forbidden" id="startup"><span class="glyphicon glyphicon-play"></span>启动</a></li>
					<li><a class="btn-forbidden" id="shutdown"><span class="glyphicon glyphicon-stop"></span>关机</a></li>
					<li><a id="creatVMISO"><span class="glyphicon glyphicon-record"></span>新建</a></li>
				</ul>
			</div>
			<div class="btn-group">
				<button class="btn btn-default dropdown-toggle" data-toggle="dropdown"><span  id="selecthost">选择服务器</span>
					<span class="caret" style="margin-left:15px"></span>
				</button>
				<ul class="dropdown-menu" id="select-server">
					<li><a id="hostall"><span class="glyphicon glyphicon-tasks"></span>全部</a></li>
				</ul>
			</div>
			<div class="btn-group">
				<button class="btn btn-default dropdown-toggle" data-toggle="dropdown"><span id="selectimportant">选择重要性</span>
					<span class="caret" style="margin-left:15px"></span>
				</button>
				<ul class="dropdown-menu" id="select-importance">
					<li><a id="all-star">全部</a></li>
					<li><a id="none-star"><span class="glyphicon glyphicon-star-empty"></span></a></li>
					<li><a id="one-star"><span class="glyphicon glyphicon-star"></span></a></li>
					<li><a id="two-star"><span class="glyphicon glyphicon-star"></span><span class="glyphicon glyphicon-star"></span></a></li>
					<li><a id="three-star"><span class="glyphicon glyphicon-star"></span><span class="glyphicon glyphicon-star"></span><span class="glyphicon glyphicon-star"></span></a></li>
					<li><a id="four-star"><span class="glyphicon glyphicon-star"></span><span class="glyphicon glyphicon-star"></span><span class="glyphicon glyphicon-star"></span><span class="glyphicon glyphicon-star"></span></a></li>
					<li><a id="five-star"><span class="glyphicon glyphicon-star"></span><span class="glyphicon glyphicon-star"></span><span class="glyphicon glyphicon-star"></span><span class="glyphicon glyphicon-star"></span><span class="glyphicon glyphicon-star"></span></a></li>
				</ul>
			</div>
			<div class="btn-group">
				<button class="btn btn-default dropdown-toggle" data-toggle="dropdown"><span id="selecttype">选择类型</span>
					<span class="caret" style="margin-left:15px"></span>
				</button>
				<ul class="dropdown-menu">
					<li><a id="select-instance"><span class="glyphicon glyphicon-cloud"></span>主机</a></li>
					<li><a id="select-router"><span class="glyphicon glyphicon-indent-left"></span>路由器</a></li>
					<li><a id="select-loadbalance"><span class="glyphicon glyphicon-random"></span>负载均衡</a></li>
				</ul>
			</div>
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
					<th width="12%">名称</th>
					<th width="10%">状态</th>
					<th>重要性</th>
					<th>用户</th>
					<th width="8%">CPU</th>
					<th width="8%">内存</th>
					<th width="18%">网络</th>
					<th>运行时间</th>
				</tr>
			</thead>
			<tbody id="tablebody">
			</tbody>
		</table>
	</div>
	<div id="InstanceModalContainer" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>
</div>