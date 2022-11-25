package com.healthcare.dogtraining.ui.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.healthcare.dogtraining.R;

public class VideoPlayer extends AppCompatActivity {
    PlayerView playerView;
    ProgressBar progressBar;
    ImageView btFullScreen,iv_back;
    SimpleExoPlayer simpleExoPlayer;

    String videoUrl="";


    boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        playerView=findViewById(R.id.player_view);
        iv_back=findViewById(R.id.iv_back);
        progressBar=findViewById(R.id.progress_bar);
        btFullScreen=playerView.findViewById(R.id.bt_fullscreen);


        if(getIntent().hasExtra("URL")){
            videoUrl=getIntent().getStringExtra("URL");
           }


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Uri videourl=Uri.parse(videoUrl);

        LoadControl loadControl=new DefaultLoadControl();


        BandwidthMeter bandwidthMeter=new DefaultBandwidthMeter();
        TrackSelector trackSelector=new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));


        simpleExoPlayer= ExoPlayerFactory.newSimpleInstance(VideoPlayer.this,trackSelector,loadControl);


        final DefaultHttpDataSourceFactory factory= new DefaultHttpDataSourceFactory("exoplayer_video");

        //   ExtractorsFactory extractorsFactory=new DefaultExtractorsFactory();

        //   MediaSource mediaSource=new ExtractorMediaSource(videourl,factory,extractorsFactory,null,null);


        MediaSource mediaSource = new ExtractorMediaSource.Factory(factory).setExtractorsFactory(new DefaultExtractorsFactory()).createMediaSource(Uri.parse(String.valueOf(videourl)));



        playerView.setPlayer(simpleExoPlayer);

        playerView.setKeepScreenOn(true);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                if (playbackState==Player.STATE_BUFFERING)
                {
                    progressBar.setVisibility(View.VISIBLE);






                }else if (playbackState == Player.STATE_READY)
                {
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });



        btFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag){

                    btFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_fullscreen_24));

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



                    flag=false;

                }else {

                    btFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_full_exit));


                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                    flag=true;
                }


            }
        });


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



    @Override
    protected void onPause() {
        super.onPause();
        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.getPlaybackState();
    }


    @Override
    protected void onResume() {
        super.onResume();
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.getPlaybackState();
    }


        @Override
        protected void onStop() {
        super.onStop();
        simpleExoPlayer.release();
    }


}