$(document).ready(function () {
    getAlarmBasic();
    var alaType;
    $('#createrule').on('click', function (event) {
        event.preventDefault();
        $('#RuleModalContainer').load($(this).attr('url'), {"alarmType": alaType}, function () {
            $('#RuleModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });

    $('#confirm').on('click', function (event) {
        event.preventDefault();
        var ruleId = "";
        var ruleName = "";
        var ruleprio = "";
        var ruleprot = "";
        var rulesport = "";
        var unit = "";
        $('input[name="rulerow"]:checked').each(function () {
            ruleId = $(this).parent().parent().attr("ruleid");
            ruleName = $(this).parent().parent().find("#rulename").text();
            ruleprio = $(this).parent().parent().find("#priority").text();
            ruleprot = $(this).parent().parent().find("#protocol").text();
            rulesport = $(this).parent().parent().find("#sport").text();
            unit = $(this).parent().parent().find("#unit").text();
        });
        if (ruleprio == "<") {
            ruleprio = "&lt;";
        } else {
            ruleprio = "&gt;";
        }
        var url = basePath + 'user/modal/modifyalarmrule';
        $('#RuleModalContainer').load(url, {"alarmType": alaType, "ruleId": ruleId, "ruleName": ruleName,
            "ruleprio": ruleprio, "ruleprot": ruleprot, "rulesport": rulesport, "unit": unit}, function () {
            $('#RuleModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });

    $('#basic-modify').on('click', function (event) {
        event.preventDefault();
        var url = basePath + 'common/modify';
        var alName = $('#alName').text();
        var alDesc = $('#alDesc').text();
        var alarmUuid = $('#platformcontent').attr("alarmUuid");
        $('#RuleModalContainer').load(url, {"modifyType": "al", "modifyUuid": alarmUuid,
            "modifyName": alName, "modifyDesc": alDesc}, function () {
            $('#RuleModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });

    $('#basic-period').on('click', function (event) {
        event.preventDefault();
        var url = basePath + 'user/modal/modifyperiod';
        var alarmUuid = $('#platformcontent').attr("alarmUuid");
        $('#RuleModalContainer').load(url, {"alarmUuid": alarmUuid}, function () {
            $('#RuleModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });

    $('#basic-touch').on('click', function (event) {
        event.preventDefault();
        var url = basePath + 'user/modal/modifyalert';
        var alarmUuid = $('#platformcontent').attr("alarmUuid");
        $('#RuleModalContainer').load(url, {"alarmUuid": alarmUuid}, function () {
            $('#RuleModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });

    $('#basic-resource').on('click', function (event) {
        event.preventDefault();
        var url = basePath+ 'user/modal/bindalarm';
        var alarmUuid = $('#platformcontent').attr("alarmUuid");
        $('#ResourceModalContainer').load(url, {"uuid": alarmUuid}, function () {
            $('#ResourceModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });

    function getInfoList() {
        var infoList = "";
        infoList += "[al-" + $('#platformcontent').attr("alarmUuid").substring(0, 8) + "]&nbsp;";
        return infoList;
    }

    function showbox() {
        var infoList = getInfoList();
        var showMessage = '';
        var showTitle = '';
        showMessage = '<div class="alert alert-info" style="margin:10px">'
            + '<span class="glyphicon glyphicon-info-sign"></span>&nbsp;确定要删除警告策略&nbsp;' + infoList + '?</div>';
        showTitle = '提示';

        bootbox.dialog({
            className: "oc-bootbox",
            message: showMessage,
            title: showTitle,
            buttons: {
                main: {
                    label: "确定",
                    className: "btn-primary",
                    callback: function () {
                        var uuid = $('#platformcontent').attr("alarmUuid");
                        destroyAlarm(uuid);
                        removeAllCheck();
                    }
                },
                cancel: {
                    label: "取消",
                    className: "btn-default",
                    callback: function () {
                        removeAllCheck();
                    }
                }
            }
        });
    }

    $('#basic-remove').on('click', function (event) {
        event.preventDefault();
        showbox();
    });

    function destroyAlarm(uuid) {
        $.ajax({
            type: 'get',
            url: '/AlarmAction/Destory',
            data: {uuid:uuid},
            dataType: 'json',
            success: function (result) {
                if (result) {
                    window.location.href = basePath + "user/alarm";
                } else {
                    bootbox.dialog({
                        message: '<div class="alert alert-danger" style="margin:10px"><span class="glyphicon glyphicon-warning-sign"></span>&nbsp;该策略已绑定资源，请先卸载资源</div>',
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
            },
            error: function () {
            }
        });
    }

    $('#basic-unbind').on('click', function (event) {
        event.preventDefault();
        var showMessage = '';
        var showTitle = '';
        showMessage = '<div class="alert alert-info" style="margin:10px">'
            + '<span class="glyphicon glyphicon-info-sign"></span>&nbsp;确定要解除所有资源&nbsp;?</div>';
        showTitle = '提示';

        bootbox.dialog({
            className: "oc-bootbox",
            message: showMessage,
            title: showTitle,
            buttons: {
                main: {
                    label: "确定",
                    className: "btn-primary",
                    callback: function () {
                        $.each($("#rsTable div #remove-resource"), function () {
                            $(this).click();
                        });
                    }
                },
                cancel: {
                    label: "取消",
                    className: "btn-default",
                    callback: function () {
                        removeAllCheck();
                    }
                }
            }
        });
    });

    var options = {
        bootstrapMajorVersion: 3,
        currentPage: 1,
        totalPages: 1,
        numberOfPages: 0,
        onPageClicked: function (e, originalEvent, type, page) {
            var limit = $('#limit').val();
            var alarmUuid = $('#platformcontent').attr("alarmUuid");
            getRuleList(page, limit, "", alarmUuid);
        },
        shouldShowPage: function (type, page, current) {
            switch (type) {
                case "first":
                case "last":
                    return false;
                default:
                    return true;
            }
        }
    }
    $('#pageDivider').bootstrapPaginator(options);
    getRuleList(1, 10, "", $('#platformcontent').attr("alarmUuid"));

    $('#limit').on('focusout', function () {
        var limit = $('#limit').val();
        var reg = /^[0-9]*[1-9][0-9]*$/;
        if (!reg.test(limit)) {
            $('#limit').val(10);
        }
        reloadList();
    });

    function getAlarmBasic() {
        var alarmUuid = $('#platformcontent').attr("alarmUuid");
        $('#basic-list').html("");
        $.ajax({
            type: 'get',
            url: '/AlarmAction/GetAlarm',
            data: {alarmUuid:alarmUuid},
            dataType: 'json',
            success: function (obj) {
                var showid = "al-" + alarmUuid.substring(0, 8);
                var alarmName = decodeURI(obj.alarmName);
                var alarmStatus = obj.alarmStatus;
                var stateStr = "";
                var iconStr = new Array("stopped", "running");
                var nameStr = new Array("禁用", "活跃");
                stateStr = '<span class="icon-status icon-' + iconStr[alarmStatus] + '" name="stateicon">'
                    + '</span><span name="stateword">' + nameStr[alarmStatus] + '</span>';
                var _typeStr = new Array("主机", "公网ip", "路由器", "负载均衡器监听器HTTP协议", "负载均衡器监听器HTTP5协议", "负载均衡器监听器TCP协议", "负载均衡器后端HTTP协议", "负载均衡器后端TCP协议");
                var alarmType = obj.alarmType;
                alaType = alarmType;
                var typeStr = _typeStr[alarmType];
                var alarmPeriod = obj.alarmPeriod;
                var alarmDate = decodeURI(obj.alarmDate);
                alarmDate = alarmDate.substring(0, alarmDate.length - 2);
                alarmDate = alarmDate.replace(/%3A/g, ":");
                var rsTable = '';
                $.each(obj.alarmResource, function (index, json) {
                    rsTable += '<div style="padding-left:102px"><a>' + json.rsName +
                        '</a><a id="remove-resource" rsUuid="' + json.rsUuid + '" rsType="' + alarmType + '"><span class="glyphicon glyphicon-remove delete-resource"></span></a></div>';
                });
                if (rsTable == '') {
                    rsTable = '<span class="none">尚无资源</span>';
                }
                var alarmDesc = decodeURI(obj.alarmDesc);
                alarmDesc = alarmDesc.replace(/%2F/g, "/");
                var alarmTouch = obj.alarmTouch;
                var alarmIsalarm = obj.alarmIsalarm;
                $('#basic-list').html('<dt>ID</dt><dd><a href="javascript:void(0)">'
                    + showid + '</a></dd><dt>名称</dt><dd id="alName">'
                    + alarmName + '</dd><dt>描述</dt><dd id="alDesc">' + alarmDesc + '</dd><dt>状态</dt><dd>' + stateStr + '</dd>'
                    + '<dt id="resourceType">资源类型</dt><dd>' + typeStr + '</dd><dt>监控周期</dt><dd id="alPeriod">' + alarmPeriod
                    + '分钟</dd><dt>资源列表</dt><dd id="rsTable">' + rsTable + '</dd><dt>是否通知</dt><dd id="isalarm"></dd><dt id="iscondition" style="dispaly:none">通知条件</dt><dd id="iscontent" style="dispaly:none"></dd>' +
                    '<dt>创建时间</dt><dd class="none">' + alarmDate + '</dd>');
                if (alarmIsalarm == "0") {
                    $("#isalarm").text("否");
                    $("#iscondition").hide();
                    $("#iscontent").hide();
                } else {
                    $("#isalarm").text("是");
                    $("#iscondition").show();
                    $("#iscontent").show();
                    if (alarmTouch == "0") {
                        $("#iscontent").text("资源出现异常时");
                        $("#iscontent").removeClass("none");
                    } else if (alarmTouch == "1") {
                        $("#iscontent").text("资源恢复正常时");
                        $("#iscontent").removeClass("none");
                    } else if (alarmTouch == "2") {
                        $("#iscontent").text("资源异常及恢复正常时");
                        $("#iscontent").removeClass("none");
                    } else {
                        $("#iscontent").text("尚未置设");
                        $("#iscontent").addClass("none");
                    }
                }
            },
            error: function () {
            }
        });
    }

    $("#basic-list").on('click', '#remove-resource', function (event) {
        var rsUuid = $(this).attr("rsUuid");
        var rsType = $(this).attr("rsType");
        var othis = $(this);
        $.ajax({
            type: 'get',
            url: '/AlarmAction/Remove',
            data: {rsUuid:rsUuid, rsType:rsType},
            dataType: 'text',
            success: function (obj) {
                othis.parent().remove();
                if ($("#rsTable").html() == "") {
                    $("#rsTable").html('<span class="none">尚无资源</span>');
                }
            }
        });
    });

    $('.btn-refresh').on('click', function (event) {
        event.preventDefault();
        getAlarmBasic();
    });

    $('.rule-refresh').on('click', function (event) {
        event.preventDefault();
        reloadList();
    });

    function reloadList() {
        var limit = $('#limit').val();
        var alarmUuid = $('#platformcontent').attr("alarmUuid");
        getRuleList(1, limit, "", alarmUuid);
        options = {
            currentPage: 1
        }
        $('#pageDivider').bootstrapPaginator(options);
    }

    function getRuleList(page, limit, search, alarmUuid) {
        $('#tablebody').html("");
        $.ajax({
            type: 'get',
            url: '/AlarmAction/RuleList',
            data: {alarmUuid:alarmUuid, page:page, limit:limit},
            dataType: 'json',
            success: function (array) {
                var totalnum = array[0];
                var totalp = 1;
                if (totalnum != 0) {
                    totalp = Math.ceil(totalnum / limit);
                }
                options = {
                    totalPages: totalp
                }
                $('#pageDivider').bootstrapPaginator(options);
                pageDisplayUpdate(page, totalp);
                var tableStr = "";
                for (var i = 1; i < array.length; i++) {
                    var obj = array[i];
                    var ruleId = obj.ruleUuid;
                    var ruleName = "";
                    var unit = "";
                    var rn = parseInt(obj.ruleType);
                    var rp = rn % 2;
                    var rulePriority = "";
                    if (rp == 0) {
                        rulePriority = "<";
                    } else {
                        rulePriority = ">";
                    }
                    rn -= rp;
                    if (rn == 0) {
                        ruleName = "CPU利用率";
                        unit = "%";
                    } else if (rn == 2) {
                        ruleName = "内存利用率";
                        unit = "%";
                    } else if (rn == 4) {
                        ruleName = "磁盘使用量";
                        unit = "%";
                    } else if (rn == 6) {
                        ruleName = "内网进流量";
                        unit = "Mbps";
                    } else if (rn == 8) {
                        ruleName = "内网出流量";
                        unit = "Mbps";
                    } else if (rn == 10) {
                        ruleName = "外网进流量";
                        unit = "Mbps";
                    } else if (rn == 12) {
                        ruleName = "外网出流量";
                        unit = "Mbps";
                    } else if (rn == 20) {
                        ruleName = "内网进流量";
                        unit = "Mbps";
                    } else if (rn == 22) {
                        ruleName = "内网出流量";
                        unit = "Mbps";
                    } else if (rn == 34) {
                        ruleName = "响应延迟时间";
                        unit = "毫秒";
                    } else if (rn == 30) {
                        ruleName = "请求数";
                        unit = "次/分钟";
                    } else if (rn == 32) {
                        ruleName = "并发数";
                        unit = "";
                    } else if (rn == 36) {
                        ruleName = "监听器 4xx 响应数";
                        unit = "次/分钟";
                    } else if (rn == 38) {
                        ruleName = "监听器 5xx 响应数";
                        unit = "次/分钟";
                    } else if (rn == 40) {
                        ruleName = "后端 HTTP 1xx 响应数";
                        unit = "次/分钟";
                    } else if (rn == 42) {
                        ruleName = "后端 HTTP 2xx 响应数";
                        unit = "次/分钟";
                    } else if (rn == 44) {
                        ruleName = "后端 HTTP 3xx 响应数";
                        unit = "次/分钟";
                    } else if (rn == 46) {
                        ruleName = "后端 HTTP 4xx 响应数";
                        unit = "次/分钟";
                    } else if (rn == 48) {
                        ruleName = "后端 HTTP 5xx 响应数";
                        unit = "次/分钟";
                    } else if (rn == 54) {
                        ruleName = "响应延迟时间";
                        unit = "毫秒";
                    } else if (rn == 50) {
                        ruleName = "请求数";
                        unit = "次/分钟";
                    } else if (rn == 52) {
                        ruleName = "并发数";
                        unit = "";
                    } else if (rn == 56) {
                        ruleName = "监听器 4xx 响应数";
                        unit = "次/分钟";
                    } else if (rn == 58) {
                        ruleName = "监听器 5xx 响应数";
                        unit = "次/分钟";
                    } else if (rn == 60) {
                        ruleName = "后端 HTTP 1xx 响应数";
                        unit = "次/分钟";
                    } else if (rn == 62) {
                        ruleName = "后端 HTTP 2xx 响应数";
                        unit = "次/分钟";
                    } else if (rn == 64) {
                        ruleName = "后端 HTTP 3xx 响应数";
                        unit = "次/分钟";
                    } else if (rn == 66) {
                        ruleName = "后端 HTTP 4xx 响应数";
                        unit = "次/分钟";
                    } else if (rn == 68) {
                        ruleName = "后端 HTTP 5xx 响应数";
                        unit = "次/分钟";
                    } else if (rn == 70) {
                        ruleName = "并发数";
                        unit = "";
                    } else if (rn == 72) {
                        ruleName = "连接数";
                        unit = "";
                    } else if (rn == 80) {
                        ruleName = "响应延迟时间";
                        unit = "毫秒";
                    } else if (rn == 82) {
                        ruleName = "HTTP 1xx 响应数";
                        unit = "次/分钟";
                    } else if (rn == 84) {
                        ruleName = "HTTP 2xx 响应数";
                        unit = "次/分钟";
                    } else if (rn == 86) {
                        ruleName = "HTTP 3xx 响应数";
                        unit = "次/分钟";
                    } else if (rn == 88) {
                        ruleName = "HTTP 4xx 响应数";
                        unit = "次/分钟";
                    } else if (rn == 90) {
                        ruleName = "HTTP 5xx 响应数";
                        unit = "次/分钟";
                    } else if (rn == 100) {
                        ruleName = "连接数";
                        unit = "";
                    }
                    var rulethreshold = obj.rulePeriod;
                    var ruleperiod = obj.ruleThreshold;
                    var thistr = '<tr ruleid="' + ruleId + '"><td class="rcheck"><input type="checkbox" name="rulerow"></td><td name="rulename" id="rulename">'
                        + ruleName + '</td><td name="priority" id="priority">' + rulePriority + '</td><td name="protocol"><span  id="protocol">'
                        + ruleperiod + '</span>&nbsp;<sapn id="unit">' + unit + '</span></td><td name="sport" id="sport">' + rulethreshold + '</td></tr>';
                    tableStr += thistr;
                }
                $('#tablebody').html(tableStr);
            },
            error: function () {
            }
        });
    }

    $('#deleterule').on('click', function (event) {
        event.preventDefault();
        $('input[name="rulerow"]:checked').each(function () {
            var ruleId = $(this).parent().parent().attr("ruleid");
            deleteRule(ruleId);
        });
        removeAllCheck();
    });

    function deleteRule(ruleId) {
        var thistr = $("#tablebody").find('[ruleid="' + ruleId + '"]');
        $.ajax({
            type: 'get',
            url: '/AlarmAction/DeleteRule',
            data: {ruleId:ruleId},
            dataType: 'text',
            complete: function () {
                $(thistr).remove();
                $('#deleterule').addClass('btn-disable').attr('disabled', true);
                getAlarmBasic();
            }
        });
    }

    $('#tablebody').on('change', 'input:checkbox', function (event) {
        event.preventDefault();
        var count = 0;
        $('input[name="rulerow"]:checked').each(function () {
            count++;
        });
        if (count == 0) {
            $('#deleterule').addClass('btn-disable').attr('disabled', true);
            $('#confirm').addClass('btn-disable').attr('disabled', true);
        } else if (count == 1) {
            $('#confirm').removeClass('btn-disable').attr('disabled', false);
            $('#deleterule').removeClass('btn-disable').attr('disabled', false);
        } else {
            $('#deleterule').removeClass('btn-disable').attr('disabled', false);
            $('#confirm').addClass('btn-disable').attr('disabled', true);
        }
    });

    function pageDisplayUpdate(current, total) {
        $('#currentP').html(current);
        $('#totalP').html(total);
    }

    function removeAllCheck() {
        $('input[name="rulerow"]:checked').each(function () {
            $(this)[0].checked = false;
            $(this).change();
        });
    }
});