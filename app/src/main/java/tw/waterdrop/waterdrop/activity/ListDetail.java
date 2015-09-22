package tw.waterdrop.waterdrop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import tw.waterdrop.waterdrop.R;

public class ListDetail extends Activity {
    private final String TAG = "ListDetail";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.list_detail);
        Bundle bundle = this.getIntent().getExtras();
        final String title = bundle.getString("title");
        final String description = bundle.getString("content");
        TextView titleView = (TextView) findViewById(R.id.v_d_title);
        titleView.setText(title);

        WebView descriptionWebView = (WebView) findViewById(R.id.v_d_description);
        // descriptionWebView.loadData(description, "text/html", "utf-8");

        WebSettings ws = descriptionWebView.getSettings();
       // ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);


        descriptionWebView.loadDataWithBaseURL(null, description, "text/html",
                "utf-8", null);

        Button shareBtn = (Button) findViewById(R.id.share_button);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                shareTo(title,description,"Share");

            }
        });


    }

    public  void shareTo(String subject, String body, String chooserTitle) {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);


        startActivity(Intent.createChooser(sharingIntent, chooserTitle));
    }


}
