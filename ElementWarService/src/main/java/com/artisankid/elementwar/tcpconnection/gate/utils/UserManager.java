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
        User user = userMap.get(userID);
        if(user == null) {
            user = new User();
            user.setState(User.State.Free);
            userMap.put(userID, user);
        }
        return userMap.get(userID);
    }

    public static void setUser(User user) {
        userMap.put(user.getUserID(), user);
    }

    /**
     * 获取全部的空闲的用户ID
     * @return
     */
    public static List<String> getMatchUserIDs() {
        List<String> userIDs = new ArrayList<>();
        for (Map.Entry<String, User> entry : userMap.entrySet()) {
            if(entry.getValue().getState() == User.State.Match) {
                userIDs.add(entry.getKey());
            }
        }
        return userIDs;
    }
}
