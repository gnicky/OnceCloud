/**
 * 
 */
$("#pfsubmit").on("click", function() {
	var pfName = $("#pf_name").val();
	var protocol = $("#period").find("option:selected").text();
	var srcPort = $("#pf_srcport").val();
	var destIP = $("#pf_desIp").val();
	var destPort = $("#pf_desport").val();
	var srcIP = $("#platformcontent").attr("rtIp");
	if ($("#forwardport-form").valid() && checkIP(destIP)) {
		console.log("say hello");
		$.ajax({
			type : "post",
			url : "/RouterAction/AddPortForwarding",
			data : {
				protocol : protocol,
				srcIP : srcIP,
				srcPort : srcPort,
				destIP : destIP,
				destPort : destPort,
				pfName : pfName
			},
			dataType : "json",
			success : function(json) {
				var uuid = json.uuid;
				var thistr = '<tr pfuuid="'
						+ uuid
						+ '" pfrouter="'
						+ srcIP
						+ '" protocol="'
						+ protocol
						+ '" srcPort="'
						+ srcPort
						+ '" destIP="'
						+ destIP
						+ '" destPort="'
						+ destPort
						+ '">'
						+ '<td class="rcheck"><input type="checkbox" name="rulerow"></td><td>'
						+ pfName + '</td><td>' + protocol + '</td><td>'
						+ srcPort + '</td><td>' + destIP + '</td><td>'
						+ destPort + '</td></tr>';
				$('#tablebody').prepend(thistr);
			}
		});
		$('#RouterModalContainer').modal('hide');
	}
});

function checkIP(internalIP) {
	var routerUuid = $('#platformcontent').attr("routerUuid");
	var result = false;
	$.ajax({
		type: "post",
		url: "/RouterAction/CheckVnetIP",
		data: {routerUuid:routerUuid,internalIP:internalIP},
		dataType: "json",
		async : false,
		success: function(obj) {
			$.each(obj, function(index, json){
				var ip = '192.168.' + json.vnet + '.*';
				var reg = new RegExp(ip, "g");
				var destIP = $("#pf_desIp").val();
				result |= reg.test(destIP);
			});
			if (!result) {
				bootbox.dialog({
                        message: '<div class="alert alert-danger" style="margin:10px"><span class="glyphicon glyphicon-warning-sign"></span>&nbsp;内网IP不在该路由所管制范围内，请重新填写</div>',
                        title: "提示",
                        buttons: {
                            main: {
                                label: "确定",
                                className: "btn-primary",
                                callback: function () {
                                }
                            }
                        }
                    });
			}
		}
	});
	console.log(result);
	return result;
}

$("#forwardport-form").validate({
			rules : {
				pf_name : {
					required : true,
					maxlength : 20,
					legal : true
				},
				pf_srcport : {
					required : true,
					digits : true,
					range : [1, 65535]
				},
				pf_desIp : {
					required : true,
					ip : true
				},
				pf_desport : {
					required : true,
					digits : true,
					range : [1, 65535]
				}
			},
			messages : {
				pf_name : {
					required : "<span class='help'>名称不能为空</span>",
					maxlength : "<span class='help'>名称不能超过20个字符</span>",
					legal : "<span class='unit'>名称包含非法字符</span>"
				},
				pf_srcport : {
					required : "<span class='unit'>端口不能为空</span>",
					digits : "<span class='unit'>端口必须是1~65535之间的整数</span>",
					range : "<span class='unit'>端口必须是1~65535之间的整数</span>"
				},
				pf_desIp : {
					required : "<span class='unit'>内网IP不能为空</span>",
					ip : "<span class='unit'>请输入合法IP地址</span>"
				},
				pf_desport : {
					required : "<span class='unit'>端口不能为空</span>",
					digits : "<span class='unit'>端口必须是1~65535之间的整数</span>",
					range : "<span class='unit'>端口必须是1~65535之间的整数</span>"
				}
			}
		});