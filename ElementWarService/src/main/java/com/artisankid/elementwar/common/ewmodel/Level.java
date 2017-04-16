package com.artisankid.elementwar.common.ewmodel;

/**
 * Created by faterman on 17/4/2.
 */

public class Level {
    private String levelID;
    private String cname;
    private String ename;

    public String getLevelID() {
        return levelID;
    }

    public void setLevelID(String levelID) {
        this.levelID = levelID;
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
