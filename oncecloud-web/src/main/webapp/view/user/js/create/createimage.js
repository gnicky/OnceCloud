$(document).ready(function () {
    $('#cloneImageAction').on('click', function (event) {
        event.preventDefault();
        var valid = $("#create-form").valid();
        if (valid) {
            var imgaeName = document.getElementById("image_name").value;
            var imageDesc = document.getElementById("image_desc").value;
            var boxes = document.getElementsByName("vmrow");
            if (boxes.length > 0) {
                for (var i = 0; i < boxes.length; i++) {
                    if (boxes[i].checked == true) {
                        var vmuuid = $(boxes[i]).parent().parent().attr("rowid");
                        $.ajax({
                            type: 'post',
                            url: '/ImageAction',
                            data: 'action=clone&imagename=' + imgaeName + '&imagedesc=' + imageDesc
                                + '&vmuuid=' + vmuuid,
                            dataType: 'json',
                            success: function (obj) {
                            },
                            error: function () {
                            }
                        });
                    }
                }
            } else {
                var rsid = $("#modal-dialog").attr("rsid");
                $.ajax({
                    type: 'post',
                    url: '/ImageAction',
                    data: 'action=clone&imagename=' + imgaeName + '&imagedesc=' + imageDesc
                        + '&vmuuid=' + rsid,
                    dataType: 'json',
                    success: function (obj) {
                    },
                    error: function () {
                    }
                });
            }
            $('#InstanceModalContainer').modal('hide');
        }
        removeAllCheck();
    });

    $('#cancelAction').on('click', function (event) {
        event.preventDefault();
        $('#InstanceModalContainer').modal('hide');
        removeAllCheck();
    });

    $("#create-form").validate({
        rules: {
            image_name: {
                required: true,
                minlength: 3,
                maxlength: 80
            },
            image_desc: {
                required: true,
                minlength: 3,
                maxlength: 80
            }
        },
        messages: {
            image_name: {
                required: "<span class='help'>映像名称不能为空</span>",
                minlength: "<span class='help'>映像名称不能少于3个字符</span>",
                maxlength: "<span class='help'>映像名称不能多于80个字符</span>"
            },
            image_desc: {
                required: "<span class='help'>映像备注不能为空</span>",
                minlength: "<span class='help'>映像备注不能少于3个字符</span>",
                maxlength: "<span class='help'>映像备注不能多于80个字符</span>"
            }
        }
    });

    function removeAllCheck() {
        var boxes = document.getElementsByName("vmrow");
        for (var i = 0; i < boxes.length; i++) {
            boxes[i].checked = false;
            $(boxes[i]).change();
        }
    }
});