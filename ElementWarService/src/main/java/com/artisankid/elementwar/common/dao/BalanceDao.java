package com.artisankid.elementwar.common.dao;

import com.artisankid.elementwar.common.ewmodel.Balance;
import com.artisankid.elementwar.common.ewmodel.Balance.BalenceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BalanceDao {
	/**
	 * 根据方程式ID查询平衡对象
	 * 
	 * @return
	 */
	public List<Balance> selectByFormulaID(String formulaID, BalenceType type) {
		String sql = "SELECT Balance.balanceID, elementID, count FROM Formula_Balance LEFT JOIN Balance ON Formula_Balance.balanceID = Balance.balanceID WHERE formulaID = '"
				+ formulaID + "' AND type = '" + type.getValue() + "';";

		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		List<Map<String, Object>> result = manager.select(sql);
		manager.close();

		ArrayList<Balance> objects = new ArrayList<Balance>();
		for (Map<String, Object> map : result) {
			Balance object = new Balance();
			object.setBalanceID((String) map.get("balanceID"));
			object.setElementID((String) map.get("elementID"));
			object.setCount((Integer) map.get("count"));
			objects.add(object);
		}
		return objects;
	}
	
	/**
	 * 根据元素ID和数量查询平衡对象
	 * 
	 * @return
	 */
	public Balance selectByElementID(String elementID, int count) {
		String sql = "SELECT * FROM Balance WHERE elementID = '"
				+ elementID + "' AND count = '" + count + "';";

		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		Map<String, Object> result = manager.selectOne(sql);
		manager.close();

		if(result == null) {
			return null;
		}
		
		Balance object = new Balance();
		object.setBalanceID((String) result.get("balanceID"));
		object.setElementID((String) result.get("elementID"));
		object.setCount((Integer) result.get("count"));
		return object;
	}
	
	/**
	 * 插入Balance对象
	 * @param object
	 * @return
	 */
	public boolean insert(Balance object) {
		String sql = "INSERT INTO Balance (balanceID, elementID, count) VALUES ('" + object.getBalanceID() + "', '"
				+ object.getElementID() + "', '" + object.getCount() + "');";
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.insert(sql);
		manager.close();
		return result;
	}
	
	/**
	 * 更新Balance对象
	 * @param object
	 * @return
	 */
	public boolean update(Balance object) {
		String sql = "UPDATE Balance SET elementID = '" + object.getElementID() + "', count = '" + object.getCount() + "' WHERE balanceID = '" + object.getBalanceID() + "';";
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.update(sql);
		manager.close();
		return result;
	}
	
	/**
	 * 删除Balance对象
	 * @param object
	 * @return
	 */
	public boolean delete(String balanceID) {
		String sql = "DELETE FROM Balance WHERE balanceID = '" + balanceID + "';";
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.delete(sql);
		manager.close();
		return result;
	}
}
