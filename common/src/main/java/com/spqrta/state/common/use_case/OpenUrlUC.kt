package com.spqrta.state.common.use_case

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.spqrta.state.common.logic.action.AppAction
import com.spqrta.state.common.util.noActions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

@Suppress("OPT_IN_USAGE")
class OpenUrlUC(
    private val context: Context
) {
    fun flow(url: String): Flow<List<AppAction>> {
        return { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }.asFlow()
            .noActions()

    }
}
