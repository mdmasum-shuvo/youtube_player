package com.appifly.youttube_player

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import com.appifly.youttube_player.ui.theme.Youttube_playerTheme
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class PlayerActivity : ComponentActivity() {
    private lateinit var fullscreenViewContainer: ViewGroup
    private var isFullscreen = false
    private var youTubePlayer: YouTubePlayer? = null
    private lateinit var youTubePlayerView: YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Youttube_playerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AndroidView(factory = { context->
                        View.inflate(context, R.layout.youtube_player,null)
                    }, update = { view ->
                        youTubePlayerView = view.findViewById(R.id.youtube_player_view)

                        fullscreenViewContainer = view.findViewById(R.id.full_screen_view_container)
                        initYouTubePlayerView(youTubePlayerView)

                    })


                }

            }
        }
    }

    private fun initYouTubePlayerView(youTubePlayerView: YouTubePlayerView) {
        // The player will automatically release itself when the activity is destroyed.
        // The player will automatically pause when the activity is stopped
        // If you don't add YouTubePlayerView as a lifecycle observer, you will have to release it manually.
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addFullscreenListener(object : FullscreenListener {
            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                isFullscreen = true

                // the video will continue playing in fullscreenView
                fullscreenViewContainer.visibility = View.VISIBLE
                fullscreenViewContainer.addView(fullscreenView)

                if (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                }
            }

            override fun onExitFullscreen() {
                isFullscreen = false

                // the video will continue playing in the player
                fullscreenViewContainer.visibility = View.GONE
                fullscreenViewContainer.removeAllViews()

                if (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_SENSOR) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
                }
            }
        })

        val youTubePlayerListener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                this@PlayerActivity.youTubePlayer = youTubePlayer
                youTubePlayer.loadOrCueVideo(lifecycle, "NxJpu2XKWJA", 0f)

            }

            override fun onPlaybackRateChange(youTubePlayer: YouTubePlayer, playbackRate: PlayerConstants.PlaybackRate) {
            }
        }

        val iFramePlayerOptions = IFramePlayerOptions.Builder()
            .controls(1)
            .fullscreen(1) // enable full screen button
            .build()

        youTubePlayerView.enableAutomaticInitialization = false
        youTubePlayerView.initialize(youTubePlayerListener, iFramePlayerOptions)
    }

}