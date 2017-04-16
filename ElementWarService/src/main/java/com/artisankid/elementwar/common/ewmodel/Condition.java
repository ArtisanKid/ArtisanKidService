package com.artisankid.elementwar.common.ewmodel;

/**
 * Created by faterman on 17/4/2.
 */

public class Condition {
    private String conditionID;
    private String cname;
    private String ename;

    public String getConditionID() {
        return conditionID;
    }

    public void setConditionID(String conditionID) {
        this.conditionID = conditionID;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String name) {
        this.cname = name;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String name) {
        this.ename = name;
    }
}
