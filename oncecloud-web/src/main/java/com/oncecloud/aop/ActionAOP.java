package com.oncecloud.aop;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oncecloud.dao.OCExceptionDAO;
import com.oncecloud.entity.OCException;
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

//	@Before("myMethod()")
	public void beforeAction(JoinPoint joinpoint) {
		/*User user = (User) request.getSession().getAttribute("user");
		if(user != null) {
			System.out.println(user.getUserId());
		}*/
		System.out.println("方法名称：  " + joinpoint.getSignature().getName());
		for (Object o : joinpoint.getArgs()) {
			System.out.println("方法的参数：   " + o.toString());
		}
		System.out.println("代理对象：   "
				+ joinpoint.getTarget().getClass().getName());
	}

	@AfterThrowing(pointcut="myMethod()",throwing="throwable")
	public void afterThrowingException(JoinPoint joinpoint,RuntimeException throwable) {
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
