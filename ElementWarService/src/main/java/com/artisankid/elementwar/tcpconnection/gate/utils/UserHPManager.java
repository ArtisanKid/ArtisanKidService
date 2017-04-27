package com.artisankid.elementwar.tcpconnection.gate.utils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by LiXiangYu on 2017/4/27.
 */
public class UserHPManager {
    private static ConcurrentHashMap<String, Integer> userHPMap = new ConcurrentHashMap<>();

    public static Integer getUserHP(String userID) {
        return userHPMap.get(userID);
    }

    public static void getUserHP(String userID, Integer hp) {
        userHPMap.put(userID, hp);
    }
}
