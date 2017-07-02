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
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
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

    static private Logger logger = LoggerFactory.getLogger(Invite.class);

    @ActionRequestMap(actionKey = ContainerOuterClass.Container.INVITE_MESSAGE_FIELD_NUMBER)
    public void inviteMessage(ChannelHandlerContext context, ContainerOuterClass.Container container) {
        InviteMessageOuterClass.InviteMessage message = container.getInviteMessage();
        String messageID = message.getMessageId();
        final String senderID = message.getSenderId();

        if(UserManager.getUser(senderID).getState() != User.State.Free) {
            String error = "InviteMessage" + " messageID:" + messageID + " senderID:" + senderID + " sender状态错误";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        String receiverID = message.getReceiverId();
        if(receiverID == null) {
            String error = "InviteMessage" + " messageID:" + messageID + " senderID:" + senderID + " receiverID为空";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        MagicianDao dao = new MagicianDao();
        final Magician receiver = dao.selectByOpenID(receiverID);
        if(receiver == null) {
            String error = "InviteMessage" + " messageID:" + messageID + " senderID:" + senderID + " receiverID无效";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        logger.debug("InviteMessage" + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " 开始邀请...");

        Long expiredTime = new Double(message.getExpiredTime() * 1000L).longValue();

        if(UserManager.getUser(receiverID) == null) {
            logger.debug("InviteMessage" + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " 用户不在线，准备发送InviteReplyNotice...");
            Invite.InviteReplyNotice(senderID, messageID, InviteReply.Offline, expiredTime);
            return;
        }

        if(UserManager.getUser(receiverID).getState() != User.State.Free) {
            logger.debug("InviteMessage" + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " 被邀请用户状态为Busy，准备发送InviteReplyNotice...");
            Invite.InviteReplyNotice(senderID, messageID, InviteReply.Busy, expiredTime);
            return;
        }

        logger.debug("InviteMessage" + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " 状态变为Inviting，准备发送InviteNotice...");

        EpicManager.WriteEpic(senderID, null, "向" + receiverID  + "发送了战书，要消灭这个邪恶的懦夫！");

        UserManager.getUser(senderID).setState(User.State.Inviting);
        UserManager.getUser(receiverID).setState(User.State.Inviting);

        Invite.InviteNotice(receiverID, messageID, senderID, expiredTime);
    }

    static public void InviteNotice(final String receiverID, final String messageID, final String senderID, final Long expiredTime) {
        logger.debug("InviteNotice" + " messageID:" + messageID + " receiverID:" + receiverID + " 开始发送...");

        InviteNoticeOuterClass.InviteNotice.Builder notice = InviteNoticeOuterClass.InviteNotice.newBuilder();
        notice.setMessageId(messageID);
        notice.setSendTime(System.currentTimeMillis() / 1000.);
        notice.setExpiredTime(expiredTime / 1000.);

        Magician sender = new MagicianDao().selectByOpenID(senderID);
        if(sender.getOpenID() != null) {
            notice.setSenderId(sender.getOpenID());
        }
        if(sender.getNickname() != null) {
            notice.setSenderName(sender.getNickname());
        }
        if(sender.getSmallPortrait() != null) {
            notice.setSenderPortraitUrl(sender.getSmallPortrait());
        }

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setMessageType(ContainerOuterClass.Container.MessageType.InviteNotice);
        container.setInviteNotice(notice);

        final Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                logger.error("InviteNotice" + " messageID:" + messageID + " receiverID:" + receiverID + " 发送超时，状态变为Free");
                UserManager.getUser(receiverID).setState(User.State.Free);
                UserManager.getUser(senderID).setState(User.State.Free);
            }
        };
        timer.schedule(task, expiredTime - System.currentTimeMillis());

        ChannelHandlerContext ctx = UserContextManager.getContext(receiverID);
        ctx.writeAndFlush(container).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(expiredTime <= System.currentTimeMillis()) {
                    return;
                }

                timer.cancel();

                if(future.isCancelled()
                        || !future.isSuccess()) {
                    logger.error("InviteNotice" + " messageID:" + messageID + " receiverID:" + receiverID  + " 发送失败，状态为Free");
                    UserManager.getUser(receiverID).setState(User.State.Free);
                    UserManager.getUser(senderID).setState(User.State.Free);
                    return;
                }

                logger.debug("InviteNotice" + " messageID:" + messageID + " receiverID:" + receiverID + " 发送成功");

                EpicManager.WriteEpic(receiverID, null, "向" + receiverID  + "发送的战书已经送达！");
            }
        });
    }

    @ActionRequestMap(actionKey = ContainerOuterClass.Container.INVITE_REPLY_MESSAGE_FIELD_NUMBER)
    public void inviteReplyMessage(ChannelHandlerContext context, ContainerOuterClass.Container container) {
        InviteReplyMessageOuterClass.InviteReplyMessage message = container.getInviteReplyMessage();
        String messageID = message.getMessageId();
        String senderID = message.getSenderId();

        String receiverID = message.getReceiverId();
        if(receiverID == null) {
            String error = "InviteReplyMessage" + " messageID:" + messageID + " senderID:" + senderID + " receiverID为空";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        MagicianDao dao = new MagicianDao();
        Magician receiver = dao.selectByOpenID(receiverID);
        if(receiver == null) {
            String error = "InviteReplyMessage" + " messageID:" + messageID + " senderID:" + senderID + " receiverID无效";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        logger.debug("InviteReplyMessage" + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " 开始回复邀请...");

        Long expiredTime = new Double(message.getExpiredTime() * 1000L).longValue();

        if(message.getIsAgree()) {
            logger.debug("InviteReplyMessage" + " messageID:" + messageID + " senderID:" + senderID + " 同意邀请，状态变为Invited，准备发送InviteReplyNotice...");

            EpicManager.WriteEpic(senderID, null, "接受了" + receiverID  + "的战书，尽管放马过来吧！");
            EpicManager.WriteEpic(receiverID, null, senderID  + "接受了战书，让他知道我们的拳头到底有多硬！");

            UserManager.getUser(senderID).setState(User.State.Invited);
            UserManager.getUser(receiverID).setState(User.State.Invited);

            //创建房间
            RoomManager.createRoom(Arrays.asList(senderID, receiverID));

            Invite.InviteReplyNotice(receiverID, messageID, InviteReply.Agree, expiredTime);
            Invite.InviteReplyNotice(senderID, messageID, InviteReply.Agree, expiredTime);
        } else {
            logger.debug("InviteReplyMessage" + " messageID:" + messageID + " senderID:" + senderID + " 拒绝邀请，准备发送InviteReplyNotice...");

            EpicManager.WriteEpic(senderID, null, "拒绝了" + receiverID  + "的挑战，留他一条小命！");
            EpicManager.WriteEpic(receiverID, null, senderID  + "拒绝了挑战，吓破他的胆子！");

            UserManager.getUser(senderID).setState(User.State.Free);
            UserManager.getUser(receiverID).setState(User.State.Free);

            Invite.InviteReplyNotice(receiverID, messageID, InviteReply.Refuse, expiredTime);
            Invite.InviteReplyNotice(senderID, messageID, InviteReply.Refuse, expiredTime);
        }
    }

    static public void InviteReplyNotice(final String receiverID, final String messageID, final InviteReply reply, final Long expiredTime) {
        logger.debug("InviteReplyNotice" + " messageID:" + messageID + " receiverID:" + receiverID + " reply:" + reply + " 开始发送...");

        InviteReplyNoticeOuterClass.InviteReplyNotice.Builder notice = InviteReplyNoticeOuterClass.InviteReplyNotice.newBuilder();
        notice.setMessageId(messageID);
        notice.setSendTime(System.currentTimeMillis() / 1000L);
        notice.setExpiredTime(expiredTime / 1000L);

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
                logger.error("InviteReplyNotice" + " messageID:" + messageID + " receiverID:" + receiverID + " reply:" + reply + " 发送超时，状态变为Free");
                Invite.InviteReplyNoticeFailed(receiverID);
            }
        };
        timer.schedule(task, expiredTime - System.currentTimeMillis());

        ChannelHandlerContext ctx = UserContextManager.getContext(receiverID);
        ctx.writeAndFlush(container).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(expiredTime <= System.currentTimeMillis()) {
                    return;
                }

                timer.cancel();

                if(future.isCancelled()
                        || !future.isSuccess()) {
                    logger.error("InviteReplyNotice" + " messageID:" + messageID + " receiverID:" + receiverID  + " 发送失败，状态为Free");
                    Invite.InviteReplyNoticeFailed(receiverID);
                    return;
                }

                if(reply == InviteReply.Agree) {
                    Room room = RoomManager.getRoom(receiverID);
                    if(room == null) {
                        logger.debug("InviteReplyNotice" + " messageID:" + messageID + " receiverID:" + receiverID + " 已有用户发送失败");
                        return;
                    }

                    logger.debug("InviteReplyNotice" + " messageID:" + messageID + " receiverID:" + receiverID + " reply:" + reply + " 发送成功，状态变为WaitingInRoom");

                    EpicManager.WriteEpic(receiverID, null,  "接受战书的回复已经送达，血液已经沸腾起来了！");

                    UserManager.getUser(receiverID).setState(User.State.InRooming);

                    for(User user : room.getUsers()) {
                        if(user.getState() != User.State.InRooming) {
                            logger.debug("InviteReplyNotice" + " messageID:" + messageID + " receiverID:" + receiverID + " 等待其他人状态变更");
                            return;
                        }
                    }

                    logger.debug("InviteReplyNotice" + " messageID:" + messageID + " receiverID:" + receiverID + " reply:" + reply + " 准备进入房间...");

                    for(User user : room.getUsers()) {
                        InRoom.InRoomNotice(user.getUserID(), room.getRoomID());
                    }
                } else {
                    logger.debug("InviteReplyNotice" + " messageID:" + messageID + " receiverID:" + receiverID + " reply:" + reply + " 发送成功，状态变为Free");
                    EpicManager.WriteEpic(receiverID, null,  "拒绝战书的回复已经送达！");
                }
            }
        });
    }

    static private void InviteReplyNoticeFailed(String userID) {
        Room room = RoomManager.getRoom(userID);
        if(room == null) {
            return;
        }

        for(User user : room.getUsers()) {
            user.setState(User.State.Free);
        }
        RoomManager.removeRoom(userID);
    }
}
