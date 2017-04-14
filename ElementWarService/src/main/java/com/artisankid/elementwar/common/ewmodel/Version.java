package com.artisankid.elementwar.common.ewmodel;

/**
 * Created by faterman on 17/4/2.
 */

public class Version {
	public enum VersionType {	
		Release("1", 1),
		Resource("2", 2);
		
	    public static VersionType enumOf(int value) {
	        switch (value) {
	        case 1:
	            return Release;
	        case 2:
	            return Resource;
	        default:
	            return null;
	        }
	    }
		
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

		private VersionType(String k, int v){
			this.key = k;
			this.value = v;
		}
	}
	
	private String versionID;
    private VersionType type;
    private String latestVersion;
    private Boolean forceUpdate;
    private Boolean alert;
    private String message;
    private String url;
    
    public String getVersionID() {
        return versionID;
    }

    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }

    public VersionType getType() {
        return type;
    }

    public void setType(VersionType type) {
        this.type = type;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public boolean getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(Boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public Boolean getAlert() {
        return alert;
    }

    public void setAlert(Boolean alert) {
        this.alert = alert;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
