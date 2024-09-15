package com.spqrta.state.ui.view.dynalist

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.spqrta.state.common.logic.action.DynalistAction
import com.spqrta.state.common.logic.features.dynalist.DynalistLoadingState
import com.spqrta.state.common.logic.features.dynalist.DynalistState
import com.spqrta.state.common.logic.features.gtd2.logic.toElement
import com.spqrta.state.ui.theme.FontSize
import com.spqrta.state.ui.view.common.controls.TextActionButton
import com.spqrta.state.ui.view.gtd2.element.ElementView

@Composable
fun DynalistView(state: DynalistState) {
    Column {
        Text(
            text = "Dynalist",
            fontSize = FontSize.TITLE,
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
        when (state) {
            is DynalistState.KeyNotSet -> {
                var key by rememberSaveable { mutableStateOf("") }
                Column {
                    Text("Please set the API key")
                    TextActionButton(
                        text = "Get API key",
                        action = DynalistAction.OpenGetApiKeyPage
                    )
                    TextField(
                        value = key,
                        onValueChange = {
                            key = it
                        },
                        label = { Text("API key") }
                    )
                    TextActionButton(
                        text = "Set API key",
                        action = DynalistAction.SetApiKey(key),
                    )
                }

            }

            is DynalistState.DocsLoading -> {
                Text(text = "Loading docs")
            }

            is DynalistState.CreatingDoc -> {
                Text("Creating doc")
            }

            is DynalistState.DocCreated -> {
                when (val loadingState = state.loadingState) {
                    is DynalistLoadingState.Initial -> {
                        Text("Initial")
                    }

                    is DynalistLoadingState.Loaded -> {
                        loadingState.nodes.forEach {
                            ElementView(element = it.toElement())
                        }
                    }
                }
            }
        }
    }
}
