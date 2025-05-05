package com.example.slapimage.tictactoe.game.ai

import com.example.slapimage.tictactoe.model.AiDifficulty
import com.example.slapimage.tictactoe.model.DoozCell

abstract class GameAi {

    abstract var difficulty: AiDifficulty

    abstract var gameCells: List<List<DoozCell>>

    abstract fun play(): DoozCell
}
