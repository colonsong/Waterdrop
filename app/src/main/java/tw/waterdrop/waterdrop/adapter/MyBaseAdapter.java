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

public class MyBaseAdapter extends BaseAdapter {

    private LayoutInflater myInflater;
    private Context context;
    ArrayList<Map<String, Object>> list = null;
    private String TAG = "MyBaseAdapter";

    public MyBaseAdapter(Context context, ArrayList<Map<String, Object>> list2) {
        this.context = context;
        this.myInflater = LayoutInflater.from(context);
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
    public int getItemViewType(int position) {

        if ((String) getItem(position).get("image") == null) {
            // 沒圖
            return 1;

        } else {
            // 有圖
            return 0;
        }

    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 自訂類別，表達個別listItem中的view物件集合。
        ViewHolder ViewHolder;

        if (convertView == null) {
            // 取得listItem容器 view
            int type = getItemViewType(position);
            switch (type) {
                case 0:
                    Log.d(TAG, "有圖");
                    convertView = myInflater.inflate(R.layout.mylist, null);
                    // 建構listItem內容view
                    ViewHolder = new ViewHolder(
                            (ImageView) convertView.findViewById(R.id.leftListIcon),
                            (TextView) convertView.findViewById(R.id.leftListText),
                            (TextView) convertView.findViewById(R.id.textView2),
                            (TextView) convertView
                                    .findViewById(R.id.descriptionText)
                    );
                    break;
                default:
                    Log.d(TAG, "沒圖");
                    convertView = myInflater.inflate(R.layout.mylist_noimg, null);
                    // 建構listItem內容view
                    ViewHolder = new ViewHolder(
                            (TextView) convertView.findViewById(R.id.leftListText),
                            (TextView) convertView.findViewById(R.id.textView2),
                            (TextView) convertView
                                    .findViewById(R.id.descriptionText)
                    );
                    break;

            }


            // 設置容器內容
            convertView.setTag(ViewHolder);
        } else {
            ViewHolder = (ViewHolder) convertView.getTag();

            int type = getItemViewType(position);
            if (type == 0 &&  ViewHolder.icon != null)
                ViewHolder.icon.setImageResource(R.drawable.blank);

        }

        // 向ViewHolder中填入的数据
/*
        if ((String) getItem(position).get("changeImg") != null) {
			if (getItem(position).get("changeImg").toString() == "1") {
				File imgFile = new File(getItem(position).get("image_blank")
						.toString());
				ImageView myImage = ViewHolder.icon;

				myImage.setImageURI(Uri.fromFile(imgFile));

			}
		}
		*/
        if (getItem(position).get("bitmap") != null ) {
            ImageView myImage = ViewHolder.icon;
            myImage.setImageBitmap((Bitmap) getItem(position).get("bitmap"));

        }
        ViewHolder.title.setText((String) getItem(position).get("title"));
        ViewHolder.datetime.setText((String) getItem(position).get("datetime"));
        ViewHolder.contentNoHtml.setText((String) getItem(position).get(
                "contentNoHtml"));

        return convertView;
    }

    // 自訂類別，表達個別listItem中的view物件集合。
    private static class ViewHolder {
        ImageView icon;
        TextView title;
        TextView datetime;
        TextView contentNoHtml;


        public ViewHolder(ImageView icon, TextView title, TextView datetime,
                       TextView content) {
            this.contentNoHtml = content;
            if (icon != null) {
                this.icon = icon;

            }
            this.title = title;
            this.datetime = datetime;
        }

        public ViewHolder(TextView title, TextView datetime, TextView content) {
            this.contentNoHtml = content;
            this.title = title;
            this.datetime = datetime;
        }
    }
}