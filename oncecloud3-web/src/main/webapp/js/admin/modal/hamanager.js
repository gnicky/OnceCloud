$(document).ready(function () {
  
    fillBlank();
   

    function fillBlank() {
            var poolid = '';
            var poolname = '';
    
            $('input[name="poolrow"]:checked').each(function () {
            	poolid = $(this).parent().parent().attr("poolid");
            	poolname = $(this).parent().parent().attr("poolname");
            });
            $('#modalcontent').attr('poolid', poolid);

            $('#pool_uuid').val("pool-"+poolid.substring(0, 8));
            $('#pool_name').val(poolname);
            
            $.ajax({
                type: 'post',
                url: '/PoolAction/PoolHa',
                data: {
                	poolUuid: poolid,
                },
                dataType: 'json',
                success: function (poolha) {
                	$("#master_ip").val(poolha.masterip);
                	$("#ha_path").val(poolha.hapath);
                }
            });
            
          
        }

    $('#startHaAction').on('click', function (event) {
        event.preventDefault();
        if ($("#create-form").valid()) {
           
                $.ajax({
                    type: 'post',
                    url: '/PoolAction/StartHa',
                    data: {
                    	poolUuid: $("#modalcontent").attr("poolid"),
                    	masterIP:$("#master_ip").val(),
                    	haPath:$("#ha_path").val(),
                    },
                    dataType: 'json',
                    complete: function (data) {
                      console.log(data);
             		   alert(data.responseText);
                    }
                });
            } 
    });
    
    $('#closeHaAction').on('click', function (event) {
        event.preventDefault();
        if ($("#create-form").valid()) {
           
                $.ajax({
                    type: 'post',
                    url: '/PoolAction/StopHa',
                    data: {
                    	poolUuid: $("#modalcontent").attr("poolid"),
                    	masterIP:$("#master_ip").val(),
                    	haPath:$("#ha_path").val(),
                    },
                    dataType: 'json',
                    complete: function (data) {
                    	
                		   alert(data.responseText);
           
                    }
                });
            } 
    });


    $("#create-form").validate({
        rules: {
        	ha_path: {
                required: true,
            }
        },
        messages: {
        	ha_path: {
                required: "<span class='help'>请填写高可用路径</span>",
            }
        }
    });
});