package com.example.smartescortapp.entity;


/**
 * @author 章可政
 * @create 2021/4/14
 */
public class CreateTaskResponse {

    /**
     * 任务创建反馈：0：创建成功；1：创建失败；
     */
    private int msgCode;

    public int getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(int msgCode) {
        this.msgCode = msgCode;
    }
}
