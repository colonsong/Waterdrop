package tw.waterdrop.waterdrop.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;
import java.util.Map;

import tw.waterdrop.waterdrop.util.ImageWorker;

public class UploadBaseAdapter extends BaseAdapter {
    private final static String TAG = "UploadBaseAdapter";
    private List pictureList;

    private Context mContext;
    private GridView.LayoutParams mImageViewLayoutParams;
    private int pictureListSize;

    public static Map<Integer, Boolean> selectedPicMap;
    private ImageWorker imageWorker;


    public UploadBaseAdapter(Context context, List pictureList, Map<Integer, Boolean> selectedPicMap, ImageWorker imageWorker) {
        super();
        Log.v(TAG, "gridview constructor");
        this.mContext = context;
        this.pictureList = pictureList;
        this.pictureListSize = pictureList.size();
        this.selectedPicMap = selectedPicMap;
        this.imageWorker = imageWorker;
        this.mImageViewLayoutParams = new GridView.
                LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 360);

    }

    @Override
    public int getCount() {
        Log.v(TAG,"gridview getCount" );
        return pictureListSize;
    }

    @Override
    public String getItem(int position) {

        Log.v(TAG,"gridview getItem" + position);
        return pictureList.get(position).toString();
    }

    @Override
    public long getItemId(int position) {

        Log.v(TAG,"gridview getItemId" + position);
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v(TAG,"gridview getView" + position);
        Log.v(TAG,"takstest getView" + position);
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

        }
        //imageView.setLayoutParams(mImageViewLayoutParams);

       // ViewGroup.LayoutParams para;
      //  para = imageView.getLayoutParams();

        //获取
        //Log.d(TAG, "layout height0: " + para.height);
        //Log.d(TAG, "layout width0: " + para.width);

        //设置
        //int width_height = (int) Pixel_dp.convertDpToPixel(120f,context);
        //Log.d(TAG, "width_height: " + width_height);

        //para.height = 360;
        //para.width = 360;
       // imageView.setLayoutParams(para);


        // imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        Log.v(TAG,"positionTest" + position);

            imageWorker.drawImage(position, imageView);


        // viewTag.icon.setImageBitmap(
        //      decodeSampledBitmapFromResource(list.get(position).toString(), 120, 120));

        return imageView;

    }



}