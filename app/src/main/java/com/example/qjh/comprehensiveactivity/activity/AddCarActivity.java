package com.example.qjh.comprehensiveactivity.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;
import com.parkingwang.keyboard.OnInputChangedListener;
import com.parkingwang.keyboard.PopupKeyboard;
import com.parkingwang.keyboard.view.InputView;

public class AddCarActivity extends BaseActivity implements View.OnClickListener {

    private InputView input_view;
    private ImageView RTU;
    private TextView tv_confirm;
    private String CarNumber; //车牌号
    public static  final  String CARNUMBER="CARNUMBER";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commmon_dialog_addcar);
        RTU = (ImageView) findViewById(R.id.RTU);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        input_view = (InputView) findViewById(R.id.input_view);
        RTU.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);

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
                            CarNumber=number;
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_confirm:
                Intent intent=new Intent(AddCarActivity.this,MyCarActivity.class);
                intent.putExtra(CARNUMBER,CarNumber);
                setResult(RESULT_OK,intent);
                finish();
                break;

            case R.id.RTU:
                finish();
                break;
        }
    }
}
