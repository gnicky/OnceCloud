reloadList(1);

function reloadList(page) {
    var limit = $('#limit').val();
    var search = $('#search').val();
    getLBList(page, limit, search);
    if (page == 1) {
        options = {
            currentPage: 1
        };
        $('#pageDivider').bootstrapPaginator(options);
    }
    allDisable();
}

function removeAllCheck() {
    $('input[name="lbrow"]:checked').each(function () {
        $(this)[0].checked = false;
        $(this).change();
    });
}

function allDisable() {
    $("#startup").addClass('btn-forbidden');
    $("#shutdown").addClass('btn-forbidden');
    $("#destroy").addClass('btn-forbidden');
}

$('#tablebody').on('change', 'input:checkbox', function (event) {
    event.preventDefault();
    allDisable();
    var running = 0;
    var process = 0;
    var stopped = 0;
    var total = 0;
    $('input[name="lbrow"]:checked').each(function () {
        var stateicon = $(this).parent().parent().find('[name="stateicon"]');
        if (stateicon.hasClass('icon-running')) {
            running++;
        } else if (stateicon.hasClass('icon-stopped')) {
            stopped++;
        } else {
            process++;
        }
        total++;
    });
    if (total > 0 && process == 0) {
        $("#destroy").removeClass('btn-forbidden');
        if (running > 0 && stopped == 0) {
            $("#shutdown").removeClass('btn-forbidden');
        } else if (running == 0 && stopped > 0) {
            $("#startup").removeClass('btn-forbidden');
        }
    }
});

$('#create').on('click', function (event) {
    event.preventDefault();
    $('#LBModalContainer').load($(this).attr('url'), '', function () {
        $('#LBModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
});

function getLBList(page, limit, search) {
    $('#tablebody').html("");
    $.ajax({
        type: 'get',
        url: '/LBAction/LBList',
        data: {page: page, limit: limit, search: search},
        dataType: 'json',
        success: function (array) {
            var total = array[0];
            var totalp = 1;
            if (total != 0) {
                totalp = Math.ceil(total / limit);
            }
            options = {
                totalPages: totalp
            };
            $('#pageDivider').bootstrapPaginator(options);
            pageDisplayUpdate(page, totalp);
            var tableStr = "";
            for (var i = 1; i < array.length; i++) {
                var obj = array[i];
                var uuid = obj.uuid;
                var name = decodeURI(obj.name);
                var power = obj.power;
                var showid = "lb-" + uuid.substring(0, 8);
                var iconStr = new Array("stopped", "running", "process", "process", "process", "process");
                var nameStr = new Array("已关机", "活跃", "创建中", "销毁中", "启动中", "关机中");
                var stateStr = '<td><span class="icon-status icon-' + iconStr[power] + '" name="stateicon">'
                    + '</span><span name="stateword">' + nameStr[power] + '</span></td>';
                var publicip = obj.eip;
                if (publicip != "") {
                    publicip = '<a><span class="glyphicon glyphicon-globe"></span>&nbsp;&nbsp;' + publicip + '</a>';
                }
                var thistr = '<tr rowid="' + uuid + '" lbname="' + name + '"><td class="rcheck"><input type="checkbox" name="lbrow"></td><td><a class="id">' + showid + '</a></td>'
                    + '<td>' + name + '</td>' + stateStr + '<td name="eip">' + publicip + '</td><td name="capacity">' + obj.capacity + '</td>'
                    + '<td class="time">' + decodeURI(obj.createdate) + '</td></tr>';
                tableStr += thistr;
            }
            $('#tablebody').html(tableStr);
        }
    });
}

$('#tablebody').on('click', '.id', function (event) {
    event.preventDefault();
    event.preventDefault();
    var uuid = $(this).parent().parent().attr('rowid');
    var form = $("<form></form>");
    form.attr("action","/loadbalance/detail");
    form.attr('method','post');
    var input = $('<input type="text" name="lbUuid" value="' + uuid + '" />');
    form.append(input);
    form.css('display','none');
    form.appendTo($('body'));
    form.submit();
});

function getInfoList() {
    var infoList = "";
    $('input[name="lbrow"]:checked').each(function () {
        infoList += "[lb-" + $(this).parent().parent().attr("rowid").substring(0, 8) + "]&nbsp;";
    });
    return infoList;
}

function showbox(type) {
    var infoList = getInfoList();
    var infoArray = new Array("启动负载均衡", "销毁负载均衡", "关闭负载均衡");
    var showMessage = '';
    var showTitle = '';
    if (type == 2) {
        showMessage = '<div class="alert alert-info" style="margin:10px 10px 0">1. 强制关机会丢失内存中的数据<br/>'
            + '2. 为保证数据的完整性，请在强制关机前暂停所有文件的写操作，或进行正常关机。</div>'
            + '<div class="item" style="margin:0"><div class="controls" style="margin-left:100px">'
            + '<label class="inline"><input type="checkbox" id="force">&nbsp;强制关机</label></div></div>';
        showTitle = infoArray[type] + '&nbsp;' + infoList + '?';
    } else {
        showMessage = '<div class="alert alert-info" style="margin:10px">'
            + '<span class="glyphicon glyphicon-info-sign"></span>&nbsp;'
            + infoArray[type] + '&nbsp;' + infoList + '?</div>';
        showTitle = '提示';
    }
    bootbox.dialog({
        className: "oc-bootbox",
        message: showMessage,
        title: showTitle,
        buttons: {
            main: {
                label: "确定",
                className: "btn-primary",
                callback: function () {
                    $('input[name="lbrow"]:checked').each(function () {
                        var uuid = $(this).parent().parent().attr("rowid");
                        if (type == 0) {
                            startLB(uuid);
                        } else if (type == 1) {
                            destroyLB(uuid);
                        } else if (type == 2) {
                            var force = $('#force')[0].checked;
                            shutdownLB(uuid, force);
                        }
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

$('#startup').on('click', function (event) {
    event.preventDefault();
    showbox(0);
});
$('#destroy').on('click', function (event) {
    event.preventDefault();
    showbox(1);
});
$('#shutdown').on('click', function (event) {
    event.preventDefault();
    showbox(2);
});

function getRuleList(page, limit, search) {
    $('#tablebody').html("");
}

function startLB(uuid) {
    var thistr = $("#tablebody").find('[rowid="' + uuid + '"]');
    thistr.find('[name="stateicon"]').removeClass("icon-stopped");
    thistr.find('[name="stateicon"]').addClass('icon-process');
    thistr.find('[name="stateword"]').text('启动中');
    $.ajax({
        type: 'get',
        url: '/LBAction',
        data: 'action=startup&uuid=' + uuid,
        dataType: 'json',
        success: function (array) {
        }
    });
}

function destroyLB(uuid) {
    var thistr = $("#tablebody").find('[rowid="' + uuid + '"]');
    var thisicon = thistr.find('[name="stateicon"]');
    thisicon.removeClass("icon-stopped");
    thisicon.removeClass("icon-running");
    thisicon.addClass('icon-process');
    thistr.find('[name="stateword"]').text('销毁中');
    $.ajax({
        type: 'get',
        url: '/LBAction',
        data: 'action=destroy&uuid=' + uuid,
        dataType: 'json',
        success: function (array) {
        }
    });
}

function shutdownLB(uuid, force) {
    var thistr = $("#tablebody").find('[rowid="' + uuid + '"]');
    thistr.find('[name="stateicon"]').removeClass("icon-running");
    thistr.find('[name="stateicon"]').addClass('icon-process');
    thistr.find('[name="stateword"]').text('关机中');
    thistr.find('.console').remove();
    $.ajax({
        type: 'get',
        url: '/LBAction',
        data: 'action=shutdown&uuid=' + uuid + '&force=' + force,
        dataType: 'json',
        success: function (array) {
        }
    });
}