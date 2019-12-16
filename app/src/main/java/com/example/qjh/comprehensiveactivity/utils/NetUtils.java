package com.example.qjh.comprehensiveactivity.utils;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

import com.example.qjh.comprehensiveactivity.activity.ConnectActivity;
import com.example.qjh.comprehensiveactivity.beans.Car;
import com.example.qjh.comprehensiveactivity.nerwork.ConnectedThread;

public class NetUtils  {


    private static Toast toast;
    /*
   连接蓝牙串口的UUID
    */
    public final static String UUID = "00001101-0000-1000-8000-00805F9B34FB";
        /*
    连接完成
     */
    public final static int CONNECT_FINISHED = 10010;
    /*
    连接失败
     */
    public final static int CONNECT_FAILED = 10011;
    public final static String CONNECT_FAILED_REASON = "CONNECT_FAILED_REASON";

    public static void showToast(Context context, String str) {
        if (toast == null) {
            toast = Toast.makeText(context, str, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else
            toast.setText(str);
        toast.show();
    }

    /*
    蓝牙发送数据网络层
     */
    public static ConnectedThread connectedThread;
    /*
    调试TAG
     */
    public final static String TAG = "lzh_car";
    /*
    控制命令
     */
    private final static String COMMAD_FORWORD = "W";  //前进
    private final static String COMMAD_BACK = "B";  //后退
    private final static String COMMAD_LEFT = "L"; //左移动
    private final static String COMMAD_RIGHT = "R";//右移动
    private final static String COMMAD_STOP = "S";//停止

    public static void GO_FOWRD() {
        connectedThread.write(COMMAD_FORWORD.getBytes());
    }

    public static void GO_BACK() {
        connectedThread.write(COMMAD_BACK.getBytes());
    }


    public static void GO_LEFT() {
        connectedThread.write(COMMAD_LEFT.getBytes());

    }

    public static void GO_RIGHT() {
        connectedThread.write(COMMAD_RIGHT.getBytes());

    }

    public static void GO_STOP() {
        connectedThread.write(COMMAD_STOP.getBytes());

    }

    public  static  void  Cancel()
    {
        connectedThread.cancel();
    }

    /*
    读取消息的Handler
     */
    public static Handler readHandler;
    public final static int MESSAGE_READ = 2000;

    /*
    HANDLE C
     */
    public static void HandlerCommand(String str, Context context) {
        if (connectedThread == null) {
            showToast(context, "小车未连接！");
            return;
        }
        switch (str)
        {
            case "W":
                GO_FOWRD();
                break;
            case "B":
                GO_BACK();
                break;
            case "L":
                GO_LEFT();
                break;
            case "R":
                GO_RIGHT();
                break;
            case "S":
                GO_STOP();
                break;

        }
    }
}
