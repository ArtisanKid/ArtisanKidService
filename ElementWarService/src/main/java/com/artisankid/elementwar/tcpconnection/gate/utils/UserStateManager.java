package com.artisankid.elementwar.tcpconnection.gate.utils;

import com.sun.org.apache.bcel.internal.generic.NEW;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by LiXiangYu on 2017/4/27.
 */
public class UserStateManager {
    public enum UserState {
        Busy, Free
    }

    private static ConcurrentHashMap<String, UserState> userStateMap = new ConcurrentHashMap<>();

    public static UserState getUserState(String userID) {
        return userStateMap.get(userID);
    }

    /**
     * 获取全部的空闲的用户ID
     * @return
     */
    public static List<String> getFreeUserIDs() {
        List<String> userIDs = new ArrayList<>();
        for (Map.Entry<String, UserState> entry : userStateMap.entrySet()) {
            if(entry.getValue() == UserState.Free) {
                userIDs.add(entry.getKey());
            }
        }
        return userIDs;
    }
}
