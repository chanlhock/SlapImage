package com.example.slapimage.tictactoe.model

import android.os.Parcelable
import androidx.annotation.StringRes
import com.example.slapimage.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class AiDifficulty(
    @StringRes val persianNameStringResource: Int
) : Parcelable {
    Easy(R.string.easy), Medium(R.string.medium), Hard(R.string.hard)
}
