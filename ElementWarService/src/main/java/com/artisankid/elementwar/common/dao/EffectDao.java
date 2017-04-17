package com.artisankid.elementwar.common.dao;

import com.artisankid.elementwar.common.ewmodel.Effect;
import com.artisankid.elementwar.common.ewmodel.Effect.EffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EffectDao {
	/**
	 * 查询全部反应效果对象
	 * 
	 * @return
	 */
	public List<Effect> selectAll() {
		String sql = "SELECT * FROM Effect";
		return this.selectBySQL(sql);
	}
	
	/**
	 * 查询卡片ID对应的效果对象
	 * 
	 * @return
	 */
	public List<Effect> selectByCardID(String cardID) {
		String sql = "SELECT * FROM Effect WHERE effectID IN (SELECT effectID FROM Card_Effect WHERE cardID = '" + cardID + "');";
		return this.selectBySQL(sql);
	}
	
	/**
	 * 查询卷轴ID对应的效果对象
	 * 
	 * @return
	 */
	public List<Effect> selectByScrollID(String scrollID) {
		String sql = "SELECT * FROM Effect WHERE effectID IN (SELECT effectID FROM Scroll_Effect WHERE scrollID = '" + scrollID + "');";
		return this.selectBySQL(sql);
	}
	
	private List<Effect> selectBySQL(String sql) {
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		List<Map<String, Object>> resultList = manager.select(sql);
		manager.close();

		ArrayList<Effect> objects = new ArrayList<>();
		for (Map<String, Object> result : resultList) {
			Effect object = new Effect();
			object.setEffectID(result.get("effectID").toString());
			object.setCname(result.get("cname").toString());
			object.setEname(result.get("ename").toString());
			object.setType(EffectType.enumOf(Integer.parseInt(result.get("type").toString())));
			object.setValue(Integer.parseInt(result.get("value").toString()));
			object.setWitticism(result.get("witticism").toString());
			objects.add(object);
		}
		return objects;
	}

	/**
	 * 根据反应效果ID查询反应效果对象
	 * 
	 * @param effectID
	 * @return
	 */
	public Effect selectByEffectID(String effectID) {
		String sql = "SELECT * FROM Effect WHERE effectID = '" + effectID + "';";

		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		Map<String, Object> result = manager.selectOne(sql);
		manager.close();
		
		if(result == null) {
			return null;
		}
		
		Effect object = new Effect();
		object.setEffectID(result.get("effectID").toString());
		object.setCname(result.get("cname").toString());
		object.setEname(result.get("ename").toString());
		object.setType(EffectType.enumOf(Integer.parseInt(result.get("type").toString())));
		object.setValue(Integer.parseInt(result.get("value").toString()));
		object.setWitticism(result.get("witticism").toString());
		return object;
	}
	
	/**
	 * 插入Effect对象
	 * @param object
	 * @return
	 */
	public boolean insert(Effect object) {
		String sql = "INSERT INTO Effect (effectID, cname, ename, type, value, witticism) VALUES ('"
				+ object.getEffectID() + "', '"
				+ object.getCname() + "', '"
				+ object.getEname() + "', '"
				+ object.getType().getValue() + "', '"
				+ object.getValue() + "', '"
				+ object.getWitticism() + "');";
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.insert(sql);
		manager.close();
		return result;
	}
	
	/**
	 * 更新Effect对象
	 * @param object
	 * @return
	 */
	public boolean update(Effect object) {
		String sql = "UPDATE Effect SET "
				+ "cname = '" + object.getCname() + "', "
				+ "ename = '" + object.getEname() + "', "
				+ "type = '" + object.getType().getValue() + "', "
				+ "value = '" + object.getValue() + "', "
				+ "witticism = '" + object.getWitticism() + "' "
				+ "WHERE effectID = '" + object.getEffectID() + "';";

		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.update(sql);
		manager.close();
		return result;
	}
	
	/**
	 * 删除Effect对象
	 * @param effectID
	 * @return
	 */
	public boolean delete(String effectID) {
		String sql = "DELETE FROM Effect WHERE effectID = '" + effectID + "';";
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.delete(sql);
		manager.close();
		return result;
	}
}
