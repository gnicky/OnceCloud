package com.oncecloud.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebMvc
@EnableWebSocket
public class MessageConfig extends WebMvcConfigurerAdapter implements
		WebSocketConfigurer {
	private MessageWebSocketHandler messageWebSocketHandler;
	private MessageHandshakeInterceptor messageHandshakeInterceptor;

	private MessageWebSocketHandler getMessageWebSocketHandler() {
		return messageWebSocketHandler;
	}

	@Autowired
	private void setMessageWebSocketHandler(
			MessageWebSocketHandler messageWebSocketHandler) {
		this.messageWebSocketHandler = messageWebSocketHandler;
	}

	private MessageHandshakeInterceptor getMessageHandshakeInterceptor() {
		return messageHandshakeInterceptor;
	}

	@Autowired
	private void setMessageHandshakeInterceptor(
			MessageHandshakeInterceptor messageHandshakeInterceptor) {
		this.messageHandshakeInterceptor = messageHandshakeInterceptor;
	}

	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(this.getMessageWebSocketHandler(),
				"/messagingService").addInterceptors(
				this.getMessageHandshakeInterceptor());
		registry.addHandler(this.getMessageWebSocketHandler(),
				"/sockjs/messagingService")
				.addInterceptors(this.getMessageHandshakeInterceptor())
				.withSockJS();
	}

}