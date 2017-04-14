package com.artisankid.elementwar.common.ewmodel;

/**
 * Created by faterman on 17/4/2.
 */

public class User extends BaseUser {	
	public enum LoginType {
	    Wechat("1", 1),
	    QQ("2", 2),
	    Weibo("3", 3);
		
		private String key;
		private int value;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		private LoginType(String k, int v){
			this.key = k;
			this.value = v;
		}
	}
    
    private String portrait;
    private String largePortrait;
    private String mobile;
    private double birthday;
    private String motto;
    private double winPercent;
    private double manSynthesisPercent;
    private double autoSynthesisPercent;
    private int rank;
    private LoginType loginType;
    
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
    
    public double getBirthday() {
        return birthday;
    }

    public void setBirthday(double birthday) {
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
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
