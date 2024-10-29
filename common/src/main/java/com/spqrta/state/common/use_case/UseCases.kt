package com.spqrta.state.common.use_case

import com.spqrta.state.common.AppScope
import com.spqrta.state.common.use_case.dynalist.CreateDemoTaskTreeUC
import com.spqrta.state.common.use_case.dynalist.CreateDynalistDocUC
import com.spqrta.state.common.use_case.dynalist.CreateDynalistNodeUC
import com.spqrta.state.common.use_case.dynalist.GetDynalistDocsUC
import com.spqrta.state.common.use_case.dynalist.InitDynalistDocUC
import com.spqrta.state.common.use_case.dynalist.LoadDynalistUC
import com.spqrta.state.common.use_case.foreground_service.StartFgsUC
import com.spqrta.state.common.use_case.foreground_service.StopFgsUC
import com.spqrta.state.common.use_case.play_sound.PlaySoundUC

class UseCases(
    val appScope: AppScope
) {
    val showToastUC = ShowToastUC(appScope)
    val createDynalistNodeUC = CreateDynalistNodeUC(appScope.dynalistApi)

    val createDemoTaskTreeUC = CreateDemoTaskTreeUC(createDynalistNodeUC)
    val createDynalistDocUC = CreateDynalistDocUC(appScope.dynalistApi)
    val getDynalistDocsUC = GetDynalistDocsUC(appScope.dynalistApi)
    val initDynalistDocUC = InitDynalistDocUC(
        dynalistApi = appScope.dynalistApi,
        createDemoTaskTreeUC = createDemoTaskTreeUC,
        createDynalistDocUC = createDynalistDocUC,
        createDynalistNodeUC = createDynalistNodeUC,
    )
    val loadDynalistUC = LoadDynalistUC(appScope.dynalistApi)
    val loadStateUC = LoadStateUC(appScope)
    val openUrlUC = OpenUrlUC(appScope.appContext)
    val playNotificationSoundUC = PlayNotificationSoundUC(appScope.appContext)
    val playSoundUC = PlaySoundUC(appScope.mediaPlayers)
    val saveStateUC = SaveStateUC(appScope)
    val sendNotificationUC = SendNotificationUC(appScope.appContext, showToastUC)
    val startFgsUC = StartFgsUC(appScope.appContext)
    val stopFgsUC = StopFgsUC(appScope.appContext)
    val tickUC = TickUC(appScope)
    val updateStatsUC = UpdateStatsUC()
    val vibrateUC = VibrateUC(appScope.appContext, playNotificationSoundUC)
}
