package com.artisankid.elementwar.common.ewmodel;

import java.util.List;

/**
 * Created by LiXiangYu on 2017/4/16.
 */
public class Room {
    private String roomID;
    private List<BaseMagician> magicians;

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public List<BaseMagician> getMagicians() {
        return magicians;
    }

    public void setMagicians(List<BaseMagician> magicians) {
        this.magicians = magicians;
    }
}
