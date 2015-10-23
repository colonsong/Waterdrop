package tw.waterdrop.waterdrop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Map;

import tw.waterdrop.waterdrop.R;
import tw.waterdrop.waterdrop.fragment.AlbumFragment;

public class AlbumBaseAdapter extends BaseAdapter {

    private LayoutInflater myInflater;
    ArrayList<Map<String, Object>> picList = null;
    private String TAG = "AlbumBaseAdapter";
    private AlbumFragment albumFragment;

    public AlbumBaseAdapter(AlbumFragment albumFragment) {
        this.albumFragment = albumFragment;
        myInflater = LayoutInflater.from(albumFragment.getActivity());
        this.picList = albumFragment.getAlbumlist();
    }

    @Override
    public int getCount() {
        return picList.size();
    }

    @Override
    public Map<String, Object> getItem(int position) {
        return picList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 自訂類別，表達個別listItem中的view物件集合。

        ImageView imgView;
        if (convertView == null) {
            // 取得listItem容器 view



            imgView = (ImageView) myInflater.inflate(R.layout.album_pic, parent,false);
            // 建構listItem內容view




        } else {
           imgView = (ImageView) convertView;
           // if (type == 0)
            //	viewTag.icon.setImageResource(R.drawable.blank);

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
        Map<String, Object> hm = picList.get(position);
        if(hm != null)
        {
            String url =(String) hm.get("pic");
            Glide.with(albumFragment)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.blank)
                    .crossFade()
                    .into(imgView);
        }

        return imgView;
    }

}