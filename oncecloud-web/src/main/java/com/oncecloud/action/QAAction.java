package com.oncecloud.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.entity.User;
import com.oncecloud.manager.QAManager;

/**
 * @author hehai
 * @version 2014/08/24
 */
public class QAAction extends HttpServlet {
	private static final long serialVersionUID = 2234918116676127248L;
	private QAManager qaManager;

	private QAManager getQaManager() {
		return qaManager;
	}

	private void setQaManager(QAManager qaManager) {
		this.qaManager = qaManager;
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String action = request.getParameter("action");
		if (user != null && action != null) {
			int userId = user.getUserId();
			if (action.equals("getquestion")) {
				int userLevel = user.getUserLevel();
				int page = Integer.parseInt(request.getParameter("page"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				String search = request.getParameter("search");
				JSONArray ja = this.getQaManager().getQuestionList(userId,
						userLevel, page, limit, search);
				out.print(ja.toString());
			} else if (action.equals("closequestion")) {
				int qaId = Integer.parseInt(request.getParameter("qaid"));
				JSONArray ja = this.getQaManager().closeQuestion(userId, qaId);
				out.print(ja.toString());
			} else if (action.equals("create")) {
				String qaTitle = request.getParameter("title");
				String qaContent = request.getParameter("content");
				JSONObject jo = this.getQaManager().createQuestion(userId,
						qaTitle, qaContent);
				out.print(jo.toString());
			} else if (action.equals("addreply")) {
				String qaContent = request.getParameter("content");
				int qaTid = Integer.parseInt(request.getParameter("qaid"));
				JSONArray ja = this.getQaManager().addReply(userId, qaTid,
						qaContent);
				out.print(ja.toString());
			} else if (action.equals("detail")) {
				String qaId = request.getParameter("qaid");
				session.setAttribute("qaId", qaId);
			} else if (action.equals("questiondetail")) {
				int qaId = Integer.parseInt(request.getParameter("qaid"));
				JSONArray ja = this.getQaManager().getQuestionDetail(qaId);
				out.print(ja.toString());
			} else if (action.equals("getanswer")) {
				int qaId = Integer.parseInt(request.getParameter("qaid"));
				JSONArray ja = this.getQaManager().getAnswerList(qaId);
				out.print(ja.toString());
			}
		}
	}
}
