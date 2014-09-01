<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>MessageTest</title>
</head>
<body>
	<script type="text/javascript">
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
					console.log('content: ' + obj.content)
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
	</script>
</body>
</html>