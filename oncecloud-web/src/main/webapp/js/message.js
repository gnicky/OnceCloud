$(function () {
    connect();
});

$(window).unload(function() {
	disconnect();
});

function connect() {
    if ('WebSocket' in window) {
        console.log('Websocket supported');
        var host = window.location.host;
        socket = new WebSocket('ws://' + host + '/messagingService');
        console.log('Connection attempted');

        socket.onopen = function () {
            console.log('Connection open');
        };

        socket.onclose = function () {
            console.log('Disconnecting connection');
        };

        socket.onmessage = function (event) {
            console.log(event.data);
            var obj = JSON.parse(event.data);
            console.log('message type: ' + obj.messageType);
            console.log('content: ' + obj.content);
            eval("(" + obj.messageType + "(obj)" + ")");
        };

    } else {
        console.log('Websocket not supported');
    }
}

function disconnect() {
    socket.close();
    console.log("Disconnected");
}

function send(message) {
    socket.send(JSON.stringify({
        'message': message
    }));
}

function ws_sticky(obj) {
    $.sticky(obj.content,{'autoclose':false});
}

function ws_delete_row(obj) {
    var thistr = $('#tablebody').find('[rowid="' + obj.rowId + '"]');
    $(thistr).remove();
}

function ws_edit_row_console(obj) {
    var thistr = $('#tablebody').find('[rowid="' + obj.rowId + '"]');
    if (thistr.size() == 1) {
        if (obj.option == "add") {
            var thistd = thistr.find('[name="console"]');
            if (thistd.find('.console').size() == 0) {
                thistd.append('<a class="console" data-uuid='
                    + obj.rowId + '><img src="../img/user/console.png"></a>');
            }
        } else {
            thistr.find('.console').remove();
        }
    }
}

function ws_edit_row_ip(obj) {
    var thistr = $('#tablebody').find('[rowid="' + obj.rowId + '"]');
    if (thistr.size() == 1) {
        thistr.find('[name="sip"]').html('<a>(' + obj.network + ')&nbsp;/&nbsp;' + obj.ip + '</a>');
    }
}

function ws_edit_row_for_bind_volume(obj) {
    var thistr = $('#tablebody').find('[rowid="' + obj.rowId + '"]');
    if (thistr.size() == 1) {
        thistr.find('[vmuuid]').attr('vmuuid', obj.vmId).html('<a><span class="glyphicon glyphicon-cloud"></span>&nbsp;&nbsp;' + obj.vmName + '</a>');
    }
}

function ws_edit_row_for_unbind_volume(obj) {
    var thistr = $('#tablebody').find('[rowid="' + obj.rowId + '"]');
    if (thistr.size() == 1) {
        thistr.find('[vmuuid]').attr('vmuuid', "").text("");
    }
}

function ws_edit_row_status(obj) {
    var thistr = $('#tablebody').find('[rowid="' + obj.rowId + '"]');
    if (thistr.size() == 1) {
        thistr.find('[name="stateicon"]').removeClass();
        thistr.find('[name="stateicon"]').addClass("icon-status");
        thistr.find('[name="stateicon"]').addClass("icon-" + obj.icon);
        thistr.find('[name="stateword"]').text(obj.word);
    }
}


var arrayid ="";

///展示一个提示
function showMessageNoAutoClose(content) {
   var showid = $.sticky(content,{'autoclose':false});
   arrayid = $.cookie('alertStr');
   if(arrayid==null)
   {
      arrayid="";
   }
   setTimeout("",500);
   arrayid+=$("#"+showid.id)[0].outerHTML+"---";
   alert(arrayid);
   $.cookie('alertStr',arrayid);
   return showid.id;
}

///根据id，关闭对应的提示
function hideMessageNoAutoClose(objId) {
	$("#"+objId).dequeue().slideUp('fast', function(){
		var closest = $(this).closest('.sticky-queue');
		var elem = closest.find('.sticky');
		
		arrayid = $.cookie('alertStr');
		var newstr= $("#"+objId)[0].outerHTML +"---";
		var indexi = arrayid.indexOf(newstr);
		var newcookie =arrayid.substring(0,indexi) + arrayid.substring(indexi+newstr.length);
		$.cookie('alertStr',newcookie);
		
		$(this).remove();
		if(elem.length == '1'){
			closest.remove()
		}
	});
}

///新页面中，还原没有关闭的提示
$(function(){
	arrayid = $.cookie('alertStr');
	if(arrayid!="" && arrayid !=null)
	{
		var words = arrayid.split('---');
		for(var i=0;i<words.length -1 ;i++)
			{
			    
				var position = 'top-right'; 
				// Make sure the sticky queue exists
				if(!$('body').find('.sticky-queue').html())
					{ $('body').append('<div class="sticky-queue ' + position + '"></div>'); }
				
				// Building and inserting sticky note
				$('.sticky-queue').prepend(words[i]);
				
				
			}
		$(".sticky").css('height', 38);
		
	}
})