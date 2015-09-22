package tw.waterdrop.waterdrop.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tw.waterdrop.waterdrop.R;


/**
 * Created by colon on 2014/7/23.
 */
public class About extends Fragment {

    public static final String CAT_COLOR = "cat_color";

    private String color = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        color = this.getArguments().getString(CAT_COLOR);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater, container);
    }

    private View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.about, container, false);

        TextView txtCat = (TextView) view.findViewById(R.id.txtApple);
        String colorCat = color + " " + txtCat.getText().toString();
        txtCat.setText(colorCat);

        return view;
    }

}