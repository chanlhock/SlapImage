package com.example.slapimage.solitaire
object GamePresenter {
    var view: GameView? = null

    fun onDeckTap() {
        GameModel.onDeckTap()
        updateView()
    }

    fun onWastePileTap() {
        GameModel.onWastePileTap()
        updateView()
    }

    fun onFoundationPileTap(foundationIndex: Int) {
        GameModel.onFoundationPileTap(foundationIndex)
        updateView()
    }

    fun onTableauPileTap(tableauIndex: Int, cardIndex: Int) {
        GameModel.onTableauTap(tableauIndex, cardIndex)
        updateView()
    }

    private fun updateView() {
        view?.update()
    }

    fun gameWon() {
        view?.gameWon()
    }
}