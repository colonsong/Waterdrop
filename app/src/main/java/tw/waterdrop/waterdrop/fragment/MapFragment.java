package tw.waterdrop.waterdrop.fragment;


import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import tw.waterdrop.waterdrop.R;
import tw.waterdrop.waterdrop.task.SetMarkerTask;



/**
 * Created by colon on 2014/7/22.
 */
public class MapFragment extends Fragment implements
        GoogleMap.OnMyLocationChangeListener, GoogleMap.OnCameraChangeListener {
    public static final int INDEX =4;
    public static com.google.android.gms.maps.MapFragment mapFragment;
    private String TAG = "MapFragment";
    public static GoogleMap map;
    private MapView mapView;
    public  View rootView;
    public static HashMap<String, JSONObject> eventMarkerMap;
    public static Context context;
    public static Queue<SetMarkerTask> markerTaskQueue;
    public static HashMap<String, String> markerHash;

    public MapFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.context = getActivity();





                /*
                 * rootView = inflater.inflate(R.layout.map,container,false); if
				 * (map == null) { map = ((MapFragment)
				 * getFragmentManager().findFragmentById( R.id.map)).getMap();
				 *
				 *
				 * }
				 */

        try {
            //rootView = inflater.inflate(R.layout.map, null, false);
            if(rootView == null) {
                eventMarkerMap =  new HashMap<String, JSONObject>();
                markerTaskQueue = new LinkedList<SetMarkerTask>();
                markerHash = new HashMap<String, String>();
                rootView = inflater.inflate(R.layout.map, null, false);

                mapView = (MapView) rootView.findViewById(R.id.mapview);
                mapView.onCreate(savedInstanceState);

                mapView.onResume();// needed to get the map to display immediately


                MapsInitializer.initialize(getActivity());

                map = mapView.getMap();
            }





               // FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
               // transaction.add(R.id.content_frame, mapFrag).commit();








               // SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager()
                 //       .findFragmentById(R.id.map);


                //map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

                //SupportMapFragment m = ((SupportMapFragment) getChildFragmentManager()
                  //      .findFragmentById(R.id.map));

                // check if map is created successfully or not



            //mapFragment = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map));

            //map = mapFragment.getMap();
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            map.setOnCameraChangeListener(this);


        } catch (InflateException e) {
					/* map is already there, just return view as it is */
        }
				/*
				 * MainActivity myActivity = new MainActivity();
				 * MainActivity.SetMarkerTask setMarker = myActivity.new
				 * SetMarkerTask();
				 *
				 *
				 * setMarker.execute("http://waterdrop.tw/mobile/get_marker",
				 * "25.041952", "121.533272");
				 */

            CameraPosition cameraPosition = CameraPosition.builder()
                    .target(new LatLng(25.041952, 121.533272)).zoom(14)
                    .build();
            CameraUpdate cameraUpdate = CameraUpdateFactory
                    .newCameraPosition(cameraPosition);

            map.animateCamera(cameraUpdate);
        //location btn in right top
         //map.setMyLocationEnabled(true);
         map.setOnMyLocationChangeListener(this);
        return rootView;


    }




    @Override
    public void onMyLocationChange(Location location) {

        Log.v(TAG, location.toString());
        Toast.makeText(context, "Location:" + location.toString(),
                Toast.LENGTH_SHORT).show();

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCameraChange(CameraPosition cameraPostion) {

        Log.v("markerQSize", "markerQSize:" + markerTaskQueue.size());
        if (markerTaskQueue.size() >= 1) {
            markerTaskQueue.poll().cancel(true);

            Log.v("markerQSize", "markerQ:刪除Queue" + markerTaskQueue.size());

        }

        SetMarkerTask setMarker = new SetMarkerTask(getActivity().getApplicationContext());

        HashMap<String, String> marHashMap = new HashMap<String, String>();
        marHashMap.put("URL", "http://waterdrop.tw/mobile/get_marker");
        marHashMap
                .put("lat", String.valueOf(cameraPostion.target.latitude));
        marHashMap.put("lng",
                String.valueOf(cameraPostion.target.longitude));
        setMarker.execute(marHashMap);
        markerTaskQueue.offer(setMarker);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /*
        com.google.android.gms.maps.MapFragment f = (com.google.android.gms.maps.MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        if (f != null) {
            getFragmentManager().beginTransaction().remove(f).commit();
            markerHash = new HashMap<String, String>();
            markerTaskQueue = new LinkedList<SetMarkerTask>();
        }
        */
    }


}
