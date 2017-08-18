package pl.fidano.apps.polishradio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.function.Consumer;

public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String message = intent.getStringExtra("state");
        Bundle b = intent.getExtras();
        b.keySet().forEach((k) -> Log.d(TAG, intent.getStringExtra(k.toString())));
    }
}
