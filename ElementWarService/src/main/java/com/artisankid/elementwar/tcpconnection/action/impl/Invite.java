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
        Offline, Busy, Agree, Refuse
    }

    private Logger logger = LoggerFactory.getLogger(Invite.class);

    @ActionRequestMap(actionKey = ContainerOuterClass.Container.INVITE_MESSAGE_FIELD_NUMBER)
    public void inviteMessage(ChannelHandlerContext context, ContainerOuterClass.Container container) {
        InviteMessageOuterClass.InviteMessage message = container.getInviteMessage();
        String messageID = message.getMessageId();
        final String senderID = message.getSenderId();

        logger.debug("InviteMessage" + " messageID:" + messageID + " senderID:" + senderID + " 开始邀请...");

        String receiverID = message.getReceiverId();
        if(receiverID == null) {
            logger.error("InviteMessage" + " messageID:" + messageID + " senderID:" + senderID + " receiverID为空");
            return;
        }

        MagicianDao dao = new MagicianDao();
        final Magician receiver = dao.selectByOpenID(receiverID);
        if(receiver == null) {
            logger.error("InviteMessage" + " messageID:" + messageID + " senderID:" + senderID + " receiverID无效");
            return;
        }

        Long expiredTime = new Double(message.getExpiredTime() * 1000L).longValue();

        if(UserManager.getUser(receiverID) == null) {
            logger.debug("InviteMessage" + " messageID:" + messageID + " senderID:" + senderID + " 用户不在线，准备发送InviteReplyNotice...");
            inviteReplyNotice(senderID, messageID, receiver, InviteReply.Offline, expiredTime);
            return;
        }

        User.State state = UserManager.getUser(receiverID).getState();
        if(state == User.State.Free) {
            logger.debug("InviteMessage" + " messageID:" + messageID + " senderID:" + senderID + " 状态变为Inviting，准备发送InviteNotice...");

            UserManager.getUser(senderID).setState(User.State.Inviting);
            //UserManager.getUser(senderID).setInviteExpiredTime(expiredTime);

            UserManager.getUser(receiverID).setState(User.State.Inviting);
            //UserManager.getUser(receiverID).setInviteExpiredTime(expiredTime);

            Magician sender = dao.selectByOpenID(senderID);
            inviteNotice(receiverID, messageID, sender, expiredTime);
        } else {
            logger.debug("InviteMessage" + " messageID:" + messageID + " senderID:" + senderID + " 准备发送InviteReplyNotice...");
            inviteReplyNotice(senderID, messageID, receiver, InviteReply.Busy, expiredTime);
        }
    }

    public void inviteNotice(final String receiverID, final String messageID, final Magician sender, Long expiredTime) {
        final String senderID = sender.getOpenID();

        logger.debug("InviteMessage" + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " 开始发送...");

        InviteNoticeOuterClass.InviteNotice.Builder notice = InviteNoticeOuterClass.InviteNotice.newBuilder();
        notice.setMessageId(messageID);
        notice.setSendTime(System.currentTimeMillis() / 1000.);
        notice.setExpiredTime(expiredTime / 1000.);

        notice.setSenderId(senderID);
        String nickname = sender.getNickname();
        if(nickname != null) {
            notice.setSenderName(nickname);
        }
        String smallPortrait = sender.getNickname();
        if(smallPortrait != null) {
            notice.setSenderPortraitUrl(smallPortrait);
        }

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setMessageType(ContainerOuterClass.Container.MessageType.InviteNotice);
        container.setInviteNotice(notice);

        final Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                logger.error("InviteMessage" + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " 发送超时，状态变为Free");

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

                logger.debug("InviteMessage" + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " 发送成功");

                timer.cancel();
            }
        });
    }

    @ActionRequestMap(actionKey = ContainerOuterClass.Container.INVITE_REPLY_MESSAGE_FIELD_NUMBER)
    public void inviteReplyMessage(ChannelHandlerContext context, ContainerOuterClass.Container container) {
        InviteReplyMessageOuterClass.InviteReplyMessage message = container.getInviteReplyMessage();
        String messageID = message.getMessageId();
        String senderID = message.getSenderId();

        logger.debug("InviteReplyMessage" + " messageID:" + messageID + " senderID:" + senderID + " 开始回复邀请...");

        String receiverID = message.getReceiverId();
        if(receiverID == null) {
            logger.error("InviteReplyMessage" + " messageID:" + messageID + " senderID:" + senderID + " receiverID为空");
            return;
        }

        MagicianDao dao = new MagicianDao();
        Magician receiver = dao.selectByOpenID(receiverID);
        if(receiver == null) {
            logger.error("InviteReplyMessage" + " messageID:" + messageID + " senderID:" + senderID + " receiverID无效");
            return;
        }

        Long expiredTime = new Double(message.getExpiredTime() * 1000L).longValue();
        Magician sender = dao.selectByOpenID(senderID);
        if(message.getIsAgree()) {
            logger.debug("InviteReplyMessage" + " messageID:" + messageID + " senderID:" + senderID + " 同意邀请，状态变为Invited，准备发送InviteReplyNotice...");

            UserManager.getUser(senderID).setState(User.State.Invited);
            UserManager.getUser(receiverID).setState(User.State.Invited);

            //创建房间
            RoomManager.createRoom(Arrays.asList(senderID, receiverID));

            inviteReplyNotice(receiverID, messageID, sender, InviteReply.Agree, expiredTime);
        } else {
            logger.debug("InviteReplyMessage" + " messageID:" + messageID + " senderID:" + senderID + " 拒绝邀请，准备发送InviteReplyNotice...");
            inviteReplyNotice(receiverID, messageID, sender, InviteReply.Refuse, expiredTime);
        }
    }

    public void inviteReplyNotice(final String receiverID, final String messageID, final Magician sender, final InviteReply reply, Long expiredTime) {
        final String senderID = sender.getOpenID();

        logger.debug("InviteReplyNotice" + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " reply:" + reply + " 开始发送...");

        InviteReplyNoticeOuterClass.InviteReplyNotice.Builder notice = InviteReplyNoticeOuterClass.InviteReplyNotice.newBuilder();
        notice.setMessageId(messageID);
        notice.setSendTime(System.currentTimeMillis() / 1000L);
        notice.setExpiredTime(expiredTime / 1000L);

        notice.setSenderId(senderID);
        String nickname = sender.getNickname();
        if(nickname != null) {
            notice.setSenderName(nickname);
        }
        String smallPortrait = sender.getSmallPortrait();
        if(smallPortrait != null) {
            notice.setSenderPortraitUrl(smallPortrait);
        }

        switch (reply) {
            case Offline:
                notice.setIsAgree(false);
                notice.setAlert("对方离线");
                break;
            case Busy:
                notice.setIsAgree(false);
                notice.setAlert("正在游戏中...");
                break;
            case Agree:
                notice.setIsAgree(true);
                notice.setAlert("对方同意了邀请！");
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
        container.setMessageType(ContainerOuterClass.Container.MessageType.InviteReplyNotice);
        container.setInviteReplyNotice(notice);

        final Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                logger.error("InviteReplyNotice" + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " reply:" + reply + " 发送超时，状态变为Free");
                User socketReceiver = UserManager.getUser(receiverID);
                if(socketReceiver != null) {
                    socketReceiver.setState(User.State.Free);
                }
                UserManager.getUser(senderID).setState(User.State.Free);
            }
        };
        timer.schedule(task, expiredTime - System.currentTimeMillis());

        ChannelHandlerContext ctx = UserContextManager.getUserContext(receiverID);
        ctx.writeAndFlush(container).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                User socketReceiver = UserManager.getUser(receiverID);
                if(socketReceiver != null) {
                    if(socketReceiver.getState() == User.State.Free) {
                        return;
                    }
                }

                if(UserManager.getUser(senderID).getState() == User.State.Free) {
                    return;
                }

                logger.debug("InviteReplyNotice" + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " reply:" + reply + " 发送成功");

                timer.cancel();

                if(reply == InviteReply.Agree) {
                    logger.debug("InviteReplyNotice" + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " reply:" + reply + " 发送成功，状态变为WaitingInRoom");
                    UserManager.getUser(receiverID).setState(User.State.WaitingInRoom);

                    Room room = RoomManager.getRoom(receiverID);
                    for(User user : room.getUsers()) {
                        User.State state = user.getState();
                        if(state != User.State.WaitingInRoom) {
                            return;
                        }
                    }

                    logger.debug("InviteReplyNotice" + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " reply:" + reply + " 准备进入房间...");

                    for(User user : room.getUsers()) {
                        InRoom.InRoomNotice(user.getUserID(), room.getRoomID());
                    }
                } else {
                    logger.debug("InviteReplyNotice" + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " reply:" + reply + " 发送成功，状态变为Free");

                    if(socketReceiver != null) {
                        socketReceiver.setState(User.State.Free);
                    }
                    UserManager.getUser(senderID).setState(User.State.Free);
                }
            }
        });
    }
}
