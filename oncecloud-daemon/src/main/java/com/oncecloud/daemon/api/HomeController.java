package com.oncecloud.daemon.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.oncecloud.daemon.monitor.WebConsoleMonitor;

@Controller
public class HomeController {
	private WebConsoleMonitor webConsoleMonitor;

	private WebConsoleMonitor getWebConsoleMonitor() {
		return webConsoleMonitor;
	}

	@Autowired
	private void setWebConsoleMonitor(WebConsoleMonitor webConsoleMonitor) {
		this.webConsoleMonitor = webConsoleMonitor;
	}

	@RequestMapping("/")
	@ResponseBody
	public ModelAndView index() {
		this.getWebConsoleMonitor().publish();
		return new ModelAndView("index");
	}
}
