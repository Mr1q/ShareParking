package com.example.qjh.comprehensiveactivity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.activity.AddParkingLotActivity;
import com.example.qjh.comprehensiveactivity.activity.BookParkLotActivity;
import com.example.qjh.comprehensiveactivity.activity.LoginActivity;
import com.example.qjh.comprehensiveactivity.adapter.MyParkingLotAdapter;
import com.example.qjh.comprehensiveactivity.beans.BaseResponse;
import com.example.qjh.comprehensiveactivity.beans.ParkingLot;
import com.example.qjh.comprehensiveactivity.constant.Constants;
import com.example.qjh.comprehensiveactivity.beans.NewsRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.suke.widget.SwitchButton;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.baidu.navisdk.module.future.interfaces.FutureTripParams.LoadingState.FAIL;

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
    private SwipeRefreshLayout refresh_parklot;
    private final int SUCCESS = 1;
    private final int DELETE_SUCCESS = 2;
    private final int DELETE_FAIL = -2;
    private final int SHARE_SUCCESS = 3;
    private final int SHARE_FAIL = -3;
    private final int CANCELSHARE_SUCCESS = 4;
    private final int CANCELSHARE_FAIL = -4;
    private final int USING_SUCCESS = 5;
    private final int USING_FAIL = -5;
    private final int UN_USING_SUCCESS = 6;
    private final int UN_USING_FAIL = -6;
    private List<ParkingLot> parkingLots = new ArrayList<>();
    private LoadingPopupView loadingPopup;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DELETE_SUCCESS:
                    parkingLots.remove(ID);
                    myParkingLotAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "删除成功！！！", Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS:
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
                                                    toDelete(items);
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
                            switch (id) {
                                case R.id.switch_share:
                                    loadingPopup = (LoadingPopupView) new XPopup.Builder(getContext())
                                            .dismissOnBackPressed(false)
                                            .asLoading("正在加载中")
                                            .show();
                                    if (Sch) {
                                        toShare(items);
                                    } else {
                                        toCancelShare(items);
                                    }
                                    break;
                                case R.id.switch_open:
                                    loadingPopup = (LoadingPopupView) new XPopup.Builder(getContext())
                                            .dismissOnBackPressed(false)
                                            .asLoading("正在加载中")
                                            .show();
                                    if (Sch) {

                                        toUsingParkLot(items);

                                    } else {
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                toUnUsingParkLot(items);
                                            }
                                        }, 1000);
                                    }
                                    break;
                            }
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
                    myParkingLotAdapter.notifyDataSetChanged();
                    //Toast.makeText(getContext(), "获取成功！！！", Toast.LENGTH_SHORT).show();
                    refresh_parklot.setRefreshing(false);
                    break;
                case SHARE_SUCCESS:
                    loadingPopup.dismiss();
                    getData();
                    Toast.makeText(getContext(), "分享成功！！！", Toast.LENGTH_SHORT).show();
                    break;

                case SHARE_FAIL:
                    loadingPopup.dismiss();
                    Toast.makeText(getContext(), "分享失败！！！", Toast.LENGTH_SHORT).show();
                    break;
                case CANCELSHARE_SUCCESS:
                    loadingPopup.dismiss();
                    getData();
                    Toast.makeText(getContext(), "取消分享成功！！！", Toast.LENGTH_SHORT).show();
                    break;

                case CANCELSHARE_FAIL:
                    loadingPopup.dismiss();
                    Toast.makeText(getContext(), "取消分享失败！！！", Toast.LENGTH_SHORT).show();
                    break;
                case USING_SUCCESS:
                    getData();
                    loadingPopup.dismiss();
                    Toast.makeText(getContext(), "开锁成功！！！", Toast.LENGTH_SHORT).show();
                    break;

                case USING_FAIL:
                    loadingPopup.dismiss();
                    Toast.makeText(getContext(), "开锁失败！！！", Toast.LENGTH_SHORT).show();
                    break;
                case UN_USING_SUCCESS:
                    loadingPopup.dismiss();
                    getData();
                    Toast.makeText(getContext(), "关锁成功！！！", Toast.LENGTH_SHORT).show();
                    break;

                case UN_USING_FAIL:
                    loadingPopup.dismiss();
                    Toast.makeText(getContext(), "关锁失败！！！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private int ID;
    private OnItemClickListener listener;
    private Request request;
    private OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build();
    private ParkingLot parkinglot;
    private LinearLayout ly_quickPark;
    private FloatingActionButton fl_addcar;


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
                Toast.makeText(getContext(), position, Toast.LENGTH_SHORT).show();
                // Log.d("class_class", "OnBannerClick: " + position);
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
            case R.id.ly_quickPark:
                Intent intent = new Intent(getContext(), BookParkLotActivity.class);
//                intent.getStringExtra()
                startActivity(intent);
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
        refresh_parklot = (SwipeRefreshLayout) view.findViewById(R.id.refresh_parklot);
        ly_quickPark = (LinearLayout) view.findViewById(R.id.ly_quickPark);
        fl_addcar = (FloatingActionButton) view.findViewById(R.id.fl_addcar);
        ly_bookparlot.setOnClickListener(this);
        ly_controlcar.setOnClickListener(this);
        ly_quickPark.setOnClickListener(this);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayout);
        getData();
        refresh_parklot.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        fl_addcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddParkingLotActivity.class);
                startActivity(intent);
            }
        });
    }


    private void getData() {
        refresh_parklot.setRefreshing(true);
        NewsRequest newsRequest = new NewsRequest(); //get请求参数
        newsRequest.setParkOwnerId(LoginActivity.ID); //当前用户的ID
        request = new Request.Builder().
                url(Constants.FindMyParkingLot + newsRequest.toStringFindMyPark()).
                build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        Log.d("onResponse_body", "onResponse: " + body);
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(body);
                        String state = jsonObject.optString("state");
                        if (state.equals("0")) {
                            handler.sendEmptyMessage(FAIL);
                        } else {
                            Type jsontype = new TypeToken<BaseResponse<List<ParkingLot>>>() {
                            }.getType();
                            BaseResponse<List<ParkingLot>> newsListResponese
                                    = gson.fromJson(body, jsontype);
                            parkingLots.clear();
                            for (ParkingLot parkingLot : newsListResponese.getData()) {
                                parkingLots.add(parkingLot);
                            }
                            handler.sendEmptyMessage(SUCCESS);
                        }
                    } else {
                        handler.sendEmptyMessage(FAIL);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void text() {
        for (int i = 0; i < 3; i++) {
            ParkingLot parkingLot = new ParkingLot();
            parkingLot.setPark_address("1");
            parkingLot.setPark_distance(123.0);
            parkingLot.setPark_latitude("12");
            parkingLot.setPark_longitude("12");
            parkingLot.setPark_name("12");
            parkingLot.setPark_ownerName("12");
            parkingLot.setPark_ownerId(1);
            parkingLot.setPark_price("1");
            parkingLot.setShare("1");
            parkingLot.setStatus("1");

            parkingLots.add(parkingLot);
        }
        parkingLots.get(0).setParklotImage("http://pic1.win4000.com/wallpaper/8/5804428543900.jpg");
        parkingLots.get(1).setParklotImage("http://img2.imgtn.bdimg.com/it/u=2338912499,2258710075&fm=26&gp=0.jpg");
        parkingLots.get(2).setParklotImage("http://pic1.win4000.com/wallpaper/8/5804428f565ea.jpg");
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
        getData();
        banner.start();


    }

    private void toCancelShare(ParkingLot items) {
        RequestBody requestBody = new FormBody.Builder()
                .add("parkId", items.getPark_id()).build();
        request = new Request.Builder().
                url(Constants.CancelShareMyParkingLot).
                post(requestBody).
                build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        Log.d("onResponse_body", "onResponse: " + body);
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(body);
                        String state = jsonObject.optString("state");
                        if (state.equals("0")) {
                            handler.sendEmptyMessage(CANCELSHARE_FAIL);
                        } else {
                            handler.sendEmptyMessage(CANCELSHARE_SUCCESS);
                        }
                    } else {
                        handler.sendEmptyMessage(CANCELSHARE_FAIL);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void toUsingParkLot(ParkingLot items) {
        RequestBody requestBody = new FormBody.Builder()
                .add("parkId", items.getPark_id()).build();
        request = new Request.Builder().
                url(Constants.UsingParkLot).
                post(requestBody).
                build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        Log.d("onResponse_body", "onResponse: " + body);
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(body);
                        String state = jsonObject.optString("state");
                        if (state.equals("0")) {
                            handler.sendEmptyMessage(USING_FAIL);
                        } else {
                            handler.sendEmptyMessage(USING_SUCCESS);
                        }
                    } else {
                        handler.sendEmptyMessage(USING_FAIL);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void toUnUsingParkLot(ParkingLot items) {
        RequestBody requestBody = new FormBody.Builder()
                .add("parkId", items.getPark_id()).build();
        request = new Request.Builder().
                url(Constants.UnUsingParkLot).
                post(requestBody).
                build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        Log.d("onResponse_body", "onResponse: " + body);
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(body);
                        String state = jsonObject.optString("state");
                        if (state.equals("0")) {
                            handler.sendEmptyMessage(UN_USING_FAIL);
                        } else {
                            handler.sendEmptyMessage(UN_USING_SUCCESS);
                        }
                    } else {
                        handler.sendEmptyMessage(UN_USING_FAIL);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void toShare(ParkingLot items) {
        RequestBody requestBody = new FormBody.Builder()
                .add("parkId", items.getPark_id()).build();
        request = new Request.Builder().
                url(Constants.ShareMyParkingLot).
                post(requestBody).
                build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        Log.d("onResponse_body", "onResponse: " + body);
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(body);
                        String state = jsonObject.optString("state");
                        if (state.equals("0")) {
                            handler.sendEmptyMessage(SHARE_FAIL);
                        } else {
                            handler.sendEmptyMessage(SHARE_SUCCESS);
                        }
                    } else {
                        handler.sendEmptyMessage(SHARE_FAIL);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void toDelete(ParkingLot items) {
        RequestBody requestBody = new FormBody.Builder()
                .add("parkId", items.getPark_id()).build();
        request = new Request.Builder().
                url(Constants.DeleteMyParkingLot).
                post(requestBody).
                build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        Log.d("onResponse_body", "onResponse: " + body);
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(body);
                        String state = jsonObject.optString("state");
                        if (state.equals("0")) {
                            handler.sendEmptyMessage(DELETE_FAIL);
                        } else {
                            handler.sendEmptyMessage(DELETE_SUCCESS);
                        }
                    } else {
                        handler.sendEmptyMessage(DELETE_FAIL);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
