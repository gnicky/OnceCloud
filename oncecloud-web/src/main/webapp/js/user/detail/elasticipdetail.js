getEipBasicList();

$('#EipModalContainer').on('hide', function (event) {
    getEipBasicList();
});

$('#modify').on('click', function (event) {
    event.preventDefault();
    var url = $("#platformcontent").attr('basePath') + 'common/modify';
    var eip = $("#platformcontent").attr("eip");
    var eipName = $("#eipname").text();
    var eipDesc = $("#eipdesc").text();
    $('#EipModalContainer').load(url, {"modifyType": "eip", "modifyUuid": eip, "modifyName": eipName, "modifyDesc": eipDesc}, function () {
        $('#EipModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
});

$('.btn-refresh').unbind();
$('.btn-refresh').on('click', function (event) {
    event.preventDefault();
    getEipBasicList();
});

$('#depend-list').on('click', '#depenid', function (event) {
    event.preventDefault();
    var depenUuid = $(this).attr('depenUuid');
    $.ajax({
        type: 'get',
        url: '/VMAction',
        data: 'action=detail&instanceuuid=' + depenUuid,
        dataType: 'text',
        success: function (response) {
            window.location.href = $("#platformcontent").attr('basePath') + "user/detail/instancedetail.jsp";
        }
    });
});

$('#depend-list').on('click', '#rsuuid', function (event) {
    event.preventDefault();
    var uuid = $(this).attr('uuid');
    var rsType = $(this).attr('type');
    var form = $("<form></form>");
    form.attr('method', 'post');
    if (rsType == 0) {
        form.attr("action", "/instance/detail");
        var input = $('<input type="text" name="instanceUuid" value="' + uuid + '" />');
        form.append(input);
    } else if (rsType == 1) {
        form.attr("action", "/loadbalance/detail");
        var input = $('<input type="text" name="lbUuid" value="' + uuid + '" />');
        form.append(input);
    } else if (rsType == 3) {
        form.attr("action", "/router/detail");
        var input = $('<input type="text" name="routerUuid" value="' + uuid + '" />');
        form.append(input);
    }
    form.css('display', 'none');
    form.appendTo($('body'));
    form.submit();
});

function getEipBasicList() {
    var eip = $("#platformcontent").attr("eip");
    $('#basic-list').html("");
    $('#depend-list').html("");
    $.ajax({
        type: 'get',
        url: '/EIPAction/BasicList',
        data: {eip: eip},
        dataType: 'json',
        success: function (obj) {
            var eipName = decodeURI(obj.eipName);
            var eipUuid = obj.eipUuid;
            var eipIp = obj.eipIp;
            var eipDepen = obj.eipDependency;
            var dependType = obj.dependType;
            var eipDescription = decodeURI(obj.eipDescription);
            var eipBandwidth = obj.eipBandwidth;
            var usedStr = '';
            var showuuid = "eip-" + eipUuid.substring(0, 8);
            if ('&nbsp;' != eipDepen) {
                var showname = '<span class="glyphicon glyphicon-cloud"></span>&nbsp;&nbsp;主机';
                var dependStr = '<a class="id" id="rsuuid" type="' + dependType + '" uuid="' + eipDepen + '">i-' + eipDepen.substring(0, 8) + '</a>';
                if (dependType == 1) {
                    showname = '<span class="glyphicon glyphicon-random"></span>&nbsp;&nbsp;负载均衡';
                    dependStr = '<a class="id" id="rsuuid" type="' + dependType + '" uuid="' + eipDepen + '">lb-' + eipDepen.substring(0, 8) + '</a>';
                } else if (dependType == 3) {
                    showname = '<span class="glyphicon glyphicon-fullscreen"></span>&nbsp;&nbsp;路由器';
                    dependStr = '<a class="id" id="rsuuid" type="' + dependType + '" uuid="' + eipDepen + '">rt-' + eipDepen.substring(0, 8) + '</a>';
                }
                usedStr = usedStr + '<td state="using"><span class="icon-status icon-using" name="stateicon"></span><span name="stateword">已分配</span></td>';
                $('#depend-list').html('<dt>应用资源</dt><dd>' + dependStr + '</dd><dt>资源类型</dt><dd>'
                    + showname + '</dd>');
            } else {
                usedStr = usedStr + '<td state="able"><span class="icon-status icon-running" name="stateicon"></span><span name="stateword">可用</span></td>';
            }
            showstr = "<a class='id'>" + showuuid + '</a>';
            var createDate = obj.createDate;
            $('#basic-list').html('<dt>ID</dt><dd>'
                + showstr + '</dd><dt>名称</dt><dd id="eipname">'
                + eipName + '</dd><dt>描述</dt><dd id="eipdesc">'
                + eipDescription + '</dd><dt>地址</dt><dd>'
                + eipIp + '</dd><dt>带宽</dt><dd>'
                + eipBandwidth + '&nbsp;Mbps</dd><dt>使用状态</dt><dd>'
                + usedStr + '</dd><dt>IP分组</dt><dd>'
                + '电信' + '</dd><dt>创建时间</dt><dd class="time">'
                + createDate + '</dd>');
        }
    });
}