package pl.fidano.apps.polishradio.ui;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;

import pl.fidano.apps.polishradio.PlayerService;
import pl.fidano.apps.polishradio.R;
import pl.fidano.apps.polishradio.models.Radio;

public class PlaybackControlsFragment extends Fragment {
    private static final String TAG = "PlaybackControlsFragmen";
    private final View.OnClickListener mButtonListener = v -> {
        // TODO: implement play stop feature
        Log.d(TAG, "PLAY/PAUSE");

        getActivity().stopService(new Intent(getActivity(), PlayerService.class));
    };
    private Radio mRadio;
    private BroadcastReceiver mReceiver;
    private ImageButton mPlayPause;
    private TextView mTitle;
    private ImageView mLogo;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "saving instance");
        if (mRadio != null) {
            outState.putSerializable("radio", mRadio);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "loading instance");
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.getSerializable("radio") != null) {
            mRadio = (Radio) savedInstanceState.getSerializable("radio");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playback_controls, container, false);

        mPlayPause = (ImageButton) rootView.findViewById(R.id.play_pause);
        mPlayPause.setEnabled(true);
        mPlayPause.setOnClickListener(mButtonListener);

        mTitle = (TextView) rootView.findViewById(R.id.title);
        mLogo = (ImageView) rootView.findViewById(R.id.logo);

        rootView.setOnClickListener(v -> Log.d(TAG, "rootView clicked!"));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume...");

        initReceiver();
        if (mRadio != null) {
            setPlayerInfo(mRadio);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause...");

        finishReceiver();
    }

    private void initReceiver() {
        mReceiver = new PlayerReceiver();
        IntentFilter filter = new IntentFilter("playing");

        getActivity().registerReceiver(mReceiver, filter);
    }

    private void finishReceiver() {
        getActivity().unregisterReceiver(mReceiver);
    }

    private void setPlayerInfo(Radio radio) {
        GlideUrl url = new GlideUrl(radio.getLogoUrl());
        Glide.with(getActivity()).load(url).into(mLogo);
        mTitle.setText(radio.getName());
    }

    class PlayerReceiver extends BroadcastReceiver {

        private final static String TAG = "PlayerReciver";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onRecive");
            mRadio = (Radio) intent.getSerializableExtra("radio");
            setPlayerInfo(mRadio);
        }
    }
}
