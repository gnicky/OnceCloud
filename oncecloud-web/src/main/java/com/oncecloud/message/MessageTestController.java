package com.oncecloud.message;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.oncecloud.entity.User;

@Controller
public class MessageTestController {
	private MessagePush messagePush;

	private MessagePush getMessagePush() {
		return messagePush;
	}

	@Autowired
	private void setMessagePush(MessagePush messagePush) {
		this.messagePush = messagePush;
	}

	@RequestMapping("/MessageTest")
	public ModelAndView messageTest() {
		return new ModelAndView("messageTest");
	}

	@RequestMapping("/SendMessage")
	@ResponseBody
	public boolean sendMessage(HttpServletRequest request) {
		this.getMessagePush().pushMessage(
				((User) request.getSession().getAttribute("user")).getUserId()
						.intValue(), new Date().toString());
		return true;
	}
}
