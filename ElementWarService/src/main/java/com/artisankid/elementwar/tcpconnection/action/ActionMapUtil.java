package com.artisankid.elementwar.tcpconnection.action;

import com.artisankid.elementwar.tcpconnection.action.filter.BusinessFilter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ActionMapUtil {

    private static Map<Integer, Action> map = new HashMap<>();

    public static Object invote(int key, Object... args) throws Exception {
        /**
         * 通过代理统一过滤数据处理
         */
        Boolean isReturn = !BusinessFilter.isIn(key, args);
        if (isReturn) {
            return null;
        }
        /**
         * 通过反射调用方法
         */
        Action action = map.get(key);
        if (action != null) {
            Method method = action.getMethod();
            try {
                return method.invoke(action.getObject(), args);
            } catch (Exception e) {
                throw e;
            }
        }
        return null;
    }

    public static void put(int key, Action action) {
        map.put(key, action);
    }
}
