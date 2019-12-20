package com.example.qjh.comprehensiveactivity.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;

public class RecommendActivity extends BaseActivity {
    private ImageView RTU;
    private Button bt_adviceCommit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_recommend);
        RTU=findViewById(R.id.RTU);
        bt_adviceCommit=(Button) findViewById(R.id.bt_adviceCommit);
        RTU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_adviceCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCommit();
            }
        });
    }

    private void toCommit() {
    }
}
