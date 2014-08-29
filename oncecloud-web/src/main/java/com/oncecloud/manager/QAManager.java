package com.oncecloud.manager;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.QADAO;
import com.oncecloud.dao.UserDAO;
import com.oncecloud.dwr.MessagePush;
import com.oncecloud.entity.QA;
import com.oncecloud.main.Utilities;

/**
 * @author hehai
 * @version 2014/08/24
 */
@Component
public class QAManager {
	private QADAO qaDAO;
	private UserDAO userDAO;

	private QADAO getQaDAO() {
		return qaDAO;
	}

	@Autowired
	private void setQaDAO(QADAO qaDAO) {
		this.qaDAO = qaDAO;
	}

	private UserDAO getUserDAO() {
		return userDAO;
	}

	@Autowired
	private void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public JSONArray getQuestionList(int userId, int userLevel, int page,
			int limit, String search) {
		JSONArray ja = new JSONArray();
		int total = this.getQaDAO().countAllQuestionList(userId, search);
		ja.put(total);
		List<QA> qaList = this.getQaDAO().getOnePageQuestionList(userId, page,
				limit, search);
		if (qaList != null) {
			for (QA qa : qaList) {
				JSONObject qaObj = new JSONObject();
				qaObj.put("qaId", qa.getQaId());
				if (userLevel == 0) {
					qaObj.put("qaUserName",
							this.getUserDAO().getUser(qa.getQaUID())
									.getUserName());
				}
				qaObj.put("qaTitle", Utilities.encodeText(qa.getQaTitle()));
				qaObj.put("qaSummary", Utilities.encodeText(Utilities
						.getSummary(qa.getQaContent())));
				qaObj.put("qaStatus", qa.getQaStatus());
				qaObj.put("qaReply", qa.getQaReply());
				qaObj.put("qaTime", Utilities.formatTime(qa.getQaTime()));
				ja.put(qaObj);
			}
		}
		return ja;
	}

	public JSONArray closeQuestion(int userId, int qaId) {
		JSONArray ja = new JSONArray();
		boolean result = this.getQaDAO().closeQuestion(qaId);
		JSONObject jo = new JSONObject();
		jo.put("isSuccess", result);
		ja.put(jo);
		// push message
		if (result == true) {
			MessagePush
					.pushMessage(userId, Utilities.stickyToSuccess("表单关闭成功"));
		} else {
			MessagePush
					.pushMessage(userId, Utilities.stickyToSuccess("表单关闭失败"));
		}
		return ja;
	}

	public JSONObject createQuestion(int userId, String qaTitle,
			String qaContent) {
		JSONObject jo = new JSONObject();
		Date qaTime = new Date();
		int qaId = this.getQaDAO().insertQuestion(userId, qaTitle, qaContent,
				qaTime);
		jo.put("qaId", qaId);
		// push message
		if (qaId > 0) {
			jo.put("qaSummary",
					Utilities.encodeText(Utilities.getSummary(qaContent)));
			jo.put("qaTime", Utilities.formatTime(qaTime));
			MessagePush
					.pushMessage(userId, Utilities.stickyToSuccess("表单提交成功"));
		} else {
			MessagePush
					.pushMessage(userId, Utilities.stickyToSuccess("表单提交失败"));
		}
		return jo;
	}

	public JSONArray addReply(int userId, int qaTid, String qaContent) {
		JSONArray ja = new JSONArray();
		Date qaTime = new Date();
		int qaId = this.getQaDAO().insertAnswer(userId, qaContent, qaTid, 3,
				qaTime);
		JSONObject jo = new JSONObject();
		jo.put("isSuccess", qaId);
		// push message
		if (qaId > 0) {
			jo.put("qaContent", Utilities.encodeText(qaContent));
			jo.put("qaTime", Utilities.formatTime(qaTime));
			MessagePush
					.pushMessage(userId, Utilities.stickyToSuccess("回复提交成功"));
		} else {
			MessagePush
					.pushMessage(userId, Utilities.stickyToSuccess("回复提交失败"));
		}
		ja.put(jo);
		return ja;
	}

	public JSONArray getQuestionDetail(int qaId) {
		JSONArray ja = new JSONArray();
		QA qa = this.getQaDAO().getQuestion(qaId);
		if (qa != null) {
			JSONObject qaObj = new JSONObject();
			qaObj.put("qaTitle", Utilities.encodeText(qa.getQaTitle()));
			qaObj.put("qaContent", Utilities.encodeText(qa.getQaContent()));
			qaObj.put("qaStatus", qa.getQaStatus());
			qaObj.put("qaTime", Utilities.formatTime(qa.getQaTime()));
			ja.put(qaObj);
		}
		return ja;
	}

	public JSONArray getAnswerList(int qaId) {
		JSONArray ja = new JSONArray();
		List<Object> qaList = this.getQaDAO().getAnswerList(qaId);
		if (qaList != null) {
			for (Object qa : qaList) {
				Object[] qaO = (Object[]) qa;
				JSONObject qaObj = new JSONObject();
				qaObj.put("qaUser", (String) qaO[0]);
				qaObj.put("qaContent", Utilities.encodeText((String) qaO[1]));
				qaObj.put("qaStatus", (Integer) qaO[2]);
				qaObj.put("qaTime", Utilities.formatTime((Date) qaO[3]));
				ja.put(qaObj);
			}
		}
		return ja;
	}
}
