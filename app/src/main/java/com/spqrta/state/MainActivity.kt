@file:OptIn(ExperimentalUnitApi::class, ExperimentalMaterialApi::class)

package com.spqrta.state

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.TextUnitType.Companion.Sp
import com.spqrta.state.app.App
import com.spqrta.state.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppView()
        }
    }
}

@Preview
@Composable
fun AppView() {
    val state = App.state.collectAsState().value

    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                Modifier
                    .padding(
                        top = Dp(32f),
                        bottom = Dp(16f),
                        start = Dp(16f),
                        end = Dp(16f),
                    )
            ) {
                Text(
                    text = "State",
                    Modifier.align(Alignment.CenterHorizontally),
                    fontSize = TextUnit(20f, TextUnitType.Sp),
                    fontWeight = FontWeight.Bold
                )
                StateView()
                ControlsView()
            }
        }
    }
}

@Preview
@Composable
fun StateView() {
    Text(
        text = "State",
    )
}

@Preview
@Composable
fun ControlsView() {
    Column {
        Button(
            onClick = {
            },
            Modifier
                .padding(
                    top = Dp(16f),
                    bottom = Dp(16f)
                )
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Regenerate")
        }
    }
}

//@Composable
//fun ColumnScope.CardView(card: Card) {
//    return Surface(
//        onClick = { App.view.onCardClicked(card) },
//        modifier = Modifier
//            .padding(bottom = Dp(18f))
//            .fillMaxWidth()
//            .align(Alignment.CenterHorizontally)
//    ) {
//        Text(
//            text = if (card.hidden) "Press to reveal" else card.text,
//            fontSize = TextUnit(20f, Sp),
//            textAlign = TextAlign.Center,
//            color = if (card.hidden) Color.DarkGray else Color.Black,
//            fontWeight = if (card.hidden) {
//                FontWeight.Light
//            } else if (card.bold) {
//                FontWeight.Bold
//            } else {
//                FontWeight.Normal
//            }
//        )
//    }
//}
