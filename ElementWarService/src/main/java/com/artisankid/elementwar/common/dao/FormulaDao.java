package com.artisankid.elementwar.common.dao;

import com.artisankid.elementwar.common.ewmodel.Balance;
import com.artisankid.elementwar.common.ewmodel.Balance.BalanceType;
import com.artisankid.elementwar.common.ewmodel.Condition;
import com.artisankid.elementwar.common.ewmodel.Formula;
import com.artisankid.elementwar.common.ewmodel.Reaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FormulaDao {
	/**
	 * 查询方程式ID对应的方程式
	 * @param formulaID
	 * @return
	 */
	public Formula selectByFormulaID(String formulaID) {
		String sql = "SELECT * FROM Formula WHERE formulaID = '" + formulaID + "';";
		return this.selectBySQL(sql);
	}

	/**
	 * 验证反应是否合法
	 * 
	 * @param reactantIDs
	 * @param conditionIDs
	 * @return
	 */
	public Formula select(List<String> reactantIDs, List<String> conditionIDs) {
		String notInReactantSQL = "SELECT DISTINCT formulaID FROM Formula_Balance WHERE type = '" + BalanceType.Reactant.getValue() + "' AND balanceID NOT IN (";
		for (int i = 0; i < reactantIDs.size(); i++) {
			if(i == 0) {
				notInReactantSQL = notInReactantSQL + "'" + reactantIDs.get(i) + "'";
			} else {
				notInReactantSQL = notInReactantSQL + ", '" + reactantIDs.get(i) + "'";
			}
		}
		notInReactantSQL = notInReactantSQL + ")";
		String reactantSQL = "SELECT formulaID FROM Formula_Balance WHERE type = '" + BalanceType.Reactant.getValue() + "' GROUP BY formulaID HAVING COUNT(*) = '" + reactantIDs.size() +"' AND formulaID NOT IN (" + notInReactantSQL + ")";
		
		String notInConditionSQL = "SELECT DISTINCT formulaID FROM Formula_Condition WHERE conditionID NOT IN (";
		for (int i = 0; i < conditionIDs.size(); i++) {
			if(i == 0) {
				notInConditionSQL = notInConditionSQL + "'" + conditionIDs.get(i) + "'";
			} else {
				notInConditionSQL = notInConditionSQL + ", '" + conditionIDs.get(i) + "'";
			}
		}
		notInConditionSQL = notInConditionSQL + ")";
		String conditionSQL = "SELECT formulaID FROM Formula_Condition GROUP BY formulaID HAVING COUNT(*) = '" + conditionIDs.size() +"' AND formulaID NOT IN (" + notInConditionSQL + ")";

		String sql = "SELECT * FROM Formula WHERE formulaID IN (" + reactantSQL + ") AND formulaID IN (" + conditionSQL + ");";
		return this.selectBySQL(sql);
	}

	/**
	 * 验证反应是否合法
	 * 
	 * @param reactantIDs
	 * @param conditionIDs
	 * @param resultantIDs
	 * @return
	 */
	public Formula select(List<String> reactantIDs, List<String> conditionIDs, List<String> resultantIDs) {
		String notInReactantSQL = "SELECT DISTINCT formulaID FROM Formula_Balance WHERE type = '" + BalanceType.Reactant.getValue() + "' AND balanceID NOT IN (";
		for (int i = 0; i < reactantIDs.size(); i++) {
			if(i == 0) {
				notInReactantSQL = notInReactantSQL + "'" + reactantIDs.get(i) + "'";
			} else {
				notInReactantSQL = notInReactantSQL + ", '" + reactantIDs.get(i) + "'";
			}
		}
		notInReactantSQL = notInReactantSQL + ")";
		String reactantSQL = "SELECT formulaID FROM Formula_Balance WHERE type = '" + BalanceType.Reactant.getValue() + "' GROUP BY formulaID HAVING COUNT(*) = '" + reactantIDs.size() +"' AND formulaID NOT IN (" + notInReactantSQL + ")";
		
		String notInConditionSQL = "SELECT DISTINCT formulaID FROM Formula_Condition WHERE conditionID NOT IN (";
		for (int i = 0; i < conditionIDs.size(); i++) {
			if(i == 0) {
				notInConditionSQL = notInConditionSQL + "'" + conditionIDs.get(i) + "'";
			} else {
				notInConditionSQL = notInConditionSQL + ", '" + conditionIDs.get(i) + "'";
			}
		}
		notInConditionSQL = notInConditionSQL + ")";
		String conditionSQL = "SELECT formulaID FROM Formula_Condition GROUP BY formulaID HAVING COUNT(*) = '" + conditionIDs.size() +"' AND formulaID NOT IN (" + notInConditionSQL + ")";

		String notInResultantSQL = "SELECT DISTINCT formulaID FROM Formula_Balance WHERE type = '" + BalanceType.Resultant.getValue() + "' AND balanceID NOT IN (";
		for (int i = 0; i < resultantIDs.size(); i++) {
			if(i == 0) {
				notInResultantSQL = notInResultantSQL + "'" + resultantIDs.get(i) + "'";
			} else {
				notInResultantSQL = notInResultantSQL + ", '" + resultantIDs.get(i) + "'";
			}
		}
		notInResultantSQL = notInResultantSQL + ")";
		String resultantSQL = "SELECT formulaID FROM Formula_Balance WHERE type = '" + BalanceType.Resultant.getValue() + "' GROUP BY formulaID HAVING COUNT(*) = '" + reactantIDs.size() +"' AND formulaID NOT IN (" + notInResultantSQL + ")";
		
		String sql = "SELECT * FROM Formula WHERE formulaID IN (" + reactantSQL + ") AND formulaID IN (" + conditionSQL + ") AND formulaID IN (" + resultantSQL + ");";
		return this.selectBySQL(sql);
	}
	
	private Formula selectBySQL(String sql) {
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		Map<String, Object> result = manager.selectOne(sql);
		manager.close();
		
		if(result == null) {
			return null;
		}

		Formula formula = new Formula();
		formula.setFormulaID((String) result.get("formulaID"));
		formula.setPhenomenon((String) result.get("phenomenon"));
		formula.setPrinciple((String) result.get("principle"));
		formula.setDetail((String) result.get("detail"));

		ReactionDao reactionDao = new ReactionDao();
		ArrayList<Reaction> reactions = (ArrayList<Reaction>)reactionDao.selectByFormulaID(formula.getFormulaID());
		formula.setReactions(reactions);
		
		BalanceDao balanceDao = new BalanceDao();
		ArrayList<Balance> reactants = (ArrayList<Balance>)balanceDao.selectByFormulaID(formula.getFormulaID(), BalanceType.Reactant);
		formula.setReactants(reactants);
		ArrayList<Balance> resultants = (ArrayList<Balance>)balanceDao.selectByFormulaID(formula.getFormulaID(), BalanceType.Resultant);
		formula.setResultants(resultants);
		
		ConditionDao conditionDao = new ConditionDao();
		ArrayList<Condition> conditions = (ArrayList<Condition>)conditionDao.selectByFormulaID(formula.getFormulaID());
		formula.setConditions(conditions);
		
		return formula;
	}
	
	/**
	 * 插入Formula对象
	 * @param object
	 * @return
	 */
	public boolean insert(Formula object) {
		ArrayList<String> sqls = new ArrayList<String>();
		
		String insertFormulaSQL = "INSERT INTO Formula (formulaID, phenomenon, principle, detail) VALUES ('" + object.getFormulaID() + "', '" + object.getPhenomenon() + "', '" + object.getPrinciple() + "', '" + object.getDetail() + "');";
		sqls.add(insertFormulaSQL);
		
		for(Reaction reaction : object.getReactions()) {
			String insertFormulaReactionSQL = "INSERT INTO Formula_Reaction (formulaID, reactionID) VALUES ('" + object.getFormulaID() + "', '" + reaction.getReactionID() + "');";
			sqls.add(insertFormulaReactionSQL);
		}
		
		for(Condition condition : object.getConditions()) {
			String insertFormulaConditionSQL = "INSERT INTO Formula_Condition (formulaID, conditionID) VALUES ('" + object.getFormulaID() + "', '" + condition.getConditionID() + "');";
			sqls.add(insertFormulaConditionSQL);
		}
		
		for(Balance balance : object.getReactants()) {
			String insertFormulaBalanceSQL = "INSERT INTO Formula_Balance (formulaID, balanceID, type) VALUES ('" + object.getFormulaID() + "', '" + balance.getBalanceID() + "', '" + BalanceType.Reactant.getValue() + "');";
			sqls.add(insertFormulaBalanceSQL);
		}
		
		for(Balance balance : object.getResultants()) {
			String insertFormulaBalanceSQL = "INSERT INTO Formula_Balance (formulaID, balanceID, type) VALUES ('" + object.getFormulaID() + "', '" + balance.getBalanceID() + "', '" + BalanceType.Resultant.getValue() + "');";
			sqls.add(insertFormulaBalanceSQL);
		}
		
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = true;
		for(String sql : sqls) {
			result = result && manager.insert(sql);
		}
		manager.close();
		return result;
	}
	
	/**
	 * 更新Formula对象
	 * @param object
	 * @return
	 */
	public boolean update(Formula object) {
		boolean deleteResult = delete(object.getFormulaID());
		boolean insertResult = insert(object);
		return deleteResult && insertResult;
	}
	
	/**
	 * 删除指定ID的公式
	 * @param formulaID
	 * @return
	 */
	public boolean delete(String formulaID) {
		ArrayList<String> sqls = new ArrayList<String>();
		
		String deleteFormulaSQL = "DELETE FROM Formula WHERE formulaID = '" + formulaID + "';";
		sqls.add(deleteFormulaSQL);
		
		String deleteFormulaReactionSQL = "DELETE FROM Formula_Reaction WHERE formulaID = '" + formulaID + "';";
		sqls.add(deleteFormulaReactionSQL);
		
		String deleteFormulaConditionSQL = "DELETE FROM Formula_Condition WHERE formulaID = '" + formulaID + "';";
		sqls.add(deleteFormulaConditionSQL);
		
		String deleteFormulaBalanceSQL = "DELETE FROM Formula_Balance WHERE formulaID = '" + formulaID + "';";
		sqls.add(deleteFormulaBalanceSQL);

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
