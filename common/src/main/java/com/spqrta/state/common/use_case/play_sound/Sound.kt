package com.spqrta.state.common.use_case.play_sound

sealed class Sound {
    object Ping : Sound()
    object Flute : Sound()
}
