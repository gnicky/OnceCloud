<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content" id="platformcontent">
	<div class="intro">
		<h1>地址池&nbsp;Address Pool</h1>
		<p class="lead" style="margin-top:10px">
			<em>地址池&nbsp;(Address Pool)</em>维护云平台中的DHCP映射列表以及公网IP资源池，使得云平台中的虚拟机可以接入内部局域网和绑定公网IP。
		</p>
	</div>
	<ul class="nav nav-tabs once-tab">
    	<li class="tab-filter active" oc-type="dhcp"><a href="javascript:void(0)"><span class="glyphicon glyphicon-fullscreen"></span>DHCP</a></li>
  		<li class="tab-filter" oc-type="publicip"><a href="javascript:void(0)"><span class="glyphicon glyphicon-eye-open"></span>公网IP</a></li>
	</ul>
    <div class="once-pane">
    	<div class="once-toolbar">
			<button class="btn btn-default btn-refresh"><span class="glyphicon glyphicon-refresh" style="margin-right:0"></span></button>
			<button id="create" class="btn btn-primary" url="${basePath}admin/create/createaddress.jsp">
				+&nbsp;地址池
			</button>
            <div class="btn-group">
				<button class="btn btn-default dropdown-toggle" data-toggle="dropdown">更多操作...
					<span class="caret" style="margin-left:15px"></span>
				</button>
				<ul class="dropdown-menu">
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
			<thead id="tablethead">
				
			</thead>
			<tbody id="tablebody">
			</tbody>
		</table>
	</div>
	<div id="IPModalContainer" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>
    </div> 
</div>