package letsdecode.com.popularmovies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/*
A separate class for handling all the broadcast events, so that more than one activity can extend it and use it
 */
public class BaseActivity extends AppCompatActivity {

    //// Reference to BroadcastReceiver
    private BroadcastReceiver mReceiver;
//    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    /**
     * This method is used to get the network information.
     */
    protected NetworkInfo getNetworkInfo() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager
                .getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Subscribe to call back as user might can the content
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onNetworkChanged();
            }
        };
        registerReceiver(mReceiver, filter);
    }

    /**
     * This method is called when there is any change in network.
     */
    protected void onNetworkChanged() {
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Un Subscribe to call back
        unregisterReceiver(mReceiver);
//        Log.d(TAG, "onPause Called base");

    }


}
