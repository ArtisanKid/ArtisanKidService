package com.artisankid.elementwar.tcpconnection.action;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ActionMapUtil {
	private static Map<String, Action> map = new HashMap<String, Action>();

	public static Object invote(String key, Object... args) throws Exception {
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

	public static void put(String key, Action action) {
		map.put(key, action);
	}
}
