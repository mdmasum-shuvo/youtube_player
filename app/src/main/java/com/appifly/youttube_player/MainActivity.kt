package com.appifly.youttube_player

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.appifly.youttube_player.ui.theme.Youttube_playerTheme
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
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
                    LiveTvScreen("wmandyRExzA")
                }
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
        youTubePlayerView.enableAutomaticInitialization=false

        val view = LayoutInflater.from(ctx).inflate(R.layout.ui_controller, null, false)
        youTubePlayerView.setCustomPlayerUi(view)
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
    })}

@Composable
fun YoutubePlayer(lifecycleOwner: LifecycleOwner, link:String?){
    val context= LocalContext.current
    val youTubePlayerView=   YouTubePlayerView(context = context).apply {
        lifecycleOwner.lifecycle.addObserver(this)
        addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {

            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(link?:"", 0f)
            }
        })
    }

    youTubePlayerView.enableAutomaticInitialization=false
    AndroidView(
        modifier = Modifier
            .fillMaxWidth(),
        factory = { context ->

            youTubePlayerView
        })
    val listener: YouTubePlayerListener = object : AbstractYouTubePlayerListener() {
        override fun onReady(youTubePlayer: YouTubePlayer) {
            // using pre-made custom ui



        }
    }
    val options: IFramePlayerOptions = IFramePlayerOptions.Builder().controls(0).build()
    youTubePlayerView.initialize(listener, options)
}
