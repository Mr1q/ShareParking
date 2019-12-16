package com.example.qjh.comprehensiveactivity.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.beans.BaseResponse;
import com.example.qjh.comprehensiveactivity.beans.NewsRequest;
import com.example.qjh.comprehensiveactivity.beans.ParkingLot;
import com.example.qjh.comprehensiveactivity.constant.Constants;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class BookParkLotActivity extends BaseActivity implements View.OnClickListener{
    private ImageView RTU;
    private TextView tv_parklotName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_reserve);
        initData();
    }

    private void initData() {
        RTU=findViewById(R.id.RTU);
        tv_parklotName=findViewById(R.id.tv_parklotName);
        RTU.setOnClickListener(this);
        tv_parklotName.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.RTU:
                finish();
                break;
            case R.id.tv_parklotName:
                toGetData();
                new XPopup.Builder(BookParkLotActivity.this)
                        .asCenterList("请选择一项", new String[]{"条目1", "条目2", "条目3", "条目4"},
                                null, 1,
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {

                                    }
                                })
                        .show();
                break;
        }
    }

    private void toGetData() {

    }
}
