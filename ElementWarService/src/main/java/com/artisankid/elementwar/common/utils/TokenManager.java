package com.artisankid.elementwar.common.utils;

/**
 * Created by LiXiangYu on 2017/5/2.
 */
public class TokenManager {
    static public String CreateAccessToken(String openID) {
        String now = new Long(System.currentTimeMillis()).toString();
        String identifier = openID + "AccessToken" + now;
        int hashCode = identifier.hashCode();
        String hashValue = Integer.toHexString(hashCode);
        return hashValue;
    }

    static public String CreateRefreshToken(String openID) {
        String now = new Long(System.currentTimeMillis()).toString();
        String identifier = openID + "RefreshToken" + now;
        int hashCode = identifier.hashCode();
        String hashValue = Integer.toHexString(hashCode);
        return hashValue;
    }
}
