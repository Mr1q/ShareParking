package com.example.qjh.comprehensiveactivity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.activity.MapActivity;
import com.example.qjh.comprehensiveactivity.adapter.SurroundAdapter;
import com.example.qjh.comprehensiveactivity.beans.ParkingLot;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

public class SurroundFragment  extends Fragment {
    public static final String EXTRA_LOG = "EXTRA_LOG";
    public static final String EXTRA_LAT = "EXTRA_LAT";
    private ImageView iv_map;
    private RecyclerView rl_surroundCar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_fragment_surroundcar, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData(view);
        rl_surroundCar.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE ){
                    if (!ViewCompat.canScrollVertically(recyclerView, 1)){
                        Toast.makeText(getContext(),"拉到底了",Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        iv_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), MapActivity.class);
                intent.putExtra("123","123");
                startActivity(intent);
            }
        });


    }

    private void initData(@NonNull View view) {
        iv_map=view.findViewById(R.id.iv_map);
        rl_surroundCar=(RecyclerView) view.findViewById(R.id.rl_surroundCar);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        rl_surroundCar.setLayoutManager(linearLayoutManager);
        List<ParkingLot> parkingLots=new ArrayList<>();
        for(int i=0;i<3;i++) {
            ParkingLot parkingLot=new ParkingLot();
            parkingLot.setPark_address("1");
            parkingLot.setPark_distance(123);
            parkingLot.setPark_latitude("25.310993");
            parkingLot.setPark_longitude("110.423171");
//            LatLng point = new LatLng(25.320883, 110.423171);
            parkingLot.setPark_name("12");
            parkingLot.setPark_ownerName("12");
            parkingLot.setPark_ownerId(1);
            parkingLot.setPark_price(1);
            parkingLot.setShare("1");
            parkingLot.setStatus("1");

            parkingLot.setParklotImage("http://pic1.win4000.com/wallpaper/8/5804428543900.jpg");
            parkingLots.add(parkingLot);
        }
        SurroundAdapter surroundAdapter=new SurroundAdapter(getContext(),parkingLots);
        rl_surroundCar.setAdapter(surroundAdapter);
        surroundAdapter.setOnItemClick(new SurroundAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(ParkingLot items, int id) {

            }

            @Override
            public void OnItemClick(ParkingLot items) {
                Intent intent=new Intent(getContext(), MapActivity.class);
                intent.putExtra(EXTRA_LOG,items.getPark_longitude());
                intent.putExtra(EXTRA_LAT,items.getPark_latitude());
                startActivity(intent);
            }

            @Override
            public void OnItemClick2(ParkingLot items, int id, Boolean Sch, SwitchButton switchButton) {

            }
        });
    }
}
