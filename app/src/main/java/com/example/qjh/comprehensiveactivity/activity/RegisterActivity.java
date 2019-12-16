package com.example.qjh.comprehensiveactivity.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.constant.Constants;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;
import com.example.qjh.comprehensiveactivity.utils.Code;
import com.example.qjh.comprehensiveactivity.utils.CreamUtils;
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

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_register_showCode;

    private Button bt_register;
    private EditText et_register_username;
    private EditText et_register_password;
    private EditText et_register_password_confirm;
    private EditText et_register_confirm;  //验证码
    private TextView tv_pwd_tips;
    private String realCode;
    private Request request;
    private OkHttpClient okHttpClient=new OkHttpClient();
    private static final int SUCCESS = 0;
    private static final int FAIL = -1;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    Toast("注册成功");
                    finish();
                    break;
                case FAIL:
                    Toast("注册失败");
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_register);
        CreamUtils.setWindow(this);
        initData();

    }

    /**
     * 初始化信息
     */
    private void initData() {
        iv_register_showCode = (ImageView) findViewById(R.id.iv_register_showCode);
        tv_pwd_tips = (TextView) findViewById(R.id.tv_pwd_tips);
        bt_register = (Button) findViewById(R.id.bt_register);
        bt_register.setOnClickListener(this);
        et_register_username = (EditText) findViewById(R.id.et_register_username);
        et_register_password = (EditText) findViewById(R.id.et_register_password);
        et_register_password_confirm = (EditText) findViewById(R.id.et_register_password_confirm);
        et_register_confirm = (EditText) findViewById(R.id.et_register_confirm);
        et_register_password_confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pwd = et_register_password.getText().toString().trim();
                String confirm = et_register_password_confirm.getText().toString().trim();
                if (pwd.equals(confirm)) {
                    tv_pwd_tips.setVisibility(View.INVISIBLE);
                } else {
                    tv_pwd_tips.setVisibility(View.VISIBLE);
                }
            }
        });
        iv_register_showCode.setOnClickListener(this);

        //将验证码用图片的形式显示出来
        iv_register_showCode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //获取验证码按钮
            case R.id.iv_register_showCode:
                iv_register_showCode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
                break;
            //注册按钮
            case R.id.bt_register:
                if (isOk()) {
                    toRegister();
                }else
                {
                    Toast("注册失败");
                }

                break;
        }
    }

    private boolean isOk() {
        if (et_register_username.getText().toString().isEmpty()) return false;
        if (et_register_password.getText().toString().isEmpty()) return false;
        if (et_register_password_confirm.getText().toString().isEmpty()) return false;
        if (!(et_register_password_confirm.getText().toString().equals(et_register_password.getText().toString())))
            return false;
        if (et_register_confirm.getText().toString().isEmpty() || (!et_register_confirm.getText().toString().equals(realCode))) return false;

        return true;
    }

    private void toRegister() {
        RequestBody requestBody = new FormBody.Builder()
                .add("username", et_register_username.getText().toString().trim())
                .add("password", et_register_password.getText().toString().trim())
                .build();
        request = new Request.Builder().
                url(Constants.Register)
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
}
