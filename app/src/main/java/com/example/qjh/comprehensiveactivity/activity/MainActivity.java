package com.example.qjh.comprehensiveactivity.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qjh.comprehensiveactivity.R;

import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket bluetoothSocket = null;
    //唯一的标识
    private static final UUID MY_UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //    private String blueAddress = "00:87:63:12:37:35";//蓝牙模块的MAC地址
    private String blueAddress = "98:D3:31:F7:34:F8";//蓝牙模块的MAC地址
    private Button send;
    private EditText input;
    private OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(blueAddress);
        send = findViewById(R.id.send);
        input = findViewById(R.id.input);

        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
            mBluetoothAdapter.cancelDiscovery();
            bluetoothSocket.connect();
            Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    outputStream = bluetoothSocket.getOutputStream();
                    byte[] buffer;
                    String message = input.getText().toString();
                    buffer = message.getBytes();
                    outputStream.write(buffer);
                    Toast.makeText(MainActivity.this, "发送成功", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.d("12", "12");

            }
        });

    }
}
//    /**
//     * 检查权限
//     */
//    private void checkPermissions() {
//        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
//        List<String> permissionDeniedList = new ArrayList<>();
//        for (String permission : permissions) {
//            int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
//            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//                onPermissionGranted(permission);
//            } else {
//                permissionDeniedList.add(permission);
//            }
//        }
//        if (!permissionDeniedList.isEmpty()) {
//            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
//            ActivityCompat.requestPermissions(this, deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
//        }
//    }
//
//    /**
//     * 权限回调
//     * @param requestCode
//     * @param permissions
//     * @param grantResults
//     */
//    @Override
//    public final void onRequestPermissionsResult(int requestCode,
//                                                 @NonNull String[] permissions,
//                                                 @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case REQUEST_CODE_PERMISSION_LOCATION:
//                if (grantResults.length > 0) {
//                    for (int i = 0; i < grantResults.length; i++) {
//                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                            onPermissionGranted(permissions[i]);
//                        }
//                    }
//                }
//                break;
//        }
//    }
//    /**
//     * 开启GPS
//     * @param permission
//     */
//    private void onPermissionGranted(String permission) {
//        switch (permission) {
//            case Manifest.permission.ACCESS_FINE_LOCATION:
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
//                    new AlertDialog.Builder(this)
//                            .setTitle("提示")
//                            .setMessage("当前手机扫描蓝牙需要打开定位功能。")
//                            .setNegativeButton("取消",
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            finish();
//                                        }
//                                    })
//                            .setPositiveButton("前往设置",
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                                            startActivityForResult(intent, REQUEST_CODE_OPEN_GPS);
//                                        }
//                                    })
//
//                            .setCancelable(false)
//                            .show();
//                } else {
//                    //GPS已经开启了
//                }
//                break;
//        }
//    }
//    /**
//     * 蓝牙是否打开   true为打开
//     * @return
//     */
//    public boolean isBlueEnable(){
//        return isSupportBlue() && mBluetoothAdapter.isEnabled();
//    }
//    /**
//     * 蓝牙是否打开   true为打开
//     * @return
//     */
//    public boolean isBlueEnable(){
//        return isSupportBlue() && mBluetoothAdapter.isEnabled();
//    }
//    /**
//     * 自动打开蓝牙（同步）
//     * 这个方法打开蓝牙会弹出提示
//     * 需要在onActivityResult 方法中判断resultCode == RESULT_OK  true为成功
//     */
//    public void openBlueSync(Activity activity, int requestCode){
//        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//        activity.startActivityForResult(intent, requestCode);
//    }
//    /**
//     * 扫描的方法 返回true 扫描成功
//     * 通过接收广播获取扫描到的设备
//     * @return
//     */
//    public boolean scanBlue(){
//        if (!isBlueEnable()){
//            Log.e(TAG, "Bluetooth not enable!");
//            return false;
//        }
//
//        //当前是否在扫描，如果是就取消当前的扫描，重新扫描
//        if (mBluetoothAdapter.isDiscovering()){
//            mBluetoothAdapter.cancelDiscovery();
//        }
//
//        //此方法是个异步操作，一般搜索12秒
//        return mBluetoothAdapter.startDiscovery();
//    }

