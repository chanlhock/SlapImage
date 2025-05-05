package com.example.slapimage.tictactoe.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DoozCell(
    val x: Int,
    val y: Int,
    var owner: Player? = null
) : Parcelable