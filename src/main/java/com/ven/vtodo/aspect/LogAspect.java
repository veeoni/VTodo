package com.ven.vtodo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component//spring通过组件扫描到这个组件
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.ven.vtodo.web..*(..))")//按execution规定拦截哪些类的哪些方法
    public void log() {
    }

    @Around("log()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object returnValue = null;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        Long startTime = System.currentTimeMillis();
        returnValue = point.proceed(point.getArgs());
        Long endTime = System.currentTimeMillis();
        logger.info("------------------doAround---------------------");
        logger.info("rest  " + request.getRequestURI() + "---used time---" + (endTime - startTime) + "ms");
        return returnValue;
    }

    @Before("log()")//只要有请求，都会在方法前执行这个
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        //获取类名和方法名需要用AOP
        String classMethod = joinPoint.getSignature().getName() + "." + joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        RequestLog requestLog = new RequestLog(url, ip, classMethod, args);
        logger.info("-----------------doBefore---------------------");
        logger.info("Request: {}", requestLog);
    }

    @After("log()")
    public void doAfter() {
        logger.info("------------------doAfter---------------------");
    }

    @AfterReturning(returning = "result", pointcut = "log()")
    public void doAfterReturn(Object result) {
        logger.info("Result: {}", result);
    }

    private static class RequestLog {
        private final String url;
        private final String ip;
        private final String classMethod;
        private final Object[] args;

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
