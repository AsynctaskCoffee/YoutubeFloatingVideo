package com.pipvideo.youtubepipdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.utils.YouTubePlayerTracker;
import com.pipvideo.youtubepipvideoplayer.FlyingVideo;
import com.pipvideo.youtubepipvideoplayer.TaskCoffeeVideo;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    LinearLayout videoContainer;
    ArrayList<DummyContents> dummyContents = new ArrayList<>();
    ScrollView scrollView;
    YouTubePlayerTracker mTracker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        FlyingVideo.get(MainActivity.this).close();

    }


    public void bottomVideoStart(View view, String videoId) {
        FlyingVideo.get(MainActivity.this)
                .setFloatMode(TaskCoffeeVideo.FLOAT_MOVE.FREE)
                .setFullScreenToggleEnabled(true, "-YOUR-YOUTUBE-API-KEY-")
                .setVideoStartSecond((mTracker == null) ? 0 : mTracker.getCurrentSecond())
                .coffeeVideoSetup(videoId)
                .setFlyGravity(TaskCoffeeVideo.FLY_GRAVITY.BOTTOM)
                .show(view);
    }

    public void topVideoStart(View view, String videoId) {
        FlyingVideo.get(MainActivity.this)
                .setFloatMode(TaskCoffeeVideo.FLOAT_MOVE.STICKY)
                .setVideoStartSecond((mTracker == null) ? 0 : mTracker.getCurrentSecond())
                .coffeeVideoSetup(videoId)
                .setFlyGravity(TaskCoffeeVideo.FLY_GRAVITY.TOP)
                .show(view);
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
        ImageView dummyImgUser = v.findViewById(R.id.dummy_img_user);
        ImageView startFloatTopGravity = v.findViewById(R.id.start_float_top_gravity);
        ImageView startFloatBottomGravity = v.findViewById(R.id.start_float_bottom_gravity);
        TextView dummyUserName = v.findViewById(R.id.dummy_txt_username);
        TextView dummyDate = v.findViewById(R.id.dummy_txt_date);
        TextView dummyContentText = v.findViewById(R.id.dummy_txt_content);
        YouTubePlayerView dummyYoutubePlayer = v.findViewById(R.id.dummy_youtube_view);
        dummyImgUser.setImageResource(dummyContent.getPfImgId());
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
                                FlyingVideo.get(MainActivity.this).close();
                                mTracker = new YouTubePlayerTracker();
                                initializedYouTubePlayer.addListener(mTracker);
                                break;
                        }
                    }
                });
            }
        }), true);

        startFloatTopGravity.setOnClickListener(v1 -> {
            topVideoStart(v1, dummyContent.getVideoId());
        });
        startFloatBottomGravity.setOnClickListener(v12 -> {
            bottomVideoStart(v12, dummyContent.getVideoId());
        });

        v.setLayoutParams(params);
        return v;
    }

    class DummyContents {

        private int pfImgId;
        private String userName;
        private String description;
        private String date;
        private String videoId;
        private String likeCount;
        private String commentCount;


        public DummyContents(int pfImgId, String userName, String description, String date, String videoId, String likeCount, String commentCount) {
            this.pfImgId = pfImgId;
            this.userName = userName;
            this.description = description;
            this.date = date;
            this.videoId = videoId;
            this.likeCount = likeCount;
            this.commentCount = commentCount;
        }

        public int getPfImgId() {
            return pfImgId;
        }

        public void setPfImgId(int pfImgId) {
            this.pfImgId = pfImgId;
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
        dummyContents.add(new DummyContents(R.drawable.annenmay, "AnnenMayKantereit Official", getString(R.string.content_5), "11:25 Thursday", "tERRFWuYG48", "", ""));
        dummyContents.add(new DummyContents(R.drawable.jarryjones, "Jerry Jones", getString(R.string.content_6), "09:00 Friday", "zg79C7XM1Xs", "", ""));
        dummyContents.add(new DummyContents(R.drawable.quaresma, "Ricardo Quaresma", getString(R.string.content_4), "16:30 Monday", "WgtXl2n9iUc", "", ""));
        dummyContents.add(new DummyContents(R.drawable.elonmusk, "Elon Musk", getString(R.string.content_2), "18:00 Wednesday", "sX1Y2JMK6g8", "", ""));
        dummyContents.add(new DummyContents(R.drawable.lolpng, "Egemen ÖZOGUL", getString(R.string.content_1), "15:04 Monday", "uR7RNP59lsE", "", ""));
        dummyContents.add(new DummyContents(R.drawable.seydasevgen, "Seyda SEVGEN", getString(R.string.content_3), "00:10 Sunday", "94gahPBPIqk", "", ""));
    }
}
