package com.artisankid.elementwar.common.ewmodel;

import java.util.List;

/**
 * Created by faterman on 17/4/2.
 */

public class Scroll extends BaseScroll {
	private Level level;
    private Formula formula;
    private List<Effect> effects;
    private String detail;
    
    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Formula getFormula() {
        return formula;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public void setEffects(List<Effect> effects) {
        this.effects = effects;
    }
    
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
