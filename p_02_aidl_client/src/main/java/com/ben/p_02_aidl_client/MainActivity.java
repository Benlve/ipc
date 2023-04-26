package com.ben.p_02_aidl_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ben.p_01_aidl_server.IPassBasicType;
import com.ben.p_01_aidl_server.IPassObjType;
import com.ben.p_01_aidl_server.Person;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final String PACKAGE_NAME = "com.ben.p_01_aidl_server";
    private static final String CLASS_NAME = "com.ben.p_01_aidl_server.TestAIDLService";

    private Button aidlGetBinder, aidlSendBasicType, aidlSendParcelType;
    private TextView dataSend, dataCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aidlGetBinder = findViewById(R.id.aidl_get_binder);
        aidlSendBasicType = findViewById(R.id.aidl_send_basic_type);
        aidlSendParcelType = findViewById(R.id.aidl_send_parcel_type);

        aidlGetBinder.setOnClickListener(this);
        aidlSendBasicType.setOnClickListener(this);
        aidlSendParcelType.setOnClickListener(this);

        dataSend = findViewById(R.id.data_send);
        dataCallback = findViewById(R.id.data_callback);


    }

    private IPassBasicType passBasicType;
    private IPassObjType passObjType;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "cuThread = " + Thread.currentThread().getName() +
                    ", onServiceConnected() name = " + name + ", service = " + service.getClass().getName());

            passBasicType = IPassBasicType.Stub.asInterface(service);
            passObjType = IPassObjType.Stub.asInterface(service);
            Log.d(TAG, "passBasicType = " + passBasicType.getClass().getName() +
                    ", passObjType = " + passObjType.getClass().getName());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceConnected() name = " + name);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aidl_get_binder:
                getBinder();
                break;
            case R.id.aidl_send_basic_type:
                sendBasic();
                break;
            case R.id.aidl_send_parcel_type:
                sendParcel();
                break;
        }
    }

    private void getBinder() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(PACKAGE_NAME, CLASS_NAME));
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    private void sendBasic() {
        if (passBasicType instanceof IPassBasicType) {
            try {
                dataSend.setText("发送int : 你好我是客户端 , String : 你好我是客户端");
                int i = passBasicType.passIntType(2022);
                String str = passBasicType.passStrType("你好我是客户端");

                dataCallback.setText("binder返回 : int : " + i + ", String : " + str);
                Log.d(TAG, "i = " + i + ",str= " + str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendParcel() {
        if (passObjType instanceof IPassObjType) {
            Person p = new Person("Tom", 23);
            try {
                dataSend.setText("发送Person : p : " + p);
                String personStr = passObjType.passPerson(p);
                dataCallback.setText("binder返回 : String : " + personStr);
                Log.d(TAG, "personStr = " + personStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}