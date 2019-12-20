package com.example.qjh.comprehensiveactivity.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.beans.User;
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

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserActivity extends TakePhotoActivity implements View.OnClickListener {
    private TextView Username;
    private TextView name;
    private TextView phone;
    private TextView address;
    private RelativeLayout rl_username;
    private RelativeLayout rl_name;
    private RelativeLayout rl_photo;
    private RelativeLayout rl_phone;
    private RelativeLayout rl_address;
    private Button bt_userfragment_save;
    private CircleImageView head_imgae;

    private Request request;
    private OkHttpClient okHttpClient = new OkHttpClient();


    public SharedPreferences.Editor editor;

    public static final int MODIFY_ADDRESS = 1;
    public static final int MODIFY_USERNAME = 2;
    public static final int MODIFY_PHONE = 3;
    public static final int MODIFY_NAME = 4;
    public static final String MODIFY_TYPE = "MODIFY_TYPE";
    private static final int SUCCESS = 0;
    private static final int FAIL = -1;
    private static final int PHOTO_SUCCESS = 2;
    private static final int PHOTO_FAIL = -2;
    private String Path;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    Toast.makeText(UserActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra(LoginActivity.EXTRA_KEY_User_PHONE, phone.getText().toString());
                    intent.putExtra(LoginActivity.EXTRA_KEY_User_ADDRESS, address.getText().toString());
                    intent.putExtra(LoginActivity.EXTRA_KEY_User_NAME, name.getText().toString());
                    intent.putExtra(LoginActivity.EXTRA_KEY_User_USERNAME, Username.getText().toString());
                    intent.putExtra(LoginActivity.EXTRA_KEY_User_PHOTO, Path);
                    setResult(RESULT_OK, intent);
                    editor = LoginActivity.sharedPreferences.edit();
                    editor.putString(LoginActivity.EXTRA_KEY_User_PHONE, phone.getText().toString());
                    editor.putString(LoginActivity.EXTRA_KEY_User_ADDRESS, address.getText().toString());
                    editor.putString(LoginActivity.EXTRA_KEY_User_NAME, name.getText().toString());
                    editor.putString(LoginActivity.EXTRA_KEY_User_USERNAME, Username.getText().toString());
                    editor.putString(LoginActivity.EXTRA_KEY_User_PHOTO, Path);
                    editor.commit();
                    finish();
                    break;
                case FAIL:
                    Toast.makeText(UserActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    break;
                case PHOTO_SUCCESS:
                    Glide.with(UserActivity.this).load(Path).into(head_imgae);//.setImageBitmap(bitmap);
                    Toast.makeText(UserActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    break;
                case PHOTO_FAIL:
                    Toast.makeText(UserActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private Uri uri;
    private TakePhoto takePhoto;
    private TakePhotoActivity take;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commmn_activity_usermsg);
        uri = Uri.fromFile(new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg"));
        initData();
    }

    @Override
    public void takeSuccess(TResult result) {
        Bitmap bitmap = BitmapFactory.decodeFile(result.getImage().getOriginalPath());
        Path = result.getImage().getCompressPath();

        toChangePhoto();
    }

    private void toChangePhoto() {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userId", LoginActivity.ID)
                .addFormDataPart("file", "uesr_photourl",
                        RequestBody.create(MediaType.parse("image/jpg"), new File(Path)))
                .build();
        request = new Request.Builder().
                url(Constants.ChangeUserPhoto)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("shiabi_class", "onFailure: " + e.getMessage());
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
                            handler.sendEmptyMessage(PHOTO_FAIL);
                        } else {
                            handler.sendEmptyMessage(PHOTO_SUCCESS);
                        }
                    } else {
                        Log.d("onResponse_body", "onResponse: " + body);
                        handler.sendEmptyMessage(PHOTO_FAIL);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        });
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        //设置压缩规则，最大500kb
        takePhoto.onEnableCompress(new CompressConfig.Builder().setMaxSize(500 * 1024).create(), true);
        return takePhoto;
    }

    private void initData() {
        Username = (TextView) findViewById(R.id.Username);
        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        address = (TextView) findViewById(R.id.address);
        rl_username = (RelativeLayout) findViewById(R.id.rl_username);
        rl_name = (RelativeLayout) findViewById(R.id.rl_name);
        rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);
        rl_address = (RelativeLayout) findViewById(R.id.rl_address);
        rl_photo = (RelativeLayout) findViewById(R.id.rl_photo);
        head_imgae = (CircleImageView) findViewById(R.id.head_imgae);
        bt_userfragment_save = (Button) findViewById(R.id.bt_userfragment_save);
        //rl_username.setOnClickListener(this);  用户名不能改
        rl_name.setOnClickListener(this);
        rl_phone.setOnClickListener(this);
        rl_address.setOnClickListener(this);
        bt_userfragment_save.setOnClickListener(this);
        rl_photo.setOnClickListener(this);


        Intent intent = getIntent();
        if (intent != null) {
            name.setText(intent.getStringExtra(LoginActivity.EXTRA_KEY_User_NAME));
            Username.setText(intent.getStringExtra(LoginActivity.EXTRA_KEY_User_USERNAME));
            phone.setText(intent.getStringExtra(LoginActivity.EXTRA_KEY_User_PHONE));
            address.setText(intent.getStringExtra(LoginActivity.EXTRA_KEY_User_ADDRESS));
            Path=intent.getStringExtra(LoginActivity.EXTRA_KEY_User_PHOTO);
            Glide.with(this).load(Path).into(head_imgae);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.rl_address:
                intent = new Intent(UserActivity.this, ModifyActivityAll.class);
                intent.putExtra(LoginActivity.EXTRA_KEY_User_ADDRESS, address.getText().toString());
                intent.putExtra(MODIFY_TYPE, "1");
                startActivityForResult(intent, MODIFY_ADDRESS);
                break;
            case R.id.rl_username:
                intent = new Intent(UserActivity.this, ModifyActivityAll.class);
                intent.putExtra(LoginActivity.EXTRA_KEY_User_USERNAME, Username.getText().toString());
                intent.putExtra(MODIFY_TYPE, "2");
                startActivityForResult(intent, MODIFY_USERNAME);
                break;


            case R.id.rl_name:
                intent = new Intent(UserActivity.this, ModifyActivityAll.class);
                intent.putExtra(LoginActivity.EXTRA_KEY_User_NAME, name.getText().toString());
                intent.putExtra(MODIFY_TYPE, "4");
                startActivityForResult(intent, MODIFY_NAME);
                break;


            case R.id.rl_phone:
                intent = new Intent(UserActivity.this, ModifyActivityAll.class);
                intent.putExtra(LoginActivity.EXTRA_KEY_User_PHONE, phone.getText().toString());
                intent.putExtra(MODIFY_TYPE, "3");
                startActivityForResult(intent, MODIFY_PHONE);
                break;

            case R.id.bt_userfragment_save:
                toModify();
                break;
            case R.id.rl_photo:
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(UserActivity.this);
                View view = LayoutInflater.from(UserActivity.this).inflate(R.layout.common_item_photo, null);
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
                        takes(getTakePhoto());
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

        }
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

    private void takes(TakePhoto takePhoto) {
        configCompress(takePhoto);
        int size = Math.min(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        CropOptions cropOptions = new CropOptions.Builder().setOutputX(size).setOutputX(size).setWithOwnCrop(false).create();
        //Log.d("uri_user", "take: " + uri.toString());
        takePhoto.onPickFromGalleryWithCrop(uri, cropOptions);

    }

    private void toModify() {
        RequestBody requestBody = new FormBody.Builder()
                .add("username", Username.getText().toString().trim())
                .add("name", name.getText().toString().trim())
                .add("phone", phone.getText().toString().trim())
                .add("address", address.getText().toString().trim())
                .build();
        request = new Request.Builder().
                url(Constants.Modify)
                .post(requestBody)
                .build();
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        });
    }

    /*

   @intent:接受活动传回数据
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MODIFY_NAME:
                if (resultCode == RESULT_OK) {
                    String value = data.getStringExtra(MODIFY_TYPE);
                    name.setText(value);
                }
                break;
            case MODIFY_PHONE:
                if (resultCode == RESULT_OK) {
                    String value = data.getStringExtra(MODIFY_TYPE);
                    phone.setText(value);
                }
                break;
            case MODIFY_USERNAME:
                if (resultCode == RESULT_OK) {
                    String value = data.getStringExtra(MODIFY_TYPE);
                    Username.setText(value);
                }
                break;
            case MODIFY_ADDRESS:
                if (resultCode == RESULT_OK) {
                    String value = data.getStringExtra(MODIFY_TYPE);
                    address.setText(value);
                }
                break;
        }

    }


}
