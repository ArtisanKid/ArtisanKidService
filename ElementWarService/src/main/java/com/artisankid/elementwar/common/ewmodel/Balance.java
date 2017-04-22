package com.artisankid.elementwar.common.ewmodel;

public class Balance {
	public enum BalanceType {
		Reactant("1", 1),//反应物
		Resultant("2", 2),//生成物
		Catalyst("3", 3);//催化剂
		
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

		private BalanceType(String k, int v){
			this.key = k;
			this.value = v;
		}
	}

	private String elementID;
    private int count;
    
    public String getElementID() {
        return elementID;
    }

    public void setElementID(String elementID) {
        this.elementID = elementID;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
