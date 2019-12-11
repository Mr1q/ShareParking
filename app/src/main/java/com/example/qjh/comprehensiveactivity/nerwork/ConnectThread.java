package com.example.qjh.comprehensiveactivity.nerwork;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.qjh.comprehensiveactivity.nerwork.ConnectedThread;
import com.example.qjh.comprehensiveactivity.utils.NetUtils;

import java.io.IOException;
import java.util.UUID;


/**
 * Created by Administrator on 2016/4/29 0029.
 * 客户端配对并连接
 */
public class ConnectThread extends Thread {

    private final BluetoothSocket mmSocket;

    private final BluetoothDevice mmDevice;
    private Handler mhandler;



    public ConnectThread(BluetoothDevice device, Handler mhandler) {
              this.mhandler = mhandler;
        BluetoothSocket tmp = null;
        mmDevice = device;
        try {
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(NetUtils.UUID));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //连接
        mmSocket = tmp;

    }


    public void run() {
        try {
               mmSocket.connect(); //尝试连接
        } catch (IOException connectException) {
            // 连接失败，发送信息
            Message msg = new Message();
            msg.what = NetUtils.CONNECT_FAILED;
            Bundle bundle = new Bundle();
            bundle.putString(NetUtils.CONNECT_FAILED_REASON, connectException.getMessage());
            msg.setData(bundle);
            mhandler.sendMessage(msg);
            try {
                mmSocket.close();
            } catch (IOException closeException) {

            }
            return;
        }

        Message msg = new Message();
        msg.what = NetUtils.CONNECT_FINISHED;
        mhandler.sendMessage(msg);
        //发送成功
        // Do work to manage the connection (in a separate thread)

      NetUtils.connectedThread = new ConnectedThread(mmSocket);
//        NetUtils.connectedThread.start();

    }


    /**
     * Will cancel an in-progress connection, and close the socket
     */

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}