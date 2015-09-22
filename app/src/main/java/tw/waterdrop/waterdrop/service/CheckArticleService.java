package tw.waterdrop.waterdrop.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import tw.waterdrop.lib.JSON;
import tw.waterdrop.waterdrop.R;
import tw.waterdrop.waterdrop.activity.MainActivity;

/**
 * Created by colon on 2015/3/15.
 */
public class CheckArticleService extends IntentService {
    private final String TAG = "CheckArticleService";
    private static final String QUERY_URL = "http://waterdrop.tw/mobile/get_last_article/";
    private int last_article_id = 0;
    private String article_title = "";
    private String article_classify = "";
    private int prev_article_id = 0;
    private JSONObject jsonObject;



    public CheckArticleService() {

        super(CheckArticleService.class.getName());
        setIntentRedelivery(true);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG,"onCreate");

    }
    private void _check_last_article()
    {
        Log.d(TAG,"_check_last_article");
        // Create a client to perform networking


        // Have the client get a JSONArray of data
        // and define how to respond












    }

    private void _push_notification(String title , String content)
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle(title)
                        .setContentText(content);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(2, mBuilder.setAutoCancel(true).build());
    }




    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG,"onHandleIntent");

      //  while(true) {



            //
           // try
            //{
                SharedPreferences data = getSharedPreferences("tw.waterdrop",MODE_PRIVATE);
                prev_article_id = data.getInt("last_article_id",0);
                Log.d(TAG,"prev_article_id:" + prev_article_id);
        try{
            jsonObject = new JSON()
                    .getJSONFromURL("http://waterdrop.tw/mobile/get_last_article");
            JSONArray last_article  = jsonObject.getJSONArray("contents");
            Log.d(TAG,"last_article:" + last_article.toString());
            int mCount = last_article.length();

            if (mCount == 1) {
                JSONObject rssRow = (JSONObject) last_article.get(0);

                last_article_id = Integer.parseInt(rssRow.getString("blogContents_id"));
                Log.d(TAG,"last_article_id:" + last_article_id);
                article_title = rssRow.getString("title");
                article_classify = rssRow.getString("classify");
                SharedPreferences data2 = getSharedPreferences("tw.waterdrop",MODE_PRIVATE);
                data2.edit().putInt("last_article_id",last_article_id).commit();



                if(prev_article_id != last_article_id)
                {
                    _push_notification("摳冷又開始嘴砲惹",article_title + "["+ article_classify+"]");
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
                //SystemClock.sleep(10000);
           // }
           // catch (InterruptedException e) {
             //   e.printStackTrace();
            //}


       // }
              /*
              Log.d(LOG_TAG,"run on timer");

              //get now last article id
              _check_last_article();
              */
                  //每隔1秒


        /*
        try {
            TimeUnit.SECONDS.sleep(300);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/




    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Check Article onDestroy");
        super.onDestroy();

    }
}
