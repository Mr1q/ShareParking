package com.example.qjh.comprehensiveactivity.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.beans.BaseResponse;
import com.example.qjh.comprehensiveactivity.beans.User;
import com.example.qjh.comprehensiveactivity.constant.Constants;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;
import com.example.qjh.comprehensiveactivity.utils.CreamUtils;
import com.example.qjh.comprehensiveactivity.beans.NewsRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private TextView iv_common_register;
    private Button bt_login;
    private EditText et_login_username;
    private EditText et_login_password;
    private CheckBox cb_login_Rember;
    private ImageView iv_common_hide; //是否显示密码
    private Request request;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private static final int SUCCESS = 0;
    private static final int FAIL = -1;
    private User user;
    public static final String EXTRA_KEY_User_ADDTIME = "EXTRA_KEY_User_ADDTIME";
    public static final String EXTRA_KEY_User_USERNAME = "EXTRA_KEY_User_USERNAME";
    public static final String EXTRA_KEY_User_NAME = "EXTRA_KEY_User_NAME";
    public static final String EXTRA_KEY_User_MYID = "EXTRA_KEY_User_MYID";
    public static final String EXTRA_KEY_User_PHONE = "EXTRA_KEY_NOTE_PHONE";
    public static final String EXTRA_KEY_User_ADDRESS = "EXTRA_KEY_User_ADDRESS";
    public static final String EXTRA_KEY_User_PASSWORD = "EXTRA_KEY_User_PASSWORD";
    public static final String EXTRA_KEY_User_CHECKED = "EXTRA_KEY_User_CHECKED";
    public static final String EXTRA_KEY_User_PHOTO = "EXTRA_KEY_User_PHOTO";
    public static final String EXTRA_KEY_User_DEFAULTCAR = "EXTRA_KEY_User_DEFAULTCAR";
    //持久化技术
    public static SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public static Boolean isLogin=true;

    public  static  String ID; //用户ID
    public  static  String Username;//用户名
    public  static  String defaultCar;//默认车牌号
    public  static  String userHeadPhoto;//用户头像路径

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    Toast("登录成功");
                    if (cb_login_Rember.isChecked()) {
                        editor.putBoolean(EXTRA_KEY_User_CHECKED, true);
                        editor.putString(EXTRA_KEY_User_USERNAME, user.getMyusername());
                        editor.putString(EXTRA_KEY_User_PHONE, user.getMyphone());
                        editor.putString(EXTRA_KEY_User_MYID, user.getMyid());
                        editor.putString(EXTRA_KEY_User_NAME, user.getMyname());
                        editor.putString(EXTRA_KEY_User_ADDRESS, user.getMyaddress());
                        editor.putString(EXTRA_KEY_User_ADDTIME, user.getMyaddtime());
                        editor.putString(EXTRA_KEY_User_PASSWORD, user.getMypasswprd());
                        editor.putString(EXTRA_KEY_User_PHOTO, user.getHeadPhotoURL());
                        editor.putString(EXTRA_KEY_User_DEFAULTCAR, user.getCar_number());
                    } else {
                        editor.clear();
                    }
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, TotalActivity.class);
                    intent.putExtra(EXTRA_KEY_User_USERNAME, user.getMyusername());
                    Log.d("login_class", "handleMessage: "+user.getMyusername());
                    intent.putExtra(EXTRA_KEY_User_PHONE, user.getMyphone());
                    intent.putExtra(EXTRA_KEY_User_MYID, user.getMyid());
                    intent.putExtra(EXTRA_KEY_User_NAME, user.getMyname());
                    intent.putExtra(EXTRA_KEY_User_ADDRESS, user.getMyaddress());
                    intent.putExtra(EXTRA_KEY_User_ADDTIME, user.getMyaddtime());
                    intent.putExtra(EXTRA_KEY_User_PASSWORD, user.getMypasswprd());
                    intent.putExtra(EXTRA_KEY_User_PHOTO, user.getHeadPhotoURL());
                    intent.putExtra(EXTRA_KEY_User_DEFAULTCAR, user.getCar_number());
                    ID=user.getMyid();
                    Username=user.getMyusername();
                    defaultCar=user.getCar_number();
                    userHeadPhoto=user.getHeadPhotoURL();
                    startActivity(intent);
                    finish();
                    break;
                case FAIL:
                    Toast("登录失败");
                    break;
            }
        }
    };
    private boolean switchs = false;  //选择是否隐藏密码

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_login);
        CreamUtils.setWindow(this);
        initData();
    }

    private void initData() {
        iv_common_register = (TextView) findViewById(R.id.iv_common_register);
        et_login_username = (EditText) findViewById(R.id.et_login_username);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
        iv_common_hide = (ImageView) findViewById(R.id.iv_common_hide);
        cb_login_Rember = (CheckBox) findViewById(R.id.cb_login_Rember);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_login.setOnClickListener(this);
        iv_common_register.setOnClickListener(this);
        iv_common_hide.setOnClickListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        if(!isLogin)  //退出登录，返回登录界面
        {
            editor.clear();
            editor.apply();

        }
        Boolean checkeds = sharedPreferences.getBoolean(EXTRA_KEY_User_CHECKED, false);
        if (checkeds) {
            String username = sharedPreferences.getString(EXTRA_KEY_User_USERNAME, "");
            String phone = sharedPreferences.getString(EXTRA_KEY_User_PHONE, "");
            String myid = sharedPreferences.getString(EXTRA_KEY_User_MYID, "");
            String name = sharedPreferences.getString(EXTRA_KEY_User_NAME, "");
            String address = sharedPreferences.getString(EXTRA_KEY_User_ADDRESS, "");
            String addtime = sharedPreferences.getString(EXTRA_KEY_User_ADDTIME, "");
            String pasword = sharedPreferences.getString(EXTRA_KEY_User_PASSWORD, "");
            String photo = sharedPreferences.getString(EXTRA_KEY_User_PHOTO, "");
            String defaultCars = sharedPreferences.getString(EXTRA_KEY_User_DEFAULTCAR, "");
            Intent intent = new Intent(LoginActivity.this, TotalActivity.class);
            intent.putExtra(EXTRA_KEY_User_USERNAME, username);
            intent.putExtra(EXTRA_KEY_User_PHONE, phone);
            intent.putExtra(EXTRA_KEY_User_MYID, myid);
            intent.putExtra(EXTRA_KEY_User_NAME, name);
            intent.putExtra(EXTRA_KEY_User_ADDRESS, address);
            intent.putExtra(EXTRA_KEY_User_ADDTIME, addtime);
            intent.putExtra(EXTRA_KEY_User_PASSWORD, pasword);
            intent.putExtra(EXTRA_KEY_User_PHOTO, photo);
            intent.putExtra(EXTRA_KEY_User_DEFAULTCAR, defaultCar);
            ID=myid;
            Username=username;
            defaultCar=defaultCars;
            userHeadPhoto=photo;
            startActivity(intent);
            finish();


        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_common_register:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                //Toast.makeText(LoginActivity.this,"正在开发中..",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_login:
                tologin();
                break;
            case R.id.iv_common_hide:
                switchs = !switchs;
                if (switchs) {
                    iv_common_hide.setImageResource(R.mipmap.common_activity_login_pwdsee);
                    et_login_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    iv_common_hide.setImageResource(R.mipmap.common_activity_login_hide);
                    et_login_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    et_login_password.setTypeface(Typeface.DEFAULT);
                }
                break;

        }
    }

    private void tologin() {
        NewsRequest newsRequest = new NewsRequest();
        newsRequest.setUsername(et_login_username.getText().toString().trim());
        newsRequest.setPassword(et_login_password.getText().toString().trim());
        RequestBody requestBody=new FormBody.Builder()
                .add("username",et_login_username.getText().toString().trim())
                .add("password",et_login_password.getText().toString().trim()).build();
        request = new Request.Builder().
                post(requestBody).
                url(Constants.Login).
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
                            Type jsontype = new TypeToken<BaseResponse<List<User>>>() {
                            }.getType();
                            BaseResponse<List<User>> newsListResponese
                                    = gson.fromJson(body, jsontype);
                            user = newsListResponese.getUserData().get(0);

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
