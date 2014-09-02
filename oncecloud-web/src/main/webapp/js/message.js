$(function () {
    connect();
});

$(window).unload(function() {
	disconnect();
});

function connect() {
    if ('WebSocket' in window) {
        console.log('Websocket supported');
        socket = new WebSocket('ws://localhost:8080/messagingService');
        console.log('Connection attempted');

        socket.onopen = function () {
            console.log('Connection open!');
        };

        socket.onclose = function () {
            console.log('Disconnecting connection');
        };

        socket.onmessage = function (event) {
            console.log(event.data);
            var obj = JSON.parse(event.data);
            console.log('message type: ' + obj.messageType);
            console.log('content: ' + obj.content);
            eval("(" + obj.messageType + "(" + obj + ")" + ")");
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
    $.sticky(obj.content);
}

function ws_delete_row(obj) {
    var thistr = $('#tablebody').find('[rowid="' + obj.rowId + '"]');
    $(thistr).remove();
}

function ws_edit_row_console(rowId, option) {
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

function ws_edit_row_ip(rowId, network, ip) {
    var thistr = $('#tablebody').find('[rowid="' + obj.rowId + '"]');
    if (thistr.size() == 1) {
        thistr.find('[name="sip"]').html('<a>(' + obj.network + ')&nbsp;/&nbsp;' + obj.ip + '</a>');
    }
}

function ws_edit_row_for_bind_volume(rowId, vmId, vmName) {
    var thistr = $('#tablebody').find('[rowid="' + obj.rowId + '"]');
    if (thistr.size() == 1) {
        thistr.find('[vmuuid]').attr('vmuuid', obj.vmId).html('<a><span class="glyphicon glyphicon-cloud"></span>&nbsp;&nbsp;' + obj.vmName + '</a>');
    }
}

function ws_edit_row_for_unbind_volume(rowId) {
    var thistr = $('#tablebody').find('[rowid="' + obj.rowId + '"]');
    if (thistr.size() == 1) {
        thistr.find('[vmuuid]').attr('vmuuid', "").text("");
    }
}

function ws_edit_row_status(rowId, icon, word) {
    var thistr = $('#tablebody').find('[rowid="' + obj.rowId + '"]');
    if (thistr.size() == 1) {
        thistr.find('[name="stateicon"]').removeClass();
        thistr.find('[name="stateicon"]').addClass("icon-status");
        thistr.find('[name="stateicon"]').addClass("icon-" + obj.icon);
        thistr.find('[name="stateword"]').text(obj.word);
    }
}