package tw.waterdrop.waterdrop.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import tw.waterdrop.waterdrop.R;
import tw.waterdrop.waterdrop.activity.ListDetail;
import tw.waterdrop.waterdrop.activity.MainActivity;
import tw.waterdrop.waterdrop.adapter.MyBaseAdapter;
import tw.waterdrop.waterdrop.task.RssTask;
import tw.waterdrop.waterdrop.task.RssTotalTask;
import tw.waterdrop.waterdrop.task.ImageDownloader;



/**
 * Created by colon on 2014/7/22.
 */
public class BlogFragment extends Fragment {
    public static final int INDEX =1;
    public static final Number LoadingItemNum = 6;
    public static Queue<tw.waterdrop.waterdrop.task.ImageDownloader> imageQueue;
    public Map<Integer, Boolean> rssIsRead = new HashMap<Integer, Boolean>();
    private static View rootView;
    public static ProgressDialog rssDialog;
    public static ListView mListView;
    public static final String TAG = "Blog";
    public static int totalRss;
    public final int rssPageNum = 100;
    //SAVE LOADED IMAGE
    public static HashMap<Integer, String> imageLoaded;
    public static boolean isReadRss = false;
    // private SimpleAdapter adapter;
    public static BaseAdapter adapter;
    private static Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_planet,
                container, false);
        mListView = (ListView) rootView.findViewById(R.id.myListView);
        imageQueue = new LinkedList<ImageDownloader>();

        if (isReadRss) {
            String[] from = new String[]{"image", "title",
                    "datetime", "contentNoHtml"};
            int[] to = new int[]{R.id.leftListIcon,
                    R.id.leftListText, R.id.textView2,
                    R.id.descriptionText};

            adapter = new MyBaseAdapter(mContext, RssTask.list);
            mListView.setAdapter(adapter);
            mListView.post(new Runnable() {
                public void run() {
                    drawImage(mListView.getFirstVisiblePosition(),
                            mListView.getLastVisiblePosition());
                    setListViewListener();
                }
            });
            return rootView;
        }
        isReadRss = true;
        // 避免在main使用new file增加效率，有值就代表曾經下載過了
        imageLoaded = new HashMap<Integer, String>();

        showrssDialog();

        _getRss();
        // Getting Caching directory
        MainActivity.cacheDirectory = getActivity().getCacheDir();
        // Getting a reference to ListView of activity_main
        return rootView;
    }

    public  static void drawImage(int start, int end) {
        Log.v(TAG, "start:" + start + "end:" + end);
        for (int i = start; i <= end; i++) {

            Map<String, Object> hm = RssTask.list.get(i);

            String imgUrl = (String) hm.get("image");
            if (imgUrl == null) {
                continue;
            }
            // Temporary file to store the downloaded image
				/*
				 * File tmpFile = new File(MainActivity.cacheDirectory.getPath()
				 * + "/rss_cover_" + i + ".png");
				 */
            String hashUrl =imageLoaded.get(i);
            if (hashUrl != null) {
                // hm.put("image_blank", hashUrl);
                // adapter.notifyDataSetChanged();
            } else {

                ImageDownloader imageLoader = new ImageDownloader();

                HashMap<String, Object> hmDownload = new HashMap<String, Object>();
                hmDownload.put("image", imgUrl);
                hmDownload.put("position", i);
                // Starting ImageLoaderTask to download and populate image
                // in
                // the
                // listview
                imageLoader.execute(hmDownload);
                // 限制只能載入數量
                if (imageQueue.size() == LoadingItemNum.intValue()) {
                    imageQueue.poll().cancel(true);

                }
                imageQueue.offer(imageLoader);

            }
        }

    }
    public  void setListViewListener() {
        setOnClickListener();
        setScrollListener();
    }
    public  void setOnClickListener() {
        mListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int position, long id) {

                        String title = RssTask.list.get(position).get("title")
                                .toString();
                        String description = RssTask.list.get(position)
                                .get("content").toString();
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), ListDetail.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("title", title);
                        bundle.putString("content", description);
                        intent.putExtras(bundle);
                        getActivity().startActivity(intent);

                    }

                });
    }

    public  void setScrollListener() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: //

                        // mBusy = false;
                        drawImage(mListView.getFirstVisiblePosition(),
                                mListView.getLastVisiblePosition());
                        Log.v(TAG, mListView.getLastVisiblePosition() + "");
                        int rssLastSize = RssTask.list.size();
                        // 避免重複讀取
                        if (rssIsRead.get(rssLastSize) != null) {
                            Toast.makeText(getActivity(), "讀取中..", Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                        if (mListView.getLastVisiblePosition() + 3 > rssLastSize) {
                            //到底了
                            if (rssLastSize < totalRss) {

                                RssTask rssTask = new RssTask(mContext);
                                rssTask.execute(
                                        "http://waterdrop.tw/mobile/blog/"
                                                + rssLastSize + "/"
                                                + rssPageNum + "/", "add"
                                );
                                setListViewListener();
                                rssIsRead.put(rssLastSize, true);

                                Toast.makeText(
                                        getActivity(),
                                        "讀取文章中.." + rssLastSize + " - "
                                                + (rssLastSize + rssPageNum),
                                        Toast.LENGTH_SHORT
                                ).show();
                            } else {
                                Toast.makeText(getActivity(), "已無文章",
                                        Toast.LENGTH_SHORT).show();
                            }

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

    public  void showrssDialog() {
        rssDialog = new ProgressDialog(getActivity());
        // 设置进度条风格，风格为长形
        rssDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置ProgressDialog 标题
        rssDialog.setTitle("滴一滴水");
        // 设置ProgressDialog 提示信息
        rssDialog.setMessage("請稍後...");
        // 设置ProgressDialog 标题图标
        rssDialog.setIcon(R.drawable.my_icon);
        rssDialog.setCanceledOnTouchOutside(false);
        // 设置ProgressDialog 的进度条是否不明确
        rssDialog.setIndeterminate(false);
        // rssDialog.setMax(15);
        // 设置ProgressDialog 是否可以按退回按键取消
        rssDialog.setCancelable(true);
        // 让ProgressDialog显示
        rssDialog.show();
    }
    private  void _getRss() {
        RssTask rssTask = new RssTask(mContext);

        rssTask.execute("http://waterdrop.tw/mobile/blog");
        setListViewListener();

        RssTotalTask rssTotalTask = new RssTotalTask(mContext);
        rssTotalTask.execute();


			/*
			 * ThreadXMLParser thread = new ThreadXMLParser();
			 * thread.HandlerManager = new Handler() {
			 *
			 * @Override public void handleMessage(Message msg) {
			 *
			 * list = (ArrayList<Map<String, Object>>) msg.obj; String[] from =
			 * new String[] { "image", "title", "publishDate",
			 * "descriptonNoHtml" }; int[] to = new int[] { R.id.imageView1,
			 * R.id.textView1, R.id.textView2, R.id.descriptionText };
			 *
			 * // adapter = new SimpleAdapter(getBaseContext(), //
			 * list,R.layout.mylist, from, to); adapter = new
			 * MyBaseAdapter(getActivity(), list);
			 * mListView.setAdapter(adapter);
			 *
			 * mListView.post(new Runnable() { public void run() {
			 * drawImage(mListView.getFirstVisiblePosition(),
			 * mListView.getLastVisiblePosition()); setListViewListener(); }
			 * });
			 *
			 * rssDialog.dismiss(); } };
			 *
			 * thread.start_parser();
			 */
    }


}
