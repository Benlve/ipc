package com.ben.p_04_binder_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final int INVOKE_CODE = 1980;
    private static final String PACKAGE_NAME = "com.ben.p_03_binder_server";
    private static final String CLASS_NAME = "com.ben.p_03_binder_server.BinderService";

    private Button getBinder, sendData;
    private TextView dataSend, dataCallback;

    private IBinder binderProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getBinder = findViewById(R.id.get_binder);
        sendData = findViewById(R.id.send_data);

        getBinder.setOnClickListener(this);
        sendData.setOnClickListener(this);

        dataSend = findViewById(R.id.data_send);
        dataCallback = findViewById(R.id.data_callback);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_binder:
                getBinder();
                break;
            case R.id.send_data:
                sendData();
                break;
        }
    }


    private void getBinder() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(PACKAGE_NAME, CLASS_NAME));
        bindService(intent, conn, BIND_AUTO_CREATE);
    }


    private void sendData() {
        Log.d(TAG, "sendData()");
        if (binderProxy != null) {
            //1.获取发数据和接受返回数据的Parcel
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            Log.d(TAG, "sendData() -> obtain()");
            dataSend.setText("发送数据 -> int : 2023, String : 我是客户端data");
            //2.与服务端规定好读写顺序，两端读写顺序要一致
            data.writeInt(2023);
            data.writeString("我是客户端data");
            try {
                //3.用户态 -> 内核态，当前线程被调起
                binderProxy.transact(INVOKE_CODE, data, reply, 0);
                //4.请求结果
                String returnStr = reply.readString();
                dataCallback.setText("Binder回调数据 -> String : " + returnStr);
                Log.d(TAG, "sendData() returnStr = " + returnStr);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binderProxy = service;
            Log.d(TAG, "onServiceConnected() binderProxy = " + binderProxy);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected()");
        }
    };
}