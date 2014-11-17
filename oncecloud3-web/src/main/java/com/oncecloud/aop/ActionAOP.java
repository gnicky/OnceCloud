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
import com.oncecloud.dao.impl.OCExceptionDAOImpl;
import com.oncecloud.entity.OCHException;
import com.oncecloud.entity.User;

@Component
@Aspect
public class ActionAOP {

	@Pointcut("execution(* com.oncecloud.ui.action..*.*(..))")
	public void myMethod() {
		
	};

	@AfterThrowing(pointcut="myMethod()",throwing="throwable")
	public void afterThrowingException(JoinPoint joinpoint,RuntimeException throwable) {
		OCHException oce = new OCHException();
		oce.setExcUid(1);
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
		OCExceptionDAO ocExceptionDAO = new OCExceptionDAOImpl();
		ocExceptionDAO.save(oce);
	}

}
