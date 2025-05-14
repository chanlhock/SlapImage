package com.example.slapimage.solitaire

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun TableauPileView(
    index: Int,
    modifier: Modifier = Modifier
) {
    val tableauPile = GameModel.tableauPiles[index]
    val cards = tableauPile.cards

    Box(modifier = modifier) {
        cards.forEachIndexed { i, card ->
            if (i < cards.size) { // Additional safety check
                Box(
                    modifier = Modifier
                        .zIndex(i.toFloat())
                        .offset(y = (i * 25).dp)
                ) {
                    Image(
                        painter = painterResource(
                            id = if (card.faceUp) getResourceForCard(card) else cardBackDrawable
                        ),
                        contentDescription = "Tableau Card $i",
                        modifier = Modifier
                            .size(width = cardWidth(), height = cardHeight())
                            .clickable {
                                if (card.faceUp && i < cards.size) { // Check before tap
                                    GamePresenter.onTableauPileTap(index, i)
                                }
                            }
                    )
                }
            }
        }
    }
}