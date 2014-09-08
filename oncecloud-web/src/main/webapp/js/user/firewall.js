reloadList(1);

function reloadList(page) {
    var limit = $('#limit').val();
    var search = $('#search').val();
    getFirewallList(page, limit, search);
    if (page == 1) {
        options = {
            currentPage: 1
        };
        $('#pageDivider').bootstrapPaginator(options);
    }
    allDisable();
}

function removeAllCheck() {
    $('input[name="firewallrow"]:checked').each(function () {
        $(this)[0].checked = false;
        $(this).change();
    });
}

function allDisable() {
    $("#bind").addClass('btn-forbidden');
    $("#bindlb").addClass('btn-forbidden');
    $("#bindrt").addClass('btn-forbidden');
    $("#binddb").addClass('btn-forbidden');
    $("#delete").addClass('btn-forbidden');
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
        $("#bind").removeClass('btn-forbidden');
        $("#bindlb").removeClass('btn-forbidden');
        $("#bindrt").removeClass('btn-forbidden');
        $("#binddb").removeClass('btn-forbidden');
        if (def == 0) {
            $("#delete").removeClass('btn-forbidden');
        }
    } else if (total > 1) {
        if (def == 0) {
            $("#delete").removeClass('btn-forbidden');
        }
    }
});

$('#createfw').on('click', function (event) {
    event.preventDefault();
    $('#FirewallModalContainer').load($(this).attr('url'), '', function () {
        $('#FirewallModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
});

$('#bind').on('click', function (event) {
    event.preventDefault();
    $('#FirewallModalContainer').load($(this).attr('url'), {'type': 'vm'}, function () {
        $('#FirewallModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
});

$('#bindlb').on('click', function (event) {
    event.preventDefault();
    $('#FirewallModalContainer').load($(this).attr('url'), {'type': 'lb'}, function () {
        $('#FirewallModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
});


$('#bindrt').on('click', function (event) {
    event.preventDefault();
    $('#FirewallModalContainer').load($(this).attr('url'), {'type': 'rt'}, function () {
        $('#FirewallModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
});

$('#delete').on('click', function (event) {
    event.preventDefault();
    var infoList = "";
    $('input[name="firewallrow"]:checked').each(function () {
        infoList += "[fw-" + $(this).parent().parent().attr("firewallid").substring(0, 8) + "]&nbsp;";
    });
    bootbox.dialog({
        message: '<div class="alert alert-info" style="margin:10px"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;删除防火墙&nbsp;' + infoList + '?</div>',
        title: "提示",
        buttons: {
            main: {
                label: "确定",
                className: "btn-primary",
                callback: function () {
                    $('input[name="firewallrow"]:checked').each(function () {
                        var firewallId = $(this).parent().parent().attr('firewallid');
                        deleteFirewall(firewallId);
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
});

function deleteFirewall(firewallId) {
    $.ajax({
        type: 'post',
        url: '/FirewallAction/DeleteFirewall',
        data: {firewallId: firewallId},
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
        }
    });
}

function getFirewallList(page, limit, search) {
    $('#tablebody').html("");
    $.ajax({
        type: 'get',
        url: '/FirewallAction/FirewallList',
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
        }
    });
}

$('#tablebody').on('click', '.firewallid', function (event) {
    event.preventDefault();
    var firewallId = $(this).parent().attr('firewallid');
    var form = $("<form></form>");
    form.attr("action","/firewall/detail");
    form.attr('method','post');
    var input = $('<input type="text" name="firewallId" value="' + firewallId + '" />');
    form.append(input);
    form.css('display','none');
    form.appendTo($('body'));
    form.submit();
});
