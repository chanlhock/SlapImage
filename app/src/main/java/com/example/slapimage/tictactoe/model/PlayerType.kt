package com.example.slapimage.tictactoe.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class PlayerType : Parcelable {
    Human, Computer
}
