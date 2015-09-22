package tw.waterdrop.waterdrop.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
import tw.waterdrop.waterdrop.task.AlbumImageDownloader;
import tw.waterdrop.waterdrop.task.AlbumTask;


/**
 * Created by colon on 2014/7/24.
 */
public class AlbumFragment extends Fragment {
    public static final int INDEX=2;
    private static final String TAG = "album";
    public static ArrayList<Map<String, Object>> albumList = new ArrayList<Map<String, Object>>();
    public static GridView album_grid;
    public static View rootView;
    public static HashMap<Integer, String> imageLoaded;
    public Map<Integer, Boolean> picIsRead = new HashMap<Integer, Boolean>();
    public Context context;
    public static BaseAdapter baseAdapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.context = getActivity();
        return album(inflater, container);


    }

    public View album(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.album, container, false);

        if (albumList.size() == 0) {
            album_grid = (GridView) rootView.findViewById(R.id.album_grid);
            AlbumTask albumTask = new AlbumTask(getActivity().getApplicationContext());

            albumTask.execute();
            albumOnItemClickListener();


        } else {
            GridView gridview = (GridView) rootView
                    .findViewById(R.id.album_grid);
            ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
             baseAdapter = new AlbumBaseAdapter(getActivity(), albumList);

            album_grid.setAdapter(baseAdapter);
            albumDrawImage(0, albumList.size() - 1);
            //album_grid.setOnItemClickListener(null);
            albumOnItemClickListener();

        }
        imageLoaded = new HashMap<Integer, String>();


        setScrollListener();
        return rootView;
    }

    public void setScrollListener() {
        album_grid.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: //

                        // mBusy = false;
                        albumDrawImage(album_grid.getFirstVisiblePosition(),
                                album_grid.getLastVisiblePosition());
                        Log.v(TAG, album_grid.getLastVisiblePosition() + "");
                        int albumLastSize =  albumList.size();
                        // 避免重複讀取
                        if (picIsRead.get(albumLastSize) != null) {
                            Toast.makeText(getActivity().getApplicationContext(), "讀取中..", Toast.LENGTH_SHORT)
                                    .show();
                           return;
                        }
                        if (album_grid.getLastVisiblePosition() + 3 > albumLastSize) {
                            //到底了


                                AlbumTask albumTask = new AlbumTask(getActivity().getApplicationContext());
                                albumTask.execute();
                                albumOnItemClickListener();

                                picIsRead.put(albumLastSize, true);

                                Toast.makeText(
                                        getActivity().getApplicationContext(),
                                        "讀取文章中..", Toast.LENGTH_SHORT
                                ).show();


                        }

                        System.out.println("停止...");
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:

                        // mBusy = true;
                        System.out.println("正在滑动...");
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:

                        // mBusy = true;
                        System.out.println("开始滚动...");

                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });
    }

    public static void albumDrawImage(int start, int end) {
        //MainActivity.cacheDirectory = MainActivity.context.getCacheDir();
        Log.v(TAG, "start:" + start + "end:" + end);
        for (int i = start; i <= end; i++) {

            Map<String, Object> hm = albumList.get(i);

            String imgUrl = (String) hm.get("pic");
            if (imgUrl == null) {
                continue;
            }

            String hashUrl =imageLoaded.get(i);
            if (hashUrl != null) {
                // hm.put("image_blank", hashUrl);
                // adapter.notifyDataSetChanged();
            } else {
                AlbumImageDownloader imageLoader = new AlbumImageDownloader();
                HashMap<String, Object> albumMap = new HashMap<String, Object>();
                albumMap.put("image", imgUrl);
                albumMap.put("position", i);
                imageLoader.execute(albumMap);
            }
            // Temporary file to store the downloaded image
                /*
				 * File tmpFile = new File(MainActivity.cacheDirectory.getPath()
				 * + "/rss_cover_" + i + ".png");
				 */




        }

    }


    public void albumOnItemClickListener() {
        album_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(), "轉換高解析照片中 " + position, Toast.LENGTH_LONG).show();
                String pic = albumList.get(position).get("pic")
                        .toString();

                Intent intent = new Intent();
                intent.setClass(getActivity().getApplicationContext(), AlbumPic.class);
                Bundle bundle = new Bundle();
                bundle.putString("pic", pic);
                bundle.putString("position", String.valueOf(position));

                intent.putExtras(bundle);
                getActivity().getApplicationContext().startActivity(intent);
            }

        });

    }
}
