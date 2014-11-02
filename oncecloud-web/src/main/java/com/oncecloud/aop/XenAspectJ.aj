package com.oncecloud.aop;

import java.util.Date;

import com.oncecloud.dao.OCExceptionDAO;
import com.oncecloud.entity.OCHException;

public aspect XenAspectJ {
	pointcut mypointcut() : execution(public * com.once.xenapi..*(..));

	after() throwing(Exception ex) : mypointcut() {
		OCHException oce = new OCHException();
		oce.setExcUid(1);
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
		OCExceptionDAO ocExceptionDAO = new OCExceptionDAO();
		ocExceptionDAO.save(oce);
	}
	
}
