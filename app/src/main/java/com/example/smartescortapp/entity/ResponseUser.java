package com.example.smartescortapp.entity;

/**
 * @author 章可政
 * @date 2021/4/27
 */
public class ResponseUser {
    private String access_token;
    private String user_id;
    private String user_name;
    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
