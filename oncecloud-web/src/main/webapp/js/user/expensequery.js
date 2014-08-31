initialize();
reloadList(1);

function reloadList(page) {
    var limit = $('#limit').val();
    var search = $('#search').val();
    var type = $("input[name='resource-type']:checked").val();
    var month = $("#start-time").val();
    getQueryList(page, limit, search, type, month);
    if (page == 1) {
        options = {
            currentPage: 1
        };
        $('#pageDivider').bootstrapPaginator(options);
    }
}

$('#query-submit').on('click', function (event) {
    event.preventDefault();
    reloadList();
    $("#query-result").css("display", "block");
});

$('#reset-submit').on('click', function (event) {
    event.preventDefault();
    initialize();
});

function getQueryList(page, limit, search, type, month) {
    $('#tablebody').html("");
    var totalPrice = 0.0;
    $.ajax({
        type: 'get',
        url: '/FeeAction/QueryList',
        data: {page: page, limit: limit, search: search, type: type, month: month},
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
                var thistr = '<tr><td>' + resourceStr + '</td><td class="time">' + feeStartDate + '<br>'
                    + feeEndDate + '</td><td style="text-align:right"><span class="price out">-&nbsp;'
                    + feeExpense.toFixed(2) + '</span></td><td style="text-align:right"><span class="price">' + feePrice.toFixed(2)
                    + '</span><span class="unit">元/小时</span></td></tr>';
                tableStr += thistr;
            }
            $('#tablebody').html(tableStr);
            $("#total-price").text(totalPrice.toFixed(2));
        }
    });
}

function initialize() {
    var month = new Date();
    var monthStr = new Array("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
    $("#start-time").val(month.getFullYear() + '-' + monthStr[month.getMonth()]);
}