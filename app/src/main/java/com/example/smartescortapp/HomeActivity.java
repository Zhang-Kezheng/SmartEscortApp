package com.example.smartescortapp;

import android.os.Message;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class HomeActivity extends BaseActivity  implements View.OnClickListener {

    private Button task_start;


    @Override
    public void customHandleMessage(Message msg) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_home);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        task_start=findViewById(R.id.task_start);
    }

    @Override
    public void afterInitView() {
        task_start.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.task_start:
                startActivity(CreateTaskActivity.class);
                this.finish();
                break;
        }
    }
}