package com.artisankid.elementwar.tcpconnection.action;

import java.lang.reflect.Method;

/**
 * action处理器
 *
 * Created by WangShaoHua on 2017/4/22.
 */
public class Action {

    private Method method;

    private Object object;

    private String beanName;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}

