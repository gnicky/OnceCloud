<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content" id="platformcontent">
	<div class="intro">
		<h1>用户资源财务报表&nbsp;Report</h1>
		<p class="lead" style="margin-top:10px">
			<em>用户资源财务报表&nbsp;(Report)</em>
		</p>
	</div>
	 <div id="mainreport">
	 <div class="row" style="margin:0px;background: #eee;padding: 5px 0;font-size: 16px;font-weight: normal;padding: 10px;">
	  <div class="col-md-2">用户列表下拉
	 </div>
	 <div class="col-md-4">
	  <select id="userId">
		   <option>苏州文化博览中心有限公司</option>
		   <option>苏州工业园区清源华衍水务有限公司</option>
		   <option>苏州工业园区邻里中心发展有限公司</option>
		   <option>苏州国科数据中心有限公司</option>
	   </select></div>
	 </div>
	 <br/>
	  <div class="row" style="margin:0px">
	  <div class="col-md-6" id="picnet"></div>
	  <div class="col-md-6" id="piccpu"></div>
	 </div>
	 
	 <div class="row" style="margin:0px">
	  <div class="col-md-6" id="picmemory"></div>
	  <div class="col-md-6" id="picdisk"></div>
	 </div>
	 
	 <div class="row" style="margin:0px">
	  <div class="col-md-12" id="picmoney"></div>
	 </div>
	 </div>
</div>