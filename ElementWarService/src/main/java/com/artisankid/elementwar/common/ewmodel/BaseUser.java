package com.artisankid.elementwar.common.ewmodel;

import java.util.List;

public class BaseUser {
	
	public enum ConnectState {
	    Online("1", 1),
	    Offline("2", 2);
		
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

		private ConnectState(String k, int v){
			this.key = k;
			this.value = v;
		}
	}
	
	public enum UserRelation {
	    Friend("1", 1),
	    Opponent("2", 2);
		
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

		private UserRelation(String k, int v){
			this.key = k;
			this.value = v;
		}
	}
	
	private String openID;
    private String nickname;
    private String smallPortrait;
    private int strength;
    private String honor;
    private List<Emblem> emblems;//暂时不开发
    private ConnectState state;
    
    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getSmallPortrait() {
        return smallPortrait;
    }

    public void setSmallPortrait(String smallPortrait) {
        this.smallPortrait = smallPortrait;
    }
    
    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public String getHonor() {
        return honor;
    }

    public void setHonor(String honor) {
        this.honor = honor;
    }
    
    public List<Emblem> getEmblems() {
        return emblems;
    }

    public void setEmblems(List<Emblem> emblems) {
        this.emblems = emblems;
    }
    
    public ConnectState getState() {
        return state;
    }

    public void setState(ConnectState state) {
        this.state = state;
    }
}
