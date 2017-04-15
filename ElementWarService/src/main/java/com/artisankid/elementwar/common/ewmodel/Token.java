package com.artisankid.elementwar.common.ewmodel;

public class Token {
	private String accessToken;
    private Long expiredTime;
    private String refreshToken;
    private Long refreshTokenExpiredTime;
    
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
    public Long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Long expiredTime) {
        this.expiredTime = expiredTime;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getRefreshTokenExpiredTime() {
        return refreshTokenExpiredTime;
    }

    public void setRefreshTokenExpiredTime(Long expiredTime) {
        this.refreshTokenExpiredTime = expiredTime;
    }
}
