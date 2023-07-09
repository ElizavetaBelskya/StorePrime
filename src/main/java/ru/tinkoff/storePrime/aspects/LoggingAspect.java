package ru.tinkoff.storePrime.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;


@Component
@Aspect
public class LoggingAspect {

    Logger logger = Logger.getLogger("StorePrime logger");

    @Pointcut("within(ru.tinkoff.storePrime.controller.handler.RestExceptionHandler)")
    public void methods() {}

    @Before("methods() && args(java.lang.Throwable, ..)")
    public void logError(JoinPoint joinPoint) {
        Throwable throwable = (Throwable) joinPoint.getArgs()[0];
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Annotation[] annotations = method.getAnnotations();
        ResponseStatus status = null;
        for (Annotation annotation : annotations) {
            if (annotation instanceof ResponseStatus) {
                status = (ResponseStatus) annotation;
            }
        }
        HttpStatus code = status != null ? status.value() : null;
        String errorMessage = throwable.getMessage();
        if (code == HttpStatus.INTERNAL_SERVER_ERROR) {
            logger.log(Level.SEVERE, "Internal server error: " + errorMessage);
        } else {
            logger.log(Level.WARNING, errorMessage);
        }
    }

}
