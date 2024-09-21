package com.spqrta.state.ui.view.gtd2.meta

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.spqrta.state.common.logic.features.gtd2.Gtd2State
import com.spqrta.state.common.logic.features.gtd2.meta.MetaState
import com.spqrta.state.ui.theme.FontSize
import kotlinx.serialization.json.Json

val json = Json {
    prettyPrint = true
}

@Composable
fun MetaView(gtd2State: Gtd2State) {
    Text(
        text = json.encodeToString(MetaState.serializer(), gtd2State.metaState),
        fontSize = FontSize.TITLE
    )
}
