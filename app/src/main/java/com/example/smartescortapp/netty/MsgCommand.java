package com.example.smartescortapp.netty;

/**
 * @author 章可政
 * @create 2021/4/13
 */
public class MsgCommand {
    /**
     * 连接服务器成功
     */
    public static final int CONNECT_SUCCESS=-4;
    /**
     * 连接服务器失败
     */
    public static final int CONNECT_FAIL=-3;
    /**
     * 重新连接服务器
     */
    public static final int RECONNECT=-2;
    /**
     * 正在连接服务器
     */
    public static final int CONNECT=-1;
    /**
     * 心跳包指令
     */
    public static final int HEART_COMMAND=0;
    /**
     * 手持机向中间件发送登录命令，以及中间件返回给手持机登录结果命令
     */
    public static final int LOGIN_COMMAND=1;

    /**
     * 中间件向手持机下发任务信息
     */
    public static final int ISSUE_TASK_COMMAND=2;

    /**
     * 手持机与脚铐断开连接时，由脚铐发送脚铐的位置信息给中间件，中间件调用平台接口上传脚铐信息，然后向手持机下发脚铐信息
     */
    public static final int UN_CONNECT_SHACKLE_COMMAND=4;

    /**
     * 手持机上传的信息包，主要就是上传自身的坐标与脚铐的坐标和感知数据，当脚铐与手持机无法通信时，其中的脚铐数据为0xff
     */
    public static final int HAND_HELD_INFO_COMMAND=5;

    /**
     * 超距报警上报，当标签出现与手持机距离过大时，手持机会发送超距报警信息，由中间件接收，然后发送给平台
     */
    public static final int OVER_DISTANCE_COMMAND=6;

    /**
     * 任务信息，手持机创建任务，通过中间件上传平台，创建成功后返回信息给手持机
     */
    public static final int CREATE_TASK_COMMAND=7;


    private static byte SEQ;
    public static byte getSEQ(){
        return SEQ++;
    }

}
