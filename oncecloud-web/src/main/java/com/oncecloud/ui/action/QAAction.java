package com.oncecloud.ui.action;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.entity.User;
import com.oncecloud.manager.QAManager;
import com.oncecloud.ui.model.ListModel;

@RequestMapping("/QAAction")
@Controller
public class QAAction {
	private QAManager qaManager;

	public QAManager getQaManager() {
		return qaManager;
	}

	@Autowired
	public void setQaManager(QAManager qaManager) {
		this.qaManager = qaManager;
	}

	@RequestMapping(value = "/QuestionList", method = { RequestMethod.GET })
	@ResponseBody
	public String questionList(HttpServletRequest request, ListModel list) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			JSONArray ja = this.getQaManager().getQuestionList(
					user.getUserId(), user.getUserLevel(), list.getPage(),
					list.getLimit(), list.getSearch());
			return ja.toString();
		} else {
			return "";
		}
	}
}