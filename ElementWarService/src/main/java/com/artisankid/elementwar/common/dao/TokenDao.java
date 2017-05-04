package com.artisankid.elementwar.common.dao;

import com.artisankid.elementwar.common.ewmodel.Token;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TokenDao {
	/**
	 * 查询全部Token对象
	 * @return
	 */
	public List<Token> selectAll() {
		String sql = "SELECT * FROM Magician_Token";
		return selectBySQL(sql);
	}

	/**
	 * 根据openID查询Token
	 * @param openID
	 * @return
	 */
	public List<Token> selectByOpenID(String openID) {
		String sql = "SELECT * FROM Magician_Token WHERE openID = '" + openID + "';";
		return selectBySQL(sql);
	}

	private List<Token> selectBySQL(String sql) {
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		List<Map<String, Object>> result = manager.select(sql);
		manager.close();

		ArrayList<Token> objects = new ArrayList<>();
		for (Map<String, Object> map : result) {
			Token object = new Token();
			object.setAccessToken(map.get("access_token").toString());
			object.setRefreshToken(map.get("refresh_token").toString());

			Timestamp accessTokenExpiredTime = (Timestamp)map.get("access_token_expired_time");
			object.setExpiredTime(accessTokenExpiredTime.getTime());
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
		String sql = "SELECT * FROM Magician_Token WHERE access_token = '" + accessToken + "';";
		
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		Map<String, Object> result = manager.selectOne(sql);
		manager.close();
		
		if(result == null) {
			return null;
		}
		
		Token object = new Token();
		object.setAccessToken(result.get("access_token").toString());
		object.setRefreshToken(result.get("refresh_token").toString());

		Timestamp accessTokenExpiredTime = (Timestamp)result.get("access_token_expired_time");
		object.setExpiredTime(accessTokenExpiredTime.getTime());
		return object;
	}
	
	/**
	 * 根据refreshToken查询Token
	 * @param refreshToken
	 * @return
	 */
	public Token selectByRefreshToken(String refreshToken) {
		String sql = "SELECT * FROM Magician_Token WHERE refresh_token = '" + refreshToken + "';";
		
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		Map<String, Object> result = manager.selectOne(sql);
		manager.close();
		
		if(result == null) {
			return null;
		}
		
		Token object = new Token();
		object.setAccessToken(result.get("access_token").toString());
		object.setRefreshToken(result.get("refresh_token").toString());

		Timestamp accessTokenExpiredTime = (Timestamp)result.get("access_token_expired_time");
		object.setExpiredTime(accessTokenExpiredTime.getTime());
		return object;
	}
	
	/**
	 * 插入Token对象
	 * @param object
	 * @return
	 */
	public boolean insert(String openID, Token object) {
		String sql = "INSERT INTO Magician_Token (openID, access_token, access_token_expired_time, refresh_token, refresh_token_expired_time) VALUES ('"
				+ openID + "', '"
				+ object.getAccessToken() + "', "
				+ "?, '"
				+ object.getRefreshToken() + "', "
				+ "?);";

		Long accessTokenExpiredTime = object.getExpiredTime();
		final Timestamp accessTokenExpiredTimestamp = new Timestamp(accessTokenExpiredTime);

		Long refreshTokenExpiredTime = accessTokenExpiredTime + 30 * 24 * 60 * 60 * 1000;//refreshToken过期时间 天*小时*分钟*秒
		final Timestamp refreshTokenExpiredTimestamp = new Timestamp(refreshTokenExpiredTime);

		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.update(sql, new StatementHandler() {
			@Override
			public void supplyToStatement(PreparedStatement statement) {
				try {
					statement.setTimestamp(1, accessTokenExpiredTimestamp);
					statement.setTimestamp(2, refreshTokenExpiredTimestamp);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		manager.close();
		return result;
	}
	
	/**
	 * 更新Token对象
	 * @param object
	 * @return
	 */
	public boolean update(Token object) {
		String sql = "UPDATE User_Token SET "
				+ "access_token = '" + object.getAccessToken() + "', "
				+ "access_token_expired_time = ?, "
				+ "refresh_token = '" + object.getRefreshToken() + "', "
				+ "refresh_token_expired_time = ? "
				+ "WHERE refresh_token = '" + object.getRefreshToken() + "';";

		Long accessTokenExpiredTime = object.getExpiredTime();
		final Timestamp accessTokenExpiredTimestamp = new Timestamp(accessTokenExpiredTime);

		Long refreshTokenExpiredTime = accessTokenExpiredTime + 30 * 24 * 60 * 60 * 1000;//refreshToken过期时间 天*小时*分钟*秒
		final Timestamp refreshTokenExpiredTimestamp = new Timestamp(refreshTokenExpiredTime);

		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.update(sql, new StatementHandler() {
			@Override
			public void supplyToStatement(PreparedStatement statement) {
				try {
					statement.setTimestamp(1, accessTokenExpiredTimestamp);
					statement.setTimestamp(2, refreshTokenExpiredTimestamp);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		manager.close();
		return result;
	}
	
	/**
	 * 删除指定accessToken的Token对象
	 * @param accessToken
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
