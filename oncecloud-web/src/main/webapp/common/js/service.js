$(document).ready(function() {
	var options = {
		bootstrapMajorVersion: 3,
		currentPage: 1,
		totalPages: 1,
		numberOfPages: 0,
		onPageClicked: function(e, originalEvent, type, page) {
			var limit = $('#limit').val();
			var search = $('#search').val();
			getQuestionList(page, limit, search);
		},
		shouldShowPage:function(type, page, current) {
			switch(type) {
				case "first":
				case "last":
					return false;
				default:
					return true;
			}
		}
	}

	$('#apply').on('click', function(event) {
		event.preventDefault();
		$('#ServiceModalContainer').load($(this).attr('url'), '', function(){
			$('#ServiceModalContainer').modal({
				backdrop:false,
				show:true
			});
		});
	});
	$('#pageDivider').bootstrapPaginator(options);
	getQuestionList(1, 10, "");
	
	$('#limit').on('focusout', function() {
		var limit = $('#limit').val();
		var reg = /^[0-9]*[1-9][0-9]*$/;
		if (!reg.test(limit)) {
		    $("#limit").val(10);
		}
		reloadList();
	});

	$('.btn-refresh').on('click', function(event) {
		event.preventDefault();
		reloadList();
	});

	$('#search').on('focusout', function(){
		reloadList();
	});
	
	$('#search').keypress(function (e) {
		var key = e.which;
		if (key == 13) {
			reloadList();
		}
	});
	
	function reloadList() {
		var limit = $('#limit').val();
		var search = $('#search').val();
		getQuestionList(1, limit, search);
		options = {
			currentPage: 1
		}
		$('#pageDivider').bootstrapPaginator(options);
	}

	function getQuestionList(page, limit, search) {
		$('#tablebody').html("");
		$.ajax({
			type: 'post',
			url: '/QAAction',
			data: "action=getquestion&page="+page+"&limit="+limit+"&search="+search,
			dataType: 'json',
			success: function(array) {
				var totalnum = array[0];
				var totalp = 1;
				if (totalnum != 0) {
					totalp = Math.ceil(totalnum/limit);
				}
				options = {
					totalPages: totalp
				}
				$('#pageDivider').bootstrapPaginator(options);
				pageDisplayUpdate(page, totalp);
				var tableStr = "";
				for(var i = 1; i < array.length; i++) {
					var obj = array[i];
					var qaId = obj.qaId;
					var qaTitle = decodeURI(obj.qaTitle);
					var qaSummary = decodeURI(obj.qaSummary);
					var qaStatus = obj.qaStatus;
					var qaReply = obj.qaReply;
					var qaTime = obj.qaTime;
					var qaStatusStr = '<td></td>';
					var qaCloseStr = '<td class="close-td"></td>';
					if (qaStatus == 1) {
						if (qaReply > 0) {
							qaStatusStr = '<td state="process"><span class="icon-status icon-running" name="stateicon"></span><span name="stateword">处理中</span></td>';
						} else {
							qaStatusStr = '<td state="create"><span class="icon-status icon-using" name="stateicon"></span><span name="stateword">新建</span></td>';
						}
						qaCloseStr = '<td class="close-td"><a href="javascript:void(0)" class="qa-close">关闭</a></td>';
					} else if (qaStatus == 0) {
						qaStatusStr = '<td state="close"><span class="icon-status icon-close" name="stateicon"></span><span name="stateword">已关闭</span></td>';
					}
					var level = $('#platformcontent').attr("userLevel");
					if (level > 0) {
						tableStr = tableStr + '<tr qaId="'+qaId+'"><td><a class="view-detail">'+decodeURI(qaTitle)+'</a></td>'
							+'<td>'+decodeURI(qaSummary)+'</td><td>'+qaReply+'</td>'+qaStatusStr
							+'<td class="time">'+qaTime+'</td>'+qaCloseStr+'</tr>';
					} else {
						var qaUser = obj.qaUserName;
						tableStr = tableStr + '<tr qaId="'+qaId+'"><td><a class="view-detail">'+decodeURI(qaTitle)+'</a></td>'
							+'<td>'+qaUser+'</td><td>'+decodeURI(qaSummary)+'</td><td>'+qaReply+'</td>'+qaStatusStr
							+'<td class="time">'+qaTime+'</td>'+qaCloseStr+'</tr>';
					}
				}
				$('#tablebody').html(tableStr);
			},
			error: function() {}
		});
	}

	function closeQuestion(id) {
		$.ajax({
			type: 'get',
			url: '/QAAction',
			data: "action=closequestion&qaid="+id,
			dataType: 'json',
			success: function(array) {
				if (array.length == 1) {
					var result = array[0].isSuccess;
					if (result == true) {
						var thistr = $("#tablebody").find('[qaId="'+id+'"]');
						var thistd = thistr.find('[state]');
						thistd.find('[name="stateicon"]').removeClass('icon-using');
						thistd.find('[name="stateicon"]').removeClass('icon-running');
						thistd.find('[name="stateicon"]').addClass('icon-close');
						thistd.find('[name="stateword"]').text('已关闭');
						thistd.attr("state", "close");
						var closetd = thistr.find('.close-td');
						closetd.html("");
					}
				}
			},
			error: function() {}
		});
	}

	function pageDisplayUpdate(current, total) {
		$('#currentPS').html(current);
		$('#totalPS').html(total);
	}

	$('#tablebody').on('click', '.qa-close', function(event) {
		event.preventDefault();
		var qaId = $(this).parent().parent().attr("qaId");
		bootbox.dialog({
			message: '<div class="alert alert-info" style="margin:10px"><span class="glyphicon glyphicon-info-sign"></span>&nbsp;关闭表单&nbsp;?</div>',
			title: "提示",
			buttons: {
				main: {
					label: "确定",
					className: "btn-primary",
					callback: function() {
						closeQuestion(qaId);
					}
				},
				cancel: {
					label: "取消",
					className: "btn-default",
					callback: function() {
					}
				}
			}
		});
	});

	$('#tablebody').on('click', '.view-detail', function(event) {
		event.preventDefault();
		var qaId = $(this).parent().parent().attr("qaId");
		var basePath = $("#platformcontent").attr("platformBasePath");
		$.ajax({
			type: 'get',
			url: '/QAAction',
			data: "action=detail&qaid="+qaId,
			dataType: 'text',
			success: function(response) {
				window.location.href = basePath + "common/detail/servicedetail.jsp";
			},
			error: function() {}
		});
	});
});