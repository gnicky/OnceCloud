$(document).ready(function () {

    $('#create, #bind').on('click', function (event) {
        event.preventDefault();
        $('#VolumeModalContainer').load($(this).attr('url'), '', function () {
            $('#VolumeModalContainer').modal({
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
            var limit = $("#limit").val();
            var search = $("#search").val();
            getVolumeList(page, limit, search);
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
    getVolumeList(1, 10, "");

    $('#limit').on('focusout', function () {
        var limit = $("#limit").val();
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
        var limit = $("#limit").val();
        var search = $("#search").val();
        getVolumeList(1, limit, search);
        options = {
            currentPage: 1,
        }
        $('#pageDivider').bootstrapPaginator(options);
    }

    function getInfoList() {
        var infoList = "";
        $('input[name="volumerow"]:checked').each(function () {
            infoList += "[i-" + $(this).parent().parent().attr("rowid").substring(0, 8) + "]&nbsp;";
        });
        return infoList;
    }

    function showbox(type) {
        var infoList = getInfoList();
        var infoArray = new Array("删除硬盘", "卸载硬盘");
        var showMessage = '';
        var showTitle = '';
        if (type == 1) {
            showMessage = '<div class="alert alert-warning" style="margin:10px; color:#c09853">'
                + '<span class="glyphicon glyphicon-warning-sign"></span>&nbsp;物理卸载硬盘时，需要注意以下几点:'
                + '<br/><br/>1. 卸载硬盘时会丢失位于缓存中的数据；<br/>2. 为保证数据的完整性，最好确保该硬盘在主机的操作系统中处于非加载状态。</div>';
            showTitle = infoArray[type] + '&nbsp;' + infoList + '?';
        } else {
            showMessage = '<div class="alert alert-info" style="margin:10px">'
                + '<span class="glyphicon glyphicon-info-sign"></span>&nbsp;'
                + infoArray[type] + '&nbsp;' + infoList + '?</div>';
            showTitle = "提示";
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
                        $('input[name="volumerow"]:checked').each(function () {
                            var uuid = $(this).parent().parent().attr("rowid");
                            if (type == 0) {
                                deleteVolume(uuid);
                            } else if (type == 1) {
                                unbind(uuid);
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

    $('#delete').on('click', function (event) {
        event.preventDefault();
        showbox(0);
    });
    $('#unbind').on('click', function (event) {
        event.preventDefault();
        showbox(1);
    });

    $('#platformcontent').on('click', '.backup', function (event) {
        event.preventDefault();
        var thisurl = $(this).attr('url');
        bootbox.dialog({
            className: "bootbox-large",
            message: '<div class="alert alert-warning" style="margin:10px; color:#c09853"><span class="glyphicon glyphicon-warning-sign"></span>&nbsp;对已经绑定主机的硬盘进行在线备份时，需要注意以下几点:<br/><br/>1. 备份只能捕获在备份任务开始时已经写入磁盘的数据，不包括当时位于缓存里的数据；<br/>2. 为了保证数据的完整性，请在创建备份前暂停所有文件的写操作，或进行离线备份。</div>',
            title: "提示",
            buttons: {
                main: {
                    label: "创建备份",
                    className: "btn-primary",
                    callback: function () {
                        $('#VolumeModalContainer').load(thisurl, '', function () {
                            $('#VolumeModalContainer').modal({
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

    function getVolumeList(page, limit, search) {
        $('#tablebody').html("");
        $.ajax({
            type: 'get',
            url: '/VolumeAction',
            data: "action=getlist&page=" + page + "&limit=" + limit + "&search=" + search,
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
                    var volumeid = obj.volumeid;
                    var volumename = decodeURI(obj.volumename);
                    var volumedepen = obj.volumedepen;
                    var depenname = decodeURI(obj.depenname);
                    var isused = obj.isused;
                    var volState = obj.volState;
                    var usedStr = "";
                    if (volState == 2) {
                        usedStr = usedStr + '<td><span class="icon-status icon-process" name="stateicon"></span><span name="stateword">创建中</span></td>';
                    } else if (volState == 3) {
                        usedStr = usedStr + '<td><span class="icon-status icon-process" name="stateicon"></span><span name="stateword">安装中</span></td>';
                    } else if (volState == 5) {
                        usedStr = usedStr + '<td><span class="icon-status icon-process" name="stateicon"></span><span name="stateword">卸载中</span></td>';
                    } else if (volState == 6) {
                        usedStr = usedStr + '<td><span class="icon-status icon-process" name="stateicon"></span><span name="stateword">删除中</span></td>';
                    } else if (volState == 1) {
                        usedStr = usedStr + '<td><span class="icon-status icon-running" name="stateicon"></span><span name="stateword">可用</span></td>';
                    } else if (volState == 4) {
                        usedStr = usedStr + '<td><span class="icon-status icon-using" name="stateicon"></span><span name="stateword">使用中</span></td>';
                    }
                    var volumesize = obj.volumesize.toFixed(2);
                    var createdate = obj.createdate;
                    var backupdate = obj.backupdate;
                    var backupStr = decodeURI(backupdate);
                    if (backupdate == "") {
                        var basePath = $('#platformcontent').attr('platformBasePath');
                        backupStr = '<a class="glyphicon glyphicon-camera backup" url="' + basePath + 'user/create/createsnapshot.jsp?rsid=' + volumeid + '&rstype=volume&rsname=' + volumename + '"></a>';
                    }
                    if (depenname != "") {
                        depenname = '<a><span class="glyphicon glyphicon-cloud"></span>&nbsp;&nbsp;' + depenname + '</a>';
                    }
                    var showid = "vol-" + volumeid.substring(0, 8);
                    var showstr = "<a class='id'>" + showid + '</a>';
                    tableStr = tableStr + '<tr rowid="' + volumeid + '"><td class="rcheck"><input type="checkbox" name="volumerow"></td><td>'
                        + showstr + '</td><td name="volumename">' + volumename + '</td>'
                        + usedStr + '<td vmuuid="' + volumedepen + '">' + depenname + '</td><td name="size">'
                        + volumesize + '</th><td name="backuptime" class="time">' + backupStr + '</td><td name="createtime" class="time">'
                        + decodeURI(createdate) + '</td></tr>';
                }
                $('#tablebody').html(tableStr);
            },
            error: function () {
            }
        });
        allDisable();
    }

    $('#tablebody').on('click', '.id', function (event) {
        event.preventDefault();
        var uuid = $(this).parent().parent().attr('rowid');
        $.ajax({
            type: 'get',
            url: '/VolumeAction',
            data: 'action=detail&volumeuuid=' + uuid,
            dataType: 'text',
            success: function (response) {
                window.location.href = $('#platformcontent').attr('platformBasePath') + "user/detail/volumedetail.jsp";
            },
            error: function () {
            }
        });
    });

    function deleteVolume(uuid) {
        var thistr = $('#tablebody').find('[rowid="' + uuid + '"]');
        if (thistr.size() == 1) {
            thistr.find('[name="stateicon"]').removeClass("icon-running").addClass('icon-process');
            thistr.find('[name="stateword"]').text('删除中');
        }
        $.ajax({
            type: 'post',
            url: '/VolumeAction',
            data: "action=delete&volumeuuid=" + uuid,
            dataType: 'json',
            success: function (obj) {
            },
            error: function () {
            }
        });
    }

    function unbind(volumeuuid) {
        var thistr = $('#tablebody').find('[rowid="' + volumeuuid + '"]');
        if (thistr.size() == 1) {
            thistr.find('[name="stateicon"]').removeClass("icon-using").addClass('icon-process');
            thistr.find('[name="stateword"]').text('卸载中');
        }
        $.ajax({
            type: 'post',
            url: '/VolumeAction',
            data: "action=unbind&volumeuuid=" + volumeuuid,
            dataType: 'json',
            success: function (obj) {
            },
            error: function () {
            }
        });
    }

    function removeAllCheck() {
        $('input[name="volumerow"]:checked').each(function () {
            $(this)[0].checked = false;
            $(this).change();
        });
    }

    function allDisable() {
        $("#bind").attr("disabled", true).addClass('btn-forbidden');
        $("#unbind").attr("disabled", true).addClass('btn-forbidden');
        $("#delete").attr("disabled", true).addClass('btn-forbidden');
        $("#backup").attr('disabled', true).addClass('btn-forbidden');
    }

    $('#tablebody').on('change', 'input:checkbox', function (event) {
        event.preventDefault();
        allDisable();
        var used = 0;
        var process = 0;
        var usable = 0;
        var total = 0;
        $('input[name="volumerow"]:checked').each(function () {
            var stateicon = $(this).parent().parent().find('[name="stateicon"]');
            if (stateicon.hasClass('icon-running')) {
                usable++;
            } else if (stateicon.hasClass('icon-using')) {
                used++;
            } else {
                process++;
            }
            total++;
        });
        if (total != 0 && process == 0) {
            $("#backup").attr('disabled', false).removeClass('btn-forbidden');
            if (used > 0 && usable == 0) {
                $("#unbind").attr('disabled', false).removeClass('btn-forbidden');
            } else if (usable > 0 && used == 0) {
                $("#bind").attr('disabled', false).removeClass('btn-forbidden');
                $("#delete").attr('disabled', false).removeClass('btn-forbidden');
            }
        }
    });

    function pageDisplayUpdate(current, total) {
        $('#currentP').html(current);
        $('#totalP').html(total);
    }
});