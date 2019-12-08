package com.example.qjh.comprehensiveactivity.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.beans.User;
import com.example.qjh.comprehensiveactivity.constant.Constants;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserActivity extends BaseActivity implements View.OnClickListener {
    private TextView Username;
    private TextView name;
    private TextView phone;
    private TextView address;
    private RelativeLayout rl_username;
    private RelativeLayout rl_name;
    private RelativeLayout rl_phone;
    private RelativeLayout rl_address;
    private Button bt_userfragment_save;

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
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    Toast("修改成功");
                    Intent intent = new Intent();

                    intent.putExtra(LoginActivity.EXTRA_KEY_User_PHONE, phone.getText().toString());
                    intent.putExtra(LoginActivity.EXTRA_KEY_User_ADDRESS, address.getText().toString());
                    intent.putExtra(LoginActivity.EXTRA_KEY_User_NAME, name.getText().toString());
                    intent.putExtra(LoginActivity.EXTRA_KEY_User_USERNAME, Username.getText().toString());
                    setResult(RESULT_OK, intent);
                    editor = LoginActivity.sharedPreferences.edit();
                    editor.putString(LoginActivity.EXTRA_KEY_User_PHONE,phone.getText().toString());
                    editor.putString(LoginActivity.EXTRA_KEY_User_ADDRESS,address.getText().toString());
                    editor.putString(LoginActivity.EXTRA_KEY_User_NAME,name.getText().toString());
                    editor.putString(LoginActivity.EXTRA_KEY_User_USERNAME,Username.getText().toString());
                    editor.commit();
                    finish();
                    break;
                case FAIL:
                    Toast("修改失败");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commmn_activity_usermsg);
        initData();
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
        bt_userfragment_save = (Button) findViewById(R.id.bt_userfragment_save);
        //rl_username.setOnClickListener(this);  昵称不能更改
        rl_name.setOnClickListener(this);
        rl_phone.setOnClickListener(this);
        rl_address.setOnClickListener(this);
        bt_userfragment_save.setOnClickListener(this);


        Intent intent = getIntent();
        if (intent != null) {
            name.setText(intent.getStringExtra(LoginActivity.EXTRA_KEY_User_NAME));
            Username.setText(intent.getStringExtra(LoginActivity.EXTRA_KEY_User_USERNAME));
            phone.setText(intent.getStringExtra(LoginActivity.EXTRA_KEY_User_PHONE));
            address.setText(intent.getStringExtra(LoginActivity.EXTRA_KEY_User_ADDRESS));
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
        }
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
