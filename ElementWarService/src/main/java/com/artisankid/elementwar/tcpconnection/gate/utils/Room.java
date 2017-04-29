package com.artisankid.elementwar.tcpconnection.gate.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by LiXiangYu on 2017/4/28.
 */
public class Room {
    private String roomID;
    private ConcurrentHashMap<String, User> userMap = new ConcurrentHashMap<>();

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setUserIDs(List<String> userIDs) {
        for(String userID : userIDs) {
            User user = new User();
            user.setHp(30);
            userMap.put(userID, user);
        }
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        for (Map.Entry<String, User> entry : userMap.entrySet()) {
            users.add(entry.getValue());
        }
        return users;
    }

    public void addUserID(String userID) {
        User user = new User();
        user.setHp(30);
        userMap.put(userID, user);
    }

    public User getUser(String userID) {
        return userMap.get(userID);
    }

    public boolean isAllUserReady() {
        for(User user : getUsers()) {
            if(user.getState() != User.State.Match) {
                return false;
            }
        }
        return true;
    }
}