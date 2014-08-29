package com.oncecloud.ui.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.entity.User;
import com.oncecloud.manager.QAManager;

@RequestMapping("/QAAction")
@Controller
public class QAController {
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
	public String questionList(HttpServletRequest request, @RequestParam int page,
			@RequestParam int limit, @RequestParam String search) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			JSONArray ja = this.getQaManager().getQuestionList(user.getUserId(),
					user.getUserLevel(), page, limit, search);
			return ja.toString();
		} else {
			return "";
		}
	}
}
