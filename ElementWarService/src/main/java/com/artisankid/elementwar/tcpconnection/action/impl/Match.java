package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.common.ewmodel.User;
import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass.Container;
import com.artisankid.elementwar.ewmessagemodel.MatchMessageOuterClass.MatchMessage;
import com.artisankid.elementwar.ewmessagemodel.MatchNoticeOuterClass.MatchNotice;
import com.artisankid.elementwar.tcpconnection.annotations.ActionRequestMap;
import com.artisankid.elementwar.tcpconnection.annotations.NettyAction;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by LiXiangYu on 2017/4/24.
 */
@NettyAction
public class Match {
    private Logger logger = LoggerFactory.getLogger(Notice.class);
    /**
     * 处理消息逻辑
     *
     * @param o
     * @return
     */
    @ActionRequestMap(actionKey = Container.MATCH_MESSAGE_FIELD_NUMBER)
    public void matchMessage(ChannelHandlerContext ctx, Object o) {
        Container container = (Container) o;
        MatchMessage message = container.getMatchMessage();
        String messageID = message.getMessageId();
        logger.debug("receive msg:" + messageID);

        //TODO:(1)查询当前的匹配集合中是否有和当前用户实力相差绝对值<20的用户，有则调用matchNotice方法
        //TODO:(2)如果没有(1)中对应的用户，就将当前用户添加到匹配集合中
        //TODO:(3)如果匹配超时了，就将当前用户从匹配集合中移除
    }

    public void matchNotice(ChannelHandlerContext ctx, String messageID, User user) {
        MatchNotice.Builder notice = MatchNotice.newBuilder();
        notice.setMessageId(messageID);
        long now = System.currentTimeMillis();
        notice.setSendTime(now / 1000);
        notice.setExpiredTime(now / 1000 + 200);
        notice.setNeedResponse(Boolean.FALSE);
        notice.setUserId(user.getOpenID());
        notice.setUserName(user.getNickname());
        notice.setUserPortraitUrl(user.getSmallPortrait());

        Container.Builder noticeContainer = Container.newBuilder();
        noticeContainer.setMatchNotice(notice);
        ctx.writeAndFlush(noticeContainer);
    }
}
