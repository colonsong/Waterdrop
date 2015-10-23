package tw.waterdrop.waterdrop.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import tw.waterdrop.lib.JSON;
import tw.waterdrop.waterdrop.R;
import tw.waterdrop.waterdrop.fragment.AlbumFragment;


/**
 * Created by colon on 2014/7/22.
 */
public class AlbumTask extends

        AsyncTask<Integer, Integer, String> {
    private String TAG = "AlbumTask";
    private JSONObject jObject;
    private Integer lastVisiblePosition = 0;
    private static boolean isUpdate = false;
    private JSONArray album = null;
    private Context my_context;
    private AlbumFragment albumFragment;
    public AlbumTask(AlbumFragment albumFragment)
    {
        my_context  =  albumFragment.getActivity();
        this.albumFragment = albumFragment;
    }
    @Override
    protected String doInBackground(Integer... data) {




        try {
            jObject = new JSON()
                    .getJSONFromURL("http://waterdrop.tw/mobile/get_album_by_rand");
        } catch (IOException e) {

            e.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onPostExecute(String jsonString) {


        Log.v(TAG, "onPostExecute" + jObject.toString());
        try {
            album = jObject.getJSONArray("contents");
        } catch (JSONException e1) {

            e1.printStackTrace();
        }
        int mCount = album.length();

        try {

            for (int i = 0; i < mCount; i++) {

                JSONObject rssRow = (JSONObject) album.get(i);
                Map<String, Object> albumMap = new HashMap<String, Object>();
                albumMap.put("pic_blank", R.drawable.blank);
                albumMap.put("pic", rssRow.get("pic"));
                albumFragment.getAlbumlist().add(albumMap);

            }
            AlbumFragment.baseAdapter.notifyDataSetChanged();




        } catch (JSONException e) {

            e.printStackTrace();
        }

    }


}