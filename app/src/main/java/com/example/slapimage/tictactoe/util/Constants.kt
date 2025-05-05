package com.example.slapimage.tictactoe.util

import com.example.slapimage.tictactoe.model.AiDifficulty

object Constants {
    const val firstPlayerPolicy = "firstPlayerPolicy"
    const val theme = "theme"

    const val gameSize = "gameSize"

    const val gamePlayersType = "gamePlayersType"
    const val firstPlayerName = "firstPlayerName"

    const val secondPlayerName = "secondPlayerName"
    const val firstPlayerShape = "firstPlayerShape"

    const val secondPlayerShape = "secondPlayerShape"
    const val aiDifficulty = "aiDifficulty"

    const val isSoundOn = "isSoundOn"
    const val isVibrationOn = "isVibrationOn"

    object Shapes {
        const val ringShape = "ringShape"
        const val circleShape = "circleShape"
        const val xShape = "xShape"
        const val triangleShape = "triangleShape"
        const val rectangleShape = "rectangleShape"
    }

    val difficulties = listOf(AiDifficulty.Easy, AiDifficulty.Medium, AiDifficulty.Hard)

    val aiPlayDelayRange = 350L..750L

    val diceRange = 1..6

    val PERSIAN_REGEX = Regex("[\\u0621-\\u064a]+")
}