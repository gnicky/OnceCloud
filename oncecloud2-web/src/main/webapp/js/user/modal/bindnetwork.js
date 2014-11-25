$(document).ready(function () {

    $("#InstanceModalContainer").on("hidden", function () {
        $(this).removeData("modal");
        $(this).children().remove();
    });

    $('#bind2vm').on('click', function (event) {
        event.preventDefault();
        var vmuuidStr = '[';
        var vnId = '';
        var flag = 0;
        $('input[name="vmrow"]:checked').each(function () {
            flag++;
            var vmuuid = $(this).parent().parent().attr('rowid');
            vmuuidStr = flag == 1 ? vmuuidStr + vmuuid : vmuuidStr + "," + vmuuid;
        });
        vmuuidStr = vmuuidStr + ']';

        var vnList = document.getElementsByName("image-item");
        for (var i = 0; i < vnList.length; i++) {
            if ($(vnList[i]).hasClass('selected')) {
                vnId = $(vnList[i]).attr("uuid");
            }
        }

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
            getVlanList(page, 5, "");
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
    getVlanList(1, 5, "");

    function getVlanList(page, limit, search) {
        $('#vlanlist').html("");
        $.ajax({
            type: 'get',
            url: '/VnetAction/VnetList',
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
                $('#bindPS').bootstrapPaginator(options);
                pageDisplayUpdate(page, totalp);
                var tableStr = "";
                tableStr = tableStr + '<div name="image-item" class="image-item" uuid="-1"><div class="image-left">'
                    + "基础网络" + '</div>&nbsp;&nbsp;</div>';
                if (0 != totalnum) {
                    for (var i = 1; i < array.length; i++) {
                        var obj = array[i];
                        var uuid = obj.uuid;
                        var vnid = "vn-" + obj.uuid.substring(0, 8);
                        var name = decodeURIComponent(obj.name);
                        var routerid = "rt-" + obj.routerid.substring(0, 8);
                        tableStr = tableStr + '<div name="image-item" class="image-item" uuid="'
                            + uuid + '"><div class="image-left">'
                            + name + '</div>ID:&nbsp;&nbsp;' + vnid + '</div>';
                    }
                }
                $('#vlanlist').html(tableStr);
            }
        });
    }

    $('.vlanlist').on('click', '.image-item', function (event) {
        event.preventDefault();
        $('div[name="image-item"]').each(function () {
            if ($(this).hasClass('selected')) {
                $(this).removeClass('selected');
            }
        });
        $(this).addClass('selected');
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
    	var content = "<div class='alert alert-warning'>主机正在加入私有网络</div>";
    	var conid = showMessageNoAutoClose(content);
        $.ajax({
            type: 'get',
            url: '/VnetAction/AddVM',
            data: {vnId: vnId, vmuuidStr: vmuuidStr, content:content, conid:conid},
            dataType: 'json',
            success: function (obj) {
            	if (obj.isSuccess)
                	reloadList(1);
                else {
					bootbox.dialog({
						message : '<div class="alert alert-danger" style="margin:10px"><span class="glyphicon glyphicon-warning-sign"></span>&nbsp;每台主机只能绑定一个网络或绑定时出错</div>',
						title : "提示",
						buttons : {
							main : {
								label : "确定",
								className : "btn-primary",
								callback : function() {
								}
							}
						}
					});
				}
            }
        });
    }

    function pageDisplayUpdate(current, total) {
        $('#currentPS').html(current);
        $('#totalPS').html(total);
    }

    function removeAllCheck() {
        $('input[name="vmrow"]:checked').each(function () {
            $(this)[0].checked = false;
            $(this).change();
        });
    }
});