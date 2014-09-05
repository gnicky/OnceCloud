reloadList();
	
function reloadList() {
    var limit = $("#limit").val();
    getHistoryList(1, limit);
    options = {
        currentPage: 1
    }
    $('#pageDivider').bootstrapPaginator(options);
}

function getHistoryList(page, limit) {
    $('#tablebody').html("");
    $.ajax({
        type: 'get',
        url: '/ChargeAction/ChargeList',
        data: {page:page, limit:limit},
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
                var recordBill = obj.recordbill;
                var recordDate = obj.recorddate;
                var recordType = obj.recordtype;
                var recordStr = new Array("用户充值", "系统赠送");
                tableStr = tableStr + '<tr><td>' + recordBill + '</td><td>' + recordStr[recordType]
                    + '</td><td>' + recordDate + '</td></tr>';
            }
            $('#tablebody').html(tableStr);
        },
        error: function () {
        }
    });
}