package com.example.qjh.comprehensiveactivity.controler;

import android.support.v7.app.AppCompatActivity;

/**
 *
 *
 */
public class BaseActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollecter.AddActivity(this);

    }

    /**
     *
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollecter.removeActivity(this);
    }
}
