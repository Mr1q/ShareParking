package com.example.qjh.comprehensiveactivity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.adapter.MyParkingLotAdapter;
import com.example.qjh.comprehensiveactivity.beans.BaseResponse;
import com.example.qjh.comprehensiveactivity.beans.NewsRequest;
import com.example.qjh.comprehensiveactivity.beans.ParkingLot;
import com.example.qjh.comprehensiveactivity.constant.Constants;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;
import com.example.qjh.comprehensiveactivity.utils.GetDate;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.suke.widget.SwitchButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BookParkLotActivity extends BaseActivity implements View.OnClickListener {
    private ImageView RTU;
    private TextView tv_parklotName;
    private TextView tv_time;
    private TextView tv_price;
    private Button tv_CarNumber;
    private Button bt_book;
    private final int SUCCESS = 1;
    private final int FAIL = -1;
    private final int GET_SUCCESS = 2;
    private final int GET_FAIL = -2;
    private ParkingLot parkingLot = new ParkingLot();
    private List<ParkingLot> parkingLots = new ArrayList<>();
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    Toast.makeText(BookParkLotActivity.this, "预订成功！！！", Toast.LENGTH_SHORT).show();
                    new XPopup.Builder(BookParkLotActivity.this)
//                         .dismissOnTouchOutside(false)
//                         .autoDismiss(false)
                            .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                            .setPopupCallback(new SimpleCallback()).asConfirm("提醒", "是否进入导航",
                            "取消", "确定",
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                        Intent intent=new Intent(BookParkLotActivity.this,MapActivity.class);
                                        intent.putExtra("asd","asd");
                                        startActivity(intent);
                                        finish();
                                }
                            }, null, false)
                            .show();




                    break;
                case FAIL:
                    Toast.makeText(BookParkLotActivity.this, "预订失败！！！", Toast.LENGTH_SHORT).show();
                    break;
                case GET_SUCCESS:
                    tv_parklotName.setText(parkingLots.get(0).getPark_name());
                    tv_price.setText(parkingLots.get(0).getPark_price() + "/每小时");
                    break;
                case GET_FAIL:
                    Toast("重新获取");
                    break;
            }
        }
    };
    private Request request;
    private OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_reserve);
        initData();
    }

    private void initData() {
        RTU = findViewById(R.id.RTU);
        tv_parklotName = findViewById(R.id.tv_parklotName);
        tv_price = findViewById(R.id.tv_price);
        tv_time = findViewById(R.id.tv_time);
        tv_CarNumber = findViewById(R.id.tv_CarNumber);
        bt_book = findViewById(R.id.bt_book);
        RTU.setOnClickListener(this);
        tv_parklotName.setOnClickListener(this);
        tv_CarNumber.setOnClickListener(this);
        bt_book.setOnClickListener(this);
        tv_time.setText(GetDate.StringTime());
//        tv_price.setText();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RTU:
                finish();
                break;
            case R.id.tv_parklotName:
                getShortDistance(); //获取最近的停车位
                break;
            case R.id.tv_CarNumber:
                if (LoginActivity.defaultCar == null) {
                    Toast("没有默认车辆");
                }
                new XPopup.Builder(BookParkLotActivity.this)
                        .asCenterList("请选择", new String[]{LoginActivity.defaultCar},
                                null, 1,
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        tv_CarNumber.setText(text);
                                    }
                                })
                        .show();
                break;
            case R.id.bt_book:
                toOrder();
                break;
        }
    }

    private void getShortDistance() {
        // TODO: 2019/12/20 ;获取随机距离


        if (MyAddressActivity.lat == null) {
            Toast("请获取获取当前位置");
            return;
        }
        NewsRequest newsRequest = new NewsRequest();
        newsRequest.setLatitude(MyAddressActivity.lat);
        newsRequest.setLongitude(MyAddressActivity.log);
        newsRequest.setUserId(LoginActivity.ID);
        Log.d("getShortDistance", "getShortDistance: " + newsRequest.toStringFindClosePark());
        request = new Request.Builder().
                url(Constants.QuickParklot + newsRequest.toStringFindClosePark()).
                build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d("onResponse_body_class", "onResponse: " + e.getMessage());
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
                        if (state.equals("0")) {
                            handler.sendEmptyMessage(GET_FAIL);
                        } else {
                            JSONObject jsonArray = jsonObject.optJSONObject("data");
                            parkingLot.setPark_id(jsonArray.optString("park_id"));
                            parkingLot.setPark_name(jsonArray.optString("park_name"));
                            parkingLot.setPark_address(jsonArray.optString("park_address"));
                            parkingLot.setPark_price(jsonArray.optString("park_price"));
                            parkingLot.setParklotImage(jsonArray.optString("park_photoURL"));
                            parkingLots.add(parkingLot);
//                            BaseResponse<ParkingLot> newsListResponese
//                                    = gson.fromJson(body, jsontype);
                            //  ParkingLot parkingLot = newsListResponese.getCarData();
                            // parkingLots.add(parkingLot);

                            handler.sendEmptyMessage(GET_SUCCESS);
                        }
                    } else {

                        handler.sendEmptyMessage(GET_FAIL);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    /**
     * 预订车位
     */
    private void toOrder() {

        RequestBody requestBody = new FormBody.Builder()
                .add("price", String.valueOf(parkingLots.get(0).getPark_price()))
                .add("userId", LoginActivity.ID)
                .add("carNumber", tv_CarNumber.getText().toString())
                .add("carId", parkingLots.get(0).getPark_id())
                .add("parkId",parkingLots.get(0).getPark_id())
                .build();
        request = new Request.Builder().
                url(Constants.OrderParklot).
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
                    String body = response.body().string();
                    Log.d("onResponse_body_order", "onResponse: " + body);
                    if (response.isSuccessful()) {
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
