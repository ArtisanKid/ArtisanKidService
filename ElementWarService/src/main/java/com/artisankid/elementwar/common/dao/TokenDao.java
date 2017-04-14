package com.artisankid.elementwar.common.dao;

import com.artisankid.elementwar.common.ewmodel.Reaction;
import com.artisankid.elementwar.common.ewmodel.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TokenDao {
	/**
	 * 查询全部Token对象
	 * @return
	 */
	public List<Token> selectAll() {
		String sql = "SELECT * FROM User_Token";
		
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		List<Map<String, Object>> result = manager.select(sql);
		manager.close();

		ArrayList<Token> objects = new ArrayList<Token>();
		for (Map<String, Object> map : result) {
			Token object = new Token();
			object.setAccessToken((String) map.get("access_token"));
			object.setRefreshToken((String) map.get("refresh_token"));
			object.setExpiredTime((Double) map.get("access_token_expired_time"));
			objects.add(object);
		}
		return objects;
	}
	
	/**
	 * 根据方程式ID查询反应类型对象
	 * @return
	 */
	public List<Reaction> selectByFormulaID(String formulaID) {
		String sql = "SELECT Reaction.reactionID, name FROM Formula_Reaction LEFT JOIN Reaction ON Formula_Reaction.reactionID = Reaction.reactionID WHERE formulaID = '"
				+ formulaID + "';";
		
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		List<Map<String, Object>> result = manager.select(sql);
		manager.close();

		ArrayList<Reaction> objects = new ArrayList<Reaction>();
		for (Map<String, Object> map : result) {
			Reaction object = new Reaction();
			object.setReactionID((String) map.get("reactionID"));
			object.setName((String) map.get("name"));
			objects.add(object);
		}
		return objects;
	}
	
	/**
	 * 根据accessToken查询Token
	 * @param accessToken
	 * @return
	 */
	public Token selectByAccessToken(String accessToken) {
		String sql = "SELECT * FROM User_Token WHERE access_token = '" + accessToken + "';";
		
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		Map<String, Object> result = manager.selectOne(sql);
		manager.close();
		
		if(result == null) {
			return null;
		}
		
		Token object = new Token();
		object.setAccessToken((String) result.get("access_token"));
		object.setRefreshToken((String) result.get("refresh_token"));
		object.setExpiredTime((Double) result.get("access_token_expired_time"));
		return object;
	}
	
	/**
	 * 根据refreshToken查询Token
	 * @param refreshToken
	 * @return
	 */
	public Token selectByRefreshToken(String refreshToken) {
		String sql = "SELECT * FROM User_Token WHERE refresh_token = '" + refreshToken + "';";
		
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		Map<String, Object> result = manager.selectOne(sql);
		manager.close();
		
		if(result == null) {
			return null;
		}
		
		Token object = new Token();
		object.setAccessToken((String) result.get("access_token"));
		object.setRefreshToken((String) result.get("refresh_token"));
		object.setExpiredTime((Double) result.get("access_token_expired_time"));
		return object;
	}
	
	/**
	 * 插入Token对象
	 * @param object
	 * @return
	 */
	public boolean insert(String openID, Token object) {
		double accessTokenExpiredTime = object.getExpiredTime();
		double refreshTokenExpiredTime = accessTokenExpiredTime + 30 * 24 * 60 * 60;//refreshToken过期时间 天*小时*分钟*秒
		String sql = "INSERT INTO User_Token (openID, access_token, access_token_expired_time, refresh_token, refresh_token_expired_time) VALUES ('" + openID + "', '" + object.getAccessToken() + "', '" + accessTokenExpiredTime +"', '" + object.getRefreshToken() +"', '" + refreshTokenExpiredTime +"');";
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.insert(sql);
		manager.close();
		return result;
	}
	
	/**
	 * 更新Token对象
	 * @param object
	 * @return
	 */
	public boolean update(String accessToken, Token object) {
		double accessTokenExpiredTime = object.getExpiredTime();
		double refreshTokenExpiredTime = accessTokenExpiredTime + 30 * 24 * 60 * 60;//refreshToken过期时间 天*小时*分钟*秒
		String sql = "UPDATE User_Token SET access_token = '" + object.getAccessToken() + "', access_token_expired_time = '" + accessTokenExpiredTime + "', refresh_token = '"+ object.getRefreshToken() + "', refresh_token_expired_time = '" + refreshTokenExpiredTime + "' WHERE access_token = '" + accessToken + "';";
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.update(sql);
		manager.close();
		return result;
	}
	
	/**
	 * 删除指定accessToken的Token对象
	 * @param object
	 * @return
	 */
	public boolean delete(String accessToken) {
		String sql = "DELETE FROM User_Token WHERE access_token = '" + accessToken + "';";
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.delete(sql);
		manager.close();
		return result;
	}
}
