package com.example.smartescortapp;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.multidex.MultiDex;
import com.example.smartescortapp.entity.CreateTask;
import com.example.smartescortapp.entity.LoginResponse;
import com.example.smartescortapp.entity.ResponseUser;
import com.example.smartescortapp.entity.User;
import com.example.smartescortapp.netty.NettyClient;
import com.example.smartescortapp.netty.TcpClient;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 章可政
 * @date 2021/4/25
 */
public class SmartEscortApplication extends Application {

    public static NettyClient client;

    public static ResponseUser user;

    public static Config config =new Config();

    public static CreateTask task;

    public static User requestUser;
    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
    }
}
