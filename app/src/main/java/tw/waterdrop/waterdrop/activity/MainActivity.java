package tw.waterdrop.waterdrop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;

import tw.waterdrop.lib.Service;
import tw.waterdrop.waterdrop.R;
import tw.waterdrop.waterdrop.fragment.AlbumFragment;
import tw.waterdrop.waterdrop.fragment.BasicTestFragment;
import tw.waterdrop.waterdrop.fragment.BlogFragment;
import tw.waterdrop.waterdrop.fragment.BlogTabFragment;
import tw.waterdrop.waterdrop.fragment.MapFragment;
import tw.waterdrop.waterdrop.fragment.NavigationDrawerFragment;
import tw.waterdrop.waterdrop.fragment.UploadPicFragment;
import tw.waterdrop.waterdrop.service.CheckArticleService;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    public static Fragment fragment;

    public static File cacheDirectory;
    private final String TAG = "MainActivity";
    private final String ARTICLE_SERVICE_NAME = "tw.waterdrop.waterdrop.service.CheckArticleService";
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.v(TAG, getClass().getEnclosingMethod().getName().toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }


    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doViews();
        doControllers();

    }

    private void doViews() {
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    private void doControllers() {
        if (Service.isServiceRunning(getBaseContext(), ARTICLE_SERVICE_NAME) == false) {
            Log.d(TAG, "ARTICLE_SERVICE START");
            startService(new Intent(this, CheckArticleService.class));
        }
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        // Intent intent = new Intent(this,MenuActivity.class);
        // intent.putExtra(Constants.Extra.FRAGMENT_INDEX,position);
        //startActivity(intent);


        String tag = "";
        switch (position) {
            case 0:
                tag = MapFragment.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new MapFragment();
                }


                break;

            case 1:
                tag = BlogFragment.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new BlogFragment();
                }
                break;
            case 2:
                tag = AlbumFragment.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new AlbumFragment();
                }
                break;
            case 3:
                tag = UploadPicFragment.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new UploadPicFragment();
                }
                break;
            case 4:
                tag = BasicTestFragment.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new BasicTestFragment();
                }
                break;
            case 5:
                tag = BlogTabFragment.class.getSimpleName();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment == null) {
                    fragment = new BlogTabFragment();
                }
                break;

        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment, tag).commit();

        onSectionAttached(position);

        /*
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
                */
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.title_section0);
                break;
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;

            case 4:
                mTitle = getString(R.string.title_section4);
                break;

            case 5:
                mTitle = getString(R.string.title_section5);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
