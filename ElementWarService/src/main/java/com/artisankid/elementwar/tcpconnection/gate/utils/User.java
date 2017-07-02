package com.artisankid.elementwar.tcpconnection.gate.utils;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by LiXiangYu on 2017/4/28.
 */
public class User {
    public enum ConnectState {
        Disconnected, //断开状态
        Connected, //连接
    }

    public enum State {
        Free, Matching, Matched, Inviting, Invited, InRooming, InRoomed, Gaming
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

    private Integer stillDealTimes = 0;//继续抽牌次数
    private Integer ignoreDealTimes = 0;//跳过抽牌次数

    private ConnectState connectState;

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

    public void setConnectState(ConnectState state) {
        this.connectState = state;
    }

    public State getState() {
        if(state == null) {
            setState(State.Free);
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

    public Integer getStillDealTimes() {
        return stillDealTimes;
    }

    public void addStillDealTimes() {
        stillDealTimes += 1;
    }

    public void minusStillDealTimes() {
        stillDealTimes -= 1;
        if(stillDealTimes < 0) {
            stillDealTimes = 0;
        }
    }

    public Integer getIgnoreDealTimes() {
        return ignoreDealTimes;
    }

    public void addIgnoreDealTimes(Integer times) {
        ignoreDealTimes += times;
    }

    public void minusIgnoreDealTimes() {
        ignoreDealTimes -= 1;
        if(ignoreDealTimes < 0) {
            ignoreDealTimes = 0;
        }
    }

    public void addCardIDs(List<String> cardIDs) {
        this.cardIDs.addAll(cardIDs);
    }

    public void removeCardID(String cardID) {
        this.cardIDs.remove(cardID);
    }

    public Integer existCardID(String cardID) {
        Integer count = 0;
        for(String _cardID : cardIDs) {
            if(_cardID.equals(cardID)) {
                count++;
            }
        }
        return count;
    }

    public ConnectState getConnectState() {
        if(connectState == null) {
            setConnectState(ConnectState.Disconnected);
        }
        return connectState;
    }
}
