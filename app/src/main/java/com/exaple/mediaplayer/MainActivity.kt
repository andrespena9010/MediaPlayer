package com.exaple.mediaplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import com.exaple.mediaplayer.ui.theme.MediaPlayerTheme
import com.exaple.mediaplayer.ui.views.MediaPlayer4K
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MediaPlayerTheme {
                val subBunny = MediaItem.SubtitleConfiguration.Builder( File( filesDir, "big_buck_bunny.srt" ).toUri() )
                    .setMimeType(MimeTypes.APPLICATION_SUBRIP)
                    .setLanguage("en")
                    .build()

                MediaPlayer4K(
                    listOf(
                        MediaItem.fromUri( File( filesDir, "Porco Rosso MultiAudio.mp4" ).toUri() ),
                        MediaItem.fromUri( File( filesDir, "v1_3840_2160.mp4" ).toUri() ),
                        MediaItem.Builder()
                            .setUri( File( filesDir, "big_buck_bunny.mp4" ).toUri() )
                            .setSubtitleConfigurations( listOf( subBunny ) )
                            .build(),
                        MediaItem.Builder()
                            .setUri( File( filesDir, "big_buck_bunny_less.mp4" ).toUri() )
                            .setSubtitleConfigurations( listOf( subBunny ) )
                            .build(),
                        MediaItem.fromUri( File( filesDir, "reel.mp4" ).toUri() )
                    )
                )
            }
        }
    }
}