package com.example.qjh.comprehensiveactivity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.activity.MapActivity;

public class SurroundFragment  extends Fragment {
    private ImageView iv_map;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_fragment_surroundcar, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iv_map=view.findViewById(R.id.iv_map);
        iv_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), MapActivity.class);
                startActivity(intent);
            }
        });

    }
}
