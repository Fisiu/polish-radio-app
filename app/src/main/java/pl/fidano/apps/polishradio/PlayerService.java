package pl.fidano.apps.polishradio;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private static final String TAG = "PlayerService";

    private static final int ONGOING_NOTIFICATION_ID = 1;

    private static final String ACTION_PLAY = "pl.fidano.apps.polishradio.action.PLAY";
    private static final String ACTION_STOP = "pl.fidano.apps.polishradio.action.STOP";

    private WifiManager.WifiLock mWifiLock;
    private PowerManager.WakeLock wakeLock;

    private MediaPlayer mPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "running onCreate...");


        mWifiLock = ((WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "PlayerWiFiLockTag");

//        wakeLock = ((PowerManager) getApplicationContext().getSystemService(POWER_SERVICE))
//                .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PlayerWakeLockTag");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);

        Log.d(TAG, "running onStartCommand...");

        if(intent.getAction().equals(ACTION_PLAY)) {
            mPlayer = new MediaPlayer();

            mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
//            playRadio();

            mWifiLock.acquire();
//            wakeLock.acquire();

            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            try {
                mPlayer.setDataSource(intent.getStringExtra("url"));
                mPlayer.setOnPreparedListener(this);
                mPlayer.setOnErrorListener(this);
                mPlayer.prepareAsync(); // prepare async to not block main thread
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    private void playRadio() {
        Intent notificationIntent = new Intent(this, PolishRadio.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Polish Radio")
                .setContentText("Nazwa stacji")
                .setSmallIcon(R.drawable.ic_menu_camera)
                .setContentIntent(pendingIntent)
                .setTicker("Ticker text")
                .build();

        startForeground(ONGOING_NOTIFICATION_ID, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "running onDestroy...");

        if (mPlayer != null) {
            if(mPlayer.isPlaying()) {mPlayer.stop();}

            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;
        }

        if(wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }

        if (mWifiLock != null && mWifiLock.isHeld()) {
            mWifiLock.release();
        }

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(TAG, "running onPrepared...");

        mPlayer.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d(TAG, "running onError...");
        // return false;
        // TODO: Enhance error handling
        mp.reset();
        wakeLock.release();
        return true; // error was handled
    }
}
