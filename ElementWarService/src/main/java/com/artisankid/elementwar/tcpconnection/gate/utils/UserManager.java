package com.artisankid.elementwar.tcpconnection.gate.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by LiXiangYu on 2017/4/28.
 */
public class UserManager {
    private static ConcurrentHashMap<String, User> userMap = new ConcurrentHashMap<>();

    public static User getUser(String userID) {
        return userMap.get(userID);
    }

    public static void addUser(User user) {
        userMap.put(user.getUserID(), user);
    }

    public static void removeUser(String userID) {
        userMap.remove(userID);
    }

    /**
     * 获取全部的空闲的用户ID
     * @return
     */
    public static List<String> getMatchUserIDs() {
        List<String> userIDs = new ArrayList<>();
        for (Map.Entry<String, User> entry : userMap.entrySet()) {
            if(entry.getValue().getState() == User.State.Matching) {
                userIDs.add(entry.getKey());
            }
        }
        return userIDs;
    }
}
