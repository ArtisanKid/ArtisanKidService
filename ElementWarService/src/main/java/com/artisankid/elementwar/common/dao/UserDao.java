package com.artisankid.elementwar.common.dao;

import com.artisankid.elementwar.common.ewmodel.User;
import com.artisankid.elementwar.common.ewmodel.Magician;
import com.artisankid.elementwar.common.ewmodel.BaseMagician;
import com.artisankid.elementwar.common.ewmodel.BaseMagician.ConnectState;
import com.artisankid.elementwar.common.ewmodel.BaseMagician.UserRelation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserDao {
	/**
	 * 添加联合登录用户
	 * 
	 * @param user
	 * @return 返回联合登录用户对应的openID
	 */
	public String insert(User user) {
		//添加的联合登录用户
		String thirdPartyUserTable = null;
		switch (user.getLoginType()) {
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

		//将要返回的openID
		String openID;

		DatabaseManager manager = new DatabaseManager();
		manager.connection();

		String selectUserSQL = "SELECT userID FROM User_Union WHERE unionID = '" + user.getUnionID()+ "' AND platform = '" + user.getLoginType().getValue() + "';";
		Map<String, Object> userResult = manager.selectOne(selectUserSQL);
		if(userResult == null) {
			//表示此三方账号没有登陆过平台
			//插入联合登录用户
			//创建userID
			//创建openID
			String insertUnionUserSQL = "INSERT INTO " +  thirdPartyUserTable + " (unionID, openID, nickname, portrait, small_portrait, large_portrait, mobile, motto, access_token, access_token_expired_time, refresh_token) VALUES ('"
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

			final Timestamp accessTokenExpiredTimestamp = new Timestamp(user.getToken().getExpiredTime());
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

			//TODO:创建唯一的userID
			String userID = "这里需要一个新的userID";
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

			MagicianDao magicianDao = new MagicianDao();
			boolean insertMagicianResult = magicianDao.insert(userID);

			if(insertUnionUserResult && insertUserResult && insertUserUnionResult && insertMagicianResult) {

			}

			openID = magicianDao.selectByUserID(userID).getOpenID();
		} else {
			//表示此三方账号登陆过平台
			String updateUnionUserSQL = "UPDATE " +  thirdPartyUserTable + " SET "
					+ "openID = '" + user.getOpenID() + "', "
					+ "nickname = '" + user.getNickname() + "', "
					+ "portrait = '" + user.getPortrait() + "', "
					+ "getSmallPortrait = '" + user.getSmallPortrait() + "', "
					+ "large_portrait = '" + user.getLargePortrait() + "', "
					+ "mobile = '" + user.getMobile() + "', "
					+ "access_token = '" + user.getToken().getAccessToken() + "', "
					+ "access_token_expired_time = ?, "
					+ "refresh_token = '" + user.getToken().getRefreshToken() + "' "
					+ "WHERE unionID = '" + user.getUnionID() + "';";

			final Timestamp accessTokenExpiredTimestamp = new Timestamp(user.getToken().getExpiredTime());
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

			String userID = userResult.get("userID").toString();

			MagicianDao magicianDao = new MagicianDao();
			Magician magician = magicianDao.selectByUserID(userID);
			if(magician == null) {
				//表示此三方账号没有登陆过应用
				//更新联合登录用户
				//创建openID
				boolean insertMagicianResult = magicianDao.insert(userID);

				if(updateUnionUserResult && insertMagicianResult) {

				}

				magician = magicianDao.selectByUserID(userID);
			}

			openID = magician.getOpenID();
		}
		manager.close();
		return openID;
	}
}
