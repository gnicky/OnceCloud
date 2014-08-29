$(document).ready(function () {

    var options = {
        bootstrapMajorVersion: 3,
        currentPage: 1,
        totalPages: 1,
        numberOfPages: 0,
        onPageClicked: function (e, originalEvent, type) {
            var limit = $('#limit').val();
            var search = $('#search').val();
            getFirewallList(page, limit, search);
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
    getFirewallList(1, 10, "");

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

    function reloadList() {
        var limit = $("#limit").val();
        var search = $("#search").val();
        getFirewallList(1, limit, search);
        options = {
            currentPage: 1,
        }
        $('#pageDivider').bootstrapPaginator(options);
    }

    $('#createfw').on('click', function (event) {
        event.preventDefault();
        $('#FirewallModalContainer').load($(this).attr('url'), '', function () {
            $('#FirewallModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
        removeAllCheck();
    });

    $('#bind').on('click', function (event) {
        event.preventDefault();
        $('#FirewallModalContainer').attr('bindtype', 'vm');
        $('#FirewallModalContainer').load($(this).attr('url'), '', function () {
            $('#FirewallModalContainer').modal({
                backdrop: false,
                show: true
            });
        });

    });

    $('#bindlb').on('click', function (event) {
        event.preventDefault();
        $('#FirewallModalContainer').attr('bindtype', 'lb');
        $('#FirewallModalContainer').load($(this).attr('url'), '', function () {
            $('#FirewallModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });


    $('#bindrt').on('click', function (event) {
        event.preventDefault();
        $('#FirewallModalContainer').attr('bindtype', 'rt');
        $('#FirewallModalContainer').load($(this).attr('url'), '', function () {
            $('#FirewallModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });

    $('#binddb').on('click', function (event) {
        event.preventDefault();
        $('#FirewallModalContainer').attr('bindtype', 'db');
        $('#FirewallModalContainer').load($(this).attr('url'), '', function () {
            $('#FirewallModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });

    $('#delete').on('click', function (event) {
        event.preventDefault();
        if ($(this).attr('disabled') == "disabled") {
            return;
        }
        var boxes = document.getElementsByName("firewallrow");
        var infoList = "";
        for (var i = 0; i < boxes.length; i++) {
            if (boxes[i].checked == true) {
                infoList += "[fw-" + $(boxes[i]).parent().parent().attr('firewallid').substring(0, 8) + "]&nbsp;";
            }
        }
        bootbox.dialog({
            message: '<div class="alert alert-info" style="margin:10px"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;删除防火墙&nbsp;' + infoList + '?</div>',
            title: "提示",
            buttons: {
                main: {
                    label: "确定",
                    className: "btn-primary",
                    callback: function () {
                        for (var i = 0; i < boxes.length; i++) {
                            if (boxes[i].checked == true) {
                                var firewallId = $(boxes[i]).parent().parent().attr('firewallid');
                                deleteFirewall(firewallId);
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

    function deleteFirewall(firewallId) {
        $.ajax({
            type: 'get',
            url: '/FirewallAction',
            data: 'action=deletefirewall&firewallId=' + firewallId,
            dataType: 'json',
            success: function (obj) {
                if (obj.result == true) {
                    var thistr = $("#tablebody").find('[firewallid="' + firewallId + '"]');
                    $(thistr).remove();
                } else {
                    bootbox.dialog({
                        message: '<div class="alert alert-danger" style="margin:10px"><span class="glyphicon glyphicon-warning-sign">'
                            + '</span>&nbsp;无法删除，依然有其他资源依赖于该防火墙</div>',
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

    function getFirewallList(page, limit, search) {
        $('#tablebody').html("");
        $.ajax({
            type: 'get',
            url: '/FirewallAction',
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
                    var firewallId = obj.firewallId;
                    var showid = firewallId.substring(0, 8);
                    var firewallName = decodeURI(obj.firewallName);
                    var ruleSize = obj.ruleSize;
                    var createDate = obj.createDate;
                    var def = obj.def;
                    var showtd = '<td class="firewallid" def="0"><a>fw-' + showid + '</a></td>';
                    if (def == 1) {
                        showtd = '<td class="firewallid" def="1"><a>fw-' + showid + '<span class="glyphicon glyphicon-star" style="margin-left:6px"></span></a></td>';
                    }
                    var thistr = '<tr firewallid="'
                        + firewallId + '"><td class="rcheck"><input type="checkbox" name="firewallrow"></td>' + showtd + '<td>'
                        + firewallName + '</td><td>' + ruleSize + '</td><td>' + createDate + '</td></tr>';
                    tableStr += thistr;
                }
                $('#tablebody').html(tableStr);
            },
            error: function () {
            }
        });
    }

    $('#tablebody').on('click', '.firewallid', function (event) {
        event.preventDefault();
        var firewallId = $(this).parent().attr("firewallid");
        var basePath = $("#platformcontent").attr("platformBasePath");
        $.ajax({
            type: 'get',
            url: '/FirewallAction',
            data: 'action=detail&firewallId=' + firewallId,
            dataType: 'text',
            success: function (response) {
                window.location.href = basePath + "user/detail/firewalldetail.jsp";
            },
            error: function () {
            }
        });
    });

    function allDisable() {
        $("#bind").attr("disabled", true).addClass('btn-forbidden');
        $("#bindlb").attr("disabled", true).addClass('btn-forbidden');
        $("#bindrt").attr("disabled", true).addClass('btn-forbidden');
        $("#binddb").attr("disabled", true).addClass('btn-forbidden');
        $("#delete").attr("disabled", true).addClass('btn-forbidden');
    }

    $('#tablebody').on('change', 'input:checkbox', function (event) {
        event.preventDefault();
        allDisable();
        var def = 0;
        var common = 0;
        var total = 0;
        $('input[name="firewallrow"]:checked').each(function () {
            var isDef = $(this).parent().parent().find('.firewallid').attr("def");
            if (isDef == 1) {
                def++;
            } else {
                common++;
            }
            total++;
        });
        if (total == 1) {
            $("#bind").attr('disabled', false).removeClass('btn-forbidden');
            $("#bindlb").attr("disabled", false).removeClass('btn-forbidden');
            $("#bindrt").attr("disabled", false).removeClass('btn-forbidden');
            $("#binddb").attr("disabled", false).removeClass('btn-forbidden');
            if (def == 0) {
                $("#delete").attr('disabled', false).removeClass('btn-forbidden');
            }
        } else if (total > 1) {
            if (def == 0) {
                $("#delete").attr('disabled', false).removeClass('btn-forbidden');
            }
        }
    });

    function removeAllCheck() {
        $('input[name="firewallrow"]:checked').each(function () {
            $(this)[0].checked = false;
            $(this).change();
        });
    }

    function pageDisplayUpdate(current, total) {
        $('#currentP').html(current);
        $('#totalP').html(total);
    }
});