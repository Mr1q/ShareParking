package com.example.qjh.comprehensiveactivity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout pwd_modify;
    private RelativeLayout rl_userMsg;
    private ImageView RTU;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_setting);
        initView();

    }

    private void initView() {
        pwd_modify = (RelativeLayout) findViewById(R.id.pwd_modify);
        rl_userMsg = (RelativeLayout) findViewById(R.id.rl_userMsg);
        RTU = (ImageView) findViewById(R.id.RTU);
        pwd_modify.setOnClickListener(this);
        rl_userMsg.setOnClickListener(this);
        RTU.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = null;
        switch (v.getId()) {

            case R.id.pwd_modify:
                intent=new Intent(this,ModifyPwdActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_userMsg:
                intent=new Intent(this,UserActivity.class);
                startActivity(intent);
                break;
            case R.id.RTU:
                finish();
                break;
        }
    }
}
