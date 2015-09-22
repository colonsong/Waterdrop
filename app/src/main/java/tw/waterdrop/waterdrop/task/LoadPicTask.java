package tw.waterdrop.waterdrop.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import tw.waterdrop.waterdrop.adapter.UploadBaseAdapter;
import tw.waterdrop.waterdrop.util.ImageCache;

/**
 * Created by colon on 2014/7/30.
 */
public class LoadPicTask extends AsyncTask<Object, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    public Object data = "";
    private String TAG = "LoadPicTask";
    private boolean mFadeInBitmap = true;
    private static final int FADE_IN_TIME = 200;
    private Context mContext;
    private Bitmap mLoadingBitmap;
    private ImageCache imageCache;
    protected static boolean mPauseWork = false;
    private static final Object mPauseWorkLock = new Object();

    public LoadPicTask(Context context, ImageView imageView, ImageCache imageCache) {
        this.mContext = context;
        this.imageCache = imageCache;
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);

    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(Object... params) {
        data = params[0];
        String position = String.valueOf(params[1]);
        Bitmap bitmap = null;

        final String path = String.valueOf(data);
        // If the image cache is available and this task has not been cancelled by another
        // thread and the ImageView that was originally bound to this task is still bound back
        // to this task and our "exit early" flag is not set then try and fetch the bitmap from
        // the cache
        // Wait here if work is paused and the task is not cancelled
        synchronized (mPauseWorkLock) {
            while (mPauseWork && !isCancelled()) {
                try {
                    mPauseWorkLock.wait();
                } catch (InterruptedException e) {
                }
            }
        }


        if (imageCache != null && !isCancelled() && getAttachedImageView() != null
                ) {
            bitmap = imageCache.getBitmapFromDiskCache(position);
        }


        if (bitmap == null && !isCancelled() && getAttachedImageView() != null) {
            // bitmap = decodeSampledBitmapFromFile(path,360,360);

            BitmapFactory.Options options = new BitmapFactory.Options();
            //先取寬高
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(path, options);

            options.inScaled = true;
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 360, 360);
//options.inDither = true;
            //density size
            options.inDensity = options.outWidth;
            options.inTargetDensity = 360 * options.inSampleSize;
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            bitmap = BitmapFactory.decodeFile(path, options);
        }
        //儲存CACHE
        if (bitmap != null && imageCache != null) {
            imageCache.addBitmapToCache(position, bitmap);


        }

        return bitmap;
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {

        if (isCancelled()) {
            bitmap = null;

        }


        final ImageView imageView = getAttachedImageView();

        if (imageView != null && bitmap != null) {
            imageView.setImageBitmap(bitmap);

        }


        /*
        final ImageView imageView = getAttachedImageView();

        if (imageView != null && bitmap != null) {

           // final LoadPicTask bitmapWorkerTask =
             //       UploadBaseAdapter.getBitmapWorkerTask(imageView);

                imageView.setImageBitmap(bitmap);

        }


        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
        */

    }

    @Override
    protected void onCancelled(Bitmap value) {
        super.onCancelled(value);
        synchronized (mPauseWorkLock) {
            mPauseWorkLock.notifyAll();
        }
    }

    /**
     * Called when the processing is complete and the final bitmap should be set on the ImageView.
     *
     * @param imageView
     * @param bitmap
     */
    private void setImageBitmap(ImageView imageView, Bitmap bitmap) {
        if (mFadeInBitmap) {
            // Transition drawable with a transparent drwabale and the final bitmap
            final TransitionDrawable td =
                    new TransitionDrawable(new Drawable[]{
                            new ColorDrawable(android.R.color.transparent),
                            new BitmapDrawable(mContext.getResources(), bitmap)
                    });
            // Set background to loading bitmap
            if (Build.VERSION.SDK_INT < 16) {
                imageView.setBackgroundDrawable(
                        new BitmapDrawable(mContext.getResources(), mLoadingBitmap));
            } else {
                imageView.setBackground(
                        new BitmapDrawable(mContext.getResources(), mLoadingBitmap));
            }


            imageView.setImageDrawable(td);
            td.startTransition(FADE_IN_TIME);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    /**
     * Returns the ImageView associated with this task as long as the ImageView's task still
     * points to this task as well. Returns null otherwise.
     * 沒用了
     */
    private ImageView getAttachedImageView() {
        final ImageView imageView = imageViewReference.get();
        final LoadPicTask bitmapWorkerTask = UploadBaseAdapter.getBitmapWorkerTask(imageView);

        if (this == bitmapWorkerTask) {
            return imageView;
        }

        return null;
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * Pause any ongoing background work. This can be used as a temporary
     * measure to improve performance. For example background work could
     * be paused when a ListView or GridView is being scrolled using a
     * {@link android.widget.AbsListView.OnScrollListener} to keep
     * scrolling smooth.
     * <p/>
     * If work is paused, be sure setPauseWork(false) is called again
     * before your fragment or activity is destroyed (for example during
     * {@link android.app.Activity#onPause()}), or there is a risk the
     * background thread will never finish.
     */
    public static void setPauseWork(boolean pauseWork) {
        synchronized (mPauseWorkLock) {
            mPauseWork = pauseWork;
            if (!mPauseWork) {
                mPauseWorkLock.notifyAll();
            }
        }
    }


}
