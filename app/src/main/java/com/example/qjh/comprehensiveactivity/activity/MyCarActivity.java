package com.example.qjh.comprehensiveactivity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.adapter.CarAllAdapter;
import com.example.qjh.comprehensiveactivity.beans.Car;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;
import com.example.qjh.comprehensiveactivity.utils.DialogUtil;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.SimpleCallback;

import java.util.ArrayList;

/**
 * 添加车辆
 */
public class MyCarActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView rl_allcar;
    private Button bt_carfragment_add;
    private ArrayList<Car> carArrayList = new ArrayList<>();
    private final int SUCCESSS = 0;
    private CarAllAdapter carAllAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_mycars);
        initData();
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        rl_allcar.setLayoutManager(linearLayout);
        for (int i = 0; i < 5; i++) {
            carArrayList.add(new Car("123" + i, "456", false));
        }
        carAllAdapter = new CarAllAdapter(carArrayList);
        rl_allcar.setAdapter(carAllAdapter);
        carAllAdapter.setOnItemClick(new CarAllAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Car items, int postition) {
                final LoadingPopupView loadingPopup = (LoadingPopupView) new XPopup.Builder(MyCarActivity.this)
                        .dismissOnBackPressed(false)
                        .asLoading("正在保存中")
                        .show();
                loadingPopup.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingPopup.setTitle("正在加载。。。");
                    }
                }, 1000);


                Log.d("OnItemClick", "OnItemClick: " + postition);
                for (int i = 0; i < carArrayList.size(); i++) {
                    Log.d("OnItemClick", "OnItemClick: " + carArrayList.get(i).getAlways());
                    if (i == postition) {
                        if (carArrayList.get(i).getAlways()) {
                            carArrayList.get(i).setAlways(false);
                        } else {
                            carArrayList.get(i).setAlways(true);
                        }
                    } else {
                        if (carArrayList.get(i).getAlways()) {
                            carArrayList.get(i).setAlways(false);
                        }
                    }
                }
                carAllAdapter.notifyDataSetChanged();
                loadingPopup.delayDismissWith(3000, new Runnable() {
                    @Override
                    public void run() {
                        Toast("保存成功！！！");
                    }
                });
            }
        });


    }

    private void initData() {
        rl_allcar = (RecyclerView) findViewById(R.id.rl_allcar);
        bt_carfragment_add = (Button) findViewById(R.id.bt_carfragment_add);
        carArrayList = new ArrayList<Car>();
        bt_carfragment_add.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_carfragment_add:
                Intent intent = new Intent(MyCarActivity.this, AddCarActivity.class);
                startActivityForResult(intent, SUCCESSS);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SUCCESSS:
                if (resultCode == RESULT_OK) {
                    Car car = new Car(data.getStringExtra(AddCarActivity.CARNUMBER), "", false);
                    carArrayList.add(car);
                    carAllAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
