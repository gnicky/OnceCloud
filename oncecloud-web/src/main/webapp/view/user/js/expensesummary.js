$(document).ready(function () {

    $("#query").on('click', function (event) {
        event.preventDefault();
        window.location.href = $("#platformcontent").attr('platformBasePath') + 'user/expensequery.jsp';
    });

    initTotalExpense();

    var options = {
        bootstrapMajorVersion: 3,
        currentPage: 1,
        totalPages: 1,
        numberOfPages: 0,
        onPageClicked: function (e, originalEvent, type, page) {
            var limitnum = document.getElementById("limit").value;
            var displayType = $('.selected', $('.charge-summary')).attr('type');
            getFeeList(displayType, page, limitnum, "");
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

    $('#summaryP').bootstrapPaginator(options);
    var pages = $('#summaryP').bootstrapPaginator("getPages");
    pageDisplayUpdate(pages.current, pages.total);
    getFeeList("instance", 1, 10, "");

    $('#limit').on('focusout', function () {
        var limitnum = $("#limit").val();
        var reg = /^[0-9]*[1-9][0-9]*$/;
        if (!reg.test(limitnum)) {
            $("#limit").val(10);
            limitnum = 10;
        }
        var displayType = $('.selected', $('.charge-summary')).attr('type');
        getFeeList(displayType, 1, limitnum, "");
        options = {
            currentPage: 1,
        }
        $('#summaryP').bootstrapPaginator(options);
    });

    $('.charge-summary').on('click', '.summary-item', function (event) {
        event.preventDefault();
        $('div', $('.charge-summary')).removeClass('selected');
        $(this).addClass('selected');
        var displayType = $(this).attr('type');
        getFeeList(displayType, 1, 10, "");
    });

    function initTotalExpense() {
        $.ajax({
            type: 'get',
            url: '/FeeAction',
            data: "action=initfee",
            dataType: 'text',
            success: function (response) {
                var obj = $.parseJSON(response);
                var totalFee = obj.totalFee.toFixed(2);
                var instanceFee = obj.instanceFee.toFixed(2);
                var volumeFee = obj.volumeFee.toFixed(2);
                var snapshotFee = obj.snapshotFee.toFixed(2);
                var imageFee = obj.imageFee.toFixed(2);
                var eipFee = obj.eipFee.toFixed(2);
                var insertFee = document.getElementById("resource-total");
                insertFee.innerHTML = totalFee + '<span class="unit">元</span>';
                insertFee = document.getElementById("instance-total");
                insertFee.innerHTML = instanceFee + '<span class="unit">元</span>';
                insertFee = document.getElementById("volume-total");
                insertFee.innerHTML = volumeFee + '<span class="unit">元</span>';
                insertFee = document.getElementById("snapshot-total");
                insertFee.innerHTML = snapshotFee + '<span class="unit">元</span>';
                insertFee = document.getElementById("image-total");
                insertFee.innerHTML = imageFee + '<span class="unit">元</span>';
                insertFee = document.getElementById("eip-total");
                insertFee.innerHTML = eipFee + '<span class="unit">元</span>';
            },
            error: function () {

            }
        });
    }

    function getFeeList(displayType, page, limitnum, search) {
        var btable = document.getElementById("tablebody");
        btable.innerHTML = "";
        $.ajax({
            type: 'get',
            url: '/FeeAction',
            data: "action=getlist&page=" + page + "&limitnum=" + limitnum + "&search=" + search + "&type=" + displayType,
            dataType: 'text',
            success: function (response) {
                var array = $.parseJSON(response);
                var tableStr = "";
                if (array[0].totalpage == 0) {
                    options = {
                        totalPages: 1,
                    }
                    $('#summaryP').bootstrapPaginator(options);
                } else {
                    if (array[0].totalpage % limitnum == 0) {
                        options = {
                            totalPages: array[0].totalpage / limitnum,
                        }
                        $('#summaryP').bootstrapPaginator(options);
                    } else {
                        options = {
                            totalPages: array[0].totalpage / limitnum + 1,
                        }
                        $('#summaryP').bootstrapPaginator(options);
                    }
                }
                var pages = $('#summaryP').bootstrapPaginator("getPages");
                pageDisplayUpdate(page, pages.total);
                for (var i = 1; i < array.length; i++) {
                    var obj = array[i];
                    var feeExpense = obj.feeExpense.toFixed(2);
                    var feeName = decodeURI(obj.feeName);
                    var resourceId = obj.resourceId;
                    var resourceStr;
                    if (displayType == "instance") {
                        resourceStr = "<a class='viewdetail' href='javascript:void(0)'>i-" + resourceId.substring(0, 8) + "</a>";
                    } else if (displayType == "eip") {
                        resourceStr = "<a class='viewdetail' href='javascript:void(0)'>ip-" + resourceId.substring(0, 8) + "</a>";
                    } else if (displayType == "volume") {
                        resourceStr = "<a class='viewdetail' href='javascript:void(0)'>vol-" + resourceId.substring(0, 8) + "</a>";
                    } else if (displayType == "snapshot") {
                        resourceStr = "<a class='viewdetail' href='javascript:void(0)'>ss-" + resourceId.substring(0, 8) + "</a>";
                    } else if (displayType == "image") {
                        resourceStr = "<a class='viewdetail' href='javascript:void(0)'>image-" + resourceId.substring(0, 8) + "</a>";
                    }
                    var feeState = obj.feeState;
                    var stateStr = '<span class="icon-status icon-stopped"></span><span>已销毁</span>';
                    if (feeState == 1) {
                        stateStr = '<span class="icon-status icon-using"></span><span>使用中</span>';
                    }
                    var feePrice = obj.feePrice.toFixed(2);
                    var feeCreateDate = obj.feeCreateDate;
                    tableStr = tableStr + '<tr type="' + displayType + '" resourceId="' + resourceId + '"><td>'
                        + resourceStr + '</td><td>' + feeName + '</td><td>' + stateStr + '</td><td style="text-align:right"><span class="price out">-&nbsp;'
                        + feeExpense + '</span></td><td style="text-align:right"><span class="price">' + feePrice
                        + '</span><span class="unit">元/小时</span></td><td class="time">' + feeCreateDate
                        + '</td><td class="viewdetail"><a href="javascript:void(0)">查看详情</a></td></tr>';
                }
                btable.innerHTML = tableStr;
                rowbind();
            },
            error: function () {

            }
        });
    }

    function pageDisplayUpdate(current, total) {
        var c = document.getElementById("currentPsummary");
        var t = document.getElementById("totalPsummary");
        c.innerHTML = current + "";
        t.innerHTML = total + "";
    }

    function rowbind() {
        $(".viewdetail").on('click', function (event) {
            event.preventDefault();
            var url = $("#platformcontent").attr('platformBasePath') + 'user/modal/viewexpense.jsp';
            var type = $(this).parent().attr('type');
            var resourceId = $(this).parent().attr('resourceId');
            $('#SummaryModalContainer').load(url, {"detailtype": type, "detailuuid": resourceId}, function () {
                $('#SummaryModalContainer').modal({
                    backdrop: false,
                    show: true
                });
            });
        });
    }

});