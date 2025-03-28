package com.example.qjh.comprehensiveactivity.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.adapter.HistoryAdapter;
import com.example.qjh.comprehensiveactivity.beans.Order;
import com.example.qjh.comprehensiveactivity.constant.Constants;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecommendActivity extends BaseActivity {
    private ImageView RTU;
    private Button bt_adviceCommit;
    private EditText ed_MyAdviceContent;
    private final int SUCCESS = 1;
    private final int FAIL = -1;
    private Request request;
    private OkHttpClient okHttpClient=new OkHttpClient();
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    loadingPopup.dismiss();
                    Toast.makeText(RecommendActivity.this, "提交成功！！！", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case FAIL:
                    loadingPopup.dismiss();
                    Toast.makeText(RecommendActivity.this, "提交失败！！！", Toast.LENGTH_SHORT).show();
                    //   getData();
                    break;
            }
        }
    };
    private LoadingPopupView loadingPopup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_recommend);
        RTU=findViewById(R.id.RTU);
        bt_adviceCommit=(Button) findViewById(R.id.bt_adviceCommit);
        ed_MyAdviceContent=(EditText) findViewById(R.id.ed_MyAdviceContent);
        RTU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_adviceCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPopup = (LoadingPopupView) new XPopup.Builder(RecommendActivity.this)
                        .dismissOnBackPressed(false)
                        .asLoading("正在加载中")
                        .show();
                toCommit();
            }
        });

    }

    private void toCommit() {
        String address=ed_MyAdviceContent.getText().toString();
        RequestBody requestBody = new FormBody.Builder()
                .add("userId", LoginActivity.ID)
                .add("suggestion", address)
                .build();
        request = new Request.Builder().
                url(Constants.Advice).
                post(requestBody).
                build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        Log.d("onResponse_body", "onResponse: " + body);
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(body);
                        String state = jsonObject.optString("state");
                        if (state.equals("0")) {
                            handler.sendEmptyMessage(FAIL);
                        } else {
                            handler.sendEmptyMessage(SUCCESS);
                        }
                    } else {
                        handler.sendEmptyMessage(FAIL);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
