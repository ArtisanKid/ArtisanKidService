//package com.artisankid.elementwar.tcpconnection.aspect;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Method;
//
///**
// * ActionMapUtils处理器 AOP处理
// * <p>
// * Created by ws on 2017/4/26.
// */
//@Aspect
//@Component("handlerAspect")
//public class HandlerAspect {
//
//    @Pointcut("execution( * com.artisankid.elementwar.tcpconnection.action.impl.*.*(..))")
//    //方法切入点，execution为执行的意思，*代表任意返回值，然后是包名，.*意思是包下面的所有子包。(..)代表各种方法.
//    public void cutMethods() {
//
//    }
//
//    /**
//     * 该方法会在调用invote(String params) 之前执行。
//     * 然后再调用业务逻辑的sayHello(string str) 方法。
//     */
//    @Before("cutMethods()")
//    public void cutInvoteMethod(JoinPoint joinPoint) {
//        System.out.println("class:" + joinPoint.getClass() + ",method:" + joinPoint.getSignature() + ",args:" + joinPoint.getArgs());
//    }
//
//}
