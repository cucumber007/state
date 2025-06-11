package com.spqrta.state.common.use_case.play_sound

import android.content.Context
import android.media.MediaPlayer
import com.spqrta.state.common.R

class MediaPlayers(
    private val context: Context
) {
    fun getForSound(sound: Sound): MediaPlayer {
        return when (sound) {
            Sound.Ping -> MediaPlayer.create(context, R.raw.ping)
            Sound.Flute -> MediaPlayer.create(context, R.raw.flute)
        }
    }
}
