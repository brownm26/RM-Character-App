package com.mbrown.myapplication.composables

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.mbrown.myapplication.ui.theme.RMCharacterTheme

@Composable
fun ThemedPreview(
    content: @Composable () -> Unit
) {
    RMCharacterTheme {
        Surface {
            content()
        }
    }
}