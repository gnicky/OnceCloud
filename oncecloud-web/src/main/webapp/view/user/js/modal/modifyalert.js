$(document).ready(function () {

    $('#modifyinfomation').on('click', function (event) {
        event.preventDefault();
        var valid = $("#modify-form").valid();
        if (valid) {
            var alarmUuid = $('#modify-form').attr('uuid');
            var alarmIsalarm = $('input[type="radio"]:checked').val();
            var alarmTouch = -1;
            if (alarmIsalarm == "1") {
                $("input[name='trigger_status']:checked").each(function () {
                    alarmTouch += parseInt($(this).val());
                });
            } else {
                $("input[name='trigger_status']:checked").each(function () {
                    $(this).attr("checked", false);
                });
            }
            $.ajax({
                type: 'post',
                url: '/AlarmAction',
                data: "action=mtouch&alarmTouch=" + alarmTouch + "&alarmUuid=" + alarmUuid + "&alarmIsalarm=" + alarmIsalarm,
                dataType: 'text',
                success: function (response) {
                    if (alarmIsalarm == "0") {
                        $("#isalarm").text("否");
                        $("#iscondition").hide();
                        $("#iscontent").hide();
                    } else {
                        $("#isalarm").text("是");
                        $("#iscondition").show();
                        $("#iscontent").show();
                        if (alarmTouch == "0") {
                            $("#iscontent").text("资源出现异常时");
                            $("#iscontent").removeClass("none");
                        } else if (alarmTouch == "1") {
                            $("#iscontent").text("资源恢复正常时");
                            $("#iscontent").removeClass("none");
                        } else if (alarmTouch == "2") {
                            $("#iscontent").text("资源异常及恢复正常时");
                            $("#iscontent").removeClass("none");
                        } else {
                            $("#iscontent").text("尚未置设");
                            $("#iscontent").addClass("none");
                        }
                    }
                },
                error: function () {
                }
            });

            $('#RuleModalContainer').modal('hide');
        }
    });

    $(":radio").click(function () {
        if ($(this).val() == 1) {
            $("#advanced-options").show();
        } else {
            $("#advanced-options").hide();
        }
    });

});