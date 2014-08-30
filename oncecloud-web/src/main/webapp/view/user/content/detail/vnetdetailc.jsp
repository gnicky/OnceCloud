<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%
	String vnid = "vn-" + vnUuid.substring(0, 8);
%>
<script src="<%=basePath%>js/jquery.validate.js"></script>
<script src="<%=basePath%>js/uuid.js"></script>
<div class="content detail" id="platformcontent" vnUuid="<%=vnUuid%>"
	platformBasePath="<%=basePath%>" routerUuid="<%=routerUuid %>">
	<div class="intro">
		<h1>网络&nbsp;Networks</h1>
		<p class="lead">
			通过云平台的SDN技术，您可以快速地搭建您专属的私有云环境。相比于基础网络而言，这个网络可以提供100%的安全隔离，而且有丰富的工具帮助您进行自动化管理。要使用私有网络，请创建一个路由器，然后再创建一个或多个私有网络并连接到这个路由器，最后创建主机并加入到这些私有网络即可。
		</p>
	</div>
	<ol class="breadcrumb oc-crumb">
		<li><a href="<%=basePath %>user/vnet.jsp"><span class="glyphicon glyphicon-barcode cool-green"></span><span class="ctext">VNET</span></a></li>
		<li class="active"><a href="<%=basePath %>user/detail/vnetdetail.jsp"><%=vnid %></a></li>
	</ol>
	<div class="col-md-4">
		<div class="detail-item">
			<div class="title">
				<h3>
					基本属性&nbsp;<a href="javascript:void(0)" class="btn-refresh"><span
						class="glyphicon glyphicon-refresh"></span> </a>
					<div class="btn-group">
						<button class="btn btn-default dropdown-toggle"
							data-toggle="dropdown">
							<span class="glyphicon glyphicon-tasks"></span>
						</button>
						<ul class="dropdown-menu">
							<li><a id="modify"><span
									class="glyphicon glyphicon-pencil"></span>修改</a>
							</li>
						</ul>
					</div>
				</h3>
			</div>
			<dl id="basic-list" class="my-horizontal"></dl>
		</div>
	</div>
	<div class="col-md-8">
		<div class="detail-item detail-right">
			<div class="title">
				<h3 class="uppercase">主机
					<a href="javascript:void(0)" class="list-refresh">
						<span class="glyphicon glyphicon-refresh"></span>
					</a>
				</h3>
			</div>
			<%
				if (!routerUuid.equals("null")) {
			%>
			<div class="col-div">
				<p class="alert alert-info">
					提示：更多&nbsp;DHCP&nbsp;服务设置，请前往所属&nbsp;<a class="" id="router-a">路由器功能</a>&nbsp;页面进行操作。
				</p>
			</div>
			<%
				}
			%>
			<div class="vxnets" id="vxnets-t"></div>
		</div>
	</div>
	<div id="VnetModalContainer" type="edit" class="modal fade"
		tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
		aria-hidden="true"></div>
</div>
