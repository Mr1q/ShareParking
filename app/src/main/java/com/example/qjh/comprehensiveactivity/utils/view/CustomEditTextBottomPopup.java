package com.example.qjh.comprehensiveactivity.utils.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;


import com.example.qjh.comprehensiveactivity.R;
import com.lxj.xpopup.core.BottomPopupView;


/**
 * Description: 带有输入框的Bottom弹窗
 * Create by dance, at 2019/2/27
 */
public class CustomEditTextBottomPopup extends BottomPopupView {
    public CustomEditTextBottomPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.common_item_bottomedittext;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onShow() {
        super.onShow();
        findViewById(R.id.btn_commit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
//                new XPopup.Builder(getContext()).atView(v).asAttachList(new String[]{"aa", "bbb"}, null, null).show();
            }
        });
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
    }

    public String getComment(){
        EditText et = findViewById(R.id.et_comment);
        return et.getText().toString();
    }


}
