package com.spqrta.state.use_case

import com.spqrta.state.AppScope

class UseCases(
    private val appScope: AppScope
) {
    val saveStateUC = SaveStateUC(appScope)
    val loadStateUC = LoadStateUC(appScope)
    val tickUC = TickUC(appScope)
    val playNotificationSoundUC = PlayNotificationSoundUC(appScope.appContext)
}
