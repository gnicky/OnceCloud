package com.oncecloud.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author hehai
 * @version 2013/12/25
 */
@Component
public class MessagePush {
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
		this.getMessageWebSocketHandler().sendMessageToUser(userId,
				new StickyMessage(content));
	}

	public void deleteRow(int userId, String rowId) {
		this.getMessageWebSocketHandler().sendMessageToUser(userId,
				new DeleteRowMessage(rowId));
	}

	public void editRowStatus(int userId, String rowId, String icon, String word) {
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
