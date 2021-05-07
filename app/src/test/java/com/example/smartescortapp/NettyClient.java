package com.example.smartescortapp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class NettyClient {

    @Test
    public  void test() throws InterruptedException {

        // 客户端需要一个事件循环组
        EventLoopGroup group = new NioEventLoopGroup();

        try {

            // 创建客户端启动对象
            Bootstrap bootstrap = new Bootstrap();

            // 配置
            bootstrap.group(group)
                    .channel(NioSocketChannel.class) // 客户端通道的实现类
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(new NettyClientHandler());
                        }
                    });

            System.out.println("客户端 is ok...");

            // 启动客户端去连接服务器
            ChannelFuture channelFuture = bootstrap.connect("192.168.1.200", 6666).sync();
            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();

        }finally {
            group.shutdownGracefully();
        }
    }
}
