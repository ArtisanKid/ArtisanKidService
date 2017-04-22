package com.artisankid.elementwar.tcpconnection.action;

import com.artisankid.elementwar.tcpconnection.annotations.ActionRequestMap;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;

/**
 * Created by ws on 2017/4/22.
 */
public class ActionBeanPostProcessor implements BeanPostProcessor {

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) {
            ActionRequestMap actionMap = method.getAnnotation(ActionRequestMap.class);
            if (actionMap != null) {
                Action action = new Action();
                action.setMethod(method);
                action.setObject(bean);
                ActionMapUtil.put(actionMap.actionKey(), action);
            }
        }
        return bean;
    }

}

