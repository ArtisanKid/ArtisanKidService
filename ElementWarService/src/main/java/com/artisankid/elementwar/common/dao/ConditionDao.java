package com.artisankid.elementwar.common.dao;

import com.artisankid.elementwar.common.ewmodel.Condition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConditionDao {
	/**
	 * 查询全部反应条件对象
	 * 
	 * @return
	 */
	public List<Condition> selectAll() {
		String sql = "SELECT * FROM ElementWar.Condition";

		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		List<Map<String, Object>> result = manager.select(sql);
		manager.close();

		ArrayList<Condition> objects = new ArrayList<Condition>();
		for (Map<String, Object> map : result) {
			Condition object = new Condition();
			object.setConditionID((String) map.get("conditionD"));
			object.setName((String) map.get("name"));
			objects.add(object);
		}
		return objects;
	}
	
	/**
	 * 根据方程式ID查询反应条件对象
	 * 
	 * @return
	 */
	public List<Condition> selectByFormulaID(String formulaID) {
		String sql = "SELECT Condition.conditionID, name FROM Formula_Condition LEFT JOIN ElementWar.Condition ON Formula_Condition.conditionID = Condition.conditionID WHERE formulaID = '"
				+ formulaID + "';";

		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		List<Map<String, Object>> result = manager.select(sql);
		manager.close();

		ArrayList<Condition> objects = new ArrayList<Condition>();
		for (Map<String, Object> map : result) {
			Condition object = new Condition();
			object.setConditionID((String) map.get("conditionD"));
			object.setName((String) map.get("name"));
			objects.add(object);
		}
		return objects;
	}

	/**
	 * 根据条件ID查询反应条件对象
	 * 
	 * @param conditionID
	 * @return
	 */
	public Condition selectByConditionID(String conditionID) {
		String sql = "SELECT * FROM ElementWar.Condition WHERE conditionID = '" + conditionID + "';";

		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		Map<String, Object> result = manager.selectOne(sql);
		manager.close();
		
		if(result == null) {
			return null;
		}

		Condition object = new Condition();
		object.setConditionID((String) result.get("conditionID"));
		object.setName((String) result.get("name"));
		return object;
	}
	
	/**
	 * 插入Condition对象
	 * @param object
	 * @return
	 */
	public boolean insert(Condition object) {
		String sql = "INSERT INTO ElementWar.Condition (conditionID, name) VALUES ('" + object.getConditionID() + "', '"
				+ object.getName() + "');";
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.insert(sql);
		manager.close();
		return result;
	}
	
	/**
	 * 更新Condition对象
	 * @param object
	 * @return
	 */
	public boolean update(Condition object) {
		String sql = "UPDATE ElementWar.Condition SET name = '" + object.getName() + "' WHERE conditionID = '" + object.getConditionID() + "';";
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.update(sql);
		manager.close();
		return result;
	}
	
	/**
	 * 删除Condition对象
	 * @param object
	 * @return
	 */
	public boolean delete(String conditionID) {
		String sql = "DELETE FROM ElementWar.Condition WHERE conditionID = '" + conditionID + "';";
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.delete(sql);
		manager.close();
		return result;
	}
}
