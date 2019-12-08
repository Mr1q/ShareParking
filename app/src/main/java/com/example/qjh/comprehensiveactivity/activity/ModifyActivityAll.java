package com.example.qjh.comprehensiveactivity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;

public class ModifyActivityAll extends BaseActivity implements View.OnClickListener {
    private TextView tv_modify_type;
    private EditText et_modify_value;
    private Button bt_save;
    private ImageView RTU;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_modifyall);
        initData();

    }

    /**
     *    public static final int MODIFY_ADDRESS=1;
     *     public static final int MODIFY_USERNAME=2;
     *     public static final int MODIFY_PHONE=3;
     *     public static final int MODIFY_NAME=4;
     */
    private void initData() {
        tv_modify_type=(TextView)findViewById(R.id.tv_modify_type);
        et_modify_value=(EditText)findViewById(R.id.et_modify_value);
        bt_save=(Button)findViewById(R.id.bt_save);
        RTU=(ImageView) findViewById(R.id.RTU);
        bt_save.setOnClickListener(this);
        RTU.setOnClickListener(this);
        Intent intent = getIntent();

        if (intent != null) {
            String type=intent.getStringExtra(UserActivity.MODIFY_TYPE);
            switch (type)
            {
                case "1":
                    tv_modify_type.setText("修改地址");
                    et_modify_value.setInputType(InputType.TYPE_CLASS_TEXT);
                    et_modify_value.setText(intent.getStringExtra(LoginActivity.EXTRA_KEY_User_ADDRESS));
                break;
                case "2":
                    tv_modify_type.setText("修改昵称");
                    et_modify_value.setInputType(InputType.TYPE_CLASS_TEXT);
                    et_modify_value.setText(intent.getStringExtra(LoginActivity.EXTRA_KEY_User_USERNAME));
                    break;
                case "3":
                    tv_modify_type.setText("修改电话");
                    et_modify_value.setInputType(InputType.TYPE_CLASS_NUMBER);
                    et_modify_value.setText(intent.getStringExtra(LoginActivity.EXTRA_KEY_User_PHONE));
                    break;
                case "4":
                    tv_modify_type.setText("修改姓名");
                    et_modify_value.setInputType(InputType.TYPE_CLASS_TEXT);
                    et_modify_value.setText(intent.getStringExtra(LoginActivity.EXTRA_KEY_User_NAME));
                    break;
            }

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bt_save:
                Intent intent = new Intent();
                intent.putExtra(UserActivity.MODIFY_TYPE, et_modify_value.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.RTU:
                finish();
                break;
        }
    }
}
