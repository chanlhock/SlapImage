package com.example.slapimage.solitaire

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp


class SolitaireMainActivity : ComponentActivity(), GameView {
    private var gameState by mutableStateOf(GameModel)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.setBackgroundColor(Color(0xFF32CD32).toArgb())
        GamePresenter.view = this
        GameModel.resetGame()

        setContent {
         //   SolitaireAppTheme { // Add if you have a theme
                SolitaireGameScreen()
         //   }
        }
    }

    @Composable
    private fun SolitaireGameScreen() {
        val currentGameState by remember { derivedStateOf { gameState } }
        val density = LocalDensity.current

        // Calculate card dimensions
        val screenWidth = with(density) {
            LocalContext.current.resources.displayMetrics.widthPixels.toDp()
        }
        val cardWidth = remember { (screenWidth - 8.dp) / 7 }
        val cardHeight = remember { cardWidth * 190 / 140 }

        Column(
            modifier = Modifier
                .padding(start = 4.dp, end = 4.dp, top = 8.dp)
        ) {
            // Top row
            Row {
                DeckViewComposable(
                    modifier = Modifier.size(cardWidth, cardHeight),
                    update = { deckView ->
                        deckView.update()
                        deckView.setOnClickListener {
                            GamePresenter.onDeckTap()
                        }
                    }
                )

                WastePileView(modifier = Modifier.size(cardWidth, cardHeight))

                Spacer(modifier = Modifier.size(cardWidth, 0.dp))

                repeat(4) { i ->
                    FoundationPileView(
                        index = i,
                        modifier = Modifier.size(cardWidth, cardHeight)
                    )
                }
            }

            // Tableau piles
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = cardHeight / 2)
            ) {
                repeat(7) { i ->
                    TableauPileView(
                        index = i,
                        modifier = Modifier.width(cardWidth)
                    )
                }
            }
        }
    }

    override fun update() {
        gameState = GameModel // Triggers recomposition
    }

    override fun gameWon() {
        AlertDialog.Builder(this).apply {
            setTitle("You Won!")
            setMessage("Play again?")
            setPositiveButton("Yes") { _, _ ->
                GameModel.resetGame()
                update()
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }
}