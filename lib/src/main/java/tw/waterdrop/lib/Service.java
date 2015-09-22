package tw.waterdrop.lib;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

/**
 * Created by colon on 15/4/7.
 */
public class Service {
    private static final String TAG  = "Service";
    public static boolean isServiceRunning(Context context, String service_Name) {
        ActivityManager manager =
                (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            Log.d(TAG, "isServiceRunning " + service.service.getClassName());
            if (service_Name.equals(service.service.getClassName()))
            {
                Log.d(TAG,"isServiceRunning GOT !!!" +service.service.getClassName() );
                return true;
            }
        }
        return false;
    }
}
