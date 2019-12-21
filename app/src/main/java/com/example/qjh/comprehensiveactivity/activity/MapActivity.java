package com.example.qjh.comprehensiveactivity.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviCommonParams;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRoutePlanManager;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.example.qjh.comprehensiveactivity.Baiduapi.DemoGuideActivity;
import com.example.qjh.comprehensiveactivity.Baiduapi.ForegroundService;
import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.adapter.HistoryAdapter;
import com.example.qjh.comprehensiveactivity.beans.BaseResponse;
import com.example.qjh.comprehensiveactivity.beans.NewsRequest;
import com.example.qjh.comprehensiveactivity.beans.Order;
import com.example.qjh.comprehensiveactivity.beans.ParkingLot;
import com.example.qjh.comprehensiveactivity.constant.Constants;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;
import com.example.qjh.comprehensiveactivity.controler.PagerBottomPopup;
import com.example.qjh.comprehensiveactivity.fragment.SurroundFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lxj.xpopup.XPopup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 地图导航
 */
public class MapActivity extends BaseActivity {
    private static final int BAIDU_READ_PHONE_STATE = 100;//定位权限请求
    private LocationClient locationClient;
    private MapView mapView;
    private FloatingActionButton common_fa_loc;
    private TextView tv_parklotAddress;
    private TextView tv_parkLotName;

    private BaiduMap baiduMap;

    private Request request;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private final int SUCCESS = 1;
    private final int FAIL = -1;
    private List<ParkingLot> parkingLots = new ArrayList<>();

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    //定义Maker坐标点
                    Toast("获取成功");
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromResource(R.mipmap.common_fragment_map_marker);
                    for (int i = 0; i < parkingLots.size(); i++) {
                        LatLng point = new LatLng(Double.valueOf(parkingLots.get(i).getPark_latitude())
                                , Double.valueOf(parkingLots.get(i).getPark_longitude()));
                        OverlayOptions option = new MarkerOptions()
                                .position(point)
                                .icon(bitmap);
                        Bundle mBundle = new Bundle();
                        mBundle.putString("id", parkingLots.get(i).getPark_id());
                        mBundle.putString("price", parkingLots.get(i).getPark_price());
                        mBundle.putString("parkName", parkingLots.get(i).getPark_name());
                        mBundle.putString("lat", parkingLots.get(i).getPark_latitude());
                        mBundle.putString("lot", parkingLots.get(i).getPark_longitude());
                        baiduMap.addOverlay(option);
                        Marker marker = (Marker) baiduMap.addOverlay(option);
                        marker.setExtraInfo(mBundle);
                    }

//                    parkingLots
                    break;
                case FAIL:

                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);
        setContentView(R.layout.common_fragment_map);
        startService(new Intent(this, ForegroundService.class));
        //初始化程序
        intitData();
        Intent intent = getIntent();
        Doing();
        requestPermission();
        initLocationOption();
        getData();

        if (intent != null) {
            if (intent.getStringExtra(SurroundFragment.EXTRA_LAT) != null) {
                LatLng point3 = new LatLng(Double.valueOf(intent.getStringExtra(SurroundFragment.EXTRA_LAT))
                        , Double.valueOf(intent.getStringExtra(SurroundFragment.EXTRA_LOG)));
                String address = intent.getStringExtra(SurroundFragment.EXTRA_ADDRESS);
                String parkname = intent.getStringExtra(SurroundFragment.EXTRA_PARNAME);
                String id = intent.getStringExtra(SurroundFragment.EXTRA_ID);
                tv_parklotAddress.setText(address);
                tv_parkLotName.setText(parkname);
//               OverlayOptions option3 = new MarkerOptions()
//                       .position(point3)
//                       .icon(bitmap);
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(point3).zoom(18.0f);
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

            }

            //地图点击
            if (intent.getStringExtra(SurroundFragment.EXTRA_MYADDRESS) != null) {

                locationClient.start();
            }
            //导航
            if (intent.getStringExtra(BookParkLotActivity.EXTRA_NAVI) != null) {

                Tonavigate(MyAddressActivity.log, MyAddressActivity.lat,
                        intent.getStringExtra("lot"), intent.getStringExtra("lat"));
//               locationClient.start();
            }


        }

        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            //marker被点击时回调的方法
            //若响应点击事件，返回true，否则返回false
            //默认返回false
            @Override
            public boolean onMarkerClick(Marker marker) {

                Bundle bundle = marker.getExtraInfo();
                Toast(bundle.getString("id"));
                Intent intent1 = new Intent(MapActivity.this, BookParkLotActivity.class);

                intent1.putExtra("price", bundle.getString("price"));
                intent1.putExtra("parkId", bundle.getString("id"));
                intent1.putExtra("parkName", bundle.getString("parkName"));
                intent1.putExtra("lat", bundle.getString("lat"));
                intent1.putExtra("lot", bundle.getString("lot"));
                startActivityForResult(intent1, 1);

                // startActivityForResult(intent1, 1);
                //TODO   这里要设置OK

//                if(bundle.getInt("id")==1)
//                {
//                    Toast("1");
//                }
                //todo这里要获取信息
//                Intent intent1=new Intent(MapActivity.this,BookParkLotActivity.class);
//                startActivity(intent1);
//                new XPopup.Builder(MapActivity.this)
//                        .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
//                        .asCustom(new PagerBottomPopup(MapActivity.this))
//                        .show();

                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                Toast("123");
                if (resultCode == RESULT_OK) {
                    Toast("1234");
                    Tonavigate(MyAddressActivity.log, MyAddressActivity.lat,
                            data.getStringExtra("lot"), data.getStringExtra("lat"));
                }
                break;
        }
    }

    private void intitData() {
        mapView = findViewById(R.id.mapview);
        common_fa_loc = (FloatingActionButton) findViewById(R.id.common_fa_loc);
        tv_parklotAddress = (TextView) findViewById(R.id.tv_parklotAddress);
        tv_parkLotName = (TextView) findViewById(R.id.tv_parkLotName);
    }

    private void Doing() {
        common_fa_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LatLng point = new LatLng(25.320883, 110.423171);
//              //  intent.getStringExtra("123");
//                MapStatus.Builder builder = new MapStatus.Builder();
//                builder.target(point).zoom(18.0f);
//                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                locationClient.start();
                Log.d("location_class", "onReceiveLocation: 启动");
                //   Tonavigate();
            }
        });

    }

    private void Tonavigate(String log1, String lat1, String log2, String lat2) {

        BaiduNaviManagerFactory.getBaiduNaviManager().init(this,
                "qjh", "com.example.qjh.comprehensiveactivity",
                new IBaiduNaviManager.INaviInitListener() {

                    @Override
                    public void onAuthResult(int status, String msg) {
                        Log.d("initStart_class", "initStart: " + status);
                        if (0 == status) {
                            Log.d("initStart_class", "initStart: " + "haha");
                            // authinfo = "key校验成功!";
                        } else {
                            //  authinfo = "key校验失败, " + msg;
                        }
                    }

                    @Override
                    public void initStart() {
                        Log.d("initStart_class", "initStart: " + "start");
                    }

                    @Override
                    public void initSuccess() {
                        MapActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {

                                    BNRoutePlanNode sNode = new BNRoutePlanNode.Builder()
                                            .latitude(Double.parseDouble(lat1))
                                            .longitude(Double.parseDouble(log1))
                                            .coordinateType(BNRoutePlanNode.CoordinateType.WGS84)
                                            .build();
                                    BNRoutePlanNode eNode = new BNRoutePlanNode.Builder()
                                            .latitude(Double.parseDouble(lat2))
                                            .longitude(Double.parseDouble(log2))
                                            .coordinateType(BNRoutePlanNode.CoordinateType.WGS84)
                                            .build();

                                    routePlanToNavi(sNode, eNode);
                                }
                            }
                        });
                        Log.d("initStart_class", "initStart: " + "success");
                        // 初始化tts

                    }

                    @Override
                    public void initFailed(int errCode) {
                        Log.d("initStart_class", "initStart: " + "fail");
                    }

                });

    }

    private void routePlanToNavi(BNRoutePlanNode sNode, BNRoutePlanNode eNode) {
        List<BNRoutePlanNode> list = new ArrayList<>();
        list.add(sNode);
        list.add(eNode);

        BaiduNaviManagerFactory.getRoutePlanManager().routeplanToNavi(
                list,
                IBNRoutePlanManager.RoutePlanPreference.ROUTE_PLAN_PREFERENCE_DEFAULT,
                null,
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_START:
                                Toast.makeText(MapActivity.this.getApplicationContext(),
                                        "算路开始", Toast.LENGTH_SHORT).show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_SUCCESS:
                                Toast.makeText(MapActivity.this.getApplicationContext(),
                                        "算路成功", Toast.LENGTH_SHORT).show();
                                // 躲避限行消息
                                Bundle infoBundle = (Bundle) msg.obj;
                                if (infoBundle != null) {
                                    String info = infoBundle.getString(
                                            BNaviCommonParams.BNRouteInfoKey.TRAFFIC_LIMIT_INFO
                                    );
                                    Log.d("OnSdkDemo", "info = " + info);
                                }
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_FAILED:
                                Toast.makeText(MapActivity.this.getApplicationContext(),
                                        "算路失败", Toast.LENGTH_SHORT).show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_TO_NAVI:
                                Toast.makeText(MapActivity.this.getApplicationContext(),
                                        "算路成功准备进入导航", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(MapActivity.this,
                                        DemoGuideActivity.class);

                                startActivity(intent);
                                break;
                            default:
                                // nothing
                                break;
                        }
                    }
                });
    }

    private boolean checkValid(String startPoint, String endPoint) {
        if (TextUtils.isEmpty(startPoint) || TextUtils.isEmpty(endPoint)) {
            return false;
        }

        if (!startPoint.contains(",") || !endPoint.contains(",")) {
            return false;
        }
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        locationClient.stop();
        mapView.onDestroy();
        super.onDestroy();
    }


    /**
     * 请求权限
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) { //判断是否为android6.0系统版本，如果是，需要动态添加权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {// 没有权限，申请权限。
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        BAIDU_READ_PHONE_STATE);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case BAIDU_READ_PHONE_STATE://刚才的识别码
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//用户同意权限,执行我们的操作
                    //  startLocaion();//开始定位
                } else {
                    //用户拒绝之后,当然我们也可以弹出一个窗口,直接跳转到系统设置页面
                    Toast.makeText(this, "未开启定位权限,请手动到设置去开启权限", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 初始化定位参数配置
     */

    private void initLocationOption() {
        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        locationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        locationOption.setOpenGps(true);
        MyLocationListener myLocationListener = new MyLocationListener();
        //注册监听函数
        locationClient.registerLocationListener(myLocationListener);

        //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("bd09ll");
        //可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(0);
        //可选，设置是否需要地址信息，默认不需要
        locationOption.setIsNeedAddress(true);
        //可选，设置是否需要地址描述
        locationOption.setIsNeedLocationDescribe(true);
        //可选，设置是否需要设备方向结果
        locationOption.setNeedDeviceDirect(true);

//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);
//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locationOption.setIsNeedLocationDescribe(true);
//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationOption.setIsNeedLocationPoiList(true);
//可选，默认false，设置是否收集CRASH信息，默认收集
        locationOption.SetIgnoreCacheException(false);
//可选，默认false，设置是否开启Gps定位
        locationOption.setOpenGps(true);

//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode();
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(3000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        locationClient.setLocOption(locationOption);
//开始定位
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        //locationClient.start();

    }

    /**
     * 实现定位回调
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            //获取纬度信息
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return;
            }
            //   navigate(location);
//            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            double latitude = location.getLatitude();
            //获取经度信息
            double longitude = location.getLongitude();
            Log.d("location_class", "onReceiveLocation: " + latitude);
            Log.d("location_class", "onReceiveLocation: " + longitude);
            //   Toast(latitude+" "+longitude);
            MyAddressActivity.lat = String.valueOf(latitude);
            MyAddressActivity.log = String.valueOf(longitude);
            tv_parklotAddress.setText(location.getLocationDescribe());
            tv_parkLotName.setText(String.valueOf(latitude) + String.valueOf(longitude));
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);


            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));


        }
    }

    private void navigate(BDLocation bdLocation) {

        MyLocationData.Builder data = new MyLocationData.Builder();
        data.latitude(bdLocation.getLatitude());
        data.longitude(bdLocation.getLongitude());
        MyLocationData myLocationData = data.build();
        baiduMap.setMyLocationData(myLocationData);

    }


    /**
     * 获取周围数据
     */
    private void getData() {
        NewsRequest newsRequest = new NewsRequest();
        newsRequest.setUserId(LoginActivity.ID);//提示用户输入
        Log.d("route_class", "getData: " + Constants.FindSharePark + newsRequest.toStringShareParkLot());
        request = new Request.Builder().
                url(Constants.FindAllPark + newsRequest.toStringFindAllparklotr()).
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

                        if (state.equals("0")) {
                            handler.sendEmptyMessage(FAIL);
                        } else {
                            Type jsontype = new TypeToken<BaseResponse<List<ParkingLot>>>() {
                            }.getType();
                            BaseResponse<List<ParkingLot>> newsListResponese
                                    = gson.fromJson(body, jsontype);
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
}
