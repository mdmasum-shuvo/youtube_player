package com.appifly.youttube_player

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.lifecycle.LifecycleOwner
import com.appifly.youttube_player.ui.theme.Youttube_playerTheme
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Youttube_playerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    YoutubePlayer(this,"NxJpu2XKWJA")
                }

            }
        }
    }

    @Composable
    fun YoutubePlayer(lifecycleOwner: LifecycleOwner, link:String?){
        val context= LocalContext.current
        val view = LayoutInflater.from(context).inflate(R.layout.ui_controller, null, false)

        val fullscreenViewContainer:ViewGroup=view.findViewById(R.id.full_screen_view_container)

        val youTubePlayerView=   remember {
            YouTubePlayerView(context = context).apply {
                lifecycleOwner.lifecycle.addObserver(this)

            }
        }


        AndroidView(
            modifier = Modifier
                .fillMaxWidth(),
            factory = { context ->
                youTubePlayerView
            })
        val listener: YouTubePlayerListener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                // using pre-made custom ui
                youTubePlayer.loadVideo(link?:"", 0f)



            }
        }
        youTubePlayerView.addFullscreenListener(object : FullscreenListener{
            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                // the video will continue playing in fullscreenView
                //   playerUiContainer.visibility = View.GONE
                fullscreenViewContainer.visibility = View.VISIBLE
                fullscreenViewContainer.addView(fullscreenView)
                context.setLandscape()

            }

            override fun onExitFullscreen() {
                fullscreenViewContainer.visibility = View.GONE
                fullscreenViewContainer.removeAllViews()
                // the video will continue playing in the player
                // playerUiContainer.visibility = View.VISIBLE




            }

        })
        val iFramePlayerOptions = IFramePlayerOptions.Builder()
            .controls(1)
            .fullscreen(1) // enable full screen button
            .build()

        youTubePlayerView.enableAutomaticInitialization = false
        youTubePlayerView.initialize(listener, iFramePlayerOptions)
    }

    fun hideSystemUI() {

        //Hides the ugly action bar at the top
        actionBar?.hide()

        //Hide the status bars

        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            window.insetsController?.apply {
                hide(WindowInsets.Type.statusBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Youttube_playerTheme {
        Greeting("Android")
    }
}

@Composable
fun LiveTvScreen(
    videoId: String
) {
    val ctx = LocalContext.current
    AndroidView(factory = {
        val youTubePlayerView = YouTubePlayerView(it)
        youTubePlayerView.addFullscreenListener(object : FullscreenListener{
            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
              //  TODO("Not yet implemented")
            }

            override fun onExitFullscreen() {
               // TODO("Not yet implemented")
            }

        })
        val fragment = youTubePlayerView.addYouTubePlayerListener(
            object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer:
                                     YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    youTubePlayer.loadVideo(videoId, 0f)

                }
            }
        )



        youTubePlayerView
    })

}



