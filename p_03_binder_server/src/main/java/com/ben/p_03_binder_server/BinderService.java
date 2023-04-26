package com.ben.p_03_binder_server;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BinderService extends Service {

    private static final String TAG = "BinderService";

    private static final int INVOKE_CODE = 1980;

    public BinderService() {
        Log.d(TAG, "BinderService()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(TAG, "onBind() intent = " + intent);
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind() intent = " + intent);
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    private class MyBinder extends Binder {
        @Override
        protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
            if (code == INVOKE_CODE) {
                //1.与客户端约定好写入顺序，按照客户端写入顺序读取
                int dataInt = data.readInt();
                String dataString = data.readString();
                Log.d(TAG, "onTransact() dataInt = " + dataInt + ", dataString = " + dataString);

                //2.处理请求
                //...
                try {
                    Thread.sleep((long) ((Math.random() * 2 + 3) * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //3.将处理的结果返回
                reply.writeString("我是服务端，您的请求已经处理完毕~");

                return true;
            }

            return super.onTransact(code, data, reply, flags);
        }
    }
}