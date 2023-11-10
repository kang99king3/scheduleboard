package com.hk.calboard.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogAspect {

	Logger logger=LoggerFactory.getLogger(getClass());
	
	@Pointcut("within(com.hk.calboard.controller.*)")
	public void controller() {}
	
	@Before(value = "controller()")
	public void before(JoinPoint join) {
		logger.info("메서드명:{}",join.getSignature().getName());
	}
	
	@AfterReturning( pointcut = "controller()",  returning = "returnVal" )
	public void afterReturning(JoinPoint join, Object returnVal) {
		logger.info("declaring:{}",join.getSignature().getDeclaringType());
		if(returnVal==null) {
			return;
		}else {
			logger.info("리턴값:{}",returnVal);			
		}
	}
	
	@AfterThrowing(pointcut = "controller()", throwing = "e")
	public void afterThrowing(JoinPoint join, Exception e) {
		logger.info("메서드명:{}",join.toShortString());
		logger.info("오류:{}",e.getMessage());
	}
}
