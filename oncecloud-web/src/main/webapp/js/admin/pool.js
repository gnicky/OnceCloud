reloadList(1);

function reloadList(page) {
    var limit = $('#limit').val();
    var search = $('#search').val();
    getPoolList(page, limit, search);
    if (page == 1) {
        options = {
            currentPage: 1
        };
        $('#pageDivider').bootstrapPaginator(options);
    }
}

function removeAllCheck() {
    var boxes = document.getElementsByName("poolrow");
    for (var i = 0; i < boxes.length; i++) {
        boxes[i].checked = false;
        $(boxes[i]).change();
    }
}

$('#tablebody').on('change', 'input:checkbox', function (event) {
    event.preventDefault();
    var boxes = document.getElementsByName("poolrow");
    var count = 0;
    for (var i = 0; i < boxes.length; i++) {
        if (boxes[i].checked == true) {
            count++;
        }

    }
    if (count > 0) {
        $("#delete").removeClass('btn-forbidden');
        $("#consistency").removeClass('btn-forbidden');
    } else {
        $("#delete").addClass('btn-forbidden');
        $("#consistency").addClass('btn-forbidden');
    }
    if (1 == count) {
        $('#update').removeClass('btn-forbidden');
    } else {
        $('#update').addClass('btn-forbidden');
    }
});

$('#create').on('click', function (event) {
    event.preventDefault();
    $('#PoolModalContainer').attr('type', 'new');
    $('#PoolModalContainer').load($(this).attr('url'), '', function () {
        $('#PoolModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
});

$('#update').on('click', function (event) {
    event.preventDefault();
    $('#PoolModalContainer').attr('type', 'edit');
    $('#PoolModalContainer').load($(this).attr('url'), '', function () {
        $('#PoolModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
});

function getPoolList(page, limit, search) {
    $.ajax({
        type: 'get',
        url: '/PoolAction/PoolList',
        data: {page: page, limit: limit, search: search},
        dataType: 'json',
        success: function (array) {
            if (array.length >= 1) {
                var totalnum = array[0];
                var totalp = Math.ceil(totalnum / limit);
                if (totalp == 0) {
                    totalp == 1;
                }
                options = {
                    totalPages: totalp
                };
                $('#pageDivider').bootstrapPaginator(options);
                pageDisplayUpdate(page, totalp);
                var btable = document.getElementById("tablebody");
                var tableStr = "";
                for (var i = 1; i < array.length; i++) {
                    var obj = array[i];
                    var poolname = decodeURI(obj.poolname);
                    var pooldesc = decodeURI(obj.pooldesc);
                    var poolid = obj.poolid;
                    var mastername = decodeURI(obj.mastername);
                    var createdate = obj.createdate;
                    var dcuuid = obj.dcuuid;
                    var dcname = obj.dcname;
                    if (dcuuid == "") {
                        statestr = '<td class="pod" state="unload"></td>';
                    } else {
                        statestr = '<td class="pod" state="loaded"><a>'
                            + decodeURI(dcname) + '</a></td>';
                    }
                    var totalcpu = obj.totalcpu;
                    var totalmem = Math.round(obj.totalmem / 1024);
                    var cpustr = totalcpu + "";
                    if (totalcpu != 0) {
                        cpustr = cpustr + "&nbsp;核";
                    }
                    var memstr = totalmem + "";
                    if (totalmem != 0) {
                        memstr = memstr + "&nbsp;GB";
                    }
                    var showid = "pool-" + poolid.substring(0, 8);
                    var mytr = '<tr poolid="'
                        + poolid
                        + '" poolname="'
                        + poolname
                        + '" pooldesc="'
                        + pooldesc
                        + '" dcid="'
                        + dcuuid
                        + '"><td class="rcheck"><input type="checkbox" name="poolrow"></td>'
                        + '<td><a class="id">' + showid + '</a></td><td>'
                        + poolname + '</td><td><a>' + mastername
                        + '</a></td>' + statestr + '<td>' + cpustr
                        + '</td><td>' + memstr + '</td><td class="time">'
                        + createdate + '</td></tr>';
                    tableStr = tableStr + mytr;
                }
                btable.innerHTML = tableStr;
            }
        }
    });
}

$('#delete').on('click', function (event) {
    event.preventDefault();
    var boxes = document.getElementsByName("poolrow");
    var infoList = "";
    for (var i = 0; i < boxes.length; i++) {
        if (boxes[i].checked == true) {
            infoList += "[" + $(boxes[i]).parent().parent().attr("poolname")
                + "]&nbsp;";
        }
    }
    bootbox.dialog({
        message: '<div class="alert alert-info" style="margin:10px"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;删除资源池&nbsp;'
            + infoList + '?</div>',
        title: "提示",
        buttons: {
            main: {
                label: "确定",
                className: "btn-primary",
                callback: function () {
                    for (var i = 0; i < boxes.length; i++) {
                        if (boxes[i].checked == true) {
                            deletePool($(boxes[i]).parent().parent()
                                .attr("poolid"), $(boxes[i])
                                .parent().parent().attr("poolname"));
                        }
                    }
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
});

function deletePool(poolid, poolname) {
    $.ajax({
        type: 'post',
        url: '/PoolAction/Delete',
        data: {poolid:poolid, poolname:poolname},
        dataType: 'json',
        success: function (array) {
            if (array.length == 1) {
                var result = array[0].result;
                if (result) {
                    var thistr = $("#tablebody").find('[poolid="'
                        + poolid + '"]');
                    $(thistr).remove();
                }
            }
        }
    });
}

function unbind2rack(poolid) {
    var thistr = $("#tablebody").find('[poolid="' + poolid + '"]');
    var thistd = thistr.find('[state]');
    $.ajax({
        type: 'get',
        url: '/PoolAction/UnBind',
        data: {poolid:poolid},
        dataType: 'json',
        success: function (array) {
            if (array.length == 1) {
                var result = array[0].result;
                if (result) {
                    thistd.text("");
                    thistd.attr("state", "unload");
                }
            }
        }
    });
}

$('#load').on('click', function (event) {
    event.preventDefault();
    $('#PoolModalContainer').load($(this).attr('url'), '', function () {
        $('#PoolModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
});

$('#unload').on('click', function (event) {
    event.preventDefault();
    var boxes = document.getElementsByName("poolrow");
    var infoList = "";
    for (var i = 0; i < boxes.length; i++) {
        if (boxes[i].checked == true) {
            infoList += "[" + $(boxes[i]).parent().parent().attr("poolname")
                + "]&nbsp;";
        }
    }
    bootbox.dialog({
        className: "bootbox-large",
        message: '<div class="alert alert-info" style="margin:10px"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;从机架中移除&nbsp;'
            + infoList + '?</div>',
        title: "提示",
        buttons: {
            main: {
                label: "确定",
                className: "btn-primary",
                callback: function () {
                    for (var i = 0; i < boxes.length; i++) {
                        if (boxes[i].checked == true) {
                            unbind2rack($(boxes[i]).parent().parent()
                                .attr("poolid"));
                        }
                    }
                    removeAllCheck();
                }
            },
            cancel: {
                label: "取消",
                className: "btn-default",
                callback: function () {
                }
            }
        }
    });
});

$('#consistency').on('click', function (event) {
    event.preventDefault();
    var boxes = document.getElementsByName("poolrow");
    var infoList = "";
    for (var i = 0; i < boxes.length; i++) {
        if (boxes[i].checked == true) {
            infoList += "[" + $(boxes[i]).parent().parent().attr("poolname")
                + "]&nbsp;";
        }
    }
    bootbox.dialog({
        message: '<div class="alert alert-info" style="margin:10px"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;校验资源池的一致性&nbsp;'
            + infoList + '?</div>',
        title: "提示",
        buttons: {
            main: {
                label: "确定",
                className: "btn-primary",
                callback: function () {
                    for (var i = 0; i < boxes.length; i++) {
                        if (boxes[i].checked == true) {
                            consistencyPool($(boxes[i]).parent().parent()
                                .attr("poolid"));
                        }
                    }
                    removeAllCheck();
                }
            },
            cancel: {
                label: "取消",
                className: "btn-default",
                callback: function () {
                }
            }
        }
    });
});

function consistencyPool(poolUuid) {
    $.ajax({
        type: 'post',
        url: '/PoolAction/KeepAccordance',
        data: {poolUuid: poolUuid},
        dataType: 'json',
        success: function (array) {
        }
    });
}
