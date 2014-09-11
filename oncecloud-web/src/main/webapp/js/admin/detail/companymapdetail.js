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
						var vmlist = array.vmlist;
						for (var i = 1; i < vmlist.length; i++) {
							var obj = vmlist[i];
							var vmuuid = obj.vmid;
							var vmName = decodeURIComponent(obj.vmname);
							var state = obj.state;
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
										+ '><img src="../img/user/console.png"></a>';
							}
							var cpu = obj.cpu;
							cpu = cpu + "&nbsp;核";
							var memory = obj.memory;
							if (memory < 1024) {
								memory = memory + "&nbsp;MB";
							} else {
								memory = memory / 1024 + "&nbsp;GB";
							}
							var ip = obj.ip;
							var vlan = obj.vlan;
							var network;
							if (ip == "null") {
								if (vlan == "null") {
									network = '<a>(基础网络)</a>';
								} else {
									network = '<a>(' + vlan + ')</a>';
								}
							} else {
								if (vlan == "null") {
									network = '<a>(基础网络)&nbsp;/&nbsp;' + ip
											+ '</a>';
								} else {
									network = '<a>(' + vlan + ')&nbsp;/&nbsp;' + ip
											+ '</a>';
								}
							}
							var publicip = obj.publicip;
							var backupdate = obj.backupdate + "";
							var backupStr = decodeURIComponent(backupdate);
							if (backupdate == "") {
								var basePath = $('#platformcontent').attr(
										'platformBasePath');
								backupStr = '<a class="glyphicon glyphicon-camera backup" url="'
										+ basePath
										+ 'instance/createsnapshot?rsid='
										+ vmuuid
										+ '&rstype=instance&rsname='
										+ vmName
										+ '"></a>';
							}
							if (publicip != "") {
								publicip = '<a><span class="glyphicon glyphicon-globe"></span>&nbsp;&nbsp;'
										+ publicip + '</a>';
							}
							var thistr = '<tr rowid="'
									+ vmuuid
									+ '"><td class="rcheck"><input type="checkbox" name="vmrow"></td><td name="console">'
									+ showstr + '</td><td name="vmname">' + vmName
									+ '</td>' + stateStr + '<td name="cpuCore">'
									+ cpu + '</td><td name="memoryCapacity">'
									+ memory + '</td><td name="sip">' + network
									+ '</td><td name="pip">' + publicip
									+ '</td><td name="backuptime" class="time">'
									+ backupStr
									+ '</td><td name="createtime" class="time">'
									+ decodeURIComponent(obj.createdate)
									+ '</td></tr>';
							tableStr += thistr;
						}
						$('#tablebodyvm').html(tableStr);
						
						var volumelist = array.volumelist;
						 for (var i = 1; i < volumelist.length; i++) {
				                var obj = volumelist[i];
				                var volumeid = obj.volumeid;
				                var volumename = decodeURIComponent(obj.volumename);
				                var volumedepen = obj.volumedepen;
				                var depenname = decodeURIComponent(obj.depenname);
				                var volState = obj.volState;
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
				                var volumesize = obj.volumesize.toFixed(2);
				                var createdate = obj.createdate;
				                var backupdate = obj.backupdate;
				                var backupStr = decodeURIComponent(backupdate);
				                if (backupdate == "") {
				                    var basePath = $('#platformcontent').attr('platformBasePath');
				                    backupStr = '<a class="glyphicon glyphicon-camera backup" url="' + basePath + 'user/create/createsnapshot.jsp?rsid=' + volumeid + '&rstype=volume&rsname=' + volumename + '"></a>';
				                }
				                if (depenname != "") {
				                    depenname = '<a><span class="glyphicon glyphicon-cloud"></span>&nbsp;&nbsp;' + depenname + '</a>';
				                }
				                var showid = "vol-" + volumeid.substring(0, 8);
				                var showstr = "<a class='id'>" + showid + '</a>';
				                tableStr = tableStr + '<tr rowid="' + volumeid + '"><td class="rcheck"><input type="checkbox" name="volumerow"></td><td>'
				                    + showstr + '</td><td name="volumename">' + volumename + '</td>'
				                    + usedStr + '<td vmuuid="' + volumedepen + '">' + depenname + '</td><td name="size">'
				                    + volumesize + '</th><td name="backuptime" class="time">' + backupStr + '</td><td name="createtime" class="time">'
				                    + decodeURIComponent(createdate) + '</td></tr>';
				            }
				            $('#tablebodyvol').html(tableStr);
				            
				            
				            var eiplist = array.eiplist;
				            for (var i = 1; i < eiplist.length; i++) {
				                var obj = eiplist[i];
				                var eipIp = obj.eipIp;
				                var eipId = obj.eipId;
				                var eipName = decodeURIComponent(obj.eipName);
				                var eipDepen = obj.eipDepen;
				                var depenType = obj.depenType;
				                var depenName = decodeURIComponent(obj.depenName);
				                var isused = obj.isused;
				                var usedStr = "";
				                var iconStr = "cloud";
				                var typeStr = 'type="vm"';
				                if (depenName != "") {
				                    if (1 == depenType) {
				                        iconStr = 'random';
				                        typeStr = 'type="lb"';
				                    } else if (2 == depenType) {
				                        iconStr = 'inbox';
				                        typeStr = 'type="db"';
				                    } else if (3 == depenType) {
				                        iconStr = 'fullscreen';
				                        typeStr = 'type="rt"';
				                    }
				                    depenName = '<a><span class="glyphicon glyphicon-' + iconStr + '"></span>&nbsp;&nbsp;' + depenName + '</a>';
				                }
				                if (isused) {
				                    usedStr = usedStr + '<td><span class="icon-status icon-using" name="stateicon"></span><span name="stateword">已分配</span></td>';
				                } else {
				                    usedStr = usedStr + '<td><span class="icon-status icon-running" name="stateicon"></span><span name="stateword">可用</span></td>';
				                }
				                var eipBandwidth = obj.eipBandwidth;
				                var createDate = obj.createDate;
				                var thistr = '<tr eip="' + eipIp + '" eipId="' + eipId + '"><td class="rcheck"><input type="checkbox" name="eiprow"></td><td><a class="id">eip-' + eipId.substring(0, 8) + '</a></td><td name="eipname">'
				                    + eipName + '</td><td>'
				                    + eipIp + '</td>' + usedStr + '<td vmuuid="' + eipDepen + '" ' + typeStr + '>' + depenName + '</td><td name="bandwidth">'
				                    + eipBandwidth + '</td><td>电信</td><td name="createdate" class="time">' + decodeURIComponent(createDate) + '</td></tr>';
				                tableStr += thistr;
				            }
				            $('#tablebodyeip').html(tableStr);
					}
				}
	});
}
