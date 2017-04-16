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
		return selectSQL(sql);
	}
	
	/**
	 * 根据方程式ID查询反应条件对象
	 * 
	 * @return
	 */
	public List<Condition> selectByFormulaID(String formulaID) {
		String sql = "SELECT * FROM ElementWar.Condition WHERE conditionID IN (SELECT conditionID FROM Formula_Condition WHERE formulaID = '"
				+ formulaID + "');";
		return selectSQL(sql);
	}

	private List<Condition> selectSQL(String sql) {
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		List<Map<String, Object>> resultList = manager.select(sql);
		manager.close();

		ArrayList<Condition> objectList = new ArrayList<>();
		for (Map<String, Object> result : resultList) {
			Condition object = new Condition();
			object.setConditionID(result.get("conditionID").toString());
			object.setCname(result.get("cname").toString());
			object.setEname(result.get("ename").toString());
			objectList.add(object);
		}
		return objectList;
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
		object.setConditionID(result.get("conditionD").toString());
		object.setCname(result.get("cname").toString());
		object.setEname(result.get("ename").toString());
		return object;
	}
	
	/**
	 * 插入Condition对象
	 * @param object
	 * @return
	 */
	public boolean insert(Condition object) {
		String sql = "INSERT INTO ElementWar.Condition (conditionID, cname, ename) VALUES ('"
				+ object.getConditionID() + "', '"
				+ object.getEname() + "', '"
				+ object.getEname() + "');";
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
		String sql = "UPDATE ElementWar.Condition SET "
				+ "cname = '" + object.getCname() + "' "
				+ "ename = '" + object.getEname() + "' "
				+ "WHERE conditionID = '" + object.getConditionID() + "';";
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.update(sql);
		manager.close();
		return result;
	}
	
	/**
	 * 删除Condition对象
	 * @param conditionID
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
