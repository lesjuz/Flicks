package com.lesjuz.movy.Activity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.lesjuz.movy.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lesjuz on 3/11/2017.
 */

public class QuickPlayActivity extends YouTubeBaseActivity {

    private static final String  SELECTED_MOVIE_TRAILER_URL = "SELECTED_MOVIE_TRAILER_URL";
    private static final String  API_KEY = "AIzaSyC5NWG3OG3SeDlFOcpqeo2iSWCQqxeD_Fo" ;

    @BindView(R.id.player)
    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_play);
        ButterKnife.bind(this);

        final String trailerSource = getIntent().getStringExtra(SELECTED_MOVIE_TRAILER_URL);

        youTubePlayerView.initialize(API_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        final YouTubePlayer youTubePlayer, boolean b) {

                        // do any work here to cue video, play video, etc.
                        youTubePlayer.cueVideo(trailerSource);
                        youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                            @Override
                            public void onLoading() {
                                // required implementation
                            }

                            @Override
                            public void onLoaded(String s) {
                                youTubePlayer.play();
                            }

                            @Override
                            public void onAdStarted() {
                                // required implementation
                            }

                            @Override
                            public void onVideoStarted() {
                                // required implementation
                            }

                            @Override
                            public void onVideoEnded() {
                                // required implementation
                            }

                            @Override
                            public void onError(YouTubePlayer.ErrorReason errorReason) {
                                Toast.makeText(getApplicationContext(), "Video Playback Failed", Toast.LENGTH_SHORT);
                            }
                        });
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {
                        Toast.makeText(getApplicationContext(), "Video Player Failed to Initialize", Toast.LENGTH_SHORT);
                    }
                });
    }
}
