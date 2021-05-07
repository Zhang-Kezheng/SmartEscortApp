package com.example.smartescortapp;


import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;

import android.view.Window;
import android.view.WindowManager;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartescortapp.netty.MsgCommand;
import com.example.smartescortapp.netty.NettyClient;
import com.example.smartescortapp.netty.TcpClient;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.dialog.strategy.impl.MaterialDialogStrategy;

import java.lang.reflect.Field;

import static com.example.smartescortapp.SmartEscortApplication.client;


/**
 * @author 章可政
 * @date 2021/1/12 23:06
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 是否全屏
     */
    protected boolean isFullScreen = false;

    protected DialogLoader loader;
    protected final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            baseHandleMessage(msg);
            customHandleMessage(msg);
        }
    };
    protected MaterialDialog dialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setFullScreen(isFullScreen);
        setContentLayout();
        beforeInitView();
        initView();
        afterInitView();
        loader = DialogLoader.getInstance();
        loader.setIDialogStrategy(new MaterialDialogStrategy());
        if (client==null) {
            client= NettyClient.getInstance();
            SmartEscortApplication.client.setHandler(this.handler);
            new Thread(() -> {
                client.doConnect();
                handler.sendMessage(handler.obtainMessage(MsgCommand.CONNECT_FAIL));
            }).start();
        }
        SmartEscortApplication.client.setHandler(this.handler);
        dialog = new MaterialDialog.Builder(this)
                .iconRes(R.drawable.xui_ic_default_tip_btn)
                .limitIconToDefaultSize()
                .title("系统提示")
                .progress(true, 0)
                .cancelable(false)
                .progressIndeterminateStyle(false)
                .content("正在连接服务器，请稍后")
                .build();
    }

    private void baseHandleMessage(Message msg) {
        switch (msg.what) {
            case MsgCommand.CONNECT:
                dialog.show();
                break;
            case MsgCommand.RECONNECT:
                dialog.setContent("服务器连接失败，正在重新连接服务器，请稍后");
                break;
            case MsgCommand.CONNECT_FAIL:
                dialog.setContent("服务器连接失败，请检查网络");
                break;
            case MsgCommand.CONNECT_SUCCESS:
                dialog.setContent("服务器连接成功");
                try {
                    Thread.sleep(1000);
                    dialog.dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 是否全屏
     */
    public void setFullScreen(boolean fullScreen) {
        if (fullScreen) {
            getSupportActionBar().hide();
        }
    }

    public abstract void customHandleMessage(Message msg);

    /**
     * 设置布局文件
     */
    public abstract void setContentLayout();

    /**
     * 在实例化控件之前的逻辑操作
     */
    public abstract void beforeInitView();

    /**
     * 实例化控件
     */
    public abstract void initView();

    /**
     * 实例化控件之后的操作
     */
    public abstract void afterInitView();


    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获得屏幕的宽度
     */
    public int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }

    /**
     * 获得屏幕的高度
     */
    public int getScreenHeigh() {
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenHeigh = dm.heightPixels;
        return screenHeigh;
    }

    // ----------Activity跳转----------//
    protected void startActivity(Class<?> targetClass) {
        Intent intent = new Intent(this, targetClass);
        startActivity(intent);
    }

    // 带参数跳转
    protected void startActivity(Class<?> targetClass, String key, String value) {
        Intent intent = new Intent(this, targetClass);
        intent.putExtra(key, value);
        startActivity(intent);
    }

    // 带请求码跳转
    protected void startActivity(Class<?> targetClass, int requestCode) {
        Intent intent = new Intent(this, targetClass);
        startActivityForResult(intent, requestCode);
    }

    // 带参数和请求码跳转
    protected void startActivity(Class<?> targetClass, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, targetClass);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }


}