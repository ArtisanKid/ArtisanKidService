package com.artisankid.elementwar.common.dao;

import com.artisankid.elementwar.common.ewmodel.BaseCard;
import com.artisankid.elementwar.common.ewmodel.Card;
import com.artisankid.elementwar.common.ewmodel.Effect;
import com.artisankid.elementwar.common.ewmodel.Element;
import com.artisankid.elementwar.common.ewmodel.Element.ElementType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CardDao {
	/**
	 * 根据类型分段查询卡片
	 * @param type
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public List<BaseCard> select(ElementType type, int offset, int pageSize) {
		String sql = "SELECT * FROM Card LEFT JOIN Element ON Card.elementID = Element.elementID LIMIT " + offset + "," + pageSize + ";";
		
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		List<Map<String, Object>> result = manager.select(sql);
		manager.close(); 

		ArrayList<BaseCard> objects = new ArrayList<BaseCard>();
		for (Map<String, Object> map : result) {
			BaseCard object = new BaseCard();
			object.setCardID((String) map.get("cardID"));
			
			ElementDao elementDao = new ElementDao();
			Element element = elementDao.selectByElementID((String) map.get("elementID"));
			object.setElement(element);
			
			objects.add(object);
		}
		return objects;
	}
	
	/**
	 * 根据卡片ID查询卡片
	 * @param cardID
	 * @return
	 */
	public Card selectByCardID(String cardID) {
		String sql = "SELECT * FROM Card LEFT JOIN Element ON Card.elementID = Element.elementID WHERE cardID = '" + cardID + "';";
		
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
	    Map<String, Object> result = manager.selectOne(sql);
		manager.close(); 
		
		if(result == null) {
			return null;
		}
		
		Card object = new Card();
		object.setCardID((String) result.get("cardID"));
		object.setWitticism((String) result.get("witticism"));
		object.setDetail((String) result.get("cardID"));
		
		ElementDao elementDao = new ElementDao();
		Element element = elementDao.selectByElementID((String) result.get("elementID"));
		object.setElement(element);
		
		EffectDao effectDao = new EffectDao();
		List<Effect> effects = effectDao.selectByCardID((String) result.get("cardID"));		
		object.setEffects(effects);
		
		return object;
	}
	
	/**
	 * 插入Card对象
	 * @param card
	 * @return
	 */
	public boolean insert(Card object) {
		String sql = "INSERT INTO Card (cardID, elementID, witticism, detail) VALUES ('" + object.getCardID() + "', '" + object.getElement().getElementID() + "', '" + object.getWitticism() + "', '" + object.getDetail() + "');";
		ArrayList<String> cardEffectSQLs = new ArrayList<String>();
		for(Effect effect : object.getEffects()) {
			String cardEffectSQL = "INSERT INTO Card_Effect (cardID, effectID) VALUES ('" + object.getCardID() + "', '" + effect.getEffectID() + "');";
			cardEffectSQLs.add(cardEffectSQL);
		}
		
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.insert(sql);
		for(String cardEffectSQL : cardEffectSQLs) {
			boolean cardEffectResult = manager.insert(cardEffectSQL);
			result = result && cardEffectResult;
		}
		manager.close();
		return result;
	}
	
	/**
	 * 更新Card对象
	 * @param card
	 * @return
	 */
	public boolean update(Card object) {
		boolean deleteResult = delete(object.getCardID());
		boolean insertResult = insert(object);
		return deleteResult && insertResult;
	}
	
	/**
	 * 删除Card对象
	 * @param card
	 * @return
	 */
	public boolean delete(String cardID) {
		String sql = "DELETE FROM Card WHERE cardID ='" + cardID + "');";
		String cardEffectSQL = "DELETE FROM Card_Effect WHERE cardID ='" + cardID + "');";
		
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean deletecCardResult = manager.delete(sql);
		boolean deleteCardEffectResult = manager.delete(cardEffectSQL);
		manager.close();
		return deletecCardResult && deleteCardEffectResult;
	}
}
