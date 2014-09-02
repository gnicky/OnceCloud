$(function(){
	connect();
});

        function connect() {
			if ('WebSocket' in window) {
				console.log('Websocket supported');
				socket = new WebSocket('ws://localhost:8080/messagingService');
				console.log('Connection attempted');

				socket.onopen = function() {
					console.log('Connection open!');
				}

				socket.onclose = function() {
					console.log('Disconnecting connection');
				}

				socket.onmessage = function(event) {
					console.log(event.data);
					var obj = JSON.parse(event.data);
					console.log('message type: ' + obj.messageType);
					console.log('content: ' + obj.content);
					eval("("+ obj.messageType +"(" + obj.content + ")"+")");
				}

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
				'message' : message
			}));
		}
		
		function ws_sticky(content)
		{
			
		}
		
		function ws_delete_row(rowId)
		{
			
		}
		
		function ws_edit_row_console(rowId,option)
		{
			
		}
		
		function ws_edit_row_ip(rowId,network,ip)
		{
			
		}
		
		function ws_edit_row_for_bind_volume(rowId,vmId,vmName)
		{
			
		}
		
		function ws_edit_row_for_unbind_volume(rowId)
		{
			
		}
		
		function ws_edit_row_status(rowId,icon,word)
		{
			
		}