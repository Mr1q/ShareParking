package com.example.qjh.comprehensiveactivity.controler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.qjh.comprehensiveactivity.R;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

/**
 * 显示底部订阅
 */
public class PagerBottomPopup extends BottomPopupView implements View.OnClickListener {
    public PagerBottomPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.common_activity_reserve;
    }

    TextView tv_parklotName;
    TextView tv_bookDate;
    TextView tv_time;
    TextView tv_price;
    Button bt_book;
    ImageView RTU;
    @Override
    protected void onCreate() {
        super.onCreate();
        tv_parklotName = findViewById(R.id.tv_parklotName);
        tv_bookDate = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        tv_price = findViewById(R.id.tv_price);
        bt_book = findViewById(R.id.bt_book);
        RTU = findViewById(R.id.RTU);
        RTU.setOnClickListener(this);
    }

    @Override
    protected void onShow() {
        super.onShow();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext()) * .75f);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.RTU:
                doDismissAnimation();
                break;
        }
    }
}
