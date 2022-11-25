package com.healthcare.dogtraining.ui.TRAININGCOURSE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.google.android.exoplayer2.source.LoopingMediaSource;
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

import pl.droidsonroids.gif.GifImageView;

public class VideoVIewActivity extends AppCompatActivity {
    String chapter_name,class_name,subject_name,video_url,videoURL;
    TextView tv_header_comment;
    // VideoView video_view;
    ImageView iv_back;
    GifImageView gifImageView;
    boolean flag=false;
    PlayerView playerView;
    ProgressBar progressBar;
    ImageView btFullScreen;
    SimpleExoPlayer simpleExoPlayer;
  //  String videoURL = "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4";
    //String videoURL = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_v_iew);
        tv_header_comment = findViewById(R.id.tv_header_comment);
        iv_back = findViewById(R.id.iv_back);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        //  gifImageView=findViewById(R.id.gif);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (getIntent() != null) {

            chapter_name = getIntent().getStringExtra("chapter");
            subject_name = getIntent().getStringExtra("subject");
            class_name = getIntent().getStringExtra("class");
            video_url = getIntent().getStringExtra("video_url");
            tv_header_comment.setText(subject_name + " " + class_name + " - " + chapter_name);
            //System.out.println("sizeofarray"+getLevelTypeModel);
        }
        if (getIntent() != null) {
            video_url = getIntent().getStringExtra("video_url");
            chapter_name=getIntent().getStringExtra("chapter_name");
            tv_header_comment.setText(chapter_name);
            System.out.println("<><===videourl"+video_url);
        }

        playerView=findViewById(R.id.player_view);
        progressBar=findViewById(R.id.progress_bar);
        btFullScreen=playerView.findViewById(R.id.bt_fullscreen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Uri videourl=Uri.parse(video_url);
        LoadControl loadControl=new DefaultLoadControl();
        BandwidthMeter bandwidthMeter=new DefaultBandwidthMeter();




        TrackSelector trackSelector=new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));


        simpleExoPlayer= ExoPlayerFactory.newSimpleInstance(VideoVIewActivity.this,trackSelector,loadControl);
        final DefaultHttpDataSourceFactory factory= new DefaultHttpDataSourceFactory("exoplayer_video");
        MediaSource mediaSource = new ExtractorMediaSource.Factory(factory).setExtractorsFactory(new DefaultExtractorsFactory()).createMediaSource(Uri.parse(String.valueOf(videourl)));
        LoopingMediaSource loopingSource = new LoopingMediaSource(mediaSource);

        simpleExoPlayer.prepare(loopingSource);

        playerView.setPlayer(simpleExoPlayer);
        playerView.setKeepScreenOn(true);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.seekTo(0);
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.setRepeatMode(simpleExoPlayer.REPEAT_MODE_ONE);



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
                    simpleExoPlayer.seekTo(0);
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
                System.out.println("<><><======"+error);



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

    private boolean isLandScape(){
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
                .getDefaultDisplay();
        int rotation = display.getRotation();

        if (rotation == Surface.ROTATION_90
                || rotation == Surface.ROTATION_270) {
            return true;
        }

        return false;
    }


}



