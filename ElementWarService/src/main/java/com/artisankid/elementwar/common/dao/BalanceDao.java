package com.artisankid.elementwar.common.dao;

import com.artisankid.elementwar.common.ewmodel.Balance;
import com.artisankid.elementwar.common.ewmodel.Balance.BalanceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BalanceDao {
	/**
	 * 根据方程式ID查询平衡对象
	 * 
	 * @return
	 */
	public List<Balance> selectByFormulaID(String formulaID, BalanceType type) {
		String sql = "SELECT * FROM Formula_Element WHERE formulaID = '" + formulaID + "' AND type = '" + type.getValue() + "';";

		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		List<Map<String, Object>> resultList = manager.select(sql);
		manager.close();

		ArrayList<Balance> objects = new ArrayList<>();
		for (Map<String, Object> result : resultList) {
			Balance object = new Balance();
			object.setElementID(result.get("elementID").toString());
			object.setCount(Integer.parseInt(result.get("count").toString()));
			objects.add(object);
		}
		return objects;
	}

    /**
     * 更新公式平衡项
     * @param formulaID String
     * @param object Balance
     * @return boolean
     */
	public boolean update(String formulaID, Balance object) {
		String sql = "UPDATE Formula_Element SET count = '" + object.getCount() + "' WHERE formulaID = '" + formulaID + "' AND elementID = '" + object.getElementID() + "';";
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.update(sql);
		manager.close();
		return result;
	}

    /**
     * 删除公式平衡项
     * @param formulaID String
     * @param object Balance
     * @return boolean
     */
	public boolean delete(String formulaID, Balance object) {
		String sql = "DELETE FROM Formula_Element WHERE formulaID = '" + formulaID + "' AND elementID = '" + object.getElementID() + "';";
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.delete(sql);
		manager.close();
		return result;
	}
}
