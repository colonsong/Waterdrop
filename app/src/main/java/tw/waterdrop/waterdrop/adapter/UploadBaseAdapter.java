package tw.waterdrop.waterdrop.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import tw.waterdrop.waterdrop.fragment.UploadPicFragment;
import tw.waterdrop.waterdrop.task.LoadPicTask;
import tw.waterdrop.waterdrop.util.ImageCache;

public class UploadBaseAdapter extends BaseAdapter {

    private LayoutInflater myInflater;
    private List pictureList = null;
    private Bitmap mLoadingBitmap;
    private final static String TAG = "UploadBaseAdapter";

    private Context mContext;
    private int mItemHeight = 0;

    private GridView.LayoutParams mImageViewLayoutParams;
    private ImageCache imageCache;
    private Map<Integer,Boolean> selectedPicMap ;


    public UploadBaseAdapter(Context context, List pictureList,ImageCache imageCache ,  Map<Integer,Boolean> selectedPicMap ) {
        super();
        this.mContext = context;
        this.myInflater = LayoutInflater.from(context);
        this.pictureList = pictureList;
        this.imageCache = imageCache;
        this.selectedPicMap = selectedPicMap;

        mImageViewLayoutParams = new GridView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    @Override
    public int getCount() {
        return pictureList.size();
    }

    @Override
    public String getItem(int position) {
        return pictureList.get(position).toString();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 自訂類別，表達個別listItem中的view物件集合。
        ImageView imageView;

        if (convertView == null) {


            //這裡代表沒循環利用
            // 取得listItem容器 view

            // int type = getItemViewType(position);
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(mImageViewLayoutParams);
            //imageView = (ImageView) myInflater.inflate(R.layout.upload_pic, null);
            // 建構listItem內容view
            //convertView.findViewById(R.id.upload_pic).
            // viewTag = new ViewTag((ImageView) convertView.findViewById(R.id.upload_pic));

            //mn
            //viewTag.icon.setTag(position);


            // 設置容器內容
            // convertView.setTag(position);

        } else {
            //viewTag = new ViewTag((ImageView) convertView);


            //int type = getItemViewType(position);

            //viewTag.icon.setImageResource(R.drawable.blank);

            imageView = (ImageView) convertView;
            imageView.setPadding(0, 0, 0, 0);
        }
        //imageView.setLayoutParams(mImageViewLayoutParams);


        ViewGroup.LayoutParams para;
        para = imageView.getLayoutParams();


        //获取
        //Log.d(TAG, "layout height0: " + para.height);
        //Log.d(TAG, "layout width0: " + para.width);

        //设置
        //int width_height = (int) Pixel_dp.convertDpToPixel(120f,context);
        //Log.d(TAG, "width_height: " + width_height);

        para.height = 360;
        para.width = 360;
        imageView.setLayoutParams(para);





       // imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));


        drawImage(position, imageView , imageCache,  selectedPicMap);
        // viewTag.icon.setImageBitmap(
        //      decodeSampledBitmapFromResource(list.get(position).toString(), 120, 120));


        return imageView;

    }

    /**
     * @param imageView Any imageView
     * @return Retrieve the currently active work task (if any) associated with this imageView.
     * null if there is no such task.
     * , 被用作检索任务是否已经被分配到指定的 ImageView:
     */
    public static LoadPicTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    /**
     * Returns true if the current work has been canceled or if there was no work in
     * progress on this image view.
     * Returns false if the work in progress deals with the same data. The work is not
     * stopped in that case.
     */
    public static boolean cancelPotentialWork(Object data, ImageView imageView) {
        final LoadPicTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final Object bitmapData = bitmapWorkerTask.data;
            if (bitmapData == null || !bitmapData.equals(data)) {
                Log.v(TAG, data.toString());
                bitmapWorkerTask.cancel(true);

            } else {
                // The same work is already in progress.
                return false;
            }
        }
        return true;
    }

    //讀取VIEW時檢查自己有沒有TASK還再執行 如果有 停止 執行新的
    public static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<LoadPicTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, LoadPicTask bitmapWorkerTask) {
            super(res, bitmap);

            bitmapWorkerTaskReference =
                    new WeakReference<LoadPicTask>(bitmapWorkerTask);
        }

        public LoadPicTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    public void drawImage(Object data, ImageView imageView , ImageCache imageCache, Map<Integer,Boolean> selectedPicMap) {
        if(data == null)
        {
            return;
        }

        final int position = (Integer) data;
        Log.v(TAG, "start:" + position);
        Bitmap bitmap = null;
        if (imageCache != null) {
            bitmap = imageCache.getBitmapFromMemCache(String.valueOf(data));
        }

        if (bitmap != null) {
            // Bitmap found in memory cache
            imageView.setImageBitmap(bitmap);
            if(selectedPicMap.get(position) != null)
            {

              //  ViewPropertyAnimator.alpha(0.8f).withLayer();
              UploadPicFragment.imageToAlpha(mContext, imageView, 80);

            }

        } else {
            final LoadPicTask task = new LoadPicTask(mContext, imageView ,imageCache );
            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(mContext.getResources(), mLoadingBitmap, task);
            imageView.setImageDrawable(asyncDrawable);

            //task.execute(pictureList.get(position).toString(), position);
            Executor exec =  new ThreadPoolExecutor( 5 ,  128 ,  10 ,
                    TimeUnit.SECONDS,  new LinkedBlockingQueue<Runnable>());
            task.executeOnExecutor(exec,pictureList.get(position).toString(), position);
        }


        //final LoadPicTask task = new LoadPicTask(imgView);
        // task.execute(pictureList.get(i).toString());


        //LoadPicTask task = new LoadPicTask((ImageView) grid_view.findViewWithTag(i));
        //task.execute(pictureList.get(i).toString());


    }

    public void setItemHeight(int height) {
        if (height == mItemHeight) {
            return;
        }
        mItemHeight = height;
        mImageViewLayoutParams =
                new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mItemHeight);
       // mImageFetcher.setImageSize(height);
        notifyDataSetChanged();
    }




}