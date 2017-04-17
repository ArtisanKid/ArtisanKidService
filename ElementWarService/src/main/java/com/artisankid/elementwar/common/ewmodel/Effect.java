package com.artisankid.elementwar.common.ewmodel;

/**
 * Created by faterman on 17/4/2.
 */

public class Effect {
	public enum EffectType {
		Cure("1", 1), //治疗
		Hurt("2", 2), //伤害
		Jump("3", 3), //跳过
		Draw("4", 4); //抽牌
		
		private String key;
		private int value;
		
		public static EffectType enumOf(int value) {
	        switch (value) {
	        case 1:
	            return Cure;
	        case 2:
	            return Hurt;
	        case 3:
	            return Jump;
	        case 4:
	            return Draw;
	        default:
	            return null;
	        }
	    }

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

		private EffectType(String k, int v){
			this.key = k;
			this.value = v;
		}
	}
	
    private String effectID;
	private String cname;
	private String ename;
    private EffectType type;
    private int value;
    private String witticism;

    public String getEffectID() {
        return effectID;
    }

    public void setEffectID(String effectID) {
        this.effectID = effectID;
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

    public EffectType getType() {
        return type;
    }

    public void setType(EffectType type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getWitticism() {
        return witticism;
    }

    public void setWitticism(String witticism) {
        this.witticism = witticism;
    }
}
