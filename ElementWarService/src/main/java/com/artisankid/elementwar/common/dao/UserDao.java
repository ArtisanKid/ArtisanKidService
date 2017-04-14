package com.artisankid.elementwar.common.dao;

import com.artisankid.elementwar.common.ewmodel.BaseUser;
import com.artisankid.elementwar.common.ewmodel.BaseUser.ConnectState;
import com.artisankid.elementwar.common.ewmodel.BaseUser.UserRelation;
import com.artisankid.elementwar.common.ewmodel.User;

import java.util.ArrayList;
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
	public List<BaseUser> selectByState(ConnectState state, int offset, int pageSize) {
		String sql = "SELECT openID, nickname, small_portrait, strength, honor FROM User WHERE connect_state = '" + state.getValue()
		+ "' LIMIT " + offset + "," + pageSize + ";";
		return this.selectUsersBySQL(sql);
	}
	
	/**
	 * 根据连接状态查询用户
	 * 
	 * @param state
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public List<BaseUser> selectByRelation(UserRelation relation, int offset, int pageSize) {
		String sql = "SELECT openID, nickname, small_portrait, strength, honor FROM User WHERE connect_state = '" + relation.getValue()
		+ "' LIMIT " + offset + "," + pageSize + ";";
		return this.selectUsersBySQL(sql);
	}

	/**
	 * 根据排名查询用户
	 * 
	 * @param offset
	 * @param pageSize
	 * @param state
	 * @return
	 */
	public List<BaseUser> selectByRank(int offset, int pageSize) {
		String sql = "SELECT openID, nickname, small_portrait, strength, honor FROM User ORDER BY rank DESC LIMIT "
				+ offset + "," + pageSize + ";";
		return this.selectUsersBySQL(sql);
	}

	public List<BaseUser> selectUsersBySQL(String sql) {
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		List<Map<String, Object>> result = manager.select(sql);
		manager.close();

		ArrayList<BaseUser> objects = new ArrayList<BaseUser>();
		for (Map<String, Object> map : result) {
			BaseUser object = new BaseUser();
			object.setOpenID((String) map.get("openID"));
			object.setNickname((String) map.get("nickname"));
			object.setSmallPortrait((String) map.get("small_portrait"));
			object.setStrength((Integer) map.get("strength"));
			object.setHonor((String) map.get("honor"));
			objects.add(object);
		}
		return objects;
	}

	/**
	 * 根据openID和登录平台查询用户
	 * 
	 * @param openID
	 * @param type
	 * @return
	 */
	public User selectByOpenID(String openID) {
		String sql = "SELECT * FROM User WHERE openID = '" + openID + "';";
		return this.selectUserBySQL(sql);
	}

	/**
	 * 根据昵称查询用户
	 * 
	 * @param openID
	 * @param type
	 * @return
	 */
	public User selectByNickname(String nickname) {
		String sql = "SELECT * FROM User WHERE nickname = " + nickname + ";";
		return this.selectUserBySQL(sql);
	}

	public User selectUserBySQL(String sql) {
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		Map<String, Object> result = manager.selectOne(sql);
		manager.close();

		if(result == null) {
			return null;
		}

		User object = new User();
		object.setOpenID((String) result.get("openID"));
		object.setNickname((String) result.get("nickname"));
		object.setSmallPortrait((String) result.get("small_portrait"));
		object.setStrength((Integer) result.get("strength"));
		object.setHonor((String) result.get("honor"));

		object.setPortrait((String) result.get("portrait"));
		object.setLargePortrait((String) result.get("large_portrait"));
		object.setMobile((String) result.get("mobile"));
		object.setBirthday((Double) result.get("birthday"));
		object.setMotto((String) result.get("motto"));
		object.setWinPercent((Double) result.get("win_percent"));
		object.setManSynthesisPercent((Double) result.get("man_synthesis_percent"));
		object.setAutoSynthesisPercent((Double) result.get("auto_synthesis_percent"));
		object.setRank((Integer) result.get("rank"));

		return object;
	}

	/**
	 * 添加新用户
	 * 
	 * @param user
	 * @return
	 */
	public boolean insert(User user) {
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
		String insertThirdPartyUserSQL = "INSERT INTO " +  thirdPartyUserTable + " (openID, nickname, portrait, small_portrait, large_portrait, mobile, motto, access_token, access_token_expired_time, refresh_token) VALUES ('"
				+ user.getOpenID() + "','" + user.getNickname() + "','" + user.getPortrait() + "','"
				+ user.getSmallPortrait() + "','" + user.getLargePortrait() + "','" + user.getMobile() + "','"
				+ user.getMotto() + "', '" + user.getToken().getAccessToken() + "', '" + user.getToken().getExpiredTime() + "', '" + user.getToken().getRefreshToken()
				+ "');";

		String openID = User.createUniqueOpenID();

		String insertUserSQL = "INSERT INTO User (openID, nickname, portrait, small_portrait, large_portrait, mobile, motto) VALUES ('"
				+ openID + "','" + user.getNickname() + "','" + user.getPortrait() + "','"
				+ user.getSmallPortrait() + "','" + user.getLargePortrait() + "','" + user.getMobile() + "','"
				+ "', '" + user.getMotto()
				+ "');";

		String insertUserUnionSQL = "INSERT INTO User_Union (openID, third_party_openID, login_type) VALUES ('"
				+ openID + "','" + user.getOpenID() + "','" + user.getLoginType().getValue()
				+ "');";

		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean insertThirdPartyUserResult = manager.insert(insertThirdPartyUserSQL);
		boolean insertUserResult = manager.insert(insertUserSQL);
		boolean insertUserUnionResult = manager.insert(insertUserUnionSQL);
		manager.close();
		return insertThirdPartyUserResult && insertUserResult && insertUserUnionResult;
	}

	/**
	 * 更新用户信息
	 * 
	 * @param user
	 * @return
	 */
	public boolean update(User user) {
		String sql = "UPDATE User SET nickname = '" + user.getNickname() + "', portrait = '" + user.getPortrait()
		+ "', small_portrait = '" + user.getSmallPortrait() + "', large_portrait = '" + user.getLargePortrait()
		+ "', mobile = '" + user.getMobile() + "', strength = '"
		+ user.getStrength() + "', honor = '" + user.getHonor() + "', motto = '" + user.getMotto()
		+ "' WHERE openID = '" + user.getOpenID() + "';";

		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.insert(sql);
		manager.close();
		return result;
	}

	/**
	 * 插入用户关系
	 * 
	 * @param userID
	 * @param targetUserID
	 * @param relation
	 * @return
	 */
	public boolean insertRelation(String openID, String targetOpenID, UserRelation relation) {
		String sql = "INSERT INTO User (openID, ref_openID, type) VALUES ('" + openID + "','" + targetOpenID + "','"
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
	 * @param userID
	 * @param targetUserID
	 * @param relation
	 * @return
	 */
	public boolean updateRelation(String openID, String targetOpenID, User.UserRelation relation) {
		String sql = "UPDATE User SET type = '" + relation + "' WHERE openID = '" + openID + "' AND ref_openID = '"
				+ targetOpenID + "';";

		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.insert(sql);
		manager.close();
		return result;
	}
}
