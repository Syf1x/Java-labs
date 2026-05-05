package com.office.employeemanagement.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(* com.office.employeemanagement.service.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed(); // Выполнение самого метода

        long executionTime = System.currentTimeMillis() - start;
        log.info("AOP [Time Log]: Метод {} выполнен за {} мс",
                joinPoint.getSignature().getName(), executionTime);

        return proceed;
    }
}