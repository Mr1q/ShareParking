package com.example.qjh.comprehensiveactivity.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.adapter.DeviceAdapter;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;
import com.example.qjh.comprehensiveactivity.nerwork.ConnectThread;
import com.example.qjh.comprehensiveactivity.utils.NetUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ConnectActivity extends BaseActivity {
    private static final String DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private BluetoothAdapter mBtAdapter;
    private RecyclerView lv_ready;
    private RecyclerView lv_already;
    private LinearLayout ly_search;
    private Button bt_search;
    private List<HashMap<String, String>> devicesList = new ArrayList<HashMap<String, String>>();
    private List<HashMap<String, String>> devicesList2 = new ArrayList<HashMap<String, String>>();
    private DeviceAdapter deviceAdapter;
    private DeviceAdapter deviceAdapter2;
    private Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case  NetUtils.CONNECT_FINISHED:
                    Toast("连接成功");
                    setResult(RESULT_OK);
                    finish();
                break;
                case NetUtils.CONNECT_FAILED:
                    Toast("连接失败"+msg.getData().getString(NetUtils.CONNECT_FAILED_REASON));
                    break;

            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_devicelist);
        initData();
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();


        // 将匹配的设备添加到列表显示
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("name", device.getName());
                hashMap.put("mac", device.getAddress());
                devicesList2.add(hashMap);
                deviceAdapter.notifyDataSetChanged();
                Log.d("123", "onCreate: " + device.getName() + "\n"
                        + device.getAddress());
            }
        } else {
            Log.d("123", "未搜到任何设备: ");
            // mPairedDevices.add("未搜到任何设备");
        }


    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 发现设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device;
                // 获取蓝牙设备
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 已匹配的跳过
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("name", device.getName());
                    map.put("mac", device.getAddress());
                    devicesList.add(map);
                    deviceAdapter.notifyDataSetChanged();
                    //   mNewDevices.add(device.getName() + "\n" + device.getAddress());
                    Log.d("blue_tooth", "onReceive: " + device.getName() + "\n" + device.getAddress());
                }
            }
            //蓝牙搜索完成
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                // setProgressBarIndeterminateVisibility(false);
                ly_search.setVisibility(View.INVISIBLE);
                Toast("搜索完成");
//                scanButton.setText("搜索");
//                if (mNewDevices.getCount() == 0)
//                {
//                    mNewDevices.add("未搜到任何设备");
//                }
            }
        }
    };

    /**
     * 蓝牙搜索方法
     */
    private void BtDiscovery() {
        // 当正在搜索，则停止搜索
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
        // 否则开启蓝牙搜索
        else {
            // scanButton.setText("正在搜索(点击停止)");
            //清空新搜索到设备的列表以免重复添加
            //  mNewDevices.clear();
            // 开始搜索设备
            mBtAdapter.startDiscovery();
        }
    }


    private void initData() {
        lv_ready = (RecyclerView) findViewById(R.id.lv_ready);
        lv_already = (RecyclerView) findViewById(R.id.lv_already);
        ly_search = (LinearLayout) findViewById(R.id.ly_search);
        bt_search = (Button) findViewById(R.id.bt_search);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BtDiscovery();
                ly_search.setVisibility(View.VISIBLE);

            }
        });

        // 设置列表布局文件
        deviceAdapter = new DeviceAdapter(devicesList, this);
        deviceAdapter2 = new DeviceAdapter(devicesList2, this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        LinearLayoutManager linearLayoutManager2=new LinearLayoutManager(this);
        lv_ready.setLayoutManager(linearLayoutManager);
        lv_already.setLayoutManager(linearLayoutManager2);

        lv_ready.setAdapter(deviceAdapter);
        lv_already.setAdapter(deviceAdapter2);
        deviceAdapter.setOnItemClick(new DeviceAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(HashMap<String, String> items) {
                // 关闭蓝牙搜索以便进行蓝牙连接
            mBtAdapter.cancelDiscovery();
            BluetoothDevice device = mBtAdapter.getRemoteDevice(items.get("mac"));
            new ConnectThread(device,handler).start();
            Toast(items.get("mac")+" "+items.get("name"));
            }
        });
        deviceAdapter2.setOnItemClick(new DeviceAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(HashMap<String, String> items) {
                // 关闭蓝牙搜索以便进行蓝牙连接
                mBtAdapter.cancelDiscovery();
                BluetoothDevice device = mBtAdapter.getRemoteDevice(items.get("mac"));
                new ConnectThread(device,handler).start();
                Toast(items.get("mac")+" "+items.get("name"));
            }
        });
        // 注册蓝牙搜索广播
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            Toast.makeText(this, "设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (!mBtAdapter.isEnabled()) {
            // 弹出对话框提示用户是后打开
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1);
            // 不做提示，强行打开
            // mBluetoothAdapter.enable();
        }

    }


//        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3)
//        {
//            // 关闭蓝牙搜索以便进行蓝牙连接
//            mBtAdapter.cancelDiscovery();
//            // 列表项名称的最后17个字符为MAC地址
//
//
//            // 将MAC地址通过意图返回UI
//          //  Intent intent = new Intent();
//          //  intent.putExtra(DEVICE_ADDRESS, address);
//          ///  setResult(Activity.RESULT_OK, intent);
//           // finish();
//        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消可能存在的搜索
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }
        // 注销蓝牙广播
        this.unregisterReceiver(mReceiver);
    }
}
