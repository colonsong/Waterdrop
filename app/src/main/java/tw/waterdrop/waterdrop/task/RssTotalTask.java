package tw.waterdrop.waterdrop.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import tw.waterdrop.lib.JSON;
import tw.waterdrop.waterdrop.fragment.BlogFragment;

/**
 * Created by colon on 2014/7/22.
 */
public class RssTotalTask extends AsyncTask<String, Integer, String> {
    private JSONObject jObject;
    private final String TAG = "RssTotalTask";
    private JSONArray rss = null;
    private Context mContext;

    public RssTotalTask(Context context)
    {
        mContext = context;
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {

            jObject = new JSON()
                    .getJSONFromURL("http://waterdrop.tw/mobile/blog_total");

        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        try {

            rss = jObject.getJSONArray("contents");
            int mCount = rss.length();

            Log.v(TAG, String.valueOf(BlogFragment.totalRss));
            if (mCount == 1) {
                JSONObject rssRow = (JSONObject) rss.get(0);
                BlogFragment.totalRss = Integer.valueOf(rssRow.getString("total"));
                Log.v(TAG, String.valueOf(BlogFragment.totalRss));
                Toast.makeText(mContext, "總共" + BlogFragment.totalRss + "篇文章",
                       Toast.LENGTH_SHORT).show();

            }

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

}

