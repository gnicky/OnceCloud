$(document).ready(function () {
    $("#EipModalContainer").on("hidden", function () {
        $(this).removeData("modal");
        $(this).children().remove();
    });

    $('#bind2vm').on('click', function (event) {
        event.preventDefault();
        var bindVmuuid = $('.instancelist').find('.selected').attr("vmuuid");
        $('input[name="eiprow"]:checked').each(function () {
            bindeip(bindVmuuid, $(this).parent().parent().attr("eip"));
        });
        removeAllCheck();
    });

    $('#bindcancel').on('click', function (event) {
        event.preventDefault();
        removeAllCheck();
    });

    var options = {
        bootstrapMajorVersion: 3,
        currentPage: 1,
        totalPages: 1,
        numberOfPages: 0,
        onPageClicked: function (e, originalEvent, type, page) {
            getInstanceList(page, 5, "");
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
    $('#bindP').bootstrapPaginator(options);
    getInstanceList(1, 5, "");

    $('.instancelist').on('click', '.image-item', function (event) {
        event.preventDefault();
        $('div', $('.instancelist')).removeClass('selected');
        $(this).addClass('selected');
    });

    function getInstanceList(page, limit, search) {
        var bindtype = $('#bindmodal').attr('type');
        $('#instancelist').html("");
        $.ajax({
            type: 'get',
            url: '/EipAction',
            data: 'action=getablevms&page=' + page + '&limit=' + limit + '&search=' + search + '&bindtype=' + bindtype,
            dataType: 'json',
            success: function (array) {
                var totalnum = array[0];
                if (totalnum != 0) {
                    var totalp = 1;
                    totalp = Math.ceil(totalnum / limit);
                    options = {
                        totalPages: totalp
                    }
                    $('#bindP').bootstrapPaginator(options);
                    pageDisplayUpdate(page, totalp);
                    var tableStr = "";
                    for (var i = 1; i < array.length; i++) {
                        var obj = array[i];
                        var vmuuid = obj.vmid;
                        var vmName = decodeURI(obj.vmname);
                        var showuuid = "i-" + vmuuid.substring(0, 8);
                        if (i == 1) {
                            tableStr = tableStr + '<div class="image-item selected" vmuuid="'
                                + vmuuid + '"><div class="image-left">' + vmName + '</div>ID:&nbsp;&nbsp;' + showuuid + '</div>';
                        } else {
                            tableStr = tableStr + '<div class="image-item" vmuuid="'
                                + vmuuid + '"><div class="image-left">' + vmName + '</div>ID:&nbsp;&nbsp;' + showuuid + '</div>';
                        }
                    }
                    $('#instancelist').html(tableStr);
                } else {
                    $('#instancelist').html("没有可选择的项目");
                    $('#bind2vm').attr('disabled', true);
                }
            },
            error: function () {
            }
        });
    }

    function bindeip(vmuuid, eipIp) {
        var thistr = $("#tablebody").find('[eip="' + eipIp + '"]');
        var bindtype = $('#bindmodal').attr('type');
        thistr.find('[name="stateicon"]').removeClass("icon-running");
        thistr.find('[name="stateicon"]').addClass('icon-process');
        thistr.find('[name="stateword"]').text('绑定中');
        $.ajax({
            type: 'get',
            url: '/EipAction',
            data: 'action=bind&vmuuid=' + vmuuid + '&eipIp=' + eipIp + '&bindtype=' + bindtype,
            dataType: 'json',
            success: function (obj) {
                if (obj.result) {
                    thistr.find('[name="stateicon"]').removeClass('icon-process');
                    thistr.find('[name="stateicon"]').addClass('icon-using');
                    thistr.find('[name="stateword"]').text('已分配');
                    var vmtd = thistr.find('[vmuuid]');
                    vmtd.attr('vmuuid', obj.rsUuid);
                    vmtd.attr('type', bindtype);
                    if ('vm' == bindtype) {
                        vmtd.html('<a><span class="glyphicon glyphicon-cloud"></span>&nbsp;&nbsp;' + decodeURI(obj.rsName) + '</a>');
                    } else if ('lb' == bindtype) {
                        vmtd.html('<a><span class="glyphicon glyphicon-random"></span>&nbsp;&nbsp;' + decodeURI(obj.rsName) + '</a>');
                    } else if ('rt' == bindtype) {
                        vmtd.html('<a><span class="glyphicon glyphicon-fullscreen"></span>&nbsp;&nbsp;' + decodeURI(obj.rsName) + '</a>');
                    } else if ('db' == bindtype) {
                        vmtd.html('<a><span class="glyphicon glyphicon-inbox"></span>&nbsp;&nbsp;' + decodeURI(obj.rsName) + '</a>');
                    }
                } else {
                    thistr.find('[name="stateicon"]').removeClass('icon-process');
                    thistr.find('[name="stateicon"]').addClass('icon-running');
                    thistr.find('[name="stateword"]').text('可用');
                }
            },
            error: function () {
            }
        });
    }

    function pageDisplayUpdate(current, total) {
        $('#currentPbind').html(current);
        $('#totalPbind').html(total);
    }

    function removeAllCheck() {
        $('input[name="eiprow"]:checked').each(function () {
            $(this)[0].checked = false;
            $(this).change();
        });
    }
});