package com.oncecloud.aop;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.OCExceptionDAO;
import com.oncecloud.entity.OCException;
import com.oncecloud.entity.User;

@Component
@Aspect
public class XenAOP {
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

	@Pointcut("execution(* com.once.xenapi..*.*(..))")
	public void xenMethod() {
		
	};

	@Before("xenMethod()")
	public void xenBefore(JoinPoint joinpoint) {
		System.out.println("--------xen---------");
		System.out.println(joinpoint.getTarget().getClass().getName());
	}
	
	@AfterThrowing(pointcut="xenMethod()",throwing="throwable")
	public void xenThrowingException(JoinPoint joinpoint, RuntimeException throwable) {
		System.out.println("--------xen---------");
		User user = (User) request.getSession().getAttribute("user");
		OCException oce = new OCException();
		if(user != null) {
			oce.setExcUid(user.getUserId());
		}
		oce.setExcFunName(joinpoint.getSignature().getName());
		String args = "";
		for (Object o : joinpoint.getArgs()) {
			args += o.toString() + "|";
		}
		oce.setExcArgs(args);
		oce.setExcClassName(joinpoint.getTarget().getClass().getName());
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
