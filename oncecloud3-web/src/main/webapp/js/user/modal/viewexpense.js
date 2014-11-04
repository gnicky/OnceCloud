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
getFeeDetailList(1, 5, "");

function getFeeDetailList(page, limit, search) {
    var type = $("#tablebodydetail").attr('type');
    var resourceId = $("#tablebodydetail").attr('uuid');
    $('#tablebodydetail').html("");
    var detailTotal = 0.0;
    $.ajax({
        type: 'get',
        url: '/FeeAction/DetailList',
        data: {page: page, limit: limit, search: search, type: type, uuid: resourceId},
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
            $('#sdetailP').bootstrapPaginator(options);
            modalDisplayUpdate(page, totalp);
            var tableStr = "";
            for (var i = 1; i < array.length; i++) {
                var obj = array[i];
                var feeExpense = obj.feeExpense;
                detailTotal = detailTotal + feeExpense;
                var feeStartDate = obj.feeStartDate;
                var feeEndDate = obj.feeEndDate;
                var feePrice = obj.feePrice;
                var thistr = '<tr><td class="time">' + feeStartDate + '<br>'
                    + feeEndDate + '</td><td style="text-align:right"><span class="price out">-&nbsp;'
                    + feeExpense.toFixed(2) + '</span></td><td style="text-align:right"><span class="price">' + feePrice.toFixed(2)
                    + '</span><span class="unit">元/小时</span></td></tr>';
                tableStr += thistr;
            }
            $('#tablebodydetail').html(tableStr);
            detailTotal = detailTotal.toFixed(2);
            $("#detail-total").text(detailTotal);
        }
    });
}

function modalDisplayUpdate(current, total) {
    $('#currentPsdetail').html(current);
    $('#totalPsdetail').html(total);
}