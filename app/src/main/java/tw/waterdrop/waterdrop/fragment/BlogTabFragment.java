package tw.waterdrop.waterdrop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import tw.waterdrop.waterdrop.R;


/**
 * Created by colon on 2015/10/8.
 */
public class BlogTabFragment extends Fragment {

    private FragmentTabHost mTabHost;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.blog_tabs,container,false);
        mTabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);


        mTabHost.setup(getActivity(), getChildFragmentManager(),R.id.realtabcontent);


        mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.title_section1, android.R.drawable.star_on)),
                BlogFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.title_section2, android.R.drawable.star_on)),
                AlbumFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab3").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.title_section0, android.R.drawable.star_on)),
                MapFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab3").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.title_section3, android.R.drawable.star_on)),
                UploadPicFragment.class, null);

        return mTabHost;
    }

    private View getTabIndicator(Context context, int title, int icon) {
        View view = LayoutInflater.from(context).inflate(R.layout.blog_tabs_btn, null);

        ImageView iv = (ImageView) view.findViewById(R.id.imageView);
        iv.setImageResource(icon);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(title);
        return view;
    }

}
