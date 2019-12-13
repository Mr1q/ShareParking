package com.example.qjh.comprehensiveactivity.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.constant.Constants;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;
import com.google.gson.Gson;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.qqtheme.framework.picker.NumberPicker;
import cn.qqtheme.framework.picker.OptionPicker;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * author:qjh
 * time:2019.12.10
 */
public class AddParkingLotActivity extends TakePhotoActivity implements View.OnClickListener {
    private LinearLayout ly_addImage;
    private LinearLayout ly_price;
    private Button bt_getlocatioin;
    private TakePhoto takePhoto;
    private ImageView ivcar;
    private TextView tv_type;
    private EditText et_address;
    private TextView tv_lat;
    private TextView tv_log;
    private EditText et_parklotname;
    private EditText et_name;
    private Button bt_sure;
    private static Uri uri;
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build();

    private Request request;
    private  String price;
    private static final int SUCCESS = 0;
    private static final int FAIL = -1;
    private   LocationClient locationClient;
    private ImageView RTU;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    Toast.makeText(AddParkingLotActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case FAIL:
                    Toast.makeText(AddParkingLotActivity.this, "创建失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private InvokeParam invokeParam;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_add_parklot);
        uri = Uri.fromFile(new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg"));
        initData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);

    }



    private void initData() {
        ly_addImage = (LinearLayout) findViewById(R.id.ly_addImage);
        RTU = (ImageView) findViewById(R.id.RTU);
        ly_price = (LinearLayout) findViewById(R.id.ly_price);
        tv_type = (TextView) findViewById(R.id.tv_type);
        bt_getlocatioin = (Button) findViewById(R.id.bt_getlocatioin); //获取经纬度

        tv_lat = (TextView) findViewById(R.id.tv_lat);
        et_address = (EditText) findViewById(R.id.et_address);
        tv_log = (TextView) findViewById(R.id.tv_log);

        et_parklotname = (EditText) findViewById(R.id.et_parklotname);
        et_name = (EditText) findViewById(R.id.et_name);

        bt_sure = (Button) findViewById(R.id.bt_sure);

        ivcar = (ImageView) findViewById(R.id.ivcar);
        ly_addImage.setOnClickListener(this);
        ly_price.setOnClickListener(this);
        bt_sure.setOnClickListener(this);
        bt_getlocatioin.setOnClickListener(this);
        RTU.setOnClickListener(this);
    }

    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        //设置压缩规则，最大500kb
        takePhoto.onEnableCompress(new CompressConfig.Builder().setMaxSize(500 * 1024).create(), true);
        return takePhoto;
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);

        Bitmap bitmap = BitmapFactory.decodeFile(result.getImage().getOriginalPath());
        Glide.with(this).load(result.getImage().getOriginalPath()).into(ivcar);//.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ly_addImage:
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AddParkingLotActivity.this);
                View view = LayoutInflater.from(AddParkingLotActivity.this).inflate(R.layout.common_item_photo, null);

                view.findViewById(R.id.take_photo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int size = Math.min(getResources().getDisplayMetrics().widthPixels,
                                getResources().getDisplayMetrics().heightPixels);
                        CropOptions cropOptions = new CropOptions.Builder()
                                .setOutputX(size).setOutputX(size).
                                        setWithOwnCrop(true).create();
                        configCompress(takePhoto);
                        //相机获取照片并剪裁
                        takePhoto.onPickFromCaptureWithCrop(uri, cropOptions);

                        //相机获取不剪裁
                        //takePhoto.onPickFromCapture(uri);

                        bottomSheetDialog.dismiss();
                    }
                });


                view.findViewById(R.id.picture_choose).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        take(getTakePhoto());
                        bottomSheetDialog.dismiss();
                    }
                });
                view.findViewById(R.id.btn_cancle).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.show();
                break;
            case R.id.ly_price:
                NumberPicker pickers = new NumberPicker(this);
                pickers.setOffset(1);//偏移量
                pickers.setRange(1, 50);//数字范围  不能超过200kg
                pickers.setSelectedItem(5);
                pickers.setLabel("元");
                pickers.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(String option) {

                        price=option;
                        tv_type.setText(option + "每小时");
                    }
                });
                pickers.show();
                break;
            case R.id.bt_sure:
                toCommit();
                break;
            case R.id.bt_getlocatioin:
                initLocationOption();
                break;
            case R.id.RTU:
                finish();
                break;
        }
    }

    private void toCommit() {
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("id", Id)
//                .addFormDataPart("file", uri.getPath(),
//                        RequestBody.create(MediaType.parse("image/jpg"), new File(uri.getPath())))
//                .build();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("park_name", et_parklotname.getText().toString().trim())
                .addFormDataPart("park_address", et_address.getText().toString().trim())
                .addFormDataPart("park_ownerName", et_name.getText().toString().trim())
                .addFormDataPart("park_ownerId", LoginActivity.ID)
                .addFormDataPart("park_longitude", tv_log.getText().toString().trim())
                .addFormDataPart("park_latitude", tv_lat.getText().toString().trim())
                .addFormDataPart("park_latitude", tv_lat.getText().toString().trim())
                .addFormDataPart("park_distance", "123")
                .addFormDataPart("park_price", "123")
                .addFormDataPart("file","car_photourl",
                        RequestBody.create(MediaType.parse("image/jpg"), new File(uri.getPath())))
                .build();
        request = new Request.Builder().
                url(Constants.CreatePark)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("shiabi_class", "onFailure: "+e.getMessage());

                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String body = response.body().string();
                    if (response.isSuccessful()) {

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
                        Log.d("onResponse_body", "onResponse: " + body);
                        handler.sendEmptyMessage(FAIL);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        });

    }




    private void take(TakePhoto takePhoto) {
        configCompress(takePhoto);
        int size = Math.min(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        CropOptions cropOptions = new CropOptions.Builder().setOutputX(size).setOutputX(size).setWithOwnCrop(false).create();
        //Log.d("uri_user", "take: " + uri.toString());
        takePhoto.onPickFromGalleryWithCrop(uri, cropOptions);

    }

    private void configCompress(TakePhoto takePhoto) {
        int maxSize = Integer.parseInt("102400");//最大 压缩
        int width = Integer.parseInt("600");//宽
        int height = Integer.parseInt("600");//高
        CompressConfig config;
        config = new CompressConfig.Builder().setMaxSize(maxSize)
                .setMaxPixel(width >= height ? width : height)
                .enableReserveRaw(false)//拍照压缩后是否显示原图
                .enablePixelCompress(true)
                .create();
        takePhoto.onEnableCompress(config, true);//是否显示进度条

    }

    private void initLocationOption() {
        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        locationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        locationOption.setOpenGps(true);
        MyLocationListener myLocationListener = new MyLocationListener();
//注册监听函数
        locationClient.registerLocationListener(myLocationListener);

//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("bd09ll");
//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(0);
//可选，设置是否需要地址信息，默认不需要
        locationOption.setIsNeedAddress(true);
//可选，设置是否需要地址描述
        locationOption.setIsNeedLocationDescribe(true);

        locationOption.setNeedDeviceDirect(true);

//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);

        locationOption.setIsNeedLocationDescribe(true);

        locationOption.setIsNeedLocationPoiList(true);

        locationOption.SetIgnoreCacheException(false);

        locationOption.setOpenGps(true);


        locationOption.setOpenAutoNotifyMode();

        locationOption.setOpenAutoNotifyMode(3000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT);

        locationClient.setLocOption(locationOption);

        locationClient.start();

    }
    /**
     * 实现定位回调
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            double latitude = location.getLatitude();
            //获取经度信息
            double longitude = location.getLongitude();
            tv_lat.setText(String.valueOf(latitude ));
            tv_log.setText(String.valueOf(longitude ));

            Log.d("location_class", "onReceiveLocation: " + latitude);
            Log.d("location_class", "onReceiveLocation: " + longitude);
        }
    }
}
