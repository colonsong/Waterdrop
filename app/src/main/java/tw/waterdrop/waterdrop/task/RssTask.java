package tw.waterdrop.waterdrop.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tw.waterdrop.lib.JSON;
import tw.waterdrop.waterdrop.R;
import tw.waterdrop.waterdrop.adapter.MyBaseAdapter;
import tw.waterdrop.waterdrop.fragment.BlogFragment;

;
/**
 * Created by colon on 2014/7/22.
 */
public class RssTask extends AsyncTask<String, Integer, String> {

    public  final String TAG = "RssTask";
    private Map<String, Object> rssMap;
    private JSONObject jObject;
    private JSONArray rss = null;
    private static boolean isUpdate = false;
    public static ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private Context myContext;
    public RssTask(Context context)
    {
        myContext = context;
    }
    @Override
    protected String doInBackground(String... params) {

        try {

            jObject = new JSON().getJSONFromURL(params[0]);
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

            try {
                String patternStr = "(http[s]?://.*?staticflickr.*?)_.\\.jpg";
                Pattern pattern = Pattern.compile(patternStr);
                for (int i = 0; i < mCount; i++) {
                    JSONObject rssRow = (JSONObject) rss.get(i);
                    rssMap = new HashMap<String, Object>();
                    rssMap.put("image_blank", R.drawable.blank);
                    rssMap.put("title", rssRow.getString("title"));
                    rssMap.put("datetime", rssRow.getString("datetime"));
                    rssMap.put("content", rssRow.getString("content"));

                    Matcher matcher = pattern.matcher(rssRow
                            .getString("content"));

                    while (matcher.find()) {
                        Log.v(TAG, matcher.group(1).toString());
                        rssMap.put("image", matcher.group(1) + "_m.jpg");

                        break;
                    }
                    rssMap.put(
                            "contentNoHtml",
                            rssRow.getString("content").replaceAll(
                                    "(<.*?>)", "")
                    );

                    list.add(rssMap);
                }
                if (isUpdate) {
                    //OTHER RSS LOAD HERE
                    BlogFragment.adapter.notifyDataSetChanged();
                    Toast.makeText(myContext, "讀取完成", Toast.LENGTH_SHORT)
                            .show();

                } else {
                    //ONLY FIRST RSS LOAD COME HERE
                    isUpdate = true;
                    String[] from = new String[]{"image", "title",
                            "datetime", "contentNoHtml"};
                    int[] to = new int[]{R.id.leftListIcon,
                            R.id.leftListText, R.id.textView2,
                            R.id.descriptionText};

                    BlogFragment.adapter = new MyBaseAdapter(myContext , list);
                    BlogFragment.mListView.setAdapter(BlogFragment.adapter);

                    BlogFragment.mListView.post(new Runnable() {
                        public void run() {
                            BlogFragment.drawImage(
                                    BlogFragment.mListView.getFirstVisiblePosition(),
                                    BlogFragment.mListView.getLastVisiblePosition());

                        }
                    });
                }
                BlogFragment.rssDialog.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

}