package com.artisankid.elementwar.tcpconnection.gate.utils;

/**
 * Created by LiXiangYu on 2017/4/28.
 */
public class User {
    public enum State {
        Free, Matching, Matched, Inviting, Invited, WaitingInRoom, InRooming, InRoomed, Gaming
    }

    public enum GameState {
        Playing, //正在出牌
        Waiting //等待
    }

    private String userID;

    private State state;

    private long matchExpiredTime;

    private long inviteExpiredTime;

    private Integer strength;

    private GameState gameState;

    private long playExpiredTime;//出牌超时时间

    private Integer hp;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public State getState() {
        if(state == State.Matching) {
            long now = System.currentTimeMillis();
            if(matchExpiredTime <= now) {
                setState(State.Free);
            }
        }
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public long getMatchExpiredTime() {
        return matchExpiredTime;
    }

    public void setMatchExpiredTime(long matchExpiredTime) {
        this.matchExpiredTime = matchExpiredTime;
    }

    public long getInviteExpiredTime() {
        return inviteExpiredTime;
    }

    public void setInviteExpiredTime(long inviteExpiredTime) {
        this.inviteExpiredTime = inviteExpiredTime;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public void setGameState(GameState state) {
        this.gameState = state;
    }

    public GameState getGameState() {
        return gameState;
    }

    public long getPlayExpiredTime() {
        return playExpiredTime;
    }

    public void setPlayExpiredTime(long playExpiredTime) {
        this.playExpiredTime = playExpiredTime;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }
}
