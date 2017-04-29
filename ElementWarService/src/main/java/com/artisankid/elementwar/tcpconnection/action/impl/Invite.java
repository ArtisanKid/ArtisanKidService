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
import com.artisankid.elementwar.tcpconnection.gate.utils.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by LiXiangYu on 2017/4/26.
 */
@NettyAction
public class Invite {
    enum InviteReply {
        Busy, Agree, Refuse
    }

    private Logger logger = LoggerFactory.getLogger(Invite.class);

    @ActionRequestMap(actionKey = ContainerOuterClass.Container.INVITE_MESSAGE_FIELD_NUMBER)
    public void inviteMessage(ChannelHandlerContext context, ContainerOuterClass.Container container) {
        InviteMessageOuterClass.InviteMessage message = container.getInviteMessage();
        final String senderID = message.getSenderId();

        //首先判断是否超时
        long expiredTime = (long) (message.getExpiredTime() * 1000);
        if(expiredTime <= System.currentTimeMillis()) {
            UserManager.getUser(senderID).setState(User.State.Free);
            return;
        }

        String receiverID = message.getReceiverId();
        if(receiverID == null) {
            return;
        }

        MagicianDao dao = new MagicianDao();
        final Magician receiver = dao.selectByOpenID(receiverID);
        if(receiver == null) {
            return;
        }

        String messageID = message.getMessageId();
        User.State state = UserManager.getUser(receiverID).getState();
        if(state == User.State.Free) {
            UserManager.getUser(senderID).setState(User.State.Inviting);
            //UserManager.getUser(senderID).setInviteExpiredTime(expiredTime);

            UserManager.getUser(receiverID).setState(User.State.Inviting);
            //UserManager.getUser(receiverID).setInviteExpiredTime(expiredTime);

            Magician sender = dao.selectByOpenID(senderID);
            inviteNotice(receiverID, messageID, sender, expiredTime);
        } else {
            inviteReplyNotice(senderID, messageID, receiver, InviteReply.Busy, expiredTime);
        }
    }

    public void inviteNotice(final String receiverID, final String messageID, final Magician sender, long expiredTime) {
        final String senderID = sender.getOpenID();

        if(expiredTime <= System.currentTimeMillis()) {
            UserManager.getUser(receiverID).setState(User.State.Free);
            UserManager.getUser(senderID).setState(User.State.Free);
            return;
        }

        InviteNoticeOuterClass.InviteNotice.Builder notice = InviteNoticeOuterClass.InviteNotice.newBuilder();
        notice.setMessageId(messageID);
        notice.setSendTime(System.currentTimeMillis() / 1000);
        notice.setExpiredTime(expiredTime / 1000);
        notice.setNeedResponse(Boolean.TRUE);

        notice.setSenderId(sender.getOpenID());
        notice.setSenderName(sender.getNickname());
        notice.setSenderPortraitUrl(sender.getSmallPortrait());

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setInviteNotice(notice);

        final Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                UserManager.getUser(receiverID).setState(User.State.Free);
                UserManager.getUser(sender.getOpenID()).setState(User.State.Free);
            }
        };
        timer.schedule(task, expiredTime - System.currentTimeMillis());

        ChannelHandlerContext ctx = UserContextManager.getUserContext(receiverID);
        ctx.writeAndFlush(container).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if(UserManager.getUser(receiverID).getState() == User.State.Free
                        || UserManager.getUser(senderID).getState() == User.State.Free) {
                    return;
                }

                timer.cancel();
            }
        });
    }

    @ActionRequestMap(actionKey = ContainerOuterClass.Container.INVITE_REPLY_MESSAGE_FIELD_NUMBER)
    public void inviteReplyMessage(ChannelHandlerContext context, ContainerOuterClass.Container container) {
        InviteReplyMessageOuterClass.InviteReplyMessage message = container.getInviteReplyMessage();

        String receiverID = message.getReceiverId();
        if(receiverID == null) {
            return;
        }

        MagicianDao dao = new MagicianDao();
        Magician receiver = dao.selectByOpenID(receiverID);
        if(receiver == null) {
            return;
        }

        String senderID = message.getSenderId();

        //首先判断是否超时
        //被邀请用户的邀请回复超时时间，应该和邀请用户的超时时间是一致的
        long expiredTime = (long) (message.getExpiredTime() * 1000);
        if(expiredTime <= System.currentTimeMillis()) {
            UserManager.getUser(senderID).setState(User.State.Free);
            UserManager.getUser(receiverID).setState(User.State.Free);
            return;
        }

        Magician sender = dao.selectByOpenID(senderID);
        if(sender == null) {
            return;
        }

        String messageID = message.getMessageId();
        if(message.getIsAgree()) {
            RoomManager.createRoom(Arrays.asList(senderID, receiverID));
            UserManager.getUser(senderID).setState(User.State.Invited);
            UserManager.getUser(senderID).setHp(30);
            UserManager.getUser(receiverID).setState(User.State.Invited);
            UserManager.getUser(receiverID).setHp(30);

            inviteReplyNotice(receiverID, messageID, sender, InviteReply.Agree, expiredTime);
        } else {
            inviteReplyNotice(receiverID, messageID, sender, InviteReply.Refuse, expiredTime);
        }
    }

    public void inviteReplyNotice(final String receiverID, String messageID, final Magician sender, final InviteReply reply, long expiredTime) {
        final String senderID = sender.getOpenID();

        if(expiredTime <= System.currentTimeMillis()) {
            UserManager.getUser(receiverID).setState(User.State.Free);
            UserManager.getUser(senderID).setState(User.State.Free);
            return;
        }

        InviteReplyNoticeOuterClass.InviteReplyNotice.Builder notice = InviteReplyNoticeOuterClass.InviteReplyNotice.newBuilder();
        notice.setMessageId(messageID);
        notice.setSendTime(System.currentTimeMillis() / 1000);
        notice.setExpiredTime(expiredTime / 1000);
        notice.setNeedResponse(Boolean.FALSE);

        notice.setSenderId(senderID);
        notice.setSenderName(sender.getNickname());
        notice.setSenderPortraitUrl(sender.getSmallPortrait());

        switch (reply) {
            case Agree:
                notice.setIsAgree(true);
                notice.setAlert("对方同意了邀请！");
                break;
            case Busy:
                notice.setIsAgree(false);
                notice.setAlert("正在游戏中...");
                break;
            case Refuse:
                notice.setIsAgree(false);
                notice.setAlert("对方拒绝了邀请");
                break;
            default:
                notice.setIsAgree(false);
                notice.setAlert("错误状态");
        }

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setInviteReplyNotice(notice);

        final Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                UserManager.getUser(receiverID).setState(User.State.Free);
                UserManager.getUser(senderID).setState(User.State.Free);
            }
        };
        timer.schedule(task, expiredTime - System.currentTimeMillis());

        ChannelHandlerContext ctx = UserContextManager.getUserContext(receiverID);
        ctx.writeAndFlush(container).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if(UserManager.getUser(receiverID).getState() == User.State.Free
                        || UserManager.getUser(senderID).getState() == User.State.Free) {
                    return;
                }

                timer.cancel();

                if(reply == InviteReply.Agree) {
                    UserManager.getUser(receiverID).setState(User.State.WaitingInRoom);

                    Room room = RoomManager.getRoom(receiverID);
                    for(User user : room.getUsers()) {
                        User.State state = user.getState();
                        if(state != User.State.WaitingInRoom) {
                            return;
                        }
                    }

                    for(User user : room.getUsers()) {
                        InRoom.InRoomNotice(user.getUserID(), room.getRoomID());
                    }
                } else {
                    UserManager.getUser(receiverID).setState(User.State.Free);
                    UserManager.getUser(senderID).setState(User.State.Free);
                }
            }
        });
    }
}
