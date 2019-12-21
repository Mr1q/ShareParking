package com.example.qjh.comprehensiveactivity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.qjh.comprehensiveactivity.R;

import com.example.qjh.comprehensiveactivity.activity.LoginActivity;
import com.example.qjh.comprehensiveactivity.activity.MapActivity;
import com.example.qjh.comprehensiveactivity.adapter.SurroundAdapter;
import com.example.qjh.comprehensiveactivity.beans.BaseResponse;
import com.example.qjh.comprehensiveactivity.beans.NewsRequest;
import com.example.qjh.comprehensiveactivity.beans.ParkingLot;
import com.example.qjh.comprehensiveactivity.constant.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lxj.xpopup.XPopup;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.suke.widget.SwitchButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SurroundFragment extends Fragment {
    public static final String EXTRA_LOG = "EXTRA_LOG";
    public static final String EXTRA_LAT = "EXTRA_LAT";
    public static final String EXTRA_PARNAME = "EXTRA_PARNAME";
    public static final String EXTRA_ADDRESS = "EXTRA_ADDRESS";
    public static final String EXTRA_MYADDRESS = "ETRA_MYADDRESS";
    public static final String EXTRA_ID = "EXTRA_ID";
    private ImageView iv_map;
    private RecyclerView rl_surroundCar;
    private View nullview;
    private EditText search_home;
    private SmartRefreshLayout swipe;
    private int visitPosition;
    private SurroundAdapter surroundAdapter;
    private Request request;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private final int SUCCESS = 1;
    private final int FAIL = -1;
    private final int COLLECTSUCCESS = 2;
    private final int COLLECTFAIL = -2;
    private final int CANCEL_COLLECTSUCCESS = 3;
    private final int CANCEL_COLLECTFAIL = -3;
    private int PageNum = 1; //第一页
    private int PageNumAdd = 1; //
    private String longitude;//经度
    private String latitude;//纬度
    private String count = "-1";

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    if(parkingLots.size()!=0)
                    {
                        nullview.setVisibility(View.INVISIBLE);
                        rl_surroundCar.setVisibility(View.VISIBLE);
                    }else
                    {
                        nullview.setVisibility(View.VISIBLE);
                        rl_surroundCar.setVisibility(View.INVISIBLE);
                    }
                    if (count.equals(String.valueOf(parkingLots.size()))) {
                        pageSize = String.valueOf(parkingLots.size());
                        Log.d("handleMessages", "handleMessage: " + pageSize);
                        swipe.setLoadmoreFinished(true);
                    } else {
                        swipe.setLoadmoreFinished(false);
                    }

                    surroundAdapter.notifyDataSetChanged();
                    surroundAdapter.setOnItemClick(new SurroundAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(ParkingLot items, int id) {

                        }

                        @Override
                        public void OnItemClick(ParkingLot items) {
                            Intent intent = new Intent(getContext(), MapActivity.class);
                            Log.d("OnItemClick", "OnItemClick: " + items.getPark_longitude());
                            Log.d("OnItemClick", "OnItemClick: " + items.getPark_latitude());
                            intent.putExtra(EXTRA_LOG, items.getPark_longitude());
                            intent.putExtra(EXTRA_PARNAME, items.getPark_name());
                            intent.putExtra(EXTRA_ADDRESS, items.getPark_address());
                            intent.putExtra(EXTRA_LAT, items.getPark_latitude());
                            intent.putExtra(EXTRA_ID, items.getPark_id());
                            startActivity(intent);
                        }

                        @Override
                        public void OnItemCollectClick(ParkingLot items) {
                            if (items.getPark_collect().equals("0")) {
                                toCollect(items);
                            } else {
                                toCancelCollect(items);
                            }

                        }

                        @Override
                        public void OnItemClickToDetail(ParkingLot items) {

                        }

                        @Override
                        public void OnItemClickToCommend(ParkingLot items) {
                            toRecommend(items);
                        }

                        @Override
                        public void OnItemClick2(ParkingLot items, int id, Boolean Sch, SwitchButton switchButton) {

                        }
                    });
                    // Toast.makeText(getContext(), "取消分享成功！！！", Toast.LENGTH_SHORT).show();
                    break;
                case COLLECTSUCCESS:
                    getData(false);
                    Toast.makeText(getContext(), "收藏成功！！！", Toast.LENGTH_SHORT).show();
                    break;
                case COLLECTFAIL:
                    Toast.makeText(getContext(), "收藏失败！！！", Toast.LENGTH_SHORT).show();
                    break;
                case CANCEL_COLLECTSUCCESS:
                    getData(false);
                    Toast.makeText(getContext(), "取消收藏成功！！！", Toast.LENGTH_SHORT).show();
                    break;
                case CANCEL_COLLECTFAIL:
                    Toast.makeText(getContext(), "取消收藏失败！！！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    private void toCancelCollect(ParkingLot parkingLot) {
        RequestBody requestBody = new FormBody.Builder()
                .add("parkId", parkingLot.getPark_id())
                .add("userId", LoginActivity.ID).
                        build();
        request = new Request.Builder().
                url(Constants.CancelCollectParklot).
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
                            handler.sendEmptyMessage(CANCEL_COLLECTFAIL);
                        } else {
                            handler.sendEmptyMessage(CANCEL_COLLECTSUCCESS);
                        }
                    } else {
                        handler.sendEmptyMessage(CANCEL_COLLECTFAIL);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void toCollect(ParkingLot parkingLot) {
        RequestBody requestBody = new FormBody.Builder()
                .add("parkId", parkingLot.getPark_id())
                .add("userId", LoginActivity.ID).
                        build();
        request = new Request.Builder().
                url(Constants.CollectParklot).
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
                            handler.sendEmptyMessage(COLLECTFAIL);
                        } else {
                            handler.sendEmptyMessage(COLLECTSUCCESS);
                        }
                    } else {
                        handler.sendEmptyMessage(COLLECTFAIL);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private List<ParkingLot> parkingLots = new ArrayList<>();
    private String pageSize = "2";

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
        initLocationOption();
        rl_surroundCar.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                surroundAdapter.changeMoreStatus(SurroundAdapter.PULLUP_LOAD_MORE);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (visitPosition + 1) == surroundAdapter.getItemCount()) {
                    if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                        // Toast.makeText(getContext(),"拉到底了",Toast.LENGTH_SHORT).show();
                        surroundAdapter.changeMoreStatus(SurroundAdapter.LOADING_MORE);
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rl_surroundCar.getLayoutManager();
                visitPosition = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
        iv_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapActivity.class);
                intent.putExtra(EXTRA_MYADDRESS, "-1");
                startActivity(intent);
            }
        });
        getData(false);
        surroundAdapter = new SurroundAdapter(getContext(), parkingLots);
        rl_surroundCar.setAdapter(surroundAdapter);
    }

    private void initData(@NonNull View view) {
        iv_map = view.findViewById(R.id.iv_map);
        search_home = view.findViewById(R.id.search_home);
        swipe = (SmartRefreshLayout) view.findViewById(R.id.swipe);
        rl_surroundCar = (RecyclerView) view.findViewById(R.id.rl_surroundCar);
        nullview = (View) view.findViewById(R.id.nullview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rl_surroundCar.setLayoutManager(linearLayoutManager);
        //getData();
        // text(parkingLots);
        swipe.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                PageNum = 1; //第一页
                if (count.equals(String.valueOf(parkingLots.size()))) {
                    pageSize = count;
                }
                getData(false);
                refreshlayout.finishRefresh();
            }
        });
        swipe.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                PageNumAdd++;
                PageNum = PageNumAdd;
                getData(true);
                refreshlayout.finishLoadmore();
            }
        });


//        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                       getData();
//                    }
//                }, 800);
//            }
//        });
        search_home.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    toSearch(search_home.getText().toString().trim());
                    hideKeyboard(search_home);
                    return true;
                }
                return false;
            }
        });

    }

    /**
     * 隐藏软键盘
     *
     * @param view
     */
    public static void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void toSearch(String searchString) {
        // List<ParkingLot> parks = new ArrayList<>();
        for (int i = 0; i < parkingLots.size(); i++) {
            if (!parkingLots.get(i).getPark_name().equals(searchString)) {
                parkingLots.remove(i);
            }
        }

        surroundAdapter.notifyDataSetChanged();
    }

    private void getData(Boolean loadMore) {
        //todo:提示用户输入
        NewsRequest newsRequest = new NewsRequest();
        newsRequest.setDistance("100");//提示用户输入
        newsRequest.setLatitude(latitude);
        newsRequest.setLongitude(longitude);
        newsRequest.setPageNo(String.valueOf(PageNum));
        newsRequest.setPageSize(pageSize);
        newsRequest.setUserId(LoginActivity.ID);

        Log.d("route_class", "getData: " + Constants.FindSharePark + newsRequest.toStringShareParkLot());
        request = new Request.Builder().
                url(Constants.FindSharePark + newsRequest.toStringShareParkLot()).
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
                        Log.d("onResponse_body_class", "onResponse: " + body);
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(body);
                        String state = jsonObject.optString("state");
                        count = jsonObject.optString("count");
                        if (state.equals("0")) {
                            handler.sendEmptyMessage(FAIL);
                        } else {
                            Type jsontype = new TypeToken<BaseResponse<List<ParkingLot>>>() {
                            }.getType();
                            BaseResponse<List<ParkingLot>> newsListResponese
                                    = gson.fromJson(body, jsontype);
                            if (!loadMore) {
                                parkingLots.clear();
                            }
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

    private void text(List<ParkingLot> parkingLots) {
        for (int i = 0; i < 3; i++) {
            ParkingLot parkingLot = new ParkingLot();
            parkingLot.setPark_address("1");
            parkingLot.setPark_distance(12.3);
            parkingLot.setPark_latitude("25.310993");
            parkingLot.setPark_longitude("110.423171");
//            LatLng point = new LatLng(25.320883, 110.423171);
            parkingLot.setPark_name("12");
            parkingLot.setPark_ownerName("12");
            parkingLot.setPark_ownerId(1);
            parkingLot.setPark_price("1");
            parkingLot.setShare("1");
            parkingLot.setStatus("1");
            parkingLot.setParklotImage("http://pic1.win4000.com/wallpaper/8/5804428543900.jpg");
            parkingLots.add(parkingLot);
        }
        parkingLots.get(1).setPark_latitude("25.310888");
        parkingLots.get(1).setPark_longitude("110.423191");
        parkingLots.get(2).setPark_latitude("25.310999");
        parkingLots.get(2).setPark_longitude("110.423991");

    }

    private LocationClient locationClient;

    private void initLocationOption() {
        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        locationClient = new LocationClient(getContext().getApplicationContext());
        //声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        locationOption.setOpenGps(true);
        MyLocationListener myLocationListener = new MyLocationListener();

        locationClient.registerLocationListener(myLocationListener);

        //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("bd09ll");
        //可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(0);
//可选，设置是否需要地址信息，默认不需要
        locationOption.setIsNeedAddress(true);
//可选，设置是否需要地址描述
        locationOption.setIsNeedLocationDescribe(true);

        locationOption.setNeedDeviceDirect(true);

//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);

        locationOption.setIsNeedLocationDescribe(true);

        locationOption.setIsNeedLocationPoiList(true);

        locationOption.SetIgnoreCacheException(false);

        locationOption.setOpenGps(true);


        locationOption.setOpenAutoNotifyMode();

        locationOption.setOpenAutoNotifyMode(3000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT);

        locationClient.setLocOption(locationOption);

        locationClient.start();

    }

    /**
     * 实现定位回调
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            double latitudes = location.getLatitude();
            //获取经度信息
            double longitudes = location.getLongitude();
            longitude = String.valueOf(longitudes);
            latitude = String.valueOf(latitudes);
            Log.d("location_class", "onReceiveLocation: " + latitude);
            Log.d("location_class", "onReceiveLocation: " + longitude);
        }
    }

    private void toRecommend(ParkingLot parkingLot) {
        new XPopup.Builder(getContext())
                .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                .asCustom(new BottomCommentPopup(getContext(), parkingLot.getPark_id())/*.enableDrag(false)*/)
                .show();
    }

}
