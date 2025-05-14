package com.example.slapimage.solitaire

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun FoundationPileView(
    index: Int,
    modifier: Modifier = Modifier
) {
    val foundationPile = GameModel.foundationPiles[index]
    val topCard = foundationPile.cards.lastOrNull() // Safe access

    Image(
        painter = painterResource(
            id = topCard?.let { getResourceForCard(it) } ?: wastePileDrawable
        ),
        contentDescription = "Foundation ${foundationPile.suit}",
        modifier = modifier
            .clickable { GamePresenter.onFoundationPileTap(index) }
            .size(width = cardWidth(), height = cardHeight())
    )
}