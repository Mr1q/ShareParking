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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.adapter.CarAllAdapter;
import com.example.qjh.comprehensiveactivity.beans.BaseResponse;
import com.example.qjh.comprehensiveactivity.beans.Car;
import com.example.qjh.comprehensiveactivity.beans.NewsRequest;
import com.example.qjh.comprehensiveactivity.beans.ParkingLot;
import com.example.qjh.comprehensiveactivity.constant.Constants;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;
import com.example.qjh.comprehensiveactivity.utils.DialogUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.SimpleCallback;

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

import static com.baidu.navisdk.module.future.interfaces.FutureTripParams.LoadingState.FAIL;

/**
 * 添加车辆
 */
public class MyCarActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView rl_allcar;
    private Button bt_carfragment_add;
    private ImageView RTU;
    private ArrayList<Car> carArrayList = new ArrayList<>();
    private final int SUCCESSS = 0;
    private CarAllAdapter carAllAdapter;
    private Request request;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Gson gson;
    private JSONObject jsonObject;
    private final int SUCCESS = 1;
    private final int UNSUCCESS = -1;
    private final int DELETE_SUCCESS = 2;
    private final int DELETE_FAIL = -2;
    private final int SETDEFAULT_SUCCESS = 3;
    private final int SETDEFAULT_FAIL = -3;
    private LoadingPopupView loadingPopup;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    if(carArrayList.size()!=0)
                    {
                        nullview.setVisibility(View.INVISIBLE);
                        rl_allcar.setVisibility(View.VISIBLE);
                    }else
                    {
                        nullview.setVisibility(View.VISIBLE);
                        rl_allcar.setVisibility(View.INVISIBLE);
                    }
                    carAllAdapter = new CarAllAdapter(carArrayList, MyCarActivity.this);
                    rl_allcar.setAdapter(carAllAdapter);
                    carAllAdapter.notifyDataSetChanged();
                    carAllAdapter.setOnItemClick(new CarAllAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(Car items, int postition) {
                             new XPopup.Builder(MyCarActivity.this)
                                    .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                                    .setPopupCallback(new SimpleCallback() {
                                        //如果你自己想拦截返回按键事件，则重写这个方法，返回true即可
                                        @Override
                                        public boolean onBackPressed() {
                                            // ToastUtils.showShort("我拦截的返回按键，按返回键XPopup不会关闭了");
                                            return false;
                                        }
                                    }).asConfirm("提醒", "是否设置默认！！！！0.0",
                                    "取消", "确定",
                                    new OnConfirmListener() {
                                        @Override
                                        public void onConfirm() {
                                              loadingPopup = (LoadingPopupView)
                                                    new XPopup.Builder(MyCarActivity.this)
                                                            .dismissOnBackPressed(false)
                                                            .asLoading("正在保存中")
                                                            .show();
                                            toSetDefaultCar(items);

                                        }
                                    }, null, false)
                                    .show();

//                            final LoadingPopupView loadingPopup = (LoadingPopupView) new XPopup.Builder(MyCarActivity.this)
//                                    .dismissOnBackPressed(false)
//                                    .asLoading("正在保存中")
//                                    .show();
//                            loadingPopup.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    loadingPopup.setTitle("正在加载。。。");
//                                }
//                            }, 1000);
//                            for (int i = 0; i < carArrayList.size(); i++) {
//                                Log.d("OnItemClick", "OnItemClick: " + carArrayList.get(i).getAlways());
//                                if (i == postition) {
//                                    if (carArrayList.get(i).equals("0")) {
//                                        carArrayList.get(i).setAlways("0");
//                                    } else {
//                                        carArrayList.get(i).setAlways("1");
//                                    }
//                                } else {
//                                    if (carArrayList.get(i).equals("0")) {
//                                        carArrayList.get(i).setAlways("0");
//                                    }
//                                }
//                            }
                            carAllAdapter.notifyDataSetChanged();
//                            loadingPopup.delayDismissWith(3000, new Runnable() {
//                                @Override
//                                public void run() {
//                                    //Toast("保存成功！！！");
//                                }
//                            });
                        }

                        @Override
                        public void OnLongItemClick(Car items) {
                            new XPopup.Builder(MyCarActivity.this)
                                    .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                                    .setPopupCallback(new SimpleCallback() {
                                        //如果你自己想拦截返回按键事件，则重写这个方法，返回true即可
                                        @Override
                                        public boolean onBackPressed() {
                                            // ToastUtils.showShort("我拦截的返回按键，按返回键XPopup不会关闭了");
                                            return false;
                                        }
                                    }).asConfirm("提醒", "是否删除！！！！0.0",
                                    "取消", "确定",
                                    new OnConfirmListener() {
                                        @Override
                                        public void onConfirm() {
                                            final LoadingPopupView loadingPopup = (LoadingPopupView)
                                                    new XPopup.Builder(MyCarActivity.this)
                                                            .dismissOnBackPressed(false)
                                                            .asLoading("正在删除中")
                                                            .show();
                                            loadingPopup.delayDismissWith(800, new Runnable() {
                                                @Override
                                                public void run() {
                                                    toDelete(items);
                                                }
                                            });
                                        }
                                    }, null, false)
                                    .show();

                        }
                    });
                    // Toast.makeText(MyCarActivity.this, "创建成功！！！", Toast.LENGTH_SHORT).show();
                    break;
                case UNSUCCESS:
                    Toast.makeText(MyCarActivity.this, "创建失败！！！", Toast.LENGTH_SHORT).show();
                    break;
                case DELETE_FAIL:
                    Toast.makeText(MyCarActivity.this, "删除失败！！！", Toast.LENGTH_SHORT).show();
                    break;
                case DELETE_SUCCESS:
                    getData();

                    Toast.makeText(MyCarActivity.this, "删除成功！！！", Toast.LENGTH_SHORT).show();
                    break;
                case SETDEFAULT_FAIL:
                    getData();
                    Toast.makeText(MyCarActivity.this, "设置默认失败！！！", Toast.LENGTH_SHORT).show();
                    break;
                case SETDEFAULT_SUCCESS:
                    getData();
                    loadingPopup.dismiss();
                    Toast.makeText(MyCarActivity.this, "设置默认成功！！！", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };
    private View nullview;


    private void toDelete(Car car) {

        RequestBody requestBody = new FormBody.Builder()
                .add("carId", car.getCarId())
                .add("carNumber",car.getCarNumber())
                .add("userId",LoginActivity.ID)
                .build();

        request = new Request.Builder().
                url(Constants.DeleteMyCar).
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
                            handler.sendEmptyMessage(DELETE_FAIL);
                        } else {
                            if(car.getAlways().equals("1"))
                            {
                                LoginActivity.defaultCar="";
                            }
                            handler.sendEmptyMessage(DELETE_SUCCESS);
                        }
                    } else {
                        handler.sendEmptyMessage(DELETE_FAIL);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void toSetDefaultCar(Car car) {

        RequestBody requestBody = new FormBody.Builder()
                .add("userId", LoginActivity.ID)
                .add("carNumber", car.getCarNumber())
                .add("carId", car.getCarId())
                .build();
        request = new Request.Builder().
                url(Constants.SetDefaultCar).
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
                    Log.d("onResponse_class", "onResponse: " + body);
                    if (response.isSuccessful()) {
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(body);
                        String state = jsonObject.optString("state");
                        if (state.equals("0")) {
                            handler.sendEmptyMessage(SETDEFAULT_FAIL);
                        } else {
                            LoginActivity.defaultCar=car.getCarNumber();
                            handler.sendEmptyMessage(SETDEFAULT_SUCCESS);
                        }
                    } else {
                        handler.sendEmptyMessage(SETDEFAULT_FAIL);
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
        setContentView(R.layout.common_activity_mycars);
        initData();
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        rl_allcar.setLayoutManager(linearLayout);
        //获取数据
        getData();
    }

    private void getData() {
        NewsRequest newsRequest = new NewsRequest(); //get请求参数
        newsRequest.setUserId(LoginActivity.ID); //当前用户的ID
        request = new Request.Builder().
                url(Constants.FindMyCar + newsRequest.toStringFindMyCar()).
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
                        gson = new Gson();
                        jsonObject = new JSONObject(body);
                        String state = jsonObject.optString("state");
                        if (state.equals("0")) {
                            handler.sendEmptyMessage(FAIL);
                        } else {
                            Type jsontype = new TypeToken<BaseResponse<List<Car>>>() {
                            }.getType();
                            BaseResponse<List<Car>> car = gson.fromJson(body, jsontype);
                            carArrayList.clear();
                            for (Car parkingLot : car.getCarData()) {
                                carArrayList.add(parkingLot);
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

    private void initData() {
        rl_allcar = (RecyclerView) findViewById(R.id.rl_allcar);
        bt_carfragment_add = (Button) findViewById(R.id.bt_carfragment_add);
        nullview =  findViewById(R.id.nullview);
        RTU = (ImageView) findViewById(R.id.RTU);
        RTU.setOnClickListener(this);
        carArrayList = new ArrayList<Car>();
        bt_carfragment_add.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_carfragment_add:
                Intent intent = new Intent(MyCarActivity.this, AddCarActivity.class);
                startActivityForResult(intent, SUCCESSS);
                break;
            case R.id.RTU:
                finish();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SUCCESSS:
                if (resultCode == RESULT_OK) {
                    getData();
                    // Car car = new Car(data.getStringExtra(AddCarActivity.CARNUMBER), "", false);
                    // carArrayList.add(car);
//                    carAllAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
