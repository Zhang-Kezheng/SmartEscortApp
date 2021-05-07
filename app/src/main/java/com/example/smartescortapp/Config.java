package com.example.smartescortapp;

/**
 * @author 章可政
 * @date 2021/4/26
 */
public class Config {
    private String ip="192.168.1.200";
    private int port=6666;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
