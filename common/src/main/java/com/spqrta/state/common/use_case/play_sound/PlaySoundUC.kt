package com.spqrta.state.common.use_case.play_sound

import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.util.noActions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

@Suppress("OPT_IN_USAGE")
class PlaySoundUC(
    private val mediaPlayers: MediaPlayers,
) {
    fun flow(sound: Sound): Flow<List<AppAction>> {
        return {
            val mediaPlayer = mediaPlayers.getForSound(sound)
            mediaPlayer.start()
        }.asFlow().noActions()
    }
}
