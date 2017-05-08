package com.artisankid.elementwar.common.dao;

import com.artisankid.elementwar.common.ewmodel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScrollDao {
	/**
	 * 根据分段查询卷轴
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public List<BaseScroll> select(int offset, int pageSize) {
		String sql = "SELECT scrollID, name, witticism FROM Scroll LIMIT " + offset + "," + pageSize + ";";
		return this.selectScrollsBySQL(sql);
	}

	/**
	 * 根据等级ID和反应类型查询卷轴
	 * @param levelID
	 * @param reactionID
	 * @return
	 */
	public List<BaseScroll> select(String levelID, String reactionID) {
		String sql = "SELECT scrollID, name, witticism FROM Scroll LEFT JOIN Formula_Reaction ON Scroll.formulaID = Formula_Reaction.formulaID WHERE levelID = '" + levelID + "' AND reactionID = '" + reactionID + "';";
		return this.selectScrollsBySQL(sql);
	}

	/**
	 * 根据关键字查询卷轴
	 *
	 * @param levelID 可以为null
	 * @param reactionID 可以为null
	 * @param keyword 可以为null
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public List<BaseScroll> selectByKeyword(String levelID, String reactionID, String keyword, int offset, int pageSize) {
		if((levelID == null || levelID.length() == 0)
				&& (reactionID == null || reactionID.length() == 0)
				&& (keyword == null || keyword.length() == 0) ) {
			return new ArrayList<>();
		}

		String sql = "SELECT scrollID, name, witticism FROM Scroll";
		if(levelID != null && reactionID != null && keyword != null &&
				levelID.length() > 0 && reactionID.length() > 0 && keyword.length() > 0) {
			sql = sql + " WHERE levelID = '" + levelID + "' AND reactionID = '" + reactionID + "' AND witticism LIKE '%" + keyword + "%' OR detail LIKE '%" + keyword + "%'";
		} else if(levelID != null && reactionID != null &&
				levelID.length() > 0 && reactionID.length() > 0) {
			sql = sql + " WHERE levelID = '" + levelID + "' AND reactionID = '" + reactionID + "'";
		} else if(levelID != null && keyword != null &&
				levelID.length() > 0 && keyword.length() > 0) {
			sql = sql + " WHERE levelID = '" + levelID + "' AND witticism LIKE '%" + keyword + "%' OR detail LIKE '%" + keyword + "%'";
		} else if(reactionID != null && keyword != null &&
				reactionID.length() > 0 && keyword.length() > 0) {
			sql = sql + " WHERE reactionID = '" + reactionID + "' AND witticism LIKE '%" + keyword + "%' OR detail LIKE '%" + keyword + "%'";
		} else if(levelID != null && levelID.length() > 0) {
			sql = sql + " WHERE levelID = '" + levelID + "'";
		} else if(reactionID != null && reactionID.length() > 0) {
			sql = sql + " WHERE reactionID = '" + reactionID + "'";
		} else if(keyword != null && keyword.length() > 0) {
			sql = sql + " WHERE witticism LIKE '%" + keyword + "%' OR detail LIKE '%" + keyword + "%'";
		}
		sql = sql + " LIMIT " + offset + "," + pageSize + ";";
		return this.selectScrollsBySQL(sql);
	}

	private List<BaseScroll> selectScrollsBySQL(String sql) {
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		List<Map<String, Object>> result = manager.select(sql);
		manager.close();

		ArrayList<BaseScroll> scrolls = new ArrayList<>();
		for (Map<String, Object> map : result) {
			BaseScroll scroll = new BaseScroll();
			scroll.setScrollID((String) map.get("scrollID"));
			scroll.setName((String) map.get("name"));		    
			scroll.setWitticism((String) map.get("witticism"));
			scrolls.add(scroll);
		}
		return scrolls;
	}

	/**
	 * 根据卷轴ID查询卷轴
	 *
	 * @param scrollID
	 * @return
	 */
	public Scroll selectByScrollID(String scrollID) {
		String sql = "SELECT * FROM Scroll WHERE scrollID = '" + scrollID + "';";
		return this.selectScrollBySQL(sql);
	}

	public Scroll selectByFormulaID(String formulaID) {
		String sql = "SELECT * FROM Scroll WHERE formulaID = '" + formulaID + "';";
		return this.selectScrollBySQL(sql);
	}

	private Scroll selectScrollBySQL(String sql) {
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		Map<String, Object> result = manager.selectOne(sql);
		manager.close();

		if(result == null) {
			return null;
		}

		Scroll scroll = new Scroll();
		if(result.get("scrollID") != null) {
			scroll.setScrollID(result.get("scrollID").toString());
		}
		if(result.get("name") != null) {
			scroll.setName(result.get("name").toString());
		}
		if(result.get("witticism") != null) {
			scroll.setWitticism(result.get("witticism").toString());
		}
		if(result.get("detail") != null) {
			scroll.setDetail(result.get("detail").toString());
		}

		if(result.get("levelID") != null) {
			LevelDao levelDao = new LevelDao();
			Level level = levelDao.selectByLevelID(result.get("levelID").toString());
			scroll.setLevel(level);
		}

		if(result.get("formulaID") != null) {
			FormulaDao formulaDao = new FormulaDao();
			Formula formula = formulaDao.selectByFormulaID(result.get("formulaID").toString());
			scroll.setFormula(formula);
		}

		if(scroll.getScrollID() != null) {
			EffectDao effectDao = new EffectDao();
			List<Effect> effects = effectDao.selectByScrollID(scroll.getScrollID());
			scroll.setEffects(effects);
		}

		return scroll;
	}

	/**
	 * 插入Scroll对象
	 * @param object
	 * @return
	 */
	public boolean insert(Scroll object) {
		ArrayList<String> sqlList = new ArrayList<>();

		String insertScrollSQL = "INSERT INTO Scroll (scrollID, name, levelID, witticism, detail, formulaID) VALUES ('"
				+ object.getScrollID() + "', '"
				+ object.getName() + "', '"
				+ object.getLevel().getLevelID() + "', '"
				+ object.getWitticism() + "', '"
				+ object.getDetail() + "', '"
				+ object.getFormula().getFormulaID() + "');";
		sqlList.add(insertScrollSQL);

		if(object.getEffects() != null) {
			for (Effect effect : object.getEffects()) {
				String insertScrollEffectSQL = "INSERT INTO Scroll_Effect (scrollID, effectID) VALUES ('"
						+ object.getScrollID() + "', '"
						+ effect.getEffectID() + "');";
				sqlList.add(insertScrollEffectSQL);
			}
		}

		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = true;
		for(String sql : sqlList) {
			result = result && manager.insert(sql);
		}
		manager.close();
		return result;
	}

	/**
	 * 更新Scroll对象
	 * @param object
	 * @return
	 */
	public boolean update(Scroll object) {
		boolean deleteResult = delete(object.getScrollID());
		boolean insertResult = insert(object);
		return deleteResult && insertResult;
	}

	/**
	 * 删除指定卷轴ID的对象
	 * @param scrollID
	 * @return
	 */
	public boolean delete(String scrollID) {
		ArrayList<String> sqls = new ArrayList<String>();

		String deleteScrollSQL = "DELETE FROM Scroll WHERE scrollID = '" + scrollID + "';";
		sqls.add(deleteScrollSQL);

		String deleteScrollEffectSQL = "DELETE FROM Scroll_Effect WHERE scrollID = '" + scrollID + "';";
		sqls.add(deleteScrollEffectSQL);

		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = true;
		for(String sql : sqls) {
			result = result && manager.delete(sql);
		}
		manager.close();
		return result;
	}
}
