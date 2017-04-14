package com.artisankid.elementwar.common.dao;

import com.artisankid.elementwar.common.ewmodel.Epic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Epic完全是用户产生的数据，所以不涉及更新和删除操作
 * @author LiXiangYu
 *
 */
public class EpicDao {
	/**
	 * 根据用户ID来获取一级史诗列表
	 * @param openID
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public List<Epic> selectByOpenID(String openID, int offset, int pageSize) {
		String sql = "SELECT Epic.epicID, history FROM User_Epic LEFT JOIN Epic ON User_Epic.epicID = Epic.epicID WHERE openID = '" + openID + "' ORDER BY User_Epic.id DESC LIMIT "
				+ offset + "," + pageSize + ";";
		return this.selectEpicsBySQL(sql);
	}

	/**
	 * 根据史诗ID来获取次级史诗列表
	 * @param epicID
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public List<Epic> selectByEpicID(String epicID, int offset, int pageSize) {
		String sql = "SELECT epicID, history FROM Epic WHERE ref_epicID = '" + epicID + "' ORDER BY id DESC LIMIT "
				+ offset + "," + pageSize + ";";
		return this.selectEpicsBySQL(sql);
	}
	
	public List<Epic> selectEpicsBySQL(String sql) {
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		List<Map<String, Object>> result = manager.select(sql);
		manager.close();

		ArrayList<Epic> objects = new ArrayList<Epic>();
		for (Map<String, Object> map : result) {
			Epic object = new Epic();
			object.setEpicID((String) map.get("epicID"));
			object.setHistory((String) map.get("history"));
			objects.add(object);
		}
		return objects;
	}
	
	/**
	 * 存储一级史诗对象，关联用户
	 * @param openID
	 * @param epic
	 * @return
	 */
	public boolean insert(String openID, Epic epic) {
		String epicSQL = "INSERT INTO Epic (epicID, history) VALUES ('" + epic.getEpicID() + "', '" + epic.getHistory() + "');";
		String relationSQL = "INSERT INTO User_Epic (openID, epicID) VALUES ('" + openID + "', '" + epic.getEpicID() + "');";
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean epicResult = manager.insert(epicSQL);
		boolean relationResult = manager.insert(relationSQL);
		manager.close();
		return epicResult && relationResult;
	}
	
	/**
	 * 存储次级史诗对象，关联上级
	 * @param epic
	 * @param epicID
	 * @return
	 */
	public boolean insert(Epic epic, String refEpicID) {
		String sql = "INSERT INTO Epic (epicID, history, ref_epicID) VALUES ('"+ epic.getEpicID() + "', '" + epic.getHistory() +"', '" + refEpicID + "');";
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.insert(sql);
		manager.close();
		return result;
	}
}
