package com.example.qjh.comprehensiveactivity.activity;

import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.adapter.OrderListAdapter;
import com.example.qjh.comprehensiveactivity.adapter.SurroundAdapter;
import com.example.qjh.comprehensiveactivity.beans.BaseResponse;
import com.example.qjh.comprehensiveactivity.beans.NewsRequest;
import com.example.qjh.comprehensiveactivity.beans.Order;
import com.example.qjh.comprehensiveactivity.beans.ParkingLot;
import com.example.qjh.comprehensiveactivity.constant.Constants;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.suke.widget.SwitchButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OrderListActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private ImageView RTU;
    private Request request;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private String count;
    private final int SUCCESS = 1;
    private final int FAIL = -1;
    private final int OPENSUCCESS = 2;
    private final int OPENFAIL = -2;
    private final int CLOSESUCCESS = 3;
    private final int CLOSEFAIL = -3;
    private List<Order> orderList = new ArrayList<>();
    private OrderListAdapter orderListAdapter;
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
                    orderListAdapter = new OrderListAdapter(OrderListActivity.this, orderList);
                    orderListAdapter.setOnItemClick(new OrderListAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(Order items, String status) {
                            switch (status) {
                                case "0":
                                    toOpen(items);
                                    break;
                                case "1":
                                    toClose(items);
                                    break;
                            }
                        }
                    });

                    recyclerView.setAdapter(orderListAdapter);
                    orderListAdapter.notifyDataSetChanged();
                    break;
                case FAIL:
                    Toast.makeText(OrderListActivity.this, "重新获取！！！", Toast.LENGTH_SHORT).show();
                    //   getData();
                    break;
                case OPENSUCCESS:
                    Toast.makeText(OrderListActivity.this, "打开成功！！！", Toast.LENGTH_SHORT).show();
                    getData();
                    break;
                case OPENFAIL:
                    Toast.makeText(OrderListActivity.this, "打开失败！！！", Toast.LENGTH_SHORT).show();
                    //   getData();
                    break;
                case CLOSESUCCESS:
                    Toast.makeText(OrderListActivity.this, "结束使用成功！！！", Toast.LENGTH_SHORT).show();
                    getData();
                    break;
                case CLOSEFAIL:
                    Toast.makeText(OrderListActivity.this, "结束失败！！！", Toast.LENGTH_SHORT).show();
                    //   getData();
                    break;
            }
        }
    };

    private void toOpen(Order order) {
        RequestBody requestBody = new FormBody.Builder()
                .add("id", order.getId()).build();
        request = new Request.Builder().
                url(Constants.OpenOrderParklot).
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
                            handler.sendEmptyMessage(OPENFAIL);
                        } else {
                            handler.sendEmptyMessage(OPENSUCCESS);
                        }
                    } else {
                        handler.sendEmptyMessage(OPENFAIL);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void toClose(Order order) {
        RequestBody requestBody = new FormBody.Builder()
                .add("id", order.getId()).build();
        request = new Request.Builder().
                url(Constants.CloseOrderParklot).
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
                            handler.sendEmptyMessage(CLOSEFAIL);
                        } else {
                            handler.sendEmptyMessage(CLOSESUCCESS);
                        }
                    } else {
                        handler.sendEmptyMessage(CLOSEFAIL);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_orderlist);
        recyclerView = findViewById(R.id.recyclerView);
        RTU = findViewById(R.id.RTU);
        nullview = findViewById(R.id.nullview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
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
        newsRequest.setPageNo("1");
        newsRequest.setPageSize("100");
        request = new Request.Builder().
                url(Constants.getOrderParklot + newsRequest.toStringFindOrderList()).
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
                            Type jsontype = new TypeToken<BaseResponse<List<Order>>>() {
                            }.getType();
                            BaseResponse<List<Order>> newsListResponese
                                    = gson.fromJson(body, jsontype);
                            orderList.clear();
                            for (Order parkingLot : newsListResponese.getCarData()) {
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
