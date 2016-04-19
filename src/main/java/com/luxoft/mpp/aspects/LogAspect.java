package com.luxoft.mpp.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by iivaniv on 18.04.2016.
 */
@Aspect
@Service(value = "logAspect1")
public class LogAspect {


    @Before("execution(* com.luxoft.mpp.service.*.*(..))")
    public void logBeforeController(JoinPoint joinPoint){

        System.out.println("Before method "+ joinPoint.getStaticPart().getSignature().toString());

    }

    @After("execution(* com.luxoft.mpp.service.*.*(..))")
    public void logAfterController(JoinPoint joinPoint){

        System.out.println("After method "+ joinPoint.getStaticPart().getSignature().toString());

    }


}


