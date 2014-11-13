reloadList(1);

function reloadList(page) {
    var limit = $("#limit").val();
    var search = $('#search').val();
    getAlarmLogList(page, limit, search);
    if (page == 1) {
        options = {
            currentPage: 1
        };
        $('#pageDivider').bootstrapPaginator(options);
    }
    allDisable();
}

function removeAllCheck() {
    $('input[name="alarmlogrow"]').each(function () {
        $(this).attr("checked", false);
        $(this).change();
    });
    allDisable();
}

function allDisable() {
    $("#delete").addClass('btn-disable').attr("disabled","disabled").removeClass("btn-primary");
    $("#read").addClass('btn-disable').attr("disabled","disabled").removeClass("btn-primary");
}

$('#tablebody').on('change', 'input:checkbox', function (event) {
    event.preventDefault();
    allDisable();
    var count = 0;
    var flag = 0;
    $('input[name="alarmlogrow"]:checked').each(function (index) {
        count++;
        if ($(this).attr("statusid") == 0) {
        	flag++;
        }
    });
    if (count == flag && count>0) {
         $("#read").removeClass('btn-disable').removeAttr("disabled").addClass("btn-primary");
    }
    if(flag == 0 && count>0)
        $("#delete").removeClass('btn-disable').removeAttr("disabled").addClass("btn-primary");
});




function getAlarmLogList(page, limit, search) {
    $('#tablebody').html("");
    $.ajax({
        type: 'get',
        url: '/AlarmLogAction/AlarmLogList',
        data: { page: page, limit: limit, search: search},
        dataType: 'json',
        success: function (array) {
            if (array.length >= 1) {
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
                    var alarmType = decodeURIComponent(obj.alarmType);
                    var alarmlogID = obj.alarmlogID;
                    var alarmObject = obj.alarmObject;
                    var alarmDesc = decodeURIComponent(obj.alarmDesc);
                    var alarmtatus = obj.alarmtatus;
                    var createDate = obj.createDate;
                    var showid = "";
                    var typeStr = "公有";
                    var userId = $('input[name="hidden-area"]').val();
                    if (userId) {
                        if (userId == 1) {
                        	showid = "host-" + alarmObject.substring(0, 8);
                        } else {
                        	showid = "i-" + alarmObject.substring(0, 8);
                        }
                    }
                    var showlogid = "alarm-"+alarmlogID.substring(0, 8);
                    var statusName ="已读"
                    if(alarmtatus==0)
                	{
                	   statusName ="<span style='color:red'>未读</span>"
                	}
                    
                  
                    var mytr = '<tr alarmlogID="' + alarmlogID + '"><td class="rcheck"><input type="checkbox" name="alarmlogrow"  statusid='+alarmtatus+'></td>'
                        + '<td>' + showlogid + '</td><td><a>' + showid
                        + '</a></td><td>' + alarmType + '</td><td>' + alarmDesc + '</td><td>' + statusName
                        + '</td><td class="time">' + createDate + '</td>';
                    mytr += '</tr>';
                    tableStr = tableStr + mytr;
                }
                $('#tablebody').html(tableStr);
            }
        }
    });
}

$('#tablebody').on('click', '.id', function (event) {
    event.preventDefault();
    var imageuuid = $(this).parent().parent().attr('imageUId');
    var imagetype = $(this).parent().parent().attr('imageType');
    var form = $("<form></form>");
    form.attr("action", "/image/detail");
    form.attr('method', 'post');
    var input = $('<input type="text" name="imageUuid" value="' + imageuuid + '" />');
    var input1 = $('<input type="text" name="imageType" value="' + imagetype + '" />');
    form.append(input);
    form.append(input1);
    form.css('display', 'none');
    form.appendTo($('body'));
    form.submit();
});

$('#delete').on('click', function (event) {
    event.preventDefault();
    var infoList = "";
    bootbox.dialog({
        message: '<div class="alert alert-info" style="margin:10px"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;删除所选告警日志&nbsp; ?</div>',
        title: "提示",
        buttons: {
            main: {
                label: "确定",
                className: "btn-primary",
                callback: function () {
                    $('input[name="alarmlogrow"]:checked').each(function () {
                        deleteAlarmLog($(this).parent().parent().attr("alarmlogID"));
                    });
                    removeAllCheck();
                }
            },
            cancel: {
                label: "取消",
                className: "btn-default",
                callback: function () {
                    removeAllCheck();
                }
            }
        }
    });
});

function deleteAlarmLog(alarmlogID) {
    $.ajax({
        type: 'post',
        url: '/AlarmLogAction/Delete',
        data: {alarmLogId: alarmlogID},
        dataType: 'json',
        success: function (obj) {
            if (obj.result) {
                var thistr = $("#tablebody").find('[alarmlogID="' + alarmlogID + '"]');
                $(thistr).remove();
            }
        }
    });
}


$('#read').on('click', function (event) {
    event.preventDefault();
    var infoList = "";
    bootbox.dialog({
        message: '<div class="alert alert-info" style="margin:10px"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;标为已读所选告警日志&nbsp; ?</div>',
        title: "提示",
        buttons: {
            main: {
                label: "确定",
                className: "btn-primary",
                callback: function () {
                    $('input[name="alarmlogrow"]:checked').each(function () {
                        readAlarmLog($(this).parent().parent().attr("alarmlogID"));
                    });
                    removeAllCheck();
                }
            },
            cancel: {
                label: "取消",
                className: "btn-default",
                callback: function () {
                    removeAllCheck();
                }
            }
        }
    });
});

function readAlarmLog(alarmlogid) {
	alert(alarmlogid)
    $.ajax({
        type: 'post',
        url: '/AlarmLogAction/read',
        data: {alarmLogId: alarmlogid},
        dataType: 'json',
        success: function (obj) {
            if (obj.result) {
            	reloadList(1);
            }
        }
    });
}

