<%@page import="com.oncecloud.listener.UserSessionListener"%>
<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<div class="content" id="platformcontent" basePath="${basePath}" uuid="${switchid}">
	<div class="intro">
		<h1>交换机&nbsp;Switch</h1>
		<p class="lead" style="margin-top:10px">
			<em>交换机&nbsp;(Switch)</em>网络核心交换机的使用详情
		</p>
	</div>

	<div id="line"></div>
   <div id="tagdiv" class="main"></div>
   
   <br/>
    <br/>
   <div class="once-pane">
    <table class="table table-bordered once-table">
			<thead>
				<tr>
					<th width="10%">端口编号</th>
					<th width="15%">所属交换机</th>
					<th width="10%">所属Vlan</th>
                    <th width="10%">状态</th>
             		<th width="10%">当前速率</th>
                    <th width="25%">连接服务器</th>
					<th width="20%">连接网口</th>
				</tr>
			</thead>
			<tbody id="tablebody">
			</tbody>
		</table>
		</div>
	  <!-- 
    <div class="switch">
  
    <div class="switch_left"></div>
    <div class="switch_mid">
        <div class="interface">
            <ul>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
				 <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
              </ul>
              <ul>
                     <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
				 <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                </ul>
                
                <ul>
                       <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
				 <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                </ul>
                  
       
        </div>
        <div class="clear_both"></div>
 
        </div>
    <div class="switch_right"></div>
      <div class="switch_title">交换机一</div>
    </div>
    
    <div class="switch">
  
    <div class="switch_left"></div>
    <div class="switch_mid">
        <div class="interface">
            <ul>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
				 <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
              </ul>
              <ul>
                     <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
				 <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                </ul>
                
                <ul>
                       <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
				 <li> <a ></a><a></a></li>
                  <li> <a ></a><a></a></li>
                </ul>
                  
       
        </div>
        <div class="clear_both"></div>
          </div>
    <div class="switch_right"></div>
      <div class="switch_title">交换机一</div>
    </div>-->
        
	</div>
</div>
		