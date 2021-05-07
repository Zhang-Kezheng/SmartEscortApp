package com.example.smartescortapp.netty;

import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledDirectByteBuf;

import java.io.Serializable;


public class Message implements ISendable , IPulseSendable ,Serializable{
    /**
     * 包起始标志 固定值 0xff
     */
    private static final byte STX= (byte) 0xff;
    /**
     * 有效负载长度 指的是payload的字节数组的长度
     */
    private byte LEN;
    /**
     * 序列号 没发送完一个消息，内容+1，用于检测丢包的情况
     */
    private byte SEQ;
    /**
     * 消息编号 有效数据包的编号，定义了有效负载数据内放的是什么类型的消息
     */
    private byte MSG;
    /**
     * 有效负载数据
     */
    private byte[] PAYLOAD;
    /**
     * 校验和 从下标1开始到有效负载数据的校验和
     */
    private byte CKA;

    /**
     * 快速获取协议
     * @param msg 协议类型
     * @param payload 负载数据
     * @param seq 序列号
     * @return 协议
     */
    public static Message getMessage(int msg, String payload, int seq){
        byte[] bytes = payload.getBytes();
        if (bytes.length>255){
            throw new RuntimeException("消息长度超过了限制");
        }
        Message message = new Message();
        message.setPAYLOAD(bytes);
        message.setLEN((byte)bytes.length);
        message.setMSG((byte) msg);
        message.setSEQ((byte) seq);
        int cka= message.LEN+ message.SEQ+ message.MSG;
        for (byte aByte : bytes) {
            cka =cka+ aByte;
        }
        System.out.println(cka);
        message.setCKA((byte) cka);
        return message;
    }

    /**
     * 协议转成字节数组
     * @return 字节数组
     */
    public byte[] parse(){
        ByteArrayBuilder builder=new ByteArrayBuilder();
        builder.append(STX);
        builder.append(LEN);
        builder.append(SEQ);
        builder.append(MSG);
        for (byte b : PAYLOAD) {
            builder.append(b);
        }
        builder.append(CKA);
        return builder.toByteArray();
    }


    @Override
    public String toString() {
        return "Protocol{" +
                "STX="+STX+
                ",LEN=" + LEN +
                ", SEQ=" + SEQ +
                ", MSG=" + MSG +
                ", PAYLOAD=" + new String(PAYLOAD) +
                ", CKA=" + CKA +
                '}';
    }
    public static Message getInstance(byte[] data) {
        ByteBuf dataBuf = new UnpooledDirectByteBuf(ByteBufAllocator.DEFAULT,1,data.length+2);
        dataBuf.writeBytes(data);
        byte stx = dataBuf.readByte();
        if (stx!=STX)return null;
        Message message=new Message();
        message.LEN= dataBuf.readByte();
        message.SEQ= dataBuf.readByte();
        message.MSG=dataBuf.readByte();
        byte[] payload=new byte[message.LEN&0xFF];
        dataBuf.readBytes(payload);
        message.PAYLOAD=payload;
        message.CKA= (byte) (dataBuf.readByte()&0xff);
        dataBuf.release();
        return message;
    }

    public static byte getSTX() {
        return STX;
    }

    public byte getLEN() {
        return LEN;
    }

    public void setLEN(byte LEN) {
        this.LEN = LEN;
    }

    public byte getSEQ() {
        return SEQ;
    }

    public void setSEQ(byte SEQ) {
        this.SEQ = SEQ;
    }

    public byte getMSG() {
        return MSG;
    }

    public void setMSG(byte MSG) {
        this.MSG = MSG;
    }

    public byte[] getPAYLOAD() {
        return PAYLOAD;
    }

    public void setPAYLOAD(byte[] PAYLOAD) {
        this.PAYLOAD = PAYLOAD;
    }

    public byte getCKA() {
        return CKA;
    }

    public void setCKA(byte CKA) {
        this.CKA = CKA;
    }
}
