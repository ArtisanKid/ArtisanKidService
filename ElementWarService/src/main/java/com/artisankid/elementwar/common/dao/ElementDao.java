package com.artisankid.elementwar.common.dao;

import com.artisankid.elementwar.common.ewmodel.Atom;
import com.artisankid.elementwar.common.ewmodel.Element;
import com.artisankid.elementwar.common.ewmodel.Element.ElementType;
import com.artisankid.elementwar.common.ewmodel.Molecule;

import java.util.Map;

public class ElementDao {
	/**
	 * 根据元素ID查询元素
	 * @param elementID
	 * @return
	 */
	public Element selectByElementID(String elementID) {
		String sql = "SELECT * FROM Element WHERE elementID = " + elementID + ";";
		
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		Map<String, Object> result = manager.selectOne(sql);
		manager.close();
		
		if(result == null) {
			return null;
		}
		
		Element object = null;
		if(ElementType.enumOf((Integer)result.get("type")) == ElementType.Atom) {
			Atom atom = new Atom();
			atom.setRadius((Integer) result.get("radius"));
			atom.setColor((String) result.get("color"));
			object = atom;
		} else {
			object = new Molecule();
		}
		
		object.setElementID((String) result.get("elementID"));
		object.setType(ElementType.enumOf((Integer)result.get("type")));
		object.setName((String) result.get("name"));
		object.setCname((String) result.get("cname"));
		object.setEname((String) result.get("ename"));
		object.setDim2((String) result.get("dim2"));
		object.setDim3((String) result.get("dim3"));
		object.setWeight((Integer) result.get("weight"));
		object.setDetail((String) result.get("detail"));
		
		return object;
	}
	
	/**
	 * 插入Atom对象
	 * @param object
	 * @return
	 */
	public boolean insert(Atom object) {
		String sql = "INSERT INTO Element (elementID, type, name, cname, ename, dim2, dim3, weight, detail, radius, color) VALUES ('" + object.getElementID() + "', '" + object.getType().getValue() + "', '" + object.getName() + "', '" + object.getCname() + "', '" + object.getEname() + "', '" + object.getDim2() + "', '" + object.getDim3() + "', '" + object.getWeight() + "', '" + object.getDetail() + "', '" + object.getRadius() + "', '" + object.getColor() + "');";
		
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.insert(sql);
		manager.close();
		return result;
	}
	
	/**
	 * 插入Molecule对象
	 * @param object
	 * @return
	 */
	public boolean insert(Molecule object) {
		String sql = "INSERT INTO Element (elementID, type, name, cname, ename, dim2, dim3, weight, detail) VALUES ('" + object.getElementID() + "', '" + object.getType().getValue() + "', '" + object.getName() + "', '" + object.getCname() + "', '" + object.getEname() + "', '" + object.getDim2() + "', '" + object.getDim3() + "', '" + object.getWeight() + "', '" + object.getDetail() + "');";
		
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.insert(sql);
		manager.close();
		return result;
	}
	
	/**
	 * 更新Atom对象
	 * @param object
	 * @return
	 */
	public boolean update(Atom object) {
		String sql = "UPDATE Element SET type = '" + object.getType().getValue() + "', name = '" + object.getName() + "', cname = '" + object.getCname() + "', ename = '" + object.getEname() + "', dim2 = '" + object.getDim2() + "', dim3 = '" + object.getDim3() + "', weight = '" + object.getWeight() + "', detail = '" + object.getDetail() + "', radius = '" + object.getRadius() + "', color = '" + object.getColor() + "' WHERE elementID = '" + object.getElementID() + "';";
		
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.update(sql);
		manager.close();
		return result;
	}
	
	/**
	 * 更新Molecule对象
	 * @param object
	 * @return
	 */
	public boolean update(Molecule object) {
		String sql = "UPDATE Element SET type = '" + object.getType().getValue() + "', name = '" + object.getName() + "', cname = '" + object.getCname() + "', ename = '" + object.getEname() + "', dim2 = '" + object.getDim2() + "', dim3 = '" + object.getDim3() + "', weight = '" + object.getWeight() + "', detail = '" + object.getDetail() + "' WHERE elementID = '" + object.getElementID() + "';";
		
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.update(sql);
		manager.close();
		return result;
	}
	
	/**
	 * 删除指定元素ID对应的对象
	 * @param elementID
	 * @return
	 */
	public boolean delete(String elementID) {
		String sql = "DELETE FROM Element WHERE elementID = '" + elementID + "';";
		
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.delete(sql);
		manager.close();
		return result;
	}
}
