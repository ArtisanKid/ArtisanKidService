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
	 * 根据连接状态查询用户
	 * 
	 * @param state
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public List<BaseMagician> selectByState(ConnectState state, int offset, int pageSize) {
		String sql = "SELECT openID, nickname, small_portrait, strength, honor FROM User LEFT JOIN Magician ON User.userID = Magician.userID WHERE connect_state = '" + state.getValue()
		+ "' LIMIT " + offset + "," + pageSize + ";";
		return this.selectMagiciansBySQL

				(sql);
	}
	
	/**
	 * 根据连接状态查询用户
	 * 
	 * @param openID
	 * @param relation
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public List<BaseMagician> selectByRelation(String openID, UserRelation relation, int offset, int pageSize) {
		String sql = "SELECT openID, nickname, small_portrait, strength, honor FROM User LEFT JOIN Magician ON User.userID = Magician.userID WHERE openID IN (SELECT openID FROM Magician_Magician WHERE openID = '" + openID + "' AND relation = '" + relation.getValue() + "' ORDER BY create_time LIMIT " + offset + "," + pageSize + ") ORDER BY create_time;";
		return this.selectMagiciansBySQL(sql);
	}

	/**
	 * 根据排名查询用户
	 * 
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public List<BaseMagician> selectByRank(int offset, int pageSize) {
		String sql = "SELECT openID, nickname, small_portrait, strength, honor FROM User LEFT JOIN Magician ON User.userID = Magician.userID ORDER BY rank DESC LIMIT " + offset + "," + pageSize + ";";
		return this.selectMagiciansBySQL(sql);
	}

	/**
	 * 根据房间查询用户
	 *
	 * @param roomID
	 * @return
	 */
	public List<BaseMagician> selectByRoomID(String roomID) {
		String sql = "SELECT openID, nickname, small_portrait, strength, honor FROM User LEFT JOIN Magician ON User.userID = Magician.userID WHERE openID IN (SELECT openID FROM Room_Magician WHERE roomID = '" + roomID + "');";
		return this.selectMagiciansBySQL(sql);
	}

	private List<BaseMagician> selectMagiciansBySQL(String sql) {
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		List<Map<String, Object>> result = manager.select(sql);
		manager.close();

		List<BaseMagician> objects = new ArrayList<>();
		for (Map<String, Object> map : result) {
			BaseMagician object = new BaseMagician();
			object.setOpenID((String) map.get("openID"));
			object.setNickname((String) map.get("nickname"));
			object.setSmallPortrait((String) map.get("small_portrait"));
			object.setStrength((Integer) map.get("strength"));
			object.setHonor((String) map.get("honor"));
			objects.add(object);
		}
		return objects;
	}

	/*
	 * 根据openID和登录平台查询用户
	 * 
	 * @param openID
	 * @return
	 */
	public Magician selectByOpenID(String openID) {
		String sql = "SELECT * FROM User LEFT JOIN Magician ON User.userID = Magician.userID WHERE openID = '" + openID + "';";
		return this.selectMagicianBySQL(sql);
	}

	/**
	 * 根据昵称查询用户
	 * 
	 * @param nickname
	 * @return
	 */
	public Magician selectByNickname(String nickname) {
		String sql = "SELECT * FROM User LEFT JOIN Magician ON User.userID = Magician.userID WHERE nickname = '" + nickname + "';";
		return this.selectMagicianBySQL(sql);
	}

	public Magician selectMagicianBySQL(String sql) {
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		Map<String, Object> result = manager.selectOne(sql);
		manager.close();

		if(result == null) {
			return null;
		}

		Magician object = new Magician();
		object.setOpenID((String) result.get("openID"));
		object.setNickname((String) result.get("nickname"));
		object.setSmallPortrait((String) result.get("small_portrait"));
		object.setStrength((Integer) result.get("strength"));
		object.setHonor((String) result.get("honor"));

		object.setPortrait((String) result.get("portrait"));
		object.setLargePortrait((String) result.get("large_portrait"));
		object.setMobile((String) result.get("mobile"));

		Date birthday = (Date)result.get("birthday");
		object.setBirthday(birthday.getTime());
		object.setMotto((String) result.get("motto"));

		int win_count = (Integer)result.get("win_count");
		int lose_count = (Integer)result.get("lose_count");
		int total_play_count = win_count + lose_count;
		if(total_play_count > 0) {
			object.setWinPercent(win_count / total_play_count);
		}

		long man_synthesis_count = (Long)result.get("man_synthesis_count");
		long auto_synthesis_count = (Long)result.get("auto_synthesis_count");
		long total_synthesis_count = auto_synthesis_count + auto_synthesis_count;
		if(total_synthesis_count > 0) {
			object.setManSynthesisPercent(man_synthesis_count / total_synthesis_count);
			object.setAutoSynthesisPercent(auto_synthesis_count / total_synthesis_count);
		}
		object.setRank((Long) result.get("rank"));

		return object;
	}

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
		List<Map<String, Object>> userResult = manager.select(selectUserSQL);
		if(userResult.size() == 0) {
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

			openID = "这里需要一个新的openID";
			String insertMagicianSQL = "INSERT INTO Magician (userID, openID) VALUES ('" + userID + "', '" + openID + "');";
			boolean insertMagicianResult = manager.insert(insertMagicianSQL);

			if(insertUnionUserResult && insertUserResult && insertUserUnionResult && insertMagicianResult) {

			}
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

			String userID = (String)userResult.get(0).get("userID");
			String selectMagicianSQL = "SELECT openID FROM Magician WHERE userID = '" + userID + "';";
			List<Map<String, Object>> magicianResult = manager.select(selectMagicianSQL);
			if(magicianResult.size() == 0) {
				//表示此三方账号没有登陆过应用
				//更新联合登录用户
				//创建openID
				openID = "这里需要一个新的openID";
				String insertMagicianSQL = "INSERT INTO Magician (userID, openID) VALUES ('" + userID + "', '" + openID + "');";
				boolean insertMagicianResult = manager.insert(insertMagicianSQL);

				if(updateUnionUserResult && insertMagicianResult) {

				}
			} else {
				//表示此三方账号登陆过应用
				openID = (String)magicianResult.get(0).get("openID");
			}
		}
		manager.close();
		return openID;
	}

	/**
	 * 更新用户信息
	 * 
	 * @param magician
	 * @return
	 */
	public boolean update(Magician magician) {
		String updateUserSQL = "UPDATE User SET "
				+ "nickname = '" + magician.getNickname() + "', "
				+ "portrait = '" + magician.getPortrait() + "', "
				+ "small_portrait = '" + magician.getSmallPortrait() + "', "
				+ "large_portrait = '" + magician.getLargePortrait() + "', "
				+ "mobile = '" + magician.getMobile() + "' "
				+ "WHERE userID = (SELECT userID FROM Magician WHERE openID = '" + magician.getOpenID() + "');";

		String updateMagicianSQL = "UPDATE Magician SET "
				+ "strength = '" + magician.getStrength() + "', "
				+ "honor = '" + magician.getHonor() + "' "
				+ "WHERE openID = '" + magician.getOpenID() + "';";

		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean updateUserResult = manager.update(updateUserSQL);
		boolean updateMagicianResult = manager.update(updateMagicianSQL);
		manager.close();
		return updateUserResult && updateMagicianResult;
	}

	/**
	 * 插入用户关系
	 * 
	 * @param openID
	 * @param targetOpenID
	 * @param relation
	 * @return
	 */
	public boolean insertRelation(String openID, String targetOpenID, UserRelation relation) {
		String sql = "INSERT INTO Magician_Relation (openID, ref_openID, type) VALUES ('"
				+ openID + "', '"
				+ targetOpenID + "', '"
				+ relation.getValue() + "');";

		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.insert(sql);
		manager.close();
		return result;
	}

	/**
	 * 更新用户关系
	 * 
	 * @param openID
	 * @param targetOpenID
	 * @param relation
	 * @return
	 */
	public boolean updateRelation(String openID, String targetOpenID, UserRelation relation) {
		String sql = "UPDATE Magician_Relation SET "
				+ "type = '" + relation.getValue() + "' "
				+ "WHERE openID = '" + openID + "' AND ref_openID = '" + targetOpenID + "';";

		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.insert(sql);
		manager.close();
		return result;
	}
}
