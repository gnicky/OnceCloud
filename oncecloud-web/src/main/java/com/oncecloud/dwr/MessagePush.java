package com.oncecloud.dwr;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import com.oncecloud.entity.User;

/**
 * @author hehai
 * @version 2013/12/25
 */
public class MessagePush {
	private static Logger logger = Logger.getLogger(MessagePush.class);

	public static void pushMessage(int userId, String pushContent) {
		ScriptBuffer script = new ScriptBuffer();
		script.appendCall("showMessage", pushContent);
		Collection<ScriptSession> pages = DwrSessionListener.manager
				.getAllScriptSessions();
		for (ScriptSession session : pages) {
			if (session.getAttribute("user") != null) {
				int userTargetId = ((User) session.getAttribute("user"))
						.getUserId();
				if (userTargetId == userId) {
					logger.debug("Push Message: User [" + userId
							+ "] Content [" + pushContent + "]");
					session.addScript(script);
				}
			}
		}
	}

	public static void deleteRow(int userId, String rowId) {
		ScriptBuffer script = new ScriptBuffer();
		script.appendCall("dwrDeleteRow", rowId);
		Collection<ScriptSession> pages = DwrSessionListener.manager
				.getAllScriptSessions();
		for (ScriptSession session : pages) {
			if (session.getAttribute("user") != null) {
				int userTargetId = ((User) session.getAttribute("user"))
						.getUserId();
				if (userTargetId == userId) {
					logger.debug("Push Message: User [" + userId
							+ "] Delete Row [" + rowId + "]");
					session.addScript(script);
				}
			}
		}
	}

	public static void editRowStatus(int userId, String rowId, String icon,
			String word) {
		ScriptBuffer script = new ScriptBuffer();
		script.appendCall("dwrEditRowStatus", rowId, icon, word);
		Collection<ScriptSession> pages = DwrSessionListener.manager
				.getAllScriptSessions();
		for (ScriptSession session : pages) {
			if (session.getAttribute("user") != null) {
				int userTargetId = ((User) session.getAttribute("user"))
						.getUserId();
				if (userTargetId == userId) {
					logger.debug("Push Message: User [" + userId
							+ "] Edit Row Status [" + rowId + "]");
					session.addScript(script);
				}
			}
		}
	}

	public static void editRowStatusForUnbindVolume(int userId, String rowId) {
		ScriptBuffer script = new ScriptBuffer();
		script.appendCall("dwrEditRowStatusForUnbindVolume", rowId);
		Collection<ScriptSession> pages = DwrSessionListener.manager
				.getAllScriptSessions();
		for (ScriptSession session : pages) {
			if (session.getAttribute("user") != null) {
				int userTargetId = ((User) session.getAttribute("user"))
						.getUserId();
				if (userTargetId == userId) {
					logger.debug("Push Message: User [" + userId
							+ "] Edit Row Status [" + rowId + "]");
					session.addScript(script);
				}
			}
		}
	}

	public static void editRowStatusForBindVolume(int userId, String rowId,
			String vmId, String vmName) {
		ScriptBuffer script = new ScriptBuffer();
		script.appendCall("dwrEditRowStatusForBindVolume", rowId, vmId, vmName);
		Collection<ScriptSession> pages = DwrSessionListener.manager
				.getAllScriptSessions();
		for (ScriptSession session : pages) {
			if (session.getAttribute("user") != null) {
				int userTargetId = ((User) session.getAttribute("user"))
						.getUserId();
				if (userTargetId == userId) {
					logger.debug("Push Message: User [" + userId
							+ "] Edit Row Status [" + rowId + "]");
					session.addScript(script);
				}
			}
		}
	}

	public static void editRowIP(int userId, String rowId, String network,
			String ip) {
		ScriptBuffer script = new ScriptBuffer();
		script.appendCall("dwrEditRowIP", rowId, network, ip);
		Collection<ScriptSession> pages = DwrSessionListener.manager
				.getAllScriptSessions();
		for (ScriptSession session : pages) {
			if (session.getAttribute("user") != null) {
				int userTargetId = ((User) session.getAttribute("user"))
						.getUserId();
				if (userTargetId == userId) {
					logger.debug("Push Message: User [" + userId
							+ "] Edit Row IP [" + rowId + "]");
					session.addScript(script);
				}
			}
		}
	}

	public static void editRowConsole(int userId, String rowId, String option) {
		ScriptBuffer script = new ScriptBuffer();
		script.appendCall("dwrEditRowConsole", rowId, option);
		Collection<ScriptSession> pages = DwrSessionListener.manager
				.getAllScriptSessions();
		for (ScriptSession session : pages) {
			if (session.getAttribute("user") != null) {
				int userTargetId = ((User) session.getAttribute("user"))
						.getUserId();
				if (userTargetId == userId) {
					logger.debug("Push Message: User [" + userId
							+ "] Edit Row Console [" + rowId + "]");
					session.addScript(script);
				}
			}
		}
	}
}
