package com.example.qjh.comprehensiveactivity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.activity.ConnectActivity;
import com.example.qjh.comprehensiveactivity.utils.NetUtils;
import com.example.qjh.comprehensiveactivity.utils.view.WaveView;

public class ControlCarFragment extends Fragment implements View.OnClickListener {

    private WaveView begin_connect;
    private FrameLayout ly_control;
    private Button bt_back;
    private Button bt_up;
    private Button bt_left;
    private Button bt_right;
    private Button bt_stop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_fragment_controlcar, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        begin_connect = (WaveView) view.findViewById(R.id.begin_connect);
        ly_control = (FrameLayout) view.findViewById(R.id.ly_control);
        bt_back = (Button) view.findViewById(R.id.bt_back);
        bt_up = (Button) view.findViewById(R.id.bt_up);
        bt_left = (Button) view.findViewById(R.id.bt_left);
        bt_right = (Button) view.findViewById(R.id.bt_right);
        bt_stop = (Button) view.findViewById(R.id.bt_stop);
        begin_connect.setOnClickListener(this);

        bt_up.setOnClickListener(this);
        bt_back.setOnClickListener(this);
        bt_right.setOnClickListener(this);
        bt_left.setOnClickListener(this);
        bt_stop.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.begin_connect:
                Intent intent = new Intent(getContext(), ConnectActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.bt_left:
                NetUtils.GO_LEFT();
                break;
            case R.id.bt_right:
                NetUtils.GO_RIGHT();
                break;
            case R.id.bt_up:
                NetUtils.GO_FOWRD();
                break;
            case R.id.bt_back:
                NetUtils.GO_BACK();
                break;
            case R.id.bt_stop:
                NetUtils.GO_STOP();
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == -1) {
                    begin_connect.setVisibility(View.INVISIBLE);
                    ly_control.setVisibility(View.VISIBLE);
                    break;
                }
        }
    }
}
