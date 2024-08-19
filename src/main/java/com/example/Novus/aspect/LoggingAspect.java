package com.example.Novus.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.example.Novus.service.*.*(..))")
    private void serviceLayer() {}

    @Pointcut("execution(* com.example.Novus.controller.*.*(..))")
    private void controllerLayer() {}

    @Around("serviceLayer()")
    public Object logAroundService(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethod(joinPoint, "Service");
    }

    @Around("controllerLayer()")
    public Object logAroundController(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        logger.info("Received HTTP {} request to {}", request.getMethod(), request.getRequestURI());

        Object result = logMethod(joinPoint, "Controller");

        logger.info("Returning from HTTP {} request to {}", request.getMethod(), request.getRequestURI());
        return result;
    }

    private Object logMethod(ProceedingJoinPoint joinPoint, String layer) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        logger.info("Entering {} method: {}.{}", layer, className, methodName);

        long start = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            logger.info("Exiting {} method: {}.{} - Execution time: {} ms", layer, className, methodName, executionTime);
        } catch (Exception e) {
            logger.error("Exception in {} {}.{}: {}", layer, className, methodName, e.getMessage());
            throw e;
        }

        return result;
    }
}