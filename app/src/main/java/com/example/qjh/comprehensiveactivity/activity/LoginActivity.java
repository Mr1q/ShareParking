package com.example.qjh.comprehensiveactivity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;
import com.example.qjh.comprehensiveactivity.utils.CreamUtils;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private TextView iv_common_login;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_login);
        CreamUtils.setWindow(this);
        initData();
    }

    private void initData() {
        iv_common_login=(TextView)findViewById(R.id.iv_common_login);
        iv_common_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.iv_common_login:
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                //Toast.makeText(LoginActivity.this,"正在开发中..",Toast.LENGTH_SHORT).show();
            break;
        }
    }
}
