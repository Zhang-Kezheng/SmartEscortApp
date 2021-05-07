package com.example.smartescortapp.netty;

import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.example.smartescortapp.SmartEscortApplication;
import com.google.gson.Gson;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author 章可政
 * @date 2021/4/27
 */
@ChannelHandler.Sharable
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private Handler handler;
    private int what;
    private ChannelHandlerContext ctx;
    private NettyClient client;

    public NettyClientHandler(NettyClient client) {
        this.client = client;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message instance= (Message) msg;
        if (instance==null)return;
        android.os.Message message = handler.obtainMessage();
        message.what=what;
        Bundle bundle = new Bundle();
        bundle.putSerializable("data",instance);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx=ctx;
        ctx.fireChannelActive();
        System.out.println("连接成功");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        client.doConnect();
        if (SmartEscortApplication.requestUser!=null){
            ctx.writeAndFlush(Message.getMessage(1,new Gson().toJson(SmartEscortApplication.requestUser),MsgCommand.getSEQ()));
        }
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public void sendMessage(Message message){
        ctx.writeAndFlush(message);
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                // write heartbeat to server
                ctx.writeAndFlush(Message.getMessage(MsgCommand.HEART_COMMAND,"", MsgCommand.getSEQ()));
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }

    }
}
