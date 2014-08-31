reloadList(1);

function reloadList(page) {
    var limit = $('#limit').val();
    var search = $('#search').val();
    getHostList(page, limit, search);
    if (page == 1) {
        options = {
            currentPage: 1
        }
        $('#pageDivider').bootstrapPaginator(options);
    }
    allDisable();
}

function allDisable() {
	$("#update").addClass('btn-forbidden');
	$("#delete").addClass('btn-forbidden');
	$("#add2pool").addClass('btn-forbidden');
	$("#remove4pool").addClass('btn-forbidden');
}

function removeAllCheck() {
	var boxes = document.getElementsByName("hostrow");
	for (var i = 0; i < boxes.length; i++) {
		boxes[i].checked = false;
		$(boxes[i]).change();
	}
}

$('#tablebody').on('change', 'input:checkbox', function(event) {
	event.preventDefault();
	allDisable();
	var bindcount = 0;
	var unbindcount = 0;
	var count = 0;
	$('input[name="hostrow"]:checked').each(function() {
				var state = $(this).parent().parent().find('[state]')
						.attr('state');
				if (state == 'load') {
					bindcount++;
				} else {
					unbindcount++;
				}
				count++;
			});
	if (count == 1) {
		$('#update').removeClass('btn-forbidden');
		if (count == bindcount) {
			$("#remove4pool").removeClass('btn-forbidden');
		} else if (count == unbindcount) {
			$("#add2pool").removeClass('btn-forbidden');
			$("#delete").removeClass('btn-forbidden');
		}
	}
});

$('#create').on('click', function(event) {
			event.preventDefault();
			$('#ServerModalContainer').attr('type', 'new');
			$('#ServerModalContainer').load($(this).attr('url'), '',
					function() {
						$('#ServerModalContainer').modal({
									backdrop : false,
									show : true
								});
					});
		});

$('#update').on('click', function(event) {
			event.preventDefault();
			$('#ServerModalContainer').attr('type', 'edit');
			$('#ServerModalContainer').load($(this).attr('url'), '',
					function() {
						$('#ServerModalContainer').modal({
									backdrop : false,
									show : true
								});
					});
		});


$('#tablebody').on('click', '.srdetail', function(event) {
	event.preventDefault();
	var hostuuid = $(this).parent().parent().attr('hostid');
	var hostname = $(this).parent().parent().attr('hostname');
	var url = $("#platformcontent").attr('platformBasePath')
			+ 'admin/modal/storageofhost.jsp';
	$('#ServerModalContainer').load(url, {
				"hostuuid" : hostuuid,
				"hostname" : hostname
			}, function() {
				$('#ServerModalContainer').modal({
							backdrop : false,
							show : true
						});
			});
});

$('#tablebody').on('click', '.id', function(event) {
	event.preventDefault();
	var hostid = $(this).parent().parent().attr('hostid');
	$.ajax({
				type : 'get',
				url : '/HostAction',
				data : {action:"detail", hostid:hostid},
				dataType : 'text',
				success : function(response) {
					window.location.href = $('#platformcontent')
							.attr('platformBasePath')
							+ "admin/detail/hostdetail.jsp";
				}
			});
});

$('#add2pool').on('click', function(event) {
	event.preventDefault();
	var boxes = document.getElementsByName("hostrow");
	var count = 1;
	var uuidStr = '[';
	var teshu = '['
	for (var i = 0; i < boxes.length; i++) {
		if (boxes[i].checked == true) {
			var hostuuid = $(boxes[i]).parent().parent().attr('hostid');
			if (count == 1) {
				uuidStr = uuidStr + '"' + hostuuid + '"';
				count++;
			} else {
				uuidStr = uuidStr + ',"' + hostuuid + '"';
			}
		}
	}
	uuidStr = uuidStr + ']';
	$.ajax({
		type : 'get',
		url : '/HostAction',
		data : {action:"issamesr", uuidjsonstr:uuidStr},
		dataType : 'json',
		success : function(array) {
			var obj1 = array[0];
			array.shift();
			var url = $("#platformcontent").attr('platformBasePath')
					+ 'admin/modal/addtopool.jsp';
			if (obj1.isSuccess) {
				uuidStr.replace(new RegExp('"', "gm"), '_');
				$('#ServerModalContainer').load(url, {
							"uuidjsonstr" : uuidStr
						}, function() {
							$('#ServerModalContainer').modal({
										backdrop : false,
										show : true
									});
						});
			} else {
				bootbox.dialog({
					message : '<div class="alert alert-info" style="margin:10px"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;组建资源池的服务器存储必须一致&nbsp;</div>',
					title : "提示",
					buttons : {
						main : {
							label : "确定",
							className : "btn-primary",
							callback : function() {
								removeAllCheck();
							}
						},
						cancel : {
							label : "取消",
							className : "btn-default",
							callback : function() {
								removeAllCheck();
							}
						}
					}
				});
			}
		}
	});
});

$('#remove4pool').on('click', function(event) {
	event.preventDefault();
	var boxes = document.getElementsByName("hostrow");
	var infoList = "";
	for (var i = 0; i < boxes.length; i++) {
		if (boxes[i].checked == true) {
			infoList += "[" + $(boxes[i]).parent().parent().attr("hostname")
					+ "]&nbsp;";
		}
	}
	bootbox.dialog({
		message : '<div class="alert alert-info" style="margin:10px"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;服务器&nbsp;'
				+ infoList + '离开资源池?</div>',
		title : "提示",
		buttons : {
			main : {
				label : "确定",
				className : "btn-primary",
				callback : function() {
					for (var i = 0; i < boxes.length; i++) {
						if (boxes[i].checked == true) {
							ejectPool($(boxes[i]).parent().parent()
									.attr("hostid"));
						}
					}
					removeAllCheck();
				}
			},
			cancel : {
				label : "取消",
				className : "btn-default",
				callback : function() {
					removeAllCheck();
				}
			}
		}
	});
});

function ejectPool(hostid) {
	$.ajax({
				type : 'post',
				url : '/HostAction',
				data : {action:"r4pool", hostuuid:hostid},
				dataType : 'json',
				success : function(array) {
					var obj = array[0];
					if (obj.isSuccess) {
						var thistr = $("#tablebody").find('[hostid="' + hostid
								+ '"]');
						var thistd = thistr.find('[state]');
						thistd.html("<a></a>");
						thistd.attr("state", "unload");
					}
				}
			});
}

function getHostList(page, limit, search) {
	$.ajax({
		type : 'get',
		url : '/HostAction/HostList',
		data : {page:page, limit:limit, search:search},
		dataType : 'json',
		success : function(array) {
			if (array.length >= 1) {
				var totalnum = array[0];
				var totalp = Math.ceil(totalnum / limit);
				options = {
					totalPages : totalp
				}
				$('#pageDivider').bootstrapPaginator(options);
				pageDisplayUpdate(page, totalp);
				var btable = document.getElementById("tablebody");
				var tableStr = "";
				for (var i = 1; i < array.length; i++) {
					var obj = array[i];
					var hostname = decodeURI(obj.hostname);
					var hostdesc = decodeURI(obj.hostdesc);
					var hostid = obj.hostid;
					var hostip = obj.hostip;
					var createdate = obj.createdate;
					var hostid = obj.hostid;
					var hostcpu = obj.hostcpu;
					var hostmem = obj.hostmem;
					var rackUuid = obj.rackUuid;
					var rackName = decodeURI(obj.rackName);
					var showid = "host-" + hostid.substring(0, 8);
					var srsize = obj.srsize;
					var poolid = obj.poolid;
					var state = "load";
					var showpoolid = '';
					if (poolid == "") {
						state = "unload";
					}
					var poolname = decodeURI(obj.poolname);
					var mytr = '<tr hostid="'
							+ hostid
							+ '" hostname="'
							+ hostname
							+ '" hostip="'
							+ hostip
							+ '" hostdesc="'
							+ hostdesc
							+ '" rackid="'
							+ rackUuid
							+ '"><td class="rcheck"><input type="checkbox" name="hostrow"></td>'
							+ '<td><a class="id">'
							+ showid
							+ '</a></td><td>'
							+ hostname
							+ '</td><td>'
							+ hostip
							+ '</td><td>'
							+ hostcpu
							+ '&nbsp;核</td>'
							+ '<td>'
							+ hostmem
							+ '&nbsp;MB</td><td state="'
							+ state
							+ '"><a>'
							+ poolname
							+ '</a></td><td><a>'
							+ rackName
							+ '</a></td><td><a class="srdetail" size='
							+ srsize
							+ '><span class="glyphicon glyphicon-hdd"></span>&nbsp;&nbsp;'
							+ srsize + '</a></td><td class="time">'
							+ createdate + '</td></tr>';
					tableStr += mytr;
				}
				btable.innerHTML = tableStr;
			}
		}
	});
}

$('#delete').on('click', function(event) {
	event.preventDefault();
	var boxes = document.getElementsByName("hostrow");
	var infoList = "";
	for (var i = 0; i < boxes.length; i++) {
		if (boxes[i].checked == true) {
			infoList += "[" + $(boxes[i]).parent().parent().attr("hostname")
					+ "]&nbsp;";
		}
	}
	bootbox.dialog({
		message : '<div class="alert alert-info" style="margin:10px"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;删除服务器&nbsp;'
				+ infoList + '?</div>',
		title : "提示",
		buttons : {
			main : {
				label : "确定",
				className : "btn-primary",
				callback : function() {
					for (var i = 0; i < boxes.length; i++) {
						if (boxes[i].checked == true) {
							deleteHost($(boxes[i]).parent().parent()
											.attr("hostid"), $(boxes[i])
											.parent().parent().attr("hostname"));
						}
					}
					removeAllCheck();
				}
			},
			cancel : {
				label : "取消",
				className : "btn-default",
				callback : function() {
					removeAllCheck();
				}
			}
		}
	});
});

function deleteHost(hostid, hostname) {
	$.ajax({
				type : 'post',
				url : '/HostAction',
				data : {action:"delete", hostid:hostid, hostname:hostname},
				dataType : 'json',
				success : function(array) {
					if (array.length == 1) {
						var result = array[0].result;
						if (result) {
							var thistr = $("#tablebody").find('[hostid="'
									+ hostid + '"]');
							$(thistr).remove();
						}
					}
				}
			});
}