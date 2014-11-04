package com.oncecloud.common.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class CommonInterceptor implements HandlerInterceptor {

	private List<String> uncheckUrls;

	public void setUncheckUrls(List<String> uncheckUrls) {
		this.uncheckUrls = uncheckUrls;
	}

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
//		if(ex.getClass() == NoSuchRequestHandlingMethodException.class){  
//            response.sendRedirect(request.getContextPath()+"/account/404.jsp");  
//        }else{  
//            response.sendRedirect(request.getContextPath()+"/account/500.jsp");  
//        }
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String requestUrl = request.getRequestURI();
		if (uncheckUrls.contains(requestUrl)) {
			return true;
		} else {
			if (request.getSession().getAttribute("user") == null) {
				response.sendRedirect("/login");
				return false;
			} else {
				return true;
			}
		}
	}

}