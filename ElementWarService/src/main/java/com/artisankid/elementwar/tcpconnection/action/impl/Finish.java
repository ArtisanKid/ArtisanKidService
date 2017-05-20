package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.FinishNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.gate.utils.RoomManager;
import com.artisankid.elementwar.tcpconnection.gate.utils.User;
import com.artisankid.elementwar.tcpconnection.gate.utils.UserContextManager;
import com.artisankid.elementwar.tcpconnection.gate.utils.UserManager;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by LiXiangYu on 2017/4/26.
 */
public class Finish {
    private Logger logger = LoggerFactory.getLogger(Finish.class);

    static public void FinishNotice(String winnerID) {
        FinishNoticeOuterClass.FinishNotice.Builder notice = FinishNoticeOuterClass.FinishNotice.newBuilder();
        long now = System.currentTimeMillis();
        notice.setSendTime(now / 1000);
        notice.setExpiredTime(now / 1000 + 30);
        notice.setNeedResponse(Boolean.FALSE);
        notice.setWinnerId(winnerID);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setMessageType(ContainerOuterClass.Container.MessageType.FinishNotice);
        container.setFinishNotice(notice);

        List<User> users = RoomManager.getRoom(winnerID).getUsers();
        for(User user : users) {
            ChannelHandlerContext ctx = UserContextManager.getUserContext(winnerID);
            ctx.writeAndFlush(container);

            user.setState(User.State.Free);
            user.setGameState(User.GameState.Waiting);
        }

        RoomManager.removeRoom(winnerID);
    }
}
