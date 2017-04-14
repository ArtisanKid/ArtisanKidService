package com.artisankid.elementwar.common.ewmodel;

import java.util.List;

/**
 * Created by faterman on 17/4/2.
 */

public class Card extends BaseCard {
    private String witticism;
    private String detail;
    private List<Effect> effects;

    public String getWitticism() {
        return witticism;
    }

    public void setWitticism(String witticism) {
        this.witticism = witticism;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
    
    public List<Effect> getEffects() {
        return effects;
    }

    public void setEffects(List<Effect> effects) {
        this.effects = effects;
    }
}
