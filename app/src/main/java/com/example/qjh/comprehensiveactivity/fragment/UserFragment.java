package com.example.qjh.comprehensiveactivity.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.activity.LoginActivity;
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


public class UserFragment extends Fragment {
    private final int SUCCESS = 110;
    private RelativeLayout user_enter;
    private TextView userName;
    private String phone;
    private String email;
    private String Head_uri;
    private CircleImageView message_image;
    private RelativeLayout setting;
    private RelativeLayout curior;
    private RelativeLayout order_history;
    private RelativeLayout about_app;
    private RelativeLayout modify_tation;
    private RelativeLayout myaddress;
    private RelativeLayout advice;
    public TextView station_id;
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

        user_enter = (RelativeLayout) view.findViewById(R.id.user_enter);
        userName = (TextView) view.findViewById(R.id.User_Msg);
        message_image = (CircleImageView) view.findViewById(R.id.message_image);
        setting = (RelativeLayout) view.findViewById(R.id.setting);
        myaddress = (RelativeLayout) view.findViewById(R.id.myaddress);
        modify_tation = (RelativeLayout) view.findViewById(R.id.modify_tation);
        advice = (RelativeLayout) view.findViewById(R.id.advice);
        order_history = (RelativeLayout) view.findViewById(R.id.order_history);
        about_app = (RelativeLayout) view.findViewById(R.id.about_app);
        station_id = (TextView) view.findViewById(R.id.station_id);


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


}
