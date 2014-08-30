<%@page import="com.oncecloud.listener.UserSessionListener"%>
<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content" id="platformcontent" platformUserId="<%=user.getUserId()%>" platformBasePath="<%=basePath %>">
	<div class="intro">
		<h1>网络拓扑图&nbsp;Switch</h1>
		<p class="lead" style="margin-top:10px">
			<em>网络拓扑图&nbsp;(Switch)</em>网络拓扑图绘制编辑
		</p>
	</div>
	<br/>
   <div class="row" style="margin:0px;">
   <div class="col-md-2" >
   <div id="left" class="bigdiv">
	   <div class="node" id="node1">服务器</div>
	             <div class="node" id="node2">交换机</div>
	             <div class="node" id="node3">存储集群</div>
	            <div class="node" id="node4">测试</div></div>
	            </div>
    <div class="col-md-10">
    <div id="right" class="bigdiv">
     <!--  <div id="sasa"  style="width:50px;" class="node">拖拉区域</div></div></div> -->
   </div>
</div>
		