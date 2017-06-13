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
import android.widget.Toast;

import java.io.IOException;

import pl.fidano.apps.polishradio.models.Radio;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private static final String TAG = "PlayerService";

    private static final int RADIO_PLAYER_NOTIFICATION_ID = 1;

    private static final String ACTION_PLAY = "pl.fidano.apps.polishradio.action.PLAY";
    private static final String ACTION_STOP = "pl.fidano.apps.polishradio.action.STOP";

    private WifiManager.WifiLock mWifiLock;

    private MediaPlayer mPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "running onCreate...");

        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnErrorListener(this);

        mWifiLock = ((WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "PlayerWiFiLockTag");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Log.d(TAG, "running onStartCommand...");

        Radio radio = (Radio) intent.getSerializableExtra("radio");

        if (intent.getAction().equals(ACTION_PLAY)) {

            mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mWifiLock.acquire();

            try {
                mPlayer.reset(); // when switching between stations, first reset player
                mPlayer.setDataSource(radio.getStreamUrl());
            } catch (IOException e) {
                Log.e(TAG, "Exception when setting stream data source");

                e.printStackTrace();
            }

            mPlayer.prepareAsync(); // prepare async to not block main thread
            showNotification(radio);
        }
        return START_STICKY;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(TAG, "running onPrepared...");

        mPlayer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "running onDestroy...");

        stopForeground(true);

        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.release();
            mPlayer = null;
        }

        if (mWifiLock != null && mWifiLock.isHeld()) {
            mWifiLock.release();
        }

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d(TAG, "running onError...");
        // TODO: Enhance error handling

        switch (what) {
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                showToast("Unknown media error");
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                showToast("Media server died");
                break;
        }


        Log.d(TAG, "extra: " + extra);

        stopForeground(true);
        mPlayer.reset();
        if (mWifiLock != null && mWifiLock.isHeld()) {
            mWifiLock.release();
        }
        return true; // error was handled
    }

    private void showNotification(Radio radio) {
        Intent notificationIntent = new Intent(this, PolishRadio.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setContentTitle(radio.getName())
                .setContentText(radio.getUrl())
                .setSmallIcon(android.R.drawable.ic_btn_speak_now)
                .setContentIntent(pendingIntent)
                .setTicker(radio.getName())
                .build();

        startForeground(RADIO_PLAYER_NOTIFICATION_ID, notification);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
