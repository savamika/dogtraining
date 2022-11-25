package com.healthcare.dogtraining.ui.YOUTUBE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;

import com.google.android.exoplayer2.ui.PlayerView;
import com.healthcare.dogtraining.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;


public class YoutubevideoviewActivity extends AppCompatActivity {
    // Replace video id with your video Id
    private String YOUTUBE_VIDEO_ID = "uZnWUZW1hQo";
    private String BASE_URL = "https://www.youtube.com";
    // private String mYoutubeLink = "https://www.youtube.com/watch?v=KA-DryAZanM&ab_channel=one_little_cat";
    // private String mYoutubeLink = BASE_URL + "/watch?v=" + YOUTUBE_VIDEO_ID;
    String mYoutubeLink;
    PlayerView mPlayerView;
    private YouTubePlayerView youTubePlayerView;
    String videoId ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtubevideoview);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        mPlayerView = findViewById(R.id.mPlayerView);
        youTubePlayerView = findViewById(R.id.activity_main_youtubePlayerView);
        if (getIntent() != null) {
            mYoutubeLink = getIntent().getStringExtra("mYoutubeLink");
            Log.e("mYoutubeLink", "onCreate: " + mYoutubeLink);
            System.out.print("<><>====viewyoutube" + mYoutubeLink);
            if (mYoutubeLink.contains("embed")) {

                String[] splitString = mYoutubeLink.split("/");
                Log.e("videoid", "onCreate: " + splitString[4]);
                videoId = splitString[4];

                getLifecycle().addObserver(youTubePlayerView);
                //  extractYoutubeUrl();

                youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {

                        youTubePlayer.loadVideo(videoId, 0);
                    }
                });

            }else{
                Log.e("notcontain", "onCreate: " );
                youTubePlayerView.setVisibility(View.GONE);
                mPlayerView.setVisibility(View.VISIBLE);
                extractYoutubeUrl();
            }
        }


    }

    private void extractYoutubeUrl() {
        @SuppressLint("StaticFieldLeak") YouTubeExtractor mExtractor = new YouTubeExtractor(this) {
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> sparseArray, VideoMeta videoMeta) {

                System.out.print("<><==videometabahar" + videoMeta);
                System.out.print("<><ytfiles" + sparseArray);

                if (sparseArray != null) {

                    int itag = 22;
                    String streamUrl = sparseArray.get(itag).getUrl();
                    playVideo(streamUrl);
                    System.out.print("<><==videometa" + videoMeta);


                }
            }
        };
        mExtractor.extract(mYoutubeLink, true, true);
    }

    private void playVideo(String streamUrl) {
        System.out.print("<><><downloadurl" + streamUrl);
        PlayerView mPlayerView = findViewById(R.id.mPlayerView);
        mPlayerView.setPlayer(ExoPlayerManager.getSharedInstance(YoutubevideoviewActivity.this).getPlayerView().getPlayer());
        ExoPlayerManager.getSharedInstance(YoutubevideoviewActivity.this).playStream(streamUrl);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ExoPlayerManager.getSharedInstance(YoutubevideoviewActivity.this).destroyPlayer();

        System.out.println("<><======ondestroy");


    }

    @Override
    protected void onStop() {
        super.onStop();
        ExoPlayerManager.getSharedInstance(YoutubevideoviewActivity.this).destroyPlayer();
        System.out.println("<><======onstop");
    }


}

