initPage();
var host = "all";
var importance = 6;
var type = "instance";
reloadList(1);

function reloadList(page) {
    var limit = $('#limit').val();
    getVMList(page, limit);
    if (page == 1) {
        options = {
            currentPage: 1
        };
        $('#pageDivider').bootstrapPaginator(options);
    }
    allDisable();
}

function removeAllCheck() {
    $('input[name="vmrow"]:checked').each(function () {
        $(this)[0].checked = false;
        $(this).change();
    });
}

function allDisable() {
    $("#startup").attr("disabled", true).addClass('btn-forbidden');
    $("#shutdown").attr("disabled", true).addClass('btn-forbidden');
    $("#restart").attr("disabled", true).addClass('btn-forbidden');
    $("#destroy").attr("disabled", true).addClass('btn-forbidden');
}

$('#tablebody').on('change', 'input:checkbox', function (event) {
    event.preventDefault();
    allDisable();
    var running = 0;
    var process = 0;
    var stopped = 0;
    var total = 0;
    $('input[name="vmrow"]:checked').each(function () {
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
    if (total != 0 && process == 0) {
        $("#destroy").attr('disabled', false).removeClass('btn-forbidden');
        if (running > 0 && stopped == 0) {
            $("#shutdown").attr('disabled', false).removeClass('btn-forbidden');
            $("#restart").attr('disabled', false).removeClass('btn-forbidden');
        } else if (running == 0 && stopped > 0) {
            $("#startup").attr('disabled', false).removeClass('btn-forbidden');
        }
    }
});

$("#all-star").on("click", function () {
    $("#selectimportant").text("全部");
    importance = 6;
});

$("#none-star").on("click", function () {
    $("#selectimportant").text("未标星");
    importance = 0;
});

$("#one-star").on("click", function () {
    $("#selectimportant").text("一颗星");
    importance = 1;
});

$("#two-star").on("click", function () {
    $("#selectimportant").text("二颗星");
    importance = 2;
});

$("#three-star").on("click", function () {
    $("#selectimportant").text("三颗星");
    importance = 3;
});

$("#four-star").on("click", function () {
    $("#selectimportant").text("四颗星");
    importance = 4;
});

$("#five-star").on("click", function () {
    $("#selectimportant").text("五颗星");
    importance = 5;
});

$("#select-instance").on("click", function () {
    $("#selecttype").text("主机");
    type = "instance";
});

$("#select-router").on("click", function () {
    $("#selecttype").text("路由器");
    type = "router";
});

$("#select-loadbalance").on("click", function () {
    $("#selecttype").text("负载均衡");
    type = "loadbalance";
});

function getInfoList() {
    var infoList = "";
    var typeStr = "i";
    if (type == "router") {
        typeStr = "rt";
    } else if (type == "loadbalance") {
        typeStr = "lb";
    }
    $('input[name="vmrow"]:checked').each(function () {
        infoList += "[" + typeStr + "-" + $(this).parent().parent().attr("rowid").substring(0, 8) + "]&nbsp;";
    });
    return infoList;
}

function showbox(type) {
    var infoList = getInfoList();
    var infoArray = new Array("启动主机", "关闭主机");
    var showMessage = '';
    var showTitle = '';
    if (type == 1) {
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
                    $('input[name="vmrow"]:checked').each(function () {
                        var uuid = $(this).parent().parent().attr("rowid");
                        if (type == 0) {
                            startVM(uuid);
                        } else if (type == 1) {
                            var force = $('#force')[0].checked;
                            shutdownVM(uuid, force);
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

$('#shutdown').on('click', function (event) {
    event.preventDefault();
    showbox(1);
});

function loadList(action, page, limit, str) {
    $('#tablebody').html("");
    $.ajax({
        type: 'get',
        url: action,
        data: {page: page, limit: limit, host: host, importance: importance, type: type},
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
                var vmuuid = obj.vmid;
                var vmName = decodeURI(obj.vmname);
                var userName = decodeURI(obj.userName);
                var state = obj.state;
                var showuuid = str + vmuuid.substring(0, 8);
                var showstr = "<a class='id'>" + showuuid + '</a>';
                var iconStr = new Array("stopped", "running", "process", "process", "process", "process", "process");
                var nameStr = new Array("已关机", "正常运行", "创建中", "销毁中", "启动中", "关机中", "重启中");
                var stateStr = '<td><span class="icon-status icon-' + iconStr[state] + '" name="stateicon">'
                    + '</span><span name="stateword">' + nameStr[state] + '</span></td>';
                if (state == 1 && str == "i-") {
                    showstr = showstr + '<a class="console" data-uuid=' + vmuuid + '><img src="../../img/user/console.png"></a>';
                }
                var cpu = obj.cpu;
                cpu = cpu + "&nbsp;核";
                var memory = obj.memory;
                if (memory < 1024) {
                    memory = memory + "&nbsp;MB";
                } else {
                    memory = memory / 1024 + "&nbsp;GB";
                }
                var ip = obj.ip;
                var vlan = obj.vlan;
                var network;
                if (ip == "null") {
                    if (vlan == "null" || vlan == undefined) {
                        network = '<a>(基础网络)</a>';
                    } else {
                        network = '<a>(' + vlan + ')</a>';
                    }
                } else {
                    if (vlan == "null" || vlan == undefined) {
                        network = '<a>(基础网络)&nbsp;/&nbsp;' + ip + '</a>';
                    } else {
                        network = '<a>(' + vlan + ')&nbsp;/&nbsp;' + ip + '</a>';
                    }
                }
                var importance = obj.importance;
                var thistr = '<tr rowid="' + vmuuid + '"><td class="rcheck"><input type="checkbox" name="vmrow"></td><td name="console">' + showstr + '</td><td name="vmname">'
                    + vmName + '</td>' + stateStr + '<td name="vmimportance">' + importance + '</td><td name="userName">' + userName + '</td><td name="cpuCore">'
                    + cpu + '</td><td name="memoryCapacity">'
                    + memory + '</td><td name="sip">' + network + '</td><td name="createtime" class="time">' + decodeURI(obj.createdate) + '</td></tr>';
                tableStr += thistr;
            }
            $('#tablebody').html(tableStr);
        }
    });
}

function getVMList(page, limit) {
    $('#tablebody').html("");
    if (type == "instance") {
        loadList('/VMAction/AdminList', page, limit, "i-");
    } else if (type == "router") {
        loadList('/RouterAction/AdminList', page, limit, "rt-");
    } else if (type == "loadbalance") {
        loadList('/LBAction/AdminList', page, limit, "lb-");
    }
}

function adminstart(action, uuid) {
    $.ajax({
        type: 'get',
        url: action,
        data: {action: "adminstartup", uuid: uuid},
        dataType: 'json',
        success: function (array) {
        }
    });
}

function startVM(uuid) {
    var thistr = $("#tablebody").find('[rowid="' + uuid + '"]');
    thistr.find('[name="stateicon"]').removeClass("icon-stopped");
    thistr.find('[name="stateicon"]').addClass('icon-process');
    thistr.find('[name="stateword"]').text('启动中');
    if (type == "instance") {
        adminstart('/VMAction', uuid);
    } else if (type == "router") {
        adminstart('/RouterAction', uuid);
    } else if (type == "loadbalance") {
        adminstart('/LBAction', uuid);
    }
}

function adminshut(action, uuid, force) {
    $.ajax({
        type: 'get',
        url: action,
        data: {action: "adminshutdown", uuid: uuid, force: force},
        dataType: 'json',
        success: function (array) {
        }
    });
}

function shutdownVM(uuid, force) {
    var thistr = $("#tablebody").find('[rowid="' + uuid + '"]');
    thistr.find('[name="stateicon"]').removeClass("icon-running");
    thistr.find('[name="stateicon"]').addClass('icon-process');
    thistr.find('[name="stateword"]').text('关机中');
    thistr.find('.console').remove();
    if (type == "instance") {
        adminshut('/VMAction', uuid, force);
    } else if (type == "router") {
        adminshut('/RouterAction', uuid, force);
    } else if (type == "loadbalance") {
        adminshut('/LBAction', uuid, force);
    }
}

function initPage() {
    $.ajax({
        type: 'get',
        url: '/HostAction/ALLList',
        dataType: 'json',
        success: function (array) {
            $.each(array, function (index, json) {
                $("#select-server").append('<li><a id="host' + index + '" hostUuid="' + json.hostUuid + '"><sapn chalss="glyphicon glyphicon-tasks"></span>' + json.hostName + '</a></li>');
                $("#host" + index).on("click", function () {
                    $("#selecthost").text(json.hostName);
                    host = json.hostUuid;
                });
            });
        }
    });
}

$("#hostall").on("click", function () {
    $("#selecthost").text("全部");
    host = "all";
});

$('#tablebody').on('click', '.console', function (event) {
    event.preventDefault();
    var uuid = $(this).data("uuid");
    var vnc = $('#platformcontent').attr("vncServer");
    var token = uuid.substring(0, 8);
    var url = vnc + "console.html?id=" + token;
    window.open(url, "novnc", 'height=600, width=810, top=0, left=0');
});