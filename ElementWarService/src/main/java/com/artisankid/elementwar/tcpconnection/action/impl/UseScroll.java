package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.common.dao.MagicianDao;
import com.artisankid.elementwar.common.dao.ScrollDao;
import com.artisankid.elementwar.common.ewmodel.Effect;
import com.artisankid.elementwar.common.ewmodel.Magician;
import com.artisankid.elementwar.common.ewmodel.Scroll;
import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.UseScrollMessageOuterClass;
import com.artisankid.elementwar.ewmessagemodel.UseScrollNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.annotations.ActionRequestMap;
import com.artisankid.elementwar.tcpconnection.annotations.NettyAction;
import com.artisankid.elementwar.tcpconnection.gate.utils.RoomManager;
import com.artisankid.elementwar.tcpconnection.gate.utils.User;
import com.artisankid.elementwar.tcpconnection.gate.utils.UserContextManager;
import com.artisankid.elementwar.tcpconnection.gate.utils.UserManager;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created by LiXiangYu on 2017/4/26.
 */
@NettyAction
public class UseScroll {
    private Logger logger = LoggerFactory.getLogger(UseCard.class);

    @ActionRequestMap(actionKey = ContainerOuterClass.Container.USE_SCROLL_MESSAGE_FIELD_NUMBER)
    public void UseScrollMessage(ChannelHandlerContext context, ContainerOuterClass.Container container) {
        UseScrollMessageOuterClass.UseScrollMessage message = container.getUseScrollMessage();

        String receiverID = message.getReceiverId();
        if(receiverID == null) {
            return;
        }

        MagicianDao magicianDao = new MagicianDao();
        Magician receiver = magicianDao.selectByOpenID(receiverID);
        if(receiver == null) {
            return;
        }

        String scrollID = message.getScrollId();
        if(scrollID == null) {
            return;
        }

        ScrollDao scrollDao = new ScrollDao();
        Scroll scroll = scrollDao.selectByScrollID(scrollID);
        if(scroll == null) {
            return;
        }

        String senderID = message.getSenderId();

        //根据卷轴效果处理血量
        //卷轴不区分对别人使用还是自己使用
        for(Effect effect : scroll.getEffects()) {
            switch (effect.getType()) {
                case Cure: {
                    Integer hp = UserManager.getUser(senderID).getHp();
                    hp += effect.getValue();
                    UserManager.getUser(senderID).setHp(hp);
                    break;
                }
                case Hurt: {
                    Integer hp = UserManager.getUser(receiverID).getHp();
                    hp -= effect.getValue();
                    UserManager.getUser(receiverID).setHp(hp);
                    break;
                }
                case Jump: {
                    Deal.DealNotice(senderID, Arrays.asList("CO2"));
                    break;
                }
                case Draw:
                    Deal.DealNotice(senderID, Arrays.asList("CO2"));
                    break;
            }
        }

        for(User user : RoomManager.getRoom(senderID).getUsers()) {
            useScrollNotice(user.getUserID(), senderID, receiverID, scrollID);
        }

        if(UserManager.getUser(receiverID).getHp() <= 0) {
            Finish.FinishNotice(senderID);
        } else if(UserManager.getUser(senderID).getHp() <= 0) {
            Finish.FinishNotice(receiverID);
        }
    }

    public void useScrollNotice(String receiverID, String senderID, String effectReceiverID, String scrollID) {
        UseScrollNoticeOuterClass.UseScrollNotice.Builder notice = UseScrollNoticeOuterClass.UseScrollNotice.newBuilder();
        long now = System.currentTimeMillis();
        long expiredTime = now + 10 * 1000;
        notice.setSendTime(now / 1000);
        notice.setExpiredTime(expiredTime / 1000);
        notice.setNeedResponse(Boolean.FALSE);
        notice.setSenderId(senderID);
        notice.setReceiverId(effectReceiverID);
        notice.setScrollId(scrollID);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setMessageType(ContainerOuterClass.Container.MessageType.UseScrollNotice);
        container.setUseScrollNotice(notice);

        ChannelHandlerContext ctx = UserContextManager.getUserContext(receiverID);
        ctx.writeAndFlush(container);
    }
}
