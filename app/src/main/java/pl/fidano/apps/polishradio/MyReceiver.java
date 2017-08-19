package pl.fidano.apps.polishradio;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        String action = intent.getAction();

        switch (action) {
            case BluetoothDevice.ACTION_ACL_CONNECTED:
                Log.d(TAG, "acl connected");
                break;
            case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                Log.d(TAG, "acl disconnected");
                context.stopService(new Intent(context, PlayerService.class));
                break;
        }
    }
}
