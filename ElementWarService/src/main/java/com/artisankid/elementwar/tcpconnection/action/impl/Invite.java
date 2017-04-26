package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.common.dao.MagicianDao;
import com.artisankid.elementwar.common.ewmodel.Magician;
import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.InviteMessageOuterClass;
import com.artisankid.elementwar.ewmessagemodel.InviteNoticeOuterClass;
import com.artisankid.elementwar.ewmessagemodel.InviteReplyMessageOuterClass;
import com.artisankid.elementwar.ewmessagemodel.InviteReplyNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.annotations.ActionRequestMap;
import com.artisankid.elementwar.tcpconnection.annotations.NettyAction;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by LiXiangYu on 2017/4/26.
 */
@NettyAction
public class Invite {
    enum InviteReply {
        Agree, Busy, Refuse, Timeout, Offline
    }

    private Logger logger = LoggerFactory.getLogger(Invite.class);

    @ActionRequestMap(actionKey = ContainerOuterClass.Container.INVITE_MESSAGE_FIELD_NUMBER)
    public void inviteMessage(ChannelHandlerContext context, ContainerOuterClass.Container container) {
        InviteMessageOuterClass.InviteMessage message = container.getInviteMessage();
        MagicianDao dao = new MagicianDao();

        Magician sender = dao.selectByOpenID(message.getSenderId());

        String receiverID = message.getReceiverId();
        if(receiverID == null) {
            return;
        }

        Magician receiver = dao.selectByOpenID(receiverID);
        if(receiver == null) {
            return;
        }

        //TODO:判断receiver的状态，根据状态区别后面的逻辑

        ChannelHandlerContext targetContext = null;//TODO:获取对应用户的Context
        inviteNotice(targetContext, message.getMessageId(), sender);
    }

    public void inviteNotice(ChannelHandlerContext ctx, String messageID, Magician sender) {
        InviteNoticeOuterClass.InviteNotice.Builder notice = InviteNoticeOuterClass.InviteNotice.newBuilder();
        notice.setMessageId(messageID);
        long now = System.currentTimeMillis();
        notice.setSendTime(now / 1000);
        notice.setExpiredTime(now / 1000 + 200);
        notice.setNeedResponse(Boolean.FALSE);
        notice.setSenderId(sender.getOpenID());
        notice.setSenderName(sender.getNickname());
        notice.setSenderPortraitUrl(sender.getSmallPortrait());

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setInviteNotice(notice);
        ctx.writeAndFlush(container);
    }

    @ActionRequestMap(actionKey = ContainerOuterClass.Container.INVITE_REPLY_MESSAGE_FIELD_NUMBER)
    public void inviteReplyMessage(ChannelHandlerContext context, ContainerOuterClass.Container container) {
        InviteReplyMessageOuterClass.InviteReplyMessage message = container.getInviteReplyMessage();

        MagicianDao dao = new MagicianDao();

        Magician sender = dao.selectByOpenID(message.getSenderId());
        if(sender == null) {
            return;
        }

        String receiverID = message.getReceiverId();
        if(receiverID == null) {
            return;
        }

        Magician receiver = dao.selectByOpenID(receiverID);
        if(receiver == null) {
            return;
        }

        ChannelHandlerContext targetContext = null;//TODO:获取对应用户的Context
        if(message.getIsAgree()) {
            inviteReplyNotice(targetContext, message.getMessageId(), sender, InviteReply.Agree);
        } else {
            inviteReplyNotice(targetContext, message.getMessageId(), sender, InviteReply.Refuse);
        }
    }

    public void inviteReplyNotice(ChannelHandlerContext ctx, String messageID, Magician sender, InviteReply reply) {
        InviteReplyNoticeOuterClass.InviteReplyNotice.Builder notice = InviteReplyNoticeOuterClass.InviteReplyNotice.newBuilder();
        notice.setMessageId(messageID);
        long now = System.currentTimeMillis();
        notice.setSendTime(now / 1000);
        notice.setExpiredTime(now / 1000 + 200);
        notice.setNeedResponse(Boolean.FALSE);

        notice.setSenderId(sender.getOpenID());
        notice.setSenderName(sender.getNickname());
        notice.setSenderPortraitUrl(sender.getSmallPortrait());

        if(reply == InviteReply.Agree) {
            notice.setIsAgree(true);
            notice.setAlert("对方同意了邀请！");
        } else {
            notice.setIsAgree(false);

            switch (reply) {
                case Busy:
                    notice.setAlert("正在游戏中...");
                    break;
                case Refuse:
                    notice.setAlert("对方拒绝了邀请");
                    break;
                case Timeout:
                    notice.setAlert("对方未应答");
                    break;
                case Offline:
                    notice.setAlert("对方不在线");
                    break;
                default:
                    notice.setAlert("错误状态");
            }
        }

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setInviteReplyNotice(notice);
        ctx.writeAndFlush(container);
    }
}
