package com.artisankid.elementwar.common.dao;

import com.artisankid.elementwar.common.ewmodel.Balance;
import com.artisankid.elementwar.common.ewmodel.Balance.BalanceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BalanceDao {
	/**
	 * 根据平衡ID查询平衡对象
	 *
	 * @param balanceID
	 * @return
	 */
	public Balance selectByBalanceID(String balanceID) {
		String sql = "SELECT * FROM Balance WHERE balanceID = '" + balanceID + "';";

		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		Map<String, Object> result = manager.selectOne(sql);
		manager.close();

		if(result == null) {
			return null;
		}

		Balance object = new Balance();
		object.setBalanceID(result.get("balanceID").toString());
		object.setElementID(result.get("elementID").toString());
		object.setCount(Integer.parseInt(result.get("count").toString()));
		return object;
	}

	/**
	 * 根据方程式ID查询平衡对象
	 * 
	 * @return
	 */
	public List<Balance> selectByFormulaID(String formulaID, BalanceType type) {
		String sql = "SELECT * FROM Balance WHERE balanceID IN (SELECT balanceID FROM Formula_Balance WHERE formulaID = '" + formulaID + "' AND type = '" + type.getValue() + "');";

		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		List<Map<String, Object>> resultList = manager.select(sql);
		manager.close();

		ArrayList<Balance> objects = new ArrayList<>();
		for (Map<String, Object> result : resultList) {
			Balance object = new Balance();
			object.setBalanceID(result.get("balanceID").toString());
			object.setElementID(result.get("elementID").toString());
			object.setCount(Integer.parseInt(result.get("count").toString()));
			objects.add(object);
		}
		return objects;
	}

	/**
	 * 根据元素ID和数量查询平衡对象
	 *
	 * @param elementID
	 * @param count
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
		object.setBalanceID(result.get("balanceID").toString());
		object.setElementID(result.get("elementID").toString());
		object.setCount(Integer.parseInt(result.get("count").toString()));
		return object;
	}
	
	/**
	 * 插入Balance对象
	 *
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
	 * @param balanceID
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
