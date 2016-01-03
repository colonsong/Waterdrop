package tw.waterdrop.waterdrop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import tw.waterdrop.waterdrop.R;
import tw.waterdrop.waterdrop.activity.BasicTest.BasicRecycleViewActivity;
import tw.waterdrop.waterdrop.activity.BasicTest.ServiceActivity;
import tw.waterdrop.waterdrop.activity.BasicTest.ThreadActivity;

/**
 * step new Activity
 * add in sampleArray
 * add class in android Manifext.xml
 */

public class BasicTestFragment extends Fragment {
    public static final int INDEX =0;
    //private static final = const
    private static final SampleTest[] samplesArray = new SampleTest[]{
            new SampleTest(R.string.test1,ServiceActivity.class),
            new SampleTest(R.string.test2,ThreadActivity.class),
            new SampleTest(R.string.BasicRecycleView,BasicRecycleViewActivity.class),
    };

    private static class SampleTest{

        final int title;
        final Class targetClass;

        SampleTest(int title,Class targetClass)
        {
            this.title = title;
            this.targetClass = targetClass;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.basic_test_view, container, false);

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView listview  = (ListView)  getActivity().findViewById(R.id.basic_view_listView);
        listview.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,getTitlesList()));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), samplesArray[position].targetClass));
            }
        });

    }


    private List<String> getTitlesList(){
        List<String> titles = new ArrayList<String>();
        for(SampleTest config: samplesArray)
        {
            titles.add(getString(config.title));
        }
        return titles;
    }
}