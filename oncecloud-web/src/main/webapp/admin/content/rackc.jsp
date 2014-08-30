<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content" id="platformcontent" platformUserId="<%=user.getUserId()%>" platformBasePath="<%=basePath %>">
	<div class="intro">
		<h1>机架&nbsp;Racks</h1>
		<p class="lead" style="margin-top:10px">
			<em>机架&nbsp;(Rack)</em>是冷轧钢板或合金制作的用来存放云平台服务器和存储设备的物件，可以提供对设备的保护，屏蔽电磁干扰，有序、整齐地排列设备，方便以后维护设备。
		</p>
	</div>
    <div class="once-pane">
    	<div class="once-toolbar">
			<button class="btn btn-default btn-refresh"><span class="glyphicon glyphicon-refresh" style="margin-right:0"></span></button>
			<button id="create" class="btn btn-primary" url="<%=basePath %>admin/create/createrack.jsp">
				+&nbsp;添加机架
			</button>
            <div class="btn-group">
				<button class="btn btn-default dropdown-toggle" data-toggle="dropdown">更多操作...
					<span class="caret" style="margin-left:15px"></span>
				</button>
				<ul class="dropdown-menu">
					<li><a class="btn-forbidden" id="update"  url="<%=basePath %>admin/create/createrack.jsp" disabled><span class="glyphicon glyphicon-pencil"></span>修改</a></li>
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
							<div>
								<ul id="pageDivider" style="display:inline">
								</ul>
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
					<th width="30%">名称</th>
                   <!--  <th width="20%">位置</th> -->
					<th width="30%">数据中心</th>
					<th width="20%">创建时间</th>
				</tr>
			</thead>
			<tbody id="tablebody">
			</tbody>
		</table>
	</div>
	<div id="RackModalContainer" type="" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>
    </div> 
</div>