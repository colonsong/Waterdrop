package tw.waterdrop.waterdrop.task;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import tw.waterdrop.lib.JSON;
import tw.waterdrop.waterdrop.R;
import tw.waterdrop.waterdrop.activity.ListDetail;
/**
 * Created by colon on 2014/7/22.
 */
public class SetMarkerTask extends

        AsyncTask<HashMap<String, String>, Integer, String> {

    private final String TAG = "SetMarkerTask";
    private JSONObject jObject;
    private JSONArray markers = null;
    private Context my_context ;
    public SetMarkerTask(Context context)
    {
        my_context = context;
    }
    @Override
    protected String doInBackground(HashMap<String, String>... data) {

        try {
            jObject = new JSON().getJSONFromURL(data[0].get("URL"),
                    data[0].get("lat"), data[0].get("lng"));
        } catch (IOException e) {

            e.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onPostExecute(String jsonString) {

        Log.v(TAG, "onPostExecute" + jObject.toString());
        try {

            markers = jObject.getJSONArray("contents");
            int mCount = markers.length();
            for (int i = 0; i < mCount; i++) {

                try {


                    IconGenerator iconG = new IconGenerator(my_context);
                    JSONObject marker = (JSONObject) markers.get(i);
                    String title = "";
                    if (marker.getString("title") == "null") {
                        title = "[想吃]" + marker.getString("gp_title");
                        //ㄇ
                        iconG.setTextAppearance(R.style.iconGenText);
                    } else {
                        title = marker.getString("title");
                    }
                    //Log.d(TAG, "title:" + marker.getString("title") +marker.getString("title").length());
                    //Log.d(TAG, "gp_title" + marker.getString("gp_title") + marker.getString("gp_title").length());
                    if (tw.waterdrop.waterdrop.fragment.MapFragment.markerHash.get(marker.getString("lat")
                            + marker.getString("lng")) != null) {
                        continue;
                    } else {
                        tw.waterdrop.waterdrop.fragment.MapFragment.markerHash.put(
                                marker.getString("lat")
                                        + marker.getString("lng"), ""
                        );
                    }


                    Bitmap iconBitmap = iconG.makeIcon(title);

                    Marker my_marker = tw.waterdrop.waterdrop.fragment.MapFragment.map.addMarker(
                            new MarkerOptions()
                                    .position(
                                            new LatLng(Float.parseFloat(marker
                                                    .getString("lat")), Float
                                                    .parseFloat(marker
                                                            .getString("lng")))
                                    )
                                    .title(title)

                                    .snippet(marker.getString("address"))
                                    .icon(BitmapDescriptorFactory
                                            .fromBitmap(iconBitmap))
                    );

                    if (tw.waterdrop.waterdrop.fragment.MapFragment.eventMarkerMap.get(my_marker.getId()) == null) {
                        tw.waterdrop.waterdrop.fragment.MapFragment.eventMarkerMap.put(my_marker.getId(), marker);
                    }
                    //Log.e(TAG,eventMarkerMap.toString());

                    //點地圖上的標題
                    tw.waterdrop.waterdrop.fragment.MapFragment.map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            String title = "";
                            try {
                                JSONObject marker_json = tw.waterdrop.waterdrop.fragment.MapFragment.eventMarkerMap.get(marker.getId());

                                //Log.e(TAG,marker_json.toString());
                                if (marker_json.getString("title") == "null") {
                                    title = marker_json.getString("gp_title");
                                } else {
                                    title = marker_json.getString("title");
                                }

                                if (marker_json.getString("contents") == "null") {
                                    return;
                                }

                                Intent intent = new Intent();
                                intent.setClass(my_context, ListDetail.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("title", title);
                                bundle.putString("content", marker_json.getString("contents"));
                                intent.putExtras(bundle);
                                my_context.startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }

    }

}