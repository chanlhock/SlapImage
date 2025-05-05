package com.example.slapimage.tictactoe.game.logic

import com.example.slapimage.tictactoe.game.ai.GameAi
import com.example.slapimage.tictactoe.model.DoozCell
import com.example.slapimage.tictactoe.model.Player

abstract class GameLogic {

    abstract var winner: Player?

    abstract var winnerCells: List<DoozCell>

    abstract var ai: GameAi

    abstract fun findWinner(): Player?

    abstract fun isGameDrew(): Boolean
}