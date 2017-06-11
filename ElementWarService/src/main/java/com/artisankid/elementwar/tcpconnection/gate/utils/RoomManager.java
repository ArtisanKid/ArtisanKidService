package com.artisankid.elementwar.tcpconnection.gate.utils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by LiXiangYu on 2017/4/28.
 */
public class RoomManager {
    private static ConcurrentHashMap<String, Room> userRoomMap = new ConcurrentHashMap<>();

    public static Room createRoom(List<String> userIDs) {
        String roomID = "";
        for(String userID : userIDs) {
            roomID += userID + "+";
        }

        Room room = new Room();
        room.setRoomID(roomID);
        room.setUserIDs(userIDs);
        for(String userID : userIDs) {
            userRoomMap.put(userID, room);
        }
        return room;
    }

    public static Room getRoom(String userID) {
        return userRoomMap.get(userID);
    }

    public static void removeRoom(String userID) {
        Room room = getRoom(userID);
        if(room == null) {
            return;
        }

        for(User user : room.getUsers()) {
            userRoomMap.remove(user.getUserID());
        }
    }
}
