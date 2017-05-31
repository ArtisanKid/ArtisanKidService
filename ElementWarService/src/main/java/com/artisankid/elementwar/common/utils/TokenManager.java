package com.artisankid.elementwar.common.utils;

import com.artisankid.elementwar.common.dao.TokenDao;
import com.artisankid.elementwar.common.ewmodel.Token;
import com.artisankid.elementwar.ewmodel.ResponseClass;
import com.google.gson.Gson;

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

    static public Boolean VerifyAccessToken(String accessToken) {
        TokenDao tokenDao = new TokenDao();
        if(tokenDao.selectByAccessToken(accessToken) != null) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    static public Boolean VerifyAccessToken(String openID, String accessToken) {
        TokenDao tokenDao = new TokenDao();
        for(Token token : tokenDao.selectByOpenID(openID)) {
            if(token.getAccessToken().equals(accessToken)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}
