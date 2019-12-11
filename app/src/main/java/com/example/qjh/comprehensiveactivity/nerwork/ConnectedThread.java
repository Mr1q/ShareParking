package com.example.qjh.comprehensiveactivity.nerwork;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;




public class ConnectedThread extends Thread {

    private final BluetoothSocket mmSocket;
    private final OutputStream mmOutStream;

    public ConnectedThread(BluetoothSocket socket) {
        mmSocket = socket;
        OutputStream tmpOut = null;
        // Get the input and output streams, using temp objects because

        // member streams are final

        try {
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
        }

        mmOutStream = tmpOut;

    }

    /* Call this from the main Activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
        }
    }




    /* Call this from the main Activity to shutdown the connection */

    public void cancel() {

        try {
            mmSocket.close();
        } catch (IOException e) {
        }
    }

}