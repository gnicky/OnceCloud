$(function(){
	showlit();
})

function showlit(){
	$('#tablebodyvm').html("");
	$('#tablebodyvol').html("");
	$('#tablebodyeip').html("");
	$.ajax({
				type : 'get',
				url : '/DatacenterAction/CompanyDetail',
				data : {cid : $("#platformcontent").attr("platformUserId") },
				dataType : 'json',
				success : function(array) {
					if (array.length > 0) {
						var vmlist = array[0].vmlist;
						var tableStr="";
						for (var i = 0; i < vmlist.length; i++) {
							var obj = eval("("+vmlist[i]+")");
							var vmuuid = obj.vmUuid;
							var vmName = decodeURIComponent(obj.vmName);
							var state = obj.vmPower;
							var showuuid = "i-" + vmuuid.substring(0, 8);
							var showstr = "<a class='id'>" + showuuid + '</a>';
							var iconStr = new Array("stopped", "running",
									"process", "process", "process", "process",
									"process");
							var nameStr = new Array("已关机", "正常运行", "创建中", "销毁中",
									"启动中", "关机中", "重启中");
							var stateStr = '<td><span class="icon-status icon-'
									+ iconStr[state] + '" name="stateicon">'
									+ '</span><span name="stateword">'
									+ nameStr[state] + '</span></td>';
							if (state == 1) {
								showstr = showstr
										+ '<a class="console" data-uuid='
										+ vmuuid
										+ '></a>';
							}
							var cpu = obj.vmCpu;
							cpu = cpu + "&nbsp;核";
							var memory = obj.vmMem;
							if (memory < 1024) {
								memory = memory + "&nbsp;MB";
							} else {
								memory = memory / 1024 + "&nbsp;GB";
							}
							
							var thistr = '<tr rowid="'
									+ '"><td name="vmname">' + vmName
									+ '</td>' + stateStr + '<td name="cpuCore">'
									+ cpu + '</td><td name="memoryCapacity">'
									+ memory + '</td><td name="createtime" class="time">'
									+ decodeURIComponent(obj.createDate)
									+ '</td></tr>';
							tableStr += thistr;
						}
						$('#tablebodyvm').html(tableStr);
						
						var volumelist = array[0].volumelist;
						tableStr="";
						 for (var i = 0; i < volumelist.length; i++) {
				                var obj = eval("("+volumelist[i]+")");
				                var volumeid = obj.volumeUuid;
				                var volumename = decodeURIComponent(obj.volumeName);
				               
				                var volState = obj.volumeStatus;
				                var usedStr = "";
				                if (volState == 2) {
				                    usedStr = usedStr + '<td><span class="icon-status icon-process" name="stateicon"></span><span name="stateword">创建中</span></td>';
				                } else if (volState == 3) {
				                    usedStr = usedStr + '<td><span class="icon-status icon-process" name="stateicon"></span><span name="stateword">安装中</span></td>';
				                } else if (volState == 5) {
				                    usedStr = usedStr + '<td><span class="icon-status icon-process" name="stateicon"></span><span name="stateword">卸载中</span></td>';
				                } else if (volState == 6) {
				                    usedStr = usedStr + '<td><span class="icon-status icon-process" name="stateicon"></span><span name="stateword">删除中</span></td>';
				                } else if (volState == 1) {
				                    usedStr = usedStr + '<td><span class="icon-status icon-running" name="stateicon"></span><span name="stateword">可用</span></td>';
				                } else if (volState == 4) {
				                    usedStr = usedStr + '<td><span class="icon-status icon-using" name="stateicon"></span><span name="stateword">使用中</span></td>';
				                }
				                var volumesize = obj.volumeSize;
				                var createdate = obj.createDate;
				               
				               
				                var showid = "vol-" + volumeid.substring(0, 8);
				                var showstr = "<a class='id'>" + showid + '</a>';
				                tableStr = tableStr + '<tr rowid="' + volumeid + '"><td name="volumename">' + volumename + '</td>'
				                    + usedStr + '</td><td name="size">'
				                    + volumesize + '</td><td name="createtime" class="time">'
				                    + decodeURIComponent(createdate) + '</td></tr>';
				            }
				            $('#tablebodyvol').html(tableStr);
				            
				            tableStr="";
				            var eiplist = array[0].eiplist;
				            for (var i = 0; i < eiplist.length; i++) {
				                var obj = eval("("+eiplist[i]+")");
				                var eipIp = obj.eipIp;
				                var eipId = obj.eipUuid;
				                var eipName = decodeURIComponent(obj.eipName);
				                var eipDepen = obj.eipDependency;
				                var usedStr="";
				                if (eipDepen!=null && eipDepen!="") {
				                    usedStr = usedStr + '<td><span class="icon-status icon-using" name="stateicon"></span><span name="stateword">已分配</span></td>';
				                } else {
				                    usedStr = usedStr + '<td><span class="icon-status icon-running" name="stateicon"></span><span name="stateword">可用</span></td>';
				                }
				                var eipBandwidth = obj.eipBandwidth;
				                var createDate = obj.createDate;
				                var thistr = '<tr eip="' + eipIp + '" eipId="' + eipId + '"><td name="eipname">'
				                    + eipName + '</td><td>'
				                    + eipIp + '</td>'  + usedStr + '</td><td name="bandwidth">'
				                    + eipBandwidth + '</td><td>电信</td><td name="createdate" class="time">' + decodeURIComponent(createDate) + '</td></tr>';
				                tableStr += thistr;
				            }
				            $('#tablebodyeip').html(tableStr);
				            
				            ////vm作图
				           // drawVmLine('sixhours');
				            
					}
				}
	});
}

var vmChart;
$(function () {
    $('#vmpic').highcharts({
        title: {
            text: '虚拟机数目变化',
            x: -20 //center
        },
       /* subtitle: {
            text: 'Source: WorldClimate.com',
            x: -20
        },*/
        xAxis: {
            categories: ['201401', '201402', '201403', '201404', '201405', '201406','201407', '201408', '201409']
        },
        yAxis: {
            title: {
                text: '虚拟机数量 (台)'
            },
            allowDecimals:false,
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            valueSuffix: '台'
        },
       /* legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },*/
        series: [{
            name: '数量',
            data: [7, 5, 10, 9, 3, 6,13, 13,5]
        }]
    });
});
	

function updateVMData(uuid, type) {
    $.ajax({
        type: 'get',
        url: '/PerformanceAction/CPU',
        data: {uuid: uuid, type: type},
        dataType: 'text',
        success: function (response) {
            var obj = jQuery.parseJSON(response);
            var hasPic = false;
            for (key in obj) {
                hasPic = true;
                var cpuId = key;
                var array = obj[key];
                var vmData = [];
                for (var c = 0; c < array.length; c++) {
                    var times = array[c].times;
                    var usage = array[c].usage;
                    vmData.push({x: times, y: usage});
                }
                vmChart.addSeries({
                    name: cpuId,
                    data: vmData
                }, false);
            }
            if (hasPic) {
            	vmChart.redraw();
            } else {
                $('#chart-area-1').html('<div class="no-data">没有数据</div>');
            }
        }
    });
}
