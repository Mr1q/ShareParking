package com.example.qjh.comprehensiveactivity.controler;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 *管理所有活动
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollecter.AddActivity(this);
    }

    public  void Toast(String msg)
    {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }


    /**
     *清除所有活动
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollecter.removeActivity(this);
    }
}
