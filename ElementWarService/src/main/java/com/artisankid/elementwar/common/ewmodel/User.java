package com.artisankid.elementwar.common.ewmodel;


/**
 * Created by LiXiangYu on 2017/4/15.
 */
public class User {
    public enum Gender  {
        Man("1", 1),
        Woman("2", 2);

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

        private Gender(String k, int v){
            this.key = k;
            this.value = v;
        }
    }

    public enum LoginType {
        Wechat("1", 1),
        QQ("2", 2),
        Weibo("3", 3);

        public static LoginType enumOf(int value) {
            switch (value) {
                case 1:
                    return Wechat;
                case 2:
                    return QQ;
                case 3:
                    return Weibo;
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

        private LoginType(String k, int v){
            this.key = k;
            this.value = v;
        }
    }

    private String userID;
    private String unionID;
    private String openID;
    private String nickname;
    private Gender gender;
    private String portrait;
    private String smallPortrait;
    private String largePortrait;
    private String mobile;
    private Long birthday;
    private String motto;
    private LoginType loginType;
    private Token token;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUnionID() {
        return unionID;
    }

    public void setUnionID(String unionID) {
        this.unionID = unionID;
    }

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public String getNickname() {
        return nickname;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Gender getGender() {
        return gender;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getSmallPortrait() {
        return smallPortrait;
    }

    public void setSmallPortrait(String smallPortrait) {
        this.smallPortrait = smallPortrait;
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
}
