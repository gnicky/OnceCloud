$(document).ready(function () {

    $('#modifyinfomation').on('click', function (event) {
        event.preventDefault();
        var valid = $("#modify-form").valid();
        if (valid) {
            var alarmUuid = $('#modify-form').attr('uuid');
            var period = $('#period').find("option:selected").val();
            $.ajax({
                type: 'post',
                url: '/AlarmAction/ModifyPeriod',
                data: {period: period, alarmUuid: alarmUuid},
                dataType: 'json',
                success: function (obj) {
                    if (obj.isSuccess) {
                        $("#alPeriod").text(period + "分钟");
                    }
                },
                error: function () {
                }
            });

            $('#RuleModalContainer').modal('hide');
        }
    });

});