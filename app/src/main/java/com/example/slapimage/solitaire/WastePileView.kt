package com.example.slapimage.solitaire

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.zIndex
import androidx.compose.ui.unit.dp

@Composable
fun WastePileView(modifier: Modifier = Modifier) {
    // Make wastePile observable for Compose
    val wastePile = remember { mutableStateListOf<Card>().apply { addAll(GameModel.wastePile) } }
    val cardWidth = cardWidth()

    // Calculate overlap in pixels first, then convert back to dp
    val overlapOffset = with(LocalDensity.current) {
        remember { (cardWidth.toPx() * 0.3f).toDp() }
    }

    Box(
        modifier = modifier
            .clickable { GamePresenter.onWastePileTap() } // Moved click here
    ) {
        if (wastePile.isEmpty()) {
            // Show empty waste pile background
            Image(
                painter = painterResource(id = wastePileDrawable),
                contentDescription = "Empty Waste Pile",
                modifier = Modifier.size(width = cardWidth, height = cardHeight())
            )
        } else {
            // Show last 3 cards with overlap
            wastePile.takeLast(3).forEachIndexed { i, card ->
                Box(
                    modifier = Modifier
                        .zIndex(i.toFloat())
                        .offset(x = overlapOffset * i * 0.5f)
                ) {
                    Image(
                        painter = painterResource(id = getResourceForCard(card)),
                        contentDescription = "Waste Card $i",
                        modifier = Modifier.size(width = cardWidth, height = cardHeight())
                    )
                }
            }
        }
    }
}