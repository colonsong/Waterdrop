package tw.waterdrop.waterdrop.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import tw.waterdrop.waterdrop.Constants;
import tw.waterdrop.waterdrop.R;
import tw.waterdrop.waterdrop.fragment.AlbumFragment;
import tw.waterdrop.waterdrop.fragment.BasicTestFragment;
import tw.waterdrop.waterdrop.fragment.BlogFragment;
import tw.waterdrop.waterdrop.fragment.MapFragment;
import tw.waterdrop.waterdrop.fragment.UploadPicFragment;

/**
 * Created by colon on 15/7/21.
 */
public class MenuActivity extends FragmentActivity{
    //ctrl + o
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int frIndex = getIntent().getIntExtra(Constants.Extra.FRAGMENT_INDEX,0);
        Fragment fr = null;
        String tag = null;
        int titleRes;
        switch(frIndex)
        {
            case MapFragment.INDEX:
                tag = MapFragment.class.getSimpleName();
                fr = getSupportFragmentManager().findFragmentByTag(tag);
                if(fr == null)
                {
                    fr = new MapFragment();
                }
                titleRes = R.string.title_section0;
                break;
            case BlogFragment.INDEX:
                tag = BlogFragment.class.getSimpleName();
                fr = getSupportFragmentManager().findFragmentByTag(tag);
                if(fr == null)
                {
                    fr = new BlogFragment();
                }
                titleRes = R.string.title_section1;
                break;
            case AlbumFragment.INDEX:
                tag = AlbumFragment.class.getSimpleName();
                fr = getSupportFragmentManager().findFragmentByTag(tag);
                if(fr == null)
                {
                    fr = new AlbumFragment();
                }
                titleRes = R.string.title_section2;
                break;
            case UploadPicFragment.INDEX:
                tag = UploadPicFragment.class.getSimpleName();
                fr = getSupportFragmentManager().findFragmentByTag(tag);
                if(fr == null)
                {
                    fr = new UploadPicFragment();
                }
                titleRes = R.string.title_section3;
                break;
            case BasicTestFragment.INDEX:
                tag = BasicTestFragment.class.getSimpleName();
                fr = getSupportFragmentManager().findFragmentByTag(tag);
                if(fr == null)
                {
                    fr = new BasicTestFragment();
                }
                titleRes = R.string.title_section4;
                break;
        }
       // setTitle(titleRes);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fr,tag).commit();

    }
}
