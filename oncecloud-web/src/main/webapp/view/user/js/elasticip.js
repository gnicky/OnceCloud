$(document).ready(function () {

    $('#apply, #bind').on('click', function (event) {
        event.preventDefault();
        $('#EipModalContainer').load($(this).attr('url'), {'type': 'vm'}, function () {
            $('#EipModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });
    $('#bindlb').on('click', function (event) {
        event.preventDefault();
        $('#EipModalContainer').load($(this).attr('url'), {'type': 'lb'}, function () {
            $('#EipModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });
    $('#bindrt').on('click', function (event) {
        event.preventDefault();
        $('#EipModalContainer').load($(this).attr('url'), {'type': 'rt'}, function () {
            $('#EipModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });
    $('#binddb').on('click', function (event) {
        event.preventDefault();
        $('#EipModalContainer').load($(this).attr('url'), {'type': 'db'}, function () {
            $('#EipModalContainer').modal({
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
            getEipList(page, limit, search);
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
    getEipList(1, 10, "");

    $('.btn-refresh').on('click', function (event) {
        event.preventDefault();
        reloadList();
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

    $('#limit').on('focusout', function () {
        var limit = $("#limit").val();
        var reg = /^[0-9]*[1-9][0-9]*$/;
        if (!reg.test(limit)) {
            $("#limit").val(10);
        }
        reloadList();
    });

    function reloadList() {
        var limit = $('#limit').val();
        var search = $('#search').val();
        getEipList(1, limit, search);
        options = {
            currentPage: 1,
        }
        $('#pageDivider').bootstrapPaginator(options);
    }

    $('#bandwidth').on('click', function (event) {
        event.preventDefault();
        $("#changeBandwidth").modal({
            backdrop: false,
            show: true
        });
    });

    $("#changeBandwidth").on("hidden", function () {
        $(this).removeData("modal");
        removeAllCheck();
    });

    function getEipList(page, limit, search) {
        $('#tablebody').html("");
        $.ajax({
            type: 'get',
            url: '/EipAction',
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
                    var eipIp = obj.eipIp;
                    var eipId = obj.eipId;
                    var eipName = decodeURI(obj.eipName);
                    var eipDepen = obj.eipDepen;
                    var depenType = obj.depenType;
                    var depenName = decodeURI(obj.depenName);
                    var isused = obj.isused;
                    var usedStr = "";
                    var iconStr = "cloud";
                    var typeStr = 'type="vm"';
                    if (depenName != "") {
                        if (1 == depenType) {
                            iconStr = 'random';
                            typeStr = 'type="lb"';
                        } else if (2 == depenType) {
                            iconStr = 'inbox';
                            typeStr = 'type="db"';
                        } else if (3 == depenType) {
                            iconStr = 'fullscreen';
                            typeStr = 'type="rt"';
                        }
                        depenName = '<a><span class="glyphicon glyphicon-' + iconStr + '"></span>&nbsp;&nbsp;' + depenName + '</a>';
                    }
                    if (isused) {
                        usedStr = usedStr + '<td><span class="icon-status icon-using" name="stateicon"></span><span name="stateword">已分配</span></td>';
                    } else {
                        usedStr = usedStr + '<td><span class="icon-status icon-running" name="stateicon"></span><span name="stateword">可用</span></td>';
                    }
                    var eipBandwidth = obj.eipBandwidth;
                    var createDate = obj.createDate;
                    var thistr = '<tr eip="' + eipIp + '" eipId="' + eipId + '"><td class="rcheck"><input type="checkbox" name="eiprow"></td><td><a class="id">ip-' + eipId.substring(0, 8) + '</a></td><td name="eipname">'
                        + eipName + '</td><td>'
                        + eipIp + '</td>' + usedStr + '<td vmuuid="' + eipDepen + '" ' + typeStr + '>' + depenName + '</td><td name="bandwidth">'
                        + eipBandwidth + '</td><td>电信</td><td name="createdate" class="time">' + decodeURI(createDate) + '前</td></tr>';
                    tableStr += thistr;
                }
                $('#tablebody').html(tableStr);
            },
            error: function () {
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

    function showbox(type) {
        var infoList = getInfoList();
        var infoArray = new Array("删除公网IP", "解绑公网IP");
        var showMessage = '';
        var showTitle = '';
        showMessage = '<div class="alert alert-info" style="margin:10px">'
            + '<span class="glyphicon glyphicon-info-sign"></span>&nbsp;'
            + infoArray[type] + '&nbsp;' + infoList + '?</div>';
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
                        $('input[name="eiprow"]:checked').each(function () {
                            var eip = $(this).parent().parent().attr("eip");
                            if (type == 0) {
                                deleteEip(eip);
                            } else if (type == 1) {
                                unbind(eip);
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

    function getInfoList() {
        var infoList = "";
        $('input[name="eiprow"]:checked').each(function () {
            infoList += "[" + $(this).parent().parent().attr("eip") + "]&nbsp;";
        });
        return infoList;
    }

    $("#bandwidthAction").on('click', function (event) {
        event.preventDefault();
        var valid = $('#changebw-form').valid();
        if (valid) {
            var bandwidthS = parseInt($('#bandwidthSize').val(), 10);
            var apply = 0;
            $('input[name="eiprow"]:checked').each(function () {
                var current = $(this).parent().parent().find('[name="bandwidth"]').text();
                var currentInt = parseInt(current);
                apply = apply + bandwidthS - currentInt;
            });
            $.ajax({
                type: 'post',
                url: '/EipAction',
                data: 'action=quota&count=0&size=' + apply,
                dataType: 'text',
                cache: false,
                success: function (msg) {
                    if (msg != "ok") {
                        var quota = msg.split(":");
                        bootbox.dialog({
                            message: '<div class="alert alert-danger" style="margin:10px"><span class="glyphicon glyphicon-warning-sign"></span>&nbsp;超过配额，新申请[' + apply + ']&nbsp;Mbps带宽，目前剩余[' + quota[1] + ']&nbsp;Mbps带宽配额，您可以通过联系我们来申请扩大配额</div>',
                            title: "提示",
                            buttons: {
                                main: {
                                    label: "确定",
                                    className: "btn-primary",
                                    callback: function () {
                                    }
                                },
                                cancel: {
                                    label: "取消",
                                    className: "btn-default",
                                    callback: function () {
                                        $('#changeBandwidth').modal('hide');
                                    }
                                }
                            }
                        });
                    } else {
                        $('input[name="eiprow"]:checked').each(function () {
                            var currentIp = $(this).parent().parent().attr('eip');
                            changeBandwidth(currentIp, bandwidthS);
                        });
                        $('#changeBandwidth').modal('hide');
                    }
                    removeAllCheck();
                },
                error: function () {
                }
            });
        }
    });

    $("#changebw-form").validate({
        rules: {
            bandwidthSize: {
                required: true,
                digits: true,
                range: [1, 10]
            }
        },
        messages: {
            bandwidthSize: {
                required: "<span class='unit'>数量不能为空</span>",
                digits: "<span class='unit'>带宽必须是10以内的整数</span>",
                range: "<span class='unit'>带宽必须是10以内的整数</span>"
            }
        }
    });

    function changeBandwidth(eip, bandwidth) {
        $.ajax({
            type: 'post',
            url: '/EipAction',
            data: 'action=bandwidth&eip=' + eip + '&size=' + bandwidth,
            dataType: 'json',
            success: function (obj) {
                if (obj.result == true) {
                    var thistr = $("#tablebody").find('[eip="' + eip + '"]');
                    var thistd = thistr.find('[name="bandwidth"]');
                    thistd.text(bandwidth);
                }
            },
            error: function () {
            }
        });
    }

    $('#tablebody').on('click', '.id', function (event) {
        event.preventDefault();
        var eip = $(this).parent().parent().attr('eip');
        $.ajax({
            type: 'get',
            url: '/EipAction',
            data: 'action=detail&eip=' + eip,
            dataType: 'text',
            success: function (response) {
                window.location.href = $('#platformcontent').attr('platformBasePath') + "user/detail/elasticipdetail.jsp";
            },
            error: function () {
            }
        });
    });

    function deleteEip(eip) {
        $.ajax({
            type: 'get',
            url: '/EipAction',
            data: "action=delete&eip=" + eip,
            dataType: 'text',
            success: function (response) {
                $("#tablebody").find('[eip="' + eip + '"]').remove();
            },
            error: function () {
            }
        });
    }

    function unbind(eipIp) {
        var thistr = $("#tablebody").find('[eip="' + eipIp + '"]');
        thistr.find('[name="stateicon"]').removeClass("icon-using");
        thistr.find('[name="stateicon"]').addClass('icon-process');
        thistr.find('[name="stateword"]').text('解绑中');
        var vmuuid = thistr.find('[vmuuid]').attr('vmuuid');
        var bindtype = thistr.find('[vmuuid]').attr('type');
        $.ajax({
            type: 'get',
            url: '/EipAction',
            data: "action=unbind&eipIp=" + eipIp + "&vmuuid=" + vmuuid + '&bindtype=' + bindtype,
            dataType: 'json',
            success: function (obj) {
                if (obj.result == true) {
                    thistr.find('[name="stateicon"]').removeClass("icon-process");
                    thistr.find('[name="stateicon"]').addClass('icon-running');
                    thistr.find('[name="stateword"]').text('可用');
                    var vmtd = thistr.find('[vmuuid]');
                    vmtd.attr('vmuuid', "");
                    vmtd.text("");
                } else {
                    thistr.find('[name="stateicon"]').removeClass("icon-process");
                    thistr.find('[name="stateicon"]').addClass('icon-using');
                    thistr.find('[name="stateword"]').text('使用中');
                }
            },
            error: function () {
            }
        });
    }

    $('#tablebody').on('change', 'input:checkbox', function (event) {
        event.preventDefault();
        allDisable();
        var running = 0;
        var using = 0;
        var total = 0;
        $('input[name="eiprow"]:checked').each(function () {
            var stateicon = $(this).parent().parent().find('[name="stateicon"]');
            if (stateicon.hasClass('icon-running')) {
                running++;
            } else if (stateicon.hasClass('icon-using')) {
                using++;
            }
            total++;
        });
        if (total != 0) {
            $("#bandwidth").attr('disabled', false).removeClass('btn-forbidden');
            if (running > 0 && using == 0) {
                $("#bind").attr("disabled", false).removeClass('btn-forbidden');
                $("#bindlb").attr("disabled", false).removeClass('btn-forbidden');
                $("#bindrt").attr("disabled", false).removeClass('btn-forbidden');
                $("#binddb").attr("disabled", false).removeClass('btn-forbidden');
                $("#delete").attr("disabled", false).removeClass('btn-forbidden');
            } else if (running == 0 && using > 0) {
                $("#unbind").attr("disabled", false).removeClass('btn-forbidden');
            }
        }
    });

    function allDisable() {
        $("#bind").attr("disabled", true).addClass('btn-forbidden');
        $("#bindlb").attr("disabled", true).addClass('btn-forbidden');
        $("#bindrt").attr("disabled", true).addClass('btn-forbidden');
        $("#binddb").attr("disabled", true).addClass('btn-forbidden');
        $("#unbind").attr("disabled", true).addClass('btn-forbidden');
        $("#delete").attr('disabled', true).addClass('btn-forbidden');
        $("#bandwidth").attr('disabled', true).addClass('btn-forbidden');
    }

    function pageDisplayUpdate(current, total) {
        $('#currentP').html(current);
        $('#totalP').html(total);
    }

    function removeAllCheck() {
        $('input[name="eiprow"]:checked').each(function () {
            $(this)[0].checked = false;
            $(this).change();
        });
    }
});