package com.oncecloud.message;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author hehai
 * @version 2013/12/25
 */
@Component
public class MessagePush {
	private final static Logger logger = Logger.getLogger(MessagePush.class);
	
	private MessageWebSocketHandler messageWebSocketHandler;

	private MessageWebSocketHandler getMessageWebSocketHandler() {
		return messageWebSocketHandler;
	}

	@Autowired
	private void setMessageWebSocketHandler(
			MessageWebSocketHandler messageWebSocketHandler) {
		this.messageWebSocketHandler = messageWebSocketHandler;
	}

	public void pushMessage(int userId, String content) {
		logger.info("User [" + userId + "] Content [" + content + "]");
		this.getMessageWebSocketHandler().sendMessageToUser(userId,
				new StickyMessage(content));
	}

	public void pushMessageClose(int userId, String content, String conid) {
		this.getMessageWebSocketHandler().sendMessageToUser(userId,
				new StickyMessageClose(content, conid));
	}

	public void deleteRow(int userId, String rowId) {
		logger.info("User [" + userId + "] RowId [" + rowId + "]");
		this.getMessageWebSocketHandler().sendMessageToUser(userId,
				new DeleteRowMessage(rowId));
	}

	public void editRowStatus(int userId, String rowId, String icon, String word) {
		logger.info("User [" + userId + "] RowId [" + rowId + "] Icon [" + icon + "] Word [" + word + "]");
		this.getMessageWebSocketHandler().sendMessageToUser(userId,
				new EditRowStatusMessage(rowId, icon, word));
	}

	public void editRowStatusForUnbindVolume(int userId, String rowId) {
		this.getMessageWebSocketHandler().sendMessageToUser(userId,
				new EditRowStatusForUnbindVolumeMessage(rowId));
	}

	public void editRowStatusForBindVolume(int userId, String rowId,
			String vmId, String vmName) {
		this.getMessageWebSocketHandler().sendMessageToUser(userId,
				new EditRowStatusForBindVolumeMessage(rowId, vmId, vmName));
	}

	public void editRowIP(int userId, String rowId, String network, String ip) {
		this.getMessageWebSocketHandler().sendMessageToUser(userId,
				new EditRowIPMessage(rowId, network, ip));
	}

	public void editRowConsole(int userId, String rowId, String option) {
		this.getMessageWebSocketHandler().sendMessageToUser(userId,
				new EditRowConsoleMessage(rowId, option));
	}
}
