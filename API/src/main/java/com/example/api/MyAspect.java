package com.example.api;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect {
    @Before( "execution(* com.example.api.HpPrinter.*(..))")
    public void before() {
        System.out.println("I'm before");
    }

    @After("execution(* com.example.api.HpPrinter.*(..))")
    public void after() {
        System.out.println("I'm after");
    }

    @Around("execution(* com.example.api.HpPrinter.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("I'm around before");

        // 執行切入點的方法，obj 為切入點方法執行的結果
        Object obj = pjp.proceed();

        System.out.println("I'm around after");
        return obj;
    }
}
