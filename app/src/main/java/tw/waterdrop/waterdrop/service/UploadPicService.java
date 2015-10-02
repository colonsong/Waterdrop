package tw.waterdrop.waterdrop.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import tw.waterdrop.lib.JSON;
import tw.waterdrop.waterdrop.R;
import tw.waterdrop.waterdrop.activity.MainActivity;
import tw.waterdrop.waterdrop.util.MultipartUtility;

/**
 * Created by colon on 2015/3/15.
 */
public class UploadPicService extends IntentService {
    private final String TAG = "UploadPicService";

    public UploadPicService() {
        super(CheckArticleService.class.getName());
        //setIntentRedelivery(true);
        Log.v(TAG,"constructor");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");
        final String path = intent.getStringExtra("path");
        String charset = "UTF-8";
        File uploadFile1 = new File(path);


        String requestURL = "http://waterdrop.tw/album/upload/419/";

        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);

            multipart.addHeaderField("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
            multipart.addHeaderField("Host", "waterdrop.tw");

            multipart.addHeaderField("Origin", "http://waterdrop.tw");
            multipart.addFormField("is_mobile", "true");

            multipart.addFilePart("files", uploadFile1);

            List<String> response = multipart.finish();

            Log.v(TAG,"UPLOAD SERVER REPLIED:");
            Log.v(TAG,"UPLOAD res:" + response.toString());
            notification(path);
            //  Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();
        } catch (IOException ex) {
            Log.v(TAG, "UPLOAD exception" + ex.toString());
        }

    }

    private void notification(String path)
    {
        final int notifyID = (int)(Math.random()*1000+1); // 通知的識別號碼
       // final int progressMax = 100; // 進度條的最大值，通常都是設為100。若是設為0，且indeterminate為false的話，表示不使用進度條
       // final int progress = 50; // 進度值
       // final boolean indeterminate = true; // 是否為不確定的進度，如果不確定的話，進度條將不會明確顯示目前的進度。若是設為false，且progressMax為0的話，表示不使用進度條

        Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle(); // 建立BigPictureStyle
        bigPictureStyle.setBigContentTitle("滴一滴水上傳了新照片"); // 當BigPictureStyle顯示時，用BigPictureStyle的setBigContentTitle覆蓋setContentTitle的設定
        bigPictureStyle.setSummaryText(path);

        BitmapFactory.Options options;
        options = new BitmapFactory.Options();
        options.inScaled = true;
        options.inSampleSize = 8;
        options.inJustDecodeBounds = false;
        final Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        bigPictureStyle.bigPicture(bitmap); // 設定BigPictureStyle的大圖片

        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
        final Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(R.drawable.app_icon).setContentTitle("滴一滴水上傳了新照片").setContentText(path).setStyle(bigPictureStyle).setDefaults(Notification.DEFAULT_ALL).build(); // 建立通知
        notificationManager.notify(notifyID, notification); // 發送通知
    }

}
