$(document).ready(function () {
  
    fillBlank();
   

    function fillBlank() {
            var hostid = '';
            var hostip = '';
    
            $('input[name="hostrow"]:checked').each(function () {
                hostid = $(this).parent().parent().attr("hostid");
                hostip = $(this).parent().parent().attr("hostip");
            });
            $('#modalcontent').attr('hostid', hostid);

            $('#host_uuid').val("host-"+hostid.substring(0, 8));
            $('#host_ip').val(hostip);
            
            $.ajax({
                type: 'post',
                url: '/PowerAction/PowerHost',
                data: {
                	hostid: hostid,
                },
                dataType: 'json',
                success: function (power) {
                	$("#power_uuid").val(power.powerUuid);
                	$("#motherboard_ip").val(power.motherboardIP);
                	$("#port").val(power.powerPort);
                	$("#power_username").val(power.powerUsername);
                	$("#power_pwd").val(power.powerPassword);
                	if(power.powerValid==0)
            		{
            	       $("#server_status").val("未验证/验证不通过");
            	       $("#startServerAction").attr("disabled","disabled");
            	       $("#closeServerAction").attr("disabled","disabled");
            		}
                	else
            		{
                		checkStatus(power.powerUuid,hostid,power.motherboardIP,power.powerPort,power.powerUsername,power.powerPassword);
            		}
                }
            });
            
          
        }
    
    $("#validAction").click(function(){
    	 if($("#create-form").valid())
		 {
    		 checkStatus($("#power_uuid").val(), $('#modalcontent').attr('hostid'),$("#motherboard_ip").val(),$("#port").val(),$("#power_username").val(),$("#power_pwd").val());
		 }
    });
    
    
    function checkStatus(powerid,hostid,hostmotherip,hostport,hostusername,hostpsw,valid)
    {
    	  $.ajax({
              type: 'post',
              url: '/PowerAction/PowerStatus',
              data: {
            	  powerid:powerid,
            	  hostid:hostid,
            	  hostip: hostmotherip,
            	  hostport:hostport,
            	  hostusername:hostusername,
            	  hostpsw:hostpsw,
              },
              dataType: 'json',
              success: function (powerstatus) {
              	//console.log(powerstatus);
              	if(powerstatus==-1)
          		{
          	       $("#server_status").val("验证不通过");
          	       $("#startServerAction").attr("disabled","disabled");
          	       $("#closeServerAction").attr("disabled","disabled");
          		}
              	else
          		{
              		if(powerstatus==0)
          			{
	          			 $("#server_status").val("关机");
	            	     $("#closeServerAction").attr("disabled","disabled");
	            	     $("#startServerAction").removeAttr("disabled");
          			}
              		if(powerstatus==1)
          			{
	          			 $("#server_status").val("运行中");
	          			 $("#startServerAction").attr("disabled","disabled");
	          			 $("#closeServerAction").removeAttr("disabled","disabled");
          			}
              		
          		}
              }
          });
    }
    

    $('#startServerAction').on('click', function (event) {
        event.preventDefault();
        if ($("#create-form").valid()) {
           
                $.ajax({
                    type: 'post',
                    url: '/PowerAction/StartPower',
                    data: {
                   	  hostip: $("#motherboard_ip").val(),
                   	  hostport:$("#port").val(),
                   	  hostusername:$("#power_username").val(),
                   	  hostpsw:$("#power_pwd").val(),
                    },
                    dataType: 'json',
                    success: function (array) {
                    	if(array)
                		{
                		   alert("启动服务器成功");
                		   $('#ServerModalContainer').modal('hide');
                		}
                    	else
                    	{
                    	   alert("启动服务器失败");
                    	}
                    }
                });
            } 
    });
    
    $('#closeServerAction').on('click', function (event) {
        event.preventDefault();
        if ($("#create-form").valid()) {
           
                $.ajax({
                    type: 'post',
                    url: '/PowerAction/ShutDownPower',
                    data: {
                   	  hostip: $("#motherboard_ip").val(),
                   	  hostport:$("#port").val(),
                   	  hostusername:$("#power_username").val(),
                   	  hostpsw:$("#power_pwd").val(),
                    },
                    dataType: 'json',
                    success: function (array) {
                    	if(array)
                		{
                		   alert("关闭服务器成功");
                		   $('#ServerModalContainer').modal('hide');
                		}
                    	else
                    	{
                    	   alert("关闭服务器失败");
                    	}
                    }
                });
            } 
    });


    $("#create-form").validate({
        rules: {
        	motherboard_ip: {
                required: true,
            },
            port: {
                required: true,
                digits:true,
            },
            power_username: {
                required: true,
            },
            power_pwd: {
                required: true,
            }
        },
        messages: {
        	motherboard_ip: {
                required: "<span class='help'>请填写服务器主板IP</span>",
            },
            port: {
                required: "<span class='help'>请填写服务器端口</span>",
                digits:"<span class='help'>端口为整数</span>",
            },
            power_username: {
                required: "<span class='help'>请填写账号</span>",
            },
            power_pwd: {
                required: "<span class='help'>请填写密码</span>",
            }
        }
    });
});