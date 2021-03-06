package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.FinishNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.gate.utils.EpicManager;
import com.artisankid.elementwar.tcpconnection.gate.utils.RoomManager;
import com.artisankid.elementwar.tcpconnection.gate.utils.User;
import com.artisankid.elementwar.tcpconnection.gate.utils.UserContextManager;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by LiXiangYu on 2017/4/26.
 */
public class Finish {
    private static Logger logger = LoggerFactory.getLogger(Finish.class);

    static public void FinishNotice(String winnerID) {
        logger.debug("FinishNotice" + " winnerID:" + winnerID + " 开始发送...");

        FinishNoticeOuterClass.FinishNotice.Builder notice = FinishNoticeOuterClass.FinishNotice.newBuilder();
        long now = System.currentTimeMillis();
        long expiredTime = now + 30 * 1000;
        notice.setSendTime(now / 1000);
        notice.setExpiredTime(expiredTime / 1000);
        notice.setWinnerId(winnerID);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setMessageType(ContainerOuterClass.Container.MessageType.FinishNotice);
        container.setFinishNotice(notice);

        for(User user : RoomManager.getRoom(winnerID).getUsers()) {
            if(user.getUserID().equals(winnerID)) {
                EpicManager.WriteEpic(RoomManager.getRoom(winnerID).getRoomID(), "战士用鲜血守护荣耀，胜利是勇者最佳的褒奖！");
            } else {
                EpicManager.WriteEpic(RoomManager.getRoom(winnerID).getRoomID(), "在不公平的命运之中仍然至死奋战，真正的勇士虽败犹荣！");
            }

            //循环发送finish消息，不再关注是否已经发送到客户端
            ChannelHandlerContext ctx = UserContextManager.getContext(user.getUserID());
            ctx.writeAndFlush(container);

            user.setState(User.State.Free);
            user.setGameState(User.GameState.Waiting);
        }

        RoomManager.removeRoom(winnerID);
    }
}
