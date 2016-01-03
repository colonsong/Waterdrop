package tw.waterdrop.waterdrop.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ColonPc on 2015/12/26.
 */
public class ShutdownBroadcastReceiver extends BroadcastReceiver {
    public final String TAG = "ShutDownReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "shutdown now");

        String action = intent.getAction();
        Log.d("tag", "action:" + action);

        //Intent killIntent = new Intent(AppContext.INTENT_INTENT_KILL_ACTIVITY);
       // killIntent.setType(AppContext.INTENT_TYPE_KILL_ACTIVITY);
       // context.sendBroadcast(killIntent);
    }
}
