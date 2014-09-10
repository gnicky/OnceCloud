$("#InstanceModalContainer").on("hidden", function () {
    $(this).removeData("modal");
    $(this).children().remove();
});

$("#wizard").bwizard({
    backBtnText: "",
    nextBtnText: ""
});

var options = {
    bootstrapMajorVersion: 3,
    currentPage: 1,
    totalPages: 1,
    numberOfPages: 0,
    onPageClicked: function (e, originalEvent, type, page) {
        var type = $('.provider').find('.selected').attr("type");
        getImageList(page, 8, "", type);
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
};
$('#tplpage').bootstrapPaginator(options);
getImageList(1, 8, "", "system");

$('.li-disable').unbind();

function getImageList(page, limit, search, type) {
    $('#imagelist').html("");
    $.ajax({
        type: 'post',
        url: '/ImageAction/ImageList',
        data: {page: page, limit: limit, search: search, type: type},
        dataType: 'json',
        success: function (array) {
            var tableStr = "";
            if (array.length >= 1) {
                var totalnum = array[0];
                var totalp = 1;
                if (totalnum != 0) {
                    totalp = Math.ceil(totalnum / limit);
                }
                options = {
                    totalPages: totalp
                };
                $('#tplpage').bootstrapPaginator(options);
                modalPageUpdate(page, totalp);
                for (var i = 1; i < array.length; i++) {
                    var obj = array[i];
                    var imageid = obj.imageid;
                    var imagename = decodeURI(obj.imagename);
                    var imagestatus = obj.imagestatus;
                    var imageplatform = decodeURI(obj.imageplatform);
                    if (imagestatus == 1) {
                        if (i == 1) {
                            tableStr = tableStr + '<div class="image-item selected" imageid="' + imageid + '" platform="' + imageplatform + '">' + imagename + '</div>';
                            var s = document.getElementById("selectedImage");
                            s.innerHTML = imagename;
                        } else {
                            tableStr = tableStr + '<div class="image-item" imageid="' + imageid + '" platform="' + imageplatform + '">' + imagename + '</div>';
                        }
                    }
                }
                $('#imagelist').html(tableStr);
            }
        }
    });
}

$('.provider').on('click', '.provider-filter', function (event) {
    event.preventDefault();
    $('a', $('.provider')).removeClass('selected');
    $(this).addClass('selected');
    var type = $('.provider').find('.selected').attr("type");
    getImageList(1, 8, "", type);
});

$('.imagelist').on('click', '.image-item', function (event) {
    event.preventDefault();
    $('div', $('#imagelist')).removeClass('selected');
    $(this).addClass('selected');
    $('#selectedImage').html($('#imagelist').find('.selected').text());
});

$('.btn-back').on('click', function (event) {
    event.preventDefault();
    $("#wizard").bwizard("back");
});

$('.btn-next').on('click', function (event) {
    event.preventDefault();
    var platform = $('#imagelist').find('.selected').attr("platform");
    if (platform == "Windows") {
        $('#vmDefaultUser').val("Administrator");
    } else {
        $('#vmDefaultUser').val("root");
    }
    $("#wizard").bwizard("next");
});

$('#createvmAction').on('click', function (event) {
    event.preventDefault();
    var valid = $('#basicinfo-form').valid();
    var pwval = pwvalid();
    if (valid && pwval) {
        var imageuuid = $('.imagelist').find('.selected').attr("imageid");
        var cpuCore = $('.cpu').find('.selected').attr("core");
        var memoryCapacity = $('.memory').find('.selected').attr("capacity");
        var vmName = $('#instance_name').val();
        var vmCount = parseInt($('#count').val(), 10);
        var loginPwd = $('#login_passwd').val();
        $.ajax({
            type: 'post',
            url: '/VMAction/Quota',
            data: {count: vmCount},
            dataType: 'text',
            success: function (msg) {
                if (msg != "ok") {
                    bootbox.dialog({
                        message: '<div class="alert alert-danger" style="margin:10px"><span class="glyphicon glyphicon-warning-sign"></span>&nbsp;超过配额，目前剩余[' + msg + ']个主机配额，您可以通过联系我们来申请扩大配额</div>',
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
                                    $('#InstanceModalContainer').modal('hide');
                                }
                            }
                        }
                    });
                } else {
                    for (var i = 0; i < vmCount; i++) {
                        var vmuuid = uuid.v4();
                        preCreateVM(vmuuid, imageuuid, cpuCore, memoryCapacity, vmName, loginPwd);
                    }
                    $('#InstanceModalContainer').modal('hide');
                }
            }
        });
    }
});

$('#instance_name').on('focusout', function () {
	var name = $('#instance_name').val();
	if (name.length > 20) {
		name = name.substring(0, 20) + "...";
	}
    $('#selectedName').html(name);
});

$('#count').on('focusout', function () {
    $('#selectedCount').html($('#count').val());
    priceDisplayUpdate();
});

$('#basicinfo-form').validate({
    rules: {
        instance_name: {
            required: true,
            maxlength: 20
        },
        count: {
            required: true,
            digits: true
        }
    },
    messages: {
        instance_name: {
            required: "<span class='unit'>主机名称不能为空</span>",
            maxlength: "<span class='unit'>主机名称不能超过20个字符</span>"
        },
        count: {
            required: "<span class='unit'>主机个数不能为空</span>",
            digits: "<span class='unit'>请输入合法整数</span>"
        }
    }
});

function modalPageUpdate(current, total) {
    $('#currentPtpl').html(current);
    $('#totalPtpl').html(total);
}

function preCreateVM(vmuuid, imageuuid, cpuCore1, memoryCapacity, vmName, loginPwd) {
    cpuCore = cpuCore1 + "&nbsp;核";
    var memoryInt = parseInt(memoryCapacity);
    var memoryStr;
    if (memoryInt < 1) {
        memoryStr = "512&nbsp;MB";
    } else {
        memoryStr = memoryInt + "&nbsp;GB";
    }
    var showuuid = "i-" + vmuuid.substring(0, 8);
    var showstr = "<a class='id'>" + showuuid + '</a>';
    var basePath = $('#platformcontent').attr('platformBasePath');
    var backupStr = '<a class="glyphicon glyphicon-camera backup" url="' + basePath + 'user/create/createsnapshot.jsp?rsid=' + vmuuid + '&rstype=instance&rsname=' + vmName + '"></a>';
    $("#tablebody").prepend('<tr rowid="' + vmuuid + '"><td class="rcheck"><input type="checkbox" name="vmrow"></td><td name="console">' + showstr + '</td><td name="vmname">'
        + vmName + '</td><td><span class="icon-status icon-process" name="stateicon"></span><span name="stateword">创建中</span></td><td name="cpuCore">'
        + cpuCore + '</td><td name="memoryCapacity">'
        + memoryStr + '</td><td name="sip"><a>(基础网络)</a></td><td name="pip"></td><td name="backuptime">' + backupStr + '</td><td name="createtime" class="time"><1分钟</td></tr>');
    createVM(vmuuid, imageuuid, cpuCore1, memoryCapacity, vmName, loginPwd);
}

function createVM(vmuuid, imageuuid, cpuCore, memoryCapacity, vmName, loginPwd) {
    $.ajax({
        type: 'post',
        url: '/VMAction/CreateVM',
        data: {imageUuid: imageuuid, cpu: cpuCore, memory: memoryCapacity, vmName: vmName, password: loginPwd, vmUuid: vmuuid},
        dataType: 'json'
    });
}

function priceDisplayUpdate() {
    var cpuCore = $('.cpu').find('.selected').attr("core");
    var memoryCapacity = $('.memory').find('.selected').attr("capacity");
    var vmCount = $('#count').val();
    updatePrice(cpuCore, memoryCapacity, vmCount);
}

function updatePrice(cpuCore, memoryCapacity, vmCount) {
}

$('.types').on('click', '.types-item', function (event) {
    event.preventDefault();
	var selindex = $(this).index();
	var core = new Array(0,0,1,1,2,2);
    var mem = new Array(1,2,2,3,5,6);
    $('.types-item',$('.types')).removeClass('selected');
    $(this).addClass('selected');
    $('div', $('.cpu')).removeClass('selected');
    $('.cpu-options').eq(core[selindex]).addClass('selected');
    $('#selectedCore').html($('.cpu').find('.selected').attr("core") + "&nbsp;核");
    $('div', $('.memory')).removeClass('selected');
    $('.memory-options').eq(mem[selindex]).addClass('selected');
    $('#selectedCap').html($('.memory').find('.selected').attr("capacity") + "&nbsp;G");
    $('#selectedType').html($(this).find(".type-name").text());
});

$('.cpu').on('click', '.cpu-options', function (event) {
    event.preventDefault();
    $('div', $('.cpu')).removeClass('selected');
    $(this).addClass('selected');
    $('#selectedCore').html($('.cpu').find('.selected').attr("core") + "&nbsp;核");
    $('.types-item',$('.types')).removeClass('selected');
    $('#selectedType').html("定制");
    priceDisplayUpdate();
});

$('.memory').on('click', '.memory-options', function (event) {
    event.preventDefault();
    if (!$(this).hasClass('disabled')) {
        $('div', $('.memory')).removeClass('selected');
        $(this).addClass('selected');
        $('#selectedCap').html($('.memory').find('.selected').attr("capacity") + "&nbsp;G");
        $('.types-item',$('.types')).removeClass('selected');
   		$('#selectedType').html("定制");
        priceDisplayUpdate();
    }
});

$('#display-pwd').on('change', function (event) {
    event.preventDefault();
    if (this.checked == true) {
        $('#login_passwd').attr('type', 'text');
    } else {
        $('#login_passwd').attr('type', 'password');
    }
});

function pwvalid() {
    var text = $('input[name=login_passwd]').val();
    if (/^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*).{8,}$/.test(text)) {
        return true;
    } else {
        return false;
    }
}

$('input[name=login_passwd]').on('input', function (e) {
    var text = $('input[name=login_passwd]').val();
    if (/^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*).{8,}$/.test(text)) {
        $('#pw-alert').removeClass("alert-info");
        $('#pw-alert').removeClass("alert-danger");
        $('#pw-alert').addClass("alert-success");
    } else {
        $('#pw-alert').removeClass("alert-info");
        $('#pw-alert').removeClass("alert-success");
        $('#pw-alert').addClass("alert-danger");
    }
});
