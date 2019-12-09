package com.example.qjh.comprehensiveactivity.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.parkingwang.keyboard.KeyboardInputController;
import com.parkingwang.keyboard.OnInputChangedListener;
import com.parkingwang.keyboard.PopupKeyboard;
import com.parkingwang.keyboard.view.InputView;
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
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;

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
        pictures.add("http://img2.imgtn.bdimg.com/it/u=2338912499,2258710075&fm=26&gp=0.jpg");
        pictures.add("http://pic1.win4000.com/wallpaper/8/5804428f565ea.jpg");
        pictures.add("http://pic1.win4000.com/wallpaper/8/580442a15e0c4.jpg");
        pictures.add("http://pic1.win4000.com/wallpaper/8/5804428543900.jpg");
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
//        appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
//        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.CTL);
//        final Toolbar toolbar_base = (Toolbar) view.findViewById(R.id.toolbar);
//        //设置TOOLBAR
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar_base);
//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                Log.d("asd", verticalOffset + "  ");
//                if (verticalOffset <= -200) {
//                    collapsingToolbarLayout.setTitle("首页");//设置标题
//                    //使用下面两个CollapsingToolbarLayout的方法设置展开透明->折叠时你想要的颜色
//                    //  CTL.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
//                    collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.White));
//
//                } else {
//                    collapsingToolbarLayout.setTitle("");
//                }
//            }
//        });

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
