package com.ben.p_01_aidl_server;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.support.v4.os.IResultReceiver;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.concurrent.locks.LockSupport;

public class TestAIDLService extends Service {

    private static final String TAG = "TestAIDLService";

    public TestAIDLService() {
        Log.d(TAG, "TestAIDLService()");
    }

    private static final int MESSAGE_ID = 8642;

    private Thread mBinderT = null;

    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "curThread = " + Thread.currentThread().getName() + ", onCreate()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(TAG, "curThread = " + Thread.currentThread().getName() + ", onBind() intent = " + intent);
        //主线程延迟处理
        long time = (long) (Math.random() * 2 + 3) * 1000;
        mHandler.postDelayed(() -> {
            Log.d(TAG, "curThread = " + Thread.currentThread().getName() + ", postDelayed() unpark > time = " + time);
            LockSupport.unpark(mBinderT);
        }, time);

        return passObj;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "curThread = " + Thread.currentThread().getName() + "onBind() intent = " + intent);
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    private IBinder mBinder = new IPassBasicType.Stub() {
        @Override
        public int passIntType(int data) throws RemoteException {
            mBinderT = Thread.currentThread();
            Log.d(TAG, "curThread = " + Thread.currentThread().getName() + ",PID = " + Process.myPid() + "收到" + Binder.getCallingPid() + "的数据： data = " + data);
            //处理请求
            Log.d(TAG, "curThread = " + Thread.currentThread().getName() + "处理请求...");

            data++;

            LockSupport.park();

            Log.d(TAG, "curThread = " + Thread.currentThread().getName() + "处理完成");

            return data;
        }

        @Override
        public String passStrType(String data) throws RemoteException {
            StringBuilder append = new StringBuilder().append(data).append(data).append(data);
            return new String(append);
        }
    };


    private IBinder passObj = new IPassObjType.Stub() {
        @Override
        public String passPerson(Person person) throws RemoteException {
            String name = person.getName();
            int age = person.getAge();
            Log.d(TAG, "passPerson() name = " + name + ", age = " + age);
            person.setAge(++age);
            return person.toString();
        }
    };
}