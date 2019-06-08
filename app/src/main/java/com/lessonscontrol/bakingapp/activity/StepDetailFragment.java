package com.lessonscontrol.bakingapp.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.lessonscontrol.bakingapp.R;
import com.lessonscontrol.bakingapp.data.Step;

/**
 * A fragment representing a single Step detail screen.
 * on handsets.
 */
public class StepDetailFragment extends Fragment {

    private Step step;

    private PlayerView playerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StepDetailFragment() {
        //Empty
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(Step.PARCELABLE_KEY)) {
            step = getArguments().getParcelable(Step.PARCELABLE_KEY);
        }
    }

    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);

        if (step != null) {
            ((TextView) rootView.findViewById(R.id.step_title)).setText(step.getShortDescription());
            ((TextView) rootView.findViewById(R.id.step_instructions)).setText(step.getDescription());

            Context context = getContext();

            TextView noVideoProvidedLabel = rootView.findViewById(R.id.label_no_video_provided);
            playerView = rootView.findViewById(R.id.video_player);

            if (!TextUtils.isEmpty(step.getVideoURL()) && context != null) {
                noVideoProvidedLabel.setVisibility(View.GONE);
                setupVideoPlayer(rootView, context);
            } else {
                playerView.setVisibility(View.GONE);
                noVideoProvidedLabel.setVisibility(View.VISIBLE);
            }
        }

        return rootView;
    }

    private void setupVideoPlayer(View rootView, Context context) {
        String appName = getString(R.string.app_name);
        DataSource.Factory dataSourceFactory =
                new DefaultHttpDataSourceFactory(Util.getUserAgent(context, appName));
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(step.getVideoURL()));
        ExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(context);
        exoPlayer.prepare(mediaSource);
        exoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onPlayerError(ExoPlaybackException error) {
                rootView.findViewById(R.id.label_error_loading_video).setVisibility(View.VISIBLE);
                playerView.setVisibility(View.GONE);
            }
        });
        playerView.setPlayer(exoPlayer);
    }

    @Override
    public void onDetach() {
        if (playerView != null && playerView.getPlayer() != null) {
            playerView.getPlayer().stop();
            playerView.getPlayer().release();
        }
        super.onDetach();
    }
}
