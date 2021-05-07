package com.example.smartescortapp.entity;


import com.example.smartescortapp.R;

/**
 * @author 章可政
 * @create 2021/4/14
 * 登录请求平台返回的响应实体类
 */

public class LoginResponse extends PlatformResponse{
    private ResponseUser data;

    public ResponseUser getData() {
        return data;
    }

    public void setData(ResponseUser data) {
        this.data = data;
    }
}
