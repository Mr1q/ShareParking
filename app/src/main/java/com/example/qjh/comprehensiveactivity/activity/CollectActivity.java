package com.example.qjh.comprehensiveactivity.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.adapter.CollectAdapter;
import com.example.qjh.comprehensiveactivity.adapter.HistoryAdapter;
import com.example.qjh.comprehensiveactivity.beans.BaseResponse;
import com.example.qjh.comprehensiveactivity.beans.NewsRequest;
import com.example.qjh.comprehensiveactivity.beans.Order;
import com.example.qjh.comprehensiveactivity.beans.ParkingLot;
import com.example.qjh.comprehensiveactivity.constant.Constants;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CollectActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private ImageView RTU;
    private Request request;
    private OkHttpClient okHttpClient=new OkHttpClient();
    private String count;
    private final int SUCCESS = 1;
    private final int FAIL = -1;
    private List<ParkingLot> orderList=new ArrayList<>();
    private CollectAdapter orderListAdapter;
    private View nullview;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    if (orderList.size() == 0) {
                        nullview.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    } else {
                        nullview.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    orderListAdapter=new CollectAdapter(CollectActivity.this,orderList);
                    recyclerView.setAdapter(orderListAdapter);
                    orderListAdapter.notifyDataSetChanged();
                    break;
                case FAIL:
                    Toast.makeText(CollectActivity.this, "重新获取！！！", Toast.LENGTH_SHORT).show();
                    //   getData();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_collecthistory);
        recyclerView=findViewById(R.id.recyclerView);
        RTU=findViewById(R.id.RTU);
        nullview=findViewById(R.id.nullview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(CollectActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        getData();

        RTU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void getData() {
        NewsRequest newsRequest = new NewsRequest();
        newsRequest.setUserId(LoginActivity.ID);
        request = new Request.Builder().
                url(Constants.CollectParklotHistory + newsRequest.toStringFindMyCar()).
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
                        Log.d("onResponse_body_class", "onResponse: " + body);
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(body);
                        String state = jsonObject.optString("state");
                        count = jsonObject.optString("count");
                        if (state.equals("0")) {
                            handler.sendEmptyMessage(FAIL);
                        } else {
                            Type jsontype = new TypeToken<BaseResponse<List<ParkingLot>>>() {
                            }.getType();
                            BaseResponse<List<ParkingLot>> newsListResponese
                                    = gson.fromJson(body, jsontype);
                            orderList.clear();
                            for (ParkingLot parkingLot : newsListResponese.getCarData()) {
                                orderList.add(parkingLot);
                            }
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
