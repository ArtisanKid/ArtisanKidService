package com.artisankid.elementwar.common.dao;

import com.artisankid.elementwar.common.ewmodel.Reaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReactionDao {
	/**
	 * 查询全部反应类型对象
	 * @return
	 */
	public List<Reaction> selectAll() {
		String sql = "SELECT * FROM Reaction";
		
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
	 * 根据反应类型ID查询反应类型对象
	 * @param reactionID
	 * @return
	 */
	public Reaction selectByReactionID(String reactionID) {
		String sql = "SELECT * FROM Reaction WHERE reactionID = '" + reactionID + "';";
		
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		Map<String, Object> result = manager.selectOne(sql);
		manager.close();
		
		if(result == null) {
			return null;
		}
		
		Reaction object = new Reaction();
		object.setReactionID((String) result.get("reactionID"));
		object.setName((String) result.get("name"));
		return object;
	}
	
	/**
	 * 插入Reaction对象
	 * @param object
	 * @return
	 */
	public boolean insert(Reaction object) {
		String sql = "INSERT INTO Reaction (reactionID, name) VALUES ('" + object.getReactionID() + "', '" + object.getName() + "');";
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.insert(sql);
		manager.close();
		return result;
	}
	
	/**
	 * 更新Reaction对象
	 * @param object
	 * @return
	 */
	public boolean update(Reaction object) {
		String sql = "UPDATE Reaction SET name = '" + object.getName() + "' WHERE reactionID = '" + object.getReactionID() + "';";
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.update(sql);
		manager.close();
		return result;
	}
	
	/**
	 * 删除Reaction对象
	 * @param object
	 * @return
	 */
	public boolean delete(String reactionID) {
		String sql = "DELETE FROM Reaction WHERE reactionID = '" + reactionID + "';";
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.delete(sql);
		manager.close();
		return result;
	}
}
