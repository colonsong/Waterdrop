package tw.waterdrop.waterdrop.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.waterdrop.waterdrop.R;
import tw.waterdrop.waterdrop.adapter.UploadBaseAdapter;
import tw.waterdrop.waterdrop.util.ImageCache;
import tw.waterdrop.waterdrop.util.ImageCache.ImageCacheParams;
import tw.waterdrop.waterdrop.util.Utils;

public class UploadPicFragment extends Fragment {
    public static final int INDEX =3;
    private List pictureList = new ArrayList();
    private static final  String TAG = "UploadPIC";
    private static final String picture_path = "storage/ext_sd/DCIM/100MEDIA/";
    private static final String IMAGE_CACHE_DIR = "thumbs";
    private BaseAdapter baseAdapter;
    private List titleList = new ArrayList();
    private static ImageCache mImageCache;
    private static Context mContext;

    private Map<Integer,Boolean> selectedPicMap = new HashMap<Integer, Boolean>();
    private SharedPreferences settings;
    // 選單項目物件
    private MenuItem add_item, search_item, revert_item, share_item, delete_item;

    // 已選擇項目數量
    private int selectedCount = 0;

    public UploadPicFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //onCreate是指创建该fragment类似于Activity.onCreate，你可以在其中初始化除了view之外的东西
        super.onCreate(savedInstanceState);
        //設定選單
        setHasOptionsMenu(true);
        //設定環境
        mContext = getActivity().getApplicationContext();

        //能夠挖到的最大內存
        //int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //從虛擬機挖多少內存過來
        //int nowUseMemory = (int) Runtime.getRuntime().totalMemory();

        initData();
        doView();
        doController();

    }
    private void doController() {

    }

    private void doView()
    {
        baseAdapter = new UploadBaseAdapter(mContext, pictureList ,mImageCache ,selectedPicMap);

    }

    private void initData()
    {
        ImageCacheParams cacheParams = new ImageCacheParams(IMAGE_CACHE_DIR);
        cacheParams.memCacheSize = 1024 * 1024 * Utils.getMemoryClass(getActivity()) / 3;
        mImageCache = ImageCache.findOrCreateCache(getActivity(),cacheParams);
        getFiles(picture_path);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
/**
 * onCreateView是创建该fragment对应的视图，你必须在这里创建自己的视图并返回给调用者，例如
 return inflater.inflate(R.layout.fragment_settings, container, false);。
 super.onCreateView有没有调用都无所谓，因为super.onCreateView是直接返回null的。
 */
        //mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
        //mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);
        final View v  = inflater.inflate(R.layout.upload, container, false);
        final GridView grid_view = (GridView) v.findViewById(R.id.upload_grid);
        grid_view.setAdapter(baseAdapter);


        grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                //ImageView viewImg =(ImageView) view;

                //viewImg.setCropToPadding(true);
                //view.setBackgroundColor(Color.parseColor("#91C7FF"));

                //lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                //lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);

                //int padding_num = (int)Pixel_dp.convertPixelToDp(50,mcontext);


                if(selectedPicMap.get(position) == null)
                {

                    imageToAlpha(mContext,view,80);
                    selectedPicMap.put((int)id,true);
                }
            }
        });


        //setScrollListener();

        //drawImage(grid_view.getFirstVisiblePosition(),
        //       grid_view.getVisiblePositioLastn());



        return v;


    }

    /*
    public Bitmap getMagicDrawingCache(View view) {
        Bitmap bitmap = (Bitmap) view.getTag(cacheBitmapKey);
        Boolean dirty = (Boolean) view.getTag(cacheBitmapDirtyKey);
        if (view.getWidth() + view.getHeight() == 0) {
            view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        }
        int viewWidth = view.getWidth();
        int viewHeight = view.getHeight();
        if (bitmap == null || bitmap.getWidth() != viewWidth || bitmap.getHeight() != viewHeight) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            bitmap = Bitmap.createBitmap(viewWidth, viewHeight, bitmap_quality);
            view.setTag(cacheBitmapKey, bitmap);
            dirty = true;
        }
        if (dirty == true || !quick_cache) {
            bitmap.eraseColor(color_background);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            view.setTag(cacheBitmapDirtyKey, false);
        }
        return bitmap;
    }
    */

    public static Bitmap getViewBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(360, 360, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
    public static void imageToAlpha(Context mcontext,View v,int alpha_num)
   {
       // view.setPadding(padding_num,padding_num,padding_num,padding_num);

       // Drawable imgDrawable = viewImg.getDrawable();
       // 防止出现Immutable bitmap passed to Canvas constructor错误
       Bitmap upload_bitmap = BitmapFactory.decodeResource(mcontext.getResources(),
               R.drawable.upload).copy(Bitmap.Config.ARGB_8888, true);
// 新的图片
    //look for draw cache size
      // ViewConfiguration.get(mcontext).getScaledMaximumDrawingCacheSize();
      // imageview.buildDrawingCache();
      // Bitmap newBitmap = imageview.getDrawingCache();
       //Bitmap newBitmap = ((BitmapDrawable)imageview.getDrawable()).getBitmap();
     // imageview.destroyDrawingCache();

       Bitmap newBitmap = getViewBitmap(v);
       if(newBitmap == null)
       {
           return;
       }

       // Bitmap newBitmap = Bitmap.createBitmap(imgDrawable);
       //newBitmap.eraseColor(Color.TRANSPARENT);

       Canvas canvas = new Canvas(newBitmap);

       Paint paint = new Paint();


       paint.setColor(Color.BLACK);
       paint.setAlpha(alpha_num);
       // Paint transparentPaint = new Paint();
       //transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
       //transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
       canvas.drawRect(0, 0, newBitmap.getWidth(), newBitmap.getHeight(), paint);
       //canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
      // int cx = (360 - newBitmap.getWidth()) >> 1; // same as (...) / 2
       //int cy = (360 - newBitmap.getHeight()) >> 1;
       canvas.drawBitmap(upload_bitmap,100,100,null);
       //drawBitmap
       //canvas.save(Canvas.ALL_SAVE_FLAG);

       // 存储新合成的图片
       //canvas.restore();
       ImageView viewImg =(ImageView) v;
       viewImg.setImageBitmap(newBitmap);

/*
                ViewGroup.LayoutParams para;
                para = view.getLayoutParams();



                para.height = 360;
                para.width = 360;


                view.setLayoutParams(para);
                */
   }

    private static String[] imageFormatSet = new String[]{"jpg","png","jpeg"};
    //判断是否为图片文件
    private static boolean isPictureFile(String path)
    {
        for(String format : imageFormatSet){
            if(path.contains(format)){
                return true;
            }
        }

        return false;
    }

    private void getFiles(String url) {
        File files = new File(url);
        File[] file = files.listFiles();

        try{
            for(File f : file){
                if(f.isDirectory()){
                   // Log.v(TAG,f.getAbsolutePath());
                    getFiles(f.getAbsolutePath());
                }else{
                    if(isPictureFile(f.getAbsolutePath())){
                        pictureList.add(f.getAbsolutePath());
                        titleList.add(f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(".")));
                    }
                }
               // Log.v(TAG,file.length + "");
               // if(pictureList.size() > 30) {
                 //   break;
                //}
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /*
    private  void setScrollListener() {
        grid_view.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: //


                       // drawImage(grid_view.getFirstVisiblePosition(),
                       //         grid_view.getLastVisiblePosition());


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
    */

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_upload, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }


    // 使用者選擇所有的選單項目都會呼叫這個方法
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 使用參數取得使用者選擇的選單項目元件編號
        int itemId = item.getItemId();

        // 判斷該執行什麼工作，目前還沒有加入需要執行的工作
        switch (itemId) {
            case R.id.search_item:
                break;
            // 使用者選擇新增選單項目
            case R.id.add_item:
                break;
            // 取消所有已勾選的項目
            case R.id.revert_item:
                break;
            // 刪除
            case R.id.delete_item:
                // 沒有選擇
                break;
            case R.id.googleplus_item:
                break;
            case R.id.facebook_item:
                break;
        }
        return false;
    }
}