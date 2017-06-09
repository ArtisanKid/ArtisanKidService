package com.artisankid.elementwar.common.ewmodel;

public class Token {
	private String accessToken;
    private Double expiredTime; //秒
    private String refreshToken;
    private Double refreshTokenExpiredTime;//秒
    
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
    public Double getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Double expiredTime) {
        this.expiredTime = expiredTime;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Double getRefreshTokenExpiredTime() {
        return refreshTokenExpiredTime;
    }

    public void setRefreshTokenExpiredTime(Double expiredTime) {
        this.refreshTokenExpiredTime = expiredTime;
    }
}
