package com.spqrta.state.common.use_case

import com.spqrta.state.common.AppScope
import com.spqrta.state.common.use_case.play_sound.PlaySoundUC

class UseCases(
    val appScope: AppScope
) {
    val showToastUC = ShowToastUC(appScope)

    val loadDynalistUC = LoadDynalistUC(appScope.dynalistApi)
    val loadStateUC = LoadStateUC(appScope)
    val playNotificationSoundUC = PlayNotificationSoundUC(appScope.appContext)
    val playSoundUC = PlaySoundUC(appScope.mediaPlayers)
    val saveStateUC = SaveStateUC(appScope)
    val sendNotificationUC = SendNotificationUC(appScope.appContext, showToastUC)
    val tickUC = TickUC(appScope)
    val updateStatsUC = UpdateStatsUC()
    val vibrateUC = VibrateUC(appScope.appContext, playNotificationSoundUC)
}
