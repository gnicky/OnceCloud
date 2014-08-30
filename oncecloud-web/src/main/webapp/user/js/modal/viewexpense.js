$(document).ready(function () {

    $("#SummaryModalContainer").on("hidden", function () {
        $(this).removeData("modal");
        $(this).children().remove();
    });

    var options = {
        bootstrapMajorVersion: 3,
        currentPage: 1,
        totalPages: 1,
        numberOfPages: 0,
        onPageClicked: function (e, originalEvent, type, page) {
            getFeeDetailList(page, 5, "");
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
    $('#sdetailP').bootstrapPaginator(options);

    var pages = $('#sdetailP').bootstrapPaginator("getPages");
    pageDisplayUpdate(pages.current, pages.total);
    getFeeDetailList(1, 5, "");

    function getFeeDetailList(page, limitnum, search) {
        var type = $("#tablebodydetail").attr('type');
        var resourceId = $("#tablebodydetail").attr('uuid');
        var btable = document.getElementById("tablebodydetail");
        btable.innerHTML = "";
        var detailTotal = 0.0;
        $.ajax({
            type: 'get',
            url: '/FeeAction',
            data: "action=getdetaillist&page=" + page + "&limitnum=" + limitnum + "&search=" + search + "&type=" + type + "&uuid=" + resourceId,
            dataType: 'text',
            success: function (response) {
                var array = $.parseJSON(response);
                var tableStr = "";
                if (array[0].totalpage == 0) {
                    options = {
                        totalPages: 1,
                    }
                    $('#sdetailP').bootstrapPaginator(options);
                } else {
                    if (array[0].totalpage % limitnum == 0) {
                        options = {
                            totalPages: array[0].totalpage / limitnum,
                        }
                        $('#sdetailP').bootstrapPaginator(options);
                    } else {
                        options = {
                            totalPages: array[0].totalpage / limitnum + 1,
                        }
                        $('#sdetailP').bootstrapPaginator(options);
                    }
                }
                var pages = $('#sdetailP').bootstrapPaginator("getPages");
                pageDisplayUpdate(page, pages.total);
                for (var i = 1; i < array.length; i++) {
                    var obj = array[i];
                    var feeExpense = obj.feeExpense;
                    detailTotal = detailTotal + feeExpense;
                    var feeStartDate = obj.feeStartDate;
                    var feeEndDate = obj.feeEndDate;
                    var feePrice = obj.feePrice;
                    tableStr = tableStr + '<tr><td class="time">' + feeStartDate + '<br>'
                        + feeEndDate + '</td><td style="text-align:right"><span class="price out">-&nbsp;'
                        + feeExpense.toFixed(2) + '</span></td><td style="text-align:right"><span class="price">' + feePrice.toFixed(2)
                        + '</span><span class="unit">元/小时</span></td></tr>';
                }
                btable.innerHTML = tableStr;
                detailTotal = detailTotal.toFixed(2);
                $("#detail-total").text(detailTotal);
            },
            error: function () {

            }
        });
    }

    function pageDisplayUpdate(current, total) {
        var c = document.getElementById("currentPsdetail");
        var t = document.getElementById("totalPsdetail");
        c.innerHTML = current + "";
        t.innerHTML = total + "";
    }
});