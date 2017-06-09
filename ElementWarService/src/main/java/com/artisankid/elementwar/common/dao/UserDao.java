package com.artisankid.elementwar.common.dao;

import com.artisankid.elementwar.common.ewmodel.User;
import com.artisankid.elementwar.common.ewmodel.Magician;
import com.artisankid.elementwar.common.ewmodel.BaseMagician;
import com.artisankid.elementwar.common.ewmodel.BaseMagician.ConnectState;
import com.artisankid.elementwar.common.ewmodel.BaseMagician.UserRelation;
import com.artisankid.elementwar.common.utils.MagicianManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserDao {
    /*
     * 根据unionID和登录类型查询用户
     *
     * @param unionID
     * @param type
     * @return
     */
    public User selectByUnionID(String unionID, User.LoginType type) {
        String selectUserSQL = "SELECT userID FROM User_Union WHERE unionID = '" + unionID + "' AND platform = '" + type.getValue() + "';";

        DatabaseManager manager = new DatabaseManager();
        manager.connection();
        Map<String, Object> userResult = manager.selectOne(selectUserSQL);
        manager.close();

        if (userResult == null) {
            return null;
        }

        User user = new User();
        user.setUserID(userResult.get("userID").toString());
        return user;
    }

    private String tableName(User.LoginType type) {
        String thirdPartyUserTable = null;
        switch (type) {
            case Wechat:
                thirdPartyUserTable = "WeChatUser";
                break;
            case QQ:
                thirdPartyUserTable = "QQUser";
                break;
            case Weibo:
                thirdPartyUserTable = "WeiboUser";
                break;
            default:
                break;
        }
        return thirdPartyUserTable;
    }

    /**
     * 添加联合登录用户
     *
     * @param user
     * @return 返回联合登录用户对应的userID
     */
    public String insert(User user) {
        String thirdPartyUserTable = tableName(user.getLoginType());
        DatabaseManager manager = new DatabaseManager();
        manager.connection();

        //表示此三方账号没有登陆过平台
        //插入联合登录用户
        String insertUnionUserSQL = "INSERT INTO " + thirdPartyUserTable + " (unionID, openID, nickname, portrait, small_portrait, large_portrait, mobile, motto, access_token, access_token_expired_time, refresh_token) VALUES ('"
                + user.getUnionID() + "', '"
                + user.getOpenID() + "', '"
                + user.getNickname() + "', '"
                + user.getPortrait() + "', '"
                + user.getSmallPortrait() + "', '"
                + user.getLargePortrait() + "', '"
                + user.getMobile() + "', '"
                + user.getMotto() + "', '"
                + user.getToken().getAccessToken() + "', "
                + "?, '"
                + user.getToken().getRefreshToken() + "');";

        Long expiredTime = new Double(user.getToken().getExpiredTime() * 1000).longValue();
        final Timestamp accessTokenExpiredTimestamp = new Timestamp(expiredTime);
        boolean insertUnionUserResult = manager.update(insertUnionUserSQL, new StatementHandler() {
            @Override
            public void supplyToStatement(PreparedStatement statement) {
                try {
                    statement.setTimestamp(1, accessTokenExpiredTimestamp);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        String userID = MagicianManager.CreateUserID();
        String insertUserSQL = "INSERT INTO User (userID, nickname, portrait, small_portrait, large_portrait, mobile, motto) VALUES ('"
                + userID + "', '"
                + user.getNickname() + "', '"
                + user.getPortrait() + "', '"
                + user.getSmallPortrait() + "', '"
                + user.getLargePortrait() + "', '"
                + user.getMobile() + "', '"
                + user.getMotto() + "');";
        boolean insertUserResult = manager.insert(insertUserSQL);

        String insertUserUnionSQL = "INSERT INTO User_Union (userID, unionID, platform) VALUES ('" + userID + "', '" + user.getUnionID() + "', '" + user.getLoginType().getValue() + "');";
        boolean insertUserUnionResult = manager.insert(insertUserUnionSQL);
        manager.close();

        MagicianDao magicianDao = new MagicianDao();
        boolean insertMagicianResult = magicianDao.insert(userID);

        if (insertUnionUserResult && insertUserResult && insertUserUnionResult && insertMagicianResult) {

        }
        return userID;
    }

    /**
     * 更新联合登录用户
     *
     * @param user
     * @return 返回联合登录用户对应的userID
     */
    public String update(User user) {
        String thirdPartyUserTable = tableName(user.getLoginType());

        DatabaseManager manager = new DatabaseManager();
        manager.connection();

        //表示此三方账号登陆过平台
        String updateUnionUserSQL = "UPDATE " + thirdPartyUserTable + " SET "
                + "openID = '" + user.getOpenID() + "', "
                + "nickname = '" + user.getNickname() + "', "
                + "portrait = '" + user.getPortrait() + "', "
                + "small_portrait = '" + user.getSmallPortrait() + "', "
                + "large_portrait = '" + user.getLargePortrait() + "', "
                + "mobile = '" + user.getMobile() + "', "
                + "access_token = '" + user.getToken().getAccessToken() + "', "
                + "access_token_expired_time = ?, "
                + "refresh_token = '" + user.getToken().getRefreshToken() + "' "
                + "WHERE unionID = '" + user.getUnionID() + "';";

        Long expiredTime = new Double(user.getToken().getExpiredTime() * 1000).longValue();
        final Timestamp accessTokenExpiredTimestamp = new Timestamp(expiredTime);
        boolean updateUnionUserResult = manager.update(updateUnionUserSQL, new StatementHandler() {
            @Override
            public void supplyToStatement(PreparedStatement statement) {
                try {
                    statement.setTimestamp(1, accessTokenExpiredTimestamp);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        manager.close();

        String userID = user.getUserID();
        MagicianDao magicianDao = new MagicianDao();
        if (magicianDao.selectByUserID(userID) == null) {
            //表示此三方账号没有登陆过应用
            //更新联合登录用户
            boolean insertMagicianResult = magicianDao.insert(userID);
            if (updateUnionUserResult && insertMagicianResult) {

            }
        }

        return userID;
    }
}
