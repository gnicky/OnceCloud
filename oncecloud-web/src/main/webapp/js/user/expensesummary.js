getFeeSummary();
reloadList(1);

function reloadList(page) {
    var limit = $('#limit').val();
    var search = $('#search').val();
    var type = $('.selected', $('.charge-summary')).attr('type');
    getFeeList(page, limit, search, type);
    if (page == 1) {
        options = {
            currentPage: 1
        };
        $('#pageDivider').bootstrapPaginator(options);
    }
}

$('.charge-summary').on('click', '.summary-item', function (event) {
    event.preventDefault();
    $('div', $('.charge-summary')).removeClass('selected');
    $(this).addClass('selected');
    reloadList();
});

function getFeeSummary() {
    $.ajax({
        type: 'get',
        url: '/FeeAction/FeeSummary',
        dataType: 'json',
        success: function (obj) {
            var totalFee = obj.totalFee.toFixed(2);
            var instanceFee = obj.instanceFee.toFixed(2);
            var volumeFee = obj.volumeFee.toFixed(2);
            var snapshotFee = obj.snapshotFee.toFixed(2);
            var imageFee = obj.imageFee.toFixed(2);
            var eipFee = obj.eipFee.toFixed(2);
            $('#resource-total').html(totalFee + '<span class="unit">元</span>');
            $('#instance-total').html(instanceFee + '<span class="unit">元</span>');
            $('#volume-total').html(volumeFee + '<span class="unit">元</span>');
            $('#snapshot-total').html(snapshotFee + '<span class="unit">元</span>');
            $('#image-total').html(imageFee + '<span class="unit">元</span>');
            $('#eip-total').html(eipFee + '<span class="unit">元</span>');
        }
    });
}

function getFeeList(page, limit, search, type) {
    $('#tablebody').html("");
    $.ajax({
        type: 'get',
        url: '/FeeAction/FeeList',
        data: {page: page, limit: limit, search: search, type: type},
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
                var feeExpense = obj.feeExpense.toFixed(2);
                var feeName = decodeURIComponent(obj.feeName);
                var resourceId = obj.resourceId;
                var resourceStr = "";
                if (type == "instance") {
                    resourceStr = "<a class='viewdetail' href='javascript:void(0)'>i-" + resourceId.substring(0, 8) + "</a>";
                } else if (type == "eip") {
                    resourceStr = "<a class='viewdetail' href='javascript:void(0)'>eip-" + resourceId.substring(0, 8) + "</a>";
                } else if (type == "volume") {
                    resourceStr = "<a class='viewdetail' href='javascript:void(0)'>vol-" + resourceId.substring(0, 8) + "</a>";
                } else if (type == "snapshot") {
                    resourceStr = "<a class='viewdetail' href='javascript:void(0)'>ss-" + resourceId.substring(0, 8) + "</a>";
                } else if (type == "image") {
                    resourceStr = "<a class='viewdetail' href='javascript:void(0)'>image-" + resourceId.substring(0, 8) + "</a>";
                }
                var feeState = obj.feeState;
                var stateStr = '<span class="icon-status icon-stopped"></span><span>已销毁</span>';
                if (feeState == 1) {
                    stateStr = '<span class="icon-status icon-using"></span><span>使用中</span>';
                }
                var feePrice = obj.feePrice.toFixed(2);
                var feeCreateDate = obj.feeCreateDate;
                var thistr = '<tr type="' + type + '" resourceId="' + resourceId + '"><td>'
                    + resourceStr + '</td><td>' + feeName + '</td><td>' + stateStr + '</td><td style="text-align:right"><span class="price out">-&nbsp;'
                    + feeExpense + '</span></td><td style="text-align:right"><span class="price">' + feePrice
                    + '</span><span class="unit">元/小时</span></td><td class="time">' + feeCreateDate
                    + '</td><td class="viewdetail"><a href="javascript:void(0)">查看详情</a></td></tr>';
                tableStr += thistr;
            }
            $('#tablebody').html(tableStr);
        }
    });
}

$("#tablebody").on('click', ".viewdetail", function (event) {
    event.preventDefault();
    var url = $("#platformcontent").attr('basePath') + 'expense/view';
    var type = $(this).parents('tr').attr('type');
    var resourceId = $(this).parents('tr').attr('resourceId');
    $('#SummaryModalContainer').load(url, {"type": type, "resourceUuid": resourceId}, function () {
        $('#SummaryModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
});