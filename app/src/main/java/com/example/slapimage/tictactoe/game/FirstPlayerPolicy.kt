package com.example.slapimage.tictactoe.game

import android.os.Parcelable
import androidx.annotation.StringRes
import com.example.slapimage.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class FirstPlayerPolicy(
    @StringRes val persianNameStringResource: Int
) : Parcelable {
    DiceRolling(R.string.dice_rolling_start), HumanFirst(R.string.human_first_start)
}