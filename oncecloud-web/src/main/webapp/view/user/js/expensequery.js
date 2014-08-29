$(document).ready(function () {

    $("#summary").on('click', function (event) {
        event.preventDefault();
        window.location.href = $("#platformcontent").attr('platformBasePath') + 'user/expensesummary.jsp';
    });

    init();

    var options = {
        bootstrapMajorVersion: 3,
        currentPage: 1,
        totalPages: 1,
        numberOfPages: 0,
        onPageClicked: function (e, originalEvent, type, page) {
            var limitnum = document.getElementById("limit").value;
            var type = $("input[name='resource-type']:checked").val();
            var month = $("#start-time").val();
            getQueryList(type, month, page, limitnum, "");
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

    $('#queryP').bootstrapPaginator(options);
    var pages = $('#queryP').bootstrapPaginator("getPages");
    pageDisplayUpdate(pages.current, pages.total);

    $('#limit').on('focusout', function () {
        var limitnum = $("#limit").val();
        var reg = /^[0-9]*[1-9][0-9]*$/;
        if (!reg.test(limitnum)) {
            $("#limit").val(10);
            limitnum = 10;
        }
        var type = $("input[name='resource-type']:checked").val();
        var month = $("#start-time").val();
        getQueryList(type, month, 1, limitnum, "");
        options = {
            currentPage: 1,
        }
        $('#queryP').bootstrapPaginator(options);
    });

    $('#query-submit').on('click', function (event) {
        event.preventDefault();
        var limitnum = document.getElementById("limit").value;
        var type = $("input[name='resource-type']:checked").val();
        var month = $("#start-time").val();
        getQueryList(type, month, 1, limitnum, "");
        $("#query-result").css("display", "block");
    });

    $('#reset-submit').on('click', function (event) {
        event.preventDefault();
        init();
    });

    function getQueryList(type, month, page, limitnum, search) {
        var btable = document.getElementById("tablebody");
        btable.innerHTML = "";
        var totalPrice = 0.0;
        $.ajax({
            type: 'get',
            url: '/FeeAction',
            data: "action=querylist&page=" + page + "&limitnum=" + limitnum + "&search=" + search + "&type=" + type + "&month=" + month,
            dataType: 'text',
            success: function (response) {
                var array = $.parseJSON(response);
                var tableStr = "";
                if (array[0].totalpage == 0) {
                    options = {
                        totalPages: 1,
                    }
                    $('#queryP').bootstrapPaginator(options);
                } else {
                    if (array[0].totalpage % limitnum == 0) {
                        options = {
                            totalPages: array[0].totalpage / limitnum,
                        }
                        $('#queryP').bootstrapPaginator(options);
                    } else {
                        options = {
                            totalPages: array[0].totalpage / limitnum + 1,
                        }
                        $('#queryP').bootstrapPaginator(options);
                    }
                }
                var pages = $('#queryP').bootstrapPaginator("getPages");
                pageDisplayUpdate(page, pages.total);
                for (var i = 1; i < array.length; i++) {
                    var obj = array[i];
                    var feeId = obj.feeId;
                    var feeExpense = obj.feeExpense;
                    totalPrice = totalPrice + feeExpense;
                    var feeStartDate = obj.feeStartDate;
                    var feeEndDate = obj.feeEndDate;
                    var feePrice = obj.feePrice;
                    var resourceStr;
                    if (type == "instance") {
                        resourceStr = "<a class='viewdetail' href='javascript:void(0)'>i-" + feeId.substring(0, 8) + "</a>";
                    } else if (type == "eip") {
                        resourceStr = "<a class='viewdetail' href='javascript:void(0)'>ip-" + feeId.substring(0, 8) + "</a>";
                    } else if (type == "volume") {
                        resourceStr = "<a class='viewdetail' href='javascript:void(0)'>vol-" + feeId.substring(0, 8) + "</a>";
                    } else if (type == "snapshot") {
                        resourceStr = "<a class='viewdetail' href='javascript:void(0)'>ss-" + feeId.substring(0, 8) + "</a>";
                    } else if (type == "image") {
                        resourceStr = "<a class='viewdetail' href='javascript:void(0)'>image-" + feeId.substring(0, 8) + "</a>";
                    }
                    tableStr = tableStr + '<tr><td>' + resourceStr + '</td><td class="time">' + feeStartDate + '<br>'
                        + feeEndDate + '</td><td style="text-align:right"><span class="price out">-&nbsp;'
                        + feeExpense.toFixed(2) + '</span></td><td style="text-align:right"><span class="price">' + feePrice.toFixed(2)
                        + '</span><span class="unit">元/小时</span></td></tr>';
                }
                btable.innerHTML = tableStr;
                $("#total-price").text(totalPrice.toFixed(2));
            },
            error: function () {

            }
        });
    }

    function pageDisplayUpdate(current, total) {
        var c = document.getElementById("currentPquery");
        var t = document.getElementById("totalPquery");
        c.innerHTML = current + "";
        t.innerHTML = total + "";
    }

    function init() {
        var rad = document.getElementsByName("resource-type");
        rad[0].checked = 'checked';
        var month = new Date();
        var monthStr = new Array("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        $("#start-time").val(month.getFullYear() + '-' + monthStr[month.getMonth()]);
    }
});