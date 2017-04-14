package com.artisankid.elementwar.common.ewmodel;

import java.util.List;

public class Formula {
	private String formulaID;//方程式ID
    private List<Reaction> reactions;//反应类型
    private List<Balance> reactants;//反应物
    private List<Condition> conditions;//条件
    private List<Balance> resultants;//生成物
    private String phenomenon;//现象
    private String principle;//原理
    private String detail;//描述

    public String getFormulaID() {
        return formulaID;
    }

    public void setFormulaID(String formulaID) {
        this.formulaID = formulaID;
    }

    public List<Reaction> getReactions() {
        return reactions;
    }
    
    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }
    
    public List<Balance> getReactants() {
        return reactants;
    }

    public void setReactants(List<Balance> reactants) {
        this.reactants = reactants;
    }
    
    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }
    
    public List<Balance> getResultants() {
        return resultants;
    }

    public void setResultants(List<Balance> resultants) {
        this.reactants = resultants;
    }
    
    public String getPhenomenon() {
        return phenomenon;
    }

    public void setPhenomenon(String phenomenon) {
        this.phenomenon = phenomenon;
    }
    
    public String getPrinciple() {
        return principle;
    }

    public void setPrinciple(String principle) {
        this.principle = principle;
    }
    
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
