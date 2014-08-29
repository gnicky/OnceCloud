reloadList(1);

function reloadList(page) {
	var limit = $('#limit').val();
	var search = $('#search').val();
	getSRList(page, limit, search);
	if (page == 1) {
		options = {
			currentPage : 1
		}
		$('#pageDivider').bootstrapPaginator(options);
	}
}

function removeAllCheck() {
	var boxes = document.getElementsByName("srrow");
	for (var i = 0; i < boxes.length; i++) {
		boxes[i].checked = false;
		$(boxes[i]).change();
	}
}

$('#tablebody').on('change', 'input:checkbox', function(event) {
			var status = $(this).get(0).checked;
			$('input[name="srrow"]').attr('checked', false);
			$(this).get(0).checked = status;
			if (status == true) {
				$("#load").attr('disabled', false).removeClass('btn-forbidden');
				$('#update').attr('disabled', false)
						.removeClass('btn-forbidden');
				if ($(this).attr('srsize') == 0) {
					$("#delete").attr('disabled', false)
							.removeClass('btn-forbidden');
				} else {
					$("#delete").attr('disabled', true)
							.addClass('btn-forbidden');

				}
			} else {
				$("#delete").attr('disabled', true).addClass('btn-forbidden');
				$("#load").attr('disabled', true).addClass('btn-forbidden');
				$("#update").attr('disabled', true).addClass('btn-forbidden');
			}
		});
		
$('#create').on('click', function(event) {
			event.preventDefault();
			$('#SRModalContainer').attr('type', 'new');
			$('#SRModalContainer').load($(this).attr('url'), '', function() {
						$('#SRModalContainer').modal({
									backdrop : false,
									show : true
								});
					});
		});

$('#update').on('click', function(event) {
			event.preventDefault();
			$('#SRModalContainer').attr('type', 'edit');
			$('#SRModalContainer').load($(this).attr('url'), '', function() {
						$('#SRModalContainer').modal({
									backdrop : false,
									show : true
								});
					});
		});

$('#load').on('click', function(event) {
			event.preventDefault();
			if ($(this).attr('disabled') == "disabled") {
				return;
			}
			$('#SRModalContainer').load($(this).attr('url'), '', function() {
						$('#SRModalContainer').modal({
									backdrop : false,
									show : true
								});
					});
		});

function getSRList(page, limit, searchstr) {
	$.ajax({
		type : 'get',
		url : '/SRAction',
		data : {action:"getlist", page:page, limitnum:limit, search:searchstr},
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
					var srid = obj.srid;
					var srname = decodeURI(obj.srname);
					var srDesc = decodeURI(obj.srDesc);
					var srAddress = obj.srAddress;
					var createDate = obj.createDate;
					var srType = obj.srType;
					var srDir = obj.srDir;
					var showid = "sr-" + srid.substring(0, 8);
					var statestr = "";
					var srsize = obj.srsize;
					var rackid = obj.rackid;
					var rackname = decodeURI(obj.rackname);

					tableStr = tableStr
							+ '<tr srid="'
							+ srid
							+ '" srname="'
							+ srname
							+ '" srtype="'
							+ srType
							+ '" srdesc="'
							+ srDesc
							+ '" rackid="'
							+ rackid
							+ '" ><td class="rcheck"><input type="checkbox" name="srrow" srsize='
							+ srsize + '></td>' + '<td><a>' + showid
							+ '</a></td><td>' + srname + '</td><td>'
							+ srAddress + '</td><td>' + srDir + '</td><td><a>'
							+ srType.toUpperCase() + '</a></td><td>' + rackname
							+ '</td><td class="time">' + createDate
							+ '</td></tr>';
				}
				btable.innerHTML = tableStr;
			}
		}
	});
}

$('#delete').on('click', function(event) {
	event.preventDefault();
	if ($(this).attr('disabled') == "disabled") {
		return;
	}
	var boxes = document.getElementsByName("srrow");
	var infoList = "";
	for (var i = 0; i < boxes.length; i++) {
		if (boxes[i].checked == true) {
			infoList += "[" + $(boxes[i]).parent().parent().attr("srname")
					+ "]&nbsp;";
		}
	}
	bootbox.dialog({
		message : '<div class="alert alert-info" style="margin:10px"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;删除存储&nbsp;'
				+ infoList + '?</div>',
		title : "提示",
		buttons : {
			main : {
				label : "确定",
				className : "btn-primary",
				callback : function() {
					for (var i = 0; i < boxes.length; i++) {
						if (boxes[i].checked == true) {
							deletesr(
									$(boxes[i]).parent().parent().attr("srid"),
									$(boxes[i]).parent().parent()
											.attr("srname"));
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

function deletesr(srid, srname) {
	$.ajax({
				type : 'post',
				url : '/SRAction',
				data : 'action=delete&srid=' + srid + '&srname=' + srname,
				dataType : 'json',
				success : function(array) {
					if (array.length == 1) {
						var result = array[0].result;
						if (result) {
							var thistr = $("#tablebody").find('[srid="' + srid
									+ '"]');
							$(thistr).remove();
						}
					}
					removeAllCheck();
				}
			});
}


$('#load').on('click', function(event) {
			event.preventDefault();
			$('#SRModalContainer').load($(this).attr('url'), '', function() {
						$('#SRModalContainer').modal({
									backdrop : false,
									show : true
								});
					});
		});

$('#unload').on('click', function(event) {
	event.preventDefault();
	var boxes = document.getElementsByName("srrow");
	var infoList = "";
	for (var i = 0; i < boxes.length; i++) {
		if (boxes[i].checked == true) {
			infoList += "[" + $(boxes[i]).parent().parent().attr("srname")
					+ "]&nbsp;";
		}
	}
	bootbox.dialog({
		className : "bootbox-large",
		message : '<div class="alert alert-info" style="margin:10px"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;从机架上卸载&nbsp;'
				+ infoList + '?</div>',
		title : "提示",
		buttons : {
			main : {
				label : "确定",
				className : "btn-primary",
				callback : function() {
					for (var i = 0; i < boxes.length; i++) {
						if (boxes[i].checked == true) {
							var userid = document
									.getElementById("platformcontent")
									.getAttribute("platformUserId");
							unbind(	$(boxes[i]).parent().parent()
											.attr("volumeuuid"), userid);
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
