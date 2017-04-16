package com.artisankid.elementwar.common.dao;

import com.artisankid.elementwar.common.ewmodel.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LevelDao {
	/**
	 * 查询全部等级对象
	 * @return
	 */
	public List<Level> selectAll() {
		String sql = "SELECT * FROM Level";

		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		List<Map<String, Object>> resultList = manager.select(sql);
		manager.close();

		ArrayList<Level> objects = new ArrayList<Level>();
		for (Map<String, Object> result : resultList) {
			Level object = new Level();
			object.setLevelID(result.get("levelID").toString());
			object.setCname(result.get("cname").toString());
			object.setEname(result.get("ename").toString());
			objects.add(object);
		}
		return objects;
	}
	
	/**
	 * 根据等级ID查询反应条件对象
	 * @param levelID
	 * @return
	 */
	public Level selectByLevelID(String levelID) {
		String sql = "SELECT * FROM Level WHERE levelID = '" + levelID + "';";

		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		Map<String, Object> result = manager.selectOne(sql);
		manager.close();
		
		if(result == null) {
			return null;
		}
		
		Level object = new Level();
		object.setLevelID(result.get("levelID").toString());
		object.setCname(result.get("cname").toString());
		object.setEname(result.get("ename").toString());
		return object;
	}
	
	/**
	 * 插入Level对象
	 * @param object
	 * @return
	 */
	public boolean insert(Level object) {
		String sql = "INSERT INTO Level (levelID, cname, ename) VALUES ('"
				+ object.getLevelID() + "', '"
				+ object.getCname() + "', '"
				+ object.getEname() + "');";

		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.insert(sql);
		manager.close();
		return result;
	}
	
	/**
	 * 更新Level对象
	 * @param object
	 * @return
	 */
	public boolean update(Level object) {
		String sql = "UPDATE Level SET "
				+ "cname = '" + object.getCname() + "' "
				+ "ename = '" + object.getEname() + "' "
				+ "WHERE levelID = '" + object.getLevelID() + "';";

		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.update(sql);
		manager.close();
		return result;
	}
	
	/**
	 * 删除Level对象
	 * @param levelID
	 * @return
	 */
	public boolean delete(String levelID) {
		String sql = "DELETE FROM Level WHERE levelID = '" + levelID + "';";
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.delete(sql);
		manager.close();
		return result;
	}
}
