package com.spqrta.state.common.use_case

import com.spqrta.state.common.AppScope

class UseCases(
    appScope: AppScope
) {
    val loadDynalistUC = LoadDynalistUC(appScope.dynalistApi)
    val loadStateUC = LoadStateUC(appScope)
    val playNotificationSoundUC = PlayNotificationSoundUC(appScope.appContext)
    val saveStateUC = SaveStateUC(appScope)
    val showToastUC = ShowToastUC(appScope)
    val tickUC = TickUC(appScope)
    val updateStatsUC = UpdateStatsUC()
    val vibrateUC = VibrateUC(appScope.appContext, playNotificationSoundUC)
}
