package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.common.ewmodel.User;
import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.MatchMessageOuterClass;
import com.artisankid.elementwar.ewmessagemodel.MatchNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.annotations.ActionRequestMap;
import com.artisankid.elementwar.tcpconnection.annotations.NettyAction;
import com.artisankid.elementwar.tcpconnection.gate.utils.ClientConnection;
import com.artisankid.elementwar.tcpconnection.gate.utils.ClientConnectionMap;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by LiXiangYu on 2017/4/24.
 */
@NettyAction
public class Match {
    private Logger logger = LoggerFactory.getLogger(Match.class);

    @ActionRequestMap(actionKey = ContainerOuterClass.Container.MATCH_MESSAGE_FIELD_NUMBER)
    public void matchMessage(ChannelHandlerContext ctx, ContainerOuterClass.Container container) {
        MatchMessageOuterClass.MatchMessage message = container.getMatchMessage();

        //TODO 获取逻辑根据用户Id获取对应的连接从而获取上下文
        List<String> userIDs = ClientConnectionMap

        //TODO:(1)查询当前的匹配集合中是否有和当前用户实力相差绝对值<20的用户，有则调用matchNotice方法
        //TODO:(2)如果没有(1)中对应的用户，就将当前用户添加到匹配集合中
        //TODO:(3)如果匹配超时了，就将当前用户从匹配集合中移除
    }

    public void matchNotice(ChannelHandlerContext ctx, String messageID, User user) {
        MatchNoticeOuterClass.MatchNotice.Builder notice = MatchNoticeOuterClass.MatchNotice.newBuilder();
        notice.setMessageId(messageID);
        long now = System.currentTimeMillis();
        notice.setSendTime(now / 1000);
        notice.setExpiredTime(now / 1000 + 200);
        notice.setNeedResponse(Boolean.FALSE);
        notice.setUserId(user.getOpenID());
        notice.setUserName(user.getNickname());
        notice.setUserPortraitUrl(user.getSmallPortrait());

        ContainerOuterClass.Container.Builder noticeContainer = ContainerOuterClass.Container.newBuilder();
        noticeContainer.setMatchNotice(notice);
        ctx.writeAndFlush(noticeContainer);
    }
}
