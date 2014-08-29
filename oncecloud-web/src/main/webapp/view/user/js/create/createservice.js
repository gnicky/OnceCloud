$(document).ready(function () {
    $('#applyserviceAction').on('click', function (event) {
        event.preventDefault();
        var valid = $("#apply-form").valid();
        if (valid) {
            var title = document.getElementById("qa_title").value;
            var content = document.getElementById("qa_content").value;
            $.ajax({
                type: 'post',
                url: '/QAAction',
                data: 'action=create&title=' + title + '&content=' + content,
                dataType: 'json',
                success: function (obj) {
                    var qaId = obj.qaId;
                    if (qaId > 0) {
                        var qaSummary = obj.qaSummary;
                        var qaTime = obj.qaTime;
                        var qaStatusStr = '<td state="create"><span class="icon-status icon-using" name="stateicon"></span><span name="stateword">新建</span></td>';
                        var qaCloseStr = '<td class="close-td"><a href="javascript:void(0)" class="qa-close">关闭</a></td>';
                        $("#tablebody").prepend('<tr qaId="' + qaId + '"><td><a class="view-detail" href="javascript:void(0)">' + title + '</a></td>'
                            + '<td>' + decodeURI(qaSummary) + '</td><td>0</td>' + qaStatusStr
                            + '<td class="time">' + qaTime + '</td>' + qaCloseStr + '</tr>');
                    }
                    $('#ServiceModalContainer').modal('hide');
                },
                error: function () {
                }
            });
        }
    });

    $("#apply-form").validate({
        rules: {
            qa_title: {
                required: true,
                maxlength: 16
            },
            qa_content: {
                required: true,
                minlength: 5
            }
        },
        messages: {
            qa_title: {
                required: "<span class='help'>表单标题不能为空</span>",
                maxlength: "<span class='help'>不能超过16个字符</span>"
            },
            qa_content: {
                required: "<span class='help'>表单描述不能为空</span>",
                minlength: "<span class='help'>请输入至少5个字符</span>"
            }
        }
    });
});