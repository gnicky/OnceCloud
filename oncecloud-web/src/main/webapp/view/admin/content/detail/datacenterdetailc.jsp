<%@page import="com.oncecloud.listener.UserSessionListener"%>
<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content" id="platformcontent">
	<div class="intro">
		<h1>数据中心&nbsp;Datacenters</h1>
		<p class="lead" style="margin-top:10px">
			<em>数据中心&nbsp;(Datacenter)</em>是云平台中最大的管理单元，它包含一到多个机架以及上面的服务器集群，并包含专线接入，散热，监控等多种配套设施，向外界提供稳定、优质、高效的云计算服务。
		</p>
	</div>
	<ul class="nav nav-tabs once-tab">
		<li class="tab-filter active" type="pooltab">
			<a href="javascript:void(0)"><span class="glyphicon glyphicon-tint"></span>资源池</a>
		</li>
		<li class="tab-filter" type="racktab">
			<a href="javascript:void(0)"><span class="glyphicon glyphicon-road"></span>机架</a>
		</li>
	</ul>
	<div id="line"></div>
   	<div id="tagdiv"></div>
	</div>
</div>
		