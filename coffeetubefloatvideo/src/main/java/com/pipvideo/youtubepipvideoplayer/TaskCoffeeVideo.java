package com.pipvideo.youtubepipvideoplayer;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class TaskCoffeeVideo {
    private AppCompatActivity activity;
    private TaskCoffeeVideo coffeeVideo;
    private static TaskCoffeeVideo instance;


    public TaskCoffeeVideo() {
    }

    public TaskCoffeeVideo(AppCompatActivity activity) {
        this.activity = activity;
        coffeeVideo = this;
    }

    public static TaskCoffeeVideo getInstance(AppCompatActivity activity) {
        if (instance == null) {
            instance = new TaskCoffeeVideo(activity);
        }
        return instance;
    }

    public TaskCoffeeVideo setVideoStartSecond(float videoStartSecond) {
        this.videoStartSecond = videoStartSecond;
        if (youTubePlayer != null) {
            youTubePlayer.seekTo(videoStartSecond);
        }
        return coffeeVideo;
    }

    private float videoStartSecond = 0f;
    private String youtubeVideoId = "nPLV7lGbmT4";
    private int screenHeight;
    private int screenWidht;
    private PopupWindow popupWindow;
    private View popupView;
    private String TAG = "Basicodemine";
    private YouTubePlayerView playerView;
    private int popupWidht;
    private int popupHeight;
    private FrameLayout draggablePanel;
    private ImageView playPauseButton;
    private ProgressBar progressBar;
    private YouTubePlayer youTubePlayer = null;
    private SeekBar seekBar;
    private boolean isSetupNeeded = true;

    public enum FLOAT_MOVE {
        STICKY, FREE
    }

    public static FLOAT_MOVE FloatMode = FLOAT_MOVE.STICKY;

    public FLOAT_MOVE getFloatMode() {
        return FloatMode;
    }

    public TaskCoffeeVideo setFloatMode(FLOAT_MOVE floatMode) {
        FloatMode = floatMode;
        return coffeeVideo;
    }


    @SuppressLint("ClickableViewAccessibility")
    public TaskCoffeeVideo coffeeVideoSetup(String youtubeVideoId) {
        if (isSetupNeeded) {
            setVideoScale(SCALE.W16H9);
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
            screenWidht = size.x;
            setPopupWidht(getScreenWidht() - 250);
            setPopupHeight(((getScreenWidht() - 250) / 16) * 9);
            LayoutInflater layoutInflater = (LayoutInflater) activity.getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            popupView = layoutInflater.inflate(R.layout.youtube_pip_layout, null);
            setDefaultPopupWindow();
            setUpViews(popupView);
            draggablePanel.setOnTouchListener(new View.OnTouchListener() {
                int orgX, orgY;
                int offsetX, offsetY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            orgX = (int) event.getX();
                            orgY = (int) event.getY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            offsetX = (int) event.getRawX() - orgX;
                            offsetY = (int) event.getRawY() - orgY;
                            popupWindow.update(offsetX, offsetY, -1, -1, true);
                            break;
                        case MotionEvent.ACTION_UP:
                            if (getFloatMode() == FLOAT_MOVE.STICKY)
                                repositionScript(popupWindow, offsetX, offsetY);
                            break;
                    }
                    return true;
                }
            });
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    isSetupNeeded = true;
                }
            });
            isSetupNeeded = false;
        }
        setPlayerView(youtubeVideoId);
        return coffeeVideo;
    }


    private void repositionScript(final PopupWindow popupWindow, final int defX, final int defY) {
        TypedValue tv = new TypedValue();
        int actionBarHeight;
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
        } else {
            actionBarHeight = 150;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        int mDuration = 300;
        animator.setDuration(mDuration);
        final int endX = (getScreenWidht() - getPopupWidht()) / 2;
        final int endY = (defY < getScreenHeight() / 2) ? (actionBarHeight) : (getScreenHeight() - (actionBarHeight + getPopupHeight()));
        animator.addUpdateListener(animation -> popupWindow.update((int) (defX + (endX - defX) * (float) animation.getAnimatedValue()), (int) (defY + (endY - defY) * (float) animation.getAnimatedValue()), -1, -1, true));
        animator.start();
    }

    public void show(View targetView) {
        popupWindow.showAtLocation(targetView, Gravity.NO_GRAVITY, (getScreenWidht() - getPopupWidht()) / 2, 100);
    }

    private AbstractYouTubePlayerListener playerListener;



    public void show(int x, int y, int gravity) {
        popupWindow.showAtLocation(null, gravity, x, y);
    }

    private void setPlayerView(String youtubeVideoId) {
        activity.getLifecycle().addObserver(playerView);
        playerView.getPlayerUIController().showUI(false);

        playerView.initialize(initializedYouTubePlayer -> initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady() {
                super.onReady();
                youTubePlayer = initializedYouTubePlayer;
                initializedYouTubePlayer.loadVideo(youtubeVideoId, videoStartSecond);
                initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady() {
                        super.onReady();
                    }

                    @Override
                    public void onCurrentSecond(float second) {
                        super.onCurrentSecond(second);
//                        Log.d(TAG, "video second " + second);
                        seekBar.setProgress((int) second);
                    }

                    @Override
                    public void onVideoDuration(float duration) {
                        super.onVideoDuration(duration);
//                        Log.d(TAG, "video duration " + duration);
                        seekBar.setProgress(0);
                        seekBar.setMax((int) duration);
                    }

                    @Override
                    public void onStateChange(@NonNull PlayerConstants.PlayerState state) {
                        super.onStateChange(state);
                        playerView.setTag(state.name());
                        switch (state) {
                            case PLAYING:
                                Log.d(TAG, "video state " + state.name());
                                progressBar.setVisibility(View.GONE);
                                playPauseButton.setVisibility(View.VISIBLE);
                                break;
                            case UNKNOWN:
                                Log.d(TAG, "video state " + state.name());
                                break;
                            case BUFFERING:
                                progressBar.setVisibility(View.VISIBLE);
                                playPauseButton.setVisibility(View.GONE);
                                Log.d(TAG, "video state " + state.name());
                                break;
                            case VIDEO_CUED:
                                Log.d(TAG, "video state " + state.name());
                                break;
                            case ENDED:
                                Log.d(TAG, "video state " + state.name());
                                break;
                            case PAUSED:
                                Log.d(TAG, "video state " + state.name());
                                break;

                        }
                    }
                });
            }
        }), true);
    }


    private void setDefaultPopupWindow() {
        popupWindow = new PopupWindow(activity.getBaseContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(20);
        }
        popupWindow.setContentView(popupView);
        popupWindow.setWidth(getPopupWidht());
        popupWindow.setHeight(getPopupHeight());
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setClippingEnabled(false);
        popupWindow.setAnimationStyle(R.style.Animation);
    }

    private void setUpViews(View v) {
        playerView = v.findViewById(R.id.youtube_player);
        draggablePanel = v.findViewById(R.id.draggablePanel);
        playPauseButton = v.findViewById(R.id.ytb_play_pause_button);
        progressBar = v.findViewById(R.id.ytb_progressbar);
        seekBar = v.findViewById(R.id.ytb_seek_bar);
        setupUIListeners();
    }

    private void setupUIListeners() {
        playPauseButton.setOnClickListener(v -> {
            if (playerView.getTag() != null && playerView.getTag().equals(PlayerConstants.PlayerState.PLAYING.name())) {
                if (youTubePlayer != null) {
                    youTubePlayer.pause();
                    playerView.setTag(PlayerConstants.PlayerState.PAUSED.name());
                    playPauseButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.mediaplay));
                }
            } else if (playerView.getTag() != null && playerView.getTag().equals(PlayerConstants.PlayerState.PAUSED.name())) {
                if (youTubePlayer != null) {
                    youTubePlayer.play();
                    playerView.setTag(PlayerConstants.PlayerState.PLAYING.name());
                    playPauseButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.pausemedia));
                }
            }
        });
    }

    public int getPopupWidht() {
        return popupWidht;
    }

    public void setPopupWidht(int popupWidht) {
        this.popupWidht = popupWidht;
    }

    public int getPopupHeight() {
        return popupHeight;
    }

    public void setPopupHeight(int popupHeight) {
        this.popupHeight = popupHeight;
    }

    private enum SCALE {
        W4H3, W16H9
    }

    private SCALE videoScale;

    public SCALE getVideoScale() {
        return videoScale;
    }

    public void setVideoScale(SCALE videoScale) {
        this.videoScale = videoScale;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public int getScreenWidht() {
        return screenWidht;
    }

    public void setScreenWidht(int screenWidht) {
        this.screenWidht = screenWidht;
    }
}
