package com.example.qjh.comprehensiveactivity.fragment;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.activity.AboutAppActivity;
import com.example.qjh.comprehensiveactivity.activity.LoginActivity;
import com.example.qjh.comprehensiveactivity.activity.MyAddressActivity;
import com.example.qjh.comprehensiveactivity.activity.MyCarActivity;
import com.example.qjh.comprehensiveactivity.activity.UserActivity;
import com.example.qjh.comprehensiveactivity.controler.ActivityCollecter;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;
import com.example.qjh.comprehensiveactivity.utils.CreamUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.support.constraint.Constraints.TAG;

import android.support.v7.widget.Toolbar;

public class UserFragment extends Fragment implements View.OnClickListener {
    private final int SUCCESS = 110;
    private RelativeLayout user_enter;
    private TextView userName;

    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private String phone;
    private String address;
    private String name;
    private String username;
    private CircleImageView message_image;
    private ImageView setting;
    private RelativeLayout curior;
    private RelativeLayout order_history;
    private RelativeLayout about_app;
    private RelativeLayout rl_mycars;
    private RelativeLayout rl_myaddress;
    private RelativeLayout advice;
    public TextView station_id;
    public Button bt_userfragment_logout;
    public static String stationid;
    private String Id;

    private OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build();

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

            }

        }
    };


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData(view);


    }

    private void initData(View view) {
        user_enter = (RelativeLayout) view.findViewById(R.id.user_enter);
        userName = (TextView) view.findViewById(R.id.User_Msg);
        message_image = (CircleImageView) view.findViewById(R.id.message_image);
        setting = (ImageView) view.findViewById(R.id.setting);
        rl_mycars = (RelativeLayout) view.findViewById(R.id.rl_mycars);
        rl_myaddress = (RelativeLayout) view.findViewById(R.id.rl_myaddress);
        advice = (RelativeLayout) view.findViewById(R.id.advice);
        order_history = (RelativeLayout) view.findViewById(R.id.order_history);
        about_app = (RelativeLayout) view.findViewById(R.id.about_app);
        station_id = (TextView) view.findViewById(R.id.station_id);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.CTL);
        final Toolbar toolbar_base = (Toolbar) view.findViewById(R.id.toolbar);
        //设置TOOLBAR
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar_base);


        bt_userfragment_logout = (Button) view.findViewById(R.id.bt_userfragment_logout);
        bt_userfragment_logout.setOnClickListener(this);
        user_enter.setOnClickListener(this);
        setting.setOnClickListener(this);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            username = bundle.getString(LoginActivity.EXTRA_KEY_User_USERNAME);
            userName.setText(username);
            phone = bundle.getString(LoginActivity.EXTRA_KEY_User_PHONE);
            address = bundle.getString(LoginActivity.EXTRA_KEY_User_ADDRESS);
            name = bundle.getString(LoginActivity.EXTRA_KEY_User_NAME);
        }

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d("asd", verticalOffset + "  ");
                if (verticalOffset <= -200) {
                    collapsingToolbarLayout.setTitle(username);//设置标题
                    //使用下面两个CollapsingToolbarLayout的方法设置展开透明->折叠时你想要的颜色
                    //  CTL.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
                    collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.White));

                } else {
                    collapsingToolbarLayout.setTitle("");
                }
            }
        });


        about_app.setOnClickListener(this);
        rl_mycars.setOnClickListener(this);
        rl_myaddress.setOnClickListener(this);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_fragment_user, container, false);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    public static final int MODIFY_TYPE = 0;

    @Override
    public void onClick(View v) {
        Intent intent2;
        switch (v.getId()) {
            case R.id.bt_userfragment_logout:
                ActivityCollecter.FinishALL();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                LoginActivity.isLogin = false;
                startActivity(intent);
                break;
            case R.id.user_enter:
                intent2 = new Intent(getContext(), UserActivity.class);
                intent2.putExtra(LoginActivity.EXTRA_KEY_User_USERNAME, username);
                intent2.putExtra(LoginActivity.EXTRA_KEY_User_PHONE, phone);
                intent2.putExtra(LoginActivity.EXTRA_KEY_User_ADDRESS, address);
                intent2.putExtra(LoginActivity.EXTRA_KEY_User_NAME, name);
                startActivityForResult(intent2, MODIFY_TYPE);
                break;
            case R.id.setting:
                //设置界面

                break;
            case R.id.about_app:
                intent2 = new Intent(getContext(), AboutAppActivity.class);
                startActivity(intent2);
                break;
            case R.id.rl_mycars:
                intent2 = new Intent(getContext(), MyCarActivity.class);
                startActivity(intent2);
                break;
            case R.id.rl_myaddress:
                intent2 = new Intent(getContext(), MyAddressActivity.class);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MODIFY_TYPE:
                if (resultCode == -1) {
                    phone = data.getStringExtra(LoginActivity.EXTRA_KEY_User_PHONE);
                    address = data.getStringExtra(LoginActivity.EXTRA_KEY_User_ADDRESS);
                    name = data.getStringExtra(LoginActivity.EXTRA_KEY_User_NAME);
                    username = data.getStringExtra(LoginActivity.EXTRA_KEY_User_USERNAME);
                }
                break;

        }
    }

}
