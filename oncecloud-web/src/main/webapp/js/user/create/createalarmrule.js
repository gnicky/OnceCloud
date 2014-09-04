$(document).ready(function () {
    initselect();

    function initselect() {
        var type = $("#modal-type").attr("alatype");
        if (type == "0") {
            $("#rule_protocol").append("<option value='0'>CPU利用率</option>");
            $("#alarm-unit").text("%");
            $("#rule_protocol").append("<option value='2'>内存利用率</option>");
            $("#rule_protocol").append("<option value='4'>磁盘使用量</option>");
            $("#rule_protocol").append("<option value='6'>内网进流量</option>");
            $("#rule_protocol").append("<option value='8'>内网出流量</option>");
        } else if (type == "1") {
            $("#rule_protocol").append("<option value='10'>外网进流量</option>");
            $("#alarm-unit").text("Mbps");
            $("#rule_protocol").append("<option value='12'>外网出流量</option>");
        } else if (type == "2") {
            $("#rule_protocol").append("<option value='20'>内网进流量</option>");
            $("#alarm-unit").text("Mbps");
            $("#rule_protocol").append("<option value='22'>内网出流量</option>");
        } else if (type == "3") {
            $("#rule_protocol").append("<option value='34'>响应延迟时间</option>");
            $("#alarm-unit").text("毫秒");
            $("#rule_protocol").append("<option value='30'>请求数</option>");
            $("#rule_protocol").append("<option value='32'>并发数</option>");
            $("#rule_protocol").append("<option value='36'>监听器 4xx 响应数</option>");
            $("#rule_protocol").append("<option value='38'>监听器 5xx 响应数</option>");
            $("#rule_protocol").append("<option value='40'>后端 HTTP 1xx 响应数</option>");
            $("#rule_protocol").append("<option value='42'>后端 HTTP 2xx 响应数</option>");
            $("#rule_protocol").append("<option value='44'>后端 HTTP 3xx 响应数</option>");
            $("#rule_protocol").append("<option value='46'>后端 HTTP 4xx 响应数</option>");
            $("#rule_protocol").append("<option value='48'>后端 HTTP 5xx 响应数</option>");
        } else if (type == "4") {
            $("#rule_protocol").append("<option value='54'>响应延迟时间</option>");
            $("#alarm-unit").text("毫秒");
            $("#rule_protocol").append("<option value='50'>请求数</option>");
            $("#rule_protocol").append("<option value='52'>并发数</option>");
            $("#rule_protocol").append("<option value='56'>监听器 4xx 响应数</option>");
            $("#rule_protocol").append("<option value='58'>监听器 5xx 响应数</option>");
            $("#rule_protocol").append("<option value='60'>后端 HTTP 1xx 响应数</option>");
            $("#rule_protocol").append("<option value='62'>后端 HTTP 2xx 响应数</option>");
            $("#rule_protocol").append("<option value='64'>后端 HTTP 3xx 响应数</option>");
            $("#rule_protocol").append("<option value='66'>后端 HTTP 4xx 响应数</option>");
            $("#rule_protocol").append("<option value='68'>后端 HTTP 5xx 响应数</option>");
        } else if (type == "5") {
            $("#rule_protocol").append("<option value='70'>并发数</option>");
            $("#rule_protocol").append("<option value='72'>连接数</option>");
            $("#alarm-unit").text("");
        } else if (type == "6") {
            $("#rule_protocol").append("<option value='80'>响应延迟时间</option>");
            $("#alarm-unit").text("毫秒");
            $("#rule_protocol").append("<option value='82'>HTTP 1xx 响应数</option>");
            $("#rule_protocol").append("<option value='84'>HTTP 2xx 响应数</option>");
            $("#rule_protocol").append("<option value='86'>HTTP 3xx 响应数</option>");
            $("#rule_protocol").append("<option value='88'>HTTP 4xx 响应数</option>");
            $("#rule_protocol").append("<option value='90'>HTTP 5xx 响应数</option>");
        } else if (type == "7") {
            $("#rule_protocol").append("<option value='100'>连接数</option>");
            $("#alarm-unit").text("");
        }
    }

    $("#rule_protocol").change(function () {
        var str = $(this).val();
        if (str == 0 || str == 2 || str == 4) {
            $("#alarm-unit").text("%");
        } else if (str == 6 || str == 8 || str == 10 || str == 12 || str == 20 || str == 22) {
            $("#alarm-unit").text("Mbps");
        } else if (str == 34 || str == 54 || str == 80) {
            $("#alarm-unit").text("毫秒");
        } else if (str == 32 || str == 52 || str == 70 || str == 72 || str == 100) {
            $("#alarm-unit").text("");
        } else {
            $("#alarm-unit").text("次/分钟");
        }
    });

    $("#rule_condition").change(function () {
        var str = $(this).val();
        if (str == "once") {
            $("#rperiod").hide();
        } else {
            $("#rperiod").show();
        }
    });

    $("#RuleModalContainer").on("hidden", function () {
        $(this).removeData("modal");
        $(this).children().remove();
    });

    $("#createruleAction").on('click', function (event) {
        event.preventDefault();
        var valid = $("#createrule-form").valid();
        if (valid) {
            var rule_protocol = $("#rule_protocol").find("option:selected").val();
            var ruleName = $("#rule_protocol").find("option:selected").text();
            var rule_mm = $("#rule_mm").find("option:selected").val();
            var rulePriority = $("#rule_mm").find("option:selected").text();
            var ruletype = parseInt(rule_protocol) + parseInt(rule_mm);
            var rulethreshold = $("#rule_threshold").val();
            var ruleperiod = $("#rule_period").val();
            var ruleId = uuid.v4();
            var alarmUuid = $('#platformcontent').attr("alarmUuid");
            $.ajax({
                type: 'post',
                url: '/AlarmAction/CreateRule',
                data : {
					ruletype : ruletype,
					rulethreshold : rulethreshold,
					ruleperiod : ruleperiod,
					ruleId : ruleId,
					alarmUuid : alarmUuid
				},
                dataType: 'json',
                success: function () {
                    $("#tablebody").append('<tr ruleid="' + ruleId + '"><td class="rcheck"><input type="checkbox" name="rulerow"></td><td name="rulename" id="rulename">'
                        + ruleName + '</td><td name="priority" id="priority">' + rulePriority + '</td><td name="protocol"><span id="protocol">'
                        + rulethreshold + '</span>&nbsp;<span id="unit">' + $("#alarm-unit").text() + '</span></td><td name="sport" id="sport">' + ruleperiod + '</td></tr>');
                    $("#RuleModalContainer").modal('hide');
                },
                error: function () {
                }
            });
        }
    });

    $("#createrule-form").validate({
        rules: {
            rule_threshold: {
                required: true
            },
            rule_period: {
                required: true,
                digits: true,
                range: [1, 100]
            }
        },
        messages: {
            rule_threshold: {
                required: "<span class='unit'>阈值不能为空</span>"
            },
            rule_period: {
                required: "<span class='unit'>周期不能为空</span>",
                digits: "<span class='unit'>周期必须是整数</span>",
                range: "<span class='unit'>周期必须在1到100之间</span>"
            }
        }
    });
});