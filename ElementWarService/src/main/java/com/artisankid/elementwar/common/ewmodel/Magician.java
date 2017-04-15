package com.artisankid.elementwar.common.ewmodel;

/**
 * Created by LiXiangYu on 2017/4/15.
 */
public class Magician extends BaseMagician {
    private String portrait;
    private String largePortrait;
    private String mobile;
    private Long birthday;
    private String motto;
    private double winPercent;
    private double manSynthesisPercent;
    private double autoSynthesisPercent;
    private long rank;

    private Token token;

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getLargePortrait() {
        return largePortrait;
    }

    public void setLargePortrait(String largePortrait) {
        this.largePortrait = largePortrait;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public double getWinPercent() {
        return winPercent;
    }

    public void setWinPercent(double winPercent) {
        this.winPercent = winPercent;
    }

    public double getManSynthesisPercent() {
        return manSynthesisPercent;
    }

    public void setManSynthesisPercent(double manSynthesisPercent) {
        this.manSynthesisPercent = manSynthesisPercent;
    }

    public double getAutoSynthesisPercent() {
        return autoSynthesisPercent;
    }

    public void setAutoSynthesisPercent(double autoSynthesisPercent) {
        this.autoSynthesisPercent = autoSynthesisPercent;
    }

    public long getRank() {
        return rank;
    }

    public void setRank(long rank) {
        this.rank = rank;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public static String createUniqueOpenID() {
        return "生成openID";
    }
}
