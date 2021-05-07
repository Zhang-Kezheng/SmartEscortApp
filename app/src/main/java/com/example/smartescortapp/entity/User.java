package com.example.smartescortapp.entity;

import java.io.Serializable;

/**
 * @author 章可政
 * @date 2021/4/25
 */
public class User implements Serializable {
    /**
     * 用户名；登录的账号，数据库中唯一值
     */
    private String userName;
    /**
     * 密码；
     */
    private String passWord;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
