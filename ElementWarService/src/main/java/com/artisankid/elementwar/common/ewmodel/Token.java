package com.artisankid.elementwar.common.ewmodel;

public class Token {
	private String accessToken;
    private double expiredTime;
    private String refreshToken;
    
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
    public double getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(double expiredTime) {
        this.expiredTime = expiredTime;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
