package com.artisankid.elementwar.tcpconnection.client;

import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.DealNoticeOuterClass;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;


/**
 * Created by Dell on 2016/2/15.
 * 模拟客户端聊天：自己给自己发消息
 */
public class ClientHandler extends SimpleChannelInboundHandler<Object>  {

    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        String message="春 朱自清 \n" +
                "盼望着，盼望着，东风来了，春天的脚步近了。 \n" +
                "一切都像刚睡醒的样子，欣欣然张开了眼。山朗润起来了，水涨起来了，太阳的脸红起来了。 \n" +
                "小草偷偷地从土地里钻出来，嫩嫩的，绿绿的。园子里，田野里，瞧去，一大片一大片满是的。坐着，躺着，打两个滚，踢几脚球，赛几趟跑，捉几回迷藏。风轻俏俏的，草软绵绵的。 \n" +
                "桃树，杏树，梨树，你不让我，我不让你，都开满了花赶趟儿。红的像火，粉的像霞，白的像雪。花里带着甜味；闭了眼，树上仿佛已经满是桃儿，杏儿，梨儿。花下成千成百的蜜蜂嗡嗡的闹着，大小的蝴蝶飞来飞去。野花遍地是：杂样儿，有名字的，没名字的，散在草丛里像眼睛像星星，还眨呀眨。 \n" +
                "“吹面不寒杨柳风”，不错的，像母亲的手抚摸着你，风里带着些心翻的泥土的气息，混着青草味儿，还有各种花的香，都在微微润湿的空气里酝酿。鸟儿将巢安在繁花嫩叶当中，高兴起来，呼朋引伴的卖弄清脆的歌喉，唱出婉转的曲子，跟清风流水应和着。牛背上牧童的短笛，这时候也成天嘹亮的响着。 \n" +
                "雨是最寻常的，一下就是三两天。可别恼。看，像牛牦，像花针，像细丝，密密的斜织着，人家屋顶上全笼着一层薄烟。树叶却绿得发亮，小草也青得逼你的眼。傍晚时候，上灯了，一点点黄晕的光，烘托出一片安静而和平的夜。在乡下，小路上，石桥边，有撑着伞慢慢走着的人，地里还有工作的农民，披着所戴着笠。他们的房屋稀稀疏疏的，在雨里静默着。 \n" +
                "天上的风筝渐渐多了，地上的孩子也多了。城里乡下，家家户户，老老小小，也赶趟似的，一个个都出来了。舒活舒活筋骨，抖擞抖擞精神，各做各的一份事儿去。“一年之计在于春”，刚起头儿，有的是功夫，有的是希望 \n" +
                "春天像刚落地的娃娃，从头到脚都是新的，它生长着。 \n" +
                "春天像小姑娘，花枝招展的笑着走着。 \n" +
                "春天像健壮的青年，有铁一般的胳膊和腰脚，领着我们向前去。";
        DealNoticeOuterClass.DealNotice.Builder dealNotice=  DealNoticeOuterClass.DealNotice.newBuilder();
        dealNotice.setMessageId(message);

        Calendar nowDate=Calendar.getInstance();
        double sendTime = nowDate.getTimeInMillis() / 1000;
        dealNotice.setSendTime(sendTime);

        nowDate.add(Calendar.SECOND,3000);
        dealNotice.setExpiredTime(nowDate.getTimeInMillis() / 1000);
        dealNotice.setNeedResponse(Boolean.TRUE);
        dealNotice.setCalibrationTime(nowDate.getTimeInMillis()/1000);


        ContainerOuterClass.Container.Builder containerBuilder = ContainerOuterClass.Container.newBuilder();
        containerBuilder.setDealNotice(dealNotice);
        ctx.writeAndFlush(containerBuilder);

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ContainerOuterClass.Container container = (ContainerOuterClass.Container) msg;
        DealNoticeOuterClass.DealNotice dealNotice = container.getDealNotice();

        String messageId = dealNotice.getMessageId();
        logger.error("receive msg:" + messageId);

        DealNoticeOuterClass.DealNotice.Builder returnDealNotice=  DealNoticeOuterClass.DealNotice.newBuilder();
        returnDealNotice.setMessageId("0000_000001_00000000000000000000000000000000000");

        Calendar nowDate=Calendar.getInstance();
        double sendTime = nowDate.getTimeInMillis() / 1000;
        returnDealNotice.setSendTime(sendTime);

        nowDate.add(Calendar.SECOND,3000);
        returnDealNotice.setExpiredTime(nowDate.getTimeInMillis() / 1000);
        returnDealNotice.setNeedResponse(Boolean.TRUE);
        returnDealNotice.setCalibrationTime(nowDate.getTimeInMillis()/1000);


        ContainerOuterClass.Container.Builder containerBuilder = ContainerOuterClass.Container.newBuilder();
        containerBuilder.setDealNotice(returnDealNotice);
        ctx.writeAndFlush(containerBuilder);


    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("exceptionCaught-------------------,cause"+cause.getMessage());
    }
}
