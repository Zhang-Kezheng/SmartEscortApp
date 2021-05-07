package com.example.smartescortapp.netty;

import android.os.Handler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author 章可政
 * @date 2021/4/27
 */
public class NettyClient {
    /**
     * TCP客户端实例，单例
     */
    private static NettyClient instance;
    /**
     * 服务器IP
     */
    private static String ip = "192.168.1.7";
    /**
     * 服务器端口
     */
    private static int port = 6666;
    /**
     * 客户端连接服务器连接次数。超过三次后退出客户端
     */
    private int times;
    private NettyClientHandler nettyClientHandler;

    private NioEventLoopGroup workGroup = new NioEventLoopGroup(4);

    private Channel channel;

    private Bootstrap bootstrap;

    private Handler handler;

    public static NettyClient getInstance() {
        if (instance == null) {
            instance = new NettyClient();
        }
        return instance;
    }

    private NettyClient() {
        bootstrap = new Bootstrap();
        nettyClientHandler = new NettyClientHandler(this);
        bootstrap.group(workGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,1,1,3,0));
                        ch.pipeline().addLast("decoder", new Decoder());
                        ch.pipeline().addLast("encoder", new Encoder());
                        ch.pipeline().addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                        ch.pipeline().addLast(nettyClientHandler);
                    }

                });
    }


    public void doConnect() {

        if (channel != null && channel.isActive()) {
            return;
        }
        ChannelFuture future = bootstrap.connect(ip, port);
        //正在尝试连接客户端
        if (times == 0) handler.sendMessage(handler.obtainMessage(MsgCommand.CONNECT));
        times++;
        future.addListener((ChannelFutureListener) futureListener -> {
            if (futureListener.isSuccess()) {
                channel = futureListener.channel();
                System.out.println("Connect to server successfully!");
                times = 0;
                //连接成功
                handler.sendMessage(handler.obtainMessage(MsgCommand.CONNECT_SUCCESS));
            } else {
                System.out.println("Failed to connect to server, try connect after 3s");
                //连接失败，正在尝试重连客户端
                if (times<=5){
                    handler.sendMessage(handler.obtainMessage(MsgCommand.RECONNECT));
                }else {
                    handler.sendMessage(handler.obtainMessage(MsgCommand.CONNECT_FAIL));
                }
                futureListener.channel().eventLoop().schedule(this::doConnect, 3, TimeUnit.SECONDS);
            }
        });
    }

    public void sendMessage(Message message, int what) {
        nettyClientHandler.setWhat(what);
        nettyClientHandler.sendMessage(message);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
        nettyClientHandler.setHandler(handler);
    }
}
