package com.artisankid.elementwar.tcpconnection.action.impl;
import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.DeadNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.annotations.ActionRequestMap;
import com.artisankid.elementwar.tcpconnection.annotations.NettyAction;
import com.artisankid.elementwar.tcpconnection.gate.utils.EpicManager;
import com.artisankid.elementwar.tcpconnection.gate.utils.RoomManager;
import com.artisankid.elementwar.tcpconnection.gate.utils.User;
import com.artisankid.elementwar.tcpconnection.gate.utils.UserContextManager;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by LiXiangYu on 2017/7/2.
 */
@NettyAction
public class Dead {
    static private Logger logger = LoggerFactory.getLogger(Dead.class);

    @ActionRequestMap(actionKey = ContainerOuterClass.Container.DEAD_NOTICE_FIELD_NUMBER)
    static public void DeadNotice(String deadID) {
        logger.debug("FinishNotice" + " deadID:" + deadID + " 开始发送...");

        DeadNoticeOuterClass.DeadNotice.Builder notice = DeadNoticeOuterClass.DeadNotice.newBuilder();
        long now = System.currentTimeMillis();
        long expiredTime = now + 30 * 1000;
        notice.setSendTime(now / 1000);
        notice.setExpiredTime(expiredTime / 1000);
        notice.setDeadId(deadID);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setMessageType(ContainerOuterClass.Container.MessageType.DeadNotice);
        container.setDeadNotice(notice);

        for(User user : RoomManager.getRoom(deadID).getUsers()) {
            if(user.getUserID().equals(deadID)) {
                user.setState(User.State.Free);
                user.setGameState(User.GameState.Waiting);

                EpicManager.WriteEpic(RoomManager.getRoom(deadID).getRoomID(), "出师未捷身先死，长使英雄泪满襟！");
            } else {
                EpicManager.WriteEpic(RoomManager.getRoom(deadID).getRoomID(), deadID + "倒下了！");
            }

            //循环发送finish消息，不再关注是否已经发送到客户端
            ChannelHandlerContext ctx = UserContextManager.getContext(user.getUserID());
            ctx.writeAndFlush(container);
        }

        RoomManager.removeRoom(deadID);
    }
}
