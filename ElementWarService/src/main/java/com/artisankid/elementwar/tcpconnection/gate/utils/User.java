package com.artisankid.elementwar.tcpconnection.gate.utils;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by LiXiangYu on 2017/4/28.
 */
public class User {
    public enum State {
        Free, Matching, Matched, Inviting, Invited, WaitingInRoom, InRooming, InRoomed, Gaming
    }

    public enum GameState {
        Waiting, //等待
        Playing, //正在出牌
    }

    private String userID;
    private Integer strength;

    private State state;

    private String matchMessageID;
    private Long matchExpiredTime = 0L;

    private Integer hp = 0;
    private GameState gameState;
    private Long playExpiredTime;//出牌超时时间
    private List<String> cardIDs = new ArrayList<>();

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public State getState() {
        if(state == State.Matching) {
            Long now = System.currentTimeMillis();
            if(matchExpiredTime <= now) {
                setState(State.Free);
            }
        }
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getMatchMessageID() {
        return matchMessageID;
    }

    public void setMatchMessageID(String matchMessageID) {
        this.matchMessageID = matchMessageID;
    }

    public Long getMatchExpiredTime() {
        return matchExpiredTime;
    }

    public void setMatchExpiredTime(Long matchExpiredTime) {
        this.matchExpiredTime = matchExpiredTime;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public void addHp(Integer hp) {
        this.hp += hp;
    }

    public void minusHp(Integer hp) {
        this.hp -= hp;
    }

    public void setGameState(GameState state) {
        this.gameState = state;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Long getPlayExpiredTime() {
        return playExpiredTime;
    }

    public void setPlayExpiredTime(Long playExpiredTime) {
        this.playExpiredTime = playExpiredTime;
    }

    public List<String> getCardIDs() {
        return cardIDs;
    }

    public void addCardIDs(List<String> cardIDs) {
        this.cardIDs.addAll(cardIDs);
    }

    public void removeCardID(String cardID) {
        this.cardIDs.remove(cardID);
    }

    public boolean existCardID(String cardID) {
        return this.cardIDs.contains(cardID);
    }
}
