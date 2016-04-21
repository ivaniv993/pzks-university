package com.luxoft.mpp.aspects;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by iivaniv on 18.04.2016.
 */
@Aspect
@Service
public class LogAspect {

    static Logger logger = null;

    static{
        logger = Logger.getRootLogger();
        BasicConfigurator.configure();
    }


    @Before("execution(* com.luxoft.mpp.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint){
        logger.info("Before service method "+ joinPoint.getStaticPart().getSignature().toString());
    }

    @After("execution(* com.luxoft.mpp.service.*.*(..))")
    public void logAfter(JoinPoint joinPoint){
        logger.info("After service method " + joinPoint.getStaticPart().getSignature().toString());
    }

    @AfterReturning("execution(* com.luxoft.mpp.service.*.*(..))")
    public void logAfterReturning(JoinPoint joinPoint){
        logger.info("After successful returning method " + joinPoint.getStaticPart().getSignature().toString());

    }


}


