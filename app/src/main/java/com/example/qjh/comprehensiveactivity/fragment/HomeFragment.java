package com.example.qjh.comprehensiveactivity.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.utils.CreamUtils;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class HomeFragment extends Fragment implements OnBannerListener {
    private Banner banner;
    private List pictures = new ArrayList<>();
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_fragment_home, container, false);


        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity().getApplicationContext();
        initData(view);

        pictures.add("http://www.tianyaguet.club/group1/M00/00/02/rBAACV2YEU2AIrYvAAVWOhuF4pk490.png");
        pictures.add("http://www.tianyaguet.club/group1/M00/00/02/rBAACV2YEU2AIrYvAAVWOhuF4pk490.png");
        pictures.add("http://www.tianyaguet.club/group1/M00/00/02/rBAACV2YEYKAYax1AAOg6sQMWAI717.png");
        banner.setImages(pictures).setImageLoader(new GlideImageLoader()).start();
        banner.setOnBannerListener(this);

    }

    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(context,position,Toast.LENGTH_SHORT).show();
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Log.d("home_layout",path.toString());
            Glide.with(context)
                    .load(path)
                    .into(imageView);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    /**
     * 初始化数据
     * @param view
     */
    private void initData(View view) {
        banner=(Banner) view.findViewById(R.id.banner);
    }
    @Override
    public void onPause() {
        super.onPause();
        if (banner != null) {
            banner.startAutoPlay();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        banner.start();



    }
}
