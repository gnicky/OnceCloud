$(document).ready(function () {
    var options = {
        bootstrapMajorVersion: 3,
        currentPage: 1,
        totalPages: 1,
        numberOfPages: 0,
        onPageClicked: function (e, originalEvent, type, page) {
            var limit = document.getElementById("limit").value;
            var search = document.getElementById("search").value;
            getPoolList(page, limit, search);
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

    $('#create').on('click', function (event) {
        event.preventDefault();
        $('#PoolModalContainer').attr('type', 'new')
        $('#PoolModalContainer').load($(this).attr('url'), '', function () {
            $('#PoolModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });


    $('#update').on('click', function (event) {
        event.preventDefault();
        $('#PoolModalContainer').attr('type', 'edit')
        $('#PoolModalContainer').load($(this).attr('url'), '', function () {
            $('#PoolModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });

    $('#poolP').bootstrapPaginator(options);
    getPoolList(1, 10, "");

    $('#limit').on('focusout', function () {
        var limit = $("#limit").val();
        var reg = /^[0-9]*[1-9][0-9]*$/;
        if (!reg.test(limit)) {
            $("#limit").val(10);
            limit = 10;
        }
        var search = document.getElementById("search").value;
        getPoolList(1, limit, search);
        options = {
            currentPage: 1,
        }
        $('#poolP').bootstrapPaginator(options);
    });

    $('#search').on('focusout', function () {
        search();
    });

    $('#search').keypress(function (e) {
        var key = e.which;
        if (key == 13) {
            search();
        }
    });

    function search() {
        var limitnum = document.getElementById("limit").value;
        var search = document.getElementById("search").value;
        getPoolList(1, limitnum, search);
        options = {
            currentPage: 1
        }
        $('#poolP').bootstrapPaginator(options);
    }

    function getPoolList(page, limit, searchstr) {
        $.ajax({
            type: 'get',
            url: '/PoolAction',
            data: 'action=getlist&page=' + page + '&limitnum=' + limit + '&search=' + searchstr,
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
                    }
                    $('#poolP').bootstrapPaginator(options);
                    pageDisplayUpdate(page, totalp);
                    var btable = document.getElementById("tablebody");
                    var tableStr = "";
                    for (var i = 1; i < array.length; i++) {
                        var obj = array[i];
                        var poolname = decodeURI(obj.poolname);
                        var pooldesc = decodeURI(obj.pooldesc);
                        var poolid = obj.poolid;
                        var poolmaster = obj.poolmaster;
                        var mastername = decodeURI(obj.mastername);
                        var createdate = obj.createdate;
                        var dcuuid = obj.dcuuid;
                        var dcname = obj.dcname;
                        if (dcuuid == "") {
                            statestr = '<td class="pod" state="unload"></td>';
                        } else {
                            statestr = '<td class="pod" state="loaded"><a>' + decodeURI(dcname) + '</a></td>';
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
                        var mytr = '<tr poolid="' + poolid + '" poolname="' + poolname + '" pooldesc="' + pooldesc + '" dcid="' + dcuuid + '"><td class="rcheck"><input type="checkbox" name="poolrow"></td>'
                            + '<td><a class="id">' + showid + '</a></td><td>' + poolname + '</td><td><a>' + mastername + '</a></td>' + statestr + '<td>' + cpustr + '</td><td>'
                            + memstr + '</td><td class="time">' + createdate + '</td></tr>';
                        tableStr = tableStr + mytr;
                    }
                    btable.innerHTML = tableStr;
                }
            },
            error: function () {

            }
        });
    }

    $('#delete').on('click', function (event) {
        event.preventDefault();
        if ($(this).attr('disabled') == "disabled") {
            return;
        }
        var boxes = document.getElementsByName("poolrow");
        var infoList = "";
        for (var i = 0; i < boxes.length; i++) {
            if (boxes[i].checked == true) {
                infoList += "[" + $(boxes[i]).parent().parent().attr("poolname") + "]&nbsp;";
            }
        }
        bootbox.dialog({
            message: '<div class="alert alert-info" style="margin:10px"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;删除资源池&nbsp;' + infoList + '?</div>',
            title: "提示",
            buttons: {
                main: {
                    label: "确定",
                    className: "btn-primary",
                    callback: function () {
                        for (var i = 0; i < boxes.length; i++) {
                            if (boxes[i].checked == true) {
                                deletePool($(boxes[i]).parent().parent().attr("poolid"), $(boxes[i]).parent().parent().attr("poolname"));
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
            url: '/PoolAction',
            data: 'action=delete&poolid=' + poolid + '&poolname=' + poolname,
            dataType: 'json',
            success: function (array) {
                if (array.length == 1) {
                    var result = array[0].result;
                    if (result) {
                        var thistr = $("#tablebody").find('[poolid="' + poolid + '"]');
                        $(thistr).remove();
                    }
                }
            },
            error: function () {

            }
        });
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
        var bindcount = 0;
        var unbindcount = 0;
        for (var i = 0; i < boxes.length; i++) {
            if (boxes[i].checked == true) {
                count++;
                /*if ($(boxes[i]).parent().parent().find('.pod').attr('state') =='loaded') {
                 bindcount++;
                 } else {
                 unbindcount++;
                 }*/
            }

        }
        if (count > 0) {
            $("#delete").attr('disabled', false).removeClass('btn-forbidden');
            $("#consistency").attr('disabled', false).removeClass('btn-forbidden');
        }
        else {
            $("#delete").attr('disabled', true).addClass('btn-forbidden');
            $("#consistency").attr('disabled', true).addClass('btn-forbidden');
        }
        if (1 == count) {
            $('#update').removeClass('btn-forbidden');
            $("#update").attr('disabled', false);
        } else {
            $('#update').addClass('btn-forbidden');
            $("#update").attr('disabled', true);
        }
        /*if (count == 0) {
         $("#delete").attr('disabled', true).addClass('btn-forbidden');
         $("#load").attr('disabled',true).addClass('btn-forbidden');
         $("#unload").attr('disabled',true).addClass('btn-forbidden');
         } else {
         if (bindcount==count) {
         $("#unload").attr('disabled',false).removeClass('btn-forbidden');
         } else if(unbindcount==count) {
         $("#load").attr('disabled',false).removeClass('btn-forbidden');
         $("#delete").attr('disabled', false).removeClass('btn-forbidden');
         } else {
         $("#delete").attr('disabled', true).addClass('btn-forbidden');
         $("#load").attr('disabled',true).addClass('btn-forbidden');
         $("#unload").attr('disabled',true).addClass('btn-forbidden');
         }
         }*/
    });

    function pageDisplayUpdate(current, total) {
        var c = document.getElementById("currentPS");
        var t = document.getElementById("totalPS");
        c.innerHTML = current + "";
        t.innerHTML = total + "";
    }

    $('.btn-refresh').on('click', function (event) {
        event.preventDefault();
        var limit = document.getElementById("limit").value;
        var search = document.getElementById("search").value;
        getPoolList(1, limit, search);
        options = {
            currentPage: 1
        }
        $('#poolP').bootstrapPaginator(options);
    });

    function unbind2rack(poolid) {
        var thistr = $("#tablebody").find('[poolid="' + poolid + '"]');
        var thistd = thistr.find('[state]');
        $.ajax({
            type: 'get',
            url: '/PoolAction',
            data: 'action=unbind&poolid=' + poolid,
            dataType: 'json',
            success: function (array) {
                if (array.length == 1) {
                    var result = array[0].result;
                    if (result) {
                        thistd.text("");
                        thistd.attr("state", "unload");
                    }
                }
            },
            error: function () {

            }
        });
    }

    $('#load').on('click', function (event) {
        event.preventDefault();
        if ($(this).attr('disabled') == "disabled") {
            return;
        }
        $('#PoolModalContainer').load($(this).attr('url'), '', function () {
            $('#PoolModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });

    $('#unload').on('click', function (event) {
        event.preventDefault();
        if ($(this).attr('disabled') == "disabled") {
            return;
        }
        var boxes = document.getElementsByName("poolrow");
        var infoList = "";
        for (var i = 0; i < boxes.length; i++) {
            if (boxes[i].checked == true) {
                infoList += "[" + $(boxes[i]).parent().parent().attr("poolname") + "]&nbsp;";
            }
        }
        bootbox.dialog({
            className: "bootbox-large",
            message: '<div class="alert alert-info" style="margin:10px"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;从机架中移除&nbsp;' + infoList + '?</div>',
            title: "提示",
            buttons: {
                main: {
                    label: "确定",
                    className: "btn-primary",
                    callback: function () {
                        for (var i = 0; i < boxes.length; i++) {
                            if (boxes[i].checked == true) {
                                unbind2rack($(boxes[i]).parent().parent().attr("poolid"));
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
        if ($(this).attr('disabled') == "disabled") {
            return;
        }
        var boxes = document.getElementsByName("poolrow");
        var infoList = "";
        for (var i = 0; i < boxes.length; i++) {
            if (boxes[i].checked == true) {
                infoList += "[" + $(boxes[i]).parent().parent().attr("poolname") + "]&nbsp;";
            }
        }
        bootbox.dialog({
            message: '<div class="alert alert-info" style="margin:10px"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;统一资源池的一致性&nbsp;' + infoList + '?</div>',
            title: "提示",
            buttons: {
                main: {
                    label: "确定",
                    className: "btn-primary",
                    callback: function () {
                        for (var i = 0; i < boxes.length; i++) {
                            if (boxes[i].checked == true) {
                                consistencyPool($(boxes[i]).parent().parent().attr("poolid"));
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
            url: '/PoolAction',
            data: 'action=consistency&poolUuid=' + poolUuid,
            dataType: 'json',
            success: function (array) {

            },
            error: function () {

            }
        });
    }
});