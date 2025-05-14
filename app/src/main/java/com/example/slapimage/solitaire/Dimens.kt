package com.example.slapimage.solitaire

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun cardWidth(): Dp {
    val context = LocalContext.current
    return with(LocalDensity.current) {
        ((context.resources.displayMetrics.widthPixels - 32.dp.toPx()) / 7).toDp()
    }
}

@Composable
fun cardHeight(): Dp {
    return cardWidth() * 190 / 140
}