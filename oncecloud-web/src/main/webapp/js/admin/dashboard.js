getLogList(4, 0);
getQuestionList(1, 10, "");

$.ajax({
    type: 'get',
    url: '/DatacenterAction/Overview',
    dataType: 'json',
    success: function (array) {
        if (array.length == 1) {
            var item = array[0];
            $("#dcNum").text(item.viewDc);
            $("#rackNum").text(item.viewRack);
            $("#poolNum").text(item.viewPool);
            $("#serviceNum").text(item.viewServer);
            $("#srNum").text(item.viewSr);
            $("#vmNum").text(item.viewVm);
            $("#outipNum").text(item.viewOutip);
            $("#dhcpNum").text(item.viewDhcp);
            $("#fireNum").text(item.viewFirewall);
            $("#imageNum").text(item.viewImage);
        }
    }
});

$('.resource-item').on('click', function (event) {
    event.preventDefault();
    var type = $(this).data("type");
    window.location.href = "/" + type;
});

function getQuestionList(page, limit, search) {
	$('#service-area').html("");
    $.ajax({
        type: 'get',
        url: '/QAAction/QuestionList',
        data: {page: page, limit: limit, search: search},
        dataType: 'json',
        success: function (array) {
            if (array.length > 1) {
                var allitem = "";
                for (var i = 1; i < array.length; i++) {
                    var obj = array[i];
                    var qaTitle = obj.qaTitle;
                    var qaTime = obj.qaTime;
                    var qaStatus = obj.qaStatus;
                    var qaReply = obj.qaReply;
                    var serviceIcon = '<span class="glyphicon glyphicon-question-sign"></span>';
                    var statusSpan;
                    if (qaStatus == 1) {
                        if (qaReply == 0) {
                            statusSpan = '<span class="consumed"><span class="icon-status icon-using"></span>新建</span>';
                        } else {
                            statusSpan = '<span class="consumed"><span class="icon-status icon-running"></span>处理中</span>';
                        }
                    } else {
                        statusSpan = '<span class="consumed"><span class="icon-status icon-close"></span>已关闭</span>';
                    }
                    var thisitem = '<div class="act-item">' + serviceIcon
                        + '<span>' + decodeURI(qaTitle) + '</span>'
                        + '<span class="created"><span class="glyphicon glyphicon-time icon-right"></span>' + qaTime + '</span>'
                        + statusSpan + '</div>';
                    allitem += thisitem;
                }
                $('#service-area').html(allitem);
            } else {
                $('#service-area').html('<div><span class="unit">目前没有表单<span></div>');
            }
        }
    });
}

function getLogList(status, start) {
    $('#act-area').html("");
    $.ajax({
        type: 'get',
         url: '/LogAction',
        data: {status: status, start: start, num: "10"},
        dataType: 'json',
        success: function (jsonArray) {
            if (jsonArray.length > 0) {
                var iconStr = new Array("cloud", "inbox", "camera", "globe", "globe",
                    "flash", "record", "sort", "fullscreen", "random", "camera", "flash",
                    "user", "tint", "hdd", "road", "tasks", "fullscreen", "globe", "indent-left", "thumbs-up");
                var statusIconStr = new Array("remove", "ok");
                for (var i = 0; i < jsonArray.length; i++) {
                    var jsonObj = jsonArray[i];
                    var logObject = jsonObj.logObject;
                    var logObjectStr = decodeURI(jsonObj.logObjectStr);
                    var logActionStr = decodeURI(jsonObj.logActionStr);
                    var logStatus = jsonObj.logStatus;
                    var logTime = jsonObj.logTime;
                    var logElapse = jsonObj.logElapse;
                    var logElapseSpan = "";
                    if (logStatus < 2) {
                        var logElapseStr = logElapse + "秒";
                        if (logElapse == 0) {
                            logElapseStr = "< 1秒";
                        }
                        logElapseSpan = '<span class="consumed"><span class="icon-right glyphicon glyphicon-'
                            + statusIconStr[logStatus] + '"></span>' + logElapseStr + '</span>';
                    }
                    var logIcon = '<span class="glyphicon glyphicon-' + iconStr[logObject] + '"></span>';
                    var alertItem = '<div class="act-item">' + logIcon
                        + '<span>' + logActionStr + logObjectStr + '</span>'
                        + '<span class="created"><span class="glyphicon glyphicon-time icon-right"></span>' + logTime + '</span>'
                        + logElapseSpan + '</div>';
                    $('#act-area').append(alertItem);
                }
            } else {
                $('#act-area').html('<div><span class="unit">目前没有操作<span></div>');
            }
        }
    });
}