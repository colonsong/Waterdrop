package tw.waterdrop.waterdrop.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import tw.waterdrop.waterdrop.R;

public class AlbumBaseAdapter extends BaseAdapter {

    private LayoutInflater myInflater;
    ArrayList<Map<String, Object>> list = null;
    private String TAG = "AlbumBaseAdapter";

    public AlbumBaseAdapter(Context handler, ArrayList<Map<String, Object>> list2) {
        myInflater = LayoutInflater.from(handler);
        this.list = list2;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Map<String, Object> getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 自訂類別，表達個別listItem中的view物件集合。
        ViewTag viewTag;

        if (convertView == null) {
            // 取得listItem容器 view

            int type = getItemViewType(position);
            Log.d(TAG, "getView " + position + " " + convertView + " type = "
                    + type);
            Log.d(TAG, "有圖");
            convertView = myInflater.inflate(R.layout.album_image, null);
            // 建構listItem內容view
            viewTag = new ViewTag(
                    (ImageView) convertView.findViewById(R.id.album_image));
            viewTag.icon.setTag(list.get(position).get("pic"));


            // 設置容器內容
            convertView.setTag(viewTag);
        } else {
            viewTag = (ViewTag) convertView.getTag();

            int type = getItemViewType(position);
            if (type == 0)
            	viewTag.icon.setImageResource(R.drawable.blank);

        }
        if (getItem(position).get("bitmap") != null) {

            viewTag.icon.setImageBitmap((Bitmap) getItem(position).get("bitmap"));




        }
        // 向ViewHolder中填入的数据
/*
        if ((String) getItem(position).get("changeImg") != null) {
			if (getItem(position).get("changeImg").toString() == "1") {
				File imgFile = new File(getItem(position).get("image_blank")
						.toString());
				ImageView myImage = viewTag.icon;

				myImage.setImageURI(Uri.fromFile(imgFile));

			}
		}
		*/


        return convertView;
    }

    // 自訂類別，表達個別listItem中的view物件集合。
    class ViewTag {
        ImageView icon;


        public ViewTag(ImageView icon) {

            if (icon != null) {
                this.icon = icon;

            }

        }

        public ViewTag(TextView title, TextView datetime, TextView content) {

        }
    }
}