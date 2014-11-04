package com.oncecloud.common.aop;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.OCExceptionDAO;
import com.oncecloud.entity.OCHException;
import com.oncecloud.entity.User;

@Component
@Aspect
public class ActionAOP {
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

	@Pointcut("execution(* com.oncecloud.ui.action..*.*(..))")
	public void myMethod() {
		
	};

	@AfterThrowing(pointcut="myMethod()",throwing="throwable")
	public void afterThrowingException(JoinPoint joinpoint,RuntimeException throwable) {
		User user = (User) request.getSession().getAttribute("user");
		OCHException oce = new OCHException();
		if(user != null) {
			oce.setExcUid(user.getUserId());
		}
		oce.setExcFunName(joinpoint.getSignature().getName());
		String args = "";
		for (Object o : joinpoint.getArgs()) {
			args += o.toString() + "|";
		}
		oce.setExcArgs(args);
		oce.setExcClassName(joinpoint.getSignature().toString());
		String exception = throwable.getMessage() + "|" + throwable.getCause() + "|";
		for (StackTraceElement str : throwable.getStackTrace()) {
			if(str.getClassName().contains("com.oncecloud")) {
				exception += str.toString() + "|";
			}
		}
		oce.setExcException(exception);
		oce.setExcDate(new Date());
		this.getOcExceptionDAO().save(oce);
	}

}
