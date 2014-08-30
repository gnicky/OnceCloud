$(document).ready(function () {

    $('#create, #image').on('click', function (event) {
        event.preventDefault();
        $('#InstanceModalContainer').load($(this).attr('url'), '', function () {
            $('#InstanceModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });

    var options = {
        bootstrapMajorVersion: 3,
        currentPage: 1,
        totalPages: 1,
        numberOfPages: 0,
        onPageClicked: function (e, originalEvent, type, page) {
            var limit = $('#limit').val();
            var search = $('#search').val();
            getAlarmList(page, limit, search);
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
    };

    $('#pageDivider').bootstrapPaginator(options);
    getAlarmList(1, 10, "");

    $('#limit').on('focusout', function () {
        var limit = $('#limit').val();
        var reg = /^[0-9]*[1-9][0-9]*$/;
        if (!reg.test(limit)) {
            $("#limit").val(10);
        }
        reloadList();
    });

    $('#search').on('focusout', function () {
        reloadList();
    });

    $('#search').keypress(function (e) {
        var key = e.which;
        if (key == 13) {
            reloadList();
        }
    });

    $('.btn-refresh').on('click', function (event) {
        event.preventDefault();
        reloadList();
    });

    function reloadList() {
        var limit = $('#limit').val();
        var search = $('#search').val();
        getAlarmList(1, limit, search);
        options = {
            currentPage: 1,
        }
        $('#pageDivider').bootstrapPaginator(options);
    }

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
        $.ajax({
            type: 'get',
            url: '/AlarmAction',
            data: 'action=detail&alarmUuid=' + uuid,
            dataType: 'text',
            success: function (response) {
                window.location.href = $('#platformcontent').attr('platformBasePath') + "user/detail/alarmdetail.jsp";
            },
            error: function () {
            }
        });
    });

    function getAlarmList(page, limit, search) {
        $('#tablebody').html("");
        $.ajax({
            type: 'get',
            url: '/AlarmAction',
            data: 'action=getlist&page=' + page + '&limit=' + limit + '&search=' + search,
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
            },
            error: function () {
            }
        });
        allDisable();
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
                            },
                        }
                    });
                }
            },
            error: function () {
            }
        });
    }

    function removeAllCheck() {
        $('input[name="alrow"]:checked').each(function () {
            $(this)[0].checked = false;
            $(this).change();
        });
    }

    $('#tablebody').on('change', 'input:checkbox', function (event) {
        event.preventDefault();
        allDisable();
        var total = 0;
        $('input[name="alrow"]:checked').each(function () {
            total++;
        });
        if (total == 1) {
            $("#destroy").attr('disabled', false).removeClass('btn-forbidden');
            $("#addresource").attr('disabled', false).removeClass('btn-forbidden');
        }
    });

    $('#addresource').on('click', function (event) {
        event.preventDefault();
        var uuid = "";
        $('input[name="alrow"]:checked').each(function () {
            uuid = $(this).parent().parent().attr("rowid");
        });
        $('#ResourceModalContainer').load($(this).attr('url'), {"uuid": uuid}, function () {
            $('#ResourceModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });

    function allDisable() {
        $("#destroy").attr("disabled", true).addClass('btn-forbidden');
        $("#addresource").attr("disabled", true).addClass('btn-forbidden');
    }

    function pageDisplayUpdate(current, total) {
        $('#currentP').html(current);
        $('#totalP').html(total);
    }
});