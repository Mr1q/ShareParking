package com.example.qjh.comprehensiveactivity.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;


public class MyAddressActivity extends BaseActivity implements View.OnClickListener{
    private Button bt_getlocatioin;
    private static final int BAIDU_READ_PHONE_STATE = 100;//定位权限请求
    private LocationClient locationClient;
    private TextView tv_lat;
    private TextView tv_log;
    private TextView tv_area;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_myaddress);
        SDKInitializer.initialize(this);
        SDKInitializer.setCoordType(CoordType.BD09LL);
        initData();
        bt_getlocatioin.setOnClickListener(this);
        requestPermission();
        initLocationOption();

    }

    private void initData() {
        bt_getlocatioin=(Button)findViewById(R.id.bt_getlocatioin);
        tv_lat=(TextView) findViewById(R.id.tv_lat);
        tv_log=(TextView)findViewById(R.id.tv_log);
        tv_area=(TextView)findViewById(R.id.tv_area);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bt_getlocatioin:
                locationClient.start();
                break;
        }
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
        locationClient = new LocationClient(this);
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

      //  locationClient.start();
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

            double latitude = location.getLatitude();
            //获取经度信息
            double longitude = location.getLongitude();
            tv_lat.setText(String.valueOf(latitude));
            tv_log.setText(String.valueOf(longitude));
            tv_area.setText(String.valueOf(location.getLocationDescribe()));







        }
    }

}
