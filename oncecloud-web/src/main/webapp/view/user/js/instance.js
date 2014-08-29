$(document).ready(function () {

    $('#create, #image, #addtovlan').on('click', function (event) {
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
            getVMList(page, limit, search);
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
    getVMList(1, 10, "");

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
        getVMList(1, limit, search);
        options = {
            currentPage: 1,
        }
        $('#pageDivider').bootstrapPaginator(options);
    }

    function getInfoList() {
        var infoList = "";
        $('input[name="vmrow"]:checked').each(function () {
            infoList += "[i-" + $(this).parent().parent().attr("rowid").substring(0, 8) + "]&nbsp;";
        });
        return infoList;
    }

    function showbox(type) {
        var infoList = getInfoList();
        var infoArray = new Array("启动主机", "重启主机", "销毁主机", "关闭主机");
        var showMessage = '';
        var showTitle = '';
        if (type == 3) {
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
                                restartVM(uuid);
                            } else if (type == 2) {
                                destroyVM(uuid);
                            } else if (type == 3) {
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
    $('#restart').on('click', function (event) {
        event.preventDefault();
        showbox(1);
    });
    $('#destroy').on('click', function (event) {
        event.preventDefault();
        showbox(2);
    });
    $('#shutdown').on('click', function (event) {
        event.preventDefault();
        showbox(3);
    });

    $('#tablebody').on('click', '.id', function (event) {
        event.preventDefault();
        var uuid = $(this).parent().parent().attr('rowid');
        $.ajax({
            type: 'get',
            url: '/VMAction',
            data: 'action=detail&instanceuuid=' + uuid,
            dataType: 'text',
            success: function (response) {
                window.location.href = $('#platformcontent').attr('platformBasePath') + "user/detail/instancedetail.jsp";
            },
            error: function () {
            }
        });
    });

    $('#platformcontent').on('click', '.backup', function (event) {
        event.preventDefault();
        var thisurl = $(this).attr('url');
        bootbox.dialog({
            className: "bootbox-large",
            message: '<div class="alert alert-warning" style="margin:10px; color:#c09853">'
                + '<span class="glyphicon glyphicon-warning-sign"></span>&nbsp;对正在运行的主机进行在线备份时，需要注意以下几点:<br/><br/>1. 备份只能捕获在备份任务开始时已经写入磁盘的数据，不包括当时位于内存里的数据；<br/>2. 为了保证数据的完整性，请在创建备份前暂停所有文件的写操作，或进行离线备份。</div>',
            title: "提示",
            buttons: {
                main: {
                    label: "创建备份",
                    className: "btn-primary",
                    callback: function () {
                        $('#InstanceModalContainer').load(thisurl, '', function () {
                            $('#InstanceModalContainer').modal({
                                backdrop: false,
                                show: true
                            });
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

    function getVMList(page, limit, search) {
        $('#tablebody').html("");
        $.ajax({
            type: 'get',
            url: '/VMAction',
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
                    var vmuuid = obj.vmid;
                    var vmName = decodeURI(obj.vmname);
                    var state = obj.state;
                    var stateStr = "";
                    var showuuid = "i-" + vmuuid.substring(0, 8);
                    var showstr = "<a class='id'>" + showuuid + '</a>';
                    var iconStr = new Array("stopped", "running", "process", "process", "process", "process", "process");
                    var nameStr = new Array("已关机", "正常运行", "创建中", "销毁中", "启动中", "关机中", "重启中");
                    var stateStr = '<td><span class="icon-status icon-' + iconStr[state] + '" name="stateicon">'
                        + '</span><span name="stateword">' + nameStr[state] + '</span></td>';
                    if (state == 1) {
                        var showstr = showstr + '<a class="console" data-uuid=' + vmuuid + '><img src="../img/user/console.png"></a>';
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
                        if (vlan == "null") {
                            network = '<a>(基础网络)</a>';
                        } else {
                            network = '<a>(' + vlan + ')</a>';
                        }
                    } else {
                        if (vlan == "null") {
                            network = '<a>(基础网络)&nbsp;/&nbsp;' + ip + '</a>';
                        } else {
                            network = '<a>(' + vlan + ')&nbsp;/&nbsp;' + ip + '</a>';
                        }
                    }
                    var publicip = obj.publicip;
                    var backupdate = obj.backupdate + "";
                    var backupStr = decodeURI(backupdate);
                    if (backupdate == "") {
                        var basePath = $('#platformcontent').attr('platformBasePath');
                        backupStr = '<a class="glyphicon glyphicon-camera backup" url="' + basePath + 'user/create/createsnapshot.jsp?rsid=' + vmuuid + '&rstype=instance&rsname=' + vmName + '"></a>';
                    }
                    if (publicip != "") {
                        publicip = '<a><span class="glyphicon glyphicon-globe"></span>&nbsp;&nbsp;' + publicip + '</a>'
                    }
                    var thistr = '<tr rowid="' + vmuuid + '"><td class="rcheck"><input type="checkbox" name="vmrow"></td><td name="console">' + showstr + '</td><td name="vmname">'
                        + vmName + '</td>' + stateStr + '<td name="cpuCore">'
                        + cpu + '</td><td name="memoryCapacity">'
                        + memory + '</td><td name="sip">' + network + '</td><td name="pip">' + publicip + '</td><td name="backuptime" class="time">' + backupStr + '</td><td name="createtime" class="time">' + decodeURI(obj.createdate) + '</td></tr>';
                    tableStr += thistr;
                }
                $('#tablebody').html(tableStr);
            },
            error: function () {
            }
        });
        allDisable();
    }

    function startVM(uuid) {
        var thistr = $("#tablebody").find('[rowid="' + uuid + '"]');
        thistr.find('[name="stateicon"]').removeClass("icon-stopped");
        thistr.find('[name="stateicon"]').addClass('icon-process');
        thistr.find('[name="stateword"]').text('启动中');
        $.ajax({
            type: 'get',
            url: '/VMAction',
            data: 'action=startup&uuid=' + uuid,
            dataType: 'json',
            success: function (array) {
            },
            error: function () {
            }
        });
    }

    function restartVM(uuid) {
        var thistr = $("#tablebody").find('[rowid="' + uuid + '"]');
        thistr.find('[name="stateicon"]').removeClass("icon-running");
        thistr.find('[name="stateicon"]').addClass('icon-process');
        thistr.find('[name="stateword"]').text('重启中');
        thistr.find('.console').remove();
        $.ajax({
            type: 'get',
            url: '/VMAction',
            data: 'action=restart&uuid=' + uuid,
            dataType: 'json',
            success: function (array) {
            },
            error: function () {
            }
        });
    }

    function destroyVM(uuid) {
        var xmlhttp5;
        var thistr = $("#tablebody").find('[rowid="' + uuid + '"]');
        var thisicon = thistr.find('[name="stateicon"]');
        thisicon.removeClass("icon-stopped");
        thisicon.removeClass("icon-running");
        thisicon.addClass('icon-process');
        thistr.find('[name="stateword"]').text('销毁中');
        $.ajax({
            type: 'get',
            url: '/VMAction',
            data: 'action=destroy&uuid=' + uuid,
            dataType: 'json',
            success: function (array) {
            },
            error: function () {
            }
        });
    }

    function shutdownVM(uuid, force) {
        var thistr = $("#tablebody").find('[rowid="' + uuid + '"]');
        thistr.find('[name="stateicon"]').removeClass("icon-running");
        thistr.find('[name="stateicon"]').addClass('icon-process');
        thistr.find('[name="stateword"]').text('关机中');
        thistr.find('.console').remove();
        $.ajax({
            type: 'get',
            url: '/VMAction',
            data: 'action=shutdown&uuid=' + uuid + '&force=' + force,
            dataType: 'json',
            success: function (array) {
            },
            error: function () {
            }
        });
    }

    function removeAllCheck() {
        $('input[name="vmrow"]:checked').each(function () {
            $(this)[0].checked = false;
            $(this).change();
        });
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
            $("#backup").attr('disabled', false).removeClass('btn-forbidden');
            $("#image").attr('disabled', false).removeClass('btn-forbidden');
            $("#addtovlan").attr('disabled', false).removeClass('btn-forbidden');
            if (running > 0 && stopped == 0) {
                $("#shutdown").attr('disabled', false).removeClass('btn-disable');
                $("#restart").attr('disabled', false).removeClass('btn-forbidden');
            } else if (running == 0 && stopped > 0) {
                $("#startup").attr('disabled', false).removeClass('btn-disable');
            }
        }
    });

    function allDisable() {
        $("#startup").attr("disabled", true).addClass('btn-disable');
        $("#shutdown").attr("disabled", true).addClass('btn-disable');
        $("#restart").attr("disabled", true).addClass('btn-forbidden');
        $("#destroy").attr("disabled", true).addClass('btn-forbidden');
        $("#backup").attr('disabled', true).addClass('btn-forbidden');
        $("#image").attr('disabled', true).addClass('btn-forbidden');
        $("#addtovlan").attr('disabled', true).addClass('btn-forbidden');
    }

    $('#tablebody').on('click', '.console', function (event) {
        event.preventDefault();
        var uuid = $(this).data("uuid");
        var vnc = $('#platformcontent').attr("vncServer");
        var token = uuid.substring(0, 8);
        var url = vnc + "console.html?id=" + token;
        window.open(url, "novnc", 'height=600, width=810, top=0, left=0');
    });

    function pageDisplayUpdate(current, total) {
        $('#currentP').html(current);
        $('#totalP').html(total);
    }
});