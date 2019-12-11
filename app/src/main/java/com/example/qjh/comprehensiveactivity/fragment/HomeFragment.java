package com.example.qjh.comprehensiveactivity.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.activity.AddCarActivity;
import com.example.qjh.comprehensiveactivity.activity.AddParkingLotActivity;
import com.example.qjh.comprehensiveactivity.activity.LoginActivity;
import com.example.qjh.comprehensiveactivity.activity.MyCarActivity;
import com.example.qjh.comprehensiveactivity.adapter.MyParkingLotAdapter;
import com.example.qjh.comprehensiveactivity.adapter.SurroundAdapter;
import com.example.qjh.comprehensiveactivity.beans.ParkingLot;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;
import com.example.qjh.comprehensiveactivity.utils.CreamUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.parkingwang.keyboard.KeyboardInputController;
import com.parkingwang.keyboard.OnInputChangedListener;
import com.parkingwang.keyboard.PopupKeyboard;
import com.parkingwang.keyboard.view.InputView;
import com.suke.widget.SwitchButton;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.support.constraint.Constraints.TAG;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private Banner banner;
    private List pictures = new ArrayList<>();
    private Context context;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView recyclerView;
    public LinearLayout ly_bookparlot; //预定车位
    public LinearLayout ly_controlcar; //预定车位
    private MyParkingLotAdapter myParkingLotAdapter;
    private final int SUCCESS = 1;
    private List<ParkingLot> parkingLots = new ArrayList<>();
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    parkingLots.remove(ID);
                    myParkingLotAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "删除成功！！！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private int ID;
    private OnItemClickListener listener;

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
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
               // Toast.makeText(getContext(), position, Toast.LENGTH_SHORT).show();
                Log.d("class_class", "OnBannerClick: "+position);

            }
        });


    }

    public void setOnItemClick(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        /*注意参数*/
        public void OnItemClick(int id);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ly_bookparlot:
                listener.OnItemClick(R.id.ly_bookparlot);
                break;
            case R.id.ly_controlcar:
                listener.OnItemClick(R.id.ly_controlcar);
                break;
        }
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Log.d("home_layout", path.toString());
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
     *
     * @param view
     */
    private void initData(View view) {
        banner = (Banner) view.findViewById(R.id.banner);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        ly_bookparlot = (LinearLayout) view.findViewById(R.id.ly_bookparlot);
        ly_controlcar = (LinearLayout) view.findViewById(R.id.ly_controlcar);
        ly_bookparlot.setOnClickListener(this);
        ly_controlcar.setOnClickListener(this);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayout);


        for (int i = 0; i < 3; i++) {
            ParkingLot parkingLot = new ParkingLot();
            parkingLot.setPark_address("1");
            parkingLot.setPark_distance(123);
            parkingLot.setPark_latitude("12");
            parkingLot.setPark_longitude("12");
            parkingLot.setPark_name("12");
            parkingLot.setPark_ownerName("12");
            parkingLot.setPark_ownerId(1);
            parkingLot.setPark_price(1);
            parkingLot.setShare("1");
            parkingLot.setStatus("1");

            parkingLots.add(parkingLot);
        }

        parkingLots.get(0).setParklotImage("http://pic1.win4000.com/wallpaper/8/5804428543900.jpg");
        parkingLots.get(1).setParklotImage("http://img2.imgtn.bdimg.com/it/u=2338912499,2258710075&fm=26&gp=0.jpg");
        parkingLots.get(2).setParklotImage("http://pic1.win4000.com/wallpaper/8/5804428f565ea.jpg");
        myParkingLotAdapter = new MyParkingLotAdapter(getContext(), parkingLots);
        myParkingLotAdapter.setOnItemClick(new MyParkingLotAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(ParkingLot items, int id) {
                new XPopup.Builder(getContext())
//                         .dismissOnTouchOutside(false)
//                         .autoDismiss(false)
                        .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                        .setPopupCallback(new SimpleCallback() {
                            @Override
                            public void onCreated() {
                                Log.e("tag", "弹窗创建了");
                            }

                            @Override
                            public void onShow() {
                                Log.e("tag", "onShow");
                            }

                            @Override
                            public void onDismiss() {
                                Log.e("tag", "onDismiss");
                            }

                            //如果你自己想拦截返回按键事件，则重写这个方法，返回true即可
                            @Override
                            public boolean onBackPressed() {
                                // ToastUtils.showShort("我拦截的返回按键，按返回键XPopup不会关闭了");
                                return false;
                            }
                        }).asConfirm("提醒", "是否删除！！！！0.0",
                        "取消", "确定",
                        new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                final LoadingPopupView loadingPopup = (LoadingPopupView) new XPopup.Builder(getContext())
                                        .dismissOnBackPressed(false)
                                        .asLoading("正在删除中")
                                        .show();
                                loadingPopup.delayDismissWith(800, new Runnable() {
                                    @Override
                                    public void run() {
                                        handler.sendEmptyMessage(SUCCESS);
                                        ID = id;

                                    }
                                });
                            }
                        }, null, false)
                        .show();


            }

            @Override
            public void OnItemClick(ParkingLot items) {

            }

            @Override
            public void OnItemClick2(ParkingLot items, int id, Boolean Sch, SwitchButton switchButton) {

            }
        });
        myParkingLotAdapter.setOnItemClick_Add(new MyParkingLotAdapter.OnItemClickListenerAdd() {
            @Override
            public void OnItemAddImage() {
                Intent intent = new Intent(getContext(), AddParkingLotActivity.class);
                startActivity(intent);


            }
        });
        recyclerView.setAdapter(myParkingLotAdapter);
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
