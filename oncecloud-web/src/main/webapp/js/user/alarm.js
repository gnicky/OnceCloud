reloadList(1);

function reloadList(page) {
    var limit = $('#limit').val();
    var search = $('#search').val();
    getAlarmList(page, limit, search);
    if (page == 1) {
        options = {
            currentPage: 1
        };
        $('#pageDivider').bootstrapPaginator(options);
    }
    allDisable();
}

function removeAllCheck() {
    $('input[name="alrow"]:checked').each(function () {
        $(this)[0].checked = false;
        $(this).change();
    });
}

function allDisable() {
    $("#destroy").addClass('btn-forbidden');
    $("#bindalarm").addClass('btn-forbidden');
}

$('#tablebody').on('change', 'input:checkbox', function (event) {
    event.preventDefault();
    allDisable();
    var total = 0;
    $('input[name="alrow"]:checked').each(function () {
        total++;
    });
    if (total == 1) {
        $("#destroy").removeClass('btn-forbidden');
        $("#bindalarm").removeClass('btn-forbidden');
    }
});

$('#create').on('click', function (event) {
    event.preventDefault();
    $('#AlarmModalContainer').load($(this).attr('url'), '', function () {
        $('#AlarmModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
});

function getInfoList() {
    var infoList = "";
    $('input[name="alrow"]:checked').each(function () {
        infoList += "[al-" + $(this).parent().parent().attr("rowid").substring(0, 8) + "]&nbsp;";
    });
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
                    $('input[name="alrow"]:checked').each(function () {
                        var uuid = $(this).parent().parent().attr("rowid");
                        destroyAlarm(uuid);
                    });
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

$('#destroy').on('click', function (event) {
    event.preventDefault();
    showbox();
});

$('#tablebody').on('click', '.id', function (event) {
    event.preventDefault();
    var uuid = $(this).parent().parent().attr('rowid');
    var form = $("<form></form>");
    form.attr("action","/alarm/detail");
    form.attr('method','post');
    var input = $('<input type="text" name="alarmUuid" value="' + uuid + '" />');
    form.append(input);
    form.css('display','none');
    form.appendTo($('body'));
    form.submit();
});

function getAlarmList(page, limit, search) {
    $('#tablebody').html("");
    $.ajax({
        type: 'get',
        url: '/AlarmAction/AlarmList',
        data: {page: page, limit: limit, search: search},
        dataType: 'json',
        success: function (array) {
            var totalnum = array[0];
            var totalp = 1;
            if (totalnum != 0) {
                totalp = Math.ceil(totalnum / limit);
            }
            options = {
                totalPages: totalp
            };
            $('#pageDivider').bootstrapPaginator(options);
            pageDisplayUpdate(page, totalp);
            var tableStr = "";
            for (var i = 1; i < array.length; i++) {
                var obj = array[i];
                var alarmUuid = obj.alarmUuid;
                var alarmName = decodeURI(obj.alarmName);
                var alarmStatus = obj.alarmStatus;
                var stateStr = "";
                var showuuid = "al-" + alarmUuid.substring(0, 8);
                var showstr = "<a class='id'>" + showuuid + '</a>';
                var iconStr = new Array("stopped", "running");
                var nameStr = new Array("禁用", "活跃");
                stateStr = '<td><span class="icon-status icon-' + iconStr[alarmStatus] + '" name="stateicon">'
                    + '</span><span name="stateword">' + nameStr[alarmStatus] + '</span></td>';
                var _typeStr = new Array("主机", "公网ip", "路由器", "负载均衡器监听器HTTP协议", "负载均衡器监听器HTTP5协议", "负载均衡器监听器TCP协议", "负载均衡器后端HTTP协议", "负载均衡器后端TCP协议");
                var alarmType = obj.alarmType;
                var typeStr = _typeStr[alarmType];
                var alarmPeriod = obj.alarmPeriod;
                var _modifyStr = new Array("否", "是");
                var alarmModify = obj.alarmModify;
                var modifyStr = _modifyStr[alarmModify];
                var alarmDate = decodeURI(obj.alarmDate);
                alarmDate = alarmDate.substring(0, alarmDate.length - 2);
                alarmDate = alarmDate.replace(/%3A/g, ":");
                var thistr = '<tr rowid="' + alarmUuid + '"><td class="rcheck"><input type="checkbox" name="alrow"></td><td name="console">' + showstr + '</td><td name="alarmName">'
                    + alarmName + '</td>' + stateStr + '<td name="alarmType">'
                    + typeStr + '</td><td name="alarmPeriod">'
                    + alarmPeriod + '分钟</td><td name="alarmModify">' + modifyStr + '</td>' + '<td name="createtime" class="time">' + alarmDate + '</td></tr>';
                tableStr += thistr;
            }
            $('#tablebody').html(tableStr);
        }
    });
}

function destroyAlarm(uuid) {
    $.ajax({
        type: 'get',
        url: '/AlarmAction',
        data: 'action=destroy&uuid=' + uuid,
        dataType: 'json',
        success: function (result) {
            if (result) {
                $("#tablebody").find('[rowid="' + uuid + '"]').remove();
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
        }
    });
}

$('#bindalarm').on('click', function (event) {
    event.preventDefault();
    var uuid = "";
    $('input[name="alrow"]:checked').each(function () {
        uuid = $(this).parent().parent().attr("rowid");
    });
    $('#AlarmModalContainer').load($(this).attr('url'), {"uuid": uuid}, function () {
        $('#AlarmModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
});