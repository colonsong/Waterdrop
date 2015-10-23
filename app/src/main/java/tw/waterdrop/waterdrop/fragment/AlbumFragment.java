package tw.waterdrop.waterdrop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tw.waterdrop.waterdrop.R;
import tw.waterdrop.waterdrop.activity.AlbumPic;
import tw.waterdrop.waterdrop.adapter.AlbumBaseAdapter;
import tw.waterdrop.waterdrop.task.AlbumTask;


/**
 * Created by colon on 2014/7/24.
 */
public class AlbumFragment extends Fragment {
    public static final int INDEX = 2;
    private static final String TAG = "AlbumFragment";
    private static ArrayList<Map<String, Object>> albumList = new ArrayList<Map<String, Object>>();
    private static GridView album_grid;


    public static HashMap<Integer, String> imageLoaded;
    public Map<Integer, Boolean> picIsRead = new HashMap<Integer, Boolean>();

    public static BaseAdapter baseAdapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.album, container, false);
        album_grid = (GridView) rootView.findViewById(R.id.album_grid);
        if (albumList.size() == 0) {
            AlbumTask albumTask = new AlbumTask(this);
            albumTask.execute();
        }
        baseAdapter = new AlbumBaseAdapter(this);
        album_grid.setAdapter(baseAdapter);
        albumOnItemClickListener();
        return rootView;

    }

    public ArrayList<Map<String, Object>> getAlbumlist() {
        return albumList;
    }


    public GridView get_grid_view() {
        return album_grid;
    }



    public void albumOnItemClickListener() {
        album_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "轉換高解析照片中 " + position, Toast.LENGTH_LONG).show();
                String pic = albumList.get(position).get("pic")
                        .toString();

                Intent intent = new Intent();
                intent.setClass(getActivity(), AlbumPic.class);
                Bundle bundle = new Bundle();
                bundle.putString("pic", pic);
                bundle.putString("position", String.valueOf(position));

                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }

        });

    }
}
