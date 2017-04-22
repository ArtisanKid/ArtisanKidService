package com.artisankid.elementwar.tcpconnection.action;

import java.lang.reflect.Method;

/**
 * action处理器
 *
 * Created by ws on 2017/4/22.
 */
public class Action {

    private Method method;

    private Object object;

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
}

