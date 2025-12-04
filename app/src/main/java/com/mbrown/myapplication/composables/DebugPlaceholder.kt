package com.mbrown.myapplication.composables

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import com.mbrown.myapplication.R

enum class DebugPlaceholder(@param:DrawableRes val imageRes: Int) {
    CHARACTER(R.drawable.character_placeholder)
}

@Composable
fun debugPlaceholder(debugPlaceholder: DebugPlaceholder) =
    if (LocalInspectionMode.current) {
        painterResource(debugPlaceholder.imageRes)
    } else {
        null
    }