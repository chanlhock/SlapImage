package com.example.slapimage.solitaire

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.slapimage.R

class DeckView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    fun update() {
        val gameModel = GameModel // Access the game model
        val isEmpty = gameModel.deck.cards.isEmpty() // Check if deck is empty

        setImageResource(
            if (isEmpty) {
                R.drawable.empty_pile // Make sure this drawable exists
            } else {
                R.drawable.cardback_green5 // Your deck back image
            }
        )
    }
}

@Composable
fun DeckViewComposable(
    modifier: Modifier = Modifier,
    update: (DeckView) -> Unit = {}
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            DeckView(context).apply {
                update(this) // Initial update
            }
        },
        update = { view ->
            update(view) // Update when recomposed
        }
    )
}