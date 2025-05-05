package com.example.slapimage.tictactoe.game.logic

import com.example.slapimage.tictactoe.game.ai.GameAi
import com.example.slapimage.tictactoe.game.ai.SimpleGameAi
import com.example.slapimage.tictactoe.model.AiDifficulty
import com.example.slapimage.tictactoe.model.DoozCell
import com.example.slapimage.tictactoe.model.Player
import com.example.slapimage.tictactoe.util.Utility.rotated

class SimpleGameLogic(
    private val gameCells: List<List<DoozCell>>,
    private val gameSize: Int,
    aiDifficulty: AiDifficulty
) : GameLogic() {

    override var winnerCells = listOf<DoozCell>()

    override var ai: GameAi = SimpleGameAi(gameCells, aiDifficulty)

    override var winner: Player? = null

    override fun findWinner(): Player? {
        winner = findRowOrColumnWinner(gameCells)
        if (winner != null) return winner

        winner = findRowOrColumnWinner(gameCells.rotated())
        if (winner != null) return winner

        winner = findDiagonalWinner()
        if (winner != null) return winner

        return null
    }

    override fun isGameDrew(): Boolean {
        winner = findWinner()
        return winner == null && gameCells.all { row -> row.all { it.owner != null } }
    }

    private fun findRowOrColumnWinner(
        gameCells: List<List<DoozCell>>
    ): Player? {
        for (i in gameCells.indices) {
            val row = gameCells[i]
            if (row.isNotEmpty() && row.any { it.owner == null })
                continue
            if (row.isNotEmpty() && row.all { it.owner == row.first().owner }) {
                winnerCells = row
                return row.first().owner
            }
        }
        return null
    }

    private fun findDiagonalWinner(): Player? {
        val firstRow = gameCells.first()

        if (firstRow.first().owner == null && firstRow.last().owner == null)
            return null

        /**
         *  a x x
         *  x a x
         *  x x a
         */
        if (firstRow.first().owner != null) {
            val diagonals = mutableListOf<DoozCell>()
            diagonals.add(firstRow.first())
            for (i in 1 until gameSize) {
                val nextCell = gameCells[i][i]
                if (nextCell.owner != null) diagonals.add(nextCell) else break
                if (nextCell != diagonals.last()) break
            }
            if (diagonals.isNotEmpty() && diagonals.size == gameSize && diagonals.all { it.owner == firstRow.first().owner }) {
                winnerCells = diagonals
                return firstRow.first().owner
            }
        }

        /**
         *  x x a
         *  x a x
         *  a x x
         */
        if (firstRow.last().owner != null) {
            val diagonals = mutableListOf<DoozCell>()
            diagonals.add(firstRow.last())
            var i = 1
            var j = gameSize - 2
            while (j > -1) {
                val nextCell = gameCells[i][j]
                if (nextCell.owner != null) diagonals.add(nextCell) else break
                if (nextCell != diagonals.last()) break
                i++
                j--
            }
            if (diagonals.isNotEmpty() && diagonals.size == gameSize && diagonals.all { it.owner == firstRow.last().owner }) {
                winnerCells = diagonals
                return firstRow.last().owner
            }
        }

        return null
    }
}