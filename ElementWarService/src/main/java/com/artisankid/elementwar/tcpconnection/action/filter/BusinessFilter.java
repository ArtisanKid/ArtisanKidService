package com.artisankid.elementwar.tcpconnection.action.filter;

/**
 * 业务逻辑过滤器
 *
 * Created by WangShaohua on 2017/4/30.
 */
public class BusinessFilter {
    /**
     * 业务逻辑是否需要进行过滤
     *
     * @return
     */
    public static boolean  isIn(int key, Object... args){
        return Boolean.TRUE;
    }
}
