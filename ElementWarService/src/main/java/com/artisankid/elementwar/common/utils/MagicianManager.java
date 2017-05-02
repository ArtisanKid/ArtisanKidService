package com.artisankid.elementwar.common.utils;

/**
 * Created by LiXiangYu on 2017/5/2.
 */
public class MagicianManager {
    static public String CreateUserID() {
        String now = new Long(System.currentTimeMillis()).toString();
        String identifier = "ArtisanKid" + now;
        int hashCode = identifier.hashCode();
        String hashValue = Integer.toHexString(hashCode);
        return hashValue;
    }

    static public String CreateOpenID(String userID) {
        String now = new Long(System.currentTimeMillis()).toString();
        String identifier = userID + "ElementWar" + now;
        int hashCode = identifier.hashCode();
        String hashValue = Integer.toHexString(hashCode);
        return hashValue;
    }
}
