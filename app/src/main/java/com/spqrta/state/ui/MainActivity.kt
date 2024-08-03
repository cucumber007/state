package com.spqrta.state.ui

import android.os.Bundle
import android.view.Surface
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.spqrta.state.common.logic.App
import com.spqrta.state.common.logic.action.OnResumeAction
import com.spqrta.state.ui.view.main.AppView


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val orientation = when (val rotation = windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> Portrait
            Surface.ROTATION_90 -> Landscape
            Surface.ROTATION_180 -> Portrait
            Surface.ROTATION_270 -> Portrait
            else -> throw IllegalArgumentException("Unknown rotation $rotation")
        }
        setContent {
            AppView(orientation)
        }
    }

    override fun onResume() {
        super.onResume()
        App.handleAction(OnResumeAction())
    }
}




