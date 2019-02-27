package com.pipvideo.youtubepipdemo;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.utils.YouTubePlayerTracker;
import com.pipvideo.youtubepipvideoplayer.FlyingVideo;
import com.pipvideo.youtubepipvideoplayer.TaskCoffeeVideo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    LinearLayout videoContainer;
    ArrayList<DummyContents> dummyContents = new ArrayList<>();
    YouTubePlayerView selectedView = null;
    ScrollView scrollView;
    YouTubePlayerTracker mTracker = null;
    DummyContents selectedDummyContent = null;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        scrollView.setOnTouchListener((v, event) -> {
            checkVisible(selectedView);
            return false;
        });
    }

    private void checkVisible(YouTubePlayerView selectedView) {
        Rect scrollBounds = new Rect();
        scrollView.getHitRect(scrollBounds);
        if (selectedView != null)
            if (selectedView.getLocalVisibleRect(scrollBounds)) {
                Log.d("visible", "ok");
            } else {
                Log.d("visible", "no"+"  videoID: "+selectedDummyContent.getUserName());
                float videoSecond = mTracker.getCurrentSecond();

                FlyingVideo.get(MainActivity.this)
                        .setFloatMode(TaskCoffeeVideo.FLOAT_MOVE.STICKY)
                        .setVideoStartSecond(videoSecond)
                        .coffeeVideoSetup(selectedDummyContent.getVideoId())
                        .show(selectedView);

                ((YouTubePlayer) selectedView.getTag()).pause();
            }
    }


    void setupViews() {
        fillDummys();
        scrollView = findViewById(R.id.scroll_view);
        videoContainer = findViewById(R.id.video_container);
        for (int i = 0; i < dummyContents.size(); i++)
            videoContainer.addView(socialRow(dummyContents.get(i)));
    }


    public View socialRow(DummyContents dummyContent) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 15);
        View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.dummy_social_view, null);
        TextView dummyUserName = v.findViewById(R.id.dummy_txt_username);
        TextView dummyDate = v.findViewById(R.id.dummy_txt_date);
        TextView dummyContentText = v.findViewById(R.id.dummy_txt_content);
        YouTubePlayerView dummyYoutubePlayer = v.findViewById(R.id.dummy_youtube_view);
        dummyContentText.setText(dummyContent.getDescription());
        dummyUserName.setText(dummyContent.getUserName());
        dummyDate.setText(dummyContent.getDate());
        dummyYoutubePlayer.initialize(initializedYouTubePlayer -> initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady() {
                super.onReady();
                Log.d("video", dummyContent.getVideoId());
                initializedYouTubePlayer.cueVideo(dummyContent.getVideoId(), 0.0f);
                initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onStateChange(@NonNull PlayerConstants.PlayerState state) {
                        super.onStateChange(state);
                        switch (state) {
                            case PLAYING:
                                selectedView = null;
                                selectedDummyContent = null;
                                mTracker = new YouTubePlayerTracker();
                                initializedYouTubePlayer.addListener(mTracker);
                                selectedView = dummyYoutubePlayer;
                                selectedView.setTag(initializedYouTubePlayer);
                                selectedDummyContent = dummyContent;
                                break;
                            case ENDED:
                            case PAUSED:
                                selectedView = null;
                                break;
                        }
                    }
                });
            }
        }), true);
        v.setLayoutParams(params);
        return v;
    }


    class DummyContents {

        private String userName;
        private String description;
        private String date;
        private String videoId;
        private String likeCount;
        private String commentCount;


        public DummyContents(String userName, String description, String date, String videoId, String likeCount, String commentCount) {
            this.userName = userName;
            this.description = description;
            this.date = date;
            this.videoId = videoId;
            this.likeCount = likeCount;
            this.commentCount = commentCount;
        }


        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        public String getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(String likeCount) {
            this.likeCount = likeCount;
        }

        public String getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(String commentCount) {
            this.commentCount = commentCount;
        }
    }

    void fillDummys() {
        dummyContents.add(new DummyContents("AnnenMayKantereit Official", getString(R.string.content_5), "11:25 Thursday", "tERRFWuYG48", "", ""));
        dummyContents.add(new DummyContents("Jerry Jones", getString(R.string.content_6), "09:00 Friday", "zg79C7XM1Xs", "", ""));
        dummyContents.add(new DummyContents("Ricardo Quaresma", getString(R.string.content_4), "16:30 Monday", "WgtXl2n9iUc", "", ""));
        dummyContents.add(new DummyContents("Elon Musk", getString(R.string.content_2), "18:00 Wednesday", "sX1Y2JMK6g8", "", ""));
        dummyContents.add(new DummyContents("Egemen ÖZOGUL", getString(R.string.content_1), "15:04 Monday", "uR7RNP59lsE", "", ""));
        dummyContents.add(new DummyContents("Seyda SEVGEN", getString(R.string.content_3), "00:10 Sunday", "94gahPBPIqk", "", ""));
    }
}
