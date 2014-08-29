$(document).ready(function () {

    $("#RuleModalContainer").on("hidden", function () {
        $(this).removeData("modal");
        $(this).children().remove();
    });

    $("#createruleAction").on('click', function (event) {
        event.preventDefault();
        var valid = $("#createrule-form").valid();
        if (valid) {
            var ruleName = $("#rule_name").val();
            var rulePriority = $("#rule_priority").val();
            var ruleProtocol = $("#rule_protocol option:selected").val();
            var ruleSport = $("#rule_sport").val();
            var ruleEport = $("#rule_eport").val();
            var ruleIp = $("#rule_ip").val();
            var ruleId = uuid.v4();
            var firewallId = $('#platformcontent').attr("firewallId");
            $.ajax({
                type: 'post',
                url: '/FirewallAction',
                data: 'action=createrule&ruleName=' + ruleName + '&rulePriority=' + rulePriority
                    + '&ruleProtocol=' + ruleProtocol + '&ruleSport=' + ruleSport + '&ruleEport=' + ruleEport
                    + '&ruleIp=' + ruleIp + '&firewallId=' + firewallId + '&ruleId=' + ruleId,
                dataType: 'json',
                success: function (obj) {
                    if (obj.isSuccess) {
                        if (ruleIp == "") {
                            ruleIp = "所有地址";
                        }
                        $("#tablebody").append('<tr ruleid="' + ruleId + '" state="1"><td class="rcheck"><input type="checkbox" name="rulerow"></td><td name="rulename">'
                            + ruleName + '</td><td name="priority">' + rulePriority + '</td><td name="protocol">'
                            + ruleProtocol + '</td><td name="sport">' + ruleSport + '</td><td name="eport">'
                            + ruleEport + '</td><td name="ip">' + ruleIp + '</td><td class="operate"><a>禁用</a></td></tr>');
                    }
                    $("#confirm").removeClass('btn-default').addClass('btn-primary');
                    $("#suggestion").show();
                    $("#RuleModalContainer").modal('hide');
                },
                error: function () {
                }
            });
        }
    });

    $("#rule_protocol").change(function () {
        if ($(this).val() == "ICMP") {
            $("#rule_sport").addClass('oc-disable').attr('disabled', "disabled");
            $("#rule_eport").addClass('oc-disable').attr('disabled', "disabled");
        } else {
            $("#rule_sport").removeClass('oc-disable').removeAttr('disabled');
            $("#rule_eport").removeClass('oc-disable').removeAttr('disabled');
        }
    });

    $("#rule_sport").on('focusout', function (event) {
        event.preventDefault();
        if (/^\d+$/.test($('#rule_sport').val()) && /^\d+$/.test($('#rule_eport').val())) {
            var sport = parseInt($('#rule_sport').val());
            var eport = parseInt($('#rule_eport').val());
            if (sport > eport) {
                $('#rule_eport').val(sport);
            }
        }
    });

    $("#rule_eport").on('focusout', function (event) {
        event.preventDefault();
        if (/^\d+$/.test($('#rule_sport').val()) && /^\d+$/.test($('#rule_eport').val())) {
            var sport = parseInt($('#rule_sport').val());
            var eport = parseInt($('#rule_eport').val());
            if (sport > eport) {
                $('#rule_eport').val(sport);
            }
        }
    });

    $("#createrule-form").validate({
        rules: {
            rule_name: {
                required: true,
                maxlength: 16
            },
            rule_priority: {
                required: true,
                digits: true,
                range: [0, 100]
            },
            rule_sport: {
                required: true,
                digits: true,
                range: [1, 65535]
            },
            rule_eport: {
                required: true,
                digits: true,
                range: [1, 65535]
            },
            rule_ip: {
                networkSegment: true
            }
        },
        messages: {
            rule_name: {
                required: "<span class='unit'>规则名不能为空</span>",
                maxlength: "<span class='unit'>不能超过16个字符</span>"
            },
            rule_priority: {
                required: "<span class='unit'>优先级不能为空</span>",
                digits: "<span class='unit'>优先级必须是整数</span>",
                range: "<span class='unit'>优先级必须在0到100之间</span>"
            },
            rule_sport: {
                required: "<span class='unit'>起始端口不能为空</span>",
                digits: "<span class='unit'>起始端口必须是整数</span>",
                range: "<span class='unit'>起始端口必须在1到65535之间</span>"
            },
            rule_eport: {
                required: "<span class='unit'>结束端口不能为空</span>",
                digits: "<span class='unit'>结束端口必须是整数</span>",
                range: "<span class='unit'>结束端口必须在1到65535之间</span>"
            },
            rule_ip: {
                networkSegment: "<span class='unit'>请输入规范的网段</span>"
            }
        }
    });
});