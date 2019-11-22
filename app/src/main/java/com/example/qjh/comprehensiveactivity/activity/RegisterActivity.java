package com.example.qjh.comprehensiveactivity.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;
import com.example.qjh.comprehensiveactivity.utils.CreamUtils;

public class RegisterActivity  extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_register);
        CreamUtils.setWindow(this);
    }
}
