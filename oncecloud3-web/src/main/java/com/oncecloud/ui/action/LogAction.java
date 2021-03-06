package com.oncecloud.ui.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oncecloud.entity.User;
import com.oncecloud.manager.LogManager;

@RequestMapping("/LogAction")
@Controller
public class LogAction {
	private LogManager logManager;

	private LogManager getLogManager() {
		return logManager;
	}

	@Autowired
	private void setLogManager(LogManager logManager) {
		this.logManager = logManager;
	}

	@RequestMapping(value = "/LogList", method = { RequestMethod.GET })
	@ResponseBody
	public String logList(HttpServletRequest request, @RequestParam int status,
			@RequestParam int start, @RequestParam int num)
			throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		int userId = user.getUserId();
		JSONArray ja = this.getLogManager().getLogList(userId, status, start,
				num);
		return ja.toString();
	}
}
