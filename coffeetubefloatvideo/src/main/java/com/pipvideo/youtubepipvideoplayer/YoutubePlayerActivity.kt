package com.pipvideo.youtubepipvideoplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_youtube_player.*


class YoutubePlayerActivity : YouTubeBaseActivity() {

    lateinit var videoId: String
    var startSecond: Float = 0.0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_youtube_player)
        if (intent.hasExtra("startSecond")) {
            try {
                startSecond = intent.extras!!.getFloat("startSecond")
            } catch (e: Exception) {
                startSecond = 0.0f
                e.printStackTrace()
            }
        }
        videoId = intent.extras!!.getString("videoId").toString()
        ytbPlayer.initialize(
                TaskCoffeeVideo.apiKey,
                object : YouTubePlayer.OnInitializedListener {
                    override fun onInitializationSuccess(
                            p0: YouTubePlayer.Provider?,
                            p1: YouTubePlayer,
                            p2: Boolean
                    ) {
                        try {
                            p1.loadVideo(videoId, startSecond.toInt() * 1000)
                        } catch (e: Exception) {
                        }
                        p1.play()
                        p1.setPlayerStateChangeListener(playerStateChangeListener)
                    }

                    override fun onInitializationFailure(
                            p0: YouTubePlayer.Provider?,
                            p1: YouTubeInitializationResult?
                    ) {
                        openYouTubeIntent()
                    }
                })

    }

    private val playerStateChangeListener: YouTubePlayer.PlayerStateChangeListener =
            object : YouTubePlayer.PlayerStateChangeListener {
                override fun onAdStarted() {

                }

                override fun onLoading() {

                }

                override fun onVideoStarted() {
                }

                override fun onLoaded(p0: String?) {
                }

                override fun onVideoEnded() {
                }

                override fun onError(p0: YouTubePlayer.ErrorReason?) {
                    try {
                        openYouTubeIntent()
                    } catch (e: Exception) {
                    }
                }

            }

    fun openYouTubeIntent() {
        try {
            val webIntent = Intent(Intent.ACTION_VIEW)
            webIntent.data = Uri.parse("https://www.youtube.com/watch?v=$videoId")
            startActivity(webIntent)
            finish()
        } catch (ex: ActivityNotFoundException) {

        }
    }


    override fun onBackPressed() {
        overridePendingTransition(0, 0)
        finish()
    }
}