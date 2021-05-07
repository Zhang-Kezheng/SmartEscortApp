package com.example.smartescortapp;

import android.app.Application;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import com.example.smartescortapp.entity.CreateTask;
import com.example.smartescortapp.entity.CreateTaskResponse;
import com.example.smartescortapp.netty.Message;
import com.example.smartescortapp.netty.MsgCommand;
import com.google.gson.Gson;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.dialog.strategy.InputInfo;

public class CreateTaskActivity extends BaseActivity implements View.OnClickListener , DialogInterface.OnClickListener {

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private CreateTask createTask;
    private Gson gson=new Gson();

    @Override
    public void customHandleMessage(android.os.Message msg) {
        if (msg.what==MsgCommand.CREATE_TASK_COMMAND){
            Bundle bundle = msg.getData();
            Message message = (Message) bundle.getSerializable("data");
            CreateTaskResponse response = gson.fromJson(new String(message.getPAYLOAD()), CreateTaskResponse.class);
            if (response.getMsgCode()==0){
                showToast("任务创建成功");
                SmartEscortApplication.task=createTask;
                startActivity(HomeActivity.class);
            }
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_create_task);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        button1=findViewById(R.id.button);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);
        button4=findViewById(R.id.button4);
        button5=findViewById(R.id.button5);
        button6=findViewById(R.id.button6);
        button7=findViewById(R.id.button7);
    }

    @Override
    public void afterInitView() {
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        createTask=new CreateTask();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                loader.showSingleChoiceDialog(this,"押解类型",new String[]{"押解","就医","指正"},0,this,"确定","取消").show();
                break;
            case R.id.button2:
                InputInfo inputInfo = new InputInfo(InputType.TYPE_CLASS_TEXT);
                inputInfo.setHint("请输入被押解人员编号");
                loader.showInputDialog(this, R.drawable.xui_ic_default_clear_btn, "请输入被押解人员编号", "请在输入框输入被押解人员编号", inputInfo, null, "确认", (dialog, which) -> {
                    String input = ((MaterialDialog)dialog).getInputEditText().getText().toString();
                    createTask.setPersonnelID(input);
                    dialog.dismiss();
                }, "取消", (dialog, which) -> {
                    dialog.cancel();
                }).show();
                break;
            case R.id.button3:
                InputInfo personnelName = new InputInfo(InputType.TYPE_CLASS_TEXT);
                personnelName.setHint("请输入被押解人员姓名");
                loader.showInputDialog(this, R.drawable.xui_ic_default_clear_btn, "请输入被押解人员姓名", "请在输入框输入被押解人员姓名", personnelName, null, "确认", (dialog, which) -> {
                    String input = ((MaterialDialog)dialog).getInputEditText().getText().toString();
                    createTask.setPersonnelName(input);
                    dialog.dismiss();
                }, "取消", (dialog, which) -> {
                    dialog.cancel();
                }).show();
                break;
            case R.id.button4:
                break;
            case R.id.button5:
                InputInfo safeDistance = new InputInfo(InputType.TYPE_CLASS_NUMBER);
                safeDistance.setHint("安全距离设定");
                loader.showInputDialog(this, R.drawable.xui_ic_default_clear_btn, "请输入安全距离", "请在输入框输入安全距离", safeDistance, null, "确认", (dialog, which) -> {
                    String input = ((MaterialDialog)dialog).getInputEditText().getText().toString();
                    createTask.setSafeDistance(input);
                    dialog.dismiss();
                }, "取消", (dialog, which) -> {
                    dialog.cancel();
                }).show();
                break;
            case R.id.button6:
                InputInfo destination = new InputInfo(InputType.TYPE_CLASS_TEXT);
                destination.setHint("目的地");
                loader.showInputDialog(this, R.drawable.xui_ic_default_clear_btn, "请输入目的地", "请在输入框输入目的地", destination, null, "确认", (dialog, which) -> {
                    String input = ((MaterialDialog)dialog).getInputEditText().getText().toString();
                    createTask.setDestination(input);
                    dialog.dismiss();
                }, "取消", (dialog, which) -> {
                    dialog.cancel();
                }).show();
                break;
            case R.id.button7:
                createTask.setUser_account(SmartEscortApplication.user.getUser_name());
                Message message = Message.getMessage(MsgCommand.CREATE_TASK_COMMAND, gson.toJson(createTask), MsgCommand.getSEQ());
                SmartEscortApplication.client.sendMessage(message,MsgCommand.CREATE_TASK_COMMAND);
                break;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        createTask.setTaskType(which);
    }
}