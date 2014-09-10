$(document).ready(function () {

    $("#VnetModalContainer").on("hidden", function () {
        $(this).removeData("modal");
        $(this).children().remove();
    });

    $('#bind2vm').on('click', function (event) {
        event.preventDefault();
        var vnId;
        $('input[name="vnrow"]:checked').each(function () {
            vnId = $(this).parent().parent().attr('rowid');
        });
        var vmboxes = document.getElementsByName("image-item");
        var vmuuidStr = '[';
        var flag = 0;
        for (var i = 0; i < vmboxes.length; i++) {
            if ($(vmboxes[i]).hasClass('selected')) {
                flag++;
                var vmuuid = $(vmboxes[i]).attr("vmuuid");
                if (flag == 1) {
                    vmuuidStr = vmuuidStr + vmuuid;
                } else {
                    vmuuidStr = vmuuidStr + ',' + vmuuid;
                }
            }
        }
        vmuuidStr = vmuuidStr + ']';
        bindvn(vnId, vmuuidStr);
        removeAllCheck();
    });

    $('#cancelbind').on('click', function (event) {
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
    $('#bindPS').bootstrapPaginator(options);
    getInstanceList(1, 5, "");

    function getInstanceList(page, limit, search) {
        $('#instancelist').html("");
        $.ajax({
            type: 'get',
            url: '/VMAction/VMList',
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
                }
                $('#bindPS').bootstrapPaginator(options);
                pageDisplayUpdate(page, totalp);
                var tableStr = "";
                if (0 != totalnum) {
                    for (var i = 1; i < array.length; i++) {
                        var obj = array[i];
                        var vmuuid = obj.vmid;
                        var vmName = decodeURIComponent(obj.vmname);
                        var showuuid = "i-" + vmuuid.substring(0, 8);
                        tableStr = tableStr + '<div name="image-item" class="image-item" vmuuid="'
                            + vmuuid + '"><div class="image-left">'
                            + vmName + '</div>ID:&nbsp;&nbsp;' + showuuid + '</div>';
                    }
                } else {
                    $('#alert').html('没有可选择的主机');
                }
                $('#instancelist').html(tableStr);
            },
            error: function () {
            }
        });
    }

    $('.instancelist').on('click', '.image-item', function (event) {
        event.preventDefault();
        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
        } else {
            $(this).addClass('selected');
        }
        var count = 0;
        $('div[name="image-item"]').each(function () {
            if ($(this).hasClass('selected')) {
                count++;
            }
        });
        if (count == 0) {
            $('#bind2vm').attr('disabled', true);
        } else {
            $('#bind2vm').attr('disabled', false);
        }
    });

    function bindvn(vnId, vmuuidStr) {
        $.ajax({
            type: 'get',
            url: '/VnetAction/AddVM',
            data: {vnId: vnId, vmuuidStr: vmuuidStr},
            dataType: 'text',
            success: function (response) {
            },
            error: function () {
            }
        });
    }

    function pageDisplayUpdate(current, total) {
        $('#currentPS').html(current);
        $('#totalPS').html(total);
    }

    function removeAllCheck() {
        $('input[name="vnrow"]:checked').each(function () {
            $(this)[0].checked = false;
            $(this).change();
        });
    }
});