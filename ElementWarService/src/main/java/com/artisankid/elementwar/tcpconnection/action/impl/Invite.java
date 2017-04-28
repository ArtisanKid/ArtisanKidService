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
import com.artisankid.elementwar.tcpconnection.gate.utils.UserContextManager;
import com.artisankid.elementwar.tcpconnection.gate.utils.UserStateManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

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
        final String messageID = message.getMessageId();

        MagicianDao dao = new MagicianDao();

        final String senderID = message.getSenderId();
        Magician sender = dao.selectByOpenID(senderID);

        String receiverID = message.getReceiverId();
        if(receiverID == null) {
            return;
        }

        final Magician receiver = dao.selectByOpenID(receiverID);
        if(receiver == null) {
            return;
        }

        UserStateManager.UserState state = UserStateManager.getUserState(receiverID);
        if(state == UserStateManager.UserState.Busy) {
            inviteReplyNotice(senderID, messageID, receiver, InviteReply.Busy);
        } else if(state == UserStateManager.UserState.Free) {
            inviteNotice(receiver, messageID, sender);
        }
    }

    public void inviteNotice(final Magician receiver, final String messageID, final Magician sender) {
        InviteNoticeOuterClass.InviteNotice.Builder notice = InviteNoticeOuterClass.InviteNotice.newBuilder();
        notice.setMessageId(messageID);
        long now = System.currentTimeMillis();
        notice.setSendTime(now / 1000);
        notice.setExpiredTime(now / 1000 + 30);
        notice.setNeedResponse(Boolean.TRUE);

        notice.setSenderId(sender.getOpenID());
        notice.setSenderName(sender.getNickname());
        notice.setSenderPortraitUrl(sender.getSmallPortrait());

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setInviteNotice(notice);

        final Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                inviteReplyNotice(sender.getOpenID(), messageID, receiver, InviteReply.Timeout);
            }
        };
        timer.schedule(task, 30 * 1000);

        ChannelHandlerContext ctx = UserContextManager.getUserContext(receiver.getOpenID());
        ctx.writeAndFlush(container).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                timer.cancel();
            }
        });
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

        if(message.getIsAgree()) {
            inviteReplyNotice(receiverID, message.getMessageId(), sender, InviteReply.Agree);
        } else {
            inviteReplyNotice(receiverID, message.getMessageId(), sender, InviteReply.Refuse);
        }
    }

    public void inviteReplyNotice(String receiverID, String messageID, Magician sender, final InviteReply reply) {
        InviteReplyNoticeOuterClass.InviteReplyNotice.Builder notice = InviteReplyNoticeOuterClass.InviteReplyNotice.newBuilder();
        notice.setMessageId(messageID);
        long now = System.currentTimeMillis();
        notice.setSendTime(now / 1000);
        notice.setExpiredTime(now / 1000 + 30);
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

        Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {

            }
        };
        timer.schedule(task, 30 * 1000);

        ChannelHandlerContext ctx = UserContextManager.getUserContext(receiverID);
        ctx.writeAndFlush(container).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if(reply == InviteReply.Agree) {
                    //TODO:进入房间通知
                }
            }
        });
    }
}
