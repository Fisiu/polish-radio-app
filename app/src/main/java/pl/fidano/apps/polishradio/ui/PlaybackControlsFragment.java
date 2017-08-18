package pl.fidano.apps.polishradio.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import pl.fidano.apps.polishradio.PlayerService;
import pl.fidano.apps.polishradio.R;

public class PlaybackControlsFragment extends Fragment {

    private static final String TAG = "PlaybackControlsFragmen";
    private final View.OnClickListener mButtonListener = v -> {
        // TODO: implement play stop feature
        Log.d(TAG, "PLAY/PAUSE");

        getActivity().stopService(new Intent(getActivity(), PlayerService.class));
    };
    private ImageButton mPlayPause;
    private TextView mTitle;
    private TextView mSubtitle;
    private TextView mExtraInfo;
    private ImageView mAlbumArt;
    private String mArtUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playback_controls, container, false);

        mPlayPause = (ImageButton) rootView.findViewById(R.id.play_pause);
        mPlayPause.setEnabled(true);
        mPlayPause.setOnClickListener(mButtonListener);

        mTitle = (TextView) rootView.findViewById(R.id.title);
        mSubtitle = (TextView) rootView.findViewById(R.id.artist);
        mExtraInfo = (TextView) rootView.findViewById(R.id.extra_info);
        mAlbumArt = (ImageView) rootView.findViewById(R.id.album_art);

        rootView.setOnClickListener(v -> Log.d(TAG, "rootView clicked!"));

        return rootView;
    }
}
