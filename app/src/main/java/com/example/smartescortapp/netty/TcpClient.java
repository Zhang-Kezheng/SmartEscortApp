package com.example.smartescortapp.netty;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.smartescortapp.SmartEscortApplication;
import com.example.smartescortapp.entity.LoginResponse;
import com.example.smartescortapp.entity.PlatformResponse;
import com.example.smartescortapp.entity.User;
import com.google.gson.Gson;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.core.protocol.IReaderProtocol;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.client.sdk.client.action.SocketActionAdapter;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 章可政
 * @date 2021/4/25
 */
public class TcpClient {

    private Handler handler;
    private int what;
    public static TcpClient instance;
    public IConnectionManager manager;
    public  byte seq;
    private Gson gson=new Gson();
    public static TcpClient getInstance(){
        if (instance==null){
            instance=new TcpClient();
        }
        return instance;
    }
    public void start(){
        ConnectionInfo info = new ConnectionInfo("124.71.130.182", 6666);
        //调用OkSocket,开启这次连接的通道,调用通道的连接方法进行连接.
        manager = OkSocket.open(info);
//        //获得当前连接通道的参配对象
        OkSocketOptions options= manager.getOption();
        manager.registerReceiver(new SocketActionAdapter(){
            @Override
            public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
                System.out.println("连接成功");
                OkSocket.open(info)
                        .getPulseManager()
                        .setPulseSendable(Message.getMessage(0,"",seq++))//只需要设置一次,下一次可以直接调用pulse()
                        .pulse();//开始心跳,开始心跳后,心跳管理器会自动进行心跳触发
            }

            @Override
            public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(data.getBodyBytes().length+data.getHeadBytes().length);
                byteBuffer.put(data.getHeadBytes());
                byteBuffer.put(data.getBodyBytes());
                Message instance = Message.getInstance(byteBuffer.array());
                if (instance==null)return;
                if (instance.getMSG()==0){
                    manager.getPulseManager().feed();
                    return;
                }
                android.os.Message message = handler.obtainMessage();
                message.what=what;
                Bundle bundle = new Bundle();
                bundle.putSerializable("data",instance);
                message.setData(bundle);
                handler.sendMessage(message);

            }


            @Override
            public void onSocketDisconnection(ConnectionInfo info, String action, Exception e) {
                System.out.println("断开连接");
            }
        });
        //调用通道进行连接
        manager.connect();
        OkSocketOptions.Builder okOptionsBuilder = new OkSocketOptions.Builder(options);
        okOptionsBuilder.setReaderProtocol(new IReaderProtocol() {
            @Override
            public int getHeaderLength() {
                return 4;
            }

            @Override
            public int getBodyLength(byte[] header, ByteOrder byteOrder) {
                int a=(header[1]&0xff)+1;
                return a;
            }
        });
        manager.option(okOptionsBuilder.build());

    }

    public void sendMessage(Message message,Handler handler,int what) {
        this.handler=handler;
        this.what=what;
        manager.send(message);
        seq++;
    }


}
