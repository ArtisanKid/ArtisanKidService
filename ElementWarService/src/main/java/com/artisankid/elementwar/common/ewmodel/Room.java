package com.artisankid.elementwar.common.ewmodel;

import java.util.List;

/**
 * Created by LiXiangYu on 2017/4/16.
 */
public class Room {
    private String roomID;
    private List<Magician> magicianList;

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public List<Magician> getMagicianList() {
        return magicianList;
    }

    public void setMagicianList(List<Magician> magicianList) {
        this.magicianList = magicianList;
    }
}
