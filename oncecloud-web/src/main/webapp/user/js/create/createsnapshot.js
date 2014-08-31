$(document).ready(function () {
    initResourceList();

    $('#createsnapshotAction').on('click', function (event) {
        event.preventDefault();
        var valid = $("#backupinfo-form").valid();
        if (valid) {
            var resourceUuid = $("#platformcontent").attr("resourceUuid");
            if (resourceUuid != null) {
                var resourceType = $("#platformcontent").attr("resourceType");
                var snapshotid = uuid.v4();
                syncsnapshot(snapshotid, $("#snapshot_name").val(), resourceUuid, resourceType);
            } else {
                var chain = getNewChain();
                $.ajax({
                    type: 'get',
                    url: '/SnapshotAction',
                    data: 'action=quota&count=' + chain,
                    dataType: 'text',
                    success: function (msg) {
                        if (msg != "ok") {
                            bootbox.dialog({
                                message: '<div class="alert alert-danger" style="margin:10px"><span class="glyphicon glyphicon-warning-sign"></span>&nbsp;申请创建[' + chain + ']个新备份链，超过配额，目前剩余[' + msg + ']个备份链配额，您可以通过联系我们来申请扩大配额</div>',
                                title: "提示",
                                buttons: {
                                    main: {
                                        label: "确定",
                                        className: "btn-primary",
                                        callback: function () {
                                        }
                                    },
                                    cancel: {
                                        label: "取消",
                                        className: "btn-default",
                                        callback: function () {
                                            $('#VolumeModalContainer').modal('hide');
                                            $('#InstanceModalContainer').modal('hide');
                                        }
                                    }
                                }
                            });
                        } else {
                            var vmitems = document.getElementsByName("vm-select-item");
                            for (var i = 0; i < vmitems.length; i++) {
                                var snapshotid = uuid.v4();
                                snapshot(snapshotid, $("#snapshot_name").val(), $(vmitems[i]).attr('uuid'), "instance");
                            }

                            var volumeitems = document.getElementsByName("volume-select-item");
                            for (var i = 0; i < volumeitems.length; i++) {
                                var snapshotid = uuid.v4();
                                snapshot(snapshotid, $("#snapshot_name").val(), $(volumeitems[i]).attr('uuid'), "volume");
                            }
                            $('#VolumeModalContainer').modal('hide');
                            $('#InstanceModalContainer').modal('hide');
                        }
                    },
                    error: function () {
                    }
                });
            }
        }
    });

    $("#backupinfo-form").validate({
        rules: {
            snapshot_name: {
                required: true,
                maxlength: 16
            }
        },
        messages: {
            snapshot_name: {
                required: "<span class='unit'>快照名不能为空</span>",
                maxlength: "<span class='unit'>不能超过16个字符</span>"
            }
        }
    });

    function getNewChain() {
        var newchain = 0;
        var vmboxes = document.getElementsByName("vmrow");
        for (var i = 0; i < vmboxes.length; i++) {
            if (vmboxes[i].checked == true) {
                if ($(vmboxes[i]).parent().parent().find(".glyphicon-camera").size() > 0) {
                    newchain++;
                }
            }
        }
        var volumeboxes = document.getElementsByName("volumerow");
        for (var i = 0; i < volumeboxes.length; i++) {
            if (volumeboxes[i].checked == true) {
                if ($(volumeboxes[i]).parent().parent().find(".glyphicon-camera").size() > 0) {
                    newchain++;
                }
            }
        }
        return newchain;
    }

    function initResourceList() {
        var resourceUuid = $("#platformcontent").attr("resourceUuid");
        var rsid = $("#modal-dialog").attr("rsid");
        if (resourceUuid != null) {
            var resourceType = $("#platformcontent").attr("resourceType");
            var resourceName = $("#platformcontent").attr("resourceName");
            if (resourceType == "instance") {
                var showuuid = "i-" + resourceUuid.substring(0, 8);
                $('#resource-list').append('<div name="vm-select-item" uuid="' + resourceUuid + '"><label class="inline" style="margin:0; padding:0 !important"><span class="glyphicon glyphicon-cloud"></span>&nbsp;' + resourceName + '&nbsp;<a>(' + showuuid + ')</a></label></div>');
            } else {
                var showuuid = "vol-" + resourceUuid.substring(0, 8);
                $('#resource-list').append('<div name="volume-select-item" uuid="' + resourceUuid + '"><label class="inline" style="margin:0; padding:0 !important"><span class="glyphicon glyphicon-inbox"></span>&nbsp;' + resourceName + '&nbsp;<a>(' + showuuid + ')</a></label></div>');
            }
        } else if (rsid != "null") {
            var rstype = $("#modal-dialog").attr("rstype");
            var rsname = $("#modal-dialog").attr("rsname");
            if (rstype == "instance") {
                var showuuid = "i-" + rsid.substring(0, 8);
                $('#resource-list').append('<div name="vm-select-item" uuid="'
                    + rsid + '"><label class="inline" style="margin:0; padding:0 !important"><span class="glyphicon glyphicon-cloud"></span>&nbsp;' + rsname + '&nbsp;<a>(' + showuuid + ')</a></label></div>');
            } else {
                var showuuid = "vol-" + rsid.substring(0, 8);
                $('#resource-list').append('<div name="volume-select-item" uuid="'
                    + rsid + '"><label class="inline" style="margin:0; padding:0 !important"><span class="glyphicon glyphicon-inbox"></span>&nbsp;' + rsname + '&nbsp;<a>(' + showuuid + ')</a></label></div>');
            }
        } else {
            var vmboxes = document.getElementsByName("vmrow");
            for (var i = 0; i < vmboxes.length; i++) {
                if (vmboxes[i].checked == true) {
                    var vmuuid = $(vmboxes[i]).parent().parent().attr("rowid");
                    var showuuid = "i-" + vmuuid.substring(0, 8);
                    var vmName = $(vmboxes[i]).parent().parent().find('[name="vmname"]').text();
                    $('#resource-list').append('<div name="vm-select-item" uuid="' + vmuuid + '"><label class="inline" style="margin:0; padding:0 !important"><span class="glyphicon glyphicon-cloud"></span>&nbsp;' + vmName + '&nbsp;<a>(' + showuuid + ')</a></label></div>');
                }
            }
            removeAllCheck("vmrow");
            var volumeboxes = document.getElementsByName("volumerow");
            for (var i = 0; i < volumeboxes.length; i++) {
                if (volumeboxes[i].checked == true) {
                    var volumeuuid = $(volumeboxes[i]).parent().parent().attr("rowid");
                    var volumeName = $(volumeboxes[i]).parent().parent().find('[name="volumename"]').text();
                    var showuuid = "vol-" + volumeuuid.substring(0, 8);
                    $('#resource-list').append('<div name="volume-select-item" uuid="' + volumeuuid + '"><label class="inline" style="margin:0; padding:0 !important"><span class="glyphicon glyphicon-inbox"></span>&nbsp;' + volumeName + '&nbsp;<a>(' + showuuid + ')</a></label></div>');
                }
            }
            removeAllCheck("volumerow");
        }
    }

    function snapshot(snapshotId, snapshotName, resourceUuid, resourceType) {
        $.ajax({
            type: 'post',
            url: '/SnapshotAction',
            data: 'action=create&snapshotId=' + snapshotId + '&snapshotName='
                + snapshotName + '&resourceUuid=' + resourceUuid + '&resourceType=' + resourceType,
            dataType: 'json',
            success: function (obj) {
                if (obj.isSuccess) {
                    var thistd = $("#tablebody").find('[rowid="' + resourceUuid + '"]').find('[name="backuptime"]');
                    thistd.text(decodeURI(obj.backupDate));
                }
            },
            error: function () {
            }
        });
    }

    function syncsnapshot(snapshotId, snapshotName, resourceUuid, resourceType) {
        $.ajax({
            type: 'post',
            url: '/SnapshotAction',
            async: false,
            data: 'action=create&snapshotId=' + snapshotId + '&snapshotName=' + snapshotName + '&resourceUuid=' + resourceUuid + '&resourceType=' + resourceType,
            dataType: 'json',
            success: function (msg) {
                getSnapshotDetailList();
                getSnapshotBasicList();
                $('#SnapshotModalContainer').modal('hide');
            },
            error: function () {
            }
        });
    }

    function getSnapshotDetailList() {
        var uid = $("#platformcontent").attr("platformUserId");
        var resourceUuid = $("#platformcontent").attr("resourceUuid");
        var resourceType = $("#platformcontent").attr("resourceType");
        var btable = document.getElementById("snapshot-graph");
        btable.innerHTML = "";
        $.ajax({
            type: 'get',
            url: '/SnapshotAction',
            data: 'action=getdetaillist&resourceUuid=' + resourceUuid + '&resourceType=' + resourceType,
            dataType: 'json',
            success: function (msg) {
                var array = msg;
                var height = 16 + 32 * array.length;
                var svgStr = '<svg width="40" height="' + height + '"><g transform="translate(20,16)">';
                var listStr = '<div class="snapshot-list" id="snapshot-list">';
                listStr = listStr + '<a class="new-snapshot" id="backup" url="../create/createsnapshot.jsp"><span class="tip">新建备份</span></a>';
                for (var i = 0; i < array.length; i++) {
                    var obj = array[i];
                    var snapshotId = obj.snapshotId;
                    var snapshotName = decodeURI(obj.snapshotName);
                    var snapshotSize = obj.snapshotSize;
                    var backupDate = obj.backupDate;
                    var showid = "ss-" + snapshotId.substring(0, 8);
                    var title = '<ul snapshotId="' + snapshotId + '" snapshotName="' + snapshotName + '">';
                    if (i == 0) {
                        svgStr = svgStr + '<path d="M0,16L0,-10" class="is-head"></path><circle id="' + showid + '" r="5" cx="0" cy="16"></circle>';
                    } else {
                        var current = 32 * i - 10;
                        var step = 16 + 32 * i;
                        svgStr = svgStr + '<path d="M0,' + current + 'L0,' + step + '"></path><circle id="' + showid + '" r="5" cx="0" cy="' + step + '"></circle>';
                    }
                    if (i % 2 == 1) {
                        var title = '<ul snapshotId="' + snapshotId + '" class="odd">';
                    }
                    listStr = listStr + title + '<li class="background"></li><li class="id"><span>'
                        + showid + '</span></li><li class="time">' + backupDate + '</li><li>'
                        + snapshotSize.toFixed(2) + '&nbsp;GB</li><li><span>'
                        + snapshotName + '</span>&nbsp;</li></ul>';
                }
                btable.innerHTML = svgStr + '</g></svg>' + listStr + '</div>';
            },
            error: function () {
            }
        });
    }

    function getSnapshotBasicList() {
        var resourceUuid = $("#platformcontent").attr("resourceUuid");
        var resourceType = $("#platformcontent").attr("resourceType");
        var basiclist = document.getElementById("basic-list");
        basiclist.innerHTML = "";
        $.ajax({
            type: 'get',
            url: '/SnapshotAction',
            data: "action=getoneresource&resourceUuid=" + resourceUuid + "&resourceType=" + resourceType,
            dataType: 'text',
            success: function (response) {
                var obj = jQuery.parseJSON(response);
                var resourceName = obj.resourceName;
                var snapshotCount = obj.snapshotCount;
                var snapshotSize = obj.snapshotSize;
                var backupDate = obj.backupDate;
                var showid = "ss-" + resourceUuid.substring(0, 8);
                var showname;
                if (resourceType == "instance") {
                    showname = '<span class="glyphicon glyphicon-cloud"></span>&nbsp;&nbsp;主机';
                } else {
                    showname = '<span class="glyphicon glyphicon-inbox"></span>&nbsp;&nbsp;硬盘';
                }
                basiclist.innerHTML = '<dt>备份链&nbsp;ID</dt><dd><a href="javascript:void(0)">'
                    + showid + '</a></dd><dt>状态</dt><dd><span class="icon-status icon-running" name="stateicon"></span><span name="stateword">可用</span></dd>'
                    + '<dt>资源类型</dt><dd>' + showname + '</dd><dt>总量</dt><dd>' + snapshotSize.toFixed(2) + '&nbsp;GB</dd><dt>备份点</dt><dd>'
                    + snapshotCount + '&nbsp;个</dd><dt>距上次备份时间</dt><dd>' + decodeURI(backupDate) + '</dd>';
            },
            error: function () {
            }
        });
    }

    function removeAllCheck(namerow) {
        var boxes = document.getElementsByName(namerow);
        for (var i = 0; i < boxes.length; i++) {
            boxes[i].checked = false;
            $(boxes[i]).change();
        }
    }
});