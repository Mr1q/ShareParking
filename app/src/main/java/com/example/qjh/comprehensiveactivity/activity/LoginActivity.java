package com.example.qjh.comprehensiveactivity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;
import com.example.qjh.comprehensiveactivity.utils.CreamUtils;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private TextView iv_common_login;
    private Button bt_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_login);
        CreamUtils.setWindow(this);
        initData();
    }

    private void initData() {
        iv_common_login = (TextView) findViewById(R.id.iv_common_register);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_login.setOnClickListener(this);
        iv_common_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_common_register:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                //Toast.makeText(LoginActivity.this,"正在开发中..",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_login:
                intent = new Intent(LoginActivity.this, TotalActivity.class);
                startActivity(intent);
                break;
        }
    }






}
