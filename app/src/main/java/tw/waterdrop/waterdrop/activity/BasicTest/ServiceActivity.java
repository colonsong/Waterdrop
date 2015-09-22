package tw.waterdrop.waterdrop.activity.BasicTest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import tw.waterdrop.lib.Service;
import tw.waterdrop.waterdrop.R;
import tw.waterdrop.waterdrop.service.BasicTestService;
import tw.waterdrop.waterdrop.service.CheckArticleService;

/**
 * Created by colon on 2015/3/22.
 */
public class ServiceActivity extends Activity {

    private Button startServiceBtn,stopServiceBtn,bindServiceBtn,unBindServiceBtn,callServiceBtn,checkServiceBtn,startIntentServiceBtn,stopIntentServiceBtn;
    private BasicTestService basicTestService;
    public static final String TAG = "ServiceActivity";
    private ServiceConnection serviceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG,"onServiceConnected()" + name.getClassName())   ;
            Toast.makeText(ServiceActivity.this, "onServiceConnected", Toast.LENGTH_LONG).show();
            basicTestService = ((BasicTestService.LocalBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG,"onServiceDisConnected()" + name.getClassName())   ;
            Toast.makeText(ServiceActivity.this, "onServiceDisConnected", Toast.LENGTH_LONG).show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "Service Activity onCreate", Toast.LENGTH_LONG).show();

        setContentView(R.layout.basic_test_service);
        startServiceBtn = (Button) findViewById(R.id.startServiceBtn);
        stopServiceBtn = (Button) findViewById(R.id.stopServiceBtn);
        bindServiceBtn = (Button) findViewById(R.id.bindServiceBtn);
        unBindServiceBtn = (Button) findViewById(R.id.unBindServiceBtn);
        callServiceBtn = (Button) findViewById(R.id.callServiceFunBtn);
        checkServiceBtn = (Button) findViewById(R.id.checkServiceLog);
        startIntentServiceBtn = (Button) findViewById(R.id.startIntentServiceBtn);
        stopIntentServiceBtn = (Button) findViewById(R.id.stopIntentServiceBtn);

        startServiceBtn.setOnClickListener(startServiceClick);
        stopServiceBtn.setOnClickListener(stopServiceClick);
        bindServiceBtn.setOnClickListener(bindServiceClick);
        unBindServiceBtn.setOnClickListener(unBindServiceClick);
        callServiceBtn.setOnClickListener(callServiceClick);
        checkServiceBtn.setOnClickListener(checkServiceLog);
        startIntentServiceBtn.setOnClickListener(startIntentServiceClick);
        stopIntentServiceBtn.setOnClickListener(stopIntentServiceClick);
    }
    private View.OnClickListener startServiceClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            basicTestService = null;
            Intent it = new Intent(ServiceActivity.this,BasicTestService.class);
            startService(it);
        }
    };

    private View.OnClickListener stopServiceClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            basicTestService = null;
            Intent it = new Intent(ServiceActivity.this,BasicTestService.class);
            stopService(it);


        }
    };
    private View.OnClickListener startIntentServiceClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            basicTestService = null;
            Intent it = new Intent(ServiceActivity.this,CheckArticleService.class);
            startService(it);
        }
    };

    private View.OnClickListener stopIntentServiceClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            basicTestService = null;
            Intent it = new Intent(ServiceActivity.this,CheckArticleService.class);
            stopService(it);

        }
    };
    private View.OnClickListener bindServiceClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            basicTestService = null;
            Intent it = new Intent(ServiceActivity.this,BasicTestService.class);
            bindService(it,serviceConn,BIND_AUTO_CREATE);

        }
    };
    private View.OnClickListener unBindServiceClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            basicTestService = null;
            unbindService(serviceConn);
        }
    };

    private View.OnClickListener checkServiceLog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Service.isServiceRunning(getApplication(),"tw.waterdrop.waterdrop.service.CheckArticleService");
        }
    };







    private View.OnClickListener callServiceClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           if(basicTestService != null)
           {
               Log.v(TAG,"callServiceClick");
               basicTestService.myMethod();
           }


        }
    };



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
