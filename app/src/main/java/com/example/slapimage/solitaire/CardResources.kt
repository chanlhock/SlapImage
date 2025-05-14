package com.example.slapimage.solitaire

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.slapimage.R

// Card drawable constants
val cardBackDrawable = R.drawable.cardback_green5
val wastePileDrawable = R.drawable.cardback_blue1

@Composable
fun getResourceForCard(card: Card): Int {
    val resName = "card${card.suit}${cardsMap[card.value]}".lowercase()
    return LocalContext.current.resources.getIdentifier(
        resName,
        "drawable",
        LocalContext.current.packageName
    )
}

