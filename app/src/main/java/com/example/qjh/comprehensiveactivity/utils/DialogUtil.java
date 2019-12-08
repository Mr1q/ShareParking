package com.example.qjh.comprehensiveactivity.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.activity.LoginActivity;
import com.parkingwang.keyboard.OnInputChangedListener;
import com.parkingwang.keyboard.PopupKeyboard;
import com.parkingwang.keyboard.view.InputView;

public class DialogUtil {

    private Dialog dialog;
    private View inflate;
    private Activity context;
    private InputView input_view;
    private TextView tv_cancel;
    private TextView tv_confirm;

    //中间显示的dialog
    public void showCentreDialog() {
        //自定义dialog显示布局
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);
        dialog.show();
    }

    private void initData() {
        inflate = LayoutInflater.from(context).inflate(R.layout.commmon_dialog_addcar, null);
        //自定义dialog显示风格
        dialog = new Dialog(context, R.style.DialogCentre);
        //点击其他区域消失
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(inflate);
        tv_cancel = (TextView) inflate.findViewById(R.id.tv_cancel);
        tv_confirm = (TextView) inflate.findViewById(R.id.tv_confirm);
        input_view = (InputView) inflate.findViewById(R.id.input_view);
        final PopupKeyboard mPopupKeyboard = new PopupKeyboard(context);
        // 弹出键盘内部包含一个KeyboardView，在此绑定输入两者关联。
        mPopupKeyboard.attach(input_view, context);

        // KeyboardInputController提供一个默认实现的新能源车牌锁定按钮
        mPopupKeyboard.getController()
                .setDebugEnabled(true)
                .addOnInputChangedListener(new OnInputChangedListener() {
                    @Override
                    public void onChanged(String number, boolean isCompleted) {
                        if (isCompleted) {
                            mPopupKeyboard.dismiss(context.getWindow());
                        }
                    }

                    @Override
                    public void onCompleted(String number, boolean isAutoCompleted) {
                        mPopupKeyboard.dismiss(context.getWindow());

                    }
                });


    }

    public DialogUtil(Activity context) {
        this.context=context;
        initData();

    }

    //关闭dialog时调用
    public void close() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

}
