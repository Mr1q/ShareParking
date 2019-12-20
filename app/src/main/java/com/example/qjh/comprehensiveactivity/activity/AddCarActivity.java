package com.example.qjh.comprehensiveactivity.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.adapter.MyParkingLotAdapter;
import com.example.qjh.comprehensiveactivity.beans.ParkingLot;
import com.example.qjh.comprehensiveactivity.constant.Constants;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;
import com.google.gson.Gson;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.parkingwang.keyboard.OnInputChangedListener;
import com.parkingwang.keyboard.PopupKeyboard;
import com.parkingwang.keyboard.view.InputView;
import com.suke.widget.SwitchButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddCarActivity extends TakePhotoActivity implements View.OnClickListener {

    private InputView input_view;
    private ImageView RTU;
    private TextView tv_confirm;
    private EditText et_carAddress;
    private ImageView iv_addcar;
    private String CarNumber; //车牌号
    public static final String CARNUMBER = "CARNUMBER";
    private Request request;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private final int SUCCESS = 1;
    private final int UNSUCCESS = -1;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    Toast.makeText(AddCarActivity.this, "创建成功！！！", Toast.LENGTH_SHORT).show();
                    // toSetDefaultCar();
                    Intent intent = new Intent(AddCarActivity.this, MyCarActivity.class);
                    // intent.putExtra(CARNUMBER, CarNumber);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case UNSUCCESS:
                    Toast.makeText(AddCarActivity.this, "创建失败！！！", Toast.LENGTH_SHORT).show();
                    break;


            }
        }
    };
    private Uri uri;
    private TakePhoto takePhoto;
    private String Path;
    private Gson gson = null;
    private JSONObject jsonObject=null;

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
        Bitmap bitmap = BitmapFactory.decodeFile(result.getImage().getOriginalPath());
        Path = result.getImage().getCompressPath();
        Glide.with(this).load(bitmap).into(iv_addcar);//.setImageBitmap(bitmap);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commmon_dialog_addcar);
        RTU = (ImageView) findViewById(R.id.RTU);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        input_view = (InputView) findViewById(R.id.input_view);
        et_carAddress = (EditText) findViewById(R.id.et_carAddress);
        iv_addcar = (ImageView) findViewById(R.id.iv_addcar);
        RTU.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
        iv_addcar.setOnClickListener(this);
        final PopupKeyboard mPopupKeyboard = new PopupKeyboard(this);
        // 弹出键盘内部包含一个KeyboardView，在此绑定输入两者关联。
        mPopupKeyboard.attach(input_view, this);

        // KeyboardInputController提供一个默认实现的新能源车牌锁定按钮
        mPopupKeyboard.getController()
                .setDebugEnabled(true)
                .addOnInputChangedListener(new OnInputChangedListener() {
                    @Override
                    public void onChanged(String number, boolean isCompleted) {
                        if (isCompleted) {
                            mPopupKeyboard.dismiss(getWindow());
                        }
                    }

                    @Override
                    public void onCompleted(String number, boolean isAutoCompleted) {
                        mPopupKeyboard.dismiss(getWindow());
                        CarNumber = number;
                    }
                });

        uri = Uri.fromFile(new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm:
                toCreateCar();
                break;

            case R.id.RTU:
                finish();
                break;
            case R.id.iv_addcar:
                toTakePhoto();


                break;
        }
    }

    private void toTakePhoto() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AddCarActivity.this);
        View view = LayoutInflater.from(AddCarActivity.this).inflate(R.layout.common_item_photo, null);
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

    }

    private void toCreateCar() {
        if(Path==null)
        {
            Toast.makeText(AddCarActivity.this,"请添加图片",Toast.LENGTH_SHORT).show();
            return;
        }
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("car_address", et_carAddress.getText().toString())
                .addFormDataPart("car_userId", LoginActivity.ID)
                .addFormDataPart("car_number", CarNumber)
                .addFormDataPart("file", "car_photourl",
                        RequestBody.create(MediaType.parse("image/jpg"), new File(Path)))
                .build();
        request = new Request.Builder().
                url(Constants.CreateCar).
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
                    Log.d("onResponse_body", "onResponse: " + body);
                    if (response.isSuccessful()) {
                        Log.d("onResponse_body", "onResponse: " + body);
                        gson = new Gson();
                         jsonObject = new JSONObject(body);
                        String state = jsonObject.optString("state");
                        if (state.equals("0")) {
                            handler.sendEmptyMessage(UNSUCCESS);
                        } else {
                            handler.sendEmptyMessage(SUCCESS);
                        }
                    } else {
                        handler.sendEmptyMessage(UNSUCCESS);
                    }
                } catch (JSONException e) {
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
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        //设置压缩规则，最大500kb
        takePhoto.onEnableCompress(new CompressConfig.Builder().setMaxSize(500 * 1024).create(), true);
        return takePhoto;
    }

}
