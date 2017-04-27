package com.artisankid.elementwar.tcpconnection.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * ActionMapUtils处理器 AOP处理
 * <p>
 * Created by ws on 2017/4/26.
 */
@Aspect
@Component
public class HandlerAspect {

//    @Pointcut("execution(* com.artisankid.elementwar.tcpconnection.action.*.*(..))")
//    //方法切入点，execution为执行的意思，*代表任意返回值，然后是包名，.*意思是包下面的所有子包。(..)代表各种方法.
//    public void init() {
//
//    }

    /**
     * 该方法会在调用invote(String params) 之前执行。
     * 然后再调用业务逻辑的sayHello(string str) 方法。
     */
    @Before("execution(* com.artisankid.elementwar.tcpconnection.action.ActionMapUtil (..))")
    public void cutInvoteMethod() {
        System.out.println("---------------------------------cutInvoteMethod");
    }

}
