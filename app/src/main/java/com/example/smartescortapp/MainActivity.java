package com.example.smartescortapp;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.smartescortapp.entity.LoginResponse;
import com.example.smartescortapp.entity.PlatformResponse;
import com.example.smartescortapp.entity.User;
import com.example.smartescortapp.netty.Message;
import com.example.smartescortapp.netty.MsgCommand;
import com.example.smartescortapp.netty.NettyClient;
import com.example.smartescortapp.netty.TcpClient;
import com.google.gson.Gson;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import static com.example.smartescortapp.SmartEscortApplication.client;
import static com.example.smartescortapp.SmartEscortApplication.config;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static List<String> mListPermissions=new ArrayList<>();
    private static final String PERMISSIONS_STORAGE = Manifest.permission.INTERNET;
    private static final String PERMISSIONS_PHONE = Manifest.permission.CHANGE_WIFI_STATE;
    private static final String PERMISSIONS_ACCOUNTS = Manifest.permission.ACCESS_WIFI_STATE;
    private static final String PERMISSIONS_LOCATION = Manifest.permission.CHANGE_NETWORK_STATE;
    private static final String PERMISSIONS_AUDIO = Manifest.permission.ACCESS_NETWORK_STATE;
    private final List<String> mPermissionList = new ArrayList<>();
    private AlertDialog dialog;
    private MaterialDialog.Builder dialog1;
    private  EditText ip;
    private  EditText port;
    private  Button confirm;
    private EditText username;
    private EditText password;
    private Gson gson=new Gson();
    private Button login;


    @Override
    public void customHandleMessage(android.os.Message msg) {
        switch (msg.what){
            case MsgCommand.LOGIN_COMMAND:
                Bundle bundle = msg.getData();
                Message message = (Message) bundle.getSerializable("data");
                LoginResponse loginResponse = gson.fromJson(new String(message.getPAYLOAD()), LoginResponse.class);
                int code = loginResponse.getCode();
                showToast(loginResponse.getMsg());
                if (code==0){
                    SmartEscortApplication.user=loginResponse.getData();
                    startActivity(HomeActivity.class);
                    this.finish();
                }else {
                    SmartEscortApplication.requestUser=null;
                }
                break;
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        username=findViewById(R.id.username);;
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        Button login_set=findViewById(R.id.login_set);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view = View.inflate(MainActivity.this, R.layout.login_set_view, null);
        builder.setView(view);
        ip =  view.findViewById(R.id.ip);
        port =  view.findViewById(R.id.port);
        ip.setText(config.getIp());
        port.setText(config.getPort()+"");
        confirm =  view.findViewById(R.id.confirm);
        dialog=builder.create();
        login.setOnClickListener(this);
        login_set.setOnClickListener(this);
        confirm.setOnClickListener(this);
        showCircleLoadingProgressDialog();
    }

    @Override
    public void afterInitView() {
        username.setText("admin");
        password.setText("rw123abc$");
        addAllPermissions();
        checkPermission();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                String s = username.getText().toString();
                String s1 = password.getText().toString();
                if ("".equals(s)&&"".equals(s1)){
                    showToast("用户名和密码不能为空");
                    return;
                }
                User user=new User();
                user.setUserName(s);
                user.setPassWord(s1);
                SmartEscortApplication.requestUser=user;
                Message message = Message.getMessage(MsgCommand.LOGIN_COMMAND, gson.toJson(user), MsgCommand.getSEQ());
                client.sendMessage(message,MsgCommand.LOGIN_COMMAND);
                dialog1.show();
                break;
            case R.id.login_set:
                dialog.show();
                break;
            case R.id.confirm:
                String s2 = ip.getText().toString();
                String s3 = port.getText().toString();
                if (s2.equals("")){
                    showToast("ip不能为空");
                    break;
                }
                if (s3.equals("")){
                    showToast("port不能为空");
                    break;
                }
                int i = Integer.parseInt(s3);
                if (i<0){
                    showToast("port不能小于0");
                }
                int max=256*64;
                if (i>max){
                    showToast("port不能大于"+max);
                    break;
                }
                config.setIp(s2);
                config.setPort(i);
                dialog.dismiss();
                break;
        }
    }
    /**
     * 添加权限
     * author LH
     * data 2016/7/27 11:27
     */
    private void addAllPermissions() {
        mListPermissions.add(PERMISSIONS_STORAGE);
        mListPermissions.add(PERMISSIONS_PHONE);
        mListPermissions.add(PERMISSIONS_ACCOUNTS);
        mListPermissions.add(PERMISSIONS_LOCATION);
        mListPermissions.add(PERMISSIONS_AUDIO);
    }

    /**
     * 检查权限
     */
    private void checkPermission() {
        //判断哪些权限未授予
        for (int i = 0; i < mListPermissions.size(); i++) {
            if (ContextCompat.checkSelfPermission(this, mListPermissions.get(i)) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(mListPermissions.get(i));
            }
        }
        if (!mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }


    /**
     * 显示正在登录弹窗
     */
    private void showCircleLoadingProgressDialog(){
        dialog1=new MaterialDialog.Builder(this)
                .iconRes(R.drawable.xui_ic_default_tip_btn)
                .limitIconToDefaultSize()
                .title("系统提示")
                .content("正在登录，请稍后")
                .progress(true, 0)
                .cancelable(false)
                .progressIndeterminateStyle(false);
    }
}