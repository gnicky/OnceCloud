$(document).ready(function () {
    getLogList(4, 0);
    getServiceList(1, 10, "");
    getBalance();

    $('#viewquota').on('click', function (event) {
        event.preventDefault();
        $('#DashboardModalContainer').load($(this).attr('url'), '', function () {
            $('#DashboardModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });

    $("#DashboardModalContainer").on("hidden", function () {
        $(this).removeData("modal");
        $(this).children().remove();
    });

    $('.resource-item').on('click', function (event) {
        event.preventDefault();
        var type = $(this).data("type");
        if (type == "service") {
            window.location.href = "../common/service.jsp";
        } else if (type == "image") {
            window.location.href = "../common/image.jsp";
        } else {
        	window.location.href = "../user/" + type + ".jsp";
        }
    });

    function getBalance() {
        $.ajax({
            type: 'get',
            url: '/UserAction',
            data: 'action=getbalance',
            dataType: 'json',
            success: function (obj) {
                $('#charge-total').html(obj.balance.toFixed(2));
            },
            error: function () {
            }
        });
    }

    function getServiceList(page, limit, search) {
        $.ajax({
            type: 'get',
            url: '/QAAction',
            data: "action=getquestion&page=" + page + "&limit=" + limit + "&search=" + search,
            dataType: 'json',
            success: function (array) {
                if (array.length <= 1) {
                    $('#service-area').html('<div><span class="unit">目前没有表单<span></div>');
                } else {
                    var tableStr = "";
                    for (var i = 1; i < array.length; i++) {
                        var obj = array[i];
                        var qaTitle = decodeURI(obj.qaTitle);
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
                        tableStr = tableStr + '<div class="act-item">' + serviceIcon
                            + '<span>' + decodeURI(qaTitle) + '</span>'
                            + '<span class="created"><span class="glyphicon glyphicon-time icon-right"></span>' + qaTime + '</span>'
                            + statusSpan + '</div>';
                    }
                    $('#service-area').html(tableStr);
                }
            },
            error: function () {
            }
        });
    }

    function getLogList(logStatus, start) {
        $.ajax({
            type: 'post',
            url: '/LogAction',
            data: "status=" + logStatus + "&start=" + start + "&num=10",
            dataType: 'json',
            success: function (jsonArray) {
                var logArea = document.getElementById("act-area");
                logArea.innerHTML = "";
                if (jsonArray.length == 0) {
                    logArea.innerHTML = '<div><span class="unit">目前没有操作<span></div>';
                } else {
                    var iconStr = new Array("cloud", "inbox", "camera", "globe", "globe",
                        "flash", "record", "sort", "fullscreen", "random", "camera", "flash",
                        "user", "tint", "hdd", "road", "tasks", "fullscreen", "globe", "indent-left", "thumbs-up");
                    var statusStr = new Array("danger", "success", "warning", "info");
                    var statusIconStr = new Array("remove", "ok");
                    for (var i = 0; i < jsonArray.length; i++) {
                        var jsonObj = jsonArray[i];
                        var logId = jsonObj.logId;
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
                        $(logArea).append(alertItem);
                    }
                }
            },
            error: function () {
            }
        });
    }
});