package com.oncecloud.aop;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.oncecloud.dao.OCExceptionDAO;
import com.oncecloud.entity.OCHException;
import com.oncecloud.entity.User;

public aspect XenAspectJ {
	private HttpServletRequest request;
	private OCExceptionDAO ocExceptionDAO;
	
	public HttpServletRequest getRequest() {
		return request;
	}

	@Autowired
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public OCExceptionDAO getOcExceptionDAO() {
		return ocExceptionDAO;
	}

	@Autowired
	public void setOcExceptionDAO(OCExceptionDAO ocExceptionDAO) {
		this.ocExceptionDAO = ocExceptionDAO;
	}
	
	pointcut mypointcut() : execution(public * com.once.xenapi..*(..));

	after() throwing(Exception ex) : mypointcut() {
		User user = (User) request.getSession().getAttribute("user");
		OCHException oce = new OCHException();
		if(user != null) {
			oce.setExcUid(user.getUserId());
		}
		oce.setExcFunName(thisJoinPoint.getSignature().getName());
		String args = "";
		for (Object o : thisJoinPoint.getArgs()) {
			args += o.toString() + "|";
		}
		oce.setExcArgs(args);
		oce.setExcClassName(thisJoinPoint.getSignature().toString());
		String exception = ex.getMessage() + "|" + ex.getCause() + "|";
		for (StackTraceElement str : ex.getStackTrace()) {
			if(str.getClassName().contains("com.oncecloud")) {
				exception += str.toString() + "|";
			}
		}
		oce.setExcException(exception);
		oce.setExcDate(new Date());
		this.getOcExceptionDAO().save(oce);
	}
	
}
