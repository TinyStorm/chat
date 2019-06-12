package org.meng.chat.server.handler.biz;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.meng.chat.server.service.Hall;

public class BizHandler extends ChannelInboundHandlerAdapter {

    private static final String CMD_ENTER = ":enter ";
    private static final String CMD_QUIT = ":quit";
    private static final String CMD_SCAN_ROOM = ":scan room";
    private static final String CMD_RENAME_SELF = ":rename ";
    private static final String SCAN_USER = ":scan user";

    private Hall hall;

    public BizHandler(Hall hall) {
        this.hall = hall;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        hall.register(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        hall.unRegister(ctx.channel());
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof String) {
            String message = (String) msg;
            if (message.startsWith(CMD_ENTER)) {
                String room = message.substring(7);
                hall.enter(room, ctx.channel());
                return;
            }
            if (message.equals(CMD_QUIT)) {
                hall.quit(ctx.channel());
                return;
            }
            if (message.equals(CMD_SCAN_ROOM)) {
                ctx.channel().writeAndFlush(hall.listRoom());
                return;
            }
            if (message.startsWith(CMD_RENAME_SELF)) {
                hall.rename(ctx.channel(), message.substring(8));
                return;
            }
            if (message.equals(SCAN_USER)) {
                hall.scanUser(ctx.channel());
                return;
            }
            hall.speak(ctx.channel().attr(Hall.NAME_KEY).get(), message);


        }
        //其他的都是业务处理


    }

}
