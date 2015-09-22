package tw.waterdrop.waterdrop.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by colon on 2015/3/15.
 */
public class BasicTestService extends Service {
    private final String LOG_TAG = "Service demo";


    public class LocalBinder extends Binder {
        public BasicTestService getService(){
            return BasicTestService.this;
        }

    }

    private LocalBinder mLocBin = new LocalBinder();


    public void myMethod()
    {
        Log.d(LOG_TAG,"myMethod");
        Toast.makeText(this, "myMethod", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG,"onCreate");



    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG,"onStartCommand");
        Toast.makeText(this, "onStartCommand", Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG,"onStartCommand @@");

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(LOG_TAG,"onUnbind");
        Toast.makeText(this, "onUnbind", Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "onBind", Toast.LENGTH_LONG).show();
        Log.d(LOG_TAG,"onBind");
        return mLocBin;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "onDestroy", Toast.LENGTH_LONG).show();
        Log.d(LOG_TAG, "onDestroy");

        super.onDestroy();
    }
}
