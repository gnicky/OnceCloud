$(function(){
	getInstanceBasicList();
	var cpuChart;
	var memoryChart;
	var vbdChart;
	var vifChart;
	drawCpuLine('sixhours');
	drawMemoryLine('sixhours');
	drawVbdLine('sixhours');
	drawVifLine('sixhours');
	cpuTimer = setInterval(function () {
	    drawCpuLine('sixhours');
	}, 5 * 60 * 1000);
	memoryTimer = setInterval(function () {
	    drawMemoryLine('sixhours');
	}, 5 * 60 * 1000);
	vbdTimer = setInterval(function () {
	    drawVbdLine('sixhours');
	}, 5 * 60 * 1000);
	vifTimer = setInterval(function () {
	    drawVifLine('sixhours');
	}, 5 * 60 * 1000);
	
	///add by cyh
	init();
})


$('#modify').on('click', function (event) {
    event.preventDefault();
    var url = $("#platformcontent").attr('platformBasePath') + 'common/modify.jsp';
    var instanceUuid = $("#platformcontent").attr("instanceUuid");
    var instanceName = $("#instancename").text();
    var instanceDesc = $("#instancedesc").text();
    $('#InstanceModalContainer').load(url, {"modifytype": "instance", "modifyuuid": instanceUuid, "modifyname": instanceName, "modifydesc": instanceDesc}, function () {
        $('#InstanceModalContainer').modal({
            backdrop: false,
            show: true
        });
    });
});
$('#InstanceModalContainer').on('hide', function (event) {
    getInstanceBasicList();
});

$('#basic-list, #basic-list2').on('click', '.console', function (event) {
    event.preventDefault();
    var uuid = $(this).data("uuid");
    var vnc = document.getElementById("platformcontent").getAttribute("novnc");
    var token = uuid.substring(0, 8);
    var url = vnc + "console.html?id=" + token;
    window.open(url, "newwindow", 'height=600,width=810,top=0,left=0');
});

$('#depend-list').on('click', '#firewallid', function (event) {
    var firewallId = $(this).attr("firewallid");
    event.preventDefault();
    var form = $("<form></form>");
    form.attr("action","/firewall/detail");
    form.attr('method','post');
    var input = $('<input type="text" name="firewallId" value="' + firewallId + '" />');
    form.append(input);
    form.css('display','none');
    form.appendTo($('body'));
    form.submit();
});

$('#depend-list').on('click', '#eip', function (event) {
    event.preventDefault();
    var eip = $(this).attr("eipip");
    var form = $("<form></form>");
    form.attr("action","/elasticip/detail");
    form.attr('method','post');
    var input = $('<input type="text" name="eipUuid" value="' + eip + '" />');
    form.append(input);
    form.css('display','none');
    form.appendTo($('body'));
    form.submit();
});

/**
 * 2014/08/15 hty
 */
$('#depend-list').on('click', '#vnetip', function (event) {
    event.preventDefault();
    var uuid = $(this).attr("vnetip");
    var routerid = $(this).attr("routerUuid");
 
    var form = $("<form></form>");
    form.attr("action","/vnet/detail");
    form.attr('method','post');
    var input = $('<input type="text" name="vnetUuid" value="' + uuid + '" />');
    form.append(input);
    var input2 = $('<input type="text" name="routerId" value="' + routerid + '" />');
    form.append(input2);
    form.css('display','none');
    form.appendTo($('body'));
    form.submit();
});


$('.detail-right').on('mouseenter', '.bootstrap-switch', function (event) {
    event.preventDefault();
    $('.oc-tip').css('display', 'inline-block');
});

$('.detail-right').on('mouseleave', '.bootstrap-switch', function (event) {
    event.preventDefault();
    $('.oc-tip').css('display', 'none');
});


$('#depend-list').on('click', '#snapshotid', function (event) {
    event.preventDefault();
    var resourceUuid = $(this).attr('rsuuid');
    var resourceName = $(this).attr('rsname');
    var form = $("<form></form>");
    form.attr("action","/snapshot/detail");
    form.attr('method','post');
    var input = $('<input type="text" name="resourceUuid" value="' + resourceUuid + '" />');
    form.append(input);
    var input2 = $('<input type="text" name="resourceName" value="' + resourceName + '" />');
    form.append(input2);
    var input3 = $('<input type="text" name="resourceType" value="instance" />');
    form.append(input3);
    form.css('display','none');
    form.appendTo($('body'));
    form.submit();
});

$('#depend-list').on('click', '.volid', function (event) {
    event.preventDefault();
    var volid = $(this).attr('voluuid');
    var form = $("<form></form>");
    form.attr("action","/volume/detail");
    form.attr('method','post');
    var input = $('<input type="text" name="volumeUuid" value="' + volid + '" />');
    form.append(input);
    form.css('display','none');
    form.appendTo($('body'));
    form.submit();
});

function thirtymin() {
    resetChart();
    $('.btn-monitor').removeClass('selected');
    $('.chart-span').text("30 秒");
    $('.chart-span').addClass("chart-right");
    $('#chart-group').addClass("chart-right");
    window.clearInterval(cpuTimer);
    window.clearInterval(memoryTimer);
    window.clearInterval(vbdTimer);
    window.clearInterval(vifTimer);
    drawCpuLine('thirtymin');
    drawMemoryLine('thirtymin');
    drawVbdLine('thirtymin');
    drawVifLine('thirtymin');
    cpuTimer = setInterval(function () {
        drawCpuLine('thirtymin');
    }, 30 * 1000);
    memoryTimer = setInterval(function () {
        drawMemoryLine('thirtymin');
    }, 30 * 1000);
    vbdTimer = setInterval(function () {
        drawVbdLine('thirtymin');
    }, 30 * 1000);
    vifTimer = setInterval(function () {
        drawVifLine('thirtymin');
    }, 30 * 1000);
}

$("#sixhours").on('click', function (event) {
    event.preventDefault();
    resetSwitch();
    $('.btn-monitor').removeClass('selected');
    $(this).addClass('selected');
    sixhours();
});

function resetChart() {
    $('#chart-area-1').html('<div id="cpupic"></div>');
    $('#chart-area-2').html('<div id="memorypic"></div>');
    $('#chart-area-3').html('<div id="vbdpic"></div>');
    $('#chart-area-4').html('<div id="vifpic"></div>');
}

function resetSwitch() {
    $('.oc-switch').bootstrapSwitch('state', false, false);
}

function sixhours() {
    resetChart();
    $('.chart-span').text("5 分钟");
    $('.chart-span').addClass("chart-right");
    $('#chart-group').addClass("chart-right");
    window.clearInterval(cpuTimer);
    window.clearInterval(memoryTimer);
    window.clearInterval(vbdTimer);
    window.clearInterval(vifTimer);
    drawCpuLine('sixhours');
    drawMemoryLine('sixhours');
    drawVbdLine('sixhours');
    drawVifLine('sixhours');
    cpuTimer = setInterval(function () {
        drawCpuLine('sixhours');
    }, 5 * 60 * 1000);
    memoryTimer = setInterval(function () {
        drawMemoryLine('sixhours');
    }, 5 * 60 * 1000);
    vbdTimer = setInterval(function () {
        drawVbdLine('sixhours');
    }, 5 * 60 * 1000);
    vifTimer = setInterval(function () {
        drawVifLine('sixhours');
    }, 5 * 60 * 1000);
}

$("#oneday").on('click', function (event) {
    event.preventDefault();
    resetSwitch();
    resetChart();
    $('.btn-monitor').removeClass('selected');
    $(this).addClass('selected');
    $('.chart-span').text("15 分钟");
    $('.chart-span').addClass("chart-right");
    $('#chart-group').addClass("chart-right");
    window.clearInterval(cpuTimer);
    window.clearInterval(memoryTimer);
    window.clearInterval(vbdTimer);
    window.clearInterval(vifTimer);
    drawCpuLine('oneday');
    drawMemoryLine('oneday');
    drawVbdLine('oneday');
    drawVifLine('oneday');
    cpuTimer = setInterval(function () {
        drawCpuLine('oneday');
    }, 15 * 60 * 1000);
    memoryTimer = setInterval(function () {
        drawMemoryLine('oneday');
    }, 15 * 60 * 1000);
    vbdTimer = setInterval(function () {
        drawVbdLine('oneday');
    }, 15 * 60 * 1000);
    vifTimer = setInterval(function () {
        drawVifLine('oneday');
    }, 15 * 60 * 1000);
});

$("#twoweeks").on('click', function (event) {
    event.preventDefault();
    resetSwitch();
    resetChart();
    $('.btn-monitor').removeClass('selected');
    $(this).addClass('selected');
    $('.chart-span').text("4 小时");
    $('.chart-span').addClass("chart-right");
    $('#chart-group').addClass("chart-right");
    window.clearInterval(cpuTimer);
    window.clearInterval(memoryTimer);
    window.clearInterval(vbdTimer);
    window.clearInterval(vifTimer);
    drawCpuLine('twoweeks');
    drawMemoryLine('twoweeks');
    drawVbdLine('twoweeks');
    drawVifLine('twoweeks');
    cpuTimer = setInterval(function () {
        drawCpuLine('twoweeks');
    }, 4 * 3600 * 1000);
    memoryTimer = setInterval(function () {
        drawMemoryLine('twoweeks');
    }, 4 * 3600 * 1000);
    vbdTimer = setInterval(function () {
        drawVbdLine('twoweeks');
    }, 4 * 3600 * 1000);
    vifTimer = setInterval(function () {
        drawVifLine('twoweeks');
    }, 4 * 3600 * 1000);
});

$("#onemonth").on('click', function (event) {
    event.preventDefault();
    resetSwitch();
    resetChart();
    $('.btn-monitor').removeClass('selected');
    $(this).addClass('selected');
    $('.chart-span').text("1 天");
    $('.chart-span').addClass("chart-right");
    $('#chart-group').addClass("chart-right");
    window.clearInterval(cpuTimer);
    window.clearInterval(memoryTimer);
    window.clearInterval(vbdTimer);
    window.clearInterval(vifTimer);
    drawCpuLine('onemonth');
    drawMemoryLine('onemonth');
    drawVbdLine('onemonth');
    drawVifLine('onemonth');
    cpuTimer = setInterval(function () {
        drawCpuLine('onemonth');
    }, 24 * 3600 * 1000);
    memoryTimer = setInterval(function () {
        drawMemoryLine('onemonth');
    }, 24 * 3600 * 1000);
    vbdTimer = setInterval(function () {
        drawVbdLine('onemonth');
    }, 24 * 3600 * 1000);
    vifTimer = setInterval(function () {
        drawVifLine('onemonth');
    }, 24 * 3600 * 1000);
});

$('.btn-refresh').on('click', function (event) {
    event.preventDefault();
    getInstanceBasicList();
});

function drawCpuLine(types) {
    var instanceUuid = $("#platformcontent").attr("instanceUuid");
    Highcharts.setOptions({
        global: {
            useUTC: false
        }
    });
    cpuChart = new Highcharts.Chart({
        chart: {
            renderTo: 'cpupic',
            type: 'spline',
            animation: Highcharts.svg, // don't animate in old IE
            height: 300,
            events: {
                load: function () {
                }
            }
        },
        title: {
            text: ''
        },
        xAxis: {
            type: 'datetime'
        },
        yAxis: {
            title: {
                text: ''
            },
            min: 0,
            labels: {
                formatter: function () {
                    return this.value + ' %';
                }
            }
        },
        plotOptions: {
            spline: {
                lineWidth: 2.0,
                fillOpacity: 0.1,
                marker: {
                    enabled: false,
                    states: {
                        hover: {
                            enabled: true,
                            radius: 2
                        }
                    }
                },
                shadow: false
            }
        },
        tooltip: {
            formatter: function () {
                return '<b>CPU ' + this.series.name + '</b><br>' +
                    Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br>' +
                    Highcharts.numberFormat(this.y, 2) + ' %';
            }
        },
        legend: {
            enabled: false
        },
        exporting: {
            enabled: false
        },
        series: []
    });

    updateCpuData(instanceUuid, types);
}

function drawMemoryLine(types) {
    var instanceUuid = $("#platformcontent").attr("instanceUuid");
    Highcharts.setOptions({
        global: {
            useUTC: false
        }
    });
    memoryChart = new Highcharts.Chart({
        chart: {
            renderTo: 'memorypic',
            type: 'spline',
            animation: Highcharts.svg, // don't animate in old IE
            height: 300,
            events: {
                load: function () {
                }
            }
        },
        title: {
            text: ''
        },
        xAxis: {
            type: 'datetime'
        },
        yAxis: {
            title: {
                text: ''
            },
            min: 0,
            labels: {
                formatter: function () {
                    return this.value + ' GB';
                }
            }
        },
        plotOptions: {
            spline: {
                lineWidth: 2.0,
                fillOpacity: 0.1,
                marker: {
                    enabled: false,
                    states: {
                        hover: {
                            enabled: true,
                            radius: 2
                        }
                    }
                },
                shadow: false
            }
        },
        tooltip: {
            formatter: function () {
                return '<b>' + this.series.name + '</b><br>' +
                    Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br>' +
                    Highcharts.numberFormat(this.y, 2) + ' GB';
            }
        },
        legend: {
            enabled: false
        },
        exporting: {
            enabled: false
        },
        series: []
    });

    updateMemoryData(instanceUuid, types);
}

function drawVbdLine(types) {
    var instanceUuid = $("#platformcontent").attr("instanceUuid");
    Highcharts.setOptions({
        global: {
            useUTC: false
        }
    });
    vbdChart = new Highcharts.Chart({
        chart: {
            renderTo: 'vbdpic',
            type: 'spline',
            animation: Highcharts.svg, // don't animate in old IE
            height: 300,
            events: {
                load: function () {
                }
            }
        },
        title: {
            text: ''
        },
        xAxis: {
            type: 'datetime'
        },
        yAxis: {
            title: {
                text: ''
            },
            min: 0,
            labels: {
                formatter: function () {
                    return this.value + ' kbps';
                }
            }
        },
        plotOptions: {
            spline: {
                lineWidth: 2.0,
                fillOpacity: 0.1,
                marker: {
                    enabled: false,
                    states: {
                        hover: {
                            enabled: true,
                            radius: 2
                        }
                    }
                },
                shadow: false
            }
        },
        tooltip: {
            formatter: function () {
                return '<b>' + this.series.name + '</b><br>' +
                    Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br>' +
                    Highcharts.numberFormat(this.y, 2) + ' kbps';
            }
        },
        legend: {
            enabled: false
        },
        exporting: {
            enabled: false
        },
        series: []
    });

    updateVbdData(instanceUuid, types);

}

function drawVifLine(types) {
    var instanceUuid = $("#platformcontent").attr("instanceUuid");
    Highcharts.setOptions({
        global: {
            useUTC: false
        }
    });
    vifChart = new Highcharts.Chart({
        chart: {
            renderTo: 'vifpic',
            type: 'spline',
            animation: Highcharts.svg, // don't animate in old IE
            height: 300,
            events: {
                load: function () {
                }
            }
        },
        title: {
            text: ''
        },
        xAxis: {
            type: 'datetime'
        },
        yAxis: {
            title: {
                text: ''
            },
            min: 0,
            labels: {
                formatter: function () {
                    return this.value + ' kb/s';
                }
            }
        },
        plotOptions: {
            spline: {
                lineWidth: 2.0,
                fillOpacity: 0.1,
                marker: {
                    enabled: false,
                    states: {
                        hover: {
                            enabled: true,
                            radius: 2
                        }
                    }
                },
                shadow: false
            }
        },
        tooltip: {
            formatter: function () {
                return '<b>' + this.series.name + '</b><br>' +
                    Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br>' +
                    Highcharts.numberFormat(this.y, 2) + ' kb/s';
            }
        },
        legend: {
            enabled: false
        },
        exporting: {
            enabled: false
        },
        series: []
    });
    updateVifData(instanceUuid, types);
}

function updateCpuData(uuid, type) {
    $.ajax({
        type: 'get',
        url: '/PerformanceAction/CPU',
        data: {uuid:uuid,type:type},
        dataType: 'text',
        success: function (response) {
            var obj = jQuery.parseJSON(response);
            var hasPic = false;
            for (key in obj) {
                hasPic = true;
                var cpuId = key;
                var array = obj[key];
                var cpuData = [];
                for (var c = 0; c < array.length; c++) {
                    var times = array[c].times;
                    var usage = array[c].usage;
                    cpuData.push({x: times, y: usage});
                }
                cpuChart.addSeries({
                    name: cpuId,
                    data: cpuData
                }, false);
            }
            if (hasPic) {
                cpuChart.redraw();
            } else {
                $('#chart-area-1').html('<div class="no-data">没有数据</div>');
            }
        }
    });
}

function updateMemoryData(uuid, type) {
    $.ajax({
        type: 'get',
        url: '/PerformanceAction/Memory',
        data:{uuid:uuid,type:type},
        dataType: 'text',
        success: function (response) {
            var array = jQuery.parseJSON(response);
            var memoryTotalData = [];
            var memoryUsedData = [];
            var hasPic = false;
            for (var c = 0; c < array.length; c++) {
                hasPic = true;
                var times = array[c].times;
                var total = array[c].total;
                var used = array[c].used;
                memoryTotalData.push({x: times, y: total});
                memoryUsedData.push({x: times, y: used});
            }
            if (memoryTotalData.length != 0) {
                memoryChart.addSeries({
                    name: "总量",
                    data: memoryTotalData
                }, false);
                memoryChart.addSeries({
                    name: "使用",
                    data: memoryUsedData
                }, false);
            }
            if (hasPic) {
                memoryChart.redraw();
            } else {
                $('#chart-area-2').html('<div class="no-data">没有数据</div>');
            }
        }
    });
}

function updateVbdData(uuid, type) {
    $.ajax({
        type: 'get',
        url: '/PerformanceAction/VBD',
        data: {uuid:uuid,type:type},
        dataType: 'text',
        success: function (response) {
            var obj = jQuery.parseJSON(response);
            var hasPic = false;
            for (key in obj) {
                hasPic = true;
                var vbdId = key;
                var array = obj[key];
                var vbdReadData = [];
                var vbdWriteData = [];
                for (var c = 0; c < array.length; c++) {
                    var times = array[c].times;
                    var read = array[c].read;
                    var write = array[c].write;
                    vbdReadData.push({x: times, y: read});
                    vbdWriteData.push({x: times, y: write});
                }
                vbdChart.addSeries({
                    name: vbdId + " 读",
                    data: vbdReadData
                }, false);
                vbdChart.addSeries({
                    name: vbdId + " 写",
                    data: vbdWriteData
                }, false);
            }
            if (hasPic) {
                vbdChart.redraw();
            } else {
                $('#chart-area-3').html('<div class="no-data">没有数据</div>');
            }
        }
    });
}

function updateVifData(uuid, type) {
    $.ajax({
        type: 'get',
        url: '/PerformanceAction/VIF',
        data: {uuid:uuid,type:type},
        dataType: 'text',
        success: function (response) {
            var obj = jQuery.parseJSON(response);
            var hasPic = false;
            for (key in obj) {
                hasPic = true;
                var vifId = key;
                var array = obj[key];
                var vifRxData = [];
                var vifTxData = [];
                for (var c = 0; c < array.length; c++) {
                    var times = array[c].times;
                    var rx = array[c].rx;
                    var tx = array[c].tx;
                    vifRxData.push({x: times, y: rx});
                    vifTxData.push({x: times, y: tx});
                }
                vifChart.addSeries({
                    name: "VIF" + vifId + " 下行",
                    data: vifRxData
                }, false);
                vifChart.addSeries({
                    name: "VIF" + vifId + " 上行",
                    data: vifTxData
                }, false);
            }
            if (hasPic) {
                vifChart.redraw();
            } else {
                $('#chart-area-4').html('<div class="no-data">没有数据</div>');
            }
        },
        error: function () {
        }
    });
}

$('.oc-switch').bootstrapSwitch();

$('.oc-switch').on('switchChange.bootstrapSwitch', function (event, state) {
    if (state == true) {
        thirtymin();
    } else {
        sixhours();
    }
});



function getInstanceBasicList() {
    var instanceUuid = $("#platformcontent").attr("instanceUuid");
    $('#basic-list').html("");
    $.ajax({
        type: 'get',
        url: '/VMAction/VMDetail',
        data: {uuid: instanceUuid},
        dataType: 'json',
        success: function (obj) {
            var instanceName = decodeURI(obj.instanceName);
            var instanceDesc = decodeURI(obj.instanceDesc);
            var instanceState = obj.instanceState;
            var stateStr = '';
            var showstr = '';
            var showuuid = "i-" + instanceUuid.substring(0, 8);
            if (instanceState == 1) {
                stateStr = '<td state="on"><span class="icon-status icon-running" name="stateicon"></span><span name="stateword">正常运行</span></td>';
                showstr = "<a class='id'>" + showuuid + '</a><a class="console" data-uuid=' + instanceUuid + '><img src="../img/user/console.png"></a>';
            } else if (instanceState == 0) {
                stateStr = '<td state="off"><span class="icon-status icon-stopped" name="stateicon"></span><span name="stateword">已关机</span></td>';
                showstr = "<a class='id'>" + showuuid + '</a>';
            } else {
                stateStr = '<td state="off"><span class="icon-status icon-process" name="stateicon"></span><span name="stateword">创建中</span></td>';
                showstr = "<a class='id'>" + showuuid + '</a>';
            }
            $("#instancestate").val(instanceState);
            backfunction();    //add by cyh

            var createDate = obj.createDate;
            var useDate = decodeURI(obj.useDate);
            var instanceCPU = obj.instanceCPU;
            var instanceMemory = obj.instanceMemory;
            if (instanceMemory < 1024) {
                instanceMemory = instanceMemory + "&nbsp;MB";
            } else {
                instanceMemory = instanceMemory / 1024 + "&nbsp;GB";
            }
            var instanceFirewall = obj.instanceFirewall;
            if (instanceFirewall != "") {
                instanceFirewall = 'fw-' + instanceFirewall.substring(0, 8);
            } else {
                instanceFirewall = "&nbsp;";
            }
            var instanceDisk = '';
            var volList = obj.volList;
            if ('&nbsp;' != volList) {
                for (var i = 0; i < volList.length; i++) {
                    instanceDisk = instanceDisk + '<a class="id volid" voluuid="' + volList[i] + '">vol-' + volList[i].substring(0, 8) + '</a><br/>';
                }
            } else {
                instanceDisk = volList;
            }
            var instanceEip = obj.instanceEip;
            if (instanceEip == "null") {
                instanceEip = "&nbsp;";
            } else {
                instanceEip = '<a class="id" id="eip"  eipip="' + instanceEip + '">' + instanceEip + '</a>';
            }
            var instanceMAC = obj.instanceMAC;
            var instanceIP = obj.instanceIP;
            var vlan = obj.vlan;
            var network;
            if (instanceIP == "null") {
                if (vlan == "null") {
                    network = '<a>(基础网络)</a>';
                } else {
                    network = '<a id="vnetip" vnetip="' + obj.vlanUuid + '" routerUuid="' + obj.routerUuid + '">(' + vlan + ')</a>';
                }
            } else {
                if (vlan == "null") {
                    network = '<a>(基础网络)&nbsp;/&nbsp;' + instanceIP + '</a>';
                } else {
                    network = '<a id="vnetip" vnetip="' + obj.vlanUuid + '" routerUuid="' + obj.routerUuid + '">(' + vlan + ')&nbsp;/&nbsp;' + instanceIP + '</a>';
                }
            }
            var backDate = decodeURI(obj.backupdate);
            var bkId = "&nbsp;";
            if (backDate != "") {
                bkId = '<a class="id" id="snapshotid" rsuuid="' + instanceUuid + '" rsname="' + instanceName + '">bk-' + instanceUuid.substring(0, 8) + '</a>';
            } else {
                backDate = "&nbsp;";
            }
            $('#basic-list').html('<dt>ID</dt><dd>' + showstr + '</dd><dt>名称</dt><dd id="instancename">'
                + instanceName + '</dd><dt>描述</dt><dd id="instancedesc">'
                + instanceDesc + '</dd><dt>状态</dt><dd>'
                + stateStr + '</dd><dt>CPU</dt><dd>'
                + instanceCPU + '&nbsp;核</dd><dt>内存</dt><dd>'
                + instanceMemory + '</dd><dt>MAC地址</dt><dd>'
                + instanceMAC + '</dd><dt>创建时间</dt><dd class="time">'
                + createDate + '</dd><dt>运行时间</dt><dd class="time">'
                + useDate + '</dd>');
            $('#depend-list').html('<dt>网络</dt><dd>'
                + network + '</dd><dt>硬盘</dt><dd>'
                + instanceDisk + '</dd><dt>公网IP</dt><dd>'
                + instanceEip + '</dd><dt>防火墙</dt><dd><a class="id" id="firewallid" firewallid="' + obj.instanceFirewall + '">'
                + instanceFirewall + '</a></dd><dt>备份链</dt><dd>'
                + bkId + '</dd><dt>距上次备份时间</dt><dd class="time">'
                + backDate + '</dd>');


            ///add by cyh
            if (instanceIP == "null") {
                $("#instanceID").find(".private-ip").text("");
            }
            else {
                $("#instanceID").find(".private-ip").text(instanceIP);
            }

            if (obj.instancevlan != null && obj.instancevlan != "null") {
                ///私有网络
                $("#instancenetworkDiv").hide();
                $("#instancesgDiv").hide();
                $("#instancerouterDiv").show();
                $("#vnUuid").val(obj.instancevlan);
                $.ajax({
                    type: 'get',
                    url: '/VnetAction',
                    data: "action=getonevnet&vnUuid=" + obj.instancevlan,
                    dataType: 'json',
                    success: function (obj) {
                        $("#vxnetName").text(decodeURI(obj.vnetName));
                        if (obj.vnetRouter != null && obj.vnetRouter != "&nbsp;" && obj.vnetRouter != "null") {
                            $("#vnrouterUuid").val(obj.vnetRouter);
                            $("#btnshowrouter").hide();
                            $("#componentInstanceRouterDiv").show();
                            $.ajax({
                                type: 'get',
                                url: '/RouterAction',
                                data: "action=getonerouter&routerUuid=" + obj.vnetRouter,
                                dataType: 'json',
                                success: function (obj) {
                                    $("#componentRouterDiv").find(".private-ip").text(obj.routerIp);
                                    $("#componentRouterDiv").find(".router-id").text(decodeURI(obj.routerName));
                                    $("#componentRouterSgDiv").find(".sg-name").text(decodeURI(obj.routerFirewallName));

                                    if (obj.eip != "") {
                                        $("#routerunbingpublic .component-id").text(obj.eip);
                                        $("#routerbingpublic").hide();
                                        $("#routerunbingpublic").show();
                                    }
                                }
                            });
                        }
                        else {
                            $("#vnrouterUuid").val("");
                            $("#componentInstanceRouterDiv").hide();
                            $("#btnshowrouter").hide();
                        }
                    }
                });

            } else {
                ///vlan 是null，说明是基础网络
                $("#instancenetworkDiv").show();
                $("#instancesgDiv").show();
                $("#instancerouterDiv").hide();

                $("#vnUuid").val("");
                $("#vnrouterUuid").val("");
                if (obj.instanceEip != null && obj.instanceEip != "null" && obj.instanceEip != "" && obj.instanceEip != "&nbsp;") {
                    $("#unbindpublic .publicipname").text(obj.instanceEip);
                    $("#bingpublic").hide();
                    $("#unbindpublic").show();
                }

                $("#firewallId").text(decodeURI(obj.instanceFirewallName));
            }

            ///显示硬盘
            bindvolumesshow();

            $("#instanceID").find(".instance-name").text(instanceName);

            $("#basic-list2").html($('#basic-list').html());
        }
    });
}

function backfunction() {
    var state = $("#instancestate").val();
    if (state == 1) {
        ///正在运行
        $("#uistart").addClass("btn-forbidden");
        $("#uirestart").removeClass("btn-forbidden");
        $("#uiclose").removeClass("btn-forbidden");

    } else if (state == 0) {
        ///正常关机
        $("#uistart").removeClass("btn-forbidden");
        $("#uirestart").addClass("btn-forbidden");
        $("#uiclose").addClass("btn-forbidden");
    }
}

///add by cyh 
function init() {
    $('.view-types').on('click', '.view-type', function (event) {
        event.preventDefault();
        $('a', $('.view-types')).removeClass('current');
        $(this).addClass('current');
        var type = $(this).attr("oc-type");
        if (type == 'text') {
            $("#textview").show();
            $("#imageview").hide();
        } else {
            $("#textview").hide();
            $("#imageview").show();
            doShowPic();
        }
    });

    $(".graph-actions a").mouseenter(function () {
        if (!$(this).hasClass("btn-forbidden")) {
            $(this).find(".text").show();
        }
    }).mouseleave(function () {
        $(this).find(".text").hide();
    });

    $('.component-router-sg').mouseenter(function () {
        $(this).find('.btn').show();
    }).mouseleave(function () {
        $(this).find('.btn').hide();
    });

    $('.component-instance-sg').mouseenter(function () {
        $(this).find('.btn').show();
    }).mouseleave(function () {
        $(this).find('.btn').hide();
    });

    $("#componentsId").on("click", "#bingpublic", function () {
        bindpublicip("vm");
    });

    $("#componentsId").on("click", "#routerbingpublic", function () {
        bindpublicip("rt");
    });

    $("#componentsId").on("click", "#deleteeip", function () {
        unbingpublicip("vm");
    });

    $("#componentsId").on("click", "#deleteroutereip", function () {
        unbingpublicip("rt");
    });

    $("#componentsId").on("click", "#bingfirewall", function () {
        bingfirewall("vm");
    });

    $("#componentsId").on("click", "#componentRouterSgbtn", function () {
        bingfirewall("rt");
    });

    $("#componentsId").on("click", "#instanceNetID", function () {
        changeNet();///切换网络
    });

    $("#instance-volumeDiv").on("mouseenter", ".component-instance-volume", function () {
        $(this).find(".btn-delete").show();
    });

    $("#instance-volumeDiv").on("mouseleave", ".component-instance-volume", function () {
        $(this).find(".btn-delete").hide();
    });

    //bindvolumesshow();

    $("#instance-volumeDiv").on("click", "#bindvolumes", function () {
        bindvolumes();
    });

    $("#instance-volumeDiv").on("click", ".btn-delete", function () {
        deletevolumes(this);
    });

    $('#uistart').on('click', function (event) {
        event.preventDefault();
        showbox(0);
    });
    $('#uirestart').on('click', function (event) {
        event.preventDefault();
        showbox(1);
    });
    $('#destroy').on('click', function (event) {
        event.preventDefault();
        showbox(2);
    });
    $('#uiclose').on('click', function (event) {
        event.preventDefault();
        showbox(3);
    });

    $('#createimage').on('click', function (event) {
        event.preventDefault();
        var thisurl = $(this).attr('url');
        thisurl = thisurl + '?rsid=' + $("#platformcontent").attr("instanceUuid");
        $('#InstanceModalContainer').load(thisurl, '', function () {
            $('#InstanceModalContainer').modal({
                backdrop: false,
                show: true
            });
        });
    });

    $('#backup').on('click', function (event) {
        event.preventDefault();
        var thisurl = $(this).attr('url');
        thisurl = thisurl + '?rsid=' + $("#platformcontent").attr("instanceUuid") + '&rstype=instance&rsname=' + $("#instancename").text();
        bootbox.dialog({
            className: "bootbox-large",
            message: '<div class="alert alert-warning" style="margin:10px; color:#c09853">'
                + '<span class="glyphicon glyphicon-warning-sign"></span>&nbsp;对正在运行的主机进行在线备份时，需要注意以下几点:<br/><br/>1. 备份只能捕获在备份任务开始时已经写入磁盘的数据，不包括当时位于内存里的数据；<br/>2. 为了保证数据的完整性，请在创建备份前暂停所有文件的写操作，或进行离线备份。</div>',
            title: "提示",
            buttons: {
                main: {
                    label: "创建备份",
                    className: "btn-primary",
                    callback: function () {
                        $('#InstanceModalContainer').load(thisurl, '', function () {
                            $('#InstanceModalContainer').modal({
                                backdrop: false,
                                show: true
                            });
                        });
                    }
                },
                cancel: {
                    label: "取消",
                    className: "btn-default",
                    callback: function () {

                    }
                }
            }
        });
    });

}

function changeNet() {
    var infoList = $("#instanceid").val();
    var tablelist = "";
    $.ajax({
        type: 'post',
        url: '/VnetAction',
        data: 'action=getalllist',
        dataType: 'json',
        success: function (array) {
            if (array.length > 0) {
                //tablelist ='<div class="image-item selected" vnetuuid="-1"><div class="image-left">基础网络</div>&nbsp;&nbsp;vxnet-0</div>';
                $.each(array, function (index, item) {
                    if (index == 0) {
                        tablelist = tablelist + '<div class="image-item selected" vnetuuid="' + item.vnetuuid + '"><div class="image-left">' + decodeURI(item.vnetname) + '</div>&nbsp;&nbsp;' + "vn-" + item.vnetuuid.substring(0, 8) + '</div>';
                    } else {
                        tablelist = tablelist + '<div class="image-item" vnetuuid="' + item.vnetuuid + '" ><div class="image-left">' + decodeURI(item.vnetname) + '</div>&nbsp;&nbsp;' + "vn-" + item.vnetuuid.substring(0, 8) + '</div>';
                    }
                });
            }

            ///1 查询出所有可用的 公网ip。
            var showMessage = '<div class="epubliciplist" id="epubliciplist" style="margin:10px 10px 0">' + tablelist + '</div>';

            var showTitle = '选择主机 &nbsp;' + infoList + ' 要绑定的网络';

            bootbox.dialog({
                className: "oc-bootbox",
                message: showMessage,
                title: showTitle,
                buttons: {
                    main: {
                        label: "确定",
                        className: "btn-primary",
                        callback: function () {
                            var vnetuuid = $("#epubliciplist").find(".selected").attr("vnetuuid");
                            var vmuuid = $("#platformcontent").attr("instanceUuid");

                            $.ajax({
                                type: 'get',
                                url: '/VnetAction',
                                data: 'action=addonevm&vnId=' + vnetuuid + '&vmuuid=' + vmuuid,
                                dataType: 'text',
                                success: function (response) {
                                    getInstanceBasicList();
                                }
                            });

                        }
                    },
                    cancel: {
                        label: "取消",
                        className: "btn-default",
                        callback: function () {
                        }
                    }
                }
            });
            $("#epubliciplist").parents(".modal-dialog").width(600);
            $('.epubliciplist').on('click', '.image-item', function (event) {
                event.preventDefault();
                $('div', $('.epubliciplist')).removeClass('selected');
                $(this).addClass('selected');
            });
        }
    });

}

function showbox(type) {
    var infoList = $("#instanceid").val();
    var infoArray = new Array("启动主机", "重启主机", "销毁主机", "关闭主机");
    var showMessage = '';
    var showTitle = '';
    if (type == 3) {
        showMessage = '<div class="alert alert-info" style="margin:10px 10px 0">1. 强制关机会丢失内存中的数据<br/>'
            + '2. 为保证数据的完整性，请在强制关机前暂停所有文件的写操作，或进行正常关机。</div>'
            + '<div class="item" style="margin:0"><div class="controls" style="margin-left:100px">'
            + '<label class="inline"><input type="checkbox" id="force">&nbsp;强制关机</label></div></div>';
        showTitle = infoArray[type] + '&nbsp;' + infoList + '?';
    } else {
        showMessage = '<div class="alert alert-info" style="margin:10px">'
            + '<span class="glyphicon glyphicon-info-sign"></span>&nbsp;'
            + infoArray[type] + '&nbsp;' + infoList + '?</div>';
        showTitle = '提示';
    }
    bootbox.dialog({
        className: "oc-bootbox",
        message: showMessage,
        title: showTitle,
        buttons: {
            main: {
                label: "确定",
                className: "btn-primary",
                callback: function () {
                    var uuid = $("#platformcontent").attr("instanceUuid");
                    if (type == 0) {
                        startVM(uuid);
                    } else if (type == 1) {
                        restartVM(uuid);
                    } else if (type == 2) {
                        destroyVM(uuid);
                    } else if (type == 3) {
                        var force = $('#force')[0].checked;
                        shutdownVM(uuid, force);
                    }
                }
            },
            cancel: {
                label: "取消",
                className: "btn-default",
                callback: function () {
                }
            }
        }
    });
}

///绑定公网ip
function bindpublicip(bdtype) {
    var infoList = $("#instanceid").val();
    var tablelist = "";
    $.ajax({
        type: 'post',
        url: '/EipAction',
        data: 'action=getableeips',
        dataType: 'json',
        success: function (array) {
            if (array.length > 0) {
                $.each(array, function (index, item) {
                    if (index == 0) {
                        tablelist = tablelist + '<div class="image-item selected" eid="' + item.eipUuid + '" eip="' + item.eipIp + '"><div class="image-left">' + decodeURI(item.eipName) + '</div>IP:&nbsp;&nbsp;' + item.eipIp + '</div>';
                    } else {
                        tablelist = tablelist + '<div class="image-item" eid="' + item.eipUuid + '" eip="' + item.eipIp + '"><div class="image-left">' + decodeURI(item.eipName) + '</div>IP:&nbsp;&nbsp;' + item.eipIp + '</div>';
                    }
                });
            }

            ///1 查询出所有可用的 公网ip。
            var showMessage = '<div class="alert alert-warning" style="margin:10px 10px 0">默认情况下，除了少数安全端口之外，主机的大部分端口都是关闭的，您需要在防火墙中打开相应的下行规则以允许外网访问。</div>'
                + '<div class="epubliciplist" id="epubliciplist" style="margin:10px 10px 0">' + tablelist + '</div>';

            var showTitle = '选择主机 &nbsp;' + infoList + ' 要绑定的公网IP';

            bootbox.dialog({
                className: "oc-bootbox",
                message: showMessage,
                title: showTitle,
                buttons: {
                    main: {
                        label: "确定",
                        className: "btn-primary",
                        callback: function () {
                            var uuid = $("#epubliciplist").find(".selected").attr("eid");
                            var eipIp = $("#epubliciplist").find(".selected").attr("eip");
                            var vmuuid = $("#platformcontent").attr("instanceUuid");
                            var router = $("#vnrouterUuid").val();
                            if (bdtype == "vm") {
                                $.ajax({
                                    type: 'get',
                                    url: '/EipAction',
                                    data: 'action=bind&vmuuid=' + vmuuid + '&eipIp=' + eipIp + '&bindtype=' + bdtype,
                                    dataType: 'json',
                                    success: function (obj) {
                                        if (obj.result == true) {
                                            $("#unbindpublic .publicipname").text(eipIp);
                                            $("#bingpublic").hide();
                                            $("#unbindpublic").show();
                                        } else {

                                        }
                                    },
                                    error: function () {
                                    }
                                });
                            } else if (bdtype == "rt") {
                                $.ajax({
                                    type: 'get',
                                    url: '/EipAction',
                                    data: 'action=bind&vmuuid=' + router + '&eipIp=' + eipIp + '&bindtype=' + bdtype,
                                    dataType: 'json',
                                    success: function (obj) {
                                        if (obj.result == true) {
                                            $("#routerunbingpublic .component-id").text(eipIp);
                                            $("#routerbingpublic").hide();
                                            $("#routerunbingpublic").show();
                                        } else {

                                        }
                                    },
                                    error: function () {
                                    }
                                });
                            }

                        }
                    },
                    cancel: {
                        label: "取消",
                        className: "btn-default",
                        callback: function () {
                        }
                    }
                }
            });
            $("#epubliciplist").parents(".modal-dialog").width(600);
            $('.epubliciplist').on('click', '.image-item', function (event) {
                event.preventDefault();
                $('div', $('.epubliciplist')).removeClass('selected');
                $(this).addClass('selected');
            });
        },
        error: function () {

        }
    });


}

///删除硬盘
function deletevolumes(obj) {
    var showMessage = '';
    var showTitle = '';

    showMessage = '<div class="alert alert-warning" style="margin:10px; color:#c09853">'
        + '<span class="glyphicon glyphicon-warning-sign"></span>&nbsp;物理卸载硬盘时，需要注意以下几点:'
        + '<br/><br/>1. 卸载硬盘时会丢失位于缓存中的数据；<br/>2. 为保证数据的完整性，最好确保该硬盘在主机的操作系统中处于非加载状态。</div>';
    showTitle = '卸载硬盘' + '&nbsp;' + '?';

    bootbox.dialog({
        className: "oc-bootbox",
        message: showMessage,
        title: showTitle,
        buttons: {
            main: {
                label: "确定",
                className: "btn-primary",
                callback: function () {
                    var volumeuuid = $(obj).data("id");
                    $.ajax({
                        type: 'post',
                        url: '/VolumeAction/Unbind',
                        data: {volumeUuid:volumeuuid},
                        dataType: 'json',
                        success: function (obj) {
                            bindvolumesshow();
                        },
                        error: function () {
                        }
                    });
                }
            },
            cancel: {
                label: "取消",
                className: "btn-default",
                callback: function () {
                }
            }
        }
    });
}

function unbingpublicip(bdtype) {
    var vmuuid = "";
    var eipIp = "";
    if (bdtype == "vm") {
        vmuuid = $("#platformcontent").attr("instanceUuid");
        eipIp = $("#unbindpublic .publicipname").text();
    } else if (bdtype = "rt") {
        vmuuid = $("#vnrouterUuid").val();
        eipIp = $("#routerunbingpublic .component-id").text();
    }
    $.ajax({
        type: 'get',
        url: '/EipAction',
        data: "action=unbind&eipIp=" + eipIp + "&vmuuid=" + vmuuid + '&bindtype=' + bdtype,
        dataType: 'json',
        success: function (obj) {
            if (bdtype == "vm") {
                $("#unbindpublic .publicipname").text("");
                $("#bingpublic").show();
                $("#unbindpublic").hide();
            } else if (bdtype = "rt") {
                $("#routerunbingpublic .component-id").text("");
                $("#routerbingpublic").show();
                $("#routerunbingpublic").hide();
            }
        },
        error: function () {
        }
    });
}

function bingfirewall(bdtype) {
    var infoList = $("#instanceid").val();
    var tablelist = "";
    $.ajax({
        type: 'post',
        url: '/FirewallAction',
        data: 'action=getabledfirewalls',
        dataType: 'json',
        success: function (array) {
            if (array.length > 0) {
                $.each(array, function (index, item) {
                    if (index == 0) {
                        tablelist = tablelist + '<div class="image-item selected" firewallId="' + item.firewallId + '" firewallName="' + decodeURI(item.firewallName) + '"><div class="image-left">' + "fw-" + item.firewallId.substring(0, 8) + '</div>&nbsp;&nbsp;' + decodeURI(item.firewallName) + '</div>';
                    } else {
                        tablelist = tablelist + '<div class="image-item" firewallId="' + item.firewallId + '" firewallName="' + decodeURI(item.firewallName) + '"><div class="image-left">' + "fw-" + item.firewallId.substring(0, 8) + '</div>&nbsp;&nbsp;' + decodeURI(item.firewallName) + '</div>';
                    }
                });
            }

            ///1 查询出所有可用的 公网ip。
            var showMessage = '<div class="epubliciplist" id="epubliciplist" style="margin:10px 10px 0">' + tablelist + '</div>';

            var showTitle = '选择主机 &nbsp;' + infoList + ' 要绑定的防火墙';

            bootbox.dialog({
                className: "oc-bootbox",
                message: showMessage,
                title: showTitle,
                buttons: {
                    main: {
                        label: "确定",
                        className: "btn-primary",
                        callback: function () {
                            var firewallId = $("#epubliciplist").find(".selected").attr("firewallId");
                            var firewallName = $("#epubliciplist").find(".selected").attr("firewallName");
                            var bindtype = 'vm';
                            var vmuuid = "[" + $("#platformcontent").attr("instanceUuid") + "]";
                            var router = "[" + $("#vnrouterUuid").val() + "]";
                            if (bdtype == "vm") {
                                $.ajax({
                                    type: 'get',
                                    url: '/FirewallAction',
                                    data: 'action=bindfirewall&firewallId=' + firewallId + '&vmuuidStr=' + vmuuid + '&bindtype=' + bdtype,
                                    dataType: 'json',
                                    success: function (response) {
                                        if (response.isSuccess) {
                                            $("#firewallId").html(firewallName);
                                        }
                                        else {
                                            //$("#firewallId").html(firewallName);
                                            //alert("失败");
                                        }
                                    },
                                    error: function () {
                                    }
                                });
                            } else if (bdtype == "rt") {
                                $.ajax({
                                    type: 'get',
                                    url: '/FirewallAction',
                                    data: 'action=bindfirewall&firewallId=' + firewallId + '&vmuuidStr=' + router + '&bindtype=' + bdtype,
                                    dataType: 'json',
                                    success: function (response) {
                                        if (response.isSuccess) {
                                            $("#componentRouterSgDiv").find(".sg-name").text(firewallName);
                                        }
                                        else {
                                            //$("#componentRouterSgDiv").find(".sg-name").text(firewallName);
                                            //alert("失败");
                                        }
                                    },
                                    error: function () {
                                    }
                                });
                            }

                        }
                    },
                    cancel: {
                        label: "取消",
                        className: "btn-default",
                        callback: function () {
                        }
                    }
                }
            });
            $("#epubliciplist").parents(".modal-dialog").width(600);
            $('.epubliciplist').on('click', '.image-item', function (event) {
                event.preventDefault();
                $('div', $('.epubliciplist')).removeClass('selected');
                $(this).addClass('selected');
            });
        },
        error: function () {

        }
    });
}

function bindvolumes() {
    var infoList = $("#instanceid").val();
    var tablelist = "";
    $.ajax({
        type: 'post',
        url: '/VolumeAction/AvailableVolumes',
        dataType: 'json',
        success: function (array) {
            if (array.length > 0) {
                $.each(array, function (index, item) {
                    if (index == 0) {
                        tablelist = tablelist + '<div class="image-item selected" volumeId="' + item.volumeId + '" volumeName="' + decodeURI(item.volumeName) + '"><div class="image-left">' + "fw-" + item.volumeId.substring(0, 8) + '</div>&nbsp;&nbsp;' + decodeURI(item.volumeName) + '</div>';
                    } else {
                        tablelist = tablelist + '<div class="image-item" volumeId="' + item.volumeId + '" volumeName="' + decodeURI(item.volumeName) + '"><div class="image-left">' + "fw-" + item.volumeId.substring(0, 8) + '</div>&nbsp;&nbsp;' + decodeURI(item.volumeName) + '</div>';
                    }
                });
            }

            ///1 查询出所有可用的 公网ip。
            var showMessage = '<div class="epubliciplist" id="epubliciplist" style="margin:10px 10px 0">' + tablelist + '</div>';

            var showTitle = '选择主机 &nbsp;' + infoList + ' 要 挂载的硬盘';

            bootbox.dialog({
                className: "oc-bootbox",
                message: showMessage,
                title: showTitle,
                buttons: {
                    main: {
                        label: "确定",
                        className: "btn-primary",
                        callback: function () {
                            var volumeId = $("#epubliciplist").find(".selected").attr("volumeId");
                            var volumeName = $("#epubliciplist").find(".selected").attr("volumeName");
                            var bindtype = 'vm';
                            var vmuuid = $("#platformcontent").attr("instanceUuid");
                            $.ajax({
                                type: 'get',
                                url: '/VolumeAction/Bind',
                                data: {volumeUuid:volumeId,vmUuid:vmuuid},
                                dataType: 'json',
                                complete: function (response) {
                                     bindvolumesshow();
                                }
                            });
                        }
                    },
                    cancel: {
                        label: "取消",
                        className: "btn-default",
                        callback: function () {
                        }
                    }
                }
            });
            $("#epubliciplist").parents(".modal-dialog").width(600);
            $('.epubliciplist').on('click', '.image-item', function (event) {
                event.preventDefault();
                $('div', $('.epubliciplist')).removeClass('selected');
                $(this).addClass('selected');
            });
        },
        error: function () {

        }
    });
}

function bindvolumesshow() {
    var vmuuid = $("#platformcontent").attr("instanceUuid");
    $.ajax({
        type: 'get',
        url: '/VolumeAction/VolumesOfVM',
        data: {vmUuid:vmuuid},
        dataType: 'json',
        success: function (array) {
            var i = array.length;
            var j = 12 / (i + 1);
            $("#instance-volumeDiv").html("");
            if (array.length > 0) {
                $.each(array, function (index, item) {
                    $("#instance-volumeDiv").append('<div class="col-md-' + j + ' instance-volume component-instance-volume">\
			                  <a class="btn-delete" href="#" data-id="' + item.volumeId + '">\
			                  <span class="glyphicon glyphicon-remove"></span></a>\
			                  <span class="size">' + item.volumeSize + ' G</span>\
			                  <a class="volume-name"  data-permalink="">' + decodeURI(item.volumeName) + '</a>');
                });
            }
            $("#instance-volumeDiv").append(' <div class="col-md-' + j + ' component-instance-volume none">\
                    <a class="btn" href="#" id="bindvolumes"><span class="glyphicon glyphicon-hdd"></span>\
                    <span class="text">加载硬盘</span></a>\
              </div>');
        },
        error: function () {
        }
    });
}

function startVM(uuid) {
    $("#basic-list2").find("[name='stateicon']").removeClass("icon-stopped").addClass('icon-process');
    $("#basic-list2").find("[name='stateword']").text('启动中');
    $.ajax({
        type: 'get',
        url: '/VMAction/StartVM',
        data: {uuid:uuid},
        dataType: 'json',
        complete: function (array) {
            getInstanceBasicList();
        }
    });
}

function restartVM(uuid) {
    $("#basic-list2").find("[name='stateicon']").removeClass("icon-running").addClass('icon-process');
    $("#basic-list2").find("[name='stateword']").text('重启中');
    $.ajax({
        type: 'get',
        url: '/VMAction/RestartVM',
        data:{uuid:uuid},
        dataType: 'json',
        complete: function (array) {
            getInstanceBasicList();
        }
    });
}

function destroyVM(uuid) {
    $("#basic-list2").find("[name='stateicon']").removeClass("icon-running").removeClass("icon-stopped").addClass('icon-process');
    $("#basic-list2").find("[name='stateword']").text('销毁中');
    $.ajax({
        type: 'get',
        url: '/VMAction/DeleteVM',
        data:{uuid:uuid},
        dataType: 'json',
        complete: function (array) {
            window.location.href = 'instance.jsp';
        }
    });
}

function shutdownVM(uuid, force) {
    $("#basic-list2").find("[name='stateicon']").removeClass("icon-running").addClass('icon-process');
    $("#basic-list2").find("[name='stateword']").text('关机中');
    $.ajax({
        type: 'get',
        url: '/VMAction/ShutdownVM',
        data: {uuid:uuid,force:force},
        dataType: 'json',
        complete: function (array) {
            getInstanceBasicList();
        }
    });
}

