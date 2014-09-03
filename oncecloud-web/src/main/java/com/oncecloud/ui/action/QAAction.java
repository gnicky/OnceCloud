package com.oncecloud.ui.action;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
		JSONArray ja = this.getQaManager().getQuestionList(user.getUserId(),
				user.getUserLevel(), list.getPage(), list.getLimit(),
				list.getSearch());
		return ja.toString();
	}
	
	@RequestMapping(value = "/CreateQuestion", method = { RequestMethod.POST })
	@ResponseBody
	public String createQuestion(HttpServletRequest request,
			@RequestParam String title, @RequestParam String content) {
		User user = (User) request.getSession().getAttribute("user");
		JSONObject jo = this.getQaManager().createQuestion(user.getUserId(), title, content);
		return jo.toString();
	}

	@RequestMapping(value = "/CloseQuestion", method = { RequestMethod.GET })
	@ResponseBody
	public String closeQuestion(HttpServletRequest request,
			@RequestParam int qaid) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getQaManager()
				.closeQuestion(user.getUserId(), qaid);
		return ja.toString();
	}

	@RequestMapping(value = "/QuestionDetail", method = { RequestMethod.GET })
	@ResponseBody
	public String questionDetail(HttpServletRequest request,
			@RequestParam int qaId) {
		JSONArray ja = this.getQaManager().getQuestionDetail(qaId);
		return ja.toString();
	}

	@RequestMapping(value = "/Reply", method = { RequestMethod.POST })
	@ResponseBody
	public String reply(HttpServletRequest request, @RequestParam int qaId,
			@RequestParam String content) {
		User user = (User) request.getSession().getAttribute("user");
		JSONArray ja = this.getQaManager().reply(user.getUserId(),
				user.getUserLevel(), qaId, content);
		return ja.toString();
	}

	@RequestMapping(value = "/ReplyList", method = { RequestMethod.POST })
	@ResponseBody
	public String replyList(HttpServletRequest request, @RequestParam int qaId) {
		JSONArray ja = this.getQaManager().getReplyList(qaId);
		return ja.toString();
	}
}