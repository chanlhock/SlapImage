package com.example.slapimage.tictactoe.content.game

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.slapimage.R
import com.example.slapimage.tictactoe.game.FirstPlayerPolicy
import com.example.slapimage.tictactoe.model.Player
import com.example.slapimage.tictactoe.ui.ShapePreview
import com.example.slapimage.tictactoe.ui.composables.PersianText
import com.example.slapimage.tictactoe.ui.composables.isFontScaleNormal
import com.example.slapimage.tictactoe.ui.toShape

@Composable
internal fun PlayerCards(
    firstPlayerPolicy: FirstPlayerPolicy,
    players: List<Player>,
    currentPlayer: Player?
) {
    val firstPlayer = players[0]
    val secondPlayer = players[1]

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PlayerCard(
            modifier = Modifier.weight(1f),
            player = firstPlayer,
            firstPlayerPolicy = firstPlayerPolicy,
            isCurrentPlayer = firstPlayer == currentPlayer
        )
        PlayerCard(
            modifier = Modifier.weight(1f),
            player = secondPlayer,
            firstPlayerPolicy = firstPlayerPolicy,
            isCurrentPlayer = secondPlayer == currentPlayer
        )
    }
}

@Composable
internal fun PlayerCard(
    modifier: Modifier = Modifier,
    player: Player,
    firstPlayerPolicy: FirstPlayerPolicy,
    isCurrentPlayer: Boolean = true
) {
    val alpha = if (isCurrentPlayer) ContentAlpha.high else ContentAlpha.disabled

    OutlinedCard(
        modifier = modifier.alpha(alpha)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (firstPlayerPolicy == FirstPlayerPolicy.DiceRolling && isFontScaleNormal()) {
                AnimatedContent(
                    targetState = player.diceIndex,
                    label = "",
                    content = { PlayerDice(diceIndex = it) },
                    transitionSpec = {
                        (slideInVertically { it } + fadeIn())
                            .togetherWith(slideOutVertically { -it } + fadeOut())
                    }
                )
            }
            player.shape?.toShape()?.let { shape -> ShapePreview(shape, 30.dp) }
            PersianText(
                text = player.name,
                modifier = Modifier.weight(2f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun PlayerDice(
    diceIndex: Int = 1
) {
    val icon = when (diceIndex) {
        1 -> R.drawable.ic_dice_1
        2 -> R.drawable.ic_dice_2
        3 -> R.drawable.ic_dice_3
        4 -> R.drawable.ic_dice_4
        5 -> R.drawable.ic_dice_5
        6 -> R.drawable.ic_dice_6
        else -> R.drawable.ic_dice_1
    }
    Icon(
        painter = painterResource(icon),
        contentDescription = stringResource(R.string.player_turn),
    )
}