<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content" id="platformcontent">
	<div class="intro">
		<h1>地址池&nbsp;Address Pool</h1>
		<p class="lead" style="margin-top:10px">
			<em>地址池&nbsp;(Address Pool)</em>维护云平台中的DHCP映射列表以及公网IP资源池，使得云平台中的虚拟机可以接入内部局域网和绑定公网IP。
		</p>
	</div>
    <div class="once-pane">
    	<div class="once-toolbar">
			<button class="btn btn-default btn-refresh"><span class="glyphicon glyphicon-refresh" style="margin-right:0"></span></button>
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
        <table class="table table-bordered once-table views-assetsQuota">
			<thead id="tablethead">
				
			</thead>
			<tbody id="tablebody">
			</tbody>
		</table>
	</div>
</div>