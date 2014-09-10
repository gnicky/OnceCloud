$("#AlarmModalContainer").on("hidden", function() {
			$(this).removeData("modal");
			$(this).children().remove();
		});

$("#wizard").bwizard({
			backBtnText : "",
			nextBtnText : ""
		});

$('.btn-back').on('click', function(event) {
			event.preventDefault();
			$("#wizard").bwizard("back");
		});

$('.first-next').on('click', function(event) {
			event.preventDefault();
			if ($('#paramsetting-form').valid()) {
				$("#wizard").bwizard("next");
			}
		});

$('.li-disable').unbind();

$('.second-next').on('click', function(event) {
			event.preventDefault();
			$("#wizard").bwizard("next");
		});

$('#createAlarmAction').on('click', function(event) {
	event.preventDefault();
	var alarmUuid = uuid.v4();
	var alarmName = $("#alarm_name").val();
	var alarmType = $("#policy").find("option:selected").val();
	var alarmIsalarm = $('input[type="radio"]:checked').val();
	var alarmTouch = -1;
	if (alarmIsalarm == "1") {
		$("input[name='trigger_status']:checked").each(function() {
					alarmTouch += parseInt($(this).val());
				});
	} else {
		$("input[name='trigger_status']:checked").each(function() {
					$(this).attr("checked", false);
				});
	}
	var alarmPeriod = $("#period").find("option:selected").val();
	var alarmRules = new Array();
	var alarmThreshold = new Array();
	var flag = false;
	$.each($("#rules li"), function() {
				var param1 = $(this).find("#meter").val();
				var param2 = $(this).find("#condition_type").val();
				var param3 = $(this).find("#count").val();
				alarmRules.push(parseInt(param1) + parseInt(param2));
				alarmThreshold.push(param3);
				if (param3 == "") {
					flag = true;
				}
			});
	if (flag) {
		bootbox.dialog({
			message : '<div class="alert alert-danger" style="margin:10px"><span class="glyphicon glyphicon-warning-sign"></span>&nbsp;所设置参数不能为空</div>',
			title : "提示",
			buttons : {
				main : {
					label : "确定",
					className : "btn-primary",
					callback : function() {
					}
				},
				cancel : {
					label : "取消",
					className : "btn-default",
					callback : function() {
						$('#AlarmModalContainer').modal('hide');
					}
				}
			}
		});
	} else {
		var alarmThresholdstr = alarmThreshold.toString();
		var alarmRulestr = alarmRules.toString();
		$.ajax({
			type : 'get',
			url : '/AlarmAction/Create',
			data : {
				alarmUuid : alarmUuid,
				alarmName : alarmName,
				alarmType : alarmType,
				alarmIsalarm : alarmIsalarm,
				alarmTouch : alarmTouch,
				alarmPeriod : alarmPeriod,
				alarmRules : alarmRulestr,
				alarmThreshold : alarmThresholdstr
			},
			dataType : 'text',
			success : function() {
				$('#AlarmModalContainer').modal('hide');
				bootbox.dialog({
					message : '<div class="alert alert-success" style="margin:10px"><span class="glyphicon glyphicon-warning-sign"></span>&nbsp;添加规则成功</div>',
					title : "提示",
					buttons : {
						main : {
							label : "确定",
							className : "btn-primary",
							callback : function() {
								var showuuid = "al-"
										+ alarmUuid.substring(0, 8);
								var showstr = "<a class='id'>" + showuuid
										+ '</a>';
								var stateStr = '<td><span class="icon-status icon-running" name="stateicon">'
										+ '</span><span name="stateword">活跃</span></td>';
								var _typeStr = new Array("主机", "公网ip", "路由器",
										"负载均衡器监听器HTTP协议", "负载均衡器监听器HTTP5协议",
										"负载均衡器监听器TCP协议", "负载均衡器后端HTTP协议",
										"负载均衡器后端TCP协议");
								var typeStr = _typeStr[alarmType];
								$("#tablebody")
										.prepend('<tr rowid="'
												+ alarmUuid
												+ '"><td class="rcheck"><input type="checkbox" name="alrow"></td><td name="console">'
												+ showstr
												+ '</td><td name="alarmName">'
												+ alarmName
												+ '</td>'
												+ stateStr
												+ '<td name="alarmType">'
												+ typeStr
												+ '</td><td name="alarmPeriod">'
												+ alarmPeriod
												+ '分钟</td><td name="alarmModify">是</td>'
												+ '<td name="createtime" class="time">1分钟以内</td></tr>');
							}
						}
					}
				});
			}
		});
	}
});

$('#paramsetting-form').validate({
			rules : {
				alarm_name : {
					required : true,
					maxlength : 20,
					legal: true
				}
			},
			messages : {
				alarm_name : {
					required : "<span class='unit'>名称不能为空</span>",
					maxlength : "<span class='unit'>名称不能超过20个字符</span>",
					legal: "<span class='unit'>名称包含非法字符</span>"
				}
			}
		});

function removeAllOptions() {
	$("#meter").empty();
	$("#rules li").not(":first").remove();
}

$("#policy").change(function() {
			removeAllOptions();
			if ($("#policy").find("option:selected").val() == "0") {
				$("#meter").append("<option value='0'>CPU利用率</option>");
				$("#alarm-unit").text("%");
				$("#meter").append("<option value='2'>内存利用率</option>");
				$("#meter").append("<option value='4'>磁盘使用量</option>");
				$("#meter").append("<option value='6'>内网进流量</option>");
				$("#meter").append("<option value='8'>内网出流量</option>");
			} else if ($("#policy").find("option:selected").val() == "1") {
				$("#meter").append("<option value='10'>外网进流量</option>");
				$("#alarm-unit").text("Mbps");
				$("#meter").append("<option value='12'>外网出流量</option>");
			} else if ($("#policy").find("option:selected").val() == "2") {
				$("#meter").append("<option value='20'>内网进流量</option>");
				$("#alarm-unit").text("Mbps");
				$("#meter").append("<option value='22'>内网出流量</option>");
			} else if ($("#policy").find("option:selected").val() == "3") {
				$("#meter").append("<option value='34'>响应延迟时间</option>");
				$("#alarm-unit").text("毫秒");
				$("#meter").append("<option value='30'>请求数</option>");
				$("#meter").append("<option value='32'>并发数</option>");
				$("#meter").append("<option value='36'>监听器 4xx 响应数</option>");
				$("#meter").append("<option value='38'>监听器 5xx 响应数</option>");
				$("#meter")
						.append("<option value='40'>后端 HTTP 1xx 响应数</option>");
				$("#meter")
						.append("<option value='42'>后端 HTTP 2xx 响应数</option>");
				$("#meter")
						.append("<option value='44'>后端 HTTP 3xx 响应数</option>");
				$("#meter")
						.append("<option value='46'>后端 HTTP 4xx 响应数</option>");
				$("#meter")
						.append("<option value='48'>后端 HTTP 5xx 响应数</option>");
			} else if ($("#policy").find("option:selected").val() == "4") {
				$("#meter").append("<option value='54'>响应延迟时间</option>");
				$("#alarm-unit").text("毫秒");
				$("#meter").append("<option value='50'>请求数</option>");
				$("#meter").append("<option value='52'>并发数</option>");
				$("#meter").append("<option value='56'>监听器 4xx 响应数</option>");
				$("#meter").append("<option value='58'>监听器 5xx 响应数</option>");
				$("#meter")
						.append("<option value='60'>后端 HTTP 1xx 响应数</option>");
				$("#meter")
						.append("<option value='62'>后端 HTTP 2xx 响应数</option>");
				$("#meter")
						.append("<option value='64'>后端 HTTP 3xx 响应数</option>");
				$("#meter")
						.append("<option value='66'>后端 HTTP 4xx 响应数</option>");
				$("#meter")
						.append("<option value='68'>后端 HTTP 5xx 响应数</option>");
			} else if ($("#policy").find("option:selected").val() == "5") {
				$("#meter").append("<option value='70'>并发数</option>");
				$("#meter").append("<option value='72'>连接数</option>");
				$("#alarm-unit").text("");
			} else if ($("#policy").find("option:selected").val() == "6") {
				$("#meter").append("<option value='80'>响应延迟时间</option>");
				$("#alarm-unit").text("毫秒");
				$("#meter").append("<option value='82'>HTTP 1xx 响应数</option>");
				$("#meter").append("<option value='84'>HTTP 2xx 响应数</option>");
				$("#meter").append("<option value='86'>HTTP 3xx 响应数</option>");
				$("#meter").append("<option value='88'>HTTP 4xx 响应数</option>");
				$("#meter").append("<option value='90'>HTTP 5xx 响应数</option>");
			} else if ($("#policy").find("option:selected").val() == "7") {
				$("#meter").append("<option value='100'>连接数</option>");
				$("#alarm-unit").text("");
			}
		});

$("#add-rule").on('click', function(event) {
	event.preventDefault();
	$("#rules li:first").clone(true).appendTo("#rules");
	$("#rules li").not(":first").find("#remove-rule").show();
	$("#rules li:first").find("#remove-rule").hide();
	var str = $("#rules li:last").find("#meter").val();
	if (str == 0 || str == 2 || str == 4) {
		$("#rules li:last").find("#alarm-unit").text("%");
	} else if (str == 6 || str == 8 || str == 10 || str == 12 || str == 20
			|| str == 22) {
		$("#rules li:last").find("#alarm-unit").text("Mbps");
	} else if (str == 34 || str == 54 || str == 80) {
		$("#rules li:last").find("#alarm-unit").text("毫秒");
	} else if (str == 32 || str == 52 || str == 70 || str == 72 || str == 100) {
		$("#rules li:last").find("#alarm-unit").text("");
	} else {
		$("#rules li:last").find("#alarm-unit").text("次/分钟");
	}
});

$("#remove-rule").on('click', function(event) {
			event.preventDefault();
			if ($("#rules li").length > 1) {
				$(this).parent().remove();
			}
		});

$("#meter").change(function() {
	var str = $(this).val();
	if (str == 0 || str == 2 || str == 4) {
		$(this).parent().parent().find("#alarm-unit").text("%");
	} else if (str == 6 || str == 8 || str == 10 || str == 12 || str == 20
			|| str == 22) {
		$(this).parent().parent().find("#alarm-unit").text("Mbps");
	} else if (str == 34 || str == 54 || str == 80) {
		$(this).parent().parent().find("#alarm-unit").text("毫秒");
	} else if (str == 32 || str == 52 || str == 70 || str == 72 || str == 100) {
		$(this).parent().parent().find("#alarm-unit").text("");
	} else {
		$(this).parent().parent().find("#alarm-unit").text("次/分钟");
	}
});

$(":radio").click(function() {
			if ($(this).val() == 1) {
				$("#advanced-options").show();
			} else {
				$("#advanced-options").hide();
			}
		});